/*     */ package com.sun.xml.bind.v2.runtime.reflect;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.ClassFactory;
/*     */ import com.sun.xml.bind.v2.runtime.Coordinator;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class AdaptedAccessor<BeanT, InMemValueT, OnWireValueT>
/*     */   extends Accessor<BeanT, OnWireValueT>
/*     */ {
/*     */   private final Accessor<BeanT, InMemValueT> core;
/*     */   private final Class<? extends XmlAdapter<OnWireValueT, InMemValueT>> adapter;
/*     */   private XmlAdapter<OnWireValueT, InMemValueT> staticAdapter;
/*     */   
/*     */   AdaptedAccessor(Class<OnWireValueT> targetType, Accessor<BeanT, InMemValueT> extThis, Class<? extends XmlAdapter<OnWireValueT, InMemValueT>> adapter) {
/*  57 */     super(targetType);
/*  58 */     this.core = extThis;
/*  59 */     this.adapter = adapter;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAdapted() {
/*  64 */     return true;
/*     */   }
/*     */   
/*     */   public OnWireValueT get(BeanT bean) throws AccessorException {
/*  68 */     InMemValueT v = this.core.get(bean);
/*     */     
/*  70 */     XmlAdapter<OnWireValueT, InMemValueT> a = getAdapter();
/*     */     try {
/*  72 */       return (OnWireValueT)a.marshal(v);
/*  73 */     } catch (Exception e) {
/*  74 */       throw new AccessorException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void set(BeanT bean, OnWireValueT o) throws AccessorException {
/*  79 */     XmlAdapter<OnWireValueT, InMemValueT> a = getAdapter();
/*     */     try {
/*  81 */       this.core.set(bean, (InMemValueT)a.unmarshal(o));
/*  82 */     } catch (Exception e) {
/*  83 */       throw new AccessorException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object getUnadapted(BeanT bean) throws AccessorException {
/*  88 */     return this.core.getUnadapted(bean);
/*     */   }
/*     */   
/*     */   public void setUnadapted(BeanT bean, Object value) throws AccessorException {
/*  92 */     this.core.setUnadapted(bean, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XmlAdapter<OnWireValueT, InMemValueT> getAdapter() {
/* 103 */     Coordinator coordinator = Coordinator._getInstance();
/* 104 */     if (coordinator != null) {
/* 105 */       return coordinator.getAdapter(this.adapter);
/*     */     }
/* 107 */     synchronized (this) {
/* 108 */       if (this.staticAdapter == null)
/* 109 */         this.staticAdapter = (XmlAdapter<OnWireValueT, InMemValueT>)ClassFactory.create(this.adapter); 
/*     */     } 
/* 111 */     return this.staticAdapter;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\AdaptedAccessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */