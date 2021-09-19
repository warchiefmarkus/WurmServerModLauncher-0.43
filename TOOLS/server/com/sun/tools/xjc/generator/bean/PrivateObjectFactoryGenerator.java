/*    */ package com.sun.tools.xjc.generator.bean;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JPackage;
/*    */ import com.sun.codemodel.JResourceFile;
/*    */ import com.sun.codemodel.fmt.JPropertyFile;
/*    */ import com.sun.tools.xjc.model.CElementInfo;
/*    */ import com.sun.tools.xjc.model.Model;
/*    */ import com.sun.tools.xjc.outline.Aspect;
/*    */ import com.sun.tools.xjc.runtime.JAXBContextFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class PrivateObjectFactoryGenerator
/*    */   extends ObjectFactoryGeneratorImpl
/*    */ {
/*    */   public PrivateObjectFactoryGenerator(BeanGenerator outline, Model model, JPackage targetPackage) {
/* 60 */     super(outline, model, targetPackage.subPackage("impl"));
/*    */     
/* 62 */     JPackage implPkg = targetPackage.subPackage("impl");
/*    */ 
/*    */     
/* 65 */     JClass factory = outline.generateStaticClass(JAXBContextFactory.class, implPkg);
/*    */ 
/*    */     
/* 68 */     JPropertyFile jaxbProperties = new JPropertyFile("jaxb.properties");
/* 69 */     targetPackage.addResourceFile((JResourceFile)jaxbProperties);
/* 70 */     jaxbProperties.add("javax.xml.bind.context.factory", factory.fullName());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void populate(CElementInfo ei) {
/* 76 */     populate(ei, Aspect.IMPLEMENTATION, Aspect.IMPLEMENTATION);
/*    */   }
/*    */   
/*    */   void populate(ClassOutlineImpl cc) {
/* 80 */     populate(cc, cc.implRef);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\PrivateObjectFactoryGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */