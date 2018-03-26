<template>
  <li>
    <div>{{qty}} {{unit}} {{name}}</div>
  </li>
</template>

<script>

export default {
  name: 'Ingredient',
  props: ['name', 'unit', 'qty', 'index', 'splits']
}
</script>

<style scoped>
</style>
