/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.cookie.CookieSpec;
/*    */ import org.apache.http.cookie.CookieSpecFactory;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ @Immutable
/*    */ public class RFC2109SpecFactory
/*    */   implements CookieSpecFactory
/*    */ {
/*    */   public CookieSpec newInstance(HttpParams params) {
/* 56 */     if (params != null) {
/*    */       
/* 58 */       String[] patterns = null;
/* 59 */       Collection<?> param = (Collection)params.getParameter("http.protocol.cookie-datepatterns");
/*    */       
/* 61 */       if (param != null) {
/* 62 */         patterns = new String[param.size()];
/* 63 */         patterns = param.<String>toArray(patterns);
/*    */       } 
/* 65 */       boolean singleHeader = params.getBooleanParameter("http.protocol.single-cookie-header", false);
/*    */ 
/*    */       
/* 68 */       return new RFC2109Spec(patterns, singleHeader);
/*    */     } 
/* 70 */     return new RFC2109Spec();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\RFC2109SpecFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */