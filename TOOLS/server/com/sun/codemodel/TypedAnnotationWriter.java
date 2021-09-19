/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
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
/*     */ class TypedAnnotationWriter<A extends Annotation, W extends JAnnotationWriter<A>>
/*     */   implements InvocationHandler, JAnnotationWriter<A>
/*     */ {
/*     */   private final JAnnotationUse use;
/*     */   private final Class<A> annotation;
/*     */   private final Class<W> writerType;
/*     */   private Map<String, JAnnotationArrayMember> arrays;
/*     */   
/*     */   public TypedAnnotationWriter(Class<A> annotation, Class<W> writer, JAnnotationUse use) {
/*  42 */     this.annotation = annotation;
/*  43 */     this.writerType = writer;
/*  44 */     this.use = use;
/*     */   }
/*     */   
/*     */   public JAnnotationUse getAnnotationUse() {
/*  48 */     return this.use;
/*     */   }
/*     */   
/*     */   public Class<A> getAnnotationType() {
/*  52 */     return this.annotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*  57 */     if (method.getDeclaringClass() == JAnnotationWriter.class) {
/*     */       try {
/*  59 */         return method.invoke(this, args);
/*  60 */       } catch (InvocationTargetException e) {
/*  61 */         throw e.getTargetException();
/*     */       } 
/*     */     }
/*     */     
/*  65 */     String name = method.getName();
/*  66 */     Object arg = null;
/*  67 */     if (args != null && args.length > 0) {
/*  68 */       arg = args[0];
/*     */     }
/*     */     
/*  71 */     Method m = this.annotation.getDeclaredMethod(name, new Class[0]);
/*  72 */     Class<?> rt = m.getReturnType();
/*     */ 
/*     */     
/*  75 */     if (rt.isArray()) {
/*  76 */       return addArrayValue(proxy, name, rt.getComponentType(), method.getReturnType(), arg);
/*     */     }
/*     */ 
/*     */     
/*  80 */     if (Annotation.class.isAssignableFrom(rt)) {
/*  81 */       Class<? extends Annotation> r = (Class)rt;
/*  82 */       return (new TypedAnnotationWriter((Class)r, (Class)method.getReturnType(), this.use.annotationParam(name, r))).createProxy();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     if (arg instanceof JType) {
/*  89 */       JType targ = (JType)arg;
/*  90 */       checkType(Class.class, rt);
/*  91 */       if (m.getDefaultValue() != null)
/*     */       {
/*  93 */         if (targ.equals(targ.owner().ref((Class)m.getDefaultValue())))
/*  94 */           return proxy; 
/*     */       }
/*  96 */       this.use.param(name, targ);
/*  97 */       return proxy;
/*     */     } 
/*     */ 
/*     */     
/* 101 */     checkType(arg.getClass(), rt);
/* 102 */     if (m.getDefaultValue() != null && m.getDefaultValue().equals(arg))
/*     */     {
/* 104 */       return proxy;
/*     */     }
/* 106 */     if (arg instanceof String) {
/* 107 */       this.use.param(name, (String)arg);
/* 108 */       return proxy;
/*     */     } 
/* 110 */     if (arg instanceof Boolean) {
/* 111 */       this.use.param(name, ((Boolean)arg).booleanValue());
/* 112 */       return proxy;
/*     */     } 
/* 114 */     if (arg instanceof Integer) {
/* 115 */       this.use.param(name, ((Integer)arg).intValue());
/* 116 */       return proxy;
/*     */     } 
/* 118 */     if (arg instanceof Class) {
/* 119 */       this.use.param(name, (Class)arg);
/* 120 */       return proxy;
/*     */     } 
/* 122 */     if (arg instanceof Enum) {
/* 123 */       this.use.param(name, (Enum)arg);
/* 124 */       return proxy;
/*     */     } 
/*     */     
/* 127 */     throw new IllegalArgumentException("Unable to handle this method call " + method.toString());
/*     */   }
/*     */   
/*     */   private Object addArrayValue(Object proxy, String name, Class<?> itemType, Class<?> expectedReturnType, Object arg) {
/* 131 */     if (this.arrays == null)
/* 132 */       this.arrays = new HashMap<String, JAnnotationArrayMember>(); 
/* 133 */     JAnnotationArrayMember m = this.arrays.get(name);
/* 134 */     if (m == null) {
/* 135 */       m = this.use.paramArray(name);
/* 136 */       this.arrays.put(name, m);
/*     */     } 
/*     */ 
/*     */     
/* 140 */     if (Annotation.class.isAssignableFrom(itemType)) {
/* 141 */       Class<? extends Annotation> r = (Class)itemType;
/* 142 */       if (!JAnnotationWriter.class.isAssignableFrom(expectedReturnType))
/* 143 */         throw new IllegalArgumentException("Unexpected return type " + expectedReturnType); 
/* 144 */       return (new TypedAnnotationWriter((Class)r, (Class)expectedReturnType, m.annotate(r))).createProxy();
/*     */     } 
/*     */ 
/*     */     
/* 148 */     if (arg instanceof JType) {
/* 149 */       checkType(Class.class, itemType);
/* 150 */       m.param((JType)arg);
/* 151 */       return proxy;
/*     */     } 
/* 153 */     checkType(arg.getClass(), itemType);
/* 154 */     if (arg instanceof String) {
/* 155 */       m.param((String)arg);
/* 156 */       return proxy;
/*     */     } 
/* 158 */     if (arg instanceof Boolean) {
/* 159 */       m.param(((Boolean)arg).booleanValue());
/* 160 */       return proxy;
/*     */     } 
/* 162 */     if (arg instanceof Integer) {
/* 163 */       m.param(((Integer)arg).intValue());
/* 164 */       return proxy;
/*     */     } 
/* 166 */     if (arg instanceof Class) {
/* 167 */       m.param((Class)arg);
/* 168 */       return proxy;
/*     */     } 
/*     */ 
/*     */     
/* 172 */     throw new IllegalArgumentException("Unable to handle this method call ");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkType(Class<?> actual, Class expected) {
/* 181 */     if (expected == actual || expected.isAssignableFrom(actual)) {
/*     */       return;
/*     */     }
/* 184 */     if (expected == JCodeModel.boxToPrimitive.get(actual)) {
/*     */       return;
/*     */     }
/* 187 */     throw new IllegalArgumentException("Expected " + expected + " but found " + actual);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private W createProxy() {
/* 194 */     return (W)Proxy.newProxyInstance(this.writerType.getClassLoader(), new Class[] { this.writerType }, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <W extends JAnnotationWriter<?>> W create(Class<W> w, JAnnotatable annotatable) {
/* 202 */     Class<? extends Annotation> a = findAnnotationType(w);
/* 203 */     return (W)(new TypedAnnotationWriter<Annotation, JAnnotationWriter<Annotation>>((Class)a, w, annotatable.annotate(a))).createProxy();
/*     */   }
/*     */   
/*     */   private static Class<? extends Annotation> findAnnotationType(Class clazz) {
/* 207 */     for (Type t : clazz.getGenericInterfaces()) {
/* 208 */       if (t instanceof ParameterizedType) {
/* 209 */         ParameterizedType p = (ParameterizedType)t;
/* 210 */         if (p.getRawType() == JAnnotationWriter.class)
/* 211 */           return (Class<? extends Annotation>)p.getActualTypeArguments()[0]; 
/*     */       } 
/* 213 */       if (t instanceof Class) {
/*     */         
/* 215 */         Class<? extends Annotation> r = findAnnotationType((Class)t);
/* 216 */         if (r != null) return r; 
/*     */       } 
/*     */     } 
/* 219 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\TypedAnnotationWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */