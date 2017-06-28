package com.system.model;

public class UserModel
{
	private int id;
	private String name;
	private String password;
	private String nickName;
	private String mail;
	private String qq;
	private String phone;
	private int status;
	private String createUser;
	private int createUserId;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getNickName()
	{
		return nickName;
	}
	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}
	public String getMail()
	{
		return mail;
	}
	public void setMail(String mail)
	{
		this.mail = mail;
	}
	public String getQq()
	{
		return qq;
	}
	public void setQq(String qq)
	{
		this.qq = qq;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	@Override
	public boolean equals(Object model)
	{
		if(model==null)
			return false;
		
		if(model instanceof UserModel)
		{
			UserModel user = (UserModel)model;
			if(user.getName()!=null && user.getName().equalsIgnoreCase(this.name))
			{
				if(user.getPassword()!=null && user.getPassword().equalsIgnoreCase(this.password))
					return true;
			}
		}
		
		return super.equals(model);
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public int getCreateUserId()
	{
		return createUserId;
	}
	public void setCreateUserId(int createUserId)
	{
		this.createUserId = createUserId;
	}
	
}
