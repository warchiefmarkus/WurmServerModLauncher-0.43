/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeValuePropertyInfo;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ValuePropertyLoader;
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
/*     */ public final class ValueProperty<BeanT>
/*     */   extends PropertyImpl<BeanT>
/*     */ {
/*     */   private final TransducedAccessor<BeanT> xacc;
/*     */   private final Accessor<BeanT, ?> acc;
/*     */   
/*     */   public ValueProperty(JAXBContextImpl context, RuntimeValuePropertyInfo prop) {
/*  76 */     super(context, (RuntimePropertyInfo)prop);
/*  77 */     this.xacc = TransducedAccessor.get(context, (RuntimeNonElementRef)prop);
/*  78 */     this.acc = prop.getAccessor();
/*     */   }
/*     */   
/*     */   public final void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/*  82 */     if (this.xacc.hasValue(o))
/*  83 */       this.xacc.writeText(w, o, this.fieldName); 
/*     */   }
/*     */   
/*     */   public void serializeURIs(BeanT o, XMLSerializer w) throws SAXException, AccessorException {
/*  87 */     this.xacc.declareNamespace(o, w);
/*     */   }
/*     */   
/*     */   public boolean hasSerializeURIAction() {
/*  91 */     return this.xacc.useNamespace();
/*     */   }
/*     */   
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chainElem, QNameMap<ChildLoader> handlers) {
/*  95 */     handlers.put(StructureLoaderBuilder.TEXT_HANDLER, new ChildLoader((Loader)new ValuePropertyLoader(this.xacc), null));
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyKind getKind() {
/* 100 */     return PropertyKind.VALUE;
/*     */   }
/*     */   
/*     */   public void reset(BeanT o) throws AccessorException {
/* 104 */     this.acc.set(o, null);
/*     */   }
/*     */   
/*     */   public String getIdValue(BeanT bean) throws AccessorException, SAXException {
/* 108 */     return this.xacc.print(bean).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\ValueProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */