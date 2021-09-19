/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.xml.bind.Util;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JaxBeanInfo<BeanT>
/*     */ {
/*     */   protected short flag;
/*     */   private static final short FLAG_IS_ELEMENT = 1;
/*     */   private static final short FLAG_IS_IMMUTABLE = 2;
/*     */   private static final short FLAG_HAS_ELEMENT_ONLY_CONTENTMODEL = 4;
/*     */   private static final short FLAG_HAS_BEFORE_UNMARSHAL_METHOD = 8;
/*     */   private static final short FLAG_HAS_AFTER_UNMARSHAL_METHOD = 16;
/*     */   private static final short FLAG_HAS_BEFORE_MARSHAL_METHOD = 32;
/*     */   private static final short FLAG_HAS_AFTER_MARSHAL_METHOD = 64;
/*     */   private static final short FLAG_HAS_LIFECYCLE_EVENTS = 128;
/*     */   private LifecycleMethods lcm;
/*     */   public final Class<BeanT> jaxbType;
/*     */   private final Object typeName;
/*     */   
/*     */   protected JaxBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo rti, Class<BeanT> jaxbType, QName[] typeNames, boolean isElement, boolean isImmutable, boolean hasLifecycleEvents) {
/*  93 */     this(grammar, rti, jaxbType, typeNames, isElement, isImmutable, hasLifecycleEvents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JaxBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo rti, Class<BeanT> jaxbType, QName typeName, boolean isElement, boolean isImmutable, boolean hasLifecycleEvents) {
/* 100 */     this(grammar, rti, jaxbType, typeName, isElement, isImmutable, hasLifecycleEvents);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JaxBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo rti, Class<BeanT> jaxbType, boolean isElement, boolean isImmutable, boolean hasLifecycleEvents) {
/* 107 */     this(grammar, rti, jaxbType, (Object)null, isElement, isImmutable, hasLifecycleEvents);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JaxBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo rti, Class<BeanT> jaxbType, Object typeName, boolean isElement, boolean isImmutable, boolean hasLifecycleEvents) {
/* 135 */     this.lcm = null;
/*     */     grammar.beanInfos.put(rti, this);
/*     */     this.jaxbType = jaxbType;
/*     */     this.typeName = typeName;
/*     */     this.flag = (short)((isElement ? 1 : 0) | (isImmutable ? 2 : 0) | (hasLifecycleEvents ? 128 : 0));
/*     */   } public final boolean hasBeforeUnmarshalMethod() {
/* 141 */     return ((this.flag & 0x8) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasAfterUnmarshalMethod() {
/* 148 */     return ((this.flag & 0x10) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasBeforeMarshalMethod() {
/* 155 */     return ((this.flag & 0x20) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasAfterMarshalMethod() {
/* 162 */     return ((this.flag & 0x40) != 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isElement() {
/* 185 */     return ((this.flag & 0x1) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isImmutable() {
/* 196 */     return ((this.flag & 0x2) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasElementOnlyContentModel() {
/* 206 */     return ((this.flag & 0x4) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void hasElementOnlyContentModel(boolean value) {
/* 216 */     if (value) {
/* 217 */       this.flag = (short)(this.flag | 0x4);
/*     */     } else {
/* 219 */       this.flag = (short)(this.flag & 0xFFFFFFFB);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean lookForLifecycleMethods() {
/* 230 */     return ((this.flag & 0x80) != 0);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getElementNamespaceURI(BeanT paramBeanT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getElementLocalName(BeanT paramBeanT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<QName> getTypeNames() {
/* 279 */     if (this.typeName == null) return Collections.emptyList(); 
/* 280 */     if (this.typeName instanceof QName) return Collections.singletonList((QName)this.typeName); 
/* 281 */     return Arrays.asList((QName[])this.typeName);
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
/*     */   public QName getTypeName(@NotNull BeanT instance) {
/* 293 */     if (this.typeName == null) return null; 
/* 294 */     if (this.typeName instanceof QName) return (QName)this.typeName; 
/* 295 */     return ((QName[])this.typeName)[0];
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
/*     */   public abstract BeanT createInstance(UnmarshallingContext paramUnmarshallingContext) throws IllegalAccessException, InvocationTargetException, InstantiationException, SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean reset(BeanT paramBeanT, UnmarshallingContext paramUnmarshallingContext) throws SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getId(BeanT paramBeanT, XMLSerializer paramXMLSerializer) throws SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void serializeBody(BeanT paramBeanT, XMLSerializer paramXMLSerializer) throws SAXException, IOException, XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void serializeAttributes(BeanT paramBeanT, XMLSerializer paramXMLSerializer) throws SAXException, IOException, XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void serializeRoot(BeanT paramBeanT, XMLSerializer paramXMLSerializer) throws SAXException, IOException, XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void serializeURIs(BeanT paramBeanT, XMLSerializer paramXMLSerializer) throws SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Loader getLoader(JAXBContextImpl paramJAXBContextImpl, boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Transducer<BeanT> getTransducer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void link(JAXBContextImpl grammar) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void wrapUp() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 419 */   private static final Class[] unmarshalEventParams = new Class[] { Unmarshaller.class, Object.class };
/* 420 */   private static Class[] marshalEventParams = new Class[] { Marshaller.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setLifecycleFlags() {
/*     */     try {
/* 428 */       for (Method m : this.jaxbType.getDeclaredMethods()) {
/* 429 */         String name = m.getName();
/* 430 */         if (name.equals("beforeUnmarshal")) {
/* 431 */           if (match(m, unmarshalEventParams)) {
/* 432 */             cacheLifecycleMethod(m, (short)8);
/*     */           }
/*     */         }
/* 435 */         else if (name.equals("afterUnmarshal")) {
/* 436 */           if (match(m, unmarshalEventParams)) {
/* 437 */             cacheLifecycleMethod(m, (short)16);
/*     */           }
/*     */         }
/* 440 */         else if (name.equals("beforeMarshal")) {
/* 441 */           if (match(m, marshalEventParams)) {
/* 442 */             cacheLifecycleMethod(m, (short)32);
/*     */           }
/*     */         }
/* 445 */         else if (name.equals("afterMarshal") && 
/* 446 */           match(m, marshalEventParams)) {
/* 447 */           cacheLifecycleMethod(m, (short)64);
/*     */         }
/*     */       
/*     */       } 
/* 451 */     } catch (SecurityException e) {
/*     */       
/* 453 */       logger.log(Level.WARNING, Messages.UNABLE_TO_DISCOVER_EVENTHANDLER.format(new Object[] { this.jaxbType.getName(), e }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean match(Method m, Class[] params) {
/* 459 */     return Arrays.equals((Object[])m.getParameterTypes(), (Object[])params);
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
/*     */   private void cacheLifecycleMethod(Method m, short lifecycleFlag) {
/* 472 */     if (this.lcm == null) {
/* 473 */       this.lcm = new LifecycleMethods();
/*     */     }
/*     */ 
/*     */     
/* 477 */     m.setAccessible(true);
/*     */     
/* 479 */     this.flag = (short)(this.flag | lifecycleFlag);
/*     */     
/* 481 */     switch (lifecycleFlag) {
/*     */       case 8:
/* 483 */         this.lcm.beforeUnmarshal = m;
/*     */         break;
/*     */       case 16:
/* 486 */         this.lcm.afterUnmarshal = m;
/*     */         break;
/*     */       case 32:
/* 489 */         this.lcm.beforeMarshal = m;
/*     */         break;
/*     */       case 64:
/* 492 */         this.lcm.afterMarshal = m;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final LifecycleMethods getLifecycleMethods() {
/* 503 */     return this.lcm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void invokeBeforeUnmarshalMethod(UnmarshallerImpl unm, Object child, Object parent) throws SAXException {
/* 510 */     Method m = (getLifecycleMethods()).beforeUnmarshal;
/* 511 */     invokeUnmarshallCallback(m, child, unm, parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void invokeAfterUnmarshalMethod(UnmarshallerImpl unm, Object child, Object parent) throws SAXException {
/* 518 */     Method m = (getLifecycleMethods()).afterUnmarshal;
/* 519 */     invokeUnmarshallCallback(m, child, unm, parent);
/*     */   }
/*     */   
/*     */   private void invokeUnmarshallCallback(Method m, Object child, UnmarshallerImpl unm, Object parent) throws SAXException {
/*     */     try {
/* 524 */       m.invoke(child, new Object[] { unm, parent });
/* 525 */     } catch (IllegalAccessException e) {
/* 526 */       UnmarshallingContext.getInstance().handleError(e);
/* 527 */     } catch (InvocationTargetException e) {
/* 528 */       UnmarshallingContext.getInstance().handleError(e);
/*     */     } 
/*     */   }
/*     */   
/* 532 */   private static final Logger logger = Util.getClassLogger();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\JaxBeanInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */