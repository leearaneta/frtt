<template>
  <div>
    <li>
      <div>{{name}}
        <button
          v-bind:index='index'
          v-on:click='this.handleRemove'
        >remove</button>
      </div>
    </li>
  </div>
</template>

<script>

export default {
  name: 'RecipeName',
  props: ['name', 'index', 'handleRemove']
}
</script>

<style scoped>
</style>
