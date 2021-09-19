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
/*    */ public class InvalidJarDiffException
/*    */   extends DownloadException
/*    */ {
/*    */   public InvalidJarDiffException(URL paramURL, String paramString, Exception paramException) {
/* 24 */     super(null, paramURL, paramString, paramException);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 29 */     return ResourceManager.getString("launch.error.invalidjardiff", getResourceString());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\InvalidJarDiffException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */