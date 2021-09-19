/*     */ package winterwell.jtwitter.guts;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Observable;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientHttpRequest
/*     */   extends Observable
/*     */ {
/*     */   URLConnection connection;
/*  33 */   OutputStream os = null;
/*  34 */   Map<String, String> cookies = new HashMap<String, String>();
/*  35 */   String rawCookies = "";
/*     */   
/*     */   protected void connect() throws IOException {
/*  38 */     if (this.os == null) this.os = this.connection.getOutputStream(); 
/*     */   }
/*     */   
/*     */   protected void write(char c) throws IOException {
/*  42 */     connect();
/*  43 */     this.os.write(c);
/*     */   }
/*     */   
/*     */   protected void write(String s) throws IOException {
/*  47 */     connect();
/*  48 */     this.os.write(s.getBytes());
/*     */   }
/*     */ 
/*     */   
/*     */   protected long newlineNumBytes() {
/*  53 */     return 2L;
/*     */   }
/*     */   
/*     */   protected void newline() throws IOException {
/*  57 */     connect();
/*  58 */     write("\r\n");
/*     */   }
/*     */   
/*     */   protected void writeln(String s) throws IOException {
/*  62 */     connect();
/*  63 */     write(s);
/*  64 */     newline();
/*     */   }
/*     */   
/*  67 */   private static Random random = new Random();
/*     */   
/*     */   protected static String randomString() {
/*  70 */     return Long.toString(random.nextLong(), 36);
/*     */   }
/*     */ 
/*     */   
/*     */   private long boundaryNumBytes() {
/*  75 */     return (this.boundary.length() + 2);
/*     */   }
/*     */   
/*  78 */   String boundary = "---------------------------" + 
/*  79 */     randomString() + randomString() + randomString(); private boolean isCanceled; private int bytesSent;
/*     */   
/*     */   private void boundary() throws IOException {
/*  82 */     write("--");
/*  83 */     write(this.boundary);
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
/*     */   public ClientHttpRequest(URL url) throws IOException {
/* 107 */     this(url.openConnection());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientHttpRequest(String urlString) throws IOException {
/* 117 */     this(new URL(urlString));
/*     */   }
/*     */   
/*     */   private void postCookies() {
/* 121 */     StringBuffer cookieList = new StringBuffer(this.rawCookies);
/* 122 */     for (Map.Entry<String, String> cookie : this.cookies.entrySet()) {
/* 123 */       if (cookieList.length() > 0) {
/* 124 */         cookieList.append("; ");
/*     */       }
/* 126 */       cookieList.append(String.valueOf(cookie.getKey()) + "=" + (String)cookie.getValue());
/*     */     } 
/* 128 */     if (cookieList.length() > 0) {
/* 129 */       this.connection.setRequestProperty("Cookie", cookieList.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookies(String rawCookies) throws IOException {
/* 140 */     this.rawCookies = (rawCookies == null) ? "" : rawCookies;
/* 141 */     this.cookies.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookie(String name, String value) throws IOException {
/* 151 */     this.cookies.put(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookies(Map<? extends String, ? extends String> cookies) throws IOException {
/* 160 */     if (cookies != null) {
/* 161 */       this.cookies.putAll(cookies);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookies(String[] cookies) throws IOException {
/* 172 */     if (cookies != null) {
/* 173 */       for (int i = 0; i < cookies.length - 1; i += 2) {
/* 174 */         setCookie(cookies[i], cookies[i + 1]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private long writeNameNumBytes(String name) {
/* 181 */     return 
/* 182 */       newlineNumBytes() + 
/* 183 */       "Content-Disposition: form-data; name=\"".length() + (
/* 184 */       name.getBytes()).length + 
/* 185 */       1L;
/*     */   }
/*     */   
/*     */   private void writeName(String name) throws IOException {
/* 189 */     newline();
/* 190 */     write("Content-Disposition: form-data; name=\"");
/* 191 */     write(name);
/* 192 */     write('"');
/*     */   }
/*     */   
/* 195 */   public ClientHttpRequest(URLConnection connection) throws IOException { this.isCanceled = false;
/* 196 */     this.bytesSent = 0;
/*     */     this.connection = connection;
/*     */     connection.setDoOutput(true);
/*     */     connection.setDoInput(true);
/* 200 */     connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary); } public int getBytesSent() { return this.bytesSent; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 205 */     this.isCanceled = true;
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void pipe(InputStream in, OutputStream out) throws IOException {
/* 210 */     byte[] buf = new byte[1024];
/*     */     
/* 212 */     this.bytesSent = 0;
/* 213 */     this.isCanceled = false;
/* 214 */     synchronized (in) {
/* 215 */       int nread; while ((nread = in.read(buf, 0, buf.length)) >= 0) {
/* 216 */         out.write(buf, 0, nread);
/* 217 */         this.bytesSent += nread;
/* 218 */         if (this.isCanceled) {
/* 219 */           throw new IOException("Canceled");
/*     */         }
/* 221 */         out.flush();
/* 222 */         setChanged();
/* 223 */         notifyObservers(Integer.valueOf(this.bytesSent));
/* 224 */         clearChanged();
/*     */       } 
/*     */     } 
/* 227 */     out.flush();
/* 228 */     buf = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameter(String name, String value) throws IOException {
/* 238 */     boundary();
/* 239 */     writeName(name);
/* 240 */     newline(); newline();
/* 241 */     writeln(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameter(String name, String filename, InputStream is) throws IOException {
/* 252 */     boundary();
/* 253 */     writeName(name);
/* 254 */     write("; filename=\"");
/* 255 */     write(filename);
/* 256 */     write('"');
/* 257 */     newline();
/* 258 */     write("Content-Type: ");
/* 259 */     String type = URLConnection.guessContentTypeFromName(filename);
/* 260 */     if (type == null) type = "application/octet-stream"; 
/* 261 */     writeln(type);
/* 262 */     newline();
/* 263 */     pipe(is, this.os);
/* 264 */     newline();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getFilePostSize(String name, File file) {
/* 269 */     String filename = file.getPath();
/* 270 */     String type = URLConnection.guessContentTypeFromName(filename);
/* 271 */     if (type == null) type = "application/octet-stream";
/*     */     
/* 273 */     return 
/* 274 */       boundaryNumBytes() + 
/* 275 */       writeNameNumBytes(name) + 
/* 276 */       "; filename=\"".length() + (
/* 277 */       filename.getBytes()).length + 
/* 278 */       1L + 
/* 279 */       newlineNumBytes() + 
/* 280 */       "Content-Type: ".length() + 
/* 281 */       type.length() + 
/* 282 */       newlineNumBytes() + 
/* 283 */       newlineNumBytes() + 
/* 284 */       file.length() + 
/* 285 */       newlineNumBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameter(String name, File file) throws IOException {
/* 295 */     FileInputStream fis = null;
/*     */     try {
/* 297 */       fis = new FileInputStream(file);
/* 298 */       setParameter(name, file.getPath(), fis);
/*     */     } finally {
/* 300 */       if (fis != null) {
/* 301 */         fis.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameter(Object name, Object object) throws IOException {
/* 313 */     if (object instanceof File) {
/* 314 */       setParameter(name.toString(), (File)object);
/*     */     } else {
/* 316 */       setParameter(name.toString(), object.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(Map parameters) throws IOException {
/* 326 */     if (parameters != null) {
/* 327 */       for (Iterator<Map.Entry> i = parameters.entrySet().iterator(); i.hasNext(); ) {
/* 328 */         Map.Entry entry = i.next();
/* 329 */         setParameter(entry.getKey().toString(), entry.getValue());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(Object... parameters) throws IOException {
/* 340 */     for (int i = 0; i < parameters.length - 1; i += 2) {
/* 341 */       setParameter(parameters[i].toString(), parameters[i + 1]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPostFooterSize() {
/* 347 */     return boundaryNumBytes() + 2L + 
/* 348 */       newlineNumBytes() + newlineNumBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputStream doPost() throws IOException {
/* 357 */     boundary();
/* 358 */     writeln("--");
/* 359 */     this.os.close();
/*     */     
/* 361 */     return this.connection.getInputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream post() throws IOException {
/* 370 */     postCookies();
/* 371 */     return doPost();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream post(Map parameters) throws IOException {
/* 382 */     postCookies();
/* 383 */     setParameters(parameters);
/* 384 */     return doPost();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream post(Object... parameters) throws IOException {
/* 395 */     postCookies();
/* 396 */     setParameters(parameters);
/* 397 */     return doPost();
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
/*     */   public InputStream post(Map cookies, Map parameters) throws IOException {
/* 410 */     setCookies(cookies);
/* 411 */     postCookies();
/* 412 */     setParameters(parameters);
/* 413 */     return doPost();
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
/*     */   public InputStream post(String raw_cookies, Map parameters) throws IOException {
/* 426 */     setCookies(raw_cookies);
/* 427 */     postCookies();
/* 428 */     setParameters(parameters);
/* 429 */     return doPost();
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
/*     */   public InputStream post(String[] cookies, Object[] parameters) throws IOException {
/* 442 */     setCookies(cookies);
/* 443 */     postCookies();
/* 444 */     setParameters(parameters);
/* 445 */     return doPost();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream post(URL url, Map parameters) throws IOException {
/* 456 */     return (new ClientHttpRequest(url)).post(parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream post(URL url, Object[] parameters) throws IOException {
/* 467 */     return (new ClientHttpRequest(url)).post(parameters);
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
/*     */   public static InputStream post(URL url, Map cookies, Map parameters) throws IOException {
/* 480 */     return (new ClientHttpRequest(url)).post(cookies, parameters);
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
/*     */   public static InputStream post(URL url, String[] cookies, Object[] parameters) throws IOException {
/* 493 */     return (new ClientHttpRequest(url)).post(cookies, parameters);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\guts\ClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */