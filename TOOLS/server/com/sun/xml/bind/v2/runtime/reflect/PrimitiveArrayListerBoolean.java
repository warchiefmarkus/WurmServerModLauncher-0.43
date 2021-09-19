/*     */ package com.sun.xml.bind.v2.runtime.reflect;
/*     */ 
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import javax.xml.bind.JAXBException;
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
/*     */ final class PrimitiveArrayListerBoolean<BeanT>
/*     */   extends Lister<BeanT, boolean[], Boolean, PrimitiveArrayListerBoolean.BooleanArrayPack>
/*     */ {
/*     */   static void register() {
/*  55 */     Lister.primitiveArrayListers.put(boolean.class, new PrimitiveArrayListerBoolean());
/*     */   }
/*     */   
/*     */   public ListIterator<Boolean> iterator(final boolean[] objects, XMLSerializer context) {
/*  59 */     return new ListIterator<Boolean>() {
/*  60 */         int idx = 0;
/*     */         public boolean hasNext() {
/*  62 */           return (this.idx < objects.length);
/*     */         }
/*     */         
/*     */         public Boolean next() {
/*  66 */           return Boolean.valueOf(objects[this.idx++]);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public BooleanArrayPack startPacking(BeanT current, Accessor<BeanT, boolean[]> acc) {
/*  72 */     return new BooleanArrayPack();
/*     */   }
/*     */   
/*     */   public void addToPack(BooleanArrayPack objects, Boolean o) {
/*  76 */     objects.add(o);
/*     */   }
/*     */   
/*     */   public void endPacking(BooleanArrayPack pack, BeanT bean, Accessor<BeanT, boolean[]> acc) throws AccessorException {
/*  80 */     acc.set(bean, pack.build());
/*     */   }
/*     */   
/*     */   public void reset(BeanT o, Accessor<BeanT, boolean[]> acc) throws AccessorException {
/*  84 */     acc.set(o, new boolean[0]);
/*     */   }
/*     */   
/*     */   static final class BooleanArrayPack {
/*  88 */     boolean[] buf = new boolean[16];
/*     */     int size;
/*     */     
/*     */     void add(Boolean b) {
/*  92 */       if (this.buf.length == this.size) {
/*     */         
/*  94 */         boolean[] nb = new boolean[this.buf.length * 2];
/*  95 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/*  96 */         this.buf = nb;
/*     */       } 
/*  98 */       if (b != null)
/*  99 */         this.buf[this.size++] = b.booleanValue(); 
/*     */     }
/*     */     
/*     */     boolean[] build() {
/* 103 */       if (this.buf.length == this.size)
/*     */       {
/* 105 */         return this.buf;
/*     */       }
/* 107 */       boolean[] r = new boolean[this.size];
/* 108 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 109 */       return r;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\PrimitiveArrayListerBoolean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */