/*     */ package com.sun.tools.xjc.api.impl.j2s;
/*     */ 
/*     */ import com.sun.mirror.declaration.FieldDeclaration;
/*     */ import com.sun.mirror.declaration.MethodDeclaration;
/*     */ import com.sun.mirror.declaration.TypeDeclaration;
/*     */ import com.sun.mirror.type.TypeMirror;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import com.sun.tools.xjc.api.J2SJAXBModel;
/*     */ import com.sun.tools.xjc.api.Reference;
/*     */ import com.sun.xml.bind.api.ErrorListener;
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.core.ArrayInfo;
/*     */ import com.sun.xml.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.bind.v2.model.core.Element;
/*     */ import com.sun.xml.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator;
/*     */ import com.sun.xml.txw2.output.ResultFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.SchemaOutputResolver;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Result;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JAXBModelImpl
/*     */   implements J2SJAXBModel
/*     */ {
/*     */   private final Map<QName, Reference> additionalElementDecls;
/*  81 */   private final List<String> classList = new ArrayList<String>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final TypeInfoSet<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration> types;
/*     */ 
/*     */ 
/*     */   
/*     */   private final AnnotationReader<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration> reader;
/*     */ 
/*     */ 
/*     */   
/*     */   private XmlSchemaGenerator<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration> xsdgen;
/*     */ 
/*     */   
/*  96 */   private final Map<Reference, NonElement<TypeMirror, TypeDeclaration>> refMap = new HashMap<Reference, NonElement<TypeMirror, TypeDeclaration>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBModelImpl(TypeInfoSet<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration> types, AnnotationReader<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration> reader, Collection<Reference> rootClasses, Map<QName, Reference> additionalElementDecls) {
/* 103 */     this.types = types;
/* 104 */     this.reader = reader;
/* 105 */     this.additionalElementDecls = additionalElementDecls;
/*     */     
/* 107 */     Navigator<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration> navigator = types.getNavigator();
/*     */     
/* 109 */     for (ClassInfo<TypeMirror, TypeDeclaration> i : (Iterable<ClassInfo<TypeMirror, TypeDeclaration>>)types.beans().values()) {
/* 110 */       this.classList.add(i.getName());
/*     */     }
/*     */     
/* 113 */     for (ArrayInfo<TypeMirror, TypeDeclaration> a : (Iterable<ArrayInfo<TypeMirror, TypeDeclaration>>)types.arrays().values()) {
/* 114 */       String javaName = navigator.getTypeName(a.getType());
/* 115 */       this.classList.add(javaName);
/*     */     } 
/*     */     
/* 118 */     for (EnumLeafInfo<TypeMirror, TypeDeclaration> l : (Iterable<EnumLeafInfo<TypeMirror, TypeDeclaration>>)types.enums().values()) {
/* 119 */       QName tn = l.getTypeName();
/* 120 */       if (tn != null) {
/* 121 */         String javaName = navigator.getTypeName(l.getType());
/* 122 */         this.classList.add(javaName);
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     for (Reference ref : rootClasses) {
/* 127 */       this.refMap.put(ref, getXmlType(ref));
/*     */     }
/*     */ 
/*     */     
/* 131 */     Iterator<Map.Entry<QName, Reference>> itr = additionalElementDecls.entrySet().iterator();
/* 132 */     while (itr.hasNext()) {
/* 133 */       Map.Entry<QName, Reference> entry = itr.next();
/* 134 */       if (entry.getValue() == null)
/*     */         continue; 
/* 136 */       NonElement<TypeMirror, TypeDeclaration> xt = getXmlType(entry.getValue());
/*     */       
/* 138 */       assert xt != null;
/* 139 */       this.refMap.put(entry.getValue(), xt);
/* 140 */       if (xt instanceof ClassInfo) {
/* 141 */         ClassInfo<TypeMirror, TypeDeclaration> xct = (ClassInfo<TypeMirror, TypeDeclaration>)xt;
/* 142 */         Element<TypeMirror, TypeDeclaration> elem = xct.asElement();
/* 143 */         if (elem != null && elem.getElementName().equals(entry.getKey())) {
/* 144 */           itr.remove();
/*     */           continue;
/*     */         } 
/*     */       } 
/* 148 */       ElementInfo<TypeMirror, TypeDeclaration> ei = types.getElementInfo(null, entry.getKey());
/* 149 */       if (ei != null && ei.getContentType() == xt)
/* 150 */         itr.remove(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<String> getClassList() {
/* 155 */     return this.classList;
/*     */   }
/*     */   
/*     */   public QName getXmlTypeName(Reference javaType) {
/* 159 */     NonElement<TypeMirror, TypeDeclaration> ti = this.refMap.get(javaType);
/*     */     
/* 161 */     if (ti != null) {
/* 162 */       return ti.getTypeName();
/*     */     }
/* 164 */     return null;
/*     */   }
/*     */   
/*     */   private NonElement<TypeMirror, TypeDeclaration> getXmlType(Reference r) {
/* 168 */     if (r == null) {
/* 169 */       throw new IllegalArgumentException();
/*     */     }
/* 171 */     XmlJavaTypeAdapter xjta = (XmlJavaTypeAdapter)r.annotations.getAnnotation(XmlJavaTypeAdapter.class);
/* 172 */     XmlList xl = (XmlList)r.annotations.getAnnotation(XmlList.class);
/*     */     
/* 174 */     Ref<TypeMirror, TypeDeclaration> ref = new Ref(this.reader, this.types.getNavigator(), r.type, xjta, xl);
/*     */ 
/*     */     
/* 177 */     return this.types.getTypeInfo(ref);
/*     */   }
/*     */   
/*     */   public void generateSchema(SchemaOutputResolver outputResolver, ErrorListener errorListener) throws IOException {
/* 181 */     getSchemaGenerator().write(outputResolver, (ErrorListener)errorListener);
/*     */   }
/*     */   
/*     */   public void generateEpisodeFile(Result output) {
/* 185 */     getSchemaGenerator().writeEpisodeFile(ResultFactory.createSerializer(output));
/*     */   }
/*     */   
/*     */   private synchronized XmlSchemaGenerator<TypeMirror, TypeDeclaration, FieldDeclaration, MethodDeclaration> getSchemaGenerator() {
/* 189 */     if (this.xsdgen == null) {
/* 190 */       this.xsdgen = new XmlSchemaGenerator(this.types.getNavigator(), this.types);
/*     */       
/* 192 */       for (Map.Entry<QName, Reference> e : this.additionalElementDecls.entrySet()) {
/* 193 */         Reference value = e.getValue();
/* 194 */         if (value != null) {
/* 195 */           NonElement<TypeMirror, TypeDeclaration> typeInfo = this.refMap.get(value);
/* 196 */           if (typeInfo == null)
/* 197 */             throw new IllegalArgumentException((new StringBuilder()).append(e.getValue()).append(" was not specified to JavaCompiler.bind").toString()); 
/* 198 */           this.xsdgen.add(e.getKey(), !(value.type instanceof com.sun.mirror.type.PrimitiveType), typeInfo); continue;
/*     */         } 
/* 200 */         this.xsdgen.add(e.getKey(), false, null);
/*     */       } 
/*     */     } 
/*     */     
/* 204 */     return this.xsdgen;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\impl\j2s\JAXBModelImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */