package com.system.flow.model;

public class SpModel
{
	private int id;
	private String fullName;
	private String shortName;
	private int currency;
	private String contactPerson;
	private int commerceUserId;
	private String phone;
	private String mail;
	private int status;
	
	public int getId()
	{
		return id;
	}
	public String getFullName()
	{
		return fullName;
	}
	public String getShortName()
	{
		return shortName;
	}
	public int getCurrency()
	{
		return currency;
	}
	public String getContactPerson()
	{
		return contactPerson;
	}
	public int getCommerceUserId()
	{
		return commerceUserId;
	}
	public String getPhone()
	{
		return phone;
	}
	public String getMail()
	{
		return mail;
	}
	public int getStatus()
	{
		return status;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}
	public void setCurrency(int currency)
	{
		this.currency = currency;
	}
	public void setContactPerson(String contactPerson)
	{
		this.contactPerson = contactPerson;
	}
	public void setCommerceUserId(int commerceUserId)
	{
		this.commerceUserId = commerceUserId;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public void setMail(String mail)
	{
		this.mail = mail;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	
}
