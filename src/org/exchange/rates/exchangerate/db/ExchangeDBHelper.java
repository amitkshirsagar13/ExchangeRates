package org.exchange.rates.exchangerate.db;

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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.getrate.inr.RateList;
import org.getrate.inr.RateModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.exchange.rates.exchangerate.db.ExchangeContract.Columns.*;


public class ExchangeDBHelper extends SQLiteOpenHelper {
    public ExchangeDBHelper(Context context) {
        super(context, ExchangeContract.DB_NAME, null, ExchangeContract.DB_VERSION);
        onCreate(this.getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS " + ExchangeContract.EXCHANGERATES + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ExchangeContract.Columns.COMPANY + " TEXT, " + ExchangeContract.Columns.RATE + " TEXT, " + ExchangeContract.Columns.DATE + " TEXT)");

        try {
            sqlDB.execSQL(sqlQuery);
            Log.d("ExchangeDBHelper", "Create table: " + sqlQuery);
        } catch (SQLiteException sqLiteException) {
            Log.d("ExchangeDBHelper", "Table already exists: " + ExchangeContract.EXCHANGERATES);
            //sqlDB.execSQL("delete from " + ExchangeContract.EXCHANGERATES);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + ExchangeContract.EXCHANGERATES);
        onCreate(sqlDB);
    }

    public void addExchangeRates(RateList rateList) {
        if (rateList != null && rateList.size() > 0) {
            // obtain a readable database
            SQLiteDatabase db = getWritableDatabase();

            for (RateModel rateModel : rateList) {
                addExchangeRate(db, rateModel);
            }

            // close the database connection
            db.close();
        }

    }

    private void addExchangeRate(SQLiteDatabase db, RateModel rateModel) {
        ContentValues values = new ContentValues();
        values.put(COMPANY, rateModel.getCompany());
        values.put(RATE, rateModel.getRate());
        values.put(DATE, getCurrentDate());
        // add the row
        db.insert(ExchangeContract.EXCHANGERATES, null, values);
    }

    private String getCurrentDate() {
        return getStringDate(new Date());
    }

    public String getStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        return sdf.format(date);
    }

    public RateList getExchangeRates() {
        SQLiteDatabase db = getReadableDatabase();

        RateList rateList = new RateList();


        // send query
        Cursor cursor = db.query(ExchangeContract.EXCHANGERATES, new String[]{
                        COMPANY,
                        RATE, DATE},
                DATE + " = ?",
                new String[]{getCurrentDate()}, null, null, null); // get all rows
        if (cursor != null) {
            // add items to the list
            for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
                rateList.addRateModel(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            }

            // close the cursor
            cursor.close();
        }

        // close the database connection
        db.close();

        return rateList;
    }

    public void deleteRates() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("delete from " + ExchangeContract.EXCHANGERATES);
    }
}