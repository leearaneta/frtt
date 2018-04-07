<template>
  <ul style="padding-left:0px;">
    <RecipeName
      v-for="(name, index) in recipeNames"
      :name='name'
      :index='index'
      @handleChange='handleChange'
      @handleRemove='handleRemove'
      :key='index'
    />
  </ul>
</template>

<script>
import { mapMutations, mapGetters } from 'vuex'
import RecipeName from '../dumb/RecipeName'

export default {
  name: 'RecipeNamesContainer',
  computed: mapGetters(['recipeNames']),
  methods: {
    ...mapMutations(['removeRecipe', 'changeRecipeQuantity']),
    handleRemove (index) {
      this.removeRecipe({index})
    },
    handleChange (qty, index) {
      this.changeRecipeQuantity({qty, index})
    }
  },
  components: { RecipeName }
}
</script>

<style scoped>
ul {
  list-style-type: none;
}
</style>
