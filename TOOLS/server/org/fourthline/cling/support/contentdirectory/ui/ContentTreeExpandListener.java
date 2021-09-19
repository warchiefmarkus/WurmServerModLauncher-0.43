/*    */ package org.fourthline.cling.support.contentdirectory.ui;
/*    */ 
/*    */ import javax.swing.event.TreeExpansionEvent;
/*    */ import javax.swing.event.TreeWillExpandListener;
/*    */ import javax.swing.tree.DefaultMutableTreeNode;
/*    */ import javax.swing.tree.DefaultTreeModel;
/*    */ import javax.swing.tree.ExpandVetoException;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.controlpoint.ControlPoint;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContentTreeExpandListener
/*    */   implements TreeWillExpandListener
/*    */ {
/*    */   protected final ControlPoint controlPoint;
/*    */   protected final Service service;
/*    */   protected final DefaultTreeModel treeModel;
/*    */   protected final ContentBrowseActionCallbackCreator actionCreator;
/*    */   
/*    */   public ContentTreeExpandListener(ControlPoint controlPoint, Service service, DefaultTreeModel treeModel, ContentBrowseActionCallbackCreator actionCreator) {
/* 42 */     this.controlPoint = controlPoint;
/* 43 */     this.service = service;
/* 44 */     this.treeModel = treeModel;
/* 45 */     this.actionCreator = actionCreator;
/*    */   }
/*    */   
/*    */   public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
/* 49 */     DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
/*    */ 
/*    */     
/* 52 */     treeNode.removeAllChildren();
/* 53 */     this.treeModel.nodeStructureChanged(treeNode);
/*    */ 
/*    */ 
/*    */     
/* 57 */     ActionCallback callback = this.actionCreator.createContentBrowseActionCallback(this.service, this.treeModel, treeNode);
/*    */ 
/*    */     
/* 60 */     this.controlPoint.execute(callback);
/*    */   }
/*    */   
/*    */   public void treeWillCollapse(TreeExpansionEvent e) throws ExpandVetoException {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirector\\ui\ContentTreeExpandListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */