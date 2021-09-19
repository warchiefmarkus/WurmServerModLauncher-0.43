package org.flywaydb.core.internal.util.scanner.classpath;

import java.io.IOException;
import java.net.URL;

public interface UrlResolver {
  URL toStandardJavaUrl(URL paramURL) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\UrlResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */