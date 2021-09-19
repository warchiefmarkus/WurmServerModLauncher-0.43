/*    */ package com.sun.tools.xjc.addon.sync;
/*    */ 
/*    */ import com.sun.codemodel.JMethod;
/*    */ import com.sun.tools.xjc.BadCommandLineException;
/*    */ import com.sun.tools.xjc.Options;
/*    */ import com.sun.tools.xjc.Plugin;
/*    */ import com.sun.tools.xjc.outline.ClassOutline;
/*    */ import com.sun.tools.xjc.outline.Outline;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.ErrorHandler;
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
/*    */ public class SynchronizedMethodAddOn
/*    */   extends Plugin
/*    */ {
/*    */   public String getOptionName() {
/* 58 */     return "Xsync-methods";
/*    */   }
/*    */   
/*    */   public String getUsage() {
/* 62 */     return "  -Xsync-methods     :  generate accessor methods with the 'synchronized' keyword";
/*    */   }
/*    */   
/*    */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/* 66 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler) {
/* 71 */     for (ClassOutline co : model.getClasses()) {
/* 72 */       augument(co);
/*    */     }
/* 74 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void augument(ClassOutline co) {
/* 81 */     for (JMethod m : co.implClass.methods())
/* 82 */       m.getMods().setSynchronized(true); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\addon\sync\SynchronizedMethodAddOn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */