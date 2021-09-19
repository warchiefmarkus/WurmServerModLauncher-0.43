/*     */ package com.sun.javaws.util;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionID
/*     */   implements Comparable
/*     */ {
/*     */   private String[] _tuple;
/*     */   private boolean _usePrefixMatch = false;
/*     */   private boolean _useGreaterThan = false;
/*     */   private boolean _isCompound = false;
/*     */   private VersionID _rest;
/*     */   
/*     */   public VersionID(String paramString) {
/*  34 */     if (paramString == null && paramString.length() == 0) {
/*  35 */       this._tuple = new String[0];
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  40 */     int i = paramString.indexOf("&");
/*  41 */     if (i >= 0) {
/*  42 */       this._isCompound = true;
/*  43 */       VersionID versionID = new VersionID(paramString.substring(0, i));
/*  44 */       this._rest = new VersionID(paramString.substring(i + 1));
/*  45 */       this._tuple = versionID._tuple;
/*  46 */       this._usePrefixMatch = versionID._usePrefixMatch;
/*  47 */       this._useGreaterThan = versionID._useGreaterThan;
/*     */     } else {
/*     */       
/*  50 */       if (paramString.endsWith("+")) {
/*  51 */         this._useGreaterThan = true;
/*  52 */         paramString = paramString.substring(0, paramString.length() - 1);
/*  53 */       } else if (paramString.endsWith("*")) {
/*  54 */         this._usePrefixMatch = true;
/*  55 */         paramString = paramString.substring(0, paramString.length() - 1);
/*     */       } 
/*     */       
/*  58 */       ArrayList arrayList = new ArrayList();
/*  59 */       int j = 0;
/*  60 */       for (byte b = 0; b < paramString.length(); b++) {
/*     */         
/*  62 */         if (".-_".indexOf(paramString.charAt(b)) != -1) {
/*  63 */           if (j < b) {
/*  64 */             String str = paramString.substring(j, b);
/*  65 */             arrayList.add(str);
/*     */           } 
/*  67 */           j = b + 1;
/*     */         } 
/*     */       } 
/*  70 */       if (j < paramString.length()) {
/*  71 */         arrayList.add(paramString.substring(j, paramString.length()));
/*     */       }
/*  73 */       this._tuple = new String[arrayList.size()];
/*  74 */       this._tuple = arrayList.<String>toArray(this._tuple);
/*     */     } 
/*  76 */     Trace.println("Created version ID: " + this, TraceLevel.NETWORK);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSimpleVersion() {
/*  81 */     return (!this._useGreaterThan && !this._usePrefixMatch && !this._isCompound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(VersionID paramVersionID) {
/*  90 */     if (this._isCompound && 
/*  91 */       !this._rest.match(paramVersionID)) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     return this._usePrefixMatch ? isPrefixMatchTuple(paramVersionID) : (this._useGreaterThan ? paramVersionID.isGreaterThanOrEqualTuple(this) : matchTuple(paramVersionID));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 102 */     if (matchTuple(paramObject)) {
/* 103 */       VersionID versionID = (VersionID)paramObject;
/* 104 */       if ((this._rest == null || this._rest.equals(versionID._rest)) && 
/* 105 */         this._useGreaterThan == versionID._useGreaterThan && this._usePrefixMatch == versionID._usePrefixMatch)
/*     */       {
/* 107 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean matchTuple(Object paramObject) {
/* 117 */     if (paramObject == null || !(paramObject instanceof VersionID)) return false; 
/* 118 */     VersionID versionID = (VersionID)paramObject;
/*     */ 
/*     */     
/* 121 */     String[] arrayOfString1 = normalize(this._tuple, versionID._tuple.length);
/* 122 */     String[] arrayOfString2 = normalize(versionID._tuple, this._tuple.length);
/*     */ 
/*     */     
/* 125 */     for (byte b = 0; b < arrayOfString1.length; b++) {
/* 126 */       Object object1 = getValueAsObject(arrayOfString1[b]);
/* 127 */       Object object2 = getValueAsObject(arrayOfString2[b]);
/* 128 */       if (!object1.equals(object2)) return false; 
/*     */     } 
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   private Object getValueAsObject(String paramString) {
/* 134 */     if (paramString.length() > 0 && paramString.charAt(0) != '-') {
/* 135 */       try { return Integer.valueOf(paramString); }
/* 136 */       catch (NumberFormatException numberFormatException) {}
/*     */     }
/* 138 */     return paramString;
/*     */   }
/*     */   
/*     */   public boolean isGreaterThan(VersionID paramVersionID) {
/* 142 */     return isGreaterThanOrEqualHelper(paramVersionID, false, true);
/*     */   }
/*     */   
/*     */   public boolean isGreaterThanOrEqual(VersionID paramVersionID) {
/* 146 */     return isGreaterThanOrEqualHelper(paramVersionID, true, true);
/*     */   }
/*     */   
/*     */   private boolean isGreaterThanOrEqualTuple(VersionID paramVersionID) {
/* 150 */     return isGreaterThanOrEqualHelper(paramVersionID, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isGreaterThanOrEqualHelper(VersionID paramVersionID, boolean paramBoolean1, boolean paramBoolean2) {
/* 157 */     if (paramBoolean2 && this._isCompound && 
/* 158 */       !this._rest.isGreaterThanOrEqualHelper(paramVersionID, paramBoolean1, true)) {
/* 159 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 163 */     String[] arrayOfString1 = normalize(this._tuple, paramVersionID._tuple.length);
/* 164 */     String[] arrayOfString2 = normalize(paramVersionID._tuple, this._tuple.length);
/*     */     
/* 166 */     for (byte b = 0; b < arrayOfString1.length; ) {
/*     */       
/* 168 */       Object object1 = getValueAsObject(arrayOfString1[b]);
/* 169 */       Object object2 = getValueAsObject(arrayOfString2[b]);
/* 170 */       if (object1.equals(object2)) {
/*     */         b++; continue;
/*     */       } 
/* 173 */       if (object1 instanceof Integer && object2 instanceof Integer) {
/* 174 */         return (((Integer)object1).intValue() > ((Integer)object2).intValue());
/*     */       }
/* 176 */       String str1 = arrayOfString1[b].toString();
/* 177 */       String str2 = arrayOfString2[b].toString();
/* 178 */       return (str1.compareTo(str2) > 0);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     return paramBoolean1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isPrefixMatchTuple(VersionID paramVersionID) {
/* 191 */     String[] arrayOfString = normalize(paramVersionID._tuple, this._tuple.length);
/*     */     
/* 193 */     for (byte b = 0; b < this._tuple.length; ) {
/* 194 */       String str1 = this._tuple[b];
/* 195 */       String str2 = arrayOfString[b];
/* 196 */       if (str1.equals(str2)) {
/*     */         b++;
/*     */         continue;
/*     */       } 
/* 200 */       return false;
/*     */     } 
/*     */     
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private String[] normalize(String[] paramArrayOfString, int paramInt) {
/* 208 */     if (paramArrayOfString.length < paramInt) {
/*     */       
/* 210 */       String[] arrayOfString = new String[paramInt];
/* 211 */       System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
/* 212 */       Arrays.fill((Object[])arrayOfString, paramArrayOfString.length, arrayOfString.length, "0");
/* 213 */       return arrayOfString;
/*     */     } 
/* 215 */     return paramArrayOfString;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Object paramObject) {
/* 220 */     if (paramObject == null || !(paramObject instanceof VersionID)) return -1; 
/* 221 */     VersionID versionID = (VersionID)paramObject;
/* 222 */     return equals(versionID) ? 0 : (isGreaterThanOrEqual(versionID) ? 1 : -1);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 226 */     StringBuffer stringBuffer = new StringBuffer();
/* 227 */     for (byte b = 0; b < this._tuple.length - 1; b++) {
/* 228 */       stringBuffer.append(this._tuple[b]);
/* 229 */       stringBuffer.append('.');
/*     */     } 
/* 231 */     if (this._tuple.length > 0) stringBuffer.append(this._tuple[this._tuple.length - 1]); 
/* 232 */     if (this._useGreaterThan) stringBuffer.append('+'); 
/* 233 */     if (this._usePrefixMatch) stringBuffer.append('*'); 
/* 234 */     if (this._isCompound) { stringBuffer.append("&"); stringBuffer.append(this._rest); }
/* 235 */      return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\util\VersionID.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */