<template>
  <ul>
    <RecipeName
      v-for="(name, index) in recipeNames"
      v-bind:name="name"
      :handleRemove="handleRemove"
      v-bind:key="index"
    />
  </ul>
</template>

<script>
import { mapMutations, mapGetters } from 'vuex'
import RecipeName from '../dumb/RecipeName'

export default {
  name: 'RecipeNamesContainer',
  computed: { ...mapGetters(['recipeNames']) },
  methods: {
    ...mapMutations(['removeRecipe']),
    handleRemove: function (event) {
      this.removeRecipe(event.target.index)
    }
  },
  components: { RecipeName }
}
</script>

<style scoped>
ul {
  list-style-type: none
}
</style>
