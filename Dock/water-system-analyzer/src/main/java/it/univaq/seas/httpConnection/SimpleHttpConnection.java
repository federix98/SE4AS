/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.seas.httpConnection;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.hc.client5.http.async.methods.SimpleBody;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;

/**
 *
 * @author valerio
 */
public class SimpleHttpConnection {
    
    public static final String URLDOCKER = "http://water-system-planner:8081/message";
    public static final String URLLOCAL = "http://localhost:8081/message";
    public static void invoke( String message) {
  try(
   CloseableHttpAsyncClient client = 
      HttpAsyncClients.createDefault();) {
   client.start();
    
    final SimpleHttpRequest request;
      request = SimpleRequestBuilder
              .post()
              .setUri(URLLOCAL)
              .setBody(message, ContentType.APPLICATION_JSON)
              .build();
    
    Future<SimpleHttpResponse> future = 
     client.execute(request, 
      new FutureCallback<SimpleHttpResponse>() {

       @Override
       public void completed(SimpleHttpResponse result) {
        String response = result.getBodyText();
        System.out.println("response::"+response);
       }

       @Override
       public void failed(Exception ex) {
        System.out.println("response::"+ex);
       }

       @Override
       public void cancelled() {
        // do nothing
       }
        
      });
    
    HttpResponse response = future.get();
    
    // Get HttpResponse Status
    System.out.println(response.getCode()); // 200
    System.out.println(response.getReasonPhrase()); // OK
 
  } catch (InterruptedException 
     | ExecutionException 
     | IOException e) {
    e.printStackTrace();
  } 
 }

    
}
