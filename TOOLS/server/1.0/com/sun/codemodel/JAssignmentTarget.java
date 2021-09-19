package 1.0.com.sun.codemodel;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JGenerable;

public interface JAssignmentTarget extends JGenerable, JExpression {
  JExpression assign(JExpression paramJExpression);
  
  JExpression assignPlus(JExpression paramJExpression);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JAssignmentTarget.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */