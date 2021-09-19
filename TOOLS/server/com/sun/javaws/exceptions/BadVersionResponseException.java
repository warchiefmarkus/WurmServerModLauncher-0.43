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
/*    */ public class BadVersionResponseException
/*    */   extends DownloadException
/*    */ {
/*    */   private String _responseVersionID;
/*    */   
/*    */   public BadVersionResponseException(URL paramURL, String paramString1, String paramString2) {
/* 25 */     super(paramURL, paramString1);
/* 26 */     this._responseVersionID = paramString2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 31 */     return ResourceManager.getString("launch.error.badversionresponse", getResourceString(), this._responseVersionID);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\BadVersionResponseException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */