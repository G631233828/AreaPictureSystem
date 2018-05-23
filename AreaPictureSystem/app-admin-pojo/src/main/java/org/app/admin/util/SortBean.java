package org.app.admin.util;

import org.app.admin.pojo.AdminUser;

/**
 * @描述：所有排行的基类
 * @author chrealcool
 * 
 */

public class SortBean implements Comparable<SortBean> {
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSortnum() {
		return sortnum;
	}

	public void setSortnum(int sortnum) {
		this.sortnum = sortnum;
	}

	public int getUploadnum() {
		return uploadnum;
	}

	public void setUploadnum(int uploadnum) {
		this.uploadnum = uploadnum;
	}


	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}


	public int getForderActivityNum() {
		return forderActivityNum;
	}

	public void setForderActivityNum(int forderActivityNum) {
		this.forderActivityNum = forderActivityNum;
	}






	@Override
	public String toString() {
		return "SortBean [name=" + name + ", sortnum=" + sortnum + ", uploadnum=" + uploadnum + ", adminUser="
				+ adminUser + ", forderActivityNum=" + forderActivityNum + "]";
	}






	private String name;
    private int sortnum;
    private int uploadnum;
    private AdminUser adminUser;
    private int forderActivityNum;
    
    
    
    
	@Override
	public int compareTo(SortBean o) {
		return o.uploadnum-this.uploadnum;
	}

}
