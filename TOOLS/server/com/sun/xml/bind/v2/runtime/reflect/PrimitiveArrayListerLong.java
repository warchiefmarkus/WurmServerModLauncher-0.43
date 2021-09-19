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
/*     */ final class PrimitiveArrayListerLong<BeanT>
/*     */   extends Lister<BeanT, long[], Long, PrimitiveArrayListerLong.LongArrayPack>
/*     */ {
/*     */   static void register() {
/*  55 */     Lister.primitiveArrayListers.put(long.class, new PrimitiveArrayListerLong());
/*     */   }
/*     */   
/*     */   public ListIterator<Long> iterator(final long[] objects, XMLSerializer context) {
/*  59 */     return new ListIterator<Long>() {
/*  60 */         int idx = 0;
/*     */         public boolean hasNext() {
/*  62 */           return (this.idx < objects.length);
/*     */         }
/*     */         
/*     */         public Long next() {
/*  66 */           return Long.valueOf(objects[this.idx++]);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public LongArrayPack startPacking(BeanT current, Accessor<BeanT, long[]> acc) {
/*  72 */     return new LongArrayPack();
/*     */   }
/*     */   
/*     */   public void addToPack(LongArrayPack objects, Long o) {
/*  76 */     objects.add(o);
/*     */   }
/*     */   
/*     */   public void endPacking(LongArrayPack pack, BeanT bean, Accessor<BeanT, long[]> acc) throws AccessorException {
/*  80 */     acc.set(bean, pack.build());
/*     */   }
/*     */   
/*     */   public void reset(BeanT o, Accessor<BeanT, long[]> acc) throws AccessorException {
/*  84 */     acc.set(o, new long[0]);
/*     */   }
/*     */   
/*     */   static final class LongArrayPack {
/*  88 */     long[] buf = new long[16];
/*     */     int size;
/*     */     
/*     */     void add(Long b) {
/*  92 */       if (this.buf.length == this.size) {
/*     */         
/*  94 */         long[] nb = new long[this.buf.length * 2];
/*  95 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/*  96 */         this.buf = nb;
/*     */       } 
/*  98 */       if (b != null)
/*  99 */         this.buf[this.size++] = b.longValue(); 
/*     */     }
/*     */     
/*     */     long[] build() {
/* 103 */       if (this.buf.length == this.size)
/*     */       {
/* 105 */         return this.buf;
/*     */       }
/* 107 */       long[] r = new long[this.size];
/* 108 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 109 */       return r;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\PrimitiveArrayListerLong.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */