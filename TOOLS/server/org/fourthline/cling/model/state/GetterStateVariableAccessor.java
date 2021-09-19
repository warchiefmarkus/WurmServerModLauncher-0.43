/*    */ package org.fourthline.cling.model.state;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.seamless.util.Reflections;
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
/*    */ public class GetterStateVariableAccessor
/*    */   extends StateVariableAccessor
/*    */ {
/*    */   private Method getter;
/*    */   
/*    */   public GetterStateVariableAccessor(Method getter) {
/* 32 */     this.getter = getter;
/*    */   }
/*    */   
/*    */   public Method getGetter() {
/* 36 */     return this.getter;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getReturnType() {
/* 41 */     return getGetter().getReturnType();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object read(Object serviceImpl) throws Exception {
/* 46 */     return Reflections.invoke(getGetter(), serviceImpl, new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return super.toString() + " Method: " + getGetter();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\state\GetterStateVariableAccessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */