/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.URL;
/*     */ import java.net.URLStreamHandlerFactory;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpHeaders;
/*     */ import org.fourthline.cling.model.message.UpnpMessage;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
/*     */ import org.fourthline.cling.transport.spi.StreamClient;
/*     */ import org.fourthline.cling.transport.spi.StreamClientConfiguration;
/*     */ import org.seamless.http.Headers;
/*     */ import org.seamless.util.Exceptions;
/*     */ import org.seamless.util.URIUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   implements StreamClient
/*     */ {
/*     */   static final String HACK_STREAM_HANDLER_SYSTEM_PROPERTY = "hackStreamHandlerProperty";
/*  72 */   private static final Logger log = Logger.getLogger(StreamClient.class.getName());
/*     */   
/*     */   protected final StreamClientConfigurationImpl configuration;
/*     */   
/*     */   public StreamClientImpl(StreamClientConfigurationImpl configuration) throws InitializationException {
/*  77 */     this.configuration = configuration;
/*     */     
/*  79 */     if (ModelUtil.ANDROID_EMULATOR || ModelUtil.ANDROID_RUNTIME)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  92 */       throw new InitializationException("This client does not work on Android. The design of HttpURLConnection is broken, we can not add additional 'permitted' HTTP methods. Read the Cling manual.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     log.fine("Using persistent HTTP stream client connections: " + configuration.isUsePersistentConnections());
/*  99 */     System.setProperty("http.keepAlive", Boolean.toString(configuration.isUsePersistentConnections()));
/*     */ 
/*     */     
/* 102 */     if (System.getProperty("hackStreamHandlerProperty") == null) {
/* 103 */       log.fine("Setting custom static URLStreamHandlerFactory to work around bad JDK defaults");
/*     */ 
/*     */       
/*     */       try {
/* 107 */         URL.setURLStreamHandlerFactory(
/* 108 */             (URLStreamHandlerFactory)Class.forName("org.fourthline.cling.transport.impl.FixedSunURLStreamHandler")
/*     */             
/* 110 */             .newInstance());
/*     */       }
/* 112 */       catch (Throwable t) {
/* 113 */         throw new InitializationException("Failed to set modified URLStreamHandlerFactory in this environment. Can't use bundled default client based on HTTPURLConnection, see manual.");
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 118 */       System.setProperty("hackStreamHandlerProperty", "alreadyWorkedAroundTheEvilJDK");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamClientConfigurationImpl getConfiguration() {
/* 124 */     return this.configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamResponseMessage sendRequest(StreamRequestMessage requestMessage) {
/* 130 */     UpnpRequest requestOperation = (UpnpRequest)requestMessage.getOperation();
/* 131 */     log.fine("Preparing HTTP request message with method '" + requestOperation.getHttpMethodName() + "': " + requestMessage);
/*     */     
/* 133 */     URL url = URIUtil.toURL(requestOperation.getURI());
/*     */     
/* 135 */     HttpURLConnection urlConnection = null;
/*     */ 
/*     */     
/*     */     try {
/* 139 */       urlConnection = (HttpURLConnection)url.openConnection();
/*     */       
/* 141 */       urlConnection.setRequestMethod(requestOperation.getHttpMethodName());
/*     */ 
/*     */       
/* 144 */       urlConnection.setReadTimeout(this.configuration.getTimeoutSeconds() * 1000);
/* 145 */       urlConnection.setConnectTimeout(this.configuration.getTimeoutSeconds() * 1000);
/*     */       
/* 147 */       applyRequestProperties(urlConnection, requestMessage);
/* 148 */       applyRequestBody(urlConnection, requestMessage);
/*     */       
/* 150 */       log.fine("Sending HTTP request: " + requestMessage);
/* 151 */       InputStream inputStream = urlConnection.getInputStream();
/* 152 */       return createResponse(urlConnection, inputStream);
/*     */     }
/* 154 */     catch (ProtocolException ex) {
/* 155 */       log.log(Level.WARNING, "HTTP request failed: " + requestMessage, Exceptions.unwrap(ex));
/* 156 */       return null;
/* 157 */     } catch (IOException ex) {
/*     */       
/* 159 */       if (urlConnection == null) {
/* 160 */         log.log(Level.WARNING, "HTTP request failed: " + requestMessage, Exceptions.unwrap(ex));
/* 161 */         return null;
/*     */       } 
/*     */       
/* 164 */       if (ex instanceof java.net.SocketTimeoutException) {
/* 165 */         log.info("Timeout of " + 
/* 166 */             getConfiguration().getTimeoutSeconds() + " seconds while waiting for HTTP request to complete, aborting: " + requestMessage);
/*     */ 
/*     */         
/* 169 */         return null;
/*     */       } 
/*     */       
/* 172 */       if (log.isLoggable(Level.FINE)) {
/* 173 */         log.fine("Exception occurred, trying to read the error stream: " + Exceptions.unwrap(ex));
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 182 */     catch (Exception ex) {
/* 183 */       log.log(Level.WARNING, "HTTP request failed: " + requestMessage, Exceptions.unwrap(ex));
/* 184 */       return null;
/*     */     }
/*     */     finally {
/*     */       
/* 188 */       if (urlConnection != null)
/*     */       {
/* 190 */         urlConnection.disconnect();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyRequestProperties(HttpURLConnection urlConnection, StreamRequestMessage requestMessage) {
/* 202 */     urlConnection.setInstanceFollowRedirects(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     if (!requestMessage.getHeaders().containsKey(UpnpHeader.Type.USER_AGENT)) {
/* 210 */       urlConnection.setRequestProperty(UpnpHeader.Type.USER_AGENT
/* 211 */           .getHttpName(), 
/* 212 */           getConfiguration().getUserAgentValue(requestMessage.getUdaMajorVersion(), requestMessage.getUdaMinorVersion()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 217 */     applyHeaders(urlConnection, (Headers)requestMessage.getHeaders());
/*     */   }
/*     */   
/*     */   protected void applyHeaders(HttpURLConnection urlConnection, Headers headers) {
/* 221 */     log.fine("Writing headers on HttpURLConnection: " + headers.size());
/* 222 */     for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)headers.entrySet()) {
/* 223 */       for (String v : entry.getValue()) {
/* 224 */         String headerName = entry.getKey();
/* 225 */         log.fine("Setting header '" + headerName + "': " + v);
/* 226 */         urlConnection.setRequestProperty(headerName, v);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyRequestBody(HttpURLConnection urlConnection, StreamRequestMessage requestMessage) throws IOException {
/* 233 */     if (requestMessage.hasBody()) {
/* 234 */       urlConnection.setDoOutput(true);
/*     */     } else {
/* 236 */       urlConnection.setDoOutput(false);
/*     */       
/*     */       return;
/*     */     } 
/* 240 */     if (requestMessage.getBodyType().equals(UpnpMessage.BodyType.STRING)) {
/* 241 */       IO.writeUTF8(urlConnection.getOutputStream(), requestMessage.getBodyString());
/* 242 */     } else if (requestMessage.getBodyType().equals(UpnpMessage.BodyType.BYTES)) {
/* 243 */       IO.writeBytes(urlConnection.getOutputStream(), requestMessage.getBodyBytes());
/*     */     } 
/* 245 */     urlConnection.getOutputStream().flush();
/*     */   }
/*     */ 
/*     */   
/*     */   protected StreamResponseMessage createResponse(HttpURLConnection urlConnection, InputStream inputStream) throws Exception {
/* 250 */     if (urlConnection.getResponseCode() == -1) {
/* 251 */       log.warning("Received an invalid HTTP response: " + urlConnection.getURL());
/* 252 */       log.warning("Is your Cling-based server sending connection heartbeats with RemoteClientInfo#isRequestCancelled? This client can't handle heartbeats, read the manual.");
/*     */ 
/*     */       
/* 255 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 259 */     UpnpResponse responseOperation = new UpnpResponse(urlConnection.getResponseCode(), urlConnection.getResponseMessage());
/*     */     
/* 261 */     log.fine("Received response: " + responseOperation);
/*     */ 
/*     */     
/* 264 */     StreamResponseMessage responseMessage = new StreamResponseMessage(responseOperation);
/*     */ 
/*     */     
/* 267 */     responseMessage.setHeaders(new UpnpHeaders(urlConnection.getHeaderFields()));
/*     */ 
/*     */     
/* 270 */     byte[] bodyBytes = null;
/* 271 */     InputStream is = null;
/*     */     try {
/* 273 */       is = inputStream;
/* 274 */       if (inputStream != null) bodyBytes = IO.readBytes(is); 
/*     */     } finally {
/* 276 */       if (is != null) {
/* 277 */         is.close();
/*     */       }
/*     */     } 
/* 280 */     if (bodyBytes != null && bodyBytes.length > 0 && responseMessage.isContentTypeMissingOrText()) {
/*     */       
/* 282 */       log.fine("Response contains textual entity body, converting then setting string on message");
/* 283 */       responseMessage.setBodyCharacters(bodyBytes);
/*     */     }
/* 285 */     else if (bodyBytes != null && bodyBytes.length > 0) {
/*     */       
/* 287 */       log.fine("Response contains binary entity body, setting bytes on message");
/* 288 */       responseMessage.setBody(UpnpMessage.BodyType.BYTES, bodyBytes);
/*     */     } else {
/*     */       
/* 291 */       log.fine("Response did not contain entity body");
/*     */     } 
/*     */     
/* 294 */     log.fine("Response message complete: " + responseMessage);
/* 295 */     return responseMessage;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\StreamClientImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */