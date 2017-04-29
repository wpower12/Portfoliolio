package com.poweriii.portfoliolio;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.text.format.DateFormat;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link StockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockFragment extends Fragment {
    private static final String SYMBOL_ARG = "symbol";
    private static final String NAME_ARG = "name";
    private static final String PRICE_ARG = "price";

    private String mSymbol;
    private String mName;
    private double mPrice;

    public StockFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment StockFragment.
     */
    public static StockFragment newInstance(Stock s) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putString(SYMBOL_ARG, s.mStockSymbol);
        args.putString(NAME_ARG, s.mStockName);
        args.putDouble(PRICE_ARG, s.mStockPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(SYMBOL_ARG);
            mName = getArguments().getString(NAME_ARG);
            mPrice = getArguments().getDouble(PRICE_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View l = inflater.inflate(R.layout.fragment_stock, container, false);

        // 1 day chart
        String url = "https://chart.yahoo.com/z?t=1d&s="+mSymbol;
        ImageView iv = (ImageView)l.findViewById(R.id.imageView);
        Picasso.with(this.getContext()).load(url).into(iv);

        String price = String.format("$%1$.2f", mPrice);
        ((TextView)l.findViewById(R.id.textCompanyName)).setText(mName);
        ((TextView)l.findViewById(R.id.textCompanyPrice)).setText(price);
        return l;
    }

}
