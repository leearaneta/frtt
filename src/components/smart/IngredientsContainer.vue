<template>
  <ul>
    <Ingredient
      v-for="(ingredient, index) in ingredientsList"
      v-bind:name="ingredient.name"
      v-bind:unit="ingredient.unit"
      v-bind:qty="ingredient.qty"
      v-bind:key="index"
    />
  </ul>
</template>

<script>
import { mapGetters } from 'vuex'
import Ingredient from '../dumb/Ingredient'

export default {
  name: 'IngredientsContainer',
  computed: { ...mapGetters(['ingredientsList']) },
  components: { Ingredient }
}
</script>

<style scoped>
ul {
  list-style-type: none
}
</style>
