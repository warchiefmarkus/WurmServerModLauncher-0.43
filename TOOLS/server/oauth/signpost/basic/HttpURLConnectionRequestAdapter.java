/*    */ package oauth.signpost.basic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import oauth.signpost.http.HttpRequest;
/*    */ 
/*    */ public class HttpURLConnectionRequestAdapter
/*    */   implements HttpRequest
/*    */ {
/*    */   protected HttpURLConnection connection;
/*    */   
/*    */   public HttpURLConnectionRequestAdapter(HttpURLConnection connection) {
/* 17 */     this.connection = connection;
/*    */   }
/*    */   
/*    */   public String getMethod() {
/* 21 */     return this.connection.getRequestMethod();
/*    */   }
/*    */   
/*    */   public String getRequestUrl() {
/* 25 */     return this.connection.getURL().toExternalForm();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRequestUrl(String url) {}
/*    */ 
/*    */   
/*    */   public void setHeader(String name, String value) {
/* 33 */     this.connection.setRequestProperty(name, value);
/*    */   }
/*    */   
/*    */   public String getHeader(String name) {
/* 37 */     return this.connection.getRequestProperty(name);
/*    */   }
/*    */   
/*    */   public Map<String, String> getAllHeaders() {
/* 41 */     Map<String, List<String>> origHeaders = this.connection.getRequestProperties();
/* 42 */     Map<String, String> headers = new HashMap<String, String>(origHeaders.size());
/* 43 */     for (String name : origHeaders.keySet()) {
/* 44 */       List<String> values = origHeaders.get(name);
/* 45 */       if (!values.isEmpty()) {
/* 46 */         headers.put(name, values.get(0));
/*    */       }
/*    */     } 
/* 49 */     return headers;
/*    */   }
/*    */   
/*    */   public InputStream getMessagePayload() throws IOException {
/* 53 */     return null;
/*    */   }
/*    */   
/*    */   public String getContentType() {
/* 57 */     return this.connection.getRequestProperty("Content-Type");
/*    */   }
/*    */   
/*    */   public HttpURLConnection unwrap() {
/* 61 */     return this.connection;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\basic\HttpURLConnectionRequestAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */