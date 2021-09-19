/*    */ package org.fourthline.cling.support.model;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.ModelUtil;
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
/*    */ public enum RecordQualityMode
/*    */ {
/* 28 */   EP("0:EP"),
/* 29 */   LP("1:LP"),
/* 30 */   SP("2:SP"),
/* 31 */   BASIC("0:BASIC"),
/* 32 */   MEDIUM("1:MEDIUM"),
/* 33 */   HIGH("2:HIGH"),
/* 34 */   NOT_IMPLEMENTED("NOT_IMPLEMENTED");
/*    */   
/*    */   private String protocolString;
/*    */   
/*    */   RecordQualityMode(String protocolString) {
/* 39 */     this.protocolString = protocolString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return this.protocolString;
/*    */   }
/*    */   
/*    */   public static RecordQualityMode valueOrExceptionOf(String s) throws IllegalArgumentException {
/* 48 */     for (RecordQualityMode recordQualityMode : values()) {
/* 49 */       if (recordQualityMode.protocolString.equals(s)) {
/* 50 */         return recordQualityMode;
/*    */       }
/*    */     } 
/* 53 */     throw new IllegalArgumentException("Invalid record quality mode string: " + s);
/*    */   }
/*    */   
/*    */   public static RecordQualityMode[] valueOfCommaSeparatedList(String s) {
/* 57 */     String[] strings = ModelUtil.fromCommaSeparatedList(s);
/* 58 */     if (strings == null) return new RecordQualityMode[0]; 
/* 59 */     List<RecordQualityMode> result = new ArrayList<>();
/* 60 */     for (String rqm : strings) {
/* 61 */       for (RecordQualityMode recordQualityMode : values()) {
/* 62 */         if (recordQualityMode.protocolString.equals(rqm)) {
/* 63 */           result.add(recordQualityMode);
/*    */         }
/*    */       } 
/*    */     } 
/* 67 */     return result.<RecordQualityMode>toArray(new RecordQualityMode[result.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\RecordQualityMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */