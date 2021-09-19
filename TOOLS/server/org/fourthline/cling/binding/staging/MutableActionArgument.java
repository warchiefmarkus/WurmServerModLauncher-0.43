/*    */ package org.fourthline.cling.binding.staging;
/*    */ 
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
/*    */ public class MutableActionArgument
/*    */ {
/*    */   public String name;
/*    */   public String relatedStateVariable;
/*    */   public ActionArgument.Direction direction;
/*    */   public boolean retval;
/*    */   
/*    */   public ActionArgument build() {
/* 31 */     return new ActionArgument(this.name, this.relatedStateVariable, this.direction, this.retval);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\staging\MutableActionArgument.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */