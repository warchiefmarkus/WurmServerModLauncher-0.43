/*     */ package com.sun.tools.jxc.apt;
/*     */ 
/*     */ import com.sun.mirror.declaration.AnnotationMirror;
/*     */ import com.sun.mirror.declaration.Declaration;
/*     */ import com.sun.mirror.declaration.FieldDeclaration;
/*     */ import com.sun.mirror.declaration.MethodDeclaration;
/*     */ import com.sun.mirror.declaration.ParameterDeclaration;
/*     */ import com.sun.mirror.declaration.TypeDeclaration;
/*     */ import com.sun.mirror.type.MirroredTypeException;
/*     */ import com.sun.mirror.type.MirroredTypesException;
/*     */ import com.sun.mirror.type.TypeMirror;
/*     */ import com.sun.xml.bind.v2.model.annotation.AbstractInlineAnnotationReaderImpl;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.annotation.LocatableAnnotation;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InlineAnnotationReaderImpl
/*     */   extends AbstractInlineAnnotationReaderImpl<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration>
/*     */ {
/*  67 */   public static final InlineAnnotationReaderImpl theInstance = new InlineAnnotationReaderImpl();
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getClassAnnotation(Class<A> a, TypeDeclaration clazz, Locatable srcPos) {
/*  72 */     return (A)LocatableAnnotation.create(clazz.getAnnotation(a), srcPos);
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getFieldAnnotation(Class<A> a, FieldDeclaration f, Locatable srcPos) {
/*  76 */     return (A)LocatableAnnotation.create(f.getAnnotation(a), srcPos);
/*     */   }
/*     */   
/*     */   public boolean hasFieldAnnotation(Class<? extends Annotation> annotationType, FieldDeclaration f) {
/*  80 */     return (f.getAnnotation(annotationType) != null);
/*     */   }
/*     */   
/*     */   public boolean hasClassAnnotation(TypeDeclaration clazz, Class<? extends Annotation> annotationType) {
/*  84 */     return (clazz.getAnnotation(annotationType) != null);
/*     */   }
/*     */   
/*     */   public Annotation[] getAllFieldAnnotations(FieldDeclaration field, Locatable srcPos) {
/*  88 */     return getAllAnnotations((Declaration)field, srcPos);
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> a, MethodDeclaration method, Locatable srcPos) {
/*  92 */     return (A)LocatableAnnotation.create(method.getAnnotation(a), srcPos);
/*     */   }
/*     */   
/*     */   public boolean hasMethodAnnotation(Class<? extends Annotation> a, MethodDeclaration method) {
/*  96 */     return (method.getAnnotation(a) != null);
/*     */   }
/*     */   
/*  99 */   private static final Annotation[] EMPTY_ANNOTATION = new Annotation[0];
/*     */   
/*     */   public Annotation[] getAllMethodAnnotations(MethodDeclaration method, Locatable srcPos) {
/* 102 */     return getAllAnnotations((Declaration)method, srcPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Annotation[] getAllAnnotations(Declaration decl, Locatable srcPos) {
/* 109 */     List<Annotation> r = new ArrayList<Annotation>();
/*     */     
/* 111 */     for (AnnotationMirror m : decl.getAnnotationMirrors()) {
/*     */       try {
/* 113 */         String fullName = m.getAnnotationType().getDeclaration().getQualifiedName();
/* 114 */         Class<? extends Annotation> type = getClass().getClassLoader().loadClass(fullName).asSubclass(Annotation.class);
/*     */         
/* 116 */         Annotation annotation = decl.getAnnotation(type);
/* 117 */         if (annotation != null)
/* 118 */           r.add(LocatableAnnotation.create(annotation, srcPos)); 
/* 119 */       } catch (ClassNotFoundException e) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 124 */     return r.<Annotation>toArray(EMPTY_ANNOTATION);
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getMethodParameterAnnotation(Class<A> a, MethodDeclaration m, int paramIndex, Locatable srcPos) {
/* 128 */     ParameterDeclaration[] params = (ParameterDeclaration[])m.getParameters().toArray((Object[])new ParameterDeclaration[0]);
/* 129 */     return (A)LocatableAnnotation.create(params[paramIndex].getAnnotation(a), srcPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getPackageAnnotation(Class<A> a, TypeDeclaration clazz, Locatable srcPos) {
/* 134 */     return (A)LocatableAnnotation.create(clazz.getPackage().getAnnotation(a), srcPos);
/*     */   }
/*     */   
/*     */   public TypeMirror getClassValue(Annotation a, String name) {
/*     */     try {
/* 139 */       a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
/*     */       assert false;
/* 141 */       throw new IllegalStateException("should throw a MirroredTypeException");
/* 142 */     } catch (IllegalAccessException e) {
/* 143 */       throw new IllegalAccessError(e.getMessage());
/* 144 */     } catch (InvocationTargetException e) {
/* 145 */       if (e.getCause() instanceof MirroredTypeException) {
/* 146 */         MirroredTypeException me = (MirroredTypeException)e.getCause();
/* 147 */         return me.getTypeMirror();
/*     */       } 
/*     */       
/* 150 */       throw new RuntimeException(e);
/* 151 */     } catch (NoSuchMethodException e) {
/* 152 */       throw new NoSuchMethodError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public TypeMirror[] getClassArrayValue(Annotation a, String name) {
/*     */     try {
/* 158 */       a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
/*     */       assert false;
/* 160 */       throw new IllegalStateException("should throw a MirroredTypesException");
/* 161 */     } catch (IllegalAccessException e) {
/* 162 */       throw new IllegalAccessError(e.getMessage());
/* 163 */     } catch (InvocationTargetException e) {
/* 164 */       if (e.getCause() instanceof MirroredTypesException) {
/* 165 */         MirroredTypesException me = (MirroredTypesException)e.getCause();
/* 166 */         Collection<TypeMirror> r = me.getTypeMirrors();
/* 167 */         return r.<TypeMirror>toArray(new TypeMirror[r.size()]);
/*     */       } 
/*     */       
/* 170 */       throw new RuntimeException(e);
/* 171 */     } catch (NoSuchMethodException e) {
/* 172 */       throw new NoSuchMethodError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String fullName(MethodDeclaration m) {
/* 177 */     return m.getDeclaringType().getQualifiedName() + '#' + m.getSimpleName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\apt\InlineAnnotationReaderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */