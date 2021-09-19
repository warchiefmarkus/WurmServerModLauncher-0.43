/*    */ package com.sun.tools.xjc.model;
/*    */ 
/*    */ import com.sun.codemodel.JCodeModel;
/*    */ import com.sun.codemodel.JType;
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
/*    */ public class SymbolSpace
/*    */ {
/*    */   private JType type;
/*    */   private final JCodeModel codeModel;
/*    */   
/*    */   public SymbolSpace(JCodeModel _codeModel) {
/* 65 */     this.codeModel = _codeModel;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JType getType() {
/* 77 */     if (this.type == null) return (JType)this.codeModel.ref(Object.class); 
/* 78 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(JType _type) {
/* 82 */     if (this.type == null)
/* 83 */       this.type = _type; 
/*    */   }
/*    */   
/*    */   public String toString() {
/* 87 */     if (this.type == null) return "undetermined"; 
/* 88 */     return this.type.name();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\SymbolSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */