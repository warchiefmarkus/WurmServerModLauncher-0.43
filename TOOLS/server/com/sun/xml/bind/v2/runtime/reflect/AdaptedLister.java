/*     */ package com.sun.xml.bind.v2.runtime.reflect;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.runtime.Coordinator;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
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
/*     */ final class AdaptedLister<BeanT, PropT, InMemItemT, OnWireItemT, PackT>
/*     */   extends Lister<BeanT, PropT, OnWireItemT, PackT>
/*     */ {
/*     */   private final Lister<BeanT, PropT, InMemItemT, PackT> core;
/*     */   private final Class<? extends XmlAdapter<OnWireItemT, InMemItemT>> adapter;
/*     */   
/*     */   AdaptedLister(Lister<BeanT, PropT, InMemItemT, PackT> core, Class<? extends XmlAdapter<OnWireItemT, InMemItemT>> adapter) {
/*  59 */     this.core = core;
/*  60 */     this.adapter = adapter;
/*     */   }
/*     */   
/*     */   private XmlAdapter<OnWireItemT, InMemItemT> getAdapter() {
/*  64 */     return Coordinator._getInstance().getAdapter(this.adapter);
/*     */   }
/*     */   
/*     */   public ListIterator<OnWireItemT> iterator(PropT prop, XMLSerializer context) {
/*  68 */     return new ListIteratorImpl(this.core.iterator(prop, context), context);
/*     */   }
/*     */   
/*     */   public PackT startPacking(BeanT bean, Accessor<BeanT, PropT> accessor) throws AccessorException {
/*  72 */     return this.core.startPacking(bean, accessor);
/*     */   }
/*     */   
/*     */   public void addToPack(PackT pack, OnWireItemT item) throws AccessorException {
/*     */     InMemItemT r;
/*     */     try {
/*  78 */       r = (InMemItemT)getAdapter().unmarshal(item);
/*  79 */     } catch (Exception e) {
/*  80 */       throw new AccessorException(e);
/*     */     } 
/*  82 */     this.core.addToPack(pack, r);
/*     */   }
/*     */   
/*     */   public void endPacking(PackT pack, BeanT bean, Accessor<BeanT, PropT> accessor) throws AccessorException {
/*  86 */     this.core.endPacking(pack, bean, accessor);
/*     */   }
/*     */   
/*     */   public void reset(BeanT bean, Accessor<BeanT, PropT> accessor) throws AccessorException {
/*  90 */     this.core.reset(bean, accessor);
/*     */   }
/*     */   
/*     */   private final class ListIteratorImpl implements ListIterator<OnWireItemT> {
/*     */     private final ListIterator<InMemItemT> core;
/*     */     private final XMLSerializer serializer;
/*     */     
/*     */     public ListIteratorImpl(ListIterator<InMemItemT> core, XMLSerializer serializer) {
/*  98 */       this.core = core;
/*  99 */       this.serializer = serializer;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 103 */       return this.core.hasNext();
/*     */     }
/*     */     
/*     */     public OnWireItemT next() throws SAXException, JAXBException {
/* 107 */       InMemItemT next = this.core.next();
/*     */       try {
/* 109 */         return (OnWireItemT)AdaptedLister.this.getAdapter().marshal(next);
/* 110 */       } catch (Exception e) {
/* 111 */         this.serializer.reportError(null, e);
/* 112 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\AdaptedLister.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */