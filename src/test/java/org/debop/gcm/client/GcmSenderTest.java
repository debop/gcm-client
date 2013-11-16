package org.debop.gcm.client;

import org.debop.gcm.message.GcmMessage;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * org.debop.gcm.client.GcmSenderTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 2013. 11. 17. 오전 3:37
 */
public class GcmSenderTest {

    // Sender 인증 토큰 (GCM 등록 시 발급받고, 푸시 요청 시 Registration ID (단말 ID)와 함께 제공해야 합니다.)
    public static final String SERVER_API_KEY = "AIzaSyCVQy7ncg8UXg7S7V2tviEel0mIaAN-dlc";

    // Registration ID
    public static final String DEVICE_ID_PANTEC = "APA91bHqx8bXum8-V6sq1Hwi5nbfBEfAMRXZW_4sQJlBZmpMfRQv4xxtwhVv3mhy2-FjUpE_5mayadpJXS0BTYBUNo7egb3zEJtYhUdJaaZlCCmXb_s7b_sM9BRMjyMGn69tZM1JaQ9Q3uaD1rRfzilhFo_XQYlbfQ";
    public static final String DEVICE_ID_LG = "APA91bGRhJN9CeuB2_CpMh0TAc0NZfD1m38l0CdXHKa5VdYt0J9b6PzH5lCk7t0rB20GN-TryCn7jjiU3J4ZO9zji312Rq91itZpVgplwV0Xyg_9Hs0gVtGMCzPooE9lIeH0oofPRDUJq9_wJFWymStjxStBO5lppA";


    @Test
    public void sendTest() throws Exception {
        GcmSender sender = new GcmSender(SERVER_API_KEY);
        GcmMessage msg =
                new GcmMessage.Builder()
                        .setTimeToLive(60)
                        .addData("body", "동해물과 백두산이")
                        .addData("eventKey", "eventKey1")
                        .build();

        int result = sender.send(DEVICE_ID_LG, msg, 3);
    }

    @Test
    public void sendMultipleTest() throws Exception {
        GcmSender sender = new GcmSender(SERVER_API_KEY);
        GcmMessage msg =
                new GcmMessage.Builder()
                        .setTimeToLive(60)
                        .addData("body", "동해물과 백두산이")
                        .addData("eventKey", "eventKey1")
                        .build();

        Set<String> registrationIds = new HashSet<String>();
        registrationIds.add(DEVICE_ID_LG);
        registrationIds.add(DEVICE_ID_PANTEC);

        int result = sender.send(registrationIds, msg, 3);
    }
}
