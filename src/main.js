// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App'
import store from './store'

Vue.config.productionTip = false
Vue.use(Vuex)
Vue.use(ElementUI)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  components: { App },
  render: h => h(App),
  template: '<App/>',
  store
})
