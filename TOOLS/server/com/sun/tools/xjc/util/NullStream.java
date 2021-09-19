package com.sun.tools.xjc.util;

import java.io.IOException;
import java.io.OutputStream;

public class NullStream extends OutputStream {
  public void write(int b) throws IOException {}
  
  public void close() throws IOException {}
  
  public void flush() throws IOException {}
  
  public void write(byte[] b, int off, int len) throws IOException {}
  
  public void write(byte[] b) throws IOException {}
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\NullStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */