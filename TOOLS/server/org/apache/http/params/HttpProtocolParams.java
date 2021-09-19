/*     */ package org.apache.http.params;
/*     */ 
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
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
/*     */ public final class HttpProtocolParams
/*     */   implements CoreProtocolPNames
/*     */ {
/*     */   public static String getHttpElementCharset(HttpParams params) {
/*  57 */     if (params == null) {
/*  58 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  60 */     String charset = (String)params.getParameter("http.protocol.element-charset");
/*     */     
/*  62 */     if (charset == null) {
/*  63 */       charset = HTTP.DEF_PROTOCOL_CHARSET.name();
/*     */     }
/*  65 */     return charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setHttpElementCharset(HttpParams params, String charset) {
/*  75 */     if (params == null) {
/*  76 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  78 */     params.setParameter("http.protocol.element-charset", charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getContentCharset(HttpParams params) {
/*  89 */     if (params == null) {
/*  90 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  92 */     String charset = (String)params.getParameter("http.protocol.content-charset");
/*     */     
/*  94 */     if (charset == null) {
/*  95 */       charset = HTTP.DEF_CONTENT_CHARSET.name();
/*     */     }
/*  97 */     return charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setContentCharset(HttpParams params, String charset) {
/* 107 */     if (params == null) {
/* 108 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 110 */     params.setParameter("http.protocol.content-charset", charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProtocolVersion getVersion(HttpParams params) {
/* 121 */     if (params == null) {
/* 122 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 124 */     Object param = params.getParameter("http.protocol.version");
/*     */     
/* 126 */     if (param == null) {
/* 127 */       return (ProtocolVersion)HttpVersion.HTTP_1_1;
/*     */     }
/* 129 */     return (ProtocolVersion)param;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setVersion(HttpParams params, ProtocolVersion version) {
/* 139 */     if (params == null) {
/* 140 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 142 */     params.setParameter("http.protocol.version", version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getUserAgent(HttpParams params) {
/* 153 */     if (params == null) {
/* 154 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 156 */     return (String)params.getParameter("http.useragent");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setUserAgent(HttpParams params, String useragent) {
/* 166 */     if (params == null) {
/* 167 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 169 */     params.setParameter("http.useragent", useragent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean useExpectContinue(HttpParams params) {
/* 180 */     if (params == null) {
/* 181 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 183 */     return params.getBooleanParameter("http.protocol.expect-continue", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setUseExpectContinue(HttpParams params, boolean b) {
/* 194 */     if (params == null) {
/* 195 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 197 */     params.setBooleanParameter("http.protocol.expect-continue", b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CodingErrorAction getMalformedInputAction(HttpParams params) {
/* 208 */     if (params == null) {
/* 209 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 211 */     Object param = params.getParameter("http.malformed.input.action");
/* 212 */     if (param == null)
/*     */     {
/* 214 */       return CodingErrorAction.REPORT;
/*     */     }
/* 216 */     return (CodingErrorAction)param;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setMalformedInputAction(HttpParams params, CodingErrorAction action) {
/* 227 */     if (params == null) {
/* 228 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 230 */     params.setParameter("http.malformed.input.action", action);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CodingErrorAction getUnmappableInputAction(HttpParams params) {
/* 241 */     if (params == null) {
/* 242 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 244 */     Object param = params.getParameter("http.unmappable.input.action");
/* 245 */     if (param == null)
/*     */     {
/* 247 */       return CodingErrorAction.REPORT;
/*     */     }
/* 249 */     return (CodingErrorAction)param;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setUnmappableInputAction(HttpParams params, CodingErrorAction action) {
/* 260 */     if (params == null) {
/* 261 */       throw new IllegalArgumentException("HTTP parameters may no be null");
/*     */     }
/* 263 */     params.setParameter("http.unmappable.input.action", action);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\params\HttpProtocolParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */