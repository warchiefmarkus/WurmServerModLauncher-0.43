/*    */ package com.sun.tools.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.JDefinedClass;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.tools.xjc.model.CElementInfo;
/*    */ import com.sun.tools.xjc.model.Model;
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
/*    */ public final class DualObjectFactoryGenerator
/*    */   extends ObjectFactoryGenerator
/*    */ {
/*    */   public final ObjectFactoryGenerator publicOFG;
/*    */   public final ObjectFactoryGenerator privateOFG;
/*    */   
/*    */   DualObjectFactoryGenerator(BeanGenerator outline, Model model, JPackage targetPackage) {
/* 62 */     this.publicOFG = new PublicObjectFactoryGenerator(outline, model, targetPackage);
/* 63 */     this.privateOFG = new PrivateObjectFactoryGenerator(outline, model, targetPackage);
/*    */ 
/*    */     
/* 66 */     this.publicOFG.getObjectFactory().field(28, Void.class, "_useJAXBProperties", JExpr._null());
/*    */   }
/*    */ 
/*    */   
/*    */   void populate(CElementInfo ei) {
/* 71 */     this.publicOFG.populate(ei);
/* 72 */     this.privateOFG.populate(ei);
/*    */   }
/*    */   
/*    */   void populate(ClassOutlineImpl cc) {
/* 76 */     this.publicOFG.populate(cc);
/* 77 */     this.privateOFG.populate(cc);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JDefinedClass getObjectFactory() {
/* 84 */     return this.privateOFG.getObjectFactory();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\DualObjectFactoryGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */