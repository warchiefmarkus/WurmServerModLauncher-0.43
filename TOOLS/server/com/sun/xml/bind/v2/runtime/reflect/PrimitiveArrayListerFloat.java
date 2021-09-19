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
/*     */ final class PrimitiveArrayListerFloat<BeanT>
/*     */   extends Lister<BeanT, float[], Float, PrimitiveArrayListerFloat.FloatArrayPack>
/*     */ {
/*     */   static void register() {
/*  55 */     Lister.primitiveArrayListers.put(float.class, new PrimitiveArrayListerFloat());
/*     */   }
/*     */   
/*     */   public ListIterator<Float> iterator(final float[] objects, XMLSerializer context) {
/*  59 */     return new ListIterator<Float>() {
/*  60 */         int idx = 0;
/*     */         public boolean hasNext() {
/*  62 */           return (this.idx < objects.length);
/*     */         }
/*     */         
/*     */         public Float next() {
/*  66 */           return Float.valueOf(objects[this.idx++]);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public FloatArrayPack startPacking(BeanT current, Accessor<BeanT, float[]> acc) {
/*  72 */     return new FloatArrayPack();
/*     */   }
/*     */   
/*     */   public void addToPack(FloatArrayPack objects, Float o) {
/*  76 */     objects.add(o);
/*     */   }
/*     */   
/*     */   public void endPacking(FloatArrayPack pack, BeanT bean, Accessor<BeanT, float[]> acc) throws AccessorException {
/*  80 */     acc.set(bean, pack.build());
/*     */   }
/*     */   
/*     */   public void reset(BeanT o, Accessor<BeanT, float[]> acc) throws AccessorException {
/*  84 */     acc.set(o, new float[0]);
/*     */   }
/*     */   
/*     */   static final class FloatArrayPack {
/*  88 */     float[] buf = new float[16];
/*     */     int size;
/*     */     
/*     */     void add(Float b) {
/*  92 */       if (this.buf.length == this.size) {
/*     */         
/*  94 */         float[] nb = new float[this.buf.length * 2];
/*  95 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/*  96 */         this.buf = nb;
/*     */       } 
/*  98 */       if (b != null)
/*  99 */         this.buf[this.size++] = b.floatValue(); 
/*     */     }
/*     */     
/*     */     float[] build() {
/* 103 */       if (this.buf.length == this.size)
/*     */       {
/* 105 */         return this.buf;
/*     */       }
/* 107 */       float[] r = new float[this.size];
/* 108 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 109 */       return r;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\PrimitiveArrayListerFloat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */