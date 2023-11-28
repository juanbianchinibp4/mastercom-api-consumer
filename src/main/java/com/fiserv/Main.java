package com.fiserv;

import bp4.mastercom_api_client.ApiClient;
import bp4.mastercom_api_client.ApiException;
import bp4.mastercom_api_client.api.TransactionsApi;
import bp4.mastercom_api_client.model.TransactionSearchRequest;
import bp4.mastercom_api_client.model.TransactionSummary;
import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.developer.encryption.FieldLevelEncryptionConfig;
import com.mastercard.developer.encryption.FieldLevelEncryptionConfigBuilder;
import com.mastercard.developer.interceptors.OkHttpEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

import com.mastercard.developer.utils.EncryptionUtils;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, GeneralSecurityException, EncryptionException, ApiException {
        ApiClient client = new ApiClient();
        OkHttpClient okHttpClient = client.getHttpClient();

        OkHttpClient.Builder httpClientBuilder = okHttpClient.newBuilder();

        // Configure the Mastercard service URL
        client.setBasePath("https://sandbox.api.mastercard.com/mastercom");
        // Load the signing key
        PrivateKey signingKey = null;
        try {
            signingKey =
                    AuthenticationUtils.loadSigningKey(
                            "src/main/resources/Mastercom_test_client-sandbox.p12",
                            "keyalias",
                            "keystorepassword"
                    );
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
        httpClientBuilder.addInterceptor(new OkHttpOAuth1Interceptor("tOtuKPlv5pUb0d-iNWZf0m7zxTZd6f_mcGKNHg-P2b774e70!f1e33cbc653249b397e7b65476f8710c0000000000000000", signingKey));
        // ...
        client.setHttpClient(httpClientBuilder.build());

        TransactionsApi api = new TransactionsApi(client);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest()
                .acquirerRefNumber("12345678901234567890123")
                .tranStartDate("2023-01-01")
                .tranEndDate("2023-01-31");

        TransactionSummary searchResponse = api.transactionSearch(searchRequest);

        logger.info("logger" +searchResponse.getAuthorizationSummary().toString());
    }
}