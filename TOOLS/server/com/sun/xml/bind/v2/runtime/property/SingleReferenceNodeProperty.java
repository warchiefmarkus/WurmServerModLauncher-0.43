/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.ClassFactory;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeElement;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeReferencePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.ElementBeanInfoImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.WildcardLoader;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.annotation.DomHandler;
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
/*     */ final class SingleReferenceNodeProperty<BeanT, ValueT>
/*     */   extends PropertyImpl<BeanT>
/*     */ {
/*     */   private final Accessor<BeanT, ValueT> acc;
/*  71 */   private final QNameMap<JaxBeanInfo> expectedElements = new QNameMap();
/*     */   
/*     */   private final DomHandler domHandler;
/*     */   private final WildcardMode wcMode;
/*     */   
/*     */   public SingleReferenceNodeProperty(JAXBContextImpl context, RuntimeReferencePropertyInfo prop) {
/*  77 */     super(context, (RuntimePropertyInfo)prop);
/*  78 */     this.acc = prop.getAccessor().optimize(context);
/*     */     
/*  80 */     for (RuntimeElement e : prop.getElements()) {
/*  81 */       this.expectedElements.put(e.getElementName(), context.getOrCreate((RuntimeTypeInfo)e));
/*     */     }
/*     */     
/*  84 */     if (prop.getWildcard() != null) {
/*  85 */       this.domHandler = (DomHandler)ClassFactory.create((Class)prop.getDOMHandler());
/*  86 */       this.wcMode = prop.getWildcard();
/*     */     } else {
/*  88 */       this.domHandler = null;
/*  89 */       this.wcMode = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reset(BeanT bean) throws AccessorException {
/*  94 */     this.acc.set(bean, null);
/*     */   }
/*     */   
/*     */   public String getIdValue(BeanT beanT) {
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/* 102 */     ValueT v = (ValueT)this.acc.get(o);
/* 103 */     if (v != null) {
/*     */       try {
/* 105 */         JaxBeanInfo bi = w.grammar.getBeanInfo(v, true);
/* 106 */         if (bi.jaxbType == Object.class && this.domHandler != null)
/*     */         
/*     */         { 
/* 109 */           w.writeDom(v, this.domHandler, o, this.fieldName); }
/*     */         else
/* 111 */         { bi.serializeRoot(v, w); } 
/* 112 */       } catch (JAXBException e) {
/* 113 */         w.reportError(this.fieldName, (Throwable)e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
/* 120 */     for (QNameMap.Entry<JaxBeanInfo> n : (Iterable<QNameMap.Entry<JaxBeanInfo>>)this.expectedElements.entrySet()) {
/* 121 */       handlers.put(n.nsUri, n.localName, new ChildLoader(((JaxBeanInfo)n.getValue()).getLoader(chain.context, true), (Receiver)this.acc));
/*     */     }
/* 123 */     if (this.domHandler != null) {
/* 124 */       handlers.put(CATCH_ALL, new ChildLoader((Loader)new WildcardLoader(this.domHandler, this.wcMode), (Receiver)this.acc));
/*     */     }
/*     */   }
/*     */   
/*     */   public PropertyKind getKind() {
/* 129 */     return PropertyKind.REFERENCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName) {
/* 134 */     JaxBeanInfo bi = (JaxBeanInfo)this.expectedElements.get(nsUri, localName);
/* 135 */     if (bi != null) {
/* 136 */       if (bi instanceof ElementBeanInfoImpl) {
/* 137 */         final ElementBeanInfoImpl ebi = (ElementBeanInfoImpl)bi;
/*     */         
/* 139 */         return new Accessor<BeanT, Object>(ebi.expectedType) {
/*     */             public Object get(BeanT bean) throws AccessorException {
/* 141 */               ValueT r = (ValueT)SingleReferenceNodeProperty.this.acc.get(bean);
/* 142 */               if (r instanceof JAXBElement) {
/* 143 */                 return ((JAXBElement)r).getValue();
/*     */               }
/*     */               
/* 146 */               return r;
/*     */             }
/*     */             
/*     */             public void set(BeanT bean, Object value) throws AccessorException {
/* 150 */               if (value != null) {
/*     */                 try {
/* 152 */                   value = ebi.createInstanceFromValue(value);
/* 153 */                 } catch (IllegalAccessException e) {
/* 154 */                   throw new AccessorException(e);
/* 155 */                 } catch (InvocationTargetException e) {
/* 156 */                   throw new AccessorException(e);
/* 157 */                 } catch (InstantiationException e) {
/* 158 */                   throw new AccessorException(e);
/*     */                 } 
/*     */               }
/* 161 */               SingleReferenceNodeProperty.this.acc.set(bean, value);
/*     */             }
/*     */           };
/*     */       } 
/*     */       
/* 166 */       return this.acc;
/*     */     } 
/*     */     
/* 169 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\SingleReferenceNodeProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */