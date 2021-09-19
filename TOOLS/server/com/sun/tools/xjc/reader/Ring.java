/*     */ package com.sun.tools.xjc.reader;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ public final class Ring
/*     */ {
/*  77 */   private final Map<Class, Object> components = (Map)new HashMap<Class<?>, Object>();
/*     */   
/*  79 */   private static final ThreadLocal<Ring> instances = new ThreadLocal<Ring>() {
/*     */       public Ring initialValue() {
/*  81 */         return new Ring();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> void add(Class<T> clazz, T instance) {
/*  88 */     assert !(get()).components.containsKey(clazz);
/*  89 */     (get()).components.put(clazz, instance);
/*     */   }
/*     */   
/*     */   public static <T> void add(T o) {
/*  93 */     add(o.getClass(), o);
/*     */   }
/*     */   
/*     */   public static <T> T get(Class<T> key) {
/*  97 */     T t = (T)(get()).components.get(key);
/*  98 */     if (t == null) {
/*     */       try {
/* 100 */         Constructor<T> c = key.getDeclaredConstructor(new Class[0]);
/* 101 */         c.setAccessible(true);
/* 102 */         t = c.newInstance(new Object[0]);
/* 103 */         if (!(get()).components.containsKey(key))
/*     */         {
/* 105 */           add(key, t); } 
/* 106 */       } catch (InstantiationException e) {
/* 107 */         throw new Error(e);
/* 108 */       } catch (IllegalAccessException e) {
/* 109 */         throw new Error(e);
/* 110 */       } catch (NoSuchMethodException e) {
/* 111 */         throw new Error(e);
/* 112 */       } catch (InvocationTargetException e) {
/* 113 */         throw new Error(e);
/*     */       } 
/*     */     }
/*     */     
/* 117 */     assert t != null;
/* 118 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ring get() {
/* 125 */     return instances.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Ring begin() {
/* 132 */     Ring r = instances.get();
/* 133 */     instances.set(new Ring());
/* 134 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void end(Ring old) {
/* 141 */     instances.set(old);
/*     */   }
/*     */   
/*     */   private Ring() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\Ring.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */