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
/*     */ final class PrimitiveArrayListerCharacter<BeanT>
/*     */   extends Lister<BeanT, char[], Character, PrimitiveArrayListerCharacter.CharacterArrayPack>
/*     */ {
/*     */   static void register() {
/*  55 */     Lister.primitiveArrayListers.put(char.class, new PrimitiveArrayListerCharacter());
/*     */   }
/*     */   
/*     */   public ListIterator<Character> iterator(final char[] objects, XMLSerializer context) {
/*  59 */     return new ListIterator<Character>() {
/*  60 */         int idx = 0;
/*     */         public boolean hasNext() {
/*  62 */           return (this.idx < objects.length);
/*     */         }
/*     */         
/*     */         public Character next() {
/*  66 */           return Character.valueOf(objects[this.idx++]);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public CharacterArrayPack startPacking(BeanT current, Accessor<BeanT, char[]> acc) {
/*  72 */     return new CharacterArrayPack();
/*     */   }
/*     */   
/*     */   public void addToPack(CharacterArrayPack objects, Character o) {
/*  76 */     objects.add(o);
/*     */   }
/*     */   
/*     */   public void endPacking(CharacterArrayPack pack, BeanT bean, Accessor<BeanT, char[]> acc) throws AccessorException {
/*  80 */     acc.set(bean, pack.build());
/*     */   }
/*     */   
/*     */   public void reset(BeanT o, Accessor<BeanT, char[]> acc) throws AccessorException {
/*  84 */     acc.set(o, new char[0]);
/*     */   }
/*     */   
/*     */   static final class CharacterArrayPack {
/*  88 */     char[] buf = new char[16];
/*     */     int size;
/*     */     
/*     */     void add(Character b) {
/*  92 */       if (this.buf.length == this.size) {
/*     */         
/*  94 */         char[] nb = new char[this.buf.length * 2];
/*  95 */         System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
/*  96 */         this.buf = nb;
/*     */       } 
/*  98 */       if (b != null)
/*  99 */         this.buf[this.size++] = b.charValue(); 
/*     */     }
/*     */     
/*     */     char[] build() {
/* 103 */       if (this.buf.length == this.size)
/*     */       {
/* 105 */         return this.buf;
/*     */       }
/* 107 */       char[] r = new char[this.size];
/* 108 */       System.arraycopy(this.buf, 0, r, 0, this.size);
/* 109 */       return r;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\PrimitiveArrayListerCharacter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */