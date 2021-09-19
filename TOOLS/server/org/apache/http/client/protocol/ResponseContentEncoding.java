/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HeaderElement;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.HttpResponseInterceptor;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.client.entity.DeflateDecompressingEntity;
/*    */ import org.apache.http.client.entity.GzipDecompressingEntity;
/*    */ import org.apache.http.protocol.HttpContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class ResponseContentEncoding
/*    */   implements HttpResponseInterceptor
/*    */ {
/*    */   public static final String UNCOMPRESSED = "http.client.response.uncompressed";
/*    */   
/*    */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 74 */     HttpEntity entity = response.getEntity();
/*    */ 
/*    */ 
/*    */     
/* 78 */     if (entity != null && entity.getContentLength() != 0L) {
/* 79 */       Header ceheader = entity.getContentEncoding();
/* 80 */       if (ceheader != null) {
/* 81 */         HeaderElement[] codecs = ceheader.getElements();
/* 82 */         HeaderElement[] arr$ = codecs; int len$ = arr$.length, i$ = 0; if (i$ < len$) { HeaderElement codec = arr$[i$];
/* 83 */           String codecname = codec.getName().toLowerCase(Locale.US);
/* 84 */           if ("gzip".equals(codecname) || "x-gzip".equals(codecname)) {
/* 85 */             response.setEntity((HttpEntity)new GzipDecompressingEntity(response.getEntity()));
/* 86 */             if (context != null) context.setAttribute("http.client.response.uncompressed", Boolean.valueOf(true));  return;
/*    */           } 
/* 88 */           if ("deflate".equals(codecname)) {
/* 89 */             response.setEntity((HttpEntity)new DeflateDecompressingEntity(response.getEntity()));
/* 90 */             if (context != null) context.setAttribute("http.client.response.uncompressed", Boolean.valueOf(true));  return;
/*    */           } 
/* 92 */           if ("identity".equals(codecname)) {
/*    */             return;
/*    */           }
/*    */ 
/*    */           
/* 97 */           throw new HttpException("Unsupported Content-Coding: " + codec.getName()); }
/*    */       
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\protocol\ResponseContentEncoding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */