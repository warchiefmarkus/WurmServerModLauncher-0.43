/*    */ package org.apache.http.protocol;
/*    */ 
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
/*    */ @NotThreadSafe
/*    */ public final class DefaultedHttpContext
/*    */   implements HttpContext
/*    */ {
/*    */   private final HttpContext local;
/*    */   private final HttpContext defaults;
/*    */   
/*    */   public DefaultedHttpContext(HttpContext local, HttpContext defaults) {
/* 48 */     if (local == null) {
/* 49 */       throw new IllegalArgumentException("HTTP context may not be null");
/*    */     }
/* 51 */     this.local = local;
/* 52 */     this.defaults = defaults;
/*    */   }
/*    */   
/*    */   public Object getAttribute(String id) {
/* 56 */     Object obj = this.local.getAttribute(id);
/* 57 */     if (obj == null) {
/* 58 */       return this.defaults.getAttribute(id);
/*    */     }
/* 60 */     return obj;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object removeAttribute(String id) {
/* 65 */     return this.local.removeAttribute(id);
/*    */   }
/*    */   
/*    */   public void setAttribute(String id, Object obj) {
/* 69 */     this.local.setAttribute(id, obj);
/*    */   }
/*    */   
/*    */   public HttpContext getDefaults() {
/* 73 */     return this.defaults;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 78 */     StringBuilder buf = new StringBuilder();
/* 79 */     buf.append("[local: ").append(this.local);
/* 80 */     buf.append("defaults: ").append(this.defaults);
/* 81 */     buf.append("]");
/* 82 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\DefaultedHttpContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */