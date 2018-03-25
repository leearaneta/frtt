<template>
  <div>
    <input v-model="search" placeholder='butts haha'>
    <button v-on:click="handleClick">hey</button>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'RecipeNameContainer',
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
      this.$store.dispatch('submitURL', this.search)
      this.search = ''
    }
  }
}
</script>

<style scoped>
</style>