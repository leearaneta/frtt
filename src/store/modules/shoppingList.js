import _ from 'lodash'
import convert from 'convert-units'
import axios from 'axios'
import Fraction from 'fraction.js'

function qtyReduce (ingredients) {
  return ingredients
    .map(i => i.qty)
    .reduce((a, b) => a.add(new Fraction(b)), new Fraction('0'))
    .toString()
}

function merge (ingredients) { // wow, what a humungo functo
  let {name, unit: arbitraryUnit} = _.sample(ingredients)
  let splits = ingredients.map(i => _.omit(i, 'name'))
  // if any entries have no quantity, aggregate should have no quantity
  if (!ingredients.map(x => x.qty).reduce((a, b) => a && b)) return {name, splits}
  // if any entries are unitless, aggregate should have no unit
  if (!ingredients.map(x => x.unit).reduce((a, b) => a && b)) return {name, splits, qty: qtyReduce(ingredients)}
  // if all units are the same, don't convert
  if (ingredients.map(x => x.unit).reduce((a, b) => a === b)) return {name, splits, unit: arbitraryUnit, qty: qtyReduce(ingredients)}

  try { // might fail because of conversion errors
    let standardizedIngredients = splits.map(x => {
      return {
        ...x,
        unit: arbitraryUnit,
        qty: convert(x.qty).from(x.unit).to(arbitraryUnit)
      }
    })
    let unitSum = qtyReduce(standardizedIngredients)
    let [qty, unit] = convert(unitSum).from(arbitraryUnit).toBest().split(' ')
    return {name, splits, unit, qty}
  } catch (error) {
    return {name, splits} // maybe set this to active:true
  }
}

const state = {
  recipes: [],
  error: ''
}

const getters = {
  recipeNames: state => state.recipes.map(x => x.name),
  ingredientsList: state => {
    return _.chain(state.recipes)
      .flatMap(recipe => { // omg
        return recipe.ingredients.map(i => {
          return { ...i, recipe: recipe.name }
        })
      })
      .groupBy('name')
      .values()
      .map(merge)
      .value()
  }
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
  submitURL ({commit, state}, url) {
    commit('setError', '')
    let myAxios = axios.create({
      baseURL: 'http://localhost:8081',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
    myAxios.post('/parse', {'name': url})
      .then(response => {
        commit('pushToRecipes', response.data)
      })
      .catch(error => commit('setError', error))
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
