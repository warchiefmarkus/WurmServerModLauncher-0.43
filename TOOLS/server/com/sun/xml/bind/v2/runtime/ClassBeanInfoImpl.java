/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.FinalArrayList;
/*     */ import com.sun.xml.bind.Util;
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.ClassFactory;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeClassInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.property.AttributeProperty;
/*     */ import com.sun.xml.bind.v2.runtime.property.Property;
/*     */ import com.sun.xml.bind.v2.runtime.property.PropertyFactory;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.StructureLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.XsiTypeLoader;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassBeanInfoImpl<BeanT>
/*     */   extends JaxBeanInfo<BeanT>
/*     */ {
/*     */   public final Property<BeanT>[] properties;
/*     */   private Property<? super BeanT> idProperty;
/*     */   private Loader loader;
/*     */   private Loader loaderWithTypeSubst;
/*     */   private RuntimeClassInfo ci;
/*     */   private final Accessor<? super BeanT, Map<QName, String>> inheritedAttWildcard;
/*     */   private final Transducer<BeanT> xducer;
/*     */   public final ClassBeanInfoImpl<? super BeanT> superClazz;
/*     */   private final Accessor<? super BeanT, Locator> xmlLocatorField;
/*     */   private final Name tagName;
/*     */   private AttributeProperty<BeanT>[] attributeProperties;
/*     */   private Property<BeanT>[] uriProperties;
/*     */   private final Method factoryMethod;
/*     */   
/*     */   ClassBeanInfoImpl(JAXBContextImpl owner, RuntimeClassInfo ci) {
/* 132 */     super(owner, (RuntimeTypeInfo)ci, (Class<BeanT>)ci.getClazz(), ci.getTypeName(), ci.isElement(), false, true);
/*     */     
/* 134 */     this.ci = ci;
/* 135 */     this.inheritedAttWildcard = ci.getAttributeWildcard();
/* 136 */     this.xducer = ci.getTransducer();
/* 137 */     this.factoryMethod = ci.getFactoryMethod();
/*     */     
/* 139 */     if (this.factoryMethod != null) {
/* 140 */       int classMod = this.factoryMethod.getDeclaringClass().getModifiers();
/*     */       
/* 142 */       if (!Modifier.isPublic(classMod) || !Modifier.isPublic(this.factoryMethod.getModifiers())) {
/*     */         
/*     */         try {
/* 145 */           this.factoryMethod.setAccessible(true);
/* 146 */         } catch (SecurityException e) {
/*     */           
/* 148 */           logger.log(Level.FINE, "Unable to make the method of " + this.factoryMethod + " accessible", e);
/* 149 */           throw e;
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 155 */     if (ci.getBaseClass() == null) {
/* 156 */       this.superClazz = null;
/*     */     } else {
/* 158 */       this.superClazz = owner.getOrCreate(ci.getBaseClass());
/*     */     } 
/* 160 */     if (this.superClazz != null && this.superClazz.xmlLocatorField != null) {
/* 161 */       this.xmlLocatorField = this.superClazz.xmlLocatorField;
/*     */     } else {
/* 163 */       this.xmlLocatorField = ci.getLocatorField();
/*     */     } 
/*     */     
/* 166 */     Collection<? extends RuntimePropertyInfo> ps = ci.getProperties();
/* 167 */     this.properties = (Property<BeanT>[])new Property[ps.size()];
/* 168 */     int idx = 0;
/* 169 */     boolean elementOnly = true;
/* 170 */     for (RuntimePropertyInfo info : ps) {
/* 171 */       Property<? super BeanT> p = PropertyFactory.create(owner, info);
/* 172 */       if (info.id() == ID.ID)
/* 173 */         this.idProperty = p; 
/* 174 */       this.properties[idx++] = (Property)p;
/* 175 */       elementOnly &= info.elementOnlyContent();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 180 */     hasElementOnlyContentModel(elementOnly);
/*     */ 
/*     */     
/* 183 */     if (ci.isElement()) {
/* 184 */       this.tagName = owner.nameBuilder.createElementName(ci.getElementName());
/*     */     } else {
/* 186 */       this.tagName = null;
/*     */     } 
/* 188 */     setLifecycleFlags();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void link(JAXBContextImpl grammar) {
/* 193 */     if (this.uriProperties != null) {
/*     */       return;
/*     */     }
/* 196 */     super.link(grammar);
/*     */     
/* 198 */     if (this.superClazz != null) {
/* 199 */       this.superClazz.link(grammar);
/*     */     }
/* 201 */     getLoader(grammar, true);
/*     */ 
/*     */     
/* 204 */     if (this.superClazz != null) {
/* 205 */       if (this.idProperty == null) {
/* 206 */         this.idProperty = this.superClazz.idProperty;
/*     */       }
/* 208 */       if (!this.superClazz.hasElementOnlyContentModel()) {
/* 209 */         hasElementOnlyContentModel(false);
/*     */       }
/*     */     } 
/*     */     
/* 213 */     FinalArrayList<AttributeProperty> finalArrayList = new FinalArrayList();
/* 214 */     FinalArrayList<Property<BeanT>> finalArrayList1 = new FinalArrayList();
/* 215 */     for (ClassBeanInfoImpl<BeanT> bi = this; bi != null; classBeanInfoImpl = bi.superClazz) {
/* 216 */       ClassBeanInfoImpl<? super BeanT> classBeanInfoImpl; for (int i = bi.properties.length - 1; i >= 0; i--) {
/* 217 */         Property<BeanT> p = bi.properties[i];
/* 218 */         if (p instanceof AttributeProperty)
/* 219 */           finalArrayList.add((AttributeProperty)p); 
/* 220 */         if (p.hasSerializeURIAction())
/* 221 */           finalArrayList1.add(p); 
/*     */       } 
/*     */     } 
/* 224 */     if (grammar.c14nSupport) {
/* 225 */       Collections.sort((List<AttributeProperty>)finalArrayList);
/*     */     }
/* 227 */     if (finalArrayList.isEmpty()) {
/* 228 */       this.attributeProperties = (AttributeProperty<BeanT>[])EMPTY_PROPERTIES;
/*     */     } else {
/* 230 */       this.attributeProperties = finalArrayList.<AttributeProperty<BeanT>>toArray((AttributeProperty<BeanT>[])new AttributeProperty[finalArrayList.size()]);
/*     */     } 
/* 232 */     if (finalArrayList1.isEmpty()) {
/* 233 */       this.uriProperties = (Property<BeanT>[])EMPTY_PROPERTIES;
/*     */     } else {
/* 235 */       this.uriProperties = finalArrayList1.<Property<BeanT>>toArray((Property<BeanT>[])new Property[finalArrayList1.size()]);
/*     */     } 
/*     */   }
/*     */   public void wrapUp() {
/* 239 */     for (Property<BeanT> p : this.properties)
/* 240 */       p.wrapUp(); 
/* 241 */     this.ci = null;
/* 242 */     super.wrapUp();
/*     */   }
/*     */   
/*     */   public String getElementNamespaceURI(BeanT bean) {
/* 246 */     return this.tagName.nsUri;
/*     */   }
/*     */   
/*     */   public String getElementLocalName(BeanT bean) {
/* 250 */     return this.tagName.localName;
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanT createInstance(UnmarshallingContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException, SAXException {
/* 255 */     BeanT bean = null;
/* 256 */     if (this.factoryMethod == null) {
/* 257 */       bean = (BeanT)ClassFactory.create0(this.jaxbType);
/*     */     } else {
/* 259 */       Object o = ClassFactory.create(this.factoryMethod);
/* 260 */       if (this.jaxbType.isInstance(o)) {
/* 261 */         bean = (BeanT)o;
/*     */       } else {
/* 263 */         throw new InstantiationException("The factory method didn't return a correct object");
/*     */       } 
/*     */     } 
/*     */     
/* 267 */     if (this.xmlLocatorField != null)
/*     */       
/*     */       try {
/* 270 */         this.xmlLocatorField.set(bean, new LocatorImpl((Locator)context.getLocator()));
/* 271 */       } catch (AccessorException e) {
/* 272 */         context.handleError((Exception)e);
/*     */       }  
/* 274 */     return bean;
/*     */   }
/*     */   
/*     */   public boolean reset(BeanT bean, UnmarshallingContext context) throws SAXException {
/*     */     try {
/* 279 */       if (this.superClazz != null)
/* 280 */         this.superClazz.reset(bean, context); 
/* 281 */       for (Property<BeanT> p : this.properties)
/* 282 */         p.reset(bean); 
/* 283 */       return true;
/* 284 */     } catch (AccessorException e) {
/* 285 */       context.handleError((Exception)e);
/* 286 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getId(BeanT bean, XMLSerializer target) throws SAXException {
/* 291 */     if (this.idProperty != null) {
/*     */       try {
/* 293 */         return this.idProperty.getIdValue(bean);
/* 294 */       } catch (AccessorException e) {
/* 295 */         target.reportError(null, (Throwable)e);
/*     */       } 
/*     */     }
/* 298 */     return null;
/*     */   }
/*     */   
/*     */   public void serializeRoot(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 302 */     if (this.tagName == null) {
/* 303 */       target.reportError((ValidationEvent)new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(new Object[] { bean.getClass().getName() }, ), null, null));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 311 */       target.startElement(this.tagName, bean);
/* 312 */       target.childAsSoleContent(bean, null);
/* 313 */       target.endElement();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void serializeBody(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 318 */     if (this.superClazz != null)
/* 319 */       this.superClazz.serializeBody(bean, target); 
/*     */     try {
/* 321 */       for (Property<BeanT> p : this.properties)
/* 322 */         p.serializeBody(bean, target, null); 
/* 323 */     } catch (AccessorException e) {
/* 324 */       target.reportError(null, (Throwable)e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void serializeAttributes(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 329 */     for (AttributeProperty<BeanT> p : this.attributeProperties) {
/*     */       try {
/* 331 */         p.serializeAttributes(bean, target);
/* 332 */       } catch (AccessorException e) {
/* 333 */         target.reportError(null, (Throwable)e);
/*     */       } 
/*     */     } 
/*     */     try {
/* 337 */       if (this.inheritedAttWildcard != null) {
/* 338 */         Map<QName, String> map = (Map<QName, String>)this.inheritedAttWildcard.get(bean);
/* 339 */         target.attWildcardAsAttributes(map, null);
/*     */       } 
/* 341 */     } catch (AccessorException e) {
/* 342 */       target.reportError(null, (Throwable)e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void serializeURIs(BeanT bean, XMLSerializer target) throws SAXException {
/*     */     try {
/* 348 */       for (Property<BeanT> p : this.uriProperties) {
/* 349 */         p.serializeURIs(bean, target);
/*     */       }
/* 351 */       if (this.inheritedAttWildcard != null) {
/* 352 */         Map<QName, String> map = (Map<QName, String>)this.inheritedAttWildcard.get(bean);
/* 353 */         target.attWildcardAsURIs(map, null);
/*     */       } 
/* 355 */     } catch (AccessorException e) {
/* 356 */       target.reportError(null, (Throwable)e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
/* 361 */     if (this.loader == null) {
/*     */ 
/*     */       
/* 364 */       StructureLoader sl = new StructureLoader(this);
/* 365 */       this.loader = (Loader)sl;
/* 366 */       if (this.ci.hasSubClasses()) {
/* 367 */         this.loaderWithTypeSubst = (Loader)new XsiTypeLoader(this);
/*     */       } else {
/*     */         
/* 370 */         this.loaderWithTypeSubst = this.loader;
/*     */       } 
/*     */       
/* 373 */       sl.init(context, this, this.ci.getAttributeWildcard());
/*     */     } 
/* 375 */     if (typeSubstitutionCapable) {
/* 376 */       return this.loaderWithTypeSubst;
/*     */     }
/* 378 */     return this.loader;
/*     */   }
/*     */   
/*     */   public Transducer<BeanT> getTransducer() {
/* 382 */     return this.xducer;
/*     */   }
/*     */   
/* 385 */   private static final AttributeProperty[] EMPTY_PROPERTIES = new AttributeProperty[0];
/*     */   
/* 387 */   private static final Logger logger = Util.getClassLogger();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\ClassBeanInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */