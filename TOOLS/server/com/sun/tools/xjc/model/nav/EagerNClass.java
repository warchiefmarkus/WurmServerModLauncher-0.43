/*    */ package com.sun.tools.xjc.model.nav;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import com.sun.codemodel.JType;
/*    */ import com.sun.tools.xjc.outline.Aspect;
/*    */ import com.sun.tools.xjc.outline.Outline;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.HashSet;
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
/*    */ public class EagerNClass
/*    */   extends EagerNType
/*    */   implements NClass
/*    */ {
/*    */   final Class c;
/*    */   
/*    */   public EagerNClass(Class type) {
/* 54 */     super(type);
/* 55 */     this.c = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBoxedType() {
/* 60 */     return boxedTypes.contains(this.c);
/*    */   }
/*    */ 
/*    */   
/*    */   public JClass toType(Outline o, Aspect aspect) {
/* 65 */     return o.getCodeModel().ref(this.c);
/*    */   }
/*    */   
/*    */   public boolean isAbstract() {
/* 69 */     return Modifier.isAbstract(this.c.getModifiers());
/*    */   }
/*    */   
/* 72 */   private static final Set<Class> boxedTypes = (Set)new HashSet<Class<?>>();
/*    */   
/*    */   static {
/* 75 */     boxedTypes.add(Boolean.class);
/* 76 */     boxedTypes.add(Character.class);
/* 77 */     boxedTypes.add(Byte.class);
/* 78 */     boxedTypes.add(Short.class);
/* 79 */     boxedTypes.add(Integer.class);
/* 80 */     boxedTypes.add(Long.class);
/* 81 */     boxedTypes.add(Float.class);
/* 82 */     boxedTypes.add(Double.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\nav\EagerNClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */