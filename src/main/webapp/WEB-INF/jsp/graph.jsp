<!DOCTYPE html>
<%@ page language="java" isThreadSafe="true" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>NoteGraph</title>

    <link rel="stylesheet" href="http://47.95.115.246/font-awesome-4.7.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css">

    <!-- ECharts单文件引入 -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/echarts/4.2.1/echarts.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>

<div class="container-fluid">
    <!-- 菜单栏 -->
    <div class="row">
        <div class="btn-group" role="group" aria-label="Basic example">
            <button type="button" class="btn btn-primary" data-toggle="tooltip" data-placement="bottom" title="新建笔记" id="new_button">
                <i class="fa fa-plus" aria-hidden="true"></i> 新建笔记
            </button>
        </div>
    </div>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="graph"></div>
</div>

<script type="text/javascript">
    $('#graph').height($(window).height() - 50);

    var myChart = echarts.init(document.getElementById('graph'));
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
        backgroundColor: '#ffffff',
        series: [
            {
                name: 'Node Map',
                type: 'graph',
                layout: 'force',
                data: graph.nodes,
                links: graph.links,
                roam: true,
                draggable: true,
                force: {
                    initLayout: 'circular',
                    repulsion: 1500,
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
                    frontFamily: 'Microsoft YaHei',
                    color: '#000000',
                    align: 'center'
                },
                edgeLabel: {
                    show: true,
                    position: 'middle',
                    color: '#000000',
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
    myChart.setOption(option, true);
    myChart.on('click', function (params) {
        if (params.dataType == 'node') {
            window.open(params.data.href);
        }
    });

    $("#new_button").bind('click', function () {
        window.open("/note");
    });

    $(window).resize(function () {          //当浏览器大小变化时
        $('#graph').height($(window).height() - 50);
        myChart.resize();
    });
</script>
</body>
</html>