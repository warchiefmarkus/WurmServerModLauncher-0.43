/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.istack.FinalArrayList;
/*     */ import com.sun.xml.bind.v2.TODO;
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationSource;
/*     */ import com.sun.xml.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.bind.v2.model.core.Element;
/*     */ import com.sun.xml.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.bind.v2.model.core.TypeRef;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import com.sun.xml.bind.v2.runtime.SwaRefAdapter;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlElementDecl;
/*     */ import javax.xml.bind.annotation.XmlID;
/*     */ import javax.xml.bind.annotation.XmlIDREF;
/*     */ import javax.xml.bind.annotation.XmlInlineBinaryData;
/*     */ import javax.xml.bind.annotation.XmlSchema;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ElementInfoImpl<T, C, F, M>
/*     */   extends TypeInfoImpl<T, C, F, M>
/*     */   implements ElementInfo<T, C>
/*     */ {
/*     */   private final QName tagName;
/*     */   private final NonElement<T, C> contentType;
/*     */   private final T tOfJAXBElementT;
/*     */   private final T elementType;
/*     */   private final ClassInfo<T, C> scope;
/*     */   private final XmlElementDecl anno;
/*     */   private ElementInfoImpl<T, C, F, M> substitutionHead;
/*     */   private FinalArrayList<ElementInfoImpl<T, C, F, M>> substitutionMembers;
/*     */   private final M method;
/*     */   private final Adapter<T, C> adapter;
/*     */   private final boolean isCollection;
/*     */   private final ID id;
/*     */   private final PropertyImpl property;
/*     */   private final MimeType expectedMimeType;
/*     */   private final boolean inlineBinary;
/*     */   private final QName schemaType;
/*     */   
/*     */   protected class PropertyImpl
/*     */     implements ElementPropertyInfo<T, C>, TypeRef<T, C>, AnnotationSource
/*     */   {
/*     */     public NonElement<T, C> getTarget() {
/* 138 */       return ElementInfoImpl.this.contentType;
/*     */     }
/*     */     public QName getTagName() {
/* 141 */       return ElementInfoImpl.this.tagName;
/*     */     }
/*     */     
/*     */     public List<? extends TypeRef<T, C>> getTypes() {
/* 145 */       return Collections.singletonList(this);
/*     */     }
/*     */     
/*     */     public List<? extends NonElement<T, C>> ref() {
/* 149 */       return Collections.singletonList(ElementInfoImpl.this.contentType);
/*     */     }
/*     */     
/*     */     public QName getXmlName() {
/* 153 */       return ElementInfoImpl.this.tagName;
/*     */     }
/*     */     
/*     */     public boolean isCollectionRequired() {
/* 157 */       return false;
/*     */     }
/*     */     
/*     */     public boolean isCollectionNillable() {
/* 161 */       return true;
/*     */     }
/*     */     
/*     */     public boolean isNillable() {
/* 165 */       return true;
/*     */     }
/*     */     
/*     */     public String getDefaultValue() {
/* 169 */       String v = ElementInfoImpl.this.anno.defaultValue();
/* 170 */       if (v.equals("\000")) {
/* 171 */         return null;
/*     */       }
/* 173 */       return v;
/*     */     }
/*     */     
/*     */     public ElementInfoImpl<T, C, F, M> parent() {
/* 177 */       return ElementInfoImpl.this;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 181 */       return "value";
/*     */     }
/*     */     
/*     */     public String displayName() {
/* 185 */       return "JAXBElement#value";
/*     */     }
/*     */     
/*     */     public boolean isCollection() {
/* 189 */       return ElementInfoImpl.this.isCollection;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isValueList() {
/* 196 */       return ElementInfoImpl.this.isCollection;
/*     */     }
/*     */     
/*     */     public boolean isRequired() {
/* 200 */       return true;
/*     */     }
/*     */     
/*     */     public PropertyKind kind() {
/* 204 */       return PropertyKind.ELEMENT;
/*     */     }
/*     */     
/*     */     public Adapter<T, C> getAdapter() {
/* 208 */       return ElementInfoImpl.this.adapter;
/*     */     }
/*     */     
/*     */     public ID id() {
/* 212 */       return ElementInfoImpl.this.id;
/*     */     }
/*     */     
/*     */     public MimeType getExpectedMimeType() {
/* 216 */       return ElementInfoImpl.this.expectedMimeType;
/*     */     }
/*     */     
/*     */     public QName getSchemaType() {
/* 220 */       return ElementInfoImpl.this.schemaType;
/*     */     }
/*     */     
/*     */     public boolean inlineBinaryData() {
/* 224 */       return ElementInfoImpl.this.inlineBinary;
/*     */     }
/*     */     
/*     */     public PropertyInfo<T, C> getSource() {
/* 228 */       return (PropertyInfo<T, C>)this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A extends Annotation> A readAnnotation(Class<A> annotationType) {
/* 237 */       return (A)ElementInfoImpl.this.reader().getMethodAnnotation(annotationType, ElementInfoImpl.this.method, ElementInfoImpl.this);
/*     */     }
/*     */     
/*     */     public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 241 */       return ElementInfoImpl.this.reader().hasMethodAnnotation(annotationType, ElementInfoImpl.this.method);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ElementInfoImpl(ModelBuilder<T, C, F, M> builder, RegistryInfoImpl<T, C, F, M> registry, M m) throws IllegalAnnotationException {
/* 251 */     super(builder, registry);
/*     */     
/* 253 */     this.method = m;
/* 254 */     this.anno = (XmlElementDecl)reader().getMethodAnnotation(XmlElementDecl.class, m, this);
/* 255 */     assert this.anno != null;
/* 256 */     assert this.anno instanceof com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */     
/* 258 */     this.elementType = (T)nav().getReturnType(m);
/* 259 */     T baseClass = (T)nav().getBaseClass(this.elementType, nav().asDecl(JAXBElement.class));
/* 260 */     if (baseClass == null) {
/* 261 */       throw new IllegalAnnotationException(Messages.XML_ELEMENT_MAPPING_ON_NON_IXMLELEMENT_METHOD.format(new Object[] { nav().getMethodName(m) }, ), this.anno);
/*     */     }
/*     */ 
/*     */     
/* 265 */     this.tagName = parseElementName(this.anno);
/* 266 */     T[] methodParams = (T[])nav().getMethodParameters(m);
/*     */ 
/*     */     
/* 269 */     Adapter<T, C> a = null;
/* 270 */     if (methodParams.length > 0) {
/* 271 */       XmlJavaTypeAdapter adapter = (XmlJavaTypeAdapter)reader().getMethodAnnotation(XmlJavaTypeAdapter.class, m, this);
/* 272 */       if (adapter != null) {
/* 273 */         a = new Adapter(adapter, reader(), nav());
/*     */       } else {
/* 275 */         XmlAttachmentRef xsa = (XmlAttachmentRef)reader().getMethodAnnotation(XmlAttachmentRef.class, m, this);
/* 276 */         if (xsa != null) {
/* 277 */           TODO.prototype("in APT swaRefAdapter isn't avaialble, so this returns null");
/* 278 */           a = new Adapter(this.owner.nav.asDecl(SwaRefAdapter.class), this.owner.nav);
/*     */         } 
/*     */       } 
/*     */     } 
/* 282 */     this.adapter = a;
/*     */ 
/*     */     
/* 285 */     this.tOfJAXBElementT = (methodParams.length > 0) ? methodParams[0] : (T)nav().getTypeArgument(baseClass, 0);
/*     */ 
/*     */ 
/*     */     
/* 289 */     if (this.adapter == null) {
/* 290 */       T list = (T)nav().getBaseClass(this.tOfJAXBElementT, nav().asDecl(List.class));
/* 291 */       if (list == null) {
/* 292 */         this.isCollection = false;
/* 293 */         this.contentType = builder.getTypeInfo(this.tOfJAXBElementT, this);
/*     */       } else {
/* 295 */         this.isCollection = true;
/* 296 */         this.contentType = builder.getTypeInfo((T)nav().getTypeArgument(list, 0), this);
/*     */       } 
/*     */     } else {
/*     */       
/* 300 */       this.contentType = builder.getTypeInfo((T)this.adapter.defaultType, this);
/* 301 */       this.isCollection = false;
/*     */     } 
/*     */ 
/*     */     
/* 305 */     T s = (T)reader().getClassValue((Annotation)this.anno, "scope");
/* 306 */     if (s.equals(nav().ref(XmlElementDecl.GLOBAL.class))) {
/* 307 */       this.scope = null;
/*     */     } else {
/*     */       
/* 310 */       NonElement<T, C> scp = builder.getClassInfo((C)nav().asDecl(s), this);
/* 311 */       if (!(scp instanceof ClassInfo)) {
/* 312 */         throw new IllegalAnnotationException(Messages.SCOPE_IS_NOT_COMPLEXTYPE.format(new Object[] { nav().getTypeName(s) }, ), this.anno);
/*     */       }
/*     */ 
/*     */       
/* 316 */       this.scope = (ClassInfo<T, C>)scp;
/*     */     } 
/*     */     
/* 319 */     this.id = calcId();
/*     */     
/* 321 */     this.property = createPropertyImpl();
/*     */     
/* 323 */     this.expectedMimeType = Util.calcExpectedMediaType(this.property, builder);
/* 324 */     this.inlineBinary = reader().hasMethodAnnotation(XmlInlineBinaryData.class, this.method);
/* 325 */     this.schemaType = Util.calcSchemaType(reader(), this.property, registry.registryClass, getContentInMemoryType(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   final QName parseElementName(XmlElementDecl e) {
/* 330 */     String local = e.name();
/* 331 */     String nsUri = e.namespace();
/* 332 */     if (nsUri.equals("##default")) {
/*     */       
/* 334 */       XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, nav().getDeclaringClassForMethod(this.method), this);
/*     */       
/* 336 */       if (xs != null) {
/* 337 */         nsUri = xs.namespace();
/*     */       } else {
/* 339 */         nsUri = this.builder.defaultNsUri;
/*     */       } 
/*     */     } 
/*     */     
/* 343 */     return new QName(nsUri.intern(), local.intern());
/*     */   }
/*     */   
/*     */   protected PropertyImpl createPropertyImpl() {
/* 347 */     return new PropertyImpl();
/*     */   }
/*     */   
/*     */   public ElementPropertyInfo<T, C> getProperty() {
/* 351 */     return this.property;
/*     */   }
/*     */   
/*     */   public NonElement<T, C> getContentType() {
/* 355 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public T getContentInMemoryType() {
/* 359 */     if (this.adapter == null) {
/* 360 */       return this.tOfJAXBElementT;
/*     */     }
/* 362 */     return (T)this.adapter.customType;
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getElementName() {
/* 367 */     return this.tagName;
/*     */   }
/*     */   
/*     */   public T getType() {
/* 371 */     return this.elementType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean canBeReferencedByIDREF() {
/* 381 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private ID calcId() {
/* 386 */     if (reader().hasMethodAnnotation(XmlID.class, this.method)) {
/* 387 */       return ID.ID;
/*     */     }
/* 389 */     if (reader().hasMethodAnnotation(XmlIDREF.class, this.method)) {
/* 390 */       return ID.IDREF;
/*     */     }
/* 392 */     return ID.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassInfo<T, C> getScope() {
/* 397 */     return this.scope;
/*     */   }
/*     */   
/*     */   public ElementInfo<T, C> getSubstitutionHead() {
/* 401 */     return this.substitutionHead;
/*     */   }
/*     */   
/*     */   public Collection<? extends ElementInfoImpl<T, C, F, M>> getSubstitutionMembers() {
/* 405 */     if (this.substitutionMembers == null) {
/* 406 */       return Collections.emptyList();
/*     */     }
/* 408 */     return (Collection<? extends ElementInfoImpl<T, C, F, M>>)this.substitutionMembers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void link() {
/* 416 */     if (this.anno.substitutionHeadName().length() != 0)
/* 417 */     { QName name = new QName(this.anno.substitutionHeadNamespace(), this.anno.substitutionHeadName());
/*     */       
/* 419 */       this.substitutionHead = this.owner.getElementInfo((C)null, name);
/* 420 */       if (this.substitutionHead == null) {
/* 421 */         this.builder.reportError(new IllegalAnnotationException(Messages.NON_EXISTENT_ELEMENT_MAPPING.format(new Object[] { name.getNamespaceURI(), name.getLocalPart() }, ), (Annotation)this.anno));
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 426 */         this.substitutionHead.addSubstitutionMember(this);
/*     */       }  }
/* 428 */     else { this.substitutionHead = null; }
/* 429 */      super.link();
/*     */   }
/*     */   
/*     */   private void addSubstitutionMember(ElementInfoImpl<T, C, F, M> child) {
/* 433 */     if (this.substitutionMembers == null)
/* 434 */       this.substitutionMembers = new FinalArrayList(); 
/* 435 */     this.substitutionMembers.add(child);
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 439 */     return nav().getMethodLocation(this.method);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\ElementInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */