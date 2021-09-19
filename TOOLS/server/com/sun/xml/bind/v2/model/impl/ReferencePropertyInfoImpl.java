/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.bind.v2.model.core.Element;
/*     */ import com.sun.xml.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.core.ReferencePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.XmlAnyElement;
/*     */ import javax.xml.bind.annotation.XmlElementRef;
/*     */ import javax.xml.bind.annotation.XmlElementRefs;
/*     */ import javax.xml.bind.annotation.XmlMixed;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlSchema;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ReferencePropertyInfoImpl<T, C, F, M>
/*     */   extends ERPropertyInfoImpl<T, C, F, M>
/*     */   implements ReferencePropertyInfo<T, C>
/*     */ {
/*     */   private Set<Element<T, C>> types;
/*     */   private final boolean isMixed;
/*     */   private final WildcardMode wildcard;
/*     */   private final C domHandler;
/*     */   
/*     */   public ReferencePropertyInfoImpl(ClassInfoImpl<T, C, F, M> classInfo, PropertySeed<T, C, F, M> seed) {
/*  88 */     super(classInfo, seed);
/*     */     
/*  90 */     this.isMixed = (seed.readAnnotation(XmlMixed.class) != null);
/*     */     
/*  92 */     XmlAnyElement xae = (XmlAnyElement)seed.readAnnotation(XmlAnyElement.class);
/*  93 */     if (xae == null) {
/*  94 */       this.wildcard = null;
/*  95 */       this.domHandler = null;
/*     */     } else {
/*  97 */       this.wildcard = xae.lax() ? WildcardMode.LAX : WildcardMode.SKIP;
/*  98 */       this.domHandler = (C)nav().asDecl(reader().getClassValue((Annotation)xae, "value"));
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<? extends Element<T, C>> ref() {
/* 103 */     return getElements();
/*     */   }
/*     */   
/*     */   public PropertyKind kind() {
/* 107 */     return PropertyKind.REFERENCE;
/*     */   }
/*     */   
/*     */   public Set<? extends Element<T, C>> getElements() {
/* 111 */     if (this.types == null)
/* 112 */       calcTypes(false); 
/* 113 */     assert this.types != null;
/* 114 */     return this.types;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void calcTypes(boolean last) {
/*     */     XmlElementRef[] ann;
/* 125 */     this.types = new LinkedHashSet<Element<T, C>>();
/* 126 */     XmlElementRefs refs = (XmlElementRefs)this.seed.readAnnotation(XmlElementRefs.class);
/* 127 */     XmlElementRef ref = (XmlElementRef)this.seed.readAnnotation(XmlElementRef.class);
/*     */     
/* 129 */     if (refs != null && ref != null) {
/* 130 */       this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(new Object[] { nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), ref.annotationType().getName(), refs.annotationType().getName() }, ), (Annotation)ref, (Annotation)refs));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     if (refs != null) {
/* 138 */       ann = refs.value();
/*     */     }
/* 140 */     else if (ref != null) {
/* 141 */       ann = new XmlElementRef[] { ref };
/*     */     } else {
/* 143 */       ann = null;
/*     */     } 
/*     */     
/* 146 */     if (ann != null) {
/* 147 */       Navigator<T, C, F, M> nav = nav();
/* 148 */       AnnotationReader<T, C, F, M> reader = reader();
/*     */       
/* 150 */       T defaultType = (T)nav.ref(XmlElementRef.DEFAULT.class);
/* 151 */       C je = (C)nav.asDecl(JAXBElement.class);
/*     */       
/* 153 */       for (XmlElementRef r : ann) {
/*     */         boolean yield;
/* 155 */         T type = (T)reader.getClassValue((Annotation)r, "type");
/* 156 */         if (type.equals(defaultType)) type = (T)nav.erasure(getIndividualType()); 
/* 157 */         if (nav.getBaseClass(type, je) != null) {
/* 158 */           yield = addGenericElement(r);
/*     */         } else {
/* 160 */           yield = addAllSubtypes(type);
/*     */         } 
/* 162 */         if (last && !yield) {
/*     */ 
/*     */           
/* 165 */           if (type.equals(nav.ref(JAXBElement.class))) {
/*     */             
/* 167 */             this.parent.builder.reportError(new IllegalAnnotationException(Messages.NO_XML_ELEMENT_DECL.format(new Object[] { getEffectiveNamespaceFor(r), r.name() }, ), this));
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 173 */             this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ELEMENT_REF.format(new Object[0]), this));
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 186 */     this.types = Collections.unmodifiableSet(this.types);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean addGenericElement(XmlElementRef r) {
/* 194 */     String nsUri = getEffectiveNamespaceFor(r);
/*     */     
/* 196 */     return addGenericElement(this.parent.owner.getElementInfo(this.parent.getClazz(), new QName(nsUri, r.name())));
/*     */   }
/*     */   
/*     */   private String getEffectiveNamespaceFor(XmlElementRef r) {
/* 200 */     String nsUri = r.namespace();
/*     */     
/* 202 */     XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
/* 203 */     if (xs != null && xs.attributeFormDefault() == XmlNsForm.QUALIFIED)
/*     */     {
/*     */       
/* 206 */       if (nsUri.length() == 0) {
/* 207 */         nsUri = this.parent.builder.defaultNsUri;
/*     */       }
/*     */     }
/* 210 */     return nsUri;
/*     */   }
/*     */   
/*     */   private boolean addGenericElement(ElementInfo<T, C> ei) {
/* 214 */     if (ei == null)
/* 215 */       return false; 
/* 216 */     this.types.add(ei);
/* 217 */     for (ElementInfo<T, C> subst : (Iterable<ElementInfo<T, C>>)ei.getSubstitutionMembers())
/* 218 */       addGenericElement(subst); 
/* 219 */     return true;
/*     */   }
/*     */   
/*     */   private boolean addAllSubtypes(T type) {
/* 223 */     Navigator<T, C, F, M> nav = nav();
/*     */ 
/*     */     
/* 226 */     NonElement<T, C> t = this.parent.builder.getClassInfo((C)nav.asDecl(type), this);
/* 227 */     if (!(t instanceof ClassInfo))
/*     */     {
/* 229 */       return false;
/*     */     }
/* 231 */     boolean result = false;
/*     */     
/* 233 */     ClassInfo<T, C> c = (ClassInfo<T, C>)t;
/* 234 */     if (c.isElement()) {
/* 235 */       this.types.add(c.asElement());
/* 236 */       result = true;
/*     */     } 
/*     */ 
/*     */     
/* 240 */     for (ClassInfo<T, C> ci : (Iterable<ClassInfo<T, C>>)this.parent.owner.beans().values()) {
/* 241 */       if (ci.isElement() && nav.isSubClassOf(ci.getType(), type)) {
/* 242 */         this.types.add(ci.asElement());
/* 243 */         result = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 248 */     for (ElementInfo<T, C> ei : (Iterable<ElementInfo<T, C>>)this.parent.owner.getElementMappings(null).values()) {
/* 249 */       if (nav.isSubClassOf(ei.getType(), type)) {
/* 250 */         this.types.add(ei);
/* 251 */         result = true;
/*     */       } 
/*     */     } 
/*     */     
/* 255 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void link() {
/* 260 */     super.link();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     calcTypes(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isMixed() {
/* 271 */     return this.isMixed;
/*     */   }
/*     */   
/*     */   public final WildcardMode getWildcard() {
/* 275 */     return this.wildcard;
/*     */   }
/*     */   
/*     */   public final C getDOMHandler() {
/* 279 */     return this.domHandler;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\ReferencePropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */