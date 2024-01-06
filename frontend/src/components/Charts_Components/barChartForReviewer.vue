<template>
    <div id="myChart4" :style="{width: '100%', height: '500%'}"/>
    <h2>评论随时间分布图</h2>
</template>

<script>
import * as echarts from 'echarts';
import {ref} from 'vue';

const TimeGranularity = ref([]);
const max = ref(0);
const num = ref(0);
const posNum = ref(0);
const negNum = ref(0);

export default {
    mounted() {
        this.drawLine();
        const myChart4 = echarts.init(document.getElementById('myChart4'))
        window.addEventListener("click", ()=>myChart4.resize())
    },
    methods: {
        drawLine() {
            const myChart4 = echarts.init(document.getElementById('myChart4'))
            var option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        crossStyle: {
                            color: '#999'
                        }
                    }
                },
                toolbox: {
                    feature: {
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                legend: {
                    data: ['Positive', 'Negative', 'Total']
                },
                xAxis: [
                    {
                        type: 'category',
                        data: TimeGranularity.value,
                        axisPointer: {
                            type: 'shadow'
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: 'numberOfIssue',
                        min: 0,
                        max: Math.ceil(max.value / 4) * 4,
                        interval: Math.ceil(max.value / 4),
                        axisLabel: {
                            formatter: '{value} 个'
                        }
                    },
                    {
                        type: 'value'
                    }

                ],

                series: [
                    {
                        name: 'Positive',
                        type: 'bar',
                        tooltip: {
                            valueFormatter: function (value) {
                                return value + ' 个';
                            }
                        },
                        data: posNum.value
                    },
                    {
                        name: 'Negative',
                        type: 'bar',
                        tooltip: {
                            valueFormatter: function (value) {
                                return value + ' 个';
                            }
                        },
                        data: negNum.value
                    },
                    {
                        name: 'Total',
                        type: 'line',
                        yAxisIndex: 1,
                        tooltip: {
                            valueFormatter: function (value) {
                                return value + ' 个';
                            }
                        },
                        data: num.value
                    }
                ]
            };

            myChart4.setOption(option);
            window.addEventListener('resize', function () {
                myChart4.resize();
            })
        },
        getdata(data) {

            TimeGranularity.value = data.map((value) => value.yearMonth);
            console.log("$$$$$$$$$$$$$$$$$$$");
            console.log(TimeGranularity.value);
            num.value = data.map((value) => value.num)
            posNum.value = data.map((value) => value.posNum);
            negNum.value = data.map((value) => value.negNum);
            max.value = data.sort((a, b) => b.num - a.num)[0].num;
            this.drawLine();
        }
    }
}
</script>

<style scoped>

</style>