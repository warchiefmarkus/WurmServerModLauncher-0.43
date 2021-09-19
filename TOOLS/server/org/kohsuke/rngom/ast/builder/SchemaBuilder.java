package org.kohsuke.rngom.ast.builder;

import java.util.List;
import org.kohsuke.rngom.parse.Context;
import org.kohsuke.rngom.parse.IllegalSchemaException;
import org.kohsuke.rngom.parse.Parseable;

public interface SchemaBuilder<N extends org.kohsuke.rngom.ast.om.ParsedNameClass, P extends org.kohsuke.rngom.ast.om.ParsedPattern, E extends org.kohsuke.rngom.ast.om.ParsedElementAnnotation, L extends org.kohsuke.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> {
  NameClassBuilder<N, E, L, A, CL> getNameClassBuilder() throws BuildException;
  
  P makeChoice(List<P> paramList, L paramL, A paramA) throws BuildException;
  
  P makeInterleave(List<P> paramList, L paramL, A paramA) throws BuildException;
  
  P makeGroup(List<P> paramList, L paramL, A paramA) throws BuildException;
  
  P makeOneOrMore(P paramP, L paramL, A paramA) throws BuildException;
  
  P makeZeroOrMore(P paramP, L paramL, A paramA) throws BuildException;
  
  P makeOptional(P paramP, L paramL, A paramA) throws BuildException;
  
  P makeList(P paramP, L paramL, A paramA) throws BuildException;
  
  P makeMixed(P paramP, L paramL, A paramA) throws BuildException;
  
  P makeEmpty(L paramL, A paramA);
  
  P makeNotAllowed(L paramL, A paramA);
  
  P makeText(L paramL, A paramA);
  
  P makeAttribute(N paramN, P paramP, L paramL, A paramA) throws BuildException;
  
  P makeElement(N paramN, P paramP, L paramL, A paramA) throws BuildException;
  
  DataPatternBuilder makeDataPatternBuilder(String paramString1, String paramString2, L paramL) throws BuildException;
  
  P makeValue(String paramString1, String paramString2, String paramString3, Context paramContext, String paramString4, L paramL, A paramA) throws BuildException;
  
  Grammar<P, E, L, A, CL> makeGrammar(Scope<P, E, L, A, CL> paramScope);
  
  P annotate(P paramP, A paramA) throws BuildException;
  
  P annotateAfter(P paramP, E paramE) throws BuildException;
  
  P commentAfter(P paramP, CL paramCL) throws BuildException;
  
  P makeExternalRef(Parseable paramParseable, String paramString1, String paramString2, Scope<P, E, L, A, CL> paramScope, L paramL, A paramA) throws BuildException, IllegalSchemaException;
  
  L makeLocation(String paramString, int paramInt1, int paramInt2);
  
  A makeAnnotations(CL paramCL, Context paramContext);
  
  ElementAnnotationBuilder<P, E, L, A, CL> makeElementAnnotationBuilder(String paramString1, String paramString2, String paramString3, L paramL, CL paramCL, Context paramContext);
  
  CL makeCommentList();
  
  P makeErrorPattern();
  
  boolean usesComments();
  
  P expandPattern(P paramP) throws BuildException, IllegalSchemaException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\ast\builder\SchemaBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */