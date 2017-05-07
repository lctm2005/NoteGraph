<!DOCTYPE html>
<%@ page language="java" isThreadSafe="true" pageEncoding="gbk" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>NoteForceView</title>
    <!-- ECharts单文件引入 -->
</head>
<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="main" style="height:800px"></div>
<script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
</body>
<script type="text/javascript">


    require.config({
        paths: {
            echarts: 'http://echarts.baidu.com/build/dist'
        }
    });
    require(
        [
            'echarts',
            'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
            'echarts/chart/bar',
            'echarts/chart/force'
        ],
        function (ec) {
            var myChart = ec.init(document.getElementById('main'));
            var option = {
                title: {
                    text: '知识地图',
                    subtext: '数据来Evernote',
                    x: 'right',
                    y: 'bottom'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: function (params, ticket, callback) {
                        return params.data.name;
                    }
                },
                series: [
                    {
                        type: 'force',
                        name: "笔记",
                        symbol: 'rectangle',
                        symbolSize: [60, 14],
                        draggable: false,
                        ribbonType: false,
                        categories: [
//                            {
//                                name: '人物'
//                            },
//                            {
//                                name: '家人'
//                            },
//                            {
//                                name: '朋友'
//                            }
                        ],
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true,
                                    textStyle: {
                                        color: '#fff'
                                    }
                                },
                                nodeStyle: {
                                    color:'#1e90ff',
                                    brushType: 'both',
                                    borderColor: 'rgba(255,215,0,0.4)',
                                    borderWidth: 1
                                },
                                linkStyle: {
                                    type: 'curve'
                                }
                            },
                            emphasis: {
                                label: {
                                    show: false
                                    // textStyle: null      // 默认使用全局文本样式，详见TEXTSTYLE
                                },
                                nodeStyle: {
                                    //r: 30
                                },
                                linkStyle: {}
                            }
                        },
                        useWorker: false,
                        minRadius: 15,
                        maxRadius: 25,
                        gravity: 1.1,
                        scaling: 1.1,
                        roam: 'move',
                        nodes: ${nodes},
                        links: ${links},
                    }
                ]
            }

            function focus(param) {
                var data = param.data;
                if (data.source == undefined || data.target == undefined) {
                    window.open(data.href)
                }
            }

            var ecConfig = require('echarts/config');
            myChart.on(ecConfig.EVENT.CLICK, focus)
            myChart.setOption(option);

        }
    );
</script>
</html>