<template>
  <div id="app">
    <SearchContainer />
    <RecipeNamesContainer />
  </div>
</template>

<script>
import SearchContainer from './components/SearchContainer.vue'
import RecipeNamesContainer from './components/RecipeNamesContainer.vue'

export default {
  name: 'App',
  components: {
    SearchContainer,
    RecipeNamesContainer
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
