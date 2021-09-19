/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Lister;
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
/*     */ public final class Scope<BeanT, PropT, ItemT, PackT>
/*     */ {
/*     */   public final UnmarshallingContext context;
/*     */   private BeanT bean;
/*     */   private Accessor<BeanT, PropT> acc;
/*     */   private PackT pack;
/*     */   private Lister<BeanT, PropT, ItemT, PackT> lister;
/*     */   
/*     */   Scope(UnmarshallingContext context) {
/*  63 */     this.context = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasStarted() {
/*  70 */     return (this.bean != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  77 */     if (this.bean == null) {
/*     */       
/*  79 */       assert clean();
/*     */       
/*     */       return;
/*     */     } 
/*  83 */     this.bean = null;
/*  84 */     this.acc = null;
/*  85 */     this.pack = null;
/*  86 */     this.lister = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finish() throws AccessorException {
/*  94 */     if (hasStarted()) {
/*  95 */       this.lister.endPacking(this.pack, this.bean, this.acc);
/*  96 */       reset();
/*     */     } 
/*  98 */     assert clean();
/*     */   }
/*     */   
/*     */   private boolean clean() {
/* 102 */     return (this.bean == null && this.acc == null && this.pack == null && this.lister == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Accessor<BeanT, PropT> acc, Lister<BeanT, PropT, ItemT, PackT> lister, ItemT value) throws SAXException {
/*     */     try {
/* 110 */       if (!hasStarted()) {
/* 111 */         this.bean = (BeanT)(this.context.getCurrentState()).target;
/* 112 */         this.acc = acc;
/* 113 */         this.lister = lister;
/* 114 */         this.pack = (PackT)lister.startPacking(this.bean, acc);
/*     */       } 
/*     */       
/* 117 */       lister.addToPack(this.pack, value);
/* 118 */     } catch (AccessorException e) {
/* 119 */       Loader.handleGenericException((Exception)e, true);
/*     */       
/* 121 */       this.lister = Lister.getErrorInstance();
/* 122 */       this.acc = Accessor.getErrorInstance();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(Accessor<BeanT, PropT> acc, Lister<BeanT, PropT, ItemT, PackT> lister) throws SAXException {
/*     */     try {
/* 134 */       if (!hasStarted()) {
/* 135 */         this.bean = (BeanT)(this.context.getCurrentState()).target;
/* 136 */         this.acc = acc;
/* 137 */         this.lister = lister;
/* 138 */         this.pack = (PackT)lister.startPacking(this.bean, acc);
/*     */       } 
/* 140 */     } catch (AccessorException e) {
/* 141 */       Loader.handleGenericException((Exception)e, true);
/*     */       
/* 143 */       this.lister = Lister.getErrorInstance();
/* 144 */       this.acc = Accessor.getErrorInstance();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\Scope.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */