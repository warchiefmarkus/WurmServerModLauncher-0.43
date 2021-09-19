/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.msv.grammar.ChoiceExp;
/*    */ import com.sun.tools.xjc.generator.LookupTableBuilder;
/*    */ import com.sun.tools.xjc.generator.LookupTableUse;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ class LookupTableCache
/*    */   implements LookupTableBuilder
/*    */ {
/* 26 */   private final Map cache = new HashMap();
/*    */   private final LookupTableBuilder core;
/*    */   
/*    */   public LookupTableCache(LookupTableBuilder _core) {
/* 30 */     this.core = _core;
/*    */   }
/*    */   
/*    */   public LookupTableUse buildTable(ChoiceExp exp) {
/* 34 */     if (this.cache.containsKey(exp)) {
/* 35 */       return (LookupTableUse)this.cache.get(exp);
/*    */     }
/* 37 */     LookupTableUse t = this.core.buildTable(exp);
/* 38 */     this.cache.put(exp, t);
/* 39 */     return t;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\LookupTableCache.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */