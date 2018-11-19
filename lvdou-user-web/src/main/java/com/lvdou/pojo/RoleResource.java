package com.lvdou.pojo;

import javax.persistence.Table;

@Table(name = "tb_role_resource")
public class RoleResource implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Integer roleId;
	private Integer resourceId;

	public void setRoleId(Integer roleId){
		this.roleId = roleId;
	}
	public Integer getRoleId(){
		return this.roleId;
	}
	public void setResourceId(Integer resourceId){
		this.resourceId = resourceId;
	}
	public Integer getResourceId(){
		return this.resourceId;
	}

}