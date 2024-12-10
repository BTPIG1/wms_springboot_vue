<!--1.修改： 把官网element.eleme.cn上的container组件复制下，el-container-》template，再修改样式。-->
<!--再App.vue中注册使用组件-->
<template>
    <el-container style="height: 100%; border: 1px solid #eee">
        <el-aside :width="aside_width" style="background-color: rgb(238, 241, 246);
            height: 100vh;margin-left: -1px">
<!--           :isCollapse接收子组件传来的isCollapse -->
            <Aside :isCollapse="isCollapse"></Aside>
        </el-aside>

        <el-container style="height: 100%;">
            <el-header style="text-align: right; font-size: 12px;height: 100%;border-bottom: darkgray 1px solid">
<!--               @aCollapse="doCollapse" 解释：@aCollapse是触发的方法名字(条件)，"doCollapse"是触发后调用的方法-->
                <Header @aCollapse="doCollapse" :icon="icon"></Header>
            </el-header>

            <el-main style="height: 100%;width: 100%">
<!--                <Main></Main>-->
                <router-view/>
            </el-main>
        </el-container>
    </el-container>
</template>

<!--修改：删除setup-->
<script>

import Header from "@/components/Header.vue";
import Aside from "@/components/Aside.vue";

export default {
    name: 'Index',
    components:{Header, Aside},
    data(){
        return{
            isCollapse:false,
            aside_width:'200px',
            icon:"el-icon-s-fold"
        }
    },
    methods:{
        doCollapse(){
            console.log("doCollapse");
            this.isCollapse=!this.isCollapse;
            if(!this.isCollapse){
                this.aside_width='200px';
                this.icon="el-icon-s-fold"
            }else{
                this.aside_width='64px';
                this.icon="el-icon-s-unfold"
            }
        }
    }
};
</script>

<!--修改：添加样式-->
<style scoped>
    .el-header {
        #background-color: #B3C0D1;
        color: #333;
        line-height: 60px;
    }

    .el-main {
        padding: 5px;
    }

    .el-aside {
        overflow: hidden;
        color: #333;
    }
</style>