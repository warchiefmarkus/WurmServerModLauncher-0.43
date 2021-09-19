/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BasicHttpContext
/*     */   implements HttpContext
/*     */ {
/*     */   private final HttpContext parentContext;
/*  47 */   private Map<String, Object> map = null;
/*     */   
/*     */   public BasicHttpContext() {
/*  50 */     this(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHttpContext(HttpContext parentContext) {
/*  55 */     this.parentContext = parentContext;
/*     */   }
/*     */   
/*     */   public Object getAttribute(String id) {
/*  59 */     if (id == null) {
/*  60 */       throw new IllegalArgumentException("Id may not be null");
/*     */     }
/*  62 */     Object obj = null;
/*  63 */     if (this.map != null) {
/*  64 */       obj = this.map.get(id);
/*     */     }
/*  66 */     if (obj == null && this.parentContext != null) {
/*  67 */       obj = this.parentContext.getAttribute(id);
/*     */     }
/*  69 */     return obj;
/*     */   }
/*     */   
/*     */   public void setAttribute(String id, Object obj) {
/*  73 */     if (id == null) {
/*  74 */       throw new IllegalArgumentException("Id may not be null");
/*     */     }
/*  76 */     if (this.map == null) {
/*  77 */       this.map = new HashMap<String, Object>();
/*     */     }
/*  79 */     this.map.put(id, obj);
/*     */   }
/*     */   
/*     */   public Object removeAttribute(String id) {
/*  83 */     if (id == null) {
/*  84 */       throw new IllegalArgumentException("Id may not be null");
/*     */     }
/*  86 */     if (this.map != null) {
/*  87 */       return this.map.remove(id);
/*     */     }
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  97 */     if (this.map != null) {
/*  98 */       this.map.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     if (this.map != null) {
/* 105 */       return this.map.toString();
/*     */     }
/* 107 */     return "{}";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\BasicHttpContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */