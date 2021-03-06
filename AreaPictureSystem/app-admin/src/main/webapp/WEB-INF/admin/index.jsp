<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page isELIgnored="false"%>
<!doctype html>
<html lang="en" class="app">
<head>
<meta charset="utf-8" />
<title>图片库系统后台</title>


<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="description" content="">
<meta name="keywords" content="index">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />
<meta name="apple-mobile-web-app-title" content="WeChat" />

<!-- <style type="text/css">
.box {
	width: 650px;
	height: 500px;
	overflow: hidden;
	margin: 0 auto;
}
</style> -->
</head>

<!-- 动态表格 -->
<link
	href="${pageContext.request.contextPath}/assets/admin/css/plugins/dataTables/dataTables.bootstrap.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/assets/admin/js/plugins/datapicker/css/bootstrap-datetimepicker.min.css"
	rel="stylesheet" media="screen">
<script type="text/javascript">
	function clearSelect() {
		//设置查询时间温控
		$("#start").val('');
		$("#end").val('');
		/* 	//设置选择查询条件选中项为空
			$("#searchQuery1 option").each(function() {
				if ($(this).val() == '') {
					$(this).attr("selected", true);
				}
			});
			
			//设置选择查询条件选中项为空
			$("#searchCompany1 option").each(function() {
				if ($(this).val() == '') {
					$(this).attr("selected", true);
				}
			});
		 */

	}

	function clearSelectMonth() {

		$("#month option").each(function() {
			if ($(this).val() == '') {
				$(this).attr("selected", true);
			}
		});

	}
</script>




