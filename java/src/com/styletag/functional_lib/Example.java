package com.styletag.functional_lib;

public class Example {
	public static void main(String[] args){
		ExcelRead xl = new ExcelRead("//home//styletag//java_test//Test Framework//src//com//styletag//test_cases//InputData.xlsx");
		
		int rowcount=xl.rowCountInSheet(1);
		System.out.println((rowcount+1 )+ " rows");
		//for(int i=2;i<=rowcount;i++)
		{
			System.out.println(xl.read(2, 0));
			System.out.println(xl.read(2,1) + "\n");
		}
	}

}