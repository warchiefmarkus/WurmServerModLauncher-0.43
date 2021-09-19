/*    */ package com.sun.tools.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.tools.xjc.model.CElementInfo;
/*    */ import com.sun.tools.xjc.model.Model;
/*    */ import com.sun.tools.xjc.outline.Aspect;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class PublicObjectFactoryGenerator
/*    */   extends ObjectFactoryGeneratorImpl
/*    */ {
/*    */   public PublicObjectFactoryGenerator(BeanGenerator outline, Model model, JPackage targetPackage) {
/* 51 */     super(outline, model, targetPackage);
/*    */   }
/*    */   
/*    */   void populate(CElementInfo ei) {
/* 55 */     populate(ei, Aspect.IMPLEMENTATION, Aspect.EXPOSED);
/*    */   }
/*    */   
/*    */   void populate(ClassOutlineImpl cc) {
/* 59 */     populate(cc, (JClass)cc.ref);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\PublicObjectFactoryGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */