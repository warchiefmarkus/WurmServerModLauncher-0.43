/*     */ package org.fourthline.cling.transport.impl.jetty;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.eclipse.jetty.client.ContentExchange;
/*     */ import org.eclipse.jetty.client.HttpClient;
/*     */ import org.eclipse.jetty.client.HttpExchange;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.io.Buffer;
/*     */ import org.eclipse.jetty.io.ByteArrayBuffer;
/*     */ import org.eclipse.jetty.util.thread.ExecutorThreadPool;
/*     */ import org.eclipse.jetty.util.thread.ThreadPool;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpHeaders;
/*     */ import org.fourthline.cling.model.message.UpnpMessage;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.header.ContentTypeHeader;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.transport.spi.AbstractStreamClient;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
/*     */ import org.fourthline.cling.transport.spi.StreamClient;
/*     */ import org.fourthline.cling.transport.spi.StreamClientConfiguration;
/*     */ import org.seamless.util.Exceptions;
/*     */ import org.seamless.util.MimeType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamClientImpl
/*     */   extends AbstractStreamClient<StreamClientConfigurationImpl, StreamClientImpl.HttpContentExchange>
/*     */ {
/*  57 */   private static final Logger log = Logger.getLogger(StreamClient.class.getName());
/*     */   
/*     */   protected final StreamClientConfigurationImpl configuration;
/*     */   protected final HttpClient client;
/*     */   
/*     */   public StreamClientImpl(StreamClientConfigurationImpl configuration) throws InitializationException {
/*  63 */     this.configuration = configuration;
/*     */     
/*  65 */     log.info("Starting Jetty HttpClient...");
/*  66 */     this.client = new HttpClient();
/*     */ 
/*     */ 
/*     */     
/*  70 */     this.client.setThreadPool((ThreadPool)new ExecutorThreadPool(
/*  71 */           getConfiguration().getRequestExecutorService())
/*     */         {
/*     */           protected void doStop() throws Exception {}
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.client.setTimeout(((configuration.getTimeoutSeconds() + 5) * 1000));
/*  82 */     this.client.setConnectTimeout((configuration.getTimeoutSeconds() + 5) * 1000);
/*     */     
/*  84 */     this.client.setMaxRetries(configuration.getRequestRetryCount());
/*     */     
/*     */     try {
/*  87 */       this.client.start();
/*  88 */     } catch (Exception ex) {
/*  89 */       throw new InitializationException("Could not start Jetty HTTP client: " + ex, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamClientConfigurationImpl getConfiguration() {
/*  97 */     return this.configuration;
/*     */   }
/*     */ 
/*     */   
/*     */   protected HttpContentExchange createRequest(StreamRequestMessage requestMessage) {
/* 102 */     return new HttpContentExchange(getConfiguration(), this.client, requestMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Callable<StreamResponseMessage> createCallable(final StreamRequestMessage requestMessage, final HttpContentExchange exchange) {
/* 108 */     return new Callable<StreamResponseMessage>()
/*     */       {
/*     */         public StreamResponseMessage call() throws Exception {
/* 111 */           if (StreamClientImpl.log.isLoggable(Level.FINE)) {
/* 112 */             StreamClientImpl.log.fine("Sending HTTP request: " + requestMessage);
/*     */           }
/* 114 */           StreamClientImpl.this.client.send((HttpExchange)exchange);
/* 115 */           int exchangeState = exchange.waitForDone();
/*     */           
/* 117 */           if (exchangeState == 7)
/*     */             try {
/* 119 */               return exchange.createResponse();
/* 120 */             } catch (Throwable t) {
/* 121 */               StreamClientImpl.log.log(Level.WARNING, "Error reading response: " + requestMessage, Exceptions.unwrap(t));
/* 122 */               return null;
/*     */             }  
/* 124 */           if (exchangeState == 11)
/*     */           {
/* 126 */             return null; } 
/* 127 */           if (exchangeState == 9)
/*     */           {
/* 129 */             return null;
/*     */           }
/* 131 */           StreamClientImpl.log.warning("Unhandled HTTP exchange status: " + exchangeState);
/* 132 */           return null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void abort(HttpContentExchange exchange) {
/* 140 */     exchange.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean logExecutionException(Throwable t) {
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*     */     try {
/* 151 */       this.client.stop();
/* 152 */     } catch (Exception ex) {
/* 153 */       log.info("Error stopping HTTP client: " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static class HttpContentExchange
/*     */     extends ContentExchange
/*     */   {
/*     */     protected final StreamClientConfigurationImpl configuration;
/*     */     
/*     */     protected final HttpClient client;
/*     */     protected final StreamRequestMessage requestMessage;
/*     */     protected Throwable exception;
/*     */     
/*     */     public HttpContentExchange(StreamClientConfigurationImpl configuration, HttpClient client, StreamRequestMessage requestMessage) {
/* 168 */       super(true);
/* 169 */       this.configuration = configuration;
/* 170 */       this.client = client;
/* 171 */       this.requestMessage = requestMessage;
/* 172 */       applyRequestURLMethod();
/* 173 */       applyRequestHeaders();
/* 174 */       applyRequestBody();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onConnectionFailed(Throwable t) {
/* 179 */       StreamClientImpl.log.log(Level.WARNING, "HTTP connection failed: " + this.requestMessage, Exceptions.unwrap(t));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onException(Throwable t) {
/* 184 */       StreamClientImpl.log.log(Level.WARNING, "HTTP request failed: " + this.requestMessage, Exceptions.unwrap(t));
/*     */     }
/*     */     
/*     */     public StreamClientConfigurationImpl getConfiguration() {
/* 188 */       return this.configuration;
/*     */     }
/*     */     
/*     */     public StreamRequestMessage getRequestMessage() {
/* 192 */       return this.requestMessage;
/*     */     }
/*     */     
/*     */     protected void applyRequestURLMethod() {
/* 196 */       UpnpRequest requestOperation = (UpnpRequest)getRequestMessage().getOperation();
/* 197 */       if (StreamClientImpl.log.isLoggable(Level.FINE)) {
/* 198 */         StreamClientImpl.log.fine("Preparing HTTP request message with method '" + requestOperation
/*     */             
/* 200 */             .getHttpMethodName() + "': " + 
/* 201 */             getRequestMessage());
/*     */       }
/*     */       
/* 204 */       setURL(requestOperation.getURI().toString());
/* 205 */       setMethod(requestOperation.getHttpMethodName());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void applyRequestHeaders() {
/* 210 */       UpnpHeaders headers = getRequestMessage().getHeaders();
/* 211 */       if (StreamClientImpl.log.isLoggable(Level.FINE)) {
/* 212 */         StreamClientImpl.log.fine("Writing headers on HttpContentExchange: " + headers.size());
/*     */       }
/*     */ 
/*     */       
/* 216 */       if (!headers.containsKey(UpnpHeader.Type.USER_AGENT)) {
/* 217 */         setRequestHeader(UpnpHeader.Type.USER_AGENT
/* 218 */             .getHttpName(), 
/* 219 */             getConfiguration().getUserAgentValue(
/* 220 */               getRequestMessage().getUdaMajorVersion(), 
/* 221 */               getRequestMessage().getUdaMinorVersion()));
/*     */       }
/*     */       
/* 224 */       for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)headers.entrySet()) {
/* 225 */         for (String v : entry.getValue()) {
/* 226 */           String headerName = entry.getKey();
/* 227 */           if (StreamClientImpl.log.isLoggable(Level.FINE))
/* 228 */             StreamClientImpl.log.fine("Setting header '" + headerName + "': " + v); 
/* 229 */           addRequestHeader(headerName, v);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void applyRequestBody() {
/* 236 */       if (getRequestMessage().hasBody()) {
/* 237 */         if (getRequestMessage().getBodyType() == UpnpMessage.BodyType.STRING) {
/* 238 */           ByteArrayBuffer buffer; if (StreamClientImpl.log.isLoggable(Level.FINE)) {
/* 239 */             StreamClientImpl.log.fine("Writing textual request body: " + getRequestMessage());
/*     */           }
/*     */ 
/*     */           
/* 243 */           MimeType contentType = (getRequestMessage().getContentTypeHeader() != null) ? (MimeType)getRequestMessage().getContentTypeHeader().getValue() : ContentTypeHeader.DEFAULT_CONTENT_TYPE_UTF8;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 248 */           String charset = (getRequestMessage().getContentTypeCharset() != null) ? getRequestMessage().getContentTypeCharset() : "UTF-8";
/*     */ 
/*     */           
/* 251 */           setRequestContentType(contentType.toString());
/*     */           
/*     */           try {
/* 254 */             buffer = new ByteArrayBuffer(getRequestMessage().getBodyString(), charset);
/* 255 */           } catch (UnsupportedEncodingException ex) {
/* 256 */             throw new RuntimeException("Unsupported character encoding: " + charset, ex);
/*     */           } 
/* 258 */           setRequestHeader("Content-Length", String.valueOf(buffer.length()));
/* 259 */           setRequestContent((Buffer)buffer);
/*     */         } else {
/*     */           
/* 262 */           if (StreamClientImpl.log.isLoggable(Level.FINE)) {
/* 263 */             StreamClientImpl.log.fine("Writing binary request body: " + getRequestMessage());
/*     */           }
/* 265 */           if (getRequestMessage().getContentTypeHeader() == null) {
/* 266 */             throw new RuntimeException("Missing content type header in request message: " + this.requestMessage);
/*     */           }
/*     */           
/* 269 */           MimeType contentType = (MimeType)getRequestMessage().getContentTypeHeader().getValue();
/*     */           
/* 271 */           setRequestContentType(contentType.toString());
/*     */           
/* 273 */           ByteArrayBuffer buffer = new ByteArrayBuffer(getRequestMessage().getBodyBytes());
/* 274 */           setRequestHeader("Content-Length", String.valueOf(buffer.length()));
/* 275 */           setRequestContent((Buffer)buffer);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected StreamResponseMessage createResponse() {
/* 285 */       UpnpResponse responseOperation = new UpnpResponse(getResponseStatus(), UpnpResponse.Status.getByStatusCode(getResponseStatus()).getStatusMsg());
/*     */ 
/*     */       
/* 288 */       if (StreamClientImpl.log.isLoggable(Level.FINE)) {
/* 289 */         StreamClientImpl.log.fine("Received response: " + responseOperation);
/*     */       }
/* 291 */       StreamResponseMessage responseMessage = new StreamResponseMessage(responseOperation);
/*     */ 
/*     */       
/* 294 */       UpnpHeaders headers = new UpnpHeaders();
/* 295 */       HttpFields responseFields = getResponseFields();
/* 296 */       for (String name : responseFields.getFieldNamesCollection()) {
/* 297 */         for (String value : responseFields.getValuesCollection(name)) {
/* 298 */           headers.add(name, value);
/*     */         }
/*     */       } 
/* 301 */       responseMessage.setHeaders(headers);
/*     */ 
/*     */       
/* 304 */       byte[] bytes = getResponseContentBytes();
/* 305 */       if (bytes != null && bytes.length > 0 && responseMessage.isContentTypeMissingOrText()) {
/*     */         
/* 307 */         if (StreamClientImpl.log.isLoggable(Level.FINE))
/* 308 */           StreamClientImpl.log.fine("Response contains textual entity body, converting then setting string on message"); 
/*     */         try {
/* 310 */           responseMessage.setBodyCharacters(bytes);
/* 311 */         } catch (UnsupportedEncodingException ex) {
/* 312 */           throw new RuntimeException("Unsupported character encoding: " + ex, ex);
/*     */         }
/*     */       
/* 315 */       } else if (bytes != null && bytes.length > 0) {
/*     */         
/* 317 */         if (StreamClientImpl.log.isLoggable(Level.FINE))
/* 318 */           StreamClientImpl.log.fine("Response contains binary entity body, setting bytes on message"); 
/* 319 */         responseMessage.setBody(UpnpMessage.BodyType.BYTES, bytes);
/*     */       
/*     */       }
/* 322 */       else if (StreamClientImpl.log.isLoggable(Level.FINE)) {
/* 323 */         StreamClientImpl.log.fine("Response did not contain entity body");
/*     */       } 
/*     */       
/* 326 */       if (StreamClientImpl.log.isLoggable(Level.FINE))
/* 327 */         StreamClientImpl.log.fine("Response message complete: " + responseMessage); 
/* 328 */       return responseMessage;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\jetty\StreamClientImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */