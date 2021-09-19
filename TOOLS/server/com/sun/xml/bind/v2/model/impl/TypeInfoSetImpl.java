/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.core.BuiltinLeafInfo;
/*     */ import com.sun.xml.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.bind.v2.model.core.LeafInfo;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.bind.v2.runtime.RuntimeUtil;
/*     */ import com.sun.xml.bind.v2.util.FlattenIterator;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.annotation.XmlNs;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlRegistry;
/*     */ import javax.xml.bind.annotation.XmlSchema;
/*     */ import javax.xml.bind.annotation.XmlTransient;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TypeInfoSetImpl<T, C, F, M>
/*     */   implements TypeInfoSet<T, C, F, M>
/*     */ {
/*     */   @XmlTransient
/*     */   public final Navigator<T, C, F, M> nav;
/*     */   @XmlTransient
/*     */   public final AnnotationReader<T, C, F, M> reader;
/*  92 */   private final Map<T, BuiltinLeafInfo<T, C>> builtins = new LinkedHashMap<T, BuiltinLeafInfo<T, C>>();
/*     */ 
/*     */ 
/*     */   
/*  96 */   private final Map<C, EnumLeafInfoImpl<T, C, F, M>> enums = new LinkedHashMap<C, EnumLeafInfoImpl<T, C, F, M>>();
/*     */ 
/*     */ 
/*     */   
/* 100 */   private final Map<T, ArrayInfoImpl<T, C, F, M>> arrays = new LinkedHashMap<T, ArrayInfoImpl<T, C, F, M>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlJavaTypeAdapter(RuntimeUtil.ToStringAdapter.class)
/* 112 */   private final Map<C, ClassInfoImpl<T, C, F, M>> beans = new LinkedHashMap<C, ClassInfoImpl<T, C, F, M>>();
/*     */ 
/*     */   
/*     */   @XmlTransient
/* 116 */   private final Map<C, ClassInfoImpl<T, C, F, M>> beansView = Collections.unmodifiableMap(this.beans);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   private final Map<C, Map<QName, ElementInfoImpl<T, C, F, M>>> elementMappings = new LinkedHashMap<C, Map<QName, ElementInfoImpl<T, C, F, M>>>();
/*     */ 
/*     */   
/* 125 */   private final Iterable<? extends ElementInfoImpl<T, C, F, M>> allElements = (Iterable)new Iterable<ElementInfoImpl<ElementInfoImpl<T, C, F, M>, C, F, M>>()
/*     */     {
/*     */       public Iterator<ElementInfoImpl<T, C, F, M>> iterator() {
/* 128 */         return (Iterator<ElementInfoImpl<T, C, F, M>>)new FlattenIterator(TypeInfoSetImpl.this.elementMappings.values());
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final NonElement<T, C> anyType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Map<String, String>> xmlNsCache;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeInfoSetImpl(Navigator<T, C, F, M> nav, AnnotationReader<T, C, F, M> reader, Map<T, ? extends BuiltinLeafInfoImpl<T, C>> leaves) {
/* 150 */     this.nav = nav;
/* 151 */     this.reader = reader;
/* 152 */     this.builtins.putAll((Map)leaves);
/*     */     
/* 154 */     this.anyType = createAnyType();
/*     */ 
/*     */     
/* 157 */     for (Map.Entry<Class<?>, Class<?>> e : (Iterable<Map.Entry<Class<?>, Class<?>>>)RuntimeUtil.primitiveToBox.entrySet()) {
/* 158 */       this.builtins.put((T)nav.getPrimitive(e.getKey()), leaves.get(nav.ref(e.getValue())));
/*     */     }
/*     */ 
/*     */     
/* 162 */     this.elementMappings.put(null, new LinkedHashMap<QName, ElementInfoImpl<T, C, F, M>>());
/*     */   }
/*     */   
/*     */   protected NonElement<T, C> createAnyType() {
/* 166 */     return new AnyTypeImpl<T, C>(this.nav);
/*     */   }
/*     */   
/*     */   public Navigator<T, C, F, M> getNavigator() {
/* 170 */     return this.nav;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ClassInfoImpl<T, C, F, M> ci) {
/* 177 */     this.beans.put(ci.getClazz(), ci);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(EnumLeafInfoImpl<T, C, F, M> li) {
/* 184 */     this.enums.put(li.clazz, li);
/*     */   }
/*     */   
/*     */   public void add(ArrayInfoImpl<T, C, F, M> ai) {
/* 188 */     this.arrays.put(ai.getType(), ai);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NonElement<T, C> getTypeInfo(T type) {
/* 199 */     type = (T)this.nav.erasure(type);
/*     */     
/* 201 */     LeafInfo<T, C> l = (LeafInfo<T, C>)this.builtins.get(type);
/* 202 */     if (l != null) return (NonElement<T, C>)l;
/*     */     
/* 204 */     if (this.nav.isArray(type)) {
/* 205 */       return (NonElement<T, C>)this.arrays.get(type);
/*     */     }
/*     */     
/* 208 */     C d = (C)this.nav.asDecl(type);
/* 209 */     if (d == null) return null; 
/* 210 */     return getClassInfo(d);
/*     */   }
/*     */   
/*     */   public NonElement<T, C> getAnyTypeInfo() {
/* 214 */     return this.anyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NonElement<T, C> getTypeInfo(Ref<T, C> ref) {
/* 222 */     assert !ref.valueList;
/* 223 */     C c = (C)this.nav.asDecl(ref.type);
/* 224 */     if (c != null && this.reader.getClassAnnotation(XmlRegistry.class, c, null) != null) {
/* 225 */       return null;
/*     */     }
/* 227 */     return getTypeInfo((T)ref.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, ? extends ClassInfoImpl<T, C, F, M>> beans() {
/* 234 */     return this.beansView;
/*     */   }
/*     */   
/*     */   public Map<T, ? extends BuiltinLeafInfo<T, C>> builtins() {
/* 238 */     return this.builtins;
/*     */   }
/*     */   
/*     */   public Map<C, ? extends EnumLeafInfoImpl<T, C, F, M>> enums() {
/* 242 */     return this.enums;
/*     */   }
/*     */   
/*     */   public Map<? extends T, ? extends ArrayInfoImpl<T, C, F, M>> arrays() {
/* 246 */     return this.arrays;
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
/*     */   public NonElement<T, C> getClassInfo(C type) {
/* 261 */     LeafInfo<T, C> l = (LeafInfo<T, C>)this.builtins.get(this.nav.use(type));
/* 262 */     if (l != null) return (NonElement<T, C>)l;
/*     */     
/* 264 */     l = (LeafInfo<T, C>)this.enums.get(type);
/* 265 */     if (l != null) return (NonElement<T, C>)l;
/*     */     
/* 267 */     if (this.nav.asDecl(Object.class).equals(type)) {
/* 268 */       return this.anyType;
/*     */     }
/* 270 */     return (NonElement<T, C>)this.beans.get(type);
/*     */   }
/*     */   
/*     */   public ElementInfoImpl<T, C, F, M> getElementInfo(C scope, QName name) {
/* 274 */     while (scope != null) {
/* 275 */       Map<QName, ElementInfoImpl<T, C, F, M>> m = this.elementMappings.get(scope);
/* 276 */       if (m != null) {
/* 277 */         ElementInfoImpl<T, C, F, M> r = m.get(name);
/* 278 */         if (r != null) return r; 
/*     */       } 
/* 280 */       scope = (C)this.nav.getSuperClass(scope);
/*     */     } 
/* 282 */     return (ElementInfoImpl<T, C, F, M>)((Map)this.elementMappings.get(null)).get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void add(ElementInfoImpl<T, C, F, M> ei, ModelBuilder<T, C, F, M> builder) {
/* 290 */     C scope = null;
/* 291 */     if (ei.getScope() != null) {
/* 292 */       scope = (C)ei.getScope().getClazz();
/*     */     }
/* 294 */     Map<QName, ElementInfoImpl<T, C, F, M>> m = this.elementMappings.get(scope);
/* 295 */     if (m == null) {
/* 296 */       this.elementMappings.put(scope, m = new LinkedHashMap<QName, ElementInfoImpl<T, C, F, M>>());
/*     */     }
/* 298 */     ElementInfoImpl<T, C, F, M> existing = m.put(ei.getElementName(), ei);
/*     */     
/* 300 */     if (existing != null) {
/* 301 */       QName en = ei.getElementName();
/* 302 */       builder.reportError(new IllegalAnnotationException(Messages.CONFLICTING_XML_ELEMENT_MAPPING.format(new Object[] { en.getNamespaceURI(), en.getLocalPart() }, ), ei, existing));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<QName, ? extends ElementInfoImpl<T, C, F, M>> getElementMappings(C scope) {
/* 310 */     return this.elementMappings.get(scope);
/*     */   }
/*     */   
/*     */   public Iterable<? extends ElementInfoImpl<T, C, F, M>> getAllElements() {
/* 314 */     return this.allElements;
/*     */   }
/*     */   
/*     */   public Map<String, String> getXmlNs(String namespaceUri) {
/* 318 */     if (this.xmlNsCache == null) {
/* 319 */       this.xmlNsCache = new HashMap<String, Map<String, String>>();
/*     */       
/* 321 */       for (ClassInfoImpl<T, C, F, M> ci : beans().values()) {
/* 322 */         XmlSchema xs = (XmlSchema)this.reader.getPackageAnnotation(XmlSchema.class, ci.getClazz(), null);
/* 323 */         if (xs == null) {
/*     */           continue;
/*     */         }
/* 326 */         String uri = xs.namespace();
/* 327 */         Map<String, String> m = this.xmlNsCache.get(uri);
/* 328 */         if (m == null) {
/* 329 */           this.xmlNsCache.put(uri, m = new HashMap<String, String>());
/*     */         }
/* 331 */         for (XmlNs xns : xs.xmlns()) {
/* 332 */           m.put(xns.prefix(), xns.namespaceURI());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 337 */     Map<String, String> r = this.xmlNsCache.get(namespaceUri);
/* 338 */     if (r != null) return r; 
/* 339 */     return Collections.emptyMap();
/*     */   }
/*     */   
/*     */   public Map<String, String> getSchemaLocations() {
/* 343 */     Map<String, String> r = new HashMap<String, String>();
/* 344 */     for (ClassInfoImpl<T, C, F, M> ci : beans().values()) {
/* 345 */       XmlSchema xs = (XmlSchema)this.reader.getPackageAnnotation(XmlSchema.class, ci.getClazz(), null);
/* 346 */       if (xs == null) {
/*     */         continue;
/*     */       }
/* 349 */       String loc = xs.location();
/* 350 */       if (loc.equals("##generate")) {
/*     */         continue;
/*     */       }
/* 353 */       r.put(xs.namespace(), loc);
/*     */     } 
/* 355 */     return r;
/*     */   }
/*     */   
/*     */   public final XmlNsForm getElementFormDefault(String nsUri) {
/* 359 */     for (ClassInfoImpl<T, C, F, M> ci : beans().values()) {
/* 360 */       XmlSchema xs = (XmlSchema)this.reader.getPackageAnnotation(XmlSchema.class, ci.getClazz(), null);
/* 361 */       if (xs == null) {
/*     */         continue;
/*     */       }
/* 364 */       if (!xs.namespace().equals(nsUri)) {
/*     */         continue;
/*     */       }
/* 367 */       XmlNsForm xnf = xs.elementFormDefault();
/* 368 */       if (xnf != XmlNsForm.UNSET)
/* 369 */         return xnf; 
/*     */     } 
/* 371 */     return XmlNsForm.UNSET;
/*     */   }
/*     */   
/*     */   public final XmlNsForm getAttributeFormDefault(String nsUri) {
/* 375 */     for (ClassInfoImpl<T, C, F, M> ci : beans().values()) {
/* 376 */       XmlSchema xs = (XmlSchema)this.reader.getPackageAnnotation(XmlSchema.class, ci.getClazz(), null);
/* 377 */       if (xs == null) {
/*     */         continue;
/*     */       }
/* 380 */       if (!xs.namespace().equals(nsUri)) {
/*     */         continue;
/*     */       }
/* 383 */       XmlNsForm xnf = xs.attributeFormDefault();
/* 384 */       if (xnf != XmlNsForm.UNSET)
/* 385 */         return xnf; 
/*     */     } 
/* 387 */     return XmlNsForm.UNSET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(Result out) throws JAXBException {
/* 398 */     JAXBContext context = JAXBContext.newInstance(new Class[] { getClass() });
/* 399 */     Marshaller m = context.createMarshaller();
/* 400 */     m.marshal(this, out);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\TypeInfoSetImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */