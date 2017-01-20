module Css
	[ #c1's
    {:name => "women", :css => "#women"}, #women
    {:name => "best_seller", :css => "#best-seller"},  #best-seller

    #sale_catalog page
	{:name => "first_sale", :css => "#sales-container > div > ul > li:nth-child(1) div.double-arrow > a"},
    
    #product_catalog page
	{:name => "first_product", :css => "#product-container > div > ul > li.product-wrap:nth-child(5) > div.products-wrapper .go-to-product"},                                        
       
    #register_page
	{:name => "register_page_title", :css => "#st-register > h1"},	
	{:name => 'register_link', :css => "#account-wrap > div > div:nth-child(3) > a" }, 
	{:name => "register_name" , :css => "#register_name"}, 
	{:name => "register_email", :css => "#register_email"},
	{:name => "register_password", :css => "#register_password"}, 
	{:name => "register_button", :css => "#st-register"},
	{:name => "login_here", :css => "#st-register > div.register-cedentials.ng-scope > p > a"}, 
	{:name => "r_login_using_fbook", :css => "#st-register > div.social-login > a:nth-child(1) > img"}, 
	{:name => "r_login_using_gplus", :css => "#st-register > div.social-login > a:nth-child(2) > img"}, 

	#login_page
	{:name => 'login_link', :css => "#account-wrap > div > div:nth-child(1) > a"},
	{:name => "login_email", :css => "#login_email"},
    {:name => "login_password", :css => "#login_password"},
    {:name => "login_btn", :css => "#login-btn"},
    {:name => "forgot_pwd", :css => "#login-form > p.text-right.forgot-pwd > a"},
    {:name => "register_here", :css => "#login-form > p.member-yn > a"},
    {:name => "l_login_using_fbook", :css => "#st-login > div.social-login > a:nth-child(1) > img"},
    {:name => "l_login_using_gplus", :css => "#login_password"},
    
    #header facebook link
    {:name => "facebook_login_link", :css => "#profile-log-reg > div > a:nth-child(1) > img"}, #header: facebook_login
    {:name => "email", :css => "#email"},
    {:name => "pass", :css => "#pass"},
    {:name => "loginbutton", :css => "#u_0_2"},
     
    #header gplus link
    {:name => "gpluslogin", :css => "#profile-log-reg > div > a:nth-child(2) > img"}, #header_g+login
    {:name => "gemail", :css => "#Email"},
    {:name => "nextbutton", :css => "#next"},
    {:name => "passwd", :css => "#Passwd"},
    {:name => "signin", :css => "#signIn"},

    #checkout/email
    {:name => "checkout_login_email", :css => "#checkout-login_email"},
    {:name => "continue_guest_btn", :css => "#continue-guest-btn"},
    {:name => "select_guest", :css => "#checkout-login_false"},
    
    #header - account_section
    {:name => "account_member_name", :css => "#account-member-name"},
    {:name => "account_member_name_caret", :css => "#account-member-name > .caret"},
    {:name => "logout", :css => "#account-wrap > div > div:nth-child(1) > div.account-inline.ng-scope > ul > li.show-profile-inner-logout > a"},
    {:name => "account_wrap", :css => "#account-wrap > div"},
 
    #search field                                
    {:name => "search_field", :css => "#aws_form_search > #appendedInputButtons"}, #search input text
    {:name => "search_button", :css => "#aws_form_search > #submitButton"}, #search button
    
    #product_view page
    {:name => "add_to_cart_button", :css => "#add-to-cart-button"}, # add_to_cart button
    {:name => "productviewpage_productname", :css => "#sale-main-desc > div.cart-form.pull-left > h1"},
    {:name => "size", :css => ".in-stock:nth-child(2) div"}, #size select
    #mini_cart
    {:name => "mini_cart_text", :css => ".mini-cart-text"},   
    
    #cart_page
    {:name => "proceed_to_checkout", :css => "#cart-checkout-btn"}, #proceed_to_checkout button
    {:name => "cartpage_productname", :css => "#cart_product_1 > div:nth-child(2) > p:nth-child(1) > a"},
    
    #checkout/email
    {:name => "continue", :css => "#open-two"},  #continue_button
    
    #checkout/address
    {:name => "add_address_checkout", :css => "#add-address-checkout"}, # add_address
    {:name => "address_pincode", :css => "#address_pincode"},
    {:name => "firstname", :css => "#firstname"},
    {:name => "address_phone", :css => "#address-phone"},
    {:name => "address_address1", :css => "#address_address1"},
    {:name => "address_landmark", :css => "#address_landmark"},
    {:name => "create_btn", :css => "#create-btn"},
    {:name => "select_address", :css => "#address-body a.overflow-address :nth-child(1)"},
    {:name => "continue3", :css => "#open-three"},
    {:name => "apply_coupon_code", :css => "input.apply_coupon_code"},
    
    #payment_page
    #debit_card fields
    {:name => "debit_card_payment", :css => "#debit-card-payment > p"},
    {:name => "debit_card_number", :css => "#debit_card_number"},
    {:name => "debit_card_name", :css => "#debit_card_name"},
    {:name => "debit_card_exp_month", :css => "#debit_card_exp_month"},
    {:name => "debit_card_exp_year", :css => "#debit_card_exp_year"},
    {:name => "debit_card_ccv", :css => "#debit_card_ccv"},
    {:name => "debitCardButton", :css => "#debitCardButton"},     
    #payment - COD fields
    {:name => "cod_payment", :css => "#cod-payment"},   
    {:name => "codbutton", :css => "#codButton"},
    #styletag wallet
    {:name => "wallet_payment", :css => "#wallet-payment"},
    {:name => "walletsButton", :css => "input#walletsButton"},
    #credit_card fields
    {:name => "credit_card_number", :css => "#credit_card_number"},
    {:name => "credit_card_name", :css => "#credit_card_name"},
    {:name => "credit_card_exp_month", :css => "#credit_card_exp_month"},
    {:name => "credit_card_exp_year", :css => "#credit_card_exp_year"},
    {:name => "credit_card_ccv", :css => "#credit_card_ccv"},
    {:name => "creditCardButton", :css => "#creditCardButton"},
    {:name => "thanku_header", :css => "#thanku_header"},
     
    #checkout/order
    {:name => "proceed_pay_btn", :css => ".proceed-pay-btn"},
    {:name => "order_number", :css => "#order-cancel > div > section > p:nth-child(2) > span"},
    
    #my_account
    #my_orders page
    {:name => "my_order", :css => "#account-wrap > div > div:nth-child(1) > div.account-inline.ng-scope > ul > li.show-profile-inner.my-order > a"},
    {:name => "allorder_page_ordernumber", :css => "#orders-information > section > div:nth-child(1) > div.thanku-inner-header > div.bit-2.padding-zero.pull-left.text-left > p.text-uppercase.margin-th-each > strong"},
    {:name => "allorder_page_orderstatus", :css => "#orders-information > section > div:nth-child(1) > div.thanku-inner-header > div.bit-2.padding-zero.pull-left.text-left > p.text-capitalize > strong"},
    {:name => "pending_orders", :css => "#tabs > ul > li:nth-child(2) > a"},
    {:name => "pendingorders_ordernumber", :css => "#orders-information > section > div:nth-child(1) > div.thanku-inner-header > div.bit-2.padding-zero.pull-left.text-left > p.text-uppercase.margin-th-each > strong"}
    
    ].each do |record|
		define_method "#{record[:name]}" do |*args|
		$driver.find_element(:css, record[:css])
		end   
	end
	# creating dynamic methods 
	# creats method at run time
	# array of hashes
	#{:name => "", css => ""},
end
