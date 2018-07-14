package com.capgemini.mpa.repo;

import java.util.Map;

import com.capgemini.mpa.beans.Customer;

public class WalletRepoImpl implements WalletRepo 
{
	private Map<String, Customer> data;
	
	public WalletRepoImpl(Map<String, Customer> data) 
	{
		super();
		this.data = data;
	}
	
//	public WalletRepoImpl() 
//	{
//		
//	}
	@Override
	public boolean save(Customer customer) 
	{
		data.put(customer.getMobileNo(), customer);
		return true;
	}

	@Override
	public Customer findOne(String mobileNo) 
	{
		Customer customer = data.get(mobileNo);
		return customer;
	}

}
