package com.phantoms.framework.cloudbase.dbconfig.model;

import java.util.Date;

public class ModuleConfig {
    /**
     * @Fields id 
     */
    private Integer id;

    /**
     * @Fields moduleCode 模块编码
     */
    private String moduleCode;

    /**
     * @Fields moduleName 模块名称
     */
    private String moduleName;

    /**
     * @Fields remark 
     */
    private String remark;

    /**
     * @Fields tableNum 分表的数量，用于求模定位具体的表
     */
    private Integer tableNum;

    /**
     * @Fields hold1 
     */
    private String hold1;

    /**
     * @Fields hlod2 
     */
    private String hlod2;

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

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode == null ? null : moduleCode.trim();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getTableNum() {
        return tableNum;
    }

    public void setTableNum(Integer tableNum) {
        this.tableNum = tableNum;
    }

    public String getHold1() {
        return hold1;
    }

    public void setHold1(String hold1) {
        this.hold1 = hold1 == null ? null : hold1.trim();
    }

    public String getHlod2() {
        return hlod2;
    }

    public void setHlod2(String hlod2) {
        this.hlod2 = hlod2 == null ? null : hlod2.trim();
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