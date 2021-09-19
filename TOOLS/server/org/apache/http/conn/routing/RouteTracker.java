/*     */ package org.apache.http.conn.routing;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ @NotThreadSafe
/*     */ public final class RouteTracker
/*     */   implements RouteInfo, Cloneable
/*     */ {
/*     */   private final HttpHost targetHost;
/*     */   private final InetAddress localAddress;
/*     */   private boolean connected;
/*     */   private HttpHost[] proxyChain;
/*     */   private RouteInfo.TunnelType tunnelled;
/*     */   private RouteInfo.LayerType layered;
/*     */   private boolean secure;
/*     */   
/*     */   public RouteTracker(HttpHost target, InetAddress local) {
/*  81 */     if (target == null) {
/*  82 */       throw new IllegalArgumentException("Target host may not be null.");
/*     */     }
/*  84 */     this.targetHost = target;
/*  85 */     this.localAddress = local;
/*  86 */     this.tunnelled = RouteInfo.TunnelType.PLAIN;
/*  87 */     this.layered = RouteInfo.LayerType.PLAIN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  94 */     this.connected = false;
/*  95 */     this.proxyChain = null;
/*  96 */     this.tunnelled = RouteInfo.TunnelType.PLAIN;
/*  97 */     this.layered = RouteInfo.LayerType.PLAIN;
/*  98 */     this.secure = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RouteTracker(HttpRoute route) {
/* 109 */     this(route.getTargetHost(), route.getLocalAddress());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void connectTarget(boolean secure) {
/* 119 */     if (this.connected) {
/* 120 */       throw new IllegalStateException("Already connected.");
/*     */     }
/* 122 */     this.connected = true;
/* 123 */     this.secure = secure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void connectProxy(HttpHost proxy, boolean secure) {
/* 134 */     if (proxy == null) {
/* 135 */       throw new IllegalArgumentException("Proxy host may not be null.");
/*     */     }
/* 137 */     if (this.connected) {
/* 138 */       throw new IllegalStateException("Already connected.");
/*     */     }
/* 140 */     this.connected = true;
/* 141 */     this.proxyChain = new HttpHost[] { proxy };
/* 142 */     this.secure = secure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void tunnelTarget(boolean secure) {
/* 152 */     if (!this.connected) {
/* 153 */       throw new IllegalStateException("No tunnel unless connected.");
/*     */     }
/* 155 */     if (this.proxyChain == null) {
/* 156 */       throw new IllegalStateException("No tunnel without proxy.");
/*     */     }
/* 158 */     this.tunnelled = RouteInfo.TunnelType.TUNNELLED;
/* 159 */     this.secure = secure;
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
/*     */   public final void tunnelProxy(HttpHost proxy, boolean secure) {
/* 172 */     if (proxy == null) {
/* 173 */       throw new IllegalArgumentException("Proxy host may not be null.");
/*     */     }
/* 175 */     if (!this.connected) {
/* 176 */       throw new IllegalStateException("No tunnel unless connected.");
/*     */     }
/* 178 */     if (this.proxyChain == null) {
/* 179 */       throw new IllegalStateException("No proxy tunnel without proxy.");
/*     */     }
/*     */ 
/*     */     
/* 183 */     HttpHost[] proxies = new HttpHost[this.proxyChain.length + 1];
/* 184 */     System.arraycopy(this.proxyChain, 0, proxies, 0, this.proxyChain.length);
/*     */     
/* 186 */     proxies[proxies.length - 1] = proxy;
/*     */     
/* 188 */     this.proxyChain = proxies;
/* 189 */     this.secure = secure;
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
/*     */   public final void layerProtocol(boolean secure) {
/* 201 */     if (!this.connected) {
/* 202 */       throw new IllegalStateException("No layered protocol unless connected.");
/*     */     }
/*     */     
/* 205 */     this.layered = RouteInfo.LayerType.LAYERED;
/* 206 */     this.secure = secure;
/*     */   }
/*     */   
/*     */   public final HttpHost getTargetHost() {
/* 210 */     return this.targetHost;
/*     */   }
/*     */   
/*     */   public final InetAddress getLocalAddress() {
/* 214 */     return this.localAddress;
/*     */   }
/*     */   
/*     */   public final int getHopCount() {
/* 218 */     int hops = 0;
/* 219 */     if (this.connected)
/* 220 */       if (this.proxyChain == null) {
/* 221 */         hops = 1;
/*     */       } else {
/* 223 */         hops = this.proxyChain.length + 1;
/*     */       }  
/* 225 */     return hops;
/*     */   }
/*     */   
/*     */   public final HttpHost getHopTarget(int hop) {
/* 229 */     if (hop < 0) {
/* 230 */       throw new IllegalArgumentException("Hop index must not be negative: " + hop);
/*     */     }
/* 232 */     int hopcount = getHopCount();
/* 233 */     if (hop >= hopcount) {
/* 234 */       throw new IllegalArgumentException("Hop index " + hop + " exceeds tracked route length " + hopcount + ".");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 239 */     HttpHost result = null;
/* 240 */     if (hop < hopcount - 1) {
/* 241 */       result = this.proxyChain[hop];
/*     */     } else {
/* 243 */       result = this.targetHost;
/*     */     } 
/* 245 */     return result;
/*     */   }
/*     */   
/*     */   public final HttpHost getProxyHost() {
/* 249 */     return (this.proxyChain == null) ? null : this.proxyChain[0];
/*     */   }
/*     */   
/*     */   public final boolean isConnected() {
/* 253 */     return this.connected;
/*     */   }
/*     */   
/*     */   public final RouteInfo.TunnelType getTunnelType() {
/* 257 */     return this.tunnelled;
/*     */   }
/*     */   
/*     */   public final boolean isTunnelled() {
/* 261 */     return (this.tunnelled == RouteInfo.TunnelType.TUNNELLED);
/*     */   }
/*     */   
/*     */   public final RouteInfo.LayerType getLayerType() {
/* 265 */     return this.layered;
/*     */   }
/*     */   
/*     */   public final boolean isLayered() {
/* 269 */     return (this.layered == RouteInfo.LayerType.LAYERED);
/*     */   }
/*     */   
/*     */   public final boolean isSecure() {
/* 273 */     return this.secure;
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
/*     */   public final HttpRoute toRoute() {
/* 285 */     return !this.connected ? null : new HttpRoute(this.targetHost, this.localAddress, this.proxyChain, this.secure, this.tunnelled, this.layered);
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
/*     */   public final boolean equals(Object o) {
/* 301 */     if (o == this)
/* 302 */       return true; 
/* 303 */     if (!(o instanceof RouteTracker)) {
/* 304 */       return false;
/*     */     }
/* 306 */     RouteTracker that = (RouteTracker)o;
/* 307 */     return (this.connected == that.connected && this.secure == that.secure && this.tunnelled == that.tunnelled && this.layered == that.layered && LangUtils.equals(this.targetHost, that.targetHost) && LangUtils.equals(this.localAddress, that.localAddress) && LangUtils.equals((Object[])this.proxyChain, (Object[])that.proxyChain));
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
/*     */   public final int hashCode() {
/* 328 */     int hash = 17;
/* 329 */     hash = LangUtils.hashCode(hash, this.targetHost);
/* 330 */     hash = LangUtils.hashCode(hash, this.localAddress);
/* 331 */     if (this.proxyChain != null) {
/* 332 */       for (int i = 0; i < this.proxyChain.length; i++) {
/* 333 */         hash = LangUtils.hashCode(hash, this.proxyChain[i]);
/*     */       }
/*     */     }
/* 336 */     hash = LangUtils.hashCode(hash, this.connected);
/* 337 */     hash = LangUtils.hashCode(hash, this.secure);
/* 338 */     hash = LangUtils.hashCode(hash, this.tunnelled);
/* 339 */     hash = LangUtils.hashCode(hash, this.layered);
/* 340 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 350 */     StringBuilder cab = new StringBuilder(50 + getHopCount() * 30);
/*     */     
/* 352 */     cab.append("RouteTracker[");
/* 353 */     if (this.localAddress != null) {
/* 354 */       cab.append(this.localAddress);
/* 355 */       cab.append("->");
/*     */     } 
/* 357 */     cab.append('{');
/* 358 */     if (this.connected)
/* 359 */       cab.append('c'); 
/* 360 */     if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED)
/* 361 */       cab.append('t'); 
/* 362 */     if (this.layered == RouteInfo.LayerType.LAYERED)
/* 363 */       cab.append('l'); 
/* 364 */     if (this.secure)
/* 365 */       cab.append('s'); 
/* 366 */     cab.append("}->");
/* 367 */     if (this.proxyChain != null) {
/* 368 */       for (int i = 0; i < this.proxyChain.length; i++) {
/* 369 */         cab.append(this.proxyChain[i]);
/* 370 */         cab.append("->");
/*     */       } 
/*     */     }
/* 373 */     cab.append(this.targetHost);
/* 374 */     cab.append(']');
/*     */     
/* 376 */     return cab.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 383 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\routing\RouteTracker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */