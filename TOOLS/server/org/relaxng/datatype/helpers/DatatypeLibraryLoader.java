package org.relaxng.datatype.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import org.relaxng.datatype.DatatypeLibrary;
import org.relaxng.datatype.DatatypeLibraryFactory;

public class DatatypeLibraryLoader implements DatatypeLibraryFactory {
  private final Service service = new Service(DatatypeLibraryFactory.class);
  
  public DatatypeLibrary createDatatypeLibrary(String paramString) {
    Enumeration enumeration = this.service.getProviders();
    while (enumeration.hasMoreElements()) {
      DatatypeLibraryFactory datatypeLibraryFactory = enumeration.nextElement();
      DatatypeLibrary datatypeLibrary = datatypeLibraryFactory.createDatatypeLibrary(paramString);
      if (datatypeLibrary != null)
        return datatypeLibrary; 
    } 
    return null;
  }
  
  private static class Service {
    private final Class serviceClass;
    
    private final Enumeration configFiles;
    
    private Enumeration classNames = null;
    
    private final Vector providers = new Vector();
    
    private Loader loader;
    
    private static final int START = 0;
    
    private static final int IN_NAME = 1;
    
    private static final int IN_COMMENT = 2;
    
    public Service(Class param1Class) {
      try {
        this.loader = new Loader2();
      } catch (NoSuchMethodError noSuchMethodError) {
        this.loader = new Loader();
      } 
      this.serviceClass = param1Class;
      String str = "META-INF/services/" + this.serviceClass.getName();
      this.configFiles = this.loader.getResources(str);
    }
    
    public Enumeration getProviders() {
      return new ProviderEnumeration();
    }
    
    private synchronized boolean moreProviders() {
      while (true) {
        while (this.classNames != null) {
          while (this.classNames.hasMoreElements()) {
            String str = this.classNames.nextElement();
            try {
              Class clazz = this.loader.loadClass(str);
              Object object = clazz.newInstance();
              if (this.serviceClass.isInstance(object)) {
                this.providers.addElement(object);
                return true;
              } 
            } catch (ClassNotFoundException classNotFoundException) {
            
            } catch (InstantiationException instantiationException) {
            
            } catch (IllegalAccessException illegalAccessException) {
            
            } catch (LinkageError linkageError) {}
          } 
          this.classNames = null;
        } 
        if (!this.configFiles.hasMoreElements())
          return false; 
        this.classNames = parseConfigFile(this.configFiles.nextElement());
      } 
    }
    
    private static Enumeration parseConfigFile(URL param1URL) {
      try {
        InputStreamReader inputStreamReader;
        InputStream inputStream = param1URL.openStream();
        try {
          inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
          inputStreamReader = new InputStreamReader(inputStream, "UTF8");
        } 
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        Vector vector = new Vector();
        StringBuffer stringBuffer = new StringBuffer();
        byte b = 0;
        while (true) {
          int i = bufferedReader.read();
          if (i < 0) {
            if (stringBuffer.length() != 0)
              vector.addElement(stringBuffer.toString()); 
            return vector.elements();
          } 
          char c = (char)i;
          switch (c) {
            case '\n':
            case '\r':
              b = 0;
              break;
            case '\t':
            case ' ':
              break;
            case '#':
              b = 2;
              break;
            default:
              if (b != 2) {
                b = 1;
                stringBuffer.append(c);
              } 
              break;
          } 
          if (stringBuffer.length() != 0 && b != 1) {
            vector.addElement(stringBuffer.toString());
            stringBuffer.setLength(0);
          } 
        } 
      } catch (IOException iOException) {
        return null;
      } 
    }
    
    private static class Loader2 extends Loader {
      private ClassLoader cl = Loader2.class.getClassLoader();
      
      Loader2() {
        ClassLoader classLoader1 = Thread.currentThread().getContextClassLoader();
        for (ClassLoader classLoader2 = classLoader1; classLoader2 != null; classLoader2 = classLoader2.getParent()) {
          if (classLoader2 == this.cl) {
            this.cl = classLoader1;
            break;
          } 
        } 
      }
      
      Enumeration getResources(String param2String) {
        try {
          return this.cl.getResources(param2String);
        } catch (IOException iOException) {
          return new DatatypeLibraryLoader.Service.Singleton(null);
        } 
      }
      
      Class loadClass(String param2String) throws ClassNotFoundException {
        return Class.forName(param2String, true, this.cl);
      }
    }
    
    private static class Loader {
      private Loader() {}
      
      Enumeration getResources(String param2String) {
        URL uRL;
        ClassLoader classLoader = Loader.class.getClassLoader();
        if (classLoader == null) {
          uRL = ClassLoader.getSystemResource(param2String);
        } else {
          uRL = classLoader.getResource(param2String);
        } 
        return new DatatypeLibraryLoader.Service.Singleton(uRL);
      }
      
      Class loadClass(String param2String) throws ClassNotFoundException {
        return Class.forName(param2String);
      }
    }
    
    private static class Singleton implements Enumeration {
      private Object obj;
      
      private Singleton(Object param2Object) {
        this.obj = param2Object;
      }
      
      public boolean hasMoreElements() {
        return (this.obj != null);
      }
      
      public Object nextElement() {
        if (this.obj == null)
          throw new NoSuchElementException(); 
        Object object = this.obj;
        this.obj = null;
        return object;
      }
    }
    
    private class ProviderEnumeration implements Enumeration {
      private int nextIndex;
      
      private final DatatypeLibraryLoader.Service this$0;
      
      private ProviderEnumeration(DatatypeLibraryLoader.Service this$0) {
        DatatypeLibraryLoader.Service.this = DatatypeLibraryLoader.Service.this;
        this.nextIndex = 0;
      }
      
      public boolean hasMoreElements() {
        return (this.nextIndex < DatatypeLibraryLoader.Service.this.providers.size() || DatatypeLibraryLoader.Service.this.moreProviders());
      }
      
      public Object nextElement() {
        try {
          return DatatypeLibraryLoader.Service.this.providers.elementAt(this.nextIndex++);
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
          throw new NoSuchElementException();
        } 
      }
    }
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\relaxng\datatype\helpers\DatatypeLibraryLoader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */