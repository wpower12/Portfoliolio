package com.poweriii.portfoliolio;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by wkp3 on 4/20/17.
 */

public class StockListAdapter extends BaseAdapter {

    private Context ctx;
    private Portfolio portfolio;
    private LayoutInflater mInflater;

    public StockListAdapter(Context c, Portfolio p ){
        ctx = c;
        portfolio = p;
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return portfolio.stocks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t = new TextView(this.ctx);
        t.setText( portfolio.stocks.get(position).mStockSymbol.toUpperCase() );
        t.setGravity(Gravity.CENTER_HORIZONTAL);
        t.setTextSize(24);
        t.setElevation(5);
        return t;
    }


}
