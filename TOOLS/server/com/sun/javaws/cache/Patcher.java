package com.sun.javaws.cache;

import java.io.IOException;
import java.io.OutputStream;

public interface Patcher {
  void applyPatch(PatchDelegate paramPatchDelegate, String paramString1, String paramString2, OutputStream paramOutputStream) throws IOException;
  
  public static interface PatchDelegate {
    void patching(int param1Int);
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\Patcher.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */