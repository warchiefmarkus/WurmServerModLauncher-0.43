/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ 
/*    */ class DuplicateAttributeDetector
/*    */ {
/*  9 */   private List nameClasses = new ArrayList();
/* 10 */   private Alternative alternatives = null;
/*    */   
/*    */   private static class Alternative {
/*    */     private int startIndex;
/*    */     private int endIndex;
/*    */     private Alternative parent;
/*    */     
/*    */     private Alternative(int startIndex, Alternative parent) {
/* 18 */       this.startIndex = startIndex;
/* 19 */       this.endIndex = startIndex;
/* 20 */       this.parent = parent;
/*    */     }
/*    */   }
/*    */   
/*    */   boolean addAttribute(NameClass nc) {
/* 25 */     int lim = this.nameClasses.size();
/* 26 */     for (Alternative a = this.alternatives; a != null; a = a.parent) {
/* 27 */       for (int j = a.endIndex; j < lim; j++) {
/* 28 */         if (nc.hasOverlapWith(this.nameClasses.get(j)))
/* 29 */           return false; 
/* 30 */       }  lim = a.startIndex;
/*    */     } 
/* 32 */     for (int i = 0; i < lim; i++) {
/* 33 */       if (nc.hasOverlapWith(this.nameClasses.get(i)))
/* 34 */         return false; 
/* 35 */     }  this.nameClasses.add(nc);
/* 36 */     return true;
/*    */   }
/*    */   
/*    */   void startChoice() {
/* 40 */     this.alternatives = new Alternative(this.nameClasses.size(), this.alternatives);
/*    */   }
/*    */   
/*    */   void alternative() {
/* 44 */     this.alternatives.endIndex = this.nameClasses.size();
/*    */   }
/*    */   
/*    */   void endChoice() {
/* 48 */     this.alternatives = this.alternatives.parent;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\DuplicateAttributeDetector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */