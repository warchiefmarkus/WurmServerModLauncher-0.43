/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.ProtocolException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.net.URLStreamHandlerFactory;
/*     */ import java.util.logging.Logger;
/*     */ import sun.net.www.protocol.http.Handler;
/*     */ import sun.net.www.protocol.http.HttpURLConnection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedSunURLStreamHandler
/*     */   implements URLStreamHandlerFactory
/*     */ {
/*  43 */   private static final Logger log = Logger.getLogger(FixedSunURLStreamHandler.class.getName());
/*     */   
/*     */   public URLStreamHandler createURLStreamHandler(String protocol) {
/*  46 */     log.fine("Creating new URLStreamHandler for protocol: " + protocol);
/*  47 */     if ("http".equals(protocol)) {
/*  48 */       return new Handler()
/*     */         {
/*     */           protected URLConnection openConnection(URL u) throws IOException {
/*  51 */             return openConnection(u, null);
/*     */           }
/*     */           
/*     */           protected URLConnection openConnection(URL u, Proxy p) throws IOException {
/*  55 */             return new FixedSunURLStreamHandler.UpnpURLConnection(u, this);
/*     */           }
/*     */         };
/*     */     }
/*  59 */     return null;
/*     */   }
/*     */   
/*     */   static class UpnpURLConnection
/*     */     extends HttpURLConnection
/*     */   {
/*  65 */     private static final String[] methods = new String[] { "GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "SUBSCRIBE", "UNSUBSCRIBE", "NOTIFY" };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected UpnpURLConnection(URL u, Handler handler) throws IOException {
/*  71 */       super(u, handler);
/*     */     }
/*     */     
/*     */     public UpnpURLConnection(URL u, String host, int port) throws IOException {
/*  75 */       super(u, host, port);
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized OutputStream getOutputStream() throws IOException {
/*  80 */       String savedMethod = this.method;
/*     */       
/*  82 */       if (this.method.equals("PUT") || this.method.equals("POST") || this.method.equals("NOTIFY")) {
/*     */         
/*  84 */         this.method = "PUT";
/*     */       }
/*     */       else {
/*     */         
/*  88 */         this.method = "GET";
/*     */       } 
/*  90 */       OutputStream os = super.getOutputStream();
/*  91 */       this.method = savedMethod;
/*  92 */       return os;
/*     */     }
/*     */     
/*     */     public void setRequestMethod(String method) throws ProtocolException {
/*  96 */       if (this.connected) {
/*  97 */         throw new ProtocolException("Cannot reset method once connected");
/*     */       }
/*  99 */       for (String m : methods) {
/* 100 */         if (m.equals(method)) {
/* 101 */           this.method = method;
/*     */           return;
/*     */         } 
/*     */       } 
/* 105 */       throw new ProtocolException("Invalid UPnP HTTP method: " + method);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\FixedSunURLStreamHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */