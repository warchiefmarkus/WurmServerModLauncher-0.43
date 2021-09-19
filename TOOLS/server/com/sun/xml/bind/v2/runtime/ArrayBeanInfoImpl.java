/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeArrayInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.TagName;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ final class ArrayBeanInfoImpl
/*     */   extends JaxBeanInfo
/*     */ {
/*     */   private final Class itemType;
/*     */   private final JaxBeanInfo itemBeanInfo;
/*     */   private Loader loader;
/*     */   
/*     */   public ArrayBeanInfoImpl(JAXBContextImpl owner, RuntimeArrayInfo rai) {
/*  72 */     super(owner, (RuntimeTypeInfo)rai, rai.getType(), rai.getTypeName(), false, true, false);
/*  73 */     this.itemType = this.jaxbType.getComponentType();
/*  74 */     this.itemBeanInfo = owner.getOrCreate((RuntimeTypeInfo)rai.getItemType());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void link(JAXBContextImpl grammar) {
/*  79 */     getLoader(grammar, false);
/*  80 */     super.link(grammar);
/*     */   }
/*     */   
/*     */   private final class ArrayLoader extends Loader implements Receiver {
/*     */     public ArrayLoader(JAXBContextImpl owner) {
/*  85 */       super(false);
/*  86 */       this.itemLoader = ArrayBeanInfoImpl.this.itemBeanInfo.getLoader(owner, true);
/*     */     }
/*     */ 
/*     */     
/*     */     private final Loader itemLoader;
/*     */     
/*     */     public void startElement(UnmarshallingContext.State state, TagName ea) {
/*  93 */       state.target = new ArrayList();
/*     */     }
/*     */ 
/*     */     
/*     */     public void leaveElement(UnmarshallingContext.State state, TagName ea) {
/*  98 */       state.target = ArrayBeanInfoImpl.this.toArray((List)state.target);
/*     */     }
/*     */ 
/*     */     
/*     */     public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 103 */       if (ea.matches("", "item")) {
/* 104 */         state.loader = this.itemLoader;
/* 105 */         state.receiver = this;
/*     */       } else {
/* 107 */         super.childElement(state, ea);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<QName> getExpectedChildElements() {
/* 113 */       return Collections.singleton(new QName("", "item"));
/*     */     }
/*     */     
/*     */     public void receive(UnmarshallingContext.State state, Object o) {
/* 117 */       ((List<Object>)state.target).add(o);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object toArray(List list) {
/* 122 */     int len = list.size();
/* 123 */     Object array = Array.newInstance(this.itemType, len);
/* 124 */     for (int i = 0; i < len; i++)
/* 125 */       Array.set(array, i, list.get(i)); 
/* 126 */     return array;
/*     */   }
/*     */   
/*     */   public void serializeBody(Object array, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 130 */     int len = Array.getLength(array);
/* 131 */     for (int i = 0; i < len; i++) {
/* 132 */       Object item = Array.get(array, i);
/*     */       
/* 134 */       target.startElement("", "item", null, null);
/* 135 */       if (item == null) {
/* 136 */         target.writeXsiNilTrue();
/*     */       } else {
/* 138 */         target.childAsXsiType(item, "arrayItem", this.itemBeanInfo);
/*     */       } 
/* 140 */       target.endElement();
/*     */     } 
/*     */   }
/*     */   
/*     */   public final String getElementNamespaceURI(Object array) {
/* 145 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public final String getElementLocalName(Object array) {
/* 149 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object createInstance(UnmarshallingContext context) {
/* 154 */     return new ArrayList();
/*     */   }
/*     */   
/*     */   public final boolean reset(Object array, UnmarshallingContext context) {
/* 158 */     return false;
/*     */   }
/*     */   
/*     */   public final String getId(Object array, XMLSerializer target) {
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void serializeAttributes(Object array, XMLSerializer target) {}
/*     */ 
/*     */   
/*     */   public final void serializeRoot(Object array, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 170 */     target.reportError((ValidationEvent)new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(new Object[] { array.getClass().getName() }, ), null, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void serializeURIs(Object array, XMLSerializer target) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Transducer getTransducer() {
/* 183 */     return null;
/*     */   }
/*     */   
/*     */   public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
/* 187 */     if (this.loader == null) {
/* 188 */       this.loader = new ArrayLoader(context);
/*     */     }
/*     */     
/* 191 */     return this.loader;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\ArrayBeanInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */