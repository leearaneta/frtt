<template>
  <li>
    <div>
      <div>{{name}}</div>
      <el-button
        icon="el-icon-delete"
        size="mini"
        v-bind:index='index'
        v-on:click='handleRemove'
      />
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
