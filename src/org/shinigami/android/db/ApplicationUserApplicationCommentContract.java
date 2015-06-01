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

public class ApplicationUserApplicationCommentContract extends ShinigamiContract{
    public static final String SHINIGAMIUSERAPPCOMMENT = "userappcomment";

    public class Columns {
        public static final String APPLICATIONNAME = "applicationname";
        public static final String COMMENT = "comment";
        public static final String _ID = BaseColumns._ID;
    }
}