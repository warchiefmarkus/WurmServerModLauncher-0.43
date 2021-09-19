/*     */ package org.seamless.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Representation<E>
/*     */   implements Serializable
/*     */ {
/*     */   private URL url;
/*     */   private CacheControl cacheControl;
/*     */   private Integer contentLength;
/*     */   private String contentType;
/*     */   private Long lastModified;
/*     */   private String entityTag;
/*     */   private E entity;
/*     */   
/*     */   public Representation(CacheControl cacheControl, Integer contentLength, String contentType, Long lastModified, String entityTag, E entity) {
/*  37 */     this(null, cacheControl, contentLength, contentType, lastModified, entityTag, entity);
/*     */   }
/*     */   
/*     */   public Representation(URL url, CacheControl cacheControl, Integer contentLength, String contentType, Long lastModified, String entityTag, E entity) {
/*  41 */     this.url = url;
/*  42 */     this.cacheControl = cacheControl;
/*  43 */     this.contentLength = contentLength;
/*  44 */     this.contentType = contentType;
/*  45 */     this.lastModified = lastModified;
/*  46 */     this.entityTag = entityTag;
/*  47 */     this.entity = entity;
/*     */   }
/*     */   
/*     */   public Representation(URLConnection urlConnection, E entity) {
/*  51 */     this(urlConnection.getURL(), CacheControl.valueOf(urlConnection.getHeaderField("Cache-Control")), Integer.valueOf(urlConnection.getContentLength()), urlConnection.getContentType(), Long.valueOf(urlConnection.getLastModified()), urlConnection.getHeaderField("Etag"), entity);
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
/*     */   public URL getUrl() {
/*  63 */     return this.url;
/*     */   }
/*     */   
/*     */   public CacheControl getCacheControl() {
/*  67 */     return this.cacheControl;
/*     */   }
/*     */   
/*     */   public Integer getContentLength() {
/*  71 */     return (this.contentLength == null || this.contentLength.intValue() == -1) ? null : this.contentLength;
/*     */   }
/*     */   
/*     */   public String getContentType() {
/*  75 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public Long getLastModified() {
/*  79 */     return (this.lastModified.longValue() == 0L) ? null : this.lastModified;
/*     */   }
/*     */   
/*     */   public String getEntityTag() {
/*  83 */     return this.entityTag;
/*     */   }
/*     */   
/*     */   public E getEntity() {
/*  87 */     return this.entity;
/*     */   }
/*     */   
/*     */   public Long getMaxAgeOrNull() {
/*  91 */     return (getCacheControl() == null || getCacheControl().getMaxAge() == -1 || getCacheControl().getMaxAge() == 0) ? null : Long.valueOf(getCacheControl().getMaxAge());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpired(long storedOn, long maxAge) {
/*  99 */     return (storedOn + maxAge * 1000L < (new Date()).getTime());
/*     */   }
/*     */   
/*     */   public boolean isExpired(long storedOn) {
/* 103 */     return (getMaxAgeOrNull() == null || isExpired(storedOn, getMaxAgeOrNull().longValue()));
/*     */   }
/*     */   
/*     */   public boolean isNoStore() {
/* 107 */     return (getCacheControl() != null && getCacheControl().isNoStore());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNoCache() {
/* 112 */     return (getCacheControl() != null && getCacheControl().isNoCache());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mustRevalidate() {
/* 117 */     return (getCacheControl() != null && getCacheControl().isProxyRevalidate());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEntityTagChanged(String currentEtag) {
/* 122 */     return (getEntityTag() != null && !getEntityTag().equals(currentEtag));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasBeenModified(long currentModificationTime) {
/* 127 */     return (getLastModified() == null || getLastModified().longValue() < currentModificationTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 132 */     return "(" + getClass().getSimpleName() + ") CT: " + getContentType();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\http\Representation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */