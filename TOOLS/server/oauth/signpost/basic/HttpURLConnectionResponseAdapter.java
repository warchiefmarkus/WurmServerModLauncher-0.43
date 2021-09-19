/*    */ package oauth.signpost.basic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import oauth.signpost.http.HttpResponse;
/*    */ 
/*    */ public class HttpURLConnectionResponseAdapter
/*    */   implements HttpResponse
/*    */ {
/*    */   private HttpURLConnection connection;
/*    */   
/*    */   public HttpURLConnectionResponseAdapter(HttpURLConnection connection) {
/* 14 */     this.connection = connection;
/*    */   }
/*    */   
/*    */   public InputStream getContent() throws IOException {
/*    */     try {
/* 19 */       return this.connection.getInputStream();
/* 20 */     } catch (IOException e) {
/* 21 */       return this.connection.getErrorStream();
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getStatusCode() throws IOException {
/* 26 */     return this.connection.getResponseCode();
/*    */   }
/*    */   
/*    */   public String getReasonPhrase() throws Exception {
/* 30 */     return this.connection.getResponseMessage();
/*    */   }
/*    */   
/*    */   public Object unwrap() {
/* 34 */     return this.connection;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\basic\HttpURLConnectionResponseAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */