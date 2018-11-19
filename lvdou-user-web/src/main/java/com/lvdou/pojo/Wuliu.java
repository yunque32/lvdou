package com.lvdou.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "tb_wuliu")
public class Wuliu implements Serializable {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "pick_time")
    private Date pickTime;
    @Column(name = "driver_man")
    private String driverMan;
    @Column(name = "pick_place")
    private String pickPlace;
    @Column(name = "ruku_time")
    private Date rukuTime;
    @Column(name = "chuku_time")
    private Date chukuTime;
    @Column(name = "cangku_address")
    private String cangkuAddress;
    @Column(name = "save_style")
    private String saveStyle;
    @Column(name = "compancy")
    private String compancy;

    public String getDriverMan() {
        return driverMan;
    }

    public void setDriverMan(String driverMan) {
        this.driverMan = driverMan;
    }

    public String getCompancy() {
        return compancy;
    }

    public void setCompancy(String compancy) {
        this.compancy = compancy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }


    public String getPickPlace() {
        return pickPlace;
    }

    public void setPickPlace(String pickPlace) {
        this.pickPlace = pickPlace;
    }

    public Date getRukuTime() {
        return rukuTime;
    }

    public void setRukuTime(Date rukuTime) {
        this.rukuTime = rukuTime;
    }

    public Date getChukuTime() {
        return chukuTime;
    }

    public void setChukuTime(Date chukuTime) {
        this.chukuTime = chukuTime;
    }

    public String getCangkuAddress() {
        return cangkuAddress;
    }

    public void setCangkuAddress(String cangkuAddress) {
        this.cangkuAddress = cangkuAddress;
    }

    public String getSaveStyle() {
        return saveStyle;
    }

    public void setSaveStyle(String saveStyle) {
        this.saveStyle = saveStyle;
    }






}
