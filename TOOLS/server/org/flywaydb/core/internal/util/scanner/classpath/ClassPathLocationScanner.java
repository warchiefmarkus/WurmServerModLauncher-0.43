package org.flywaydb.core.internal.util.scanner.classpath;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

public interface ClassPathLocationScanner {
  Set<String> findResourceNames(String paramString, URL paramURL) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\ClassPathLocationScanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */