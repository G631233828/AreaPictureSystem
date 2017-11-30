/**   
* @Title: ResourceService.java 
* @Package org.app.admin.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author fliay
* @date 2017年11月20日 下午3:45:50 
* @version V1.0   
*/
package org.app.admin.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.app.admin.pojo.Resource;
import org.app.framework.service.GeneralServiceImpl;
import org.app.framework.util.Common;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/** 
* @ClassName: ResourceService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author fliay
* @date 2017年11月20日 下午3:45:50 
*  
*/
@Repository
public class ResourceService extends GeneralServiceImpl<Resource>{
	
		/**
		 * 
		* @Title: maxUploadThisMonth 
		* @Description: TODO(获取本月上传文件最大的) 
		* @param @return    设定文件 
		* @return int    返回类型 
		* @throws
		 */
		public int maxUploadThisMonthForDay(String date,String boundId){
			
			Query query = new Query();
			
			if(Common.isNotEmpty(boundId)){
				
				query.addCriteria(Criteria.where("boundId").is(boundId));
			}
			
			if(Common.isNotEmpty(date)){
				
				query.addCriteria(Criteria.where("createDate").is(date));
			}
			
			List<Resource> list = this.find(query, Resource.class);
			
			if(list.size()>0)
			
				return list.size();
			
			else 
				return 0;
		}
	
		
		
		/**
		 * 
		* @Title: getMonthUploadNum 
		* @Description: TODO(获取当前月的上传记录) 
		* @param @param year
		* @param @param month
		* @param @param day
		* @param @return    设定文件 
		* @return List    返回类型 
		* @throws
		 */
		public List getMonthUploadNum(){
			
			LinkedList list = new LinkedList();
			
			Calendar now = Calendar.getInstance();  
			
			int year = now.get(Calendar.YEAR);
			
			int month =now.get(Calendar.MONTH) + 1;
			
			int day = now.get(Calendar.DAY_OF_MONTH);
			
			for(int i =1;i<=day;i++){
				
				int num = this.maxUploadThisMonthForDay(year+"-"+month+"-"+i, null);
				
				list.add("["+i+","+num+"]");
				
			}
			
			return list;
		}
		
		
		
		
		
		
		
		
		
	
	 
	

		
	public static void main(String[] args) {
		
		LinkedList list = new LinkedList();
		String aa[] = new String[]{};
		Calendar now = Calendar.getInstance();  
		int year = now.get(Calendar.YEAR);
		int month =now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		for(int i =1;i<day;i++){
			
			
			list.add("["+i+","+2+"]");
			
		}
		
		System.out.println(list);
		
		
		
	}
		
		
		
		
		

	 
	 
	 
	 
	 
	 
	 
	 
	 
}
