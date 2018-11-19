package com.lvdou.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_producter")
public class Producter implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "base_name")
    private String baseName;
    @Column(name = "manager")
    private String manager;
    @Column(name = "manager_phone")
    private Long managerPhone;
    @Column(name = "office_address")
    private String officeAddress;
    @Column(name = "office_phone")
    private String officePhone;
    @Column(name = "license_id")
    private String licenseId;
    @Column(name = "complaint_time")
    private Integer complaintTime;
    @Column(name = "product_time")
    private Integer productTime;
    @Column(name = "grade")
    private Integer grade;
    @Column(name = "wuliuid")
    private Long wuliuid;


    public Long getWuliuid() {
        return wuliuid;
    }
    public void setWuliuid(Long wuliuid) {
        this.wuliuid = wuliuid;
    }
    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public Integer getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(Integer complaintTime) {
        this.complaintTime = complaintTime;
    }

    public Integer getProductTime() {
        return productTime;
    }

    public void setProductTime(Integer productTime) {
        this.productTime = productTime;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Long getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(Long managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }


}
