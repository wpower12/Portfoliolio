package com.poweriii.portfoliolio;

import java.io.Serializable;

/**
 * Created by wkp3 on 4/25/17.
 */

public class Stock implements Serializable {
    String mStockSymbol;
    String mStockName;
    double mStockPrice;

    public Stock( String symbol, String name, double price ){
        mStockSymbol = symbol;
        mStockName   = name;
        mStockPrice  = price;
    }

}
