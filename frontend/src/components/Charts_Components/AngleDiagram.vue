<template>
    <div id="myChart" :style="{width: '100%', height: '500%'}"/>
</template>

<script>
import * as echarts from 'echarts';

export default {
    mounted() {
        this.drawLine();
        const myChart = echarts.init(document.getElementById('myChart'))
        window.addEventListener("click", ()=>myChart.resize())
    },
    methods: {
        drawLine() {
            const myChart = echarts.init(document.getElementById('myChart'))
            var option = {
                color: ["#1089E7", "#F57474", "#56D0E3", "#F8B448", "#8B78F6", "#fc8251", "#5470c6", "#91cd77"],
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b}: {c} ({d}%)'
                },
                legend: {
                    bottom: 0,
                    left: 10,
                    // 小图标的宽度和高度
                    itemWidth: 10,
                    itemHeight: 10,
                    // 修改图例组件的文字为 12px
                    textStyle: {
                        color: "rgba(0,0,0,.8)",
                        fontSize: "12"
                    }
                },
                series: [{
                    name: '情绪值分布分布',
                    type: 'pie',
                    // 设置饼形图在容器中的位置
                    center: ["50%", "42%"],
                    // 修改饼形图大小，第一个为内圆半径，第二个为外圆半径
                    radius: ['40%', '60%'],
                    avoidLabelOverlap: false,
                    // 图形上的文字
                    label: {
                        show: false,
                        position: 'center'
                    },
                    // 链接文字和图形的线
                    labelLine: {
                        show: false
                    },
                    data: [{
                        value: 2,
                        name: "情绪值为-3"
                    },
                        {
                            value: 58,
                            name: "情绪值为-2"
                        },
                        {
                            value: 277,
                            name: "情绪值为-1"
                        },
                        {
                            value: 1027,
                            name: "情绪值为0"
                        },
                        {
                            value: 295,
                            name: "情绪值为1"
                        },
                        {
                            value: 91,
                            name: "情绪值为2"
                        },
                        {
                            value: 4,
                            name: "情绪值为3"
                        },
                        {
                            value: 2,
                            name: "情绪值为4"
                        }
                    ]
                }]
            };

            myChart.setOption(option);
            window.addEventListener('resize', function () {
                myChart.resize();
            })
        }
    }
}
</script>

<style scoped>

</style>