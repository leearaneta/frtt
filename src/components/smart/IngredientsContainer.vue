<template>
  <el-collapse v-bind:value="defaultExpanded">
    <Ingredient
      v-for="(ingredient, index) in ingredientsList"
      :name="ingredient.name"
      :splits="ingredient.splits"
      :unit="ingredient.unit"
      :qty="ingredient.qty"
      :key="index"
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
