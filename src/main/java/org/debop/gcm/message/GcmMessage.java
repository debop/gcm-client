package org.debop.gcm.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

/**
 * GCM (Google Cloud Messaging) Message
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 2013. 11. 17. 오전 2:40
 */
@Getter
@Setter
public class GcmMessage implements Serializable {

    /**
     * Registration Id ( Device Id )
     */
    @JsonProperty("registration_ids")
    private Set<String> registrationIds = new HashSet<String>();

    @JsonProperty("collapse_key")
    private String collapseKey;

    @JsonProperty("time_to_live")
    private Integer timeToLive;

    @JsonProperty("delay_while_idle")
    private Boolean delayWhileIdle = false;

    @JsonProperty("data")
    private Map<String, String> data = new HashMap<String, String>();

    @Override
    public int hashCode() {
        return Objects.hash(registrationIds, collapseKey);
    }

    @Override
    public String toString() {
        return "GcmMessage#"
                + "registrationIds=" + registrationIds
                + ", collapseKey=" + collapseKey
                + ", delayWhileIdle=" + delayWhileIdle
                + ", timeToLive=" + timeToLive
                + ", data=" + data;
    }

    private static final long serialVersionUID = -1928411127821882044L;


    /**
     * GCM Message Builder
     */
    public static class Builder {
        private String collapseKey;
        private Boolean delayWhileIdle;
        private Integer timeToLive;
        private Map<String, String> data = new HashMap<String, String>();

        public Builder setCollapseKey(String value) {
            this.collapseKey = value;
            return this;
        }

        public Builder setDelayWhileIdle(Boolean value) {
            this.delayWhileIdle = value;
            return this;
        }

        public Builder setTimeToLive(Integer value) {
            this.timeToLive = value;
            return this;
        }

        public Builder addData(String key, String value) {
            this.data.put(key, value);
            return this;
        }

        public Builder addData(Map<String, String> map) {
            this.data.putAll(map);
            return this;
        }

        public GcmMessage build() {
            GcmMessage msg = new GcmMessage();
            msg.collapseKey = this.collapseKey;
            msg.delayWhileIdle = this.delayWhileIdle;
            msg.timeToLive = this.timeToLive;
            msg.data.putAll(this.data);

            return msg;
        }
    }
}
