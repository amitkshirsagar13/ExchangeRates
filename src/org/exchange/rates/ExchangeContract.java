package org.exchange.rates;

/**
 * Created by Amit on 5/23/2015.
 */

import android.provider.BaseColumns;

public class ExchangeContract {
    public static final String DB_NAME = "org.exchange.rates.ExchangeRate.db";
    public static final int DB_VERSION = 1;
    public static final String EXCHANGERATES = "exchangerates";

    public class Columns {
        public static final String RATE = "rate";
        public static final String COMPANY = "company";
        public static final String DATE = "date";
        public static final String _ID = BaseColumns._ID;
    }
}