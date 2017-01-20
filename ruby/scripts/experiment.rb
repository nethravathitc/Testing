require '/home/styletag/QAnew/hybrid_framework/funtional_lib/readexceldata.rb'
include Read
require "selenium-webdriver"
require '/home/styletag/QAnew/hybrid_framework/ui_obj_info/ui_objects.rb'
include Css
require '/home/styletag/QAnew/hybrid_framework/funtional_lib/utils.rb'
include Utils

class BusinessAction
	def initialize
		super('/home/styletag/QAnew/hybrid_framework/test_case/inputdata.xls')
		#@obj = Read.new('/home/styletag/QAnew/hybrid_framework/test_case/inputdata.xls')
	end
	def method_trail1
		puts "Inside Third class func3"
		for i in 2..6
			email= dataread(i,0)
			pass = dataread(i,1)
			name = dataread(i,2)
			puts "Email : "+email + "  Password : " +pass +"  Name : " + name

		end
	end
	def method_trail2
		puts "method_trail2"
	end
	
	# LOGIN FUNCTIONALITY
	def login_positive_scenarios 
		puts "Login_positive_scenarios... "
    	wait_for_spinner
    	
    	#login_link.click
      login_name= $driver.find_element(:css,"#login-name")
      $driver.action.move_to(login_name).perform
      sleep(1)
      #$driver.find_element(:css, "#login-name > div.show-login > div.margin-between > a.login-link").click
      $driver.find_element(:css,"#account-wrap > div > div:nth-child(1) > div > div > div:nth-child(4) > a.login-link").click

    	wait_for_spinner  

    	for i in 1..20
      		login=dataread(i,0)
      		if login=="Login"
        		#puts login
        		#puts "#{i}"
        		i=i+1 #to point to the index of the next row
        		break
      		end
    	end
     	 #puts Read.dataread(i,0)
    	login_email.send_keys dataread(i,0)
    	#puts Read.dataread(i,1)
    	login_password.send_keys dataread(i,1)
    	login_btn.click

    	wait_for_spinner
    	sleep(2)
    	loggedin_name = account_member_name.text
    	wait_for_spinner
    	if loggedin_name.include? "Hello"
      	puts "user logged into styletag: PASS"
    	else
      	puts "user not logged into styletag: FAIL"
    	end

	end	

	# LOGOUT FUNCTIONALITY
  	def logout
      sleep(1)
    	menu = account_member_name
   		$driver.action.move_to(menu).perform
   		sleep(1)
    	inner_logout.click
  	end

  	# ADDING PRODUCT TO CART--HOME PAGE-->SALE-->PRODUCT CATALOG-->PRODUCT--> BUTTON CLICK
  	def adding_to_cart
    
    	sleep(2)
    	puts "click c1 ethnicwear"
    	ethnic_wear.click
    	wait_for_spinner
      sleep(2)
      puts "movin cursor over c1"
      $driver.action.move_to(ethnic_wear).perform
      $driver.find_element(:css, ".submenu > li:nth-child(1) > ul > li > ul > li > ul > li > ul > li:nth-child(1) > ul > li:nth-child(1) > a.col-dark-grey").click
	    
      wait_for_spinner
      sleep(3)

      puts "clicking on product"
      sleep(1)
      $driver.find_element(:css,"#product-container > div.ng-isolate-scope > ul > li:nth-child(4) > div > div.product-image > a > img").click

      #puts "click first sale..." NO MORE SALE VIEW
    	#first_sale.click
    	#wait_for_spinner
    	sleep(3)

    	
      @flag_no_product=0 
      i=1
      begin
        #first_product.click
        #$driver.find_element(:css,"#product-container > div > ul > li.product-wrap:nth-child(#{i}) > div.products-wrapper .go-to-product").click
        
        #it will click only non-pinned product ie normal product
        #$driver.find_element(:css,"#product-container > div.ng-isolate-scope > product-list > ul > li:nth-child(#{i}) > div > div.product-image > a > img").click
        
        sleep(3)
    	  puts "click product no #{i} ..."
    	  wait_for_spinner
        p= $driver.find_element("#sale-main-desc > div.cart-form > h1").text
	       puts p
        #puts @prod1 = productviewpage_productname.text.downcase  # storing the product name by down casting it
    	   sleep(3)
         
      	begin    
      		puts "select size add to cart ..."
      		size.click
      		sleep(3)
      		add_to_cart_button.click
        rescue   
        	begin    
          		sleep(3)
          		puts "no size found. add to cart ..."
          		add_to_cart_button.click
        	rescue
          		puts "product got soldout (or) product not available"
              @flag_no_product=1
              i++  # to select the next product
          		wait_for_spinner
        	end 
      	 end
      end while @flag_no_product==1 
  	end

  	# CHECKING MINICART--CLICK MINICART--> CHECK ADDED PRODUCT IN CART PAGE
  	def cart_check
    	puts "cart"
      if @flag_no_product==0  
    	 sleep(2)
	     mini_cart_text.click
    	 wait_for_spinner
    	 puts @prod2 = cartpage_productname.text.downcase
    	 if(@prod1 == @prod2)
    	   	puts "product is added to the cart: PASS"
    	 else
      		puts "product is not added to the cart: FAIL" # cart is invalid ie when items in the cart are sold out 
          puts "Removing sold out item present in the cart"
          remove_sold_out.click

    	 end
    	 #wait_for_spinner
    	 #puts "Proceed to checkoout ..." 
    	 #proceed_to_checkout.click  
    	 #wait_for_spinner
      
      else
        print "product is not added to cart because of non availability"
      end

  	end

    def proceed_to_pay
      sleep(1)
      mini_cart_text.click
      wait_for_spinner
      sleep(1)
      puts "Proceed to checkoout ..." 
      proceed_to_checkout.click  
      wait_for_spinner
      puts "continue as logged in"
      wait_for_spinner
      continue.click
      wait_for_spinner
      puts "add new address"
      # click add new address
      add_address_checkout.click
      wait_for_spinner

      for i in 1..20
        login=dataread(i,0)
         if login=="Login"
            i=i+1 #to point to the index of the next row
            break
          end
      end
      address_pincode.send_keys dataread(i,3)
      wait_for_spinner
      firstname.send_keys dataread(i,4)
      address_phone.send_keys dataread(i,5)
      address_address1.send_keys dataread(i,6)
      address_landmark.send_keys dataread(i,7)
      create_btn.click
      wait_for_spinner

      puts "selecting address"
      select_address.click
      wait_for_spinner
      continue3.click
      wait_for_spinner

      puts "clicking pay button"
      proceed_pay_btn.click
      longwait
    end

    def payment_method_cod
      puts "payment_method - COD"
      cod_payment.click
      wait_for_spinner
      if codbutton.displayed?
        puts "Order placing as COD"
        codbutton.click
      else
        puts "Sorry, no COD available"
      end
      sleep(10)
      #$driver.switch_to.alert.accept # for fire fox browser
      sleep(250)
      wait_for_spinner

       puts $driver.find_element(:css,"#order-cancel > div > section > p:nth-child(2) > span").text
      
    end

    def function
      elements=[]
      elements = $driver.find_element("#home-carousel > data-owl-carousel > div.owl-wrapper-outer")


    #> div > div:nth-child(1) > div > a > img
     # if(first_carousel.displayed?)
      #  first_carousel.click
      #end
      $driver.find_element("#sales-home-container > div:nth-child(1) > div > ul > li:nth-child(2) > div.image-wrap > a").click

    end


def function1
      sleep(1)

      element = $driver.find_element(:css,"#mini-cart-wrap > li > div.minicart-inner-div > a:nth-child(3) > span") 
      puts "moving cursor to minicart"
      $driver.action.move_to(element).perform

      
      begin
          empty_my_cart = $driver.find_element(:css,"#minicart-bottom > p.pull-left > a")
          sleep(1)
         #empty_my_cart.displayed?
         empty_my_cart.click
         sleep(1)
       rescue 
         puts "cart is already empty"
       end 

    end


def homePage
  i=1
  first_carousel= driver.find_element("#home-carousel > data-owl-carousel > div.owl-wrapper-outer > div > div:nth-child(1) > div > a > img")
  carousel= first_carousel
   # begin
    if(carousel.displayed?)
      carousel.click
      end
    i= i+1
    carousel = driver.find_element("#home-carousel > data-owl-carousel > div.owl-wrapper-outer > div > div:nth-child(#{i}) > div > a > img")
    #end while (carousel!= first_carousel) 
    
  
  end


  def search_with_key 
  search_value = "mugs"
  puts "search for #{search_value}"

  puts "clicking on search button"
  puts "this is button"
  #$driver.find_element(:css, "#aws_form_search > #appendedInputButtons").click
  search_field.click
  sleep(2)

  #$driver.find_element(:css, "#aws_form_search > #appendedInputButtons").clear
  search_field.clear
  sleep(2)
  sleep(2)
  #$driver.find_element(:css, "#aws_form_search > #appendedInputButtons").send_keys("#{search_value}")
  search_field.send_keys("#{search_value}")
  sleep(2)
  $driver.find_element(:css, "#aws_form_search > #submitButton").click
  sleep(2)
  wait_for_spinner
  title = $driver.find_element(:css, "#catalog-wrap > h1").text   
  print "Page title: #{title} \n"
  sleep(5)
  #$driver.find_element(:xpath, "//p[@id='price-htl-sort']/a[2]").click #high-low
  #@driver.find_element(:css, "#price-htl-sort > a:nth-child(2)").click  #low-high

  pts"\n checking for empty string"
  end

  def search_with_empty_key
  puts "clicking on search button"
  search_field.click
  sleep(2)
  puts "clearing search field"
  search_field.clear
  begin

  rescue
  end

 
  end
   def prices(i)
    #wait_for_spinner
    sleep(2)
    begin 
     price = $driver.find_element(:css,"#product-container > div > ul > li:nth-child(#{i}) > div > div.product-Info > span.product-price.pull-right > span.col-orange.text-right.text-capitalize.ng-binding").text()
     price=price.gsub("Rs. ","").gsub(",","")
    price=price.to_i
    rescue Selenium::WebDriver::Error::NoSuchElementError
    @error_sale_index << i
    price=0
  end
      return price
  end
  def price_compare
  #slider_value = @driver.find_element(:css, "#price-filter > span.price-total-count").text.gsub("Items", "").to_i
   slider_value = $driver.find_element(:css,"#price-filter > span.price-total-count").text.to_i  
  puts "slider_value is : #{slider_value}"
   @product_price =[]
  #s = []  
  @error_sale_index = []

  for i in 1..slider_value
    if prices(i)
      @product_price << prices(i)
    #else
      #error_sale_index << i
    end
    $driver.execute_script("window.scrollBy(0,100)", "")
    sleep(2)
  end
  puts "Product prices #{@product_price}"
  puts "Error sale index #{@error_sale_index}"

  flag=0
  for j in 0..slider_value-2
    #puts a = product_price[j]
    #puts b = product_price[j+1]
    if @product_price[j]!=0 && @product_price[j+1]!=0

      if @product_price[j] < @product_price[j+1]
        flag=1
        break
      end 
    end
  end
  if flag==0
    puts "prices are in descend_by_master_price"
  else
    puts "prices are not in descend_by_master_price"
  end


end


  def sort
    puts "clicking on low-high"
    #$driver.find_element(:css,"#sorting > div.pull-right > select > option:nth-child(2)").click
    low_high.click
    sleep(3)


    puts "clicking on high-low"
    #$driver.find_element(:css,"#sorting > div.pull-right > select > option:nth-child(3)").click
    high_low.click
    price_compare


  end




end



launch_styletag
obj3=BusinessAction.new
#obj3.login_positive_scenarios
obj3.adding_to_cart
#obj3.cart
#obj3.function
obj3.search
obj3.sort
#obj3.homePage
#obj3.logout
