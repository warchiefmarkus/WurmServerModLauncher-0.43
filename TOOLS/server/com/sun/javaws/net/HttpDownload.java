package com.sun.javaws.net;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface HttpDownload {
  void download(HttpResponse paramHttpResponse, File paramFile, HttpDownloadListener paramHttpDownloadListener) throws IOException, CanceledDownloadException;
  
  void download(URL paramURL, File paramFile, HttpDownloadListener paramHttpDownloadListener) throws IOException, CanceledDownloadException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\net\HttpDownload.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */