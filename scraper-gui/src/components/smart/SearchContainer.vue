<template>
  <div>
    <el-input
      size="small"
      style="width:75%;"
      placeholder="Enter a Recipe URL !!"
      v-model="search"
      clearable
    />
    <el-button type="primary" :loading="loading" size="small" @click="handleClick">Huzzah</el-button>
    <img src="https://thumbs.gfycat.com/DifficultGiftedBison-size_restricted.gif" alt="Sporty Burger" v-if="loading" />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  name: 'SearchContainer',
  data: function () {
    return {
      search: ''
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
    error: state => state.shoppingList.error,
    loading: state => state.shoppingList.loading
  })
}
</script>

<style scoped>

</style>
