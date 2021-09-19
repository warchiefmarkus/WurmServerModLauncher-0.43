/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NativeLibViolationException
/*    */   extends JNLPException
/*    */ {
/*    */   public NativeLibViolationException() {
/* 13 */     super(ResourceManager.getString("launch.error.category.security"));
/*    */   }
/*    */   
/*    */   public String getRealMessage() {
/* 17 */     return ResourceManager.getString("launch.error.nativelibviolation");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\NativeLibViolationException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */