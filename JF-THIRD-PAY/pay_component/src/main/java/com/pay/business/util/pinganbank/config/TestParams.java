package com.pay.business.util.pinganbank.config;

import com.core.teamwork.base.util.properties.PropertiesUtil;

/**
* @Title: TestParams.java 
* @Package com.pay.business.util.pinganbank.config 
* @Description: 平安商户支付相关配置文件
* @author ZHOULIBO   
* @date 2017年8月8日 下午3:05:51 
* @version V1.0
*/
public interface TestParams {
	//商户open_id
//	String OPEN_ID = "35fa0f123ea5f7718d23eba85370af8d";
	//商户open_key
//	String OPEN_KEY = "0f3bd97cc3e28bfeb733918f71a8be68";
	//URL请求
	String OPEN_URL = "https://api.orangebank.com.cn/mct1/";
	//签约的支付列表
	String PAYLIST = "paylist";
	//订单列表
	String ORDERLIST = "order";
	//订单明细
	String ORDERVIEW = "order/view";
	//下订单
	String PAYORDER = "payorder";
	//查询付款状态
	String QUERYPAYSTATUS = "paystatus";
	//取消订单接口
	String PAYCANCEL = "paycancel";
	//退款接口
	String PAYREFUND = "payrefund";
	//测试：私钥
//	String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDddN6A+Ww6AgygJb7u9J5UCgPMfqEjvkJ6uWk5i1YOukNqiAqY0o3wQPXNnfTVc8LeM3HV4XqVs2evMTTTLjhspmXQlYx9ShaLV3oXHeUx2Jsk/1s/OkuLF1eYNO2vhTcAIYfbuZKRrIYIih+1PpFM3hniA5KdQNX84E6scW1eGfQgUjqCSeKZtX07tIF867BxaQbchlyC71nlgXajg+DONN0NFNiLu+b0sM2UeY+HJQJQ3c+VkS7DAtW8N1vYcO9JzvDu6mnEI2dzAF2fAj78vinBAZFTHc7Ct4dGpPxJTRf8R3bUfXV8xZBc+3kkf5tUbizMkR4LHOftRSyD9YnRAgMBAAECggEBAJYOVaJA39OihdmSGgEiYZICQzayaw+kILm1npYuUr6h+YJa8gtBSIoOCkAsErT7voP/idfp870yFkSAbBHYVMVSLtUaMFrI8+Ow/3pgeGfBJMb5/GMoZf22cFUjMBbphi4hikQZRzZMF3n71aZi4eOa7yDVWOgTAaxadRSluvyyB+DwKdhOB8zOHiDEHgL0p6U+1OIIT0xccPHmnouXDOn9ywF+C7xnHqBPxpHBpvisD6RXD9KDhKdWYCBYlzeLD76NvgaWeny4dRYHKQE0SWYM43YHZqK8E0ofypBpj7zjS+UkpyJ7NZH5voxaf3uLGrH/ccnuFVmJq6TMnwt04z0CgYEA/14cDkYjB9NkeuRfGNrEqv+GqzVvNbHKaYuQ9/tqz7x+87Ph7CGUlLspS7tPjOZXRGlYgAPqD6KwDzo9buQNOZeOh6Agi0I30PE8AQcpVydXfKvXRLux6ZFMYPPjkDlPAXAsQH/94zfLW3TwI1uMh88o2lPCI/sM+/i21w7JNM8CgYEA3gFC+NaseMpUSiwD3D9IApSOnHe/keyUq+umEWB12tsxq+YwkfMvYVrgb8RCa5TNEj9oXsvgO6nDXENUybyCVjcWoU5BWeW4xw++FWUjTyNMyH67agI+9UUjwPVnjYrLmcge/gwINykbtYClQfoHMzKo2ogqtKILqRGXkHL0P18CgYAuV3C18mpnACiq2IidZQ3tjiNtLGw7DUGTN72eEuUGP8m2Bf3IsStadkB/OsWr5x0NECT8TjmKjtZuXP5LAl2YBvXZjOh6/RBN/YkLErag10XcHP8avQkDPtfifD/eq1e4BhgxuEhllHl15lmxwOpWtvRN8oc3qlZn33Gmw0smJwKBgGN2iTzXYTpU2+LHSYt5xpdxW1t6wxdruUg1MZgDcYn2PpDXdtdM7uNdRcSNV3y/lAki423lRbc1XdOOTwR7MqHR2I+4ccsHAvwcb3tCbslb9WC2dt0N2IsmyNgAmr5ter6RTGFhnqSoBEQTOPcQP/2OKtyNuSRonXTH7vHGrutdAoGAWQvc64nGwt1u2U9ct9iY0VDtP1fT38XO67xNSp+zUpjcfYZMho5uKFdO5Qa5/l5q3AVTY7T+dEzHCaYcmMbe+GBLLKjxu4YzPH+crXY3bE5T/XL3gJ6Hvq7iQa9iGSpDnT3Jk5IpOomGewUQ4pu/YBih09IltkEEQurewtLnMFM=";
	//回调URL
	String NOTIFY_URL=PropertiesUtil.getProperty("rate", "pa_pay_notify_url");
	//公众号支付的成功URL
	String JUMP_URL=PropertiesUtil.getProperty("rate", "pa_pay_jump_url");
	//微信公众号配置
	String CONFIG="contract/addconfig";
	//公众号URL
	String OPNE_GZH_URL="https://mixpayuat4.orangebank.com.cn/org1/";
}
