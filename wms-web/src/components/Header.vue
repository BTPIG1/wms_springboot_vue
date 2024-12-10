
<!--注意：vue不允许出现两个标签，因此必须用<div>括起来-->
<template>
    <div style="display: flex; line-height: 60px">
        <div style="margin-top: 8px; cursor: pointer;">
<!--          @click="collapse" 点击触发collapse()方法  -->
            <i :class="icon" style="font-size: 20px;" @click="collapse"></i>
        </div>
        <div style="flex: 1;text-align: center;font-size: 34px">
            <span>欢迎来到仓库管理系统</span>
        </div>
        <div style="font-size: 20px">
            <span>{{ user.name }}</span>
            <el-dropdown>
                <i class="el-icon-arrow-down" style="margin-left: 10px"></i>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
                    <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
export default {
    name: "Header",
    data(){
        return {
            user: JSON.parse(sessionStorage.getItem('CurUser')) //登录的时候保存到session中了
        }
    },
    methods:{
        toUser(){
            console.log("to_User")
            this.$router.push("/Home")
        },
        logout(){
            // 使用 this.$confirm 来弹出确认框
            this.$confirm('您确定要退出登录吗？', '提示', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning', // 弹框类型，可以是 info, success, warning, error
                center: true,
            })
                .then(() => {
                    // 点击确认后的操作
                    this.$message({
                        type:'success',
                        message:'退出登录成功'
                    })
                    this.$router.replace('/');
                    sessionStorage.clear()//清空session
                })
                .catch(() => {
                    // 点击确认后的操作
                    this.$message({
                        type:'info',
                        message:'已取消退出登录'
                    })
                });

        },
        // this.$emit('aCollapse')触发父组件方法aCollapse
        collapse(){
            this.$emit('aCollapse')
        }
    },
    created(){
      this.$router.push('/Home')
    },
    props:{
        icon:String,
    }
}
</script>

<style scoped>

</style>