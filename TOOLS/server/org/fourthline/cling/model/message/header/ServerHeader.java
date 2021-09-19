/*     */ package org.fourthline.cling.model.message.header;
/*     */ 
/*     */ import org.fourthline.cling.model.ServerClientTokens;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerHeader
/*     */   extends UpnpHeader<ServerClientTokens>
/*     */ {
/*     */   public ServerHeader() {
/*  26 */     setValue(new ServerClientTokens());
/*     */   }
/*     */   
/*     */   public ServerHeader(ServerClientTokens tokens) {
/*  30 */     setValue(tokens);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setString(String s) throws InvalidHeaderException {
/*  38 */     ServerClientTokens serverClientTokens = new ServerClientTokens();
/*     */ 
/*     */     
/*  41 */     serverClientTokens.setOsName("UNKNOWN");
/*  42 */     serverClientTokens.setOsVersion("UNKNOWN");
/*  43 */     serverClientTokens.setProductName("UNKNOWN");
/*  44 */     serverClientTokens.setProductVersion("UNKNOWN");
/*     */ 
/*     */     
/*  47 */     if (s.contains("UPnP/1.1")) {
/*  48 */       serverClientTokens.setMinorVersion(1);
/*  49 */     } else if (!s.contains("UPnP/1.")) {
/*  50 */       throw new InvalidHeaderException("Missing 'UPnP/1.' in server information: " + s);
/*     */     } 
/*     */     
/*     */     try {
/*     */       String[] osNameVersion, productNameVersion;
/*     */       
/*  56 */       int numberOfSpaces = 0;
/*  57 */       for (int i = 0; i < s.length(); i++) {
/*  58 */         if (s.charAt(i) == ' ') numberOfSpaces++;
/*     */       
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  64 */       if (s.contains(",")) {
/*     */ 
/*     */         
/*  67 */         String[] productTokens = s.split(",");
/*  68 */         osNameVersion = productTokens[0].split("/");
/*  69 */         productNameVersion = productTokens[2].split("/");
/*     */       }
/*  71 */       else if (numberOfSpaces > 2) {
/*     */ 
/*     */         
/*  74 */         String beforeUpnpToken = s.substring(0, s.indexOf("UPnP/1.")).trim();
/*  75 */         String afterUpnpToken = s.substring(s.indexOf("UPnP/1.") + 8).trim();
/*  76 */         osNameVersion = beforeUpnpToken.split("/");
/*  77 */         productNameVersion = afterUpnpToken.split("/");
/*     */       
/*     */       }
/*     */       else {
/*     */         
/*  82 */         String[] productTokens = s.split(" ");
/*  83 */         osNameVersion = productTokens[0].split("/");
/*  84 */         productNameVersion = productTokens[2].split("/");
/*     */       } 
/*     */ 
/*     */       
/*  88 */       serverClientTokens.setOsName(osNameVersion[0].trim());
/*  89 */       if (osNameVersion.length > 1) {
/*  90 */         serverClientTokens.setOsVersion(osNameVersion[1].trim());
/*     */       }
/*  92 */       serverClientTokens.setProductName(productNameVersion[0].trim());
/*  93 */       if (productNameVersion.length > 1) {
/*  94 */         serverClientTokens.setProductVersion(productNameVersion[1].trim());
/*     */       }
/*     */     }
/*  97 */     catch (Exception ex) {
/*     */ 
/*     */       
/* 100 */       serverClientTokens.setOsName("UNKNOWN");
/* 101 */       serverClientTokens.setOsVersion("UNKNOWN");
/* 102 */       serverClientTokens.setProductName("UNKNOWN");
/* 103 */       serverClientTokens.setProductVersion("UNKNOWN");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     setValue(serverClientTokens);
/*     */   }
/*     */   
/*     */   public String getString() {
/* 123 */     return getValue().getHttpToken();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\ServerHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */