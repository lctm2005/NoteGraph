<!DOCTYPE html>
<%@ page language="java" isThreadSafe="true" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>Note</title>

    <!-- Font awesome -->
    <link rel="stylesheet" href="http://47.95.115.246/font-awesome-4.7.0/css/font-awesome.min.css"/>
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
    <!-- Editormd -->
    <link rel="stylesheet" href="http://47.95.115.246/editor.md/css/editormd.css"/>
    <!-- Toastr -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" rel="stylesheet"/>

    <!-- Bootstrap -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- Axios -->
    <script src="https://cdn.staticfile.org/axios/0.18.0/axios.min.js"></script>
    <!-- Toastr -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
    <!-- Editormd -->
    <script src="http://47.95.115.246/editor.md/editormd.js"></script>
</head>


<body>
<div id="main">
    <!-- 笔记标题 -->
    <div class="input-group input-group-lg">
        <div class="input-group-prepend">
            <span class="input-group-text" id="inputGroup-sizing-lg">标题</span>
        </div>
        <input type="text" class="form-control" aria-label="Title"
               aria-describedby="inputGroup-sizing-lg" id="title">
        <!-- 删除按钮 -->
        <button id="delete-note-button" type="button" class="btn btn-danger btn-sm">
            <i class="fa fa-trash" aria-hidden="true"></i> 删除笔记
        </button>
    </div>
    <!-- 笔记内容 -->
    <div id="editor" class="container-fluid"></div>
    <!-- 保存按钮 -->
    <button id="save-note-button" type="button" class="btn btn-secondary  btn-lg btn-block">
        <i class="fa fa-floppy-o" aria-hidden="true"></i> 保存笔记
    </button>
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
        "positionClass": "toast-top-full-width",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "10000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }

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
                        path: "http://47.95.115.246/editor.md/lib/"
                    });
                    // 30s 自动保存笔记
                    setInterval(saveNote(), 30 * 1000);
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
                path: "http://47.95.115.246/editor.md/lib/"
            });
        }
    });

    function heightAdaptive() {
        $('#main').height($(window).height() - 150);
    }

    function deleteNote() {
        axios.delete('/api/note/' + noteId)
            .then(response => {
                window.close();
            }).catch(response => {
            console.log(response);
            toastr.error('删除失败');
        });
    }

    function saveNote() {
        if ("" == noteId) {
            axios.post('/api/note', {
                title: $('#title').val(),
                content: editor.getMarkdown()
            }).then(response => {
                //重定向
                window.navigate('/note/' + response.data.id);
            }).catch(response => {
                console.log(response);
                toastr.error('保存失败');
            });
        } else {
            axios.put('/api/note/' + noteId, {
                title: $('#title').val(),
                content: editor.getMarkdown()
            }).then(response => {
                toastr.success('保存成功');
            }).catch(response => {
                console.log(response);
                toastr.error('保存失败');
            });
        }
    }

    $('#save-note-button').click(function (event) {
        saveNote();
    });

    $('#delete-note-button').click(function (event) {
        toastr["error"]("<div class=\"btn-group\" ><button id=\"confirm-delete-button\", type=\"button\" class=\"btn btn-danger\" onclick=\"deleteNote()\">Yes</button><button type=\"button\" class=\"btn btn-success\">No</button></div>", "确认是否删除");
    });

    //当浏览器大小变化时
    $(window).resize(function () {
        heightAdaptive();
        if (typeof(editor) != "undefined") {
            editor.resize();
        }
    });

    $('#main').keydown(function (e) {
        if (e.ctrlKey && e.keyCode == 13) {
            saveNote();
        }
    });
</script>

</body>
</html>