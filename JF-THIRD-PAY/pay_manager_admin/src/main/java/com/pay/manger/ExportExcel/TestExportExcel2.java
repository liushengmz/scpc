package com.pay.manger.ExportExcel;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.record.entity.Payv2BussWayDetail;
import com.pay.business.record.entity.Payv2DayCompanyClear;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 * 
 * @author zhoulibo
 * @version v1.0
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            byte[]表jpg格式的图片数据
 */
public class TestExportExcel2<T> {
	//
	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataMap
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void exportExcel(Map<String,Object> map,Map<Long, Payv2DayCompanyClear> dataMap,Map<String, List<Payv2DayCompanyClear>> dayCompanyMap,OutputStream out) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("对账单管理列表");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 20);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setWrapText(true);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("起始日期");
		row.createCell(1).setCellValue(map.get("createTimeBegin").toString());
		row.createCell(2).setCellValue("终止日期");
		row.createCell(3).setCellValue(map.get("createTimeEnd").toString());

		HSSFRow row1 = sheet.createRow(1);

		// 遍历集合数据，产生数据行
		Map<Long, Payv2BussWayDetail> detailMap = new HashMap<Long, Payv2BussWayDetail>();

		Iterator<Entry<Long, Payv2DayCompanyClear>> it = dataMap.entrySet().iterator();
		int index = 1;
		while (it.hasNext()) {
			Entry<Long, Payv2DayCompanyClear> entry = it.next();
			index++;

			row1 = sheet.createRow(index);
			row1.createCell(0).setCellValue("商户名称");
			index++;

			row1 = sheet.createRow(index);
			// 取到dataset中对象的公司名称对象值，放到此行列中
			row1.createCell(0).setCellValue(entry.getValue().getCompanyName());
			index++;

			row1 = sheet.createRow(index);
			row1.createCell(0).setCellValue("商户交易汇总清单");
			index++;

			row1 = sheet.createRow(index);
			row1.createCell(0).setCellValue("收款通道");
			row1.createCell(1).setCellValue("交易笔数");
			row1.createCell(2).setCellValue("交易金额");
			row1.createCell(3).setCellValue("退款笔数");
			row1.createCell(4).setCellValue("退款金额");
			row1.createCell(5).setCellValue("交易支付净额");
			row1.createCell(6).setCellValue("成本费率");
			row1.createCell(7).setCellValue("成本手续费");
			row1.createCell(8).setCellValue("商户签约费率");
			row1.createCell(9).setCellValue("商户手续费");
			row1.createCell(10).setCellValue("手续费利润");
			row1.createCell(11).setCellValue("结算金额");
			index++;

			// 取到dataset中对象的Map集合，将此Map遍历，将Map中的对象依次存放一行中，每遍历一次index加一
			detailMap.clear();
			detailMap = entry.getValue().getDetailMap();
			for (Payv2BussWayDetail value : detailMap.values()) {
				row1 = sheet.createRow(index);
				row1.createCell(0).setCellValue(value.getRateName());
				row1.createCell(1).setCellValue(value.getSuccessCount());
				row1.createCell(2).setCellValue(value.getSuccessMoney());
				row1.createCell(3).setCellValue(value.getRefundCount());
				row1.createCell(4).setCellValue(value.getRefundMoney());
				row1.createCell(5).setCellValue(value.getBussMoney());
				row1.createCell(6).setCellValue(value.getCostRate()+"‰");
				row1.createCell(7).setCellValue(value.getCostRateMoney());
				row1.createCell(8).setCellValue(value.getBussWayRate()+"‰");
				row1.createCell(9).setCellValue(value.getBussWayRateMoney());
				row1.createCell(10).setCellValue(value.getRateProfit());
				row1.createCell(11).setCellValue(value.getAccountsMonry());
				index++;
			}
			// 遍历完List后的合计一行从dataset中对象里的属性值获取
			row1 = sheet.createRow(index);
			row1.createCell(0).setCellValue("合计");
			row1.createCell(1).setCellValue(entry.getValue().getSuccessCount());
			row1.createCell(2).setCellValue(entry.getValue().getSuccessMoney());
			row1.createCell(3).setCellValue(entry.getValue().getRefundCount());
			row1.createCell(4).setCellValue(entry.getValue().getRefundMoney());
			row1.createCell(5).setCellValue(entry.getValue().getCleanpayMoney());
			row1.createCell(7).setCellValue(entry.getValue().getRateMoney() - entry.getValue().getRateProfit());
			row1.createCell(9).setCellValue(entry.getValue().getRateMoney());
			row1.createCell(10).setCellValue(entry.getValue().getRateProfit());
			row1.createCell(11).setCellValue(entry.getValue().getAccountsMonry());
			index++;
			row1 = sheet.createRow(index);
		}

		// 遍历商户Map，每个Map中的List生成一个表格

		for (Entry<String, List<Payv2DayCompanyClear>> entry : dayCompanyMap
				.entrySet()) {
			// 生成一个表格
			HSSFSheet sheet1 = workbook.createSheet(entry.getKey());
			// 设置表格默认列宽度为15个字节
			sheet1.setDefaultColumnWidth((short) 20);
			// 生成一个样式
			HSSFCellStyle style1 = workbook.createCellStyle();
			// 设置这些样式
			style1.setWrapText(true);
			style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 产生表格标题行
			HSSFRow row2 = sheet1.createRow(0);
			row2.createCell(0).setCellValue("起始日期");
			row2.createCell(1).setCellValue(map.get("createTimeBegin").toString());
			row2.createCell(2).setCellValue("终止日期");
			row2.createCell(3).setCellValue(map.get("createTimeEnd").toString());

			HSSFRow row3 = sheet1.createRow(1);
			row3.createCell(0).setCellValue("商户名称");
			row3.createCell(1).setCellValue(entry.getKey());

//			HSSFRow row4 = sheet1.createRow(2);
//			row4.createCell(0).setCellValue("账户名称");
//			row4.createCell(1).setCellValue("账户类型");
//			row4.createCell(2).setCellValue("开户银行");
//			row4.createCell(3).setCellValue("开户账号");
//			
//			HSSFRow row5 = sheet1.createRow(3);
//			row4.createCell(0).setCellValue("账户名称");
//			row4.createCell(1).setCellValue("账户类型");
//			row4.createCell(2).setCellValue("开户银行");
//			row4.createCell(3).setCellValue("开户账号");
			
			
			
			
			// 将对象中的账单详情集合List取出，取出后遍历该数组，将数组中对象的值依次写入表格中
			List<Payv2DayCompanyClear> dayCompanyClearList = entry.getValue();

			int index1 = 3;
			for (Payv2DayCompanyClear payv2DayCompanyClear : dayCompanyClearList) {
				index1++;
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				row1 = sheet1.createRow(index1);
				row1.createCell(0).setCellValue("账单时间");
				row1.createCell(1).setCellValue(sdf.format(payv2DayCompanyClear.getClearTime()));
				index1++;

				row1 = sheet1.createRow(index1);
				row1.createCell(0).setCellValue("通道名称");
				row1.createCell(1).setCellValue("交易笔数");
				row1.createCell(2).setCellValue("交易金额");
				row1.createCell(3).setCellValue("退款笔数");
				row1.createCell(4).setCellValue("退款金额");
				row1.createCell(5).setCellValue("交易支付净额");
				row1.createCell(6).setCellValue("成本费率");
				row1.createCell(7).setCellValue("成本手续费");
				row1.createCell(8).setCellValue("商户签约费率");
				row1.createCell(9).setCellValue("商户手续费");
				row1.createCell(10).setCellValue("手续费利润");
				row1.createCell(11).setCellValue("结算金额");
				
				
				index1++;
				Map<String, Object> dataMap1 = payv2DayCompanyClear.getDetailMap();
				List<Payv2BussWayDetail> bussDetailList = (List<Payv2BussWayDetail>) dataMap1.get("bussDetailList");
				Payv2BussCompany payv2BussCompany = (Payv2BussCompany) dataMap1.get("payv2BussCompany");
				//遍历日账单详情数组，将对象中的值依次存放到表格中
				for (Payv2BussWayDetail payv2BussWayDetail : bussDetailList) {
					row1 = sheet1.createRow(index1);
					row1.createCell(0).setCellValue(payv2BussWayDetail.getRateName());
					row1.createCell(1).setCellValue(payv2BussWayDetail.getSuccessCount());
					row1.createCell(2).setCellValue(payv2BussWayDetail.getSuccessMoney());
					row1.createCell(3).setCellValue(payv2BussWayDetail.getRefundCount());
					row1.createCell(4).setCellValue(payv2BussWayDetail.getRefundMoney());
					row1.createCell(5).setCellValue(payv2BussWayDetail.getBussMoney());
					row1.createCell(6).setCellValue(payv2BussWayDetail.getCostRate()+"‰");
					row1.createCell(7).setCellValue(payv2BussWayDetail.getCostRateMoney());
					row1.createCell(8).setCellValue(payv2BussWayDetail.getBussWayRate()+"‰");
					row1.createCell(9).setCellValue(payv2BussWayDetail.getBussWayRateMoney());//商户手续费
					row1.createCell(10).setCellValue(payv2BussWayDetail.getRateProfit());//手续费利润
					row1.createCell(11).setCellValue(payv2BussWayDetail.getAccountsMonry());
//					
//					System.out.println(payv2BussCompany.getAccountName()+payv2BussCompany.getAccountBank()+payv2BussCompany.getAccountCard());
//					row1.createCell(12).setCellValue(payv2BussCompany.getAccountName());
////					row1.createCell(13).setCellValue(payv2BussCompany.getAccountType());
//					row1.createCell(14).setCellValue(payv2BussCompany.getAccountBank());
//					row1.createCell(15).setCellValue(payv2BussCompany.getAccountCard());
					index1++;
				}
				// 合计这一行在日账单对象中获取
				row1 = sheet1.createRow(index1);
				row1.createCell(0).setCellValue("合计");
				row1.createCell(1).setCellValue(payv2DayCompanyClear.getSuccessCount());
				row1.createCell(2).setCellValue(payv2DayCompanyClear.getSuccessMoney());
				row1.createCell(3).setCellValue(payv2DayCompanyClear.getRefundCount());
				row1.createCell(4).setCellValue(payv2DayCompanyClear.getRefundMoney());
				row1.createCell(5).setCellValue(payv2DayCompanyClear.getCleanpayMoney());
				row1.createCell(7).setCellValue(payv2DayCompanyClear.getRateMoney() - payv2DayCompanyClear.getRateProfit());
				row1.createCell(9).setCellValue(payv2DayCompanyClear.getRateMoney());//商户手续费合计
				row1.createCell(10).setCellValue(payv2DayCompanyClear.getRateProfit());//手续费利润合计
				row1.createCell(11).setCellValue(payv2DayCompanyClear.getAccountsMonry());
				index1++;
				
				row1 = sheet1.createRow(index1);
			}

		}

		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}