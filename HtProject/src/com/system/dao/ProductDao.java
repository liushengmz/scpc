package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.ProductModel;
import com.system.model.SpModel;
import com.system.util.StringUtil;

public class ProductDao {
	/**
	 * 产品列表
	 * @param pageIndex
	 * @param keyWord
	 * @return
	 */

	public Map<String, Object> loadProduct(int pageIndex,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 cp LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 pp ON cp.product_1_id=pp.id LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator op ON pp.operator_id=op.flag where 1=1";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (cp.name LIKE '%" + keyWord + "%' or pp.name LIKE '%"+ keyWord +"%' or op.name_cn like '%" + keyWord + "%'  )";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by cp.id desc ";
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));

		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " cp.id as productid,cp.*,op.name_cn,pp.name AS name_line ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ProductModel> list = new ArrayList<ProductModel>();
				while(rs.next())
				{
					ProductModel model = new ProductModel();
					model.setChildProductId(rs.getInt("productid"));
					model.setCnName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setProductLineName(StringUtil.getString(rs.getString("name_line"), ""));
					model.setChildProductName(StringUtil.getString(rs.getString("name"), ""));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	/**
	 * 产品线列表
	 * @param pageIndex
	 * @param keyWord
	 * @return
	 */
	public Map<String, Object> loadLineProduct(int pageIndex,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 pp LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator op ON pp.operator_id=op.flag where 1=1";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND ( pp.name LIKE '%"+ keyWord +"%' or op.name_cn like '%" + keyWord + "%'  )";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by pp.id desc ";
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));

		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " pp.id AS lineid,pp.operator_id,pp.name,op.id AS opid,op.flag,op.name_en,op.name_cn,op.bj_flag ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ProductModel> list = new ArrayList<ProductModel>();
				while(rs.next())
				{
					ProductModel model = new ProductModel();
					model.setProductLineId(rs.getInt("lineid"));
					model.setProductLineName(StringUtil.getString(rs.getString("name"), ""));
					model.setCnName(StringUtil.getString(rs.getString("name_cn"), ""));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	/**
	 * 根据ID获得产品实例
	 * @param id
	 * @return
	 */
	public ProductModel loadProductById(int id)
	{
		String sql = "select cp.id as productid,product_1_id,cp.name as productname,"
				+ "pp.id as productlineid,pp.operator_id,pp.name,"
				+ "op.id as operid,op.flag,op.name_en,op.name_cn,op.bj_flag "
				+ "from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 cp LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 pp ON cp.product_1_id=pp.id LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator op ON pp.operator_id=op.flag "
				+ "where cp.id = " + id +" ORDER BY cp.id";
		return (ProductModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					ProductModel model = new ProductModel();
					model.setChildProductId(rs.getInt("productid"));
					model.setChildLineId(rs.getInt("product_1_id"));
					model.setChildProductName(StringUtil.getString(rs.getString("productname"),""));
					model.setProductLineId(rs.getInt("productlineid"));
					model.setProductLineName(StringUtil.getString(rs.getString("name"), ""));
					model.setFlag(rs.getInt("flag"));
					model.setCnName(StringUtil.getString(rs.getString("name_cn"), ""));
					
					return model;
				}
				
				return null;
			}
		});
	}
	/**
	 * 根据ID获得产品线实例
	 * @param id
	 * @return
	 */
	public ProductModel loadLineProductById(int id)
	{
		String sql ="SELECT pp.id,pp.operator_id,pp.name,op.id as operid,op.flag,op.name_en,op.name_cn,op.bj_flag FROM tbl_product_1 pp LEFT JOIN tbl_operator op ON pp.operator_id=op.flag where pp.id="+id +" order by op.name_cn desc";
		return (ProductModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					ProductModel model = new ProductModel();
					model.setProductLineId(rs.getInt("id"));
					model.setProductLineName(StringUtil.getString(rs.getString("name"), ""));
					model.setFlag(rs.getInt("flag"));
					model.setCnName(StringUtil.getString(rs.getString("name_cn"), ""));
					
					return model;
				}
				
				return null;
			}
		});
	}
	/**
	 * 添加产品
	 * @param model
	 * @return
	 */
	public boolean addProduct(ProductModel model)
	{
		String sql="insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2(product_1_id,name) value('"+model.getChildLineId()+"','"+model.getChildProductName()+"')";
		return new JdbcControl().execute(sql);
	}
	/**
	 * 更新产品
	 * @param model
	 * @return
	 */
	public boolean updateProduct(ProductModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 set product_1_id = '"
				+ model.getChildLineId() + "',name = '"
				+ model.getChildProductName() + "' where id ="+model.getChildProductId();

		return new JdbcControl().execute(sql);
	}
	/**
	 * 添加产品线
	 * @param model
	 * @return
	 */
	public boolean addLineProduct(ProductModel model)
	{
		String sql="insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 (operator_id,name) value('"+model.getOperFlag()+"','"+model.getProductLineName()+"')";
		return new JdbcControl().execute(sql);
	}
	/**
	 * 更新产品线
	 * @param model
	 * @return
	 */
	public boolean updateLineProduct(ProductModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 set operator_id = '"
				+ model.getOperFlag() + "',name = '"
				+ model.getProductLineName() + "' where id ="+model.getProductLineId();

		return new JdbcControl().execute(sql);
	}
	/**
	 * 展示运营商
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductModel>loadProductList(){
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator order by id asc";
		return (List<ProductModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ProductModel> list = new ArrayList<ProductModel>();
				
				while(rs.next())
				{
					ProductModel model = new ProductModel();
					
					model.setOpratorId(rs.getInt("id"));
					model.setFlag(rs.getInt("flag"));
					model.setEnName(StringUtil.getString(rs.getString("name_en"), ""));
					model.setCnName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setBjflag(rs.getInt("bj_flag"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	/**
	 * 展示产品线
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductModel>loadProductLineList(){
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 order by id asc";
		return (List<ProductModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ProductModel> list = new ArrayList<ProductModel>();
				
				while(rs.next())
				{
					ProductModel model = new ProductModel();
					
					model.setProductLineId(rs.getInt("id"));
					model.setOperFlag(rs.getInt("operator_id"));
					model.setProductLineName(StringUtil.getString(rs.getString("name"), ""));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	/**
	 * 展示产品线
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductModel>loadProductLineListByFlag(int flag){
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 where operator_id="+flag+" order by id asc";
		return (List<ProductModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ProductModel> list = new ArrayList<ProductModel>();
				
				while(rs.next())
				{
					ProductModel model = new ProductModel();
					
					model.setProductLineId(rs.getInt("id"));
					model.setOperFlag(rs.getInt("operator_id"));
					model.setProductLineName(StringUtil.getString(rs.getString("name"), ""));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	/**
	 * 删除产品
	 */
	public boolean deleteProduct(int productId){
		String sql="delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 where id="+productId;
		return new JdbcControl().execute(sql);
	}
	
	/**
	 * 删除产品线
	 */
	public boolean deleteLineProduct(int lineId){
		boolean flag=true;
		//删除产品线下的产品
		JdbcControl jdbcControl=new JdbcControl();
		String delSql="delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 where product_1_id="+lineId;
		flag=jdbcControl.execute(delSql);
		//删除产品线
		if(flag){
			String sql="delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 where id="+lineId;
			flag=jdbcControl.execute(sql);
		}
		return flag;
	}
}
