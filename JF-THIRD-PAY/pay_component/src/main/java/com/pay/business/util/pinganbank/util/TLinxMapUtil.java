/**
 * @Filename: TLinxMapUtil.java
 * @Author：caiqf
 * @Date�?014-10-11
 */
package com.pay.business.util.pinganbank.util;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @Class: TLinxMapUtil.java
 * @Description: 
 * @Author：caiqf
 * @Date�?014-10-11
 */
@SuppressWarnings("all")
public class TLinxMapUtil {
	private Map map = new HashMap();
	private Set keySet = map.keySet();

	public Object get(String key) {
		return map.get(key);
	}

	public void put(String key, Object value) {
		map.put(key, value);
	}

	public void sort() {
		List list = new ArrayList(map.keySet());

//		Collections.sort(list, new Comparator() {
//			public int compare(Object a, Object b) {
//				return a.toString().compareTo(b.toString());
//			}
//		});

		this.keySet = new TreeSet(list);
	}

	public Set keySet() {
		return this.keySet;
	}

	/**
	 * @Description: ��Map��ֵ��һ������
	 * @param @param javabean ��ֵ�Ķ���
	 * @param @param m
	 * @return void
	 * @throws
	 */
	public static void mapToBean(Object javabean, Map<String, Object> m) {
		Method[] methods = javabean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {
				// �õ��÷����Ĳ�������
				Class<?>[] params = method.getParameterTypes();
				String field = method.getName();
				field = field.substring(field.indexOf("set") + 3);
				field = field.toLowerCase().charAt(0) + field.substring(1);
				Object value = m.get(field.toString());
				try {
					if (value != null && !"".equals(value)) {
						String pa = params[0].getName().toString();
						if (pa.equals("java.util.Date")) {
							value = new java.util.Date(
									((java.util.Date) value).getTime());
						} else if (pa.equals("java.lang.String")) {
							value = new java.lang.String(value.toString());
						} else if (pa.equals("java.lang.Integer")
								|| pa.equals("int")) {
							value = new java.lang.Integer(value.toString());
						} else if (pa.equals("java.lang.Long")) {
							value = new java.lang.Long(value.toString());
						} else if (pa.equals("java.lang.Double")) {
							value = new java.lang.Double(value.toString());

						} else if (pa.equals("java.lang.Float")) {
							value = new java.lang.Float(value.toString());

						} else if (pa.equals("java.lang.Short")) {
							value = new java.lang.Short(value.toString());

						} else if (pa.equals("java.lang.Byte")) {
							value = new java.lang.Byte(value.toString());

						} else if (pa.equals("java.lang.Boolean")) {
							value = new java.lang.Boolean(value.toString());

						}
						method.invoke(javabean, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
