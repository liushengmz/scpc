<%@page import="java.io.InputStream"%>
<%@page import="org.apache.log4j.Logger"%>
<%
String info = null;
int len = 0;
int temp = 0;
InputStream is = request.getInputStream();
byte[] b = new byte[1000000];
while ((temp = is.read()) != -1) {
  b[len] = (byte) temp;
  len++;
}
is.close();
info = new String(b, 0, len, "utf-8");
System.out.println("####notice:\n" + info);
System.out.println("####end:");
Logger LOG = Logger.getLogger(this.getClass());
%>