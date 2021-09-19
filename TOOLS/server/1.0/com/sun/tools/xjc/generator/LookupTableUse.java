/*    */ package 1.0.com.sun.tools.xjc.generator;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.grammar.SimpleNameClass;
/*    */ import com.sun.tools.xjc.generator.LookupTable;
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
/*    */ public class LookupTableUse
/*    */ {
/*    */   public final LookupTable table;
/*    */   public final Expression anomaly;
/*    */   public final SimpleNameClass switchAttName;
/*    */   
/*    */   LookupTableUse(LookupTable _table, Expression _anomaly, SimpleNameClass _switchAttName) {
/* 39 */     this.table = _table;
/* 40 */     this.anomaly = _anomaly;
/* 41 */     this.switchAttName = _switchAttName;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\LookupTableUse.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */