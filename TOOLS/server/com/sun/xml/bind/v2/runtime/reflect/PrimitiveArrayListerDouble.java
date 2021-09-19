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
/*     */ final class PrimitiveArrayListerDouble<BeanT>
/*     */   extends Lister<BeanT, double[], Double, PrimitiveArrayListerDouble.DoubleArrayPack>
/*     */ {
/*     */   static void register() {
/*  55 */     Lister.primitiveArrayListers.put(double.class, new PrimitiveArrayListerDouble());
/*     */   }
/*     */   
/*     */   public ListIterator<Double> iterator(final double[] objects, XMLSerializer context) {
/*  59 */     return new ListIterator<Double>() {
/*  60 */         int idx = 0;
/*     */         public boolean hasNext() {
/*  62 */           return (this.idx < objects.length);
/*     */         }
/*     */         
/*     */         public Double next() {
/*  66 */           return Double.valueOf(objects[this.idx++]);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public DoubleArrayPack startPacking(BeanT current, Accessor<BeanT, double[]> acc) {
/*  72 */     return new DoubleArrayPack();
/*     */   }
/*     */   
/*     */   public void addToPack(DoubleArrayPack objects, Double o) {
/*  76 */     objects.add(o);
/*     */   }
/*     */   
/*     */   public void endPacking(DoubleArrayPack pack, BeanT bean, Accessor<BeanT, double[]> acc) throws AccessorException {
/*  80 */     acc.set(bean, pack.build());
/*     */   }
/*     */   
/*     */   public void reset(BeanT o, Accessor<BeanT, double[]> acc) throws AccessorException {
/*  84 */     acc.set(o, new double[0]);
/*     */   }
/*     */   
/*     */   static final class DoubleArrayPack {
/*  88 */     double[] buf = new double[16];
/*     */     int size;
/*     */     
/*     */     void add(Double b) {
/*  92 */       if (this.buf.length == this.size) {
/*     */         
/*  94 */         double[] nb = new double[this.buf.length * 2];
/*  95 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/*  96 */         this.buf = nb;
/*     */       } 
/*  98 */       if (b != null)
/*  99 */         this.buf[this.size++] = b.doubleValue(); 
/*     */     }
/*     */     
/*     */     double[] build() {
/* 103 */       if (this.buf.length == this.size)
/*     */       {
/* 105 */         return this.buf;
/*     */       }
/* 107 */       double[] r = new double[this.size];
/* 108 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 109 */       return r;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\PrimitiveArrayListerDouble.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */