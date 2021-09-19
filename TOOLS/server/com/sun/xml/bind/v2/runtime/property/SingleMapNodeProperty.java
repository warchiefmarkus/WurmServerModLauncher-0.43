/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.ClassFactory;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.nav.ReflectionNavigator;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeMapPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.TagName;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ final class SingleMapNodeProperty<BeanT, ValueT extends Map>
/*     */   extends PropertyImpl<BeanT>
/*     */ {
/*     */   private final Accessor<BeanT, ValueT> acc;
/*     */   private final Name tagName;
/*     */   private final Name entryTag;
/*     */   private final Name keyTag;
/*     */   private final Name valueTag;
/*     */   private final boolean nillable;
/*     */   private JaxBeanInfo keyBeanInfo;
/*     */   private JaxBeanInfo valueBeanInfo;
/*     */   private final Class<? extends ValueT> mapImplClass;
/*     */   
/*     */   public SingleMapNodeProperty(JAXBContextImpl context, RuntimeMapPropertyInfo prop) {
/* 100 */     super(context, (RuntimePropertyInfo)prop);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     this.itemsLoader = new Loader(false)
/*     */       {
/*     */         public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */         {
/*     */           try {
/* 156 */             BeanT target = (BeanT)state.prev.target;
/* 157 */             Map map = (Map)SingleMapNodeProperty.this.acc.get(target);
/* 158 */             if (map == null) {
/* 159 */               map = (Map)ClassFactory.create(SingleMapNodeProperty.this.mapImplClass);
/* 160 */               SingleMapNodeProperty.this.acc.set(target, map);
/*     */             } 
/* 162 */             map.clear();
/* 163 */             state.target = map;
/* 164 */           } catch (AccessorException e) {
/*     */             
/* 166 */             handleGenericException((Exception)e, true);
/* 167 */             state.target = new HashMap<Object, Object>();
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 173 */           if (ea.matches(SingleMapNodeProperty.this.entryTag)) {
/* 174 */             state.loader = SingleMapNodeProperty.this.entryLoader;
/*     */           } else {
/* 176 */             super.childElement(state, ea);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public Collection<QName> getExpectedChildElements() {
/* 182 */           return Collections.singleton(SingleMapNodeProperty.this.entryTag.toQName());
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     this.entryLoader = new Loader(false)
/*     */       {
/*     */         public void startElement(UnmarshallingContext.State state, TagName ea) {
/* 194 */           state.target = new Object[2];
/*     */         }
/*     */ 
/*     */         
/*     */         public void leaveElement(UnmarshallingContext.State state, TagName ea) {
/* 199 */           Object[] keyValue = (Object[])state.target;
/* 200 */           Map<Object, Object> map = (Map)state.prev.target;
/* 201 */           map.put(keyValue[0], keyValue[1]);
/*     */         }
/*     */ 
/*     */         
/*     */         public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 206 */           if (ea.matches(SingleMapNodeProperty.this.keyTag)) {
/* 207 */             state.loader = SingleMapNodeProperty.this.keyLoader;
/* 208 */             state.receiver = SingleMapNodeProperty.keyReceiver;
/*     */             return;
/*     */           } 
/* 211 */           if (ea.matches(SingleMapNodeProperty.this.valueTag)) {
/* 212 */             state.loader = SingleMapNodeProperty.this.valueLoader;
/* 213 */             state.receiver = SingleMapNodeProperty.valueReceiver;
/*     */             return;
/*     */           } 
/* 216 */           super.childElement(state, ea);
/*     */         }
/*     */         
/*     */         public Collection<QName> getExpectedChildElements()
/*     */         {
/* 221 */           return Arrays.asList(new QName[] { SingleMapNodeProperty.access$400(this.this$0).toQName(), SingleMapNodeProperty.access$700(this.this$0).toQName() }); }
/*     */       }; this.acc = prop.getAccessor().optimize(context); this.tagName = context.nameBuilder.createElementName(prop.getXmlName()); this.entryTag = context.nameBuilder.createElementName("", "entry"); this.keyTag = context.nameBuilder.createElementName("", "key"); this.valueTag = context.nameBuilder.createElementName("", "value"); this.nillable = prop.isCollectionNillable(); this.keyBeanInfo = context.getOrCreate((RuntimeTypeInfo)prop.getKeyType()); this.valueBeanInfo = context.getOrCreate((RuntimeTypeInfo)prop.getValueType()); Class<ValueT> sig = ReflectionNavigator.REFLECTION.erasure(prop.getRawType());
/*     */     this.mapImplClass = ClassFactory.inferImplClass(sig, knownImplClasses);
/*     */   } private static final Class[] knownImplClasses = new Class[] { HashMap.class, TreeMap.class, LinkedHashMap.class }; private Loader keyLoader; private Loader valueLoader; private final Loader itemsLoader; private final Loader entryLoader; public void reset(BeanT bean) throws AccessorException { this.acc.set(bean, null); } public String getIdValue(BeanT bean) { return null; } public PropertyKind getKind() { return PropertyKind.MAP; } public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) { this.keyLoader = this.keyBeanInfo.getLoader(chain.context, true);
/*     */     this.valueLoader = this.valueBeanInfo.getLoader(chain.context, true);
/*     */     handlers.put(this.tagName, new ChildLoader(this.itemsLoader, null)); } private static final class ReceiverImpl implements Receiver
/*     */   {
/* 228 */     public ReceiverImpl(int index) { this.index = index; }
/*     */      private final int index;
/*     */     public void receive(UnmarshallingContext.State state, Object o) {
/* 231 */       ((Object[])state.target)[this.index] = o;
/*     */     }
/*     */   }
/*     */   
/* 235 */   private static final Receiver keyReceiver = new ReceiverImpl(0);
/* 236 */   private static final Receiver valueReceiver = new ReceiverImpl(1);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/* 242 */     Map map = (Map)this.acc.get(o);
/* 243 */     if (map != null) {
/* 244 */       bareStartTag(w, this.tagName, map);
/* 245 */       for (Map.Entry e : map.entrySet()) {
/* 246 */         bareStartTag(w, this.entryTag, null);
/*     */         
/* 248 */         Object key = e.getKey();
/* 249 */         if (key != null) {
/* 250 */           w.startElement(this.keyTag, key);
/* 251 */           w.childAsXsiType(key, this.fieldName, this.keyBeanInfo);
/* 252 */           w.endElement();
/*     */         } 
/*     */         
/* 255 */         Object value = e.getValue();
/* 256 */         if (value != null) {
/* 257 */           w.startElement(this.valueTag, value);
/* 258 */           w.childAsXsiType(value, this.fieldName, this.valueBeanInfo);
/* 259 */           w.endElement();
/*     */         } 
/*     */         
/* 262 */         w.endElement();
/*     */       } 
/* 264 */       w.endElement();
/*     */     }
/* 266 */     else if (this.nillable) {
/* 267 */       w.startElement(this.tagName, null);
/* 268 */       w.writeXsiNilTrue();
/* 269 */       w.endElement();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void bareStartTag(XMLSerializer w, Name tagName, Object peer) throws IOException, XMLStreamException, SAXException {
/* 274 */     w.startElement(tagName, peer);
/* 275 */     w.endNamespaceDecls(peer);
/* 276 */     w.endAttributes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Accessor getElementPropertyAccessor(String nsUri, String localName) {
/* 281 */     if (this.tagName.equals(nsUri, localName))
/* 282 */       return this.acc; 
/* 283 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\SingleMapNodeProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */