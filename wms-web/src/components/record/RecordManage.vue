<template>
    <div>
        <div style="margin-bottom: 5px;">
            <el-input v-model="goodsname" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 200px;"
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
        </div>
        <el-table :data="tableData"
                  :header-cell-style="{ background: '#f2f5fc', color: '#555555' }"
                  border
        >
            <el-table-column prop="id" label="ID" width="60">
            </el-table-column>
            <el-table-column prop="goodsname" label="物品名" width="180">
            </el-table-column>
            <el-table-column prop="storagename" label="仓库" width="180">
            </el-table-column>
            <el-table-column prop="goodstypename" label="分类" width="180">
            </el-table-column>
            <el-table-column prop="adminname" label="操作人" width="180">
            </el-table-column>
            <el-table-column prop="username" label="申请人" width="180">
            </el-table-column>
            <el-table-column prop="count" label="数量" width="180">
            </el-table-column>
            <el-table-column prop="createtime" label="操作时间" width="180">
            </el-table-column>
            <el-table-column prop="remark" label="备注">
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
    </div>
</template>

<script>
export default {
    name: "RecordManage",
    data() {
        return {
            user: JSON.parse(sessionStorage.getItem('CurUser')),
            tableData: [],
            storageData: [],
            goodsDate: [],
            pageSize:10,
            pageNum:1,
            total:0,
            goodstype:'',
            storage:'',
            goodsname:'',
            centerDialogVisible:false,
        }
    },
    methods:{
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
            this.storage= '';
            this.goodstype= '';
        },
        loadPost() {
            this.$axios.post(this.$httpUrl + '/record/listPageLR', {
                pageSize: this.pageSize,
                pageNum: this.pageNum,
                params: {
                    goodsname: this.goodsname,
                    storage: this.storage+'',
                    goodstype: this.goodstype+'',
                    roleId: this.user.roleId+'',
                    userid: this.user.id+''
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