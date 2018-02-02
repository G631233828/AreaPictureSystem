<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page isELIgnored="false"%>
<!doctype html>
<html class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>顶部</title>
<link
	href="${pageContext.request.contextPath}/assets/admin/js/plugins/datapicker/css/bootstrap-datetimepicker.min.css"
	rel="stylesheet" media="screen">
</head>
<script type="text/javascript">
	function tocleanForm() {
		$("#edAREA").show();
		$("#edDIRECTLYUTIS").show();
		$("#edBASEUTIS").show();
		$("#edPERSION").show();

		$("#AREA").each(function(){  
		     this.checked=false;
		}); 
		$("#DIRECTLYUTIS").each(function(){  
		     this.checked=false;
		}); 
		$("#BASEUTIS").each(function(){  
		     this.checked=false;
		}); 
		$("#PERSION").each(function(){  
		     this.checked=false;
		}); 
		$("#boundCompany option:first").prop("selected", 'selected');  

		
		$("#signupForm")[0].reset();
		 
		$("#toModal-form").trigger("click");

	}
</script>


<body>
	<div class="row border-bottom">
		<nav class="navbar navbar-static-top" role="navigation"
			style="margin-bottom: 0">
			<div class="navbar-header">
				<!--  显示与隐常左边的菜单 -->
				<button class=" minimalize-styl-2 btn btn-primary"
					onclick="return tocleanForm();" style="width: 200px;">
					<i class="fa fa-plus">&nbsp;&nbsp;<b>创建活动 </b></i>
				</button>
				<a class=" minimalize-styl-2 btn btn-primary" id="toModal-form"
					data-toggle="modal" style="display: none;"
					data-target="#CrateActivity" style="width: 200px;"> </a>

		
			</div>
			
					<form role="search" class="navbar-form-custom " method="post" 
					action="${pageContext.request.contextPath}/photoMessageAction/searchImgsByQuerys">

					<div class="form-group ">
						<input type="text" placeholder="请输入您需要查找的内容 …"
							class="form-control" name="selectQuery"  id="selectQuery" value="${selectQuery}">
					</div>


				</form>
			
			<ul class="nav navbar-top-links navbar-right">
				<li><span class="m-r-sm text-muted welcome-message"> <a
						href="${pageContext.request.contextPath}/adminUser/index"
						title="返回首页"> <i class="fa fa-home"></i></a>
				</span></li>

				<li><a
					href="${pageContext.request.contextPath}/adminUser/loginOut"
					target="mainframer"> <i class="fa fa-sign-out"></i> 退出
				</a></li>
			</ul>

		</nav>
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
					<h4 class="modal-title">创建活动／文件夹</h4>
				</div>

				<form method="post" id="signupForm"
					action="#">
					<div class="modal-body">
						<div class="form-group">
							<label>活动名称：</label><label for="forderActivityName"
								id="forforderActivityName" class="control-label"
								style="color: red; float: right;"></label> <input type="text"
								placeholder="请输入活动名称" name="forderActivityName"
								onkeyup="return getrepletes('forderActivityName');"
								id="forderActivityName" class="form-control" required>
						</div>
						<input type="hidden" id="forderActivityNamehid">

						<div class="form-group">
							<label>活动地址：</label> <input type="text" placeholder="活动地址"
								name="address" id="address" class="form-control">
						</div>


						<!-- 	<div class="form-group">
									<label>图片上传最大数量：</label> <input type="text"
										placeholder="图片上传最大数量" name="sumPotoCount" id="sumPotoCount"
										class="form-control" required>
								</div> -->

						<div class="form-group"
							onchange="return getrepletes('forderActivityName');">
							<label>活动所属学校：</label> <select class="form-control m-b" onchange="return getrepletes('forderActivityName');"
								name="boundCompany" id="boundCompany" required="required">
								<option id="default"  selected="selected">----请选择所属学校----</option>
								<c:forEach items="${company}" var="item" varStatus="status">
									<option id="${item.id}" value="${item.id}">${item.name}</option>
								</c:forEach>
							</select>
						</div>



						<div class="form-group">
							<label>活动时间：</label> <input type="text" placeholder="活动时间"
								name="activityTime" id="activityTime" readonly="readonly"
								onchange="return getrepletes('forderActivityName');"
								class="form-control datainput" data-date-format="yyyy-mm-dd"
								required>
						</div>


						<div class="form-group">
							<label>活动所属：${item.listType}</label>
							<div class="checkbox checkbox-inline" id="edAREA">
								<input type="checkbox" id="AREA" value="AREA" name="type"
									onclick="return getrepletes('forderActivityName');"> <label
									for="AREA"> 区域 </label>
							</div>
							<div class="checkbox checkbox-inline" id="edDIRECTLYUTIS">
								<input type="checkbox" id="DIRECTLYUTIS" value="DIRECTLYUTIS" 
									onclick="return getrepletes('forderActivityName');"
									name="type"> <label for="DIRECTLYUTIS"> 直属单位 </label>
							</div>
							
							<div class="checkbox checkbox-inline" id="edBASEUTIS">
								<input type="checkbox" id="BASEUTIS" value="BASEUTIS" 
									onclick="return getrepletes('forderActivityName');"
									name="type"> <label for="BASEUTIS"> 基层单位</label>
							</div>
							<div class="checkbox checkbox-inline" id="edPERSION">
								<input type="checkbox" id="PERSION" value="PERSION" name="type"
									onclick="return getrepletes('forderActivityName');"> <label
									for="PERSION"> 个人 </label>
							</div>
						</div>



						<div class="form-group">
							<label>描述：</label>
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
						<button type="submit" id="createActivitySubmit" class="btn btn-primary">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- 信息提示模态框（Modal） -->
	<div class="modal fade" id="titleModel" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"
		style="margin-top: 10%;">
		<div class="modal-dialog" style="width: 300px; height: 200px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="modelLabel"></h4>
				</div>
				<div class="modal-body" id="modelContent"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">确定
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
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
			var types="";
			var r1 = $("#" + o1).val();//获取需要判断是否重复的属性
			var r2 = $("#" + o1 + "hid").val();//该值的隐藏域值 判断如果是原始值则不变

			var boundCompany = $("#boundCompany").val();
			
			if (boundCompany == null) {
				boundCompany = "";
			}
			var type = $("input[name='type']:checked");
			
			if (type == null) {
				types = "";
			}else{
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
									document.getElementById("for" + o1).innerHTML = msg.msg;
									document.getElementById("for" + o1).style.cssText = "float: right; color: red;";
									$("#createActivitySubmit").attr("disabled", "true");
								} else {
									$("#createActivitySubmit").attr("disabled", false);
									document.getElementById("for" + o1).innerHTML = " ";
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
		$().ready(function() {
			$("#commentForm").validate();
			var a = "<i class='fa fa-times-circle'></i> ";
			$("#signupForm").validate({
				rules : {
					forderActivityName : "required",
					boundCompany : "required",
					activityTime : {
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
					forderActivityName : a + "请输入活动名称",
					boundCompany : a + "请选择所属单位",
					activityTime : {
						required : a + "请选择活动时间"
					},
					/* sumPotoCount : {
						required : a + "请输入上传图片最大数量",
						digits : a + "请输入正确的数字"
					}, */
					type : {
						required : ""
					}
				},submitHandler : function(form) { // 验证通过后的执行方法
					// 当前的form通过ajax方式提交（用到jQuery.Form文件）
					$.ajax({
						type : "POST",
						url : "${pageContext.request.contextPath}/forderActivity/creatOrEditActivity",
						data : $("#signupForm").serialize(),
						dataType : "json",
						success : function(data) {
							if (data.status == 200) {
								$("#signupForm")[0].reset()
							/* 	$("#modelLabel").html("信息提示");
								$("#modelContent").html(
										"<center>" + data.msg
												+ "</center>");
								$('#titleModel').modal('show'); */
								location.reload(1000);
								//$("#signupForm").modal("hide");
							} else {
								$("#modelLabel").html("信息提示");
								$("#modelContent").html(
										"<center>" + data.msg
												+ "</center>");
								$('#titleModel').modal('show');
							}
						}
					});
				}
			});
		});
	</script>



</body>
</html>