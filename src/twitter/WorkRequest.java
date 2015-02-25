package twitter;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A POJO representation of a work request.
 */
class WorkRequest {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private String id;
    private String tweet;
    
    /**
     * Create a work request by parsing it from a JSON format.
     * 
     * @param json the JSON document to parse
     * @return the parsed work request
     * @throws IOException on error parsing the document
     */
    public static WorkRequest fromJson(final InputStream json) 
            throws IOException {
        return MAPPER.readValue(json, WorkRequest.class);
    }

    public String getId() {
    	return id;
    }
    
    public void setId(String Id) {
    	id = Id;
    }
    
    public String getTweet() {
    	return tweet;
    }
    
    public void setTweet(String Tweet) {
    	tweet = Tweet;
    }
    
    /**
     * Serialize the work request to JSON.
     * 
     * @return the serialized JSON
     * @throws IOException on error serializing the value
     */
    public String toJson() throws IOException {
        return MAPPER.writeValueAsString(this);
    }
}
