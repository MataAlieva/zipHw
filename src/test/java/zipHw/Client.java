package zipHw;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Client {
    @JsonProperty
    String clientType;
    @JsonProperty
    String clientObjId;
    @JsonProperty
    String clientId;

    public Client(String clientType, String clientObjId, String clientId) {
        this.clientType = clientType;
        this.clientObjId = clientObjId;
        this.clientId = clientId;
    }

    public Client() {
    }
}

