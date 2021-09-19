/*    */ package com.sun.tools.xjc.reader.gbind;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
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
/*    */ interface ElementSet
/*    */   extends Iterable<Element>
/*    */ {
/* 53 */   public static final ElementSet EMPTY_SET = new ElementSet()
/*    */     {
/*    */       public void addNext(Element element) {}
/*    */ 
/*    */       
/*    */       public boolean contains(ElementSet element) {
/* 59 */         return (this == element);
/*    */       }
/*    */       
/*    */       public Iterator<Element> iterator() {
/* 63 */         return Collections.<Element>emptySet().iterator();
/*    */       }
/*    */     };
/*    */   
/*    */   void addNext(Element paramElement);
/*    */   
/*    */   boolean contains(ElementSet paramElementSet);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\gbind\ElementSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */