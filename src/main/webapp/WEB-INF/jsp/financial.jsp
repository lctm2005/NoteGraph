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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
    <!-- Toastr -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet"/>

    <!-- JQuery -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- Axios -->
    <script src="https://cdn.staticfile.org/axios/0.18.0/axios.min.js"></script>
    <!-- Toastr -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
    <!-- VueJs -->
    <script src="https://cdn.staticfile.org/vue/2.2.2/vue.min.js"></script>
</head>
<body>
<div id="app" class="container-fluid">
    <nav class="navbar navbar-expand-lg ">
        <a class="navbar-brand" href="#">财报分析</a>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <div class="form-inline my-2 my-lg-0" id="search_area">
                <input class="form-control mr-sm-2" type="search" id="stock_code" placeholder="股票代碼"
                       aria-label="stock_code" v-model="stockCode" v-on:keyup.enter="search">
                <button class="btn btn-success my-2 my-sm-0" id="search_button" type="button" v-on:click="search">搜索
                </button>
            </div>
        </div>
    </nav>

    <div class="row">
        <div class="col-md-4">
            <table class="table table-sm table-hover">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">财务比率</th>
                    <th scope="col">值</th>
                </tr>
                </thead>
                <tbody v-for="item in items">
                <tr v-bind:class="{'table-success': item.good, 'table-danger': item.bad, 'table-warning': item.middle}"
                    data-toggle="tooltip" data-placement="bottom" v-bind:title="item.description">
                    <td>{{item.title}}</td>
                    <td>{{item.value}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="col-md-8">
            <iframe id="iframe" v-bind:src="iframe" width="100%" height="1676" scrolling="no">
            </iframe>
        </div>
    </div>
</div>
<script>
    /**
     * 绑定
     */
    $('#search_area').keydown(function (e) {
        if (e.keyCode === 13) {
            loadData($('#search_input').val(), null);
        }
    });

    var vue = new Vue({
        el: '#app',
        data: {
            stockCode: "",
            iframe: "https://caibaoshuo.com",
            items: []
        },
        methods: {
            search: function () {
                axios.get('/api/financial_analysis/' + this.stockCode)
                    .then(response => {this.items = response.data.items})
                    .catch(function (error) { // 请求失败处理
                        console.log(error);
                    });
                this.iframe = "https://caibaoshuo.com/companies/" + this.stockCode;
            }
        }
    })
</script>
</body>
</html>