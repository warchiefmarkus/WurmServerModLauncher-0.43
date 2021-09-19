/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.ReasonPhraseCatalog;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BasicHttpResponse
/*     */   extends AbstractHttpMessage
/*     */   implements HttpResponse
/*     */ {
/*     */   private StatusLine statusline;
/*     */   private HttpEntity entity;
/*     */   private ReasonPhraseCatalog reasonCatalog;
/*     */   private Locale locale;
/*     */   
/*     */   public BasicHttpResponse(StatusLine statusline, ReasonPhraseCatalog catalog, Locale locale) {
/*  69 */     if (statusline == null) {
/*  70 */       throw new IllegalArgumentException("Status line may not be null.");
/*     */     }
/*  72 */     this.statusline = statusline;
/*  73 */     this.reasonCatalog = catalog;
/*  74 */     this.locale = (locale != null) ? locale : Locale.getDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpResponse(StatusLine statusline) {
/*  85 */     this(statusline, (ReasonPhraseCatalog)null, (Locale)null);
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
/*     */   public BasicHttpResponse(ProtocolVersion ver, int code, String reason) {
/* 101 */     this(new BasicStatusLine(ver, code, reason), (ReasonPhraseCatalog)null, (Locale)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/* 107 */     return this.statusline.getProtocolVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public StatusLine getStatusLine() {
/* 112 */     return this.statusline;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpEntity getEntity() {
/* 117 */     return this.entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 122 */     return this.locale;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatusLine(StatusLine statusline) {
/* 127 */     if (statusline == null) {
/* 128 */       throw new IllegalArgumentException("Status line may not be null");
/*     */     }
/* 130 */     this.statusline = statusline;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusLine(ProtocolVersion ver, int code) {
/* 136 */     this.statusline = new BasicStatusLine(ver, code, getReason(code));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusLine(ProtocolVersion ver, int code, String reason) {
/* 143 */     this.statusline = new BasicStatusLine(ver, code, reason);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusCode(int code) {
/* 149 */     ProtocolVersion ver = this.statusline.getProtocolVersion();
/* 150 */     this.statusline = new BasicStatusLine(ver, code, getReason(code));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReasonPhrase(String reason) {
/* 156 */     if (reason != null && (reason.indexOf('\n') >= 0 || reason.indexOf('\r') >= 0))
/*     */     {
/*     */       
/* 159 */       throw new IllegalArgumentException("Line break in reason phrase.");
/*     */     }
/* 161 */     this.statusline = new BasicStatusLine(this.statusline.getProtocolVersion(), this.statusline.getStatusCode(), reason);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntity(HttpEntity entity) {
/* 168 */     this.entity = entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocale(Locale loc) {
/* 173 */     if (loc == null) {
/* 174 */       throw new IllegalArgumentException("Locale may not be null.");
/*     */     }
/* 176 */     this.locale = loc;
/* 177 */     int code = this.statusline.getStatusCode();
/* 178 */     this.statusline = new BasicStatusLine(this.statusline.getProtocolVersion(), code, getReason(code));
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
/*     */   protected String getReason(int code) {
/* 192 */     return (this.reasonCatalog == null) ? null : this.reasonCatalog.getReason(code, this.locale);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 198 */     return this.statusline + " " + this.headergroup;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicHttpResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */