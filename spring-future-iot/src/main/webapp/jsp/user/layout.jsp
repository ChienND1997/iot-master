<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><tiles:insertAttribute name="title" ignore="true"/>Fsmart Việt Nam</title>
    <title>Title</title>

    <link rel="stylesheet" href="/vendors/iconfonts/mdi/css/materialdesignicons.min.css">
    <link rel="stylesheet" href="/vendors/iconfonts/font-awesome/css/font-awesome.css">
    <link rel="stylesheet" href="/vendors/css/vendor.bundle.base.css">
    <link rel="stylesheet" href="/vendors/css/vendor.bundle.addons.css">
    <link rel="stylesheet" href="/css/style.user.css">

</head>
<body>
<div class="container-scroller">
    <tiles:insertAttribute name="header"/>
    <div class="container-fluid page-body-wrapper">
        <tiles:insertAttribute name="menu"/>
        <div class="main-panel">
            <div class="content-wrapper">
                <tiles:insertAttribute name="body"/>
            </div>
        </div>
    </div>
    <tiles:insertAttribute name="footer"/>
</div>
</body>

<script src="/vendors/js/vendor.bundle.base.js"></script>
<script src="/vendors/js/vendor.bundle.addons.js"></script>
<script src="/js/off-canvas.js"></script>
<script src="/js/misc.js"></script>
<script src="/js/dashboard.js"></script>
</html>
