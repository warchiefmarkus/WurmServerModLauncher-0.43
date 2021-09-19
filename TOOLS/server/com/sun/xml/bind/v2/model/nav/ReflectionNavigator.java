/*     */ package com.sun.xml.bind.v2.model.nav;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReflectionNavigator
/*     */   implements Navigator<Type, Class, Field, Method>
/*     */ {
/*     */   public Class getSuperClass(Class<Object> clazz) {
/*  67 */     if (clazz == Object.class) return null; 
/*  68 */     Class<? super Object> sc = clazz.getSuperclass();
/*  69 */     if (sc == null) sc = Object.class; 
/*  70 */     return sc;
/*     */   }
/*     */   
/*  73 */   private static final TypeVisitor<Type, Class> baseClassFinder = new TypeVisitor<Type, Class>()
/*     */     {
/*     */       public Type onClass(Class c, Class sup) {
/*  76 */         if (sup == c) {
/*  77 */           return sup;
/*     */         }
/*     */ 
/*     */         
/*  81 */         Type sc = c.getGenericSuperclass();
/*  82 */         if (sc != null) {
/*  83 */           Type r = visit(sc, sup);
/*  84 */           if (r != null) return r;
/*     */         
/*     */         } 
/*  87 */         for (Type i : c.getGenericInterfaces()) {
/*  88 */           Type r = visit(i, sup);
/*  89 */           if (r != null) return r;
/*     */         
/*     */         } 
/*  92 */         return null;
/*     */       }
/*     */       
/*     */       public Type onParameterizdType(ParameterizedType p, Class sup) {
/*  96 */         Class raw = (Class)p.getRawType();
/*  97 */         if (raw == sup)
/*     */         {
/*  99 */           return p;
/*     */         }
/*     */         
/* 102 */         Type r = raw.getGenericSuperclass();
/* 103 */         if (r != null)
/* 104 */           r = visit(bind(r, raw, p), sup); 
/* 105 */         if (r != null)
/* 106 */           return r; 
/* 107 */         for (Type i : raw.getGenericInterfaces()) {
/* 108 */           r = visit(bind(i, raw, p), sup);
/* 109 */           if (r != null) return r; 
/*     */         } 
/* 111 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Type onGenericArray(GenericArrayType g, Class sup) {
/* 117 */         return null;
/*     */       }
/*     */       
/*     */       public Type onVariable(TypeVariable v, Class sup) {
/* 121 */         return visit(v.getBounds()[0], sup);
/*     */       }
/*     */ 
/*     */       
/*     */       public Type onWildcard(WildcardType w, Class sup) {
/* 126 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private Type bind(Type t, GenericDeclaration decl, ParameterizedType args) {
/* 138 */         return ReflectionNavigator.binder.visit(t, new ReflectionNavigator.BinderArg(decl, args.getActualTypeArguments()));
/*     */       }
/*     */     };
/*     */   
/*     */   private static class BinderArg {
/*     */     final TypeVariable[] params;
/*     */     final Type[] args;
/*     */     
/*     */     BinderArg(TypeVariable[] params, Type[] args) {
/* 147 */       this.params = params;
/* 148 */       this.args = args;
/* 149 */       assert params.length == args.length;
/*     */     }
/*     */     
/*     */     public BinderArg(GenericDeclaration decl, Type[] args) {
/* 153 */       this((TypeVariable[])decl.getTypeParameters(), args);
/*     */     }
/*     */     
/*     */     Type replace(TypeVariable v) {
/* 157 */       for (int i = 0; i < this.params.length; i++) {
/* 158 */         if (this.params[i].equals(v))
/* 159 */           return this.args[i]; 
/* 160 */       }  return v;
/*     */     } }
/*     */   
/* 163 */   private static final TypeVisitor<Type, BinderArg> binder = new TypeVisitor<Type, BinderArg>() {
/*     */       public Type onClass(Class c, ReflectionNavigator.BinderArg args) {
/* 165 */         return c;
/*     */       }
/*     */       public Type onParameterizdType(ParameterizedType p, ReflectionNavigator.BinderArg args) {
/*     */         int j;
/* 169 */         Type[] params = p.getActualTypeArguments();
/*     */         
/* 171 */         boolean different = false;
/* 172 */         for (int i = 0; i < params.length; i++) {
/* 173 */           Type t = params[i];
/* 174 */           params[i] = visit(t, args);
/* 175 */           j = different | ((t != params[i]) ? 1 : 0);
/*     */         } 
/*     */         
/* 178 */         Type newOwner = p.getOwnerType();
/* 179 */         if (newOwner != null)
/* 180 */           newOwner = visit(newOwner, args); 
/* 181 */         j |= (p.getOwnerType() != newOwner) ? 1 : 0;
/*     */         
/* 183 */         if (j == 0) return p;
/*     */         
/* 185 */         return new ParameterizedTypeImpl((Class)p.getRawType(), params, newOwner);
/*     */       }
/*     */       
/*     */       public Type onGenericArray(GenericArrayType g, ReflectionNavigator.BinderArg types) {
/* 189 */         Type c = visit(g.getGenericComponentType(), types);
/* 190 */         if (c == g.getGenericComponentType()) return g;
/*     */         
/* 192 */         return new GenericArrayTypeImpl(c);
/*     */       }
/*     */       
/*     */       public Type onVariable(TypeVariable v, ReflectionNavigator.BinderArg types) {
/* 196 */         return types.replace(v);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Type onWildcard(WildcardType w, ReflectionNavigator.BinderArg types) {
/*     */         int j;
/* 203 */         Type[] lb = w.getLowerBounds();
/* 204 */         Type[] ub = w.getUpperBounds();
/* 205 */         boolean diff = false;
/*     */         int i;
/* 207 */         for (i = 0; i < lb.length; i++) {
/* 208 */           Type t = lb[i];
/* 209 */           lb[i] = visit(t, types);
/* 210 */           j = diff | ((t != lb[i]) ? 1 : 0);
/*     */         } 
/*     */         
/* 213 */         for (i = 0; i < ub.length; i++) {
/* 214 */           Type t = ub[i];
/* 215 */           ub[i] = visit(t, types);
/* 216 */           j |= (t != ub[i]) ? 1 : 0;
/*     */         } 
/*     */         
/* 219 */         if (j == 0) return w;
/*     */         
/* 221 */         return new WildcardTypeImpl(lb, ub);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public Type getBaseClass(Type t, Class sup) {
/* 227 */     return baseClassFinder.visit(t, sup);
/*     */   }
/*     */   
/*     */   public String getClassName(Class clazz) {
/* 231 */     return clazz.getName();
/*     */   }
/*     */   
/*     */   public String getTypeName(Type type) {
/* 235 */     if (type instanceof Class) {
/* 236 */       Class c = (Class)type;
/* 237 */       if (c.isArray())
/* 238 */         return getTypeName(c.getComponentType()) + "[]"; 
/* 239 */       return c.getName();
/*     */     } 
/* 241 */     return type.toString();
/*     */   }
/*     */   
/*     */   public String getClassShortName(Class clazz) {
/* 245 */     return clazz.getSimpleName();
/*     */   }
/*     */   
/*     */   public Collection<? extends Field> getDeclaredFields(Class clazz) {
/* 249 */     return Arrays.asList(clazz.getDeclaredFields());
/*     */   }
/*     */   
/*     */   public Field getDeclaredField(Class clazz, String fieldName) {
/*     */     try {
/* 254 */       return clazz.getDeclaredField(fieldName);
/* 255 */     } catch (NoSuchFieldException e) {
/* 256 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Collection<? extends Method> getDeclaredMethods(Class clazz) {
/* 261 */     return Arrays.asList(clazz.getDeclaredMethods());
/*     */   }
/*     */   
/*     */   public Class getDeclaringClassForField(Field field) {
/* 265 */     return field.getDeclaringClass();
/*     */   }
/*     */   
/*     */   public Class getDeclaringClassForMethod(Method method) {
/* 269 */     return method.getDeclaringClass();
/*     */   }
/*     */   
/*     */   public Type getFieldType(Field field) {
/* 273 */     return fix(field.getGenericType());
/*     */   }
/*     */   
/*     */   public String getFieldName(Field field) {
/* 277 */     return field.getName();
/*     */   }
/*     */   
/*     */   public String getMethodName(Method method) {
/* 281 */     return method.getName();
/*     */   }
/*     */   
/*     */   public Type getReturnType(Method method) {
/* 285 */     return fix(method.getGenericReturnType());
/*     */   }
/*     */   
/*     */   public Type[] getMethodParameters(Method method) {
/* 289 */     return method.getGenericParameterTypes();
/*     */   }
/*     */   
/*     */   public boolean isStaticMethod(Method method) {
/* 293 */     return Modifier.isStatic(method.getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isSubClassOf(Type sub, Type sup) {
/* 297 */     return erasure(sup).isAssignableFrom(erasure(sub));
/*     */   }
/*     */   
/*     */   public Class ref(Class c) {
/* 301 */     return c;
/*     */   }
/*     */   
/*     */   public Class use(Class c) {
/* 305 */     return c;
/*     */   }
/*     */   
/*     */   public Class asDecl(Type t) {
/* 309 */     return erasure(t);
/*     */   }
/*     */   
/*     */   public Class asDecl(Class c) {
/* 313 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 320 */   private static final TypeVisitor<Class, Void> eraser = new TypeVisitor<Class, Void>() {
/*     */       public Class onClass(Class c, Void _) {
/* 322 */         return c;
/*     */       }
/*     */ 
/*     */       
/*     */       public Class onParameterizdType(ParameterizedType p, Void _) {
/* 327 */         return visit(p.getRawType(), null);
/*     */       }
/*     */       
/*     */       public Class onGenericArray(GenericArrayType g, Void _) {
/* 331 */         return Array.newInstance(visit(g.getGenericComponentType(), null), 0).getClass();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Class onVariable(TypeVariable v, Void _) {
/* 337 */         return visit(v.getBounds()[0], null);
/*     */       }
/*     */       
/*     */       public Class onWildcard(WildcardType w, Void _) {
/* 341 */         return visit(w.getUpperBounds()[0], null);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Class<T> erasure(Type t) {
/* 361 */     return eraser.visit(t, null);
/*     */   }
/*     */   
/*     */   public boolean isAbstract(Class clazz) {
/* 365 */     return Modifier.isAbstract(clazz.getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isFinal(Class clazz) {
/* 369 */     return Modifier.isFinal(clazz.getModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type createParameterizedType(Class<?> rawType, Type... arguments) {
/* 376 */     return new ParameterizedTypeImpl(rawType, arguments, null);
/*     */   }
/*     */   
/*     */   public boolean isArray(Type t) {
/* 380 */     if (t instanceof Class) {
/* 381 */       Class c = (Class)t;
/* 382 */       return c.isArray();
/*     */     } 
/* 384 */     if (t instanceof GenericArrayType)
/* 385 */       return true; 
/* 386 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isArrayButNotByteArray(Type t) {
/* 390 */     if (t instanceof Class) {
/* 391 */       Class<byte[]> c = (Class)t;
/* 392 */       return (c.isArray() && c != byte[].class);
/*     */     } 
/* 394 */     if (t instanceof GenericArrayType) {
/* 395 */       t = ((GenericArrayType)t).getGenericComponentType();
/* 396 */       return (t != byte.class);
/*     */     } 
/* 398 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getComponentType(Type t) {
/* 403 */     if (t instanceof Class) {
/* 404 */       Class c = (Class)t;
/* 405 */       return c.getComponentType();
/*     */     } 
/* 407 */     if (t instanceof GenericArrayType) {
/* 408 */       return ((GenericArrayType)t).getGenericComponentType();
/*     */     }
/* 410 */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */   public Type getTypeArgument(Type type, int i) {
/* 414 */     if (type instanceof ParameterizedType) {
/* 415 */       ParameterizedType p = (ParameterizedType)type;
/* 416 */       return fix(p.getActualTypeArguments()[i]);
/*     */     } 
/* 418 */     throw new IllegalArgumentException();
/*     */   }
/*     */   
/*     */   public boolean isParameterizedType(Type type) {
/* 422 */     return type instanceof ParameterizedType;
/*     */   }
/*     */   
/*     */   public boolean isPrimitive(Type type) {
/* 426 */     if (type instanceof Class) {
/* 427 */       Class c = (Class)type;
/* 428 */       return c.isPrimitive();
/*     */     } 
/* 430 */     return false;
/*     */   }
/*     */   
/*     */   public Type getPrimitive(Class primitiveType) {
/* 434 */     assert primitiveType.isPrimitive();
/* 435 */     return primitiveType;
/*     */   }
/*     */   
/*     */   public Location getClassLocation(final Class clazz) {
/* 439 */     return new Location() {
/*     */         public String toString() {
/* 441 */           return clazz.getName();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Location getFieldLocation(final Field field) {
/* 447 */     return new Location() {
/*     */         public String toString() {
/* 449 */           return field.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Location getMethodLocation(final Method method) {
/* 455 */     return new Location() {
/*     */         public String toString() {
/* 457 */           return method.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public boolean hasDefaultConstructor(Class c) {
/*     */     try {
/* 464 */       c.getDeclaredConstructor(new Class[0]);
/* 465 */       return true;
/* 466 */     } catch (NoSuchMethodException e) {
/* 467 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isStaticField(Field field) {
/* 472 */     return Modifier.isStatic(field.getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isPublicMethod(Method method) {
/* 476 */     return Modifier.isPublic(method.getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isPublicField(Field field) {
/* 480 */     return Modifier.isPublic(field.getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isEnum(Class<?> c) {
/* 484 */     return Enum.class.isAssignableFrom(c);
/*     */   }
/*     */   
/*     */   public Field[] getEnumConstants(Class<Object> clazz) {
/*     */     try {
/* 489 */       Object[] values = clazz.getEnumConstants();
/* 490 */       Field[] fields = new Field[values.length];
/* 491 */       for (int i = 0; i < values.length; i++) {
/* 492 */         fields[i] = clazz.getField(((Enum<Enum>)values[i]).name());
/*     */       }
/* 494 */       return fields;
/* 495 */     } catch (NoSuchFieldException e) {
/*     */       
/* 497 */       throw new NoSuchFieldError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Type getVoidType() {
/* 502 */     return Void.class;
/*     */   }
/*     */   
/*     */   public String getPackageName(Class clazz) {
/* 506 */     String name = clazz.getName();
/* 507 */     int idx = name.lastIndexOf('.');
/* 508 */     if (idx < 0) return ""; 
/* 509 */     return name.substring(0, idx);
/*     */   }
/*     */   
/*     */   public Class findClass(String className, Class referencePoint) {
/*     */     try {
/* 514 */       ClassLoader cl = referencePoint.getClassLoader();
/* 515 */       if (cl == null) cl = ClassLoader.getSystemClassLoader(); 
/* 516 */       return cl.loadClass(className);
/* 517 */     } catch (ClassNotFoundException e) {
/* 518 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBridgeMethod(Method method) {
/* 523 */     return method.isBridge();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOverriding(Method method, Class base) {
/* 537 */     String name = method.getName();
/* 538 */     Class[] params = method.getParameterTypes();
/*     */     
/* 540 */     while (base != null) {
/*     */       try {
/* 542 */         if (base.getDeclaredMethod(name, params) != null)
/* 543 */           return true; 
/* 544 */       } catch (NoSuchMethodException e) {}
/*     */ 
/*     */ 
/*     */       
/* 548 */       base = base.getSuperclass();
/*     */     } 
/*     */     
/* 551 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isInterface(Class clazz) {
/* 555 */     return clazz.isInterface();
/*     */   }
/*     */   
/*     */   public boolean isTransient(Field f) {
/* 559 */     return Modifier.isTransient(f.getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isInnerClass(Class clazz) {
/* 563 */     return (clazz.getEnclosingClass() != null && !Modifier.isStatic(clazz.getModifiers()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Type fix(Type t) {
/* 574 */     if (!(t instanceof GenericArrayType)) {
/* 575 */       return t;
/*     */     }
/* 577 */     GenericArrayType gat = (GenericArrayType)t;
/* 578 */     if (gat.getGenericComponentType() instanceof Class) {
/* 579 */       Class<?> c = (Class)gat.getGenericComponentType();
/* 580 */       return Array.newInstance(c, 0).getClass();
/*     */     } 
/*     */     
/* 583 */     return t;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\nav\ReflectionNavigator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */