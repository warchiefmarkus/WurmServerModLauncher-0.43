/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicStatusLine
/*     */   implements StatusLine, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2443303766890459269L;
/*     */   private final ProtocolVersion protoVersion;
/*     */   private final int statusCode;
/*     */   private final String reasonPhrase;
/*     */   
/*     */   public BasicStatusLine(ProtocolVersion version, int statusCode, String reasonPhrase) {
/*  69 */     if (version == null) {
/*  70 */       throw new IllegalArgumentException("Protocol version may not be null.");
/*     */     }
/*     */     
/*  73 */     if (statusCode < 0) {
/*  74 */       throw new IllegalArgumentException("Status code may not be negative.");
/*     */     }
/*     */     
/*  77 */     this.protoVersion = version;
/*  78 */     this.statusCode = statusCode;
/*  79 */     this.reasonPhrase = reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusCode() {
/*  85 */     return this.statusCode;
/*     */   }
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/*  89 */     return this.protoVersion;
/*     */   }
/*     */   
/*     */   public String getReasonPhrase() {
/*  93 */     return this.reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return BasicLineFormatter.DEFAULT.formatStatusLine((CharArrayBuffer)null, this).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 105 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicStatusLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */