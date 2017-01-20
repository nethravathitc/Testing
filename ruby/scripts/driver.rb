require  '/home/rashmi/test-framework/scripts/business_action.rb'
require '/home/rashmi/test-framework/funtional_lib/readexceldata.rb'
include Read
require '/home/rashmi/test-framework/funtional_lib/utils.rb'
include Utils

class Driver
	
	def initialize
		super('/home/rashmi/test-framework/test_case/test_suit.xls')
		@obj_action = BusinessAction.new
	end
	
	def test_execution
		
		#for i in 1..1
		i=1
		while dataread(i,3)!=nil
			p dataread(i,0)!= nil 
			p dataread(i,3)!=nil

			if(dataread(i,0)!=nil)

			test_suit= dataread(i,0)
			puts "\n\nTest Suit Name :" + test_suit
			
			test_case_id = dataread(i,1)
			puts "Test Case ID :" + test_case_id.to_s

			execution_flag = dataread(i,2)
			puts "Execution Flag :" + execution_flag
			end
		
			if execution_flag=="YES"
				#while dataread(i,0)!=nil || dataread(i,3)!=nil
				begin
					#puts "i value before increment "+ i.to_s
					action= dataread(i,3)
					puts "\nAction Name :" + action
					@obj_action.send("#{action}")
					i=i+1
					#puts "i value after increment "+ i.to_s
				end while(dataread(i,0)==nil) && dataread(i,3)!=nil
				
			else
				#puts "Execution Flag :" + execution_flag
				i=i+1
			end
			

		end

	end
end

launch_styletag

obj_driver = Driver.new
obj_driver.test_execution