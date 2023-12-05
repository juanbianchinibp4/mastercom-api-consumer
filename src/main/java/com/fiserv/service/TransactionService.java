package com.fiserv.service;

import bp4.mastercom_api_client.ApiClient;
import bp4.mastercom_api_client.ApiException;
import bp4.mastercom_api_client.api.TransactionsApi;
import bp4.mastercom_api_client.model.TransactionSearchRequest;
import bp4.mastercom_api_client.model.TransactionSummary;
import com.fiserv.dto.TransactionSearchDTO;
import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class TransactionService {
    @Value("${api.url}")
    private String apiUrl;
    @Value("${api.consumer_key}")
    private String consumerKey;
    @Value("${pk12file}")
    private String pk12file;
    @Value("${api.key_alias}")
    private String keyAlias;
    @Value("${api.key_store_password}")
    private String keyStorePassword;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class.getName());

    public TransactionSummary executeTransactionSearch(TransactionSearchDTO transactionSearchDTO) {
        try {
            ApiClient client = createApiClient();
            TransactionsApi api = new TransactionsApi(client);
            TransactionSearchRequest searchRequest = createTransactionSearchRequest(transactionSearchDTO);
            TransactionSummary searchResponse = api.transactionSearch(searchRequest);

            return searchResponse;
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
        client.setBasePath(apiUrl);
        PrivateKey signingKey = loadSigningKey();
        httpClientBuilder.addInterceptor(new OkHttpOAuth1Interceptor(
                consumerKey,
                signingKey));
        client.setHttpClient(httpClientBuilder.build());
        return client;
    }

    private PrivateKey loadSigningKey() throws GeneralSecurityException, IOException {
        try {
            return AuthenticationUtils.loadSigningKey(
                    pk12file, keyAlias, keyStorePassword);
        } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException |
                 UnrecoverableKeyException e) {
            throw new GeneralSecurityException("Failed to load signing key", e);
        }
    }

    private TransactionSearchRequest createTransactionSearchRequest(TransactionSearchDTO transactionSearchDTO) {
        return new TransactionSearchRequest().acquirerRefNumber(transactionSearchDTO.getAcquirerRefNumber())
                .tranStartDate(transactionSearchDTO.getTranStartDate()).tranEndDate(transactionSearchDTO.getTranEndDate());

    }
}