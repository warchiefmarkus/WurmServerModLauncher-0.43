/*     */ package org.apache.http.conn.scheme;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Scheme
/*     */ {
/*     */   private final String name;
/*     */   private final SchemeSocketFactory socketFactory;
/*     */   private final int defaultPort;
/*     */   private final boolean layered;
/*     */   private String stringRep;
/*     */   
/*     */   public Scheme(String name, int port, SchemeSocketFactory factory) {
/*  87 */     if (name == null) {
/*  88 */       throw new IllegalArgumentException("Scheme name may not be null");
/*     */     }
/*  90 */     if (port <= 0 || port > 65535) {
/*  91 */       throw new IllegalArgumentException("Port is invalid: " + port);
/*     */     }
/*  93 */     if (factory == null) {
/*  94 */       throw new IllegalArgumentException("Socket factory may not be null");
/*     */     }
/*  96 */     this.name = name.toLowerCase(Locale.ENGLISH);
/*  97 */     this.defaultPort = port;
/*  98 */     if (factory instanceof SchemeLayeredSocketFactory) {
/*  99 */       this.layered = true;
/* 100 */       this.socketFactory = factory;
/* 101 */     } else if (factory instanceof LayeredSchemeSocketFactory) {
/* 102 */       this.layered = true;
/* 103 */       this.socketFactory = new SchemeLayeredSocketFactoryAdaptor2((LayeredSchemeSocketFactory)factory);
/*     */     } else {
/* 105 */       this.layered = false;
/* 106 */       this.socketFactory = factory;
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
/*     */   @Deprecated
/*     */   public Scheme(String name, SocketFactory factory, int port) {
/* 128 */     if (name == null) {
/* 129 */       throw new IllegalArgumentException("Scheme name may not be null");
/*     */     }
/*     */     
/* 132 */     if (factory == null) {
/* 133 */       throw new IllegalArgumentException("Socket factory may not be null");
/*     */     }
/*     */     
/* 136 */     if (port <= 0 || port > 65535) {
/* 137 */       throw new IllegalArgumentException("Port is invalid: " + port);
/*     */     }
/*     */ 
/*     */     
/* 141 */     this.name = name.toLowerCase(Locale.ENGLISH);
/* 142 */     if (factory instanceof LayeredSocketFactory) {
/* 143 */       this.socketFactory = new SchemeLayeredSocketFactoryAdaptor((LayeredSocketFactory)factory);
/*     */       
/* 145 */       this.layered = true;
/*     */     } else {
/* 147 */       this.socketFactory = new SchemeSocketFactoryAdaptor(factory);
/* 148 */       this.layered = false;
/*     */     } 
/* 150 */     this.defaultPort = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getDefaultPort() {
/* 159 */     return this.defaultPort;
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
/*     */   public final SocketFactory getSocketFactory() {
/* 174 */     if (this.socketFactory instanceof SchemeSocketFactoryAdaptor) {
/* 175 */       return ((SchemeSocketFactoryAdaptor)this.socketFactory).getFactory();
/*     */     }
/* 177 */     if (this.layered) {
/* 178 */       return new LayeredSocketFactoryAdaptor((LayeredSchemeSocketFactory)this.socketFactory);
/*     */     }
/*     */     
/* 181 */     return new SocketFactoryAdaptor(this.socketFactory);
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
/*     */   public final SchemeSocketFactory getSchemeSocketFactory() {
/* 196 */     return this.socketFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 205 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isLayered() {
/* 215 */     return this.layered;
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
/*     */   public final int resolvePort(int port) {
/* 228 */     return (port <= 0) ? this.defaultPort : port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 238 */     if (this.stringRep == null) {
/* 239 */       StringBuilder buffer = new StringBuilder();
/* 240 */       buffer.append(this.name);
/* 241 */       buffer.append(':');
/* 242 */       buffer.append(Integer.toString(this.defaultPort));
/* 243 */       this.stringRep = buffer.toString();
/*     */     } 
/* 245 */     return this.stringRep;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 250 */     if (this == obj) return true; 
/* 251 */     if (obj instanceof Scheme) {
/* 252 */       Scheme that = (Scheme)obj;
/* 253 */       return (this.name.equals(that.name) && this.defaultPort == that.defaultPort && this.layered == that.layered);
/*     */     } 
/*     */ 
/*     */     
/* 257 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 263 */     int hash = 17;
/* 264 */     hash = LangUtils.hashCode(hash, this.defaultPort);
/* 265 */     hash = LangUtils.hashCode(hash, this.name);
/* 266 */     hash = LangUtils.hashCode(hash, this.layered);
/* 267 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\scheme\Scheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */