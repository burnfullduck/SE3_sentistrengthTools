<template>

    <el-button-group>
        <el-button  @click="sortByTime(1)">时间升序</el-button>
        <el-button  @click="sortByTime(-1)">时间降序</el-button>
    </el-button-group>

    <el-button-group>
        <el-button   @click="sortBySenti(1)">情绪值升序</el-button>
        <el-button  @click="sortBySenti(-1)">情绪值降序</el-button>
    </el-button-group>
    <el-table
        :data="tableData"
		style="max-height: 50em; overflow-x: hidden; overflow-y: auto; width: 100%"
        border
        stripe
        ref="tableRef"
        >

        <el-table-column
            prop="issueNo"
            label="提交序号"
            width="150">
        </el-table-column>
        <el-table-column
            prop="textNo"
            label="文本序号"
            width="120">
        </el-table-column>
        <el-table-column
            prop="subName"
            label="提交者"
            width="120">
        </el-table-column>
        <el-table-column
            prop="reviewer"
            label="评审者"
            width="120">
        </el-table-column>
        <el-table-column
            prop="time"
            label="时间"
            width="120">
        </el-table-column>
        <el-table-column
            prop="text"
            label="文本"
            width="400">
        </el-table-column>
        <el-table-column
            prop="senti"
            label="情绪值"
            width="120">
        </el-table-column>
    </el-table>


    <el-pagination
            style=""
            :page-size="50"
            :pager-count="11"
            layout="prev, pager, next"
            :total="listInfo.num"
            ref="pagRef"
            @current-change="handleCurrentChange"
    >
    </el-pagination>

</template>

<script>
    // import axios from "axios";

    export default {
        data() {
            return {
                tableData: [],
                sortInfo:{
                    timeSort:false,
                    sentiSort:true
                },
                listInfo:{
                    page:1
                    ,
                    all_data:[]
                    ,
                    num:50,
                }

            }
        },
        props:["commentData"],
        methods:{
            sortBySenti(e){
                this.listInfo.all_data.sort(function (a, b){
                   return (a.senti - b.senti)*e
                })
                console.log(this.listInfo.all_data)
                this.handleCurrentChange(this.listInfo.page)
            },
            sortByTime(e){
                this.listInfo.all_data = this.listInfo.all_data.sort(function (a, b){
                    if(a.time > b.time){
                        return 1*e
                    }else if(a.time === b.time){
                        return 0
                    }else{
                        return -1*e
                    }


                })
                this.handleCurrentChange(this.listInfo.page)
            },

            handleCurrentChange(e){
                this.listInfo.page = e
                if(this.$refs.tableRef !== undefined){
                    this.$refs.tableRef.setScrollTop(0);
                }
                // console.log(this.listInfo.all_data)
                this.tableData =  this.listInfo.all_data.filter((item, index)=>
                    index<this.listInfo.page * 50 &&
                    index>=50*(this.listInfo.page-1)
                )

            }

        },
        created: function() {
            this.listInfo.all_data = this.commentData
            this.handleCurrentChange(1)
            this.listInfo.num = this.listInfo.all_data.length

    }

}

</script>

<style scoped>

</style>