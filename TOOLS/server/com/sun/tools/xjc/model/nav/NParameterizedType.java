/*    */ package com.sun.tools.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.outline.Aspect;
/*    */ import com.sun.tools.xjc.outline.Outline;
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
/*    */ final class NParameterizedType
/*    */   implements NClass
/*    */ {
/*    */   final NClass rawType;
/*    */   final NType[] args;
/*    */   
/*    */   NParameterizedType(NClass rawType, NType[] args) {
/* 54 */     this.rawType = rawType;
/* 55 */     this.args = args;
/* 56 */     assert args.length > 0;
/*    */   }
/*    */   
/*    */   public JClass toType(Outline o, Aspect aspect) {
/* 60 */     JClass r = this.rawType.toType(o, aspect);
/*    */     
/* 62 */     for (NType arg : this.args) {
/* 63 */       r = r.narrow(arg.toType(o, aspect).boxify());
/*    */     }
/* 65 */     return r;
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 69 */     return this.rawType.isAbstract();
/*    */   }
/*    */   
/*    */   public boolean isBoxedType() {
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String fullName() {
/* 78 */     StringBuilder buf = new StringBuilder();
/* 79 */     buf.append(this.rawType.fullName());
/* 80 */     buf.append('<');
/* 81 */     for (int i = 0; i < this.args.length; i++) {
/* 82 */       if (i != 0)
/* 83 */         buf.append(','); 
/* 84 */       buf.append(this.args[i].fullName());
/*    */     } 
/* 86 */     buf.append('>');
/* 87 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\nav\NParameterizedType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */