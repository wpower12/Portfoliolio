package com.poweriii.portfoliolio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PortfolioListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PortfolioListFragment extends Fragment {

    private static final String ARG_NAMES = "arg_names";
    private String[] mNames;
    private StockListAdapter mListAdapter;

    public PortfolioListFragment() {
        // Required empty public constructor
    }

    public static PortfolioListFragment newInstance(String[] names) {
        PortfolioListFragment fragment = new PortfolioListFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_NAMES, names);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNames = getArguments().getStringArray(ARG_NAMES);
        }
        mListAdapter = new StockListAdapter( this.getContext(), mNames );
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
                ((PortfolioInterface)getActivity()).setDetail( mNames[position] );
            }
        });

        return l;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate( R.menu.action_menu_view, menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == R.id.new_stock ){
            Toast.makeText(this.getContext(), "Clicked!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Need to communicate with parent - Interface
    public interface PortfolioInterface {
        void setDetail(String symbol);
    }
}
