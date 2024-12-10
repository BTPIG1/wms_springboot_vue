import vue from 'vue'
import Vuex from 'vuex'
// 导入路由对象
import router,{resetRouter} from "../router";
vue.use(Vuex)

function addNewRoute(menuList) {
    console.log(menuList)
    let routes = router.options.routes
    console.log(routes)
    routes.forEach(routeItem=>{
        if(routeItem.path=="/Index"){
            menuList.forEach(menu=>{
                let childRoute =  {
                    path:'/'+menu.menuclick,
                    name:menu.menuname,
                    meta:{
                        title:menu.menuname
                    },
                    component:()=>import('../components/'+menu.menucomponent)
                }

                routeItem.children.push(childRoute)
            })
        }
    })

    resetRouter()
    router.addRoutes(routes)
}

export default new Vuex.Store({
    state: {
        // 动态数据 this.$store.state.menu访问
        menu: []
    },
    // mutations是唯一能修改state的方式， ------------------固定使用方法 this.$store.commit('setMenu', newMenuData);-----------------
    mutations: {
        // 保存
        setMenu(state,menuList) {
            state.menu = menuList

            addNewRoute(menuList)
        }
    },
    // getters 是 Vuex 中用于获取 state 数据的计算属性。它可以基于 state 中的数据进行派生计算（自定义计算）
    getters: {
        // 获取
        getMenu(state) {
            return state.menu
        }
    }
})