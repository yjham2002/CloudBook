package com.CBook.CB.cloudbook;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Communicator {
    private static String webUrl = "";
    private static String jsoupTemp = "";

    public static void getHttp(String url, final Handler mHandler) {
        final String httpUrl = webUrl + url;
        Thread t = new Thread() {
            public void run() {
                String jsonString = ReadHTML(httpUrl);
                if (jsonString == null) {
                    jsonString = "";
                }

                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("jsonString", jsonString);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        };
        t.start();
    }

    public void postHttp(String url, final Map<String, String> postMap, final Handler mHandler) {
        final String httpUrl = webUrl + url;
        Thread t = new Thread() {
            public void run() {
                String jsonString = HttpPostData(httpUrl, postMap);
                if (jsonString == null) {
                    jsonString = "";
                }

                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("jsonString", jsonString);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        };
        t.start();
    }

    public static void postHttps(String url, final Map<String, String> postMap, final Handler mHandler) {
        final String httpUrl = webUrl + url;
        Thread t = new Thread() {
            public void run() {
                String jsonString = HttpsPostData(httpUrl, postMap);
                if (jsonString == null) {
                    jsonString = "";
                }

                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("jsonString", jsonString);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        };
        t.start();
    }

    public static void getHttps(String url, final Handler mHandler) {
        final String httpUrl = webUrl + url;


        Thread thread = new Thread() {
            @Override
            public void run() {
                String jsonString = readHTTPS(httpUrl);
                if (jsonString == null) {
                    jsonString = "";
                }

                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("jsonString", jsonString);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        };

        thread.start();
    }

    public static void postHttps(String url, final Handler mHandler) {
        final String httpUrl = webUrl + url;

        Thread thread = new Thread() {
            @Override
            public void run() {
                String jsonString = readPostHTTPS(httpUrl);
                if (jsonString == null) {
                    jsonString = "";
                }

                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("jsonString", jsonString);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        };

        thread.start();
    }

    public static String readPostHTTPS(String address) {
        String html = new String();

        try {
            URL url = new URL(address);

            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);

            https.setConnectTimeout(2000);
            https.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=utf-8");
            https.setRequestMethod("POST");
            https.setDoInput(true);
            https.setDoOutput(true);

            if (https.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(https.getInputStream()));
                while (true) {
                    String buf = br.readLine();
                    if (buf == null)
                        break;
                    html += buf;
                }
                br.close();
                https.disconnect();
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        } finally {

        }
        return html;
    }

    public static String readHTTPS(String address) {

        String html = new String();

        try {
            URL url = new URL(address);

            trustAllHosts();
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(DO_NOT_VERIFY);

            urlConnection.setConnectTimeout(3000);
            urlConnection.setUseCaches(false);
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while (true) {
                    String buf = br.readLine();
                    if (buf == null)
                        break;
                    html += buf;
                }
                br.close();
                urlConnection.disconnect();
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        } finally {

        }
        return html;
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static String ReadHTML(String address) {
        return ReadHTML(address, 2000);
    }

    private static String ReadHTML(String address, int timeout) {
        String html = new String();

        try {
            URL url = new URL(address);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection != null) {
                urlConnection.setConnectTimeout(timeout);
                urlConnection.setUseCaches(false);
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while (true) {
                        String buf = br.readLine();
                        if (buf == null)
                            break;
                        html += buf;
                    }
                    br.close();
                    urlConnection.disconnect();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        } finally {

        }

        return html;
    }

    private String HttpPostData(String mUrl, Map<String, String> postMap) {
        String html = new String();

        try {
            // --------------------------
            // URL 설정하고 접속하기
            // --------------------------
            URL url = new URL(mUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
            // --------------------------
            // 전송 모드 설정 - 기본적인 설정이다
            // --------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true); // 서버에서 읽기 모드 지정
            http.setDoOutput(true); // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST"); // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // --------------------------
            // 서버로 값 전송
            // --------------------------
            StringBuffer buffer = new StringBuffer();

            Iterator<String> keyIter = postMap.keySet().iterator();

            ArrayList<String> keyList = new ArrayList<String>();
            while (keyIter.hasNext()) {
                String keys = (String) keyIter.next();
                keyList.add(keys);
            }
            for (int i = 0; i < keyList.size(); i++) {
                String mKey = keyList.get(i);
                String mValue = postMap.get(mKey);

                if (i < (keyList.size() - 1)) {
                    buffer.append(mKey).append("=").append(mValue).append("&");
                } else {
                    buffer.append(mKey).append("=").append(mValue);
                }
            }

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            // --------------------------
            // 서버에서 전송받기
            // --------------------------
            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) { // 서버에서 라인단위로 보내줄 것이므로
                builder.append(str + "\n"); // View에 표시하기 위해 라인 구분자 추가
            }
            html = builder.toString(); // 전송결과를 전역 변수에 저장
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
        } // try
        return html;
    }

    private static String HttpsPostData(String mUrl, Map<String, String> postMap) {
        String html = new String();

        try {
            // --------------------------
            // URL 설정하고 접속하기
            // --------------------------
            URL url = new URL(mUrl);
            trustAllHosts();
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection(); // 접속
            https.setHostnameVerifier(DO_NOT_VERIFY);
            // --------------------------
            // 전송 모드 설정 - 기본적인 설정이다
            // --------------------------
            https.setDefaultUseCaches(false);
            https.setDoInput(true); // 서버에서 읽기 모드 지정
            https.setDoOutput(true); // 서버로 쓰기 모드 지정
            https.setRequestMethod("POST"); // 전송 방식은 POST

            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            https.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // --------------------------
            // 서버로 값 전송
            // --------------------------
            StringBuffer buffer = new StringBuffer();

            Iterator<String> keyIter = postMap.keySet().iterator();

            ArrayList<String> keyList = new ArrayList<String>();
            while (keyIter.hasNext()) {
                String keys = (String) keyIter.next();
                keyList.add(keys);
            }
            for (int i = 0; i < keyList.size(); i++) {
                String mKey = keyList.get(i);
                String mValue = postMap.get(mKey);

                if (i < (keyList.size() - 1)) {
                    buffer.append(mKey).append("=").append(mValue).append("&");
                } else {
                    buffer.append(mKey).append("=").append(mValue);
                }
            }

            OutputStreamWriter outStream = new OutputStreamWriter(https.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            // --------------------------
            // 서버에서 전송받기
            // --------------------------
            InputStreamReader tmp = new InputStreamReader(https.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) { // 서버에서 라인단위로 보내줄 것이므로
                builder.append(str + "\n"); // View에 표시하기 위해 라인 구분자 추가
            }
            html = builder.toString(); // 전송결과를 전역 변수에 저장
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
        } // try
        return html;
    }

    public static void sendImg(String url, final String path, final Handler mHandler) {
        final String httpUrl = webUrl + url;


        Thread thread = new Thread() {
            @Override
            public void run() {
                String jsonString = send(httpUrl, "", path);
                if (jsonString == null) {
                    jsonString = "";
                }

                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("jsonString", jsonString);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        };

        thread.start();
    }

    private static String send(String urlString, String type, String filePath) {

        int idx = filePath.lastIndexOf(".");
        String fileType = ".jpg";
        if (idx > 0) {
            fileType = filePath.substring(idx);
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        String attachmentName = df.format(c.getTime());
        String attachmentFileName = attachmentName + fileType;

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String delimiter = "\r\n--" + boundary + "\r\n";

        HttpURLConnection httpUrlConnection = null;
        URL url;
        String response = "";
        try {
            url = new URL(urlString);

            System.setProperty("http.keepAlive", "false");

            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());

            request.writeBytes(delimiter);
            request.writeBytes("Content-Disposition: form-data; name=\"type\"" + crlf);
            request.writeBytes("Content-Type: text/plain;charset=UTF-8" + crlf);
            request.writeBytes("Content-Length: " + type.length() + crlf);
            request.writeBytes(crlf);
            request.writeBytes(type);
            request.writeBytes(delimiter);

            request.writeBytes("Content-Disposition: form-data; name=uploadedfile; filename=\""
                    + attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);
            // 테스트 부분

            // 파일 복사 작업 시작
            FileInputStream bis = new FileInputStream(filePath);

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // 버퍼 크기만큼 파일로부터 바이트 데이터를 읽는다.
            // int byteRead = in.read(buffer, 0, bufferSize);

            int len = 0;
            // 전송
            while ((len = bis.read(buffer)) != -1) {
                request.write(buffer);
            }

            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();

            InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();

            responseStream.close();

            httpUrlConnection.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response;
    }

    public static String setValue(String key, String value) {
        return "Content-Disposition: form-data; name=\"" + key + "\"r\n\r\n" + value;
    }
}