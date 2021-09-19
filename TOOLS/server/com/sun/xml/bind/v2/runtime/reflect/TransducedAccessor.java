/*     */ package com.sun.xml.bind.v2.runtime.reflect;
/*     */ 
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.istack.SAXException2;
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.impl.RuntimeModelBuilder;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.opt.OptimizedTransducedAccessorFactory;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.LocatorEx;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Patcher;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Callable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TransducedAccessor<BeanT>
/*     */ {
/*     */   public boolean useNamespace() {
/*  89 */     return false;
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
/*     */   public void declareNamespace(BeanT o, XMLSerializer w) throws AccessorException, SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract CharSequence print(@NotNull BeanT paramBeanT) throws AccessorException, SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void parse(BeanT paramBeanT, CharSequence paramCharSequence) throws AccessorException, SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean hasValue(BeanT paramBeanT) throws AccessorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TransducedAccessor<T> get(JAXBContextImpl context, RuntimeNonElementRef ref) {
/* 153 */     Transducer<?> xducer = RuntimeModelBuilder.createTransducer(ref);
/* 154 */     RuntimePropertyInfo prop = ref.getSource();
/*     */     
/* 156 */     if (prop.isCollection()) {
/* 157 */       return new ListTransducedAccessorImpl<T, Object, Object, Object>(xducer, prop.getAccessor(), Lister.create(Navigator.REFLECTION.erasure(prop.getRawType()), prop.id(), prop.getAdapter()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 162 */     if (prop.id() == ID.IDREF) {
/* 163 */       return new IDREFTransducedAccessorImpl<T, Object>(prop.getAccessor());
/*     */     }
/* 165 */     if (xducer.isDefault() && !context.fastBoot) {
/* 166 */       TransducedAccessor<T> xa = OptimizedTransducedAccessorFactory.get(prop);
/* 167 */       if (xa != null) return xa;
/*     */     
/*     */     } 
/* 170 */     if (xducer.useNamespace()) {
/* 171 */       return new CompositeContextDependentTransducedAccessorImpl<T, Object>(context, xducer, prop.getAccessor());
/*     */     }
/* 173 */     return new CompositeTransducedAccessorImpl<T, Object>(context, xducer, prop.getAccessor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void writeLeafElement(XMLSerializer paramXMLSerializer, Name paramName, BeanT paramBeanT, String paramString) throws SAXException, AccessorException, IOException, XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void writeText(XMLSerializer paramXMLSerializer, BeanT paramBeanT, String paramString) throws AccessorException, SAXException, IOException, XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class CompositeContextDependentTransducedAccessorImpl<BeanT, ValueT>
/*     */     extends CompositeTransducedAccessorImpl<BeanT, ValueT>
/*     */   {
/*     */     public CompositeContextDependentTransducedAccessorImpl(JAXBContextImpl context, Transducer<ValueT> xducer, Accessor<BeanT, ValueT> acc) {
/* 195 */       super(context, xducer, acc);
/* 196 */       assert xducer.useNamespace();
/*     */     }
/*     */     
/*     */     public boolean useNamespace() {
/* 200 */       return true;
/*     */     }
/*     */     
/*     */     public void declareNamespace(BeanT bean, XMLSerializer w) throws AccessorException {
/* 204 */       ValueT o = this.acc.get(bean);
/* 205 */       if (o != null) {
/* 206 */         this.xducer.declareNamespace(o, w);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeLeafElement(XMLSerializer w, Name tagName, BeanT o, String fieldName) throws SAXException, AccessorException, IOException, XMLStreamException {
/* 211 */       w.startElement(tagName, null);
/* 212 */       declareNamespace(o, w);
/* 213 */       w.endNamespaceDecls(null);
/* 214 */       w.endAttributes();
/* 215 */       this.xducer.writeText(w, this.acc.get(o), fieldName);
/* 216 */       w.endElement();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class CompositeTransducedAccessorImpl<BeanT, ValueT>
/*     */     extends TransducedAccessor<BeanT>
/*     */   {
/*     */     protected final Transducer<ValueT> xducer;
/*     */     
/*     */     protected final Accessor<BeanT, ValueT> acc;
/*     */ 
/*     */     
/*     */     public CompositeTransducedAccessorImpl(JAXBContextImpl context, Transducer<ValueT> xducer, Accessor<BeanT, ValueT> acc) {
/* 230 */       this.xducer = xducer;
/* 231 */       this.acc = acc.optimize(context);
/*     */     }
/*     */     
/*     */     public CharSequence print(BeanT bean) throws AccessorException {
/* 235 */       ValueT o = this.acc.get(bean);
/* 236 */       if (o == null) return null; 
/* 237 */       return this.xducer.print(o);
/*     */     }
/*     */     
/*     */     public void parse(BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
/* 241 */       this.acc.set(bean, (ValueT)this.xducer.parse(lexical));
/*     */     }
/*     */     
/*     */     public boolean hasValue(BeanT bean) throws AccessorException {
/* 245 */       return (this.acc.getUnadapted(bean) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeLeafElement(XMLSerializer w, Name tagName, BeanT o, String fieldName) throws SAXException, AccessorException, IOException, XMLStreamException {
/* 250 */       this.xducer.writeLeafElement(w, tagName, this.acc.get(o), fieldName);
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeText(XMLSerializer w, BeanT o, String fieldName) throws AccessorException, SAXException, IOException, XMLStreamException {
/* 255 */       this.xducer.writeText(w, this.acc.get(o), fieldName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class IDREFTransducedAccessorImpl<BeanT, TargetT>
/*     */     extends DefaultTransducedAccessor<BeanT>
/*     */   {
/*     */     private final Accessor<BeanT, TargetT> acc;
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<TargetT> targetType;
/*     */ 
/*     */ 
/*     */     
/*     */     public IDREFTransducedAccessorImpl(Accessor<BeanT, TargetT> acc) {
/* 274 */       this.acc = acc;
/* 275 */       this.targetType = acc.getValueType();
/*     */     }
/*     */     
/*     */     public String print(BeanT bean) throws AccessorException, SAXException {
/* 279 */       TargetT target = this.acc.get(bean);
/* 280 */       if (target == null) return null;
/*     */       
/* 282 */       XMLSerializer w = XMLSerializer.getInstance();
/*     */       try {
/* 284 */         String id = w.grammar.getBeanInfo(target, true).getId(target, w);
/* 285 */         if (id == null)
/* 286 */           w.errorMissingId(target); 
/* 287 */         return id;
/* 288 */       } catch (JAXBException e) {
/* 289 */         w.reportError(null, (Throwable)e);
/* 290 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void assign(BeanT bean, TargetT t, UnmarshallingContext context) throws AccessorException {
/* 295 */       if (!this.targetType.isInstance(t)) {
/* 296 */         context.handleError(Messages.UNASSIGNABLE_TYPE.format(new Object[] { this.targetType, t.getClass() }));
/*     */       } else {
/* 298 */         this.acc.set(bean, t);
/*     */       } 
/*     */     } public void parse(final BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
/*     */       TargetT t;
/* 302 */       final String idref = WhiteSpaceProcessor.trim(lexical).toString();
/* 303 */       final UnmarshallingContext context = UnmarshallingContext.getInstance();
/*     */       
/* 305 */       final Callable<TargetT> callable = context.getObjectFromId(idref, this.acc.valueType);
/* 306 */       if (callable == null) {
/*     */         
/* 308 */         context.errorUnresolvedIDREF(bean, idref, context.getLocator());
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 314 */         t = callable.call();
/* 315 */       } catch (SAXException e) {
/* 316 */         throw e;
/* 317 */       } catch (RuntimeException e) {
/* 318 */         throw e;
/* 319 */       } catch (Exception e) {
/* 320 */         throw new SAXException2(e);
/*     */       } 
/* 322 */       if (t != null) {
/* 323 */         assign(bean, t, context);
/*     */       } else {
/*     */         
/* 326 */         final LocatorEx.Snapshot loc = new LocatorEx.Snapshot(context.getLocator());
/* 327 */         context.addPatcher(new Patcher() {
/*     */               public void run() throws SAXException {
/*     */                 try {
/* 330 */                   TargetT t = callable.call();
/* 331 */                   if (t == null) {
/* 332 */                     context.errorUnresolvedIDREF(bean, idref, loc);
/*     */                   } else {
/* 334 */                     TransducedAccessor.IDREFTransducedAccessorImpl.this.assign((BeanT)bean, t, context);
/*     */                   } 
/* 336 */                 } catch (AccessorException e) {
/* 337 */                   context.handleError((Exception)e);
/* 338 */                 } catch (SAXException e) {
/* 339 */                   throw e;
/* 340 */                 } catch (RuntimeException e) {
/* 341 */                   throw e;
/* 342 */                 } catch (Exception e) {
/* 343 */                   throw new SAXException2(e);
/*     */                 } 
/*     */               }
/*     */             });
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean hasValue(BeanT bean) throws AccessorException {
/* 351 */       return (this.acc.get(bean) != null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\TransducedAccessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */