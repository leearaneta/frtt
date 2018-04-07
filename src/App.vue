<template>
  <div id="app">
    <MainHeader />
    <div style="display: flex; justify-content: space-evenly;">
      <el-container style="flex: 0 0 40%">
        <el-header style="height:42px;">
          <SearchContainer />
        </el-header>
        <el-main>
          <RecipeNamesContainer />
        </el-main>
      </el-container>
      <el-container style="border: 1px solid #eee; border-radius: 5%; flex: 0 0 35%">
        <el-main>
          <IngredientsContainer />
        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script>
import SearchContainer from './components/smart/SearchContainer.vue'
import RecipeNamesContainer from './components/smart/RecipeNamesContainer.vue'
import IngredientsContainer from './components/smart/IngredientsContainer.vue'
import MainHeader from './components/dumb/MainHeader.vue'

export default {
  name: 'App',
  components: {
    SearchContainer,
    RecipeNamesContainer,
    IngredientsContainer,
    MainHeader
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

.el-container {
  flex: 0 0 40%;
  padding: 0 25px 0 25px;
  height:500px;
}

</style>
