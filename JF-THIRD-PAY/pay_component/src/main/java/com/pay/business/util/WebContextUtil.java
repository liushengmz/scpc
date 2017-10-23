package com.pay.business.util;

import javax.servlet.http.HttpServletRequest;

public class WebContextUtil {

	public static String getBasePath(HttpServletRequest request) {
		return getBasePath(request, true);
	}

	/**
	 * 获取项目路径
	 * @param request
	 * @param flag 为真带端口,为假不带端口
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request, boolean flag) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String path = request.getContextPath();
		StringBuilder sb = new StringBuilder();
		if (flag) {
			sb.append(scheme).append("://").append(serverName).append(":")
					.append(port).append(path);
		} else {
			sb.append(scheme).append("://").append(serverName).append(path);
		}
		return sb.toString();
	}

}
