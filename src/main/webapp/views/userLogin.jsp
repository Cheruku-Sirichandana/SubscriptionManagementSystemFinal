<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.SpringBoot.SubscriptionManagementSystemProject.Model.UserModel" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Login page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #F4F4F4;
            margin: 0;
            padding: 0;
        }
        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        th, td {
            padding: 15px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #FF8800;
            color: #fff;
        }
        tr:hover {
            background-color: #F5F5F5;
        }
        a {
            text-decoration: none;
            color: #FF8800;
        }
        form {
            text-align: center;
            margin-top: 20px;
        }
        input[type="submit"] {
            padding: 10px 20px;
            background-color:#ff8800;
            color: #fff;
            border: none;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color:#ff8800;
        }
    </style>
</head>
<body>
<center>
    <h2>User Login </h2>
    <form:form action="userCheck" modelAttribute="userModel">
        <h3>${msg1}</h3>
        <label for="userName">Username:</label><br>
        <form:input path="userName"/><br>
        <form:errors path="userName" style="color: red;"/><br>

        <label for="userPassword">User Password:</label><br>
        <form:input path="userPassword" type="password"/><br>
        <form:errors path="userPassword" style="color: red;"/><br>

        <input type="hidden" name="userId" value="${userModel.userId}">

        <input type="hidden" id="subscriptionStatus" name="subscriptionStatus" value="${subscriptionStatus}">
        <input type="hidden" id="count" name="count" value="${count}">

        <input type="submit" value="Submit">
    </form:form>

    <h3><a href="home">Home</a></h3>
</center>
</body>
</html>
