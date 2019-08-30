<!DOCTYPE html>
<%@ page language="java" isThreadSafe="true" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>Note</title>

    <link rel="stylesheet" href="http://47.95.115.246/font-awesome-4.7.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="http://47.95.115.246/editor.md/css/editormd.css"/>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css">


    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/axios/0.18.0/axios.min.js"></script>

    <script src="http://47.95.115.246/editor.md/editormd.js"></script>
</head>


<body>
<div id="main">
    <!-- 笔记标题 -->
    <div class="input-group input-group-lg">
        <div class="input-group-prepend">
            <span class="input-group-text" id="inputGroup-sizing-lg">Title</span>
        </div>
        <input type="text" class="form-control" aria-label="Title"
               aria-describedby="inputGroup-sizing-lg" id="title">
    </div>
    <!-- 笔记内容 -->
    <div id="editor" class="container-fluid"></div>
    <!-- 保存按钮 -->
    <button id="get-md-btn" type="button" class="btn btn-secondary  btn-lg btn-block">Save Note
    </button>
    <!-- 保存提示 -->
    <div id="success-alert" class="alert alert-success fade">
        Save Successfully
    </div>
    <div id="error-alert" class="alert alert-danger fade">
        Network Error
    </div>
</div>

<script type="text/javascript">
    var noteId = "${note_id}";
    var editor;

    $(function () {
        heightAdaptive();

        if ("" != noteId) {
            axios.get('/api/note/' + noteId)
                .then(response => {
                    $('#title').val(response.data.title);
                    editor = editormd("editor", {
                        markdown: response.data.content,
                        editorTheme: "pastel-on-dark",
                        path: "../editor.md/lib/"
                    });
                })
                .catch(error => {
                    console.log(error)
                    this.error = true
                })
                .finally(() => this.loading = false);
        } else {
            editor = editormd("editor", {
                markdown: "",
                editorTheme: "pastel-on-dark",
                path: "../editor.md/lib/"
            });
        }
    });

    function heightAdaptive() {
        $('#main').height($(window).height() - 150);
    }

    $('#get-md-btn').click(function (event) {
        if ("" == noteId) {
            axios.post('/api/note', {
                title: $('#title').val(),
                content: editor.getMarkdown()
            }).then(response => (noteId = response.data.id)
                .catch(response => (console(response))));
        } else {
            axios.put('/api/note/' + noteId, {
                title: $('#title').val(),
                content: editor.getMarkdown()
            }).then(response => (console.log(response))).catch(response => (console(response)));
        }
    });

    $(window).resize(function () {          //当浏览器大小变化时
        heightAdaptive();
        if (typeof(editor) == "undefined") {
            editor.resize();
        }
    });

</script>

</body>
</html>