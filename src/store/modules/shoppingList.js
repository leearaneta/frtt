import _ from 'lodash'
import convert from 'convert-units'
import axios from 'axios'

function merge (ingredients) { // this needs to be way better
  let {name, unit: arbitraryUnit} = _.sample(ingredients)

  // if any entries have no quantity, aggregate should have no quantity
  if (!ingredients.map(x => x.qty).reduce((a, b) => a && b)) return {name}
  // if any entries are unitless, aggregate should have no unit
  if (!ingredients.map(x => x.unit).reduce((a, b) => a && b)) return {name, qty: _.sumBy(ingredients, 'qty')}
  // if all units are the same, don't convert
  if (ingredients.map(x => x.unit).reduce((a, b) => a === b)) return {name, unit: arbitraryUnit, qty: _.sumBy(ingredients, 'qty')}

  let standardizedIngredients = ingredients.map(x => _.assign(x, {
    'unit': arbitraryUnit,
    'qty': convert(x.qty).from(x.unit).to(arbitraryUnit)
  })) // TODO: handle if library can't convert

  let unitSum = _.sumBy(standardizedIngredients, 'qty')
  let [qty, unit] = convert(unitSum).from(arbitraryUnit).toBest().split(' ')
  return {name, unit, qty}
}

const state = {
  validatedRecipes: [],
  error: ''
}

const getters = {
  urls: state => state.validatedRecipes.map(x => x.url),
  ingredientList: state => {
    _.chain(state.validatedRecipes)
      .flatMap('ingredients')
      .groupBy('name')
      .values()
      .map(merge)
      .value()
  }
}

const mutations = {
  pushToValidatedRecipes (state, recipe) {
    state.validatedRecipes.push(recipe)
  },
  removeValidatedRecipe (state, url) {
    let index = state.validatedRecipes.map(x => x.url).indexOf(url)
    state.validatedRecipes.splice(index, 1)
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
        debugger
        let validatedRecipe = {url, ingredients: response.data}
        commit('pushToValidatedRecipes', validatedRecipe)
      })
      .catch(error => {
        commit('setError', error)
      })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
