package com.phantoms.framework.cloudbase.dbconfig.model;

import java.util.Date;

public class TableConfig {
    /**
     * @Fields id 
     */
    private Integer id;

    /**
     * @Fields tableName 
     */
    private String tableName;

    /**
     * @Fields tableColumns 分库字段，column1,column2
     */
    private String tableColumns;

    /**
     * @Fields moduleCode 
     */
    private String moduleCode;

    /**
     * @Fields remark 
     */
    private String remark;

    /**
     * @Fields hold2 
     */
    private String hold2;

    /**
     * @Fields hold3 
     */
    private String hold3;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(String tableColumns) {
        this.tableColumns = tableColumns == null ? null : tableColumns.trim();
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode == null ? null : moduleCode.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getHold2() {
        return hold2;
    }

    public void setHold2(String hold2) {
        this.hold2 = hold2 == null ? null : hold2.trim();
    }

    public String getHold3() {
        return hold3;
    }

    public void setHold3(String hold3) {
        this.hold3 = hold3 == null ? null : hold3.trim();
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