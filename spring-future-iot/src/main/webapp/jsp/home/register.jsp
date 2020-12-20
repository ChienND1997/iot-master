<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<tiles:insertDefinition name="home">
	<tiles:putAttribute name="body">
		<div class="container-fluid register" style="padding-top: 120px;">
			<div class="container re-login">
				<form:form method="post" action="/acount-save" modelAttribute="info">
					<p style="color: red; text-align: center;">${message}</p>
					<div class="form-group">
						<label>Số điện thoại đăng nhập:</label>
						<form:errors path="username" cssStyle="color:red;" />
						<form:input type="text" cssClass="form-control input-sm"
							path="username" name="username" placeholder="Nhập số điện thoại của bạn" />
					</div>
					<div class="form-group">
						<label>Email:</label>
						<form:errors path="email" cssStyle="color:red;" />
						<form:input type="text" cssClass="form-control input-sm"
							path="email" name="email" placeholder="Nhập email" />
					</div>
					<div class="form-group">
						<label>Mật khẩu:</label>
						<form:errors path="password" cssStyle="color:red;" />
						<form:input type="password" cssClass="form-control input-sm"
							path="password" name="password" placeholder="Độ dài 6-16 và không chứa khoảng trắng" />
					</div>
					
					<div class="form-group">
						<label>Họ và tên: </label>
						<form:errors path="fullname" cssStyle="color:red;" />
						<form:input type="text" path="fullname" name="fullname"
							cssClass="form-control input-sm" placeholder="Nhập họ và tên "/>
					</div>
					<button type="submit"
						style="background-color: #FF7519; border: none;"
						class="btn btn-primary btn-block">Đăng Ký</button>
					<hr style="margin: 10px 0;" />
					<span style="float: right;"><p style="vertical-align: -7px;">
							Đã có tài khoản? <a href="/login">Đăng nhập</a>
						</p></span>
				</form:form>
			</div>
		</div>

	</tiles:putAttribute>
</tiles:insertDefinition>