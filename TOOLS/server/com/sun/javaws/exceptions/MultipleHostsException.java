/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MultipleHostsException
/*    */   extends JNLPException
/*    */ {
/*    */   public MultipleHostsException() {
/* 13 */     super(ResourceManager.getString("launch.error.category.security"));
/*    */   }
/*    */   
/*    */   public String getRealMessage() {
/* 17 */     return ResourceManager.getString("launch.error.multiplehostsreferences");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\MultipleHostsException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */