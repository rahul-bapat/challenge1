package com.aem.challenge.core.service;

import java.util.List;

import com.aem.challenge.core.result.CurrencyResult;


public interface CryptoCurrencyService {

	public String getFileURL();
	
	public List<CurrencyResult> getBestResultsList();
	
	public String getData(String cryptoType);
	
}