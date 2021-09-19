/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeLeafInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.TextLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.XsiTypeLoader;
/*     */ import java.io.IOException;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
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
/*     */ 
/*     */ final class LeafBeanInfoImpl<BeanT>
/*     */   extends JaxBeanInfo<BeanT>
/*     */ {
/*     */   private final Loader loader;
/*     */   private final Loader loaderWithSubst;
/*     */   private final Transducer<BeanT> xducer;
/*     */   private final Name tagName;
/*     */   
/*     */   public LeafBeanInfoImpl(JAXBContextImpl grammar, RuntimeLeafInfo li) {
/*  81 */     super(grammar, (RuntimeTypeInfo)li, li.getClazz(), li.getTypeNames(), li.isElement(), true, false);
/*     */     
/*  83 */     this.xducer = li.getTransducer();
/*  84 */     this.loader = (Loader)new TextLoader(this.xducer);
/*  85 */     this.loaderWithSubst = (Loader)new XsiTypeLoader(this);
/*     */     
/*  87 */     if (isElement()) {
/*  88 */       this.tagName = grammar.nameBuilder.createElementName(li.getElementName());
/*     */     } else {
/*  90 */       this.tagName = null;
/*     */     } 
/*     */   }
/*     */   public QName getTypeName(BeanT instance) {
/*  94 */     QName tn = this.xducer.getTypeName(instance);
/*  95 */     if (tn != null) return tn;
/*     */     
/*  97 */     return super.getTypeName(instance);
/*     */   }
/*     */   
/*     */   public final String getElementNamespaceURI(BeanT _) {
/* 101 */     return this.tagName.nsUri;
/*     */   }
/*     */   
/*     */   public final String getElementLocalName(BeanT _) {
/* 105 */     return this.tagName.localName;
/*     */   }
/*     */   
/*     */   public BeanT createInstance(UnmarshallingContext context) {
/* 109 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public final boolean reset(BeanT bean, UnmarshallingContext context) {
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   public final String getId(BeanT bean, XMLSerializer target) {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void serializeBody(BeanT bean, XMLSerializer w) throws SAXException, IOException, XMLStreamException {
/*     */     try {
/* 125 */       this.xducer.writeText(w, bean, null);
/* 126 */     } catch (AccessorException e) {
/* 127 */       w.reportError(null, (Throwable)e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void serializeAttributes(BeanT bean, XMLSerializer target) {}
/*     */ 
/*     */   
/*     */   public final void serializeRoot(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 136 */     if (this.tagName == null) {
/* 137 */       target.reportError((ValidationEvent)new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(new Object[] { bean.getClass().getName() }, ), null, null));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 145 */       target.startElement(this.tagName, bean);
/* 146 */       target.childAsSoleContent(bean, null);
/* 147 */       target.endElement();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void serializeURIs(BeanT bean, XMLSerializer target) throws SAXException {
/* 154 */     if (this.xducer.useNamespace()) {
/*     */       try {
/* 156 */         this.xducer.declareNamespace(bean, target);
/* 157 */       } catch (AccessorException e) {
/* 158 */         target.reportError(null, (Throwable)e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
/* 164 */     if (typeSubstitutionCapable) {
/* 165 */       return this.loaderWithSubst;
/*     */     }
/* 167 */     return this.loader;
/*     */   }
/*     */   
/*     */   public Transducer<BeanT> getTransducer() {
/* 171 */     return this.xducer;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\LeafBeanInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */