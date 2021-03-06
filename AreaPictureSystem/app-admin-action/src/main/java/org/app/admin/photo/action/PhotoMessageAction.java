package org.app.admin.photo.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.app.admin.annotation.SystemControllerLog;
import org.app.admin.annotation.SystemErrorLog;
import org.app.admin.interceptor.LoginInterceptor;
import org.app.admin.pojo.AdminCompany;
import org.app.admin.pojo.AdminUser;
import org.app.admin.pojo.Favorites;
import org.app.admin.pojo.ForderActivity;
import org.app.admin.pojo.Label;
import org.app.admin.pojo.Resource;
import org.app.admin.pojo.Type;
import org.app.admin.service.FavoritesService;
import org.app.admin.service.ForderActivityService;
import org.app.admin.service.InformationRegisterService;
import org.app.admin.service.LabelService;
import org.app.admin.service.ResourceService;
import org.app.admin.service.TypeService;
import org.app.admin.util.BaseType;
import org.app.admin.util.EditorImgBean;
import org.app.admin.util.FileOperateUtil;
import org.app.admin.util.PhotoTime;
import org.app.admin.util.UploadUtil;
import org.app.admin.util.basetreetime.BaseTreeTime;
import org.app.admin.util.basetreetime.LayerAdmonCompany;
import org.app.admin.util.executor.ExecutorsQueue;
import org.app.framework.action.GeneralAction;
import org.app.framework.util.BasicDataResult;
import org.app.framework.util.Common;
import org.app.framework.util.CommonEnum;
import org.app.framework.util.Pagination;
import org.app.framework.util.ZipCompress;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/photoMessageAction")
public class PhotoMessageAction extends GeneralAction<ForderActivity> {

	private static final Logger log = LoggerFactory.getLogger(PhotoMessageAction.class);

	@Autowired
	private ForderActivityService forderActivityService;
	@Autowired
	private LabelService labelService;// 标签
	@Autowired
	private ResourceService resourceService;// 资源（图片）
	@Autowired
	private org.app.admin.service.AdminCompanyService AdminCompanyService;
	@Autowired
	private FavoritesService favoritesService;
	@Autowired
	private InformationRegisterService informationRegisterService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private ExecutorsQueue executorsQueue;

	/**
	 * 查找图片页面
	 *
	 * @param session
	 * @param type
	 * @param activityIndexId
	 * @return
	 */
	@SystemErrorLog(description = "查询图片出错")
	@RequestMapping("/index/{type}")
	public ModelAndView index(HttpSession session, @PathVariable("type") String type, String activityIndexId) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/photo-gallery/photoMessage/list");
		// 检查类型
		if (!BaseType.checkType(type))
			return null;
		modelAndView.addObject("webType", type);
		AdminUser adminUser = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);

		// TODO 根据type类型，加载不同类型的一级文件夹，然后按时间轴，进行分类。

		Query querylistFA = super.craeteQueryWhere("parentId", "0", "listType.type", type);

		List<ForderActivity> listFA = this.forderActivityService.find(querylistFA, ForderActivity.class);
		// 如果用户是 BASEUTIS

		if (type.equals(BaseType.Type.BASEUTIS.toString())) {

			List<PhotoTime> lpt = PhotoTime.getPhotoTime(listFA, session);
			// 加载所有的企业
			List<AdminCompany> lac = this.AdminCompanyService.find(new Query(), AdminCompany.class);

			modelAndView.addObject("listAdminCompany", lac);
			LoginInterceptor lo = new LoginInterceptor();
			List<LayerAdmonCompany> llac = lo.LayerAdmonCompany(lac, session);
			// List<LayerAdmonCompany> llac =
			// LayerAdmonCompany.LayerAdmonCompany(lac, lpt);
			List<BaseTreeTime> lbpt = BaseTreeTime.getBaseTreeTime(llac, session);
			log.info(lbpt.toString());
			modelAndView.addObject("basePhotoTimeList", lbpt);

		} else if (type.equals(BaseType.Type.PERSION.toString())) {
			modelAndView.addObject("photoTimeList", getPhotoTimeListByPersionId(BaseType.Type.PERSION.toString(), null,
					adminUser.getId(), true, session));
		} else {
			// 按日期进行分类,并且中当前菜单
			// TODO
			modelAndView.addObject("photoTimeList", PhotoTime.getPhotoTime(listFA, session));
		}

		// TODO 如果 type 是 基本层单位，（中学，小学，幼儿园）
		// 标签
		modelAndView.addObject("lableList", labelService.find(new Query(), Label.class));
		// 删除当前活动session
		session.removeAttribute("checkActivityId");

		session.setAttribute("", type);

		return modelAndView;// 返回
	}

	public List<PhotoTime> getPhotoTimeListByPersionId(String type, String check, String boundId, boolean flag,
			HttpSession session) {

		Query query = super.craeteQueryWhere("listType.type", type, "parentId", "0", "boundId", boundId);

		List<ForderActivity> listFA = this.forderActivityService.find(query, ForderActivity.class);

		return PhotoTime.getPhotoTime(listFA, session);
	}

	/**
	 * 选择中对应的主题活动
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param session
	 * @param checkId
	 * @return
	 */
	@SystemErrorLog(description = "选择中对应的主题活动")
	@RequestMapping("/checkActivity/{type}")
	public ModelAndView checkActivity(@PathVariable("type") String type,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "21") Integer pageSize,
			@RequestParam(value = "sort", defaultValue = "DESC") String sort,
			@RequestParam(value = "mfregex", defaultValue = "") String mfregex, HttpSession session,
			@RequestParam(value = "checkId", defaultValue = "") String checkId,
			@RequestParam(value = "year", defaultValue = "") String year,
			@RequestParam(value = "month", defaultValue = "") String month,
			@RequestParam(value = "day", defaultValue = "") String day,
			@RequestParam(value = "nature", defaultValue = "") String nature,
			@RequestParam(value = "company", defaultValue = "") String company) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/photo-gallery/photoMessage/list");

		try {
			nature = java.net.URLDecoder.decode(nature, "utf-8");
			company = java.net.URLDecoder.decode(company, "utf-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		session.setAttribute("yearId", year);
		session.setAttribute("monthId", month);
		session.setAttribute("dayId", day);
		session.setAttribute("companyName", company);
		session.setAttribute("nature", nature);

		String selectActivityId = "";
		// 检查类型
		if (!BaseType.checkType(type))
			return null;
		modelAndView.addObject("webType", type);
		AdminUser adminUser = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);
		// 模糊匹配
		try {
			mfregex = new String(mfregex.getBytes("iso-8859-1"), "utf8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		log.info("mfregex:" + mfregex);
		Pattern pattern = Pattern.compile(".*?" + mfregex + ".*", Pattern.CASE_INSENSITIVE);
		// 查询活动信息
		ForderActivity fa = this.forderActivityService.findOneById(checkId, ForderActivity.class);

		if (checkId != null)
			session.setAttribute("checkActivityId", checkId);
		// 标签
		modelAndView.addObject("lableList", labelService.find(new Query(), Label.class));

		try {

			Pagination<Resource> pageList = new Pagination<Resource>();

			Query query = new Query();

			if (StringUtils.isNotEmpty(mfregex)) {
				query = Query.query(Criteria.where("originalName").regex(pattern));
				modelAndView.addObject("mfregex", mfregex);
			}

			if (type.equals(BaseType.Type.PERSION.toString())) {
				query.addCriteria(Criteria.where("personActivityId").is(fa.getPersonActivityId()));
			} else if (type.equals(BaseType.Type.BASEUTIS.toString())) {
				query.addCriteria(Criteria.where("baseutisActivityId").is(fa.getBaseutisActivityId()));
			} else {
				query.addCriteria(Criteria.where("forderActivityId").is(checkId));
			}

			query.with(new Sort(Sort.Direction.DESC, "editorImgInfo.sort"));// 从1开始
			query.with(new Sort((sort.equals(String.valueOf("DESC"))) ? Sort.Direction.DESC : Sort.Direction.ASC,
					"createTime"));
			pageList = this.resourceService.findPaginationByQuery(query, pageNo, pageSize, Resource.class);
			modelAndView.addObject("listPhoto", pageList);

			// 获取当前用户收藏的图片
			// start

			if (adminUser != null) {

				Favorites favorites = this.favoritesService.findFavoritesById(adminUser.getId());

				if (favorites != null && favorites.getResource() != null) {

					modelAndView.addObject("listFavorites", favorites.getResource());
				}

			}

			// end

		} catch (Exception e) {
			log.info(e.toString());
		}
		modelAndView.addObject("fa", fa);

		return modelAndView;
	}

	/**
	 * 资料上传
	 *
	 * @param request
	 * @param printWriter
	 * @param session
	 * @param forderActivityId
	 * @param multipartFiles
	 * @throws IOException
	 */
	@SystemErrorLog(description = "文件上传出错")
	@RequestMapping("/uploadFile/{type}")
	public void uploadFile(HttpServletRequest request, @PathVariable("type") String type, PrintWriter printWriter,
			HttpSession session, @RequestParam(defaultValue = "", value = "forderActivityId") String forderActivityId,
			@RequestParam(value = "file[]", required = false) MultipartFile[] multipartFiles) {

		String typeId = "";

		// 获取 用户 session
		AdminUser au = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);
		ForderActivity f = forderActivityService.findForderById(forderActivityId);

		log.info("上传图片+活动ID" + forderActivityId);

		for (MultipartFile mpfile : multipartFiles) {

			Resource rf = UploadUtil.processResource(mpfile, au, f, type);
			log.info("文件信息:" + rf.toString());
			// 更新到数据库
			this.resourceService.insert(rf);
			this.executorsQueue.Executor(rf.getGenerateName(), this.resourceService);

			//
			// SingletionThreadPoolExecutor.getInstance().getPool()
			// .execute(new Task(rf.getGenerateName(), this.resourceService));
		}

		// 返回当前活动的ID
		printWriter.write(forderActivityId);
		printWriter.flush();
		printWriter.close();

	}

	/**
	 * 更新 资源描述
	 *
	 * @param activityId
	 * @param id
	 * @param resourceName
	 * @param person
	 * @param photographer
	 * @param resourceAddress
	 * @param description
	 * @return
	 */
	@SystemErrorLog(description = "资源更新出错")
	@RequestMapping("/update/{type}")
	public ModelAndView update(@PathVariable("type") String type, String activityId, String id, String resourceName,
			String person, String photographer, String resourceAddress, String description,
			@RequestParam(value = "sort", defaultValue = "0") long sort) {
		ModelAndView modelAndView = new ModelAndView();
		if (id != null) {

			String[] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				Resource r = this.resourceService.findOneById(ids[i], Resource.class);
				if (r.getEditorImgInfo() == null)
					r.setEditorImgInfo(new EditorImgBean());
				r.getEditorImgInfo().setResourceName(resourceName);
				r.getEditorImgInfo().setPerson(person);
				r.getEditorImgInfo().setPhotographer(photographer);
				r.getEditorImgInfo().setResourceAddress(resourceAddress);
				r.getEditorImgInfo().setDescription(description);
				r.getEditorImgInfo().setSort(sort);
				this.resourceService.save(r);
			}
			this.informationRegisterService.addInformationRegister(resourceName, person, photographer, resourceAddress);

		}

		modelAndView.setViewName("redirect:/photoMessageAction/checkActivity/" + type + "?checkId=" + activityId);

		return modelAndView;
	}

	/**
	 * 删除资源
	 *
	 * @param activityId
	 * @param id
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{type}")
	public ModelAndView delete(@RequestParam(value = "activityId", defaultValue = "0") String activityId,
			@PathVariable("type") String type, @RequestParam(value = "id", defaultValue = "0") String id,
			@RequestParam(value = "ids", defaultValue = "0") String ids,
			@RequestParam(value = "selectQuery", defaultValue = "") String selectQuery,
			@RequestParam(value = "selectVal", defaultValue = "") String selectVal,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "21") int pageSize)

	{
		ModelAndView modelAndView = new ModelAndView();

		if (type.equals("index")) {
			modelAndView.setViewName("redirect:/photoMessageAction/searchImgsByQuerys?selectQuery=" + selectQuery
					+ "&selectVal=" + selectVal + "&pageNo=" + pageNo + "&pageSize=" + pageSize);
		} else {

			modelAndView.setViewName("redirect:/photoMessageAction/checkActivity/" + type + "?checkId=" + activityId);
		}

		try {
			String deleteId[] = id.split(",");
			for (int i = 0; i < deleteId.length; i++) {
				if (!deleteId[i].isEmpty() && !deleteId[i].equals("0")) {// 删除
					resourceService.remove(resourceService.findOneById(deleteId[i], Resource.class));
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return modelAndView;
	}

	/**
	 * @param @param
	 *            resourceId
	 * @param @param
	 *            session
	 * @param @return
	 *            设定文件
	 * @return List<Resource> 返回类型
	 * @throws @Title:
	 *             toMyFavorties
	 * @Description: TODO(将资源放入收藏夹)
	 */
	@SystemErrorLog(description = "收藏图片出错")
	@RequestMapping("/toMyFavorties")
	@ResponseBody
	public List<Resource> toMyFavorties(@RequestParam(defaultValue = "", value = "resourceId") String resourceId,
			HttpSession session) {

		AdminUser adminUser = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);

		List<Resource> listResource = this.favoritesService.getResource(resourceId);

		boolean flag = this.favoritesService.toSaveFavorites(adminUser, listResource);

		if (flag) {

			return listResource;
		}
		return null;
	}

	@SystemErrorLog(description = "收藏图片出错")
	@RequestMapping("/setTheCover")
	@ResponseBody
	public BasicDataResult setTheCover(
			@RequestParam(defaultValue = "", value = "forderActivityId") String forderActivityId,
			@RequestParam(defaultValue = "", value = "id") String id, HttpSession session) {

		// 根据id来查询图片的信息
		Resource r = this.resourceService.findOneById(id, Resource.class);

		if (r == null) {
			return BasicDataResult.build(400, "未能获取到设为封面的图片资源", null);
		}

		// 根据活动id获取活动信息

		ForderActivity f = this.forderActivityService.findOneById(forderActivityId, ForderActivity.class);

		if (f == null) {
			return BasicDataResult.build(400, "未能获取到相关活动的信息，请刷新页面", null);
		}

		f.setCover(r.getId());

		this.forderActivityService.save(f);

		return BasicDataResult.build(200, "设置封面成功", null);

	}

	/**
	 * @param @param
	 *            resourceId
	 * @param @param
	 *            session
	 * @param @return
	 *            设定文件
	 * @return List<Resource> 返回类型
	 * @throws @Title:
	 *             cancelMyFavorties
	 * @Description: TODO(将资源从收藏夹删除)
	 */
	@SystemErrorLog(description = "取消收藏图片出错")
	@RequestMapping("/cancelMyFavorties")
	@ResponseBody
	public List<Resource> cancelMyFavorties(@RequestParam(defaultValue = "", value = "resourceId") String resourceId,
			HttpSession session) {

		AdminUser adminUser = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);

		List<Resource> listResource = this.favoritesService.getResource(resourceId);

		boolean flag = this.favoritesService.cancelFavorites(adminUser, listResource);

		if (flag) {

			return listResource;
		}
		return null;

	}

	@SuppressWarnings("unused")
	@SystemErrorLog(description = "同步至个人图片库")
	@RequestMapping("/copyToMyPictures")
	@ResponseBody
	public BasicDataResult copyToMyPictures(@RequestParam(defaultValue = "", value = "resourceId") String resourceId,
			HttpSession session) {

		session.removeAttribute("areaphotoTimeList");
		session.removeAttribute("directlyphotoTimeList");
		session.removeAttribute("basePhotoTimeList");
		session.removeAttribute("photoTimeList");
		session.removeAttribute("yearId");
		session.removeAttribute("monthId");
		session.removeAttribute("dayId");
		session.removeAttribute("companyName");
		session.removeAttribute("nature");

		String[] resourcesId = resourceId.split(",");

		// 如果需要同步的资源大小大于0
		if (resourcesId.length > 0) {

			AdminUser adminUser = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);

			for (int i = 0; i < resourcesId.length; i++) {

				// 1.首先根据需要同步的资源id查询该条数据的记录
				Resource res = this.resourceService.findResourceByResourceId(resourcesId[i]);
				if (res != null) {

					// 2,如果该图片已经收藏了，不会再次同步到个人图片库
					Resource resource = this.resourceService.findResourceByResourceName(adminUser.getId(),
							res.getGenerateName());

					// 3,通过图片的名称，以及图片的所属活动id进行查询
					if (Common.isEmpty(res.getForderActivityId())) {
						continue;
					}

					String oldForderActivityId = "";
					if (Common.isNotEmpty(res.getForderActivityId())) {
						oldForderActivityId = res.getForderActivityId();
					}
					// 获取活动信息
					ForderActivity oldForderActivity = this.forderActivityService.findOneById(oldForderActivityId,
							ForderActivity.class);

					// 查询是否存在该个人的活动
					ForderActivity forderActivity = this.forderActivityService.findForderActivityByActivityNameType(
							oldForderActivity.getForderActivityName(), adminUser.getId());

					// 如果图片没有收藏
					if (resource == null) {
						ForderActivity newForderActivity = new ForderActivity();
						// 如果没有该活动则创建一个新的个人活动
						// 4,如果查询出来为空，那么执行添加
						String personActivityId = null;
						if (forderActivity == null) {
							personActivityId = new ObjectId(new Date()).toString();
							newForderActivity.setActivityTime(oldForderActivity.getActivityTime());
							newForderActivity.setAddress(oldForderActivity.getAddress());
							newForderActivity.setAdminCompany(adminUser.getAdminCompany());
							newForderActivity.setBoundCompany(adminUser.getAdminCompany().getId());
							newForderActivity.setBoundId(adminUser.getId());
							newForderActivity.setAdminUser(adminUser);
							newForderActivity.setDescription(oldForderActivity.getDescription());
							newForderActivity.setForderActivityName(oldForderActivity.getForderActivityName());
							newForderActivity.setBaseutisActivityId(new ObjectId(new Date()).toString());
							newForderActivity.setPersonActivityId(personActivityId);
							newForderActivity.setParentId("0");

							String today = oldForderActivity.getActivityTime();
							String year = today.substring(0, today.indexOf("-"));
							newForderActivity.setYear(year);
							String month = today.substring(today.indexOf("-") + 1, today.lastIndexOf("-"));
							newForderActivity.setMonth(month);
							String day = today.substring(today.lastIndexOf("-") + 1, today.length());
							newForderActivity.setDay(day);
							List<Type> listType = new ArrayList<Type>();

							Type t = new Type();
							t.setType(BaseType.Type.PERSION);
							listType.add(t);
							newForderActivity.setListType(listType);
							newForderActivity.setType(BaseType.Type.PERSION.toString());
							this.forderActivityService.insert(newForderActivity);
						}

						if (Common.isNotEmpty(personActivityId)) {
							forderActivity = new ForderActivity();
							forderActivity.setPersonActivityId(personActivityId);
						}

						Resource newResource = new Resource();
						newResource.setAdminCompanyId("");
						newResource.setAdminUser(adminUser);
						newResource.setBoundId(adminUser.getId());
						newResource.setEditorImgInfo(res.getEditorImgInfo());
						newResource.setExtensionName(res.getExtensionName());
						newResource.setFileType(res.getFileType());
						newResource.setGenerateName(res.getGenerateName());
						newResource.setImgCompressionBean(res.getImgCompressionBean());
						newResource.setImgInfoBean(res.getImgInfoBean());
						newResource.setOriginalName(res.getOriginalName());
						newResource.setOriginalPath(res.getOriginalPath());
						newResource.setSource(res.getSource());
						newResource.setUploadPerson(res.getUploadPerson());

						newResource.setPersonActivityId(forderActivity.getPersonActivityId());
						this.resourceService.insert(newResource);

					}

				}
			}
			return BasicDataResult.build(200, "同步成功", true);

		} else {

			return BasicDataResult.build(203, "请选择需要同步的图片", false);
		}

	}

	/**
	 * 文件打包下载
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "download")
	@SystemErrorLog(description = "图片打包下载出错")
	@SystemControllerLog(description = "图片打包下载")
	@Scope("singleto")
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {

		// 1.获取所有需要下载的图片id
		// 2.将所有下载图片的路径转换成List<File>
		List<File> listFile = this.resourceService.getImageFile(id);

		String temporary = "WEB-INF" + File.separator + "Template" + File.separator + "Temporary" + File.separator;

		// 3.创建服务器临时文件目录
		String UPLOAD = request.getSession().getServletContext().getRealPath("/");

		log.info("临时压缩包目录：" + UPLOAD + temporary);
		// 获取临时压缩文件
		String storeName = ZipCompress.getZipFilename();
		log.info("压缩文件：" + storeName);

		File zipfile = new File(UPLOAD + temporary + storeName);

		ZipCompress.zipFile(listFile, zipfile);

		// 4.执行打包操作并且下载
		String contentType = "application/octet-stream";

		try {
			FileOperateUtil.download(request, response, storeName, contentType, temporary);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			ZipCompress.deleteFile(zipfile);
		}
		return null;
	}

	@SystemErrorLog(description = "查询所有收藏出错")
	@RequestMapping("/findMyFavorites")
	public ModelAndView findMyFavorites(HttpSession session,
			@RequestParam(value = "sort", defaultValue = "DESC") String sort,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "28") Integer pageSize,
			@RequestParam(value = "mfregex", defaultValue = "") String mfregex) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("admin/photo-gallery/photoMessage/myFavorites");

		AdminUser adminUser = (AdminUser) session.getAttribute(CommonEnum.USERSESSION);

		Favorites favorites = new Favorites();

		List<Resource> listfavorites = new ArrayList<Resource>();

		Pagination<Resource> pagination = null;

		if (adminUser != null) {

			// 查询所有我收藏的资源
			favorites = this.favoritesService.findFavoritesById(adminUser.getId());

			if (favorites != null) {
				if (favorites.getResource().size() > 0) {

					listfavorites = favorites.getResource();

				}
			}
		}
		if (pagination == null)
			pagination = new Pagination<Resource>(pageNo, pageSize, listfavorites.size());
		
		List<Resource> list2 = new ArrayList<Resource>();
		int pN = 0;
		int pS = 0;
		if (pageNo == 1) {
			pN = 0;
			pS = pageSize;
		} else {
			pN = (pageNo - 1) * pageSize;
			pS = pageNo * pageSize;
		}
		for (int i = pN; i < pS; i++) {
			if (i < listfavorites.size()) {
				list2.add(listfavorites.get(i));
			}

		}
		pagination.setDatas(list2);

		modelAndView.addObject("pagination", pagination);

		return modelAndView;

	}

	@RequestMapping(value = "/getInformationRegister")
	@ResponseBody
	@SystemErrorLog(description = "查询快捷选项")
	@SystemControllerLog(description = "查询快捷选项")
	public BasicDataResult getInformationRegister() {

		BasicDataResult result = this.informationRegisterService.togetInformationRegister();
		return result;

	}

	@SystemErrorLog(description = "查询图片出错")
	@RequestMapping("/searchImgsByQuerys")
	public ModelAndView searchImgsByQuerys(@RequestParam(defaultValue = "", value = "searchType") String searchType,
			@RequestParam(defaultValue = "", value = "serachForderQuery1") String serachForderQuery1,
			@RequestParam(defaultValue = "", value = "serachForderQueryVal1") String serachForderQueryVal1,
			@RequestParam(defaultValue = "", value = "serachForderQuery2") String serachForderQuery2,
			@RequestParam(defaultValue = "", value = "serachForderQueryVal2") String serachForderQueryVal2,
			@RequestParam(defaultValue = "", value = "serachForderQuery3") String serachForderQuery3,
			@RequestParam(defaultValue = "", value = "serachForderQueryVal3") String serachForderQueryVal3,
			@RequestParam(defaultValue = "", value = "serachForderQuery4") String serachForderQuery4,
			@RequestParam(defaultValue = "", value = "serachForderQueryVal4") String serachForderQueryVal4,
			@RequestParam(value = "sort", defaultValue = "DESC") String sort,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
			@RequestParam(value = "time1", defaultValue = "") String time1,
			@RequestParam(value = "time2", defaultValue = "") String time2

	) {
		ModelAndView modelAndView = new ModelAndView();

		if (searchType.equals("searchForderActivity")) {
			// 查询活动
			try {
				modelAndView.setViewName("redirect:/adminUser/index?time1=" + time1 + "&time2=" + time2
						+ "&serachForderQuery1=" + serachForderQuery1 + "&serachForderQueryVal1="
						+ URLEncoder.encode(serachForderQueryVal1, "utf-8") + "&serachForderQuery2="
						+ serachForderQuery2 + "&serachForderQueryVal2="
						+ URLEncoder.encode(serachForderQueryVal2, "utf-8") + "&searchType=" + searchType);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (searchType.equals("searchPicture")) {
			// 检索图片

			modelAndView.setViewName("admin/photo-gallery/photoMessage/searchIndex");

			modelAndView.addObject("webType", "index");

			Pagination<Resource> pageList = new Pagination<Resource>();

			Query query = new Query();

			Query addressQuery = new Query();

			if (serachForderQuery3.equals("forderActivityAddress") || serachForderQuery4.equals("forderActivityAddress")) {
				if (serachForderQuery3.equals("forderActivityAddress") && !serachForderQuery4.equals("forderActivityAddress")) {
					addressQuery.addCriteria(Criteria.where("address").regex(serachForderQueryVal3));
					if (Common.isNotEmpty(serachForderQuery4) && Common.isNotEmpty(serachForderQueryVal4)) {
						query.addCriteria(Criteria.where(serachForderQuery4).regex(serachForderQueryVal4));
					}
				} else if (!serachForderQuery3.equals("forderActivityAddress") && serachForderQuery4.equals("forderActivityAddress")) {
					addressQuery.addCriteria(Criteria.where("address").regex(serachForderQueryVal4));
					if (Common.isNotEmpty(serachForderQuery3) && Common.isNotEmpty(serachForderQueryVal3)) {
						query.addCriteria(Criteria.where(serachForderQuery3).regex(serachForderQueryVal3));
					}
				} else if (serachForderQuery3.equals("forderActivityAddress") && serachForderQuery4.equals("forderActivityAddress")) {
					serachEqual(serachForderQuery3, serachForderQueryVal3, serachForderQuery4, serachForderQueryVal4,
							addressQuery);
				}

				List<ForderActivity> listForderActivity = this.forderActivityService.find(addressQuery,
						ForderActivity.class);

				if (listForderActivity.size() > 0) {
					List<String> listIds = new ArrayList<String>();

					for (ForderActivity f : listForderActivity) {
						listIds.add(f.getId());
					}

					query.addCriteria(Criteria.where("forderActivityId").in(listIds));
				}else{
					
					query.addCriteria(Criteria.where("forderActivityId").is(null));
				}
			}else{
				
				if(serachForderQuery3.equals(serachForderQuery4)&&Common.isNotEmpty(serachForderQuery3)){
					serachEqual(serachForderQuery3, serachForderQueryVal3, serachForderQuery4, serachForderQueryVal4,
							query);
				}else{
					
					if(Common.isNotEmpty(serachForderQuery3)&&Common.isNotEmpty(serachForderQueryVal3)){
						query.addCriteria(Criteria.where(serachForderQuery3).regex(serachForderQueryVal3));
					}
					if(Common.isNotEmpty(serachForderQuery4)&&Common.isNotEmpty(serachForderQueryVal4)){
						query.addCriteria(Criteria.where(serachForderQuery4).regex(serachForderQueryVal4));
					}
				}
				
				
			}

			query.with(new Sort((sort.equals(String.valueOf("DESC"))) ? Sort.Direction.DESC : Sort.Direction.ASC,
					"createTime"));

			query.addCriteria(Criteria.where("personActivityId").is(null));

			pageList = this.resourceService.findPaginationByQuery(query, pageNo, pageSize, Resource.class);
			modelAndView.addObject("searchList", pageList);

		} else {
			modelAndView.setViewName("redirect:/adminUser/index");
		}
		modelAndView.addObject("serachForderQueryVal3", serachForderQueryVal3);
		modelAndView.addObject("serachForderQuery3", serachForderQuery3);
		modelAndView.addObject("serachForderQueryVal4", serachForderQueryVal4);
		modelAndView.addObject("serachForderQuery4", serachForderQuery4);
		modelAndView.addObject("searchType", searchType);
		return modelAndView;
	}

	private void serachEqual(String serachForderQuery1, String serachForderQueryVal1, String serachForderQuery2,
			String serachForderQueryVal2, Query q) {
		Criteria cr = new Criteria();

		Criteria ca1 = null;
		Criteria ca2 = null;

		if (Common.isNotEmpty(serachForderQuery2) && Common.isNotEmpty(serachForderQueryVal2)) {
			ca2 = Criteria.where(serachForderQuery2).regex(serachForderQueryVal2);
		}

		if (Common.isNotEmpty(serachForderQuery1) && Common.isNotEmpty(serachForderQueryVal1)) {
			ca1 = Criteria.where(serachForderQuery1).regex(serachForderQueryVal1);
		}

		if (ca1 != null && ca2 != null) {
			q.addCriteria(cr.orOperator(ca1, ca2));
		} else if (ca1 == null || ca2 == null) {
			if (ca1 != null) {
				q.addCriteria(cr.orOperator(ca1));
			} else if (ca2 != null) {
				q.addCriteria(cr.orOperator(ca2));
			}
		}
	}

}
