/*    */ package org.apache.http.client.methods;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.apache.http.annotation.NotThreadSafe;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @NotThreadSafe
/*    */ public class HttpPatch
/*    */   extends HttpEntityEnclosingRequestBase
/*    */ {
/*    */   public static final String METHOD_NAME = "PATCH";
/*    */   
/*    */   public HttpPatch() {}
/*    */   
/*    */   public HttpPatch(URI uri) {
/* 62 */     setURI(uri);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpPatch(String uri) {
/* 67 */     setURI(URI.create(uri));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMethod() {
/* 72 */     return "PATCH";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\methods\HttpPatch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */