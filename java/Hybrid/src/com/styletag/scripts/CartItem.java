package com.styletag.scripts;

public class CartItem {
	String name;
	int quantity;
	int price;
	int shipping;
	String size;
public void setName(String productname)
{
	name=productname;
}
public void setQuantity(int productquantity)
{
	quantity=productquantity;
}

public void setPrice(int productprice)
{
	price=productprice;
}
public void setShipping(int productshipping)
{
	shipping=productshipping;
}
public void setSize(String productsize)
{
	size=productsize;
}
public String getSize()
{
	return size;
}
public String getName()
{
	return name;
}
public int getQuantity()
{
	return quantity;
}
public int getPrice()
{
	return price;
}
public int getShipping()
{
	return shipping;
}

}
