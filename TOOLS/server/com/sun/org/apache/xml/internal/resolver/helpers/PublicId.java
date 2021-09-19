/*     */ package com.sun.org.apache.xml.internal.resolver.helpers;
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
/*     */ 
/*     */ 
/*     */ public abstract class PublicId
/*     */ {
/*     */   public static String normalize(String publicId) {
/*  58 */     String normal = publicId.replace('\t', ' ');
/*  59 */     normal = normal.replace('\r', ' ');
/*  60 */     normal = normal.replace('\n', ' ');
/*  61 */     normal = normal.trim();
/*     */     
/*     */     int pos;
/*     */     
/*  65 */     while ((pos = normal.indexOf("  ")) >= 0) {
/*  66 */       normal = normal.substring(0, pos) + normal.substring(pos + 1);
/*     */     }
/*     */     
/*  69 */     return normal;
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
/*     */   public static String encodeURN(String publicId) {
/*  83 */     String urn = normalize(publicId);
/*     */     
/*  85 */     urn = stringReplace(urn, "%", "%25");
/*  86 */     urn = stringReplace(urn, ";", "%3B");
/*  87 */     urn = stringReplace(urn, "'", "%27");
/*  88 */     urn = stringReplace(urn, "?", "%3F");
/*  89 */     urn = stringReplace(urn, "#", "%23");
/*  90 */     urn = stringReplace(urn, "+", "%2B");
/*  91 */     urn = stringReplace(urn, " ", "+");
/*  92 */     urn = stringReplace(urn, "::", ";");
/*  93 */     urn = stringReplace(urn, ":", "%3A");
/*  94 */     urn = stringReplace(urn, "//", ":");
/*  95 */     urn = stringReplace(urn, "/", "%2F");
/*     */     
/*  97 */     return "urn:publicid:" + urn;
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
/*     */   public static String decodeURN(String urn) {
/* 111 */     String publicId = "";
/*     */     
/* 113 */     if (urn.startsWith("urn:publicid:")) {
/* 114 */       publicId = urn.substring(13);
/*     */     } else {
/* 116 */       return urn;
/*     */     } 
/*     */     
/* 119 */     publicId = stringReplace(publicId, "%2F", "/");
/* 120 */     publicId = stringReplace(publicId, ":", "//");
/* 121 */     publicId = stringReplace(publicId, "%3A", ":");
/* 122 */     publicId = stringReplace(publicId, ";", "::");
/* 123 */     publicId = stringReplace(publicId, "+", " ");
/* 124 */     publicId = stringReplace(publicId, "%2B", "+");
/* 125 */     publicId = stringReplace(publicId, "%23", "#");
/* 126 */     publicId = stringReplace(publicId, "%3F", "?");
/* 127 */     publicId = stringReplace(publicId, "%27", "'");
/* 128 */     publicId = stringReplace(publicId, "%3B", ";");
/* 129 */     publicId = stringReplace(publicId, "%25", "%");
/*     */     
/* 131 */     return publicId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String stringReplace(String str, String oldStr, String newStr) {
/* 142 */     String result = "";
/* 143 */     int pos = str.indexOf(oldStr);
/*     */ 
/*     */ 
/*     */     
/* 147 */     while (pos >= 0) {
/*     */       
/* 149 */       result = result + str.substring(0, pos);
/* 150 */       result = result + newStr;
/* 151 */       str = str.substring(pos + 1);
/*     */       
/* 153 */       pos = str.indexOf(oldStr);
/*     */     } 
/*     */     
/* 156 */     return result + str;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\helpers\PublicId.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */