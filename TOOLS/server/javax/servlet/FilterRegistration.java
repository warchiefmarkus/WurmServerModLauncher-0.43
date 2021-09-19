package javax.servlet;

import java.util.Collection;
import java.util.EnumSet;

public interface FilterRegistration extends Registration {
  void addMappingForServletNames(EnumSet<DispatcherType> paramEnumSet, boolean paramBoolean, String... paramVarArgs);
  
  Collection<String> getServletNameMappings();
  
  void addMappingForUrlPatterns(EnumSet<DispatcherType> paramEnumSet, boolean paramBoolean, String... paramVarArgs);
  
  Collection<String> getUrlPatternMappings();
  
  public static interface Dynamic extends FilterRegistration, Registration.Dynamic {}
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\FilterRegistration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */