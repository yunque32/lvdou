package com.lvdou.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Table(name="goods")
public class Goods implements Serializable{

	private static final long serialVersionUID = -3888154864571208139L;
	/** 主键 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@Column(name = "goods_id")
    private Long goodsId;
	@Column(name = "title")
    private String title;
	@Column(name = "small_pic")
    private String smallPic;
	@Column(name = "price")
    private String price;
	@Column(name = "cost_price")
    private String costPrice;
	@Column(name = "seller_id")
    private String sellerId;
	@Column(name = "create_time")
    private Date createTime;
	@Column(name = "check_time")
    private  Date checkTime;
	@Column(name = "audit_status")
    private String auditStatus;
	@Column(name = "start_time")
    private Date startTime;
	@Column(name = "end_time")
    private Date endTime;
	@Column(name = "num")
    private Integer num;
	@Column(name = "stock_count")
    private Integer stockCount;
	@Column(name = "introduction")
    private String introduction;
	@Column(name = "agency_id")
    private Long agencyId;
	@Column(name = "producter_id")
    private Long producterId;
    @Column(name = "classify")
    private int classify;
    @Transient
    private GoodsDesc goodsDesc;
    /** 商品SKU列表 */
    @Transient
    private List<Item> items;
    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public Goods() {
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }


    


}