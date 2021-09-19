/*     */ package com.sun.tools.xjc.api.impl.j2s;
/*     */ 
/*     */ import com.sun.mirror.apt.AnnotationProcessorEnvironment;
/*     */ import com.sun.mirror.apt.Messager;
/*     */ import com.sun.mirror.declaration.FieldDeclaration;
/*     */ import com.sun.mirror.declaration.MethodDeclaration;
/*     */ import com.sun.mirror.declaration.TypeDeclaration;
/*     */ import com.sun.mirror.type.TypeMirror;
/*     */ import com.sun.tools.jxc.apt.InlineAnnotationReaderImpl;
/*     */ import com.sun.tools.jxc.model.nav.APTNavigator;
/*     */ import com.sun.tools.xjc.api.J2SJAXBModel;
/*     */ import com.sun.tools.xjc.api.JavaCompiler;
/*     */ import com.sun.tools.xjc.api.Reference;
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.core.ErrorHandler;
/*     */ import com.sun.xml.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.bind.v2.model.impl.ModelBuilder;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaCompilerImpl
/*     */   implements JavaCompiler
/*     */ {
/*     */   public J2SJAXBModel bind(Collection<Reference> rootClasses, Map<QName, Reference> additionalElementDecls, String defaultNamespaceRemap, AnnotationProcessorEnvironment env) {
/*  75 */     ModelBuilder<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration> builder = new ModelBuilder((AnnotationReader)InlineAnnotationReaderImpl.theInstance, (Navigator)new APTNavigator(env), Collections.emptyMap(), defaultNamespaceRemap);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     builder.setErrorHandler(new ErrorHandlerImpl(env.getMessager()));
/*     */     
/*  84 */     for (Reference ref : rootClasses) {
/*  85 */       TypeMirror t = ref.type;
/*     */       
/*  87 */       XmlJavaTypeAdapter xjta = (XmlJavaTypeAdapter)ref.annotations.getAnnotation(XmlJavaTypeAdapter.class);
/*  88 */       XmlList xl = (XmlList)ref.annotations.getAnnotation(XmlList.class);
/*     */       
/*  90 */       builder.getTypeInfo(new Ref(builder, t, xjta, xl));
/*     */     } 
/*     */     
/*  93 */     TypeInfoSet<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration> r = builder.link();
/*  94 */     if (r == null) return null;
/*     */     
/*  96 */     if (additionalElementDecls == null) {
/*  97 */       additionalElementDecls = Collections.emptyMap();
/*     */     } else {
/*     */       
/* 100 */       for (Map.Entry<QName, ? extends Reference> e : additionalElementDecls.entrySet()) {
/* 101 */         if (e.getKey() == null)
/* 102 */           throw new IllegalArgumentException("nulls in additionalElementDecls"); 
/*     */       } 
/*     */     } 
/* 105 */     return new JAXBModelImpl(r, builder.reader, rootClasses, new HashMap<QName, Reference>(additionalElementDecls));
/*     */   }
/*     */   
/*     */   private static final class ErrorHandlerImpl implements ErrorHandler {
/*     */     private final Messager messager;
/*     */     
/*     */     public ErrorHandlerImpl(Messager messager) {
/* 112 */       this.messager = messager;
/*     */     }
/*     */     
/*     */     public void error(IllegalAnnotationException e) {
/* 116 */       this.messager.printError(e.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\j2s\JavaCompilerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */