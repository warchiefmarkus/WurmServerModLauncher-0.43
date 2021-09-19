/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeRef;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.ListTransducedAccessorImpl;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.LeafPropertyLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
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
/*     */ final class ListElementProperty<BeanT, ListT, ItemT>
/*     */   extends ArrayProperty<BeanT, ListT, ItemT>
/*     */ {
/*     */   private final Name tagName;
/*     */   private final TransducedAccessor<BeanT> xacc;
/*     */   
/*     */   public ListElementProperty(JAXBContextImpl grammar, RuntimeElementPropertyInfo prop) {
/*  75 */     super(grammar, (RuntimePropertyInfo)prop);
/*     */     
/*  77 */     assert prop.isValueList();
/*  78 */     assert prop.getTypes().size() == 1;
/*  79 */     RuntimeTypeRef ref = prop.getTypes().get(0);
/*     */     
/*  81 */     this.tagName = grammar.nameBuilder.createElementName(ref.getTagName());
/*     */ 
/*     */     
/*  84 */     Transducer xducer = ref.getTransducer();
/*     */     
/*  86 */     this.xacc = (TransducedAccessor<BeanT>)new ListTransducedAccessorImpl(xducer, this.acc, this.lister);
/*     */   }
/*     */   
/*     */   public PropertyKind getKind() {
/*  90 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */   
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
/*  94 */     handlers.put(this.tagName, new ChildLoader((Loader)new LeafPropertyLoader(this.xacc), null));
/*     */   }
/*     */   
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/*  98 */     ListT list = (ListT)this.acc.get(o);
/*     */     
/* 100 */     if (list != null)
/* 101 */       if (this.xacc.useNamespace()) {
/* 102 */         w.startElement(this.tagName, null);
/* 103 */         this.xacc.declareNamespace(o, w);
/* 104 */         w.endNamespaceDecls(list);
/* 105 */         w.endAttributes();
/* 106 */         this.xacc.writeText(w, o, this.fieldName);
/* 107 */         w.endElement();
/*     */       } else {
/* 109 */         this.xacc.writeLeafElement(w, this.tagName, o, this.fieldName);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\ListElementProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */