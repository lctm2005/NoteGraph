<!DOCTYPE html>
<%@ page language="java" isThreadSafe="true" pageEncoding="gbk" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>NoteForceView</title>
    <!-- ECharts���ļ����� -->
</head>
<body>
<!-- ΪECharts׼��һ���߱���С����ߣ���Dom -->
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
            'echarts/chart/line',   // �����������ͼ�����趯̬�����л����ܣ�������ͬʱ������Ӧͼ��
            'echarts/chart/bar',
            'echarts/chart/force'
        ],
        function (ec) {
            var myChart = ec.init(document.getElementById('main'));
            var option = {
                title: {
                    text: '֪ʶ��ͼ',
                    subtext: '������Evernote',
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
                        name: "�ʼ�",
                        symbol: 'rectangle',
                        symbolSize: [60, 14],
                        draggable: false,
                        ribbonType: false,
                        categories: [
//                            {
//                                name: '����'
//                            },
//                            {
//                                name: '����'
//                            },
//                            {
//                                name: '����'
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
                                    // textStyle: null      // Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
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