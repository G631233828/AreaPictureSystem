<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false"%>

<style type="text/css">
        html{height:100%;}
        body{min-height:100%;margin:0;padding:0;position:relative;}

        .main{padding-bottom:100px;}
        .footer{position:absolute;bottom:0;width:100%;height:20px; }
    </style>

<div class="footer" style="margin-top: 50px;">
    <p style="color:#7E7E7E;text-align: center;padding-top: 15px;">
       ${websetting.copyRight}
    </p>
</div>

<!-- 引入通用js -->
	<script
		src="${pageContext.request.contextPath}/assets/web/js/common.js"></script>