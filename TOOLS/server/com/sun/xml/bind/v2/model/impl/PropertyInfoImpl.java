/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.TODO;
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import com.sun.xml.bind.v2.runtime.SwaRefAdapter;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlElementWrapper;
/*     */ import javax.xml.bind.annotation.XmlID;
/*     */ import javax.xml.bind.annotation.XmlIDREF;
/*     */ import javax.xml.bind.annotation.XmlInlineBinaryData;
/*     */ import javax.xml.bind.annotation.XmlMimeType;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlSchema;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
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
/*     */ abstract class PropertyInfoImpl<T, C, F, M>
/*     */   implements PropertyInfo<T, C>, Locatable, Comparable<PropertyInfoImpl>
/*     */ {
/*     */   protected final PropertySeed<T, C, F, M> seed;
/*     */   private final boolean isCollection;
/*     */   private final ID id;
/*     */   private final MimeType expectedMimeType;
/*     */   private final boolean inlineBinary;
/*     */   private final QName schemaType;
/*     */   protected final ClassInfoImpl<T, C, F, M> parent;
/*     */   private final Adapter<T, C> adapter;
/*     */   
/*     */   protected PropertyInfoImpl(ClassInfoImpl<T, C, F, M> parent, PropertySeed<T, C, F, M> spi) {
/*  95 */     this.seed = spi;
/*  96 */     this.parent = parent;
/*     */     
/*  98 */     if (parent == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 106 */       throw new AssertionError();
/*     */     }
/* 108 */     MimeType mt = Util.calcExpectedMediaType(this.seed, parent.builder);
/* 109 */     if (mt != null && !(kind()).canHaveXmlMimeType) {
/* 110 */       parent.builder.reportError(new IllegalAnnotationException(Messages.ILLEGAL_ANNOTATION.format(new Object[] { XmlMimeType.class.getName() }, ), this.seed.readAnnotation(XmlMimeType.class)));
/*     */ 
/*     */ 
/*     */       
/* 114 */       mt = null;
/*     */     } 
/* 116 */     this.expectedMimeType = mt;
/* 117 */     this.inlineBinary = this.seed.hasAnnotation(XmlInlineBinaryData.class);
/*     */     
/* 119 */     T t = this.seed.getRawType();
/*     */ 
/*     */     
/* 122 */     XmlJavaTypeAdapter xjta = getApplicableAdapter(t);
/* 123 */     if (xjta != null) {
/* 124 */       this.isCollection = false;
/* 125 */       this.adapter = new Adapter(xjta, reader(), nav());
/*     */     }
/*     */     else {
/*     */       
/* 129 */       this.isCollection = (nav().isSubClassOf(t, nav().ref(Collection.class)) || nav().isArrayButNotByteArray(t));
/*     */ 
/*     */       
/* 132 */       xjta = getApplicableAdapter(getIndividualType());
/* 133 */       if (xjta == null) {
/*     */         
/* 135 */         XmlAttachmentRef xsa = (XmlAttachmentRef)this.seed.readAnnotation(XmlAttachmentRef.class);
/* 136 */         if (xsa != null) {
/* 137 */           parent.builder.hasSwaRef = true;
/* 138 */           this.adapter = new Adapter(nav().asDecl(SwaRefAdapter.class), nav());
/*     */         } else {
/* 140 */           this.adapter = null;
/*     */ 
/*     */ 
/*     */           
/* 144 */           xjta = (XmlJavaTypeAdapter)this.seed.readAnnotation(XmlJavaTypeAdapter.class);
/* 145 */           if (xjta != null) {
/* 146 */             T adapter = (T)reader().getClassValue((Annotation)xjta, "value");
/* 147 */             parent.builder.reportError(new IllegalAnnotationException(Messages.UNMATCHABLE_ADAPTER.format(new Object[] { nav().getTypeName(adapter), nav().getTypeName(t) }, ), (Annotation)xjta));
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 155 */         this.adapter = new Adapter(xjta, reader(), nav());
/*     */       } 
/*     */     } 
/*     */     
/* 159 */     this.id = calcId();
/* 160 */     this.schemaType = Util.calcSchemaType(reader(), this.seed, parent.clazz, getIndividualType(), this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassInfoImpl<T, C, F, M> parent() {
/* 166 */     return this.parent;
/*     */   }
/*     */   
/*     */   protected final Navigator<T, C, F, M> nav() {
/* 170 */     return this.parent.nav();
/*     */   }
/*     */   protected final AnnotationReader<T, C, F, M> reader() {
/* 173 */     return this.parent.reader();
/*     */   }
/*     */   
/*     */   public T getRawType() {
/* 177 */     return this.seed.getRawType();
/*     */   }
/*     */   
/*     */   public T getIndividualType() {
/* 181 */     if (this.adapter != null)
/* 182 */       return (T)this.adapter.defaultType; 
/* 183 */     T raw = getRawType();
/* 184 */     if (!isCollection()) {
/* 185 */       return raw;
/*     */     }
/* 187 */     if (nav().isArrayButNotByteArray(raw)) {
/* 188 */       return (T)nav().getComponentType(raw);
/*     */     }
/* 190 */     T bt = (T)nav().getBaseClass(raw, nav().asDecl(Collection.class));
/* 191 */     if (nav().isParameterizedType(bt)) {
/* 192 */       return (T)nav().getTypeArgument(bt, 0);
/*     */     }
/* 194 */     return (T)nav().ref(Object.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 199 */     return this.seed.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isApplicable(XmlJavaTypeAdapter jta, T declaredType) {
/* 206 */     if (jta == null) return false;
/*     */     
/* 208 */     T type = (T)reader().getClassValue((Annotation)jta, "type");
/* 209 */     if (declaredType.equals(type)) {
/* 210 */       return true;
/*     */     }
/* 212 */     T adapter = (T)reader().getClassValue((Annotation)jta, "value");
/* 213 */     T ba = (T)nav().getBaseClass(adapter, nav().asDecl(XmlAdapter.class));
/* 214 */     if (!nav().isParameterizedType(ba))
/* 215 */       return true; 
/* 216 */     T inMemType = (T)nav().getTypeArgument(ba, 1);
/*     */     
/* 218 */     return nav().isSubClassOf(declaredType, inMemType);
/*     */   }
/*     */   
/*     */   private XmlJavaTypeAdapter getApplicableAdapter(T type) {
/* 222 */     XmlJavaTypeAdapter jta = (XmlJavaTypeAdapter)this.seed.readAnnotation(XmlJavaTypeAdapter.class);
/* 223 */     if (jta != null && isApplicable(jta, type)) {
/* 224 */       return jta;
/*     */     }
/*     */     
/* 227 */     XmlJavaTypeAdapters jtas = (XmlJavaTypeAdapters)reader().getPackageAnnotation(XmlJavaTypeAdapters.class, this.parent.clazz, this.seed);
/* 228 */     if (jtas != null)
/* 229 */       for (XmlJavaTypeAdapter xjta : jtas.value()) {
/* 230 */         if (isApplicable(xjta, type)) {
/* 231 */           return xjta;
/*     */         }
/*     */       }  
/* 234 */     jta = (XmlJavaTypeAdapter)reader().getPackageAnnotation(XmlJavaTypeAdapter.class, this.parent.clazz, this.seed);
/* 235 */     if (isApplicable(jta, type)) {
/* 236 */       return jta;
/*     */     }
/*     */     
/* 239 */     C refType = (C)nav().asDecl(type);
/* 240 */     if (refType != null) {
/* 241 */       jta = (XmlJavaTypeAdapter)reader().getClassAnnotation(XmlJavaTypeAdapter.class, refType, this.seed);
/* 242 */       if (jta != null && isApplicable(jta, type)) {
/* 243 */         return jta;
/*     */       }
/*     */     } 
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Adapter<T, C> getAdapter() {
/* 254 */     return this.adapter;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String displayName() {
/* 259 */     return nav().getClassName(this.parent.getClazz()) + '#' + getName();
/*     */   }
/*     */   
/*     */   public final ID id() {
/* 263 */     return this.id;
/*     */   }
/*     */   
/*     */   private ID calcId() {
/* 267 */     if (this.seed.hasAnnotation(XmlID.class)) {
/*     */       
/* 269 */       if (!getIndividualType().equals(nav().ref(String.class))) {
/* 270 */         this.parent.builder.reportError(new IllegalAnnotationException(Messages.ID_MUST_BE_STRING.format(new Object[] { getName() }, ), this.seed));
/*     */       }
/*     */       
/* 273 */       return ID.ID;
/*     */     } 
/* 275 */     if (this.seed.hasAnnotation(XmlIDREF.class)) {
/* 276 */       return ID.IDREF;
/*     */     }
/* 278 */     return ID.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public final MimeType getExpectedMimeType() {
/* 283 */     return this.expectedMimeType;
/*     */   }
/*     */   
/*     */   public final boolean inlineBinaryData() {
/* 287 */     return this.inlineBinary;
/*     */   }
/*     */   
/*     */   public final QName getSchemaType() {
/* 291 */     return this.schemaType;
/*     */   }
/*     */   
/*     */   public final boolean isCollection() {
/* 295 */     return this.isCollection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void link() {
/* 304 */     if (this.id == ID.IDREF)
/*     */     {
/* 306 */       for (TypeInfo<T, C> ti : (Iterable<TypeInfo<T, C>>)ref()) {
/* 307 */         if (!ti.canBeReferencedByIDREF()) {
/* 308 */           this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_IDREF.format(new Object[] { this.parent.builder.nav.getTypeName(ti.getType()) }, ), this));
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locatable getUpstream() {
/* 320 */     return this.parent;
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 324 */     return this.seed.getLocation();
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
/*     */   
/*     */   protected final QName calcXmlName(XmlElement e) {
/* 339 */     if (e != null) {
/* 340 */       return calcXmlName(e.namespace(), e.name());
/*     */     }
/* 342 */     return calcXmlName("##default", "##default");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final QName calcXmlName(XmlElementWrapper e) {
/* 349 */     if (e != null) {
/* 350 */       return calcXmlName(e.namespace(), e.name());
/*     */     }
/* 352 */     return calcXmlName("##default", "##default");
/*     */   }
/*     */ 
/*     */   
/*     */   private QName calcXmlName(String uri, String local) {
/* 357 */     TODO.checkSpec();
/* 358 */     if (local.length() == 0 || local.equals("##default"))
/* 359 */       local = this.seed.getName(); 
/* 360 */     if (uri.equals("##default")) {
/* 361 */       XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
/*     */ 
/*     */       
/* 364 */       if (xs != null) {
/* 365 */         QName typeName; switch (xs.elementFormDefault()) {
/*     */           case QUALIFIED:
/* 367 */             typeName = this.parent.getTypeName();
/* 368 */             if (typeName != null) {
/* 369 */               uri = typeName.getNamespaceURI();
/*     */             } else {
/* 371 */               uri = xs.namespace();
/* 372 */             }  if (uri.length() == 0)
/* 373 */               uri = this.parent.builder.defaultNsUri; 
/*     */             break;
/*     */           case UNQUALIFIED:
/*     */           case UNSET:
/* 377 */             uri = ""; break;
/*     */         } 
/*     */       } else {
/* 380 */         uri = "";
/*     */       } 
/*     */     } 
/* 383 */     return new QName(uri.intern(), local.intern());
/*     */   }
/*     */   
/*     */   public int compareTo(PropertyInfoImpl that) {
/* 387 */     return getName().compareTo(that.getName());
/*     */   }
/*     */   
/*     */   public final <A extends Annotation> A readAnnotation(Class<A> annotationType) {
/* 391 */     return (A)this.seed.readAnnotation(annotationType);
/*     */   }
/*     */   
/*     */   public final boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 395 */     return this.seed.hasAnnotation(annotationType);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\PropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */