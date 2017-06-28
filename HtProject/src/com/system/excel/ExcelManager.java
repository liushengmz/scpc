package com.system.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileOutputStream;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.system.model.ExportDetailModel;
import com.system.model.SettleAccountModel;
import com.system.util.ConfigManager;
import com.system.model.CpExportDetailModel;


public class ExcelManager
{
	Logger log = Logger.getLogger(ExcelManager.class);
	
	private Map<String, HSSFCellStyle> createStyles (HSSFWorkbook workbook)
	{
		Map<String, HSSFCellStyle> map = new HashMap<String, HSSFCellStyle>();
		
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		map.put("BASE_STYLE", style);
		
		HSSFCellStyle styleFormat = workbook.createCellStyle();
		styleFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleFormat.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleFormat.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleFormat.setDataFormat(workbook.createDataFormat().getFormat("0.00")); 
		
		map.put("FORMAT_STYLE", styleFormat);
		
		return map;
	}
	private Map<String, HSSFCellStyle> createBillStyles (HSSFWorkbook workbook)
	{
		Map<String, HSSFCellStyle> map = new HashMap<String, HSSFCellStyle>();
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		map.put("BASE_STYLE", style);
		
		HSSFCellStyle styletwo = workbook.createCellStyle();
		styletwo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styletwo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styletwo.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styletwo.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styletwo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styletwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styletwo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styletwo.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE	.index);
		map.put("BASE_STYLE_TWO", styletwo);
		
		
		HSSFCellStyle styleFormat = workbook.createCellStyle();
		styleFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleFormat.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleFormat.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleFormat.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
		styleFormat.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleFormat.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		map.put("FORMAT_STYLE", styleFormat);
		
		HSSFCellStyle styleFormattwo = workbook.createCellStyle();
		styleFormattwo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleFormattwo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleFormattwo.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleFormattwo.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleFormattwo.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleFormattwo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleFormattwo.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
		styleFormattwo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleFormattwo.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
		map.put("FORMAT_STYLE_TWO", styleFormattwo);
		
		HSSFCellStyle styleFormatRate = workbook.createCellStyle();
		styleFormatRate.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleFormatRate.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleFormatRate.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleFormatRate.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleFormatRate.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleFormatRate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleFormatRate.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
		styleFormatRate.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleFormatRate.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		map.put("FORMAT_STYLE_RATE", styleFormatRate);
		
		HSSFCellStyle styleFormatNum = workbook.createCellStyle();
		styleFormatNum.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleFormatNum.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleFormatNum.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleFormatNum.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleFormatNum.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleFormatNum.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleFormatNum.setDataFormat(workbook.createDataFormat().getFormat("0"));
		styleFormatNum.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleFormatNum.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		map.put("FORMAT_STYLE_NUM", styleFormatNum);
		
		HSSFCellStyle styleFormatPer = workbook.createCellStyle();
		styleFormatPer.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleFormatPer.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleFormatPer.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleFormatPer.setBorderTop(HSSFCellStyle.BORDER_THIN);
		styleFormatPer.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleFormatPer.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleFormatPer.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
		styleFormatPer.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleFormatPer.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		map.put("FORMAT_STYLE_PER", styleFormatPer);
		return map;
	}
	/**
	 * 
	 * @param dateType 结算类型
	 * @param date 日期
	 * @param channelName 渠道或是上游名称
	 * @param list 数据
	 * @param demoPath EXECL路径 
	 * @param os 数据流
	 */
	public void writeSettleAccountToExcel(String dateType, String date,
			String channelName, List<SettleAccountModel> list, String demoPath,OutputStream os)
	{
		InputStream is = null;
		
		try
		{
			is = new FileInputStream(demoPath);
			HSSFWorkbook book =  new HSSFWorkbook(is);
			HSSFSheet sheet = book.getSheetAt(0);
			
			int tempSpTroneNameLength = 0;
			int maxSpTroneNameLength = 0;
			
			int tempProductLineLength = 0;
			int maxProductLineLength = 0;
			
			Map<String, HSSFCellStyle> mapStyle = createStyles(book);
			
			SettleAccountModel model = null;
			
			for(int i=0; i<list.size(); i++)
			{
				model = list.get(i);
				
				HSSFRow row = sheet.createRow(i+3);
				row.createCell(1).setCellStyle(mapStyle.get("BASE_STYLE"));
				row.createCell(2).setCellStyle(mapStyle.get("BASE_STYLE"));
				row.createCell(3).setCellStyle(mapStyle.get("BASE_STYLE"));
				
				HSSFCell cell = row.createCell(4);
				cell.setCellStyle(mapStyle.get("BASE_STYLE"));
				cell.setCellValue(model.getOperatorName());
				
				cell = row.createCell(5);
				cell.setCellStyle(mapStyle.get("BASE_STYLE"));
				cell.setCellValue(model.getSpTroneName());
				
				cell = row.createCell(6);
				cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
				cell.setCellValue(model.getAmount());
				
				//核减结算款
				cell = row.createCell(7);
				cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
				
				//核减信息费要转为核减结算款
				if(model.getReduceType()==0)
				{
					cell.setCellValue(model.getReduceAmount()*model.getJiesuanlv());
				}
				else if(model.getReduceType()==1)
				{
					cell.setCellValue(model.getReduceAmount());
				}
				
				
				cell = row.createCell(8);
				cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
				cell.setCellValue(model.getJiesuanlv());
				
				cell = row.createCell(9);
				cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
				
				cell.setCellFormula("G"+ (4+i) +"*I" + (4+i) + "-H"+ (4+i));
				
				tempSpTroneNameLength = model.getSpTroneName().getBytes("GBK").length;
				tempProductLineLength = model.getOperatorName().getBytes("GBK").length;
				
				if(tempSpTroneNameLength > maxSpTroneNameLength)
					maxSpTroneNameLength = tempSpTroneNameLength;
				
				if(tempProductLineLength > maxProductLineLength)
					maxProductLineLength = tempProductLineLength;
			}
			
			//结算方式宽度
			sheet.setColumnWidth(1, 9*256);
			//结算期间宽度
			sheet.setColumnWidth(2, 18*256);
			//渠道名称宽度
			sheet.setColumnWidth(3, (channelName.getBytes("GBK").length+1)*256);
			//运营商宽度
			sheet.setColumnWidth(4, (maxProductLineLength+1)*256);
			//产品名称宽度
			sheet.setColumnWidth(5, (maxSpTroneNameLength+1)*256);
			//信息费宽度
			sheet.setColumnWidth(6, 13*256);
			//特殊信息费宽度***2016.08.04加入
			sheet.setColumnWidth(7, 13*256);
			//结算价宽度
			sheet.setColumnWidth(8, 18*256);
			//渠道酬金宽度
			sheet.setColumnWidth(9, 13*256);
			
			sheet.addMergedRegion(new CellRangeAddress(3,2+list.size(),1,1));
			sheet.getRow(3).getCell(1).setCellValue(dateType);
			sheet.getRow(3).getCell(1).setCellStyle(mapStyle.get("BASE_STYLE"));
			
			sheet.addMergedRegion(new CellRangeAddress(3,2+list.size(),2,2));
			sheet.getRow(3).getCell(2).setCellValue(date);
			sheet.getRow(3).getCell(2).setCellStyle(mapStyle.get("BASE_STYLE"));
			
			sheet.addMergedRegion(new CellRangeAddress(3,2+list.size(),3,3));
			sheet.getRow(3).getCell(3).setCellValue(channelName);
			sheet.getRow(3).getCell(3).setCellStyle(mapStyle.get("BASE_STYLE"));
			
			HSSFRow row = sheet.createRow(3+list.size());
			for(int i=0; i<9; i++)
				row.createCell(i+1).setCellStyle(mapStyle.get("BASE_STYLE"));
			
			sheet.addMergedRegion(new CellRangeAddress(3+list.size(),3+list.size(),1,8));
			row.getCell(1).setCellValue("合计");
			row.getCell(9).setCellFormula("SUM(J" + 3 + ":J" + (list.size() + 3) + ")");
			row.getCell(9).setCellStyle(mapStyle.get("FORMAT_STYLE"));
			
			sheet.setForceFormulaRecalculation(true);
			
			book.write(os);
			
			log.info("Export Excel " + channelName + "," + date + " finish" );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try{ if(is!=null)is.close(); }catch(Exception ex){}
		}
	}
	
	/**
	 * 导出账单数据
	 * @param date
	 * @param list
	 * @param demoPath
	 * @param os
	 */
	@SuppressWarnings("unchecked")
	public void writeBillDataToExcel(String date,
			 Map<Integer,Map<String,Object>> map,String demoPath,OutputStream os)
	{
			InputStream is = null;
		
		try
		{
			is = new FileInputStream(demoPath);
			HSSFWorkbook book =  new HSSFWorkbook(is);
			HSSFSheet sheet = book.getSheetAt(0);
			Map<String, HSSFCellStyle> mapStyle = createBillStyles(book);
			
			int i=2;//自增行
			int lineRow=0;//合并初始行
			
		 	List<ExportDetailModel> delist=null;
		 	int j=0;
		 	Integer entryIndex=1;
			 for (Map.Entry<Integer, Map<String,Object>> entry : map.entrySet()) {
				 	int key=entry.getKey();
				 	Map<String,Object> bean=map.get(key);
				 	delist=(List<ExportDetailModel>)bean.get("list");
				 	lineRow=i;
				 	for(int k=0;k<delist.size();k++){
				 	HSSFRow row = sheet.createRow(i);
					HSSFCell cell = row.createCell(0);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
					cell.setCellValue(Float.parseFloat((j+1)+""));
					sheet.setColumnWidth(0, 10 * 256);

					
					cell = row.createCell(1);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("billMonth"));
					sheet.setColumnWidth(1, 10 * 256);
					
					cell = row.createCell(2);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("jsName"));
					sheet.setColumnWidth(2, 10 * 256);
					
					cell = row.createCell(3);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("startDate"));
					sheet.setColumnWidth(3, 15 * 256);
					
					cell = row.createCell(4);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("endDate"));
					sheet.setColumnWidth(4, 15 * 256);

					cell = row.createCell(5);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					sheet.setColumnWidth(5, 10 * 256);

					cell = row.createCell(6);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					sheet.setColumnWidth(6, 10 * 256);

					cell = row.createCell(7);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("nickName"));
					sheet.setColumnWidth(7, 10 * 256);
					cell = row.createCell(8);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell = row.createCell(9);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("spFullNam"));
					sheet.setColumnWidth(9, 30 * 256);

					cell = row.createCell(10);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue(delist.get(k).getProductName());
					sheet.setColumnWidth(10, 20 * 256);
					cell = row.createCell(11);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue(delist.get(k).getSpTroneName());
					sheet.setColumnWidth(11, 30 * 256);

					cell = row.createCell(12);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Float.parseFloat(delist.get(k).getAmount()));
					sheet.setColumnWidth(12, 15 * 256);
					cell = row.createCell(13);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_PER"));
					cell.setCellValue(Float.parseFloat(delist.get(k).getRate()));
					sheet.setColumnWidth(13, 15 * 256);
					cell = row.createCell(14);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
					cell.setCellValue(Float.parseFloat(delist.get(k).getSpTroneBillAmount()));
					sheet.setColumnWidth(14, 15 * 256);
					cell = row.createCell(15);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					
					//Update By Andy.Chen 2016.10.27
					//cell.setCellValue(Float.parseFloat(delist.get(k).getReduceAmount()));
					cell.setCellValue(Float.parseFloat(delist.get(k).getReduceDataAmount()));
					
					sheet.setColumnWidth(15, 15 * 256);
					
					cell = row.createCell(16);
					//Update By Andy.Chen 2016.10.27
					//cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					//cell.setCellValue(delist.get(k).getReduceType()==1?"结算款":"信息费");
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					cell.setCellValue(Float.parseFloat(delist.get(k).getReduceMoneyAmount()));
					
					sheet.setColumnWidth(16, 15 * 256);
					cell = row.createCell(17);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					cell.setCellValue(Float.parseFloat(delist.get(k).getActureAmount()));
					sheet.setColumnWidth(17, 15 * 256);
					cell = row.createCell(18);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					cell.setCellValue(Float.parseFloat((String)entry.getValue().get("preBilling")));
					sheet.setColumnWidth(18, 15 * 256);
					cell = row.createCell(19);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("billingDate"));
					sheet.setColumnWidth(19, 15 * 256);
					cell = row.createCell(20);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					cell.setCellValue(Float.parseFloat((String)entry.getValue().get("kaipiaoAmount")));
					sheet.setColumnWidth(20, 15 * 256);
					cell = row.createCell(21);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("applyKaipiaoDate"));
					sheet.setColumnWidth(21, 15 * 256);

					cell = row.createCell(22);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("kaipiaoDate"));
					sheet.setColumnWidth(22, 15 * 256);
					cell = row.createCell(23);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					cell.setCellValue(Float.parseFloat((String)entry.getValue().get("actureBilling")));
					sheet.setColumnWidth(23, 15 * 256);
					cell = row.createCell(24);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("payTime"));
					sheet.setColumnWidth(24, 15 * 256);
					cell = row.createCell(25);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("statusName"));
					sheet.setColumnWidth(25, 15 * 256);
					i++;
					j++;
					if(k==delist.size()-1){
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,0,0));
						//HSSFRow hssRow=sheet.getRow(lineRow+k);
						HSSFRow hssRow=sheet.getRow(lineRow);

						HSSFCell hssCell=hssRow.getCell(0);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_NUM"));
						
						hssCell.setCellValue(entryIndex);
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,1,1));
						hssCell=hssRow.getCell(1);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_NUM"));
						hssCell.setCellValue(Float.parseFloat((String)entry.getValue().get("billMonth")));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,2,2));
						hssCell=hssRow.getCell(2);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("jsName"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,3,3));
						hssCell=hssRow.getCell(3);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("startDate"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,4,4));
						hssCell=hssRow.getCell(4);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("endDate"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,5,5));
						hssCell=hssRow.getCell(5);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,6,6));
						hssCell=hssRow.getCell(6);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,7,7));
						hssCell=hssRow.getCell(7);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("nickName"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,8,8));
						hssCell=hssRow.getCell(8);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,9,9));
						hssCell=hssRow.getCell(9);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("spFullNam"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,18,18));
						hssCell=hssRow.getCell(18);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
						hssCell.setCellValue(Float.parseFloat((String)entry.getValue().get("preBilling")));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,19,19));
						hssCell=hssRow.getCell(19);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("billingDate"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,20,20));
						hssCell=hssRow.getCell(20);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
						hssCell.setCellValue(Float.parseFloat((String)entry.getValue().get("kaipiaoAmount")));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,21,21));
						hssCell=hssRow.getCell(21);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("applyKaipiaoDate"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,22,22));
						hssCell=hssRow.getCell(22);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("kaipiaoDate"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,23,23));
						hssCell=hssRow.getCell(23);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
						hssCell.setCellValue(Float.parseFloat((String)entry.getValue().get("actureBilling")));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,24,24));
						hssCell=hssRow.getCell(24);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("payTime"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,25,25));
						hssCell=hssRow.getCell(25);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("statusName"));
						entryIndex+=1;
					}
				}				

				 	
			 }
			 sheet.setForceFormulaRecalculation(true);
			 
			 book.write(os);
			
//			log.info("Export Excel " + channelName + "," + date + " finish" );
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try{ if(is!=null)is.close(); }catch(Exception ex){}
		}
	}
	
	/**
	 * 导出账单数据
	 * @param date
	 * @param list
	 * @param demoPath
	 * @param os
	 */
	@SuppressWarnings("unchecked")
	public void writeCpBillDataToExcel(String date,
			 Map<Integer,Map<String,Object>> map,String demoPath,OutputStream os)
	{
			InputStream is = null;
		
		try
		{
			is = new FileInputStream(demoPath);
			HSSFWorkbook book =  new HSSFWorkbook(is);
			HSSFSheet sheet = book.getSheetAt(0);
			Map<String, HSSFCellStyle> mapStyle = createBillStyles(book);
			
			int i=2;//自增行
			int lineRow=0;//合并初始行
			
		 	List<CpExportDetailModel> delist=null;
		 	int j=0;
		 	Integer entryIndex=1;
			 for (Map.Entry<Integer, Map<String,Object>> entry : map.entrySet()) {
				 	int key=entry.getKey();
				 	Map<String,Object> bean=map.get(key);
				 	delist=(List<CpExportDetailModel>)bean.get("list");
				 	lineRow=i;
				 	for(int k=0;k<delist.size();k++){
				 	HSSFRow row = sheet.createRow(i);
					HSSFCell cell = row.createCell(0);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
					cell.setCellValue(Float.parseFloat((j+1)+""));
					sheet.setColumnWidth(0, 10 * 256);

					
					cell = row.createCell(1);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("billMonth"));
					sheet.setColumnWidth(1, 10 * 256);
					
					cell = row.createCell(2);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("jsName"));
					sheet.setColumnWidth(2, 10 * 256);
					
					cell = row.createCell(3);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("startDate"));
					sheet.setColumnWidth(3, 15 * 256);
					
					cell = row.createCell(4);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("endDate"));
					sheet.setColumnWidth(4, 15 * 256);

					cell = row.createCell(5);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					sheet.setColumnWidth(5, 10 * 256);

					cell = row.createCell(6);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					sheet.setColumnWidth(6, 10 * 256);

					cell = row.createCell(7);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("nickName"));
					sheet.setColumnWidth(7, 10 * 256);
					cell = row.createCell(8);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell = row.createCell(9);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue((String)entry.getValue().get("cpFullNam"));
					sheet.setColumnWidth(9, 30 * 256);

					cell = row.createCell(10);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue(delist.get(k).getProductName());
					sheet.setColumnWidth(10, 20 * 256);
					cell = row.createCell(11);
					cell.setCellStyle(mapStyle.get("BASE_STYLE"));
					cell.setCellValue(delist.get(k).getSpTroneName());
					sheet.setColumnWidth(11, 30 * 256);

					cell = row.createCell(12);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(delist.get(k).getAmount());
					sheet.setColumnWidth(12, 15 * 256);
					cell = row.createCell(13);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_PER"));
					cell.setCellValue(delist.get(k).getRate());
					sheet.setColumnWidth(13, 15 * 256);
//					cell = row.createCell(14);
//					cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
//					cell.setCellValue(delist.get(k).getSpTroneBillAmount());
//					sheet.setColumnWidth(14, 15 * 256);
					cell = row.createCell(14);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					
					cell.setCellValue(delist.get(k).getReduceAmount());
					
					sheet.setColumnWidth(14, 15 * 256);
					
					cell = row.createCell(15);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue(delist.get(k).getReduceType()==1?"结算款":"信息费");
					
					
					sheet.setColumnWidth(15, 15 * 256);
					cell = row.createCell(16);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					cell.setCellValue(delist.get(k).getActureAmount());
					sheet.setColumnWidth(16, 15 * 256);
					cell = row.createCell(17);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					cell.setCellValue((Float)entry.getValue().get("preBilling"));
					sheet.setColumnWidth(17, 15 * 256);
					cell = row.createCell(18);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("billingDate"));
					sheet.setColumnWidth(18, 15 * 256);
					cell = row.createCell(19);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					cell.setCellValue((Float)entry.getValue().get("kaipiaoAmount"));
					sheet.setColumnWidth(19, 15 * 256);
					cell = row.createCell(20);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("getbillDate"));
					sheet.setColumnWidth(20, 15 * 256);

					cell = row.createCell(21);
					cell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
					cell.setCellValue(((Float)entry.getValue().get("actureBilling")));
					sheet.setColumnWidth(21, 15 * 256);
					
					cell = row.createCell(22);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("applyPayBillDate"));
					sheet.setColumnWidth(22, 15 * 256);
					
					cell = row.createCell(23);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("payTime"));
					sheet.setColumnWidth(23, 15 * 256);
					cell = row.createCell(24);
					cell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
					cell.setCellValue((String)entry.getValue().get("statusName"));
					sheet.setColumnWidth(24, 15 * 256);
					i++;
					j++;
					if(k==delist.size()-1){
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,0,0));
						//HSSFRow hssRow=sheet.getRow(lineRow+k);
						HSSFRow hssRow=sheet.getRow(lineRow);

						HSSFCell hssCell=hssRow.getCell(0);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_NUM"));
						
						hssCell.setCellValue(entryIndex);
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,1,1));
						hssCell=hssRow.getCell(1);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_NUM"));
						hssCell.setCellValue(Float.parseFloat((String)entry.getValue().get("billMonth")));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,2,2));
						hssCell=hssRow.getCell(2);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("jsName"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,3,3));
						hssCell=hssRow.getCell(3);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("startDate"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,4,4));
						hssCell=hssRow.getCell(4);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("endDate"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,5,5));
						hssCell=hssRow.getCell(5);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,6,6));
						hssCell=hssRow.getCell(6);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,7,7));
						hssCell=hssRow.getCell(7);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("nickName"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,8,8));
						hssCell=hssRow.getCell(8);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,9,9));
						hssCell=hssRow.getCell(9);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE"));
						hssCell.setCellValue((String)entry.getValue().get("cpFullNam"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,17,17));
						hssCell=hssRow.getCell(17);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
						hssCell.setCellValue((Float)entry.getValue().get("preBilling"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,18,18));
						hssCell=hssRow.getCell(18);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("billingDate"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,19,19));
						hssCell=hssRow.getCell(19);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
						hssCell.setCellValue((Float)entry.getValue().get("kaipiaoAmount"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,20,20));
						hssCell=hssRow.getCell(20);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("getbillDate"));
						
						
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,21,21));
						hssCell=hssRow.getCell(21);
						hssCell.setCellStyle(mapStyle.get("FORMAT_STYLE_TWO"));
						hssCell.setCellValue((Float)entry.getValue().get("actureBilling"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,22,22));
						hssCell=hssRow.getCell(22);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("applyPayBillDate"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,23,23));
						hssCell=hssRow.getCell(23);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("payTime"));
						
						sheet.addMergedRegion(new CellRangeAddress(lineRow,lineRow+k,24,24));
						hssCell=hssRow.getCell(24);
						hssCell.setCellStyle(mapStyle.get("BASE_STYLE_TWO"));
						hssCell.setCellValue((String)entry.getValue().get("statusName"));
						entryIndex+=1;
					}
				}				

				 	
			 }
			 sheet.setForceFormulaRecalculation(true);
			 
			 book.write(os);
			
//			log.info("Export Excel " + channelName + "," + date + " finish" );
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try{ if(is!=null)is.close(); }catch(Exception ex){}
		}
	}
	
	
	/**
	 * 导出excel Zip文件--创建excel文档
	 * @param dateType 结算类型
	 * @param date 日期
	 * @param channelName 渠道或是上游名称
	 * @param list 数据
	 * @param demoPath EXECL路径 
	 * @param path excel保存路径
	 */
	public void writeSettleAccountToExcelZip(String dateType, String date,
			String channelName, List<SettleAccountModel> list, String demoPath,String excelPath,String fileName)
	{
		InputStream is = null;
		OutputStream out=null;
		
		try
		{
			is = new FileInputStream(demoPath);
			HSSFWorkbook book =  new HSSFWorkbook(is);
			HSSFSheet sheet = book.getSheetAt(0);
			
			int tempSpTroneNameLength = 0;
			int maxSpTroneNameLength = 0;
			
			int tempProductLineLength = 0;
			int maxProductLineLength = 0;
			
			Map<String, HSSFCellStyle> mapStyle = createStyles(book);
			
			SettleAccountModel model = null;
			
			for(int i=0; i<list.size(); i++)
			{
				model = list.get(i);
				
				HSSFRow row = sheet.createRow(i+3);
				row.createCell(1).setCellStyle(mapStyle.get("BASE_STYLE"));
				row.createCell(2).setCellStyle(mapStyle.get("BASE_STYLE"));
				row.createCell(3).setCellStyle(mapStyle.get("BASE_STYLE"));
				
				HSSFCell cell = row.createCell(4);
				cell.setCellStyle(mapStyle.get("BASE_STYLE"));
				cell.setCellValue(model.getOperatorName());
				
				cell = row.createCell(5);
				cell.setCellStyle(mapStyle.get("BASE_STYLE"));
				cell.setCellValue(model.getSpTroneName());
				
				cell = row.createCell(6);
				cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
				cell.setCellValue(model.getAmount());
				
				//核减结算款
				cell = row.createCell(7);
				cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
				
				//核减信息费要转为核减结算款
				if(model.getReduceType()==0)
				{
					cell.setCellValue(model.getReduceAmount()*model.getJiesuanlv());
				}
				else if(model.getReduceType()==1)
				{
					cell.setCellValue(model.getReduceAmount());
				}
				
				
				cell = row.createCell(8);
				cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
				cell.setCellValue(model.getJiesuanlv());
				
				cell = row.createCell(9);
				cell.setCellStyle(mapStyle.get("FORMAT_STYLE"));
				
				cell.setCellFormula("G"+ (4+i) +"*I" + (4+i) + "-H"+ (4+i));
				
				tempSpTroneNameLength = model.getSpTroneName().getBytes("GBK").length;
				tempProductLineLength = model.getOperatorName().getBytes("GBK").length;
				
				if(tempSpTroneNameLength > maxSpTroneNameLength)
					maxSpTroneNameLength = tempSpTroneNameLength;
				
				if(tempProductLineLength > maxProductLineLength)
					maxProductLineLength = tempProductLineLength;
			}
			
			//结算方式宽度
			sheet.setColumnWidth(1, 9*256);
			//结算期间宽度
			sheet.setColumnWidth(2, 18*256);
			//渠道名称宽度
			sheet.setColumnWidth(3, (channelName.getBytes("GBK").length+1)*256);
			//运营商宽度
			sheet.setColumnWidth(4, (maxProductLineLength+1)*256);
			//产品名称宽度
			sheet.setColumnWidth(5, (maxSpTroneNameLength+1)*256);
			//信息费宽度
			sheet.setColumnWidth(6, 13*256);
			//特殊信息费宽度***2016.08.04加入
			sheet.setColumnWidth(7, 13*256);
			//结算价宽度
			sheet.setColumnWidth(8, 18*256);
			//渠道酬金宽度
			sheet.setColumnWidth(9, 13*256);
			
			sheet.addMergedRegion(new CellRangeAddress(3,2+list.size(),1,1));
			sheet.getRow(3).getCell(1).setCellValue(dateType);
			sheet.getRow(3).getCell(1).setCellStyle(mapStyle.get("BASE_STYLE"));
			
			sheet.addMergedRegion(new CellRangeAddress(3,2+list.size(),2,2));
			sheet.getRow(3).getCell(2).setCellValue(date);
			sheet.getRow(3).getCell(2).setCellStyle(mapStyle.get("BASE_STYLE"));
			
			sheet.addMergedRegion(new CellRangeAddress(3,2+list.size(),3,3));
			sheet.getRow(3).getCell(3).setCellValue(channelName);
			sheet.getRow(3).getCell(3).setCellStyle(mapStyle.get("BASE_STYLE"));
			
			HSSFRow row = sheet.createRow(3+list.size());
			for(int i=0; i<9; i++)
				row.createCell(i+1).setCellStyle(mapStyle.get("BASE_STYLE"));
			
			sheet.addMergedRegion(new CellRangeAddress(3+list.size(),3+list.size(),1,8));
			row.getCell(1).setCellValue("合计");
			row.getCell(9).setCellFormula("SUM(J" + 3 + ":J" + (list.size() + 3) + ")");
			row.getCell(9).setCellStyle(mapStyle.get("FORMAT_STYLE"));
			
			sheet.setForceFormulaRecalculation(true);
			out=new FileOutputStream(excelPath+"/"+fileName );
			book.write(out);
			
			log.info("Export Excel " + channelName + "," + date + " finish" );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try{ if(is!=null)is.close(); 
				if(out!=null){
					out.flush();
					out.close();
				}
			}catch(Exception ex){}
			
		}
	}
}

