import pluralize from 'pluralize'

export default {
  methods: {
    formatUnit () {
      let {qty, unit} = this
      if (unit) {
        if (qty && parseInt(qty) !== 1) {
          unit = pluralize(unit)
        }
      }
      return [qty, unit].join(' ')
    }
  }
}
