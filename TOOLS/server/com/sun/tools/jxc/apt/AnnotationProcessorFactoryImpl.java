/*    */ package com.sun.tools.jxc.apt;
/*    */ 
/*    */ import com.sun.mirror.apt.AnnotationProcessor;
/*    */ import com.sun.mirror.apt.AnnotationProcessorEnvironment;
/*    */ import com.sun.mirror.apt.AnnotationProcessorFactory;
/*    */ import com.sun.mirror.declaration.AnnotationTypeDeclaration;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotationProcessorFactoryImpl
/*    */   implements AnnotationProcessorFactory
/*    */ {
/*    */   public Collection<String> supportedOptions() {
/* 56 */     return Arrays.asList(new String[] { "-Ajaxb.config" });
/*    */   }
/*    */   
/*    */   public Collection<String> supportedAnnotationTypes() {
/* 60 */     return Arrays.asList(new String[] { "javax.xml.bind.annotation.*" });
/*    */   }
/*    */   
/*    */   public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
/* 64 */     return new AnnotationParser(atds, env);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\apt\AnnotationProcessorFactoryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */