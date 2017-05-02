package com.poweriii.portfoliolio;

import android.app.Service;
import android.content.Context;
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by wkp3 on 4/25/17.
 */

public class StockInfoService extends Service {

    private final String QUOTE_URL = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=";
    private final IBinder mBinder = new StockInfoBinder();
    private Portfolio mPortfolio;
    private Thread mLoopThread;
    private final long TIMER_INTERVAL = 60000L;

    @Override
    public IBinder onBind(Intent intent) {
        if( mLoopThread == null ){
            mLoopThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while( true ){
                        if( mLoopThread.isInterrupted() ){
                            return;
                        }
                        updatePortfolioRequest();
                        try {
                            Thread.sleep(TIMER_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        mLoopThread.start();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mLoopThread.interrupt();
        return super.onUnbind(intent);
    }

    public class StockInfoBinder extends Binder {
        StockInfoService getService(){
            return StockInfoService.this;
        }
    }

    // Exposed Methods - The JSON Request *********************************************************

    public void sendStockRequest(final String symbol){
        Log.d("StockService", "Making json request to miod");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, QUOTE_URL+symbol, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SIS IDK", "onResponse: "+response.toString());
                        try {
                            Intent intent = new Intent();
                            intent.setAction("com.poweriii.portfoliolio.STOCKREADY");
                            if( response.isNull("Message") ){
                                String name = response.getString("Name");
                                Double price = response.getDouble("LastPrice");
                                Stock s = new Stock( symbol, name, price );
                                intent.putExtra("VALID", true);
                                intent.putExtra("STOCK", s);
                            } else {
                                Log.d("SIS IDK", "Response came back with invalid symbol");
                                intent.putExtra("VALID", false);
                            }
                            sendBroadcast(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SIS IDK", "Volley failed to make JSON request");
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(jsObjRequest);
    }

    // Helper Methods ******************************************************************************

    private void finalizeUpdatePortfolio(){
        writePortfolio(mPortfolio);
        Intent intent = new Intent();
        intent.setAction("com.poweriii.portfoliolio.PORTFOLIO");
        sendBroadcast(intent);
    }

    public void updatePortfolioRequest(){
        // Read in portfolio object
        mPortfolio = readPortfolio();

        // If the File doesnt exist yet, just stop.
        if( mPortfolio == null ){
            return;
        }

        RequestQueue rq = Volley.newRequestQueue(this);
        for(int i = 0; i < mPortfolio.stocks.size(); i++ ){
            final int stock_id = i;
            String symbol = mPortfolio.stocks.get(i).mStockSymbol;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, QUOTE_URL+symbol, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("SIS IDK", "Updating a stock");
                            try {
                                if( response.isNull("Message") ){
                                    Double price = response.getDouble("LastPrice");
                                    mPortfolio.stocks.get(stock_id).mStockPrice = price;
                                } else {
                                    Log.d("SIS IDK", "Response came back with invalid symbol");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if( stock_id == mPortfolio.stocks.size()-1 ){
                                // Last request?
                                finalizeUpdatePortfolio();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("SIS IDK", "Volley failed to make JSON request");
                        }
                    });
            rq.add(jsObjRequest);
        }
    }

    private void writePortfolio( Portfolio p ){
        try {
            ObjectOutputStream os = new ObjectOutputStream(openFileOutput(PortfolioActivity.FILE_URI,
                                                           Context.MODE_PRIVATE));
            os.writeObject( p );
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Portfolio readPortfolio(){
        Portfolio ret = null;
        try {
            ObjectInputStream is = new ObjectInputStream(openFileInput(PortfolioActivity.FILE_URI));
            ret = (Portfolio)is.readObject();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
