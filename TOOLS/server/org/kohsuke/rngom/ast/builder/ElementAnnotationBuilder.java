package org.kohsuke.rngom.ast.builder;

public interface ElementAnnotationBuilder<P extends org.kohsuke.rngom.ast.om.ParsedPattern, E extends org.kohsuke.rngom.ast.om.ParsedElementAnnotation, L extends org.kohsuke.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>> extends Annotations<E, L, CL> {
  void addText(String paramString, L paramL, CL paramCL) throws BuildException;
  
  E makeElementAnnotation() throws BuildException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\ast\builder\ElementAnnotationBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */