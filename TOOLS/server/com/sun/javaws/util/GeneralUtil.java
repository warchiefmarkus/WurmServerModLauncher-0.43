/*     */ package com.sun.javaws.util;
/*     */ 
/*     */ import java.awt.Frame;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GeneralUtil
/*     */ {
/*     */   public static boolean prefixMatchStringList(String[] paramArrayOfString, String paramString) {
/*  23 */     if (paramArrayOfString == null) return true;
/*     */     
/*  25 */     if (paramString == null) return false; 
/*  26 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  27 */       if (paramString.startsWith(paramArrayOfString[b])) return true; 
/*     */     } 
/*  29 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] getStringList(String paramString) {
/*  34 */     if (paramString == null) return null; 
/*  35 */     ArrayList arrayList = new ArrayList();
/*  36 */     byte b = 0;
/*  37 */     int i = paramString.length();
/*  38 */     StringBuffer stringBuffer = null;
/*  39 */     while (b < i) {
/*  40 */       char c = paramString.charAt(b);
/*  41 */       if (c == ' ') {
/*     */         
/*  43 */         if (stringBuffer != null) {
/*  44 */           arrayList.add(stringBuffer.toString());
/*  45 */           stringBuffer = null;
/*     */         } 
/*  47 */       } else if (c == '\\') {
/*     */         
/*  49 */         if (b + 1 < i) {
/*  50 */           c = paramString.charAt(++b);
/*  51 */           if (stringBuffer == null) stringBuffer = new StringBuffer(); 
/*  52 */           stringBuffer.append(c);
/*     */         } 
/*     */       } else {
/*  55 */         if (stringBuffer == null) stringBuffer = new StringBuffer(); 
/*  56 */         stringBuffer.append(c);
/*     */       } 
/*  58 */       b++;
/*     */     } 
/*     */     
/*  61 */     if (stringBuffer != null) {
/*  62 */       arrayList.add(stringBuffer.toString());
/*     */     }
/*  64 */     if (arrayList.size() == 0) return null; 
/*  65 */     String[] arrayOfString = new String[arrayList.size()];
/*  66 */     return arrayList.<String>toArray(arrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matchLocale(String[] paramArrayOfString, Locale paramLocale) {
/*  72 */     if (paramArrayOfString == null) return true; 
/*  73 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  74 */       if (matchLocale(paramArrayOfString[b], paramLocale)) return true; 
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean matchLocale(String paramString, Locale paramLocale) {
/*  81 */     if (paramString == null || paramString.length() == 0) return true;
/*     */ 
/*     */     
/*  84 */     String str1 = "";
/*  85 */     String str2 = "";
/*  86 */     String str3 = "";
/*     */ 
/*     */     
/*  89 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString, "_", false);
/*  90 */     if (stringTokenizer.hasMoreElements() && paramLocale.getLanguage().length() > 0) {
/*  91 */       str1 = stringTokenizer.nextToken();
/*  92 */       if (!str1.equalsIgnoreCase(paramLocale.getLanguage())) return false; 
/*     */     } 
/*  94 */     if (stringTokenizer.hasMoreElements() && paramLocale.getCountry().length() > 0) {
/*  95 */       str2 = stringTokenizer.nextToken();
/*  96 */       if (!str2.equalsIgnoreCase(paramLocale.getCountry())) return false; 
/*     */     } 
/*  98 */     if (stringTokenizer.hasMoreElements() && paramLocale.getVariant().length() > 0) {
/*  99 */       str3 = stringTokenizer.nextToken();
/* 100 */       if (!str3.equalsIgnoreCase(paramLocale.getVariant())) return false;
/*     */     
/*     */     } 
/* 103 */     return true;
/*     */   }
/*     */   
/*     */   public static long heapValToLong(String paramString) {
/* 107 */     if (paramString == null) return -1L; 
/* 108 */     long l1 = 1L;
/* 109 */     if (paramString.toLowerCase().lastIndexOf('m') != -1) {
/*     */       
/* 111 */       l1 = 1048576L;
/* 112 */       paramString = paramString.substring(0, paramString.length() - 1);
/* 113 */     } else if (paramString.toLowerCase().lastIndexOf('k') != -1) {
/*     */       
/* 115 */       l1 = 1024L;
/* 116 */       paramString = paramString.substring(0, paramString.length() - 1);
/*     */     } 
/* 118 */     long l2 = -1L;
/*     */     try {
/* 120 */       l2 = Long.parseLong(paramString);
/* 121 */       l2 *= l1;
/* 122 */     } catch (NumberFormatException numberFormatException) {
/* 123 */       l2 = -1L;
/*     */     } 
/* 125 */     return l2;
/*     */   }
/*     */   
/*     */   public static Frame getActiveTopLevelFrame() {
/* 129 */     Frame[] arrayOfFrame = Frame.getFrames();
/* 130 */     byte b = -1;
/* 131 */     if (arrayOfFrame == null) return null; 
/* 132 */     for (byte b1 = 0; b1 < arrayOfFrame.length; b1++) {
/* 133 */       if (arrayOfFrame[b1].getFocusOwner() != null) {
/* 134 */         b = b1;
/*     */       }
/*     */     } 
/* 137 */     return (b >= 0) ? arrayOfFrame[b] : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\util\GeneralUtil.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */