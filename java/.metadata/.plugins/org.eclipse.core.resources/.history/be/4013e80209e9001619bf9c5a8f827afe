package com.styletag.functionalLib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.styletag.scripts.Driver;

public class ExcelWrite {
	XSSFWorkbook workbook ;
	XSSFSheet spreadsheet1;
	XSSFSheet spreadsheet2;
	XSSFSheet spreadsheet3;
	XSSFRow row ; Cell cell;
	DateFormat df;
	Date dateobj;
	public static String  report_file_path="";
	public static String file_name;
	int rowsh1=0,rowsh2=0,rowsh3=0;
	public static int log_row_num=0,error_row_num=0;
	public static int log_newrow_flag=0,error_newrow_flag=0;

	public ExcelWrite()
	{
		workbook = new XSSFWorkbook();
		spreadsheet1 = workbook.createSheet("Results");
		spreadsheet2 = workbook.createSheet("Log");
		spreadsheet3= workbook.createSheet("Error");
		setHeader();// called only once when constructor is called	   
	    
	}
	public void setHeader()
	{
		df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	    dateobj = new Date();
	    String d =df.format(dateobj); String date1=d;
	    String sd[] = d.split("\\s");
	    System.out.println(sd[0]);
	    System.out.println(sd[1]);
	    sd[0]=sd[0].replaceAll("\\/","_");
	    sd[1]=sd[1].replaceAll(":","_");
	    d="_date_"+sd[0]+"_time_"+sd[1];
	    
	    report_file_path="//home//styletag//sanity_reports//report"+d+".xlsx";
	    file_name="report"+d+".xlsx";
	   
	   String msg1="TEST EXECUTION RESULTS ",msg2="DATE: "+df.format(dateobj);
	   //rowsh1=0;rowsh2=0;
	   row=spreadsheet1.createRow(0); // writing in to first row  of Result sheet
	   cell=row.createCell(0);
	   cell.setCellValue(msg1);
	   cell=row.createCell(1);
	   cell.setCellValue(msg2);
	   
	   row=spreadsheet2.createRow(0); // writing in to first row page of Log sheet
	   cell=row.createCell(0);
	   cell.setCellValue(msg1);
	   cell=row.createCell(1);
	   cell.setCellValue(msg2);	
	   
	   row=spreadsheet3.createRow(0); // writing in to first row page of Error sheet
	   cell=row.createCell(0);
	   cell.setCellValue(msg1);
	   cell=row.createCell(1);
	   cell.setCellValue(msg2);
	   
	}
	public void writeReports(String sheetName,String msg)
	{	//int col=0;
		//System.out.println(sheetName+"  "+msg);
	
		if (sheetName.equals("Result")) 
		{	
			rowsh1=spreadsheet1.getLastRowNum();
			rowsh1++;
			row= spreadsheet1.createRow(rowsh1);
			//if(column!=-1)
				//col=column;

			cell = row.createCell(0);
			cell.setCellValue(msg);
			
		}
		if(sheetName.equals("Log"))
		{
			rowsh2=spreadsheet2.getLastRowNum();
			rowsh2++;
			row= spreadsheet2.createRow(rowsh2);
			//if(column!=-1)
				//col=column;
			
			cell=row.createCell(0);
			cell.setCellValue(msg);
		}
		
		if(sheetName.equals("Error"))
		{
			rowsh3=spreadsheet3.getLastRowNum();
			rowsh3++;
			row= spreadsheet3.createRow(rowsh3);
			//if(column!=-1)
				//col=column;
			
			cell=row.createCell(0);
			cell.setCellValue(msg);
		}
		
		
		
		try {
			File file = new File(report_file_path);
			FileOutputStream out= new FileOutputStream(file);
			workbook.write(out);
		} catch (FileNotFoundException e) {
			System.out.println("Error in fileoutput stream");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in closing fileoutputstream");
			e.printStackTrace();
		}
		
		
		
	}	
	
	public void writeReports(String sheetName,ArrayList<String> msg)
	{	int col=0;
		//System.out.println(sheetName+"  "+msg);
		//System.out.println("coloumn count "+msg.size());
		if (sheetName.equals("Result")) 
		{	
			rowsh1=spreadsheet1.getLastRowNum();
			rowsh1++;
			row= spreadsheet1.createRow(rowsh1);
			for(int i=0;i<msg.size();i++)
			{
				cell = row.createCell(i);
				cell.setCellValue(msg.get(i));
			}
		}
		if(sheetName.equals("Console"))
		{
			rowsh2=spreadsheet2.getLastRowNum();
			rowsh2++;
			row= spreadsheet2.createRow(rowsh2);
			for(int i=0;i<msg.size();i++)
			{
				cell = row.createCell(i);
				cell.setCellValue(msg.get(i));
			}
		}
		
		if(sheetName.equals("Error"))
		{
			rowsh3=spreadsheet3.getLastRowNum();
			rowsh3++;
			row= spreadsheet1.createRow(rowsh3);
			for(int i=0;i<msg.size();i++)
			{
				cell = row.createCell(i);
				cell.setCellValue(msg.get(i));
			}
		}
		
		
		
		try {
			File file = new File(report_file_path);
			FileOutputStream out= new FileOutputStream(file);
			workbook.write(out);
		} catch (FileNotFoundException e) {
			System.out.println("Error in fileoutput stream");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in closing fileoutputstream");
			e.printStackTrace();
		}
		
		
		
	}	
	public void writeReports(String sheetName,String msg,int column)
	{	int col=0;
		//System.out.println(sheetName+"  "+msg);
		//if(column!=-1)
			//col=column;
		
		if(Driver.row_flag==0)// write to new rows by creating them
		{	
			System.out.println("inside if row_flag==0");
		if (sheetName.equals("Result")) 
		{	
			rowsh1=spreadsheet1.getLastRowNum();
			rowsh1++;
			row= spreadsheet1.createRow(rowsh1);
			if(column!=-1)
				col=column;

			cell = row.createCell(col);
			cell.setCellValue(msg);
			
		}
		if(sheetName.equals("Log"))
		{
			rowsh2=spreadsheet2.getLastRowNum();
			
			rowsh2++;
			System.out.println("writing to Row no inside Log"+rowsh2);
			row= spreadsheet2.createRow(rowsh2);
			if(column!=-1)
				col=column;
			
			cell=row.createCell(col);
			cell.setCellValue(msg);
			System.out.println("added "+msg+" :  row col"+rowsh2+" "+col);
		}
		
		if(sheetName.equals("Error"))
		{
			rowsh3=spreadsheet3.getLastRowNum();
			
			rowsh3++;
			System.out.println("writing to Row no inside Error"+rowsh3);
			row= spreadsheet3.createRow(rowsh3);
			if(column!=-1)
				col=column;
			
			cell=row.createCell(col);
			cell.setCellValue(msg);
			System.out.println("added "+msg+" :  row col"+rowsh3+" "+col);
		}
		}
		else // writing to created rows
		{
			System.out.println("Inside else ie row_flag==1");
			if (sheetName.equals("Result")) 
			{	
				int i=  Driver.row_num;
				
				rowsh1=spreadsheet1.getLastRowNum();
				
				//System.out.println("");
				
				if(i<rowsh1)
				{
					//System.out.println("inside i<rowsh1 and column value is "+col+ "column value is"+column );
					//System.out.println("i value and rowsh2 value "+i+" "+rowsh2);
					//while(i!=rowsh2)
					{
						
						i++;
						System.out.println("writing to row "+i);
						row=spreadsheet1.getRow(i);
						cell=row.createCell(column);
						cell.setCellValue(msg);
						
						Driver.row_num++; // to point to next row for the next time entry
						
						//System.out.println("added "+msg+" to row_num col_num"+i+" "+column);
					}
				}
				else // new rows need to be added second and coming columns
				{
					//System.out.println("inside else of i>rowsh1");
					//System.out.println("inside i<rowsh1 and column value is "+col+ "column value is"+column );
					//System.out.println("i value and rowsh2 value "+i+" "+rowsh2);
					
					rowsh1++;
					row= spreadsheet1.createRow(rowsh1);
					//if(column!=-1)
						//col=column;
					int j=0;
					while(j<column)
					{
						cell=row.createCell(j);
						cell.setCellValue("");
						j++;
					}

					cell = row.createCell(column);
					cell.setCellValue(msg);
					//System.out.println("added "+msg+" to row_num col_num"+rowsh1+" "+column);
					
				}
								
			}
			if(sheetName.equals("Log"))
			{ 
				//System.out.println("\nLog");
				int i=  log_row_num;
				
				rowsh2=spreadsheet2.getLastRowNum();
				//System.out.println("Row no inside Log "+rowsh2);
				//System.out.println("i value and rowsh2 value "+i+" "+rowsh2);
				
				if(i<rowsh2&&(log_newrow_flag==0))
				{
					//System.out.println("inside i<rowsh2 and column value is "+col+ "column value is"+column );
					
					//while(i!=rowsh2)
					{
						
						i++;
						//System.out.println("writing to Row no inside Log "+i);
						row=spreadsheet2.getRow(i);
					
						cell=row.createCell(column);
						cell.setCellValue(msg);
						
						log_row_num++; // to point to next row for the next time entry
						//System.out.println("i value after increment"+i);
						
						//System.out.println("added"+msg+" to row"+i+" col"+column);
						//System.out.println("Driver.row_num col_num"+Driver.row_num+" "+column);
					}
				}
				else // new rows need to be added second and coming columns
				{
					log_newrow_flag=1;
					//System.out.println("inside else of i>rowsh2");
					//System.out.println("inside i<rowsh2 and column value is "+col+ "column value is"+column );
					System.out.println("i value and rowsh2 value "+i+" "+rowsh2);
					
					
						rowsh2++;
						//System.out.println("writing to Row no inside Log "+rowsh2);
						row= spreadsheet2.createRow(rowsh2);
					//if(column!=-1)
						//col=column;
						int j=0;
						while(j<column)
						{
							cell=row.createCell(j);
							cell.setCellValue("");
							j++;
						}

						cell = row.createCell(column);
						cell.setCellValue(msg);
					
					//System.out.println("added "+msg+" to row"+rowsh2+" col "+column);
					
				}
			}
			
			if(sheetName.equals("Error"))
			{
				//System.out.println("\n Error");
				int i=  error_row_num;
				
				rowsh3=spreadsheet3.getLastRowNum();
				//System.out.println("Row no inside Error"+rowsh3);
				//System.out.println("i value and rowsh3 value "+i+" "+rowsh3);
				
				if(i<rowsh3&&error_newrow_flag==0)
				{
					//System.out.println("inside i<rowsh3 and column value is "+col+ "column value is"+column );
					
					//while(i!=rowsh2)
					{
						i++;
						//System.out.println("writing to Row no inside Error "+i);
						row=spreadsheet3.getRow(i);
						cell=row.createCell(column);
						cell.setCellValue(msg);
						
						error_row_num++; // to point to next row for the next time entry
						
						//System.out.println("added "+msg+" to row"+i+" col"+column);
						//System.out.println("Driver.row_num col_num"+Driver.row_num+" "+column);
						
					}
				}
				else // new rows need to be added second and coming columns
				{
					error_newrow_flag=1;
					//System.out.println("inside else of i>rowsh3");
					//System.out.println("inside i<rowsh1 and column value is "+col+ "column value is"+column );
					//System.out.println("i value and rowsh2 value "+i+" "+rowsh3);
					
						rowsh3++;
						System.out.println("writing to Row no inside Error "+rowsh3);
						row= spreadsheet3.createRow(rowsh3);
					//if(column!=-1)
						//col=column;
						int j=0;
						while(j<column)
						{
							cell=row.createCell(j);
							cell.setCellValue("");
							j++;
						}

						cell = row.createCell(column);
						cell.setCellValue(msg);
					
					//System.out.println("added "+msg+" to row"+rowsh3+" col"+column);
					
				}
			}
		}
		
		try {
			File file = new File(report_file_path);
			FileOutputStream out= new FileOutputStream(file);
			workbook.write(out);
		} catch (FileNotFoundException e) {
			System.out.println("Error in fileoutput stream");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in closing fileoutputstream");
			e.printStackTrace();
		}
			
	}
	public int lastRowNum(String SheetName)
	{
		if(SheetName.equals("Result"))
		{
			return spreadsheet1.getLastRowNum();
		}
		if(SheetName.equals("Log"))
		{
			return spreadsheet2.getLastRowNum();
		}
		if(SheetName.equals("Error"))
		{
			return spreadsheet3.getLastRowNum();
		}
		return (-1);
		
	}
	
	
	

}
