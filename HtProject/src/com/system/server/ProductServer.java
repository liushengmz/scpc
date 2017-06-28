package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.ProductDao;
import com.system.dao.SpDao;
import com.system.model.ProductModel;
import com.system.model.SpModel;

public class ProductServer {

	/**
	 * 产品列表
	 * @param pageIndex
	 * @param keyWord
	 * @return
	 */
	public Map<String, Object> loadProduct(int pageIndex,String keyWord)
	{
		return new ProductDao().loadProduct(pageIndex, keyWord);
	}
	/**
	 * 产品线列表
	 * @param pageIndex
	 * @param keyWord
	 * @return
	 */
	public Map<String, Object> loadLineProduct(int pageIndex,String keyWord)
	{
		return new ProductDao().loadLineProduct(pageIndex, keyWord);
	}
	/**
	 * 根据ID获取产品实例
	 * @param id
	 * @return
	 */
	public ProductModel loadProductById(int id)
	{
		return new ProductDao().loadProductById(id);
	}
	/**
	 * 根据ID获取产品线实例
	 * @param id
	 * @return
	 */
	public ProductModel loadLineProductById(int id)
	{
	
		return new ProductDao().loadLineProductById(id);
	}
	/**
	 * 添加产品
	 * @param model
	 * @return
	 */
	public boolean addProduct(ProductModel model)
	{
		return new ProductDao().addProduct(model);
	}
	/**
	 * 更新产品
	 * @param model
	 * @return
	 */
	public boolean updateProduct(ProductModel model)
	{
		return new ProductDao().updateProduct(model);
	}
	/**
	 * 删除产品
	 * @param productId
	 * @return
	 */
	public boolean deleteProduct(int productId){
		return new ProductDao().deleteProduct(productId);
	}
	/**
	 * 运营商List
	 * @return
	 */
	public List<ProductModel> loadProductList(){
		return new ProductDao().loadProductList();
	}
	/**
	 * 产品线List
	 * @return
	 */
	public List<ProductModel> loadProductLineList(){
		return new ProductDao().loadProductLineList();
	}
	/**
	 * 添加产品线
	 * @param model
	 * @return
	 */
	public boolean addLineProduct(ProductModel model)
	{
		return new ProductDao().addLineProduct(model);
	}
	/**
	 * 更新产品线
	 * @param model
	 * @return
	 */
	public boolean updateLineProduct(ProductModel model)
	{
		return new ProductDao().updateLineProduct(model);
	}
	/**
	 * 删除产品线
	 * @param productId
	 * @return
	 */
	public boolean deleteLineProduct(int lineId){
		return new ProductDao().deleteLineProduct(lineId);
	}
	/**
	 * 
	 */
	public List<ProductModel> loadProductLineListByFlag(int id){
		return new ProductDao().loadProductLineListByFlag(id);
	}

}
