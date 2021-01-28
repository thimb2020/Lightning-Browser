package acr.browser.lightning.loader;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class Util {
    static final String TAG = "Util";
    
    public static boolean download2file(String url, File f) throws MalformedURLException, IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
//        con.setRequestProperty("Accept-Encoding", "gzip");
//        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) "
//                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.2 Safari/537.36");
        con.setConnectTimeout(Define.connectTimeOut);
        con.setReadTimeout(Define.readTimeOut);
        Log.i(TAG, "url = " + url);

        con.connect();
        
        int responseCode = con.getResponseCode();
        Log.i(TAG, "responseCode = " + responseCode);
        if (responseCode == 200) {
            InputStream in = con.getInputStream();
            if ("gzip".equals(con.getContentEncoding())) {
                in = new GZIPInputStream(in);
            }
            if (in == null) {
                return false;
            }
            OutputStream os = new FileOutputStream(f);
            boolean b = Util.CopyStream(in, os);
            in.close();
            os.close();
            return b;
        }
        return false;
    }

    public static boolean CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
