package com.sun.codemodel;

public interface JAssignmentTarget extends JGenerable, JExpression {
  JExpression assign(JExpression paramJExpression);
  
  JExpression assignPlus(JExpression paramJExpression);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JAssignmentTarget.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */