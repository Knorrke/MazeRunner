package application.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Serializer {
  public static ObjectMapper create() {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper;
  }
}
