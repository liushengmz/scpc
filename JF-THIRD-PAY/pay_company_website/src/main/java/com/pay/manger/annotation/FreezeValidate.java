/**  
 * @Title: Freeze.java
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
 * <P>ClassName: Freeze</P>
 * <P>@Description: 自定义冻结操作校验注解</P>
 * <P>Company:</P>
 * @author pengzhihao
 * @date 2016-7-1下午3:38:41
 */
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FreezeValidate {

}
