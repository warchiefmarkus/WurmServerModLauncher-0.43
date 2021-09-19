/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.v2.ClassFactory;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeElement;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeReferencePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.ListIterator;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.WildcardLoader;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ArrayReferenceNodeProperty<BeanT, ListT, ItemT>
/*     */   extends ArrayERProperty<BeanT, ListT, ItemT>
/*     */ {
/*  72 */   private final QNameMap<JaxBeanInfo> expectedElements = new QNameMap();
/*     */   
/*     */   private final boolean isMixed;
/*     */   
/*     */   private final DomHandler domHandler;
/*     */   private final WildcardMode wcMode;
/*     */   
/*     */   public ArrayReferenceNodeProperty(JAXBContextImpl p, RuntimeReferencePropertyInfo prop) {
/*  80 */     super(p, (RuntimePropertyInfo)prop, prop.getXmlName(), prop.isCollectionNillable());
/*     */     
/*  82 */     for (RuntimeElement e : prop.getElements()) {
/*  83 */       JaxBeanInfo bi = p.getOrCreate((RuntimeTypeInfo)e);
/*  84 */       this.expectedElements.put(e.getElementName().getNamespaceURI(), e.getElementName().getLocalPart(), bi);
/*     */     } 
/*     */     
/*  87 */     this.isMixed = prop.isMixed();
/*     */     
/*  89 */     if (prop.getWildcard() != null) {
/*  90 */       this.domHandler = (DomHandler)ClassFactory.create((Class)prop.getDOMHandler());
/*  91 */       this.wcMode = prop.getWildcard();
/*     */     } else {
/*  93 */       this.domHandler = null;
/*  94 */       this.wcMode = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void serializeListBody(BeanT o, XMLSerializer w, ListT list) throws IOException, XMLStreamException, SAXException {
/*  99 */     ListIterator<ItemT> itr = this.lister.iterator(list, w);
/*     */     
/* 101 */     while (itr.hasNext()) {
/*     */       try {
/* 103 */         ItemT item = (ItemT)itr.next();
/* 104 */         if (item != null) {
/* 105 */           if (this.isMixed && item.getClass() == String.class) {
/* 106 */             w.text((String)item, null); continue;
/*     */           } 
/* 108 */           JaxBeanInfo bi = w.grammar.getBeanInfo(item, true);
/* 109 */           if (bi.jaxbType == Object.class && this.domHandler != null) {
/*     */ 
/*     */             
/* 112 */             w.writeDom(item, this.domHandler, o, this.fieldName); continue;
/*     */           } 
/* 114 */           bi.serializeRoot(item, w);
/*     */         }
/*     */       
/* 117 */       } catch (JAXBException e) {
/* 118 */         w.reportError(this.fieldName, (Throwable)e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void createBodyUnmarshaller(UnmarshallerChain chain, QNameMap<ChildLoader> loaders) {
/* 125 */     int offset = chain.allocateOffset();
/*     */     
/* 127 */     Receiver recv = new ArrayERProperty.ReceiverImpl(this, offset);
/*     */     
/* 129 */     for (QNameMap.Entry<JaxBeanInfo> n : (Iterable<QNameMap.Entry<JaxBeanInfo>>)this.expectedElements.entrySet()) {
/* 130 */       JaxBeanInfo beanInfo = (JaxBeanInfo)n.getValue();
/* 131 */       loaders.put(n.nsUri, n.localName, new ChildLoader(beanInfo.getLoader(chain.context, true), recv));
/*     */     } 
/*     */     
/* 134 */     if (this.isMixed)
/*     */     {
/* 136 */       loaders.put(TEXT_HANDLER, new ChildLoader(new MixedTextLoader(recv), null));
/*     */     }
/*     */ 
/*     */     
/* 140 */     if (this.domHandler != null) {
/* 141 */       loaders.put(CATCH_ALL, new ChildLoader((Loader)new WildcardLoader(this.domHandler, this.wcMode), recv));
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class MixedTextLoader
/*     */     extends Loader
/*     */   {
/*     */     private final Receiver recv;
/*     */     
/*     */     public MixedTextLoader(Receiver recv) {
/* 151 */       super(true);
/* 152 */       this.recv = recv;
/*     */     }
/*     */     
/*     */     public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
/* 156 */       if (text.length() != 0) {
/* 157 */         this.recv.receive(state, text.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public PropertyKind getKind() {
/* 163 */     return PropertyKind.REFERENCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName) {
/* 169 */     if (this.wrapperTagName != null) {
/* 170 */       if (this.wrapperTagName.equals(nsUri, localName)) {
/* 171 */         return this.acc;
/*     */       }
/* 173 */     } else if (this.expectedElements.containsKey(nsUri, localName)) {
/* 174 */       return this.acc;
/*     */     } 
/* 176 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\ArrayReferenceNodeProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */