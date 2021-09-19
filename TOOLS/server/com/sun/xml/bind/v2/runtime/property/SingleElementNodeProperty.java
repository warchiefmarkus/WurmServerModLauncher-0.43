/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeRef;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ final class SingleElementNodeProperty<BeanT, ValueT>
/*     */   extends PropertyImpl<BeanT>
/*     */ {
/*     */   private final Accessor<BeanT, ValueT> acc;
/*     */   private final boolean nillable;
/*     */   private final QName[] acceptedElements;
/*  78 */   private final Map<Class, TagAndType> typeNames = (Map)new HashMap<Class<?>, TagAndType>();
/*     */ 
/*     */   
/*     */   private RuntimeElementPropertyInfo prop;
/*     */ 
/*     */   
/*     */   private final Name nullTagName;
/*     */ 
/*     */   
/*     */   public SingleElementNodeProperty(JAXBContextImpl context, RuntimeElementPropertyInfo prop) {
/*  88 */     super(context, (RuntimePropertyInfo)prop);
/*  89 */     this.acc = prop.getAccessor().optimize(context);
/*  90 */     this.prop = prop;
/*     */     
/*  92 */     QName nt = null;
/*  93 */     boolean nil = false;
/*     */     
/*  95 */     this.acceptedElements = new QName[prop.getTypes().size()];
/*  96 */     for (int i = 0; i < this.acceptedElements.length; i++) {
/*  97 */       this.acceptedElements[i] = ((RuntimeTypeRef)prop.getTypes().get(i)).getTagName();
/*     */     }
/*  99 */     for (RuntimeTypeRef e : prop.getTypes()) {
/* 100 */       JaxBeanInfo beanInfo = context.getOrCreate((RuntimeTypeInfo)e.getTarget());
/* 101 */       if (nt == null) nt = e.getTagName(); 
/* 102 */       this.typeNames.put(beanInfo.jaxbType, new TagAndType(context.nameBuilder.createElementName(e.getTagName()), beanInfo));
/*     */       
/* 104 */       nil |= e.isNillable();
/*     */     } 
/*     */     
/* 107 */     this.nullTagName = context.nameBuilder.createElementName(nt);
/*     */     
/* 109 */     this.nillable = nil;
/*     */   }
/*     */   
/*     */   public void wrapUp() {
/* 113 */     super.wrapUp();
/* 114 */     this.prop = null;
/*     */   }
/*     */   
/*     */   public void reset(BeanT bean) throws AccessorException {
/* 118 */     this.acc.set(bean, null);
/*     */   }
/*     */   
/*     */   public String getIdValue(BeanT beanT) {
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/* 126 */     ValueT v = (ValueT)this.acc.get(o);
/* 127 */     if (v != null) {
/* 128 */       Class<?> vtype = v.getClass();
/* 129 */       TagAndType tt = this.typeNames.get(vtype);
/*     */       
/* 131 */       if (tt == null) {
/* 132 */         for (Map.Entry<Class<?>, TagAndType> e : this.typeNames.entrySet()) {
/* 133 */           if (((Class)e.getKey()).isAssignableFrom(vtype)) {
/* 134 */             tt = e.getValue();
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/* 140 */       if (tt == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 146 */         w.startElement(((TagAndType)this.typeNames.values().iterator().next()).tagName, null);
/* 147 */         w.childAsXsiType(v, this.fieldName, w.grammar.getBeanInfo(Object.class));
/*     */       } else {
/* 149 */         w.startElement(tt.tagName, null);
/* 150 */         w.childAsXsiType(v, this.fieldName, tt.beanInfo);
/*     */       } 
/* 152 */       w.endElement();
/*     */     }
/* 154 */     else if (this.nillable) {
/* 155 */       w.startElement(this.nullTagName, null);
/* 156 */       w.writeXsiNilTrue();
/* 157 */       w.endElement();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
/* 162 */     JAXBContextImpl context = chain.context;
/*     */     
/* 164 */     for (RuntimeTypeRef runtimeTypeRef : this.prop.getTypes()) {
/* 165 */       DefaultValueLoaderDecorator defaultValueLoaderDecorator; XsiNilLoader.Single single; JaxBeanInfo bi = context.getOrCreate((RuntimeTypeInfo)runtimeTypeRef.getTarget());
/*     */ 
/*     */       
/* 168 */       Loader l = bi.getLoader(context, !Modifier.isFinal(bi.jaxbType.getModifiers()));
/* 169 */       if (runtimeTypeRef.getDefaultValue() != null)
/* 170 */         defaultValueLoaderDecorator = new DefaultValueLoaderDecorator(l, runtimeTypeRef.getDefaultValue()); 
/* 171 */       if (this.nillable || chain.context.allNillable)
/* 172 */         single = new XsiNilLoader.Single((Loader)defaultValueLoaderDecorator, this.acc); 
/* 173 */       handlers.put(runtimeTypeRef.getTagName(), new ChildLoader((Loader)single, (Receiver)this.acc));
/*     */     } 
/*     */   }
/*     */   
/*     */   public PropertyKind getKind() {
/* 178 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName) {
/* 183 */     for (QName n : this.acceptedElements) {
/* 184 */       if (n.getNamespaceURI().equals(nsUri) && n.getLocalPart().equals(localName))
/* 185 */         return this.acc; 
/*     */     } 
/* 187 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\SingleElementNodeProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */