package com.sun.codemodel;

public interface JExpression extends JGenerable {
  JExpression minus();
  
  JExpression not();
  
  JExpression complement();
  
  JExpression incr();
  
  JExpression decr();
  
  JExpression plus(JExpression paramJExpression);
  
  JExpression minus(JExpression paramJExpression);
  
  JExpression mul(JExpression paramJExpression);
  
  JExpression div(JExpression paramJExpression);
  
  JExpression mod(JExpression paramJExpression);
  
  JExpression shl(JExpression paramJExpression);
  
  JExpression shr(JExpression paramJExpression);
  
  JExpression shrz(JExpression paramJExpression);
  
  JExpression band(JExpression paramJExpression);
  
  JExpression bor(JExpression paramJExpression);
  
  JExpression cand(JExpression paramJExpression);
  
  JExpression cor(JExpression paramJExpression);
  
  JExpression xor(JExpression paramJExpression);
  
  JExpression lt(JExpression paramJExpression);
  
  JExpression lte(JExpression paramJExpression);
  
  JExpression gt(JExpression paramJExpression);
  
  JExpression gte(JExpression paramJExpression);
  
  JExpression eq(JExpression paramJExpression);
  
  JExpression ne(JExpression paramJExpression);
  
  JExpression _instanceof(JType paramJType);
  
  JInvocation invoke(JMethod paramJMethod);
  
  JInvocation invoke(String paramString);
  
  JFieldRef ref(JVar paramJVar);
  
  JFieldRef ref(String paramString);
  
  JArrayCompRef component(JExpression paramJExpression);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JExpression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */