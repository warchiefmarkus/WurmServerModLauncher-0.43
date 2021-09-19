/*    */ package org.fourthline.cling.support.model;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public class SortCriterion
/*    */ {
/*    */   protected final boolean ascending;
/*    */   protected final String propertyName;
/*    */   
/*    */   public SortCriterion(boolean ascending, String propertyName) {
/* 30 */     this.ascending = ascending;
/* 31 */     this.propertyName = propertyName;
/*    */   }
/*    */   
/*    */   public SortCriterion(String criterion) {
/* 35 */     this(criterion.startsWith("+"), criterion.substring(1));
/* 36 */     if (!criterion.startsWith("-") && !criterion.startsWith("+"))
/* 37 */       throw new IllegalArgumentException("Missing sort prefix +/- on criterion: " + criterion); 
/*    */   }
/*    */   
/*    */   public boolean isAscending() {
/* 41 */     return this.ascending;
/*    */   }
/*    */   
/*    */   public String getPropertyName() {
/* 45 */     return this.propertyName;
/*    */   }
/*    */   
/*    */   public static SortCriterion[] valueOf(String s) {
/* 49 */     if (s == null || s.length() == 0) return new SortCriterion[0]; 
/* 50 */     List<SortCriterion> list = new ArrayList<>();
/* 51 */     String[] criteria = s.split(",");
/* 52 */     for (String criterion : criteria) {
/* 53 */       list.add(new SortCriterion(criterion.trim()));
/*    */     }
/* 55 */     return list.<SortCriterion>toArray(new SortCriterion[list.size()]);
/*    */   }
/*    */   
/*    */   public static String toString(SortCriterion[] criteria) {
/* 59 */     if (criteria == null) return ""; 
/* 60 */     StringBuilder sb = new StringBuilder();
/* 61 */     for (SortCriterion sortCriterion : criteria) {
/* 62 */       sb.append(sortCriterion.toString()).append(",");
/*    */     }
/* 64 */     if (sb.toString().endsWith(",")) sb.deleteCharAt(sb.length() - 1); 
/* 65 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     StringBuilder sb = new StringBuilder();
/* 71 */     sb.append(this.ascending ? "+" : "-");
/* 72 */     sb.append(this.propertyName);
/* 73 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\SortCriterion.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */