/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeElementInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.property.Property;
/*     */ import com.sun.xml.bind.v2.runtime.property.PropertyFactory;
/*     */ import com.sun.xml.bind.v2.runtime.property.UnmarshallerChain;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Discarder;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Intercepter;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.TagName;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Type;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ElementBeanInfoImpl
/*     */   extends JaxBeanInfo<JAXBElement>
/*     */ {
/*     */   private Loader loader;
/*     */   private final Property property;
/*     */   private final QName tagName;
/*     */   public final Class expectedType;
/*     */   private final Class scope;
/*     */   private final Constructor<? extends JAXBElement> constructor;
/*     */   
/*     */   ElementBeanInfoImpl(JAXBContextImpl grammar, RuntimeElementInfo rei) {
/*  89 */     super(grammar, (RuntimeTypeInfo)rei, rei.getType(), true, false, true);
/*     */     
/*  91 */     this.property = PropertyFactory.create(grammar, (RuntimePropertyInfo)rei.getProperty());
/*     */     
/*  93 */     this.tagName = rei.getElementName();
/*  94 */     this.expectedType = Navigator.REFLECTION.erasure((Type)rei.getContentInMemoryType());
/*  95 */     this.scope = (rei.getScope() == null) ? JAXBElement.GlobalScope.class : (Class)rei.getScope().getClazz();
/*     */     
/*  97 */     Class<JAXBElement> type = Navigator.REFLECTION.erasure(rei.getType());
/*  98 */     if (type == JAXBElement.class) {
/*  99 */       this.constructor = null;
/*     */     } else {
/*     */       try {
/* 102 */         this.constructor = type.getConstructor(new Class[] { this.expectedType });
/* 103 */       } catch (NoSuchMethodException e) {
/* 104 */         NoSuchMethodError x = new NoSuchMethodError("Failed to find the constructor for " + type + " with " + this.expectedType);
/* 105 */         x.initCause(e);
/* 106 */         throw x;
/*     */       } 
/*     */     } 
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
/*     */   protected ElementBeanInfoImpl(final JAXBContextImpl grammar) {
/* 120 */     super(grammar, null, JAXBElement.class, true, false, true);
/* 121 */     this.tagName = null;
/* 122 */     this.expectedType = null;
/* 123 */     this.scope = null;
/* 124 */     this.constructor = null;
/*     */     
/* 126 */     this.property = new Property<JAXBElement>() {
/*     */         public void reset(JAXBElement o) {
/* 128 */           throw new UnsupportedOperationException();
/*     */         }
/*     */         
/*     */         public void serializeBody(JAXBElement e, XMLSerializer target, Object outerPeer) throws SAXException, IOException, XMLStreamException {
/* 132 */           Class scope = e.getScope();
/* 133 */           if (e.isGlobalScope()) scope = null; 
/* 134 */           QName n = e.getName();
/* 135 */           ElementBeanInfoImpl bi = grammar.getElement(scope, n);
/* 136 */           if (bi == null) {
/*     */             JaxBeanInfo<?> tbi;
/*     */             
/*     */             try {
/* 140 */               tbi = grammar.getBeanInfo(e.getDeclaredType(), true);
/* 141 */             } catch (JAXBException x) {
/*     */               
/* 143 */               target.reportError(null, (Throwable)x);
/*     */               return;
/*     */             } 
/* 146 */             Object value = e.getValue();
/* 147 */             target.startElement(n.getNamespaceURI(), n.getLocalPart(), n.getPrefix(), null);
/* 148 */             if (value == null) {
/* 149 */               target.writeXsiNilTrue();
/*     */             } else {
/* 151 */               target.childAsXsiType(value, "value", tbi);
/*     */             } 
/* 153 */             target.endElement();
/*     */           } else {
/*     */             try {
/* 156 */               bi.property.serializeBody(e, target, e);
/* 157 */             } catch (AccessorException x) {
/* 158 */               target.reportError(null, (Throwable)x);
/*     */             } 
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void serializeURIs(JAXBElement o, XMLSerializer target) {}
/*     */         
/*     */         public boolean hasSerializeURIAction() {
/* 167 */           return false;
/*     */         }
/*     */         
/*     */         public String getIdValue(JAXBElement o) {
/* 171 */           return null;
/*     */         }
/*     */         
/*     */         public PropertyKind getKind() {
/* 175 */           return PropertyKind.ELEMENT;
/*     */         }
/*     */ 
/*     */         
/*     */         public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {}
/*     */         
/*     */         public Accessor getElementPropertyAccessor(String nsUri, String localName) {
/* 182 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void wrapUp() {}
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private final class IntercepterLoader
/*     */     extends Loader
/*     */     implements Intercepter
/*     */   {
/*     */     private final Loader core;
/*     */ 
/*     */     
/*     */     public IntercepterLoader(Loader core) {
/* 200 */       this.core = core;
/*     */     }
/*     */     
/*     */     public final void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 204 */       state.loader = this.core;
/* 205 */       state.intercepter = this;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 210 */       UnmarshallingContext context = state.getContext();
/*     */ 
/*     */       
/* 213 */       Object child = context.getOuterPeer();
/*     */       
/* 215 */       if (child != null && ElementBeanInfoImpl.this.jaxbType != child.getClass()) {
/* 216 */         child = null;
/*     */       }
/* 218 */       if (child != null) {
/* 219 */         ElementBeanInfoImpl.this.reset((JAXBElement)child, context);
/*     */       }
/* 221 */       if (child == null) {
/* 222 */         child = context.createInstance(ElementBeanInfoImpl.this);
/*     */       }
/* 224 */       fireBeforeUnmarshal(ElementBeanInfoImpl.this, child, state);
/*     */       
/* 226 */       context.recordOuterPeer(child);
/* 227 */       UnmarshallingContext.State p = state.prev;
/* 228 */       p.backup = p.target;
/* 229 */       p.target = child;
/*     */       
/* 231 */       this.core.startElement(state, ea);
/*     */     }
/*     */     
/*     */     public Object intercept(UnmarshallingContext.State state, Object o) throws SAXException {
/* 235 */       JAXBElement e = (JAXBElement)state.target;
/* 236 */       state.target = state.backup;
/* 237 */       state.backup = null;
/*     */       
/* 239 */       if (o != null)
/*     */       {
/*     */         
/* 242 */         e.setValue(o);
/*     */       }
/* 244 */       fireAfterUnmarshal(ElementBeanInfoImpl.this, e, state);
/*     */       
/* 246 */       return e;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getElementNamespaceURI(JAXBElement e) {
/* 251 */     return e.getName().getNamespaceURI();
/*     */   }
/*     */   
/*     */   public String getElementLocalName(JAXBElement e) {
/* 255 */     return e.getName().getLocalPart();
/*     */   }
/*     */   
/*     */   public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
/* 259 */     if (this.loader == null) {
/*     */       
/* 261 */       UnmarshallerChain c = new UnmarshallerChain(context);
/* 262 */       QNameMap<ChildLoader> result = new QNameMap();
/* 263 */       this.property.buildChildElementUnmarshallers(c, result);
/* 264 */       if (result.size() == 1) {
/*     */         
/* 266 */         this.loader = new IntercepterLoader(((ChildLoader)result.getOne().getValue()).loader);
/*     */       } else {
/*     */         
/* 269 */         this.loader = Discarder.INSTANCE;
/*     */       } 
/* 271 */     }  return this.loader;
/*     */   }
/*     */   
/*     */   public final JAXBElement createInstance(UnmarshallingContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
/* 275 */     return createInstanceFromValue(null);
/*     */   }
/*     */   
/*     */   public final JAXBElement createInstanceFromValue(Object o) throws IllegalAccessException, InvocationTargetException, InstantiationException {
/* 279 */     if (this.constructor == null) {
/* 280 */       return new JAXBElement(this.tagName, this.expectedType, this.scope, o);
/*     */     }
/* 282 */     return this.constructor.newInstance(new Object[] { o });
/*     */   }
/*     */   
/*     */   public boolean reset(JAXBElement e, UnmarshallingContext context) {
/* 286 */     e.setValue(null);
/* 287 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId(JAXBElement e, XMLSerializer target) {
/* 296 */     Object o = e.getValue();
/* 297 */     if (o instanceof String) {
/* 298 */       return (String)o;
/*     */     }
/* 300 */     return null;
/*     */   }
/*     */   
/*     */   public void serializeBody(JAXBElement element, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/*     */     try {
/* 305 */       this.property.serializeBody(element, target, null);
/* 306 */     } catch (AccessorException x) {
/* 307 */       target.reportError(null, (Throwable)x);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void serializeRoot(JAXBElement e, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 312 */     serializeBody(e, target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeAttributes(JAXBElement e, XMLSerializer target) {}
/*     */ 
/*     */   
/*     */   public void serializeURIs(JAXBElement e, XMLSerializer target) {}
/*     */ 
/*     */   
/*     */   public final Transducer<JAXBElement> getTransducer() {
/* 324 */     return null;
/*     */   }
/*     */   
/*     */   public void wrapUp() {
/* 328 */     super.wrapUp();
/* 329 */     this.property.wrapUp();
/*     */   }
/*     */   
/*     */   public void link(JAXBContextImpl grammar) {
/* 333 */     super.link(grammar);
/* 334 */     getLoader(grammar, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\ElementBeanInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */