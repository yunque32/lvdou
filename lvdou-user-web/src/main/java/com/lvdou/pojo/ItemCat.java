package com.lvdou.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name="item_cat")
public class ItemCat implements Serializable{
	private static final long serialVersionUID = -5192195713480494105L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@Column(name="parent_id")
    private Long parentId;
	@Column(name="name")
    private String name;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}