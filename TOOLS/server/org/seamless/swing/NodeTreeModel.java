/*    */ package org.seamless.swing;
/*    */ 
/*    */ import javax.swing.event.TreeModelListener;
/*    */ import javax.swing.tree.TreeModel;
/*    */ import javax.swing.tree.TreePath;
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
/*    */ public class NodeTreeModel
/*    */   implements TreeModel
/*    */ {
/*    */   private Node rootNode;
/*    */   private Node selectedNode;
/*    */   
/*    */   public NodeTreeModel(Node rootNode) {
/* 33 */     this.rootNode = rootNode;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getRoot() {
/* 39 */     return this.rootNode;
/*    */   }
/*    */   
/*    */   public boolean isLeaf(Object object) {
/* 43 */     Node node = (Node)object;
/* 44 */     boolean isLeaf = (node.getChildren().size() == 0);
/* 45 */     return isLeaf;
/*    */   }
/*    */   
/*    */   public int getChildCount(Object parent) {
/* 49 */     Node node = (Node)parent;
/* 50 */     return node.getChildren().size();
/*    */   }
/*    */   
/*    */   public Object getChild(Object parent, int i) {
/* 54 */     Node node = (Node)parent;
/* 55 */     Object child = node.getChildren().get(i);
/* 56 */     return child;
/*    */   }
/*    */   
/*    */   public int getIndexOfChild(Object parent, Object child) {
/* 60 */     if (parent == null || child == null) return -1; 
/* 61 */     Node node = (Node)parent;
/* 62 */     int index = node.getChildren().indexOf(child);
/* 63 */     return index;
/*    */   }
/*    */   
/*    */   public void valueForPathChanged(TreePath path, Object newvalue) {}
/*    */   
/*    */   public void addTreeModelListener(TreeModelListener l) {}
/*    */   
/*    */   public void removeTreeModelListener(TreeModelListener l) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\NodeTreeModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */