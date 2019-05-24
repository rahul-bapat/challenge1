package com.aem.challenge.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.challenge.core.models.Crypto;
import com.aem.challenge.core.result.CurrencyResult;
import com.aem.challenge.core.result.CurrencyResult.Quote;
import com.aem.challenge.core.result.CurrencyResult.QuoteBest;
import com.aem.challenge.core.service.CryptoCurrencyService;
import com.google.gson.Gson;

@Designate(ocd = CryptoCurrencyServiceImpl.Config.class)
@Component(service = CryptoCurrencyService.class, immediate = true)
public class CryptoCurrencyServiceImpl  implements CryptoCurrencyService {

	private static Gson gsonInstance;
	private String data = "";

	private static final Logger LOG = LoggerFactory.getLogger(CryptoCurrencyServiceImpl.class);
	public static final String SERVICE_NAME = "Crypto Currency Service";
	public static final String SERVICE_DESCRIPTION = "Crypto Currency API Service";
	DecimalFormat df = new DecimalFormat("#.##"); 

	List<CurrencyResult> bestResultsList = new LinkedList<CurrencyResult>();

	@ObjectClassDefinition(name = "Crypto Currency Configuration Service", description = "Crypto Currency Configuration Service")
	public static @interface Config {

		@AttributeDefinition(name = "File URL", description = "Input FIle URL", defaultValue = "20180507.json")
		String propertyFileURL() default "20180507.json";

	}

	protected String fileURL;

	@Activate
	protected void activate(final Config config) throws Exception {
		this.fileURL = config.propertyFileURL();
		gsonInstance = new Gson();
	}

	public String getFileURL() {
		return fileURL;
	}

	private List<CurrencyResult> getResultData(String data){
		bestResultsList = new LinkedList<CurrencyResult>();
		CurrencyResult[] cryptoList = gsonInstance.fromJson(data, CurrencyResult[].class);
		for(CurrencyResult result : cryptoList){
			List<Quote> quotes = result.getQuotes();
			Collections.sort(quotes, CurrencyResult.compareTime );
			/*finds best buy and sell quotes of each Currency and added them to list*/
			QuoteBest bestQuote = quoteBest(quotes);
			if(bestQuote.getSell() != null && bestQuote.getBuy() != null){
				CurrencyResult bestResult = new CurrencyResult();
				bestResult.setCurrency(result.getCurrency());
				bestResult.setBestQuote(bestQuote);
				bestResultsList.add(bestResult);					
			}
		}
		return bestResultsList;
	}

	private String readInputFileData(){
		InputStream inputStream  = this.getClass().getClassLoader().getResourceAsStream("20180507.json");
		try {
			this.data = IOUtils.toString(inputStream);
		} catch (IOException e) {
			this.data = "";
		}
		return this.data;
	}


	public String getData(String cryptoType) {
		String aCryptoData = "";
		Crypto[] cryptoList = gsonInstance.fromJson(data, Crypto[].class);
		for(Crypto crypto : cryptoList){
			if(crypto.getCurrency().equalsIgnoreCase(cryptoType)){
				aCryptoData = gsonInstance.toJson(crypto);break;
			}
		}
		return aCryptoData;
	}

	private List<CurrencyResult> getAllCryptResults(){
		bestResultsList = new LinkedList<CurrencyResult>();
		this.data = readInputFileData();
		List<CurrencyResult> results = getResultData(this.data);
		for(CurrencyResult result : results){
			List<Quote> quotes = result.getQuotes();
			Collections.sort(quotes, CurrencyResult.compareTime );
			QuoteBest quoteGood = quoteBest(quotes);
			if(quoteGood.getSell() != null && quoteGood.getBuy() != null){
				CurrencyResult bestResult = new CurrencyResult();
				bestResult.setCurrency(result.getCurrency());
				bestResult.setBestQuote(quoteGood);
				bestResultsList.add(bestResult);					
			}
		}
		return bestResultsList;
	}

	private QuoteBest quoteBest(List<Quote> quotes){		
		double diff = Double.MIN_VALUE;
		QuoteBest quoteBest = new QuoteBest();
		Quote sellQuote = null;
		Quote buyQuote = null;
		double currentDiff;
		for(int i=0; i< quotes.size()-1; i++){
			for(int j=i+1; j< quotes.size(); j++ ){
				currentDiff = quotes.get(j).getPrice() - quotes.get(i).getPrice();
				if(currentDiff > diff){
					diff = currentDiff;
					sellQuote = quotes.get(j);
					buyQuote = 	quotes.get(i);		
				}
			}
		}
		if(sellQuote != null && buyQuote != null){
			quoteBest.setDiff(df.format(diff));
			sellQuote.setTime(sellQuote.getTime());
			buyQuote.setTime(buyQuote.getTime());
			quoteBest.setSell(sellQuote);
			quoteBest.setBuy(buyQuote);
		}
		return quoteBest;
	}

	public List<CurrencyResult> getBestResultsList() {
		return getAllCryptResults();
	}

	public void setBestResultsList(List<CurrencyResult> bestResultsList) {
		this.bestResultsList = bestResultsList;
	}
}
