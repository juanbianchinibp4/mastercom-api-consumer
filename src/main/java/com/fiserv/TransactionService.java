package com.fiserv;

import bp4.mastercom_api_client.ApiClient;
import bp4.mastercom_api_client.ApiException;
import bp4.mastercom_api_client.api.TransactionsApi;
import bp4.mastercom_api_client.model.AuthorizationSummary;
import bp4.mastercom_api_client.model.TransactionSearchRequest;
import bp4.mastercom_api_client.model.TransactionSummary;
import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class.getName());

    public List<AuthorizationSummary> executeTransactionSearch(TransactionSearch transactionSearch) {
        try {
            ApiClient client = createApiClient();
            TransactionsApi api = new TransactionsApi(client);
            TransactionSearchRequest searchRequest = createTransactionSearchRequest(transactionSearch);
            TransactionSummary searchResponse = api.transactionSearch(searchRequest);
            logger.info("logger" + searchResponse.getAuthorizationSummary().toString());
            return searchResponse.getAuthorizationSummary();
        } catch (IOException | GeneralSecurityException | EncryptionException | ApiException e) {
            logger.error("An error occurred: " + e.getMessage());
        }
        return null;
    }

    private ApiClient createApiClient() throws GeneralSecurityException, IOException, EncryptionException,
            ApiException {
        ApiClient client = new ApiClient();
        OkHttpClient okHttpClient = client.getHttpClient();
        OkHttpClient.Builder httpClientBuilder = okHttpClient.newBuilder();
        client.setBasePath("https://sandbox.api.mastercard.com/mastercom");
        PrivateKey signingKey = loadSigningKey();
        httpClientBuilder.addInterceptor(new OkHttpOAuth1Interceptor(
                "tOtuKPlv5pUb0d-iNWZf0m7zxTZd6f_mcGKNHg-P2b774e70!f1e33cbc653249b397e7b65476f8710c0000000000000000",
                signingKey));
        client.setHttpClient(httpClientBuilder.build());
        return client;
    }

    private PrivateKey loadSigningKey() throws GeneralSecurityException, IOException {
        try {
            return AuthenticationUtils.loadSigningKey("src/main/resources/Mastercom_test_client-sandbox.p12",
                    "keyalias", "keystorepassword");
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException e) {
            throw new GeneralSecurityException("Failed to load signing key", e);
        }
    }

    private TransactionSearchRequest createTransactionSearchRequest(TransactionSearch transactionSearch) {
        return new TransactionSearchRequest().acquirerRefNumber(transactionSearch.getAcquirerRefNumber())
                .tranStartDate(transactionSearch.getTranStartDate()).tranEndDate(transactionSearch.getTranEndDate());

    }
}