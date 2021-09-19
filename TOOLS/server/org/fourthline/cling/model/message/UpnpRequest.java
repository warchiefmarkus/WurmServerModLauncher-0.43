/*     */ package org.fourthline.cling.model.message;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ public class UpnpRequest
/*     */   extends UpnpOperation
/*     */ {
/*     */   private Method method;
/*     */   private URI uri;
/*     */   
/*     */   public enum Method
/*     */   {
/*  34 */     GET("GET"),
/*  35 */     POST("POST"),
/*  36 */     NOTIFY("NOTIFY"),
/*  37 */     MSEARCH("M-SEARCH"),
/*  38 */     SUBSCRIBE("SUBSCRIBE"),
/*  39 */     UNSUBSCRIBE("UNSUBSCRIBE"),
/*  40 */     UNKNOWN("UNKNOWN");
/*     */     
/*  42 */     private static Map<String, Method> byName = new HashMap<String, Method>()
/*     */       {
/*     */       
/*     */       };
/*     */     
/*     */     private String httpName;
/*     */ 
/*     */     
/*     */     Method(String httpName) {
/*  51 */       this.httpName = httpName;
/*     */     } static {
/*     */     
/*     */     } public String getHttpName() {
/*  55 */       return this.httpName;
/*     */     }
/*     */     
/*     */     public static Method getByHttpName(String httpName) {
/*  59 */       if (httpName == null) return UNKNOWN; 
/*  60 */       Method m = byName.get(httpName.toUpperCase(Locale.ROOT));
/*  61 */       return (m != null) ? m : UNKNOWN;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UpnpRequest(Method method) {
/*  69 */     this.method = method;
/*     */   }
/*     */   
/*     */   public UpnpRequest(Method method, URI uri) {
/*  73 */     this.method = method;
/*  74 */     this.uri = uri;
/*     */   }
/*     */   
/*     */   public UpnpRequest(Method method, URL url) {
/*  78 */     this.method = method;
/*     */     try {
/*  80 */       if (url != null) {
/*  81 */         this.uri = url.toURI();
/*     */       }
/*  83 */     } catch (URISyntaxException e) {
/*  84 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Method getMethod() {
/*  89 */     return this.method;
/*     */   }
/*     */   
/*     */   public String getHttpMethodName() {
/*  93 */     return this.method.getHttpName();
/*     */   }
/*     */   
/*     */   public URI getURI() {
/*  97 */     return this.uri;
/*     */   }
/*     */   
/*     */   public void setUri(URI uri) {
/* 101 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     return getHttpMethodName() + ((getURI() != null) ? (" " + getURI()) : "");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\UpnpRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */