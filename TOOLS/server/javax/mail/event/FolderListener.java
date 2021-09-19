package javax.mail.event;

import java.util.EventListener;

public interface FolderListener extends EventListener {
  void folderCreated(FolderEvent paramFolderEvent);
  
  void folderDeleted(FolderEvent paramFolderEvent);
  
  void folderRenamed(FolderEvent paramFolderEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\event\FolderListener.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */