<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.SpringBoot.SubscriptionManagementSystemProject.ContentModel.ContentType" %>
<!DOCTYPE html>
<html>
<head>
    <title>Find Content</title>
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
    <h2>SubscriptionPlans Of Content </h2>

    <form action="foundSubscriptionPlansOfContent" method="get">
        <div class="form-group">
            <label for="type">Type:</label>
            <form:select id="type" path="type">
                <form:option value="MOVIE">Movie</form:option>
                <form:option value="TV_SHOW">TV Show</form:option>
                <form:option value="WEB_SERIES">Web Series</form:option>
                <form:option value="SPORTS">Sports</form:option>
                <form:option value="NEWS">News</form:option>
                <form:option value="MUSIC">Music</form:option>
            </form:select>
        </div>
        <input type="submit" value="Submit">
    </form>

    <h3><a href="home">Home</a></h3>
</center>
</body>
</html>
