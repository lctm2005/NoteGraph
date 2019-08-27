<!DOCTYPE html>
<%@ page language="java" isThreadSafe="true" pageEncoding="gbk" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>NoteForceView</title>
    <link rel="stylesheet" href="../editor.md/css/editormd.css"/>
    <!-- ECharts单文件引入 -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
<body>
<div id="editor" class="container-fluid"></div>
<button id="get-md-btn" type="button" class="btn btn-secondary  btn-lg btn-block">Save Note</button>
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="../editor.md/editormd.js"></script>
<script type="text/javascript">
    $(function () {
        editor = editormd("editor", {
            width: "100%",
            height: 1000,
            markdown: "${content}",
            editorTheme: "pastel-on-dark",
            path: "../editor.md/lib/"
        });
    });
    $("#get-md-btn").bind('click', function () {
        //TODO 保存笔记
        alert(editor.getMarkdown());
    });
</script>

</body>
</html>