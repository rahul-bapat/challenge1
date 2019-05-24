package com.aem.challenge.core.models;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import com.aem.challenge.core.result.CurrencyResult;
import com.aem.challenge.core.service.CryptoCurrencyService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;



@Model(adaptables = {SlingHttpServletRequest.class, Resource.class},
	adapters = com.aem.challenge.core.models.CryptoCurrencyComponentModel.class,
	resourceType="crypto-test/components/content/crypto-detail",
	defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CryptoCurrencyComponentModel {
	
	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@SlingObject
    private SlingHttpServletRequest request;
	
	@Inject
	private CryptoCurrencyService service;
	
	private  Resource res;
	
	public List<CurrencyResult> currencyResults;
	
	public String cryptoDataString;
	
	
	private int totalCount = 0;
	
	@ValueMapValue 
	private String cryptocode;
	
	private String loggedIn = "false";		

	public String getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(String loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	@PostConstruct
	protected void init() {	
		try{	
			this.setRes(request.getResource());
			this.setCurrencyResults(service.getBestResultsList());
			this.setCryptoDataString(service.getData(cryptocode));
		}
		catch(Exception e){
			LOG.error("::Exception occured to fetch the results from ES::"+e);
		}
	}

	public Resource getRes() {
		return res;
	}

	public void setRes(Resource res) {
		this.res = res;
	}

	public CryptoCurrencyService getService() {
		return service;
	}

	public void setService(CryptoCurrencyService service) {
		this.service = service;
	}

	public List<CurrencyResult> getCurrencyResults() {
		return currencyResults;
	}

	public void setCurrencyResults(List<CurrencyResult> currencyResults) {
		this.currencyResults = currencyResults;
	}
	public String getCryptoDataString() {
		return cryptoDataString;
	}

	public void setCryptoDataString(String cryptoDataString) {
		this.cryptoDataString = cryptoDataString;
	}


}
