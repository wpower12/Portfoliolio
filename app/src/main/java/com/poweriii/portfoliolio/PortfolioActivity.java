package com.poweriii.portfoliolio;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PortfolioActivity extends Activity implements PortfolioListFragment.PortfolioInterface {

    public final static int BAD_SYMBOL_FLAG = 1;
    public final static String FILE_URI = "stock_data";
    private FragmentManager mFragManager;
    private PortfolioListFragment mPortfolioFragment;

    private StockInfoService mStockInfoService;
    private boolean mBound = false;
    private Portfolio mPortfolio;

    // Helper Class Members ************************************************************************

    private ServiceConnection mStockServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StockInfoService.StockInfoBinder binder = (StockInfoService.StockInfoBinder) service;
            mStockInfoService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    private BroadcastReceiver mNewStockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if( !intent.getBooleanExtra("VALID", false) ){
                Toast.makeText(context, R.string.symbol_not_found, Toast.LENGTH_SHORT).show();
            } else {
                Stock s = (Stock)intent.getSerializableExtra("STOCK");
                addNewStock(s);
            }
        }
    };

    private BroadcastReceiver mUpdatedPortfolioReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            readFile();
        }
    };

    // Main Lifecycle Callbacks ********************************************************************

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, StockInfoService.class);
        bindService(intent, mStockServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        registerReceiver(mNewStockReceiver,
                         new IntentFilter("com.poweriii.portfoliolio.STOCKREADY"));
        registerReceiver(mUpdatedPortfolioReceiver,
                         new IntentFilter("com.poweriii.portfoliolio.PORTFOLIO"));

        mPortfolio = readFile();
        mPortfolioFragment = PortfolioListFragment.newInstance(mPortfolio);
        mFragManager = getFragmentManager();
        mFragManager.beginTransaction()
                .replace(R.id.main_view, mPortfolioFragment)
                .commit();

        if( !mPortfolio.stocks.isEmpty() ){
            setDetail(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNewStockReceiver);
        unregisterReceiver(mUpdatedPortfolioReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mStockServiceConnection);
            mBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == R.id.new_stock ){

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_newstock);
            dialog.setTitle("Enter Stock Symbol");
            final TextView textDialogSymbol = (TextView)dialog.findViewById(R.id.editSymbol);

            // Add New Symbol
            ((Button)dialog.findViewById(R.id.button_add)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String symbol = textDialogSymbol.getText().toString();
                    requestNewStock( symbol );
                    dialog.dismiss();
                }
            });

            // Close Out
            ((Button)dialog.findViewById(R.id.button_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if( item.getItemId() == R.id.delete_portfolio ){
            Log.d("IDK", "Deleting mPortfolio");
            mPortfolio.stocks = new ArrayList<>();
            mPortfolioFragment.updateNameList();
            writeToFile(mPortfolio);
        }
        return super.onOptionsItemSelected(item);
    }

    // PortfolioFragment Interface Methods *********************************************************

    @Override
    public void setDetail(int position) {
        int view_id;
        if( findViewById(R.id.detail_view) != null ){
            view_id = R.id.detail_view;
        } else {
            view_id = R.id.main_view;
        }
        mFragManager.beginTransaction()
                .replace(view_id, StockFragment.newInstance(mPortfolio.stocks.get(position)))
                .addToBackStack("")
                .commit();
    }

    @Override
    public void requestNewStock(String symbol) {
        if( mBound ){
            mStockInfoService.sendStockRequest( symbol );
        }
    }

    // Helper Methods ******************************************************************************

    public void addNewStock(Stock s){
        mPortfolio.addStock(s);
        mPortfolioFragment.updateNameList();
        writeToFile(mPortfolio);
        setDetail(mPortfolio.stocks.size()-1);
    }

    private Portfolio readFile(){
        Portfolio ret = null;
        try {
            ObjectInputStream is = new ObjectInputStream(openFileInput(FILE_URI));
            ret = (Portfolio)is.readObject();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void writeToFile( Portfolio p ){
        try {
            ObjectOutputStream os = new ObjectOutputStream(openFileOutput(FILE_URI, Context.MODE_PRIVATE));
            os.writeObject( p );
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
