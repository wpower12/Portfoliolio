package com.poweriii.portfoliolio;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PortfolioListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PortfolioListFragment extends Fragment {

    private static final String ARG_PORTFOLIO = "arg_p";
    private StockListAdapter mListAdapter;
    private Portfolio portfolio;

    public PortfolioListFragment() {
        // Required empty public constructor
    }

    public static PortfolioListFragment newInstance(Portfolio p) {
        PortfolioListFragment fragment = new PortfolioListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PORTFOLIO, p);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            portfolio = (Portfolio)getArguments().getSerializable(ARG_PORTFOLIO);
        }
        mListAdapter = new StockListAdapter( this.getContext(), portfolio );
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View l = inflater.inflate(R.layout.fragment_portfolio_list, container, false);

        ListView lv = ((ListView)l.findViewById(R.id.list_view));
        lv.setAdapter(mListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((PortfolioInterface)getActivity()).setDetail( position );
            }
        });
        return l;
    }

    public void updateNameList( ){
        mListAdapter.notifyDataSetChanged();
    }

    // Need to communicate with parent - Interface
    public interface PortfolioInterface {
        void setDetail(int position);
        void requestNewStock(String s);
    }
}
