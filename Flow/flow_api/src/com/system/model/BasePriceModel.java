package com.system.model;

public class BasePriceModel
{
	private int id;
	private String name;
	private int num;
	private int price;
	private int operator;
	
	public int getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public int getNum()
	{
		return num;
	}
	public int getPrice()
	{
		return price;
	}
	public int getOperator()
	{
		return operator;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
	public void setPrice(int price)
	{
		this.price = price;
	}
	public void setOperator(int operator)
	{
		this.operator = operator;
	}
	
	
}
