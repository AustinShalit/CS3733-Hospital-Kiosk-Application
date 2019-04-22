package edu.wpi.cs3733.d19.teamO.request.pizzapi;

import java.io.IOException;
import java.util.function.Consumer;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

final class Utilities {

  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

  private Utilities() {
    throw new UnsupportedOperationException();
  }

  static <T> T sendGetRequest(GenericUrl url,
                              Class<T> responseClass) throws IOException {
    HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(
        (HttpRequest request) -> request.setParser(new JsonObjectParser(JSON_FACTORY)));
    HttpRequest request = requestFactory.buildGetRequest(url);
    return request.execute().parseAs(responseClass);
  }

  static <T> T sendPostRequest(GenericUrl url,
                               Consumer<HttpHeaders> headersDecorator,
                               HttpContent content,
                               Class<T> responseClass) throws IOException {
    HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(
        (HttpRequest request) -> request.setParser(new JsonObjectParser(JSON_FACTORY)));
    HttpRequest request = requestFactory.buildPostRequest(url, content);
    headersDecorator.accept(request.getHeaders());
    return request.execute().parseAs(responseClass);
  }

  static JsonFactory getJsonFactory() {
    return JSON_FACTORY;
  }
}
