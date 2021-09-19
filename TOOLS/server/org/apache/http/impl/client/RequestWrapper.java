/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.message.AbstractHttpMessage;
/*     */ import org.apache.http.message.BasicRequestLine;
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
/*     */ @NotThreadSafe
/*     */ public class RequestWrapper
/*     */   extends AbstractHttpMessage
/*     */   implements HttpUriRequest
/*     */ {
/*     */   private final HttpRequest original;
/*     */   private URI uri;
/*     */   private String method;
/*     */   private ProtocolVersion version;
/*     */   private int execCount;
/*     */   
/*     */   public RequestWrapper(HttpRequest request) throws ProtocolException {
/*  67 */     if (request == null) {
/*  68 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/*  70 */     this.original = request;
/*  71 */     setParams(request.getParams());
/*  72 */     setHeaders(request.getAllHeaders());
/*     */     
/*  74 */     if (request instanceof HttpUriRequest) {
/*  75 */       this.uri = ((HttpUriRequest)request).getURI();
/*  76 */       this.method = ((HttpUriRequest)request).getMethod();
/*  77 */       this.version = null;
/*     */     } else {
/*  79 */       RequestLine requestLine = request.getRequestLine();
/*     */       try {
/*  81 */         this.uri = new URI(requestLine.getUri());
/*  82 */       } catch (URISyntaxException ex) {
/*  83 */         throw new ProtocolException("Invalid request URI: " + requestLine.getUri(), ex);
/*     */       } 
/*     */       
/*  86 */       this.method = requestLine.getMethod();
/*  87 */       this.version = request.getProtocolVersion();
/*     */     } 
/*  89 */     this.execCount = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetHeaders() {
/*  94 */     this.headergroup.clear();
/*  95 */     setHeaders(this.original.getAllHeaders());
/*     */   }
/*     */   
/*     */   public String getMethod() {
/*  99 */     return this.method;
/*     */   }
/*     */   
/*     */   public void setMethod(String method) {
/* 103 */     if (method == null) {
/* 104 */       throw new IllegalArgumentException("Method name may not be null");
/*     */     }
/* 106 */     this.method = method;
/*     */   }
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 110 */     if (this.version == null) {
/* 111 */       this.version = HttpProtocolParams.getVersion(getParams());
/*     */     }
/* 113 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setProtocolVersion(ProtocolVersion version) {
/* 117 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/* 122 */     return this.uri;
/*     */   }
/*     */   
/*     */   public void setURI(URI uri) {
/* 126 */     this.uri = uri;
/*     */   }
/*     */   
/*     */   public RequestLine getRequestLine() {
/* 130 */     String method = getMethod();
/* 131 */     ProtocolVersion ver = getProtocolVersion();
/* 132 */     String uritext = null;
/* 133 */     if (this.uri != null) {
/* 134 */       uritext = this.uri.toASCIIString();
/*     */     }
/* 136 */     if (uritext == null || uritext.length() == 0) {
/* 137 */       uritext = "/";
/*     */     }
/* 139 */     return (RequestLine)new BasicRequestLine(method, uritext, ver);
/*     */   }
/*     */   
/*     */   public void abort() throws UnsupportedOperationException {
/* 143 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isAborted() {
/* 147 */     return false;
/*     */   }
/*     */   
/*     */   public HttpRequest getOriginal() {
/* 151 */     return this.original;
/*     */   }
/*     */   
/*     */   public boolean isRepeatable() {
/* 155 */     return true;
/*     */   }
/*     */   
/*     */   public int getExecCount() {
/* 159 */     return this.execCount;
/*     */   }
/*     */   
/*     */   public void incrementExecCount() {
/* 163 */     this.execCount++;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\RequestWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */