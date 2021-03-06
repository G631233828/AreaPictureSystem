package org.app.admin.util;

import org.app.admin.pojo.ForderActivity;
import org.app.framework.util.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

public class PhotoTimeback   {
	// 新添加的字段
	private String year;

	private String month;

	private String day;

	private List<PhotoTimeback> listPhotoTime;

	public List<PhotoTimeback> getListPhotoTime() {
		return listPhotoTime;
	}

	public void setListPhotoTime(List<PhotoTimeback> listPhotoTime) {
		this.listPhotoTime = listPhotoTime;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	// ---------------------
	private String id;// 文件夹活动，哪于哪个公司，

	private String time;

	private List<ForderActivity> list;

	private boolean istree;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PhotoTimeback() {
		this.istree = false;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<ForderActivity> getList() {
		return list;
	}

	public void setList(List<ForderActivity> list) {
		this.list = list;
	}

	public boolean isIstree() {
		return istree;
	}

	public void setIstree(boolean istree) {
		this.istree = istree;
	}

	@Override
	public String toString() {
		return "PhotoTime [year=" + year + ", month=" + month + ", day=" + day + ", listPhotoTime=" + listPhotoTime
				+ ", id=" + id + ", time=" + time + ", list=" + list + ", istree=" + istree + "]";
	}

	/**
	 *
	 * 图片时间轴
	 * 
	 * @param listFA
	 *            文件夹集合
	 * @param isBase
	 *            属于基层单位
	 * @return 前台展示队列
	 * 
	 * @return
	 */
	public static List<PhotoTimeback> getPhotoTime(List<ForderActivity> listFA, HttpSession session) {

		List<PhotoTimeback> list = new ArrayList<PhotoTimeback>();

		Set<String> year = new HashSet<String>();
		for (ForderActivity f : listFA) {
			year.add(f.getYear());
		} // 1
		for (String y : year) {
			// 遍历已经过滤过的年份
			PhotoTimeback py = new PhotoTimeback();
			py.setYear(y);
			List<PhotoTimeback> listY = new ArrayList<PhotoTimeback>();
			// 获取月
			Set<String> month = new HashSet<String>();
			for (ForderActivity fm : listFA) {
				// 判断活动所属年份
				if (y.equals(fm.getYear())) {
					month.add(fm.getMonth());
				}
			}
			for (String m : month) {
				// 遍历已经过滤过的月份
				PhotoTimeback pm = new PhotoTimeback();
				pm.setMonth(m);

				List<PhotoTimeback> listM = new ArrayList<PhotoTimeback>();
				// 获取日
				Set<String> day = new HashSet<String>();
				for (ForderActivity fd : listFA) {
					// 判断活动的年份月份
					if (y.equals(fd.getYear()) && m.equals(fd.getMonth())) {
						day.add(fd.getDay());
					}
				}
				for (String d : day) {
					PhotoTimeback pd = new PhotoTimeback();
					pd.setDay(d);
					List<ForderActivity> listF = new ArrayList<ForderActivity>();
					for (ForderActivity f : listFA) {
						if (y.equals(f.getYear()) && m.equals(f.getMonth()) && d.equals(f.getDay())) {
							listF.add(f);
						}
					}
					pd.setList(listF);
					String dayId = (String) session.getAttribute("dayId");
					if (Common.isEmpty(dayId)) {
						dayId = graphicRandom();
						pd.setId(dayId);
					}
					listM.add(pd);
				}
				pm.setListPhotoTime(listM);
				String monthId = (String) session.getAttribute("monthId");
				if (Common.isEmpty(monthId)) {
					monthId = graphicRandom();
					pm.setId(monthId);
				}
				listY.add(pm);
			}
			// 添加子集
			py.setListPhotoTime(listY);
			String yearId = (String) session.getAttribute("yearId");
			if (Common.isEmpty(yearId)) {
				yearId =graphicRandom();
				py.setId(yearId);
			}
			list.add(py);

		}

		return list;
	}

	public static  String graphicRandom() {

		int code = (int) (Math.random() * (400000000 - 100000000)) + 100000000; // 产生1000-9999之间的一个随机数
		String codestr = String.valueOf(code);
		return codestr;
	}

	// public static List<PhotoTime> getPhotoTime(List<ForderActivity>
	// listFA,String checkDate,boolean iscompany){
	// List<PhotoTime> list=new ArrayList<PhotoTime>();
	//
	// for (ForderActivity fa:listFA) {
	// PhotoTime pt=new PhotoTime();
	// boolean check=false;
	//
	// for (PhotoTime pts:list) {
	// //区域 与 基本层单 有冲突，需要使用 isCompany检查
	// if(iscompany){
	// if(fa.getActivityTime().equals(pts.getTime())){
	// check=true;
	// pts.getList().add(fa);
	// break;
	// }
	// }else{
	// if(fa.getActivityTime().equals(pts.getTime()) &&
	// fa.getBoundCompany().equals(pts.getId())){
	// check=true;
	// pts.getList().add(fa);
	// break;
	// }
	// }
	// }
	//
	// //添加时间
	// if(check==false){
	// pt.setTime(fa.getActivityTime());
	// pt.setId(fa.getBoundCompany());//企业ID
	// if(pt.getList()==null){
	// pt.setList(new ArrayList<ForderActivity>());
	// }
	// pt.getList().add(fa);
	// }
	// //检查是否需要选择中菜单
	// if(checkDate!=null && pt.getTime()!=null){
	// if(pt.getTime().equals(checkDate)){
	// pt.setIstree(true);
	// }
	// }
	// //检查pt.gettime
	// if(pt.getTime()!=null){
	// list.add(pt);
	// }
	//
	//
	// }
	// return list;
	// }

}
