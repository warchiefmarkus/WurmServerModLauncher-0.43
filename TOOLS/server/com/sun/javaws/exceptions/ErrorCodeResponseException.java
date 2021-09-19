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
/*    */ 
/*    */ public class ErrorCodeResponseException
/*    */   extends DownloadException
/*    */ {
/*    */   private String _errorLine;
/*    */   private int _errorCode;
/*    */   private boolean _jreDownload;
/*    */   public static final int ERR_10_NO_RESOURCE = 10;
/*    */   public static final int ERR_11_NO_VERSION = 11;
/*    */   public static final int ERR_20_UNSUP_OS = 20;
/*    */   public static final int ERR_21_UNSUP_ARCH = 21;
/*    */   public static final int ERR_22_UNSUP_LOCALE = 22;
/*    */   public static final int ERR_23_UNSUP_JRE = 23;
/*    */   public static final int ERR_99_UNKNOWN = 99;
/*    */   
/*    */   public ErrorCodeResponseException(URL paramURL, String paramString1, String paramString2) {
/* 36 */     super(paramURL, paramString1);
/* 37 */     this._errorLine = paramString2;
/* 38 */     this._jreDownload = false;
/*    */ 
/*    */     
/* 41 */     this._errorCode = 99;
/* 42 */     if (this._errorLine != null)
/*    */       try {
/* 44 */         int i = this._errorLine.indexOf(' ');
/* 45 */         if (i != -1) this._errorCode = Integer.parseInt(this._errorLine.substring(0, i)); 
/* 46 */       } catch (NumberFormatException numberFormatException) {
/* 47 */         this._errorCode = 99;
/*    */       }  
/*    */   }
/*    */   
/*    */   public void setJreDownload(boolean paramBoolean) {
/* 52 */     this._jreDownload = paramBoolean;
/*    */   }
/*    */   public int getErrorCode() {
/* 55 */     return this._errorCode;
/*    */   }
/*    */   
/*    */   public String getRealMessage() {
/* 59 */     String str = this._jreDownload ? ResourceManager.getString("launch.error.noJre") : "";
/* 60 */     if (this._errorCode != 99) {
/* 61 */       return str + ResourceManager.getString("launch.error.errorcoderesponse-known", getResourceString(), this._errorCode, this._errorLine);
/*    */     }
/* 63 */     return str + ResourceManager.getString("launch.error.errorcoderesponse-unknown", getResourceString());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\ErrorCodeResponseException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */