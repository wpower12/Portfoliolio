package com.poweriii.portfoliolio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    public static StockFragment newInstance(String symbol, String name, double price) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putString(SYMBOL_ARG, symbol);
        args.putString(NAME_ARG, name);
        args.putDouble(PRICE_ARG, price);
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

        // Make the Volley Request
        RequestQueue rq = Volley.newRequestQueue(this.getContext());
        String url = "http://dev.markitondemand.com/Api/v2/InteractiveChart/json";
        JSONObject params = new JSONObject();
        JSONObject elem = new JSONObject();
        try {
            elem.put("Symbol", "AAPL");
            elem.put("Type", "price");
            params.put("Normalized", true)
                    .put("NumberOfDays", 1)
                    .put("DataPeriod", "Hour")
                    .put("DataInterval", 1)
                    .put("Elements", new JSONArray().put(elem));

            Log.d("IDK Obj", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("IDK Success", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("IDK Error", error.toString());
                    }
                }
        );
        rq.add(jsonRequest);

        // On success, it should call placeStockData()

        // placeStockData should parse json

        // use json to build graph

        // place graph into the graphview


        return l;
    }

}
