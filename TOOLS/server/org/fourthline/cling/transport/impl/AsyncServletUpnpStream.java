/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.fourthline.cling.model.message.Connection;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpHeaders;
/*     */ import org.fourthline.cling.model.message.UpnpMessage;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.transport.spi.UpnpStream;
/*     */ import org.seamless.util.Exceptions;
/*     */ import org.seamless.util.io.IO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AsyncServletUpnpStream
/*     */   extends UpnpStream
/*     */   implements AsyncListener
/*     */ {
/*  56 */   private static final Logger log = Logger.getLogger(UpnpStream.class.getName());
/*     */   
/*     */   protected final AsyncContext asyncContext;
/*     */   
/*     */   protected final HttpServletRequest request;
/*     */   
/*     */   protected StreamResponseMessage responseMessage;
/*     */ 
/*     */   
/*     */   public AsyncServletUpnpStream(ProtocolFactory protocolFactory, AsyncContext asyncContext, HttpServletRequest request) {
/*  66 */     super(protocolFactory);
/*  67 */     this.asyncContext = asyncContext;
/*  68 */     this.request = request;
/*  69 */     asyncContext.addListener(this);
/*     */   }
/*     */   
/*     */   protected HttpServletRequest getRequest() {
/*  73 */     return this.request;
/*     */   }
/*     */   
/*     */   protected HttpServletResponse getResponse() {
/*     */     ServletResponse response;
/*  78 */     if ((response = this.asyncContext.getResponse()) == null) {
/*  79 */       throw new IllegalStateException("Couldn't get response from asynchronous context, already timed out");
/*     */     }
/*     */ 
/*     */     
/*  83 */     return (HttpServletResponse)response;
/*     */   }
/*     */   
/*     */   protected void complete() {
/*     */     try {
/*  88 */       this.asyncContext.complete();
/*  89 */     } catch (IllegalStateException ex) {
/*     */ 
/*     */       
/*  92 */       log.info("Error calling servlet container's AsyncContext#complete() method: " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  99 */       StreamRequestMessage requestMessage = readRequestMessage();
/* 100 */       if (log.isLoggable(Level.FINER)) {
/* 101 */         log.finer("Processing new request message: " + requestMessage);
/*     */       }
/* 103 */       this.responseMessage = process(requestMessage);
/*     */       
/* 105 */       if (this.responseMessage != null) {
/* 106 */         if (log.isLoggable(Level.FINER))
/* 107 */           log.finer("Preparing HTTP response message: " + this.responseMessage); 
/* 108 */         writeResponseMessage(this.responseMessage);
/*     */       } else {
/*     */         
/* 111 */         if (log.isLoggable(Level.FINER))
/* 112 */           log.finer("Sending HTTP response status: 404"); 
/* 113 */         getResponse().setStatus(404);
/*     */       }
/*     */     
/* 116 */     } catch (Throwable t) {
/* 117 */       log.info("Exception occurred during UPnP stream processing: " + t);
/* 118 */       if (log.isLoggable(Level.FINER)) {
/* 119 */         log.log(Level.FINER, "Cause: " + Exceptions.unwrap(t), Exceptions.unwrap(t));
/*     */       }
/* 121 */       if (!getResponse().isCommitted()) {
/* 122 */         log.finer("Response hasn't been committed, returning INTERNAL SERVER ERROR to client");
/* 123 */         getResponse().setStatus(500);
/*     */       } else {
/* 125 */         log.info("Could not return INTERNAL SERVER ERROR to client, response was already committed");
/*     */       } 
/* 127 */       responseException(t);
/*     */     } finally {
/* 129 */       complete();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onStartAsync(AsyncEvent event) throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onComplete(AsyncEvent event) throws IOException {
/* 141 */     if (log.isLoggable(Level.FINER))
/* 142 */       log.finer("Completed asynchronous processing of HTTP request: " + event.getSuppliedRequest()); 
/* 143 */     responseSent(this.responseMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTimeout(AsyncEvent event) throws IOException {
/* 148 */     if (log.isLoggable(Level.FINER))
/* 149 */       log.finer("Asynchronous processing of HTTP request timed out: " + event.getSuppliedRequest()); 
/* 150 */     responseException(new Exception("Asynchronous request timed out"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(AsyncEvent event) throws IOException {
/* 155 */     if (log.isLoggable(Level.FINER))
/* 156 */       log.finer("Asynchronous processing of HTTP request error: " + event.getThrowable()); 
/* 157 */     responseException(event.getThrowable());
/*     */   } protected StreamRequestMessage readRequestMessage() throws IOException {
/*     */     StreamRequestMessage requestMessage;
/*     */     byte[] bodyBytes;
/*     */     ServletInputStream servletInputStream;
/* 162 */     String requestMethod = getRequest().getMethod();
/* 163 */     String requestURI = getRequest().getRequestURI();
/*     */     
/* 165 */     if (log.isLoggable(Level.FINER)) {
/* 166 */       log.finer("Processing HTTP request: " + requestMethod + " " + requestURI);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 173 */       requestMessage = new StreamRequestMessage(UpnpRequest.Method.getByHttpName(requestMethod), URI.create(requestURI));
/*     */     }
/* 175 */     catch (IllegalArgumentException ex) {
/* 176 */       throw new RuntimeException("Invalid request URI: " + requestURI, ex);
/*     */     } 
/*     */     
/* 179 */     if (((UpnpRequest)requestMessage.getOperation()).getMethod().equals(UpnpRequest.Method.UNKNOWN)) {
/* 180 */       throw new RuntimeException("Method not supported: " + requestMethod);
/*     */     }
/*     */ 
/*     */     
/* 184 */     requestMessage.setConnection(createConnection());
/*     */ 
/*     */     
/* 187 */     UpnpHeaders headers = new UpnpHeaders();
/* 188 */     Enumeration<String> headerNames = getRequest().getHeaderNames();
/* 189 */     while (headerNames.hasMoreElements()) {
/* 190 */       String headerName = headerNames.nextElement();
/* 191 */       Enumeration<String> headerValues = getRequest().getHeaders(headerName);
/* 192 */       while (headerValues.hasMoreElements()) {
/* 193 */         String headerValue = headerValues.nextElement();
/* 194 */         headers.add(headerName, headerValue);
/*     */       } 
/*     */     } 
/* 197 */     requestMessage.setHeaders(headers);
/*     */ 
/*     */ 
/*     */     
/* 201 */     InputStream is = null;
/*     */     try {
/* 203 */       servletInputStream = getRequest().getInputStream();
/* 204 */       bodyBytes = IO.readBytes((InputStream)servletInputStream);
/*     */     } finally {
/* 206 */       if (servletInputStream != null)
/* 207 */         servletInputStream.close(); 
/*     */     } 
/* 209 */     if (log.isLoggable(Level.FINER)) {
/* 210 */       log.finer("Reading request body bytes: " + bodyBytes.length);
/*     */     }
/* 212 */     if (bodyBytes.length > 0 && requestMessage.isContentTypeMissingOrText()) {
/*     */       
/* 214 */       if (log.isLoggable(Level.FINER))
/* 215 */         log.finer("Request contains textual entity body, converting then setting string on message"); 
/* 216 */       requestMessage.setBodyCharacters(bodyBytes);
/*     */     }
/* 218 */     else if (bodyBytes.length > 0) {
/*     */       
/* 220 */       if (log.isLoggable(Level.FINER))
/* 221 */         log.finer("Request contains binary entity body, setting bytes on message"); 
/* 222 */       requestMessage.setBody(UpnpMessage.BodyType.BYTES, bodyBytes);
/*     */     
/*     */     }
/* 225 */     else if (log.isLoggable(Level.FINER)) {
/* 226 */       log.finer("Request did not contain entity body");
/*     */     } 
/*     */     
/* 229 */     return requestMessage;
/*     */   }
/*     */   
/*     */   protected void writeResponseMessage(StreamResponseMessage responseMessage) throws IOException {
/* 233 */     if (log.isLoggable(Level.FINER)) {
/* 234 */       log.finer("Sending HTTP response status: " + ((UpnpResponse)responseMessage.getOperation()).getStatusCode());
/*     */     }
/* 236 */     getResponse().setStatus(((UpnpResponse)responseMessage.getOperation()).getStatusCode());
/*     */ 
/*     */     
/* 239 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)responseMessage.getHeaders().entrySet()) {
/* 240 */       for (String value : entry.getValue()) {
/* 241 */         getResponse().addHeader(entry.getKey(), value);
/*     */       }
/*     */     } 
/*     */     
/* 245 */     getResponse().setDateHeader("Date", System.currentTimeMillis());
/*     */ 
/*     */     
/* 248 */     byte[] responseBodyBytes = responseMessage.hasBody() ? responseMessage.getBodyBytes() : null;
/* 249 */     int contentLength = (responseBodyBytes != null) ? responseBodyBytes.length : -1;
/*     */     
/* 251 */     if (contentLength > 0) {
/* 252 */       getResponse().setContentLength(contentLength);
/* 253 */       log.finer("Response message has body, writing bytes to stream...");
/* 254 */       IO.writeBytes((OutputStream)getResponse().getOutputStream(), responseBodyBytes);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract Connection createConnection();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\AsyncServletUpnpStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */