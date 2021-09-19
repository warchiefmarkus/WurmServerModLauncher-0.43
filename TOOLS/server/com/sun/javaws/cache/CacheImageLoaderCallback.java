package com.sun.javaws.cache;

import com.sun.javaws.jnl.IconDesc;
import java.awt.Image;
import java.io.File;

public interface CacheImageLoaderCallback {
  void imageAvailable(IconDesc paramIconDesc, Image paramImage, File paramFile);
  
  void finalImageAvailable(IconDesc paramIconDesc, Image paramImage, File paramFile);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\CacheImageLoaderCallback.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */