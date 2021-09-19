package org.fourthline.cling.support.contentdirectory.ui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.meta.Service;

public interface ContentBrowseActionCallbackCreator {
  ActionCallback createContentBrowseActionCallback(Service paramService, DefaultTreeModel paramDefaultTreeModel, DefaultMutableTreeNode paramDefaultMutableTreeNode);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirector\\ui\ContentBrowseActionCallbackCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */