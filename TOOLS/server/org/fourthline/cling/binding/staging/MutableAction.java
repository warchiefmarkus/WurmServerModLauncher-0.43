/*    */ package org.fourthline.cling.binding.staging;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.meta.Action;
/*    */ import org.fourthline.cling.model.meta.ActionArgument;
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
/*    */ public class MutableAction
/*    */ {
/*    */   public String name;
/* 30 */   public List<MutableActionArgument> arguments = new ArrayList<>();
/*    */   
/*    */   public Action build() {
/* 33 */     return new Action(this.name, createActionArgumennts());
/*    */   }
/*    */   
/*    */   public ActionArgument[] createActionArgumennts() {
/* 37 */     ActionArgument[] array = new ActionArgument[this.arguments.size()];
/* 38 */     int i = 0;
/* 39 */     for (MutableActionArgument argument : this.arguments) {
/* 40 */       array[i++] = argument.build();
/*    */     }
/* 42 */     return array;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\staging\MutableAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */