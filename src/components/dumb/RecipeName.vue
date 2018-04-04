<template>
  <li>
    <div>
      <div>{{name}}</div>
      <el-button
        icon="el-icon-delete"
        size="mini"
        @click='handleRemove'
      />
      <el-input-number
        size="mini"
        min=0
        type="number"
        controls-position="right"
        v-model='qty'
      />
    </div>
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
