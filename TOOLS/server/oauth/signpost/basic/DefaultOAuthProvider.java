/*    */ package oauth.signpost.basic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import oauth.signpost.AbstractOAuthProvider;
/*    */ import oauth.signpost.http.HttpRequest;
/*    */ import oauth.signpost.http.HttpResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultOAuthProvider
/*    */   extends AbstractOAuthProvider
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public DefaultOAuthProvider(String requestTokenEndpointUrl, String accessTokenEndpointUrl, String authorizationWebsiteUrl) {
/* 34 */     super(requestTokenEndpointUrl, accessTokenEndpointUrl, authorizationWebsiteUrl);
/*    */   }
/*    */ 
/*    */   
/*    */   protected HttpRequest createRequest(String endpointUrl) throws MalformedURLException, IOException {
/* 39 */     HttpURLConnection connection = (HttpURLConnection)(new URL(endpointUrl)).openConnection();
/* 40 */     connection.setRequestMethod("POST");
/* 41 */     connection.setAllowUserInteraction(false);
/* 42 */     connection.setRequestProperty("Content-Length", "0");
/* 43 */     return new HttpURLConnectionRequestAdapter(connection);
/*    */   }
/*    */   
/*    */   protected HttpResponse sendRequest(HttpRequest request) throws IOException {
/* 47 */     HttpURLConnection connection = (HttpURLConnection)request.unwrap();
/* 48 */     connection.connect();
/* 49 */     return new HttpURLConnectionResponseAdapter(connection);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void closeConnection(HttpRequest request, HttpResponse response) {
/* 54 */     HttpURLConnection connection = (HttpURLConnection)request.unwrap();
/* 55 */     if (connection != null)
/* 56 */       connection.disconnect(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\basic\DefaultOAuthProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */