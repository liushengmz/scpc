package com.system.model;

public class SpTroneApiModel
{
	private int id;
	private String name;
	private int matchField;
	private String matchKeyword;
	private String apiFields;
	private int locateMatch;
	private String apiParametes;
	
	public String getApiParametes() {
		return apiParametes;
	}
	public void setApiParametes(String apiParametes) {
		this.apiParametes = apiParametes;
	}
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
	public int getMatchField()
	{
		return matchField;
	}
	public void setMatchField(int matchField)
	{
		this.matchField = matchField;
	}
	public String getMatchKeyword()
	{
		return matchKeyword;
	}
	public void setMatchKeyword(String matchKeyword)
	{
		this.matchKeyword = matchKeyword;
	}
	public String getApiFields()
	{
		return apiFields;
	}
	public void setApiFields(String apiFields)
	{
		this.apiFields = apiFields;
	}
	public int getLocateMatch()
	{
		return locateMatch;
	}
	public void setLocateMatch(int locateMatch)
	{
		this.locateMatch = locateMatch;
	}
	
}
