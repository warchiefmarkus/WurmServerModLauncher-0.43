package 1.0.com.sun.tools.xjc.generator;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.generator.ClassContext;
import com.sun.tools.xjc.generator.LookupTableBuilder;
import com.sun.tools.xjc.generator.PackageContext;
import com.sun.tools.xjc.generator.field.FieldRenderer;
import com.sun.tools.xjc.grammar.AnnotatedGrammar;
import com.sun.tools.xjc.grammar.ClassItem;
import com.sun.tools.xjc.grammar.FieldUse;
import com.sun.tools.xjc.util.CodeModelClassFactory;

public interface GeneratorContext {
  AnnotatedGrammar getGrammar();
  
  JCodeModel getCodeModel();
  
  LookupTableBuilder getLookupTableBuilder();
  
  JClass getRuntime(Class paramClass);
  
  FieldRenderer getField(FieldUse paramFieldUse);
  
  PackageContext getPackageContext(JPackage paramJPackage);
  
  ClassContext getClassContext(ClassItem paramClassItem);
  
  PackageContext[] getAllPackageContexts();
  
  CodeModelClassFactory getClassFactory();
  
  ErrorReceiver getErrorReceiver();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\GeneratorContext.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */