<template>
    <div id="myChart3" :style="{width: '100%', height: '500%'}"/>
    <h2>评论随时间分布图</h2>
</template>

<script>
import * as echarts from 'echarts';

export default {
    mounted() {
        this.drawLine()
        const myChart3 = echarts.init(document.getElementById('myChart3'))
        window.addEventListener("click", ()=>myChart3.resize())
    },
    methods: {
        drawLine() {
            const myChart3 = echarts.init(document.getElementById('myChart3'))
            var option = {
                title: {
                    text: '积极与消极评论随时间分布图'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['positive', 'negative']
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },

                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['2022-5', '2022-6', '2022-7', '2022-8', '2022-9', '2022-10', '2022-11', '2022-12', '2023-1', '2023-2', '2023-3', '2023-4', '2022-5']
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name: 'Positive',
                        type: 'line',
                        stack: 'Total',
                        data: [19, 22, 28, 28, 28, 19, 24, 27, 35, 28, 57, 42, 35]
                    },
                    {
                        name: 'Negative',
                        type: 'line',
                        stack: 'Total',
                        data: [16, 29, 20, 33, 25, 23, 21, 27, 21, 33, 21, 29, 39]
                    }
                ]
            };

            myChart3.setOption(option);
            window.addEventListener('resize', function () {
                myChart3.resize();
            })
        },
        renew() {
            const myChart3 = echarts.init(document.getElementById('myChart3'));
            window.addEventListener('resize', function () {
                myChart3.resize();

            })
        }
    }
}
</script>

<style scoped>

</style>