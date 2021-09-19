package 1.0.com.sun.codemodel;

import com.sun.codemodel.JArrayCompRef;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JGenerable;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

public interface JExpression extends JGenerable {
  com.sun.codemodel.JExpression minus();
  
  com.sun.codemodel.JExpression not();
  
  com.sun.codemodel.JExpression complement();
  
  com.sun.codemodel.JExpression incr();
  
  com.sun.codemodel.JExpression decr();
  
  com.sun.codemodel.JExpression plus(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression minus(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression mul(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression div(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression mod(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression shl(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression shr(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression shrz(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression band(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression bor(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression cand(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression cor(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression xor(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression lt(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression lte(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression gt(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression gte(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression eq(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression ne(com.sun.codemodel.JExpression paramJExpression);
  
  com.sun.codemodel.JExpression _instanceof(JType paramJType);
  
  JInvocation invoke(JMethod paramJMethod);
  
  JInvocation invoke(String paramString);
  
  JFieldRef ref(JVar paramJVar);
  
  JFieldRef ref(String paramString);
  
  JArrayCompRef component(com.sun.codemodel.JExpression paramJExpression);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JExpression.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */