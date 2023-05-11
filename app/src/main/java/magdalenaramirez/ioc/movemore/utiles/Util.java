package magdalenaramirez.ioc.movemore.utiles;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

    //private static String URL_DEFAULT = "https://10.2.66.56/index.php/";
/*
    public static JSONObject getResponse(String urlServer, Map<String, String> parameters) throws IOException, JSONException {
        JSONObject jsonObject = null; // declarar jsonObject aquí

        try {
            // Cargar el almacen de claves del sistema
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream("/sdcard/cert.p12");
            keyStore.load(fis, "password1".toCharArray());

            // Crear un administrador de claves para el algoritmo de cifrado
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "password1".toCharArray());
            System.out.println("keyManagerFactory:" + keyManagerFactory);

            // Crear un contexto SSL y configurarlo para usar el certificado
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            urlServer = URL_DEFAULT + urlServer + Util.getParamsString(parameters);
            System.out.println("URL Response: " + urlServer);

            // Crear una conexión SSL con el servidor
            URL url = new URL(urlServer);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sslContext.getSocketFactory());

            // Cambia estas líneas
            // connection.setRequestMethod("GET");
            // connection.connect();
            // int responseCode = connection.getResponseCode();
            // if (responseCode != HttpsURLConnection.HTTP_OK) {
            //     throw new IOException("HTTP error code: " + responseCode);
            // }

            // Por estas líneas
            connection.setRequestMethod("GET");

            //ignore ssl certificate validation
            connection.setHostnameVerifier((hostname, session) -> true);



            connection.connect();

            InputStream in = null;
            try {
                in = connection.getInputStream();
            } catch (Exception e) {
                Log.e("Error", "Error: " + e.getMessage());
                System.out.println("Errorr: " + e.getMessage());
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
            jsonObject = new JSONObject(response.toString());
            System.out.println("response:" + response);

            // Cerrar la conexión
            in.close();
            //return jsonObject;

        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
            // Manejar la excepción
        }
        return jsonObject;
    }

*/


    public static JSONObject getResponse(String urlServer, Map<String, String> parameters) throws IOException, JSONException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        //public static JSONObject getResponse(String urlServer, Map<String, String> parameters) throws IOException, JSONException {
        urlServer += Util.getParamsString(parameters);
        System.out.println("URL Response: " + URL_DEFAULT + urlServer);

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

        //sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        //sslContext.init(null, null, null);

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


    public static JSONObject getResponse(String urlServer) throws IOException, JSONException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException, CertificateException {

        System.out.println("URL Response: " + URL_DEFAULT + urlServer);

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream("/sdcard/cert.p12");
        keyStore.load(fis, "password1".toCharArray());

        // Crear un administrador de claves para el algoritmo de cifrado

        // Crear un contexto TLS y configurarlo para usar el certificado

        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "password1".toCharArray());

        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        //sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        //sslContext.init(null, null, null);

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


// Cierre clase principal
}

