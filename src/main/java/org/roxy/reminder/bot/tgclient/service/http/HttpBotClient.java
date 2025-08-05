package org.roxy.reminder.bot.tgclient.service.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.tgclient.dto.message.callbackanswer.CallbackAnswerDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.response.SendMessageResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Slf4j
@Component
public class HttpBotClient implements BotClient {

    private static final String SSL_PROTOCOL = "TLS";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String SEND_MESSAGE_ENDPOINT = "/sendMessage";
    private static final String GET_UPDATES_MESSAGE_ENDPOINT = "/getUpdates";
    private static final String ANSWER_CALLBACK_QUERY = "/answerCallbackQuery";
    private static final String LONG_POLLING_TIMEOUT_SECONDS = "0";

    private final String botBaseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public HttpBotClient(
            @Value("${bot.url}") String botBaseUrl, ObjectMapper objectMapper) {
        this.botBaseUrl = botBaseUrl;
        this.objectMapper = objectMapper;
        this.client = createHttpClient();
    }

    @SneakyThrows
    @Override
    public SendMessageResponseDto sendMessage(MessageDto message) {
        String jsonMessage = objectMapper.writeValueAsString(message);
        log.info("JsonMessage = {}", jsonMessage);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(botBaseUrl + SEND_MESSAGE_ENDPOINT))
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(jsonMessage))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Sent message.Response body = {}", response.body());
            return objectMapper.readValue(response.body(), SendMessageResponseDto.class);
        } catch (Exception e) {
            log.error("Failed to send message to Telegram", e);
        }
        return null;
    }

    @Override
    public String getUpdates() {
        log.info("Sending request to get updates");
        var request = HttpRequest.newBuilder()
                .uri(URI.create(botBaseUrl + GET_UPDATES_MESSAGE_ENDPOINT))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Received update. Body = {}", response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            log.error("Failed to get bot message updates from Telegram", e);
        }
        return null;
    }

    public void clearUpdates(Long lastUpdateId) {
        log.info("Clearing updates");
        final long NEXT_OFFSET = lastUpdateId + 1L;

        var request = HttpRequest.newBuilder()
                .uri(URI.create(botBaseUrl + GET_UPDATES_MESSAGE_ENDPOINT + "?offset=" + NEXT_OFFSET))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Received update after clearing. Body = {}", response.body());
        } catch (IOException | InterruptedException e) {
            log.error("Failed to get bot message updates from Telegram", e);
        }
    }

    @SneakyThrows
    public void answerCallbackQuery(String callbackQueryId) {
        log.info("Answer callback query {}", callbackQueryId);
        CallbackAnswerDto callbackAnswerDto = CallbackAnswerDto.builder()
                .callbackQueryId(callbackQueryId)
                .text("Обработка ответа")
                .showAlert(true)
                .build();

        String jsonMessage = objectMapper.writeValueAsString(callbackAnswerDto);

        var request = HttpRequest.newBuilder()
                .uri(URI.create(botBaseUrl + ANSWER_CALLBACK_QUERY))
                .POST(HttpRequest.BodyPublishers.ofString(jsonMessage))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.error("Received answer callback response. Body = {}", response.body());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to get bot message updates from Telegram", e);
        }
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