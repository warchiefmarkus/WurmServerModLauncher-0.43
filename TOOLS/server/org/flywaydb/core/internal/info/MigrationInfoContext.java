/*    */ package org.flywaydb.core.internal.info;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.flywaydb.core.api.MigrationVersion;
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
/*    */ 
/*    */ public class MigrationInfoContext
/*    */ {
/*    */   public boolean outOfOrder;
/*    */   public boolean pending;
/*    */   public boolean future;
/*    */   public MigrationVersion target;
/*    */   public MigrationVersion schema;
/*    */   public MigrationVersion baseline;
/* 60 */   public MigrationVersion lastResolved = MigrationVersion.EMPTY;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public MigrationVersion lastApplied = MigrationVersion.EMPTY;
/*    */   
/* 67 */   public Map<String, Integer> latestRepeatableRuns = new HashMap<String, Integer>();
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 71 */     if (this == o) return true; 
/* 72 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 74 */     MigrationInfoContext that = (MigrationInfoContext)o;
/*    */     
/* 76 */     if (this.outOfOrder != that.outOfOrder) return false; 
/* 77 */     if (this.pending != that.pending) return false; 
/* 78 */     if (this.future != that.future) return false; 
/* 79 */     if ((this.target != null) ? !this.target.equals(that.target) : (that.target != null)) return false; 
/* 80 */     if ((this.schema != null) ? !this.schema.equals(that.schema) : (that.schema != null)) return false; 
/* 81 */     if ((this.baseline != null) ? !this.baseline.equals(that.baseline) : (that.baseline != null)) return false; 
/* 82 */     if ((this.lastResolved != null) ? !this.lastResolved.equals(that.lastResolved) : (that.lastResolved != null)) return false; 
/* 83 */     if ((this.lastApplied != null) ? !this.lastApplied.equals(that.lastApplied) : (that.lastApplied != null)) return false; 
/* 84 */     return this.latestRepeatableRuns.equals(that.latestRepeatableRuns);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 90 */     int result = this.outOfOrder ? 1 : 0;
/* 91 */     result = 31 * result + (this.pending ? 1 : 0);
/* 92 */     result = 31 * result + (this.future ? 1 : 0);
/* 93 */     result = 31 * result + ((this.target != null) ? this.target.hashCode() : 0);
/* 94 */     result = 31 * result + ((this.schema != null) ? this.schema.hashCode() : 0);
/* 95 */     result = 31 * result + ((this.baseline != null) ? this.baseline.hashCode() : 0);
/* 96 */     result = 31 * result + ((this.lastResolved != null) ? this.lastResolved.hashCode() : 0);
/* 97 */     result = 31 * result + ((this.lastApplied != null) ? this.lastApplied.hashCode() : 0);
/* 98 */     result = 31 * result + this.latestRepeatableRuns.hashCode();
/* 99 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\info\MigrationInfoContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */