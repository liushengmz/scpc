package com.pay.manger.util;

import java.util.UUID;

import com.core.teamwork.base.util.returnback.BaseMsgTips;


public class ReturnMsgTips extends BaseMsgTips {
	
//	 *
//	 * #============================================================================
//	 * # 注册、验证码
//	 * #============================================================================
//	 *
	/**
	 * 1分钟内不能重复发送短信!
	 */
	public static final String FAIL_PHONE_CODE_REPEAT_IN_1M = "1分钟内不能重复发送短信!";
	/**
	 * 10分钟内不能重复发送短信!
	 */
	public static final String FAIL_PHONE_CODE_REPEAT_IN_10M = "10分钟内不能重复发送短信!";
	/**
	 * 发送短信验证码失败!
	 */
	public static final String FAIL_SEND_PHONE_CODE = "发送短信验证码失败!";
	/**
	 * 该手机号已注册!
	 */
	public static final String FAIL_PHONE_IS_REG = "该手机号已注册!";
	/**
	 * 该手机号未注册!
	 */
	public static final String FAIL_PHONE_NOT_REG = "该手机号未注册!";
	/**
	 * 手机号与密码不匹配!
	 */
	public static final String FAIL_PHONE_PASSWORD_NOT_MATCHING = "手机号与密码不匹配!";
	/**
	 * 该帐号无效或已被锁定，请联系管理员!
	 */
	public static final String FAIL_ID_IS_LOCK = "该帐号无效或已被锁定，请联系管理员!";
	/**
	 * 新密码不能与旧密码相同
	 */
	public static final String FAIL_SAME_PWD = "新密码不能与旧密码相同";
	/**
	 * 手机验证码错误！
	 */
	public static final String FAIL_PHONE_CODE = "手机验证码错误！";
	
//	 *
//	 * #============================================================================
//	 * # 用户   USER开头
//	 * #============================================================================
//	 *
	/**
	 * 用户未登录
	 */
	public static final String USER_NOT_LOGIN = "用户未登录";
	/**
	 * 用户重复操作
	 */
	public static final String USER_NOT_REPEAT = "用户重复操作";
	
	/**
	 * 用户账号被冻结
	 */
	public static final String USER_IS_FREEZE = "你的账号已被冻结";
	
	/**
	 * 用户账号被删除
	 */
	public static final String USER_IS_DELETE = "你的账号已被删除";

	/**
	 * 该账号已存在
	 */
	public static final String USER_NAME_EXISTS = "该帐号已存在";

	/**
	 * 帐号或密码错误
	 */
	public static final String USER_NAME_PWD_ERROR = "帐号或密码错误";
//	 *
//	 * #============================================================================
//	 * # 资源
//	 * #============================================================================
//	 *	
	/**
	 * 用户session有效时长
	 */
	public static final String RESOURCES_NOT_EXIST = "资源不存在";
	
	/**
	 * 文件上传失败
	 */
	public static final String FILE_UPLOAD_FAILED = "文件上传失败";
	
	/**
	 * 文件上传丢失
	 */
	public static final String FILE_UPLOAD_LOSE = "文件上传丢失";
	
//	 *
//	 * #============================================================================
//	 * # 产品 PRO开头
//	 * #============================================================================
//	 *	
	/**
	 * 产品不存在
	 */
	public static final String PRO_NOT_EXIST = "产品不存在";
	
	public static final String ERROR_PHONE_CODE = "手机验证码错误！";
	
	public static final String FAIL_COMPANY_NON_EXIST = "该公司信息不存在！";
	
	public static final String FAIL_PAY_PASSWORD_ERROR_LATTER_10M = "支付密码输入错误次数过多,请10分钟后再试!";
	
	public static final String FAIL_PAY_PASSWORD_ERROR = "支付密码错误!";
	
	public static final String FAIL_NOT_LOGIN = "请先登录再进行操作!";
	
	public static final String ERROR_ILLEGAL_OPERATION = "请勿进行非法操作！";
	
	public static final String FAIL_PAY_PASSWORD_SETTED = "已经设置过支付密码!";
	
	public static final String FAIL_SET_PAY_PASSWORD = "设置支付密码失败,请输入正确的密码和手机号码!!";
	
	public static final String FAIL_PHONE_CURR_PHONE_NOT_MATCHING = "手机号与当前帐号的手机号不匹配!";
	
	public static final String FAIL_LOGIN_PASSWORD_SETTED = "已经设置过登陆密码!";
	
	public static final String FAIL_PASSWORD_ERROR_LATTER_10M = "登录密码输入错误次数过多,请10分钟后再试!";
	
	public static final String FAIL_PASSWORD_ERROR = "密码错误!";
	
	public static final String FAIL_UPDATE_PASSWORD_TIMEOUT_RETRY = "修改密码超时,请再次验证旧密码!";
	
	public static final String ERROR_ILLEGAL_NICK= "昵称已经使用";
	
	public static final String OPEN_ILLEGAL_NICK= "已经有开户记录了";
	
	public static final String NO_DATA= "没有相关数据";
}
