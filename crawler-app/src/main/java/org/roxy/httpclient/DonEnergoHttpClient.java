package org.roxy.httpclient;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class DonEnergoHttpClient {

    private static final String SSL_PROTOCOL = "TLS";

    private final HttpClient client;

    public DonEnergoHttpClient() {
        this.client = createHttpClient();
    }

    @SneakyThrows
    public CompletableFuture<String> getPageContentAsync(URI uri) {

        var request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(
                        response -> {
                            int statusCode = response.statusCode();
                            if (statusCode == 200) {
                                return response.body();
                            } else {
                                log.error("Failed to get URL {},Status code = {} ", uri.getPath(), statusCode);
                                return "";
                            }
                        }).exceptionally(ex -> {
                    System.err.printf("Request to %s failed: %s%n", uri.getPath(), ex.getMessage());
                    return "";
                });
    }


    private HttpClient createHttpClient() {

        SSLContext sslContext = createTrustAllSslContext();
        return HttpClient.newBuilder()
                .sslContext(sslContext)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    private SSLContext createTrustAllSslContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance(SSL_PROTOCOL);
            sslContext.init(null, createTrustAllManagers(), new java.security.SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
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