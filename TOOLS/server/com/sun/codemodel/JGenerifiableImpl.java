/*    */ package com.sun.codemodel;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ abstract class JGenerifiableImpl
/*    */   implements JGenerifiable, JDeclaration
/*    */ {
/* 34 */   private List<JTypeVar> typeVariables = null;
/*    */   
/*    */   protected abstract JCodeModel owner();
/*    */   
/*    */   public void declare(JFormatter f) {
/* 39 */     if (this.typeVariables != null) {
/* 40 */       f.p('<');
/* 41 */       for (int i = 0; i < this.typeVariables.size(); i++) {
/* 42 */         if (i != 0) f.p(','); 
/* 43 */         f.d(this.typeVariables.get(i));
/*    */       } 
/* 45 */       f.p('>');
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public JTypeVar generify(String name) {
/* 51 */     JTypeVar v = new JTypeVar(owner(), name);
/* 52 */     if (this.typeVariables == null)
/* 53 */       this.typeVariables = new ArrayList<JTypeVar>(3); 
/* 54 */     this.typeVariables.add(v);
/* 55 */     return v;
/*    */   }
/*    */   
/*    */   public JTypeVar generify(String name, Class bound) {
/* 59 */     return generify(name, owner().ref(bound));
/*    */   }
/*    */   
/*    */   public JTypeVar generify(String name, JClass bound) {
/* 63 */     return generify(name).bound(bound);
/*    */   }
/*    */   
/*    */   public JTypeVar[] typeParams() {
/* 67 */     if (this.typeVariables == null) {
/* 68 */       return JTypeVar.EMPTY_ARRAY;
/*    */     }
/* 70 */     return this.typeVariables.<JTypeVar>toArray(new JTypeVar[this.typeVariables.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JGenerifiableImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */