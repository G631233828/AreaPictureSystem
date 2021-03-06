package org.app.webAdmin.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.app.framework.action.GeneralAction;
import org.app.framework.util.BasicDataResult;
import org.app.framework.util.Common;
import org.app.framework.util.CommonEnum;
import org.app.webAdmin.pojo.AdminRole;
import org.app.webAdmin.pojo.AdminUser;
import org.app.webAdmin.pojo.Contest;
import org.app.webAdmin.pojo.Statistics;
import org.app.webAdmin.pojo.UsersUploads;
import org.app.webAdmin.service.AdminRoleService;
import org.app.webAdmin.service.AdminUserService;
import org.app.webAdmin.service.ContestImagesService;
import org.app.webAdmin.service.ContestService;
import org.app.webAdmin.service.UsersUploadsService;
import org.app.webAdmin.util.BaseType.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/adminUser")
public class AdminUserAction extends GeneralAction<AdminUser> {
	private static final Logger log = LoggerFactory.getLogger(AdminUserAction.class);

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private UsersUploadsService usersUploadsService;
	@Autowired
	private ContestImagesService contestImagesService;
	@Autowired
	private ContestService contestService;


	/**
	 * 用户查询
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView list(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/app-admin/user/list");
		Query query = new Query();
		try {
			AdminUser adminUser = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);
			
			modelAndView.addObject("pageList", this.adminUserService.find(query, AdminUser.class));
		} catch (Exception e) {
			log.error("select 'AdminUser' table error ");
		}
		return modelAndView;// 返回
	}

	/**
	 * 编缉 或 添加
	 * 
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping("/editor")
	public ModelAndView editor(HttpSession session, String id) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/app-admin/user/editor");
		try {
			if (id != null && id != "") {
				log.info("come!");
				modelAndView.addObject("bean", this.adminUserService.findOneById(id, AdminUser.class));
			}
			// 加载所有的角色信息
			modelAndView.addObject("role", this.adminRoleService.find(new Query(), AdminRole.class));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return modelAndView;// 返回
	}
	
	
	
	@RequestMapping("/createOrUpdateToFind")
	public ModelAndView list(HttpSession session, AdminUser adminUser, String roleId,String type) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/adminUser/list");
		
		if(Common.isNotEmpty(type)){
			if(type.equals("USER")){
				adminUser.setUserType(UserType.USER);
			}else if(type.equals("MANAGER")){
				adminUser.setUserType(UserType.MANAGER);
			}else if(type.equals("ADMINISTRATORS")){
				adminUser.setUserType(UserType.ADMINISTRATORS);
			}else{
				adminUser.setUserType(null);
			}
			
		}
		
		// git AdminRole and AdminCompany
		if (roleId != null)
			adminUser.setAdminRole(this.adminRoleService.findOneById(roleId, AdminRole.class));
		try {
			if (adminUser != null) {
				if (adminUser.getId() == null) {// create or update
					// check AdminUser.userName
					if (this.adminUserService.findCountByQuery(
							super.craeteQueryWhere("userName", adminUser.getUserName()), AdminUser.class) > 0) {
						session.setAttribute("error", "添加失败，您添加的帐号信息已经存在。");
					} else {
						this.adminUserService.insert(adminUser);
					}
				} else
					this.adminUserService.save(adminUser);
			}
			log.info(adminUser.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return modelAndView;// 返回
	}

	/**
	 * 后台登录
	 * 
	 * @return
	 */

	@RequestMapping("/login")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/login");// 登录页面
		return modelAndView;// 返回
	}

	/**
	 * 后台登录
	 * 
	 * @param session
	 * @return ModelAndView
	 */
	@RequestMapping("/checkLogin")
	@ResponseBody
	public BasicDataResult checkLogin(HttpSession session, String username, String password) {
		
		Query query = new Query();
		
		query.addCriteria(Criteria.where("userName").is(username)).addCriteria(Criteria.where("password").is(password));
		
		// 检查帐号登录
		AdminUser au = this.adminUserService.findOneByQuery(
				super.craeteQueryWhere("userName", username, "password", password), AdminUser.class);
		if (au != null) {// 不等于空,保存用户帐号信息
			// 加载权限
			session.setAttribute("listMenu", au.getAdminRole().getListMenu());
			session.setAttribute(CommonEnum.USERSESSION, au);
			return BasicDataResult.build(200, "登录成功", true);
		}
		
		return BasicDataResult.build(203, "用户名或密码错误", false);
	}
/*	@RequestMapping("/checkLogin")
	public ModelAndView checkLogin(HttpSession session, String username, String password) {
		ModelAndView modelAndView = new ModelAndView();
		//modelAndView.setViewName("redirect:/adminUser/index");
		modelAndView.setViewName("redirect:/adminUser/index");
		// 清除菜单
		session.removeAttribute(CommonEnum.WEBMENUSESSION);
		try {
			// 检查帐号登录
			AdminUser au = this.adminUserService.findOneByQuery(
					super.craeteQueryWhere("userName", username, "password", password), AdminUser.class);
			if (au != null) {// 不等于空,保存用户帐号信息
				// 加载权限
				session.setAttribute("listMenu", au.getAdminRole().getListMenu());
				session.setAttribute(CommonEnum.USERSESSION, au);
				
			} else {
				// 返回到登录。对应跟目录地址
				modelAndView.setViewName("redirect:/adminUser/login");
			}
		} catch (Exception e) {
			log.error("用户登录异常：" + e.toString());
		}
		return modelAndView;// 返回
	}
*/
	/**
	 * 登录成功后，重定向
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/index");
		
		List<Statistics> lists = new ArrayList<Statistics>();
		
		
		//获取所有的比赛信息
		List<Contest> listContest = this.contestService.find(new Query(), Contest.class);
		
		for(Contest c:listContest){
			Statistics st = new Statistics();
			
			Query query = new Query();
			
			query.addCriteria(Criteria.where("contestId").is(c.getId()));
			
			List<UsersUploads> listUsersUploads = this.usersUploadsService.find(query, UsersUploads.class);
			
			st.setContest(c);
			st.setUsersUploads(listUsersUploads);
			int polls = 0;
			
			for(UsersUploads up:listUsersUploads){
				polls+=up.getPoll();
			}
			st.setPolls(polls);
			lists.add(st);
			
		}
		
		modelAndView.addObject("lists", lists);
			
			return modelAndView;// 返回
	}



	/**
	 * 注销
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/loginOut")
	public ModelAndView loginOut(HttpSession session) {

		ModelAndView modelAndView = new ModelAndView();
		// 重定向到登录页面。
		modelAndView.setViewName("redirect:/adminUser/login");
		// 注销session(后台登录）
		session.removeAttribute(CommonEnum.USERSESSION);

		return modelAndView;// 返回
	}

	/***
	 * 
	 * @Title: delete @Description: TODO(删除用户) @param @param id @param @return
	 * 设定文件 @return ModelAndView 返回类型 @throws
	 */
	@RequestMapping("/delete")
	public ModelAndView delete(@RequestParam(defaultValue = "", value = "id") String id) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("redirect:/adminUser/list");

		String delIds[] = id.split(",");

		for (int i = 0; i < delIds.length; i++) {
			if (Common.isNotEmpty(delIds[i])) {

				AdminUser adminUser = this.adminUserService.findOneById(delIds[i], AdminUser.class);

				if (adminUser != null) {

					this.adminUserService.remove(adminUser);

				}
			}

		}

		return modelAndView;

	}
	
	
	
	
	/**
	 * 
	 * @Title: checkPassword @Description: TODO(这里用一句话描述这个方法的作用) @param @return
	 * 设定文件 @return BasicDataResult 返回类型 @throws
	 */

	@RequestMapping("/checkPassword")
	@ResponseBody
	public BasicDataResult checkPassword(@RequestParam(defaultValue = "", value = "password") String password,
			HttpSession session) {
		BasicDataResult result = this.adminUserService.passwordByUserId(session, password);
		return result;

	}
	
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public BasicDataResult updatePassword(HttpSession session,@RequestParam(defaultValue="",value="password")String password){
		
		
		BasicDataResult result = this.adminUserService.updatePassword(session, password);
		
		if(result.getStatus().equals(200)){
			// 注销session(后台登录）
			session.removeAttribute(CommonEnum.USERSESSION);
		}
		
		return result;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
