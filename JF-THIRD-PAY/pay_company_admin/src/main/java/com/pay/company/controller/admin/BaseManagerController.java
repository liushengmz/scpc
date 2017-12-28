package com.pay.company.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.core.teamwork.base.util.ValidatorUtil;
import com.pay.business.merchant.entity.Payv2BussCompany;



/** 
 * @ClassName: BaseManagerController
 * @Description: manager项目的父类
 * @author buyuer
 * @date 2015年11月12日 下午5:05:06
 * 
 */
public class BaseManagerController<T, E>  {
	
    /**
     * 
     * @author buyuer
     * @Title: getAdmin
     * @Description: 获取admin
     */
    public Payv2BussCompany getAdmin() {
        Payv2BussCompany admin = (Payv2BussCompany) getSessionAttr("admin");
        if (admin != null) {
        	
        	Object userName = getSessionAttr("userName");
        	Object bussCompanyId = getSessionAttr("bussCompanyId");
        	System.out.println("Andy Log : userName : " + userName + ";bussCompanyId:" + bussCompanyId + ";admin id:" + admin.getId());
            return admin;
        }
        return null;
    }

    /**
     * 
     * @author buyuer
     * @Title: setAdmin
     * @Description: 将admin存入session
     */
    public void setAdmin(Payv2BussCompany admin) {
        getSession().setAttribute("admin", admin);
    }
    
    /**
     * 获取session
     * 
     * @param code
     * @return
     */
    protected Object getSessionAttr(String code) {
        HttpSession session = getSession();
        
        if (session != null && ValidatorUtil.isNotEmpty(code)) {
            return getSession().getAttribute(code);
        } else {
            return null;
        }
    }

    /**
     * 
     * 
     * @author    Buyuer
     * @version   V0.1 2015年10月30日[版本号, YYYY-MM-DD]
     * @see       获取session方法
     * @parm
     */
    protected HttpSession getSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //30分钟session失效
        //12小时SESSION有效 MODIFY BY ANDY.CHEN
        request.getSession().setMaxInactiveInterval(43200);
        return request.getSession();
    }
    
    /**
     * 根据用户是否登录 以及当前状态是否在线下版
     * @return
     */
    protected Payv2BussCompany isOffLine(){
    	Payv2BussCompany admin = getAdmin();
		if(admin == null || admin.getCurrentUserStatus()!=2){
			return null;
		}else{
			return admin;
		}
    }
}
