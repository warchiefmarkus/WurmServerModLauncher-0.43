/*    */ package org.flywaydb.core.internal.resolver;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.flywaydb.core.api.resolver.ResolvedMigration;
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
/*    */ public class ResolvedMigrationComparator
/*    */   implements Comparator<ResolvedMigration>
/*    */ {
/*    */   public int compare(ResolvedMigration o1, ResolvedMigration o2) {
/* 28 */     if (o1.getVersion() != null && o2.getVersion() != null) {
/* 29 */       return o1.getVersion().compareTo(o2.getVersion());
/*    */     }
/* 31 */     if (o1.getVersion() != null) {
/* 32 */       return Integer.MIN_VALUE;
/*    */     }
/* 34 */     if (o2.getVersion() != null) {
/* 35 */       return Integer.MAX_VALUE;
/*    */     }
/* 37 */     return o1.getDescription().compareTo(o2.getDescription());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\ResolvedMigrationComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */