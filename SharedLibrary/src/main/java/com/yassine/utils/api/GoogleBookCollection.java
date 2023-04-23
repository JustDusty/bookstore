package com.yassine.utils.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBookCollection {
  private Integer totalItems;
  private List<GoogleBook> items;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class GoogleBook {
    private VolumeInfo volumeInfo;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VolumeInfo {
      private String title;
      private List<String> authors;
      private String publisher;
      private String publishedDate;
      private String description;
      private List<IndustryIdentifier> industryIdentifiers;
      private int pageCount;
      private List<String> categories;
      private String previewLink;
      private String infoLink;
      private String language;
      private ImageLinks imageLinks;
      
      @Data
      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class ImageLinks {
        private String smallThumbnail;
        private String thumbnail;
      }
      

      @Data
      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class IndustryIdentifier {
        private String identifier;
      }
      
      
    }
  }

}
