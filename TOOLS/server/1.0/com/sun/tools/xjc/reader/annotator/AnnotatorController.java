package 1.0.com.sun.tools.xjc.reader.annotator;

import com.sun.msv.grammar.Expression;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.reader.NameConverter;
import com.sun.tools.xjc.reader.PackageTracker;
import org.xml.sax.Locator;

public interface AnnotatorController {
  NameConverter getNameConverter();
  
  PackageTracker getPackageTracker();
  
  void reportError(Expression[] paramArrayOfExpression, String paramString);
  
  void reportError(Locator[] paramArrayOfLocator, String paramString);
  
  ErrorReceiver getErrorReceiver();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\AnnotatorController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */