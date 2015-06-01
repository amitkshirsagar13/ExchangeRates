package org.shinigami.android.db;

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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.exchange.rates.exchangerate.db.ExchangeContract;
import org.shinigami.json.dto.AndroidApplicationMetadata;
import org.shinigami.json.dto.AndroidUser;
import org.shinigami.json.dto.AndroidUserApplication;
import org.shinigami.json.dto.Metadata;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.shinigami.android.db.ApplicationContract.Columns.BUILDDATE;
import static org.shinigami.android.db.ApplicationContract.Columns.VERSION;
import static org.shinigami.android.db.ApplicationUserApplicationContract.Columns.APPLICATIONNAME;


public class ShinigamiDBHelper extends SQLiteOpenHelper {
	public ShinigamiDBHelper(Context context) {
		super(context, ShinigamiContract.DB_NAME, null, ShinigamiContract.DB_VERSION);
		onCreate(this.getWritableDatabase());
	}

	@Override
	public void onCreate(SQLiteDatabase sqlDB) {
		String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS " + ApplicationContract.SHINIGAMIAPP + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationContract.Columns.APPLICATIONNAME + " TEXT, " + ApplicationContract.Columns.VERSION + " TEXT, " + ApplicationContract.Columns.BUILDDATE + " TEXT)");

		try {
			sqlDB.execSQL(sqlQuery);
			Log.d("ShinigamiDBHelper", "Create table: " + sqlQuery);
		} catch (SQLiteException sqLiteException) {
			Log.d("ShinigamiDBHelper", "Table already exists: " + ApplicationContract.SHINIGAMIAPP);
		}

		sqlQuery = String.format("CREATE TABLE IF NOT EXISTS " + ApplicationUserContract.SHINIGAMIUSER + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationUserContract.Columns.EMAIL + " TEXT, " + ApplicationUserContract.Columns.PHONENUMBER + " TEXT, " + ApplicationUserContract.Columns.ISUPDATED + " BOOLEAN, " + ApplicationUserContract.Columns.LASTUPDATED + " TEXT)");

		try {
			sqlDB.execSQL(sqlQuery);
			Log.d("ShinigamiDBHelper", "Create table: " + sqlQuery);
		} catch (SQLiteException sqLiteException) {
			Log.d("ShinigamiDBHelper", "Table already exists: " + ApplicationUserContract.SHINIGAMIUSER);
		}

		sqlQuery = String.format("CREATE TABLE IF NOT EXISTS " + ApplicationUserApplicationContract.SHINIGAMIUSERAPP + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationUserApplicationContract.Columns.APPLICATIONNAME + " TEXT, " + ApplicationUserApplicationContract.Columns.CRTDATE + " TEXT, " + ApplicationUserApplicationContract.Columns.USEDCOUNT + " TEXT, " + ApplicationUserApplicationContract.Columns.ISUPDATED + " BOOLEAN, " + ApplicationUserApplicationContract.Columns.LASTUPDATED + " TEXT)");

		try {
			sqlDB.execSQL(sqlQuery);
			Log.d("ShinigamiDBHelper", "Create table: " + sqlQuery);
		} catch (SQLiteException sqLiteException) {
			Log.d("ShinigamiDBHelper", "Table already exists: " + ApplicationUserApplicationContract.SHINIGAMIUSERAPP);
		}

		sqlQuery = String.format("CREATE TABLE IF NOT EXISTS " + ApplicationUserApplicationCommentContract.SHINIGAMIUSERAPPCOMMENT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ApplicationUserApplicationCommentContract.Columns.APPLICATIONNAME + " TEXT, " + ApplicationUserApplicationCommentContract.Columns.COMMENT + " TEXT)");

		try {
			sqlDB.execSQL(sqlQuery);
			Log.d("ShinigamiDBHelper", "Create table: " + sqlQuery);
		} catch (SQLiteException sqLiteException) {
			Log.d("ShinigamiDBHelper", "Table already exists: " + ApplicationUserApplicationCommentContract.SHINIGAMIUSERAPPCOMMENT);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
		sqlDB.execSQL("DROP TABLE IF EXISTS " + ApplicationContract.SHINIGAMIAPP);
		sqlDB.execSQL("DROP TABLE IF EXISTS " + ApplicationUserContract.SHINIGAMIUSER);
		sqlDB.execSQL("DROP TABLE IF EXISTS " + ApplicationUserApplicationContract.SHINIGAMIUSERAPP);
		sqlDB.execSQL("DROP TABLE IF EXISTS " + ApplicationUserApplicationCommentContract.SHINIGAMIUSERAPPCOMMENT);
		onCreate(sqlDB);
	}

	public void addAndroidUser(SQLiteDatabase db, AndroidUser userModel) {
		ContentValues values = new ContentValues();
		values.put(ApplicationUserContract.Columns.EMAIL, userModel.getEmail());
		values.put(ApplicationUserContract.Columns.PHONENUMBER, userModel.getPhone());
		values.put(ApplicationUserContract.Columns.ISUPDATED, userModel.isUpdated());
		values.put(ApplicationUserContract.Columns.LASTUPDATED, userModel.getLastUpdated());
		// add the row
		db.insert(ApplicationUserContract.SHINIGAMIUSER
				, null, values);

		for (AndroidUserApplication userApplicationModel : userModel.getApplicationNames()) {
			addAndroidUserApplication(db, userApplicationModel);
		}
	}

	private void addAndroidUserApplication(SQLiteDatabase db, AndroidUserApplication userApplicationModel) {
		ContentValues values = new ContentValues();
		values.put(ApplicationUserApplicationContract.Columns.APPLICATIONNAME, userApplicationModel.getApplicationName());
		values.put(ApplicationUserApplicationContract.Columns.CRTDATE, userApplicationModel.getCrtDate());
		values.put(ApplicationUserApplicationContract.Columns.USEDCOUNT, userApplicationModel.getUsedCount());
		values.put(ApplicationUserApplicationContract.Columns.ISUPDATED, userApplicationModel.isUpdated());
		values.put(ApplicationUserApplicationContract.Columns.LASTUPDATED, userApplicationModel.getLastUpdated());

		// add the row
		db.insert(ApplicationUserApplicationContract.SHINIGAMIUSERAPP
				, null, values);

		for (String comment : userApplicationModel.getComments()) {
			ContentValues values1 = new ContentValues();
			values1.put(ApplicationUserApplicationCommentContract.Columns.APPLICATIONNAME, userApplicationModel.getApplicationName());
			values1.put(ApplicationUserApplicationCommentContract.Columns.COMMENT, comment);

			db.insert(ApplicationUserApplicationCommentContract.SHINIGAMIUSERAPPCOMMENT
					, null, values1);
		}
	}


	public int updateAndroidUserApplication(SQLiteDatabase db, AndroidUserApplication userApplicationModel) {
		ContentValues values = new ContentValues();
		values.put(ApplicationUserApplicationContract.Columns.APPLICATIONNAME, userApplicationModel.getApplicationName());
		values.put(ApplicationUserApplicationContract.Columns.CRTDATE, userApplicationModel.getCrtDate());
		values.put(ApplicationUserApplicationContract.Columns.USEDCOUNT, userApplicationModel.getUsedCount());
		values.put(ApplicationUserApplicationContract.Columns.ISUPDATED, userApplicationModel.isUpdated());
		values.put(ApplicationUserApplicationContract.Columns.LASTUPDATED, getCurrentDate());

		// updating row
		return db.update(ApplicationUserApplicationContract.SHINIGAMIUSERAPP, values, ApplicationUserApplicationContract.Columns.APPLICATIONNAME + " = ?",
				new String[]{userApplicationModel.getApplicationName()});
	}


	public static String getCurrentDate() {
		return getStringDate(new Date());
	}

	public static String getStringDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		return sdf.format(date);
	}

	public AndroidApplicationMetadata getApplicationMetadata(String applicationName) {
		SQLiteDatabase db = getReadableDatabase();

		AndroidApplicationMetadata androidApplicationMetadata = new AndroidApplicationMetadata();
		// send query
		Cursor cursor = db.query(ApplicationContract.SHINIGAMIAPP, new String[]{
						APPLICATIONNAME,
						VERSION, BUILDDATE},
				APPLICATIONNAME + " = ?",
				new String[]{applicationName}, null, null, null); // get all rows
		if (cursor != null) {
			// add items to the list
			for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
				androidApplicationMetadata.setApplicationName(cursor.getString(0));
				Metadata metadata = new Metadata();
				metadata.setVersion(cursor.getString(1));
				metadata.setBuildDate(cursor.getString(2));
				androidApplicationMetadata.setMetadata(metadata);
			}

			// close the cursor
			cursor.close();
		}

		// close the database connection
		db.close();

		return androidApplicationMetadata;
	}

	public void deleteRates() {
		SQLiteDatabase db = getReadableDatabase();
		db.execSQL("delete from " + ExchangeContract.EXCHANGERATES);
	}

	public AndroidUser getUser(String email, String applicationName) {

		SQLiteDatabase db = getReadableDatabase();

		AndroidUser androidUser = new AndroidUser();

		androidUser.setEmail(email);


		AndroidUserApplication userApplication;

		// send query

		Cursor cursor = db.query(ApplicationUserContract.SHINIGAMIUSER, new String[]{
						ApplicationUserContract.Columns.PHONENUMBER,
						ApplicationUserContract.Columns.LASTUPDATED}, null,
				null, null, null, null); // get all rows

		if (cursor != null) {
			// add items to the list
			for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
				androidUser.setPhone(cursor.getString(0));
				androidUser.setLastUpdated(cursor.getString(1));
			}
		}

		cursor = db.query(ApplicationUserApplicationContract.SHINIGAMIUSERAPP, new String[]{
						ApplicationUserApplicationContract.Columns.APPLICATIONNAME,
						ApplicationUserApplicationContract.Columns.USEDCOUNT,
						ApplicationUserApplicationContract.Columns.LASTUPDATED}, null,
				null, null, null, null); // get all rows
		if (cursor != null) {
			// add items to the list
			for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
				userApplication = new AndroidUserApplication();

				userApplication.setApplicationName(cursor.getString(0));
				userApplication.setUsedCount(Integer.parseInt(cursor.getString(1)));
				userApplication.setUpdated(false);
				userApplication.setLastUpdated(cursor.getString(2));


				Cursor cursor1 = db.query(ApplicationUserApplicationCommentContract.SHINIGAMIUSERAPPCOMMENT, new String[]{
								ApplicationUserApplicationCommentContract.Columns.COMMENT},
						ApplicationUserApplicationCommentContract.Columns.APPLICATIONNAME + " = ?",
						new String[]{userApplication.getApplicationName()}, null, null, null); // get all rows

				if (cursor1 != null) {
					// add items to the list
					for (cursor1.moveToFirst(); cursor1.isAfterLast() == false; cursor1.moveToNext()) {
						userApplication.getComments().add(cursor1.getString(0));
					}
					// close the cursor
					cursor1.close();
				}

				androidUser.getApplicationNames().add(userApplication);
			}
			// close the cursor
			cursor.close();
		}


		// close the database connection
		db.close();

		return androidUser;
	}
}