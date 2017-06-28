<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="org.demo.service.ServiceReceive"%>
<%@page import="org.demo.service.HttpSend"%>
<%@page import="org.demo.service.ThreadPool"%>
<%@page import="org.demo.service.ScaleService"%>
<%@page import="org.demo.info.Receive"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.Random"%>
<%
  // mm要在两个地方进行配置
  String serviceIdPrefix = "demo-";
  int currentScale = 0;
  String phone = request.getParameter("mobile");
  String destAddr = "";//request.getParameter("destaddr");
  String message = "";//request.getParameter("sms_msg");
  String linkId = request.getParameter("tnsid");
  String ret = request.getParameter("ret");
  String serviceId = request.getParameter("cmd");
  String cmdId = request.getParameter("cmdId");
  message = request.getParameter("cmd");
  String region = request.getParameter("region");
  ServiceReceive serviceService = new ServiceReceive();
  //System.out.println(tmpMessage.toString() + " " + java.net.URLEncoder.encode(tmpMessage.toString(), "utf-8"));
  //check user name 
  Receive receive = new Receive();
  // special proc
  if (phone.length() == 11) {
    receive.setPhone(Long.parseLong(phone));
  } else {
    receive.setDestAddr(phone);
  }

  //receive.setDestAddr(destAddr);
  receive.setMessage(message);
  receive.setLinkId(linkId);
  receive.setServiceId(serviceIdPrefix + serviceId);
  receive.setFromIp(request.getRemoteAddr());
  receive.setStatusReport(Integer.parseInt(ret));
  receive.setMsgType("mr");
  Random rnd = new Random();

  currentScale = ScaleService.getServiceIdScale(serviceIdPrefix + serviceId);

  if (rnd.nextInt(100) < currentScale) {
    receive.setSendStatus(-1);
  }
  long id = serviceService.addReceive(receive);
  //System.out.println(id);
  if (receive.getPhone() == 0) {
    receive.setPhone(13800138000L);
  }
  if (id > 0) {
    receive.setId(id);
    response.getWriter().print("OK");
    if (receive.getSendStatus() == 0 && ret.equals("0")) {
      HttpSend httpSend = new HttpSend();
      httpSend.setSendUrl("http://target.com:port/xxx");
      httpSend.setReceive(receive);
      ThreadPool.mThreadPool.execute(httpSend);
    }
  }
  java.util.Enumeration headerNames = request.getHeaderNames();
  while (headerNames.hasMoreElements()) {
    String headerKey = (String) headerNames.nextElement();
    System.out.println(headerKey + ":" + request.getHeader(headerKey));
  }
%>