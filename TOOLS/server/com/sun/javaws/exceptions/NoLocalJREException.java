/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ import com.sun.javaws.jnl.LaunchDesc;
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
/*    */ public class NoLocalJREException
/*    */   extends JNLPException
/*    */ {
/*    */   private String _message;
/*    */   
/*    */   public NoLocalJREException(LaunchDesc paramLaunchDesc, String paramString, boolean paramBoolean) {
/* 21 */     super(ResourceManager.getString("launch.error.category.config"), paramLaunchDesc);
/*    */     
/* 23 */     if (paramBoolean) {
/* 24 */       this._message = ResourceManager.getString("launch.error.wont.download.jre", paramString);
/*    */     } else {
/*    */       
/* 27 */       this._message = ResourceManager.getString("launch.error.cant.download.jre", paramString);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 33 */     return this._message;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\NoLocalJREException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */