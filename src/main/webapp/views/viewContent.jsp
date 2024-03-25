<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.SpringBoot.SubscriptionManagementSystemProject.Model.ContentModel" %>

<html>
<head>
    <title>View Content</title>
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
            background-color: #ff8800;
            color: #fff;
            border: none;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #ff8800;
        }
    </style>
</head>
<body>
<center><h3>Contents</h3></center>
<center><h3>${msg}</h3></center>
<table>
    <tr>

        <b><td>Content Id</td></b>
        <b><td>Content Title</td></b>
        <b><td>Content Type</td></b>
        <b><td>Duration In Minutes</td></b>
        <b><td>SubscriptionLevel</td></b>
        <b><td>Category</td></b>
    </tr>
    <%
        List<ContentModel> contentList = (List<ContentModel>) request.getAttribute("contentList1");
        if (contentList != null) {
            for (ContentModel content : contentList) {
    %>
    <tr>
        <td><%= content.getContentId() %></td>
        <td><%= content.getTitle() %></td>
        <td><%= content.getType() %></td>
        <td><%= content.getCategory() %></td>
        <td><%= content.getDurationInMinutes() %></td>
        <td><%= content.getSubscriptionLevel() %></td>


         <td><form action="deleteContent">
         <input type="hidden" name="contentId" value="<%= content.getContentId() %>" >
          <input type="hidden" name="title" value="<%= content.getTitle() %>" >
                    <input type="submit" value="DeleteContent"></form></td>

                     <td><form action="updateContent">
                      <input type="hidden" name="contentId" value="<%= content.getContentId() %>" >
                           <input type="hidden" name="title" value="<%= content.getTitle() %>" >
                       <input type="submit" value="UpdateContent"></form></td>
</tr>

    <%
        }
    } else {
    %>
    <tr>
        <td colspan="6">No Content found</td>
    </tr>
    <% } %>
</table>

<center> <h3><a href="content">Back</a></h3></center>

<center> <h3><a href="adminFeatures">AdminFeatures</a></h3></center>


                    <center><h3><a href="home">Home</a></h3></center>
</body>
</html>
