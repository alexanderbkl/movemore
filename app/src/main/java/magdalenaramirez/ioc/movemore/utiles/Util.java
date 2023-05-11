package magdalenaramirez.ioc.movemore.utiles;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Util {

    private static JSONObject jsonObject;
    private static String URL_DEFAULT = "https://10.2.66.56/index.php/";

    public static String getParamsString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();

        result.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        String resultString = result.toString();
        return resultString.length() > 1
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }



    public static SSLContext createSSLContext() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream("/sdcard/cert.p12");
        keyStore.load(fis, "password1".toCharArray());

        // Crear un administrador de claves para el algoritmo de cifrado

        // Crear un contexto SSL y configurarlo para usar el certificado

        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "password1".toCharArray());

        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        return sslContext;
    }


    public static JSONObject getResponse(String urlServer, Map<String, String> parameters) throws IOException, JSONException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
    //public static JSONObject getResponse(String urlServer, Map<String, String> parameters) throws IOException, JSONException {
        urlServer += Util.getParamsString(parameters);
        System.out.println("URL Response: " + URL_DEFAULT + urlServer);

        SSLContext sslContext = createSSLContext();

        // Crear una conexión SSL con el servidor
        URL url = new URL(URL_DEFAULT+urlServer);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sslContext.getSocketFactory());



        connection.connect();

        InputStream in = null;

        try {
            in = connection.getInputStream();
        } catch (Exception e) {
            Log.e("Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("reach");

        // Realizar la petición HTTPS
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder response = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        // Parsear el JSON de la respuesta

        JSONObject jsonObject = new JSONObject(response.toString());

        System.out.println("response:" + response);

        // Cerrar la conexión

        assert in != null;
        in.close();








        return jsonObject;


    }


    public static JSONObject getResponse(String urlServer, String requestMethod) {

        System.out.println("URL Response: " + URL_DEFAULT + urlServer);
        JSONObject jsonObject = null;
        try {
            SSLContext sslContext = createSSLContext();

            // Crear una conexión SSL con el servidor
            URL url = new URL(URL_DEFAULT+urlServer);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            //set post request
            connection.setRequestMethod(requestMethod);
            connection.setSSLSocketFactory(sslContext.getSocketFactory());



            connection.connect();

            InputStream in = null;

            try {
                in = connection.getInputStream();
            } catch (Exception e) {
                Log.e("Error", "Error: " + e.getMessage());
                e.printStackTrace();
            }


            // Realizar la petición HTTPS
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder response = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            // Parsear el JSON de la respuesta

            jsonObject = new JSONObject(response.toString());

            System.out.println("response:" + response);

            // Cerrar la conexión

            assert in != null;
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



        return jsonObject;

    }
    public static JSONObject getResponse(String urlServer) throws IOException, JSONException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException, CertificateException {

        System.out.println("URL Response: " + URL_DEFAULT + urlServer);

        SSLContext sslContext = createSSLContext();

        // Crear una conexión SSL con el servidor
        URL url = new URL(URL_DEFAULT+urlServer);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        //set post request
        connection.setRequestMethod("GET");
        connection.setSSLSocketFactory(sslContext.getSocketFactory());



        connection.connect();

        InputStream in = null;

        try {
            in = connection.getInputStream();
        } catch (Exception e) {
            Log.e("Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }


        // Realizar la petición HTTPS
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder response = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        // Parsear el JSON de la respuesta

        JSONObject jsonObject = new JSONObject(response.toString());

        System.out.println("response:" + response);

        // Cerrar la conexión

        assert in != null;
        in.close();


        return jsonObject;

    }



// Cierre clase principal
}

