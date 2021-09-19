/*     */ package com.sun.xml.bind.v2;
/*     */ 
/*     */ import com.sun.xml.bind.Util;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassFactory
/*     */ {
/*  67 */   private static final Class[] emptyClass = new Class[0];
/*  68 */   private static final Object[] emptyObject = new Object[0];
/*     */   
/*  70 */   private static final Logger logger = Util.getClassLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final ThreadLocal<Map<Class, WeakReference<Constructor>>> tls = new ThreadLocal<Map<Class, WeakReference<Constructor>>>() {
/*     */       public Map<Class, WeakReference<Constructor>> initialValue() {
/*  79 */         return (Map)new WeakHashMap<Class<?>, WeakReference<Constructor>>();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T create0(Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
/*  87 */     Map<Class<?>, WeakReference<Constructor>> m = tls.get();
/*  88 */     Constructor<T> cons = null;
/*  89 */     WeakReference<Constructor> consRef = m.get(clazz);
/*  90 */     if (consRef != null)
/*  91 */       cons = consRef.get(); 
/*  92 */     if (cons == null) {
/*     */       try {
/*  94 */         cons = clazz.getDeclaredConstructor(emptyClass);
/*  95 */       } catch (NoSuchMethodException e) {
/*  96 */         NoSuchMethodError exp; logger.log(Level.INFO, "No default constructor found on " + clazz, e);
/*     */         
/*  98 */         if (clazz.getDeclaringClass() != null && !Modifier.isStatic(clazz.getModifiers())) {
/*  99 */           exp = new NoSuchMethodError(Messages.NO_DEFAULT_CONSTRUCTOR_IN_INNER_CLASS.format(new Object[] { clazz.getName() }));
/*     */         } else {
/* 101 */           exp = new NoSuchMethodError(e.getMessage());
/*     */         } 
/* 103 */         exp.initCause(e);
/* 104 */         throw exp;
/*     */       } 
/*     */       
/* 107 */       int classMod = clazz.getModifiers();
/*     */       
/* 109 */       if (!Modifier.isPublic(classMod) || !Modifier.isPublic(cons.getModifiers())) {
/*     */         
/*     */         try {
/* 112 */           cons.setAccessible(true);
/* 113 */         } catch (SecurityException e) {
/*     */           
/* 115 */           logger.log(Level.FINE, "Unable to make the constructor of " + clazz + " accessible", e);
/* 116 */           throw e;
/*     */         } 
/*     */       }
/*     */       
/* 120 */       m.put(clazz, new WeakReference<Constructor>(cons));
/*     */     } 
/*     */     
/* 123 */     return cons.newInstance(emptyObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T create(Class<T> clazz) {
/*     */     try {
/* 132 */       return create0(clazz);
/* 133 */     } catch (InstantiationException e) {
/* 134 */       logger.log(Level.INFO, "failed to create a new instance of " + clazz, e);
/* 135 */       throw new InstantiationError(e.toString());
/* 136 */     } catch (IllegalAccessException e) {
/* 137 */       logger.log(Level.INFO, "failed to create a new instance of " + clazz, e);
/* 138 */       throw new IllegalAccessError(e.toString());
/* 139 */     } catch (InvocationTargetException e) {
/* 140 */       Throwable target = e.getTargetException();
/*     */ 
/*     */ 
/*     */       
/* 144 */       if (target instanceof RuntimeException) {
/* 145 */         throw (RuntimeException)target;
/*     */       }
/*     */       
/* 148 */       if (target instanceof Error) {
/* 149 */         throw (Error)target;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 154 */       throw new IllegalStateException(target);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object create(Method method) {
/*     */     Throwable throwable;
/*     */     try {
/* 164 */       return method.invoke(null, emptyObject);
/* 165 */     } catch (InvocationTargetException ive) {
/* 166 */       Throwable target = ive.getTargetException();
/*     */       
/* 168 */       if (target instanceof RuntimeException) {
/* 169 */         throw (RuntimeException)target;
/*     */       }
/* 171 */       if (target instanceof Error) {
/* 172 */         throw (Error)target;
/*     */       }
/* 174 */       throw new IllegalStateException(target);
/* 175 */     } catch (IllegalAccessException e) {
/* 176 */       logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), e);
/* 177 */       throw new IllegalAccessError(e.toString());
/* 178 */     } catch (IllegalArgumentException iae) {
/* 179 */       logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), iae);
/* 180 */       throwable = iae;
/* 181 */     } catch (NullPointerException npe) {
/* 182 */       logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), npe);
/* 183 */       throwable = npe;
/* 184 */     } catch (ExceptionInInitializerError eie) {
/* 185 */       logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), eie);
/* 186 */       throwable = eie;
/*     */     } 
/*     */ 
/*     */     
/* 190 */     NoSuchMethodError exp = new NoSuchMethodError(throwable.getMessage());
/* 191 */     exp.initCause(throwable);
/* 192 */     throw exp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Class<? extends T> inferImplClass(Class<T> fieldType, Class[] knownImplClasses) {
/* 202 */     if (!fieldType.isInterface())
/*     */     {
/*     */       
/* 205 */       return fieldType;
/*     */     }
/* 207 */     for (Class<?> impl : knownImplClasses) {
/* 208 */       if (fieldType.isAssignableFrom(impl)) {
/* 209 */         return impl.asSubclass(fieldType);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 215 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\ClassFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */