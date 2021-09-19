/*    */ package com.sun.tools.xjc.outline;
/*    */ 
/*    */ import com.sun.codemodel.JEnumConstant;
/*    */ import com.sun.tools.xjc.model.CEnumConstant;
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
/*    */ public abstract class EnumConstantOutline
/*    */ {
/*    */   public final CEnumConstant target;
/*    */   public final JEnumConstant constRef;
/*    */   
/*    */   protected EnumConstantOutline(CEnumConstant target, JEnumConstant constRef) {
/* 66 */     this.target = target;
/* 67 */     this.constRef = constRef;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\outline\EnumConstantOutline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */