package net.optifine.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpPipelineSender extends Thread {
   private HttpPipelineConnection httpPipelineConnection = null;
   private static final String CRLF = null;
   private static Charset ASCII = Charset.forName("ASCII");

   private void writeRequest(HttpRequest httpRequest, OutputStream out) throws IOException {
      this.write(out, httpRequest.getMethod() + " " + httpRequest.getFile() + " " + httpRequest.getHttp() + "\r\n");
      Map headers = httpRequest.getHeaders();

      for (String str : (Iterable<String>)(headers.keySet())) {
         String str2 = (String)httpRequest.getHeaders().get(str);
         this.write(out, str + ": " + str2 + "\r\n");
      }

      this.write(out, "\r\n");
   }

   @Override
   public void run() {
      HttpPipelineRequest httpPipelineRequest = null;

      try {
         this.connect();

         while (!Thread.interrupted()) {
            httpPipelineRequest = this.httpPipelineConnection.getNextRequestSend();
            HttpRequest httpRequest = httpPipelineRequest.getHttpRequest();
            OutputStream outputStream = this.httpPipelineConnection.getOutputStream();
            this.writeRequest(httpRequest, outputStream);
            this.httpPipelineConnection.onRequestSent(httpPipelineRequest);
         }
      } catch (InterruptedException e) {
         return;
      } catch (Exception e2) {
         this.httpPipelineConnection.onExceptionSend(httpPipelineRequest, e2);
      }
   }

   private void connect() throws IOException {
      String host = this.httpPipelineConnection.getHost();
      int port = this.httpPipelineConnection.getPort();
      Proxy proxy = this.httpPipelineConnection.getProxy();
      Socket socket = new Socket(proxy);
      socket.connect(new InetSocketAddress(host, port), 5000);
      this.httpPipelineConnection.setSocket(socket);
   }

   public HttpPipelineSender(HttpPipelineConnection httpPipelineConnection2) {
      super("HttpPipelineSender");
      this.httpPipelineConnection = httpPipelineConnection2;
   }

   private void write(OutputStream out, String str) throws IOException {
      byte[] bytes = str.getBytes(ASCII);
      out.write(bytes);
   }
}
