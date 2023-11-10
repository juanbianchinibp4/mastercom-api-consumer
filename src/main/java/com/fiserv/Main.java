package com.fiserv;

import bp4.mastercom_api_client.ApiClient;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.http.HttpClient;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class Main {
    public static void main(String[] args) {
        ApiClient client = new ApiClient();
        OkHttpClient okHttpClient = client.getHttpClient();

        OkHttpClient.Builder httpClientBuilder = okHttpClient.newBuilder();

        // Configure the Mastercard service URL
        client.setBasePath("https://sandbox.api.mastercard.com/mdes");
        // Load the signing key
        PrivateKey signingKey = null;
        try {
            signingKey = AuthenticationUtils.loadSigningKey("./path/to/your/signing-key.p12", "keyalias", "keystorepassword");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
        // Add the interceptor code responsible for signing HTTP requests
        httpClientBuilder.addInterceptor(new OkHttpOAuth1Interceptor("000000000000000000000000000000000000000000000000!000000000000000000000000000000000000000000000000", signingKey));

        client.setHttpClient(httpClientBuilder.build());

    }
}