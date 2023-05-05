package com.yassine.web.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yassine.web.config.HttpTraces;
import com.yassine.web.config.HttpTraces.Exchange;

@Controller
@RequestMapping
public class DashboardController {

  @Value("${myapp.actuator.url}")
  private String actuatorUrl;


  private HttpTraces fetchJsonDataFromURL(String url) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    URL jsonResponse = new URL(url);
    HttpTraces trace = mapper.readValue(jsonResponse, HttpTraces.class);
    filterHttpTrace(trace);

    for (Exchange exchange : trace.getExchanges()) {
      Map<String, List<String>> headers = exchange.getRequest().getHeaders();
      for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
        String headerName = entry.getKey();
        List<String> headerValues = entry.getValue();

        // print the header name and values
        System.out.println(headerName + ": " + String.join(",", headerValues));

      }
    }

    return trace;
  }


  private void formatExchangeTimes(List<Exchange> exchanges) {
    for (Exchange exchange : exchanges) {
      ZonedDateTime timestamp = ZonedDateTime.parse(exchange.getTimestamp());
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy - HH:mm:ss");
      String formattedTimestamp = timestamp.format(formatter);
      exchange.setTimestamp(formattedTimestamp);

      Duration timetaken = Duration.parse(exchange.getTimeTaken());
      long seconds = timetaken.getSeconds();
      int nanos = timetaken.getNano();
      String formattedTimetaken = String.format("%d.%09d s", seconds, nanos);
      exchange.setTimeTaken(formattedTimetaken);
    }
  }


  @GetMapping("/admin/index")
  public String dashboard(Model model) throws IOException {
    model.addAttribute("httpTraces", fetchJsonDataFromURL(actuatorUrl));
    return "admin/index";
  }


  public void filterHttpTrace(HttpTraces httpTrace) {
    List<Exchange> exchanges = httpTrace.getExchanges();
    formatExchangeTimes(exchanges);
    List<Exchange> filtered = new ArrayList<>();
    for (Exchange exchange : exchanges) {
      String uri = exchange.getRequest().getUri();
      if (!(uri.contains("css") || uri.contains("js") || uri.contains("fonts")
          || uri.contains("images") || uri.contains("img") || uri.contains("placeholder")
          || uri.contains("favicon")))
        filtered.add(exchange);
    }
    httpTrace.setExchanges(filtered);
  }



}
