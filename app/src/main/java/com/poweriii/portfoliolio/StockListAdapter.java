package com.poweriii.portfoliolio;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by wkp3 on 4/20/17.
 */

public class StockListAdapter extends BaseAdapter {

    private Context ctx;
    private String[] names;

    public StockListAdapter(Context c, String[] n ){
        ctx = c;
        names = n;
    }

    @Override
    public int getCount() {
        return names.length;
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
        t.setText( names[position] );
        t.setGravity(Gravity.CENTER_HORIZONTAL);
        t.setTextSize(24);
        t.setPadding(10, 8, 10, 8);
        return t;
    }


}
