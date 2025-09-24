package org.roxy.reminder.bot.service.suggestion.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

@Slf4j
@Service
public class DaDataWebClient {

    private static final String SSL_PROTOCOL = "TLS";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String AUTHORIZATION_TOKEN = "Authorization";
    private static final String APPLICATION_JSON = "application/json";
    private static final String DADATA_SUGGESTION_URL = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/address";
    @Value("${dadata.api-key}")
    private String API_KEY;
    @Value("${dadata.secret-key}")
    private String SECRET_KEY;

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public DaDataWebClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.client = createHttpClient();
    }

    public DaDataSuggestionsResponse getSuggestions(DaDataSearchRequest searchRequest) {
        try {
            HttpRequest request = createHttpRequest(searchRequest);
            log.info("Suggestion request body = {}", objectMapper.writeValueAsString(searchRequest));
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.error(response.body());
            }
            log.info("Suggestion response body = {}", response.body());
            return objectMapper.readValue(response.body(), DaDataSuggestionsResponse.class);
        } catch (Exception e) {
            log.error("Failed to get suggestion", e);
        }
        return new DaDataSuggestionsResponse(List.of());
    }

    private HttpRequest createHttpRequest(DaDataSearchRequest searchRequest) {
        String jsonMessage;
        try {
            jsonMessage = objectMapper.writeValueAsString(searchRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("JsonMessage = {}", jsonMessage);
        return HttpRequest.newBuilder()
                .uri(URI.create(DADATA_SUGGESTION_URL))
                .header(AUTHORIZATION_TOKEN, "Token " + API_KEY)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(jsonMessage))
                .build();
    }

    private HttpClient createHttpClient() {
        try {
            SSLContext sslContext = createTrustAllSslContext();
            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();
        } catch (RuntimeException e) {
            log.error("Failed to initialize HTTP client", e);
        }
        return null;
    }

    private SSLContext createTrustAllSslContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance(SSL_PROTOCOL);
            sslContext.init(null, createTrustAllManagers(), new java.security.SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("Failed to create SSL context", e);
            throw new RuntimeException("SSL context initialization failed", e);
        }
    }

    private TrustManager[] createTrustAllManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {

                    }
                }
        };
    }
}