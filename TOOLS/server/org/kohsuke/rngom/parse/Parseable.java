package org.kohsuke.rngom.parse;

import org.kohsuke.rngom.ast.builder.BuildException;
import org.kohsuke.rngom.ast.builder.IncludedGrammar;
import org.kohsuke.rngom.ast.builder.SchemaBuilder;
import org.kohsuke.rngom.ast.builder.Scope;

public interface Parseable {
  <P extends org.kohsuke.rngom.ast.om.ParsedPattern> P parse(SchemaBuilder<?, P, ?, ?, ?, ?> paramSchemaBuilder) throws BuildException, IllegalSchemaException;
  
  <P extends org.kohsuke.rngom.ast.om.ParsedPattern> P parseInclude(String paramString1, SchemaBuilder<?, P, ?, ?, ?, ?> paramSchemaBuilder, IncludedGrammar<P, ?, ?, ?, ?> paramIncludedGrammar, String paramString2) throws BuildException, IllegalSchemaException;
  
  <P extends org.kohsuke.rngom.ast.om.ParsedPattern> P parseExternal(String paramString1, SchemaBuilder<?, P, ?, ?, ?, ?> paramSchemaBuilder, Scope paramScope, String paramString2) throws BuildException, IllegalSchemaException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\Parseable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */