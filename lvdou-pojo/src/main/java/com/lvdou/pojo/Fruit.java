package com.lvdou.pojo;


import javax.persistence.*;

@Table(name="tb_fruit")
public class Fruit implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name="fruit_name")
	private String fruitName;
	@Column(name="fruit_num")
	private String fruitNum;
	@Column(name="origin_place")
	private String originPlace;
	@Column(name="fruit_classify")
	private String fruitClassify;
	@Column(name="fruit_weight")
	private String fruitWeight;
	@Column(name="packing")
	private String packing;

	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return this.id;
	}
	public void setFruitName(String fruitName){
		this.fruitName = fruitName;
	}
	public String getFruitName(){
		return this.fruitName;
	}
	public void setFruitNum(String fruitNum){
		this.fruitNum = fruitNum;
	}
	public String getFruitNum(){
		return this.fruitNum;
	}
	public void setOriginPlace(String originPlace){
		this.originPlace = originPlace;
	}
	public String getOriginPlace(){
		return this.originPlace;
	}
	public void setFruitClassify(String fruitClassify){
		this.fruitClassify = fruitClassify;
	}
	public String getFruitClassify(){
		return this.fruitClassify;
	}
	public void setFruitWeight(String fruitWeight){
		this.fruitWeight = fruitWeight;
	}
	public String getFruitWeight(){
		return this.fruitWeight;
	}
	public void setPacking(String packing){
		this.packing = packing;
	}
	public String getPacking(){
		return this.packing;
	}

}