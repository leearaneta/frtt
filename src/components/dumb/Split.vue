<template>
  <div>
    {{ display }} ( {{ recipe }} )
  </div>
</template>

<script>
import IngredientMixin from '../mixins/IngredientMixin'

export default {
  name: 'Split',
  props: ['qty', 'unit', 'recipe'],
  mixins: [IngredientMixin],
  computed: {
    display: function () {
      return this.formatUnit()
    }
  }
}
</script>

<style scoped>
</style>
