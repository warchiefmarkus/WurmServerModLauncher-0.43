/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeRef;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.LeafPropertyLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
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
/*     */ final class SingleElementLeafProperty<BeanT>
/*     */   extends PropertyImpl<BeanT>
/*     */ {
/*     */   private final Name tagName;
/*     */   private final boolean nillable;
/*     */   private final Accessor acc;
/*     */   private final String defaultValue;
/*     */   private final TransducedAccessor<BeanT> xacc;
/*     */   
/*     */   public SingleElementLeafProperty(JAXBContextImpl context, RuntimeElementPropertyInfo prop) {
/*  75 */     super(context, (RuntimePropertyInfo)prop);
/*  76 */     RuntimeTypeRef ref = prop.getTypes().get(0);
/*  77 */     this.tagName = context.nameBuilder.createElementName(ref.getTagName());
/*  78 */     assert this.tagName != null;
/*  79 */     this.nillable = ref.isNillable();
/*  80 */     this.defaultValue = ref.getDefaultValue();
/*  81 */     this.acc = prop.getAccessor().optimize(context);
/*     */     
/*  83 */     this.xacc = TransducedAccessor.get(context, (RuntimeNonElementRef)ref);
/*  84 */     assert this.xacc != null;
/*     */   }
/*     */   
/*     */   public void reset(BeanT o) throws AccessorException {
/*  88 */     this.acc.set(o, null);
/*     */   }
/*     */   
/*     */   public String getIdValue(BeanT bean) throws AccessorException, SAXException {
/*  92 */     return this.xacc.print(bean).toString();
/*     */   }
/*     */   
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/*  96 */     boolean hasValue = this.xacc.hasValue(o);
/*  97 */     if (hasValue) {
/*  98 */       this.xacc.writeLeafElement(w, this.tagName, o, this.fieldName);
/*     */     }
/* 100 */     else if (this.nillable) {
/* 101 */       w.startElement(this.tagName, null);
/* 102 */       w.writeXsiNilTrue();
/* 103 */       w.endElement();
/*     */     } 
/*     */   } public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
/*     */     DefaultValueLoaderDecorator defaultValueLoaderDecorator;
/*     */     XsiNilLoader.Single single;
/* 108 */     LeafPropertyLoader leafPropertyLoader = new LeafPropertyLoader(this.xacc);
/* 109 */     if (this.defaultValue != null)
/* 110 */       defaultValueLoaderDecorator = new DefaultValueLoaderDecorator((Loader)leafPropertyLoader, this.defaultValue); 
/* 111 */     if (this.nillable || chain.context.allNillable)
/* 112 */       single = new XsiNilLoader.Single((Loader)defaultValueLoaderDecorator, this.acc); 
/* 113 */     handlers.put(this.tagName, new ChildLoader((Loader)single, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyKind getKind() {
/* 118 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName) {
/* 123 */     if (this.tagName.equals(nsUri, localName)) {
/* 124 */       return this.acc;
/*     */     }
/* 126 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\SingleElementLeafProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */