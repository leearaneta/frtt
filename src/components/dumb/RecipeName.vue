<template>
  <li style="display: flex; margin-bottom: 10px; justify-content: space-between;">
      <i
        class="el-icon-delete recipe-name"
        size="mini"
        @click='handleRemove'
      />
      <div>{{name}}</div>
      <el-input-number
        class="recipe-name"
        size="mini"
        controls-position="right"
        v-model='qty'
      />
  </li>
</template>

<script>

export default {
  name: 'RecipeName',
  data () {
    return {
      qty: 1
    }
  },
  watch: {
    qty () {
      this.$emit('handleChange', this.qty, this.index)
    }
  },
  props: ['name', 'index'],
  methods: {
    handleRemove () {
      this.$emit('handleRemove', this.index)
    }
  }
}
</script>

<style>

.recipe-name {
  margin: 0 10px 0px 10px;
}

.el-input-number--mini {
  width: 50px;
}

.el-input-number--mini .el-input-number__decrease, .el-input-number--mini .el-input-number__increase {
  width: 15px;
}

.el-input-number.is-controls-right .el-input__inner {
  padding-left: 0px;
  padding-right: 15px;
}

</style>
