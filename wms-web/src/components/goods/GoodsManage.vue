<template>
    <div>
        <div style="margin-bottom: 5px;">
            <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 200px;"
                      @keyup.enter.native="loadPost"></el-input>
            <el-select v-model="storage" placeholder="请选择仓库" style="margin-left: 5px;">
                <el-option
                    v-for="item in storageData"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
            </el-select>
            <el-select v-model="goodstype" placeholder="请选择类型">
                <el-option
                    v-for="item in goodsDate"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                    <span style="float: left">{{ item.name }}</span>
                </el-option>
            </el-select>
            <el-button type="primary" style="margin-left: 5px;" @click="loadPost">查询</el-button>
            <el-button type="success" @click="resetParam">重置</el-button>

            <el-button type="primary" style="margin-left: 5px;" @click="add" v-if="user.roleId!=2">新增</el-button>

            <el-button type="primary" style="margin-left: 5px;" @click="inGoods">入库</el-button>
            <el-button type="danger" style="margin-left: 5px;" @click="outGoods">出库</el-button>
        </div>
        <el-table :data="tableData"
                  :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
                  border
                  highlight-current-row
                  @current-change="handleInGoodsCurrentChange"
        >
            <el-table-column prop="id" label="ID" width="60">
            </el-table-column>
            <el-table-column prop="name" label="物品名" width="180">
            </el-table-column>
            <el-table-column prop="storage" label="仓库" width="180" :formatter="formatStorage">
            </el-table-column>
            <el-table-column prop="goodstype" label="物品类型" width="180" :formatter="formatGoods">
            </el-table-column>
            <el-table-column prop="count" label="总计" width="180">
            </el-table-column>
            <el-table-column prop="remark" label="备注">
            </el-table-column>
            <el-table-column prop="operate" label="操作">
                <template slot-scope="scope">
                    <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
                    <el-popconfirm
                        title="确定删除吗？"
                        @confirm="del(scope.row.id)"
                        style="margin-left: 5px;"
                    >
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

<!--        修改操作对话框-->
        <el-dialog
            title="提示"
            :visible.sync="centerDialogVisible"
            width="30%"
            center>

            <el-form ref="form" :rules="rules" :model="form" label-width="80px">
                <el-form-item label="物品" prop="name">
                    <el-col :span="20">
                        <el-input v-model="form.name"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="仓库" prop="storage">
                    <el-select v-model="form.storage" placeholder="请选择">
                        <el-option
                            v-for="item in storageData"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                            <span style="float: left">{{ item.name }}</span>
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="类型" prop="goodstype">
                    <el-select v-model="form.goodstype" placeholder="请选择">
                        <el-option
                            v-for="item in goodsDate"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                            <span style="float: left">{{ item.name }}</span>
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="存量" prop="count">
                    <el-col :span="20">
                        <el-input v-model="form.count"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-col :span="20">
                        <el-input type="textarea" v-model="form.remark"></el-input>
                    </el-col>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="centerDialogVisible = false;this.form.id=''">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </span>
        </el-dialog>

<!--    入库操作对话框-->
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
    </div>
</template>

<script>
import SelectUser from "@/components/user/SelectUser.vue";

