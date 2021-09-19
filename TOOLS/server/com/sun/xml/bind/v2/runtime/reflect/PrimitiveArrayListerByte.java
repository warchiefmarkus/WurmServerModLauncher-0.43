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
/*     */ final class PrimitiveArrayListerByte<BeanT>
/*     */   extends Lister<BeanT, byte[], Byte, PrimitiveArrayListerByte.ByteArrayPack>
/*     */ {
/*     */   static void register() {
/*  55 */     Lister.primitiveArrayListers.put(byte.class, new PrimitiveArrayListerByte());
/*     */   }
/*     */   
/*     */   public ListIterator<Byte> iterator(final byte[] objects, XMLSerializer context) {
/*  59 */     return new ListIterator<Byte>() {
/*  60 */         int idx = 0;
/*     */         public boolean hasNext() {
/*  62 */           return (this.idx < objects.length);
/*     */         }
/*     */         
/*     */         public Byte next() {
/*  66 */           return Byte.valueOf(objects[this.idx++]);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public ByteArrayPack startPacking(BeanT current, Accessor<BeanT, byte[]> acc) {
/*  72 */     return new ByteArrayPack();
/*     */   }
/*     */   
/*     */   public void addToPack(ByteArrayPack objects, Byte o) {
/*  76 */     objects.add(o);
/*     */   }
/*     */   
/*     */   public void endPacking(ByteArrayPack pack, BeanT bean, Accessor<BeanT, byte[]> acc) throws AccessorException {
/*  80 */     acc.set(bean, pack.build());
/*     */   }
/*     */   
/*     */   public void reset(BeanT o, Accessor<BeanT, byte[]> acc) throws AccessorException {
/*  84 */     acc.set(o, new byte[0]);
/*     */   }
/*     */   
/*     */   static final class ByteArrayPack {
/*  88 */     byte[] buf = new byte[16];
/*     */     int size;
/*     */     
/*     */     void add(Byte b) {
/*  92 */       if (this.buf.length == this.size) {
/*     */         
/*  94 */         byte[] nb = new byte[this.buf.length * 2];
/*  95 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/*  96 */         this.buf = nb;
/*     */       } 
/*  98 */       if (b != null)
/*  99 */         this.buf[this.size++] = b.byteValue(); 
/*     */     }
/*     */     
/*     */     byte[] build() {
/* 103 */       if (this.buf.length == this.size)
/*     */       {
/* 105 */         return this.buf;
/*     */       }
/* 107 */       byte[] r = new byte[this.size];
/* 108 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 109 */       return r;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\PrimitiveArrayListerByte.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */