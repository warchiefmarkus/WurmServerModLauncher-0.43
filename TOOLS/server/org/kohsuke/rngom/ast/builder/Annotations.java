package org.kohsuke.rngom.ast.builder;

public interface Annotations<E extends org.kohsuke.rngom.ast.om.ParsedElementAnnotation, L extends org.kohsuke.rngom.ast.om.Location, CL extends CommentList<L>> {
  void addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, L paramL) throws BuildException;
  
  void addElement(E paramE) throws BuildException;
  
  void addComment(CL paramCL) throws BuildException;
  
  void addLeadingComment(CL paramCL) throws BuildException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\ast\builder\Annotations.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */