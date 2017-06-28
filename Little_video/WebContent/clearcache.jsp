<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.system.cache.CacheConfigMgr"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	CacheConfigMgr.refreshAllCache();
%>