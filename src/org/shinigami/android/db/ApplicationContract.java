package org.shinigami.android.db;

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

public class ApplicationContract extends ShinigamiContract{
    public static final String SHINIGAMIAPP = "application";

    public class Columns {
        public static final String APPLICATIONNAME = "applicationname";
        public static final String VERSION = "version";
        public static final String BUILDDATE = "builddate";
        public static final String _ID = BaseColumns._ID;
    }
}