package com.pay.business.util.hfbpay.WeChatSubscrip.pay;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
* @Title: XmlUtil.java 
* @Package com.pay.business.util.hfbpay.WeChatSubscrip.pay 
* @Description: 解析汇付宝退款返回的报文XML
* @author ZHOULIBO   
* @date 2017年7月12日 上午10:58:25 
* @version V1.0
*/
public class XmlUtil {
	public static boolean jiexiXml(String xml){
		boolean xmlYes=false;
		//创建一个新的字符串
        StringReader read = new StringReader(xml);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
            System.out.println(root.getName());//输出根元素的名称（测试）
            //得到根元素所有子元素的集合
            List jiedian = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Namespace ns = root.getNamespace();
            Element et = null;
			for (int i = 0; i < jiedian.size(); i++) {
				et = (Element) jiedian.get(i);// 循环依次得到子元素
				if(et.getName().equals("ret_code")&&et.getText().equals("0000")){
					xmlYes=true;
					break;
				}
			}
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return xmlYes;
		
	}
	
	public static Map<String,Object> jiexiXml1(String xml){
		Map<String,Object> info=new HashMap<String, Object>();
		//创建一个新的字符串
        StringReader read = new StringReader(xml);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
            System.out.println(root.getName());//输出根元素的名称（测试）
            //得到根元素所有子元素的集合
            List jiedian = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Namespace ns = root.getNamespace();
            Element et = null;
			for (int i = 0; i < jiedian.size(); i++) {
				et = (Element) jiedian.get(i);// 循环依次得到子元素
				if(et.getName().equals("ret_code")&&et.getText().equals("0000")){
					info.put("ret_code", "10000");
				}
				if(et.getName().equals("detail_data")){
					info.put("textInfo",et.getText());
				}
				if(et.getName().equals("total_page")){
					info.put("total_page",et.getText());
				}
			}
			//如果没有code 证明对账失败
			if(!info.containsKey("ret_code")){
				//获取失败原因
				for (int i = 0; i < jiedian.size(); i++) {
					et = (Element) jiedian.get(i);// 循环依次得到子元素
					if(et.getName().equals("ret_msg")&&!et.getText().equals("")){
						info.put("ret_code", "10001");
						info.put("ret_msg",et.getText());
					}
				}
			}	
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return info;
		
	}
	public static void main(String[] args) {
//		String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?> <root><ret_code>0000</ret_code><ret_msg>操作成功</ret_msg><agent_id>1602809</agent_id><sign>e61fb9a553bdecbfc52e674b32a37b2b</sign></root>";
		String xml1="<?xml version=\"1.0\" encoding=\"utf-8\"?><root><ret_code>0000</ret_code><ret_msg></ret_msg><agent_id>1664502</agent_id><detail_data>"
				+ "H1701062084825AT,101697400220170106031522215CWZ,2017-1-6 15:15:22,2017-1-6 15:18:14,0.01,微信支付,0.01\n"
				+ "H1701062080123AP,101697400220170106031750042LoI,2017-1-6 15:17:50,2017-1-6 15:20:52,0.01,支付宝,0.01\n"
				+ "H1701062083497AE,101713400220170106031801923ggH,2017-1-6 15:18:01,2017-1-6 15:21:03,0.01,支付宝,0.01\n</detail_data>"
				+ "<total_page>2</total_page><total_count>53</total_count><sign>1a256c75beb00734f096ea01e348279a</sign></root>";
//		jiexiXml(xml);
		Map<String,Object> info=jiexiXml1(xml1);
		String infoMap=	(String) info.get("textInfo");
		String []infoMap2=	infoMap.split("\n");
		for (int i = 0; i < infoMap2.length; i++) {
			String indo=infoMap2[i];
			System.out.println(indo);
			String indo2[]=indo.split(",");
			System.out.println("商户订单号："+indo2[1]);
			System.out.println("交易金额："+indo2[4]);
			System.out.println("交易时间："+indo2[3]);
		}
//		System.out.println(info);
//		System.out.println(infoMap2);
	}
}
