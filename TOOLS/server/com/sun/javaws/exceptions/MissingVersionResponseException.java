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
/*    */ public class MissingVersionResponseException
/*    */   extends DownloadException
/*    */ {
/*    */   public MissingVersionResponseException(URL paramURL, String paramString) {
/* 24 */     super(paramURL, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 29 */     return ResourceManager.getString("launch.error.missingversionresponse", getResourceString());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\MissingVersionResponseException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */