
package com.aem.challenge.core.result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrencyResult {
	
	private static final Logger LOG = LoggerFactory.getLogger(CurrencyResult.class);
	
	private List<Quote> quotes = new ArrayList<>();
	
    private QuoteBest bestQuote;
	
    private String currency;
	
    private String date;
	
	public static class Quote {

	    private String time;

	    private String price;
	   
	    public String getTime() {
			return time;
		}

		public double getPrice() {
			return Double.parseDouble(this.price); 
		}

		public void setTime(String time) {
			this.time = time;
		}

		public void setPrice(String price) {
			this.price = price;
		}
	}
	
	public static class QuoteBest {
		
	    private Quote sell;
		
	    private Quote buy;
		
	    private String diff;
		
		public Quote getSell() {
			return sell;
		}

		public Quote getBuy() {
			return buy;
		}

		public void setSell(Quote sell) {
			this.sell = sell;
		}

		public void setBuy(Quote buy) {
			this.buy = buy;
		}

		public String getDiff() {
			return diff;
		}

		public void setDiff(String diff) {
			this.diff = diff;
		}
	}

	public List<Quote> getQuotes() {
		return quotes;
	}

	public void setQuotes(List<Quote> quotes) {
		this.quotes = quotes;
	}

	public String getCurrency() {
		return currency;
	}

	public String getDate() {
		return (date != null) ? date : "";
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public static final Comparator<Quote> compareTime = 
			new Comparator<Quote>() {
		public int compare(Quote e1, Quote e2) {
			try{
				return e1.getTime().compareTo(e2.getTime());
			}catch(Exception e){
				LOG.error(":: Exception occurred to compare the quotes ::"+e);
			}
			return 0;
			
		}
	};

	public QuoteBest getBestQuote() {
		return bestQuote;
	}

	public void setBestQuote(QuoteBest bestQuote) {
		this.bestQuote = bestQuote;
	}
	
}
