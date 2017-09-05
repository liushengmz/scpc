package com.pay.company.interceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private static final String TAGS = "(?i)(<|%3C)(.*?)script(.*?)(>|%3E)|(<|%3C)(.*?)frame(.*?)(>|%3E)|u003c|u003e";
	private static final String EVENTS = "(\\s|%20)(onload|onunload|onchange|onsubmit|onreset|onselect|onblur|onfocus|onkeydown|onkeypress|onkeyup|onclick|ondblclick|onmousedown|onmousemove|onmouseout|onmouseover|onmouseup|onerror).*";
	private static final String EXP = "(\\s|%20)src(\\s|%20)*=|script(\\s|%20)*:|eval(\\s|%20)*\\((.*?)\\)|expression(\\s|%20)*\\((.*?)\\)|alert(\\s|%20)*\\((.*?)\\)";
	private static final String XSS_REGEX = TAGS + "|" + EVENTS + "|" + EXP;
	private static final String SQL_REGEX = "('.+--)|(--)|\\/\\*|\\*\\/|(\\|)|(%7C)|(\\s|%20)((?i)select|delete|insert|update|union|truncate|drop)(\\s|%20)";

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		return filterParamString(super.getParameter(name));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> rawMap = super.getParameterMap();
		Map<String, String[]> filteredMap = new HashMap<String, String[]>(rawMap.size());
		Set<String> keys = rawMap.keySet();
		for (String key : keys) {
			String[] rawValue = rawMap.get(key);
			String[] filteredValue = filterStringArray(rawValue);
			filteredMap.put(key, filteredValue);
		}
		return filteredMap;
	}

	@Override
	public Cookie[] getCookies() {
		Cookie[] existingCookies = super.getCookies();
		if (existingCookies != null) {
			for (int i = 0; i < existingCookies.length; ++i) {
				Cookie cookie = existingCookies[i];
				cookie.setValue(filterParamString(cookie.getValue()));
			}
		}
		return existingCookies;
	}

	@Override
	public String getQueryString() {
		return filterParamString(super.getQueryString());
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] rawValues = super.getParameterValues(name);
		if (rawValues == null)
			return null;
		String[] filteredValues = new String[rawValues.length];
		for (int i = 0; i < rawValues.length; i++) {
			filteredValues[i] = filterParamString(rawValues[i]);
		}
		return filteredValues;
	}

	private String[] filterStringArray(String[] rawValue) {
		String[] filteredArray = new String[rawValue.length];
		for (int i = 0; i < rawValue.length; i++) {
			filteredArray[i] = filterParamString(rawValue[i]);
		}
		return filteredArray;
	}

	private String filterParamString(String rawValue) {
		if (StringUtils.isBlank(rawValue))
			return rawValue;
		rawValue = rawValue.replaceAll(XSS_REGEX, "");
		rawValue = rawValue.replaceAll(SQL_REGEX, "");
		rawValue = rawValue.replace("'", "&apos;");
		rawValue = rawValue.replace("\"", "&quot;");
		return rawValue;
	}

}
