<template>
  <li>
    <div>{{name}}
      <button
        v-bind:index='index'
        v-on:click='handleRemove'
      >remove</button>
    </div>
  </li>
</template>

<script>

export default {
  name: 'RecipeName',
  props: ['name', 'index', 'handleRemove']
}
</script>

<style scoped>
</style>
