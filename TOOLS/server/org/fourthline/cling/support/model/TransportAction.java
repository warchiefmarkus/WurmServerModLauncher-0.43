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
/*    */ public enum TransportAction
/*    */ {
/* 28 */   Play,
/* 29 */   Stop,
/* 30 */   Pause,
/* 31 */   Seek,
/* 32 */   Next,
/* 33 */   Previous,
/* 34 */   Record;
/*    */   
/*    */   public static TransportAction[] valueOfCommaSeparatedList(String s) {
/* 37 */     String[] strings = ModelUtil.fromCommaSeparatedList(s);
/* 38 */     if (strings == null) return new TransportAction[0]; 
/* 39 */     List<TransportAction> result = new ArrayList<>();
/* 40 */     for (String taString : strings) {
/* 41 */       for (TransportAction ta : values()) {
/* 42 */         if (ta.name().equals(taString)) {
/* 43 */           result.add(ta);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 48 */     return result.<TransportAction>toArray(new TransportAction[result.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\TransportAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */