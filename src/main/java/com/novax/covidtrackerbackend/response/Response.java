package com.novax.covidtrackerbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Data
public class Response<T extends Object> {

    private HashMap<String,Object> responseBody;
    private int responseCode;

    public Response() {
        this.responseBody = new HashMap<>();
    }

    public ResponseEntity<HashMap<String,Object>> getResponseEntity(){
        return ResponseEntity
                .status(responseCode)
                .body(responseBody);
    }

    public void reset() {
        this.responseBody = new HashMap<>();
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
        responseBody.put("Exception",e.getMessage());
        return this;
    }

    public Response addField(String fieldName,Object value){
        responseBody.put(fieldName,value);
        return this;
    }

}
