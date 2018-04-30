<template>
  <el-collapse-item :title="display" :name="name">
    <Split
      v-for="(split, index) in splits"
      :recipe="split.recipe"
      :qty="split.qty"
      :unit="split.unit"
      :key="index"
    />
  </el-collapse-item>
</template>

<script>
import Split from './Split'
import IngredientMixin from '../mixins/IngredientMixin'
import pluralize from 'pluralize'

export default {
  name: 'Ingredient',
  props: ['name', 'unit', 'qty', 'index', 'splits'],
  mixins: [IngredientMixin],
  components: {Split},
  methods: {
    formatIngredient () {
      let {qty, unit, name} = this
      if (qty) {
        if (!unit && parseInt(qty) !== 1) {
          name = pluralize(name)
        } else if (unit) {
          name = ' of ' + name
        }
      }
      return name
    }
  },
  computed: {
    display () {
      return this.formatUnit() + this.formatIngredient()
    }
  }
}
</script>

<style scoped>
</style>
