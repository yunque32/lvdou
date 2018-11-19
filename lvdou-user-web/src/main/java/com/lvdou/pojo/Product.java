package com.lvdou.pojo;


import javax.persistence.*;

@Table(name="tb_product")
public class Product implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@Column(name="product_name")
	private String productName;
	@Column(name="quality_grade")
	private String qualityGrade;

	//产品分类
	@Column(name="product_classify")
	private String productClassify;
	@Column(name = "unit_sta")
	private String unitSta;
	@Column(name="packing")
	private String packing;

	@Column(name = "agency_id")
	private Long agencyId;
	@Column(name = "producter_id")
	private Long producterId;
	@Column(name = "product_sta")
	private String productSta;

	@Column(name = "comweb")
	private String comweb;
	@Column(name = "simplePlantTime")
	private String simplePlantTime;

	public String getUnitSta() {
		return unitSta;
	}

	public String getQualityGrade() {
		return qualityGrade;
	}

	public void setQualityGrade(String qualityGrade) {
		this.qualityGrade = qualityGrade;
	}


	public void setUnitSta(String unitSta) {
		this.unitSta = unitSta;
	}




	public String getProductSta() {
		return productSta;
	}

	public void setProductSta(String productSta) {
		this.productSta = productSta;
	}

	public String getComweb() {
		return comweb;
	}

	public void setComweb(String comweb) {
		this.comweb = comweb;
	}

	public String getSimplePlantTime() {
		return simplePlantTime;
	}

	public void setSimplePlantTime(String simplePlantTime) {
		this.simplePlantTime = simplePlantTime;
	}



	public void setId(Long id){
		this.id = id;
	}
	public Long getId(){
		return this.id;
	}
	public void setProductName(String productName){
		this.productName = productName;
	}
	public String getProductName(){
		return this.productName;
	}
	public void setProductClassify(String productClassify){
		this.productClassify = productClassify;
	}
	public String getProductClassify(){
		return this.productClassify;
	}
	public void setPacking(String packing){
		this.packing = packing;
	}
	public String getPacking(){
		return this.packing;
	}

	public Long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}

	public Long getProducterId() {
		return producterId;
	}

	public void setProducterId(Long producterId) {
		this.producterId = producterId;
	}
}