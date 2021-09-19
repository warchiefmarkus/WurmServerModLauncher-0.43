/*    */ package impl.org.controlsfx.table;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Optional;
/*    */ 
/*    */ final class DupeCounter<T>
/*    */ {
/*  8 */   private final HashMap<T, Integer> counts = new HashMap<>();
/*    */   private final boolean enforceFloor;
/*    */   
/*    */   public DupeCounter(boolean enforceFloor) {
/* 12 */     this.enforceFloor = enforceFloor;
/*    */   } public int add(T value) {
/*    */     int newVal;
/* 15 */     Integer prev = this.counts.get(value);
/*    */     
/* 17 */     if (prev == null) {
/* 18 */       newVal = 1;
/* 19 */       this.counts.put(value, Integer.valueOf(newVal));
/*    */     } else {
/* 21 */       newVal = prev.intValue() + 1;
/* 22 */       this.counts.put(value, Integer.valueOf(newVal));
/*    */     } 
/* 24 */     return newVal;
/*    */   }
/*    */   public int get(T value) {
/* 27 */     return ((Integer)Optional.<Integer>ofNullable(this.counts.get(value)).orElse(Integer.valueOf(0))).intValue();
/*    */   }
/*    */   public int remove(T value) {
/* 30 */     Integer prev = this.counts.get(value);
/* 31 */     if (prev != null && prev.intValue() > 0) {
/* 32 */       int newVal = prev.intValue() - 1;
/* 33 */       if (newVal == 0) {
/* 34 */         this.counts.remove(value);
/*    */       } else {
/* 36 */         this.counts.put(value, Integer.valueOf(newVal));
/*    */       } 
/* 38 */       return newVal;
/*    */     } 
/* 40 */     if (this.enforceFloor) {
/* 41 */       throw new IllegalStateException();
/*    */     }
/*    */     
/* 44 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return this.counts.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\table\DupeCounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */