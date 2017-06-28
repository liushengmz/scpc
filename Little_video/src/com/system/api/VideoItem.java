
package com.system.api;

import com.system.model.LvImgModel;
import com.system.model.LvVideoBaseModel;
import com.system.server.LvImgServer;
import com.system.util.ConfigManager;

public class VideoItem
{
	int						id;
	String					imgs;
	String					name;
	String					url;
	private static final String	pfxImg;
	private static final String	pfxVideo;

	static
	{
		pfxImg = ConfigManager.getConfigData("LittleVideoImg");
		pfxVideo = ConfigManager.getConfigData("LittleVideoVideo");
	}

	public VideoItem()
	{
	}

	public VideoItem(LvVideoBaseModel m)
	{
		this.setId(m.getId());
		this.setImgs(null);
		this.setName(m.getName());
		String url = m.getPath();

		this.setUrl(String.format(pfxVideo, url));
		LvImgModel imgM = new LvImgServer().getLvImgById(m.getImgId());
		if (imgM == null)
			return;
		url = imgM.getPath();
		this.setImgs(String.format(pfxImg, url));
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getImgs()
	{
		return imgs;
	}

	public void setImgs(String imgs)
	{
		this.imgs = imgs;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

}
