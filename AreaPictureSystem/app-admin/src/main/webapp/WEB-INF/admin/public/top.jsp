<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page isELIgnored="false"%>
<!doctype html>
<html class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge;chrome=1">
<title>顶部</title>
<link
	href="${pageContext.request.contextPath}/assets/admin/js/plugins/datapicker/css/bootstrap-datetimepicker.min.css"
	rel="stylesheet" media="screen">



<!--[if lt IE 8]>
    <script>
        alert('图片库系统 已不支持IE6-8，请使用谷歌、火狐等浏览器\n或360、QQ等国产浏览器的极速模式浏览本页面！');
    </script>
    <![endif]-->




<style type="text/css">
.navbar-form-custom .form-control {
	background: none repeat scroll 0 0 rgba(0, 0, 0, 0);
	border: medium none;
	font-size: 14px;
	height: 60px;
	width: 145px;
	margin: 0;
	z-index: 2000;
}
</style>


</head>
<script type="text/javascript">
	function tocleanForm() {
		$("#edAREA").show();
		$("#edDIRECTLYUTIS").show();
		$("#edBASEUTIS").show();
		$("#edPERSION").show();
		$("#edit").val('');
		$("#boundCompany").attr("disabled", false);

		$("#AREA").each(function() {
			this.checked = false;
		});
		$("#DIRECTLYUTIS").each(function() {
			this.checked = false;
		});
		$("#BASEUTIS").each(function() {
			this.checked = false;
		});
		$("#PERSION").each(function() {
			this.checked = false;
		});
		$("#boundCompany option:first").prop("selected", 'selected');

		$("#CrateActivityForm")[0].reset();

		$("#toModal-form").trigger("click");

	}

	function selectType() {
		var type = $("#searchType").find("option:selected").val(); //获取Select选择的Text
		if (type == "searchForderActivity") {
			$("#serachForder").show();
			$("#serachPicture").hide();
			$("#serachForderQuery3").val("");
			$("#serachForderQuery4").val("");
			$("#serachForderQueryVal3").val("");
			$("#serachForderQueryVal4").val("");
		} else {
			$("#serachForder").hide();
			$("#serachPicture").show();
			$("#serachForderQuery1").val("");
			$("#serachForderQuery2").val("");
			$("#serachForderQueryVal1").val("");
			$("#serachForderQueryVal2").val("");
		}

	}
</script>



<body>





	<div class="row border-bottom white-bg">
		<div class="row">
			<div class="col-xs-6 col-sm-4">
				<div style="width: 130px; float: left;">
					<button class=" minimalize-styl-2 btn btn-primary"
						onclick="return tocleanForm();" style="width: 130px;">
						<i class="fa fa-plus">&nbsp;&nbsp;<b>创建文件夹</b></i>
					</button>

					<a class=" minimalize-styl-2 btn btn-primary" id="toModal-form"
						data-toggle="modal" style="display: none;"
						data-target="#CrateActivity" style="width: 200px;"> </a>
				</div>


			</div>
			<!-- Optional: clear the XS cols if their content doesn't match in height -->
			<div class="clearfix visible-xs"></div>
			<div class="col-xs-6 col-sm-8">
				<div class="navbar-collapse collapse" id="navbar"
					style="float: right;">
					<ul class="nav navbar-nav"
						style="margin-left: 20px; margin-top: 3px;">
						<!-- 以上菜单为固定菜单，以下菜单为动态数据库权限菜单-->


						<c:forEach items="${listMenu}" var="item" varStatus="status">
							<c:if test="${item.type eq 'NoMenu'  && item.pid eq '0'}">
								<li><a aria-expanded="false" role="button"
									href="${pageContext.request.contextPath}/${item.url}?activeMenu=${item.id}">
										${item.name}</a></li>
							</c:if>

							<c:if test="${item.type eq 'HaveMenu'  && item.pid eq '0'}">
								<li class="dropdown"><a aria-expanded="false" role="button"
									href="#" class="dropdown-toggle" data-toggle="dropdown">
										${item.name } <span class="caret"></span>
								</a>
									<ul role="menu" class="dropdown-menu">
										<c:forEach items="${listMenu}" var="subitem"
											varStatus="status">
											<c:set var="val" value="${subitem.id}_${item.id}"></c:set>
											<c:choose>
												<c:when test="${sessionScope.webMenuSession eq  val}">
													<li class="active">
												</c:when>
												<c:otherwise>
													<li>
												</c:otherwise>
											</c:choose>


											<c:if test="${subitem.pid == item.id}">
												<a
													href="${pageContext.request.contextPath}/${subitem.url}?activeMenu=${subitem.id}_${item.id}">
													<i class="fa ${subitem.icon}"></i> ${subitem.name}
												</a>
											</c:if></li>
						</c:forEach>

					</ul>
					</li>

					</c:if>
					</c:forEach>
					</ul>




					<ul class="nav navbar-top-links navbar-right"
						style="margin-top: -5px; float: right;">

						<li><a
							href="${pageContext.request.contextPath}/adminUser/index"
							title="返回首页"> <i class="fa fa-home"></i> 首页
						</a></li>

						<li><a
							href="${pageContext.request.contextPath}/adminUser/loginOut"
							target="mainframer"> <i class="fa fa-sign-out"></i> 退出
						</a></li>
					</ul>
				</div>
			</div>
		</div>

		<div class="ibox-content">
			<form role="search" method="post" class="form-inline"
				action="${pageContext.request.contextPath}/photoMessageAction/searchImgsByQuerys">


				<div class="form-group">
					<select class="input-sm form-control input-s-sm inline"
						name="searchType" id="searchType" style="height: 34px;"
						onchange="return selectType()">
						<option value="">请选择查询条件</option>
						<option value="searchForderActivity">查找活动</option>
						<option value="searchPicture">查找图片</option>
					</select>
				</div>


				<div class="form-group" style="display: none;" id="serachForder">
					<div class="form-group">
						<select class="input-sm form-control input-s-sm inline"
							name="serachForderQuery1" id="serachForderQuery1"
							style="height: 34px;">
							<option value="">请选择查询条件</option>
							<option value="forderActivityName">活动主题</option>
							<option value="name">活动创建人</option>
							<option value="address">地点</option>
						</select>
					</div>
					<div class="form-group">
						<input type="text" placeholder="输入查询内容..." class="form-control"
							name="serachForderQueryVal1" id="serachForderQueryVal1"
							value="${serachForderQueryVal1}">
					</div>

					<div class="form-group">
						<select class="input-sm form-control input-s-sm inline"
							name="serachForderQuery2" id="serachForderQuery2"
							style="height: 34px;" onchange="return selectOne();">
							<option value="">请选择查询条件</option>
							<option value="forderActivityDate">时间段查询活动</option>
							<option value="forderActivityName">活动主题</option>
							<option value="name">活动创建人</option>
							<option value="address">地点</option>
						</select>
					</div>

					<div class="form-group" style="display: none;" id="betweenDate">
						<div class="input-daterange input-group" id="datepicker">
							<input type="text" class="input-sm form-control datainput"
								value="${time1 }" name="time1" data-date-format="yyyy-mm-dd"
								id="time1" placeholder="开始时间" style="height: 34px;"> <span
								class="input-group-addon">到</span> <input type="text"
								class="input-sm form-control datainput" value="${time2 }"
								id="time2" name="time2" data-date-format="yyyy-mm-dd"
								placeholder="结束时间" style="height: 34px;">
						</div>
					</div>

					<div class="form-group" id="CommonSearch">
						<input type="text" placeholder="输入查询内容..." class="form-control"
							name="serachForderQueryVal2" id="serachForderQueryVal2"
							value="${serachForderQueryVal2}">
					</div>
				</div>



				<div class="form-group" style="display: none;" id="serachPicture">
					<div class="form-group">
						<select class="input-sm form-control input-s-sm inline"
							name="serachForderQuery3" id="serachForderQuery3" style="height: 34px;">
							<option value="">请选择查询条件</option>
							<option value="uploadPerson">上传者</option>
							<option value="editorImgInfo.resourceName">图片名称</option>
							<option value="editorImgInfo.person">图片主要人物</option>
							<option value="editorImgInfo.photographer">拍摄者</option>
							<option value="editorImgInfo.resourceAddress">图片所在地址</option>
							<option value="forderActivityAddress">活动地址</option>
						</select>

					</div>

					<div class="form-group">
						<input type="text" placeholder="输入查询内容..." class="form-control"
							name="serachForderQueryVal3" id="serachForderQueryVal3" value="${serachForderQueryVal3}">
					</div>
					<div class="form-group">
						<select class="input-sm form-control input-s-sm inline"
							name="serachForderQuery4" id="serachForderQuery4" style="height: 34px;">
							<option value="">请选择查询条件</option>
							<option value="uploadPerson">上传者</option>
							<option value="editorImgInfo.resourceName">图片名称</option>
							<option value="editorImgInfo.person">图片主要人物</option>
							<option value="editorImgInfo.photographer">拍摄者</option>
							<option value="editorImgInfo.resourceAddress">图片所在地址</option>
							<option value="forderActivityAddress">活动地址</option>
						</select>
					</div>

					<div class="form-group" >
						<input type="text" placeholder="输入查询内容..." class="form-control"
							name="serachForderQueryVal4" id="serachForderQueryVal4" value="${serachForderQueryVal4}">
					</div>
				</div>



				<button class=" btn btn-primary" type="submit">搜索</button>
			</form>
		</div>
		<script type="text/javascript">
			$(function() {
				var searchType = '${searchType}';
				//回显 "selectQuery"  下拉框id  ${schoolCode} 后台放作用域里的值  
				$("#searchType option").each(function() {
					if ($(this).val() == searchType) {
						$(this).attr("selected", true);
					}
				});

				var serachForderQuery1 = '${serachForderQuery1}';
				var serachForderQuery2 = '${serachForderQuery2}';
				var serachForderQueryVal1 = '${serachForderQueryVal1}';
				var serachForderQueryVal2 = '${serachForderQueryVal2}';
				var serachForderQuery3 = '${serachForderQuery3}';
				var serachForderQuery4 = '${serachForderQuery4}';
				var serachForderQueryVal3 = '${serachForderQueryVal3}';
				var serachForderQueryVal4 = '${serachForderQueryVal4}';
				
				
				
				var time1 = '${time1}';
				var time2 = '${time2}';

				if (serachForderQuery1 != "" || serachForderQuery2 != "") {
					$("#serachForder").show();
					$("#serachForderQuery1 option").each(function() {
						if ($(this).val() == serachForderQuery1) {
							$(this).attr("selected", true);
						}
					});
					$("#serachForderQuery2 option").each(function() {
						if ($(this).val() == serachForderQuery2) {
							$(this).attr("selected", true);
						}
					});
					
					if(serachForderQuery2 == 'forderActivityDate'){
						$("#betweenDate").show();
						$("#CommonSearch").hide();
					}
					
					
					
					
					
					
					
					
					
					
			/* 		$("#serachForderQueryVal1 option").each(function() {
						if ($(this).val() == serachForderQueryVal1) {
							$(this).attr("selected", true);
						}
					}); */
					if (serachForderQueryVal2 != "") {
						$("#CommonSearch").show();
						$("#betweenDate").hide();
						$("#serachForderQueryVal2 option").each(function() {
							if ($(this).val() == serachForderQueryVal2) {
								$(this).attr("selected", true);
							}
						});

					}

					if (time1 != "" || time2 != "") {
						$("#CommonSearch").hide();
						$("#betweenDate").show();
						$("#time2 option").each(function() {
							if ($(this).val() == time2) {
								$(this).attr("selected", true);
							}
						});
						$("#time1 option").each(function() {
							if ($(this).val() == time1) {
								$(this).attr("selected", true);
							}
						});
					}

				}else if(serachForderQuery3 != "" || serachForderQuery4 != ""){
					
					$("#serachPicture").show();
					$("#serachForderQuery3 option").each(function() {
						if ($(this).val() == serachForderQuery3) {
							$(this).attr("selected", true);
						}
					});
					$("#serachForderQuery4 option").each(function() {
						if ($(this).val() == serachForderQuery4) {
							$(this).attr("selected", true);
						}
					});
					$("#serachForderQueryVal3 option").each(function() {
						if ($(this).val() == serachForderQueryVal3) {
							$(this).attr("selected", true);
						}
					});
					if (serachForderQueryVal4 != "") {
						$("#serachForderQueryVal4 option").each(function() {
							if ($(this).val() == serachForderQueryVal4) {
								$(this).attr("selected", true);
							}
						});

					}

					
				}

			});

			function selectOne() {
				var serachForderQuery2 = $("#serachForderQuery2").val();
				if (serachForderQuery2 == 'forderActivityDate') {
					$("#CommonSearch").hide();
					$("#serachForderQueryVal2").val('');
					$("#betweenDate").show();
				} else {
					$("#CommonSearch").show();
					$("#time1").val('');
					$("#time2").val('');
					$("#betweenDate").hide();
				}
			}
		</script>





	</div>



	<!-- 创建活动 -->
	<div class="modal inmodal" id="CrateActivity" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h4 class="modal-title">新活动／上传图片</h4>
				</div>

				<form method="post" id="CrateActivityForm" action="#">
					<div class="modal-body">
						<div class="form-group">
							<label>拍摄活动：</label><label for="forderActivityName"
								id="forforderActivityName" class="control-label"
								style="color: red; float: right;"></label> <input type="text"
								placeholder="请输入活动名称" name="forderActivityName"
								onkeyup="return getrepletes('forderActivityName');"
								id="forderActivityName" class="form-control" required>
						</div>
						<input type="hidden" id="forderActivityNamehid">




						<div class="form-group">
							<label>拍摄地址：</label> <input type="text" placeholder="活动地址"
								name="address" id="address" class="form-control">
						</div>



						<div class="form-group"
							onchange="return getrepletes('forderActivityName');">
							<label>活动所属学校：</label> <select class="form-control m-b"
								onchange="return getrepletes('forderActivityName');"
								name="boundCompany" id="boundCompany" required="required">
								<option id="default" selected="selected">----请选择所属学校----</option>
								<c:forEach items="${company}" var="item" varStatus="status">
									<option id="${item.id}" value="${item.id}">${item.name}</option>
								</c:forEach>
							</select>
						</div>



						<div class="form-group">
							<label>拍摄时间：</label> <input type="text" placeholder="拍摄时间"
								name="activityTime" id="activityTime"
								onchange="return getrepletes('forderActivityName');"
								class="form-control datainput" data-date-format="yyyy-mm-dd"
								required>
						</div>


						<div class="form-group" id="belongTo" style="display: none;">
							<label>活动所属：${item.listType}</label>
							<div class="checkbox checkbox-inline" id="edAREA">
								<input type="checkbox" id="AREA" value="AREA" name="type"
									onclick="return getrepletes('forderActivityName');"> <label
									for="AREA"> 区域 </label>
							</div>
							<div class="checkbox checkbox-inline" id="edDIRECTLYUTIS">
								<input type="checkbox" id="DIRECTLYUTIS" value="DIRECTLYUTIS"
									onclick="return getrepletes('forderActivityName');" name="type">
								<label for="DIRECTLYUTIS"> 直属单位 </label>
							</div>

							<div class="checkbox checkbox-inline" id="edBASEUTIS">
								<input type="checkbox" id="BASEUTIS" value="BASEUTIS"
									onclick="return getrepletes('forderActivityName');" name="type">
								<label for="BASEUTIS"> 基层单位</label>
							</div>
							<div class="checkbox checkbox-inline" id="edPERSION">
								<input type="checkbox" id="PERSION" value="PERSION" name="type"
									onclick="return getrepletes('forderActivityName');"> <label
									for="PERSION"> 个人 </label>
							</div>
						</div>



						<div class="form-group">
							<label>拍摄描述：</label>
							<textarea rows="5" cols="7"
								style="resize: none; overflow: scroll;" placeholder="描述"
								name="description" id="description" class="form-control"></textarea>
						</div>
						<input type="hidden" name="boundId" id="boundId"
							value="${sessionScope.userSession.id}">
						<%-- 			<c:if
							test="${sessionScope.userSession.userType != 'ADMINISTRATORS' }">
							<input type="hidden" name="boundCompany" id="boundCompany"
								value="${sessionScope.userSession.adminCompany.id}">

						</c:if> --%>
						<input type="hidden" name="parentId" id="parentId" value="0">
						<input type="hidden" name="type" id="type" value="${webType}">
						<input type="hidden" name="edit" id="edit">
					</div>
					<div class="modal-footer">
						<button type="submit" id="createActivitySubmit"
							class="btn btn-primary">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- 信息提示模态框（Modal） -->
	<div class="modal inmodal fade" id="titleModel" tabindex="-1"
		role="dialog" aria-hidden="true" style="padding: 15%">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<span style="float: left; color: red; font-size: 18px;"
						id="modelLabel"></span>
				</div>
				<div class="modal-body">
					<h3 id="modelContent"></h3>
				</div>
				<input type="hidden" id="delete-id">
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">确定
					</button>
				</div>
			</div>
		</div>
	</div>










	<!-- 添加jqueryvalidate -->
	<script
		src="${pageContext.request.contextPath}/assets/admin/js/plugins/validate/jquery.validate.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/assets/admin/js/plugins/validate/messages_zh.min.js"></script>


	<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/admin/js/plugins/datapicker/js/bootstrap-datetimepicker.js"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/assets/admin/js/plugins/datapicker/js/locales/bootstrap-datetimepicker.zh-CN.js"
		charset="UTF-8"></script>
	<script type="text/javascript">
		$('.datainput').datetimepicker({
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			minView : 2,
			forceParse : 0
		});
	</script>






	<script type="text/javascript">
		//ajax判断有没有重复
		function getrepletes(o1) {
			var types = "";
			var r1 = $("#" + o1).val();//获取需要判断是否重复的属性
			var r2 = $("#" + o1 + "hid").val();//该值的隐藏域值 判断如果是原始值则不变

			var boundCompany = $("#boundCompany").val();

			if (boundCompany == null) {
				boundCompany = "";
			}
			var type = $("input[name='type']:checked");

			if (type == null) {
				types = "";
			} else {
				$(type).each(function() {

					types += this.value + ",";

				});

			}
			var activityTime = $("#activityTime").val();

			if (activityTime == null) {
				activityTime = "";
			}

			var data = o1 + "=" + r1 + "&companyId=" + boundCompany + "&type="
					+ types + "&activityTime=" + activityTime;
			if (r1 != r2) {
				$
						.ajax({
							type : "POST",
							url : "${pageContext.request.contextPath}/forderActivity/ajaxgetRepletes",
							data : data,
							dataType : "json",
							success : function(msg) {
								if (msg.status == 200) {
									$("#createActivitySubmit").attr("disabled",
											false);
									document.getElementById("for" + o1).innerHTML = " ";
								} else {
									document.getElementById("for" + o1).innerHTML = msg.msg;
									document.getElementById("for" + o1).style.cssText = "float: right; color: red;";
									$("#createActivitySubmit").attr("disabled",
											"true");
								}
							}
						});
			} else {
				document.getElementById("for" + o1).innerHTML = " ";
				$("#createActivitySubmit").attr("disabled", false);
			}
		}
	</script>











	<script type="text/javascript">
		$.validator.setDefaults({
			highlight : function(a) {
				$(a).closest(".form-group").removeClass("has-success")
						.addClass("has-error")
			},
			success : function(a) {
				a.closest(".form-group").removeClass("has-error").addClass(
						"has-success")
			},
			errorElement : "span",
			errorPlacement : function(a, b) {
				if (b.is(":radio") || b.is(":checkbox")) {
					a.appendTo(b.parent().parent().parent())
				} else {
					a.appendTo(b.parent())
				}
			},
			errorClass : "help-block m-b-none",
			validClass : "help-block m-b-none"
		});
		$()
				.ready(
						function() {
							$("#commentForm").validate();
							var a = "<i class='fa fa-times-circle'></i> ";
							$("#CrateActivityForm")
									.validate(
											{
												rules : {
													forderActivityName : {
														required : true
													},
													boundCompany : {
														required : true,
														remote : {
															url : "${pageContext.request.contextPath}/forderActivity/findCompanyType",
															type : "POST",
															data : {
																boundCompany : function() {
																	return $(
																			"#boundCompany")
																			.val();
																}
															},
															dataType : "json",
															dataFilter : function(
																	data, type) {

																var jsondata = $
																		.parseJSON(data);
																if (jsondata.status == 200) {
																	$(
																			"#belongTo")
																			.show();

																	if (jsondata.data == "JICHENG") {

																		$(
																				"#DIRECTLYUTIS")
																				.each(
																						function() {
																							this.checked = false;
																						});
																		$(
																				"#edDIRECTLYUTIS")
																				.hide();
																		$(
																				"#edBASEUTIS")
																				.show();
																	}
																	if (jsondata.data == "ZHISHU") {
																		$(
																				"#BASEUTIS")
																				.each(
																						function() {
																							this.checked = false;
																						});
																		$(
																				"#edBASEUTIS")
																				.hide();
																		$(
																				"#edDIRECTLYUTIS")
																				.show();
																	}

																	return true;
																}
																$("#belongTo")
																		.hide();
																return false;
															}
														},

													},
													activityTime : {
														date : true,
														required : true,
													},
													/* 	sumPotoCount : {
															required : true,
															digits : true
														}, */
													type : {
														required : true,
													}
												},
												messages : {
													forderActivityName : {
														required : a
																+ "请输入活动名称"
													},
													boundCompany : a
															+ "请选择所属单位",
													activityTime : {
														date : a
																+ "请输入正确的日期,例：2017-01-01",
														required : a
																+ "请选择拍摄时间"
													},
													/* sumPotoCount : {
														required : a + "请输入上传图片最大数量",
														digits : a + "请输入正确的数字"
													}, */
													type : {
														required : ""
													}
												},
												submitHandler : function(form) { // 验证通过后的执行方法
													// 当前的form通过ajax方式提交（用到jQuery.Form文件）
													$
															.ajax({
																type : "POST",
																url : "${pageContext.request.contextPath}/forderActivity/creatOrEditActivity",
																data : $(
																		"#CrateActivityForm")
																		.serialize(),
																dataType : "json",
																success : function(
																		data) {
																	if (data.status == 200) {
																		$("#CrateActivityForm")[0]
																				.reset()
																		/* 	$("#modelLabel").html("信息提示");
																			$("#modelContent").html(
																					"<center>" + data.msg
																							+ "</center>");
																			$('#titleModel').modal('show'); */
																		location
																				.reload(1000);
																		//$("#CrateActivityForm").modal("hide");
																	} else {
																		$(
																				"#modelLabel")
																				.html(
																						"信息提示");
																		$(
																				"#modelContent")
																				.html(
																						"<center>"
																								+ data.msg
																								+ "</center>");
																		$(
																				'#titleModel')
																				.modal(
																						'show');
																	}
																}
															});
												}
											});
						});
	</script>



</body>
</html>