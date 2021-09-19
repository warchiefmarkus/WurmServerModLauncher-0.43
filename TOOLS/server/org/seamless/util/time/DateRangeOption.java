/*    */ package org.seamless.util.time;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public enum DateRangeOption
/*    */   implements Serializable
/*    */ {
/* 24 */   ALL("All dates", DateRange.Preset.ALL.getDateRange()),
/* 25 */   MONTH_TO_DATE("Month to date", DateRange.Preset.MONTH_TO_DATE.getDateRange()),
/* 26 */   YEAR_TO_DATE("Year to date", DateRange.Preset.YEAR_TO_DATE.getDateRange()),
/* 27 */   LAST_MONTH("Last month", DateRange.Preset.LAST_MONTH.getDateRange()),
/* 28 */   LAST_YEAR("Last year", DateRange.Preset.LAST_YEAR.getDateRange()),
/* 29 */   CUSTOM("Custom dates", null);
/*    */   
/*    */   String label;
/*    */   DateRange dateRange;
/*    */   
/*    */   DateRangeOption(String label, DateRange dateRange) {
/* 35 */     this.label = label;
/* 36 */     this.dateRange = dateRange;
/*    */   }
/*    */   
/*    */   public String getLabel() {
/* 40 */     return this.label;
/*    */   }
/*    */   
/*    */   public DateRange getDateRange() {
/* 44 */     return this.dateRange;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\time\DateRangeOption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */