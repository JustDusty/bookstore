package com.yassine.web.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yassine.web.config.HttpTraces;
import com.yassine.web.config.HttpTraces.Exchange;

@Controller
@RequestMapping("/admin/index")
public class DashboardController {

  @Value("${myapp.actuator.url}")
  private String actuatorUrl;

  private HttpTraces fetchJsonDataFromURL(String url) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    URL mapping = new URL(url);
    HttpTraces trace = mapper.readValue(mapping, HttpTraces.class);
    filterHttpTrace(trace);
    return trace;
  }


  @GetMapping
  public String adminHome(Model model) throws IOException {
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


  private void formatExchangeTimes(List<Exchange> exchanges) {
    for (Exchange exchange : exchanges) {
      ZonedDateTime timestamp = ZonedDateTime.parse(exchange.getTimestamp());
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
      String formattedTimestamp = timestamp.format(formatter);
      exchange.setTimestamp(formattedTimestamp);

      Duration timetaken = Duration.parse(exchange.getTimeTaken());
      long seconds = timetaken.getSeconds();
      int nanos = timetaken.getNano();
      String formattedTimetaken = String.format("%d.%09d s", seconds, nanos);
      exchange.setTimeTaken(formattedTimetaken);
    }
  }



}
