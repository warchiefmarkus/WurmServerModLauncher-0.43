/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.core.TypeRef;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeRef;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.RuntimeUtil;
/*     */ import com.sun.xml.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.ListIterator;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Lister;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.NullSafeAccessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.TextLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.JAXBException;
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
/*     */ abstract class ArrayElementProperty<BeanT, ListT, ItemT>
/*     */   extends ArrayERProperty<BeanT, ListT, ItemT>
/*     */ {
/*  80 */   private final Map<Class, TagAndType> typeMap = (Map)new HashMap<Class<?>, TagAndType>();
/*     */ 
/*     */ 
/*     */   
/*  84 */   private Map<TypeRef<Type, Class>, JaxBeanInfo> refs = new HashMap<TypeRef<Type, Class>, JaxBeanInfo>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected RuntimeElementPropertyInfo prop;
/*     */ 
/*     */   
/*     */   private final Name nillableTagName;
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArrayElementProperty(JAXBContextImpl grammar, RuntimeElementPropertyInfo prop) {
/*  96 */     super(grammar, (RuntimePropertyInfo)prop, prop.getXmlName(), prop.isCollectionNillable());
/*  97 */     this.prop = prop;
/*     */     
/*  99 */     List<? extends RuntimeTypeRef> types = prop.getTypes();
/*     */     
/* 101 */     Name n = null;
/*     */     
/* 103 */     for (RuntimeTypeRef typeRef : types) {
/* 104 */       Class type = (Class)typeRef.getTarget().getType();
/* 105 */       if (type.isPrimitive()) {
/* 106 */         type = (Class)RuntimeUtil.primitiveToBox.get(type);
/*     */       }
/* 108 */       JaxBeanInfo beanInfo = grammar.getOrCreate((RuntimeTypeInfo)typeRef.getTarget());
/* 109 */       TagAndType tt = new TagAndType(grammar.nameBuilder.createElementName(typeRef.getTagName()), beanInfo);
/*     */ 
/*     */       
/* 112 */       this.typeMap.put(type, tt);
/* 113 */       this.refs.put(typeRef, beanInfo);
/* 114 */       if (typeRef.isNillable() && n == null) {
/* 115 */         n = tt.tagName;
/*     */       }
/*     */     } 
/* 118 */     this.nillableTagName = n;
/*     */   }
/*     */ 
/*     */   
/*     */   public void wrapUp() {
/* 123 */     super.wrapUp();
/* 124 */     this.refs = null;
/* 125 */     this.prop = null;
/*     */   }
/*     */   
/*     */   protected void serializeListBody(BeanT beanT, XMLSerializer w, ListT list) throws IOException, XMLStreamException, SAXException, AccessorException {
/* 129 */     ListIterator<ItemT> itr = this.lister.iterator(list, w);
/*     */     
/* 131 */     boolean isIdref = itr instanceof Lister.IDREFSIterator;
/*     */     
/* 133 */     while (itr.hasNext()) {
/*     */       try {
/* 135 */         ItemT item = (ItemT)itr.next();
/* 136 */         if (item != null) {
/* 137 */           Class<?> itemType = item.getClass();
/* 138 */           if (isIdref)
/*     */           {
/*     */             
/* 141 */             itemType = ((Lister.IDREFSIterator)itr).last().getClass();
/*     */           }
/*     */           
/* 144 */           TagAndType tt = this.typeMap.get(itemType);
/* 145 */           while (tt == null && itemType != null) {
/*     */             
/* 147 */             itemType = itemType.getSuperclass();
/* 148 */             tt = this.typeMap.get(itemType);
/*     */           } 
/*     */           
/* 151 */           if (tt == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 164 */             w.startElement(((TagAndType)this.typeMap.values().iterator().next()).tagName, null);
/* 165 */             w.childAsXsiType(item, this.fieldName, w.grammar.getBeanInfo(Object.class));
/*     */           } else {
/* 167 */             w.startElement(tt.tagName, null);
/* 168 */             serializeItem(tt.beanInfo, item, w);
/*     */           } 
/*     */           
/* 171 */           w.endElement(); continue;
/*     */         } 
/* 173 */         if (this.nillableTagName != null) {
/* 174 */           w.startElement(this.nillableTagName, null);
/* 175 */           w.writeXsiNilTrue();
/* 176 */           w.endElement();
/*     */         }
/*     */       
/* 179 */       } catch (JAXBException e) {
/* 180 */         w.reportError(this.fieldName, (Throwable)e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void serializeItem(JaxBeanInfo paramJaxBeanInfo, ItemT paramItemT, XMLSerializer paramXMLSerializer) throws SAXException, AccessorException, IOException, XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createBodyUnmarshaller(UnmarshallerChain chain, QNameMap<ChildLoader> loaders) {
/* 196 */     int offset = chain.allocateOffset();
/* 197 */     Receiver recv = new ArrayERProperty.ReceiverImpl(this, offset);
/*     */     
/* 199 */     for (RuntimeTypeRef typeRef : this.prop.getTypes()) {
/*     */       XsiNilLoader.Array array; DefaultValueLoaderDecorator defaultValueLoaderDecorator;
/* 201 */       Name tagName = chain.context.nameBuilder.createElementName(typeRef.getTagName());
/* 202 */       Loader item = createItemUnmarshaller(chain, typeRef);
/*     */       
/* 204 */       if (typeRef.isNillable() || chain.context.allNillable)
/* 205 */         array = new XsiNilLoader.Array(item); 
/* 206 */       if (typeRef.getDefaultValue() != null) {
/* 207 */         defaultValueLoaderDecorator = new DefaultValueLoaderDecorator((Loader)array, typeRef.getDefaultValue());
/*     */       }
/* 209 */       loaders.put(tagName, new ChildLoader((Loader)defaultValueLoaderDecorator, recv));
/*     */     } 
/*     */   }
/*     */   
/*     */   public final PropertyKind getKind() {
/* 214 */     return PropertyKind.ELEMENT;
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
/*     */   private Loader createItemUnmarshaller(UnmarshallerChain chain, RuntimeTypeRef typeRef) {
/* 231 */     if (PropertyFactory.isLeaf(typeRef.getSource())) {
/* 232 */       Transducer xducer = typeRef.getTransducer();
/* 233 */       return (Loader)new TextLoader(xducer);
/*     */     } 
/* 235 */     return ((JaxBeanInfo)this.refs.get(typeRef)).getLoader(chain.context, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName) {
/* 240 */     if (this.wrapperTagName != null) {
/* 241 */       if (this.wrapperTagName.equals(nsUri, localName))
/* 242 */         return this.acc; 
/*     */     } else {
/* 244 */       for (TagAndType tt : this.typeMap.values()) {
/* 245 */         if (tt.tagName.equals(nsUri, localName))
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 250 */           return (Accessor)new NullSafeAccessor(this.acc, this.lister); } 
/*     */       } 
/*     */     } 
/* 253 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\ArrayElementProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */