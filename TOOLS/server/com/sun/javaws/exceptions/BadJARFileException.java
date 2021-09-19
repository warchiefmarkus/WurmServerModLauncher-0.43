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
/*    */ public class BadJARFileException
/*    */   extends DownloadException
/*    */ {
/*    */   public BadJARFileException(URL paramURL, String paramString, Exception paramException) {
/* 19 */     super(null, paramURL, paramString, paramException);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 24 */     return ResourceManager.getString("launch.error.badjarfile", getResourceString());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\BadJARFileException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */