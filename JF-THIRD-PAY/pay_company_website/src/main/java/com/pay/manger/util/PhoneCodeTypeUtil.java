package com.pay.manger.util;

/**
 * 
 * 
 * @author    Buyuer
 * @version   V0.1 2015年10月24日[版本号, YYYY-MM-DD]
 * @see       类型
 * @since     短信验证码的类型
 */
public class PhoneCodeTypeUtil {

    /**
     * 注册
     */
    public final static int CODE_TYPE_REG = 1;

    /**
     * 绑定手机
     */
    public final static int CODE_TYPE_BIND_PHONE = 2;

    /**
     * 找回支付密码
     */
    public final static int CODE_TYPE_FIND_PAY_PWD = 3;
    
    /**
     * 提现
     */
    public final static int CODE_TYPE_PAY = 4;

    /**
     * 切换账户
     */
    public final static int CODE_TYPE_CHANG = 5;
    
    /**
     * 设置支付密码
     */
    public final static int CODE_SET_PAYPASSWORD = 6;
    
    /**
     * 设置用户密码
     */
    public final static int CODE_SET_PASSWORD = 7;
    
    /**
     * 忘记密码-找回密码
     */
    public final static int CODE_TYPE_FIND_PWD = 8;
    
    /**
     * 支付验证
     */
    public final static int CODE_TYPE_PAY_SMS = 9;
}
