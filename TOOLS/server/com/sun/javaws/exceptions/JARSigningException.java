/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ import java.net.URL;
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
/*    */ public class JARSigningException
/*    */   extends DownloadException
/*    */ {
/*    */   private int _code;
/*    */   private String _missingEntry;
/*    */   public static final int MULTIPLE_CERTIFICATES = 0;
/*    */   public static final int MULTIPLE_SIGNERS = 1;
/*    */   public static final int BAD_SIGNING = 2;
/*    */   public static final int UNSIGNED_FILE = 3;
/*    */   public static final int MISSING_ENTRY = 4;
/*    */   
/*    */   public JARSigningException(URL paramURL, String paramString, int paramInt) {
/* 32 */     super(null, paramURL, paramString, null);
/* 33 */     this._code = paramInt;
/*    */   }
/*    */   
/*    */   public JARSigningException(URL paramURL, String paramString1, int paramInt, String paramString2) {
/* 37 */     super(null, paramURL, paramString1, null);
/* 38 */     this._code = paramInt;
/* 39 */     this._missingEntry = paramString2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JARSigningException(URL paramURL, String paramString, int paramInt, Exception paramException) {
/* 45 */     super(null, paramURL, paramString, paramException);
/* 46 */     this._code = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 51 */     switch (this._code) { case 0:
/* 52 */         return ResourceManager.getString("launch.error.jarsigning-multicerts", getResourceString());
/* 53 */       case 1: return ResourceManager.getString("launch.error.jarsigning-multisigners", getResourceString());
/* 54 */       case 2: return ResourceManager.getString("launch.error.jarsigning-badsigning", getResourceString());
/* 55 */       case 3: return ResourceManager.getString("launch.error.jarsigning-unsignedfile", getResourceString());
/* 56 */       case 4: return ResourceManager.getString("launch.error.jarsigning-missingentry", getResourceString()) + "\n" + ResourceManager.getString("launch.error.jarsigning-missingentryname", this._missingEntry); }
/*    */     
/* 58 */     return "<error>";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\JARSigningException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */