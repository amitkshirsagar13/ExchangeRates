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

import android.provider.BaseColumns;

public class ExchangeContract {
    public static final String DB_NAME = "org.exchange.rates.exchangerate.db";
    public static final int DB_VERSION = 1;
    public static final String EXCHANGERATES = "exchangerates";

    public class Columns {
        public static final String RATE = "rate";
        public static final String COMPANY = "company";
        public static final String DATE = "date";
        public static final String _ID = BaseColumns._ID;
    }
}