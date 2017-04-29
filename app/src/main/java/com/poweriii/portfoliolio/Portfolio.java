package com.poweriii.portfoliolio;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wkp3 on 4/25/17.
 */

public class Portfolio implements Serializable {
    public ArrayList<Stock> stocks;

    public Portfolio(){
        stocks = new ArrayList<>();
    }

    public void addStock( Stock s ){
        stocks.add(s);
    }
}
