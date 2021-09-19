/*    */ package oauth.signpost.basic;
/*    */ 
/*    */ import java.net.HttpURLConnection;
/*    */ import oauth.signpost.AbstractOAuthConsumer;
/*    */ import oauth.signpost.http.HttpRequest;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultOAuthConsumer
/*    */   extends AbstractOAuthConsumer
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public DefaultOAuthConsumer(String consumerKey, String consumerSecret) {
/* 33 */     super(consumerKey, consumerSecret);
/*    */   }
/*    */ 
/*    */   
/*    */   protected HttpRequest wrap(Object request) {
/* 38 */     if (!(request instanceof HttpURLConnection)) {
/* 39 */       throw new IllegalArgumentException("The default consumer expects requests of type java.net.HttpURLConnection");
/*    */     }
/*    */     
/* 42 */     return new HttpURLConnectionRequestAdapter((HttpURLConnection)request);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\basic\DefaultOAuthConsumer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */