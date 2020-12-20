<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<tiles:insertDefinition name="user">
    <tiles:putAttribute name="body">
        <script src="/js/sockjs-0.3.4.min.js" type="text/javascript"></script>
        <script src="/js/stomp.js" type="text/javascript"></script>
        <script src="/js/device.details.js" type="text/javascript"></script>
        <div class="row">
        <div class="col-md-3 grid-margin stretch-card">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title" align="center">Thêm Thiết Bị</h4>
                    <p style="color:red" align="center">${isExist}</p>
                    <form:form action="/device/save" modelAttribute="device" method="post">
                        <div class="form-group">
                            <label>Mac ID:<form:errors path="macAddress" cssStyle="color:red;"/></label>
                            <form:input type="text" path="macAddress" cssClass="form-control form-control-lg" placeholder="Địa chỉ mac"/>
                        </div>
                        <div class="form-group">
                            <label>Mô Tả:<form:errors path="description" cssStyle="color:red;" /></label>
                            <form:input type="text" path="description" cssClass="form-control form-control-lg" placeholder="VD: tên, định danh"/>
                        </div>
                        <div class="form-group">
                            <label>Vị Trí:<form:errors path="location" cssStyle="color:red;" /></label>
                            <form:input type="text" path="location" cssClass="form-control form-control-lg" placeholder="Vị trí đặt thiết bị"/>
                        </div>

                        <div class="form-group">
                            <label>Thể Loại:</label>
                            <form:select path="typeCode" cssClass="form-control form-control-sm" >
                                <form:options  items="${optionTypeDevice}"/>
                            </form:select>
                        </div>
                        <div class="form-group">
                            <label>Hệ Mật:</label>
                            <select name="algorithm" class="form-control form-control-sm" >
                                <c:forEach items="${algorithms}" var="algorithm" varStatus="loop">
                                    <option value="${algorithm}">${algorithm}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-success mr-2">Thêm</button>
                    </form:form>
                </div>
            </div>
        </div>

        <div class="col-md-9 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title" align="center">Danh Sách Thiết Bị</h4>
                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>Mac ID</th>
                            <th>Thể Loại</th>
                            <th>Key Secret - IV</th>
                            <th>Ngày tạo</th>
                            <th>Vị Trí</th>
                            <th>Mô Tả</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${lstDeviceAndKeys}" var="deviceAndKey" varStatus="loop">
                            <tr>
                                <td><a href="/device/${deviceAndKey.typeCode}/${deviceAndKey.macAddress}">${deviceAndKey.macAddress}</a></td>
                                <td>${deviceAndKey.typeName}</td>
                                <td>${deviceAndKey.keySecret} - ${deviceAndKey.initVector}</td>
                                <td>${deviceAndKey.createDate}</td>
                                <td>${deviceAndKey.location}</td>
                                <td>${deviceAndKey.description}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>