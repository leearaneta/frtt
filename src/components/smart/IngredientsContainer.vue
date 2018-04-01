<template>
  <el-collapse v-model="defaultExpanded">
    <Ingredient
      v-for="(ingredient, index) in ingredientsList"
      v-bind:name="ingredient.name"
      v-bind:splits="ingredient.splits"
      v-bind:unit="ingredient.unit"
      v-bind:qty="ingredient.qty"
      v-bind:key="index"
    />
  </el-collapse>
</template>

<script>
import { mapGetters } from 'vuex'
import Ingredient from '../dumb/Ingredient'

export default {
  name: 'IngredientsContainer',
  computed: mapGetters(['ingredientsList', 'defaultExpanded']),
  components: { Ingredient }
}
</script>

<style scoped>
</style>
