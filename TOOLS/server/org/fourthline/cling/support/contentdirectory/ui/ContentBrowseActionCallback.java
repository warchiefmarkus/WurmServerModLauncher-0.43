/*     */ package org.fourthline.cling.support.contentdirectory.ui;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.MutableTreeNode;
/*     */ import org.fourthline.cling.model.action.ActionException;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.support.contentdirectory.callback.Browse;
/*     */ import org.fourthline.cling.support.model.BrowseFlag;
/*     */ import org.fourthline.cling.support.model.DIDLContent;
/*     */ import org.fourthline.cling.support.model.SortCriterion;
/*     */ import org.fourthline.cling.support.model.container.Container;
/*     */ import org.fourthline.cling.support.model.item.Item;
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
/*     */ public abstract class ContentBrowseActionCallback
/*     */   extends Browse
/*     */ {
/*  46 */   private static Logger log = Logger.getLogger(ContentBrowseActionCallback.class.getName());
/*     */   
/*     */   protected final DefaultTreeModel treeModel;
/*     */   protected final DefaultMutableTreeNode treeNode;
/*     */   
/*     */   public ContentBrowseActionCallback(Service service, DefaultTreeModel treeModel, DefaultMutableTreeNode treeNode) {
/*  52 */     super(service, ((Container)treeNode.getUserObject()).getId(), BrowseFlag.DIRECT_CHILDREN, "*", 0L, null, new SortCriterion[] { new SortCriterion(true, "dc:title") });
/*  53 */     this.treeModel = treeModel;
/*  54 */     this.treeNode = treeNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public ContentBrowseActionCallback(Service service, DefaultTreeModel treeModel, DefaultMutableTreeNode treeNode, String filter, long firstResult, long maxResults, SortCriterion... orderBy) {
/*  59 */     super(service, ((Container)treeNode.getUserObject()).getId(), BrowseFlag.DIRECT_CHILDREN, filter, firstResult, Long.valueOf(maxResults), orderBy);
/*  60 */     this.treeModel = treeModel;
/*  61 */     this.treeNode = treeNode;
/*     */   }
/*     */   
/*     */   public DefaultTreeModel getTreeModel() {
/*  65 */     return this.treeModel;
/*     */   }
/*     */   
/*     */   public DefaultMutableTreeNode getTreeNode() {
/*  69 */     return this.treeNode;
/*     */   }
/*     */   
/*     */   public void received(ActionInvocation actionInvocation, DIDLContent didl) {
/*  73 */     log.fine("Received browse action DIDL descriptor, creating tree nodes");
/*  74 */     final List<DefaultMutableTreeNode> childNodes = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  79 */       for (Container childContainer : didl.getContainers()) {
/*  80 */         DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childContainer)
/*     */           {
/*     */             public boolean isLeaf() {
/*  83 */               return false;
/*     */             }
/*     */           };
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
/*  96 */         childNodes.add(childNode);
/*     */       } 
/*     */ 
/*     */       
/* 100 */       for (Item childItem : didl.getItems()) {
/* 101 */         DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childItem)
/*     */           {
/*     */             public boolean isLeaf() {
/* 104 */               return true;
/*     */             }
/*     */           };
/* 107 */         childNodes.add(childNode);
/*     */       }
/*     */     
/* 110 */     } catch (Exception ex) {
/* 111 */       log.fine("Creating DIDL tree nodes failed: " + ex);
/* 112 */       actionInvocation.setFailure(new ActionException(ErrorCode.ACTION_FAILED, "Can't create tree child nodes: " + ex, ex));
/*     */ 
/*     */       
/* 115 */       failure(actionInvocation, null);
/*     */     } 
/*     */     
/* 118 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 120 */             ContentBrowseActionCallback.this.updateTreeModel(childNodes);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void updateStatus(final Browse.Status status) {
/* 126 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 128 */             ContentBrowseActionCallback.this.updateStatusUI(status, ContentBrowseActionCallback.this.treeNode, ContentBrowseActionCallback.this.treeModel);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void failure(ActionInvocation invocation, UpnpResponse operation, final String defaultMsg) {
/* 135 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 137 */             ContentBrowseActionCallback.this.failureUI(defaultMsg);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected void updateTreeModel(List<DefaultMutableTreeNode> childNodes) {
/* 143 */     log.fine("Adding nodes to tree: " + childNodes.size());
/*     */     
/* 145 */     removeChildren();
/*     */ 
/*     */     
/* 148 */     for (DefaultMutableTreeNode childNode : childNodes) {
/* 149 */       insertChild(childNode);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void removeChildren() {
/* 154 */     this.treeNode.removeAllChildren();
/* 155 */     this.treeModel.nodeStructureChanged(this.treeNode);
/*     */   }
/*     */   
/*     */   protected void insertChild(MutableTreeNode childNode) {
/* 159 */     int index = (this.treeNode.getChildCount() <= 0) ? 0 : this.treeNode.getChildCount();
/* 160 */     this.treeModel.insertNodeInto(childNode, this.treeNode, index);
/*     */   }
/*     */   
/*     */   public abstract void updateStatusUI(Browse.Status paramStatus, DefaultMutableTreeNode paramDefaultMutableTreeNode, DefaultTreeModel paramDefaultTreeModel);
/*     */   
/*     */   public abstract void failureUI(String paramString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirector\\ui\ContentBrowseActionCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */