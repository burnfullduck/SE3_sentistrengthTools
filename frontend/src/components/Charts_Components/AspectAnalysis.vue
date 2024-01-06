<template>
    <div id="myChart2" :style="{width: '90%', height: '500%'}"/>
    <br/>
    <ol style="word-break: break-all">结合统计数据senti值分析可得下面三条结论：
        <li>
            issue评论种类比较纷杂。从前端代码到后端查询和数据处理，再到数据库的连接，<br />
            底层有安装配置、更新状态问题，宏观上也有嵌入与集成相关的问题。
        </li>
        <li>
            从数量上来看，在该项目这一版本下，用户最关心的还是提出项目相关问题（共141条，占32.6%）和<br />
            问题解决分享（共79条，占18.2%），剩余相关汇聚项分布较为均匀。
        </li>
        <li>
            从情绪值上来看，“感谢与喜爱”、“问题解决”、“文档与支持”有很强的积极情绪，“问题反馈”、“导入导出格式”、“前端代码问题”则负面情绪居多，<br />
            剩余的例如“需求与建议”、“数据库连接与访问”、“可视化图表”等,则积极评论和消极评论数量相近。
            因为不能百分百保证样本的完美性，为确保实验的严谨，<br />
            我们视这些标签的评论为接近中性（根据统计计算各标签所有senti值的平均值，这些剩余标签得均值都在0到1之间，且极差不超过0.4）。
        </li>
    </ol>
</template>

<script>
import * as echarts from 'echarts';

export default {
    mounted() {
        const myChart2 = echarts.init(document.getElementById('myChart2'));
        this.drawLine(myChart2);
        window.onclick = () => myChart2.resize();
    },
    methods: {
        drawLine(c) {
            var option = {
                color: ["#1089E7", "#33CCFF", "#5470c6", "#56D0E3", "#66FF33", "#75bedc", "#8B78F6", "#91cd77", "#9999CC", "#CCFF33", "#ef6567", "#F57474", "#F8B448", "#fc8251"],
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
                        color: "rgba(0,0,0,.5)",
                        fontSize: "13"
                    }
                },
                series: [{
                    name: '情绪方面占比',
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
                        value: 6,
                        name: "感谢与喜爱"
                    },
                        {
                            value: 141,
                            name: "问题反馈"
                        },
                        {
                            value: 79,
                            name: "问题解决"
                        },
                        {
                            value: 33,
                            name: "需求与建议"
                        },
                        {
                            value: 12,
                            name: "文档与支持"
                        },
                        {
                            value: 30,
                            name: "安装和配置"
                        },
                        {
                            value: 26,
                            name: "导入导出格式"
                        },
                        {
                            value: 25,
                            name: "数据库连接与访问"
                        },
                        {
                            value: 16,
                            name: "审核与关闭"
                        },
                        {
                            value: 31,
                            name: "可视化与图表"
                        },
                        {
                            value: 11,
                            name: "嵌入与集成"
                        },
                        {
                            value: 5,
                            name: "查询与数据处理"
                        },
                        {
                            value: 4,
                            name: "更新与状态"
                        },
                        {
                            value: 14,
                            name: "前端代码问题"
                        }
                    ]
                }]
            };
            c.setOption(option);
            window.addEventListener('resize', function () {
                c.resize();
            })
        }
    }
}
</script>

<style scoped>

</style>