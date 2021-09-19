package org.kohsuke.rngom.ast.builder;

import org.kohsuke.rngom.parse.Context;

public interface DataPatternBuilder<P extends org.kohsuke.rngom.ast.om.ParsedPattern, E extends org.kohsuke.rngom.ast.om.ParsedElementAnnotation, L extends org.kohsuke.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> {
  void addParam(String paramString1, String paramString2, Context paramContext, String paramString3, L paramL, A paramA) throws BuildException;
  
  void annotation(E paramE);
  
  P makePattern(L paramL, A paramA) throws BuildException;
  
  P makePattern(P paramP, L paramL, A paramA) throws BuildException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\ast\builder\DataPatternBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */