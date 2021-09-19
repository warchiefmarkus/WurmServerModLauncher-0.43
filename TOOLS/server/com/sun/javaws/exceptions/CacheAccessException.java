/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CacheAccessException
/*    */   extends JNLPException
/*    */ {
/*    */   private String _message;
/*    */   
/*    */   public CacheAccessException(boolean paramBoolean) {
/* 18 */     super(ResourceManager.getString("launch.error.category.config"));
/*    */     
/* 20 */     if (paramBoolean) {
/* 21 */       this._message = ResourceManager.getString("launch.error.cant.access.system.cache");
/*    */     } else {
/*    */       
/* 24 */       this._message = ResourceManager.getString("launch.error.cant.access.user.cache");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 30 */     return this._message;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\CacheAccessException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */