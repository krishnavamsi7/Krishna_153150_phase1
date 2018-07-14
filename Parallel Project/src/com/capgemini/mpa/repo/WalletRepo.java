package com.capgemini.mpa.repo;

import com.capgemini.mpa.beans.Customer;

public interface WalletRepo 
{
public boolean save(Customer customer);
	
	public Customer findOne(String mobileNo);
}
