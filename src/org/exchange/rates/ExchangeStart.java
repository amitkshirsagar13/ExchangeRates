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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ExchangeStart extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start);

        Log.d("MyLogger", "Is this coming in log?");


    }

    public void getRatePage(View view) {
        startActivity(new Intent(this, ExchangeRateDisplay.class));
    }

    public void getLocationDetails(View view) {


        GetRestDataThread getData = new GetRestDataThread();
        getData.execute(((EditText) findViewById(R.id.zipCode)).getText().toString());

        Log.d("MyLogger", "Click Worked");

    }

    class GetRestDataThread extends AsyncTask<String, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(String... params) {

            Map<String, String> map = new HashMap<String, String>();
            try {

                URL url = new URL(
                        "http://api.zippopotam.us/us/" + params[0]);
                Log.d("MyLogger", "Log1");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.d("MyLogger", "Log2");

                if (conn.getResponseCode() != 200) {

                    Log.d("MyLogger", "Log3 Error");
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                Log.d("MyLogger", "Log4");
                String output;
                StringBuffer responseString = new StringBuffer();
                while ((output = br.readLine()) != null) {
                    responseString.append(output);
                }
                JSONObject responseJson = new JSONObject(responseString.toString());

                Log.d("MyLogger", "Log5");
                String country = responseJson.getString("country");
                String state = ((JSONObject) responseJson.getJSONArray("places").get(0)).getString("state");
                String city = ((JSONObject) responseJson.getJSONArray("places").get(0)).getString("place name");
                String longitude = ((JSONObject) responseJson.getJSONArray("places").get(0)).getString("longitude");
                String latitude = ((JSONObject) responseJson.getJSONArray("places").get(0)).getString("latitude");

                Log.d("MyLogger", "Log6");
                map.put("country", country);
                map.put("state", state);
                map.put("city", city);
                map.put("longitude", longitude);
                map.put("latitude", latitude);

                Log.d("MyLogger", "Log7");

                Log.d("MyLogger", responseJson.toString());
                conn.disconnect();


            } catch (Exception e) {
                Log.d("MyLogger", "Exception not null");
            }


            return map;
        }

        @Override
        protected void onPostExecute(Map<String, String> stringStringMap) {
            super.onPostExecute(stringStringMap);

            TextView country = (TextView) findViewById(R.id.countryValue);
            TextView state = (TextView) findViewById(R.id.stateValue);
            TextView city = (TextView) findViewById(R.id.cityValue);
            TextView lang = (TextView) findViewById(R.id.langValue);
            TextView lat = (TextView) findViewById(R.id.latValue);
            country.setText(stringStringMap.get("country"));
            state.setText(stringStringMap.get("state"));
            city.setText(stringStringMap.get("city"));
            lang.setText(stringStringMap.get("longitude"));
            lat.setText(stringStringMap.get("latitude"));
        }
    }

}
