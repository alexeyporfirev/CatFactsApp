package application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import items.Cat;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CatFacts {
    private static final String REMOTE_SERVICE_URI =
            "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet request =
                new HttpGet(REMOTE_SERVICE_URI);

        try {
            CloseableHttpResponse response = httpClient.execute(request);
            String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            List<Cat> cats = mapper.readValue(body, new TypeReference<List<Cat>>() {});
            cats.stream().filter(value -> value.getUpvotes() > 0)
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Ошибка чтения ответа!" + e.getMessage());
        }
    }
}
