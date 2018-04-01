<template>
  <div>
    <div>{{ error }}</div>
    <el-input
      placeholder="Enter a Recipe URL !!"
      v-model="search"
      clearable
    />
    <el-button type="primary" v-on:click="handleClick">Huzzah</el-button>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'SearchContainer',
  data: function () {
    return { search: '' }
  },
  watch: {
    search: function (a, b) {
      this.search = a
    }
  },
  methods: {
    ...mapActions(['submitURL']),
    handleClick: function (event) {
      this.submitURL(this.search)
      this.search = ''
    }
  },
  computed: mapState({
    error: state => state.shoppingList.error
  })
}
</script>

<style scoped>
</style>
