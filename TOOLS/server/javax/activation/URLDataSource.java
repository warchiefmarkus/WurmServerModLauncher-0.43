/*     */ package javax.activation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URLDataSource
/*     */   implements DataSource
/*     */ {
/*  61 */   private URL url = null;
/*  62 */   private URLConnection url_conn = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLDataSource(URL url) {
/*  72 */     this.url = url;
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
/*     */   public String getContentType() {
/*  87 */     String type = null;
/*     */     
/*     */     try {
/*  90 */       if (this.url_conn == null)
/*  91 */         this.url_conn = this.url.openConnection(); 
/*  92 */     } catch (IOException e) {}
/*     */     
/*  94 */     if (this.url_conn != null) {
/*  95 */       type = this.url_conn.getContentType();
/*     */     }
/*  97 */     if (type == null) {
/*  98 */       type = "application/octet-stream";
/*     */     }
/* 100 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 110 */     return this.url.getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 120 */     return this.url.openStream();
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
/*     */   public OutputStream getOutputStream() throws IOException {
/* 133 */     this.url_conn = this.url.openConnection();
/*     */     
/* 135 */     if (this.url_conn != null) {
/* 136 */       this.url_conn.setDoOutput(true);
/* 137 */       return this.url_conn.getOutputStream();
/*     */     } 
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() {
/* 148 */     return this.url;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\URLDataSource.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */