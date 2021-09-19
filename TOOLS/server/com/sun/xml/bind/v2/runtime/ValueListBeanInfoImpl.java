/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.FinalArrayList;
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
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
/*     */ final class ValueListBeanInfoImpl
/*     */   extends JaxBeanInfo
/*     */ {
/*     */   private final Class itemType;
/*     */   private final Transducer xducer;
/*     */   private final Loader loader;
/*     */   
/*     */   public ValueListBeanInfoImpl(JAXBContextImpl owner, Class<BeanT> arrayType) throws JAXBException {
/*  67 */     super(owner, null, arrayType, false, true, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     this.loader = new Loader(true)
/*     */       {
/*     */         public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
/*  76 */           FinalArrayList finalArrayList = new FinalArrayList();
/*     */           
/*  78 */           int idx = 0;
/*  79 */           int len = text.length();
/*     */           
/*     */           while (true) {
/*  82 */             int p = idx;
/*  83 */             while (p < len && !WhiteSpaceProcessor.isWhiteSpace(text.charAt(p))) {
/*  84 */               p++;
/*     */             }
/*  86 */             CharSequence token = text.subSequence(idx, p);
/*  87 */             if (!token.equals("")) {
/*     */               try {
/*  89 */                 finalArrayList.add(ValueListBeanInfoImpl.this.xducer.parse(token));
/*  90 */               } catch (AccessorException e) {
/*  91 */                 handleGenericException((Exception)e, true);
/*     */                 continue;
/*     */               } 
/*     */             }
/*  95 */             if (p == len)
/*     */               break; 
/*  97 */             while (p < len && WhiteSpaceProcessor.isWhiteSpace(text.charAt(p)))
/*  98 */               p++; 
/*  99 */             if (p == len)
/*     */               break; 
/* 101 */             idx = p;
/*     */           } 
/*     */           
/* 104 */           state.target = ValueListBeanInfoImpl.this.toArray((List)finalArrayList); }
/*     */       };
/*     */     this.itemType = this.jaxbType.getComponentType();
/*     */     this.xducer = owner.getBeanInfo(arrayType.getComponentType(), true).getTransducer();
/*     */     assert this.xducer != null; } private Object toArray(List list) {
/* 109 */     int len = list.size();
/* 110 */     Object array = Array.newInstance(this.itemType, len);
/* 111 */     for (int i = 0; i < len; i++)
/* 112 */       Array.set(array, i, list.get(i)); 
/* 113 */     return array;
/*     */   }
/*     */   
/*     */   public void serializeBody(Object array, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 117 */     int len = Array.getLength(array);
/* 118 */     for (int i = 0; i < len; i++) {
/* 119 */       Object item = Array.get(array, i);
/*     */       try {
/* 121 */         this.xducer.writeText(target, item, "arrayItem");
/* 122 */       } catch (AccessorException e) {
/* 123 */         target.reportError("arrayItem", (Throwable)e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void serializeURIs(Object array, XMLSerializer target) throws SAXException {
/* 129 */     if (this.xducer.useNamespace()) {
/* 130 */       int len = Array.getLength(array);
/* 131 */       for (int i = 0; i < len; i++) {
/* 132 */         Object item = Array.get(array, i);
/*     */         try {
/* 134 */           this.xducer.declareNamespace(item, target);
/* 135 */         } catch (AccessorException e) {
/* 136 */           target.reportError("arrayItem", (Throwable)e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public final String getElementNamespaceURI(Object array) {
/* 143 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public final String getElementLocalName(Object array) {
/* 147 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public final Object createInstance(UnmarshallingContext context) {
/* 151 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public final boolean reset(Object array, UnmarshallingContext context) {
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   public final String getId(Object array, XMLSerializer target) {
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void serializeAttributes(Object array, XMLSerializer target) {}
/*     */ 
/*     */   
/*     */   public final void serializeRoot(Object array, XMLSerializer target) throws SAXException {
/* 167 */     target.reportError((ValidationEvent)new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(new Object[] { array.getClass().getName() }, ), null, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Transducer getTransducer() {
/* 176 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
/* 181 */     return this.loader;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\ValueListBeanInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */