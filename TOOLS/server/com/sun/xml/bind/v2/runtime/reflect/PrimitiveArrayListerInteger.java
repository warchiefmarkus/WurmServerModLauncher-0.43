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
/*     */ final class PrimitiveArrayListerInteger<BeanT>
/*     */   extends Lister<BeanT, int[], Integer, PrimitiveArrayListerInteger.IntegerArrayPack>
/*     */ {
/*     */   static void register() {
/*  55 */     Lister.primitiveArrayListers.put(int.class, new PrimitiveArrayListerInteger());
/*     */   }
/*     */   
/*     */   public ListIterator<Integer> iterator(final int[] objects, XMLSerializer context) {
/*  59 */     return new ListIterator<Integer>() {
/*  60 */         int idx = 0;
/*     */         public boolean hasNext() {
/*  62 */           return (this.idx < objects.length);
/*     */         }
/*     */         
/*     */         public Integer next() {
/*  66 */           return Integer.valueOf(objects[this.idx++]);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public IntegerArrayPack startPacking(BeanT current, Accessor<BeanT, int[]> acc) {
/*  72 */     return new IntegerArrayPack();
/*     */   }
/*     */   
/*     */   public void addToPack(IntegerArrayPack objects, Integer o) {
/*  76 */     objects.add(o);
/*     */   }
/*     */   
/*     */   public void endPacking(IntegerArrayPack pack, BeanT bean, Accessor<BeanT, int[]> acc) throws AccessorException {
/*  80 */     acc.set(bean, pack.build());
/*     */   }
/*     */   
/*     */   public void reset(BeanT o, Accessor<BeanT, int[]> acc) throws AccessorException {
/*  84 */     acc.set(o, new int[0]);
/*     */   }
/*     */   
/*     */   static final class IntegerArrayPack {
/*  88 */     int[] buf = new int[16];
/*     */     int size;
/*     */     
/*     */     void add(Integer b) {
/*  92 */       if (this.buf.length == this.size) {
/*     */         
/*  94 */         int[] nb = new int[this.buf.length * 2];
/*  95 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/*  96 */         this.buf = nb;
/*     */       } 
/*  98 */       if (b != null)
/*  99 */         this.buf[this.size++] = b.intValue(); 
/*     */     }
/*     */     
/*     */     int[] build() {
/* 103 */       if (this.buf.length == this.size)
/*     */       {
/* 105 */         return this.buf;
/*     */       }
/* 107 */       int[] r = new int[this.size];
/* 108 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 109 */       return r;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\PrimitiveArrayListerInteger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */