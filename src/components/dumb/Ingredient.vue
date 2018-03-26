<template>
  <el-collapse-item v-bind:title="display" v-bind:name="index">
    <div v-for="split in splits" v-bind:key="split.recipe">
      {{ split.qty }} {{ split.name }} ( {{ split.recipe }} )
    </div>
  </el-collapse-item>
</template>

<script>
function formatDisplay (i) {
  let {qty, unit, name} = i
  if (unit) {
    if (qty && parseInt(qty) !== 1) {
      unit += 's'
    }
    unit += ' of'
  }
  return [qty, unit, name].join(' ')
}

export default {
  name: 'Ingredient',
  props: ['name', 'unit', 'qty', 'index', 'splits'],
  computed: {
    display: function () {
      return formatDisplay(this)
    }
  }
}
</script>

<style scoped>
</style>
