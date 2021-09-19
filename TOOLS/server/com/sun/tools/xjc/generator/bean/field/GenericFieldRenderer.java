/*    */ package com.sun.tools.xjc.generator.bean.field;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.bean.ClassOutlineImpl;
/*    */ import com.sun.tools.xjc.model.CPropertyInfo;
/*    */ import com.sun.tools.xjc.outline.FieldOutline;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class GenericFieldRenderer
/*    */   implements FieldRenderer
/*    */ {
/*    */   private Constructor constructor;
/*    */   
/*    */   public GenericFieldRenderer(Class fieldClass) {
/*    */     try {
/* 56 */       this.constructor = fieldClass.getDeclaredConstructor(new Class[] { ClassOutlineImpl.class, CPropertyInfo.class });
/* 57 */     } catch (NoSuchMethodException e) {
/* 58 */       throw new NoSuchMethodError(e.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public FieldOutline generate(ClassOutlineImpl context, CPropertyInfo prop) {
/*    */     try {
/* 64 */       return this.constructor.newInstance(new Object[] { context, prop });
/* 65 */     } catch (InstantiationException e) {
/* 66 */       throw new InstantiationError(e.getMessage());
/* 67 */     } catch (IllegalAccessException e) {
/* 68 */       throw new IllegalAccessError(e.getMessage());
/* 69 */     } catch (InvocationTargetException e) {
/* 70 */       Throwable t = e.getTargetException();
/* 71 */       if (t instanceof RuntimeException)
/* 72 */         throw (RuntimeException)t; 
/* 73 */       if (t instanceof Error) {
/* 74 */         throw (Error)t;
/*    */       }
/*    */       
/* 77 */       throw new AssertionError(t);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\field\GenericFieldRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */