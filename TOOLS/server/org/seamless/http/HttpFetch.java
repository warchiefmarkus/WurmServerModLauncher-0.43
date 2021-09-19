/*    */ package org.seamless.http;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import org.seamless.util.io.IO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpFetch
/*    */ {
/*    */   public static Representation<byte[]> fetchBinary(URL url) throws IOException {
/* 31 */     return fetchBinary(url, 500, 500);
/*    */   }
/*    */   
/*    */   public static Representation<byte[]> fetchBinary(URL url, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
/* 35 */     return (Representation)fetch(url, connectTimeoutMillis, readTimeoutMillis, (RepresentationFactory)new RepresentationFactory<byte[]>() {
/*    */           public Representation<byte[]> createRepresentation(URLConnection urlConnection, InputStream is) throws IOException {
/* 37 */             return (Representation)new Representation<byte>(urlConnection, IO.readBytes(is));
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public static Representation<String> fetchString(URL url, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
/* 43 */     return fetch(url, connectTimeoutMillis, readTimeoutMillis, new RepresentationFactory<String>() {
/*    */           public Representation<String> createRepresentation(URLConnection urlConnection, InputStream is) throws IOException {
/* 45 */             return new Representation<String>(urlConnection, IO.readLines(is));
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public static <E> Representation<E> fetch(URL url, int connectTimeoutMillis, int readTimeoutMillis, RepresentationFactory<E> factory) throws IOException {
/* 52 */     return fetch(url, "GET", connectTimeoutMillis, readTimeoutMillis, factory);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> Representation<E> fetch(URL url, String method, int connectTimeoutMillis, int readTimeoutMillis, RepresentationFactory<E> factory) throws IOException {
/* 58 */     HttpURLConnection urlConnection = null;
/* 59 */     InputStream is = null;
/*    */     try {
/* 61 */       urlConnection = (HttpURLConnection)url.openConnection();
/*    */       
/* 63 */       urlConnection.setRequestMethod(method);
/*    */       
/* 65 */       urlConnection.setConnectTimeout(connectTimeoutMillis);
/* 66 */       urlConnection.setReadTimeout(readTimeoutMillis);
/*    */       
/* 68 */       is = urlConnection.getInputStream();
/*    */       
/* 70 */       return factory.createRepresentation(urlConnection, is);
/*    */     
/*    */     }
/* 73 */     catch (IOException ex) {
/* 74 */       if (urlConnection != null) {
/* 75 */         int responseCode = urlConnection.getResponseCode();
/* 76 */         throw new IOException("Fetching resource failed, returned status code: " + responseCode);
/*    */       } 
/*    */       
/* 79 */       throw ex;
/*    */     } finally {
/* 81 */       if (is != null) is.close();
/*    */     
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void validate(URL url) throws IOException {
/* 90 */     fetch(url, "HEAD", 500, 500, new RepresentationFactory() {
/*    */           public Representation createRepresentation(URLConnection urlConnection, InputStream is) throws IOException {
/* 92 */             return new Representation(urlConnection, null);
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public static interface RepresentationFactory<E> {
/*    */     Representation<E> createRepresentation(URLConnection param1URLConnection, InputStream param1InputStream) throws IOException;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\http\HttpFetch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */