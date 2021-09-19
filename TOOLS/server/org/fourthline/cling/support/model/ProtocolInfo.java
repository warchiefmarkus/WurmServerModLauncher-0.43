/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
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
/*     */ public class ProtocolInfo
/*     */ {
/*     */   public static final String WILDCARD = "*";
/*  30 */   protected Protocol protocol = Protocol.ALL;
/*  31 */   protected String network = "*";
/*  32 */   protected String contentFormat = "*";
/*  33 */   protected String additionalInfo = "*";
/*     */   
/*     */   public ProtocolInfo(String s) throws InvalidValueException {
/*  36 */     if (s == null) throw new NullPointerException(); 
/*  37 */     s = s.trim();
/*  38 */     String[] split = s.split(":");
/*  39 */     if (split.length != 4) {
/*  40 */       throw new InvalidValueException("Can't parse ProtocolInfo string: " + s);
/*     */     }
/*  42 */     this.protocol = Protocol.value(split[0]);
/*  43 */     this.network = split[1];
/*  44 */     this.contentFormat = split[2];
/*  45 */     this.additionalInfo = split[3];
/*     */   }
/*     */   
/*     */   public ProtocolInfo(MimeType contentFormatMimeType) {
/*  49 */     this.protocol = Protocol.HTTP_GET;
/*  50 */     this.contentFormat = contentFormatMimeType.toString();
/*     */   }
/*     */   
/*     */   public ProtocolInfo(Protocol protocol, String network, String contentFormat, String additionalInfo) {
/*  54 */     this.protocol = protocol;
/*  55 */     this.network = network;
/*  56 */     this.contentFormat = contentFormat;
/*  57 */     this.additionalInfo = additionalInfo;
/*     */   }
/*     */   
/*     */   public Protocol getProtocol() {
/*  61 */     return this.protocol;
/*     */   }
/*     */   
/*     */   public String getNetwork() {
/*  65 */     return this.network;
/*     */   }
/*     */   
/*     */   public String getContentFormat() {
/*  69 */     return this.contentFormat;
/*     */   }
/*     */   
/*     */   public MimeType getContentFormatMimeType() throws IllegalArgumentException {
/*  73 */     return MimeType.valueOf(this.contentFormat);
/*     */   }
/*     */   
/*     */   public String getAdditionalInfo() {
/*  77 */     return this.additionalInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  82 */     if (this == o) return true; 
/*  83 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/*  85 */     ProtocolInfo that = (ProtocolInfo)o;
/*     */     
/*  87 */     if (!this.additionalInfo.equals(that.additionalInfo)) return false; 
/*  88 */     if (!this.contentFormat.equals(that.contentFormat)) return false; 
/*  89 */     if (!this.network.equals(that.network)) return false; 
/*  90 */     if (this.protocol != that.protocol) return false;
/*     */     
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  97 */     int result = this.protocol.hashCode();
/*  98 */     result = 31 * result + this.network.hashCode();
/*  99 */     result = 31 * result + this.contentFormat.hashCode();
/* 100 */     result = 31 * result + this.additionalInfo.hashCode();
/* 101 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     return this.protocol.toString() + ":" + this.network + ":" + this.contentFormat + ":" + this.additionalInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\ProtocolInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */