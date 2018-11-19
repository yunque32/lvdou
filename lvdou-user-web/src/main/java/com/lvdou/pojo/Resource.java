package com.lvdou.pojo;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Resource implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "grant_key")
	private String grantKey;
	@Column(name = "page_url")
	private String pageUrl;
	@Column(name = "resource_type")
	private String resourceType;
	@Column(name = "icon")
	private String icon;
	@Column(name = "pid")
	private Integer pid;
	@Column(name = "open")
	private Integer open;

	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return this.id;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setGrantKey(String grantKey){
		this.grantKey = grantKey;
	}
	public String getGrantKey(){
		return this.grantKey;
	}
	public void setPageUrl(String pageUrl){
		this.pageUrl = pageUrl;
	}
	public String getPageUrl(){
		return this.pageUrl;
	}
	public void setResourceType(String resourceType){
		this.resourceType = resourceType;
	}
	public String getResourceType(){
		return this.resourceType;
	}
	public void setIcon(String icon){
		this.icon = icon;
	}
	public String getIcon(){
		return this.icon;
	}
	public void setPid(Integer pid){
		this.pid = pid;
	}
	public Integer getPid(){
		return this.pid;
	}
	public void setOpen(Integer open){
		this.open = open;
	}
	public Integer getOpen(){
		return this.open;
	}

	@Override
	public String toString() {
		return "Resource{" +
				"id=" + id +
				", name='" + name + '\'' +
				", grantKey='" + grantKey + '\'' +
				", pageUrl='" + pageUrl + '\'' +
				", resourceType='" + resourceType + '\'' +
				", icon='" + icon + '\'' +
				", pid=" + pid +
				", open=" + open +
				'}';
	}
}