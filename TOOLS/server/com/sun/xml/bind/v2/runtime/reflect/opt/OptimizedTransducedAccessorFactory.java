/*     */ package com.sun.xml.bind.v2.runtime.reflect.opt;
/*     */ 
/*     */ import com.sun.xml.bind.Util;
/*     */ import com.sun.xml.bind.v2.bytecode.ClassTailor;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeClassInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OptimizedTransducedAccessorFactory
/*     */ {
/*  67 */   private static final Logger logger = Util.getClassLogger();
/*     */   
/*     */   private static final String fieldTemplateName;
/*     */   private static final String methodTemplateName;
/*     */   
/*     */   static {
/*  73 */     String s = TransducedAccessor_field_Byte.class.getName();
/*  74 */     fieldTemplateName = s.substring(0, s.length() - "Byte".length()).replace('.', '/');
/*     */     
/*  76 */     s = TransducedAccessor_method_Byte.class.getName();
/*  77 */     methodTemplateName = s.substring(0, s.length() - "Byte".length()).replace('.', '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final TransducedAccessor get(RuntimePropertyInfo prop) {
/*  87 */     Accessor acc = prop.getAccessor();
/*     */ 
/*     */     
/*  90 */     Class<?> opt = null;
/*     */     
/*  92 */     TypeInfo<Type, Class<?>> parent = prop.parent();
/*  93 */     if (!(parent instanceof RuntimeClassInfo)) {
/*  94 */       return null;
/*     */     }
/*  96 */     Class dc = (Class)((RuntimeClassInfo)parent).getClazz();
/*  97 */     String newClassName = ClassTailor.toVMClassName(dc) + "_JaxbXducedAccessor_" + prop.getName();
/*     */ 
/*     */     
/* 100 */     if (acc instanceof Accessor.FieldReflection) {
/*     */       
/* 102 */       Accessor.FieldReflection racc = (Accessor.FieldReflection)acc;
/* 103 */       Field field = racc.f;
/*     */       
/* 105 */       int mods = field.getModifiers();
/* 106 */       if (Modifier.isPrivate(mods) || Modifier.isFinal(mods))
/*     */       {
/*     */         
/* 109 */         return null;
/*     */       }
/* 111 */       Class<?> t = field.getType();
/* 112 */       if (t.isPrimitive()) {
/* 113 */         opt = AccessorInjector.prepare(dc, fieldTemplateName + (String)suffixMap.get(t), newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(dc), "f_" + t.getName(), field.getName() });
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     if (acc.getClass() == Accessor.GetterSetterReflection.class) {
/* 123 */       Accessor.GetterSetterReflection gacc = (Accessor.GetterSetterReflection)acc;
/*     */       
/* 125 */       if (gacc.getter == null || gacc.setter == null) {
/* 126 */         return null;
/*     */       }
/* 128 */       Class<?> t = gacc.getter.getReturnType();
/*     */       
/* 130 */       if (Modifier.isPrivate(gacc.getter.getModifiers()) || Modifier.isPrivate(gacc.setter.getModifiers()))
/*     */       {
/*     */         
/* 133 */         return null;
/*     */       }
/*     */       
/* 136 */       if (t.isPrimitive()) {
/* 137 */         opt = AccessorInjector.prepare(dc, methodTemplateName + (String)suffixMap.get(t), newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(dc), "get_" + t.getName(), gacc.getter.getName(), "set_" + t.getName(), gacc.setter.getName() });
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     if (opt == null) {
/* 149 */       return null;
/*     */     }
/* 151 */     logger.log(Level.FINE, "Using optimized TransducedAccessor for " + prop.displayName());
/*     */ 
/*     */     
/*     */     try {
/* 155 */       return (TransducedAccessor)opt.newInstance();
/* 156 */     } catch (InstantiationException e) {
/* 157 */       logger.log(Level.INFO, "failed to load an optimized TransducedAccessor", e);
/* 158 */     } catch (IllegalAccessException e) {
/* 159 */       logger.log(Level.INFO, "failed to load an optimized TransducedAccessor", e);
/* 160 */     } catch (SecurityException e) {
/* 161 */       logger.log(Level.INFO, "failed to load an optimized TransducedAccessor", e);
/*     */     } 
/* 163 */     return null;
/*     */   }
/*     */   
/* 166 */   private static final Map<Class, String> suffixMap = (Map)new HashMap<Class<?>, String>();
/*     */   
/*     */   static {
/* 169 */     suffixMap.put(byte.class, "Byte");
/* 170 */     suffixMap.put(short.class, "Short");
/* 171 */     suffixMap.put(int.class, "Integer");
/* 172 */     suffixMap.put(long.class, "Long");
/* 173 */     suffixMap.put(boolean.class, "Boolean");
/* 174 */     suffixMap.put(float.class, "Float");
/* 175 */     suffixMap.put(double.class, "Double");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\opt\OptimizedTransducedAccessorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */