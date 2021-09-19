/*     */ package com.sun.xml.bind.v2.model.annotation;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
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
/*     */ public class LocatableAnnotation
/*     */   implements InvocationHandler, Locatable, Location
/*     */ {
/*     */   private final Annotation core;
/*     */   private final Locatable upstream;
/*     */   
/*     */   public static <A extends Annotation> A create(A annotation, Locatable parentSourcePos) {
/*  65 */     if (annotation == null) return null; 
/*  66 */     Class<? extends Annotation> type = annotation.annotationType();
/*  67 */     if (quicks.containsKey(type))
/*     */     {
/*  69 */       return (A)((Quick)quicks.get(type)).newInstance(parentSourcePos, (Annotation)annotation);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  74 */     ClassLoader cl = LocatableAnnotation.class.getClassLoader();
/*     */     
/*     */     try {
/*  77 */       Class<?> loadableT = Class.forName(type.getName(), false, cl);
/*  78 */       if (loadableT != type) {
/*  79 */         return annotation;
/*     */       }
/*  81 */       return (A)Proxy.newProxyInstance(cl, new Class[] { type, Locatable.class }, new LocatableAnnotation((Annotation)annotation, parentSourcePos));
/*     */     
/*     */     }
/*  84 */     catch (ClassNotFoundException e) {
/*     */       
/*  86 */       return annotation;
/*  87 */     } catch (IllegalArgumentException e) {
/*     */ 
/*     */       
/*  90 */       return annotation;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   LocatableAnnotation(Annotation core, Locatable upstream) {
/*  96 */     this.core = core;
/*  97 */     this.upstream = upstream;
/*     */   }
/*     */   
/*     */   public Locatable getUpstream() {
/* 101 */     return this.upstream;
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*     */     try {
/* 110 */       if (method.getDeclaringClass() == Locatable.class)
/* 111 */         return method.invoke(this, args); 
/* 112 */       if (Modifier.isStatic(method.getModifiers()))
/*     */       {
/*     */ 
/*     */         
/* 116 */         throw new IllegalArgumentException();
/*     */       }
/* 118 */       return method.invoke(this.core, args);
/* 119 */     } catch (InvocationTargetException e) {
/* 120 */       if (e.getTargetException() != null)
/* 121 */         throw e.getTargetException(); 
/* 122 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 127 */     return this.core.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   private static final Map<Class, Quick> quicks = (Map)new HashMap<Class<?>, Quick>();
/*     */   
/*     */   static {
/* 137 */     for (Quick q : Init.getAll())
/* 138 */       quicks.put(q.annotationType(), q); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\LocatableAnnotation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */