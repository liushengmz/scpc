package com.system.model;

public class SysCodeModel
{
	private int id;
	private int flag;
	private String codeName;
	private String remark;
	
	public SysCodeModel()
	{
		
	}
	
	public SysCodeModel(int type)
	{
		this.id = -1;
		this.flag = 0;
		this.codeName = "未知状态";
		this.remark = "数据库未找到对应的数据";
	}
	
	public int getId()
	{
		return id;
	}
	public int getFlag()
	{
		return flag;
	}
	public String getCodeName()
	{
		return codeName;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setFlag(int flag)
	{
		this.flag = flag;
	}
	public void setCodeName(String codeName)
	{
		this.codeName = codeName;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
}
