/*     */ package org.apache.http.impl.entity;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @Immutable
/*     */ public class LaxContentLengthStrategy
/*     */   implements ContentLengthStrategy
/*     */ {
/*     */   private final int implicitLen;
/*     */   
/*     */   public LaxContentLengthStrategy(int implicitLen) {
/*  72 */     this.implicitLen = implicitLen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LaxContentLengthStrategy() {
/*  80 */     this(-1);
/*     */   }
/*     */   
/*     */   public long determineLength(HttpMessage message) throws HttpException {
/*  84 */     if (message == null) {
/*  85 */       throw new IllegalArgumentException("HTTP message may not be null");
/*     */     }
/*     */     
/*  88 */     HttpParams params = message.getParams();
/*  89 */     boolean strict = params.isParameterTrue("http.protocol.strict-transfer-encoding");
/*     */     
/*  91 */     Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
/*     */ 
/*     */     
/*  94 */     if (transferEncodingHeader != null) {
/*  95 */       HeaderElement[] encodings = null;
/*     */       try {
/*  97 */         encodings = transferEncodingHeader.getElements();
/*  98 */       } catch (ParseException px) {
/*  99 */         throw new ProtocolException("Invalid Transfer-Encoding header value: " + transferEncodingHeader, px);
/*     */       } 
/*     */ 
/*     */       
/* 103 */       if (strict)
/*     */       {
/* 105 */         for (int i = 0; i < encodings.length; i++) {
/* 106 */           String encoding = encodings[i].getName();
/* 107 */           if (encoding != null && encoding.length() > 0 && !encoding.equalsIgnoreCase("chunked") && !encoding.equalsIgnoreCase("identity"))
/*     */           {
/*     */             
/* 110 */             throw new ProtocolException("Unsupported transfer encoding: " + encoding);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 115 */       int len = encodings.length;
/* 116 */       if ("identity".equalsIgnoreCase(transferEncodingHeader.getValue()))
/* 117 */         return -1L; 
/* 118 */       if (len > 0 && "chunked".equalsIgnoreCase(encodings[len - 1].getName()))
/*     */       {
/* 120 */         return -2L;
/*     */       }
/* 122 */       if (strict) {
/* 123 */         throw new ProtocolException("Chunk-encoding must be the last one applied");
/*     */       }
/* 125 */       return -1L;
/*     */     } 
/*     */     
/* 128 */     Header contentLengthHeader = message.getFirstHeader("Content-Length");
/* 129 */     if (contentLengthHeader != null) {
/* 130 */       long contentlen = -1L;
/* 131 */       Header[] headers = message.getHeaders("Content-Length");
/* 132 */       if (strict && headers.length > 1) {
/* 133 */         throw new ProtocolException("Multiple content length headers");
/*     */       }
/* 135 */       for (int i = headers.length - 1; i >= 0; i--) {
/* 136 */         Header header = headers[i];
/*     */         try {
/* 138 */           contentlen = Long.parseLong(header.getValue());
/*     */           break;
/* 140 */         } catch (NumberFormatException e) {
/* 141 */           if (strict) {
/* 142 */             throw new ProtocolException("Invalid content length: " + header.getValue());
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 147 */       if (contentlen >= 0L) {
/* 148 */         return contentlen;
/*     */       }
/* 150 */       return -1L;
/*     */     } 
/*     */     
/* 153 */     return this.implicitLen;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\entity\LaxContentLengthStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */