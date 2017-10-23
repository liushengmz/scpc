package com.pay.business.util.wxpay;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: XMLUtil 
 * @Description: xml工具类
 * @author yangyu
 * @date 2016年11月10日 上午11:47:02
 */
public class XMLUtil {

	/**
	 * 输入流转换xml对象
	 * @param input
	 * @return
	 * @throws Exception
	 */
    public static String toXMLString(InputStream input) throws Exception {
        try {
            return new SAXReader().read(input).asXML();
        } finally {
            input.close();
        }
    }

    /**
     * 输入流转 map对象
     * @param input
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> toMap(InputStream input) throws Exception {
        try {
            Document doc = new SAXReader().read(input);
            Element root = doc.getRootElement();
            List<Element> elements = root.elements();
            Map<String, String> map = new TreeMap<String, String>();
            for (Element element : elements) {
                map.put(element.getName(), element.getText());
            }
            return map;
        } finally {
            input.close();
        }
    }

    /**
     * xml 字符串转 map
     * @param xml
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> toMap(String xml) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        Document doc = DocumentHelper.parseText(xml);
        Element root = doc.getRootElement();
        List<Element> elements = root.elements();
        for (Element element : elements) {
            map.put(element.getName(), element.getText());
        }
        return map;
    }

    /**
     * Map转xml
     * @param map
     * @return
     * @throws Exception
     */
    @SuppressWarnings("all")
    public static String toXML(Map map) throws Exception {
        return toXML(map, null);
    }

    /**
     * Map转xml
     * @param map
     * @param e
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static String toXML(Map<String, Object> map, Element e) throws Exception {
        if (e == null) {
            e = DocumentHelper.createDocument().addElement("xml");
        }
        for (String key : map.keySet()) {
            Object val = map.get(key);
            if (val != null && val instanceof Map) {
                toXML((Map<String, Object>) val, e.addElement(key));
            } else if (val instanceof Collection) {
                Collection<Object> c = (Collection<Object>) val;
                Iterator<Object> it = c.iterator();
                while (it.hasNext()) {
                    toXML((Map<String, Object>) it.next(), e.addElement(key));
                }
            } else {
                e.addElement(key).setText(val != null ? val.toString() : "");
            }
        }
        return e.asXML();
    }

    /**
     * String 转 org.dom4j.Document
     * @param xml
     * @return
     * @throws DocumentException
     */
	public static Document strToDocument(String xml) throws DocumentException {
		return DocumentHelper.parseText(xml);
	}

	/**
	 * xml转换为JSON字符串
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static String xmlToJSONString(String xml) throws Exception{
		Map<String, String> map = toMap(xml);
		return JSON.toJSONString(map);
	}
	
	/**
	 * JSON字符串转换为XML
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static String jsonToXml(String jsonString) throws Exception{
		return jsonToXml(JSON.parseObject(jsonString));
	}
	
	/**
	 * JSON转XML 
	 * @param json 使用的FastJSON
	 * @return
	 * @throws Exception
	 */
	public static String jsonToXml(JSON json) throws Exception{
		Map map = JSON.toJavaObject(json, Map.class);
		return toXML(map);
	}
	
	/**
	 * 去除字符串两端的"xml"字符
	 * @param xml
	 * @return
	 */
	public static String removeXmlSign(final String xml){
		if(StringUtils.isNotBlank(xml)){
			String resultXml = null;
			resultXml = StringUtils.trim(xml);
			if(StringUtils.startsWith(resultXml, "<xml>")){
				resultXml = StringUtils.substring(resultXml, 5, resultXml.length());
				if(StringUtils.endsWith(resultXml, "</xml>")){
					int length = resultXml.length();
					resultXml = StringUtils.substring(resultXml, 0, length-6);
				}
			}
			return resultXml;
		}else{
			throw new NullPointerException();
		}
	}
	
}
