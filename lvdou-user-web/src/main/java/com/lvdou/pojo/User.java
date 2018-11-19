package com.lvdou.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="tb_user")
public class User implements Serializable{
    
	private static final long serialVersionUID = -743836578906316194L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@Column(name="username")
    private String username;
	@Column(name="password")
    private String password;
    @Column(name = "role_list")
    private String roleList;
	@Column(name="phone")
    private String phone;
	@Column(name="email")
    private String email;
	@Column(name="created")
    private Date created;
	@Column(name="updated")
    private Date updated;
	@Column(name="source_type")
    private String sourceType;
	@Column(name="nick_name")
    private String nickName;
	@Column(name="name")
    private String name;
	@Column(name="status")
    private String status;
	@Column(name="head_pic")
    private String headPic;
	@Column(name="qq")
    private String qq;
	@Column(name="account_balance")
    private Long accountBalance;
	@Column(name="mobile_check")
    private String mobileCheck;
	@Column(name="email_check")
    private String emailCheck;
	@Column(name="sex")
    private String sex;
	@Column(name="user_level")
    private Integer userLevel;
	@Column(name="points")
    private Integer points;
	@Column(name="experience_value")
    private Integer experienceValue;
	@Column(name="birthday")
    private Date birthday;
	@Column(name="last_login_time")
    private Date lastLoginTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
    public String getRoleList() {
        return roleList;
    }

    public void setRoleList(String roleList) {
        this.roleList = roleList;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType == null ? null : sourceType.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic == null ? null : headPic.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public Long getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Long accountBalance) {
        this.accountBalance = accountBalance;
    }



    public String getMobileCheck() {
        return mobileCheck;
    }

    public void setMobileCheck(String mobileCheck) {
        this.mobileCheck = mobileCheck;
    }

    public String getEmailCheck() {
        return emailCheck;
    }

    public void setEmailCheck(String emailCheck) {
        this.emailCheck = emailCheck;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getExperienceValue() {
        return experienceValue;
    }

    public void setExperienceValue(Integer experienceValue) {
        this.experienceValue = experienceValue;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public User() {
    }

    public User(String username, String password, String phone,
                String email, Date created, Date updated,
                String sourceType, String nickName, String name,
                String status, String headPic, String qq,
                Long accountBalance, String mobileCheck,
                String emailCheck, String sex, Integer userLevel,
                Integer points, Integer experienceValue,
                Date birthday, Date lastLoginTime) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.created = created;
        this.updated = updated;
        this.sourceType = sourceType;
        this.nickName = nickName;
        this.name = name;
        this.status = status;
        this.headPic = headPic;
        this.qq = qq;
        this.accountBalance = accountBalance;
        this.mobileCheck = mobileCheck;
        this.emailCheck = emailCheck;
        this.sex = sex;
        this.userLevel = userLevel;
        this.points = points;
        this.experienceValue = experienceValue;
        this.birthday = birthday;
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", sourceType='" + sourceType + '\'' +
                ", nickName='" + nickName + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", headPic='" + headPic + '\'' +
                ", qq='" + qq + '\'' +
                ", accountBalance=" + accountBalance +
                ", mobileCheck='" + mobileCheck + '\'' +
                ", emailCheck='" + emailCheck + '\'' +
                ", sex='" + sex + '\'' +
                ", userLevel=" + userLevel +
                ", points=" + points +
                ", experienceValue=" + experienceValue +
                ", birthday=" + birthday +
                ", lastLoginTime=" + lastLoginTime +
                '}';
    }
}