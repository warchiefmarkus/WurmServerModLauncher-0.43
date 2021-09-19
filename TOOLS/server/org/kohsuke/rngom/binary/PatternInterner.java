/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ final class PatternInterner {
/*    */   private static final int INIT_SIZE = 256;
/*    */   private static final float LOAD_FACTOR = 0.3F;
/*    */   private Pattern[] table;
/*    */   private int used;
/*    */   private int usedLimit;
/*    */   
/*    */   PatternInterner() {
/* 11 */     this.table = null;
/* 12 */     this.used = 0;
/* 13 */     this.usedLimit = 0;
/*    */   }
/*    */   
/*    */   PatternInterner(PatternInterner parent) {
/* 17 */     this.table = parent.table;
/* 18 */     if (this.table != null)
/* 19 */       this.table = (Pattern[])this.table.clone(); 
/* 20 */     this.used = parent.used;
/* 21 */     this.usedLimit = parent.usedLimit;
/*    */   }
/*    */ 
/*    */   
/*    */   Pattern intern(Pattern p) {
/*    */     int i;
/* 27 */     if (this.table == null) {
/* 28 */       this.table = new Pattern[256];
/* 29 */       this.usedLimit = 76;
/* 30 */       i = firstIndex(p);
/*    */     } else {
/* 32 */       for (i = firstIndex(p); this.table[i] != null; i = nextIndex(i)) {
/* 33 */         if (p.samePattern(this.table[i]))
/* 34 */           return this.table[i]; 
/*    */       } 
/*    */     } 
/* 37 */     if (this.used >= this.usedLimit) {
/*    */       
/* 39 */       Pattern[] oldTable = this.table;
/* 40 */       this.table = new Pattern[this.table.length << 1];
/* 41 */       for (int j = oldTable.length; j > 0; ) {
/* 42 */         j--;
/* 43 */         if (oldTable[j] != null) {
/*    */           
/* 45 */           int k = firstIndex(oldTable[j]);
/* 46 */           while (this.table[k] != null)
/* 47 */             k = nextIndex(k); 
/* 48 */           this.table[k] = oldTable[j];
/*    */         } 
/*    */       } 
/* 51 */       for (i = firstIndex(p); this.table[i] != null; i = nextIndex(i));
/* 52 */       this.usedLimit = (int)(this.table.length * 0.3F);
/*    */     } 
/* 54 */     this.used++;
/* 55 */     this.table[i] = p;
/* 56 */     return p;
/*    */   }
/*    */   
/*    */   private int firstIndex(Pattern p) {
/* 60 */     return p.patternHashCode() & this.table.length - 1;
/*    */   }
/*    */   
/*    */   private int nextIndex(int i) {
/* 64 */     return (i == 0) ? (this.table.length - 1) : (i - 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\PatternInterner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */