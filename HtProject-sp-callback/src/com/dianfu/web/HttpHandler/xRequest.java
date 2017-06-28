package com.dianfu.web.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
 

public class xRequest implements ServletRequest {

	private xInputStream _stm;
	private ServletRequest _r;
	private com.database.Interface.IDatabase _dbase;

	public xRequest(ServletRequest request) {
		_r = request;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (_stm == null) {
			_stm = new xInputStream(_r.getInputStream());
		}
		return _stm;
	}

	@Override
	public AsyncContext getAsyncContext() {
		return _r.getAsyncContext();
	}

	@Override
	public Object getAttribute(String arg0) {
		return _r.getAttribute(arg0);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return _r.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding() {
		return _r.getCharacterEncoding();
	}

	@Override
	public int getContentLength() {
		return _r.getContentLength();
	}

	@Override
	public long getContentLengthLong() {
		return _r.getContentLengthLong();
	}

	@Override
	public String getContentType() {
		return _r.getContentType();
	}

	@Override
	public DispatcherType getDispatcherType() {
		return _r.getDispatcherType();
	}

	@Override
	public String getLocalAddr() {
		return _r.getLocalAddr();
	}

	@Override
	public String getLocalName() {
		return _r.getLocalName();
	}

	@Override
	public int getLocalPort() {
		return _r.getLocalPort();
	}

	@Override
	public Locale getLocale() {
		return _r.getLocale();
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return _r.getLocales();
	}

	@Override
	public String getParameter(String arg0) {
		return _r.getParameter(arg0);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return _r.getParameterMap();
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return _r.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String arg0) {
		return _r.getParameterValues(arg0);
	}

	@Override
	public String getProtocol() {
		return _r.getProtocol();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return _r.getReader();
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getRealPath(String arg0) {
		return _r.getRealPath(arg0);
	}

	@Override
	public String getRemoteAddr() {
		return _r.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return _r.getRemoteHost();
	}

	@Override
	public int getRemotePort() {
		return _r.getRemotePort();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return _r.getRequestDispatcher(arg0);
	}

	@Override
	public String getScheme() {
		return _r.getScheme();
	}

	@Override
	public String getServerName() {
		return _r.getServerName();
	}

	@Override
	public int getServerPort() {
		return _r.getServerPort();
	}

	@Override
	public ServletContext getServletContext() {
		return _r.getServletContext();
	}

	@Override
	public boolean isAsyncStarted() {
		return _r.isAsyncStarted();
	}

	@Override
	public boolean isAsyncSupported() {
		return _r.isAsyncSupported();
	}

	@Override
	public boolean isSecure() {
		return _r.isSecure();
	}

	@Override
	public void removeAttribute(String arg0) {
		_r.removeAttribute(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		_r.setAttribute(arg0, arg1);
	}

	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
		_r.setCharacterEncoding(arg0);
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return _r.startAsync();
	}

	@Override
	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) throws IllegalStateException {
		return _r.startAsync();
	}

	public com.database.Interface.IDatabase getDBase() {
		return _dbase;
	}

	public void setDBase(com.database.Interface.IDatabase _dbase) {
		this._dbase = _dbase;
	}
	
	

}
