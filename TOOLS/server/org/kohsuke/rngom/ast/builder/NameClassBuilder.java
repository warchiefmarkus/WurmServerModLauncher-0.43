package org.kohsuke.rngom.ast.builder;

import java.util.List;

public interface NameClassBuilder<N extends org.kohsuke.rngom.ast.om.ParsedNameClass, E extends org.kohsuke.rngom.ast.om.ParsedElementAnnotation, L extends org.kohsuke.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> {
  N annotate(N paramN, A paramA) throws BuildException;
  
  N annotateAfter(N paramN, E paramE) throws BuildException;
  
  N commentAfter(N paramN, CL paramCL) throws BuildException;
  
  N makeChoice(List<N> paramList, L paramL, A paramA);
  
  N makeName(String paramString1, String paramString2, String paramString3, L paramL, A paramA);
  
  N makeNsName(String paramString, L paramL, A paramA);
  
  N makeNsName(String paramString, N paramN, L paramL, A paramA);
  
  N makeAnyName(L paramL, A paramA);
  
  N makeAnyName(N paramN, L paramL, A paramA);
  
  N makeErrorNameClass();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\ast\builder\NameClassBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */