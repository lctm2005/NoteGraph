<!DOCTYPE html>
<%@ page language="java" isThreadSafe="true" pageEncoding="gbk" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>NoteForceView</title>
    <link rel="stylesheet" href="../editor.md/css/editormd.css"/>
    <!-- ECharts????????? -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css">

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="../editor.md/editormd.js"></script>
</head>


<body>
<div id="editor" class="container-fluid"></div>
<button id="get-md-btn" type="button" class="btn btn-secondary  btn-lg btn-block">Save Note</button>
<div id="success-alert" class="alert alert-success fade">
    Save Successfully
</div>
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
        $.ajax({
            url: "/api/note_content",
            method: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                uuid: '${noteId}',
                markdown: editor.getMarkdown()
            }),
            success: function (result) {
                window.location.href = "/graph";
//                $('#get-md-btn').fadeToggle(function () {
//                    $("#success-alert").toggleClass("fade show");
//                });
//                setTimeout(function(){
//                    $("#success-alert").toggleClass("fade show");
//                    $('#get-md-btn').fadeToggle();
//                },1000);
            }
        });
    });
</script>

</body>
</html>