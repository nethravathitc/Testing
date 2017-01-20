package com.styletag.scripts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.styletag.functional_lib.*;

public class Driver {
	Method m;
	public static int FLAG=0;
	public static int column=0;
	public static int row_flag=0;
	public static int row_num=0;
	public static int block=0;
		
	public static void main(String[] args) {
		
		String execution_flag,actions,msg,url,failed_actions="";
		ArrayList<String> array = new ArrayList<String>();
		
		url="http://origin-www2.stest.in";
		ExcelWrite write= new ExcelWrite();
		BusinessAction baction= new BusinessAction(write);
		
		baction.launchStyletag(url);
		msg ="Test Report of "+url;
		write.writeReports("Result", msg);
		
		//To set Header in the Result sheet
		array.add("SL No");
		array.add("TEST SCENARIO");
		array.add("TEST CASE ID");
		array.add("PASS/FAIL");
		array.add("Failed Functions");
		write.writeReports("Result", array);
		array.removeAll(array); // to remove the elements added to array
		
		
						
		ExcelRead xl= new ExcelRead("//home//styletag//java_test//Test Framework//src//com//styletag//test_cases//TestSuit.xlsx");
		int n =xl.rowCountInSheet(0);
		int i=1;
		try{
		while (xl.read(i,0)!=null)
		{	
			array.add(""+i); // serial no's
						
			String scenario =msg=xl.read(i, 0);
			array.add(msg);
			System.out.println("Test Scenario: "+msg);
			
			msg=xl.read(i,1);
			array.add(msg);
			//write.writeReports("Result", msg);
			System.out.println("Test Case ID: "+msg);
			
			execution_flag=xl.read(i,2);
			//array.add("Execution Flag "+execution_flag);
			System.out.println(execution_flag);
			//write.writeReports("Result", execution_flag);
			int j=3;
			column=0;
			int row;
			
			if(execution_flag.equals("YES"))
			{	
				row_num=write.lastRowNum("Log");// this will get the row num before the Action name is 
				row_num++;
				row=row_num;// this to store the starting the row no from where the first action logs
				//System.out.println("row_num before executing action and col num "+row_num+" "+column);
				write.writeReports("Error", scenario);
				write.writeReports("Log", scenario);
				int k=1;	
				do
				{
					actions = xl.read(i,j);
					msg="Action"+k+": "+actions;
					write.writeReports("Log", msg,column);
					write.writeReports("Error", msg,column);
															
					System.out.println(msg);
								
					try {
							Method method = baction.getClass().getMethod(actions);
							method.invoke(baction);
							
							column++;
							row_flag=1;// this is to indicate rows are added already
							System.out.println("Inside driver row_flag and col_no after executn a function"+row_flag+" "+column);
							row_num=row;
							if(FLAG==0)
							{
								failed_actions=failed_actions.concat(actions);// adding Action name to Result sheet in case of error
								failed_actions=failed_actions+" ";
							}
						} catch (Exception e) 
						{
							//e.printStackTrace();
						} 
				
					j++;
					k++;
					
				}while(xl.read(i,j)!=null);
				
				if(FLAG==0)
					array.add("FAIL");
				else
					array.add("PASS");
				array.add(failed_actions);// adding failed action after the scenarios result
				write.writeReports("Result", array);
				row_flag=0;
				failed_actions="";
				
			}
			i++;
			array.removeAll(array);// to remove the previous entry in the list, otherwise this list will store all the strings
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
