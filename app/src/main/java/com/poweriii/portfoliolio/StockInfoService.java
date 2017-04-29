package com.poweriii.portfoliolio;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wkp3 on 4/25/17.
 */

public class StockInfoService extends Service {

    private final String QUOTE_URL = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=";

    private final IBinder mBinder = new StockInfoBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class StockInfoBinder extends Binder {
        StockInfoService getService(){
            return StockInfoService.this;
        }
    }

    public void sendStockRequest(final String symbol){
        // TODO - add the stuff to actually make the JSON call.
        Log.d("StockService", "Called get Stock");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, QUOTE_URL+symbol, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SIS IDK", "onResponse: "+response.toString());

                        // Create stock
                        try {
                            String name = response.getString("Name");
                            Double price = response.getDouble("LastPrice");
                            Stock s = new Stock( symbol, name, price );

                            Intent intent = new Intent();
                            intent.setAction("com.poweriii.portfoliolio.STOCKREADY");
                            intent.putExtra("STOCK", s);
                            sendBroadcast(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(jsObjRequest);
    }

}
