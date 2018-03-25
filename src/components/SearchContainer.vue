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
    url: function (a, b) {
      this.search = a
    }
  },
  methods: {
    ...mapActions(['submitURL']),
    handleClick: function (event) {
      this.$store.dispatch('submitURL', this.search)
      this.search = ''
    }
  }
}
</script>

<style scoped>
</style>
