<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<nav class="navbar navbar-default navbar-fixed-top" id = "navbar">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#myNavbar">
				<span class="icon-bar" style="background-color: #828282;"></span> <span
					class="icon-bar" style="background-color: #828282;"></span> <span
					class="icon-bar" style="background-color: #828282;"></span>
			</button>
			<a class="navbar-brand" style="color: #1a3182e0 !important;" href="/">FSMART</a>
		</div>
		<div class="collapse navbar-collapse" id="myNavbar">
			<ul class="nav navbar-nav navbar-left nav-left">
				<li><a href="/">Trang Chủ</a></li>
				<li><a href="/">Giới Thiệu</a></li>
				<li><a href="/">Hướng Dẫn</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!hasRole('ROLE_USER')">
					<li>
                        <a href="/login">
                            <i class="fa fa-sign-in" style="font-size: 17px"></i>
                            Đăng nhập
                        </a>
                    </li>
					<li>
                        <a href="/register"><i class="fa fa-user-o" style="font-size: 17px;"></i>
                            Đăng ký
                        </a>
                    </li>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USER')">
					<li>
						<button onclick="window.location.href = '/device/add';" class="btn btn-primary btn-rounded btn-fw">Trình Quản Lý</button>
					</li>
				</sec:authorize>
				<li>
					<form action="/logout" id="logout" method="post">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}">
					</form>
				</li>
			</ul>
		</div>
	</div>
</nav>
