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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">

    <!-- Toastr -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet"/>

    <!-- ECharts -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/echarts/4.2.1/echarts.min.js"></script>
    <!-- JQuery -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- Jqpaginator -->
    <script src="http://notegraph.cn/js/jq-paginator.min.js"></script>
    <!-- Axios -->
    <script src="https://cdn.staticfile.org/axios/0.18.0/axios.min.js"></script>
    <!-- Toastr -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
</head>
<body>

<div class="container-fluid">
    <!-- 搜索展开区 -->
    <div class="row" id="search_area">
        <div class="col-md-12">
            <!-- 搜索区 -->
            <div class="row">
                <div class="col-md-12">
                    <div class="input-group mb-3">
                        <input type="text" class="form-control" placeholder="Search tags"  id="search_input"  aria-describedby="button-addon2">
                        <button class="btn btn-outline-secondary" type="button" id="search_button">Search</button>
                    </div>
                </div>
            </div>
            <!-- 标签区 -->
            <div class="row">
                <div class="col-md-12" id="tag_area">
                </div>
            </div>
        </div>
    </div>
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
        axios.get('/api/tags/search/findByTitleContains?title=' + title + '&page=' + num + "&size=100")
            .then(response => {
                var page = response.data.page;
                if (page.totalElements == 0) {
                    pagination(page.size, page.number + 1, page.totalPages, page.totalElements);
                    return;
                }
                var tagResources = response.data._embedded.tagResources;
                tagResources.forEach(function (tag) {
                    $("#tag_area").append('<span class="badge badge-primary" id="'+ tag.tagId +'" type="label" style="cursor:pointer">'+tag.title+'</span>');
                });
                /**
                 * 点击标签
                 */
                $("span[type='label']").bind('click', function (label) {
                    window.location.replace("/graph/"+label.currentTarget.id);
                });
            })
            .catch(response => error(response));
    }


    /**
     * 初始化
     */
    loadData("", null);

    function error(response) {
        console.log("error:" + response);
        if (undefined === response.data || undefined === response.data.message) {
            toastr.error("请求失败");
        } else {
            toastr.error(response.data.message);
        }
    }


    /**
     * 检索标签
     */
    $("#search_button").bind('click', function () {
        loadData($('#search_input').val(), null);
    });


    /**
     * 绑定回车
     */
    $('#search_area').keydown(function (e) {
        if (e.keyCode === 13) {
            loadData($('#search_input').val(), null);
        }
    });

</script>
</body>
</html>