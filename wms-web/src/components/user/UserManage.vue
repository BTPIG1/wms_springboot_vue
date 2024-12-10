<template>
    <div>
        <div style="margin-bottom: 5px;">
            <el-input v-model="name" placeholder="请输入名字" suffix-icon="el-icon-search" style="width: 200px;"
                      @keyup.enter.native="loadPost"></el-input>
            <el-select v-model="sex" filterable placeholder="请选择性别" style="margin-left: 5px;">
                <el-option
                    v-for="item in sexs"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
            </el-select>
            <el-button type="primary" style="margin-left: 5px;" @click="loadPost">查询</el-button>
            <el-button type="success" @click="resetParam">重置</el-button>

            <el-button type="primary" style="margin-left: 5px;" @click="add">新增</el-button>
        </div>
        <!--        固定用法（el-table :data="tableData"），tableData是定义在data中的数据，tableData在页面加载时通过loadPost方法已经赋值了result.data,所以子组件才能用prop捕获数据-->
        <el-table :data="tableData"
                  :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
                  border
        >
            <!--            el-table-column prop="id"这种组合用来捕获data中的属性-->
            <el-table-column prop="id" label="ID" width="60">
            </el-table-column>
            <el-table-column prop="no" label="账号" width="180">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="180">
            </el-table-column>
            <el-table-column prop="age" label="年龄" width="80">
            </el-table-column>
            <el-table-column prop="sex" label="性别" width="80">
                <!--                prop捕获了sex属性，通过slot的方式可以实现自己定义展示-->
                <template slot-scope="scope">
                    <el-tag
                        :type="scope.row.sex === 1 ? 'primary' : 'success'"
                        disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="roleId" label="角色" width="120">
                <template slot-scope="scope">
                    <el-tag
                        :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
                        disable-transitions>{{scope.row.roleId === 0 ? '超级管理员' :
                        (scope.row.roleId === 1 ? '管理员' : '用户')}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="phone" label="电话" width="180">
            </el-table-column>
            <el-table-column prop="operate" label="操作">
                <!--               slot插槽是一种用于在父组件中传递内容到子组件的机制，而 slot-scope 则是用来绑定插槽的作用域，使得父组件传递的数据可以在子组件中使用。-->
                <template slot-scope="scope">
                    <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
                    <el-popconfirm
                        title="确定删除吗？"
                        @confirm="del(scope.row.id)"
                        style="margin-left: 5px;"
                    >
                        <!--                        在 el-popconfirm 组件中，slot="reference" 是一个特定的插槽，用来指定哪个元素会触发弹出确认框。当用户点击该元素时，el-popconfirm 会显示一个确认框，要求用户确认是否执行某个操作（如删除）。-->
                        <el-button slot="reference" size="small" type="danger" >删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20,30]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>

        <el-dialog
            title="提示"
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
    <el-button @click="centerDialogVisible = false;">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
        </el-dialog>
    </div>
</template>


<script>
export default {
    name: "Main",
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
            this.$axios.get(this.$httpUrl+"/user/findByNo?no="+this.form.no).then(res=>res.data).then(res=>{
                console.log("--------2----------");
                if(res.code!=200){
                    callback()
                }else{
                    callback(new Error('账号已经存在'));
                }
            })
        };

        return {
            tableData: [],
            pageSize:10,
            pageNum:1,
            total:0,
            name:'',
            sex:'',
            sexs:[
                {
                    value: '1',
                    label: '男'
                }, {
                    value: '0',
                    label: '女'
                }
            ],
            centerDialogVisible:false,
            form:{
                id:'',
                no:'',
                name:'',
                password:'',
                age:'',
                phone:'',
                sex:'0',
                roleId:'2'
            },
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
    },
    methods:{
        // loadGet(){
        //     this.$axios.get(this.$httpUrl+'/user/list').then(res=>{
        //         console.log(res)
        //     })
        // },
        loadPost(){
            this.$axios.post(this.$httpUrl+'/user/listPageLR',{
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
        resetForm(){ //重置表单
            this.$refs.form.resetFields();
        },
        add(){
            this.form.id = ''
            this.centerDialogVisible = true
            this.$nextTick(()=>{
                this.resetForm()
            })
        },
        del(id){
            console.log(id)

            this.$axios.get(this.$httpUrl+'/user/del?id='+id).then(res=>res.data).then(res=>{
                console.log(res)
                if(res.code==200){

                    this.$message({
                        message: '操作成功！',
                        type: 'success'
                    });
                    this.loadPost()
                }else{
                    this.$message({
                        message: '操作失败！',
                        type: 'error'
                    });
                }

            })
        },
        doSave(){
            this.$axios.post(this.$httpUrl+'/user/save',this.form).then(res=>res.data).then(res=>{
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
        },
        doMod(){
            this.$axios.post(this.$httpUrl+'/user/update',this.form).then(res=>res.data).then(res=>{
                console.log(res)
                if(res.code==200){
                    this.form.id = '';
                    this.$message({
                        message: '操作成功！',
                        type: 'success'
                    });
                    this.centerDialogVisible = false
                    this.loadPost()
                    this.resetForm()
                } else {
                    this.$message({
                        message: '操作失败！',
                        type: 'error'
                    });
                }

            })
        },
        mod(row) {
            console.log(row.id)

            this.$nextTick(() => {
                //赋值到表单
                this.form.id = row.id
                this.form.no = row.no
                this.form.name = row.name
                this.form.password = ''
                this.form.age = row.age + ''
                this.form.sex = row.sex + ''
                this.form.phone = row.phone
                this.form.roleId = row.roleId
            })
            this.centerDialogVisible = true
        },
        save() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if (this.form.id) {
                        this.doMod();
                    } else {
                        this.form.roleId='2'
                        this.doSave();
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        resetParam() {
            this.name = ''
            this.sex = ''
            this.loadPost()
        },
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageSize = val;
            this.pageNum = 1;
            this.loadPost();
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum = val;
            this.loadPost();
        }
    },
    beforeMount() {
        // this.loadGet();
        this.loadPost();
    }
}
</script>

<style scoped>

</style>

