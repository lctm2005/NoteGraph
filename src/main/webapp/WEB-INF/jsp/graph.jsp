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
    <link href="https://cdn.bootcss.com/font-awesome/5.11.2/css/all.min.css" rel="stylesheet">
    <!-- Bootstrap -->
<%--    <link rel="stylesheet" href="http://notegraph.cn/css/bootstrap.min.css">--%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">

    <!-- Toastr -->
    <link href="http://notegraph.cn/css/toastr.min.css" rel="stylesheet"/>

    <!-- ECharts -->
    <script src="http://notegraph.cn/js/echarts.min.js"></script>
    <!-- JQuery -->
    <script src="http://notegraph.cn/js/jquery.min.js"></script>
    <!-- Jqpaginator -->
    <script src="http://notegraph.cn/js/jq-paginator.min.js"></script>
    <!-- Axios -->
    <script src="http://notegraph.cn/js/axios.min.js"></script>
    <!-- Toastr -->
    <script src="http://notegraph.cn/js/toastr.min.js"></script>
    <!-- Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
</head>
<body>

<div class="container-fluid">
    <div class="row">
        <a class="navbar-brand" href="#">NoteGraph</a>
    </div>
    <!-- 按钮区 -->
    <div class="row">
        <div class="col-md-12">
            <div class="btn-group" role="group" aria-label="1">
                <button type="button" class="btn btn-info" style="margin:5px" data-toggle="tooltip"
                        data-placement="bottom"
                        title="搜索笔记"
                        id="expand_search_button">
                    <i class="fa fa-search" aria-hidden="true"></i>
                </button>
            </div>
            <div class="btn-group" role="group" aria-label="2">
                <button type="button" class="btn btn-info" style="margin:5px" data-toggle="tooltip"
                        data-placement="bottom"
                        title="查看标签"
                        id="label_button">
                    <i class="fa fa-tag" aria-hidden="true"></i>
                </button>
            </div>
            <div class="btn-group" role="group" aria-label="3">
                <button type="button" class="btn btn-info" style="margin:5px" data-toggle="tooltip"
                        data-placement="bottom"
                        title="展开笔记"
                        id="expand_button">
                    <i class="fa fa-expand" aria-hidden="true"></i>
                </button>
            </div>
            <div class="btn-group" role="group" aria-label="4">
                <button type="button" class="btn btn-success" style="margin:5px" data-toggle="tooltip"
                        data-placement="bottom"
                        title="新建笔记"
                        id="new_button">
                    <i class="fa fa-plus-circle" aria-hidden="true"></i>
                </button>
            </div>
            <div class="btn-group" role="group" aria-label="2">
                <button type="button" class="btn btn-primary" style="margin:5px" data-toggle="tooltip"
                        data-placement="bottom"
                        title="编辑笔记"
                        id="edit_button">
                    <i class="fa fa-pencil-alt" aria-hidden="true"></i>
                </button>
            </div>
            <div class="btn-group" role="group" aria-label="2">
                <button type="button" class="btn btn-danger" style="margin:5px" data-toggle="tooltip"
                        data-placement="bottom"
                        title="删除笔记"
                        id="delete_button">
                    <i class="fa fa-trash" aria-hidden="true"></i>
                </button>
            </div>
            <div class="btn-group" role="group" aria-label="2">
                <button type="button" class="btn btn-warning" style="margin:5px" data-toggle="tooltip"
                        data-placement="bottom"
                        title="财报分析"
                        id="financial_button">
                    <i class="fa fa-wallet" aria-hidden="true"></i>
                </button>
            </div>
        </div>
    </div>
    <!-- 搜索展开区 -->
    <div class="row" style="display:none;" id="search_area">
        <div class="col-md-12">
            <!-- 搜索区 -->
            <div class="row">
                <div class="col-md-12">
                    <div class="input-group mb-3">
                        <input type="text" class="form-control" placeholder="Search notes"  id="search_input"  aria-describedby="button-addon2">
                        <button class="btn btn-outline-secondary" type="button" id="search_button">Search</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 图谱区 -->
    <div class="row" id="graph">
        <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    </div>
    <div class="row">
        <ul class="pagination justify-content-end" id="pagination"></ul>
    </div>
</div>

<script type="text/javascript">

    var tag = "${tag}";

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
        $('#graph').height($(window).height() - 200);
        $('#graph').width($(window).width());
    }

    /**
     * 初始化地图高度
     */
    adjustGraphWithHeight();

    /**
     * 自适应笔记列表高度
     */
    function heightAdaptive() {
        $('#menu').height($(window).height());
    }

    heightAdaptive();


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
            prev: '<a class="page-link" href="javascript:void(0);" aria-label="Previous"><span aria-hidden="true">&laquo;</span> </a>',
            next: '<a class="page-link" href="javascript:void(0);" aria-label="Next"><span aria-hidden="true">&raquo;</span></a>',
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
        axios.get('/api/note/search/findByTitleContains?title=' + title + '&tag=' + tag + '&page=' + num + "&size=100")
            .then(response => {
                var page = response.data.page;
                if (page.totalElements == 0) {
                    loadGraph(null, null);
                    // pagination(page.size, page.number + 1, page.totalPages, page.totalElements);
                    return;
                }
                var noteResources = response.data._embedded.noteResources;
                var noteIds = noteResources.map(function (note) {
                    return note.noteId;
                });
                // {"noteId":"a51ca953-e22d-4a95-8e84-1686cf570347","name":"Neo4J","href":"/note/a51ca953-e22d-4a95-8e84-1686cf570347","value":10}
                var notes = noteResources.map(function (note) {
                    var note = note;
                    note.name = note.noteId;
                    note.symbolSize = [55, 55];
                    note.value = 10;
                    return note;
                });
                var links = [];
                noteResources.forEach(function (note) {
                    var source = note.noteId
                    note.refs.forEach(function (ref) {
                        var link = ref;
                        link.source = source;
                        link.target = ref.noteId;
                        link.name = ref.title;
                        links.push(link);
                    });
                });
                loadGraph(notes, links);
                pagination(page.size, page.number + 1, page.totalPages, page.totalElements);
            })
            .catch(response => error(response));
    }

    var graphNodes;
    var graphLinks;

    /**
     * 加载地图
     */
    function loadGraph(notes, links) {
        graphNodes = notes;
        graphLinks = links;
        myChart.clear();
        if (null == notes || null == links) {
            return;
        }
        var option = {
            // tooltip: {
            //     trigger: 'item',
            //     formatter: function (params, ticket, callback) {
            //         return params.data.name;
            //     }
            // },
            animationDuration: 1500,
            animationEasingUpdate: 'quinticInOut',
            backgroundColor: '#ffffff',
            series: [
                {
                    name: 'Node Map',
                    type: 'graph',
                    layout: 'force',
                    data: notes,
                    links: links,
                    roam: true,
                    draggable: false,
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
        heightAdaptive();
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
    var EMPTY_NODE = {
        noteId: null,
        name: null,
        href: null,
        value: null
    };

    var selectNode = EMPTY_NODE;


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
                    selectNode = EMPTY_NODE;
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
        console.log("error:" + response);
        if (undefined === response.data || undefined === response.data.message) {
            toastr.error("请求失败");
        } else {
            toastr.error(response.data.message);
        }
    }

    function toNodes(noteResources) {
        // {"noteId":"a51ca953-e22d-4a95-8e84-1686cf570347","name":"Neo4J","href":"/note/a51ca953-e22d-4a95-8e84-1686cf570347","value":10}
        var note = noteResources;
        note.name = noteResources.noteId;
        note.symbolSize = [55, 55];
        note.value = 10;
        return note;
    }

    function toLinks(noteResources) {
        var links = [];
        noteResources.forEach(function (note) {
            var source = note.noteId
            note.refs.forEach(function (ref) {
                var link = ref;
                link.source = source;
                link.target = ref.noteId;
                link.name = ref.title;
                links.push(link);
            });
        });
        return links;
    }

    /**
     * 新建笔记
     */
    $("#new_button").bind('click', function () {
        var noteParam = {
            title: '无标题笔记',
            content: ''
        }
        if (EMPTY_NODE != selectNode) {
            noteParam.content = "@[" + selectNode.title + "](/note/" + selectNode.noteId + ")";
        }
        axios.post('/api/note', noteParam).then(response => {
            window.open(response.data._links.edit.href);
            location.reload();
        }).catch(response => error(response));
    });

    /**
     * 编辑笔记
     */
    $("#edit_button").bind('click', function () {
        if (EMPTY_NODE == selectNode) {
            toastr.warning('请选择节点');
            return;
        }
        window.open(selectNode._links.edit.href);
    });

    /**
     * 展开搜索
     */
    $("#expand_search_button").bind('click', function () {
        $("#search_area").fadeToggle("slow");
    });

    /**
     * 删除笔记
     */
    $("#delete_button").bind('click', function () {
        if (EMPTY_NODE == selectNode) {
            toastr.warning('请选择节点');
            return;
        }
        toastr["warning"](
            "<div class=\"btn-group\" >" +
            "<button style=\"margin:5px\" id=\"confirm-delete-button\", type=\"button\" class=\"btn btn-secondary\" onclick=\"deleteNote()\">Yes</button>" +
            "<button style=\"margin:5px\" type=\"button\" class=\"btn btn-light\">No</button>" +
            "</div>",
            "确认是否删除");
    });

    /**
     * 执行删除笔记
     */
    function deleteNote() {
        axios.delete('/api/note/' + selectNode.noteId).then(response => {
            location.reload();
        }).catch(response => error(response));
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
        if (selectNode.noteId == null) return;
        axios.get('/api/note/' + selectNode.noteId + '/neighbours').then(response => {
            var noteResources = response.data;
            noteResources.forEach(function (noteResource) {
                var note = toNodes(noteResource);
                var exist = false;
                graphNodes.forEach(function (existNode) {
                    if (note.name == existNode.name) {
                        exist = true;
                    }
                });
                if (!exist) {
                    graphNodes.push(note);
                }
            });

            var links = toLinks(noteResources);

            links.forEach(function (link) {
                var exist = false;
                graphLinks.forEach(function (existLink) {
                    if (link.source == existLink.source && link.target == existLink.target) {
                        exist = true;
                    }
                });
                if (!exist) {
                    graphLinks.push(link);
                }
            });

            var options = myChart.getOption();
            options.series[0].data = graphNodes;
            options.series[0].links = graphLinks;
            myChart.setOption(options);
            selectNode = EMPTY_NODE;
        }).catch(response => error(response));
    });

    /**
     * 绑定
     */
    $('#search_area').keydown(function (e) {
        if (e.keyCode === 13) {
            loadData($('#search_input').val(), null);
        }
    });

    /**
     * 财报分析
     */
    $("#financial_button").bind('click', function () {
        window.open("/financial");
    });

    /**
     * 展开标签
     */
    $("#label_button").bind('click', function () {
        window.location.replace("/label");
    });

</script>
</body>
</html>