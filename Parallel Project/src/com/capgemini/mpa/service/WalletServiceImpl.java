package com.capgemini.mpa.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.capgemini.mpa.beans.Customer;
import com.capgemini.mpa.beans.Wallet;
import com.capgemini.mpa.exception.InsufficientBalanceException;
import com.capgemini.mpa.exception.InvalidInputException;
import com.capgemini.mpa.repo.WalletRepo;
import com.capgemini.mpa.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService 
{
private Map<String, Customer> data = new HashMap<String, Customer>();
	
	WalletRepo walletdao ;
	{
		walletdao = new WalletRepoImpl(data);
	}
	
	@Override
	public Customer createAccount(String name, String mobileno, BigDecimal amount)
	{
	
		if(walletdao.findOne(mobileno)==null)
		{
			if(isMobileNumberInvalid(mobileno))
			{
			Customer customer = new Customer(name, mobileno, new Wallet((amount)));
			walletdao.save(customer);
			data.put(mobileno, customer);
			return customer;
			}
			else
			{
				throw new InvalidInputException("\nInvalid Mobile Number\n");
			}
		}
		else
		{
			throw new InvalidInputException("\nAccount Already Exists\n");
		}
	
	}

	@Override
	public Customer showBalance(String mobileno) 
	{
		Customer customer = walletdao.findOne(mobileno);
		if(customer != null)
		return customer;
		else
			throw new InvalidInputException("\nAccount doesn't exist for this Mobile Number\n");
	}

	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) 
	{
		Customer customer1 = data.get(sourceMobileNo);
		
		Customer customer2 = data.get(targetMobileNo);
		if((customer1!=null)&(customer2!=null)) {
		BigDecimal Amount = amount;
		Wallet w1=customer1.getWallet();
		Wallet w2=customer2.getWallet();
		BigDecimal balance1=w1.getBalance();
		BigDecimal balance2=w2.getBalance();
		if(balance1.compareTo(amount)>0)
		{
		customer1.setWallet(new Wallet(balance1.subtract(amount)));
		customer2.setWallet(new Wallet(balance2.add(amount)));
		System.out.println("Amount transfered to Account " + targetMobileNo + " Successfully");
		return customer1;
		}
		else
		{
			throw new InsufficientBalanceException("\nNo Sufficient Balance in Your Account\n");
		}
		}
		else
		{
			return null;
		}
	}

	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) 
	{
		Customer customer = data.get(mobileNo);
		Wallet wallet = customer.getWallet();
		BigDecimal balance = wallet.getBalance();
		BigDecimal DAmount = balance.add(amount);
		customer.setWallet(new Wallet(DAmount));
		return customer;
	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) 
	{
		Customer customer = data.get(mobileNo);
		Wallet wallet = customer.getWallet();
		BigDecimal Amount=new BigDecimal (0);
		BigDecimal balance = wallet.getBalance();
		BigDecimal WAmount = balance.subtract(amount);
		
		
		if(WAmount.compareTo(Amount)>0)
		{
			customer.setWallet(new Wallet(WAmount));
		
			return customer;
	}
		else
		{
			throw new InsufficientBalanceException("\nNo Sufficient Balance in Your Account\n");
		}
	}

	@Override
	public boolean isValid(Customer customer) throws InvalidInputException
	{
		if(customer == null)
			throw new InvalidInputException("Customer Instance cannot be null");
		if(customer.getName() == null || customer.getName().trim().isEmpty())
			throw new InvalidInputException("Customer name cannot be Empty");
		if(customer.getMobileNo()==null || isMobileNumberInvalid(customer.getMobileNo()))
			throw new InvalidInputException("Mobile Number is Invalid");
		return true;
	}
	public static boolean isMobileNumberInvalid( String mobileNo )
	{
		String patterns = "[1-9][0-9]{9}";
		
		if (mobileNo.matches(patterns)) 
		{
			return true;
		} 
		else
		{
			return false;
		}
	}
}