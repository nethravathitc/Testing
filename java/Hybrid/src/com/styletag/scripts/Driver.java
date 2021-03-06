package com.styletag.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

import com.styletag.functionalLib.*;

public class Driver {
	Method m;
	public static int FLAG=0;// set when in the business action function based on successful execution of the function
	public static int column=0;
	public static int row_flag=0;
	public static int row_num=0;
	public static int block=0;
	public static String HOME_URL;
	public static Properties properties;
		
	public static void main(String[] args) {
		
		// initalizing properties variable to configuration file
		try{	
		File file = new File(System.getProperty("user.dir")+"//config.xml");
			FileInputStream fileInput = new FileInputStream(file);
			properties = new Properties();
			properties.loadFromXML(fileInput);
			fileInput.close();
			
			String continent=properties.getProperty("favoriteAnimal");
			System.out.println("Favorite continent is: "+continent);
			HOME_URL=properties.getProperty("HOME_URL");
			
				
		String execution_flag,actions,msg,failed_actions="";
		ArrayList<String> array = new ArrayList<String>();
				
		ExcelWrite write= new ExcelWrite();
		BusinessAction baction= new BusinessAction(write);
		
		baction.launchStyletag(HOME_URL);
		msg ="Test Report of "+HOME_URL;
		write.writeReports("Result", msg);
		
		//To set Header in the Result sheet
		array.add("SL No");
		array.add("TEST SCENARIO");
		array.add("TEST CASE ID");
		array.add("PASS/FAIL");
		array.add("Failed Functions");
		write.writeReports("Result", array);
		array.removeAll(array); // to remove the elements added to array
		
		
						
		ExcelRead xl= new ExcelRead(System.getProperty("user.dir")+properties.getProperty("TestSuitFile"));
		int n =xl.rowCountInSheet(0);
		int i=1;
		int serial_num=1;
		String SL_no;
		
		while (xl.read(i,0)!=null)
		{	
			array.add(""+serial_num); // serial no's
						
			String scenario = msg=xl.read(i, 0);
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
				row_num=write.lastRowNum("Log");// this will get the row num before the Action name is written 
				int row_num_log=row_num++;
				int row_num_error=write.lastRowNum("Error")+1;
				row=row_num;// this to store the starting row no from where the first action logs
				//System.out.println("row_num before executing action and col num "+row_num+" "+column);
				
				SL_no=""+serial_num;
				write.writeReports("Log", SL_no,column);
				write.writeReports("Error",SL_no,column);
				column++;
				
				write.writeReports("Error", scenario);
				write.writeReports("Log", scenario);
				int k=1;
				
				do
				{
					
					actions = xl.read(i,j);
					msg="Action"+k+": "+actions;
					System.out.println("\n"+msg);
					if(!actions.equals(""))
					{
						/*SL_no=""+serial_num;
						write.writeReports("Log", SL_no,column);
						write.writeReports("Error",SL_no,column);
						column++;
						*/
						write.writeReports("Log", msg,column);
						write.writeReports("Error", msg,column);
					}
															
					//System.out.println(msg);
								
					try {
						   // FLAG=0; // resetting the FLAG
						   // System.out.println("Driver.FLAG value before executing the function "+FLAG);
							//Thread.sleep(300);
							Method method = baction.getClass().getMethod(actions);
							method.invoke(baction);
							
							column++;
							row_flag=1;// this is to indicate rows are added already
							//System.out.println("Inside driver row_flag and col_no after executing a function"+row_flag+" "+column);
							//row_num=row;
							write.log_row_num=row_num_log;// to reset the row pointer to the position from where second column needs to starts writing
							write.error_row_num=row_num_error;
							write.log_newrow_flag=0;// to reset the flag which will get set when new rows are added to for second and other columns
							write.error_newrow_flag=0;
							
							if(FLAG==0)
							{
								failed_actions=failed_actions.concat(actions);// adding Action name to Result sheet in case of error
								failed_actions=failed_actions+" ";
							}
							//System.out.println("inside Driver : FLAG value after executing the function "+FLAG);
							
						} catch (Exception e) 
						{
							//e.printStackTrace();
						} 
				
					j++;
					//System.out.println("j value is "+j);
					k++;
					
				}while(xl.read(i,j)!=null);
				
				if(FLAG==0)
					array.add("FAIL");
				else
					array.add("PASS");
				
				FLAG=0;//resetting the flag
				
				array.add(failed_actions);// adding failed action after the scenarios result
				write.writeReports("Result", array);
				serial_num++;
				row_flag=0;
				failed_actions="";
				
				
			}
			i++;
			array.removeAll(array);// to remove the previous entry in the list, otherwise this list will store all the strings
			//baction.cart_item_list.removeAll(baction.cart_item_list); // resetting these variables for next scenario execution
		}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
