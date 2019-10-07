<!DOCTYPE html>
<%@ page language="java" isThreadSafe="true" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>NoteGraph</title>

    <!-- Font awesome -->
    <link rel="stylesheet" href="http://47.95.115.246/font-awesome-4.7.0/css/font-awesome.min.css"/>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
    <!-- Toastr -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet"/>

    <!-- ECharts -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/echarts/4.2.1/echarts.min.js"></script>
    <!-- JQuery -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- Jqpaginator -->
    <script src="http://jqpaginator.keenwon.com/jqPaginator.min.js"></script>
    <!-- Axios -->
    <script src="https://cdn.staticfile.org/axios/0.18.0/axios.min.js"></script>
    <!-- Toastr -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
</head>
<body>

<div class="container-fluid">
    <nav class="navbar navbar-expand-lg ">
        <a class="navbar-brand" href="#">知识地图</a>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <button type="button" class="btn btn-success" data-toggle="tooltip" data-placement="bottom"
                            title="新建笔记"
                            id="new_button">
                        <i class="fa fa-plus-circle" aria-hidden="true"></i> 新建笔记
                    </button>
                    <button type="button" class="btn btn-secondary" data-toggle="tooltip"
                            data-placement="bottom" title="编辑笔记"
                            id="edit_button">
                        <i class="fa fa-pencil" aria-hidden="true"></i> 编辑笔记
                    </button>
                    <button type="button" class="btn btn-danger" data-toggle="tooltip" data-placement="bottom"
                            title="删除笔记"
                            id="delete_button">
                        <i class="fa fa-trash" aria-hidden="true"></i> 删除笔记
                    </button>
                    <button type="button" class="btn btn-secondary" data-toggle="tooltip" data-placement="bottom"
                            title="展开笔记"
                            id="expand_button">
                        <i class="fa fa-expand" aria-hidden="true"></i> 展开笔记
                    </button>
                </li>
            </ul>
            <div class="form-inline my-2 my-lg-0" id="search_area">
                <input class="form-control mr-sm-2" type="search" id="search_input" placeholder="Search"
                       aria-label="Search">
                <button class="btn btn-success my-2 my-sm-0" id="search_button" type="button">搜索</button>
            </div>
        </div>
    </nav>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="graph"></div>
    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-end" id="pagination">
        </ul>
    </nav>
</div>

<script type="text/javascript">

    /**
     * 初始化提示框
     */
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "toast-top-center",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "3000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }

    /**
     * 自适应宽高
     */
    function adjustGraphWithHeight() {
        $('#graph').height($(window).height() - 150);
        $('#graph').width($(window).width() - 30);
    }

    /**
     * 初始化地图高度
     */
    adjustGraphWithHeight();

    /**
     * 创建地图
     */
    var myChart = echarts.init(document.getElementById('graph'));

    /**
     * 分页查询
     */
    function pagination(pageSize, currentPage, totalPages, totalCounts) {
        var init = true;
        $('#pagination').jqPaginator({
            totalPages: totalPages,
            totalCounts: totalCounts,
            visiblePages: 10,
            currentPage: currentPage,
            pageSize: pageSize,
            prev: '<li class="page-item"><a class="page-link" href="javascript:void(0);">Previous</a></li>',
            next: '<li class="page-item"><a class="page-link" href="javascript:void(0);">Next</a></li>',
            page: '<li class="page-item"><a class="page-link" href="javascript:void(0);">{{page}}</a></li>',
            onPageChange: function (num, type) {
                if (init) {
                    init = false;
                } else {
                    loadData($('#search_input').val(), num - 1);
                }
            }
        });
    }

    /**
     * 加载数据
     */
    function loadData(title, num) {
        num = num == null ? 0 : num;
        axios.get('/api/note/search/findByTitleContains?title=' + title + '&page=' + num)
            .then(response => {
            var page = response.data.page;
            if (page.totalElements == 0) {
                loadGraph(null, null);
                pagination(page.size, page.number + 1, page.totalPages, page.totalElements);
                return;
            }
            var noteResources = response.data._embedded.noteResources;
            var noteIds = noteResources.map(function (note) {
                return note.noteId;
            });
            // {"noteId":"a51ca953-e22d-4a95-8e84-1686cf570347","name":"Neo4J","href":"/note/a51ca953-e22d-4a95-8e84-1686cf570347","value":10}
            var nodes = noteResources.map(function (note) {
                var node = note;
                node.name = note.noteId;
                node.symbolSize = [55, 55];
                node.value = 10;
                return node;
            });
            axios.post('/api/link/search/findByStartInOrEndIn', noteIds)
                .then(response => {
                    // {source : '丽萨-乔布斯', target : '乔布斯', weight : 1, name: '女儿'}
                    var links = response.data.map(function (data) {
                        var link = data;
                        link.source = data.start.noteId;
                        link.target = data.end.noteId;
                        link.name = data.title;
                        return link;
                    });
                    loadGraph(nodes, links);
                    pagination(page.size, page.number + 1, page.totalPages, page.totalElements);
                }).catch(response => error(response));
        }).catch(response => error(response));
    }

    var graphNodes;
    var graphLinks;
    /**
     * 加载地图
     */
    function loadGraph(nodes, links) {
        graphNodes = nodes;
        graphLinks = links;
        myChart.clear();
        if (null == nodes || null == links) {
            return;
        }
        var option = {
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
                    data: nodes,
                    links: links,
                    roam: false,
                    draggable: true,
                    backgroundColor: '#f5ff10',
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
                        align: 'center',
                        formatter: function (params) {
                            return params.data.title;
                        }
                    },
                    edgeLabel: {
                        show: true,
                        position: 'middle',
                        color: '#000000',
                        align: 'center',
                        formatter: function (params) {
                            return params.data.title;
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
    }

    /**
     * 初始化地图
     */
    loadData("", null);

    /**
     * 地图自适应浏览器高度
     */
    $(window).resize(function () {
        adjustGraphWithHeight();
        myChart.resize();
    });

    /**
     * 选中节点
     */
    myChart.on('click', function (params) {
        if (params.dataType == 'node') {
            selectEle(params);
        }
    });

    /**
     * 选中高亮，再次选中取消高亮
     */
    var emptyNode = {
        noteId: null,
        name: null,
        href: null,
        value: null
    };

    var selectNode = emptyNode;


    function selectEle(params) {
        let options = myChart.getOption()
        let nodesOption = options.series[0].data
        let name = params.data.name
        for (let m in nodesOption) {
            nodesOption[m].itemStyle = {}
            if (nodesOption[m].name === name) {
                console.log(nodesOption[m])
                if (name === selectNode.name) {
                    // 再次选中，取消选择了
                    normalize(nodesOption[m]);
                    selectNode = emptyNode;
                } else {
                    highlight(nodesOption[m]);
                    selectNode = params.data;
                }
            } else {
                normalize(nodesOption[m]);
            }
        }
        myChart.setOption(options);
    }

    function normalize(nodeOption) {
        nodeOption.label = {color: '#000000'}
        nodeOption.itemStyle.color = '#f09662'
        nodeOption.itemStyle.borderColor = '#ea6712'
        nodeOption.itemStyle.shadowBlur = 0
    }

    function highlight(nodeOption) {
        nodeOption.label = {color: '#000000'}
        nodeOption.itemStyle.color = '#02b610'
        nodeOption.itemStyle.borderColor = '#1c9156'
        nodeOption.itemStyle.shadowColor = '#2cff77'
        nodeOption.itemStyle.shadowBlur = 10
    }

    function error(response) {
        console.log(response);
        toastr.error(response.data.message);
    }

    function toNodes(noteResources) {
        // {"noteId":"a51ca953-e22d-4a95-8e84-1686cf570347","name":"Neo4J","href":"/note/a51ca953-e22d-4a95-8e84-1686cf570347","value":10}
        return noteResources.map(function (note) {
            var node = note;
            node.name = note.noteId;
            node.symbolSize = [55, 55];
            node.value = 10;
            return node;
        });
    }

    function toLinks(noteLinkResources) {
        // {source : '丽萨-乔布斯', target : '乔布斯', weight : 1, name: '女儿'}
        return noteLinkResources.map(function (data) {
            var link = data;
            link.source = data.start.noteId;
            link.target = data.end.noteId;
            link.name = data.title;
            link.id = data.linkId;
            return link;
        });
    }

    /**
     * 新建笔记
     */
    $("#new_button").bind('click', function () {
        axios.post('/api/note', {
            title: '无标题笔记',
            content: ''
        }).then(response => {
            window.open(response.data._links.edit.href);
        location.reload();
    }).
        catch(response => {
            console.log(response);
        toastr.error('创建失败');
    })
        ;
    });

    /**
     * 编辑笔记
     */
    $("#edit_button").bind('click', function () {
        if (emptyNode == selectNode) {
            toastr.warning('请选择节点');
            return;
        }
        window.open(selectNode._links.edit.href);
    });

    /**
     * 删除笔记
     */
    $("#delete_button").bind('click', function () {
        if (emptyNode == selectNode) {
            toastr.warning('请选择节点');
            return;
        }
        toastr["info"]("<div class=\"btn-group\" ><button id=\"confirm-delete-button\", type=\"button\" class=\"btn btn-primary\" onclick=\"deleteNote()\">Yes</button><button type=\"button\" class=\"btn btn-light\">No</button></div>", "确认是否删除");
    });

    /**
     * 执行删除笔记
     */
    function deleteNote() {
        axios.delete('/api/note/' + selectNode.noteId).then(response => {
            location.reload();
    }).
        catch(response => {
            console.log(response);
        toastr.error('删除失败');
    })
        ;
    }

    /**
     * 检索笔记
     */
    $("#search_button").bind('click', function () {
        loadData($('#search_input').val(), null);
    });

    /**
     * 展开笔记
     */
    $("#expand_button").bind('click', function () {
        axios.get('/api/note/' + selectNode.noteId + '/neighbours').then(response => {
                var noteResources = response.data;
                var noteIds = noteResources.map(function (note) {
                    return note.noteId;
                });
                noteIds.push(selectNode.noteId);
                var nodes = toNodes(noteResources);
                axios.post('/api/link/search/findByStartInOrEndIn', noteIds)
                    .then(response=>{
                        nodes.forEach(function (node) {
                            var exist = false;
                            graphNodes.forEach(function (existNode) {
                                if(node.name == existNode.name) {
                                    exist = true;
                                }
                            });
                            if(!exist) {
                                graphNodes.push(node);
                            }
                        });
                        var links = toLinks(response.data);
                        links.forEach(function (link) {
                            var exist = false;
                            graphLinks.forEach(function (existLink) {
                                if(link.id == existLink.id) {
                                    exist = true;
                                }
                            });
                            if(!exist) {
                                graphLinks.push(link);
                            }
                        });

                        var options = myChart.getOption();
                        options.series[0].data = graphNodes;
                        options.series[0].links = graphLinks;
                        myChart.setOption(options)
                    }).catch(response=>error(response));
        }).catch(response=>error(response));
    });

    /**
     * 绑定
     */
    $('#search_area').keydown(function (e) {
        if (e.keyCode === 13) {
            loadData($('#search_input').val(), null);
        }
    });

</script>
</body>
</html>