/**
 * Project Name:pay-protocol
 * File Name:Xml.java
 * Package Name:cn.swiftpass.pay.protocol
 * Date:2014-8-10下午10:48:21
 *
*/

package com.pay.business.util.xyBankWeChatPay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

@SuppressWarnings({"rawtypes","unchecked"})
public class XmlUtils {
   
    /**
    * @Title: parseXML 
    * @Description:map->String xml
    * @param @param parameters
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
    */
    public static String parseXML(SortedMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if (null != v && !"".equals(v) && !"attach".equals(k)&&!"body".equals(k)&&!"body".equals(k)&&!"sign".equals(k)) {
                sb.append("<" + k + ">" + parameters.get(k) + "</" + k + ">\n");
            }else{
            	 sb.append("<" + k + "><![CDATA[" + parameters.get(k) + "]]></" + k + ">\n");
//            	 sb.append("<" + k + ">" + parameters.get(k) + "</" + k + ">\n");
            }
        }
        sb.append("</xml>");
        return sb.toString();
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
}

