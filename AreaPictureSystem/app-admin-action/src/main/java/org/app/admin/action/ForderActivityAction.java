/**   
* @Title: ForderActivity.java 
* @Package org.app.admin.action 
* @Description: TODO(用一句话描述该文件做什么) 
* @author fliay
* @date 2017年11月20日 下午3:51:10 
* @version V1.0   
*/
package org.app.admin.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.app.admin.pojo.ForderActivity;
import org.app.admin.service.ForderActivityService;
import org.app.admin.service.ResourceService;
import org.app.framework.action.GeneralAction;
import org.app.framework.util.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @ClassName: ForderActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fliay
 * @date 2017年11月20日 下午3:51:10
 * 
 */
@Controller
@RequestMapping("/forderActivity")
public class ForderActivityAction extends GeneralAction<ForderActivity> {

	private static final Logger log = LoggerFactory.getLogger(ForderActivityAction.class);

	@Autowired
	private ForderActivityService forderActivityService;

	@Autowired
	private ResourceService resourceService;

	/**
	 * 
	 * @Title: list @Description: TODO(这里用一句话描述这个方法的作用) @param @param
	 * session @param @param parentId 父文件夹id @param @param id
	 * 当前文件夹id @param @return 设定文件 @return ModelAndView 返回类型 @throws
	 */
	@RequestMapping("/list")
	public ModelAndView list(HttpSession session, @ModelAttribute("parentId") String parentId,
			@RequestParam(value = "id", defaultValue = "") String id) {
		log.info("查询所有的文件夹");

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("admin/app-admin/fileSystem/list");

		// 查询所有文件夹，包括子目录
		List<ForderActivity> list = this.forderActivityService.listForderActivity(parentId, id);

		modelAndView.addObject("listForderActivity", list);
		if (list!=null) {
			for (ForderActivity f : list) {
				id = f.getParentId();
			}
		}
		
		if (Common.isNotEmpty(id)&&!("0").equals(id)) {
			modelAndView.addObject("id", id);
			// 当前目录
			ForderActivity forderActivity = this.forderActivityService.findForderById(id);

			if (forderActivity != null) {

				modelAndView.addObject("forderActivity", forderActivity);

			}

			// 上级目录

				ForderActivity parentForderActivity = this.forderActivityService
						.findForderById(forderActivity.getParentId());

				if (parentForderActivity != null) {

					modelAndView.addObject("parentForderActivity", parentForderActivity);

			}
		}else if(Common.isNotEmpty(parentId)&&Common.isEmpty(id)){
			//直接获取上级目录
			ForderActivity parentForderActivity = this.forderActivityService
					.findForderById(parentId);

			if (parentForderActivity != null) {

				modelAndView.addObject("parentForderActivity", parentForderActivity);
			}
		}
		return modelAndView;
	}

	/**
	 * 
	 * @Title: createForderActivity @Description: TODO(添加文件夹) @param @param
	 *         forderActivity ForderActivity对象 @param @param Enumtype
	 *         接受界面的枚举枚举值 @param @return 设定文件 @return ModelAndView 返回类型 @throws
	 */
	@RequestMapping("/createForder")
	public ModelAndView createForderActivity(HttpSession session,
			@ModelAttribute("forderActivityName") ForderActivity forderActivity,
			@RequestParam(value = "Enumtype", defaultValue = "") String Enumtype, 
			@RequestParam(value = "editid", defaultValue = "") String editid,RedirectAttributes model) {
		log.info("进行编辑文件夹操作");
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("redirect:/forderActivity/list");


		String message = this.forderActivityService.createForder(forderActivity, Enumtype, session,editid);
		
		if (Common.isNotEmpty(forderActivity.getParentId())) {
			
			model.addFlashAttribute("parentId", forderActivity.getParentId());
			
		}
		log.info(message);

		return modelAndView;
	}
	
	
	
	/**
	 * 
	* @Title: delete 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param model
	* @param @param id  删除的id包括集合
	* @param @param parentId   删除后需要显示的文件夹
	* @param @return    设定文件 
	* @return ModelAndView    返回类型 
	* @throws
	 */
	@RequestMapping("/delete")
	public ModelAndView delete(RedirectAttributes model,
			@RequestParam(value="id",defaultValue="")String id,
			@RequestParam(value="parentId",defaultValue="")String parentId
			){
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("redirect:/forderActivity/list");
		
		if(Common.isNotEmpty(id)){
			
			this.forderActivityService.delete(id);
			
		}
			if (Common.isNotEmpty(parentId)) {
			
			model.addFlashAttribute("parentId", parentId);
		}
		
		return modelAndView;
		
	}
	
	

	/**
	 * 通过ajax获取相同目录下是否存在重复文件夹名称的信息
	 * @param printWriter
	 * @param session
	 * @param response
	 */
	//TODO
	@RequestMapping(value = "/ajaxgetRepletes", method = RequestMethod.POST) 
    public void ajaxgetRepletes(@RequestParam(value = "roleName", defaultValue = "")String roleName,PrintWriter printWriter,HttpSession session,HttpServletResponse response) { 
		//通过ajax获取
		if(!roleName.equals("")){
		/*	Role  role;
			try {
				role = this.roleService.findRoleByRoleName(roleName);
				if(role!=null){
					printWriter.write("true");
				}else{
					printWriter.write("false");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  printWriter.flush(); 
		        printWriter.close(); */
		}
    }
	
	
	
	
	

}
