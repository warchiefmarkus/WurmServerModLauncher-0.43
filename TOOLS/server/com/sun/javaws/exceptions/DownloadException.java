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
/*    */ 
/*    */ 
/*    */ public class DownloadException
/*    */   extends JNLPException
/*    */ {
/*    */   private URL _location;
/*    */   private String _version;
/*    */   private String _message;
/*    */   
/*    */   public DownloadException(URL paramURL, String paramString) {
/* 29 */     this(null, paramURL, paramString, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected DownloadException(LaunchDesc paramLaunchDesc, URL paramURL, String paramString, Exception paramException) {
/* 36 */     super(ResourceManager.getString("launch.error.category.download"), paramLaunchDesc, paramException);
/* 37 */     this._location = paramURL;
/* 38 */     this._version = paramString;
/*    */   }
/*    */   
/* 41 */   public URL getLocation() { return this._location; } public String getVersion() {
/* 42 */     return this._version;
/*    */   }
/*    */   public String getResourceString() {
/* 45 */     String str = this._location.toString();
/* 46 */     if (this._version == null) {
/* 47 */       return ResourceManager.getString("launch.error.resourceID", str);
/*    */     }
/* 49 */     return ResourceManager.getString("launch.error.resourceID-version", str, this._version);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 54 */     return this._message;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\DownloadException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */