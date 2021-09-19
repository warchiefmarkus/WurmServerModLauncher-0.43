/*     */ package org.fourthline.cling.model.types;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.UUID;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UDN
/*     */ {
/*  41 */   private static final Logger log = Logger.getLogger(UDN.class.getName());
/*     */ 
/*     */   
/*     */   public static final String PREFIX = "uuid:";
/*     */ 
/*     */   
/*     */   private String identifierString;
/*     */ 
/*     */   
/*     */   public UDN(String identifierString) {
/*  51 */     this.identifierString = identifierString;
/*     */   }
/*     */   
/*     */   public UDN(UUID uuid) {
/*  55 */     this.identifierString = uuid.toString();
/*     */   }
/*     */   
/*     */   public boolean isUDA11Compliant() {
/*     */     try {
/*  60 */       UUID.fromString(this.identifierString);
/*  61 */       return true;
/*  62 */     } catch (IllegalArgumentException ex) {
/*  63 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getIdentifierString() {
/*  68 */     return this.identifierString;
/*     */   }
/*     */   
/*     */   public static UDN valueOf(String udnString) {
/*  72 */     return new UDN(udnString.startsWith("uuid:") ? udnString.substring("uuid:".length()) : udnString);
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
/*     */   public static UDN uniqueSystemIdentifier(String salt) {
/* 100 */     StringBuilder systemSalt = new StringBuilder();
/*     */ 
/*     */     
/* 103 */     if (!ModelUtil.ANDROID_RUNTIME) {
/*     */       try {
/* 105 */         systemSalt.append(new String(ModelUtil.getFirstNetworkInterfaceHardwareAddress(), "UTF-8"));
/* 106 */       } catch (UnsupportedEncodingException ex) {
/*     */         
/* 108 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } else {
/* 111 */       throw new RuntimeException("This method does not create a unique identifier on Android, see the Javadoc and use new UDN(UUID) instead!");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 118 */       byte[] hash = MessageDigest.getInstance("MD5").digest(systemSalt.toString().getBytes("UTF-8"));
/* 119 */       return new UDN(new UUID((new BigInteger(-1, hash))
/*     */             
/* 121 */             .longValue(), salt
/* 122 */             .hashCode()));
/*     */     
/*     */     }
/* 125 */     catch (Exception ex) {
/* 126 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 132 */     return "uuid:" + getIdentifierString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 137 */     if (this == o) return true; 
/* 138 */     if (o == null || !(o instanceof UDN)) return false; 
/* 139 */     UDN udn = (UDN)o;
/* 140 */     return this.identifierString.equals(udn.identifierString);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 145 */     return this.identifierString.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UDN.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */