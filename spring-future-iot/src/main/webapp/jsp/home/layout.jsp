<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><tiles:insertAttribute name="title" ignore="true"/>Fsmart Việt Nam</title>

    <%--<link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet"/>--%>
    <%--<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>--%>

    <%--<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">--%>
    <link href="/css/style.css" type="text/css" rel="stylesheet"/>
    <script src="/js/jquery.min.js" type="text/javascript"></script>
    <script src="/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/js/header.js" type="text/javascript"></script>
    <script src="/js/stomp.js" type="text/javascript"></script>
    <script src="/js/sockjs-0.3.4.min.js" type="text/javascript"></script>


</head>
<body>
<tiles:insertAttribute name="header"/>
<tiles:insertAttribute name="body"/>
<tiles:insertAttribute name="footer"/>
</div>
</body>
</html>