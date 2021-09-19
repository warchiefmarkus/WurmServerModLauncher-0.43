/*    */ package com.sun.codemodel;
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
/*    */ public class JFieldVar
/*    */   extends JVar
/*    */ {
/* 34 */   private JDocComment jdoc = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final JDefinedClass owner;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   JFieldVar(JDefinedClass owner, JMods mods, JType type, String name, JExpression init) {
/* 52 */     super(mods, type, name, init);
/* 53 */     this.owner = owner;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void name(String name) {
/* 59 */     if (this.owner.fields.containsKey(name))
/* 60 */       throw new IllegalArgumentException("name " + name + " is already in use"); 
/* 61 */     String oldName = name();
/* 62 */     super.name(name);
/* 63 */     this.owner.fields.remove(oldName);
/* 64 */     this.owner.fields.put(name, this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JDocComment javadoc() {
/* 74 */     if (this.jdoc == null)
/* 75 */       this.jdoc = new JDocComment(this.owner.owner()); 
/* 76 */     return this.jdoc;
/*    */   }
/*    */   
/*    */   public void declare(JFormatter f) {
/* 80 */     if (this.jdoc != null)
/* 81 */       f.g(this.jdoc); 
/* 82 */     super.declare(f);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JFieldVar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */