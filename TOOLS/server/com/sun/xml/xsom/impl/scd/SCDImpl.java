/*    */ package com.sun.xml.xsom.impl.scd;
/*    */ 
/*    */ import com.sun.xml.xsom.SCD;
/*    */ import com.sun.xml.xsom.XSComponent;
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
/*    */ public final class SCDImpl
/*    */   extends SCD
/*    */ {
/*    */   private final Step[] steps;
/*    */   private final String text;
/*    */   
/*    */   public SCDImpl(String text, Step[] steps) {
/* 25 */     this.text = text;
/* 26 */     this.steps = steps;
/*    */   }
/*    */   
/*    */   public Iterator<XSComponent> select(Iterator<? extends XSComponent> contextNode) {
/* 30 */     Iterator<? extends XSComponent> iterator = contextNode;
/*    */     
/* 32 */     int len = this.steps.length;
/* 33 */     for (int i = 0; i < len; i++) {
/* 34 */       if (i != 0 && i != len - 1 && !(this.steps[i - 1]).axis.isModelGroup() && (this.steps[i]).axis.isModelGroup())
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 40 */         iterator = new Iterators.Unique<XSComponent>(new Iterators.Map<XSComponent, XSComponent>(iterator)
/*    */             {
/*    */               protected Iterator<XSComponent> apply(XSComponent u) {
/* 43 */                 return new Iterators.Union<XSComponent>(Iterators.singleton(u), Axis.INTERMEDIATE_SKIP.iterator(u));
/*    */               }
/*    */             });
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 50 */       iterator = this.steps[i].evaluate((Iterator)iterator);
/*    */     } 
/*    */     
/* 53 */     return (Iterator)iterator;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 57 */     return this.text;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\scd\SCDImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */