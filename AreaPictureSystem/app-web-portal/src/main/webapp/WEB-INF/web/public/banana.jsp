<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page isELIgnored="false"%>
<style type="text/css">
/*-- //menu --*/
.banner {
	background: url('${webIndexBanana.banana}') no-repeat 0px 0px;
	background-size: cover;
	-webkit-background-size: cover;
	-o-background-size: cover;
	-ms-background-size: cover;
	-moz-background-size: cover;
	min-height: 730px;
}



</style>

<div class="banner">
	<div class="container">
		<div class="banner-info text-center">
			<h1>${webIndexBanana.bananaTitle }</h1>
			<p> ${webIndexBanana.bananaContent } </p>
		</div>
	</div>
</div>