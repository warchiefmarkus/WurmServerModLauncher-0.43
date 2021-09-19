package org.flywaydb.core.internal.util.scanner.classpath;

import org.flywaydb.core.internal.util.Location;
import org.flywaydb.core.internal.util.scanner.Resource;

public interface ResourceAndClassScanner {
  Resource[] scanForResources(Location paramLocation, String paramString1, String paramString2) throws Exception;
  
  Class<?>[] scanForClasses(Location paramLocation, Class<?> paramClass) throws Exception;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\ResourceAndClassScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */