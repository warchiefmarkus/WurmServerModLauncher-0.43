/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeAttributePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AttributeProperty<BeanT>
/*     */   extends PropertyImpl<BeanT>
/*     */   implements Comparable<AttributeProperty>
/*     */ {
/*     */   public final Name attName;
/*     */   public final TransducedAccessor<BeanT> xacc;
/*     */   private final Accessor acc;
/*     */   
/*     */   public AttributeProperty(JAXBContextImpl context, RuntimeAttributePropertyInfo prop) {
/*  85 */     super(context, (RuntimePropertyInfo)prop);
/*  86 */     this.attName = context.nameBuilder.createAttributeName(prop.getXmlName());
/*  87 */     this.xacc = TransducedAccessor.get(context, (RuntimeNonElementRef)prop);
/*  88 */     this.acc = prop.getAccessor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeAttributes(BeanT o, XMLSerializer w) throws SAXException, AccessorException, IOException, XMLStreamException {
/*  97 */     CharSequence value = this.xacc.print(o);
/*  98 */     if (value != null)
/*  99 */       w.attribute(this.attName, value.toString()); 
/*     */   }
/*     */   
/*     */   public void serializeURIs(BeanT o, XMLSerializer w) throws AccessorException, SAXException {
/* 103 */     this.xacc.declareNamespace(o, w);
/*     */   }
/*     */   
/*     */   public boolean hasSerializeURIAction() {
/* 107 */     return this.xacc.useNamespace();
/*     */   }
/*     */   
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chainElem, QNameMap<ChildLoader> handlers) {
/* 111 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyKind getKind() {
/* 116 */     return PropertyKind.ATTRIBUTE;
/*     */   }
/*     */   
/*     */   public void reset(BeanT o) throws AccessorException {
/* 120 */     this.acc.set(o, null);
/*     */   }
/*     */   
/*     */   public String getIdValue(BeanT bean) throws AccessorException, SAXException {
/* 124 */     return this.xacc.print(bean).toString();
/*     */   }
/*     */   
/*     */   public int compareTo(AttributeProperty that) {
/* 128 */     return this.attName.compareTo(that.attName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\AttributeProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */