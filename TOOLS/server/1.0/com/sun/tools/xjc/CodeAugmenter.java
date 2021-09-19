package 1.0.com.sun.tools.xjc;

import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.generator.GeneratorContext;
import com.sun.tools.xjc.grammar.AnnotatedGrammar;
import java.io.IOException;
import org.xml.sax.ErrorHandler;

public interface CodeAugmenter {
  String getOptionName();
  
  String getUsage();
  
  int parseArgument(Options paramOptions, String[] paramArrayOfString, int paramInt) throws BadCommandLineException, IOException;
  
  boolean run(AnnotatedGrammar paramAnnotatedGrammar, GeneratorContext paramGeneratorContext, Options paramOptions, ErrorHandler paramErrorHandler);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\CodeAugmenter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */