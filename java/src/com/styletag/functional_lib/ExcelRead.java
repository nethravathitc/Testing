package com.styletag.functional_lib;

import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelRead {
	File file1;
	XSSFWorkbook wb;
	XSSFSheet sheet;
	
	public ExcelRead(String path){
		
		try {
			 file1= new File(path);
			 wb= new XSSFWorkbook(file1);
				
		} catch (Exception e) {
			
			System.out.println("something wrong with the file \n");
		}
		
	}
	public int rowCountInSheet(int sheetNum){
		sheet= wb.getSheetAt(sheetNum);
		return sheet.getLastRowNum();
	}
	
	public String read(int row, int col){
		
		//sheet= wb.getSheetAt(sheetNum);
		try {
			return sheet.getRow(row).getCell(col).getStringCellValue();
			
		} catch (Exception e) {
			
			//System.out.println("Error in reading the excel file");
			//e.printStackTrace();
		}
		return null;
		
	}

}
