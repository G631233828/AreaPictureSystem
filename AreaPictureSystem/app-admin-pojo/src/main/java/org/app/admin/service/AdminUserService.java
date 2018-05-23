package org.app.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.app.admin.annotation.SystemErrorLog;
import org.app.admin.pojo.AdminCompany;
import org.app.admin.pojo.AdminUser;
import org.app.admin.util.SortBean;
import org.app.framework.service.GeneralServiceImpl;
import org.app.framework.util.BasicDataResult;
import org.app.framework.util.Common;
import org.app.framework.util.CommonEnum;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * 后台用户管理
 * 
 * @author aaronlau
 *
 */
@Repository("adminUserService")
public class AdminUserService extends GeneralServiceImpl<AdminUser> {

	@Autowired
	private AdminCompanyService adminCompanyService;
	@Autowired
	private StatisticsService statisticsService;

	/**
	 * 
	 * @Title: findAdminUserById @Description:
	 *         TODO(根据用户的Id获取用户的信息。) @param @param id @param @return
	 *         设定文件 @return AdminUser 返回类型 @throws
	 */
	@SystemErrorLog(description = "根据Id查询用户")
	public AdminUser findAdminUserById(String id) {

		AdminUser adminUser = this.findOneById(id, AdminUser.class);

		if (adminUser != null) {
			return adminUser;
		}
		return null;

	}

	/**
	 * 
	 * @Title: findAdminUserByTelPhone @Description:
	 *         TODO(通过用户的手机号查询是否存在该用户) @param @param telPhone @param @return
	 *         设定文件 @return AdminUser 返回类型 @throws
	 */
	@SystemErrorLog(description = "根据手机号查询用户")
	public AdminUser findAdminUserByTelPhone(String telPhone) {

		Query query = new Query();

		query.addCriteria(Criteria.where("tel").is(telPhone));

		AdminUser adminUser = this.findOneByQuery(query, AdminUser.class);

		if (adminUser != null)
			return adminUser;
		else
			return null;

	}

	/**
	 * 
	 * @Title: listUsers @Description: TODO(查询为学校管理员，老师 的用户) @param @return
	 *         设定文件 @return List<AdminUser> 返回类型 @throws
	 */
	public List<AdminUser> listUsers(String companyId) {

		Query query = new Query();

		// 获取所有用户类型为老师跟管理员的用户
		// query.addCriteria(Criteria.where("userType").in(BaseType.UserType.SCHOOLADMIN,
		// BaseType.UserType.TEACHER));

		// 如果企业ID不为空则查询该企业下的所有用户
		if (Common.isNotEmpty(companyId) && !companyId.equals("allCompany")) {
			query.addCriteria(Criteria.where("adminCompany.$id").is(new ObjectId(companyId)));
		}

		List<AdminUser> list = this.find(query, AdminUser.class);

		return list.size() > 0 ? list : null;

	}

	/**
	 * 
	 * @Title: passwordByUserId @Description:
	 *         TODO(判断当前用户输入的密码是否正确) @param @param session @param @param
	 *         password @param @return 设定文件 @return BasicDataResult 返回类型 @throws
	 */
	public BasicDataResult passwordByUserId(HttpSession session, String password) {

		AdminUser adminUser = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);

		if (adminUser != null) {

			AdminUser newAdminUser = this.findOneById(adminUser.getId(), AdminUser.class);

			if (Common.isNotEmpty(password) && newAdminUser.getPassword().equals(password)) {
				// 密码正确
				return BasicDataResult.build(200, "密码正确", null);
			}
		}
		return BasicDataResult.build(0, "密码错误", null);
	}

	/**
	 * 
	 * @Title: updatePassword @Description: TODO(修改密码) @param @param
	 *         session @param @param password @param @return 设定文件 @return
	 *         BasicDataResult 返回类型 @throws
	 */
	public BasicDataResult updatePassword(HttpSession session, String password) {

		AdminUser adminUser = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);

		if (adminUser != null) {

			AdminUser newAdminUser = this.findOneById(adminUser.getId(), AdminUser.class);

			newAdminUser.setPassword(password);
			this.save(newAdminUser);

			return BasicDataResult.build(200, "修改密码成功", null);

		}
		return BasicDataResult.build(0, "修改密码失败", null);

	}

	public HSSFWorkbook export(List<SortBean> listsort, String companyName, String forderActivityName, String start,
			String end, String month) {
		HSSFWorkbook wb = new HSSFWorkbook();
		List<AdminCompany> listCompany = this.adminCompanyService.listCompay();

		if (companyName.equals("allCompany")) {

			if (listsort != null && listsort.size() > 0) {
				// 获取所有企业的统计
				HSSFSheet sheet = wb.createSheet("学校上传统计".toString());

				HSSFCellStyle style = wb.createCellStyle();
				// 设置边框
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				// style.setWrapText(false);// 设置换行
				HSSFFont font = wb.createFont();
				font.setFontName("宋体");
				font.setFontHeightInPoints((short) 9);
				style.setFont(font);
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居
				HSSFRow row = sheet.createRow(0);// 初始化excel第一行
				for (int a = 0; a < 4; a++) {
					HSSFCell cell = row.createCell(a);
					cell.setCellValue("图片库系统上传图片统计");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(1);
				for (int a = 0; a < 4; a++) {
					HSSFCell cell = row.createCell(a);
					cell.setCellValue("统计范围：所有企业");
					sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(2);
				for (int a = 0; a < 4; a++) {
					HSSFCell cell = row.createCell(a);

					if (Common.isNotEmpty(month)) {
						cell.setCellValue("统计月数：" + month);
					} else {
						if (Common.isNotEmpty(start) && Common.isNotEmpty(end)) {

							cell.setCellValue("统计时间段：" + start + "至" + end);
						}else{
							cell.setCellValue("统计时间段：全部" );
						}
					}
					sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 4 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(3);
				for (int a = 0; a < 4; a++) {
					HSSFCell cell = row.createCell(a);
					if (Common.isEmpty(forderActivityName)) {
						cell.setCellValue("统计活动：所有活动");
					} else {
						cell.setCellValue("统计活动：" + forderActivityName);
					}
					sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 4 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(4);
				HSSFCell cell1 = row.createCell(0);
				cell1.setCellStyle(style);
				cell1.setCellValue("上传排行"); // 排行

				cell1 = row.createCell(1);
				cell1.setCellStyle(style);
				cell1.setCellValue("单位"); // 单位

				cell1 = row.createCell(2);
				cell1.setCellStyle(style);
				cell1.setCellValue("上传图片数量"); // 上传数量

				cell1 = row.createCell(3);
				cell1.setCellStyle(style);
				cell1.setCellValue("创建活动数量"); // 上传数量

				int j = 4;// 记录当前的行号
				int line = 1;// 序号
				for (SortBean sb : listsort) {

					row = sheet.createRow(j + 1);
					HSSFCell cell = row.createCell(0);
					cell.setCellStyle(style);
					cell.setCellValue(sb.getSortnum()); // 排行

					cell = row.createCell(1);
					cell.setCellStyle(style);
					cell.setCellValue(sb.getAdminUser().getAdminCompany().getName()); // 单位

					cell = row.createCell(2);
					cell.setCellStyle(style);
					cell.setCellValue(sb.getUploadnum()); // 上传数量

					cell = row.createCell(3);
					cell.setCellStyle(style);
					cell.setCellValue(sb.getForderActivityNum()); // 上传数量
					j++;
				}
				sheet.setDefaultColumnWidth(20);// 设置宽度
				sheet.autoSizeColumn(1, true);
			}

			downloadCompanyExcel(forderActivityName, start, end, month, wb, listCompany);

		} else if (Common.isNotEmpty(companyName) && !companyName.equals("allCompany")) {
			// companyName 不为空说明是一个企业的数据
			List<AdminCompany> list = new ArrayList<AdminCompany>();
			AdminCompany ac = this.adminCompanyService.findOneById(companyName, AdminCompany.class);
			list.add(ac);

			downloadCompanyExcel(forderActivityName, start, end, month, wb, list);

		} else {
			// 导出所有企业的数据
			if (listsort != null && listsort.size() > 0) {
				// 获取所有企业的统计
				HSSFSheet sheet = wb.createSheet("学校上传统计".toString());

				HSSFCellStyle style = wb.createCellStyle();
				// 设置边框
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				// style.setWrapText(false);// 设置换行
				HSSFFont font = wb.createFont();
				font.setFontName("宋体");
				font.setFontHeightInPoints((short) 10);
				style.setFont(font);
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居
				HSSFRow row = sheet.createRow(0);// 初始化excel第一行
				for (int a = 0; a < 5; a++) {
					HSSFCell cell = row.createCell(a);
					cell.setCellValue("图片库系统上传图片统计");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(1);
				for (int a = 0; a < 5; a++) {
					HSSFCell cell = row.createCell(a);
					cell.setCellValue("统计范围：所有企业");
					sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(2);
				for (int a = 0; a < 5; a++) {
					HSSFCell cell = row.createCell(a);

					if (Common.isNotEmpty(month)) {
						cell.setCellValue("统计月数：" + month);
					} else {
						if (Common.isNotEmpty(start) && Common.isNotEmpty(end)) {

							cell.setCellValue("统计时间段：" + start + "至" + end);
						}else{
							cell.setCellValue("统计时间段：全部" );
						}
					}
					sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 5 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(3);
				for (int a = 0; a < 5; a++) {
					HSSFCell cell = row.createCell(a);

					if (Common.isEmpty(forderActivityName)) {

						cell.setCellValue("统计活动：所有活动");
					} else {
						cell.setCellValue("统计活动：" + forderActivityName);
					}
					sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 5 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(4);
				HSSFCell cell1 = row.createCell(0);
				cell1.setCellStyle(style);
				cell1.setCellValue("上传排行"); // 排行

				cell1 = row.createCell(1);
				cell1.setCellStyle(style);
				cell1.setCellValue("上传者"); // 单位

				cell1 = row.createCell(2);
				cell1.setCellStyle(style);
				cell1.setCellValue("单位"); // 单位

				cell1 = row.createCell(3);
				cell1.setCellStyle(style);
				cell1.setCellValue("上传图片数量"); // 上传数量

				cell1 = row.createCell(4);
				cell1.setCellStyle(style);
				cell1.setCellValue("创建活动数量"); // 上传数量

				int j = 4;// 记录当前的行号
				int line = 1;// 序号
				for (SortBean sb : listsort) {

					row = sheet.createRow(j + 1);
					HSSFCell cell = row.createCell(0);
					cell.setCellStyle(style);
					cell.setCellValue(sb.getSortnum()); // 排行

					cell = row.createCell(1);
					cell.setCellStyle(style);
					cell.setCellValue(sb.getAdminUser().getName()); // 上传者

					cell = row.createCell(2);
					cell.setCellStyle(style);
					cell.setCellValue(sb.getAdminUser().getAdminCompany().getName()); // 单位

					cell = row.createCell(3);
					cell.setCellStyle(style);
					cell.setCellValue(sb.getUploadnum()); // 上传数量

					cell = row.createCell(4);
					cell.setCellStyle(style);
					cell.setCellValue(sb.getForderActivityNum()); // 上传数量
					j++;
				}
				sheet.setDefaultColumnWidth(18);// 设置宽度
				sheet.autoSizeColumn(1, true);
			}

			downloadCompanyExcel(forderActivityName, start, end, month, wb, listCompany);

		}

		return wb;
	}

	// 生成所有企业的excel数据
	private void downloadCompanyExcel(String forderActivityName, String start, String end, String month,
			HSSFWorkbook wb, List<AdminCompany> listCompany) {
		if (listCompany.size() > 0) {

			// 遍历所有学校
			for (AdminCompany adminCompany : listCompany) {

				// 获取所有企业的统计
				HSSFSheet sheet = wb.createSheet(adminCompany.getName());

				HSSFCellStyle style = wb.createCellStyle();
				// 设置边框
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				// style.setWrapText(false);// 设置换行
				HSSFFont font = wb.createFont();
				font.setFontName("宋体");
				font.setFontHeightInPoints((short) 9);
				style.setFont(font);
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居
				HSSFRow row = sheet.createRow(0);// 初始化excel第一行
				for (int a = 0; a < 5; a++) {
					HSSFCell cell = row.createCell(a);
					cell.setCellValue(adminCompany.getName() + "图片库系统上传图片统计");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(1);
				for (int a = 0; a < 5; a++) {
					HSSFCell cell = row.createCell(a);
					cell.setCellValue("统计范围：所有企业");
					sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(2);
				for (int a = 0; a < 5; a++) {
					HSSFCell cell = row.createCell(a);

					if (Common.isNotEmpty(month)) {
						cell.setCellValue("统计月数：" + month);
					} else {
						if (Common.isNotEmpty(start) && Common.isNotEmpty(end)) {

							cell.setCellValue("统计时间段：" + start + "至" + end);
						}else{
							cell.setCellValue("统计时间段：全部" );
						}
					}
					sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 5 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(3);
				for (int a = 0; a < 5; a++) {
					HSSFCell cell = row.createCell(a);
					if (Common.isEmpty(forderActivityName)) {
						cell.setCellValue("统计活动：所有活动");
					} else {
						cell.setCellValue("统计活动：" + forderActivityName);
					}
					sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 5 - 1));
					cell.setCellStyle(style);
				}

				row = sheet.createRow(4);
				HSSFCell cell1 = row.createCell(0);
				cell1.setCellStyle(style);
				cell1.setCellValue("上传排行"); // 排行

				cell1 = row.createCell(1);
				cell1.setCellStyle(style);
				cell1.setCellValue("上传者"); // 上传者

				cell1 = row.createCell(2);
				cell1.setCellStyle(style);
				cell1.setCellValue("单位"); // 单位

				cell1 = row.createCell(3);
				cell1.setCellStyle(style);
				cell1.setCellValue("上传图片数量"); // 上传数量

				cell1 = row.createCell(4);
				cell1.setCellStyle(style);
				cell1.setCellValue("创建活动数量"); // 上传数量

				// 获取所有的统计
				Map<AdminUser, Integer> statisticsList = this.statisticsService.findUserUploadsNum(adminCompany.getId(),
						forderActivityName, start, end, month);

				List<SortBean> listCompanySort = null;
				if (statisticsList != null) {
					listCompanySort = this.statisticsService.sortfindUserUploadsNum(statisticsList, "");
				}

				int j = 4;
				if (listCompanySort != null) {
					for (SortBean sb : listCompanySort) {

						row = sheet.createRow(j + 1);
						HSSFCell cell = row.createCell(0);
						cell.setCellStyle(style);
						cell.setCellValue(sb.getSortnum()); // 排行

						cell = row.createCell(1);
						cell.setCellStyle(style);
						cell.setCellValue(sb.getAdminUser().getName()); // 姓名

						cell = row.createCell(2);
						cell.setCellStyle(style);
						cell.setCellValue(sb.getAdminUser().getAdminCompany().getName()); // 单位

						cell = row.createCell(3);
						cell.setCellStyle(style);
						cell.setCellValue(sb.getUploadnum()); // 上传数量

						cell = row.createCell(4);
						cell.setCellStyle(style);
						cell.setCellValue(sb.getForderActivityNum()); // 上传数量
						j++;
					}

				}
				sheet.setDefaultColumnWidth(18);// 设置宽度
				sheet.autoSizeColumn(1, true);

			}
		}
	}

}
