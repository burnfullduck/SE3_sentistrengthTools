<template>
    <div class="bd">
        <div class="background"></div>
        <div class="container">
            <header class="blog-header py-3">
                <div class="row flex-nowrap justify-content-between align-items-center">
                    <!--<div class="col-4 d-flex justify-content-end align-items-center" />-->
                    <div class="text-center" style="margin: 0 auto">
                        <h1 class="cool-text neon" style="margin: 1em 0 1em 0">SentiStrength Analysis Tool</h1>
                    </div>

                    <a class="col-1 button" href="/" style="color: #ffffff; text-decoration: none; font-size: 1.1em; float: right;">
                        <p style="margin: 0 0 0 0.3em;">返回首页</p>
                    </a>
                </div>
            </header>

            <el-tabs v-model="activeName" type="card" @tab-click="handleClick" class="api jumbotron p-3 p-md-5 text-black rounded" style="background-color: rgba(255, 255, 255, 60%); ">
                <el-tab-pane label="查看总表" name="first">
                    <div class="api jumbotron p-3 p-md-5 text-black rounded back-white">
                        <h3 class="md-3" style="color: #000;display:block;margin:0 auto">1.扇形图显示各情绪值占比</h3>
                        <br/>
                        <AngleDiagram></AngleDiagram>
                    </div>

                    <div class="jumbotron p-3 p-md-5 text-black rounded back-dark">
                        <h3 class="md-3" style="color: #fff;display:block;margin:0 auto">2.按时间粒度查看总数据</h3>
                        <br/>
                        <GranularityList v-if="totalSignal2" :commentData="totalData2"></GranularityList>
                    </div>

                    <div class="api jumbotron p-3 p-md-5 text-black rounded back-white">
                        <h3 class="md-3" style="color: #000;display:block;margin:0 auto">3.总数据(按提交顺序，每页50条)</h3>
                        <br/>
                        <CommonList v-if="totalSignal1" :commentData="totalData1"></CommonList>
                    </div>
                </el-tab-pane>

                <el-tab-pane label="按评审人查看" name="second">
                    <div class="jumbotron p-3 p-md-5 text-black rounded back-white" style="display:block;margin:0 auto">
                        <h3 class="md-3" style="color: #000">1.请输入需要查看的评审者姓名，返回no data说明不存在</h3>
                        <input v-model="subName" placeholder="输入需要查看的评审者姓名" style="width: 40%">
                        <button @click="subNameChange">查找</button>
                        <div style="color: whitesmoke">{{ responseText }}</div>
                        <br/>
                    </div>

                    <div class="api jumbotron p-3 p-md-5 text-black rounded back-dark">
                        <h3 class="md-3" style="color: #fff;display:block;margin:0 auto">2.该评审者的所有评论</h3>
                        <br/>
                        <CommonList v-if="nameSignal1" :commentData="nameData1"></CommonList>
                    </div>

                    <div class="jumbotron p-3 p-md-5 text-black rounded back-white">
                        <h3 class="md-3" style="color: #000;display:block;margin:0 auto">3.按时间粒度查看</h3>
                        <br/>
                        <GranularityList v-if="nameSignal2" :commentData="nameData2"></GranularityList>
                        <br/>
                        <barChartForReviewer ref="barChart"></barChartForReviewer>
                    </div>

                </el-tab-pane>

                <el-tab-pane label="按日期查看" name="third">
                    <div class="jumbotron p-3 p-md-5 text-black rounded back-dark">
                        <div class="demo-date-picker">
                            <div class="block">
                                <h3 class="md-3" style="color: #fff">1.选择时间段（2022-5-1到2023-5-31）并查看，默认为全时间</h3>
                                <br/>
                                <el-date-picker
                                        v-model="dateRange"
                                        type="daterange"
                                        range-separator="To"
                                        start-placeholder="Start date"
                                        end-placeholder="End date"
                                        :size="size"
                                        @change="CalenderChange"
                                        :disabled-date="disabledDate"
                                />
                            </div>
                        </div>
                        <br/>
                        <CommonList v-if="timeSignal" :commentData="timeData"></CommonList>
                    </div>
                    <div class="api jumbotron p-3 p-md-5 text-black rounded back-white">
                        <lineChartForDate></lineChartForDate>
                    </div>
                </el-tab-pane>

                <el-tab-pane label="查看Aspect数据" name="fourth">
                    <div class="jumbotron p-3 p-md-5 text-black rounded back-white">
                        <h3 class="md-3" style="color: #000;display:block;margin:0 auto">情绪方面分布</h3>
                        <br/>
                        <AspectAnalysis></AspectAnalysis>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
    <i style="color: #FFFFFF;font-size: 16px;left: 50%;position: absolute;transform: translateX(-50%); padding: 5em 0 8em 0">Copyright
        &#169; 2023 | RainbowSix NJUSE | Base on Nginx and Vue3</i>
</template>


<style>
.back-dark {
    background-color: rgba(105, 105, 105, 80%);
    transition: all 0.2s linear;
}

.back-dark:hover {
    background-color: rgba(0, 0, 0, 80%);
    transition-timing-function: ease-in-out;
}

.back-white {
    background-color: rgba(255, 255, 255, 60%);
    transition: all 0.2s linear;
}

.back-white:hover {
    background-color: rgba(255, 255, 255, 80%);
    transition-timing-function: ease-in-out;
}

.bd {
    overflow-y: auto;
}

.background {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: url("@/assets/nightCity.jpg");
    background-size: cover;
    background-attachment: fixed;
    opacity: 1;
    filter: blur(8px);
    z-index: -1;
}

.demo-date-picker {
    display: flex;
    width: 100%;
    padding: 0;
    flex-wrap: wrap;
}

.demo-date-picker .block {
    padding: 30px 0;
    text-align: center;
    border-right: solid 1px var(--el-border-color);
    flex: 1;
}

.demo-date-picker .block:last-child {
    border-right: none;
}

.demo-date-picker .demonstration {
    display: block;
    color: var(--el-text-color-secondary);
    font-size: 14px;
    margin-bottom: 20px;
}
</style>

<script>
import CommonList from "@/components/List_Components/CommonList.vue";
import GranularityList from "@/components/List_Components/GranularityList.vue";
import AngleDiagram from "@/components/Charts_Components/AngleDiagram.vue";
import AspectAnalysis from "@/components/Charts_Components/AspectAnalysis.vue";
import lineChartForDate from "@/components/Charts_Components/lineChartForDate.vue";
import barChartForReviewer from "@/components/Charts_Components/barChartForReviewer.vue";

// import axios from "axios";
import axios from "axios";

export default {
    name: 'AnalysisAPI',
    components: {CommonList, GranularityList, AngleDiagram, AspectAnalysis, lineChartForDate, barChartForReviewer},
    data() {
        return {
            activeName: 'first',
            totalSignal1: false,
            totalSignal2: false,
            timeSignal: false,
            nameSignal1: false,
            nameSignal2: false,
            totalData1: [],
            totalData2: [],
            timeData: [],
            nameData1: [],
            // nameData2 ： 按照评审姓名查询的频率数据
            nameData2: [],
            dateRange: '',
            subName: '',
        };
    },
    methods: {
        handleClick(tab, event) {
            console.log(tab, event);
        },

        async CalenderChange() {
            this.timeSignal = false
            let fd = new FormData();
            fd.append("startDate", this.newDate(this.dateRange[0].toString()));
            fd.append("endDate", this.newDate(this.dateRange[1].toString()))
            axios.post('http://124.221.102.208:8082/analysis/getAllDataByDate', fd, {
                headers: {'Content-Type': 'multipart/form-data'},//定义内容格式,很重要
                timeout: 20000,
            }).then(response => {
                this.timeData = response.data;
                this.timeSignal = true
            }).catch(err => {
                console.log(err);
            })
        },
        subNameChange() {
            this.nameSignal1 = false
            this.nameSignal2 = false
            let fd = new FormData();
            fd.append("name", this.subName);

            axios.post('http://124.221.102.208:8082/analysis/getAllDataByName', fd, {
                headers: {'Content-Type': 'multipart/form-data'},//定义内容格式,很重要
                timeout: 20000,
            }).then(response => {
                this.nameData1 = response.data;
                this.nameSignal1 = true
            }).catch(err => {
                console.log(err);
            })

            axios.post('http://124.221.102.208:8082/analysis/dataEachMonthByName', fd, {
                headers: {'Content-Type': 'multipart/form-data'},//定义内容格式,很重要
                timeout: 20000,
            }).then(response => {
                this.nameData2 = response.data;
                this.nameSignal2 = true;
                this.$refs.barChart.getdata(this.nameData2);
            }).catch(err => {
                console.log(err);
            })

        },

        newDate(time) {
            let date = new Date(time)
            let y = date.getFullYear()
            let m = date.getMonth() + 1
            m = m < 10 ? '0' + m : m
            let d = date.getDate()
            d = d < 10 ? '0' + d : d
            return y + '-' + m + '-' + d
        },
        disabledDate(time) {
            let from = new Date(2022, 4, 1, 0, 0, 0, 0);
            let end = new Date(2023, 4, 31, 0, 0, 0, 0);
            return time.getTime() > end || time.getTime() < from
            // -8.64e7
        }

    },
    created: function () {

        axios.get('http://124.221.102.208:8082/analysis/getData', {
            timeout: 20000,
        }).then(response => {
            this.totalData1 = response.data;
            this.totalSignal1 = true
        }).catch(err => {
            console.log(err);
        })

        axios.get('http://124.221.102.208:8082/analysis/dataEachMonth', {
            timeout: 20000,
        }).then(response => {
            this.totalData2 = response.data;
            this.totalSignal2 = true
        }).catch(err => {
            console.log(err);
        })

    }

};

</script>