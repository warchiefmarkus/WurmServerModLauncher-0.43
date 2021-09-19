/*     */ package com.sun.tools.xjc.model.nav;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import java.lang.reflect.Type;
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
/*     */ public final class NavigatorImpl
/*     */   implements Navigator<NType, NClass, Void, Void>
/*     */ {
/*  56 */   public static final NavigatorImpl theInstance = new NavigatorImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NClass getSuperClass(NClass nClass) {
/*  62 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getBaseClass(NType nt, NClass base) {
/*  66 */     if (nt instanceof EagerNType) {
/*  67 */       EagerNType ent = (EagerNType)nt;
/*  68 */       if (base instanceof EagerNClass) {
/*  69 */         EagerNClass enc = (EagerNClass)base;
/*  70 */         return create(REFLECTION.getBaseClass(ent.t, enc.c));
/*     */       } 
/*     */       
/*  73 */       return null;
/*     */     } 
/*  75 */     if (nt instanceof NClassByJClass) {
/*  76 */       NClassByJClass nnt = (NClassByJClass)nt;
/*  77 */       if (base instanceof EagerNClass) {
/*  78 */         EagerNClass enc = (EagerNClass)base;
/*  79 */         return ref(nnt.clazz.getBaseClass(enc.c));
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getClassName(NClass nClass) {
/*  87 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getTypeName(NType type) {
/*  91 */     return type.fullName();
/*     */   }
/*     */   
/*     */   public String getClassShortName(NClass nClass) {
/*  95 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Collection<? extends Void> getDeclaredFields(NClass nClass) {
/*  99 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Void getDeclaredField(NClass clazz, String fieldName) {
/* 103 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Collection<? extends Void> getDeclaredMethods(NClass nClass) {
/* 107 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NClass getDeclaringClassForField(Void aVoid) {
/* 111 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NClass getDeclaringClassForMethod(Void aVoid) {
/* 115 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getFieldType(Void aVoid) {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getFieldName(Void aVoid) {
/* 123 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getMethodName(Void aVoid) {
/* 127 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getReturnType(Void aVoid) {
/* 131 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType[] getMethodParameters(Void aVoid) {
/* 135 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isStaticMethod(Void aVoid) {
/* 139 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isSubClassOf(NType sub, NType sup) {
/* 143 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NClass ref(Class c) {
/* 147 */     return create(c);
/*     */   }
/*     */   
/*     */   public NClass ref(JClass c) {
/* 151 */     if (c == null) return null; 
/* 152 */     return new NClassByJClass(c);
/*     */   }
/*     */   
/*     */   public NType use(NClass nc) {
/* 156 */     return nc;
/*     */   }
/*     */   
/*     */   public NClass asDecl(NType nt) {
/* 160 */     if (nt instanceof NClass) {
/* 161 */       return (NClass)nt;
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */   
/*     */   public NClass asDecl(Class c) {
/* 167 */     return ref(c);
/*     */   }
/*     */   
/*     */   public boolean isArray(NType nType) {
/* 171 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isArrayButNotByteArray(NType t) {
/* 175 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NType getComponentType(NType nType) {
/* 180 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getTypeArgument(NType nt, int i) {
/* 184 */     if (nt instanceof EagerNType) {
/* 185 */       EagerNType ent = (EagerNType)nt;
/* 186 */       return create(REFLECTION.getTypeArgument(ent.t, i));
/*     */     } 
/* 188 */     if (nt instanceof NClassByJClass) {
/* 189 */       NClassByJClass nnt = (NClassByJClass)nt;
/* 190 */       return ref(nnt.clazz.getTypeParameters().get(i));
/*     */     } 
/*     */     
/* 193 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isParameterizedType(NType nt) {
/* 197 */     if (nt instanceof EagerNType) {
/* 198 */       EagerNType ent = (EagerNType)nt;
/* 199 */       return REFLECTION.isParameterizedType(ent.t);
/*     */     } 
/* 201 */     if (nt instanceof NClassByJClass) {
/* 202 */       NClassByJClass nnt = (NClassByJClass)nt;
/* 203 */       return nnt.clazz.isParameterized();
/*     */     } 
/*     */     
/* 206 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isPrimitive(NType type) {
/* 210 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getPrimitive(Class primitiveType) {
/* 214 */     return create(primitiveType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final NType create(Type t) {
/* 219 */     if (t == null) return null; 
/* 220 */     if (t instanceof Class) {
/* 221 */       return create((Class)t);
/*     */     }
/* 223 */     return new EagerNType(t);
/*     */   }
/*     */   
/*     */   public static NClass create(Class c) {
/* 227 */     if (c == null) return null; 
/* 228 */     return new EagerNClass(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NType createParameterizedType(NClass rawType, NType... args) {
/* 236 */     return new NParameterizedType(rawType, args);
/*     */   }
/*     */   
/*     */   public static NType createParameterizedType(Class rawType, NType... args) {
/* 240 */     return new NParameterizedType(create(rawType), args);
/*     */   }
/*     */ 
/*     */   
/*     */   public Location getClassLocation(final NClass c) {
/* 245 */     return new Location() {
/*     */         public String toString() {
/* 247 */           return c.fullName();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Location getFieldLocation(Void _) {
/* 253 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public Location getMethodLocation(Void _) {
/* 257 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean hasDefaultConstructor(NClass nClass) {
/* 261 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isStaticField(Void aVoid) {
/* 265 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean isPublicMethod(Void aVoid) {
/* 269 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean isPublicField(Void aVoid) {
/* 273 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public boolean isEnum(NClass c) {
/* 277 */     return isSubClassOf(c, create(Enum.class));
/*     */   }
/*     */   
/*     */   public <T> NType erasure(NType type) {
/* 281 */     if (type instanceof NParameterizedType) {
/* 282 */       NParameterizedType pt = (NParameterizedType)type;
/* 283 */       return pt.rawType;
/*     */     } 
/* 285 */     return type;
/*     */   }
/*     */   
/*     */   public boolean isAbstract(NClass clazz) {
/* 289 */     return clazz.isAbstract();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal(NClass clazz) {
/* 297 */     return false;
/*     */   }
/*     */   
/*     */   public Void[] getEnumConstants(NClass clazz) {
/* 301 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public NType getVoidType() {
/* 305 */     return ref(void.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPackageName(NClass clazz) {
/* 310 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NClass findClass(String className, NClass referencePoint) {
/* 315 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isBridgeMethod(Void method) {
/* 319 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isOverriding(Void method, NClass clazz) {
/* 323 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isInterface(NClass clazz) {
/* 327 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isTransient(Void f) {
/* 331 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isInnerClass(NClass clazz) {
/* 335 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\nav\NavigatorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */