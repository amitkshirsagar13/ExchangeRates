package org.exchange.rates;

/**
 * <p>
 * <b>Overview:</b>
 * <p>
 *
 *
 * <pre>
 * Creation date: May 18, 2015
 * @author Amit Kshirsagar
 * @email amit.kshirsagar.13@gmail.com
 * @version 1.0
 * @since
 *
 * <p><b>Modification History:</b><p>
 *
 *
 * </pre>
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import org.getrate.inr.GetIndiaExchangeRate;
import org.getrate.inr.RateList;
import org.getrate.inr.RateModel;

import java.util.*;

public class ExchangeRateDisplay extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchangeratedisplay);
    }


    public void getExchangeRate(View view) {

        ExchangeDBHelper exchangeDBHelper = new ExchangeDBHelper(this);

        RateList rateList = exchangeDBHelper.getExchangeRates();

        Log.d("ExchangeRateDisplay", "rateList.size(): " + rateList.size());

        CheckBox forceRefresh = (CheckBox) findViewById(R.id.forceRefresh);

        if (!forceRefresh.isChecked() && rateList != null && rateList.size() > 0 && rateList.get(0) != null && rateList.get(0).getDate() != null && rateList.get(0).getDate().equals(exchangeDBHelper.getStringDate(new Date()))) {

            Log.d("MyLogger", "Getting rate from existing query...");
            ArrayList<HashMap> list = new ArrayList<HashMap>();
            for (Iterator iterator = rateList.iterator(); iterator.hasNext(); ) {
                RateModel rateModel = (RateModel) iterator.next();
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put(ExchangeContract.Columns.COMPANY, rateModel.getCompany());
                hashMap.put(ExchangeContract.Columns.RATE, rateModel.getRate());
                list.add(hashMap);
            }

            ExchangeViewListAdapter adapter = new ExchangeViewListAdapter(this, list);

            ListView listView = (ListView) findViewById(R.id.rateListView);
            listView.setAdapter(adapter);
        } else {
            exchangeDBHelper.deleteRates();
            Log.d("MyLogger", "Getting rate from fresh query...");
            GetDataThread getData = new GetDataThread(this, this);
            getData.execute();
        }


    }

    class GetDataThread extends AsyncTask<String, Void, Map<String, RateList>> {
        Context ctx;
        Activity activity;
        RateList rateList;

        public GetDataThread(Context ctx, Activity activity) {
            this.ctx = ctx;
            this.activity = activity;
        }

        @Override
        protected Map<String, RateList> doInBackground(String... params) {

            Map<String, RateList> map = new HashMap<String, RateList>();
            GetIndiaExchangeRate exchangeRate = new GetIndiaExchangeRate();

            Log.d("MyLogger", "Get Rates");
            RateList rateList = exchangeRate.retriveExchangeRates();

            map.put("rateList", rateList);
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, RateList> rateListMap) {
            super.onPostExecute(rateListMap);


            ArrayList<HashMap> list = new ArrayList<HashMap>();

            Log.d("MyLogger", "Post Rates");
            int counter = 0;
            RateList rateList = rateListMap.get("rateList");

            ExchangeDBHelper exchangeDBHelper = new ExchangeDBHelper(activity);
            exchangeDBHelper.addExchangeRates(rateList);


            for (Iterator iterator = rateList.iterator(); iterator.hasNext(); ) {
                RateModel rateModel = (RateModel) iterator.next();
                Log.d("MyLogger", rateModel.getRate() + " | " + rateModel.getCompany());
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put(ExchangeContract.Columns.COMPANY, rateModel.getCompany());
                hashMap.put(ExchangeContract.Columns.RATE, rateModel.getRate());
                list.add(hashMap);
            }

            try {
                ExchangeViewListAdapter adapter = new ExchangeViewListAdapter(activity, list);
                ListView listView = (ListView) findViewById(R.id.rateListView);

                listView.setAdapter(adapter);
            } catch (Exception e) {
                Log.d("MyLogger", "Exception: " + e.getMessage());
            }
            this.rateList = rateList;
            Log.d("MyLogger", "Got Rates");
        }

        public RateList getRateList() {
            return rateList;
        }
    }

}
