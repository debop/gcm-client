package org.debop.gcm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Http Client
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 2013. 11. 17. 오전 3:25
 */
@Slf4j
public class HttpClient {

    public final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();

    /**
     * Request send by HTTP GET method
     *
     * @param httpGet HTTP GET 요청 정보
     */
    public HttpResponse get(final HttpGet httpGet) {
        try {
            return request(httpGet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Request send by HTTP POST method
     *
     * @param httpPost Post 방식 요청 정보 배열
     */
    public HttpResponse post(final HttpPost httpPost) {
        try {
            return request(httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * send Request to server
     * @param request Http Request
     * @return HttpResponse Http response
     */
    protected HttpResponse request(final HttpUriRequest request) {
        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();
            Future<HttpResponse> response = client.execute(request, null);
            return response.get(15, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
