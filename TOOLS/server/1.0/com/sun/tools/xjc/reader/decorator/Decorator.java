package 1.0.com.sun.tools.xjc.reader.decorator;

import com.sun.msv.grammar.Expression;
import com.sun.msv.reader.State;

public interface Decorator {
  Expression decorate(State paramState, Expression paramExpression);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\decorator\Decorator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */