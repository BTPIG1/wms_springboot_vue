import VueRouter from "vue-router";

// routes是需要传入的路由信息
const routes = [
    {
        // path:'/login',
        path:'/',
        name:'login',
        component:()=>import('../components/Login.vue')
    },
    {
        path:'/Index',
        name:'index',
        component:()=>import('../components/Index.vue'),
        children:[
            {
                path:'/Home',
                name:'home',
                meta:{
                    title:'首页'
                },
                component:()=>import('../components/Home.vue')
            },
            // {
            //     path:'/Admin',
            //     name:'admin',
            //     meta:{
            //         title:'管理员管理'
            //     },
            //     component:()=>import('../components/admin/AdminManage.vue')
            // },
            // {
            //     path:'/User',
            //     name:'user',
            //     meta:{
            //         title:'用户管理'
            //     },
            //     component:()=>import('../components/user/UserManage.vue')
            // },
        ]
    }
]

// routes是需要传入的路由信息，router是新建的路由对象
const router = new VueRouter({
    mode:'history',
    routes,
});

// 这里重写的是VueRouter的原型对象上的push方法，由于js是动态绑定的，因此所有的VueRouter实例都会被影响
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
}
// 如果只想重写router单个示例的push方法可以如下实现
// router.push = ....

// 替换router对象中的routes信息
export function resetRouter() {
    router.matcher = new VueRouter({
        mode:'history',
        routes: []
    }).matcher
}

// 供其他js文件使用该对象
export default router;