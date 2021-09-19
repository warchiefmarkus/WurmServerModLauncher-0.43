/*     */ package com.sun.tools.jxc.model.nav;
/*     */ 
/*     */ import com.sun.istack.tools.APTTypeVisitor;
/*     */ import com.sun.mirror.apt.AnnotationProcessorEnvironment;
/*     */ import com.sun.mirror.declaration.ClassDeclaration;
/*     */ import com.sun.mirror.declaration.ConstructorDeclaration;
/*     */ import com.sun.mirror.declaration.Declaration;
/*     */ import com.sun.mirror.declaration.EnumConstantDeclaration;
/*     */ import com.sun.mirror.declaration.EnumDeclaration;
/*     */ import com.sun.mirror.declaration.FieldDeclaration;
/*     */ import com.sun.mirror.declaration.MemberDeclaration;
/*     */ import com.sun.mirror.declaration.MethodDeclaration;
/*     */ import com.sun.mirror.declaration.Modifier;
/*     */ import com.sun.mirror.declaration.ParameterDeclaration;
/*     */ import com.sun.mirror.declaration.TypeDeclaration;
/*     */ import com.sun.mirror.type.ArrayType;
/*     */ import com.sun.mirror.type.ClassType;
/*     */ import com.sun.mirror.type.DeclaredType;
/*     */ import com.sun.mirror.type.InterfaceType;
/*     */ import com.sun.mirror.type.PrimitiveType;
/*     */ import com.sun.mirror.type.ReferenceType;
/*     */ import com.sun.mirror.type.TypeMirror;
/*     */ import com.sun.mirror.type.TypeVariable;
/*     */ import com.sun.mirror.type.VoidType;
/*     */ import com.sun.mirror.type.WildcardType;
/*     */ import com.sun.mirror.util.Declarations;
/*     */ import com.sun.mirror.util.SourcePosition;
/*     */ import com.sun.mirror.util.TypeVisitor;
/*     */ import com.sun.mirror.util.Types;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class APTNavigator
/*     */   implements Navigator<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration>
/*     */ {
/*     */   private final AnnotationProcessorEnvironment env;
/*     */   private final PrimitiveType primitiveByte;
/*     */   
/*     */   public APTNavigator(AnnotationProcessorEnvironment env) {
/* 400 */     this.baseClassFinder = new APTTypeVisitor<TypeMirror, TypeDeclaration>() {
/*     */         public TypeMirror onClassType(ClassType type, TypeDeclaration sup) {
/* 402 */           TypeMirror r = onDeclaredType((DeclaredType)type, sup);
/* 403 */           if (r != null) return r;
/*     */ 
/*     */           
/* 406 */           if (type.getSuperclass() != null) {
/* 407 */             r = onClassType(type.getSuperclass(), sup);
/* 408 */             if (r != null) return r;
/*     */           
/*     */           } 
/* 411 */           return null;
/*     */         }
/*     */         
/*     */         protected TypeMirror onPrimitiveType(PrimitiveType type, TypeDeclaration param) {
/* 415 */           return (TypeMirror)type;
/*     */         }
/*     */         
/*     */         protected TypeMirror onVoidType(VoidType type, TypeDeclaration param) {
/* 419 */           return (TypeMirror)type;
/*     */         }
/*     */         
/*     */         public TypeMirror onInterfaceType(InterfaceType type, TypeDeclaration sup) {
/* 423 */           return onDeclaredType((DeclaredType)type, sup);
/*     */         }
/*     */ 
/*     */         
/*     */         private TypeMirror onDeclaredType(DeclaredType t, TypeDeclaration sup) {
/* 428 */           if (t.getDeclaration().equals(sup)) {
/* 429 */             return (TypeMirror)t;
/*     */           }
/* 431 */           for (InterfaceType i : t.getSuperinterfaces()) {
/* 432 */             TypeMirror r = onInterfaceType(i, sup);
/* 433 */             if (r != null) return r;
/*     */           
/*     */           } 
/* 436 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public TypeMirror onTypeVariable(TypeVariable t, TypeDeclaration sup) {
/* 442 */           for (ReferenceType r : t.getDeclaration().getBounds()) {
/* 443 */             TypeMirror m = (TypeMirror)apply((TypeMirror)r, sup);
/* 444 */             if (m != null) return m; 
/*     */           } 
/* 446 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public TypeMirror onArrayType(ArrayType type, TypeDeclaration sup) {
/* 453 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public TypeMirror onWildcard(WildcardType type, TypeDeclaration sup) {
/* 459 */           for (ReferenceType r : type.getLowerBounds()) {
/* 460 */             TypeMirror m = (TypeMirror)apply((TypeMirror)r, sup);
/* 461 */             if (m != null) return m; 
/*     */           } 
/* 463 */           return null;
/*     */         }
/*     */       }; this.env = env; this.primitiveByte = env.getTypeUtils().getPrimitiveType(PrimitiveType.Kind.BYTE);
/*     */   }
/*     */   public TypeDeclaration getSuperClass(TypeDeclaration t) { if (t instanceof ClassDeclaration) { ClassDeclaration c = (ClassDeclaration)t; ClassType sup = c.getSuperclass(); if (sup != null) return (TypeDeclaration)sup.getDeclaration();  return null; }  return this.env.getTypeDeclaration(Object.class.getName()); }
/*     */   public TypeMirror getBaseClass(TypeMirror type, TypeDeclaration sup) { return (TypeMirror)this.baseClassFinder.apply(type, sup); }
/* 469 */   public String getClassName(TypeDeclaration t) { return t.getQualifiedName(); } public String getTypeName(TypeMirror typeMirror) { return typeMirror.toString(); } public String getClassShortName(TypeDeclaration t) { return t.getSimpleName(); } public Collection<FieldDeclaration> getDeclaredFields(TypeDeclaration c) { List<FieldDeclaration> l = new ArrayList<FieldDeclaration>(c.getFields()); return sort(l); } public FieldDeclaration getDeclaredField(TypeDeclaration clazz, String fieldName) { for (FieldDeclaration fd : clazz.getFields()) { if (fd.getSimpleName().equals(fieldName)) return fd;  }  return null; } public Collection<MethodDeclaration> getDeclaredMethods(TypeDeclaration c) { List<MethodDeclaration> l = new ArrayList<MethodDeclaration>(c.getMethods()); return sort(l); } private <A extends Declaration> List<A> sort(List<A> l) { if (l.isEmpty()) return l;  SourcePosition pos = ((Declaration)l.get(0)).getPosition(); if (pos != null) { Collections.sort(l, (Comparator)SOURCE_POS_COMPARATOR); } else { Collections.reverse(l); }  return l; } public ClassDeclaration getDeclaringClassForField(FieldDeclaration f) { return (ClassDeclaration)f.getDeclaringType(); } public ClassDeclaration getDeclaringClassForMethod(MethodDeclaration m) { return (ClassDeclaration)m.getDeclaringType(); } public TypeMirror getFieldType(FieldDeclaration f) { return f.getType(); } public String getFieldName(FieldDeclaration f) { return f.getSimpleName(); } public String getMethodName(MethodDeclaration m) { return m.getSimpleName(); } public TypeMirror getReturnType(MethodDeclaration m) { return m.getReturnType(); } public TypeMirror[] getMethodParameters(MethodDeclaration m) { Collection<ParameterDeclaration> ps = m.getParameters(); TypeMirror[] r = new TypeMirror[ps.size()]; int i = 0; for (ParameterDeclaration p : ps) r[i++] = p.getType();  return r; } public boolean isStaticMethod(MethodDeclaration m) { return hasModifier((Declaration)m, Modifier.STATIC); } private boolean hasModifier(Declaration d, Modifier mod) { return d.getModifiers().contains(mod); } public boolean isSubClassOf(TypeMirror sub, TypeMirror sup) { if (sup == DUMMY) return false;  return this.env.getTypeUtils().isSubtype(sub, sup); } private String getSourceClassName(Class clazz) { Class<?> d = clazz.getDeclaringClass(); if (d == null) return clazz.getName();  String shortName = clazz.getName().substring(d.getName().length() + 1); return getSourceClassName(d) + '.' + shortName; } public TypeMirror ref(Class c) { if (c.isArray()) return (TypeMirror)this.env.getTypeUtils().getArrayType(ref(c.getComponentType()));  if (c.isPrimitive()) return getPrimitive(c);  TypeDeclaration t = this.env.getTypeDeclaration(getSourceClassName(c)); if (t == null) return DUMMY;  return (TypeMirror)this.env.getTypeUtils().getDeclaredType(t, new TypeMirror[0]); } public TypeMirror use(TypeDeclaration t) { assert t != null; return (TypeMirror)this.env.getTypeUtils().getDeclaredType(t, new TypeMirror[0]); } public TypeDeclaration asDecl(TypeMirror m) { m = this.env.getTypeUtils().getErasure(m); if (m instanceof DeclaredType) { DeclaredType d = (DeclaredType)m; return d.getDeclaration(); }  return null; } public Location getClassLocation(TypeDeclaration decl) { return getLocation(decl.getQualifiedName(), decl.getPosition()); }
/*     */   public TypeDeclaration asDecl(Class c) { return this.env.getTypeDeclaration(getSourceClassName(c)); }
/*     */   public <T> TypeMirror erasure(TypeMirror t) { Types tu = this.env.getTypeUtils(); t = tu.getErasure(t); if (t instanceof DeclaredType) { DeclaredType dt = (DeclaredType)t; if (!dt.getActualTypeArguments().isEmpty()) return (TypeMirror)tu.getDeclaredType(dt.getDeclaration(), new TypeMirror[0]);  }  return t; } public boolean isAbstract(TypeDeclaration clazz) { return hasModifier((Declaration)clazz, Modifier.ABSTRACT); } public boolean isFinal(TypeDeclaration clazz) { return hasModifier((Declaration)clazz, Modifier.FINAL); } public FieldDeclaration[] getEnumConstants(TypeDeclaration clazz) { EnumDeclaration ed = (EnumDeclaration)clazz; Collection<EnumConstantDeclaration> constants = ed.getEnumConstants(); return (FieldDeclaration[])constants.toArray((Object[])new EnumConstantDeclaration[constants.size()]); } public TypeMirror getVoidType() { return (TypeMirror)this.env.getTypeUtils().getVoidType(); } public String getPackageName(TypeDeclaration clazz) { return clazz.getPackage().getQualifiedName(); } public TypeDeclaration findClass(String className, TypeDeclaration referencePoint) { return this.env.getTypeDeclaration(className); } public boolean isBridgeMethod(MethodDeclaration method) { return method.getModifiers().contains(Modifier.VOLATILE); } public boolean isOverriding(MethodDeclaration method, TypeDeclaration base) { ClassDeclaration sc = (ClassDeclaration)base; Declarations declUtil = this.env.getDeclarationUtils(); while (true) { for (MethodDeclaration m : sc.getMethods()) { if (declUtil.overrides(method, m)) return true;  }  if (sc.getSuperclass() == null) return false;  sc = sc.getSuperclass().getDeclaration(); }  } public boolean isInterface(TypeDeclaration clazz) { return clazz instanceof com.sun.mirror.declaration.InterfaceDeclaration; } public boolean isTransient(FieldDeclaration f) { return f.getModifiers().contains(Modifier.TRANSIENT); } public boolean isInnerClass(TypeDeclaration clazz) { return (clazz.getDeclaringType() != null && !clazz.getModifiers().contains(Modifier.STATIC)); } public boolean isArray(TypeMirror t) { return t instanceof ArrayType; } public boolean isArrayButNotByteArray(TypeMirror t) { if (!isArray(t)) return false;  ArrayType at = (ArrayType)t; TypeMirror ct = at.getComponentType(); return !ct.equals(this.primitiveByte); } public TypeMirror getComponentType(TypeMirror t) { if (t instanceof ArrayType) { ArrayType at = (ArrayType)t; return at.getComponentType(); }  throw new IllegalArgumentException(); } public TypeMirror getTypeArgument(TypeMirror typeMirror, int i) { if (typeMirror instanceof DeclaredType) { DeclaredType d = (DeclaredType)typeMirror; TypeMirror[] args = (TypeMirror[])d.getActualTypeArguments().toArray((Object[])new TypeMirror[0]); return args[i]; }  throw new IllegalArgumentException(); } public boolean isParameterizedType(TypeMirror t) { if (t instanceof DeclaredType) { DeclaredType d = (DeclaredType)t; return !d.getActualTypeArguments().isEmpty(); }  return false; } public boolean isPrimitive(TypeMirror t) { return t instanceof PrimitiveType; } private static final Map<Class, PrimitiveType.Kind> primitives = (Map)new HashMap<Class<?>, PrimitiveType.Kind>(); static { primitives.put(int.class, PrimitiveType.Kind.INT); primitives.put(byte.class, PrimitiveType.Kind.BYTE); primitives.put(float.class, PrimitiveType.Kind.FLOAT); primitives.put(boolean.class, PrimitiveType.Kind.BOOLEAN); primitives.put(short.class, PrimitiveType.Kind.SHORT); primitives.put(long.class, PrimitiveType.Kind.LONG); primitives.put(double.class, PrimitiveType.Kind.DOUBLE); primitives.put(char.class, PrimitiveType.Kind.CHAR); } public TypeMirror getPrimitive(Class<void> primitiveType) { assert primitiveType.isPrimitive(); if (primitiveType == void.class) return getVoidType();  return (TypeMirror)this.env.getTypeUtils().getPrimitiveType(primitives.get(primitiveType)); } private static final TypeMirror DUMMY = new TypeMirror() {
/*     */       public void accept(TypeVisitor v) { throw new IllegalStateException(); }
/* 473 */     }; private final APTTypeVisitor<TypeMirror, TypeDeclaration> baseClassFinder; public Location getFieldLocation(FieldDeclaration decl) { return getLocation((MemberDeclaration)decl); }
/*     */ 
/*     */   
/*     */   public Location getMethodLocation(MethodDeclaration decl) {
/* 477 */     return getLocation((MemberDeclaration)decl);
/*     */   }
/*     */   
/*     */   public boolean hasDefaultConstructor(TypeDeclaration t) {
/* 481 */     if (!(t instanceof ClassDeclaration)) {
/* 482 */       return false;
/*     */     }
/* 484 */     ClassDeclaration c = (ClassDeclaration)t;
/* 485 */     for (ConstructorDeclaration init : c.getConstructors()) {
/* 486 */       if (init.getParameters().isEmpty())
/* 487 */         return true; 
/*     */     } 
/* 489 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isStaticField(FieldDeclaration f) {
/* 493 */     return hasModifier((Declaration)f, Modifier.STATIC);
/*     */   }
/*     */   
/*     */   public boolean isPublicMethod(MethodDeclaration m) {
/* 497 */     return hasModifier((Declaration)m, Modifier.PUBLIC);
/*     */   }
/*     */   
/*     */   public boolean isPublicField(FieldDeclaration f) {
/* 501 */     return hasModifier((Declaration)f, Modifier.PUBLIC);
/*     */   }
/*     */   
/*     */   public boolean isEnum(TypeDeclaration t) {
/* 505 */     return t instanceof EnumDeclaration;
/*     */   }
/*     */   
/*     */   private Location getLocation(MemberDeclaration decl) {
/* 509 */     return getLocation(decl.getDeclaringType().getQualifiedName() + '.' + decl.getSimpleName(), decl.getPosition());
/*     */   }
/*     */   
/*     */   private Location getLocation(final String name, final SourcePosition sp) {
/* 513 */     return new Location() {
/*     */         public String toString() {
/* 515 */           if (sp == null) {
/* 516 */             return name + " (Unknown Source)";
/*     */           }
/*     */ 
/*     */           
/* 520 */           return name + '(' + sp.file().getName() + ':' + sp.line() + ')';
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 528 */   private static final Comparator<Declaration> SOURCE_POS_COMPARATOR = new Comparator<Declaration>() {
/*     */       public int compare(Declaration d1, Declaration d2) {
/* 530 */         if (d1 == d2) {
/* 531 */           return 0;
/*     */         }
/* 533 */         SourcePosition p1 = d1.getPosition();
/* 534 */         SourcePosition p2 = d2.getPosition();
/*     */         
/* 536 */         if (p1 == null) {
/* 537 */           return (p2 == null) ? 0 : 1;
/*     */         }
/* 539 */         if (p2 == null) {
/* 540 */           return -1;
/*     */         }
/* 542 */         int fileComp = p1.file().compareTo(p2.file());
/* 543 */         if (fileComp == 0) {
/* 544 */           long diff = p1.line() - p2.line();
/* 545 */           if (diff == 0L) {
/* 546 */             diff = Long.signum(p1.column() - p2.column());
/* 547 */             if (diff != 0L) {
/* 548 */               return (int)diff;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 553 */             return Long.signum(System.identityHashCode(d1) - System.identityHashCode(d2));
/*     */           } 
/*     */ 
/*     */           
/* 557 */           return (diff < 0L) ? -1 : 1;
/*     */         } 
/* 559 */         return fileComp;
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\model\nav\APTNavigator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */