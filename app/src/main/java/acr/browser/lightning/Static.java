package acr.browser.lightning;

import android.content.Context;

import acr.browser.lightning.loader.Constants;
import acr.browser.lightning.loader.ImageLoader;

/**
 * Created by admin on 12/11/2017.
 */

public class Static {
    public static ImageLoader _imageLoader = null;
    public static ImageLoader getIL(Context context) {
        if (_imageLoader == null)
            _imageLoader = new ImageLoader(context, true,
                    Constants.DOWNLOAD_FOLDER + "/cache");
        return _imageLoader;
    }
/*    private static Database _mDb = null;
    public static DatabaseHelper getDB(final Context context) {
        if (_mDb == null) {
            _mDb = new DatabaseHelper(context, Constants.DB_NAME);

            try {
                _mDb.initDb(false);
                _mDb.createUserTable();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
        return _mDb;
    }*/
}
