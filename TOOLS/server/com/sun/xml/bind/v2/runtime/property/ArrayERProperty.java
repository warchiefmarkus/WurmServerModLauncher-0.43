/*     */ package com.sun.xml.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Lister;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.TagName;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
/*     */ import com.sun.xml.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ abstract class ArrayERProperty<BeanT, ListT, ItemT>
/*     */   extends ArrayProperty<BeanT, ListT, ItemT>
/*     */ {
/*     */   protected final Name wrapperTagName;
/*     */   protected final boolean isWrapperNillable;
/*     */   
/*     */   protected ArrayERProperty(JAXBContextImpl grammar, RuntimePropertyInfo prop, QName tagName, boolean isWrapperNillable) {
/*  84 */     super(grammar, prop);
/*  85 */     if (tagName == null) {
/*  86 */       this.wrapperTagName = null;
/*     */     } else {
/*  88 */       this.wrapperTagName = grammar.nameBuilder.createElementName(tagName);
/*  89 */     }  this.isWrapperNillable = isWrapperNillable;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ItemsLoader
/*     */     extends Loader
/*     */   {
/*     */     private final Accessor acc;
/*     */     private final Lister lister;
/*     */     private final QNameMap<ChildLoader> children;
/*     */     
/*     */     public ItemsLoader(Accessor acc, Lister lister, QNameMap<ChildLoader> children) {
/* 101 */       super(false);
/* 102 */       this.acc = acc;
/* 103 */       this.lister = lister;
/* 104 */       this.children = children;
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 109 */       UnmarshallingContext context = state.getContext();
/* 110 */       context.startScope(1);
/*     */       
/* 112 */       state.target = state.prev.target;
/*     */ 
/*     */       
/* 115 */       context.getScope(0).start(this.acc, this.lister);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 122 */       ChildLoader child = (ChildLoader)this.children.get(ea.uri, ea.local);
/* 123 */       if (child != null) {
/* 124 */         state.loader = child.loader;
/* 125 */         state.receiver = child.receiver;
/*     */       } else {
/* 127 */         super.childElement(state, ea);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 133 */       state.getContext().endScope(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<QName> getExpectedChildElements() {
/* 138 */       return this.children.keySet();
/*     */     }
/*     */   }
/*     */   
/*     */   public final void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/* 143 */     ListT list = (ListT)this.acc.get(o);
/*     */     
/* 145 */     if (list != null) {
/* 146 */       if (this.wrapperTagName != null) {
/* 147 */         w.startElement(this.wrapperTagName, null);
/* 148 */         w.endNamespaceDecls(list);
/* 149 */         w.endAttributes();
/*     */       } 
/*     */       
/* 152 */       serializeListBody(o, w, list);
/*     */       
/* 154 */       if (this.wrapperTagName != null) {
/* 155 */         w.endElement();
/*     */       }
/*     */     }
/* 158 */     else if (this.isWrapperNillable) {
/* 159 */       w.startElement(this.wrapperTagName, null);
/* 160 */       w.writeXsiNilTrue();
/* 161 */       w.endElement();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void serializeListBody(BeanT paramBeanT, XMLSerializer paramXMLSerializer, ListT paramListT) throws IOException, XMLStreamException, SAXException, AccessorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void createBodyUnmarshaller(UnmarshallerChain paramUnmarshallerChain, QNameMap<ChildLoader> paramQNameMap);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> loaders) {
/* 182 */     if (this.wrapperTagName != null) {
/* 183 */       XsiNilLoader xsiNilLoader; UnmarshallerChain c = new UnmarshallerChain(chain.context);
/* 184 */       QNameMap<ChildLoader> m = new QNameMap();
/* 185 */       createBodyUnmarshaller(c, m);
/* 186 */       Loader loader = new ItemsLoader(this.acc, this.lister, m);
/* 187 */       if (this.isWrapperNillable || chain.context.allNillable)
/* 188 */         xsiNilLoader = new XsiNilLoader(loader); 
/* 189 */       loaders.put(this.wrapperTagName, new ChildLoader((Loader)xsiNilLoader, null));
/*     */     } else {
/* 191 */       createBodyUnmarshaller(chain, loaders);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected final class ReceiverImpl
/*     */     implements Receiver
/*     */   {
/*     */     private final int offset;
/*     */     
/*     */     protected ReceiverImpl(int offset) {
/* 202 */       this.offset = offset;
/*     */     }
/*     */     
/*     */     public void receive(UnmarshallingContext.State state, Object o) throws SAXException {
/* 206 */       state.getContext().getScope(this.offset).add(ArrayERProperty.this.acc, ArrayERProperty.this.lister, o);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\ArrayERProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */