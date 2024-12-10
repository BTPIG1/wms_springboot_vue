import './assets/global.css'
import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import axios from "axios"
import VueRouter from "vue-router";
//  import router from './router'; ========>           import xxx from "....." as router   这里router只是代替router/index.js中暴露出的对象的一个别名xxx可以是任意
import router from './router';
import store from "@/store";
// Vue.prototype.$xxx 全局常量    使用方法：this.$xxx
Vue.prototype.$axios=axios;
Vue.prototype.$httpUrl="http://localhost:8090"
// Vue.use(XXX) 是否可以理解为使用API？导入库？
Vue.use(ElementUI);
Vue.use(VueRouter);
Vue.config.productionTip = false
// 创建Vue界面,传入router、store、App对象？展示App.vue
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
