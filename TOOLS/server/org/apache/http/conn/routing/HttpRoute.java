/*     */ package org.apache.http.conn.routing;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.apache.http.HttpHost;
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
/*     */ @Immutable
/*     */ public final class HttpRoute
/*     */   implements RouteInfo, Cloneable
/*     */ {
/*  47 */   private static final HttpHost[] EMPTY_HTTP_HOST_ARRAY = new HttpHost[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpHost targetHost;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InetAddress localAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpHost[] proxyChain;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final RouteInfo.TunnelType tunnelled;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final RouteInfo.LayerType layered;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean secure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpRoute(InetAddress local, HttpHost target, HttpHost[] proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
/*  95 */     if (target == null) {
/*  96 */       throw new IllegalArgumentException("Target host may not be null.");
/*     */     }
/*     */     
/*  99 */     if (proxies == null) {
/* 100 */       throw new IllegalArgumentException("Proxies may not be null.");
/*     */     }
/*     */     
/* 103 */     if (tunnelled == RouteInfo.TunnelType.TUNNELLED && proxies.length == 0) {
/* 104 */       throw new IllegalArgumentException("Proxy required if tunnelled.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 109 */     if (tunnelled == null)
/* 110 */       tunnelled = RouteInfo.TunnelType.PLAIN; 
/* 111 */     if (layered == null) {
/* 112 */       layered = RouteInfo.LayerType.PLAIN;
/*     */     }
/* 114 */     this.targetHost = target;
/* 115 */     this.localAddress = local;
/* 116 */     this.proxyChain = proxies;
/* 117 */     this.secure = secure;
/* 118 */     this.tunnelled = tunnelled;
/* 119 */     this.layered = layered;
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, HttpHost[] proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
/* 138 */     this(local, target, toChain(proxies), secure, tunnelled, layered);
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
/* 161 */     this(local, target, toChain(proxy), secure, tunnelled, layered);
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, boolean secure) {
/* 176 */     this(local, target, EMPTY_HTTP_HOST_ARRAY, secure, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRoute(HttpHost target) {
/* 186 */     this((InetAddress)null, target, EMPTY_HTTP_HOST_ARRAY, false, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
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
/*     */   public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure) {
/* 205 */     this(local, target, toChain(proxy), secure, secure ? RouteInfo.TunnelType.TUNNELLED : RouteInfo.TunnelType.PLAIN, secure ? RouteInfo.LayerType.LAYERED : RouteInfo.LayerType.PLAIN);
/*     */ 
/*     */     
/* 208 */     if (proxy == null) {
/* 209 */       throw new IllegalArgumentException("Proxy host may not be null.");
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
/*     */   private static HttpHost[] toChain(HttpHost proxy) {
/* 223 */     if (proxy == null) {
/* 224 */       return EMPTY_HTTP_HOST_ARRAY;
/*     */     }
/* 226 */     return new HttpHost[] { proxy };
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
/*     */   private static HttpHost[] toChain(HttpHost[] proxies) {
/* 239 */     if (proxies == null || proxies.length < 1) {
/* 240 */       return EMPTY_HTTP_HOST_ARRAY;
/*     */     }
/* 242 */     for (HttpHost proxy : proxies) {
/* 243 */       if (proxy == null) {
/* 244 */         throw new IllegalArgumentException("Proxy chain may not contain null elements.");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 249 */     HttpHost[] result = new HttpHost[proxies.length];
/* 250 */     System.arraycopy(proxies, 0, result, 0, proxies.length);
/*     */     
/* 252 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpHost getTargetHost() {
/* 259 */     return this.targetHost;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final InetAddress getLocalAddress() {
/* 265 */     return this.localAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getHopCount() {
/* 270 */     return this.proxyChain.length + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HttpHost getHopTarget(int hop) {
/* 275 */     if (hop < 0) {
/* 276 */       throw new IllegalArgumentException("Hop index must not be negative: " + hop);
/*     */     }
/* 278 */     int hopcount = getHopCount();
/* 279 */     if (hop >= hopcount) {
/* 280 */       throw new IllegalArgumentException("Hop index " + hop + " exceeds route length " + hopcount);
/*     */     }
/*     */ 
/*     */     
/* 284 */     HttpHost result = null;
/* 285 */     if (hop < hopcount - 1) {
/* 286 */       result = this.proxyChain[hop];
/*     */     } else {
/* 288 */       result = this.targetHost;
/*     */     } 
/* 290 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HttpHost getProxyHost() {
/* 295 */     return (this.proxyChain.length == 0) ? null : this.proxyChain[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public final RouteInfo.TunnelType getTunnelType() {
/* 300 */     return this.tunnelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isTunnelled() {
/* 305 */     return (this.tunnelled == RouteInfo.TunnelType.TUNNELLED);
/*     */   }
/*     */ 
/*     */   
/*     */   public final RouteInfo.LayerType getLayerType() {
/* 310 */     return this.layered;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isLayered() {
/* 315 */     return (this.layered == RouteInfo.LayerType.LAYERED);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSecure() {
/* 320 */     return this.secure;
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
/*     */   public final boolean equals(Object obj) {
/* 334 */     if (this == obj) return true; 
/* 335 */     if (obj instanceof HttpRoute) {
/* 336 */       HttpRoute that = (HttpRoute)obj;
/* 337 */       return (this.secure == that.secure && this.tunnelled == that.tunnelled && this.layered == that.layered && LangUtils.equals(this.targetHost, that.targetHost) && LangUtils.equals(this.localAddress, that.localAddress) && LangUtils.equals((Object[])this.proxyChain, (Object[])that.proxyChain));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 346 */     return false;
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
/*     */   public final int hashCode() {
/* 358 */     int hash = 17;
/* 359 */     hash = LangUtils.hashCode(hash, this.targetHost);
/* 360 */     hash = LangUtils.hashCode(hash, this.localAddress);
/* 361 */     for (int i = 0; i < this.proxyChain.length; i++) {
/* 362 */       hash = LangUtils.hashCode(hash, this.proxyChain[i]);
/*     */     }
/* 364 */     hash = LangUtils.hashCode(hash, this.secure);
/* 365 */     hash = LangUtils.hashCode(hash, this.tunnelled);
/* 366 */     hash = LangUtils.hashCode(hash, this.layered);
/* 367 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 378 */     StringBuilder cab = new StringBuilder(50 + getHopCount() * 30);
/* 379 */     if (this.localAddress != null) {
/* 380 */       cab.append(this.localAddress);
/* 381 */       cab.append("->");
/*     */     } 
/* 383 */     cab.append('{');
/* 384 */     if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED)
/* 385 */       cab.append('t'); 
/* 386 */     if (this.layered == RouteInfo.LayerType.LAYERED)
/* 387 */       cab.append('l'); 
/* 388 */     if (this.secure)
/* 389 */       cab.append('s'); 
/* 390 */     cab.append("}->");
/* 391 */     for (HttpHost aProxyChain : this.proxyChain) {
/* 392 */       cab.append(aProxyChain);
/* 393 */       cab.append("->");
/*     */     } 
/* 395 */     cab.append(this.targetHost);
/* 396 */     return cab.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 403 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\routing\HttpRoute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */