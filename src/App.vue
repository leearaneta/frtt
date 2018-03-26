<template>
  <div id="app">
    <SearchContainer />
    <RecipeNamesContainer />
    <IngredientsContainer />
  </div>
</template>

<script>
import SearchContainer from './components/smart/SearchContainer.vue'
import RecipeNamesContainer from './components/smart/RecipeNamesContainer.vue'
import IngredientsContainer from './components/smart/IngredientsContainer.vue'

export default {
  name: 'App',
  components: {
    SearchContainer,
    RecipeNamesContainer,
    IngredientsContainer
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
