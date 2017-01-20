module Read
  def initialize(p)
      @path=p
  end
  
  def dataread(r,c)
	 require 'spreadsheet'
	 #@data1
   @data1 = nil
	 book = Spreadsheet.open(@path)
   #p Spreadsheet
	 sheet1 = book.worksheet 0
	 sheet1.each_with_index do |row, index|
    	for column in 0..(row.length-1)
  		  if index==r and column==c

           #puts "Row:- #{index}, Column: #{column}, Value: #{row[column]}" 
    		  @data1 = row[column]

    		  break
		    end
  	   end
  	end
  	return @data1
  end
end


#obj= Read.new('./hybrid_framework/test_case/inputdata.xls')

#puts obj.dataread(2,0)

#p='./hybrid_framework/test_case/inputdata.xls'
#puts Read.dataread(2,0,p)

 #email=Read.dataread(2,0)
 #pass = Read.dataread(2,1)
 #name = Read.dataread(2,2)
 #puts email
 #puts pass
 #puts name
 #puts "\n\n hai this is excel reader"

