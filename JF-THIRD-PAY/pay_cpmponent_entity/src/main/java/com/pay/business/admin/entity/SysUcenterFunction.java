package com.pay.business.admin.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * TABLE:.sys_ucenter_function -------------------------------------------------------- id Long(19) NOTNULL // app_id Integer(10) NOTNULL //应用ID（可以理解为那个系统） fid Long(19) NOTNULL //父级ID fun_code
 * String(120) NOTNULL //功能编码 fun_name String(128) NOTNULL //功能名称 fun_status Integer(10) NOTNULL 0 //功能当前状态 0-有效；1-无效 fun_path String(128) //功能路径 fun_remark String(1024) //备注 create_time Date(19)
 * NOTNULL 0000-00-00 00:00:00 //创建时间 create_user_by Long(19) NOTNULL //创建用户id update_time Date(19) CURRENT_TIMESTAMP //最后一次修改时间 update_user_by Long(19) //最后一次修改的用户id
 */
public class SysUcenterFunction implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8819768370935665509L;

	private Long id;

    private Integer appId;

    private Long fid;

    private String funCode;

    private String funName;

    private Integer funStatus;

    private String funPath;

    private String funRemark;
    
    private String funIcon;

	private Date createTime;

    private Long createUserBy;

    private Date updateTime;

    private Long updateUserBy;

    private Integer funSort;//排序
    


    public String getFunIcon() {
		return funIcon;
	}

	public void setFunIcon(String funIcon) {
		this.funIcon = funIcon;
	}

    /**
     * id Long(19) NOTNULL //
     */
    public Long getId() {
        return id;
    }

    /**
     * id Long(19) NOTNULL //
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * app_id Integer(10) NOTNULL //应用ID（可以理解为那个系统）
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * app_id Integer(10) NOTNULL //应用ID（可以理解为那个系统）
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * fid Long(19) NOTNULL //父级ID
     */
    public Long getFid() {
        return fid;
    }

    /**
     * fid Long(19) NOTNULL //父级ID
     */
    public void setFid(Long fid) {
        this.fid = fid;
    }

    /**
     * fun_code String(120) NOTNULL //功能编码
     */
    public String getFunCode() {
        return funCode;
    }

    /**
     * fun_code String(120) NOTNULL //功能编码
     */
    public void setFunCode(String funCode) {
        this.funCode = funCode;
    }

    /**
     * fun_name String(128) NOTNULL //功能名称
     */
    public String getFunName() {
        return funName;
    }

    /**
     * fun_name String(128) NOTNULL //功能名称
     */
    public void setFunName(String funName) {
        this.funName = funName;
    }

    /**
     * fun_status Integer(10) NOTNULL 0 //功能当前状态 0-有效；1-无效
     */
    public Integer getFunStatus() {
        return funStatus;
    }

    /**
     * fun_status Integer(10) NOTNULL 0 //功能当前状态 0-有效；1-无效
     */
    public void setFunStatus(Integer funStatus) {
        this.funStatus = funStatus;
    }

    /**
     * fun_path String(128) //功能路径
     */
    public String getFunPath() {
        return funPath;
    }

    /**
     * fun_path String(128) //功能路径
     */
    public void setFunPath(String funPath) {
        this.funPath = funPath;
    }

    /**
     * fun_remark String(1024) //备注
     */
    public String getFunRemark() {
        return funRemark;
    }

    /**
     * fun_remark String(1024) //备注
     */
    public void setFunRemark(String funRemark) {
        this.funRemark = funRemark;
    }

    /**
     * create_time Date(19) NOTNULL 0000-00-00 00:00:00 //创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * create_time Date(19) NOTNULL 0000-00-00 00:00:00 //创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * create_user_by Long(19) NOTNULL //创建用户id
     */
    public Long getCreateUserBy() {
        return createUserBy;
    }

    /**
     * create_user_by Long(19) NOTNULL //创建用户id
     */
    public void setCreateUserBy(Long createUserBy) {
        this.createUserBy = createUserBy;
    }

    /**
     * update_time Date(19) CURRENT_TIMESTAMP //最后一次修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * update_time Date(19) CURRENT_TIMESTAMP //最后一次修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * update_user_by Long(19) //最后一次修改的用户id
     */
    public Long getUpdateUserBy() {
        return updateUserBy;
    }

    /**
     * update_user_by Long(19) //最后一次修改的用户id
     */
    public void setUpdateUserBy(Long updateUserBy) {
        this.updateUserBy = updateUserBy;
    }

	public Integer getFunSort() {
		return funSort;
	}

	public void setFunSort(Integer funSort) {
		this.funSort = funSort;
	}

    
}