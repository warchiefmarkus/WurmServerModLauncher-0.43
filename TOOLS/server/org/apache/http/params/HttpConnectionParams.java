/*     */ package org.apache.http.params;
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
/*     */ public final class HttpConnectionParams
/*     */   implements CoreConnectionPNames
/*     */ {
/*     */   public static int getSoTimeout(HttpParams params) {
/*  49 */     if (params == null) {
/*  50 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  52 */     return params.getIntParameter("http.socket.timeout", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setSoTimeout(HttpParams params, int timeout) {
/*  62 */     if (params == null) {
/*  63 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  65 */     params.setIntParameter("http.socket.timeout", timeout);
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
/*     */   public static boolean getSoReuseaddr(HttpParams params) {
/*  79 */     if (params == null) {
/*  80 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  82 */     return params.getBooleanParameter("http.socket.reuseaddr", false);
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
/*     */   public static void setSoReuseaddr(HttpParams params, boolean reuseaddr) {
/*  94 */     if (params == null) {
/*  95 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  97 */     params.setBooleanParameter("http.socket.reuseaddr", reuseaddr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getTcpNoDelay(HttpParams params) {
/* 108 */     if (params == null) {
/* 109 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 111 */     return params.getBooleanParameter("http.tcp.nodelay", true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setTcpNoDelay(HttpParams params, boolean value) {
/* 122 */     if (params == null) {
/* 123 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 125 */     params.setBooleanParameter("http.tcp.nodelay", value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getSocketBufferSize(HttpParams params) {
/* 136 */     if (params == null) {
/* 137 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 139 */     return params.getIntParameter("http.socket.buffer-size", -1);
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
/*     */   public static void setSocketBufferSize(HttpParams params, int size) {
/* 151 */     if (params == null) {
/* 152 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 154 */     params.setIntParameter("http.socket.buffer-size", size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getLinger(HttpParams params) {
/* 165 */     if (params == null) {
/* 166 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 168 */     return params.getIntParameter("http.socket.linger", -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setLinger(HttpParams params, int value) {
/* 178 */     if (params == null) {
/* 179 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 181 */     params.setIntParameter("http.socket.linger", value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getConnectionTimeout(HttpParams params) {
/* 192 */     if (params == null) {
/* 193 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 195 */     return params.getIntParameter("http.connection.timeout", 0);
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
/*     */   public static void setConnectionTimeout(HttpParams params, int timeout) {
/* 207 */     if (params == null) {
/* 208 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 210 */     params.setIntParameter("http.connection.timeout", timeout);
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
/*     */   public static boolean isStaleCheckingEnabled(HttpParams params) {
/* 222 */     if (params == null) {
/* 223 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 225 */     return params.getBooleanParameter("http.connection.stalecheck", true);
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
/*     */   public static void setStaleCheckingEnabled(HttpParams params, boolean value) {
/* 237 */     if (params == null) {
/* 238 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 240 */     params.setBooleanParameter("http.connection.stalecheck", value);
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
/*     */   public static boolean getSoKeepalive(HttpParams params) {
/* 254 */     if (params == null) {
/* 255 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 257 */     return params.getBooleanParameter("http.socket.keepalive", false);
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
/*     */   public static void setSoKeepalive(HttpParams params, boolean enableKeepalive) {
/* 269 */     if (params == null) {
/* 270 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 272 */     params.setBooleanParameter("http.socket.keepalive", enableKeepalive);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\params\HttpConnectionParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */