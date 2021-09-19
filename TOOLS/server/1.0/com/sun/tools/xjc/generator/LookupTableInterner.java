/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.msv.grammar.ChoiceExp;
/*    */ import com.sun.tools.xjc.generator.LookupTable;
/*    */ import com.sun.tools.xjc.generator.LookupTableBuilder;
/*    */ import com.sun.tools.xjc.generator.LookupTableUse;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ class LookupTableInterner
/*    */   implements LookupTableBuilder
/*    */ {
/* 29 */   private final List liveTable = new ArrayList();
/*    */ 
/*    */ 
/*    */   
/*    */   private final LookupTableBuilder core;
/*    */ 
/*    */ 
/*    */   
/*    */   public LookupTableInterner(LookupTableBuilder _core) {
/* 38 */     this.core = _core;
/*    */   }
/*    */   
/*    */   public LookupTableUse buildTable(ChoiceExp exp) {
/* 42 */     LookupTableUse t = this.core.buildTable(exp);
/* 43 */     if (t == null) return null;
/*    */     
/* 45 */     return new LookupTableUse(intern(t.table), t.anomaly, t.switchAttName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private LookupTable intern(LookupTable t) {
/* 51 */     for (int i = 0; i < this.liveTable.size(); i++) {
/* 52 */       LookupTable a = this.liveTable.get(i);
/* 53 */       if (a.isConsistentWith(t)) {
/* 54 */         a.absorb(t);
/* 55 */         return a;
/*    */       } 
/*    */     } 
/*    */     
/* 59 */     this.liveTable.add(t);
/* 60 */     return t;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LookupTable[] listTables() {
/* 67 */     return (LookupTable[])this.liveTable.toArray((Object[])new LookupTable[this.liveTable.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\LookupTableInterner.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */