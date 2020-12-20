<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<tiles:insertDefinition name="home">
	<tiles:putAttribute name="body">
		<div class="container-fluid login" style="padding-top: 120px;">
			<div class="container re-login">
				<form method="post" action="/check_sec">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}">

					<p style="color: red; text-align: center;">${error}</p>

					<div class="form-group">
						<label>Số điện thoại:</label> <input type="text"
							class="form-control input-sm" name="username" id="username"
							placeholder="Nhập số điện thoại đăng nhập" autofocus="autofocus">
					</div>
					<div class="form-group">
						<label>Mật khẩu:</label> <input type="password"
							class="form-control input-sm" id="password" name="password"
							placeholder="Nhập mật khẩu">
					</div>
					<div class="checkbox">
						<label><input type="checkbox" name="remember-me"><span
							style="vertical-align: middle">Duy trì đăng nhập.</span></label> <a
							href="" style="float: right; font-size: 14px;">Quên mật khẩu?</a>
					</div>
					<button type="submit"
						style="background-color: #FF7519; border: none;"
						class="btn btn-primary btn-block">Đăng Nhập</button>
					<hr style="margin: 10px 0;" />
					<span>Đăng nhập bằng: <a style="vertical-align: -7px;"
						href=""><i class="fa fa-facebook-official"
							style="font-size: 30px; color: #0067CA;"></i></a></span> <span
						style="float: right;"><a href="/register">Đăng ký</a></span>
				</form>
			</div>
		</div>

	</tiles:putAttribute>
</tiles:insertDefinition>