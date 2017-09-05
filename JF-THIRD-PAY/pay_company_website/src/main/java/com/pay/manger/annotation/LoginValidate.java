/**  
 * @Title: NeedLogin.java
 * @Package com.dianyou.manger.annotation
 * @Description: TODO
 * @author pengzhihao
 * @date 2016-7-1
 */
package com.pay.manger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <P>ClassName: NeedLogin</P>
 * <P>@Description: 自定义需要登录校验注解</P>
 * <P>Company:</P>
 * @author pengzhihao
 * @date 2016-7-1下午12:00:12
 */
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginValidate {

}