<body>

	<div id="wrapper">

		<!-- .aside left menu -->
		<%@include file="public/left.jsp"%>

		<div id="page-wrapper" class="gray-bg dashbard-1">
			<!-- .aside top jsp -->
			<%@include file="public/top.jsp"%>

			<div class="wrapper wrapper-content">

				<div class="row">
					<!-- 内容 -->
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								<div class="row">
									<div class="clients-list">
										<ul class="nav nav-tabs">
											<li class="active"><a data-toggle="tab" href="#tab-1"
												id="a"><i class="fa fa-folder-open-o"></i>活动列表</a></li>
											<li class=""><a data-toggle="tab" href="#tab-2" id="b"><i
													class="fa fa-bar-chart-o"></i>上传统计</a></li>
										</ul>
									</div>


									<div class="tab-content">

										<div id="tab-1" class="tab-pane active">

											<div class="row">
												<div class="col-sm-12" style="margin-top: 10px;">
													<div class="ibox float-e-margins">
														<!-- 引入最新的活动 -->

														<c:if test="${not empty forderActivityList }">

															<%@include file="index_forder.jsp"%>

														</c:if>

														<c:if test="${not empty resourcelist }">

															<%@include file="index_resource.jsp"%>

														</c:if>
													</div>


												</div>

											</div>


										</div>
										<div id="tab-2" class="tab-pane">


											<div class="ibox-content" style="margin-top: 10px;">
												<div class="row">

													<div class="col-md-3">

														<div class="form-group">
																<select class="form-control m-b" name="month"
																	onchange="return clearSelect()" id="month">
																	<option value="">选择月数</option>
																	<option value="1">1个月</option>
																	<option value="2">2个月</option>
																	<option value="3">3个月</option>
																	<option value="4">4个月</option>
																	<option value="5">5个月</option>
																	<option value="6">6个月</option>
																	<option value="7">7个月</option>
																	<option value="8">8个月</option>
																	<option value="9">9个月</option>
																	<option value="10">10个月</option>
																	<option value="11">11个月</option>
																	<option value="12">12个月</option>
																</select>
														</div>
													</div>

													<div class="col-md-5">

														<div class="form-group">
															<a class="btn btn-white btn-bitbucket flip" id="more"
																onclick="return more();"> <i
																class="fa fa-toggle-down">&nbsp;&nbsp;更多查询条件</i>
															</a>
															<button class="btn btn-primary" type="button"
																onclick="return tosearch();">查询</button>
															<button class="btn btn-primary" type="button"
																onclick="return todownload();">导出excel</button>

														</div>
													</div>
												</div>


												<div class="row panel" style="display: none;"
													id="more_panel">

													<div class="row">
														<div class="col-md-4">
															<div class="form-group">
																<div class="col-sm-6">
																	<select class="form-control" name="searchQuery1"
																		id="searchQuery1" onchange="return search1();">
																		<option value="">----请选择查询条件----</option>
																		<option value="forderActivityName">活动名称</option>
																		<option value="companySort">企业排行</option>
																		<option value="companyName">企业名称</option>
																	</select>
																</div>

																<div class="col-sm-6" id="search1"
																	style="display: none;">

																	<input type="text" placeholder="请输入查询条件"
																		name="searchVal1" id="searchVal1"
																		class="form-control " style="display: none"> <select
																		class="form-control" name="searchCompany1"
																		id="searchCompany1" style="display: none">
																		<option value="">----请选择要查询的企业----</option>
																		<c:forEach items="${companyList }" var="company"
																			varStatus="status">
																			<option value="${company.id }">${company.name}</option>
																		</c:forEach>

																	</select>
																</div>
															</div>
														</div>

													</div>


													<div class="row" style="margin-top: 10px;">
														<div class="col-md-4">
															<div class="form-group">
																<div class="col-sm-6">
																	<input type="text" placeholder="开始时间" name="start"
																		id="start" readonly="readonly"
																		onchange="return clearSelectMonth();"
																		class="form-control datainput"
																		data-date-format="yyyy-mm-dd">
																</div>

																<div class="col-sm-6">
																	<input type="text" placeholder="结束时间" name="end"
																		id="end" readonly="readonly"
																		onchange="return clearSelectMonth();"
																		class="form-control datainput"
																		data-date-format="yyyy-mm-dd">
																</div>
															</div>
														</div>

														<div class="col-md-4">
															<div class="form-group">
																<button class="btn  btn-white"
																	onclick="return toclean();">
																	<i class="fa fa-times"> </i>清除日期
																</button>
															</div>
														</div>
													</div>

												</div>


											</div>
											<div class="ibox-content">
												<div class="row">
													<table
														class="table table-striped table-bordered table-hover dataTables-example">
														<thead>
															<tr>
																<th>上传排行榜</th>
																<c:if test="${empty companySort}">
																	<th>上传者</th>
																</c:if>
																<th>单位</th>
																<th>上传数量</th>
																<th>创建活动数</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach items="${listsort}" var="item"
																varStatus="status">
																<tr>
																	<th>${item.sortnum }</th>
																	<c:if test="${empty companySort}">
																		<th>${item.name }</th>
																	</c:if>
																	<th>${item.adminUser.adminCompany.name }</th>
																	<th>${item.uploadnum }</th>
																	<th>${item.forderActivityNum }</th>
																</tr>
															</c:forEach>
														</tbody>

													</table>
												</div>
											</div>
										</div>
									</div>

								</div>
							</div>
						</div>

					</div>


				</div>
			</div>
			<%@include file="public/botton.jsp"%>
		</div>
	</div>


	<!-- 动态数据表格，前台查询 -->
	<script>
		$(document).ready(function() {
			$('.summernote').summernote({
				lang : 'zh-CN',
				onImageUpload : function(files, editor, $editable) {
					sendFile(files[0], editor, $editable);
				}
			});
			$('.dataTables-example').dataTable();//表格

		});
	</script>



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
		$(document).ready(function() {
			$(".flip").click(function() {
				$(".panel").slideToggle("slow");
			});

		});
	</script>

	<script type="text/javascript">
		function search1() {
			var search1val = $("#searchQuery1").find("option:selected").val();
			if (search1val == "") {
				$("#search1").hide();
				$("#searchVal1").hide();
				$("#searchCompany1").hide();
				$("#searchCompany1").val('');
				$("#searchVal1").val('');
			} else if (search1val == 'companyName') {

				$("#search1").show();
				$("#searchVal1").hide();
				$("#searchVal1").val('');
				$("#searchCompany1").show();

			} else if (search1val == 'forderActivityName') {

				$("#search1").show();
				$("#searchVal1").show();
				$("#searchCompany1").hide();
				$("#searchCompany1").val('');

			} else {
				$("#searchCompany1").hide();
				$("#searchCompany1").val('');
				$("#search1").hide();
				$("#searchVal1").hide();
			}
		}

		function toclean() {
			$("#start").attr("placeholder", "开始时间");
			$("#start").val('');
			$("#end").attr("placeholder", "结束时间");
			$("#end").val('');
		}

		function tosearch() {

			var month = $("#month").val();
			var searchQuery1 = $("#searchQuery1").val();
			var search1val = null;

			if (searchQuery1 == 'companySort') {
				search1val = "allCompany";
			} else if (searchQuery1 == 'companyName') {
				search1val = $("#searchCompany1").find("option:selected").val();
			} else if (searchQuery1 == 'forderActivityName') {
				search1val = $("#searchVal1").val();
			}

			var start = $("#start").val();
			var end = $("#end").val();

			var data = "month=" + month + "&" + searchQuery1 + "=" + search1val
					+ "&start=" + start + "&end=" + end + "&type=1";

			window.location.href = "${pageContext.request.contextPath}/adminUser/index?"
					+ data;

		}
		function todownload() {
			var month = $("#month").val();
			var searchQuery1 = $("#searchQuery1").val();
			var search1val = null;
			if (searchQuery1 == 'companySort') {
				search1val = "allCompany";
			} else if (searchQuery1 == 'companyName') {
				search1val = $("#searchCompany1").find("option:selected").val();
			} else if (searchQuery1 == 'forderActivityName') {
				search1val = $("#searchVal1").val();
			}
			var start = $("#start").val();
			var end = $("#end").val();

			var data = "month=" + month + "&" + searchQuery1 + "=" + search1val
					+ "&start=" + start + "&end=" + end + "&type=1";

			window.location.href = "${pageContext.request.contextPath}/adminUser/toexport?"
					+ data;

		}
	</script>


	<script type="text/javascript">
		var companyId = '${companyId}';
		var companySort = '${companySort}';
		var forderActivityName = '${forderActivityName}';
		var month = '${month}';
		var start = '${start}';
		var end = '${end}';

		if (month != "") {
			$("#month").find("option[value=" + month + "]").attr("selected",
					"selected");
		}

		if (companySort != "" || companyId != "" || forderActivityName != ""
				|| start != "" || end != "") {
			$(".panel").show();
			$("#search1").show();

			if (companySort != '') {

				$("#searchQuery1").find("option[value=companySort]").attr(
						"selected", "selected");
			}

			else if (companyId != "") {
				$("#searchCompany1").show();
				$("#searchQuery1").find("option[value=companyName]").attr(
						"selected", "selected");
				$("#searchCompany1").find("option[value=" + companyId + "]")
						.attr("selected", "selected");
			} else if (forderActivityName != "") {
				$("#searchQuery1").find("option[value=forderActivityName]")
						.attr("selected", "selected");
				$("#searchVal1").show();
				$("#searchVal1").val(forderActivityName)
			}

			$("#start").val(start);
			$("#end").val(end);
		}
	</script>


	${triggerClick }

</body>
</html>
