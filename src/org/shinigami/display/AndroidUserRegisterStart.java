package org.shinigami.display;

/**
 * <p/>
 * <b>Overview:</b>
 * <p/>
 * <p/>
 * <p/>
 * <pre>
 * Creation date: May 18, 2015
 *
 * @author Amit Kshirsagar
 * @email amit.kshirsagar.13@gmail.com
 * @version 1.0
 * @since <p><b>Modification History:</b><p>
 * <p/>
 * <p/>
 * </pre>
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.exchange.rates.R;
import org.exchange.rates.display.ExchangeRateDisplay;
import org.shinigami.android.db.ShinigamiDBHelper;
import org.shinigami.json.db.ShinigamiJsonDBInstance;
import org.shinigami.json.dto.AndroidApplicationMetadata;
import org.shinigami.json.dto.AndroidUser;
import org.shinigami.json.dto.AndroidUserApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AndroidUserRegisterStart extends Activity {


	boolean isFirstTime = true;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start);

		ShinigamiDBHelper shinigamiDBHelper = new ShinigamiDBHelper(this);

		AndroidUser androidUser = shinigamiDBHelper.getUser(getEmail(), getString(getApplicationInfo().labelRes));
		for (AndroidUserApplication userApplication : androidUser.getApplicationNamesList()){
			if (userApplication.getApplicationName().equalsIgnoreCase(getString(getApplicationInfo().labelRes))){
				isFirstTime = false;
			}
		}

		if (isFirstTime){

			AndroidUserApplication androidUserApplication = new AndroidUserApplication();
			androidUserApplication.setUsedCount(1);
			androidUserApplication.setApplicationName(getString(getApplicationInfo().labelRes));
			androidUserApplication.setUpdated(false);
			androidUserApplication.getComments();
			androidUserApplication.setCrtDate(ShinigamiDBHelper.getCurrentDate());
			androidUserApplication.setLastUpdated(ShinigamiDBHelper.getCurrentDate());
			androidUser.getApplicationNamesList().add(androidUserApplication);
			shinigamiDBHelper.addAndroidUser(shinigamiDBHelper.getWritableDatabase(), androidUser);
		}

		GetDataThread getDataThread = new GetDataThread();

		getDataThread.execute();

	}

	public void getRatePage(View view) {
		startActivity(new Intent(this, ExchangeRateDisplay.class));
	}

	public void firstTimeUser() {

		ShinigamiDBHelper shinigamiDBHelper = new ShinigamiDBHelper(this);
		AndroidApplicationMetadata androidApplicationMetadata = shinigamiDBHelper.getApplicationMetadata(getString(getApplicationInfo().labelRes));


		ShinigamiJsonDBInstance shinigamiJsonDBInstance = ShinigamiJsonDBInstance.getInstance("mongodb://androiduser:androiduser@ds043082.mongolab.com:43082/shinigami", "shinigami");

		ShinigamiJsonDBInstance.QueryUpdate queryUpdate = shinigamiJsonDBInstance.new QueryUpdate();

		AndroidApplicationMetadata androidApplicationMetadataJson = new AndroidApplicationMetadata();
		androidApplicationMetadataJson.setApplicationName(getString(getApplicationInfo().labelRes));
		try {
			androidApplicationMetadataJson = AndroidApplicationMetadata.getInstance(queryUpdate.getRecordForUpdate(ShinigamiJsonDBInstance.COLLECTIONANDROIDVERSION, androidApplicationMetadataJson), AndroidApplicationMetadata.class);
		} catch (JsonProcessingException e) {
			Log.d("AndroidUserRegister", e.getMessage());
		}

		queryUpdate = shinigamiJsonDBInstance.new QueryUpdate();

		AndroidUser androidUser = new AndroidUser();
		androidUser.setLastUpdated(ShinigamiDBHelper.getCurrentDate());
		androidUser.setUpdated(false);
		androidUser.setPhone("1112223333");
		androidUser.setEmail(getEmail());

		AndroidUserApplication androidUserApplicationJson = new AndroidUserApplication();
		androidUserApplicationJson.setApplicationName(getString(getApplicationInfo().labelRes));

		androidUserApplicationJson.setCrtDate(ShinigamiDBHelper.getCurrentDate());
		androidUserApplicationJson.setUpdated(false);
		androidUserApplicationJson.setLastUpdated(ShinigamiDBHelper.getCurrentDate());
		androidUserApplicationJson.getComments().add("Starting Use for " + androidUserApplicationJson.getApplicationName());
		androidUserApplicationJson.setUsedCount(1);

		androidUser.getApplicationNamesList().add(androidUserApplicationJson);
		try {
			queryUpdate.insertRecord(ShinigamiJsonDBInstance.COLLECTIONANDROIDUSERS, androidUser);
		} catch (JsonProcessingException e) {
			Log.d("AndroidUserRegister", e.getMessage());
		}
	}

	private String getEmail() {

		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(this).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				return account.name;
			}
		}
		return null;
	}

	public void updateUseInstance() {

		ShinigamiDBHelper shinigamiDBHelper = new ShinigamiDBHelper(this);

		AndroidUser androidUser = shinigamiDBHelper.getUser(getEmail(), getString(getApplicationInfo().labelRes));
		androidUser.setUpdated(true);

		for (AndroidUserApplication userApplication : androidUser.getApplicationNamesList()) {
			if (userApplication.getApplicationName().equalsIgnoreCase(getString(getApplicationInfo().labelRes))) {
				userApplication.setUsedCount(userApplication.getUsedCount() + 1);
				if (userApplication.getLastUpdated().equalsIgnoreCase(ShinigamiDBHelper.getCurrentDate())) {
					androidUser.setUpdated(false);
					userApplication.setUpdated(false);
				} else {
					androidUser.setUpdated(true);
					userApplication.setUpdated(true);
				}
				shinigamiDBHelper.updateAndroidUserApplication(shinigamiDBHelper.getWritableDatabase(), userApplication);
			}
		}

		if (!androidUser.isUpdated()) {
			ShinigamiJsonDBInstance shinigamiJsonDBInstance = ShinigamiJsonDBInstance.getInstance("mongodb://androiduser:androiduser@ds043082.mongolab.com:43082/shinigami", "shinigami");
			ShinigamiJsonDBInstance.QueryUpdate queryUpdate = shinigamiJsonDBInstance.new QueryUpdate();

			try {
				queryUpdate.insertRecord(ShinigamiJsonDBInstance.COLLECTIONANDROIDUSERS, androidUser);
			} catch (JsonProcessingException e) {
				Log.d("AndroidUserRegister", e.getMessage());
			}
		}

	}


	class GetDataThread extends AsyncTask<String, Void, Map<String, String>> {

		@Override
		protected Map<String, String> doInBackground(String... params) {

			Map<String, String> map = new HashMap<String, String>();

			if (isFirstTime){
				Log.d("AndroidUserRegister", "Executing firstTimeUser...");
				firstTimeUser();
			} else {
				Log.d("AndroidUserRegister", "Executing updateInstance...");
				updateUseInstance();
			}

			return map;
		}

		@Override
		protected void onPostExecute(Map<String, String> stringStringMap) {
			super.onPostExecute(stringStringMap);
		}
	}

}
