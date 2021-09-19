/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.istack.Pool;
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.api.Bridge;
/*     */ import com.sun.xml.bind.api.BridgeContext;
/*     */ import com.sun.xml.bind.api.CompositeStructure;
/*     */ import com.sun.xml.bind.api.ErrorListener;
/*     */ import com.sun.xml.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.bind.api.RawAccessor;
/*     */ import com.sun.xml.bind.api.TypeReference;
/*     */ import com.sun.xml.bind.unmarshaller.DOMScanner;
/*     */ import com.sun.xml.bind.unmarshaller.InfosetScanner;
/*     */ import com.sun.xml.bind.util.Which;
/*     */ import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
/*     */ import com.sun.xml.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl;
/*     */ import com.sun.xml.bind.v2.model.impl.RuntimeModelBuilder;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.model.nav.ReflectionNavigator;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeArrayInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeClassInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeElementInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeEnumLeafInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeLeafInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfoSet;
/*     */ import com.sun.xml.bind.v2.runtime.output.Encoded;
/*     */ import com.sun.xml.bind.v2.runtime.property.AttributeProperty;
/*     */ import com.sun.xml.bind.v2.runtime.property.Property;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.TagName;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator;
/*     */ import com.sun.xml.bind.v2.util.EditDistance;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import com.sun.xml.txw2.output.ResultFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.xml.bind.Binder;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.JAXBIntrospector;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.SchemaOutputResolver;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.Validator;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.sax.SAXTransformerFactory;
/*     */ import javax.xml.transform.sax.TransformerHandler;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JAXBContextImpl
/*     */   extends JAXBRIContext
/*     */ {
/*     */   private final Map<TypeReference, Bridge> bridges;
/*     */   private static SAXTransformerFactory tf;
/*     */   private static DocumentBuilder db;
/*     */   private final QNameMap<JaxBeanInfo> rootMap;
/*     */   private final HashMap<QName, JaxBeanInfo> typeMap;
/*     */   private final Map<Class, JaxBeanInfo> beanInfoMap;
/*     */   protected Map<RuntimeTypeInfo, JaxBeanInfo> beanInfos;
/*     */   private final Map<Class, Map<QName, ElementBeanInfoImpl>> elements;
/*     */   public final Pool<Marshaller> marshallerPool;
/*     */   public final Pool<Unmarshaller> unmarshallerPool;
/*     */   public NameBuilder nameBuilder;
/*     */   public final NameList nameList;
/*     */   private final String defaultNsUri;
/*     */   private final Class[] classes;
/*     */   protected final boolean c14nSupport;
/*     */   public final boolean xmlAccessorFactorySupport;
/*     */   public final boolean allNillable;
/*     */   private WeakReference<RuntimeTypeInfoSet> typeInfoSetCache;
/*     */   @NotNull
/*     */   private RuntimeAnnotationReader annotaitonReader;
/*     */   private boolean hasSwaRef;
/*     */   @NotNull
/*     */   private final Map<Class, Class> subclassReplacements;
/*     */   public final boolean fastBoot;
/*     */   private Encoded[] utf8nameTable;
/*     */   
/*     */   public JAXBContextImpl(Class[] classes, Collection<TypeReference> typeRefs, Map<Class<?>, Class<?>> subclassReplacements, String defaultNsUri, boolean c14nSupport, @Nullable RuntimeAnnotationReader ar, boolean xmlAccessorFactorySupport, boolean allNillable) throws JAXBException {
/*     */     RuntimeInlineAnnotationReader runtimeInlineAnnotationReader;
/*     */     boolean bool;
/* 143 */     this.bridges = new LinkedHashMap<TypeReference, Bridge>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     this.rootMap = new QNameMap();
/* 160 */     this.typeMap = new HashMap<QName, JaxBeanInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     this.beanInfoMap = (Map)new LinkedHashMap<Class<?>, JaxBeanInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     this.beanInfos = new LinkedHashMap<RuntimeTypeInfo, JaxBeanInfo>();
/*     */     
/* 178 */     this.elements = (Map)new LinkedHashMap<Class<?>, Map<QName, ElementBeanInfoImpl>>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     this.marshallerPool = (Pool<Marshaller>)new Pool.Impl<Marshaller>() { @NotNull
/*     */         protected Marshaller create() {
/* 185 */           return (Marshaller)JAXBContextImpl.this.createMarshaller();
/*     */         } }
/*     */       ;
/*     */     
/* 189 */     this.unmarshallerPool = (Pool<Unmarshaller>)new Pool.Impl<Unmarshaller>() { @NotNull
/*     */         protected Unmarshaller create() {
/* 191 */           return (Unmarshaller)JAXBContextImpl.this.createUnmarshaller();
/*     */         } }
/*     */       ;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     this.nameBuilder = new NameBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     DatatypeConverter.setDatatypeConverter(DatatypeConverterImpl.theInstance);
/*     */     
/* 260 */     if (defaultNsUri == null) defaultNsUri = "";
/*     */     
/* 262 */     if (ar == null) {
/* 263 */       runtimeInlineAnnotationReader = new RuntimeInlineAnnotationReader();
/*     */     }
/* 265 */     if (subclassReplacements == null) subclassReplacements = Collections.emptyMap(); 
/* 266 */     if (typeRefs == null) typeRefs = Collections.emptyList();
/*     */     
/* 268 */     this.annotaitonReader = (RuntimeAnnotationReader)runtimeInlineAnnotationReader;
/* 269 */     this.subclassReplacements = subclassReplacements;
/*     */ 
/*     */     
/*     */     try {
/* 273 */       bool = Boolean.getBoolean(JAXBContextImpl.class.getName() + ".fastBoot");
/* 274 */     } catch (SecurityException e) {
/* 275 */       bool = false;
/*     */     } 
/* 277 */     this.fastBoot = bool;
/*     */     
/* 279 */     this.defaultNsUri = defaultNsUri;
/* 280 */     this.c14nSupport = c14nSupport;
/* 281 */     this.xmlAccessorFactorySupport = xmlAccessorFactorySupport;
/* 282 */     this.allNillable = allNillable;
/* 283 */     this.classes = new Class[classes.length];
/* 284 */     System.arraycopy(classes, 0, this.classes, 0, classes.length);
/*     */     
/* 286 */     RuntimeTypeInfoSet typeSet = getTypeInfoSet();
/*     */ 
/*     */ 
/*     */     
/* 290 */     this.elements.put(null, new LinkedHashMap<QName, ElementBeanInfoImpl>());
/*     */ 
/*     */     
/* 293 */     for (RuntimeBuiltinLeafInfoImpl runtimeBuiltinLeafInfoImpl : RuntimeBuiltinLeafInfoImpl.builtinBeanInfos) {
/* 294 */       LeafBeanInfoImpl<?> bi = new LeafBeanInfoImpl(this, (RuntimeLeafInfo)runtimeBuiltinLeafInfoImpl);
/* 295 */       this.beanInfoMap.put(runtimeBuiltinLeafInfoImpl.getClazz(), bi);
/* 296 */       for (QName t : bi.getTypeNames()) {
/* 297 */         this.typeMap.put(t, bi);
/*     */       }
/*     */     } 
/* 300 */     for (RuntimeEnumLeafInfo e : typeSet.enums().values()) {
/* 301 */       JaxBeanInfo<?> bi = getOrCreate(e);
/* 302 */       for (QName qn : bi.getTypeNames())
/* 303 */         this.typeMap.put(qn, bi); 
/* 304 */       if (e.isElement()) {
/* 305 */         this.rootMap.put(e.getElementName(), bi);
/*     */       }
/*     */     } 
/* 308 */     for (RuntimeArrayInfo a : typeSet.arrays().values()) {
/* 309 */       JaxBeanInfo<?> ai = getOrCreate(a);
/* 310 */       for (QName qn : ai.getTypeNames()) {
/* 311 */         this.typeMap.put(qn, ai);
/*     */       }
/*     */     } 
/* 314 */     for (RuntimeClassInfo ci : typeSet.beans().values()) {
/* 315 */       ClassBeanInfoImpl<?> bi = getOrCreate(ci);
/*     */       
/* 317 */       if (bi.isElement()) {
/* 318 */         this.rootMap.put(ci.getElementName(), bi);
/*     */       }
/* 320 */       for (QName qn : bi.getTypeNames()) {
/* 321 */         this.typeMap.put(qn, bi);
/*     */       }
/*     */     } 
/*     */     
/* 325 */     for (RuntimeElementInfo n : typeSet.getAllElements()) {
/* 326 */       ElementBeanInfoImpl bi = getOrCreate(n);
/* 327 */       if (n.getScope() == null) {
/* 328 */         this.rootMap.put(n.getElementName(), bi);
/*     */       }
/* 330 */       RuntimeClassInfo scope = n.getScope();
/* 331 */       Class scopeClazz = (scope == null) ? null : (Class)scope.getClazz();
/* 332 */       Map<QName, ElementBeanInfoImpl> m = this.elements.get(scopeClazz);
/* 333 */       if (m == null) {
/* 334 */         m = new LinkedHashMap<QName, ElementBeanInfoImpl>();
/* 335 */         this.elements.put(scopeClazz, m);
/*     */       } 
/* 337 */       m.put(n.getElementName(), bi);
/*     */     } 
/*     */ 
/*     */     
/* 341 */     this.beanInfoMap.put(JAXBElement.class, new ElementBeanInfoImpl(this));
/*     */     
/* 343 */     this.beanInfoMap.put(CompositeStructure.class, new CompositeStructureBeanInfo(this));
/*     */     
/* 345 */     getOrCreate((RuntimeTypeInfo)typeSet.getAnyTypeInfo());
/*     */ 
/*     */     
/* 348 */     for (JaxBeanInfo bi : this.beanInfos.values()) {
/* 349 */       bi.link(this);
/*     */     }
/*     */     
/* 352 */     for (Map.Entry<Class<?>, Class<?>> e : RuntimeUtil.primitiveToBox.entrySet()) {
/* 353 */       this.beanInfoMap.put(e.getKey(), this.beanInfoMap.get(e.getValue()));
/*     */     }
/*     */     
/* 356 */     ReflectionNavigator nav = typeSet.getNavigator();
/*     */     
/* 358 */     for (TypeReference tr : typeRefs) {
/* 359 */       InternalBridge<?> bridge; XmlJavaTypeAdapter xjta = (XmlJavaTypeAdapter)tr.get(XmlJavaTypeAdapter.class);
/* 360 */       Adapter<Type, Class<?>> a = null;
/* 361 */       XmlList xl = (XmlList)tr.get(XmlList.class);
/*     */ 
/*     */       
/* 364 */       Class<?> erasedType = nav.erasure(tr.type);
/*     */       
/* 366 */       if (xjta != null) {
/* 367 */         a = new Adapter(xjta.value(), (Navigator)nav);
/*     */       }
/* 369 */       if (tr.get(XmlAttachmentRef.class) != null) {
/* 370 */         a = new Adapter(SwaRefAdapter.class, (Navigator)nav);
/* 371 */         this.hasSwaRef = true;
/*     */       } 
/*     */       
/* 374 */       if (a != null) {
/* 375 */         erasedType = nav.erasure((Type)a.defaultType);
/*     */       }
/*     */       
/* 378 */       Name name = this.nameBuilder.createElementName(tr.tagName);
/*     */ 
/*     */       
/* 381 */       if (xl == null) {
/* 382 */         bridge = new BridgeImpl(this, name, getBeanInfo(erasedType, true), tr);
/*     */       } else {
/* 384 */         bridge = new BridgeImpl(this, name, new ValueListBeanInfoImpl(this, erasedType), tr);
/*     */       } 
/* 386 */       if (a != null) {
/* 387 */         bridge = new BridgeAdapter<Object, Object>(bridge, (Class<? extends XmlAdapter<?, ?>>)a.adapterType);
/*     */       }
/* 389 */       this.bridges.put(tr, bridge);
/*     */     } 
/*     */ 
/*     */     
/* 393 */     this.nameList = this.nameBuilder.conclude();
/*     */     
/* 395 */     for (JaxBeanInfo bi : this.beanInfos.values()) {
/* 396 */       bi.wrapUp();
/*     */     }
/*     */     
/* 399 */     this.nameBuilder = null;
/* 400 */     this.beanInfos = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSwaRef() {
/* 407 */     return this.hasSwaRef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RuntimeTypeInfoSet getTypeInfoSet() throws IllegalAnnotationsException {
/* 416 */     if (this.typeInfoSetCache != null) {
/* 417 */       RuntimeTypeInfoSet runtimeTypeInfoSet = this.typeInfoSetCache.get();
/* 418 */       if (runtimeTypeInfoSet != null) {
/* 419 */         return runtimeTypeInfoSet;
/*     */       }
/*     */     } 
/* 422 */     RuntimeModelBuilder builder = new RuntimeModelBuilder(this, this.annotaitonReader, this.subclassReplacements, this.defaultNsUri);
/*     */     
/* 424 */     IllegalAnnotationsException.Builder errorHandler = new IllegalAnnotationsException.Builder();
/* 425 */     builder.setErrorHandler(errorHandler);
/*     */     
/* 427 */     for (Class<CompositeStructure> c : this.classes) {
/* 428 */       if (c != CompositeStructure.class)
/*     */       {
/*     */ 
/*     */         
/* 432 */         builder.getTypeInfo(new Ref(c));
/*     */       }
/*     */     } 
/* 435 */     this.hasSwaRef |= builder.hasSwaRef;
/* 436 */     RuntimeTypeInfoSet r = builder.link();
/*     */     
/* 438 */     errorHandler.check();
/* 439 */     assert r != null : "if no error was reported, the link must be a success";
/*     */     
/* 441 */     this.typeInfoSetCache = new WeakReference<RuntimeTypeInfoSet>(r);
/*     */     
/* 443 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementBeanInfoImpl getElement(Class scope, QName name) {
/* 448 */     Map<QName, ElementBeanInfoImpl> m = this.elements.get(scope);
/* 449 */     if (m != null) {
/* 450 */       ElementBeanInfoImpl bi = m.get(name);
/* 451 */       if (bi != null)
/* 452 */         return bi; 
/*     */     } 
/* 454 */     m = this.elements.get(null);
/* 455 */     return m.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ElementBeanInfoImpl getOrCreate(RuntimeElementInfo rei) {
/* 463 */     JaxBeanInfo bi = this.beanInfos.get(rei);
/* 464 */     if (bi != null) return (ElementBeanInfoImpl)bi;
/*     */ 
/*     */     
/* 467 */     return new ElementBeanInfoImpl(this, rei);
/*     */   }
/*     */   
/*     */   protected JaxBeanInfo getOrCreate(RuntimeEnumLeafInfo eli) {
/* 471 */     JaxBeanInfo bi = this.beanInfos.get(eli);
/* 472 */     if (bi != null) return bi; 
/* 473 */     bi = new LeafBeanInfoImpl(this, (RuntimeLeafInfo)eli);
/* 474 */     this.beanInfoMap.put(bi.jaxbType, bi);
/* 475 */     return bi;
/*     */   }
/*     */   
/*     */   protected ClassBeanInfoImpl getOrCreate(RuntimeClassInfo ci) {
/* 479 */     ClassBeanInfoImpl bi = (ClassBeanInfoImpl)this.beanInfos.get(ci);
/* 480 */     if (bi != null) return bi; 
/* 481 */     bi = new ClassBeanInfoImpl(this, ci);
/* 482 */     this.beanInfoMap.put(bi.jaxbType, bi);
/* 483 */     return bi;
/*     */   }
/*     */   
/*     */   protected JaxBeanInfo getOrCreate(RuntimeArrayInfo ai) {
/* 487 */     JaxBeanInfo abi = this.beanInfos.get(ai);
/* 488 */     if (abi != null) return abi;
/*     */     
/* 490 */     abi = new ArrayBeanInfoImpl(this, ai);
/*     */     
/* 492 */     this.beanInfoMap.put(ai.getType(), abi);
/* 493 */     return abi;
/*     */   }
/*     */   
/*     */   public JaxBeanInfo getOrCreate(RuntimeTypeInfo e) {
/* 497 */     if (e instanceof RuntimeElementInfo)
/* 498 */       return getOrCreate((RuntimeElementInfo)e); 
/* 499 */     if (e instanceof RuntimeClassInfo)
/* 500 */       return getOrCreate((RuntimeClassInfo)e); 
/* 501 */     if (e instanceof RuntimeLeafInfo) {
/* 502 */       JaxBeanInfo bi = this.beanInfos.get(e);
/* 503 */       assert bi != null;
/* 504 */       return bi;
/*     */     } 
/* 506 */     if (e instanceof RuntimeArrayInfo)
/* 507 */       return getOrCreate((RuntimeArrayInfo)e); 
/* 508 */     if (e.getType() == Object.class) {
/*     */       
/* 510 */       JaxBeanInfo bi = this.beanInfoMap.get(Object.class);
/* 511 */       if (bi == null) {
/* 512 */         bi = new AnyTypeBeanInfo(this, e);
/* 513 */         this.beanInfoMap.put(Object.class, bi);
/*     */       } 
/* 515 */       return bi;
/*     */     } 
/*     */     
/* 518 */     throw new IllegalArgumentException();
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
/*     */   public final JaxBeanInfo getBeanInfo(Object o) {
/* 533 */     for (Class<?> c = o.getClass(); c != Object.class; c = c.getSuperclass()) {
/* 534 */       JaxBeanInfo bi = this.beanInfoMap.get(c);
/* 535 */       if (bi != null) return bi; 
/*     */     } 
/* 537 */     if (o instanceof org.w3c.dom.Element)
/* 538 */       return this.beanInfoMap.get(Object.class); 
/* 539 */     return null;
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
/*     */   public final JaxBeanInfo getBeanInfo(Object o, boolean fatal) throws JAXBException {
/* 551 */     JaxBeanInfo bi = getBeanInfo(o);
/* 552 */     if (bi != null) return bi; 
/* 553 */     if (fatal) {
/* 554 */       if (o instanceof Document)
/* 555 */         throw new JAXBException(Messages.ELEMENT_NEEDED_BUT_FOUND_DOCUMENT.format(new Object[] { o.getClass() })); 
/* 556 */       throw new JAXBException(Messages.UNKNOWN_CLASS.format(new Object[] { o.getClass() }));
/*     */     } 
/* 558 */     return null;
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
/*     */   public final <T> JaxBeanInfo<T> getBeanInfo(Class<T> clazz) {
/* 572 */     return this.beanInfoMap.get(clazz);
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
/*     */   public final <T> JaxBeanInfo<T> getBeanInfo(Class<T> clazz, boolean fatal) throws JAXBException {
/* 584 */     JaxBeanInfo<T> bi = getBeanInfo(clazz);
/* 585 */     if (bi != null) return bi; 
/* 586 */     if (fatal)
/* 587 */       throw new JAXBException(clazz.getName() + " is not known to this context"); 
/* 588 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Loader selectRootLoader(UnmarshallingContext.State state, TagName tag) {
/* 599 */     JaxBeanInfo beanInfo = (JaxBeanInfo)this.rootMap.get(tag.uri, tag.local);
/* 600 */     if (beanInfo == null) {
/* 601 */       return null;
/*     */     }
/* 603 */     return beanInfo.getLoader(this, true);
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
/*     */   public JaxBeanInfo getGlobalType(QName name) {
/* 615 */     return this.typeMap.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNearestTypeName(QName name) {
/* 626 */     String[] all = new String[this.typeMap.size()];
/* 627 */     int i = 0;
/* 628 */     for (QName qn : this.typeMap.keySet()) {
/* 629 */       if (qn.getLocalPart().equals(name.getLocalPart()))
/* 630 */         return qn.toString(); 
/* 631 */       all[i++] = qn.toString();
/*     */     } 
/*     */     
/* 634 */     String nearest = EditDistance.findNearest(name.toString(), all);
/*     */     
/* 636 */     if (EditDistance.editDistance(nearest, name.toString()) > 10) {
/* 637 */       return null;
/*     */     }
/* 639 */     return nearest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<QName> getValidRootNames() {
/* 647 */     Set<QName> r = new TreeSet<QName>(QNAME_COMPARATOR);
/* 648 */     for (QNameMap.Entry e : this.rootMap.entrySet()) {
/* 649 */       r.add(e.createQName());
/*     */     }
/* 651 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Encoded[] getUTF8NameTable() {
/* 660 */     if (this.utf8nameTable == null) {
/* 661 */       Encoded[] x = new Encoded[this.nameList.localNames.length];
/* 662 */       for (int i = 0; i < x.length; i++) {
/* 663 */         Encoded e = new Encoded(this.nameList.localNames[i]);
/* 664 */         e.compact();
/* 665 */         x[i] = e;
/*     */       } 
/* 667 */       this.utf8nameTable = x;
/*     */     } 
/* 669 */     return this.utf8nameTable;
/*     */   }
/*     */   
/*     */   public int getNumberOfLocalNames() {
/* 673 */     return this.nameList.localNames.length;
/*     */   }
/*     */   
/*     */   public int getNumberOfElementNames() {
/* 677 */     return this.nameList.numberOfElementNames;
/*     */   }
/*     */   
/*     */   public int getNumberOfAttributeNames() {
/* 681 */     return this.nameList.numberOfAttributeNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Transformer createTransformer() {
/*     */     try {
/* 689 */       synchronized (JAXBContextImpl.class) {
/* 690 */         if (tf == null)
/* 691 */           tf = (SAXTransformerFactory)TransformerFactory.newInstance(); 
/* 692 */         return tf.newTransformer();
/*     */       } 
/* 694 */     } catch (TransformerConfigurationException e) {
/* 695 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TransformerHandler createTransformerHandler() {
/*     */     try {
/* 704 */       synchronized (JAXBContextImpl.class) {
/* 705 */         if (tf == null)
/* 706 */           tf = (SAXTransformerFactory)TransformerFactory.newInstance(); 
/* 707 */         return tf.newTransformerHandler();
/*     */       } 
/* 709 */     } catch (TransformerConfigurationException e) {
/* 710 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Document createDom() {
/* 718 */     synchronized (JAXBContextImpl.class) {
/* 719 */       if (db == null) {
/*     */         try {
/* 721 */           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 722 */           dbf.setNamespaceAware(true);
/* 723 */           db = dbf.newDocumentBuilder();
/* 724 */         } catch (ParserConfigurationException e) {
/*     */           
/* 726 */           throw new FactoryConfigurationError(e);
/*     */         } 
/*     */       }
/* 729 */       return db.newDocument();
/*     */     } 
/*     */   }
/*     */   
/*     */   public MarshallerImpl createMarshaller() {
/* 734 */     return new MarshallerImpl(this, null);
/*     */   }
/*     */   
/*     */   public UnmarshallerImpl createUnmarshaller() {
/* 738 */     return new UnmarshallerImpl(this, null);
/*     */   }
/*     */   
/*     */   public Validator createValidator() {
/* 742 */     throw new UnsupportedOperationException(Messages.NOT_IMPLEMENTED_IN_2_0.format(new Object[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   public JAXBIntrospector createJAXBIntrospector() {
/* 747 */     return new JAXBIntrospector() {
/*     */         public boolean isElement(Object object) {
/* 749 */           return (getElementName(object) != null);
/*     */         }
/*     */         
/*     */         public QName getElementName(Object jaxbElement) {
/*     */           try {
/* 754 */             return JAXBContextImpl.this.getElementName(jaxbElement);
/* 755 */           } catch (JAXBException e) {
/* 756 */             return null;
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private NonElement<Type, Class> getXmlType(RuntimeTypeInfoSet tis, TypeReference tr) {
/* 763 */     if (tr == null) {
/* 764 */       throw new IllegalArgumentException();
/*     */     }
/* 766 */     XmlJavaTypeAdapter xjta = (XmlJavaTypeAdapter)tr.get(XmlJavaTypeAdapter.class);
/* 767 */     XmlList xl = (XmlList)tr.get(XmlList.class);
/*     */     
/* 769 */     Ref<Type, Class<?>> ref = new Ref((AnnotationReader)this.annotaitonReader, (Navigator)tis.getNavigator(), tr.type, xjta, xl);
/*     */     
/* 771 */     return tis.getTypeInfo(ref);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateEpisode(Result output) {
/* 776 */     if (output == null)
/* 777 */       throw new IllegalArgumentException(); 
/* 778 */     createSchemaGenerator().writeEpisodeFile(ResultFactory.createSerializer(output));
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateSchema(SchemaOutputResolver outputResolver) throws IOException {
/* 783 */     if (outputResolver == null) {
/* 784 */       throw new IOException(Messages.NULL_OUTPUT_RESOLVER.format(new Object[0]));
/*     */     }
/* 786 */     final SAXParseException[] e = new SAXParseException[1];
/*     */     
/* 788 */     createSchemaGenerator().write(outputResolver, new ErrorListener() {
/*     */           public void error(SAXParseException exception) {
/* 790 */             e[0] = exception;
/*     */           }
/*     */           
/*     */           public void fatalError(SAXParseException exception) {
/* 794 */             e[0] = exception;
/*     */           }
/*     */           
/*     */           public void warning(SAXParseException exception) {}
/*     */           
/*     */           public void info(SAXParseException exception) {}
/*     */         });
/* 801 */     if (e[0] != null) {
/* 802 */       IOException x = new IOException(Messages.FAILED_TO_GENERATE_SCHEMA.format(new Object[0]));
/* 803 */       x.initCause(e[0]);
/* 804 */       throw x;
/*     */     } 
/*     */   }
/*     */   
/*     */   private XmlSchemaGenerator<Type, Class, Field, Method> createSchemaGenerator() {
/*     */     RuntimeTypeInfoSet tis;
/*     */     try {
/* 811 */       tis = getTypeInfoSet();
/* 812 */     } catch (IllegalAnnotationsException e) {
/*     */       
/* 814 */       throw new AssertionError(e);
/*     */     } 
/*     */     
/* 817 */     XmlSchemaGenerator<Type, Class<?>, Field, Method> xsdgen = new XmlSchemaGenerator((Navigator)tis.getNavigator(), (TypeInfoSet)tis);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 823 */     Set<QName> rootTagNames = new HashSet<QName>();
/* 824 */     for (RuntimeElementInfo ei : tis.getAllElements()) {
/* 825 */       rootTagNames.add(ei.getElementName());
/*     */     }
/* 827 */     for (RuntimeClassInfo ci : tis.beans().values()) {
/* 828 */       if (ci.isElement()) {
/* 829 */         rootTagNames.add(ci.asElement().getElementName());
/*     */       }
/*     */     } 
/* 832 */     for (TypeReference tr : this.bridges.keySet()) {
/* 833 */       if (rootTagNames.contains(tr.tagName)) {
/*     */         continue;
/*     */       }
/* 836 */       if (tr.type == void.class || tr.type == Void.class) {
/* 837 */         xsdgen.add(tr.tagName, false, null); continue;
/*     */       } 
/* 839 */       if (tr.type == CompositeStructure.class) {
/*     */         continue;
/*     */       }
/* 842 */       NonElement<Type, Class<?>> typeInfo = getXmlType(tis, tr);
/* 843 */       xsdgen.add(tr.tagName, !Navigator.REFLECTION.isPrimitive(tr.type), typeInfo);
/*     */     } 
/*     */     
/* 846 */     return xsdgen;
/*     */   }
/*     */   
/*     */   public QName getTypeName(TypeReference tr) {
/*     */     try {
/* 851 */       NonElement<Type, Class<?>> xt = getXmlType(getTypeInfoSet(), tr);
/* 852 */       if (xt == null) throw new IllegalArgumentException(); 
/* 853 */       return xt.getTypeName();
/* 854 */     } catch (IllegalAnnotationsException e) {
/*     */       
/* 856 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaOutputResolver createTestResolver() {
/* 864 */     return new SchemaOutputResolver() {
/*     */         public Result createOutput(String namespaceUri, String suggestedFileName) {
/* 866 */           SAXResult r = new SAXResult(new DefaultHandler());
/* 867 */           r.setSystemId(suggestedFileName);
/* 868 */           return r;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Binder<T> createBinder(Class<T> domType) {
/* 875 */     if (domType == Node.class) {
/* 876 */       return (Binder)createBinder();
/*     */     }
/* 878 */     return super.createBinder(domType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Binder<Node> createBinder() {
/* 883 */     return new BinderImpl<Node>(this, (InfosetScanner<Node>)new DOMScanner());
/*     */   }
/*     */   
/*     */   public QName getElementName(Object o) throws JAXBException {
/* 887 */     JaxBeanInfo<Object> bi = getBeanInfo(o, true);
/* 888 */     if (!bi.isElement())
/* 889 */       return null; 
/* 890 */     return new QName(bi.getElementNamespaceURI(o), bi.getElementLocalName(o));
/*     */   }
/*     */   
/*     */   public Bridge createBridge(TypeReference ref) {
/* 894 */     return this.bridges.get(ref);
/*     */   }
/*     */   @NotNull
/*     */   public BridgeContext createBridgeContext() {
/* 898 */     return new BridgeContextImpl(this);
/*     */   }
/*     */   
/*     */   public RawAccessor getElementPropertyAccessor(Class<?> wrapperBean, String nsUri, String localName) throws JAXBException {
/* 902 */     JaxBeanInfo<?> bi = getBeanInfo(wrapperBean, true);
/* 903 */     if (!(bi instanceof ClassBeanInfoImpl)) {
/* 904 */       throw new JAXBException(wrapperBean + " is not a bean");
/*     */     }
/* 906 */     for (ClassBeanInfoImpl cb = (ClassBeanInfoImpl)bi; cb != null; cb = cb.superClazz) {
/* 907 */       for (Property p : cb.properties) {
/* 908 */         final Accessor acc = p.getElementPropertyAccessor(nsUri, localName);
/* 909 */         if (acc != null)
/* 910 */           return new RawAccessor()
/*     */             {
/*     */ 
/*     */ 
/*     */               
/*     */               public Object get(Object bean) throws AccessorException
/*     */               {
/* 917 */                 return acc.getUnadapted(bean);
/*     */               }
/*     */               
/*     */               public void set(Object bean, Object value) throws AccessorException {
/* 921 */                 acc.setUnadapted(bean, value);
/*     */               }
/*     */             }; 
/*     */       } 
/*     */     } 
/* 926 */     throw new JAXBException(new QName(nsUri, localName) + " is not a valid property on " + wrapperBean);
/*     */   }
/*     */   
/*     */   public List<String> getKnownNamespaceURIs() {
/* 930 */     return Arrays.asList(this.nameList.namespaceURIs);
/*     */   }
/*     */   
/*     */   public String getBuildId() {
/* 934 */     Package pkg = getClass().getPackage();
/* 935 */     if (pkg == null) return null; 
/* 936 */     return pkg.getImplementationVersion();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 940 */     StringBuilder buf = new StringBuilder(Which.which(getClass()) + " Build-Id: " + getBuildId());
/* 941 */     buf.append("\nClasses known to this context:\n");
/*     */     
/* 943 */     Set<String> names = new TreeSet<String>();
/*     */     
/* 945 */     for (Class key : this.beanInfoMap.keySet()) {
/* 946 */       names.add(key.getName());
/*     */     }
/* 948 */     for (String name : names) {
/* 949 */       buf.append("  ").append(name).append('\n');
/*     */     }
/* 951 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getXMIMEContentType(Object o) {
/* 959 */     JaxBeanInfo bi = getBeanInfo(o);
/* 960 */     if (!(bi instanceof ClassBeanInfoImpl)) {
/* 961 */       return null;
/*     */     }
/* 963 */     ClassBeanInfoImpl cb = (ClassBeanInfoImpl)bi;
/* 964 */     for (Property p : cb.properties) {
/* 965 */       if (p instanceof AttributeProperty) {
/* 966 */         AttributeProperty ap = (AttributeProperty)p;
/* 967 */         if (ap.attName.equals("http://www.w3.org/2005/05/xmlmime", "contentType"))
/*     */           try {
/* 969 */             return (String)ap.xacc.print(o);
/* 970 */           } catch (AccessorException e) {
/* 971 */             return null;
/* 972 */           } catch (SAXException e) {
/* 973 */             return null;
/* 974 */           } catch (ClassCastException e) {
/* 975 */             return null;
/*     */           }  
/*     */       } 
/*     */     } 
/* 979 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBContextImpl createAugmented(Class<?> clazz) throws JAXBException {
/* 986 */     Class[] newList = new Class[this.classes.length + 1];
/* 987 */     System.arraycopy(this.classes, 0, newList, 0, this.classes.length);
/* 988 */     newList[this.classes.length] = clazz;
/*     */     
/* 990 */     return new JAXBContextImpl(newList, this.bridges.keySet(), this.subclassReplacements, this.defaultNsUri, this.c14nSupport, this.annotaitonReader, this.xmlAccessorFactorySupport, this.allNillable);
/*     */   }
/*     */ 
/*     */   
/* 994 */   private static final Comparator<QName> QNAME_COMPARATOR = new Comparator<QName>() {
/*     */       public int compare(QName lhs, QName rhs) {
/* 996 */         int r = lhs.getLocalPart().compareTo(rhs.getLocalPart());
/* 997 */         if (r != 0) return r;
/*     */         
/* 999 */         return lhs.getNamespaceURI().compareTo(rhs.getNamespaceURI());
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\JAXBContextImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */