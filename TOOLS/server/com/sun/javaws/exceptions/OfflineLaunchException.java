/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OfflineLaunchException
/*    */   extends JNLPException
/*    */ {
/*    */   public OfflineLaunchException() {
/* 13 */     super(ResourceManager.getString("launch.error.category.download"));
/*    */   }
/*    */   
/*    */   public String getRealMessage() {
/* 17 */     return ResourceManager.getString("launch.error.offlinemissingresource");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\OfflineLaunchException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */