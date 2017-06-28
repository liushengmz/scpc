package com.dianfu.web.HttpHandler;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dianfu.logical.SPCallbackProces.ErrCode;

/** MM基地回调，处理实例 */
public class SPCallbackMMBaseProc extends SPCallbackAutoProc {

	private Element _root;

	@Override
	protected boolean onInit() {
		SAXReader reader=new SAXReader();
		org.dom4j.Document xml =null;
		try {
			  xml = reader.read(getRequest().getInputStream());
		} catch ( Exception e) {
			return false;
		}
		_root=xml.getRootElement();
		return super.onInit();
	}

	@Override
	protected String getParamValue(String field) {
		// _root.getRootElement().elements(arg0)
		if (_root == null)
			return null;

		for (Object obj : _root.elements()) {
			Element node = (Element) obj;
			if (node.getNodeType() != Element.ELEMENT_NODE)
				continue;
			if (node.getName().equalsIgnoreCase(field)) {
				return node.getStringValue();
			}
		}

		return super.getParamValue(field);
	}

	@Override
	protected void write(ErrCode errCode, String msg) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SyncAppOrderResp xmlns=\"http://www.monternet.com/dsmp/schemas/\"><TransactionID>"
				+ getParamValue("OrderId") + "</TransactionID>"
				+ "<MsgType>SyncAppOrderResp</MsgType><Version>1.0.0</Version><hRet>0</hRet></SyncAppOrderResp>";
		super.write(xml);
		super.write("<!--" + msg + "-->");
	}

}
