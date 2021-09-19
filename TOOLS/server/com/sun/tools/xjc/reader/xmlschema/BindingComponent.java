/*    */ package com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.Ring;
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
/*    */ public abstract class BindingComponent
/*    */ {
/*    */   protected BindingComponent() {
/* 48 */     Ring.add(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final ErrorReporter getErrorReporter() {
/* 58 */     return (ErrorReporter)Ring.get(ErrorReporter.class);
/*    */   }
/*    */   protected final ClassSelector getClassSelector() {
/* 61 */     return (ClassSelector)Ring.get(ClassSelector.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\BindingComponent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */