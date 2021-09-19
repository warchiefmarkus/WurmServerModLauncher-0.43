/*    */ package com.sun.tools.xjc.reader.gbind;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ElementSets
/*    */ {
/*    */   public static ElementSet union(ElementSet lhs, ElementSet rhs) {
/* 53 */     if (lhs.contains(rhs))
/* 54 */       return lhs; 
/* 55 */     if (lhs == ElementSet.EMPTY_SET)
/* 56 */       return rhs; 
/* 57 */     if (rhs == ElementSet.EMPTY_SET)
/* 58 */       return lhs; 
/* 59 */     return new MultiValueSet(lhs, rhs);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static final class MultiValueSet
/*    */     extends LinkedHashSet<Element>
/*    */     implements ElementSet
/*    */   {
/*    */     public MultiValueSet(ElementSet lhs, ElementSet rhs) {
/* 69 */       addAll(lhs);
/* 70 */       addAll(rhs);
/*    */ 
/*    */       
/* 73 */       assert size() > 1;
/*    */     }
/*    */     
/*    */     private void addAll(ElementSet lhs) {
/* 77 */       if (lhs instanceof MultiValueSet) {
/* 78 */         addAll((MultiValueSet)lhs);
/*    */       } else {
/* 80 */         for (Element e : lhs) {
/* 81 */           add(e);
/*    */         }
/*    */       } 
/*    */     }
/*    */     
/*    */     public boolean contains(ElementSet rhs) {
/* 87 */       return (contains(rhs) || rhs == ElementSet.EMPTY_SET);
/*    */     }
/*    */     
/*    */     public void addNext(Element element) {
/* 91 */       for (Element e : this)
/* 92 */         e.addNext(element); 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\gbind\ElementSets.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */