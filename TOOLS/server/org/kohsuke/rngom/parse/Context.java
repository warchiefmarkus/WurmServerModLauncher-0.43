package org.kohsuke.rngom.parse;

import java.util.Enumeration;
import org.relaxng.datatype.ValidationContext;

public interface Context extends ValidationContext {
  Enumeration prefixes();
  
  Context copy();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\Context.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */