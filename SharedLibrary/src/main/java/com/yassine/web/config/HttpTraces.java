package com.yassine.web.config;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpTraces{
  private List<Exchange> exchanges;
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Exchange {
    private String timestamp;
    private Request request;
    private Response response;
    private String timeTaken;

    @Data
    public static class Request {
      private String uri;
      private String method;
      private Map<String, List<String>> headers;
    }
    @Data
    public static class Response {
        private int status;
        private Map<String, List<String>> headers;
    }
  }
 }
