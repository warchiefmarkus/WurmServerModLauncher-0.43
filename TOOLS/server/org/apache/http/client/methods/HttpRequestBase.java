/*     */ package org.apache.http.client.methods;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.client.utils.CloneUtils;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ConnectionReleaseTrigger;
/*     */ import org.apache.http.message.AbstractHttpMessage;
/*     */ import org.apache.http.message.BasicRequestLine;
/*     */ import org.apache.http.message.HeaderGroup;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class HttpRequestBase
/*     */   extends AbstractHttpMessage
/*     */   implements HttpUriRequest, AbortableHttpRequest, Cloneable
/*     */ {
/*  67 */   private Lock abortLock = new ReentrantLock();
/*     */   
/*     */   private volatile boolean aborted;
/*     */   private URI uri;
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/*  73 */     return HttpProtocolParams.getVersion(getParams());
/*     */   }
/*     */ 
/*     */   
/*     */   private ClientConnectionRequest connRequest;
/*     */   private ConnectionReleaseTrigger releaseTrigger;
/*     */   
/*     */   public abstract String getMethod();
/*     */   
/*     */   public URI getURI() {
/*  83 */     return this.uri;
/*     */   }
/*     */   
/*     */   public RequestLine getRequestLine() {
/*  87 */     String method = getMethod();
/*  88 */     ProtocolVersion ver = getProtocolVersion();
/*  89 */     URI uri = getURI();
/*  90 */     String uritext = null;
/*  91 */     if (uri != null) {
/*  92 */       uritext = uri.toASCIIString();
/*     */     }
/*  94 */     if (uritext == null || uritext.length() == 0) {
/*  95 */       uritext = "/";
/*     */     }
/*  97 */     return (RequestLine)new BasicRequestLine(method, uritext, ver);
/*     */   }
/*     */   
/*     */   public void setURI(URI uri) {
/* 101 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConnectionRequest(ClientConnectionRequest connRequest) throws IOException {
/* 106 */     if (this.aborted) {
/* 107 */       throw new IOException("Request already aborted");
/*     */     }
/* 109 */     this.abortLock.lock();
/*     */     try {
/* 111 */       this.connRequest = connRequest;
/*     */     } finally {
/* 113 */       this.abortLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReleaseTrigger(ConnectionReleaseTrigger releaseTrigger) throws IOException {
/* 119 */     if (this.aborted) {
/* 120 */       throw new IOException("Request already aborted");
/*     */     }
/* 122 */     this.abortLock.lock();
/*     */     try {
/* 124 */       this.releaseTrigger = releaseTrigger;
/*     */     } finally {
/* 126 */       this.abortLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cleanup() {
/* 131 */     if (this.connRequest != null) {
/* 132 */       this.connRequest.abortRequest();
/* 133 */       this.connRequest = null;
/*     */     } 
/* 135 */     if (this.releaseTrigger != null) {
/*     */       try {
/* 137 */         this.releaseTrigger.abortConnection();
/* 138 */       } catch (IOException ex) {}
/*     */       
/* 140 */       this.releaseTrigger = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void abort() {
/* 145 */     if (this.aborted) {
/*     */       return;
/*     */     }
/* 148 */     this.abortLock.lock();
/*     */     try {
/* 150 */       this.aborted = true;
/* 151 */       cleanup();
/*     */     } finally {
/* 153 */       this.abortLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isAborted() {
/* 158 */     return this.aborted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 167 */     this.abortLock.lock();
/*     */     try {
/* 169 */       cleanup();
/* 170 */       this.aborted = false;
/*     */     } finally {
/* 172 */       this.abortLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection() {
/* 183 */     reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 188 */     HttpRequestBase clone = (HttpRequestBase)super.clone();
/* 189 */     clone.abortLock = new ReentrantLock();
/* 190 */     clone.aborted = false;
/* 191 */     clone.releaseTrigger = null;
/* 192 */     clone.connRequest = null;
/* 193 */     clone.headergroup = (HeaderGroup)CloneUtils.clone(this.headergroup);
/* 194 */     clone.params = (HttpParams)CloneUtils.clone(this.params);
/* 195 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 200 */     return getMethod() + " " + getURI() + " " + getProtocolVersion();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\methods\HttpRequestBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */