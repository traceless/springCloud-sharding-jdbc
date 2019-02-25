package com.phantoms.framework.cloudbase.dbconfig.model;

import java.util.Date;

public class DbConfig {
    /**
     * @Fields id 
     */
    private Integer id;

    /**
     * @Fields name 数据库名称，最好不要中文
     */
    private String name;

    /**
     * @Fields masterUrl 数据库连接
     */
    private String masterUrl;

    /**
     * @Fields position 分库对应的分区的位置，小于1000
     */
    private Integer position;

    /**
     * @Fields slaveUrl 从数据库连接
     */
    private String slaveUrl;

    /**
     * @Fields remark 备注
     */
    private String remark;

    /**
     * @Fields md5 MD5 值，用于校验数据库配置是否正确，防止手动改数据库
     */
    private String md5;

    /**
     * @Fields username 登录名（主从数据库共用）
     */
    private String username;

    /**
     * @Fields password 密码（主从数据库共用）
     */
    private String password;

    /**
     * @Fields moduleCode 模块编码，垂直分库，属于哪个模块功能的库
     */
    private String moduleCode;

    /**
     * @Fields status 状态，生产，扩容中，测试，同步收集
     */
    private String status;

    /**
     * @Fields effectiveTime 生效日期
     */
    private Date effectiveTime;

    /**
     * @Fields hlod1 
     */
    private String hlod1;

    /**
     * @Fields hlod2 
     */
    private String hlod2;

    /**
     * @Fields hlod3 
     */
    private String hlod3;

    /**
     * @Fields createTime 
     */
    private Date createTime;

    /**
     * @Fields updateTime 
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl == null ? null : masterUrl.trim();
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getSlaveUrl() {
        return slaveUrl;
    }

    public void setSlaveUrl(String slaveUrl) {
        this.slaveUrl = slaveUrl == null ? null : slaveUrl.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
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

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode == null ? null : moduleCode.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getHlod1() {
        return hlod1;
    }

    public void setHlod1(String hlod1) {
        this.hlod1 = hlod1 == null ? null : hlod1.trim();
    }

    public String getHlod2() {
        return hlod2;
    }

    public void setHlod2(String hlod2) {
        this.hlod2 = hlod2 == null ? null : hlod2.trim();
    }

    public String getHlod3() {
        return hlod3;
    }

    public void setHlod3(String hlod3) {
        this.hlod3 = hlod3 == null ? null : hlod3.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}