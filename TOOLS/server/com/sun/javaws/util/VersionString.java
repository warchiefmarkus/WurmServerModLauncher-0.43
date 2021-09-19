/*    */ package com.sun.javaws.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VersionString
/*    */ {
/* 23 */   private ArrayList _versionIds = new ArrayList(); public VersionString(String paramString) {
/* 24 */     if (paramString != null) {
/* 25 */       StringTokenizer stringTokenizer = new StringTokenizer(paramString, " ", false);
/* 26 */       while (stringTokenizer.hasMoreElements())
/*    */       {
/* 28 */         this._versionIds.add(new VersionID(stringTokenizer.nextToken()));
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isSimpleVersion() {
/* 34 */     if (this._versionIds.size() == 1) {
/* 35 */       return ((VersionID)this._versionIds.get(0)).isSimpleVersion();
/*    */     }
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(VersionID paramVersionID) {
/* 42 */     for (byte b = 0; b < this._versionIds.size(); b++) {
/* 43 */       VersionID versionID = this._versionIds.get(b);
/* 44 */       boolean bool = versionID.match(paramVersionID);
/* 45 */       if (bool) return true; 
/*    */     } 
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(String paramString) {
/* 52 */     return contains(new VersionID(paramString));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsGreaterThan(VersionID paramVersionID) {
/* 57 */     for (byte b = 0; b < this._versionIds.size(); b++) {
/* 58 */       VersionID versionID = this._versionIds.get(b);
/* 59 */       boolean bool = versionID.isGreaterThan(paramVersionID);
/* 60 */       if (bool) return true; 
/*    */     } 
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsGreaterThan(String paramString) {
/* 67 */     return containsGreaterThan(new VersionID(paramString));
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean contains(String paramString1, String paramString2) {
/* 72 */     return (new VersionString(paramString1)).contains(paramString2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 77 */     StringBuffer stringBuffer = new StringBuffer();
/* 78 */     for (byte b = 0; b < this._versionIds.size(); b++) {
/* 79 */       stringBuffer.append(this._versionIds.get(b).toString());
/* 80 */       stringBuffer.append(' ');
/*    */     } 
/* 82 */     return stringBuffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\util\VersionString.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */