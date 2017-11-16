
package com.andy.system.util;

import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 * XMl串处理
 * 
 * @author computer
 *
 */
public class XmlUtils
{
	/**
	 * xml解析为map
	 * 
	 * @param ins
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> readSimpleXml(InputStream ins)
			throws Exception
	{
		Map<String, String> ret = new HashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(ins);
		Element root = doc.getRootElement();
		List<Element> dataList = root.getChildren();
		Iterator<Element> i = dataList.iterator();
		while (i.hasNext())
		{
			Element e = (Element) i.next();
			ret.put(e.getName(), e.getValue());
		}
		return ret;
	}

	/**
	 * xml解析为map
	 * 
	 * @param ins
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> readSimpleXml(String xmlStr)
			throws Exception
	{
		Map<String, String> ret = new HashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		StringReader reader = new StringReader(xmlStr);
		InputSource source = new InputSource(reader);
		Document doc = builder.build(source);
		Element root = doc.getRootElement();
		List<Element> dataList = root.getChildren();
		Iterator<Element> i = dataList.iterator();
		while (i.hasNext())
		{
			Element e = (Element) i.next();
			ret.put(e.getName(), e.getValue());
		}
		return ret;
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
