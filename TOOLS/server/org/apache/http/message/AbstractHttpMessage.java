/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.params.BasicHttpParams;
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
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractHttpMessage
/*     */   implements HttpMessage
/*     */ {
/*     */   protected HeaderGroup headergroup;
/*     */   protected HttpParams params;
/*     */   
/*     */   protected AbstractHttpMessage(HttpParams params) {
/*  51 */     this.headergroup = new HeaderGroup();
/*  52 */     this.params = params;
/*     */   }
/*     */   
/*     */   protected AbstractHttpMessage() {
/*  56 */     this(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsHeader(String name) {
/*  61 */     return this.headergroup.containsHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header[] getHeaders(String name) {
/*  66 */     return this.headergroup.getHeaders(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getFirstHeader(String name) {
/*  71 */     return this.headergroup.getFirstHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getLastHeader(String name) {
/*  76 */     return this.headergroup.getLastHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header[] getAllHeaders() {
/*  81 */     return this.headergroup.getAllHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(Header header) {
/*  86 */     this.headergroup.addHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(String name, String value) {
/*  91 */     if (name == null) {
/*  92 */       throw new IllegalArgumentException("Header name may not be null");
/*     */     }
/*  94 */     this.headergroup.addHeader(new BasicHeader(name, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(Header header) {
/*  99 */     this.headergroup.updateHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(String name, String value) {
/* 104 */     if (name == null) {
/* 105 */       throw new IllegalArgumentException("Header name may not be null");
/*     */     }
/* 107 */     this.headergroup.updateHeader(new BasicHeader(name, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeaders(Header[] headers) {
/* 112 */     this.headergroup.setHeaders(headers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeHeader(Header header) {
/* 117 */     this.headergroup.removeHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeHeaders(String name) {
/* 122 */     if (name == null) {
/*     */       return;
/*     */     }
/* 125 */     for (HeaderIterator i = this.headergroup.iterator(); i.hasNext(); ) {
/* 126 */       Header header = i.nextHeader();
/* 127 */       if (name.equalsIgnoreCase(header.getName())) {
/* 128 */         i.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public HeaderIterator headerIterator() {
/* 135 */     return this.headergroup.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public HeaderIterator headerIterator(String name) {
/* 140 */     return this.headergroup.iterator(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpParams getParams() {
/* 145 */     if (this.params == null) {
/* 146 */       this.params = (HttpParams)new BasicHttpParams();
/*     */     }
/* 148 */     return this.params;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParams(HttpParams params) {
/* 153 */     if (params == null) {
/* 154 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/* 156 */     this.params = params;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\AbstractHttpMessage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */