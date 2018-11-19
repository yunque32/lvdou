package com.lvdou.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_agency")
public class Agency implements Serializable {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "agency_name")
    private String agencyName;

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    @Column(name = "agency_manager")
    private String agencyManager;
    @Column(name = "manager_phone")
    private Long managerPhone;
    @Column(name = "sale_address")
    private String saleAddress;
    @Column(name = "license_id")
    private String liceseId;
    @Column(name = "office_address")
    private String officeAddress;
    @Column(name = "office_phone")
    private String officePhone;
    @Column(name = "complaint_time")
    private Integer complaintTime;

    public String getAgencyManager() {
        return agencyManager;
    }

    public void setAgencyManager(String agencyManager) {
        this.agencyManager = agencyManager;
    }

    @Column(name = "grade")
    private Integer grade;
    @Column(name = "wuliuid")
    private Long wuliuid;


    public String getSaleAddress() {
        return saleAddress;
    }

    public void setSaleAddress(String saleAddress) {
        this.saleAddress = saleAddress;
    }


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

    public String getLiceseId() {
        return liceseId;
    }

    public void setLiceseId(String liceseId) {
        this.liceseId = liceseId;
    }



}
