/*    */ package com.sun.xml.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.bind.api.AccessorException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NullSafeAccessor<B, V, P>
/*    */   extends Accessor<B, V>
/*    */ {
/*    */   private final Accessor<B, V> core;
/*    */   private final Lister<B, V, ?, P> lister;
/*    */   
/*    */   public NullSafeAccessor(Accessor<B, V> core, Lister<B, V, ?, P> lister) {
/* 55 */     super(core.getValueType());
/* 56 */     this.core = core;
/* 57 */     this.lister = lister;
/*    */   }
/*    */   
/*    */   public V get(B bean) throws AccessorException {
/* 61 */     V v = this.core.get(bean);
/* 62 */     if (v == null) {
/*    */       
/* 64 */       P pack = this.lister.startPacking(bean, this.core);
/* 65 */       this.lister.endPacking(pack, bean, this.core);
/* 66 */       v = this.core.get(bean);
/*    */     } 
/* 68 */     return v;
/*    */   }
/*    */   
/*    */   public void set(B bean, V value) throws AccessorException {
/* 72 */     this.core.set(bean, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\NullSafeAccessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */