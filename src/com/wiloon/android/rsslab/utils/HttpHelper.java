package com.wiloon.android.rsslab.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 6/28/12
 * Time: 9:58 PM
 */
public class HttpHelper {

    public HttpHelper(String token) {
        this.token = token;
    }

    private String token;

    public HttpHelper() {

    }

    public InputStream httpsGetStream(String strUrl) {
        InputStream rtn = null;
        URL url;
        HttpsURLConnection conn;

        try {
            url = new URL(strUrl);
            conn = (HttpsURLConnection) url.openConnection();
            conn.addRequestProperty("Authorization", "GoogleLogin auth=" + token);
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.setDoInput(true);
            RssLabLog.debug("Http Helper", "url", strUrl);
            RssLabLog.debug("https.httpsGet", "connecting...");
            conn.connect();
            int code = conn.getResponseCode();
            if (code != 200) {
                RssLabLog.error("https get error", code + "");
            }
            rtn = conn.getInputStream();
            RssLabLog.debug("Http Helper", "https.httpsGet", "connected");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    /**
     * http httpsGet
     *
     * @param strUrl
     * @return raw html response
     */
    public String httpsGet(String strUrl) {
        String response = null;
        InputStream inputStream = httpsGetStream(strUrl);
        if (inputStream != null) {
            response = Utils.streamReader(inputStream);
        }
        RssLabLog.debug("Http Helper", "https.httpsGet.response", response);
        return response;
    }

    public String post(String strUrl, String requestPayload) {
        String rtn = null;
        URL url;
        HttpsURLConnection conn;

        try {
            url = new URL(strUrl);

            conn = (HttpsURLConnection) url.openConnection();
            conn.addRequestProperty("Authorization", "GoogleLogin auth=" + token);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            RssLabLog.debug("Http Helper", "post.url", strUrl);
            RssLabLog.debug("https.post", "connecting...");
            conn.connect();
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            // requestPayload = URLEncoder.encode(requestPayload, "utf-8");
            out.writeBytes(requestPayload);
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            if (responseCode > 300) {
                RssLabLog.debug("Http Helper", "http.response.code", responseCode);
                RssLabLog.debug("http request.payload", requestPayload);
            }
            rtn = Utils.streamReader(conn.getInputStream());
            RssLabLog.debug("Http Helper", "https.post", "disconnecting.");
            conn.disconnect();
            RssLabLog.debug("Http Helper", "https.post", "disconnected.");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RssLabLog.debug("Http Helper", "https.post", "Done");
        return rtn;
    }

    public byte[] getImgAsByteArray(String url) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        InputStream inputStream = httpGetStream(url);
        if (inputStream != null) {
            byte[] buffer = new byte[1024];
            int len = -1;
            try {
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return outStream.toByteArray();
    }

    public InputStream httpGetStream(String strUrl) {
        InputStream rtn = null;
        URL url;
        HttpURLConnection conn;

        try {
            url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.setDoInput(true);
            RssLabLog.debug("Http Helper", "URL", strUrl);
            RssLabLog.debug("Http Helper", "connecting...");
            conn.connect();
            rtn = conn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    /**
     * http get
     *
     * @param url
     * @return raw html response
     */
    public String httpGet(String url) {
        String response = null;
        InputStream stream = httpGetStream(url);
        if (stream != null) {
            response = Utils.streamReader(stream);

        }
        return response;
    }
}
