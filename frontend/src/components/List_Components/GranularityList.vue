<template>
    <el-table
            :data="tableData"
            style="width:100%"
            border
            stripe
            ref="tableRef"
	>
        <el-table-column
                prop="yearMonth"
                label="时间段"
                width="350">
        </el-table-column>
        <el-table-column
                prop="num"
                label="总文本数"
                width=300>
        </el-table-column>
        <el-table-column
                prop="posNum"
                label="积极文本数（占比）"
                width=250>
        </el-table-column>
        <el-table-column
                prop="negNum"
                label="消极文本数（占比）"
                width=250>
        </el-table-column>
    </el-table>
</template>

<script>


export default {
    data() {
        return {
            tableData: [{
                Duration: '2012-01-03',
                TotalNum: '89',
                PosNum: '16 (27.8%)',
                NegNum: '19 (34.8%)',
            },{
                Duration: '2012-01-03',
                TotalNum: '89',
                PosNum: '16 (27.8%)',
                NegNum: '19 (34.8%)',
            }, {
                Duration: '2012-01-03',
                TotalNum: '89',
                PosNum: '16 (27.8%)',
                NegNum: '19 (34.8%)',
            }, {
                Duration: '2012-01-03',
                TotalNum: '89',
                PosNum: '16 (27.8%)',
                NegNum: '19 (34.8%)',
            }, {
                Duration: '2012-01-03',
                TotalNum: '89',
                PosNum: '16 (27.8%)',
                NegNum: '19 (34.8%)',
            },{
                Duration: '2012-01-03',
                TotalNum: '89',
                PosNum: '16 (27.8%)',
                NegNum: '19 (34.8%)',
            }, {
                Duration: '2012-01-03',
                TotalNum: '89',
                PosNum: '16 (27.8%)',
                NegNum: '19 (34.8%)',
            }],
            listInfo:{
                page:1
                ,
                all_data:[]
                ,
                num:50
            }

        }
    },
    props:["commentData"],
    methods:{
        handleCurrentChange(e){
            this.listInfo.page = e
            if(this.$refs.tableRef !== undefined){
                this.$refs.tableRef.setScrollTop(0);
            }
            this.tableData =  this.listInfo.all_data.filter((item, index)=>
                index<this.listInfo.page * 50 &&
                index>=50*(this.listInfo.page-1))

        }

    },
    created: async function() {
        this.listInfo.all_data = this.commentData
        this.handleCurrentChange(1)
        this.listInfo.num = this.listInfo.all_data.length
    }


}

</script>

<style scoped>

</style>