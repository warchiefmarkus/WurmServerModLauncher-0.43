/*     */ package org.fourthline.cling.support.contentdirectory.ui;
/*     */ 
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.event.TreeWillExpandListener;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import org.fourthline.cling.controlpoint.ActionCallback;
/*     */ import org.fourthline.cling.controlpoint.ControlPoint;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.support.contentdirectory.callback.Browse;
/*     */ import org.fourthline.cling.support.model.container.Container;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ContentTree
/*     */   extends JTree
/*     */   implements ContentBrowseActionCallbackCreator
/*     */ {
/*     */   protected Container rootContainer;
/*     */   protected DefaultMutableTreeNode rootNode;
/*     */   
/*     */   protected ContentTree() {}
/*     */   
/*     */   public ContentTree(ControlPoint controlPoint, Service service) {
/*  47 */     init(controlPoint, service);
/*     */   }
/*     */   
/*     */   public void init(ControlPoint controlPoint, Service service) {
/*  51 */     this.rootContainer = createRootContainer(service);
/*  52 */     this.rootNode = new DefaultMutableTreeNode(this.rootContainer)
/*     */       {
/*     */         public boolean isLeaf() {
/*  55 */           return false;
/*     */         }
/*     */       };
/*     */     
/*  59 */     DefaultTreeModel treeModel = new DefaultTreeModel(this.rootNode);
/*  60 */     setModel(treeModel);
/*     */     
/*  62 */     getSelectionModel().setSelectionMode(1);
/*  63 */     addTreeWillExpandListener(createContainerTreeExpandListener(controlPoint, service, treeModel));
/*  64 */     setCellRenderer(createContainerTreeCellRenderer());
/*     */     
/*  66 */     controlPoint.execute(createContentBrowseActionCallback(service, treeModel, getRootNode()));
/*     */   }
/*     */   
/*     */   public Container getRootContainer() {
/*  70 */     return this.rootContainer;
/*     */   }
/*     */   
/*     */   public DefaultMutableTreeNode getRootNode() {
/*  74 */     return this.rootNode;
/*     */   }
/*     */   
/*     */   public DefaultMutableTreeNode getSelectedNode() {
/*  78 */     return (DefaultMutableTreeNode)getLastSelectedPathComponent();
/*     */   }
/*     */   
/*     */   protected Container createRootContainer(Service service) {
/*  82 */     Container rootContainer = new Container();
/*  83 */     rootContainer.setId("0");
/*  84 */     rootContainer.setTitle("Content Directory on " + service.getDevice().getDisplayString());
/*  85 */     return rootContainer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TreeWillExpandListener createContainerTreeExpandListener(ControlPoint controlPoint, Service service, DefaultTreeModel treeModel) {
/*  91 */     return new ContentTreeExpandListener(controlPoint, service, treeModel, this);
/*     */   }
/*     */   
/*     */   protected DefaultTreeCellRenderer createContainerTreeCellRenderer() {
/*  95 */     return new ContentTreeCellRenderer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionCallback createContentBrowseActionCallback(Service service, DefaultTreeModel treeModel, DefaultMutableTreeNode treeNode) {
/* 102 */     return (ActionCallback)new ContentBrowseActionCallback(service, treeModel, treeNode) {
/*     */         public void updateStatusUI(Browse.Status status, DefaultMutableTreeNode treeNode, DefaultTreeModel treeModel) {
/* 104 */           ContentTree.this.updateStatus(status, treeNode, treeModel);
/*     */         }
/*     */         public void failureUI(String failureMessage) {
/* 107 */           ContentTree.this.failure(failureMessage);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public void updateStatus(Browse.Status status, DefaultMutableTreeNode treeNode, DefaultTreeModel treeModel) {
/*     */     int index;
/* 114 */     switch (status) {
/*     */       case LOADING:
/*     */       case NO_CONTENT:
/* 117 */         treeNode.removeAllChildren();
/* 118 */         index = (treeNode.getChildCount() <= 0) ? 0 : treeNode.getChildCount();
/* 119 */         treeModel.insertNodeInto(new DefaultMutableTreeNode(status.getDefaultMessage()), treeNode, index);
/* 120 */         treeModel.nodeStructureChanged(treeNode);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract void failure(String paramString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirector\\ui\ContentTree.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */