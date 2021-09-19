/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class HttpRequestExecutor
/*     */ {
/*     */   protected boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
/*  87 */     if ("HEAD".equalsIgnoreCase(request.getRequestLine().getMethod())) {
/*  88 */       return false;
/*     */     }
/*  90 */     int status = response.getStatusLine().getStatusCode();
/*  91 */     return (status >= 200 && status != 204 && status != 304 && status != 205);
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
/*     */   public HttpResponse execute(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
/* 114 */     if (request == null) {
/* 115 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 117 */     if (conn == null) {
/* 118 */       throw new IllegalArgumentException("Client connection may not be null");
/*     */     }
/* 120 */     if (context == null) {
/* 121 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/*     */     try {
/* 125 */       HttpResponse response = doSendRequest(request, conn, context);
/* 126 */       if (response == null) {
/* 127 */         response = doReceiveResponse(request, conn, context);
/*     */       }
/* 129 */       return response;
/* 130 */     } catch (IOException ex) {
/* 131 */       closeConnection(conn);
/* 132 */       throw ex;
/* 133 */     } catch (HttpException ex) {
/* 134 */       closeConnection(conn);
/* 135 */       throw ex;
/* 136 */     } catch (RuntimeException ex) {
/* 137 */       closeConnection(conn);
/* 138 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final void closeConnection(HttpClientConnection conn) {
/*     */     try {
/* 144 */       conn.close();
/* 145 */     } catch (IOException ignore) {}
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
/*     */   public void preProcess(HttpRequest request, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
/* 166 */     if (request == null) {
/* 167 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 169 */     if (processor == null) {
/* 170 */       throw new IllegalArgumentException("HTTP processor may not be null");
/*     */     }
/* 172 */     if (context == null) {
/* 173 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/* 175 */     context.setAttribute("http.request", request);
/* 176 */     processor.process(request, context);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpResponse doSendRequest(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
/* 206 */     if (request == null) {
/* 207 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 209 */     if (conn == null) {
/* 210 */       throw new IllegalArgumentException("HTTP connection may not be null");
/*     */     }
/* 212 */     if (context == null) {
/* 213 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/* 216 */     HttpResponse response = null;
/*     */     
/* 218 */     context.setAttribute("http.connection", conn);
/* 219 */     context.setAttribute("http.request_sent", Boolean.FALSE);
/*     */     
/* 221 */     conn.sendRequestHeader(request);
/* 222 */     if (request instanceof HttpEntityEnclosingRequest) {
/*     */ 
/*     */ 
/*     */       
/* 226 */       boolean sendentity = true;
/* 227 */       ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/*     */       
/* 229 */       if (((HttpEntityEnclosingRequest)request).expectContinue() && !ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*     */ 
/*     */         
/* 232 */         conn.flush();
/*     */ 
/*     */         
/* 235 */         int tms = request.getParams().getIntParameter("http.protocol.wait-for-continue", 2000);
/*     */ 
/*     */         
/* 238 */         if (conn.isResponseAvailable(tms)) {
/* 239 */           response = conn.receiveResponseHeader();
/* 240 */           if (canResponseHaveBody(request, response)) {
/* 241 */             conn.receiveResponseEntity(response);
/*     */           }
/* 243 */           int status = response.getStatusLine().getStatusCode();
/* 244 */           if (status < 200) {
/* 245 */             if (status != 100) {
/* 246 */               throw new ProtocolException("Unexpected response: " + response.getStatusLine());
/*     */             }
/*     */ 
/*     */             
/* 250 */             response = null;
/*     */           } else {
/* 252 */             sendentity = false;
/*     */           } 
/*     */         } 
/*     */       } 
/* 256 */       if (sendentity) {
/* 257 */         conn.sendRequestEntity((HttpEntityEnclosingRequest)request);
/*     */       }
/*     */     } 
/* 260 */     conn.flush();
/* 261 */     context.setAttribute("http.request_sent", Boolean.TRUE);
/* 262 */     return response;
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
/*     */   protected HttpResponse doReceiveResponse(HttpRequest request, HttpClientConnection conn, HttpContext context) throws HttpException, IOException {
/* 285 */     if (request == null) {
/* 286 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 288 */     if (conn == null) {
/* 289 */       throw new IllegalArgumentException("HTTP connection may not be null");
/*     */     }
/* 291 */     if (context == null) {
/* 292 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/* 295 */     HttpResponse response = null;
/* 296 */     int statuscode = 0;
/*     */     
/* 298 */     while (response == null || statuscode < 200) {
/*     */       
/* 300 */       response = conn.receiveResponseHeader();
/* 301 */       if (canResponseHaveBody(request, response)) {
/* 302 */         conn.receiveResponseEntity(response);
/*     */       }
/* 304 */       statuscode = response.getStatusLine().getStatusCode();
/*     */     } 
/*     */ 
/*     */     
/* 308 */     return response;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcess(HttpResponse response, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
/* 335 */     if (response == null) {
/* 336 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/* 338 */     if (processor == null) {
/* 339 */       throw new IllegalArgumentException("HTTP processor may not be null");
/*     */     }
/* 341 */     if (context == null) {
/* 342 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/* 344 */     context.setAttribute("http.response", response);
/* 345 */     processor.process(response, context);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\protocol\HttpRequestExecutor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */