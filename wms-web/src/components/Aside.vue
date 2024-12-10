<template>
    <el-menu
        style="height: 100%"
        class="el-menu-vertical-demo"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        default-active="/Home"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
    >

        <el-menu-item index="/Home">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
        </el-menu-item>
        <el-menu-item :index="'/'+item.menuclick" v-for="(item,i) in menu" :key="i">
            <i :class="item.menuicon"></i>
            <span slot="title">{{ item.menuname }}</span>
        </el-menu-item>
    </el-menu>
</template>

<style>

</style>

<script>
export default {
    name: "Aside",
    //  --------------------------------------------- data与computed的区别---------------------------------------
    // data返回的数据是在组件实例化时获取的（静态的适合一次性读取）    computed 属性通过 get 方法获取 this.$store.state.menu，并且自动响应 Vuex store 中的变化（动态会自动更新）
    data(){
        return {
            // menu: this.$store.state.menu
        };
    },
    computed:{
        menu:{
            get(){
                return this.$store.state.menu
            }
        }
    },
    methods:{

    },
    // 这里实现了父组件到子组件的传递，props可以接受父组件的data()
    props:{
        isCollapse:Boolean
    }
}
</script>