package com.lvdou.pojo;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "tb_user_role")
public class UserRole implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "role_id")
	private Integer roleId;

	public void setUserId(Long userId){
		this.userId = userId;
	}
	public Long getUserId(){
		return this.userId;
	}
	public void setRoleId(Integer roleId){
		this.roleId = roleId;
	}
	public Integer getRoleId(){
		return this.roleId;
	}



}