export default {
    name: "GoodsManage",
    components: {SelectUser},
    data() {
        let checkCount = (rule, value, callback) => {
            if(value>9999){
                callback(new Error('数量输⼊过⼤'));
            }else{
                callback();
            }
        };
        return {
            tableData: [],
            storageData:[],
            goodsDate:[],
            pageSize:10,
            pageNum:1,
            total:0,
            name:'',
            storage:'',
            goodstype:'',
            centerDialogVisible:false,
            outerVisible:false,
            innerVisible:false,
            user: JSON.parse(sessionStorage.getItem('CurUser')),
            tempUser:null,
            form:{
                id:'',
                name:'',
                storage:'',
                goodstype:'',
                count:'',
                remark:''
            },
            currentRow: null,
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
            rulesRecord: {

            },
            rules: {
                name: [
                    {required: true, message: '请输入物品名', trigger: 'blur'}
                ],
                storage:[
                    {required: true, message: '请选择仓库', trigger: 'blur'}
                ],
                goodstype:[
                    {required: true, message: '请选择分类', trigger: 'blur'}
                ],
                count: [
                    {required: true, message: '请输入数量', trigger: 'blur'},
                    {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
                    {validator:checkCount,trigger: 'blur'}
                ],
            }
        }
    },
    methods:{
        resetForm() {
            console.log(this.$refs.form);
            console.log(this.$refs.formRec); // 检查是否为 undefined
            if (this.$refs.form) {
                this.$refs.form.resetFields();
            }
            if (this.$refs.formRec) {
                this.$refs.formRec.resetFields();
            }
        },
        del(id){
            console.log(id)

            this.$axios.get(this.$httpUrl+'/goods/del?id='+id).then(res=>res.data).then(res=>{
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
        mod(row){
            this.centerDialogVisible = true
            this.$nextTick(()=>{
                //赋值到表单
                this.form.id = row.id
                this.form.name = row.name
                this.form.storage = row.storage
                this.form.count = row.count
                this.form.goodstype = row.goodstype
                this.form.remark = row.remark
            })
        },
        add(){
            this.form.id = ''
            this.centerDialogVisible = true
            this.$nextTick(()=>{
                this.resetForm()
            })

        },
        doSave(){
            this.$axios.post(this.$httpUrl+'/goods/save',this.form).then(res=>res.data).then(res=>{
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
        doMod() {
            this.$axios.post(this.$httpUrl + '/goods/update', this.form).then(res => res.data).then(res => {
                console.log(res)
                if (res.code == 200) {

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
        save() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if (this.form.id) {
                        this.doMod();
                    } else {
                        this.doSave();
                    }
                    this.form.id=''
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });

        },
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageNum = 1
            this.pageSize = val
            this.loadPost()
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.pageNum = val
            this.loadPost()
        },
        resetParam() {
            this.name = '';
            this.storage = '';
            this.goodstype = '';
        },
        loadPost() {
            this.$axios.post(this.$httpUrl + '/goods/listPageLR', {
                pageSize: this.pageSize,
                pageNum: this.pageNum,
                params: {
                    name:this.name,
                    goodstype:this.goodstype+'',
                    storage: this.storage+''
                }
            }).then(res => res.data).then(res => {
                console.log(res)
                if (res.code == 200) {
                    this.tableData = res.data
                    this.total = res.total
                } else {
                    alert('获取数据失败')
                }

            })
        },
        loadStorage(){
            this.$axios.get(this.$httpUrl+'/storage/list',{

            }).then(res=>res.data).then(res=>{
                console.log(res)
                if(res.code==200){
                    this.storageData=res.data
                }else{
                    alert('获取数据失败')
                }

            })
        },
        loadGoodstype(){
            this.$axios.get(this.$httpUrl+'/goodstype/list',{

            }).then(res=>res.data).then(res=>{
                console.log(res)
                if(res.code==200){
                    this.goodsDate=res.data
                }else{
                    alert('获取数据失败')
                }

            })
        },
        getStorage(id){
            const storage = this.storageData.find(x => x.id===id);
            return storage?storage.name:'null';
        },
        getGoodstype(row){
            const goodstype = this.goodsDate.find(x => x.id===id);
            return goodstype?goodstype.name:'null';
        },
    //     使用formatStorage(row)也可以
        formatStorage(row){
            let temp = this.storageData.find(item=>{
                return item.id == row.storage
            })
            // temp不为空
            return temp&&temp.name
        },
        formatGoods(row){
            let temp = this.goodsDate.find(item=>{
                return item.id == row.goodstype
            })
            // temp不为空
            return temp&&temp.name
        },
        setFormRecord(){
            this.$nextTick(()=>{
                //赋值到表单
                this.formRecord.goods = this.currentRow.id+'';
                this.formRecord.goodsname = this.currentRow.name;
                this.formRecord.adminId = this.user.id;
            })
        },
        inGoods(){ // 先判断是否选中，选中后在弹出表单
           if(!this.currentRow || !this.currentRow.id){
                alert("未选择货品")
                return ;
           }
            this.outerVisible=true
            this.$nextTick(()=>{
                this.resetForm()
            })
            this.formRecord.action = '1';
            this.setFormRecord()
        },
        outGoods(){ // 先判断是否选中，选中后在弹出表单
            if(!this.currentRow || !this.currentRow.id){
                alert("未选择货品")
                return ;
            }
            this.outerVisible=true
            this.$nextTick(()=>{
                this.resetForm()
            })
            this.formRecord.action = '2';
            this.setFormRecord()
        },
        handleInGoodsCurrentChange(val){
            this.currentRow = val
        },
        doInGoods(){ // 先入库货品，在增加记录
            this.$axios.post(this.$httpUrl+'/goods/inGoods',this.formRecord).then(res=>res.data).then(res=>{
                console.log(res)
                if(res.code==200){
                    this.$message({
                        message: '操作成功！',
                        type: 'success'
                    });
                    this.outerVisible = false
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
        selectUser(){
            this.innerVisible=true
        },
        doSelectUser(val){
            this.tempUser = val;
        },
        confirmUser(){
          this.formRecord.userid = this.tempUser.id;
          this.formRecord.username = this.tempUser.name;

          this.innerVisible = false
        },
    },
    beforeMount() {
        this.loadPost();
        this.loadStorage();
        this.loadGoodstype();
    }
}
</script>

<style scoped>

</style>