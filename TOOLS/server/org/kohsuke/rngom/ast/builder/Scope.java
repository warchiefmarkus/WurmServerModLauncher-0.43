package org.kohsuke.rngom.ast.builder;

public interface Scope<P extends org.kohsuke.rngom.ast.om.ParsedPattern, E extends org.kohsuke.rngom.ast.om.ParsedElementAnnotation, L extends org.kohsuke.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> extends GrammarSection<P, E, L, A, CL> {
  P makeParentRef(String paramString, L paramL, A paramA) throws BuildException;
  
  P makeRef(String paramString, L paramL, A paramA) throws BuildException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\ast\builder\Scope.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */