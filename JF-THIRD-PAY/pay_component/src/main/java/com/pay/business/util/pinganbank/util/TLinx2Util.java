package com.pay.business.util.pinganbank.util;

//~--- non-JDK imports --------------------------------------------------------

import net.sf.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.pay.business.util.PaySignUtil;
import com.pay.business.util.pinganbank.config.TestParams;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Class TLinx2Util
 * Description
 * Create 2017-03-07 14:01:23
 * @author Benny.YEE
 */
public class TLinx2Util {



    /**
     * 签名
     * @param postMap
     * @return
     */
    public static String sign(Map<String, String> postMap) {
        String sortStr = null;
        String sign    = null;

        try {

            /**
             * 1 A~z排序(加上open_key)
             */
            sortStr = TLinxUtil.sort(postMap);

            /**
             * 2 sha1加密(小写)
             */
            String sha1 = TLinxSHA1.SHA1(sortStr);

            /**
             * 3 md5加密(小写)
             */
            sign = MD5.MD5Encode(sha1).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }
    /**
     * signRSA 
     * @param postMap
     * @param PRIVATE_KEY  私钥
     * @return    设定文件 
     * String    返回类型
     */
    public static String signRSA(Map<String, String> postMap,String PRIVATE_KEY) {
        String sortStr = null;
        String sign    = null;

        try {

            /**
             * 1 A~z排序(加上open_key)
             */
            sortStr = TLinxUtil.sort(postMap);
            sign = TLinxRSACoder.sign(sortStr.getBytes("utf-8"),PRIVATE_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }
    /**
     * 验签
     * @param respObject
     * @return
     */
    public static Boolean verifySign(JSONObject respObject,String OPEN_KEY) {
        String respSign = respObject.get("sign").toString();

        respObject.remove("sign");    // 删除sign节点
        respObject.put("open_key",OPEN_KEY);

        String veriSign = sign(respObject);    // 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名

        if (respSign.equals(veriSign)) {
            System.out.println("=====验签成功=====");

            return true;
        } else {
            System.out.println("=====验签失败=====");
        }

        return false;
    }

    /**
     * AES加密，再二进制转十六进制(bin2hex)
     * @param postmap 说明：
     * @throws Exception
     */
    public static void handleEncrypt(TreeMap<String, String> datamap, TreeMap<String, String> postmap,String OPEN_KEY) throws Exception {

        JSONObject dataobj = JSONObject.fromObject(datamap);
        
        String data    = TLinxAESCoder.encrypt(dataobj.toString(),OPEN_KEY);    // AES加密，并bin2hex

        postmap.put("data", data);
    }
    /**
     * AES加密，再二进制转十六进制(bin2hex)
     * @param datamap 说明：
     *
     * @return 返回值说明：
     * @throws Exception
     */
    public static String handleEncrypt(TreeMap<String, String> datamap,String OPEN_KEY) throws Exception {
        JSONObject dataobj = JSONObject.fromObject(datamap);
        return TLinxAESCoder.encrypt(dataobj.toString(),OPEN_KEY);    // AES加密，并bin2hex
    }
    /**
     * 签名
     * @param postmap
     */
    public static void handleSign(TreeMap<String, String> postmap,String OPEN_KEY) {
        Map<String, String> veriDataMap = new HashMap<String, String>();

        veriDataMap.putAll(postmap);
        veriDataMap.put("open_key",OPEN_KEY);

        // 签名
        String sign = sign(veriDataMap);

        postmap.put("sign", sign);
    }
    
    /**
     * 签名
     * @param postmap
     */
    public static void handleSignRSA(TreeMap<String, String> postmap,String OPEN_KEY,String PRIVATE_KEY) {
        Map<String, String> veriDataMap = new HashMap<String, String>();
        veriDataMap.putAll(postmap);
        veriDataMap.put("open_key",OPEN_KEY);
        // 签名
        String sign = signRSA(veriDataMap,PRIVATE_KEY);

        postmap.put("sign", sign);
    }
    /**
     * 签名
     * @param postmap
     */
    public static void handleSignRSA2(TreeMap<String, String> getmap,TreeMap<String, String> postmap,String OPEN_KEY,String PRIVATE_KEY) {
        Map<String, String> veriDataMap = new HashMap<String, String>();
        veriDataMap.putAll(getmap);
        veriDataMap.putAll(postmap);
        veriDataMap.put("open_key",OPEN_KEY);
        // 签名
        String sign = signRSA(veriDataMap,PRIVATE_KEY);

        postmap.put("sign", sign);
    }

    /**
     * 请求接口
     * @param postmap
     * @return      响应字符串
     */
    public static String handlePost(TreeMap<String, String> postmap, String interfaceName,int type) {
    	
    	System.out.println("postmap:" + PaySignUtil.getParamStr(postmap));
    	
        String url = null;
        if(type==1){
        	url = TestParams.OPEN_URL + interfaceName;
        	System.out.println("type1 url:" + url);
        }
        if(type==2){
        	String open_id=postmap.get("open_id");
        	String timestamp=postmap.get("timestamp");
        	String randStr=postmap.get("randStr");
        	String lang=postmap.get("lang");
        	url = TestParams.OPNE_GZH_URL + interfaceName+"?open_id="+open_id+ "&lang=" +lang+"&randStr="+randStr+"&timestamp="+timestamp;
        	postmap.remove("open_id");
        	postmap.remove("timestamp");
        	postmap.remove("randStr");
        	postmap.remove("lang");
//        	String data=postmap.get("data")+"&sign="+postmap.get("sign");
//        	postmap.put("data", data);
//        	postmap.remove("sign");
        }
        if (url.contains("https")) {
            return HttpsUtil.httpMethodPost(url, postmap, "UTF-8");
        } else {
            return HttpUtil.httpMethodPost(url, postmap, "UTF-8");
        }
    }
    /**
     * 请求接口
     * @param postmap
     * @param uri 说明：
     * @return      响应字符串
     */
    public static String handlePost(TreeMap<String, String> postmap, String uri) {
        String url = TestParams.OPNE_GZH_URL+ uri;
        if (url.contains("https")) {
            return HttpsUtil.httpMethodPost(url, postmap, "UTF-8");
        } else {
            return HttpUtil.httpMethodPost(url, postmap, "UTF-8");
        }
    }
    /**
     * Method main
     * Description 说明：
     *
     * @param args 说明：
     */
//    public static void main(String[] args) {
//
//        // 初始化参数
//        String pmtType   = "0,1,2,3";
//        String timestamp = new Date().getTime() / 1000 + "";    // 时间
//
//		try {
//			// 固定参数
//			TreeMap<String, String> postmap = new TreeMap<String, String>();//请求参数的map
//			postmap.put("open_id","");
//			postmap.put("timestamp", timestamp);
//
//			TreeMap<String, String> datamap = new TreeMap<String, String>();//data参数的map
//			datamap.put("pmt_type", pmtType);
//
//			/**
//			 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
//			 */
//			handleEncrypt(datamap, postmap);
//
//            /**
//             * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
//             */
//            handleSign(postmap);
//
//            /**
//             * 3 请求、响应
//             */
//            String rspStr = handlePost(postmap, TestParams.PAYLIST);
//
//
//            /**
//             * 4 验签  有data节点时才验签
//             */
//            JSONObject respObject = JSONObject.fromObject(rspStr);
//
//            Object dataStr    = respObject.get("data");
//			System.out.println("返回data字符串="+dataStr);
//
//            if (!rspStr.isEmpty() || (dataStr != null)) {
//                if (verifySign(respObject)) {    // 验签成功
//
//                    /**
//                     * 5 AES解密，并hex2bin
//                     */
//                    String respData = TLinxAESCoder.decrypt(dataStr.toString(), TestParams.OPEN_KEY);
//
//                    System.out.println("==================响应data内容:" + respData);
//                } else {
//                    System.out.println("=====验签失败=====");
//                }
//            } else {
//                System.out.println("=====没有返回data数据=====");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}


//~ Formatted by Jindent --- http://www.jindent.com
