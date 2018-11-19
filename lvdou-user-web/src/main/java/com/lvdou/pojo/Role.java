package com.lvdou.pojo;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Role implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "keyword")
	private String keyword;
	@Column(name = "description")
	private String description;

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
	public void setKeyword(String keyword){
		this.keyword = keyword;
	}
	public String getKeyword(){
		return this.keyword;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}

	public Role() {
	}

	public Role(String name, String keyword, String description) {
		this.name = name;
		this.keyword = keyword;
		this.description = description;
	}
}