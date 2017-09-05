/**   
 * @Title: PictureCheckCode.java
 * @Package cn.iyizhan.treamwork.servlet.manager
 * @Description: TODO(用一句话描述该文件做什么)
 * @author buyuer
 * @date 2015年11月7日 上午10:48:39 
 * @version V1.0
 */
package com.pay.business.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.core.teamwork.base.util.IdentifyingCode;


/**
 * @ClassName: PictureCheckCode
 * @Description: 图片验证码servlet
 * @author buyuer
 * @date 2015年11月7日 上午10:48:39
 * 
 */

@SuppressWarnings("serial")
public class PictureCheckCode extends HttpServlet {

    public PictureCheckCode() {
        super();
    }

    public void init() throws ServletException {
        super.init();
    }

    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	IdentifyingCode idCode = new IdentifyingCode();
    	
    	BufferedImage image = new BufferedImage(idCode.getWidth(), idCode.getHeight(), BufferedImage.TYPE_INT_RGB);
		String vCode = idCode.drawGraphic(image);
		//装载code 到session中
		request.getSession().setAttribute("code", vCode);
		response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        
        ServletOutputStream sos = null;
        try {
			sos = response.getOutputStream();
			ImageIO.write(image, "jpeg",sos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			try {sos.close();} catch (IOException e) {e.printStackTrace();}
		}
    }
}
