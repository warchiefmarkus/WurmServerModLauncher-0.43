/*    */ package oauth.signpost.basic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import oauth.signpost.http.HttpRequest;
/*    */ 
/*    */ public class UrlStringRequestAdapter
/*    */   implements HttpRequest
/*    */ {
/*    */   private String url;
/*    */   
/*    */   public UrlStringRequestAdapter(String url) {
/* 15 */     this.url = url;
/*    */   }
/*    */   
/*    */   public String getMethod() {
/* 19 */     return "GET";
/*    */   }
/*    */   
/*    */   public String getRequestUrl() {
/* 23 */     return this.url;
/*    */   }
/*    */   
/*    */   public void setRequestUrl(String url) {
/* 27 */     this.url = url;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setHeader(String name, String value) {}
/*    */   
/*    */   public String getHeader(String name) {
/* 34 */     return null;
/*    */   }
/*    */   
/*    */   public Map<String, String> getAllHeaders() {
/* 38 */     return Collections.emptyMap();
/*    */   }
/*    */   
/*    */   public InputStream getMessagePayload() throws IOException {
/* 42 */     return null;
/*    */   }
/*    */   
/*    */   public String getContentType() {
/* 46 */     return null;
/*    */   }
/*    */   
/*    */   public Object unwrap() {
/* 50 */     return this.url;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\basic\UrlStringRequestAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */