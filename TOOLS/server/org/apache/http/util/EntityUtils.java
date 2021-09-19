/*     */ package org.apache.http.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.protocol.HTTP;
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
/*     */ public final class EntityUtils
/*     */ {
/*     */   public static void consumeQuietly(HttpEntity entity) {
/*     */     try {
/*  66 */       consume(entity);
/*  67 */     } catch (IOException ioex) {}
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
/*     */   public static void consume(HttpEntity entity) throws IOException {
/*  81 */     if (entity == null) {
/*     */       return;
/*     */     }
/*  84 */     if (entity.isStreaming()) {
/*  85 */       InputStream instream = entity.getContent();
/*  86 */       if (instream != null) {
/*  87 */         instream.close();
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
/*     */   public static byte[] toByteArray(HttpEntity entity) throws IOException {
/* 102 */     if (entity == null) {
/* 103 */       throw new IllegalArgumentException("HTTP entity may not be null");
/*     */     }
/* 105 */     InputStream instream = entity.getContent();
/* 106 */     if (instream == null) {
/* 107 */       return null;
/*     */     }
/*     */     try {
/* 110 */       if (entity.getContentLength() > 2147483647L) {
/* 111 */         throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
/*     */       }
/* 113 */       int i = (int)entity.getContentLength();
/* 114 */       if (i < 0) {
/* 115 */         i = 4096;
/*     */       }
/* 117 */       ByteArrayBuffer buffer = new ByteArrayBuffer(i);
/* 118 */       byte[] tmp = new byte[4096];
/*     */       int l;
/* 120 */       while ((l = instream.read(tmp)) != -1) {
/* 121 */         buffer.append(tmp, 0, l);
/*     */       }
/* 123 */       return buffer.toByteArray();
/*     */     } finally {
/* 125 */       instream.close();
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
/*     */   @Deprecated
/*     */   public static String getContentCharSet(HttpEntity entity) throws ParseException {
/* 141 */     if (entity == null) {
/* 142 */       throw new IllegalArgumentException("HTTP entity may not be null");
/*     */     }
/* 144 */     String charset = null;
/* 145 */     if (entity.getContentType() != null) {
/* 146 */       HeaderElement[] values = entity.getContentType().getElements();
/* 147 */       if (values.length > 0) {
/* 148 */         NameValuePair param = values[0].getParameterByName("charset");
/* 149 */         if (param != null) {
/* 150 */           charset = param.getValue();
/*     */         }
/*     */       } 
/*     */     } 
/* 154 */     return charset;
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
/*     */   @Deprecated
/*     */   public static String getContentMimeType(HttpEntity entity) throws ParseException {
/* 171 */     if (entity == null) {
/* 172 */       throw new IllegalArgumentException("HTTP entity may not be null");
/*     */     }
/* 174 */     String mimeType = null;
/* 175 */     if (entity.getContentType() != null) {
/* 176 */       HeaderElement[] values = entity.getContentType().getElements();
/* 177 */       if (values.length > 0) {
/* 178 */         mimeType = values[0].getName();
/*     */       }
/*     */     } 
/* 181 */     return mimeType;
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
/*     */   public static String toString(HttpEntity entity, Charset defaultCharset) throws IOException, ParseException {
/* 199 */     if (entity == null) {
/* 200 */       throw new IllegalArgumentException("HTTP entity may not be null");
/*     */     }
/* 202 */     InputStream instream = entity.getContent();
/* 203 */     if (instream == null) {
/* 204 */       return null;
/*     */     }
/*     */     try {
/* 207 */       if (entity.getContentLength() > 2147483647L) {
/* 208 */         throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
/*     */       }
/* 210 */       int i = (int)entity.getContentLength();
/* 211 */       if (i < 0) {
/* 212 */         i = 4096;
/*     */       }
/* 214 */       Charset charset = null;
/*     */       try {
/* 216 */         ContentType contentType = ContentType.get(entity);
/* 217 */         if (contentType != null) {
/* 218 */           charset = contentType.getCharset();
/*     */         }
/* 220 */       } catch (UnsupportedCharsetException ex) {
/* 221 */         throw new UnsupportedEncodingException(ex.getMessage());
/*     */       } 
/* 223 */       if (charset == null) {
/* 224 */         charset = defaultCharset;
/*     */       }
/* 226 */       if (charset == null) {
/* 227 */         charset = HTTP.DEF_CONTENT_CHARSET;
/*     */       }
/* 229 */       Reader reader = new InputStreamReader(instream, charset);
/* 230 */       CharArrayBuffer buffer = new CharArrayBuffer(i);
/* 231 */       char[] tmp = new char[1024];
/*     */       int l;
/* 233 */       while ((l = reader.read(tmp)) != -1) {
/* 234 */         buffer.append(tmp, 0, l);
/*     */       }
/* 236 */       return buffer.toString();
/*     */     } finally {
/* 238 */       instream.close();
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
/*     */   public static String toString(HttpEntity entity, String defaultCharset) throws IOException, ParseException {
/* 257 */     return toString(entity, (defaultCharset != null) ? Charset.forName(defaultCharset) : null);
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
/*     */   public static String toString(HttpEntity entity) throws IOException, ParseException {
/* 273 */     return toString(entity, (Charset)null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\htt\\util\EntityUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */