/*    */ package org.fourthline.cling.support.contentdirectory.ui;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JTree;
/*    */ import javax.swing.tree.DefaultMutableTreeNode;
/*    */ import javax.swing.tree.DefaultTreeCellRenderer;
/*    */ import org.fourthline.cling.support.model.DIDLObject;
/*    */ import org.fourthline.cling.support.model.container.Container;
/*    */ import org.fourthline.cling.support.model.item.Item;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContentTreeCellRenderer
/*    */   extends DefaultTreeCellRenderer
/*    */ {
/*    */   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
/* 42 */     super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
/*    */     
/* 44 */     DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
/*    */     
/* 46 */     if (node.getUserObject() instanceof Container) {
/*    */       
/* 48 */       Container container = (Container)node.getUserObject();
/* 49 */       setText(container.getTitle());
/* 50 */       setIcon(expanded ? getContainerOpenIcon() : getContainerClosedIcon());
/*    */     }
/* 52 */     else if (node.getUserObject() instanceof Item) {
/*    */       
/* 54 */       Item item = (Item)node.getUserObject();
/* 55 */       setText(item.getTitle());
/*    */       
/* 57 */       DIDLObject.Class upnpClass = item.getClazz();
/* 58 */       setIcon(getItemIcon(item, (upnpClass != null) ? upnpClass.getValue() : null));
/*    */     }
/* 60 */     else if (node.getUserObject() instanceof String) {
/* 61 */       setIcon(getInfoIcon());
/*    */     } 
/*    */     
/* 64 */     onCreate();
/* 65 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onCreate() {}
/*    */ 
/*    */   
/*    */   protected Icon getContainerOpenIcon() {
/* 73 */     return null;
/*    */   }
/*    */   
/*    */   protected Icon getContainerClosedIcon() {
/* 77 */     return null;
/*    */   }
/*    */   
/*    */   protected Icon getItemIcon(Item item, String upnpClass) {
/* 81 */     return null;
/*    */   }
/*    */   
/*    */   protected Icon getInfoIcon() {
/* 85 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirector\\ui\ContentTreeCellRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */