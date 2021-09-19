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
/*     */ final class PrimitiveArrayListerShort<BeanT>
/*     */   extends Lister<BeanT, short[], Short, PrimitiveArrayListerShort.ShortArrayPack>
/*     */ {
/*     */   static void register() {
/*  55 */     Lister.primitiveArrayListers.put(short.class, new PrimitiveArrayListerShort());
/*     */   }
/*     */   
/*     */   public ListIterator<Short> iterator(final short[] objects, XMLSerializer context) {
/*  59 */     return new ListIterator<Short>() {
/*  60 */         int idx = 0;
/*     */         public boolean hasNext() {
/*  62 */           return (this.idx < objects.length);
/*     */         }
/*     */         
/*     */         public Short next() {
/*  66 */           return Short.valueOf(objects[this.idx++]);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public ShortArrayPack startPacking(BeanT current, Accessor<BeanT, short[]> acc) {
/*  72 */     return new ShortArrayPack();
/*     */   }
/*     */   
/*     */   public void addToPack(ShortArrayPack objects, Short o) {
/*  76 */     objects.add(o);
/*     */   }
/*     */   
/*     */   public void endPacking(ShortArrayPack pack, BeanT bean, Accessor<BeanT, short[]> acc) throws AccessorException {
/*  80 */     acc.set(bean, pack.build());
/*     */   }
/*     */   
/*     */   public void reset(BeanT o, Accessor<BeanT, short[]> acc) throws AccessorException {
/*  84 */     acc.set(o, new short[0]);
/*     */   }
/*     */   
/*     */   static final class ShortArrayPack {
/*  88 */     short[] buf = new short[16];
/*     */     int size;
/*     */     
/*     */     void add(Short b) {
/*  92 */       if (this.buf.length == this.size) {
/*     */         
/*  94 */         short[] nb = new short[this.buf.length * 2];
/*  95 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/*  96 */         this.buf = nb;
/*     */       } 
/*  98 */       if (b != null)
/*  99 */         this.buf[this.size++] = b.shortValue(); 
/*     */     }
/*     */     
/*     */     short[] build() {
/* 103 */       if (this.buf.length == this.size)
/*     */       {
/* 105 */         return this.buf;
/*     */       }
/* 107 */       short[] r = new short[this.size];
/* 108 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 109 */       return r;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\PrimitiveArrayListerShort.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */