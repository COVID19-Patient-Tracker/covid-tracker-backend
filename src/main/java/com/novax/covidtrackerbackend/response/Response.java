package com.novax.covidtrackerbackend.response;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;

@Data
@Configuration
public class Response {

    private HashMap<String,Object> responseBody;
    private int responseCode;

    @Bean
    public Response getResponseObject(){
        return new Response();
    }

    public Response() {
        this.responseBody = new HashMap<>();
    }

    public ResponseEntity<HashMap<String,Object>> getResponseEntity(){
        return ResponseEntity.status(responseCode).contentType(MediaType.APPLICATION_JSON).body(responseBody);
    }

    public Response reset() {
        this.responseBody = new HashMap<>();
        return this;
    }

    public Response setMessage(String message) {
        responseBody.put("message",message);
        return this;
    }

    public Response setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        responseBody.put("status",responseCode);
        return this;
    }

    public Response setURI(String URI) {
        System.out.println(URI);
        responseBody.put("URI",URI);
        return this;
    }

    public Response setException(Exception e) {
        responseBody.put("exception",e.getMessage());
        return this;
    }

    public Response addField(String fieldName,Object value){
        responseBody.put(fieldName,value);
        return this;
    }

}
