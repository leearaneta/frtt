import _ from 'lodash'
import Qty from 'js-quantities'
import axios from 'axios'
import Fraction from 'fraction.js'

function qtyAdd (ingredients) {
  return ingredients
    .map(i => i.qty)
    .reduce((a, b) => a.add(new Fraction(b)), new Fraction('0'))
    .valueOf()
    .toString()
}

function unitAddConvert (ingredients) {
  return ingredients
    .map(i => Qty(new Fraction(i.qty).valueOf(), i.unit))
    .reduce((a, b) => a.gt(b) ? a.add(b) : b.add(a))
    .toPrec(0.1)
    .toString()
    .split(' ')
}

function merge (ingredients) {
  let {name, unit: arbitraryUnit} = _.sample(ingredients)
  let splits = ingredients.map(i => _.omit(i, 'name'))
  let bareIngredient = {name, splits, expand: false}

  // if any entries have no quantity, aggregate should have no quantity or unit
  if (!ingredients.map(x => x.qty).reduce((a, b) => a && b)) return bareIngredient
  // if any entries have no unit...
  if (!ingredients.map(x => x.unit).reduce((a, b) => a && b)) {
    return !_.chain(ingredients) // if ALL entries have no unit, add up quantities
      .map('unit') // if at least one entry has unit, just return bareIngredient
      .compact()
      .size()
      .value() ? {...bareIngredient, qty: qtyAdd(ingredients)} : { ...bareIngredient, expand: true }
  }
  // if all units are the same, don't convert
  if (ingredients.map(x => x.unit).reduce((a, b) => a === b)) {
    return { ...bareIngredient, unit: arbitraryUnit, qty: qtyAdd(ingredients) }
  }

  try {
    let [qty, unit] = unitAddConvert(splits)
    return { ...bareIngredient, unit, qty }
  } catch (e) {
    return { ...bareIngredient, expand: true } // maybe set this to active:true
  }
}

const state = {
  recipes: [],
  error: ''
}

const getters = {
  recipeNames: state => state.recipes.map(x => x.name),
  getRecipeByURL: (state) => (url) => state.recipes.find(r => r.url === url),
  ingredientsList: state => {
    return _.chain(state.recipes)
      .flatMap(recipe => { // omg javascript needs for comprehensions
        return recipe.ingredients.map(i => {
          return { ...i, recipe: recipe.name }
        })
      })
      .groupBy('name')
      .values()
      .map(merge)
      .value()
  },
  defaultExpanded: (state, getters) => getters
    .ingredientsList
    .filter(i => i.expand)
    .map(i => i.name)
}

const mutations = {
  pushToRecipes (state, recipe) {
    state.recipes.push(recipe)
  },
  removeRecipe (state, index) {
    state.recipes.splice(index, 1)
  },
  setError (state, error) {
    state.error = error
  }
  // TODO: change amount of servings
}

const actions = {
  submitURL ({getters, commit, state}, url) {
    if (getters.getRecipeByURL(url)) {
      commit('setError', 'that url has already been submitted')
      return
    }
    let myAxios = axios.create({
      baseURL: 'http://localhost:8081',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
    myAxios.post('/parse', {'address': url})
      .then(response => {
        commit('pushToRecipes', { ...response.data, url })
        !!state.error && commit('setError', '')
      })
      .catch(error => {
        console.log(error)
        commit('setError', "sorry, we can't handle that url :(")
      })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
