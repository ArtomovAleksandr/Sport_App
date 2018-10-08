<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <c:import url="../template/head.jsp"></c:import>
</head>

<body>
<c:import url="../template/header.jsp"></c:import>
<div class="container main-div mt-md-5">
    <div class="tools-div form-inline form-control">
        <c:import url="../template/search.jsp"></c:import>
        <a class="btn btn-outline-warning ml-2" href="/users/create">Create</a>
    </div>
    <div class="users-div">
        <table class="table table-bordered mt-2">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">First Name</th>
                <th scope="col">Last Name</th>
                <th scope="col">Phone</th>
                <th scope="col">Email</th>
                <th colspan="2"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="user">
                <tr>
                    <th scope="row">${user.id}</th>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.phone}</td>
                    <td>${user.email}</td>
                    <td colspan="2">
                        <a class="btn-edit btn btn-outline-info" href="/users/edit/${user.id}">Edit</a>
                        <button class="btn-delete btn btn-outline-danger ml-2" name="id" value="${user.id}" >Delete</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<c:import url="../template/footer.jsp"></c:import>
<!--Scripts-->
<script th:src="@{/webjars/jquery/jquery.min.js}"></script>
<script th:src="@{/webjars/jquery-ui/jquery-ui.min.js}"></script>
<script th:src="@{/webjars/bootstrap/js/bootstrap.min.js}"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript">
    $(function () {
        $(".btn-delete").click(function (e) {
            var btn = $(this);
            $.ajax({
                type: 'DELETE',
                url: "/api/1.0/users/" + btn.val()
            }).done(function () {
                location.reload(true);
            }).fail(function () {
                alert('fail');
            });
            e.preventDefault();
        });
    });
</script>
</body>

</html>