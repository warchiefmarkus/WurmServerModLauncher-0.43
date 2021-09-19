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
/*    */ public class UnsignedAccessViolationException
/*    */   extends JNLPException
/*    */ {
/*    */   URL _url;
/*    */   boolean _initial;
/*    */   
/*    */   public UnsignedAccessViolationException(LaunchDesc paramLaunchDesc, URL paramURL, boolean paramBoolean) {
/* 18 */     super(ResourceManager.getString("launch.error.category.security"), paramLaunchDesc);
/* 19 */     this._url = paramURL;
/* 20 */     this._initial = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 25 */     return ResourceManager.getString("launch.error.unsignedAccessViolation") + "\n" + ResourceManager.getString("launch.error.unsignedResource", this._url.toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBriefMessage() {
/* 33 */     if (this._initial) {
/* 34 */       return null;
/*    */     }
/* 36 */     return ResourceManager.getString("launcherrordialog.brief.continue");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\UnsignedAccessViolationException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */