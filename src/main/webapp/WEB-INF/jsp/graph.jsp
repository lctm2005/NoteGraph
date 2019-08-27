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
<div id="main" style="height:1100px"></div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/echarts/4.2.1/echarts.min.js"></script>
</body>
<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('main'));
    graph =  ${graph};
    graph.nodes.forEach(function (node) {
        node.symbolSize = [55, 55];
    });
    var option = {
        title: {
            text: '知识地图',
            subtext: '数据来Evernote',
            left: 'right',
            top: 'bottom'
        },
        tooltip: {
            trigger: 'item',
            formatter: function (params, ticket, callback) {
                return params.data.name;
            }
        },
        animationDuration: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
            {
                name: 'Les Miserables',
                type: 'graph',
                layout: 'force',
                data: graph.nodes,
                links: graph.links,
                roam: true,
                draggable: true,
                force: {
                    initLayout: 'circular',
                    repulsion: 500,
                    gravity: 0.1,
                    edgeLength: 120
                },
                focusNodeAdjacency: true,
                edgeSymbol: ['none', 'arrow'],
                itemStyle: {
                    color: '#f09662',
                    borderColor: '#ea6712',
                    borderWidth: 2
                },
                label: {
                    show: true,
                    position: 'inside',
                    fontSize: 12,
                    fontStyle: 'normal',
                    color: '#ffffff',

                },
                edgeLabel: {
                    show: true,
                    position: 'middle',
                    color: '#000',
                    align: 'center',
                    formatter: function (params) {
                        return params.data.name;
                    }
                },
                lineStyle: {
                    color: '#a6abb6',
                    width: 1.5,
                    curveness: 0
                },
                emphasis: {
                    lineStyle: {
                        width: 10
                    }
                }
            }
        ]
    }
    myChart.setOption(option);
    myChart.on('click', function (params) {
        if (params.dataType == 'node') {
            window.open(params.data.href);
        }
    });
</script>
</html>