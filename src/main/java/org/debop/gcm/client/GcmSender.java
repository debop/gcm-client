package org.debop.gcm.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.debop.gcm.message.GcmMessage;
import org.debop.gcm.util.HttpClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * GCM Message sender
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 2013. 11. 17. 오전 2:57
 */
@Slf4j
public class GcmSender {

    public static final String GCM_SERVER_URL = "https://android.googleapis.com/gcm/send";

    private final URI gcmServerUri;
    private final String serverApiKey;
    private final HttpClient httpClient;

    public GcmSender(final String serverApiKey) throws URISyntaxException {
        this.serverApiKey = serverApiKey;
        this.gcmServerUri = new URI(GCM_SERVER_URL);
        this.httpClient = new HttpClient();
    }

    public int send(final String registrationId, GcmMessage msg, int retry) {
        assert (registrationId != null && !registrationId.isEmpty());
        assert (msg != null);

        msg.getRegistrationIds().add(registrationId);

        HttpPost post;
        try {
            post = buildHttpPost(serverApiKey, msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HttpResponse response;
        boolean tryAgain;
        int attempts = 0;

        do {
            attempts++;

            try {
                response = httpClient.post(post);
                return response.getStatusLine().getStatusCode();
            } catch (Exception e) {
                log.debug("fail to request", e);
                tryAgain = true;
            }
            tryAgain = tryAgain && (attempts <= retry);

        } while (tryAgain);

        throw new RuntimeException("could not send message after #" + attempts + " attempts");
    }

    public int send(final Set<String> registrationIds, GcmMessage msg, int retry) {
        assert (registrationIds != null && registrationIds.size() > 0);
        assert (msg != null);

        msg.getRegistrationIds().addAll(registrationIds);
        HttpPost post;
        try {
            post = buildHttpPost(serverApiKey, msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HttpResponse response;
        boolean tryAgain;
        int attempts = 0;

        do {
            attempts++;

            try {
                response = httpClient.post(post);
                return response.getStatusLine().getStatusCode();
            } catch (Exception e) {
                tryAgain = true;
            }
            tryAgain = tryAgain && (attempts <= retry);

        } while (tryAgain);

        throw new RuntimeException("could not send message after #" + attempts + " attempts");
    }


    private HttpPost buildHttpPost(String apiKey, GcmMessage msg) throws JsonProcessingException {
        HttpPost post = new HttpPost(this.gcmServerUri);
        for (Header header : buildHttpHeader(apiKey)) {
            post.addHeader(header);
        }
        String json = buildMessage(msg);
        post.setEntity(new StringEntity(json, Consts.UTF_8));
        return post;
    }

    private String buildMessage(GcmMessage msg) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(msg);
    }

    private List<Header> buildHttpHeader(String apiKey) {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Authorization", "key=" + apiKey));
        headers.add(new BasicHeader("Content-Type", "application/json"));
        return headers;
    }
}
