package com.poweriii.portfoliolio;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class PortfolioActivity extends Activity implements PortfolioListFragment.PortfolioInterface {

    private static String[] testSymbols;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTestSymbols();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        fm = getFragmentManager();
        fm.beginTransaction()
          .replace(R.id.main_view, PortfolioListFragment.newInstance(testSymbols))
          .commit();

    }


    public static void setTestSymbols() {
        testSymbols = new String[4];
        testSymbols[0] = "APPL";
        testSymbols[1] = "MSFT";
        testSymbols[2] = "XMA";
        testSymbols[3] = "MMA";
    }

    @Override
    public void setDetail(String symbol) {

        // TODO - Check that detail exists, if not, overwrite master.

        fm.beginTransaction()
                .replace(R.id.detail_view, StockFragment.newInstance(symbol, "NAME", 0.50))
                .commit();
    }
}
