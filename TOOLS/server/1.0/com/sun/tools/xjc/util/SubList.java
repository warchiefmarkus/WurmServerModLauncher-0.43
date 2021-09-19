/*     */ package 1.0.com.sun.tools.xjc.util;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SubList
/*     */   extends AbstractList
/*     */ {
/*     */   private final List l;
/*     */   private final int offset;
/*     */   private int size;
/*     */   
/*     */   public SubList(List list, int fromIndex, int toIndex) {
/*  36 */     if (fromIndex < 0)
/*  37 */       throw new IndexOutOfBoundsException("fromIndex = " + fromIndex); 
/*  38 */     if (toIndex > list.size())
/*  39 */       throw new IndexOutOfBoundsException("toIndex = " + toIndex); 
/*  40 */     if (fromIndex > toIndex) {
/*  41 */       throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
/*     */     }
/*  43 */     this.l = list;
/*  44 */     this.offset = fromIndex;
/*  45 */     this.size = toIndex - fromIndex;
/*     */   }
/*     */   
/*     */   public Object set(int index, Object element) {
/*  49 */     rangeCheck(index);
/*  50 */     return this.l.set(index + this.offset, element);
/*     */   }
/*     */   
/*     */   public Object get(int index) {
/*  54 */     rangeCheck(index);
/*  55 */     return this.l.get(index + this.offset);
/*     */   }
/*     */   
/*     */   public int size() {
/*  59 */     return this.size;
/*     */   }
/*     */   
/*     */   public void add(int index, Object element) {
/*  63 */     if (index < 0 || index > this.size)
/*  64 */       throw new IndexOutOfBoundsException(); 
/*  65 */     this.l.add(index + this.offset, element);
/*  66 */     this.size++;
/*  67 */     this.modCount++;
/*     */   }
/*     */   
/*     */   public Object remove(int index) {
/*  71 */     rangeCheck(index);
/*  72 */     Object result = this.l.remove(index + this.offset);
/*  73 */     this.size--;
/*  74 */     this.modCount++;
/*  75 */     return result;
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection c) {
/*  79 */     return addAll(this.size, c);
/*     */   }
/*     */   
/*     */   public boolean addAll(int index, Collection c) {
/*  83 */     if (index < 0 || index > this.size) {
/*  84 */       throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
/*     */     }
/*  86 */     int cSize = c.size();
/*  87 */     if (cSize == 0) {
/*  88 */       return false;
/*     */     }
/*  90 */     this.l.addAll(this.offset + index, c);
/*  91 */     this.size += cSize;
/*  92 */     this.modCount++;
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   public Iterator iterator() {
/*  97 */     return listIterator();
/*     */   }
/*     */   
/*     */   public ListIterator listIterator(int index) {
/* 101 */     if (index < 0 || index > this.size) {
/* 102 */       throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
/*     */     }
/*     */     
/* 105 */     return (ListIterator)new Object(this, index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List subList(int fromIndex, int toIndex) {
/* 157 */     return new com.sun.tools.xjc.util.SubList(this, fromIndex, toIndex);
/*     */   }
/*     */   
/*     */   private void rangeCheck(int index) {
/* 161 */     if (index < 0 || index >= this.size)
/* 162 */       throw new IndexOutOfBoundsException("Index: " + index + ",Size: " + this.size); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xj\\util\SubList.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */