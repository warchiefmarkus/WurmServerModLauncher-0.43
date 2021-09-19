/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ import com.sun.javaws.jnl.LaunchDesc;
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
/*    */ public class FailedDownloadingResourceException
/*    */   extends DownloadException
/*    */ {
/*    */   public FailedDownloadingResourceException(LaunchDesc paramLaunchDesc, URL paramURL, String paramString, Exception paramException) {
/* 23 */     super(paramLaunchDesc, paramURL, paramString, paramException);
/*    */   }
/*    */ 
/*    */   
/*    */   public FailedDownloadingResourceException(URL paramURL, String paramString, Exception paramException) {
/* 28 */     this(null, paramURL, paramString, paramException);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 33 */     return ResourceManager.getString("launch.error.failedloadingresource", getResourceString());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\FailedDownloadingResourceException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */