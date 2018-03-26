<template>
  <div>
    <input v-model="search" placeholder='butts haha'>
    <button v-on:click="handleClick">hey</button>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

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
  }
}
</script>

<style scoped>
</style>
