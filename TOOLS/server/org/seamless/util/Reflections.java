/*     */ package org.seamless.util;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public class Reflections
/*     */ {
/*     */   public static Object invoke(Method method, Object target, Object... args) throws Exception {
/*     */     try {
/*  41 */       return method.invoke(target, args);
/*     */     }
/*  43 */     catch (IllegalArgumentException iae) {
/*  44 */       String message = "Could not invoke method by reflection: " + toString(method);
/*  45 */       if (args != null && args.length > 0) {
/*  46 */         message = message + " with parameters: (" + toClassNameString(", ", args) + ')';
/*     */       }
/*  48 */       message = message + " on: " + target.getClass().getName();
/*  49 */       throw new IllegalArgumentException(message, iae);
/*     */     }
/*  51 */     catch (InvocationTargetException ite) {
/*  52 */       if (ite.getCause() instanceof Exception) {
/*  53 */         throw (Exception)ite.getCause();
/*     */       }
/*  55 */       throw ite;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object get(Field field, Object target) throws Exception {
/*  63 */     boolean accessible = field.isAccessible();
/*     */     try {
/*  65 */       field.setAccessible(true);
/*  66 */       return field.get(target);
/*     */     }
/*  68 */     catch (IllegalArgumentException iae) {
/*  69 */       String message = "Could not get field value by reflection: " + toString(field) + " on: " + target.getClass().getName();
/*     */       
/*  71 */       throw new IllegalArgumentException(message, iae);
/*     */     } finally {
/*     */       
/*  74 */       field.setAccessible(accessible);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getMethod(Class<Object> clazz, String name) {
/*  81 */     for (Class<Object> superClass = clazz; superClass != null && superClass != Object.class; superClass = (Class)superClass.getSuperclass()) {
/*     */       try {
/*  83 */         return superClass.getDeclaredMethod(name, new Class[0]);
/*     */       }
/*  85 */       catch (NoSuchMethodException nsme) {}
/*     */     } 
/*     */     
/*  88 */     throw new IllegalArgumentException("No such method: " + clazz.getName() + '.' + name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void set(Field field, Object target, Object value) throws Exception {
/*  94 */     boolean accessible = field.isAccessible();
/*     */     try {
/*  96 */       field.setAccessible(true);
/*  97 */       field.set(target, value);
/*     */     }
/*  99 */     catch (IllegalArgumentException iae) {
/*     */       
/* 101 */       String message = "Could not set field value by reflection: " + toString(field) + " on: " + field.getDeclaringClass().getName();
/*     */       
/* 103 */       if (value == null) {
/* 104 */         message = message + " with null value";
/*     */       } else {
/* 106 */         message = message + " with value: " + value.getClass();
/*     */       } 
/* 108 */       throw new IllegalArgumentException(message, iae);
/*     */     } finally {
/* 110 */       field.setAccessible(accessible);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMethodPropertyName(String methodName) {
/* 117 */     String methodPropertyName = null;
/* 118 */     if (methodName.startsWith("get")) {
/* 119 */       methodPropertyName = decapitalize(methodName.substring(3));
/* 120 */     } else if (methodName.startsWith("is")) {
/* 121 */       methodPropertyName = decapitalize(methodName.substring(2));
/* 122 */     } else if (methodName.startsWith("set")) {
/* 123 */       methodPropertyName = decapitalize(methodName.substring(3));
/*     */     } 
/* 125 */     return methodPropertyName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getGetterMethod(Class<Object> clazz, String name) {
/* 131 */     for (Class<Object> superClass = clazz; superClass != null && superClass != Object.class; superClass = (Class)superClass.getSuperclass()) {
/* 132 */       for (Method method : superClass.getDeclaredMethods()) {
/* 133 */         String methodName = method.getName();
/* 134 */         if ((method.getParameterTypes()).length == 0)
/*     */         {
/* 136 */           if (methodName.startsWith("get")) {
/* 137 */             if (decapitalize(methodName.substring(3)).equals(name))
/* 138 */               return method; 
/* 139 */           } else if (methodName.startsWith("is") && 
/* 140 */             decapitalize(methodName.substring(2)).equals(name)) {
/* 141 */             return method;
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 145 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Method> getMethods(Class<Object> clazz, Class<? extends Annotation> annotation) {
/* 151 */     List<Method> methods = new ArrayList<Method>();
/* 152 */     for (Class<Object> superClass = clazz; superClass != null && superClass != Object.class; superClass = (Class)superClass.getSuperclass()) {
/* 153 */       for (Method method : superClass.getDeclaredMethods()) {
/* 154 */         if (method.isAnnotationPresent(annotation)) {
/* 155 */           methods.add(method);
/*     */         }
/*     */       } 
/*     */     } 
/* 159 */     return methods;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Field getField(Class<Object> clazz, String name) {
/* 165 */     for (Class<Object> superClass = clazz; superClass != null && superClass != Object.class; superClass = (Class)superClass.getSuperclass()) {
/*     */       try {
/* 167 */         return superClass.getDeclaredField(name);
/*     */       }
/* 169 */       catch (NoSuchFieldException nsfe) {}
/*     */     } 
/*     */     
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Field> getFields(Class<Object> clazz, Class<? extends Annotation> annotation) {
/* 178 */     List<Field> fields = new ArrayList<Field>();
/* 179 */     for (Class<Object> superClass = clazz; superClass != null && superClass != Object.class; superClass = (Class)superClass.getSuperclass()) {
/* 180 */       for (Field field : superClass.getDeclaredFields()) {
/* 181 */         if (field.isAnnotationPresent(annotation)) {
/* 182 */           fields.add(field);
/*     */         }
/*     */       } 
/*     */     } 
/* 186 */     return fields;
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
/*     */   public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {
/*     */     Type[] actualTypeArguments;
/* 200 */     Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
/* 201 */     Type<? extends T> type = childClass;
/*     */     
/* 203 */     while (!getClass(type).equals(baseClass)) {
/* 204 */       if (type instanceof Class) {
/*     */         
/* 206 */         type = ((Class)type).getGenericSuperclass(); continue;
/*     */       } 
/* 208 */       ParameterizedType parameterizedType = (ParameterizedType)type;
/* 209 */       Class<?> rawType = (Class)parameterizedType.getRawType();
/*     */       
/* 211 */       Type[] arrayOfType = parameterizedType.getActualTypeArguments();
/* 212 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])rawType.getTypeParameters();
/* 213 */       for (int i = 0; i < arrayOfType.length; i++) {
/* 214 */         resolvedTypes.put(arrayOfTypeVariable[i], arrayOfType[i]);
/*     */       }
/*     */       
/* 217 */       if (!rawType.equals(baseClass)) {
/* 218 */         type = rawType.getGenericSuperclass();
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 226 */     if (type instanceof Class) {
/* 227 */       TypeVariable[] arrayOfTypeVariable = ((Class)type).getTypeParameters();
/*     */     } else {
/* 229 */       actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
/*     */     } 
/* 231 */     List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
/*     */     
/* 233 */     for (Type baseType : actualTypeArguments) {
/* 234 */       while (resolvedTypes.containsKey(baseType)) {
/* 235 */         baseType = resolvedTypes.get(baseType);
/*     */       }
/* 237 */       typeArgumentsAsClasses.add(getClass(baseType));
/*     */     } 
/* 239 */     return typeArgumentsAsClasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getClass(Type type) {
/* 249 */     if (type instanceof Class)
/* 250 */       return (Class)type; 
/* 251 */     if (type instanceof ParameterizedType)
/* 252 */       return getClass(((ParameterizedType)type).getRawType()); 
/* 253 */     if (type instanceof GenericArrayType) {
/* 254 */       Type componentType = ((GenericArrayType)type).getGenericComponentType();
/* 255 */       Class<?> componentClass = getClass(componentType);
/* 256 */       if (componentClass != null) {
/* 257 */         return Array.newInstance(componentClass, 0).getClass();
/*     */       }
/* 259 */       return null;
/*     */     } 
/*     */     
/* 262 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object getAndWrap(Field field, Object target) {
/* 267 */     boolean accessible = field.isAccessible();
/*     */     try {
/* 269 */       field.setAccessible(true);
/* 270 */       return get(field, target);
/*     */     }
/* 272 */     catch (Exception e) {
/* 273 */       if (e instanceof RuntimeException) {
/* 274 */         throw (RuntimeException)e;
/*     */       }
/* 276 */       throw new IllegalArgumentException("exception setting: " + field.getName(), e);
/*     */     }
/*     */     finally {
/*     */       
/* 280 */       field.setAccessible(accessible);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setAndWrap(Field field, Object target, Object value) {
/* 285 */     boolean accessible = field.isAccessible();
/*     */     try {
/* 287 */       field.setAccessible(true);
/* 288 */       set(field, target, value);
/*     */     }
/* 290 */     catch (Exception e) {
/* 291 */       if (e instanceof RuntimeException) {
/* 292 */         throw (RuntimeException)e;
/*     */       }
/* 294 */       throw new IllegalArgumentException("exception setting: " + field.getName(), e);
/*     */     }
/*     */     finally {
/*     */       
/* 298 */       field.setAccessible(accessible);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Object invokeAndWrap(Method method, Object target, Object... args) {
/*     */     try {
/* 304 */       return invoke(method, target, args);
/*     */     }
/* 306 */     catch (Exception e) {
/* 307 */       if (e instanceof RuntimeException) {
/* 308 */         throw (RuntimeException)e;
/*     */       }
/* 310 */       throw new RuntimeException("exception invoking: " + method.getName(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(Member member) {
/* 316 */     return unqualify(member.getDeclaringClass().getName()) + '.' + member.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class classForName(String name) throws ClassNotFoundException {
/*     */     try {
/* 323 */       return Thread.currentThread().getContextClassLoader().loadClass(name);
/*     */     }
/* 325 */     catch (Exception e) {
/* 326 */       return Class.forName(name);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isClassAvailable(String name) {
/*     */     try {
/* 332 */       classForName(name);
/*     */     }
/* 334 */     catch (ClassNotFoundException e) {
/* 335 */       return false;
/*     */     } 
/* 337 */     return true;
/*     */   }
/*     */   
/*     */   public static Class getCollectionElementType(Type collectionType) {
/* 341 */     if (!(collectionType instanceof ParameterizedType)) {
/* 342 */       throw new IllegalArgumentException("collection type not parameterized");
/*     */     }
/* 344 */     Type[] typeArguments = ((ParameterizedType)collectionType).getActualTypeArguments();
/* 345 */     if (typeArguments.length == 0) {
/* 346 */       throw new IllegalArgumentException("no type arguments for collection type");
/*     */     }
/* 348 */     Type typeArgument = (typeArguments.length == 1) ? typeArguments[0] : typeArguments[1];
/* 349 */     if (typeArgument instanceof ParameterizedType) {
/* 350 */       typeArgument = ((ParameterizedType)typeArgument).getRawType();
/*     */     }
/* 352 */     if (!(typeArgument instanceof Class)) {
/* 353 */       throw new IllegalArgumentException("type argument not a class");
/*     */     }
/* 355 */     return (Class)typeArgument;
/*     */   }
/*     */   
/*     */   public static Class getMapKeyType(Type collectionType) {
/* 359 */     if (!(collectionType instanceof ParameterizedType)) {
/* 360 */       throw new IllegalArgumentException("collection type not parameterized");
/*     */     }
/* 362 */     Type[] typeArguments = ((ParameterizedType)collectionType).getActualTypeArguments();
/* 363 */     if (typeArguments.length == 0) {
/* 364 */       throw new IllegalArgumentException("no type arguments for collection type");
/*     */     }
/* 366 */     Type typeArgument = typeArguments[0];
/* 367 */     if (!(typeArgument instanceof Class)) {
/* 368 */       throw new IllegalArgumentException("type argument not a class");
/*     */     }
/* 370 */     return (Class)typeArgument;
/*     */   }
/*     */   
/*     */   public static Method getSetterMethod(Class clazz, String name) {
/* 374 */     Method[] methods = clazz.getMethods();
/* 375 */     for (Method method : methods) {
/* 376 */       String methodName = method.getName();
/* 377 */       if (methodName.startsWith("set") && (method.getParameterTypes()).length == 1 && 
/* 378 */         decapitalize(methodName.substring(3)).equals(name)) {
/* 379 */         return method;
/*     */       }
/*     */     } 
/*     */     
/* 383 */     throw new IllegalArgumentException("no such setter method: " + clazz.getName() + '.' + name);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method getMethod(Annotation annotation, String name) {
/*     */     try {
/* 389 */       return annotation.annotationType().getMethod(name, new Class[0]);
/*     */     }
/* 391 */     catch (NoSuchMethodException nsme) {
/* 392 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isInstanceOf(Class<Object> clazz, String name) {
/* 397 */     if (name == null) {
/* 398 */       throw new IllegalArgumentException("name cannot be null");
/*     */     }
/* 400 */     for (Class<Object> c = clazz; c != Object.class; c = (Class)c.getSuperclass()) {
/* 401 */       if (instanceOf(c, name)) {
/* 402 */         return true;
/*     */       }
/*     */     } 
/* 405 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean instanceOf(Class clazz, String name) {
/* 409 */     if (name.equals(clazz.getName())) {
/* 410 */       return true;
/*     */     }
/* 412 */     boolean found = false;
/* 413 */     Class[] interfaces = clazz.getInterfaces();
/* 414 */     for (int i = 0; i < interfaces.length && !found; i++) {
/* 415 */       found = instanceOf(interfaces[i], name);
/*     */     }
/* 417 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toClassNameString(String sep, Object... objects) {
/* 423 */     if (objects.length == 0) return ""; 
/* 424 */     StringBuilder builder = new StringBuilder();
/* 425 */     for (Object object : objects) {
/* 426 */       builder.append(sep);
/* 427 */       if (object == null) {
/* 428 */         builder.append("null");
/*     */       } else {
/* 430 */         builder.append(object.getClass().getName());
/*     */       } 
/*     */     } 
/* 433 */     return builder.substring(sep.length());
/*     */   }
/*     */   
/*     */   public static String unqualify(String name) {
/* 437 */     return unqualify(name, '.');
/*     */   }
/*     */   
/*     */   public static String unqualify(String name, char sep) {
/* 441 */     return name.substring(name.lastIndexOf(sep) + 1, name.length());
/*     */   }
/*     */   
/*     */   public static String decapitalize(String name) {
/* 445 */     if (name == null)
/* 446 */       return null; 
/* 447 */     if (name.length() == 0 || (name.length() > 1 && Character.isUpperCase(name.charAt(1)))) {
/* 448 */       return name;
/*     */     }
/* 450 */     char[] chars = name.toCharArray();
/* 451 */     chars[0] = Character.toLowerCase(chars[0]);
/* 452 */     return new String(chars);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\Reflections.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */