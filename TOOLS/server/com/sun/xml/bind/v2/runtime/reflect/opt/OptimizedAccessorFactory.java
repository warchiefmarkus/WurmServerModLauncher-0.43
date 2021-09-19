/*     */ package com.sun.xml.bind.v2.runtime.reflect.opt;
/*     */ 
/*     */ import com.sun.xml.bind.Util;
/*     */ import com.sun.xml.bind.v2.bytecode.ClassTailor;
/*     */ import com.sun.xml.bind.v2.runtime.RuntimeUtil;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OptimizedAccessorFactory
/*     */ {
/*  58 */   private static final Logger logger = Util.getClassLogger();
/*     */   
/*     */   private static final String fieldTemplateName;
/*     */   
/*     */   private static final String methodTemplateName;
/*     */   
/*     */   static {
/*  65 */     String s = FieldAccessor_Byte.class.getName();
/*  66 */     fieldTemplateName = s.substring(0, s.length() - "Byte".length()).replace('.', '/');
/*     */     
/*  68 */     s = MethodAccessor_Byte.class.getName();
/*  69 */     methodTemplateName = s.substring(0, s.length() - "Byte".length()).replace('.', '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final <B, V> Accessor<B, V> get(Method getter, Method setter) {
/*     */     Class<?> opt;
/*  80 */     if ((getter.getParameterTypes()).length != 0)
/*  81 */       return null; 
/*  82 */     Class<?>[] sparams = setter.getParameterTypes();
/*  83 */     if (sparams.length != 1)
/*  84 */       return null; 
/*  85 */     if (sparams[0] != getter.getReturnType())
/*  86 */       return null; 
/*  87 */     if (setter.getReturnType() != void.class)
/*  88 */       return null; 
/*  89 */     if (getter.getDeclaringClass() != setter.getDeclaringClass())
/*  90 */       return null; 
/*  91 */     if (Modifier.isPrivate(getter.getModifiers()) || Modifier.isPrivate(setter.getModifiers()))
/*     */     {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     Class<?> t = sparams[0];
/*  97 */     String typeName = t.getName().replace('.', '_');
/*     */     
/*  99 */     String newClassName = ClassTailor.toVMClassName(getter.getDeclaringClass()) + "$JaxbAccessorM_" + getter.getName() + '_' + setter.getName() + '_' + typeName;
/*     */ 
/*     */ 
/*     */     
/* 103 */     if (t.isPrimitive()) {
/* 104 */       opt = AccessorInjector.prepare(getter.getDeclaringClass(), methodTemplateName + ((Class)RuntimeUtil.primitiveToBox.get(t)).getSimpleName(), newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(getter.getDeclaringClass()), "get_" + t.getName(), getter.getName(), "set_" + t.getName(), setter.getName() });
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 114 */       opt = AccessorInjector.prepare(getter.getDeclaringClass(), methodTemplateName + "Ref", newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(getter.getDeclaringClass()), ClassTailor.toVMClassName(Ref.class), ClassTailor.toVMClassName(t), "()" + ClassTailor.toVMTypeName(Ref.class), "()" + ClassTailor.toVMTypeName(t), '(' + ClassTailor.toVMTypeName(Ref.class) + ")V", '(' + ClassTailor.toVMTypeName(t) + ")V", "get_ref", getter.getName(), "set_ref", setter.getName() });
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     if (opt == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     Accessor<B, V> acc = instanciate(opt);
/* 134 */     if (acc != null)
/* 135 */       logger.log(Level.FINE, "Using optimized Accessor for " + getter + " and " + setter); 
/* 136 */     return acc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final <B, V> Accessor<B, V> get(Field field) {
/*     */     Class<?> opt;
/* 147 */     int mods = field.getModifiers();
/* 148 */     if (Modifier.isPrivate(mods) || Modifier.isFinal(mods))
/*     */     {
/* 150 */       return null;
/*     */     }
/* 152 */     String newClassName = ClassTailor.toVMClassName(field.getDeclaringClass()) + "$JaxbAccessorF_" + field.getName();
/*     */ 
/*     */ 
/*     */     
/* 156 */     if (field.getType().isPrimitive()) {
/* 157 */       opt = AccessorInjector.prepare(field.getDeclaringClass(), fieldTemplateName + ((Class)RuntimeUtil.primitiveToBox.get(field.getType())).getSimpleName(), newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(field.getDeclaringClass()), "f_" + field.getType().getName(), field.getName() });
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 165 */       opt = AccessorInjector.prepare(field.getDeclaringClass(), fieldTemplateName + "Ref", newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(field.getDeclaringClass()), ClassTailor.toVMClassName(Ref.class), ClassTailor.toVMClassName(field.getType()), ClassTailor.toVMTypeName(Ref.class), ClassTailor.toVMTypeName(field.getType()), "f_ref", field.getName() });
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     if (opt == null) {
/* 178 */       return null;
/*     */     }
/* 180 */     Accessor<B, V> acc = instanciate(opt);
/* 181 */     if (acc != null)
/* 182 */       logger.log(Level.FINE, "Using optimized Accessor for " + field); 
/* 183 */     return acc;
/*     */   }
/*     */   
/*     */   private static <B, V> Accessor<B, V> instanciate(Class<Accessor<B, V>> opt) {
/*     */     try {
/* 188 */       return opt.newInstance();
/* 189 */     } catch (InstantiationException e) {
/* 190 */       logger.log(Level.INFO, "failed to load an optimized Accessor", e);
/* 191 */     } catch (IllegalAccessException e) {
/* 192 */       logger.log(Level.INFO, "failed to load an optimized Accessor", e);
/* 193 */     } catch (SecurityException e) {
/* 194 */       logger.log(Level.INFO, "failed to load an optimized Accessor", e);
/*     */     } 
/* 196 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\OptimizedAccessorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */