/*     */ package impl.org.controlsfx;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Group;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.Parent;
/*     */ import javafx.scene.Scene;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.layout.Pane;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImplUtils
/*     */ {
/*     */   public static void injectAsRootPane(Scene scene, Parent injectedParent, boolean useReflection) {
/*  51 */     Parent originalParent = scene.getRoot();
/*  52 */     scene.setRoot(injectedParent);
/*     */     
/*  54 */     if (originalParent != null) {
/*  55 */       getChildren(injectedParent, useReflection).add(0, originalParent);
/*     */ 
/*     */ 
/*     */       
/*  59 */       injectedParent.getProperties().putAll((Map)originalParent.getProperties());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void injectPane(Parent parent, Parent injectedParent, boolean useReflection) {
/*  67 */     if (parent == null) {
/*  68 */       throw new IllegalArgumentException("parent can not be null");
/*     */     }
/*     */     
/*  71 */     List<Node> ownerParentChildren = getChildren(parent.getParent(), useReflection);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     int ownerPos = ownerParentChildren.indexOf(parent);
/*  78 */     ownerParentChildren.remove(ownerPos);
/*  79 */     ownerParentChildren.add(ownerPos, injectedParent);
/*     */ 
/*     */     
/*  82 */     getChildren(injectedParent, useReflection).add(0, parent);
/*     */ 
/*     */ 
/*     */     
/*  86 */     injectedParent.getProperties().putAll((Map)parent.getProperties());
/*     */   }
/*     */   
/*     */   public static void stripRootPane(Scene scene, Parent originalParent, boolean useReflection) {
/*  90 */     Parent oldParent = scene.getRoot();
/*  91 */     getChildren(oldParent, useReflection).remove(originalParent);
/*  92 */     originalParent.getStyleClass().remove("root");
/*  93 */     scene.setRoot(originalParent);
/*     */   }
/*     */   
/*     */   public static List<Node> getChildren(Node n, boolean useReflection) {
/*  97 */     return (n instanceof Parent) ? getChildren((Parent)n, useReflection) : Collections.<Node>emptyList();
/*     */   }
/*     */   
/*     */   public static List<Node> getChildren(Parent p, boolean useReflection) {
/* 101 */     ObservableList<Node> children = null;
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (p instanceof Pane) {
/*     */ 
/*     */       
/* 108 */       children = ((Pane)p).getChildren();
/* 109 */     } else if (p instanceof Group) {
/* 110 */       children = ((Group)p).getChildren();
/* 111 */     } else if (p instanceof Control) {
/* 112 */       Control c = (Control)p;
/* 113 */       Skin<?> s = c.getSkin();
/* 114 */       children = (s instanceof SkinBase) ? ((SkinBase)s).getChildren() : getChildrenReflectively(p);
/* 115 */     } else if (useReflection) {
/*     */       
/* 117 */       children = getChildrenReflectively(p);
/*     */     } 
/*     */     
/* 120 */     if (children == null) {
/* 121 */       throw new RuntimeException("Unable to get children for Parent of type " + p.getClass() + ". useReflection is set to " + useReflection);
/*     */     }
/*     */ 
/*     */     
/* 125 */     return (children == null) ? (List<Node>)FXCollections.emptyObservableList() : (List<Node>)children;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ObservableList<Node> getChildrenReflectively(Parent p) {
/* 130 */     ObservableList<Node> children = null;
/*     */     
/*     */     try {
/* 133 */       Method getChildrenMethod = Parent.class.getDeclaredMethod("getChildren", new Class[0]);
/*     */       
/* 135 */       if (getChildrenMethod != null) {
/* 136 */         if (!getChildrenMethod.isAccessible()) {
/* 137 */           getChildrenMethod.setAccessible(true);
/*     */         }
/* 139 */         children = (ObservableList<Node>)getChildrenMethod.invoke(p, new Object[0]);
/*     */       }
/*     */     
/*     */     }
/* 143 */     catch (ReflectiveOperationException|IllegalArgumentException e) {
/* 144 */       throw new RuntimeException("Unable to get children for Parent of type " + p.getClass(), e);
/*     */     } 
/*     */     
/* 147 */     return children;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\ImplUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */