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
/*    */ @Immutable
/*    */ public class BrowserCompatSpecFactory
/*    */   implements CookieSpecFactory
/*    */ {
/*    */   public CookieSpec newInstance(HttpParams params) {
/* 55 */     if (params != null) {
/*    */       
/* 57 */       String[] patterns = null;
/* 58 */       Collection<?> param = (Collection)params.getParameter("http.protocol.cookie-datepatterns");
/*    */       
/* 60 */       if (param != null) {
/* 61 */         patterns = new String[param.size()];
/* 62 */         patterns = param.<String>toArray(patterns);
/*    */       } 
/* 64 */       return new BrowserCompatSpec(patterns);
/*    */     } 
/* 66 */     return new BrowserCompatSpec();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BrowserCompatSpecFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */