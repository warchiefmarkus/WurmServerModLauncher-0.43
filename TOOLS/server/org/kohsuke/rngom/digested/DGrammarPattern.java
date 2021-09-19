/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DGrammarPattern
/*    */   extends DPattern
/*    */   implements Iterable<DDefine>
/*    */ {
/* 13 */   private final Map<String, DDefine> patterns = new HashMap<String, DDefine>();
/*    */ 
/*    */   
/*    */   DPattern start;
/*    */ 
/*    */ 
/*    */   
/*    */   public DPattern getStart() {
/* 21 */     return this.start;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DDefine get(String name) {
/* 31 */     return this.patterns.get(name);
/*    */   }
/*    */   
/*    */   DDefine getOrAdd(String name) {
/* 35 */     if (this.patterns.containsKey(name)) {
/* 36 */       return get(name);
/*    */     }
/* 38 */     DDefine d = new DDefine(name);
/* 39 */     this.patterns.put(name, d);
/* 40 */     return d;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<DDefine> iterator() {
/* 48 */     return this.patterns.values().iterator();
/*    */   }
/*    */   
/*    */   public boolean isNullable() {
/* 52 */     return this.start.isNullable();
/*    */   }
/*    */   
/*    */   public <V> V accept(DPatternVisitor<V> visitor) {
/* 56 */     return visitor.onGrammar(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DGrammarPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */