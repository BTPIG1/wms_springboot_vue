## 第一步复制container容器
- 前提：创建前端项目，并导入idea配置运行文件运行 

导入Element-ui:
````cmd
1.安装命令
npm i element-ui -S
2.main.js全局引入
import ElementUI from 'element-ui'; 
import 'element-ui/lib/theme-chalk/index.css';
 Vue.use(ElementUI);
````

导入main.js中
````vue
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import axios from "axios"
Vue.prototype.$axios=axios;
Vue.use(ElementUI);
````

从vue官网上复制一个模板-》index.vue 并在App.vue中注册使用
````vue
<!--1.修改： 把官网element.eleme.cn上的container组件复制下，el-container-》template，再修改样式。-->
<!--再App.vue中注册使用组件-->
<template>
  <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246);height: 100%;">
      <el-menu :default-openeds="['1', '3']">
        <el-submenu index="1">
          <template slot="title"><i class="el-icon-message"></i>导航一</template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="1-1">选项1</el-menu-item>
            <el-menu-item index="1-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="1-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="1-4">
            <template slot="title">选项4</template>
            <el-menu-item index="1-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title"><i class="el-icon-menu"></i>导航二</template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="2-1">选项1</el-menu-item>
            <el-menu-item index="2-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="2-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="2-4">
            <template slot="title">选项4</template>
            <el-menu-item index="2-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title"><i class="el-icon-setting"></i>导航三</template>
          <el-menu-item-group>
            <template slot="title">分组一</template>
            <el-menu-item index="3-1">选项1</el-menu-item>
            <el-menu-item index="3-2">选项2</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分组2">
            <el-menu-item index="3-3">选项3</el-menu-item>
          </el-menu-item-group>
          <el-submenu index="3-4">
            <template slot="title">选项4</template>
            <el-menu-item index="3-4-1">选项4-1</el-menu-item>
          </el-submenu>
        </el-submenu>
      </el-menu>
    </el-aside>

    <el-container style="height: 100%;">
      <el-header style="text-align: right; font-size: 12px">
        <el-dropdown>
          <i class="el-icon-setting" style="margin-right: 15px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>查看</el-dropdown-item>
            <el-dropdown-item>新增</el-dropdown-item>
            <el-dropdown-item>删除</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span>王小虎</span>
      </el-header>

      <el-main style="height: 100%;" >
        <el-table :data="tableData">
          <el-table-column prop="date" label="日期" width="140">
          </el-table-column>
          <el-table-column prop="name" label="姓名" width="120">
          </el-table-column>
          <el-table-column prop="address" label="地址">
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </el-container>
</template>

<!--修改：删除setup-->
<script>
  export default {
    name: 'Index',
    data() {
      const item = {
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      };
      return {
        tableData: Array(20).fill(item)
      }
    }
  };
</script>

<!--修改：添加样式-->
<style scoped>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }

  .el-main {
    padding: 5px;
  }

  .el-aside {
    color: #333;
  }
</style>
````

## 第二步划分成四个vue文件：Index,Aside,Header,Main
具体看文件 

## 第三步执行跨域 
### 先导入web项目npm install axios --save
### 后端项目common包中创建CorsConfig类解决springboot跨域请求的问题
````java
package com.wms.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //是否发送Cookie
                .allowCredentials(true)
                //放行哪些原始域
                .allowedOriginPatterns("*")
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}
````
### main.js全局引入axios
````vue
import axios from "axios"
Vue.prototype.$axios=axios;
Vue.prototype.$httpUrl="http://localhost:8090/"
````
### 在Main.vue中使用样例
````vue
<script>
    export default {
        name: "Main",
        data() {
            const item = {
                date: '2016-05-02',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
            };
            return {
                tableData: Array(20).fill(item)
            }
        },
        methods:{
            loadGet(){
                this.$axios.get(this.$httpUrl+'/user/list').then(res=>{
                    console.log(res)
                })
            },
            loadPost(){
                this.$axios.post(this.$httpUrl+'user/listP',{name:"Ali"})
                    .then(res=>res.data)
                    .then(res=>{
                        console.log(res);
                    })
            }
        },
        beforeMount() {
            // this.loadGet();
            this.loadPost();
        }
    }
</script>
````
- 还有另外一种地址拦截器的方法：request.js自行搜索

## 第四步为展示后端数据准备前端页面

### 首先将后端数据赋值给tableData，很简单this.tableData = res;即可
````vue
data() {
    return {
        tableData: []
    }
},
methods:{
    loadGet(){
        this.$axios.get(this.$httpUrl+'/user/list').then(res=>{
            console.log(res)
        })
    },
    loadPost(){
        this.$axios.post(this.$httpUrl+'user/listP',{name:"Ali"})
            .then(res=>res.data)
            .then(res=>{
                console.log(res);
                this.tableData = res;
            })
    }
},
beforeMount() {
    // this.loadGet();
    this.loadPost();
}
````

### 然后继续修改表单
````vue
<!--查看后端传来的数据，这里比如是user，只需要用prop="xx"，！！！xx是user属性名-->
<el-table-column prop="id" label="ID" width="180">
        </el-table-column>
````

### 对于需要转换的属性名替换用tag转换列（可以去ele官网复制模板）
````vue
<el-table-column prop="roleId" label="权限" width="180">
    <template slot-scope="scope">
        <el-tag
            :type="scope.row.roleId === 0 ? 'danger'
                        :(scope.row.roleId === 1 ? 'primary': 'success')"
            disable-transitions>{{scope.row.roleId === 0 ? '超级管理员'
            :(scope.row.roleId === 1 ? '管理员': '普通用户')}}</el-tag>
    </template>
</el-table-column>
````

### 完善table样式(header-cell-style)以及后端返回结果封装(Result)
很简单自己看

## 第五步分页查询

### 先从ele官网复制前端分页栏

```vue
<el-pagination
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :current-page="currentPage4"
    :page-sizes="[2, 5, 10, 20]"
    :page-size="10"
    :total="total"
    layout="total, sizes, prev, pager, next, jumper">
</el-pagination>
```

### 设置data中的变量，以及点击触发的方法
```vue
data() {
            return {
                tableData: [],
                pageSize: 10,
                pageNum: 1,
                total:0,
            }
        },
methods:{
    // loadGet(){
    //     this.$axios.get(this.$httpUrl+'/user/list').then(res=>{
    //         console.log(res)
    //     })
    // },
    loadPost(){
        this.$axios.post(this.$httpUrl+'user/listPageLR',{
            pageNum:this.pageNum,
            pageSize:this.pageSize,

        })
            .then(res=>res.data)
            .then(res=>{
            console.log(res);
            if(res.code==200){
                this.tableData=res.data
                this.total=Number(res.total)
                console.log( res.total)
                console.log( this.total)
            }else{
                alert('获取数据失败')
            }
        })
    },
    handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.pageSize=val;
        this.pageNum=1;
        this.loadPost();
    },
    handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.pageNum=val;
        this.loadPost();
    }
},
beforeMount() {
    // this.loadGet();
    this.loadPost();
}
```

## 第六步 查询功能实现

查询实现非常简单，前提需要后端能接受查询参数，比如名字、性别、年龄等等，然后复制ele官网的select样例并改格式，最后修改loadPost方法即可

下面是修改格式
```vue
<div style="margin-bottom: 5px">
    <el-input placeholder="请输入名字" v-model="name" suffix-icon="el-icon-search" style="width: 300px"
              @keyup.enter.native="loadPost"></el-input>
    <el-select v-model="sex" placeholder="请选择性别" style="margin-left: 5px">
        <el-option
            v-for="item in sexs"
            :key="item.sex"
            :label="item.label"
            :value="item.sex">
        </el-option>
    </el-select>
    <el-button type="primary" style="margin-left: 5px" @click="loadPost">查询</el-button>
    <el-button type="success" @click="resetParam">重置</el-button>
</div>
```

下面是修改后的方法及新增data参数
```vue
        data() {
            return {
                tableData: [],
                pageSize: 10,
                pageNum: 1,
                total:0,
                name:'',
                sex:'',
                sexs: [
                    {
                        sex: '1',
                        label: "男"
                    }, {
                        sex: '0',
                        label: "女"
                    }
                ],
            }
        },
        methods:{
            // loadGet(){
            //     this.$axios.get(this.$httpUrl+'/user/list').then(res=>{
            //         console.log(res)
            //     })
            // },
            loadPost(){
                this.$axios.post(this.$httpUrl+'user/listPageLR',{
                    pageNum:this.pageNum,
                    pageSize:this.pageSize,
                    params:{
                        name:this.name,
                        sex:this.sex
                    }
                })
                    .then(res=>res.data)
                    .then(res=>{
                    console.log(res);
                    if(res.code==200){
                        this.tableData=res.data
                        this.total=Number(res.total)
                        console.log( this.sex)
                    }else{
                        alert('获取数据失败')
                    }
                })
            },
            resetParam(){
                this.name=''
                this.sex=''
            },
```

## 第七步 新增功能实现

### 设置新增弹出表单
```vue
<!--新增按钮绑定一个add事件用于设置centerDialogVisible为true弹出表单-->
<el-button type="primary" style="margin-left: 5px;" @click="add">新增</el-button>

<!--表单-->
<el-dialog
    title="提示"
<!--   :visible.sync="centerDialogVisible"可变参数：表单显示隐藏 -->
    :visible.sync="centerDialogVisible" 
    width="30%"
    center>

    <!--           ref是为元素注册一个引用名称通过this.$refs.form调用
                   :rules="rules"绑定校验规则   :model="form" 绑定数据模型  -->
    <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <!--            prop="no" 告诉 <el-form-item> 这个表单项需要校验的字段是 form.no    -->
        <el-form-item label="账号" prop="no">
            <el-col :span="20">
                <!--                    v-model="form.no"绑定输入框与form.no数据    -->
                <el-input v-model="form.no"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="名字" prop="name">
            <el-col :span="20">
                <el-input v-model="form.name"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-col :span="20">
                <el-input v-model="form.password"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
            <el-col :span="20">
                <el-input v-model="form.age"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="性别">
            <el-radio-group v-model="form.sex">
                <el-radio label="1">男</el-radio>
                <el-radio label="0">女</el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
            <el-col :span="20">
                <el-input v-model="form.phone"></el-input>
            </el-col>
        </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
</el-dialog>
```

上面设置好样式后，接下来是设置script
```vue
<!--data中需要设计规范约束，如年龄限制、账号并不能重复等。return中需要定义rules规则-->
data() {
    let checkAge = (rule, value, callback) => {
        if(value>150){
            callback(new Error('年龄输入过大'));
        }else{
            callback();
        }
    };
    let checkDuplicate =(rule,value,callback)=>{
        if(this.form.id){
            return callback();
        }
        this.$axios.get(this.$httpUrl+"user/findByNo?no="+this.form.no).then(res=>res.data).then(res=>{
            console.log("--------2----------");
            if(res.code!=200){
                callback()
            }else{
                callback(new Error('账号已经存在'));
            }
        })
    };
    return {
        centerDialogVisible:false,
        rules: {
        no: [
        {required: true, message: '请输入账号', trigger: 'blur'},
        {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
        {validator:checkDuplicate,trigger: 'blur'}
        ],
        name: [
        {required: true, message: '请输入名字', trigger: 'blur'}
        ],
        password: [
        {required: true, message: '请输入密码', trigger: 'blur'},
        {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'}
        ],
        age: [
        {required: true, message: '请输入年龄', trigger: 'blur'},
        {min: 1, max: 3, message: '长度在 1 到 3 个位', trigger: 'blur'},
        {pattern: /^([1-9][0-9]*){1,3}$/,message: '年龄必须为正整数字',trigger: "blur"},
        {validator:checkAge,trigger: 'blur'}
        ],
        phone: [
        {required: true,message: "手机号不能为空",trigger: "blur"},
        {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}
        ]
    }
}
```

method中需要定义显示表单方法add(),重置表单方法resetForm(),保存方法save()
```vue
<!--注意form是对表单项的注册名-->
resetForm(){ //重置表单
  this.$refs.form.resetFields();
},
add(){
    this.centerDialogVisible = true
    this.$nextTick(()=>{
        this.resetForm()
    })
},
save(){
    this.$refs.form.validate((valid)=>{
        if(valid){
            this.$axios.post(this.$httpUrl+'user/save',this.form)
                .then(res=>res.data)
                .then(res=>{
                    console.log(res)
                    if(res.code==200){

                        this.$message({
                            message: '操作成功！',
                            type: 'success'
                        });
                        this.centerDialogVisible = false
                        this.loadPost()
                        this.resetForm()
                    }else{
                        this.$message({
                            message: '操作失败！',
                            type: 'error'
                        });
                    }
                })
        }else{
            console.log('error submit!!');
            return flase;
        }
    });
},
```

## 登录功能实现
首先登录前端页面Login.vue设计与前面提到的差不多因此就直接复制粘贴了，然后在后端
实现login接口
```java
// 注意：1，使用eq不是like。2，需要返回成功后的数据（验证是谁登录）
@PostMapping("/login")
    public Result login(@RequestBody User user){
        List list = userService.lambdaQuery()
                .eq(User::getNo,user.getNo())
                .eq(User::getPassword,user.getPassword()).list();
        return list.size()>0?Result.suc(list.get(0)):Result.fail();
    }
```

### 需设置路由为了后续跳转
首先设计路由文件index.js

```js
import VueRouter from "vue-router";

// 路由参数
const routes = [
    // 这里的每个括号抱起来的都算一个路由，path是映射路径，name是名称，
    // component组件位置
    {
        // path:'/login',
        path: '/',
        name: 'login',
        component: () => import('./Login.vue')
    }
]

// 路由对象
const router = new VueRouter({
    mode: 'history',
    routes
})

export default router;
```

然后在main.js中注册(js文件在main.js注册，vue文件在app.vue中注册)
```js
// 这里有两个步骤
// 步骤1导入路由并使用
import VueRouter from "vue-router";
import router from './router';
Vue.use(VueRouter);

// 步骤2注册？
new Vue({
    router,
    render: h => h(App),
}).$mount('#app')
```

最重要的一步需要在App.vue中删除其他已经注册的组件，使用<router-view/>
```vue
<template>
    <div id="app">
        <router-view></router-view>
    </div>
</template>
```

- 如何实现跳转？
```vue
this.$axios.post(this.$httpUrl+'/user/login',this.loginForm).then(res=>res.data).then(res=>{
    console.log(res)
    if(res.code==200){
        //存储
        sessionStorage.setItem("CurUser",JSON.stringify(res.data.user))

        console.log(res.data.menu)
        //-------------------跳转到主页---------------
        this.$router.replace('/Index');
    }else{
        this.confirm_disabled=false;
        alert('校验失败，用户名或密码错误！');
        return false;
    }
});
```

## 退出功能实现
退出功能非常简单，仅需要一个路由调整即可，不过注意下点击退出会先弹出一个提示框然后再退出
```vue
<!--这里为显示用户信息提取出在登录阶段保存到session中的用户信息即可-->
data(){
    return {
    user: JSON.parse(sessionStorage.getItem('CurUser')) //登录的时候保存到session中了
    }
},
<!--确认框实现-->
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
```


## 菜单跳转

### 第一步设置router-view
```vue
<!--首先在index.vue中把下面代码修改了-->
<el-main style="height: 100%;width: 100%">
    <!--                <Main></Main>-->
    <router-view/>
</el-main>
<!--以及Aside左侧边框中修改el-menu 非常简单加上router即可-->
<el-menu
    style="height: 100vh"
    class="el-menu-vertical-demo"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    default-active="/Home"
    :collapse="isCollapse"
    :collapse-transition="false"
    router
>
```

### 第二步编写完vue页面后在index.js中设置子路由
```vue
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
        {
            path:'/Admin',
            name:'admin',
            meta:{
                title:'管理员管理'
            },
            component:()=>import('../components/admin/AdminManage.vue')
        },
        {
            path:'/User',
            name:'user',
            meta:{
                title:'用户管理'
            },
            component:()=>import('../components/user/UserManage.vue')
        },
    ]
}

<!--                注意修改完可能会报错加上下面代码即可   -->
const VueRouterPush = VueRouter.prototype.push
    VueRouter.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
}
```

### 以上设置好后路由就会根据el-menu-item index="/Home"跳转,下面是如何设计一个循环el-menu-item
```vue
<!-- 首先设计一个数组-->
data(){
    return{
        //isCollapse: false
        menu:[
        {
        menuClick:'Admin',
        menuName:'管理员管理',
        menuIcon:'el-icon-s-custom',
        },{
        menuClick:'User',
        menuName:'用户管理',
        menuIcon:'el-icon-user-solid',
        }
        ]
    };
},

<el-menu-item index="/Home">
    <i class="el-icon-s-home"></i>
    <span slot="title">首页</span>
</el-menu-item>
<!-- v-for循环读取数组中的元素-->
<el-menu-item :index="'/'+item.menuClick" v-for="(item,i) in menu" :key="i">
    <i :class="item.menuIcon"></i>
    <span slot="title">{{ item.menuName }}</span>
</el-menu-item>
```

## 动态menu设计
### 创建数据库表并使用代码生成器生成
```sql
# 这里创建了一个menu表用来管理展示给不同权限的用户不同的功能
CREATE TABLE `menu` (
  `id` int(11) NOT NULL,
  `menuCode` varchar(8) DEFAULT NULL COMMENT '菜单编码',
  `menuName` varchar(16) DEFAULT NULL COMMENT '菜单名字',
  `menuLevel` varchar(2) DEFAULT NULL COMMENT '菜单级别',
  `menuParentCode` varchar(8) DEFAULT NULL COMMENT '菜单的父code',
  `menuClick` varchar(16) DEFAULT NULL COMMENT '点击触发的函数',
  `menuRight` varchar(8) DEFAULT NULL COMMENT '权限 0超级管理员，1表示管理员，2表示普通用户，可以用逗号组合使用',
  `menuComponent` varchar(200) DEFAULT NULL,
  `menuIcon` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `menu` VALUES (1, '001', '管理员管理', '1', NULL, 'Admin', '0', 'admin/AdminManage.vue', 'el-icon-s-custom');
INSERT INTO `menu` VALUES (2, '002', '用户管理', '1', NULL, 'User', '0,1', 'user/UserManage.vue', 'el-icon-user-solid');
```

### 代码生成器生成后需要编写一个list根据用户roleId查询可用menu
```java
// 这里写法是合并到login中，因为只需要查询登录用户的权限，可以合并到login方法中来，用一个hashmap保存不同数据
@PostMapping("/login")
    public Result login(@RequestBody User user){
        List list = userService.lambdaQuery()
                .eq(User::getNo,user.getNo())
                .eq(User::getPassword,user.getPassword()).list();

        if(list.size()>0){
            User user1 = (User) list.get(0);
            List menuList = menuService.lambdaQuery().like(Menu::getMenuright,user1.getRoleId()).list();
            HashMap map = new HashMap();
            map.put("user",user1);
            map.put("menu",menuList);
            return Result.suc(map);
        }

        return Result.fail();
    }

```

### 再动态展示menu之前需要安装 npm i vuex@3.0.0

### 编写store动态管理
```js
// ./store/index.js
import vue from 'vue'
import Vuex from 'vuex'
vue.use(Vuex)
export default new Vuex.Store({
    state: {
        menu: []
    },
    mutations: {
        setMenu(state,menuList) {
            state.menu = menuList

            addNewRoute(menuList)
        }
    },
    getters: {
        getMenu(state) {
            return state.menu
        }
    }
})
// Login.vue 的confirm中加入
this.$store.commit("setMenu",res.data.menu)
```

### 在Aside中使用menu
```vue
<!--export default中加入computed就能替换静态menu-->
computed:{
    "menu":{
        get(){
            return this.$store.state.menu
        }
    }
},
```

## 动态路由设计
问题分析：router代码里index的子路由是写死的，因此需要实现一个从服务器中动态读取路由的功能

思路：在store动态管理index.js文件中新增一个addNewRoute方法用来实现加载路由
```js
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

```

## 物品管理如何用formatter替换多表联查
在GoodsManage中，由于Goods表设计中列名为storage为int类型，所以在展示时避免显示数字需要解决

- 思路
1. 使用多表联查，额外编写一个接口
2. 在跳转页面前先读取额外数据并静态保存在data中，利用formatter动态显示

1.sql多表联查
```java
------------------------------------------------先编写sql语句----------------------------------------------
${ew.customSqlSegment} 就是动态where。
！！！注意由于返回类型的参数比record多，因此这里要新建一个record的子类，并额外定义属性。 
为什么不额外建立一个表？ 我的想法：可以这样做但没必要，因为所有的数据都可以联合查询得到，通过新建一个entity也能实现。
<select id="pageLR" resultType="com.wms.entity.RecordRes">
    select a.* ,g.name goodsname,s.name storagename,gt.name goodstypename,
                   (select u.name from user u where u.id=a.userId)username,
            (select u.name from user u where u.id=a.admin_id)adminname
    from record a,goods g,storage s,goodstype gt
    ${ew.customSqlSegment}
</select>


-----------------------------------------------编写controller层方法------------------------------------
// lambdaQueryWrapper + 分页
    @PostMapping("/listPageLR")
    public Result listPageLR(@RequestBody QueryPageParam query) {
        HashMap params = query.getParams();
        String goodsname = (String)params.get("goodsname"); // 商品名
        String storage = (String)params.get("storage"); // 仓库id
        String goodstype = (String)params.get("goodstype"); // 类型id
        String roleId = (String)params.get("roleId"); // 类型id
        String userid = (String)params.get("userid"); // 类型id

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        // 由于sql语句使用了多表联查，这里不适合用LambdaQueryWrapper，应用其子类
//        LambdaQueryWrapper<Record> lambdaQueryWrapper = new LambdaQueryWrapper();
        QueryWrapper<Record> queryWrapper = new QueryWrapper(); // LambdaQueryWrapper实际上是一个替换动态where的功能  用法：lambda/like/eq(数据库属性名,类属性)
        queryWrapper.apply("g.id=a.goods and g.storage = s.id and g.goodsType = gt.id");

        if("2".equals(roleId)){
            queryWrapper.apply("a.userId= "+userid);
        }

        if(StringUtils.isNotBlank(goodsname) && !"null".equals(goodsname)) queryWrapper.like("g.name",goodsname);
        if(StringUtils.isNotBlank(storage) && !"null".equals(storage)) queryWrapper.eq("s.id",storage);
        if(StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)) queryWrapper.eq("gt.id",goodstype);

        IPage result = recordService.pageLR(page, queryWrapper);
        return Result.suc(result.getRecords(),result.getTotal() );
    }
```

2.数据量少可以预先读取到vue中的data里
```vue
<!--请求读取所有数据-->
loadStorage(){
this.$axios.get(this.$httpUrl+'/storage/list',{

}).then(res=>res.data).then(res=>{
console.log(res)
if(res.code==200){
<!--保存到storageData-->
this.storageData=res.data
}else{
alert('获取数据失败')
}

})
},

<!--定义查找方法ele官网有-->
formatStorage(row){
let temp = this.storages.find(item=>{
return item.id == row.storage
})
// temp不为空
return temp&&temp.name
},

<!--顺序查找-->
<el-table-column prop="storage" label="仓库" width="180" :formatter="formatStorage">
</el-table-column>
```

## 出入库操作实现
- 思路： 首先思考后端接口需要什么参数（RecordRes）,完善vue能传递需要的参数。 出入库后还要添加记录， 因此两个操作在controller层实现较好，可以保证事务的完整性。

需要的参数
```vue
formRecord:{
                goods:'',
                goodsname:'',
                userid:'',
                username:'',
                adminId:'',
                count:'',
                remark:'',
                action:'',
            },
```

思考: goods:'', goodsname:'',adminId:'', count:'', remark:'', action:'', 这几个参数可以通过单选框或input框实现，如何才能选择用户？
答案：这里用了一个非常巧妙的方法，复制一个已有的userManage.vue=》SelectUser.vue, 用单选框+this.$emit("xxx",val)方法传递给父组件实现。

单选框实现：
```vue
<el-table :data="tableData"
          :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
          border
          highlight-current-row
          @current-change="handleInGoodsCurrentChange"
>
<!--highlight-current-row @current-change="handleInGoodsCurrentChange" 这里是实现单选的必须，并且只能与el-table一起使用
    点击一行后会把这行数据作为参数传递到handleInGoodsCurrentChange方法中
-->

handleInGoodsCurrentChange(val){
    this.currentRow = val
},
```

其他信息填写：
```vue
 <el-dialog
    title="入库信息"
    :visible.sync="outerVisible"
    center>
    <el-form ref="formRec" :rules="rulesRecord" :model="formRecord" label-width="80px">
        <el-form-item label="货品名">
            <el-col :span="20">
                <el-input type="textarea" v-model="formRecord.goodsname"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="申请人">
            <el-col :span="20">
                <el-input type="textarea" v-model="formRecord.username" @click.native="selectUser"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="数目">
            <el-col :span="20">
                <el-input type="textarea" v-model="formRecord.count"></el-input>
            </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
            <el-col :span="20">
                <el-input type="textarea" v-model="formRecord.remark"></el-input>
            </el-col>
        </el-form-item>
        <el-dialog
            width="70%"
            title="用户选择"
            :visible.sync="innerVisible"
            append-to-body>
            <SelectUser @doSelectUser="doSelectUser"></SelectUser>
            <el-button @click="innerVisible = false;">取 消</el-button>
            <el-button type="primary" @click="confirmUser">确 定</el-button>
        </el-dialog>
    </el-form>
    <span slot="footer" class="dialog-footer">
                <el-button @click="outerVisible = false;">取 消</el-button>
                <el-button type="primary" @click="doInGoods">确 定</el-button>
            </span>
</el-dialog>
```

