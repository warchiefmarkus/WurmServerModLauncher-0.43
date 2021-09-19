/*     */ package com.sun.xml.bind.v2.model.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class RuntimeInlineAnnotationReader
/*     */   extends AbstractInlineAnnotationReaderImpl<Type, Class, Field, Method>
/*     */   implements RuntimeAnnotationReader
/*     */ {
/*     */   public <A extends Annotation> A getFieldAnnotation(Class<A> annotation, Field field, Locatable srcPos) {
/*  57 */     return LocatableAnnotation.create(field.getAnnotation(annotation), srcPos);
/*     */   }
/*     */   
/*     */   public boolean hasFieldAnnotation(Class<? extends Annotation> annotationType, Field field) {
/*  61 */     return field.isAnnotationPresent(annotationType);
/*     */   }
/*     */   
/*     */   public boolean hasClassAnnotation(Class clazz, Class<? extends Annotation> annotationType) {
/*  65 */     return clazz.isAnnotationPresent(annotationType);
/*     */   }
/*     */   
/*     */   public Annotation[] getAllFieldAnnotations(Field field, Locatable srcPos) {
/*  69 */     Annotation[] r = field.getAnnotations();
/*  70 */     for (int i = 0; i < r.length; i++) {
/*  71 */       r[i] = LocatableAnnotation.create(r[i], srcPos);
/*     */     }
/*  73 */     return r;
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> annotation, Method method, Locatable srcPos) {
/*  77 */     return LocatableAnnotation.create(method.getAnnotation(annotation), srcPos);
/*     */   }
/*     */   
/*     */   public boolean hasMethodAnnotation(Class<? extends Annotation> annotation, Method method) {
/*  81 */     return method.isAnnotationPresent(annotation);
/*     */   }
/*     */   
/*     */   public Annotation[] getAllMethodAnnotations(Method method, Locatable srcPos) {
/*  85 */     Annotation[] r = method.getAnnotations();
/*  86 */     for (int i = 0; i < r.length; i++) {
/*  87 */       r[i] = LocatableAnnotation.create(r[i], srcPos);
/*     */     }
/*  89 */     return r;
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getMethodParameterAnnotation(Class<A> annotation, Method method, int paramIndex, Locatable srcPos) {
/*  93 */     Annotation[] pa = method.getParameterAnnotations()[paramIndex];
/*  94 */     for (Annotation a : pa) {
/*  95 */       if (a.annotationType() == annotation)
/*  96 */         return LocatableAnnotation.create((A)a, srcPos); 
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getClassAnnotation(Class<A> a, Class clazz, Locatable srcPos) {
/* 102 */     return LocatableAnnotation.create((A)clazz.getAnnotation(a), srcPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   private final Map<Class<? extends Annotation>, Map<Package, Annotation>> packageCache = new HashMap<Class<? extends Annotation>, Map<Package, Annotation>>();
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getPackageAnnotation(Class<A> a, Class clazz, Locatable srcPos) {
/* 113 */     Package p = clazz.getPackage();
/* 114 */     if (p == null) return null;
/*     */     
/* 116 */     Map<Package, Annotation> cache = this.packageCache.get(a);
/* 117 */     if (cache == null) {
/* 118 */       cache = new HashMap<Package, Annotation>();
/* 119 */       this.packageCache.put(a, cache);
/*     */     } 
/*     */     
/* 122 */     if (cache.containsKey(p)) {
/* 123 */       return (A)cache.get(p);
/*     */     }
/* 125 */     A ann = LocatableAnnotation.create(p.getAnnotation(a), srcPos);
/* 126 */     cache.put(p, (Annotation)ann);
/* 127 */     return ann;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class getClassValue(Annotation a, String name) {
/*     */     try {
/* 133 */       return (Class)a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
/* 134 */     } catch (IllegalAccessException e) {
/*     */       
/* 136 */       throw new IllegalAccessError(e.getMessage());
/* 137 */     } catch (InvocationTargetException e) {
/*     */       
/* 139 */       throw new InternalError(e.getMessage());
/* 140 */     } catch (NoSuchMethodException e) {
/* 141 */       throw new NoSuchMethodError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Class[] getClassArrayValue(Annotation a, String name) {
/*     */     try {
/* 147 */       return (Class[])a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
/* 148 */     } catch (IllegalAccessException e) {
/*     */       
/* 150 */       throw new IllegalAccessError(e.getMessage());
/* 151 */     } catch (InvocationTargetException e) {
/*     */       
/* 153 */       throw new InternalError(e.getMessage());
/* 154 */     } catch (NoSuchMethodException e) {
/* 155 */       throw new NoSuchMethodError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String fullName(Method m) {
/* 160 */     return m.getDeclaringClass().getName() + '#' + m.getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\RuntimeInlineAnnotationReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */