/*     */ package com.sun.tools.xjc.reader.gbind;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ConnectedComponent
/*     */   implements Iterable<Element>
/*     */ {
/*  53 */   private final List<Element> elements = new ArrayList<Element>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isRequired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isCollection() {
/*  65 */     assert !this.elements.isEmpty();
/*     */ 
/*     */ 
/*     */     
/*  69 */     if (this.elements.size() > 1) {
/*  70 */       return true;
/*     */     }
/*     */     
/*  73 */     Element n = this.elements.get(0);
/*  74 */     return n.hasSelfLoop();
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
/*     */   public final boolean isRequired() {
/*  86 */     return this.isRequired;
/*     */   }
/*     */   
/*     */   void add(Element e) {
/*  90 */     assert !this.elements.contains(e);
/*  91 */     this.elements.add(e);
/*     */   }
/*     */   
/*     */   public Iterator<Element> iterator() {
/*  95 */     return this.elements.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     String s = this.elements.toString();
/* 103 */     if (isRequired())
/* 104 */       s = s + '!'; 
/* 105 */     if (isCollection())
/* 106 */       s = s + '*'; 
/* 107 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\gbind\ConnectedComponent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */