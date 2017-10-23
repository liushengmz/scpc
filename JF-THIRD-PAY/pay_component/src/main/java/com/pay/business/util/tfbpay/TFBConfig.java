package com.pay.business.util.tfbpay;

public class TFBConfig {

    // 接口域名，测试访问域名（正式环境改为：api.tfb8.com）
    public static String hostName = "http://apitest.tfb8.com";

    // MD5密钥，国采分配给商户的用于签名和验签的key
    public static String key = "12345";

    // 商户/平台在国采注册的账号。国采维度唯一，固定长度10位
    public static String spid = "1800071515";

    // 用户号，持卡人在商户/平台注册的账号。商户/平台维度唯一，必须为纯数字
    public static String sp_userid = "09285810";

    // 订单有效时长，以国采服务器时间为准的订单有效时间长度。单位:秒，如果不填则采用默认值
    public static String expire_time = "";

    // 订单金额的类型。1 – 人民币(单位: 分)
    public static String cur_type = "1";

    // 商户的用户使用的终端类型。1 – PC端，2 – 手机端
    public static String channel = "1";

    // 签名的方法。目前支持: MD5，RSA
    public static String encode_type = "MD5";

    // 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://商户网址//tfb_qpay-JAVA-UTF8-MD5/notify_url.jsp";

    // 商户的私钥,需要PKCS8格式
    public static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK+LzCZnUWIsRSxKyGZrZI+BU+Y+wnTXPpVbKcm5LT1fg/+o7aQR6B7pheWSEH5xLiFmtUkWSgZ7tYJhjovJkwgIJ91BQBg3rVT3xPCjeVu88mrdvzQOe6sS5WNPu3Wxbht9uACO16zupdDrruhjRUaCX5tkLukccU3bqp9FpkkNAgMBAAECgYBx8mB1nSLqgqnz8ibatGL185CuJ5a5mO36rM4XLqf66oEX9mMq2KS/S/2p4oHqUTUMYUrTQjCSvMI4+3I3soRI4k4J5VsyP9zHyHzafvNUTUyp2ybaVgmh3oxU4sx015fd+3Qc219l+Jdod+rIi68NJqhhMUU+q7yxmesCUCkZAQJBAOWH5bu9FmFIiSjWHVj6XE0904KOWSoHsenymzMZfM0s1kck1hUvwntUcmUhkiuz4BBmiKOy65MtNyJ6ChE3UP0CQQDDyi/gX/xOhCOpWoDMnYyKGyQH7GMJBIwK/X80Yha3Qtl/WrdqrpNV/ZHyQJgcIQFoMNLbNotoUOMAjthkrR1RAkAU5RAmzQnShVXnH8bAKNpqNayhf+/iAZ1SnMFAH5va2bAP/ex3NUfRDljzl+DElbVaCNt7e3gyh7UzMETmWFDJAkAwFtw1jz3ohxo/QYR7PYNEdLAf5hbZIy3GkUcKNcGAl8HWPxDn+iMkLtkHGIiD+DNhRQS1ZStOnvdyrqNF7yNRAkEAxm2MZmPHl+7jbDjHG6c+3SE6e0s7iZyatgh2gosKXdpqUWe3zVXPN04kLarZ7tasl1IBqHr1LpzdHEUReiNRBQ==";

    // 天付宝公钥
    public static String TFB_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjDrkoVbyv4jTxeKtKEiK2mZiezQvfJV3sGhiwOnB+By5sa5Sa6Ls4dt5AGVqKHxyQVKRpu/utwtEt2MijWx45P1y2xGe7oDz2hUXP0j8sSa1NP26TmWHwO7czgJxxrdJ6RNqskSfjwsa5YMsqmcrumxUIxeCg5EOkgU26bnPoZQIDAQAB";

    // 银行卡快捷签约支付申请调用的接口名
    public static String qsignPayApplyApi = hostName + "/cgi-bin/v2.0/api_qsignpay_apply.cgi";

    // 银行卡快捷签约支付确认调用的接口名
    public static String qsignPayComfirmApi = hostName + "/cgi-bin/v2.0/api_qsignpay_confirm.cgi";

    // 银行卡快捷支付申请调用的接口名
    public static String bankCardPayApi = hostName + "/cgi-bin/v2.0/api_qpay_apply.cgi";

    // 银行卡快捷支付确认调用的接口名
    public static String bankCardPayComfirmApi = hostName + "/cgi-bin/v2.0/api_qpay_confirm.cgi";

    // 银行卡快捷解约调用的接口名
    public static String qsignCancelApi = hostName + "/cgi-bin/v2.0/api_qsign_cancel.cgi";

    // 支付结果单笔查询调用的接口名
    public static String orderQueryApi = hostName + "/cgi-bin/v2.0/api_single_qry_order.cgi";

    // 快捷签约协议单笔查询调用的接口名
    public static String qsignQueryApi = hostName + "/cgi-bin/v2.0/api_single_qry_qsign.cgi";

    // 银行卡信息单笔查询调用的接口名
    public static String cardbinQueryApi = hostName + "/cgi-bin/v2.0/api_single_qry_cardbin.cgi";

    // 银行限额单笔查询调用的接口名
    public static String quotaQueryApi = hostName + "/cgi-bin/v2.0/api_single_qry_quota.cgi";

    // 快捷验证码短信重发调用的接口名
    public static String smsResendApi = hostName + "/cgi-bin/v2.0/api_sms_resend.cgi";

    // 服务器数据编码类型
    public static String serverEncodeType = "GBK";
}
