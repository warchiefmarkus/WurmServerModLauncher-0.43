/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.util.EntityUtils;
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
/*     */ public class HttpClientUtils
/*     */ {
/*     */   public static void closeQuietly(HttpResponse response) {
/*  69 */     if (response != null) {
/*  70 */       HttpEntity entity = response.getEntity();
/*  71 */       if (entity != null) {
/*     */         try {
/*  73 */           EntityUtils.consume(entity);
/*  74 */         } catch (IOException ex) {}
/*     */       }
/*     */     } 
/*     */   }
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
/*     */   public static void closeQuietly(HttpClient httpClient) {
/* 102 */     if (httpClient != null)
/* 103 */       httpClient.getConnectionManager().shutdown(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\clien\\utils\HttpClientUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */