/*    */ package com.sun.javaws.util;
/*    */ 
/*    */ import com.sun.deploy.util.DialogListener;
/*    */ import com.sun.javaws.SplashScreen;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
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
/*    */ public final class JavawsDialogListener
/*    */   implements DialogListener
/*    */ {
/*    */   public void beforeShow() {
/* 21 */     AccessController.doPrivileged(new PrivilegedAction(this) {
/*    */           public Object run() {
/* 23 */             SplashScreen.hide();
/* 24 */             return null;
/*    */           }
/*    */           
/*    */           private final JavawsDialogListener this$0;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\util\JavawsDialogListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */