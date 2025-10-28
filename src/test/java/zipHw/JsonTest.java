package zipHw;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class JsonTest {
    @Test
    public void testJson() throws IOException {
        File file = new File("src/test/resources/sampleJson.json");
        ObjectMapper mapper = new ObjectMapper();
        Client client = mapper.readValue(file,Client.class);
        Assertions.assertEquals("141658",client.clientId);
    }
}
