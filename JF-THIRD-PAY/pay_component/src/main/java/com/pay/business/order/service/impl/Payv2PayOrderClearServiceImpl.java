package com.pay.business.order.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.ftp.FtpUploadClient;
import com.core.teamwork.base.util.insertid.ZipUtil;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.csvreader.CsvReader;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.mapper.Payv2BussCompanyAppMapper;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.mapper.Payv2BussCompanyShopMapper;
import com.pay.business.order.entity.OrderClearVO;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.Payv2PayOrderClear;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.entity.Payv2PayOrderVO;
import com.pay.business.order.mapper.Payv2PayOrderClearMapper;
import com.pay.business.order.mapper.Payv2PayOrderMapper;
import com.pay.business.order.mapper.Payv2PayOrderRefundMapper;
import com.pay.business.order.service.Payv2PayOrderClearService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.mapper.Payv2PayWayMapper;
import com.pay.business.payway.mapper.Payv2PayWayRateMapper;
import com.pay.business.record.entity.Payv2BillDown;
import com.pay.business.record.entity.Payv2BussWayDetail;
import com.pay.business.record.entity.Payv2DayCompanyClear;
import com.pay.business.record.mapper.Payv2BillDownMapper;
import com.pay.business.record.mapper.Payv2BussWayDetailMapper;
import com.pay.business.record.mapper.Payv2DayCompanyClearMapper;
import com.pay.business.util.CSVUtils;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.PayRateDictValue;
import com.pay.business.util.alipay.AliPay;
import com.pay.business.util.alipay.PayConfigApi;
import com.pay.business.util.hfbpay.WeChatSubscrip.pay.HfbWeChatPay;
import com.pay.business.util.minshengbank.HttpMinshengBank;
import com.pay.business.util.pinganbank.pay.PABankPay;
import com.pay.business.util.pinganbank.pay.PANOrderVo;
import com.pay.business.util.wftpay.WftWeChatPay;
import com.pay.business.util.xyBankWeChatPay.XyBankPay;
import com.pay.business.util.xyShenzhen.XYSZBankPay;

import javassist.expr.NewArray;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author cyl
 * @version
 */
@Service("payv2PayOrderClearService")
public class Payv2PayOrderClearServiceImpl extends BaseServiceImpl<Payv2PayOrderClear, Payv2PayOrderClearMapper> implements Payv2PayOrderClearService {
	private static final Logger LOGGER = Logger.getLogger(Payv2PayOrderClearServiceImpl.class);
	// 注入当前dao对象
	@Autowired
	private Payv2PayOrderClearMapper payv2PayOrderClearMapper;

	@Autowired
	private Payv2PayOrderMapper payv2PayOrderMapper;

	@Autowired
	private Payv2BussCompanyMapper payv2BussCompanyMapper;

	@Autowired
	private Payv2BussCompanyAppMapper payv2BussCompanyAppMapper;

	@Autowired
	private Payv2BussCompanyShopMapper payv2BussCompanyShopMapper;

	@Autowired
	private Payv2PayWayMapper payv2PayWayMapper;

	@Autowired
	private Payv2BillDownMapper payv2BillDownMapper;
	@Autowired
	private Payv2PayOrderRefundMapper payv2PayOrderRefundMapper;

	@Autowired
	private Payv2PayWayRateMapper payv2PayWayRateMapper;
	@Autowired
	private Payv2DayCompanyClearMapper payv2DayCompanyClearMapper;

	@Autowired
	private Payv2BussWayDetailMapper payv2BussWayDetailMapper;
	

	public Payv2PayOrderClearServiceImpl() {
		setMapperClass(Payv2PayOrderClearMapper.class, Payv2PayOrderClear.class);
	}

	/**
	 * 调用支付宝接口,查询对账单下载地址
	 * 
	 * @param time
	 * @return
	 * @throws Exception
	 */
	private AlipayDataDataserviceBillDownloadurlQueryResponse getAlipayResponse(String time, String appId, String pkcsb_private, String aplipay_rsa_public)
			throws Exception {
		AlipayClient alipayClient = new DefaultAlipayClient(PayConfigApi.MOBILEPAY_SERVICE, appId, pkcsb_private, PayConfigApi.FORMAT, PayConfigApi.CHARSET,
				aplipay_rsa_public);
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizContent("{" + "    \"bill_type\":\"trade\"," + "    \"bill_date\":\"" + time + "\"" + "  }");
		AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
		return response;
	}

	/**
	 * 调用支付宝接口,查询对账单下载地址
	 * 
	 * @param time
	 * @return
	 * @throws Exception
	 */
	private AlipayDataDataserviceBillDownloadurlQueryResponse getAlipayResponse(String time, String appId, String pkcsb_private, String aplipay_rsa_public,
			String singType) throws Exception {
		AlipayClient alipayClient = new DefaultAlipayClient(PayConfigApi.MOBILEPAY_SERVICE, appId, pkcsb_private, PayConfigApi.FORMAT, PayConfigApi.CHARSET,
				aplipay_rsa_public, singType);
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizContent("{" + "    \"bill_type\":\"trade\"," + "    \"bill_date\":\"" + time + "\"" + "  }");
		AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
		return response;
	}

	/**
	 * 获取压缩包的 名字
	 * 
	 * @param con
	 * @return
	 * @throws Exception
	 */
	private String getFileName(HttpURLConnection con) throws Exception {
		boolean isok = false;
		String filename = "";
		con.setInstanceFollowRedirects(false);
		con.setUseCaches(false);
		con.setAllowUserInteraction(false);
		con.connect();

		Map<String, List<String>> hf = con.getHeaderFields();
		if (hf == null) {
			System.out.println("hf is null");
		}
		Set<String> key = hf.keySet();
		if (key == null) {
			System.out.println("key is null");
		}

		for (String skey : key) {
			List<String> values = hf.get(skey);
			for (String value : values) {
				String result;
				result = new String(value.getBytes("ISO-8859-1"), "GBK");
				int location = result.indexOf("filename");
				if (location >= 0) {
					result = result.substring(location + "filename".length());
					filename = result.substring(result.indexOf("=") + 1);
					isok = true;
				}
			}
			if (isok) {
				break;
			}
		}
		return filename;
	}

	/**
	 * 获取包是数据下载地址
	 * 
	 * @param response
	 * @return
	 */
	private String getDownloadurl(AlipayDataDataserviceBillDownloadurlQueryResponse response) {
		JSONObject jsonObject = JSONObject.fromObject(response.getBody());
		String urlString = jsonObject.getString("alipay_data_dataservice_bill_downloadurl_query_response");
		JSONObject jsonObject2 = JSONObject.fromObject(urlString);
		String downloadurl = jsonObject2.getString("bill_download_url");
		return downloadurl;
	}

	/**
	 * 把download下来的zip包文件保存本地
	 * 
	 * @param con
	 * @param tempZipPathFileName
	 * @throws Exception
	 */
	private void writeDataToTempZip(HttpURLConnection con, String tempZipPathFileName) throws Exception {
		// 得到输入流
		InputStream inputStream = con.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);
		File file = new File(tempZipPathFileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
	}

	/**
	 * 获取解压后要读取的csv文件名称
	 * 
	 * @param alipayFile
	 * @return
	 */
	private String getNeedReadAlipayFile(String readTempZipPathFileName) {
		File alipayFile = new File(readTempZipPathFileName);
		String alipayFileName = "";
		File[] array = alipayFile.listFiles();
		for (int i = 0; i < array.length; i++) {

			if (array[i].isFile() && array[i].getName().indexOf("(") == -1) {
				alipayFileName = array[i].getName();
			}
		}
		return alipayFileName;
	}

	/**
	 * 根据订单封装清单对象
	 * 
	 * @param pav2PayOrderObjMap
	 * @param alipayOrderNum
	 * @param alipayMoney
	 * @return
	 */
	private Payv2PayOrderClear getOrderClearByOrder(Map<String, Object> pav2PayOrderObjMap, String alipayOrderNum, String alipayMoney, String alipayOrderType,
			 String payType, Date finishedTimeDate, Double rateValue) {
		Payv2PayOrderClear clear = new Payv2PayOrderClear();
		Payv2PayOrder payv2PayOrder = (Payv2PayOrder) pav2PayOrderObjMap.get(alipayOrderNum);
		if (StringUtils.isNotEmpty(payv2PayOrder.getMerchantOrderNum())) {
			clear.setMerchantOrderNum(payv2PayOrder.getMerchantOrderNum());// 商家订单号
		}
		if (StringUtils.isNotEmpty(payv2PayOrder.getOrderNum())) {
			clear.setOrderNum(payv2PayOrder.getOrderNum());// 支付集订单号
		}
		if (payv2PayOrder.getCompanyId() != null) {
			clear.setCompanyId(payv2PayOrder.getCompanyId());// 商户id,关联payv2_buss_company表
		}

		if (payv2PayOrder.getAppId() != null) {
			clear.setAppId(payv2PayOrder.getAppId());// 应用id，关联payv2_buss_company_app或payv2_buss_company_shop表
		}

		if (payv2PayOrder.getPayWayId() != null) {
			clear.setPayWayId(payv2PayOrder.getPayWayId());// 支付渠道，关联payv2_pay_way表
		}

		if (payv2PayOrder.getOrderType() > 0) {
			clear.setMerchantType(payv2PayOrder.getOrderType());// 商户类型1.线上2.线下
		}
		clear.setType(1);// 类型1.交易2.退款

		if (StringUtils.isNotEmpty(payv2PayOrder.getOrderName())) {
			clear.setOrderName(payv2PayOrder.getOrderName());// 订单名称
		}

		clear.setCreateTime(new Date());
		double paymoney = Double.valueOf(alipayMoney);
		if (payv2PayOrder.getPayMoney() != null && payv2PayOrder.getPayDiscountMoney().doubleValue() == paymoney) {
			clear.setStatus(1);// 订单正常
			if (payv2PayOrder.getPayStatus().intValue() != 1&&payv2PayOrder.getPayStatus().intValue() != 5) {
				clear.setStatus(3);// 记录可能异常订单
				// 更新订单状态
				Payv2PayOrder der = new Payv2PayOrder();
				der.setId(payv2PayOrder.getId());
				der.setPayStatus(1);// 成功
				System.out.println(finishedTimeDate);
				der.setPayTime(finishedTimeDate);
				payv2PayOrder.setPayDiscountMoney(paymoney);
				payv2PayOrder.setPayTime(finishedTimeDate);
				payv2PayOrderMapper.updateByEntity(der);
			}
			payv2PayOrder.setAlipayOrderType(alipayOrderType);// 对账单类型
			payv2PayOrder.setPayType(payType);// 支付类型

			if (payv2PayOrder.getPayTime() == null) {
				payv2PayOrder.setPayTime(finishedTimeDate);
			}
			clear.setOrderMoney(payv2PayOrder.getPayMoney());
		} else {
			clear.setStatus(2);// 订单异常
			clear.setOrderMoney(payv2PayOrder.getPayMoney());
		}

		return clear;
	}

	/**
	 * 根据退款单和订单匹配数据 封装清单
	 * 
	 * @param payv2PayOrderRefundObjMap
	 * @param pav2PayOrderObjMap
	 * @param time
	 * @param finishedTimeDate
	 * @param alipayMoney
	 * @param alipayOrderNum
	 * @return
	 */
	private Payv2PayOrderClear getOrderClearByOrderRefund(Map<String, Object> payv2PayOrderRefundObjMap, Map<String, Object> pav2PayOrderObjMap,
			String refundNum, String alipayMoney, String alipayOrderNum, String alipayOrderType, String payType) {
		Payv2PayOrderClear clear = new Payv2PayOrderClear();
		Payv2PayOrderRefund payv2PayOrderRefund = (Payv2PayOrderRefund) payv2PayOrderRefundObjMap.get(refundNum);
		if (payv2PayOrderRefund == null) {
			return null;
		}
		Payv2PayOrder payv2PayOrder = (Payv2PayOrder) pav2PayOrderObjMap.get(payv2PayOrderRefund.getOrderNum());

		if (payv2PayOrder != null) {
			clear.setMerchantOrderNum(payv2PayOrder.getMerchantOrderNum());// 商家订单号
			clear.setOrderNum(payv2PayOrder.getOrderNum());// 支付集订单号
			clear.setRefundNum(refundNum);//退款订单号
			clear.setCompanyId(payv2PayOrder.getCompanyId());// 商户id,关联payv2_buss_company表
			clear.setAppId(payv2PayOrder.getAppId());// 应用id，关联payv2_buss_company_app或payv2_buss_company_shop表
			clear.setPayWayId(payv2PayOrder.getPayWayId());// 支付渠道，关联payv2_pay_way表
			clear.setMerchantType(payv2PayOrder.getOrderType());// 商户类型1.线上2.线下
			clear.setType(2);// 类型1.交易2.退款
			clear.setOrderName(payv2PayOrder.getOrderName());// 订单名称
			clear.setCreateTime(new Date());
			double paymoney = Double.valueOf(alipayMoney);
			clear.setOrderMoney(payv2PayOrderRefund.getRefundMoney());
			double refundMoney = payv2PayOrderRefund.getRefundMoney() == null ? 0 : payv2PayOrderRefund.getRefundMoney().doubleValue();
			// 如果金额和时间都匹配 那么就是匹配上了
			if (refundMoney == Math.abs(paymoney)) {
				clear.setStatus(1);// 订单正常
				payv2PayOrder.setAlipayOrderType(alipayOrderType);// 对账单业务类型
				payv2PayOrder.setPayType(payType);// 支付类型
				payv2PayOrder.setPayMoney(paymoney);

			} else {
				clear.setStatus(2);// 订单异常
			}
		}
		return clear;
	}

	/**
	 * 对账：每天10点执行定时器去执行
	 */
	public void job(String alipayTime, String weixinTime, Map<String, Object> pav2PayOrderObjMap,
			Map<String, Object> payv2PayOrderRefundObjMap, String payName, Payv2PayWayRate rate) throws Exception {
		if (PayRateDictValue.PAY_TYPE_ALIPAY_APP.equals(rate.getDictName()) || PayRateDictValue.PAY_TYPE_ALIPAY_WEB.equals(rate.getDictName())) {
			LOGGER.info("通道为：-----》" + rate.getDictName());
			// 1.调用支付宝接口,查询对账单下载地址
			AlipayDataDataserviceBillDownloadurlQueryResponse response = getAlipayResponse(alipayTime, rate.getRateKey1(), rate.getRateKey2(),
					rate.getRateKey3());
			if (response.isSuccess()) {
				String downloadurl = getDownloadurl(response);
				alipayHandle(downloadurl, alipayTime, pav2PayOrderObjMap, payv2PayOrderRefundObjMap, payName, rate.getPayWayRate());
			} else {
				LOGGER.error("调用失败");
			}
		}
		// 支付宝扫码支付对账接口
		else if (PayRateDictValue.PAY_TYPE_ALIPAY_SMZL.equals(rate.getDictName())) {
			LOGGER.info("通道为：-----》" + rate.getDictName());
			// 1.调用支付宝接口,查询对账单下载地址
			AlipayDataDataserviceBillDownloadurlQueryResponse response = getAlipayResponse(alipayTime, rate.getRateKey1(), rate.getRateKey2(),
					rate.getRateKey3(), "RSA2");
			if (response.isSuccess()) {
				String downloadurl = getDownloadurl(response);
				alipayHandle(downloadurl, alipayTime, pav2PayOrderObjMap, payv2PayOrderRefundObjMap, payName, rate.getPayWayRate());

			} else {
				LOGGER.error("调用失败");
			}
		}
		// 威富通统一对账接口
		else if (PayRateDictValue.PAY_TYPE_WFT_WEIXIN.equals(rate.getDictName()) || PayRateDictValue.PAY_TYPE_WFT_WEIXIN_GZH.equals(rate.getDictName())) {
			LOGGER.info("通道为：-----》" + rate.getDictName());
			String orderArrs[] = WftWeChatPay.downloadOrder(weixinTime, rate.getRateKey1(), rate.getRateKey2());
			wxHandle(orderArrs, weixinTime, pav2PayOrderObjMap, payv2PayOrderRefundObjMap, payName, rate.getPayWayRate());
		}
		//兴业深圳统一对账接口
		else if (PayRateDictValue.PAY_TYPE_XYSZ_ALI_SCAN.equals(rate.getDictName()) || PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_SCAN.equals(rate.getDictName()) 
				|| PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_WAP.equals(rate.getDictName())) {
			LOGGER.info("通道为：-----》" + rate.getDictName());
			LOGGER.info("======》兴业深圳对账开始");
			try {
				String[] xyszOderFind = XYSZBankPay.xyszOderFind(weixinTime, rate.getRateKey1(), rate.getRateKey2());
				wxHandle(xyszOderFind, weixinTime, pav2PayOrderObjMap, payv2PayOrderRefundObjMap, payName, rate.getPayWayRate());
			} catch (Exception e) {
				LOGGER.error("======》兴业深圳对账失败，失败原因：" + e.getMessage());
				e.printStackTrace();
			}
		}
		// 兴业银行：微信对账接口查询:微信公众号，微信扫码
		if (PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_GZH.equals(rate.getDictName())
				||PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_SCAN.equals(rate.getDictName())
		 ) {
			LOGGER.info("通道为：-----》" + rate.getDictName());
			LOGGER.info("======》兴业银行：微信对账开始");
			String appid = rate.getRateKey1();
			String mch_id = rate.getRateKey2();
			String key = rate.getRateKey3();
			LOGGER.info("======》对账商户号为：" + mch_id);
			if (appid != null && mch_id != null & key != null) {
				String str[] = XyBankPay.xyWechatStatement(appid, mch_id, key, weixinTime);
				if (str != null) {
					xyBankWechatHandle(str, weixinTime, pav2PayOrderObjMap, payv2PayOrderRefundObjMap, payName, rate.getPayWayRate());
				} else {
					LOGGER.error("调用失败");
				}
			} else {
				LOGGER.error("===》兴业银行微信对账参数错误：商户号,appid,key为空,请检查数据库通道参数配置");
			}
		}
		// 兴业银行：支付宝扫码对账接口查询
		else if (PayRateDictValue.PAY_TYPE_XYBANk_ALI_SCAN.equals(rate.getDictName())) {
			LOGGER.info("通道为：-----》" + rate.getDictName());
			LOGGER.info("兴业银行：支付宝扫码对账开始");
			String appid = rate.getRateKey1();
			String mch_id = rate.getRateKey2();
			String key = rate.getRateKey3();
			LOGGER.info("======》对账商户号为：" + mch_id);
			if (appid != null && mch_id != null & key != null) {
				String str[] = AliPay.xyBankAliStatement(appid, mch_id, key, weixinTime);
				if (str != null) {
					xyBankAliHandle(str, weixinTime, pav2PayOrderObjMap, payv2PayOrderRefundObjMap, payName, rate.getPayWayRate());
				} else {
					LOGGER.error("调用失败");
				}
			} else {
				LOGGER.error("===》兴业银行支付宝参数错误：商户号,appid,key为空,请检查数据库通道参数配置");
			}
		}
		// 民生银行：对账接口（支持目前通道：微信扫码；支付宝扫码对账，微信公众号,qq）
		else if (PayRateDictValue.PAY_TYPE_MSBANk_ALI_SCAN.equals(rate.getDictName())
				||PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_SCAN.equals(rate.getDictName())
				|| PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_GZH.equals(rate.getDictName())
				|| PayRateDictValue.PAY_TYPE_MSBANk_QQ_SCAN.equals(rate.getDictName())) {
			LOGGER.info("通道为：-----》" + rate.getDictName());
			LOGGER.info("民生银行对账接口对账开始");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 代理商户号:
			String expanderId = rate.getRateKey3();
			// 商户号
			String customerId = rate.getRateKey1();
			//获取加密文件名字；密码，：请注意在生成cer文件的时候，名字,密码,alias请保持一致
			String thdSysNme=rate.getRateKey4();
			LOGGER.info("======》对账商户号为：" + customerId);
			if (expanderId != null && customerId != null&thdSysNme!=null) {
				paramMap.put("expanderId", expanderId);
				paramMap.put("customerId", customerId);
				// 查询开始时间
				paramMap.put("beginDate", alipayTime);
				// 结束时间
				paramMap.put("endDate", alipayTime);
				// 服务商类型
				paramMap.put("serviceProviderId", "YF");
				// 日期类型:1-交易日期,2-清算日期
				paramMap.put("dateType", "1");
				// 交易类型：WX微信 ZFB:支付宝
				if ( PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_SCAN.equals(rate.getDictName()) ||
					 PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_GZH.equals(rate.getDictName())) {
					paramMap.put("channelId", "WX");
				}
				if (PayRateDictValue.PAY_TYPE_MSBANk_ALI_SCAN.equals(rate.getDictName())) {
					paramMap.put("channelId", "ZFB");
				}
				if (PayRateDictValue.PAY_TYPE_MSBANk_QQ_SCAN.equals(rate.getDictName())) {
					paramMap.put("channelId", "QQ");
				}
				// 获取相关配置
				paramMap = HttpMinshengBank.getParams(paramMap,thdSysNme);
				// 访问
				String responseBody = HttpMinshengBank.msBankAliStatement(paramMap);
				if (responseBody != null) {// 成功
					// 解析：生成对账文件
					MSBankHandle(responseBody, pav2PayOrderObjMap, payv2PayOrderRefundObjMap, payName, rate.getPayWayRate());
				} else {// 失败
					LOGGER.error("调用失败");
				}
			} else {
				LOGGER.error("===》民生银行对账接口参数错误：RateKey1,RateKey3,RateKey4不能为空,请检查数据库通道数据是否填写完整");
			}
		}
		// 汇付宝对账接口:汇付宝只提供成功的订单数据(支持微信与支付宝账单)
		else if (PayRateDictValue.PAY_TYPE_HFB_WEIXIN_GZH.equals(rate.getDictName()) 
				|| PayRateDictValue.PAY_TYPE_HFB_WX_WEB.equals(rate.getDictName())
				|| PayRateDictValue.PAY_TYPE_HFB_WX_SDK.equals(rate.getDictName()) 
				|| PayRateDictValue.PAY_TYPE_HFB_ALI_SDK.equals(rate.getDictName())) {
			LOGGER.info("通道为：-----》" + rate.getDictName());
			LOGGER.info("汇付宝对账开始");
			int version = 2;
			String agent_id = rate.getRateKey1();
			// RateKey4:代表：对账需要的key
			String key = rate.getRateKey4();
			// 支付类型：62=认证支付，20=网银支付，18=快捷支付，0=所有支付
			int pay_type = 0;
			// 格式为yyyyMMddHHmmss，比如 20140701110000
			String begin_time = weixinTime + "000000";
			// 格式为yyyyMMddHHmmss，比如 20140701110000
			String end_time = weixinTime + "235959";
			// 分页查询记录数量，最大500条
			int page_size = 500;
			// 分页查询页码
			int page_index = 1;
			LOGGER.info("======》对账商户号为：" + agent_id);
			if (agent_id != null && key != null) {
				// 访问
				String[] responseBody = HfbWeChatPay.hfbStatement(version, agent_id, key, pay_type, begin_time, end_time, page_size, page_index);
				if (responseBody != null) {
					// 解析：生成对账文件
					HFBHandle(responseBody, pav2PayOrderObjMap, payv2PayOrderRefundObjMap, payName, rate.getPayWayRate());
				} else {
					LOGGER.error("调用失败");
				}
			} else {
				LOGGER.error("===》汇付宝对账参数错误：商户号或者商户key为空,请检查数据库通道数据是否填写完整");
			}
		}
		//平安银行对账接口：微信，支付宝
		else if(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_SCAN.equals(rate.getDictName())
				||PayRateDictValue.PAY_TYPE_PABANk_ALI_SCAN.equals(rate.getDictName())){
			LOGGER.info("通道为：-----》" + rate.getDictName());
			LOGGER.info("平安银行对账开始");
			String OPEN_ID=rate.getRateKey1();
			String OPEN_KEY=rate.getRateKey2();
			if(OPEN_ID!=null&&OPEN_KEY!=null){
				//开发者流水号
				String outNo=null;
				//交易号
				String tradeNo=null;
				//付款订单号
				String ordNo=null;
				//交易方式
				String pmtTag=null;
				if(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_SCAN.equals(rate.getDictName())){
					pmtTag = "Weixin";
				}
				if(PayRateDictValue.PAY_TYPE_PABANk_ALI_SCAN.equals(rate.getDictName())){
					pmtTag = "AlipayPAZH";
				}
				//交易类型：1交易 2：退款：这里不限制
				String ordType=null;
				//订单状态（1交易成功，2待支付，4已取消，9等待用户输入密码确认）这
				//对账只查交易成功的状态的订单
				String status="1";
				//查询开始时间:yyy-MM-dd HH:mm:ss
				String sdate=alipayTime+" 00:00:00";
				//查询结束时间
				String edate=alipayTime+" 23:59:59";
				List<PANOrderVo>  voList=PABankPay.queryOrderList(outNo, tradeNo, ordNo, pmtTag, ordType, status, sdate, edate, OPEN_ID, OPEN_KEY);
				if(voList.size()>0){
					//解析数据
					PINGANHandle(voList, pav2PayOrderObjMap, payv2PayOrderRefundObjMap,payName, rate.getPayWayRate());
				}else{
					System.out.println("返回数据为空");
				}
			}else{
				LOGGER.error("===》平安银行对账参数错误：RateKey1,RateKey2,请检查数据库通道数据是否填写完整");
			}
			
			
		}
	}

	/**
	 * 微信对账文件处理
	 * 
	 * @param downloadurl
	 * @param time
	 * @param sdclearList
	 * @param thclearList
	 * @throws Exception
	 */
	private void wxHandle(String orderArrs[], String time, Map<String, Object> pav2PayOrderObjMap, Map<String, Object> payv2PayOrderRefundObjMap,
			 String payType, Double rateValue) throws Exception {
		/*
		 * 微信对账文件格式
		 * 交易时间,公众账号ID,第三方商户号,商户号,子商户号,设备编号,威富通订单号,第三方订单号,商户订单号,用户标识,交易类型
		 * ,交易状态,付款银行
		 * ,货币种类,总金额,企业红包金额,威富通退款单号,商户退款单号,退款金额,企业红包退款金额,退款类型,退款状态,商品名称
		 * ,商户数据包,手续费
		 * ,费率,终端类型,对账标识,门店编号,商户名称,收银员,子商户ID,免充值券金额,实收金额,扩展字段1,扩展字段2,扩展字段3,扩展字段4
		 * `2017-04-27 10:54:27,`appid,`,`,`103590007749,`IOS_SDK,`
		 * 103590007749201704271123822958
		 * ,`H17042750114951E,`DD20170427105227878240
		 * ,`openid,`pay.weixin.wappayv3
		 * ,`支付成功,`CFT,`CNY,`10.00,`0.00,`,`,`,`,`,`,`全国 10元话费
		 * 15220183163,`,`0.10
		 * ,`1.00%,`iOS_SDK,`0,`,`北京全民金服科技有限公司,`,`,`0.00,`10.00,`,`,`,`
		 * `2017-04-27 14:20:09,`appid,`,`,`103590007749,`IOS_SDK,`
		 * 103590007749201704276103512127
		 * ,`H17042750760091O,`DD2017042714184297028
		 * ,`openid,`pay.weixin.wappayv3
		 * ,`支付成功,`CFT,`CNY,`10.00,`0.00,`,`,`,`,`,`,`全国 10元话费
		 * 15220183163,`,`0.10
		 * ,`1.00%,`iOS_SDK,`0,`,`北京全民金服科技有限公司,`,`,`0.00,`10.00,`,`,`,`
		 * `2017-04-27 15:24:17,`appid,`,`,`103590007749,`AND_SDK,`
		 * 103590007749201704276103668234
		 * ,`H17042750920861U,`DD20170427152242223461
		 * ,`openid,`pay.weixin.wappayv3
		 * ,`支付成功,`CFT,`CNY,`10.00,`0.00,`,`,`,`,`,`,`全国 10元话费
		 * 18682475275,`,`0.10
		 * ,`1.00%,`AND_SDK,`0,`,`北京全民金服科技有限公司,`,`,`0.00,`10.00,`,`,`,`
		 * `2017-04-27 16:16:46,`appid,`,`,`103590007749,`AND_SDK,`
		 * 103590007749201704274132650712
		 * ,`H17042751118051R,`DD20170427161236184642
		 * ,`openid,`pay.weixin.wappayv3
		 * ,`支付成功,`CFT,`CNY,`10.00,`0.00,`,`,`,`,`,`,`全国 10元话费
		 * 13922810506,`,`0.10
		 * ,`1.00%,`AND_SDK,`0,`,`北京全民金服科技有限公司,`,`,`0.00,`10.00,`,`,`,`
		 * `2017-04-27 17:03:59,`appid,`,`,`103590007749,`IOS_SDK,`
		 * 103590007749201704277105301982
		 * ,`H17042751215811J,`DD20170427170138471692
		 * ,`openid,`pay.weixin.wappayv3
		 * ,`支付成功,`CFT,`CNY,`4.50,`0.00,`,`,`,`,`,`,`全国通用 30M流量
		 * 15220183163,`,`0.05
		 * ,`1.00%,`iOS_SDK,`0,`,`北京全民金服科技有限公司,`,`,`0.00,`4.50,`,`,`,`
		 * `2017-04-27 11:14:01,`appid,`,`,`103590007749,`IOS_SDK,`
		 * 103590007749201704271123822958
		 * ,`H17042750114951E,`DD20170427105227878240
		 * ,`openid,`pay.weixin.wappayv3
		 * ,`转入退款,`CFT,`CNY,`10.00,`,`103590007749201704276102916173
		 * ,`5647895456,`10.00,`,`ORIGINAL,`退款成功,`全国 10元话费
		 * 15220183163,`,`-0.10,`
		 * 1.00%,`iOS_SDK,`0,`,`北京全民金服科技有限公司,`,`,`0.00,`-10.00,`,`,`,`
		 * 总交易单数,总交易额,总退款金额,总企业红包退款金额,总手续费金额,总实收金额
		 * `5,`44.50,`10.00,`0,`0.35,`34.50
		 */
		if (orderArrs != null) {
			for (int i = 1; i < orderArrs.length - 2; i++) {
				String orderStrs = orderArrs[i];
				String[] orderArr = orderStrs.split(",");
				// 微信对账文件38个字段
				if (orderArr.length >= 38) {
					Payv2PayOrderClear clear = null;

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date finishedTimeDate = df.parse(getOrderStr(orderArr[0]));
					if (orderArr[11].equals("`支付成功")) {
						if (pav2PayOrderObjMap.containsKey(getOrderStr(orderArr[8]))) {

							clear = getOrderClearByOrder(pav2PayOrderObjMap, getOrderStr(orderArr[8]), getOrderStr(orderArr[14]), "交易",  payType,
									finishedTimeDate, rateValue);
							if (clear != null) {
								clear.setUpstreamTime(finishedTimeDate);
								clear.setUpstreamAmount(Double.valueOf(getOrderStr(orderArr[14])));
								clear.setClearTime(finishedTimeDate);
								clear.setTransactionId(getOrderStr(orderArr[6]));
							}

						}
					} else if (orderArr[11].equals("`转入退款")) {
						/*
						 * if(payv2PayOrderRefundObjMap.containsKey(getOrderStr(
						 * orderArr[0]))){
						 */
						clear = getOrderClearByOrderRefund(payv2PayOrderRefundObjMap, pav2PayOrderObjMap, getOrderStr(orderArr[17]), getOrderStr(orderArr[14]),
								getOrderStr(orderArr[8]), "退款",  payType);
						if (clear != null) {
							clear.setUpstreamTime(finishedTimeDate);
							clear.setUpstreamAmount(Double.valueOf(getOrderStr(orderArr[14])));
							clear.setClearTime(finishedTimeDate);
							clear.setTransactionId(getOrderStr(orderArr[16]));
						}
						// }
					}
					if (clear != null) {
						//修改对账单状态
						updateOrderClear(clear);
//						payv2PayOrderClearMapper.insertByEntity(clear);
					} else {
						// 默认成功状态
						int type = 1;
						String orderNum = "";
						String transactionId = "";
						String alipayMoney = "";
						if (orderArr[11].equals("`转入退款")) {
							// 退款状态
							type = 2;
							orderNum = getOrderStr(orderArr[17]);
							transactionId = getOrderStr(orderArr[16]);
							alipayMoney = getOrderStr(orderArr[14]);
						}
						if (orderArr[11].equals("`支付成功")) {
							//交易成功
							// 订单号
							orderNum = getOrderStr(orderArr[8]);
							transactionId = getOrderStr(orderArr[6]);
							alipayMoney = getOrderStr(orderArr[14]);
						}
						if (orderNum != null) {
							updateOrderTime(orderNum, finishedTimeDate, Double.valueOf(alipayMoney), type, transactionId);
						}
					}
				}
			}
			System.out.println("微信对账完毕");
		}
	}

	/**
	 * @Title: xyBankWechatHandle
	 * @Description: 兴业银行微信对账单
	 * @param @param orderArrs
	 * @param @param time
	 * @param @param pav2PayOrderObjMap
	 * @param @param payv2PayOrderRefundObjMap
	 * @param @param orderList
	 * @param @param payType 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void xyBankWechatHandle(String str[], String time, Map<String, Object> pav2PayOrderObjMap, Map<String, Object> payv2PayOrderRefundObjMap,
			 String payType, Double rateValue) {
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> list1 = new ArrayList<String>();

		int num = str.length / 24;

		for (int i = 1; i < str.length; i++) {
			if (i % 24 == 0) {
				list1.add(str[i]);
				list.add(list1);
				if (i == num * 24) {
					int n = list1.get(list1.size() - 1).indexOf("总交易单数");
					list1.set(list1.size() - 1, list1.get(list1.size() - 1).substring(0, n));
				}
				list1 = new ArrayList<String>();
			} else {
				list1.add(str[i]);
			}
			if (i == str.length - 1) {
				list.add(list1);
			}
		}

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (List<String> list2 : list) {
				Payv2PayOrderClear clear = null;
				for (int i = 0; i < list2.size(); i++) {
					String yes = xygetOrderStr(list2.get(i));
					// 成功
					if (yes.equals("SUCCESS")) {
						// 获取订单号
						System.out.println("交易状态：" + yes);
						System.out.println("支付集订单号：" + xygetOrderStr(list2.get(5)));
						System.out.println("总金额：" + xygetOrderStr(list2.get(11)));
						Date finishedTimeDate = df.parse(xygetOrderStr(list2.get(0)));
						System.out.println("交易时间：" + finishedTimeDate);
						System.out.println("上游订单号："+xygetOrderStr(list2.get(4)));
						int money = Integer.valueOf(xygetOrderStr(list2.get(11)));
						double money1 = money / 100.00;
						String wxMoney = String.valueOf(money1);
						if (pav2PayOrderObjMap.containsKey(xygetOrderStr(list2.get(5)))) {
							clear = getOrderClearByOrder(pav2PayOrderObjMap, xygetOrderStr(list2.get(5)), wxMoney, "交易", payType, finishedTimeDate,
									rateValue);
						}
						if (clear != null) {
							clear.setUpstreamTime(finishedTimeDate);
							clear.setUpstreamAmount(Double.valueOf(wxMoney));
							//上游订单号：
							clear.setTransactionId(xygetOrderStr(list2.get(4)));
							clear.setClearTime(finishedTimeDate);
						}
					}
					// 退款
					String refundNum="";
					if (yes.equals("REFUND")) {
						System.out.println("交易状态：" + yes);
						System.out.println("退款订单号：" + xygetOrderStr(list2.get(14)));
						System.out.println("支付集订单号：" + xygetOrderStr(list2.get(5)));
						System.out.println("退款金额：" + xygetOrderStr(list2.get(15)));
						System.out.println("上游退款订单号:"+xygetOrderStr(list2.get(4)));
						refundNum = xygetOrderStr(list2.get(14));
						int money = Integer.valueOf(xygetOrderStr(list2.get(15)));
						double money1 = money / 100.00;
						String alipayMoney = String.valueOf(money1);
						String alipayOrderNum = xygetOrderStr(list2.get(5));
						Date finishedTimeDate = df.parse(xygetOrderStr(list2.get(0)));
						System.out.println("交易时间：" + finishedTimeDate);
						clear = getOrderClearByOrderRefund(payv2PayOrderRefundObjMap, pav2PayOrderObjMap, refundNum, alipayMoney, alipayOrderNum, "退款",
								 payType);
						if (clear != null) {
							clear.setUpstreamTime(finishedTimeDate);
							clear.setUpstreamAmount(Double.valueOf(alipayMoney));
							//上游退款订单号：
							clear.setTransactionId(xygetOrderStr(list2.get(4)));
							clear.setClearTime(finishedTimeDate);
						}
					}
					
					if(clear==null){
						//交易
						int type=1;
						String orderNum=null;
						Date finishedTimeDate=null;
						double money=0.00;
						String transactionId=null;
						if (yes.equals("SUCCESS")) {
							 type=1;
							 orderNum=xygetOrderStr(list2.get(5));
							 finishedTimeDate = df.parse(xygetOrderStr(list2.get(0)));
							 int money2 = Integer.valueOf(xygetOrderStr(list2.get(11)));
							 money = money2 / 100.00;
							 transactionId=xygetOrderStr(list2.get(4));
						}
						//退款
						if (yes.equals("REFUND")) {
							 type=2;
							 orderNum=xygetOrderStr(list2.get(14));
							 finishedTimeDate = df.parse(xygetOrderStr(list2.get(0)));
							 int money2 = Integer.valueOf(xygetOrderStr(list2.get(15)));
							 money = money2 / 100.00;
							 transactionId=xygetOrderStr(list2.get(4));
						}
						if(orderNum!=null){
							//修改隔夜漏单情况
							updateOrderTime(orderNum, finishedTimeDate, money, type, transactionId);
						}
					}
				}
				if (clear != null) {
					//修改对账单状态
					updateOrderClear(clear);
//					payv2PayOrderClearMapper.insertByEntity(clear);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.error("解析失败" + e);
		}
		System.out.println("兴业银行微信对账单完毕");
	}

	/**
	 * @Title: xyBankAliHandle
	 * @Description: 兴业银行支付宝扫码对账单
	 * @param @param str
	 * @param @param time
	 * @param @param pav2PayOrderObjMap
	 * @param @param payv2PayOrderRefundObjMap
	 * @param @param orderList
	 * @param @param payType 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void xyBankAliHandle(String str[], String time, Map<String, Object> pav2PayOrderObjMap, Map<String, Object> payv2PayOrderRefundObjMap,
			 String payType, Double rateValue) {
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> list1 = new ArrayList<String>();
		int num = str.length / 28;
		for (int i = 1; i < str.length; i++) {
			if (i % 28 == 0) {
				list1.add(str[i]);
				list.add(list1);
				if (i == num * 28) {
					int n = list1.get(list1.size() - 1).indexOf("总交易单数");
					list1.set(list1.size() - 1, list1.get(list1.size() - 1).substring(0, n));
				}
				list1 = new ArrayList<String>();
			} else {
				list1.add(str[i]);
			}
			if (i == str.length - 1) {
				list.add(list1);
			}
		}
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (List<String> list2 : list) {
				Payv2PayOrderClear clear = null;
				for (int i = 0; i < list2.size(); i++) {
					String yes = xygetOrderStr(list2.get(i));
					// 成功
					if (yes.equals("交易")) {
						// 获取订单号
						System.out.println("交易状态：" + yes);
						System.out.println("支付集订单号：" + xygetOrderStr(list2.get(2)));
						System.out.println("总金额：" + xygetOrderStr(list2.get(25)));
						Date finishedTimeDate = df.parse(xygetOrderStr(list2.get(6)));
						System.out.println("交易时间：" + finishedTimeDate);
						System.out.println("上游订单号：" + xygetOrderStr(list2.get(1)));
						String alimoney =xygetOrderStr(list2.get(25));
						if (pav2PayOrderObjMap.containsKey(xygetOrderStr(list2.get(2)))) {
							clear = getOrderClearByOrder(pav2PayOrderObjMap, xygetOrderStr(list2.get(2)),alimoney, "交易", 
									payType, finishedTimeDate, rateValue);
						}
						if (clear != null) {
							clear.setUpstreamTime(finishedTimeDate);
							clear.setUpstreamAmount(Double.valueOf(alimoney));
							clear.setTransactionId(xygetOrderStr(list2.get(1)));
							clear.setClearTime(finishedTimeDate);
						}
					}
					if (yes.equals("退款")) {
						System.out.println("交易状态：" + yes);
						System.out.println("退款订单号：" + xygetOrderStr(list2.get(22)));
						System.out.println("支付集订单号：" + xygetOrderStr(list2.get(2)));
						System.out.println("退款金额：" + xygetOrderStr(list2.get(25)));
						Date finishedTimeDate = df.parse(xygetOrderStr(list2.get(6)));
						System.out.println("交易时间：" + finishedTimeDate);
						System.out.println("上游退款订单号：" + xygetOrderStr(list2.get(1)));
						String alimoney = xygetOrderStr(list2.get(25));
						clear = getOrderClearByOrderRefund(payv2PayOrderRefundObjMap, pav2PayOrderObjMap, xygetOrderStr(list2.get(22)),
								alimoney, xygetOrderStr(list2.get(2)), "退款", payType);
						if (clear != null) {
							clear.setUpstreamTime(finishedTimeDate);
							clear.setUpstreamAmount(Double.valueOf(alimoney));
							clear.setTransactionId(xygetOrderStr(list2.get(1)));
							clear.setClearTime(finishedTimeDate);
						}
					}
					
					if(clear==null){
						int type=1;
						String orderNum=null;
						Date finishedTimeDate=null;
						double money=0.00;
						String transactionId=null;
						//交易
						if (yes.equals("SUCCESS")) {
							 type=1;
							 orderNum=xygetOrderStr(list2.get(2));
							 finishedTimeDate = df.parse(xygetOrderStr(list2.get(6)));
							 String alimoney = xygetOrderStr(list2.get(25));
							 money=Double.valueOf(alimoney);
							 transactionId=xygetOrderStr(list2.get(1));
						}
						//退款
						if (yes.equals("REFUND")) {
							 type=2;
							 orderNum=xygetOrderStr(list2.get(22));
							 finishedTimeDate =df.parse(xygetOrderStr(list2.get(6)));
							 String alimoney = xygetOrderStr(list2.get(25));
							 money=Double.valueOf(alimoney);
							 transactionId=xygetOrderStr(list2.get(1));
						}
						if(orderNum!=null){
							//修改隔夜漏单情况
							updateOrderTime(orderNum, finishedTimeDate, money, type, transactionId);
						}
					}
				}
				if (clear != null) {
					//修改对账单状态
					updateOrderClear(clear);
//					payv2PayOrderClearMapper.insertByEntity(clear);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("兴业银行支付宝对账单完毕");
	}

	/**
	 * 支付宝对账文件处理
	 * 
	 * @param downloadurl
	 * @param time
	 * @param sdclearList
	 * @param thclearList
	 * @throws Exception
	 */
	private void alipayHandle(String downloadurl, String time, Map<String, Object> pav2PayOrderObjMap, Map<String, Object> payv2PayOrderRefundObjMap,
			 String payType, Double rateValue) throws Exception {
		// 2.获取支付宝返回的账单下载地址，获取数据包
		URL url = new URL(downloadurl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		String filename = getFileName(con);
		// 文件保存位置
		String userDirPath = System.getProperty("user.dir");
		String tempZipPath = userDirPath + "/alipay";
		File saveDir = new File(tempZipPath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		String tempZipPathFileName = tempZipPath + File.separator + filename;

		// 写数据到临时文件，把download下来的zip包文件保存本地
		writeDataToTempZip(con, tempZipPathFileName);
		// 关闭连接
		con.disconnect();

		// zip包文件解压的文件夹名称
		String outputPathFileName = tempZipPathFileName.substring(0, tempZipPathFileName.lastIndexOf("."));
		// 3.解压
		ZipUtil.unzip(tempZipPathFileName, outputPathFileName, true);

		// 拼接解压后的文件路径
		String readTempZipPathFileName = tempZipPath + File.separator + filename.substring(0, filename.lastIndexOf(".")) + File.separator
				+ filename.substring(0, filename.lastIndexOf("."));

		// 获取解压后需要读取数据的文件名称
		String alipayFileName = getNeedReadAlipayFile(readTempZipPathFileName);
		// 生成CsvReader对象，以，为分隔符，GBK编码方式
		CsvReader r = new CsvReader(readTempZipPathFileName + File.separator + alipayFileName, ',', Charset.forName("GBK"));
		// 读取表头
		r.readHeaders();
		// 逐条读取记录，直至读完
		int canRead = 0;

		while (r.readRecord()) {
			// 读取一条记录
			String nameaaaString = r.getRawRecord();
			if (canRead == 1) {
				// 按列名读取这条记录的值
				String transactionId = r.get(0).trim().replace("\t", "");
				String alipayOrderNum = r.get(1).trim().replace("\t", "");
				String alipayOrderType = r.get(2).trim().replace("\t", "");
				String finishedTime = r.get(5).trim().replace("\t", "");
				String refundNum = r.get(21).trim().replace("\t", "");
				Date finishedTimeDate = null;
				if (StringUtils.isNoneEmpty(finishedTime)) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					finishedTimeDate = df.parse(finishedTime);
				}

				Payv2PayOrderClear clear = null;
				String alipayMoney = r.get(11).trim().replace("\t", "");
				//打印
				System.out.println(transactionId+"\t"+alipayOrderNum+"\t"+alipayOrderType+"\t"+finishedTime+"\t"+refundNum+"\t"+alipayMoney+"\n");
				// 交易订单处理
				if (alipayOrderType.equals("交易")) {

					if (pav2PayOrderObjMap.containsKey(alipayOrderNum)) {

						clear = getOrderClearByOrder(pav2PayOrderObjMap, alipayOrderNum, alipayMoney, alipayOrderType, payType, finishedTimeDate,
								rateValue);
						if (clear != null) {
							clear.setUpstreamTime(finishedTimeDate);
							clear.setUpstreamAmount(Double.valueOf(alipayMoney));
							clear.setTransactionId(transactionId);
							clear.setClearTime(finishedTimeDate);
						}

					}

				} else if (alipayOrderType.equals("退款")) {// 退款订单处理

					clear = getOrderClearByOrderRefund(payv2PayOrderRefundObjMap, pav2PayOrderObjMap, refundNum, alipayMoney, alipayOrderNum, alipayOrderType,
							 payType);
					if (clear != null) {
						clear.setUpstreamTime(finishedTimeDate);
						clear.setUpstreamAmount(Double.valueOf(alipayMoney));
						clear.setTransactionId(transactionId);
						clear.setClearTime(finishedTimeDate);
					}
					// }
				}

				if (clear != null) {
					//修改对账单状态
					updateOrderClear(clear);
//					payv2PayOrderClearMapper.insertByEntity(clear);
				}else{
					//修改隔夜漏单情况：根据上游传回来的支付集订单号
					int type=1;
					String orderNum=null;
					if(alipayOrderType.equals("交易")){
						type=1;
						orderNum=alipayOrderNum;
					}
					if(alipayOrderType.equals("退款")){
						type=2;
						orderNum=refundNum;
					}
					if(orderNum!=null){
						updateOrderTime(orderNum, finishedTimeDate, Double.valueOf(alipayMoney), type,transactionId);
					}
				}

			}
			if (nameaaaString.indexOf("支付宝交易号") > -1) {
				canRead = 1;
			}
			if (nameaaaString.indexOf("业务明细列表结束") > -1) {
				break;
			}
		}
		r.close();
		// 删除下载的临时文件
		deleteFile(new File(tempZipPath));
	}

	/**
	 * 根据appId分组生成对账文件
	 * 
	 * @param orderList
	 * @param alipayTime	String alipayTime = DateUtil.DateToString(as,"yyyy-MM-dd");
	 * @param as	Date as = new Date(new Date().getTime()-24*60*60*1000);
	 */
	public void appIdDownBill(List<Payv2PayOrderClear> orderList, String alipayTime, Date as
			,Payv2DayCompanyClear payv2DayCompanyClear,Map<String, Object> map) {
		// 根据appId分组
		Map<Long, List<Payv2PayOrderClear>> groupOrderMap = new HashMap<Long, List<Payv2PayOrderClear>>();
		for (Payv2PayOrderClear order : orderList) {
			Long appId = order.getAppId();
			if (appId != null) {
				if (!groupOrderMap.containsKey(appId)) {
					List<Payv2PayOrderClear> orderTeamList = new ArrayList<Payv2PayOrderClear>();
					orderTeamList.add(order);
					groupOrderMap.put(appId, orderTeamList);
				} else {
					groupOrderMap.get(appId).add(order);
				}
			}
		}
		String path = System.getProperty("user.dir");
		String tempZipPath = path + "/accountstatement";
		File saveDir = new File(tempZipPath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}

		// 根据分组生成文件 ，上传
		Set<Long> keySets = groupOrderMap.keySet();
		for (Long appId : keySets) {
			List<Payv2PayOrderClear> oList = groupOrderMap.get(appId);
			List exportData = new ArrayList<Map>();
			Map row = null;
			// 文件头
			LinkedHashMap head = new LinkedHashMap();
			head.put("1", "支付集订单号");
			head.put("2", "商户订单号");
			head.put("3", "支付集退款订单号");
			head.put("4", "订单名称");
			head.put("5", "业务类型");
			head.put("6", "支付方式");
			head.put("7", "订单金额");
			head.put("8", "手续费");
			head.put("9", "交易时间");
			// 交易笔数
			int tradeTotol = 0;
			// 总金额
			double tradePayMoney = 0.0;
			// 退款总数
			int refundTotol = 0;
			// 退款金额
			double refundPayMoney = 0.0;
			// 文件内容

			// 下单时间排序
			ListSort(oList);

			for (Payv2PayOrderClear order : oList) {
				row = new LinkedHashMap<String, String>();
				double payMoney = order.getOrderMoney() == null ? 0 : order.getOrderMoney();
				int type = order.getType();
				String alipayOrderType = "交易";
				if (type==1) {
					tradeTotol++;
					tradePayMoney += payMoney;
				} else if (type==2) {
					alipayOrderType = "退款";
					refundTotol++;
					refundPayMoney += payMoney;
				}
				row.put("1", order.getOrderNum());
				row.put("2", order.getMerchantOrderNum() + "\t");
				row.put("3", order.getRefundNum() == null ? "" : order.getRefundNum() + "\t");
				row.put("4", order.getOrderName() == null ? "" : order.getOrderName());
				row.put("5", alipayOrderType);
				row.put("6", order.getPayWayId()==1?"支付宝":"微信");
				row.put("7", payMoney);
				row.put("8", order.getBussWayRateMoney() == null ? 0 : order.getBussWayRateMoney());
				row.put("9", DateUtil.DateToString(order.getPayTime(), "yyyy-MM-dd HH:mm:ss") + "\t");
				exportData.add(row);
			}
			String fileName = "account_statement";
			File targetFile = CSVUtils.createCSVFile(exportData, head, tempZipPath, fileName, alipayTime, tradeTotol, tradePayMoney, refundTotol,
					refundPayMoney);
			String fileUploadName = fileName + DateUtil.DateToString(new Date(), "yyyyMMddHHmmss")+appId;
			String url = FtpUploadClient.getInstance().uploadFile(fileUploadName + ".csv", targetFile);
			// 得到的文件地址保存起来
			Payv2BillDown down = new Payv2BillDown();
			down.setUrl(url);
			down.setAppId(appId);
			down.setCreateTime(new Date());
			down.setClearTime(as);
			payv2BillDownMapper.insertByEntity(down);
			deleteFile(targetFile);
			System.out.println(url);
		}
		
		//将帐单号更新到详情记录中
		payv2BussWayDetailMapper.updateCompanyCheckId(map);
		
		payv2DayCompanyClearMapper.updateByEntity(payv2DayCompanyClear);
	}

	/**
	 * 根据companyId分组生成对账文件
	 * 
	 * @param orderList
	 * @param alipayTime
	 * @param as
	 * @throws Exception
	 */
	public void companyIdDownBill(List<Payv2PayOrderVO> orderList, String alipayTime, Date as, Map<Long, String> map) throws Exception {
		Map<Long, List<Payv2PayOrderVO>> groupCompanyMap = new HashMap<Long, List<Payv2PayOrderVO>>();
		for (Payv2PayOrderVO order : orderList) {
			Long companyId = order.getCompanyId();
			if (companyId != null) {
				if (!groupCompanyMap.containsKey(companyId)) {
					List<Payv2PayOrderVO> orderTeamList = new ArrayList<Payv2PayOrderVO>();
					orderTeamList.add(order);
					groupCompanyMap.put(companyId, orderTeamList);
				} else {
					groupCompanyMap.get(companyId).add(order);
				}
			}
		}

		// 根据分组生成文件 ，上传
		Set<Long> keySets = groupCompanyMap.keySet();
		for (Long companyId : keySets) {
			List<Payv2PayOrderVO> oList = groupCompanyMap.get(companyId);
			// 已支付订单笔数
			int tradeTotol = 0;
			// 已支付订单金额
			double tradePayMoney = 0.0;
			// 已退款订单笔数
			int refundTotol = 0;
			// 已退款订单金额
			double refundPayMoney = 0.0;
			// 手续费
			double rateMoney = 0.0;
			// 手续费利润
			double rateProfit = 0.0;

			Long channelId = null;

			for (Payv2PayOrderVO order : oList) {
				channelId = order.getChannelId();
				double payMoney = order.getPayMoney() == null ? 0 : order.getPayMoney();
				String alipayOrderType = order.getAlipayOrderType() == null ? "" : order.getAlipayOrderType();
				if ("交易".equals(alipayOrderType)) {
					tradeTotol++;
					tradePayMoney += payMoney;
					rateMoney += order.getPayWayMoney();

					if (order.getPayWayMoney().doubleValue() >= getCeil(order.getRateValue() * payMoney / 1000, 2)) {
						rateProfit = rateProfit + order.getPayWayMoney() - getCeil(order.getRateValue() * payMoney / 1000, 2);
					} else {
						rateProfit = rateProfit + 0;
					}
				} else if ("退款".equals(alipayOrderType)) {
					refundTotol++;
					refundPayMoney += payMoney;
				}
			}

			// 新增商户每天账单结算
			Payv2DayCompanyClear pdcc = new Payv2DayCompanyClear();
			pdcc.setChannelId(channelId);
			pdcc.setClearTime(getDate(alipayTime));
			String companyCheckId = System.currentTimeMillis() + "" + companyId;
			pdcc.setCompanyCheckId(companyCheckId);
			pdcc.setCompanyId(companyId);
			pdcc.setCreateTime(new Date());
			pdcc.setRateMoney(rateMoney); // 手续费
			pdcc.setRateProfit(rateProfit);// 手续费利润
			pdcc.setRefundMoney(refundPayMoney);// 已退款订单金额
			pdcc.setSuccessMoney(tradePayMoney);// 已支付订单金额
			pdcc.setSuccessCount(tradeTotol);// 交易笔数
			pdcc.setRefundCount(refundTotol);// 退款笔数
			payv2DayCompanyClearMapper.insertByEntity(pdcc);

			map.put(companyId, companyCheckId);

		}
	}

	/**
	 * 根据appId和rateId分组生成对账文件
	 * 
	 * @param orderList
	 * @param alipayTime
	 * @param as
	 * @throws Exception
	 */
	public void comRateIdDownBill(List<Payv2PayOrderVO> orderList, String alipayTime, Date as, Map<Long, String> comMap) throws Exception {
		// 根据companyId和rateId分组
		Map<String, List<Payv2PayOrderVO>> groupComRateMap = new HashMap<String, List<Payv2PayOrderVO>>();
		for (Payv2PayOrderVO order : orderList) {
			Long appId = order.getAppId();
			Long rateId = order.getRateId();
			if (appId != null && rateId != null) {
				String comRate = appId + "-" + rateId;
				if (!groupComRateMap.containsKey(comRate)) {
					List<Payv2PayOrderVO> orderTeamList = new ArrayList<Payv2PayOrderVO>();
					orderTeamList.add(order);
					groupComRateMap.put(comRate, orderTeamList);
				} else {
					groupComRateMap.get(comRate).add(order);
				}
			}
		}
		// 根据分组生成文件 ，上传
		Set<String> keySets = groupComRateMap.keySet();
		for (String comRate : keySets) {
			List<Payv2PayOrderVO> oList = groupComRateMap.get(comRate);
			// 已支付订单笔数
			int tradeTotol = 0;
			// 已支付订单金额
			double tradePayMoney = 0.0;
			// 已退款订单笔数
			int refundTotol = 0;
			// 已退款订单金额
			double refundPayMoney = 0.0;
			// 手续费
			double rateMoney = 0.0;
			// 商户签约费率
			double bussWayRate = 0.0;
			// 成本费率
			double costRate = 0.0;
			// 成本手续费
			double costRateMoney = 0.0;

			String[] str = comRate.split("-");
			Long appId = Long.valueOf(str[0]);

			Long companyId = null;
			Long channelId = null;

			for (Payv2PayOrderVO order : oList) {
				companyId = order.getCompanyId();
				channelId = order.getChannelId();
				double payMoney = order.getPayMoney() == null ? 0 : order.getPayMoney();
				String alipayOrderType = order.getAlipayOrderType() == null ? "" : order.getAlipayOrderType();
				if ("交易".equals(alipayOrderType)) {
					tradeTotol++;
					tradePayMoney += payMoney;
					rateMoney += order.getPayWayMoney();
					costRateMoney += getCeil(order.getRateValue() * payMoney / 1000, 2);
					costRate = order.getRateValue();
					if (null != order.getComRateValue()) {
						bussWayRate = order.getComRateValue();
					}
				} else if ("退款".equals(alipayOrderType)) {
					refundTotol++;
					refundPayMoney += payMoney;
				}
			}

			// 新增商户每天账单结算
			Payv2BussWayDetail pbwd = new Payv2BussWayDetail();
			pbwd.setChannelId(channelId);
			pbwd.setCompanyId(companyId);
			pbwd.setAppId(appId);
			pbwd.setPayWayId(Long.valueOf(str[1]));
			if (companyId != null && comMap.containsKey(companyId)) {
				pbwd.setCompanyCheckId(comMap.get(companyId));
			}
			pbwd.setSuccessCount(tradeTotol); // 交易笔数
			pbwd.setSuccessMoney(tradePayMoney);// 交易金额
			pbwd.setRefundCount(refundTotol); // 退款笔数
			pbwd.setRefundMoney(refundPayMoney);// 退款金额
			pbwd.setBussMoney(tradePayMoney - refundPayMoney);// 交易净额
			pbwd.setCostRate(costRate); // 成本费率
			pbwd.setCostRateMoney(costRateMoney);// 成本手续费
			pbwd.setBussWayRate(bussWayRate);// 商户签约费率
			pbwd.setBussWayRateMoney(rateMoney);// 商户手续费
			pbwd.setClearTime(getDate(alipayTime));
			pbwd.setCreateTime(new Date());

			payv2BussWayDetailMapper.insertByEntity(pbwd);
		}
	}

	/**
	 * 向上取整
	 * 
	 * @param d
	 * @param n
	 *            保留位数
	 * @return
	 */
	public double getCeil(double d, int n) {
		BigDecimal b = new BigDecimal(String.valueOf(d));
		b = b.divide(BigDecimal.ONE, n, BigDecimal.ROUND_CEILING);
		return b.doubleValue();
	}

	// yyyyMMdd
	private Date getDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(date);
	}

	private String getOrderStr(String str) {
		return str.replace("`", "");
	}

	private String xygetOrderStr(String str) {
		return str.replace(",", "");
	}

	/**
	 * 通过递归调用删除一个文件夹及下面的所有文件
	 * 
	 * @param file
	 */
	public void deleteFile(File file) {
		if (file.isFile()) {// 表示该文件不是文件夹
			file.delete();
		} else {
			// 首先得到当前的路径
			String[] childFilePaths = file.list();
			for (String childFilePath : childFilePaths) {
				File childFile = new File(file.getAbsolutePath() + File.separator + childFilePath);
				deleteFile(childFile);
			}
			file.delete();
		}
	}

	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	public PageObject<Payv2PayOrderClear> payv2PayOrderClearList(Map<String, Object> map) {

		int totalData = payv2PayOrderClearMapper.getCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2PayOrderClear> list = payv2PayOrderClearMapper.pageQueryByObject(pageHelper.getMap());
		for (Payv2PayOrderClear clear : list) {
			clear.setIncomeMoney(0.00);// 收入金额
			clear.setReturnMoney(0.00);// 退款金额
			clear.setActuallyPayMoney(0.00);// 实收金额
			clear.setArrivalMoney(0.00);// 到账金额
			clear.setRate(0.00);// 手续费
			// 获取公司名称
			if (clear.getCompanyId() != null) {
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setId(clear.getCompanyId());
				payv2BussCompany = payv2BussCompanyMapper.selectSingle(payv2BussCompany);
				if (payv2BussCompany != null) {
					clear.setCompanyName(payv2BussCompany.getCompanyName());
				}
			}
			// 获取应用名称
			if (StringUtils.isNotEmpty(clear.getOrderNum())) {
				Payv2PayOrder payv2PayOrder = new Payv2PayOrder();
				payv2PayOrder.setOrderNum(clear.getOrderNum());
				payv2PayOrder = payv2PayOrderMapper.selectSingle(payv2PayOrder);
				if (payv2PayOrder != null) {
					clear.setOrderName(payv2PayOrder.getOrderName());
				}
			}
			if (clear.getAppId() != null && clear.getMerchantType() != null) {
				if (clear.getMerchantType().intValue() == 1) {
					Payv2BussCompanyApp payv2BussCompanyApp = new Payv2BussCompanyApp();
					payv2BussCompanyApp.setId(clear.getAppId());
					payv2BussCompanyApp = payv2BussCompanyAppMapper.selectSingle(payv2BussCompanyApp);
					if (payv2BussCompanyApp != null) {
						clear.setAppName(payv2BussCompanyApp.getAppName());
					}
				} else if (clear.getMerchantType().intValue() == 2) {
					Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
					payv2BussCompanyShop.setId(clear.getAppId());
					payv2BussCompanyShop = payv2BussCompanyShopMapper.selectSingle(payv2BussCompanyShop);
					if (payv2BussCompanyShop != null) {
						clear.setAppName(payv2BussCompanyShop.getShopName());
					}
				}
			}
			// 获取支付通道名称
			if (clear.getPayWayId() != null) {
				Payv2PayWay payv2PayWay = new Payv2PayWay();
				payv2PayWay.setId(clear.getPayWayId());
				payv2PayWay = payv2PayWayMapper.selectSingle(payv2PayWay);
				if (payv2PayWay != null) {
					clear.setWayName(payv2PayWay.getWayName());
				}
			}
		}
		PageObject<Payv2PayOrderClear> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	/**
	 * @Title: MSBankHandle
	 * @Description: 民生银行对账接口
	 * @param @param str
	 * @param @param time
	 * @param @param pav2PayOrderObjMap
	 * @param @param payv2PayOrderRefundObjMap
	 * @param @param orderList
	 * @param @param payType 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void MSBankHandle(String str, Map<String, Object> pav2PayOrderObjMap, Map<String, Object> payv2PayOrderRefundObjMap,
			 String payType, Double rateValue) {
		JSONObject jsonObject = JSONObject.fromObject(str);
		JSONObject json_to_data = jsonObject.getJSONObject("reply");// 即可
		if (json_to_data.containsKey("cupCheckDetailList") && null != json_to_data.getJSONArray("cupCheckDetailList")) {
			JSONArray json_to_strings = json_to_data.getJSONArray("cupCheckDetailList");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				if (json_to_strings.size() > 0) {
					for (int i = 0; i < json_to_strings.size(); i++) {// 循环读取即可
						Payv2PayOrderClear clear = null;
						JSONObject json_to_string1 = json_to_strings.getJSONObject(i);
						// 获取支付是否成功与退款
						String transType = (String) json_to_string1.get("transType");
						System.out.println("交易状态：" + transType);
						if (json_to_string1 != null) {
							// 商户订单号
							String alipayOrderNum = json_to_string1.getString("merchantOrderId");
							// 金额
							String alipayMoney = json_to_string1.getString("transAmt");
							// 交易时间 年月日
							Long ytime = json_to_string1.getLong("transDate");
							
							String transactionId = json_to_string1.getString("chnlOrderId");
							// 时间
							Long time = json_to_string1.getLong("transTime");
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
							SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
							Date date = new Date(ytime);
							Date date1 = new Date(time);
							String res = simpleDateFormat.format(date) + simpleDateFormat1.format(date1);
							Date finishedTimeDate = df.parse(res);
							if (transType.equals("1")) {// 成功
								if (pav2PayOrderObjMap.containsKey(alipayOrderNum)) {
									clear = getOrderClearByOrder(pav2PayOrderObjMap, alipayOrderNum, alipayMoney, "交易", payType, finishedTimeDate,
											rateValue);

								}
							}
							String refundNum=null;
							if (transType.equals("3")) {// 退款
								// 退款订单号
								refundNum = json_to_string1.getString("requestFlowNo");
								clear = getOrderClearByOrderRefund(payv2PayOrderRefundObjMap, pav2PayOrderObjMap, refundNum, alipayMoney, alipayOrderNum, "退款",
										 payType);
							}
							if (clear != null) {
								clear.setUpstreamTime(finishedTimeDate);
								clear.setUpstreamAmount(Double.valueOf(alipayMoney));
								clear.setClearTime(finishedTimeDate);
								clear.setTransactionId(transactionId);
							}
							//修个隔夜漏单数据
							if(clear==null){
								int type=1;
								String orderNum=null;
								//交易
								if (transType.equals("1")) {
									 type=1;
									 orderNum=alipayOrderNum;
									
								}
								//退款
								if (transType.equals("3")) {
									 type=2;
									 orderNum=json_to_string1.getString("requestFlowNo");
								}
								if(orderNum!=null){
									//修改隔夜漏单情况
									updateOrderTime(orderNum, finishedTimeDate, Double.valueOf(alipayMoney), type, transactionId);
								}
							}
						}
						if (clear != null) {
							//修改对账单状态
							updateOrderClear(clear);
//							payv2PayOrderClearMapper.insertByEntity(clear);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("民生银行对账单数据插入失败原因！" + e);
			}
		} else {
			LOGGER.error("民生银行对账单数据插入失败原因！" + json_to_data);
		}
		System.out.println("民生银行对账单完毕！");
	}

	/**
	 * @Title: HFBHandle
	 * @Description: 汇付宝-对账生成
	 * @param @param str
	 * @param @param pav2PayOrderObjMap
	 * @param @param payv2PayOrderRefundObjMap
	 * @param @param orderList
	 * @param @param payType
	 * @param @param rateValue 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void HFBHandle(String[] str, Map<String, Object> pav2PayOrderObjMap, Map<String, Object> payv2PayOrderRefundObjMap, 
			String payType, Double rateValue) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (str != null) {
				for (int i = 0; i < str.length; i++) {
					Payv2PayOrderClear clear = null;
					String indo = str[i];
					String indo2[] = indo.split(",");
					System.out.println("商户订单号：" + indo2[1]);
					System.out.println("交易金额：" + indo2[4]);
					System.out.println("交易时间：" + indo2[3]);
					String alipayOrderNum = String.valueOf(indo2[1]);
					String alipayMoney = String.valueOf(indo2[4]);
					// 格式：
					String finishedTimeDate1 = String.valueOf(indo2[3]);
					// 交易时间
					Date finishedTimeDate = df.parse(finishedTimeDate1);
					if (pav2PayOrderObjMap.containsKey(alipayOrderNum)) {
						clear = getOrderClearByOrder(pav2PayOrderObjMap, alipayOrderNum, alipayMoney, "交易", payType, finishedTimeDate, rateValue);
						if (clear != null) {
							clear.setUpstreamTime(finishedTimeDate);
							clear.setUpstreamAmount(Double.valueOf(alipayMoney));
							clear.setClearTime(finishedTimeDate);
							clear.setTransactionId(indo2[0]);
							//修改对账单状态
							updateOrderClear(clear);
//							payv2PayOrderClearMapper.insertByEntity(clear);
							
						}
					}
					//汇付宝没有退款对账
					if(clear==null){
						updateOrderTime(indo2[1], finishedTimeDate,Double.valueOf(alipayMoney), 1, indo2[0]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("=====>汇付宝-对账生成:原因" + e);
		}
		System.out.println("汇付宝-对账完毕！");
	}

	private static void ListSort(List<Payv2PayOrderClear> list) {
		Collections.sort(list, new Comparator<Payv2PayOrderClear>() {
			@Override
			public int compare(Payv2PayOrderClear o1, Payv2PayOrderClear o2) {
				try {
					Date dt1 = o1.getCreateTime();
					Date dt2 = o2.getCreateTime();
					if (dt1.getTime() > dt2.getTime()) {
						return 1;
					} else if (dt1.getTime() < dt2.getTime()) {
						return -1;
					} else {
						return 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
	}
	/**
	 * 
	 * updateOrderClear 
	 * 修改对账表状态
	 * @param clear    设定文件 
	 * void    返回类型
	 */
	public void updateOrderClear(Payv2PayOrderClear clear){
		Payv2PayOrderClear newClear=new Payv2PayOrderClear();
		if(clear.getType()==1){//订单：交易状态
			//上游流水号
			newClear.setTransactionId(clear.getTransactionId());
			//对账状态
			newClear.setStatus(clear.getStatus());
			//平台订单号
			newClear.setOrderNum(clear.getOrderNum());
			//对账时间
			newClear.setClearTime(clear.getClearTime());
			//上游交易时间
			newClear.setUpstreamTime(clear.getUpstreamTime());
			//上游交易金额
			newClear.setUpstreamAmount(clear.getUpstreamAmount());
			//上游状态
			newClear.setUpstreamStatus(1);
			//平台状态
			newClear.setPayStatus(1);
			//对账类型
			newClear.setType(1);
			payv2PayOrderClearMapper.updateByEntity2(newClear);
		}
		if(clear.getType()==2){//退款
			newClear.setTransactionId(clear.getTransactionId());
			newClear.setStatus(clear.getStatus());
			newClear.setRefundNum(clear.getRefundNum());
			newClear.setClearTime(clear.getClearTime());
			newClear.setUpstreamTime(clear.getUpstreamTime());
			newClear.setUpstreamAmount(clear.getUpstreamAmount());
			newClear.setUpstreamStatus(1);
			newClear.setPayStatus(1);
			newClear.setType(2);
			payv2PayOrderClearMapper.updateByEntity2(newClear);
		}
	}
	/**
	 * 
	 * updateOrderTime 
	 * 根据上游传回的支付集订单号，修改每日对账表，订单表的支付时间,退款订单表
	 * @param orderNum 订单号：退款订单号
	 * @param time 交易时间
	 * @param money 金额
	 * @param type 1：交易 2：退款
	 * @param transactionId   上游订单流水号
	 * void    返回类型
	 */
	public void updateOrderTime(String orderNum,Date time,double money,int type,String transactionId){
//		打印
		System.out.println("单号："+orderNum+"\t"+"时间："+DateUtil.DateToString(time, "yyyy-MM-dd HH:mm:ss")+"\t"+"金额："+money+"\t"+"上游流水号："+transactionId+"\n");
		//查询是否存在
		Payv2PayOrderClear payv2PayOrderClear=new Payv2PayOrderClear();
		if(type==1){
			payv2PayOrderClear.setOrderNum(orderNum);
		}
		if(type==2){
			payv2PayOrderClear.setRefundNum(orderNum);
		}
		//未支付
		payv2PayOrderClear.setPayStatus(3);
		//出账
		payv2PayOrderClear.setStatus(5);
		payv2PayOrderClear.setType(type);
		Payv2PayOrderClear payv2PayOrderClear1=payv2PayOrderClearMapper.selectSingle(payv2PayOrderClear);
		if(payv2PayOrderClear1!=null){
			//上游钱
			payv2PayOrderClear1.setUpstreamAmount(money);
			//改为支付成功
			payv2PayOrderClear1.setPayStatus(1);
			//上游状态
			payv2PayOrderClear1.setUpstreamStatus(1);
			//交易时间
			payv2PayOrderClear1.setPayTime(time);
			//上游交易时间
			payv2PayOrderClear1.setUpstreamTime(time);
			//对账状态为正常
			payv2PayOrderClear1.setStatus(1);
			//上游流水号
			payv2PayOrderClear1.setTransactionId(transactionId);
			//对账时间
			payv2PayOrderClear1.setClearTime(time);
			//修改对账表
			payv2PayOrderClearMapper.updateByEntity(payv2PayOrderClear1);
			//交易
			if(type==1){
				//先查询是否存在
				Payv2PayOrder payv2PayOrder=new Payv2PayOrder();
				payv2PayOrder.setOrderNum(orderNum);
				payv2PayOrder.setPayStatus(3);
				Payv2PayOrder payv2PayOrder1=payv2PayOrderMapper.selectSingle(payv2PayOrder);
				if(payv2PayOrder1!=null){
					//修改订单表
					//创建时间
					payv2PayOrder1.setCreateTime(time);
					//支付时间
					payv2PayOrder1.setPayTime(time);
					//订单状态：正常1
					payv2PayOrder1.setPayStatus(1);
					payv2PayOrderMapper.updateByEntity(payv2PayOrder1);
				}
			}
			//退款
			if(type==1){
				Payv2PayOrderRefund payv2PayOrderRefund=new Payv2PayOrderRefund();
				payv2PayOrderRefund.setRefundNum(orderNum);
				Payv2PayOrderRefund payv2PayOrderRefund1=payv2PayOrderRefundMapper.selectSingle(payv2PayOrderRefund);
				if(payv2PayOrderRefund1!=null){
					payv2PayOrderRefund1.setUpdateTime(new Date());
					payv2PayOrderRefund1.setRefundTime(time);
					payv2PayOrderRefundMapper.updateByEntity(payv2PayOrderRefund1);
				}
			}
			
		}
		System.out.println("隔夜对账数据完毕");
	}
	/**
	 * 获取某个时间的订单数据与退款数据
	 */
	public List<Map<String, Object>> getOderAndRefundByTime(String createTimeBegin, Long rateId) throws Exception {
		List<Map<String, Object>> mapList=new ArrayList<Map<String,Object>>();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("createTimeBegin", createTimeBegin);
		map.put("rateId", rateId);
		Map<String, Object> pav2PayOrderObjMap = new HashMap<String, Object>();
		//获取订单数据
		List<Payv2PayOrder> payv2PayOrderList = payv2PayOrderMapper.selectByObjectDate(map);
		for(Payv2PayOrder order : payv2PayOrderList){
			pav2PayOrderObjMap.put(order.getOrderNum(), order);
		}
		Map<String, Object> payv2PayOrderRefundObjMap = new HashMap<String, Object>();
		//获取退款订单数据
		List<Payv2PayOrderRefund> payv2PayOrderRefundList = payv2PayOrderRefundMapper.selectByRefundTime(map);
		for (Payv2PayOrderRefund refund : payv2PayOrderRefundList) {
			payv2PayOrderRefundObjMap.put(refund.getRefundNum(), refund);
		}
		mapList.add(payv2PayOrderRefundObjMap);
		mapList.add(pav2PayOrderObjMap);
		return mapList;
	}
	
	public List<String> getBills() {
		return payv2PayOrderClearMapper.getBills();
	}

	public List<Map<String,Object>> getBillList(String date) {
		List<Map<String,Object>> billList = payv2PayOrderClearMapper.getBillList(date);
		return billList;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject<OrderClearVO> getDifferOrder(Map<String, Object> map) {
		int totalData = payv2PayOrderClearMapper.getDifferCount(map);
		PageHelper helper = new PageHelper(totalData, map);
		List<OrderClearVO> differOrder = payv2PayOrderClearMapper.getDifferOrder(helper.getMap());
		PageObject<OrderClearVO> pageList = helper.getPageObject();
		pageList.setDataList(differOrder);
		return pageList;
	}
	
	public int isHaveBill(Map<String, Object> map) {
//		 根据条件搜索出对账集合
		List<Payv2PayOrderClear> orderClearList = payv2PayOrderClearMapper.isHaveBill(map);//搜索未出账的数据
		List<Payv2PayOrderClear> orderClearList2 = payv2PayOrderClearMapper.isHaveClear(map);//搜索已出账的数据
		int status = 0;
//		 判断是否均已出账
		System.out.println(orderClearList.size());
		if(orderClearList.size() == 0 && orderClearList2.size() > 0){
			status = 1; //有数据并可以出账单
		}else if(orderClearList.size() > 0){
			status = 2; //存在未出账的数据
		}else if(orderClearList.size() == 0 && orderClearList2.size() == 0){
			status = 3; //该商户在当前搜索条件无订单，只改变账单状态，不生成账单
		}
		
		return status;

	}

	public void updateClear(String ids) {
		payv2PayOrderClearMapper.updateClear(ids);
		//更改订单表状态已支付之类的。
		List<Map<String, Object>> ordeClear = payv2PayOrderClearMapper.getOrdeClear(ids);
		for (Map<String, Object> map : ordeClear) {
			//交易
			if(Integer.parseInt(map.get("type").toString())==1){
				Payv2PayOrder order = new Payv2PayOrder();
				order.setOrderNum(map.get("order_num").toString());	//订单号
				order.setPayMoney(Double.valueOf(map.get("order_money").toString()));	//订单金额
				order.setPayDiscountMoney(Double.valueOf(map.get("order_money").toString()));	//优惠后订单金额(=订单金额)
				order.setPayWayMoney(Double.valueOf(map.get("buss_way_rate_money").toString()));	//通道费
				order.setPayStatus(Integer.parseInt(map.get("upstream_status").toString()));	//状态(上游状态)
				payv2PayOrderMapper.updateByEntity(order);
			} else {
				Payv2PayOrderRefund refund = new Payv2PayOrderRefund();
				refund.setRefundNum(map.get("refund_num").toString());
				refund.setRefundMoney(Double.valueOf(map.get("order_money").toString()));	//退款金额
				refund.setRefundStatus(Integer.parseInt(map.get("upstream_status").toString()));	//状态(上游状态)
				payv2PayOrderRefundMapper.updateByEntity(refund);
			}
		}
	}

	public void updateOutOrder(String date, String rateId) {
		payv2PayOrderClearMapper.updateOutOrder(date, rateId);
		List<Map<String, Object>> clearDetail = payv2PayOrderClearMapper.getClearDetail(date, rateId);
		List<Payv2BussWayDetail> list = new ArrayList<Payv2BussWayDetail>();
		for (int i = 0; i < clearDetail.size(); i++) {		//只留退款记录
			if(new Integer(2).equals(Integer.valueOf(clearDetail.get(i).get("type").toString()))){
				continue;
			}
			Long companyId = Long.valueOf(clearDetail.get(i).get("company_id").toString());	//商户id
			Long appId = Long.valueOf(clearDetail.get(i).get("app_id").toString());	//appId
			Integer count = Integer.valueOf(clearDetail.get(i).get("counts").toString());	//交易总笔数
			Double money = Double.valueOf(clearDetail.get(i).get("money").toString());	//交易总额
			Double costRate = Double.valueOf(clearDetail.get(i).get("cost_rate").toString());	//成本费率
			Double bussRate = Double.valueOf(clearDetail.get(i).get("buss_way_rate").toString());	//商户费率
			Double costRateMoney = Double.valueOf(clearDetail.get(i).get("costMoney").toString());	//成本手续费
			Double bussRateMoney = Double.valueOf(clearDetail.get(i).get("wayMoney").toString());	//商户手续费
			Long channelId = Long.valueOf(clearDetail.get(i).get("channel_id").toString());	//
			
			Payv2BussWayDetail detail = new Payv2BussWayDetail();
			detail.setCompanyId(companyId);
			detail.setAppId(appId);
			detail.setSuccessCount(count);
			detail.setSuccessMoney(money);
			detail.setCostRate(costRate);
			detail.setBussWayRate(bussRate);
			detail.setCostRateMoney(costRateMoney);
			detail.setBussWayRateMoney(bussRateMoney);
			detail.setChannelId(channelId);
			detail.setRefundCount(0);
			detail.setRefundMoney(0.0);
			detail.setPayWayId(Long.valueOf(rateId));
			
			list.add(detail);
			clearDetail.remove(i);
			i--;
		}
		for (Map<String, Object> map : clearDetail) {
			Long companyId = Long.valueOf(map.get("company_id").toString());	//商户id
			Long appId = Long.valueOf(map.get("app_id").toString());	//appId
			Integer count = Integer.valueOf(map.get("counts").toString());	//退款总笔数
			Double money = Double.valueOf(map.get("money").toString());	//退款总额
			for (Payv2BussWayDetail detail : list) {
				if(companyId==detail.getCompanyId() && appId==detail.getAppId()){
					detail.setRefundCount(count);
					detail.setRefundMoney(money);
					
					continue;
				}
			}
		}
		
		for (Payv2BussWayDetail detail : list) {
			double ceil = DecimalUtil.getCeil(detail.getSuccessMoney()-detail.getRefundMoney(),2);
			detail.setBussMoney(ceil);	//支付净额
			detail.setClearTime(DateUtil.StringToDate(date, DateStyle.YYYY_MM_DD));
			detail.setCreateTime(new Date());
			payv2BussWayDetailMapper.insertByEntity(detail);
		}
	}

	
	public List<Payv2PayOrderClear> queryByApp(Map<String, Object> orderParam) {
		return payv2PayOrderClearMapper.queryByApp(orderParam);
	}
	/**
	 * PINGANHandle 
	 * 平安银行：对账接口数据解析
	 * @param list
	 * @param pav2PayOrderObjMap
	 * @param payv2PayOrderRefundObjMap
	 * @param payType
	 * @param rateValue    设定文件 
	 * void    返回类型
	 */
	public void PINGANHandle(List<PANOrderVo> list, Map<String, Object> pav2PayOrderObjMap, Map<String, Object> payv2PayOrderRefundObjMap, String payType,
			Double rateValue) {
		try {
			for (PANOrderVo panOrderVo : list) {
				Payv2PayOrderClear clear = null;
				// 交易类型
				String ordtype = panOrderVo.getOrd_type();
				// 商户流水号
				String Ord_mct_id = panOrderVo.getOrd_mct_id();
				// 支付集订单号
				String Out_no = panOrderVo.getOut_no();
				// 金额
				String trade_amount = panOrderVo.getTrade_amount();
				int trade_amount1 = Integer.valueOf(trade_amount);
				// 实际交易是解决
				String date = panOrderVo.getTrade_pay_time();
				Date finishedTimeDate = null;
				if (StringUtils.isNoneEmpty(date)) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					finishedTimeDate = df.parse(date);
				}
				double money = trade_amount1 / 100.00;
				// 交易
				if (ordtype.equals("1")) {
					if (pav2PayOrderObjMap.containsKey(Out_no)) {
						clear = getOrderClearByOrder(pav2PayOrderObjMap, Out_no, String.valueOf(money), "交易", payType, finishedTimeDate, rateValue);
					}
					if (clear != null) {
						clear.setUpstreamTime(finishedTimeDate);
						clear.setUpstreamAmount(money);
						// 上游订单号：
						clear.setTransactionId(Ord_mct_id);
						clear.setClearTime(finishedTimeDate);
					}
				}
				// 退款
				if (ordtype.equals("2")) {
					clear = getOrderClearByOrderRefund(payv2PayOrderRefundObjMap, pav2PayOrderObjMap, Out_no, String.valueOf(money), null, "退款", payType);
					if (clear != null) {
						clear.setUpstreamTime(finishedTimeDate);
						clear.setUpstreamAmount(money);
						// 上游退款订单号：
						clear.setTransactionId(Ord_mct_id);
						clear.setClearTime(finishedTimeDate);
					}
				}
				if (clear != null) {
					// 修改对账单状态
					updateOrderClear(clear);
					// payv2PayOrderClearMapper.insertByEntity(clear);
				}
				// 修改隔夜漏单情况
				if (clear == null) {
					int type = 1;
					// 交易
					if (ordtype.equals("1")) {
						type = 1;
					}
					// 退款
					if (ordtype.equals("2")) {
						type = 2;
					}
					if (Out_no != null) {
						updateOrderTime(Out_no, finishedTimeDate, money, type, Ord_mct_id);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("平安银行：对账接口数据完毕");
	}
}
