/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public abstract class HttpExchangeUpnpStream
/*     */   extends UpnpStream
/*     */ {
/*  48 */   private static Logger log = Logger.getLogger(UpnpStream.class.getName());
/*     */   
/*     */   private HttpExchange httpExchange;
/*     */   
/*     */   public HttpExchangeUpnpStream(ProtocolFactory protocolFactory, HttpExchange httpExchange) {
/*  53 */     super(protocolFactory);
/*  54 */     this.httpExchange = httpExchange;
/*     */   }
/*     */   
/*     */   public HttpExchange getHttpExchange() {
/*  58 */     return this.httpExchange;
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/*     */       byte[] bodyBytes;
/*  64 */       log.fine("Processing HTTP request: " + getHttpExchange().getRequestMethod() + " " + getHttpExchange().getRequestURI());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  70 */       StreamRequestMessage requestMessage = new StreamRequestMessage(UpnpRequest.Method.getByHttpName(getHttpExchange().getRequestMethod()), getHttpExchange().getRequestURI());
/*     */ 
/*     */       
/*  73 */       if (((UpnpRequest)requestMessage.getOperation()).getMethod().equals(UpnpRequest.Method.UNKNOWN)) {
/*  74 */         log.fine("Method not supported by UPnP stack: " + getHttpExchange().getRequestMethod());
/*  75 */         throw new RuntimeException("Method not supported: " + getHttpExchange().getRequestMethod());
/*     */       } 
/*     */ 
/*     */       
/*  79 */       ((UpnpRequest)requestMessage.getOperation()).setHttpMinorVersion(
/*  80 */           getHttpExchange().getProtocol().toUpperCase(Locale.ROOT).equals("HTTP/1.1") ? 1 : 0);
/*     */ 
/*     */       
/*  83 */       log.fine("Created new request message: " + requestMessage);
/*     */ 
/*     */       
/*  86 */       requestMessage.setConnection(createConnection());
/*     */ 
/*     */       
/*  89 */       requestMessage.setHeaders(new UpnpHeaders(getHttpExchange().getRequestHeaders()));
/*     */ 
/*     */ 
/*     */       
/*  93 */       InputStream is = null;
/*     */       try {
/*  95 */         is = getHttpExchange().getRequestBody();
/*  96 */         bodyBytes = IO.readBytes(is);
/*     */       } finally {
/*  98 */         if (is != null) {
/*  99 */           is.close();
/*     */         }
/*     */       } 
/* 102 */       log.fine("Reading request body bytes: " + bodyBytes.length);
/*     */       
/* 104 */       if (bodyBytes.length > 0 && requestMessage.isContentTypeMissingOrText()) {
/*     */         
/* 106 */         log.fine("Request contains textual entity body, converting then setting string on message");
/* 107 */         requestMessage.setBodyCharacters(bodyBytes);
/*     */       }
/* 109 */       else if (bodyBytes.length > 0) {
/*     */         
/* 111 */         log.fine("Request contains binary entity body, setting bytes on message");
/* 112 */         requestMessage.setBody(UpnpMessage.BodyType.BYTES, bodyBytes);
/*     */       } else {
/*     */         
/* 115 */         log.fine("Request did not contain entity body");
/*     */       } 
/*     */ 
/*     */       
/* 119 */       StreamResponseMessage responseMessage = process(requestMessage);
/*     */ 
/*     */       
/* 122 */       if (responseMessage != null) {
/* 123 */         log.fine("Preparing HTTP response message: " + responseMessage);
/*     */ 
/*     */         
/* 126 */         getHttpExchange().getResponseHeaders().putAll((Map<? extends String, ? extends List<String>>)responseMessage
/* 127 */             .getHeaders());
/*     */ 
/*     */ 
/*     */         
/* 131 */         byte[] responseBodyBytes = responseMessage.hasBody() ? responseMessage.getBodyBytes() : null;
/* 132 */         int contentLength = (responseBodyBytes != null) ? responseBodyBytes.length : -1;
/*     */         
/* 134 */         log.fine("Sending HTTP response message: " + responseMessage + " with content length: " + contentLength);
/* 135 */         getHttpExchange().sendResponseHeaders(((UpnpResponse)responseMessage.getOperation()).getStatusCode(), contentLength);
/*     */         
/* 137 */         if (contentLength > 0) {
/* 138 */           log.fine("Response message has body, writing bytes to stream...");
/* 139 */           OutputStream os = null;
/*     */           try {
/* 141 */             os = getHttpExchange().getResponseBody();
/* 142 */             IO.writeBytes(os, responseBodyBytes);
/* 143 */             os.flush();
/*     */           } finally {
/* 145 */             if (os != null) {
/* 146 */               os.close();
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 152 */         log.fine("Sending HTTP response status: 404");
/* 153 */         getHttpExchange().sendResponseHeaders(404, -1L);
/*     */       } 
/*     */       
/* 156 */       responseSent(responseMessage);
/*     */     }
/* 158 */     catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 169 */       log.fine("Exception occured during UPnP stream processing: " + t);
/* 170 */       if (log.isLoggable(Level.FINE)) {
/* 171 */         log.log(Level.FINE, "Cause: " + Exceptions.unwrap(t), Exceptions.unwrap(t));
/*     */       }
/*     */       try {
/* 174 */         this.httpExchange.sendResponseHeaders(500, -1L);
/* 175 */       } catch (IOException ex) {
/* 176 */         log.warning("Couldn't send error response: " + ex);
/*     */       } 
/*     */       
/* 179 */       responseException(t);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract Connection createConnection();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\HttpExchangeUpnpStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */