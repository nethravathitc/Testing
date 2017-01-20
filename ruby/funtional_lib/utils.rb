module Utils
  def shortwait
    sleep 5
  end

  def longwait
    sleep 10
  end

  def launch_styletag
    @dev_url = "http://22.22.22.22:5000/"
    @base_url = "http://origin-www.stest.in/"
    @live_url = "http://www.styletag.com/"
    # $driver = Selenium::WebDriver.for :firefox

   Selenium::WebDriver::Chrome::Service.executable_path = '/home/rashmi/Documents/chromedriver'
 	$driver = Selenium::WebDriver.for :chrome

    $driver.navigate.to @base_url
    $driver.manage.window.maximize

  end

  def launch_styletagAdmin
    @admin_dev_url = "http://www.yousa-test.com:5600/#/admin/dashboard"
    require "selenium-webdriver"
    $driver = Selenium::WebDriver.for :firefox
    $driver.navigate.to @admin_dev_url
  end

  def c1
    $driver.find_element(:css, "a#women").click
    wait_for_spinner
  end

  def sale
    $driver.find_element(:css, "#sales-container > div > ul > li:nth-child(1) > div.sale-text-wrap.height-auto > div.double-arrow > a > span").click
    wait_for_spinner
    shortwait
    $driver.execute_script("window.scrollBy(0,200)", "")
  end

  def wait_for_spinner
    spinner = $driver.find_element(:css, ".loading-spiner-holder")
    while spinner.displayed?
      sleep(2)
      print "Spinner\n"
    end
  end

  def wait_for_more_spinner
    more_spinner = $driver.find_element(:css, ".loading-filter")
    while more_spinner.displayed?
      sleep  5
      print "Spinner\n"
    end
  end

end

