package com.system.model;

public class SpTroneApiModel
{
	private int id;
	private String name;
	private int matchField;
	private String matchKeyWord;
	private String apiFiles;
	private int locateMatch;
	
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
	public String getMatchKeyWord()
	{
		return matchKeyWord;
	}
	public void setMatchKeyWord(String matchKeyWord)
	{
		this.matchKeyWord = matchKeyWord;
	}
	public String getApiFiles()
	{
		return apiFiles;
	}
	public void setApiFiles(String apiFiles)
	{
		this.apiFiles = apiFiles;
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
