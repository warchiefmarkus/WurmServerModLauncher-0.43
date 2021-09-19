/*     */ package com.sun.javaws.net;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.deploy.util.URLUtil;
/*     */ import com.sun.javaws.Globals;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicNetworkLayer
/*     */   implements HttpRequest
/*     */ {
/*     */   private static final String USER_AGENT_JAVA_VERSION = "UA-Java-Version";
/*     */   private static final String USER_AGENT = "User-Agent";
/*     */   
/*     */   static class BasicHttpResponse
/*     */     implements HttpResponse
/*     */   {
/*     */     private URL _request;
/*     */     private int _status;
/*     */     private int _length;
/*     */     private long _lastModified;
/*     */     private String _mimeType;
/*     */     private Map _headers;
/*     */     private BufferedInputStream _bis;
/*     */     private HttpURLConnection _httpURLConnection;
/*     */     private String _contentEncoding;
/*     */     
/*     */     BasicHttpResponse(URL param1URL, int param1Int1, int param1Int2, long param1Long, String param1String1, Map param1Map, BufferedInputStream param1BufferedInputStream, HttpURLConnection param1HttpURLConnection, String param1String2) {
/*  50 */       this._request = param1URL;
/*  51 */       this._status = param1Int1;
/*  52 */       this._length = param1Int2;
/*  53 */       this._lastModified = param1Long;
/*  54 */       this._mimeType = param1String1;
/*  55 */       this._headers = param1Map;
/*  56 */       this._bis = param1BufferedInputStream;
/*  57 */       this._httpURLConnection = param1HttpURLConnection;
/*  58 */       this._contentEncoding = param1String2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void disconnect() {
/*  64 */       if (this._httpURLConnection != null) {
/*  65 */         this._httpURLConnection.disconnect();
/*     */         
/*  67 */         Trace.println("Disconnect connection to " + this._request, TraceLevel.NETWORK);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public URL getRequest() {
/*  73 */       return this._request;
/*     */     } public int getStatusCode() {
/*  75 */       return this._status;
/*     */     } public int getContentLength() {
/*  77 */       return this._length;
/*     */     } public long getLastModified() {
/*  79 */       return this._lastModified;
/*     */     } public String getContentType() {
/*  81 */       return this._mimeType;
/*     */     } public String getContentEncoding() {
/*  83 */       return this._contentEncoding;
/*     */     }
/*     */     public String getResponseHeader(String param1String) {
/*  86 */       return (String)this._headers.get(param1String.toLowerCase());
/*     */     } public BufferedInputStream getInputStream() {
/*  88 */       return this._bis;
/*     */     } }
/*     */   
/*     */   public HttpResponse doGetRequest(URL paramURL) throws IOException {
/*  92 */     return doRequest(paramURL, false, null, null, true);
/*     */   }
/*     */   
/*     */   public HttpResponse doGetRequest(URL paramURL, boolean paramBoolean) throws IOException {
/*  96 */     return doRequest(paramURL, false, null, null, paramBoolean);
/*     */   }
/*     */   
/*     */   public HttpResponse doHeadRequest(URL paramURL) throws IOException {
/* 100 */     return doRequest(paramURL, true, null, null, true);
/*     */   }
/*     */   
/*     */   public HttpResponse doHeadRequest(URL paramURL, boolean paramBoolean) throws IOException {
/* 104 */     return doRequest(paramURL, true, null, null, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse doGetRequest(URL paramURL, String[] paramArrayOfString1, String[] paramArrayOfString2) throws IOException {
/* 109 */     return doRequest(paramURL, false, paramArrayOfString1, paramArrayOfString2, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse doGetRequest(URL paramURL, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean) throws IOException {
/* 114 */     return doRequest(paramURL, false, paramArrayOfString1, paramArrayOfString2, paramBoolean);
/*     */   }
/*     */   
/*     */   public HttpResponse doHeadRequest(URL paramURL, String[] paramArrayOfString1, String[] paramArrayOfString2) throws IOException {
/* 118 */     return doRequest(paramURL, true, paramArrayOfString1, paramArrayOfString2, true);
/*     */   }
/*     */   
/*     */   public HttpResponse doHeadRequest(URL paramURL, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean) throws IOException {
/* 122 */     return doRequest(paramURL, true, paramArrayOfString1, paramArrayOfString2, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpResponse doRequest(URL paramURL, boolean paramBoolean1, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean2) throws IOException {
/* 130 */     long l1 = 0L;
/* 131 */     String str1 = null;
/* 132 */     if ("file".equals(paramURL.getProtocol()) && paramURL.getFile() != null) {
/*     */       
/*     */       try {
/* 135 */         String str = URLUtil.getPathFromURL(paramURL);
/* 136 */         File file = new File(str);
/* 137 */         l1 = file.lastModified();
/*     */         
/* 139 */         Trace.println("File URL discovered. Real timestamp: " + new Date(l1), TraceLevel.NETWORK);
/*     */         
/* 141 */         if (str.endsWith(".jnlp")) { str1 = "application/x-java-jnlp-file"; }
/* 142 */         else if (str.endsWith(".jardiff")) { str1 = "application/x-java-archive-diff"; } 
/* 143 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 148 */     URLConnection uRLConnection = null;
/*     */     
/* 150 */     if (paramURL.getProtocol().equals("file")) {
/*     */       
/* 152 */       uRLConnection = createUrlConnection(new URL(paramURL.getProtocol(), paramURL.getHost(), paramURL.getPath()), paramBoolean1, paramArrayOfString1, paramArrayOfString2, paramBoolean2);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 158 */       uRLConnection = createUrlConnection(paramURL, paramBoolean1, paramArrayOfString1, paramArrayOfString2, paramBoolean2);
/*     */     } 
/*     */ 
/*     */     
/* 162 */     HttpURLConnection httpURLConnection = null;
/* 163 */     if (uRLConnection instanceof HttpURLConnection) {
/* 164 */       httpURLConnection = (HttpURLConnection)uRLConnection;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     URLUtil.setHostHeader(uRLConnection);
/*     */ 
/*     */ 
/*     */     
/* 175 */     uRLConnection.connect();
/*     */     
/* 177 */     int i = 200;
/* 178 */     if (httpURLConnection != null) {
/* 179 */       i = httpURLConnection.getResponseCode();
/*     */     }
/*     */     
/* 182 */     int j = uRLConnection.getContentLength();
/* 183 */     long l2 = (l1 != 0L) ? l1 : uRLConnection.getLastModified();
/* 184 */     String str2 = (str1 != null) ? str1 : uRLConnection.getContentType();
/*     */ 
/*     */     
/* 187 */     if (str2 != null && str2.indexOf(';') != -1) {
/* 188 */       str2 = str2.substring(0, str2.indexOf(';')).trim();
/*     */     }
/*     */ 
/*     */     
/* 192 */     HashMap hashMap = new HashMap();
/* 193 */     byte b = 1;
/* 194 */     String str3 = uRLConnection.getHeaderFieldKey(b);
/* 195 */     while (str3 != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 200 */       hashMap.put(str3.toLowerCase(), uRLConnection.getHeaderField(b));
/* 201 */       b++;
/* 202 */       str3 = uRLConnection.getHeaderFieldKey(b);
/*     */     } 
/*     */     
/* 205 */     String str4 = (String)hashMap.get("content-encoding");
/*     */     
/* 207 */     if (str4 != null) str4 = str4.toLowerCase();
/*     */     
/* 209 */     Trace.println("encoding = " + str4 + " for " + paramURL.toString(), TraceLevel.NETWORK);
/*     */     
/* 211 */     BufferedInputStream bufferedInputStream = null;
/* 212 */     if (paramBoolean1) {
/* 213 */       bufferedInputStream = null;
/*     */     } else {
/* 215 */       BufferedInputStream bufferedInputStream1 = null;
/* 216 */       bufferedInputStream1 = new BufferedInputStream(uRLConnection.getInputStream());
/*     */       
/* 218 */       if (str4 != null && (str4.compareTo("pack200-gzip") == 0 || str4.compareTo("gzip") == 0)) {
/*     */ 
/*     */         
/* 221 */         bufferedInputStream = new BufferedInputStream(new GZIPInputStream(bufferedInputStream1));
/*     */       } else {
/* 223 */         bufferedInputStream = new BufferedInputStream(bufferedInputStream1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 228 */     return new BasicHttpResponse(paramURL, i, j, l2, str2, hashMap, bufferedInputStream, httpURLConnection, str4);
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
/*     */   private URLConnection createUrlConnection(URL paramURL, boolean paramBoolean1, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean2) throws MalformedURLException, IOException {
/* 246 */     URLConnection uRLConnection = paramURL.openConnection();
/*     */ 
/*     */     
/* 249 */     addToRequestProperty(uRLConnection, "pragma", "no-cache");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     if (paramBoolean2 && paramURL.getPath().endsWith(".jar")) {
/* 259 */       String str = Globals.havePack200() ? "pack200-gzip,gzip" : "gzip";
/*     */ 
/*     */       
/* 262 */       addToRequestProperty(uRLConnection, "accept-encoding", str);
/* 263 */       addToRequestProperty(uRLConnection, "content-type", "application/x-java-archive");
/* 264 */       Trace.println("Requesting file " + paramURL.getFile() + " with Encoding = " + str, TraceLevel.NETWORK);
/*     */     } 
/*     */ 
/*     */     
/* 268 */     if (System.getProperty("http.agent") == null) {
/* 269 */       uRLConnection.setRequestProperty("User-Agent", Globals.getUserAgent());
/* 270 */       uRLConnection.setRequestProperty("UA-Java-Version", Globals.getJavaVersion());
/*     */     } 
/*     */ 
/*     */     
/* 274 */     if (paramArrayOfString1 != null && paramArrayOfString2 != null) {
/* 275 */       for (byte b = 0; b < paramArrayOfString1.length; b++) {
/* 276 */         uRLConnection.setRequestProperty(paramArrayOfString1[b], paramArrayOfString2[b]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 281 */     if (uRLConnection instanceof HttpURLConnection) {
/* 282 */       ((HttpURLConnection)uRLConnection).setRequestMethod(paramBoolean1 ? "HEAD" : "GET");
/*     */     }
/* 284 */     return uRLConnection;
/*     */   }
/*     */ 
/*     */   
/*     */   private void addToRequestProperty(URLConnection paramURLConnection, String paramString1, String paramString2) {
/* 289 */     String str = paramURLConnection.getRequestProperty(paramString1);
/* 290 */     if (str == null || str.trim().length() == 0) {
/* 291 */       str = paramString2;
/*     */     } else {
/* 293 */       str = str + "," + paramString2;
/*     */     } 
/* 295 */     paramURLConnection.setRequestProperty(paramString1, str);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\net\BasicNetworkLayer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */