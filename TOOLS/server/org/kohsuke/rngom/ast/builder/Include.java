package org.kohsuke.rngom.ast.builder;

import org.kohsuke.rngom.parse.IllegalSchemaException;
import org.kohsuke.rngom.parse.Parseable;

public interface Include<P extends org.kohsuke.rngom.ast.om.ParsedPattern, E extends org.kohsuke.rngom.ast.om.ParsedElementAnnotation, L extends org.kohsuke.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> extends GrammarSection<P, E, L, A, CL> {
  void endInclude(Parseable paramParseable, String paramString1, String paramString2, L paramL, A paramA) throws BuildException, IllegalSchemaException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\ast\builder\Include.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */