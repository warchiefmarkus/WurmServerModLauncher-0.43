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
/*    */ public class BadMimeTypeResponseException
/*    */   extends DownloadException
/*    */ {
/*    */   private String _mimeType;
/*    */   
/*    */   public BadMimeTypeResponseException(URL paramURL, String paramString1, String paramString2) {
/* 25 */     super(paramURL, paramString1);
/* 26 */     this._mimeType = paramString2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 31 */     return ResourceManager.getString("launch.error.badmimetyperesponse", getResourceString(), this._mimeType);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\BadMimeTypeResponseException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */