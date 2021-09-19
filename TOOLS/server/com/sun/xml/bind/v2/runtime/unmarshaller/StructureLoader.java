/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.runtime.ClassBeanInfoImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.bind.v2.runtime.property.AttributeProperty;
/*     */ import com.sun.xml.bind.v2.runtime.property.Property;
/*     */ import com.sun.xml.bind.v2.runtime.property.StructureLoaderBuilder;
/*     */ import com.sun.xml.bind.v2.runtime.property.UnmarshallerChain;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public final class StructureLoader
/*     */   extends Loader
/*     */ {
/*  77 */   private final QNameMap<ChildLoader> childUnmarshallers = new QNameMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChildLoader catchAll;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChildLoader textHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private QNameMap<TransducedAccessor> attUnmarshallers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Accessor<Object, Map<QName, String>> attCatchAll;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final JaxBeanInfo beanInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int frameSize;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureLoader(ClassBeanInfoImpl beanInfo) {
/* 113 */     super(true);
/* 114 */     this.beanInfo = (JaxBeanInfo)beanInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(JAXBContextImpl context, ClassBeanInfoImpl beanInfo, Accessor<?, Map<QName, String>> attWildcard) {
/* 125 */     UnmarshallerChain chain = new UnmarshallerChain(context);
/* 126 */     for (ClassBeanInfoImpl bi = beanInfo; bi != null; bi = bi.superClazz) {
/* 127 */       for (int i = bi.properties.length - 1; i >= 0; i--) {
/* 128 */         AttributeProperty ap; Property p = bi.properties[i];
/*     */         
/* 130 */         switch (p.getKind()) {
/*     */           case ATTRIBUTE:
/* 132 */             if (this.attUnmarshallers == null)
/* 133 */               this.attUnmarshallers = new QNameMap(); 
/* 134 */             ap = (AttributeProperty)p;
/* 135 */             this.attUnmarshallers.put(ap.attName.toQName(), ap.xacc);
/*     */             break;
/*     */           case ELEMENT:
/*     */           case REFERENCE:
/*     */           case MAP:
/*     */           case VALUE:
/* 141 */             p.buildChildElementUnmarshallers(chain, this.childUnmarshallers);
/*     */             break;
/*     */         } 
/*     */       
/*     */       } 
/*     */     } 
/* 147 */     this.frameSize = chain.getScopeSize();
/*     */     
/* 149 */     this.textHandler = (ChildLoader)this.childUnmarshallers.get(StructureLoaderBuilder.TEXT_HANDLER);
/* 150 */     this.catchAll = (ChildLoader)this.childUnmarshallers.get(StructureLoaderBuilder.CATCH_ALL);
/*     */     
/* 152 */     if (attWildcard != null) {
/* 153 */       this.attCatchAll = (Accessor)attWildcard;
/*     */ 
/*     */       
/* 156 */       if (this.attUnmarshallers == null)
/* 157 */         this.attUnmarshallers = EMPTY; 
/*     */     } else {
/* 159 */       this.attCatchAll = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 165 */     UnmarshallingContext context = state.getContext();
/*     */ 
/*     */ 
/*     */     
/* 169 */     assert !this.beanInfo.isImmutable();
/*     */ 
/*     */     
/* 172 */     Object child = context.getInnerPeer();
/*     */     
/* 174 */     if (child != null && this.beanInfo.jaxbType != child.getClass()) {
/* 175 */       child = null;
/*     */     }
/* 177 */     if (child != null) {
/* 178 */       this.beanInfo.reset(child, context);
/*     */     }
/* 180 */     if (child == null) {
/* 181 */       child = context.createInstance(this.beanInfo);
/*     */     }
/* 183 */     context.recordInnerPeer(child);
/*     */     
/* 185 */     state.target = child;
/*     */     
/* 187 */     fireBeforeUnmarshal(this.beanInfo, child, state);
/*     */ 
/*     */     
/* 190 */     context.startScope(this.frameSize);
/*     */     
/* 192 */     if (this.attUnmarshallers != null) {
/* 193 */       Attributes atts = ea.atts;
/* 194 */       for (int i = 0; i < atts.getLength(); i++) {
/* 195 */         String auri = atts.getURI(i);
/* 196 */         String alocal = atts.getLocalName(i);
/* 197 */         String avalue = atts.getValue(i);
/* 198 */         TransducedAccessor xacc = (TransducedAccessor)this.attUnmarshallers.get(auri, alocal);
/*     */         
/*     */         try {
/* 201 */           if (xacc != null) {
/* 202 */             xacc.parse(child, avalue);
/*     */           }
/* 204 */           else if (this.attCatchAll != null) {
/* 205 */             String qname = atts.getQName(i);
/* 206 */             if (!atts.getURI(i).equals("http://www.w3.org/2001/XMLSchema-instance"))
/*     */             { String prefix;
/* 208 */               Object o = state.target;
/* 209 */               Map<QName, String> map = (Map<QName, String>)this.attCatchAll.get(o);
/* 210 */               if (map == null) {
/*     */ 
/*     */ 
/*     */                 
/* 214 */                 if (this.attCatchAll.valueType.isAssignableFrom(HashMap.class)) {
/* 215 */                   map = new HashMap<QName, String>();
/*     */                 }
/*     */                 else {
/*     */                   
/* 219 */                   context.handleError(Messages.UNABLE_TO_CREATE_MAP.format(new Object[] { this.attCatchAll.valueType }));
/*     */                   return;
/*     */                 } 
/* 222 */                 this.attCatchAll.set(o, map);
/*     */               } 
/*     */ 
/*     */               
/* 226 */               int idx = qname.indexOf(':');
/* 227 */               if (idx < 0) { prefix = ""; }
/* 228 */               else { prefix = qname.substring(0, idx); }
/*     */               
/* 230 */               map.put(new QName(auri, alocal, prefix), avalue); } 
/*     */           } 
/* 232 */         } catch (AccessorException e) {
/* 233 */           handleGenericException((Exception)e, true);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void childElement(UnmarshallingContext.State state, TagName arg) throws SAXException {
/* 241 */     ChildLoader child = (ChildLoader)this.childUnmarshallers.get(arg.uri, arg.local);
/* 242 */     if (child == null) {
/* 243 */       child = this.catchAll;
/* 244 */       if (child == null) {
/* 245 */         super.childElement(state, arg);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 250 */     state.loader = child.loader;
/* 251 */     state.receiver = child.receiver;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<QName> getExpectedChildElements() {
/* 256 */     return this.childUnmarshallers.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
/* 261 */     if (this.textHandler != null)
/* 262 */       this.textHandler.loader.text(state, text); 
/*     */   }
/*     */   
/*     */   public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 266 */     state.getContext().endScope(this.frameSize);
/* 267 */     fireAfterUnmarshal(this.beanInfo, state.target, state.prev);
/*     */   }
/*     */   
/* 270 */   private static final QNameMap<TransducedAccessor> EMPTY = new QNameMap();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\StructureLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */