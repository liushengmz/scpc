package com.database.Logical;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 基础Model驱动
 * 
 * @author Shotgun
 *
 */
public abstract class LightDataModel implements com.database.Interface.IUpdateInfo {

	/**
	 * 有变化的字段,通常表示需要更新到数据库
	 */
	ArrayList<String> updatedFields;

	/**
	 * 存储当前值为空的字段名
	 */
	ArrayList<String> NullFields;
	/**
	 * 存储未知的字段值
	 */
	HashMap<String, Object> extrFields;
	/**
	 * 是否正在填充数据
	 */
	Boolean _dataFilling = false;
	/**
	 * 是否要忽略相同的更新
	 */
	Boolean _IgnoreEquals = false;

	public LightDataModel() {
		updatedFields = new ArrayList<String>();
		extrFields = new HashMap<String, Object>();

	}

	private Method GetMethod(String mName) {
		Class<? extends LightDataModel> cls = this.getClass();
		Method[] ms = cls.getMethods();
		// System.out.println("get method:"+ mName);
		for (Method m : ms) {
			if (m.getName().equalsIgnoreCase(mName)) {
				return m;
			}
		}
		// System.out.println(".... not found");
		return null;
	}

	/**
	 * 通过反射获取成员成公用属性值
	 * 
	 * @param key
	 *            成员名
	 * @param obj
	 *            取得的成员值
	 * @return 是否成功
	 */
	public ReturnObj GetValue(String key) {
		Method func = this.GetMethod("get_" + key);
		ReturnObj ret = new ReturnObj();
		ret.IsSucces = false;
		if (func == null)
			return ret;
		try {
			ret.Value = func.invoke(this);
			ret.IsSucces = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 通过反射设置成员成公用属性值
	 * 
	 * @param key
	 *            成员名
	 * @param value
	 *            设置的成员值
	 * @return
	 */
	public Boolean SetValue(String key, Object value) {
		Method func = GetMethod("set_" + key);
		if (func == null)
			return false;
		boolean iToDefault = value == null;
		String targetType = null;
		if (iToDefault) {
			Type[] ts = func.getGenericParameterTypes();
			targetType = ts[0].getTypeName();

			if ("byte".equals(targetType) || "int".equals(targetType) || "long".equals(targetType)
					|| "short".equals(targetType)) {
				value = 0;
			} else if ("float".equals(targetType))
				value = 0f;
			else if ("double".equals(targetType))
				value = 0.0;
			else if ("boolean".equals(targetType))
				value = false;
			else if ("char".equals(targetType))
				value = (char) 0;
			else
				iToDefault = false;
		}

		try {
			// System.out.println("setValue " + key + "=" + value);
			func.invoke(this, value);
		} catch (

		Exception e)

		{
			if (iToDefault)
				System.out.println("set  key " + key + " default value fail,target type " + targetType + ",error:"
						+ e.getMessage());
			else {
				String valueType = "null";
				if (value != null) {
					valueType = value.getClass().getTypeName();
				}
				System.out.println(
						"set  key " + key + " value fail, value type:" + valueType + ",error:" + e.getMessage());
			}
			return false;
		}
		if (iToDefault)

		{
			SetNullFlag(key);
		}
		return true;

	}

	public Boolean IsNull(String Field) {
		if (this.NullFields == null)
			return false;
		return this.NullFields.contains(Field.toLowerCase());
	}

	/**
	 * 将字段值设置为null
	 */
	public void SetNull(String key) {// throws KeyException {
		// 引用类型可以此处将被标记为null,值类型无效
		if (!SetValue(key, null)) {
			System.out.println(String.format("Key: %s not Found", key));
			// throw new KeyException(String.format("Key: %s not Found", key));
		}
		SetNullFlag(key);
	}

	/**
	 * 设置段值空标记
	 * 
	 * @param key
	 * @throws KeyException
	 *             设置不允许为空的字段
	 */
	protected void SetNullFlag(String key) { // throws KeyException
		if (IsNull(key))
			return;
		String[] fs = GetNullableFields();
		Boolean Exist = false;
		for (String s : fs) {
			if (key.equalsIgnoreCase(s)) {
				Exist = true;
				break;
			}
		}

		if (!Exist && !this.GetDataFilling()) {
			System.out.println(String.format("Key:%s Non-Nullable ", key));
			// throw new KeyException(String.format("Key:%s Non-Nullable ",
			// key));
		}
		if (NullFields == null)
			NullFields = new ArrayList<String>();
		this.NullFields.add(key.toLowerCase());
	}

	/**
	 * 删除去字段null标记
	 */
	protected void RemoveNullFlag(String key) {
		if (NullFields == null)
			return;
		String lk = key.toLowerCase();
		if (NullFields.contains(lk))
			NullFields.remove(lk);
	}

	protected abstract String[] GetNullableFields();

	/**
	 * 是否正在填充数据
	 * 
	 * @return
	 */
	Boolean GetDataFilling() {
		return this._dataFilling;
	}

	/**
	 * 设置当前是否正在填充数据
	 */
	void SetDataFilling(Boolean value) {
		this._dataFilling = value;
	}

	protected void SetFieldHasUpdate(String field, Object oVal, Object nVal) {
		if (this.GetDataFilling())
			return;
		if (this.GetIgnoreEquals()) {
			if (oVal == null) {
				if (nVal == null)
					return;
			} else if (oVal.equals(nVal))
				return;
		}
		if (updatedFields.contains(field))
			return;
		updatedFields.add(field);
	}

	/**
	 * 赋值时检查是不是否与原值相同，相同时将忽略该操作(在执行存入数据库时，该字段不会有任何表现)
	 */
	public Boolean GetIgnoreEquals() {
		return this._IgnoreEquals;
	}

	/**
	 * 赋值时检查是不是否与原值相同，相同时将忽略该操作(在执行存入数据库时，该字段不会有任何表现)
	 */
	public void SetIgnoreEquals(Boolean value) {
		this._IgnoreEquals = value;
	}

	@Override
	public String[] GetUpateFields() {
		if (this.updatedFields == null || this.updatedFields.size() == 0)
			return null;
		return this.updatedFields.toArray(new String[0]);
	}

	@Override
	public Object GetValueByName(String fields) {
		ReturnObj ret = this.GetValue(fields);
		if (ret.IsSucces)
			return ret.Value;
		return false;
	}

	@Override
	public void SetUpdated(Object IdentifyValue) {
		this.updatedFields.clear();
		if (IdentifyValue == null)
			return;
		if (IdentifyValue instanceof Long) {
			IdentifyValue = ((Long) IdentifyValue).intValue();
			// System.out.println("long to int");
		}
		this.SetValue(this.IdentifyField(), IdentifyValue);
		// if (IdentifyValue is decimal)
		// this[IdentifyField] = Convert.ToInt32((decimal)IdentifyValue);
		// else
		// this[IdentifyField] = IdentifyValue;

	}

	@Override
	public String Schema() {
		return null;
	}

	public abstract String TableName();

	public abstract String IdentifyField();

}
