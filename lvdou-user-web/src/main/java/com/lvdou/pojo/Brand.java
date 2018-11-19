package com.lvdou.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name="tb_brand")
public class Brand implements Serializable{
	private static final long serialVersionUID = -8315881228799842049L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	/** 品牌名称 */
	@Column(name="name")
    private String name;
    /** 品牌首字母 */
	@Column(name="first_char")
    private String firstChar;
    /** setter and getter method */
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    public String getFirstChar() {
        return firstChar;
    }
    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar == null ? null : firstChar.trim();
    }
}