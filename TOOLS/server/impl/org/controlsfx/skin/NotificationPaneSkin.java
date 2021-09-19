/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import com.sun.javafx.scene.traversal.ParentTraversalEngine;
/*     */ import java.util.Collections;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.Parent;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import org.controlsfx.control.NotificationPane;
/*     */ import org.controlsfx.control.action.Action;
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
/*     */ public class NotificationPaneSkin
/*     */   extends BehaviorSkinBase<NotificationPane, BehaviorBase<NotificationPane>>
/*     */ {
/*     */   private NotificationBar notificationBar;
/*     */   private Node content;
/*  47 */   private Rectangle clip = new Rectangle();
/*     */   
/*     */   public NotificationPaneSkin(final NotificationPane control) {
/*  50 */     super((Control)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*     */     
/*  52 */     this.notificationBar = new NotificationBar() {
/*     */         public void requestContainerLayout() {
/*  54 */           control.requestLayout();
/*     */         }
/*     */         
/*     */         public String getText() {
/*  58 */           return control.getText();
/*     */         }
/*     */         
/*     */         public Node getGraphic() {
/*  62 */           return control.getGraphic();
/*     */         }
/*     */         
/*     */         public ObservableList<Action> getActions() {
/*  66 */           return control.getActions();
/*     */         }
/*     */         
/*     */         public boolean isShowing() {
/*  70 */           return control.isShowing();
/*     */         }
/*     */         
/*     */         public boolean isShowFromTop() {
/*  74 */           return control.isShowFromTop();
/*     */         }
/*     */         
/*     */         public void hide() {
/*  78 */           control.hide();
/*     */         }
/*     */         
/*     */         public boolean isCloseButtonVisible() {
/*  82 */           return control.isCloseButtonVisible();
/*     */         }
/*     */         
/*     */         public double getContainerHeight() {
/*  86 */           return control.getHeight();
/*     */         }
/*     */         
/*     */         public void relocateInParent(double x, double y) {
/*  90 */           NotificationPaneSkin.this.notificationBar.relocate(x, y);
/*     */         }
/*     */       };
/*     */     
/*  94 */     control.setClip((Node)this.clip);
/*  95 */     updateContent();
/*     */     
/*  97 */     registerChangeListener((ObservableValue)control.heightProperty(), "HEIGHT");
/*  98 */     registerChangeListener((ObservableValue)control.contentProperty(), "CONTENT");
/*  99 */     registerChangeListener((ObservableValue)control.textProperty(), "TEXT");
/* 100 */     registerChangeListener((ObservableValue)control.graphicProperty(), "GRAPHIC");
/* 101 */     registerChangeListener((ObservableValue)control.showingProperty(), "SHOWING");
/* 102 */     registerChangeListener((ObservableValue)control.showFromTopProperty(), "SHOW_FROM_TOP");
/* 103 */     registerChangeListener((ObservableValue)control.closeButtonVisibleProperty(), "CLOSE_BUTTON_VISIBLE");
/*     */ 
/*     */     
/* 106 */     ParentTraversalEngine engine = new ParentTraversalEngine((Parent)getSkinnable());
/* 107 */     ((NotificationPane)getSkinnable()).setImpl_traversalEngine(engine);
/* 108 */     engine.setOverriddenFocusTraversability(Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/* 112 */     super.handleControlPropertyChanged(p);
/*     */     
/* 114 */     if ("CONTENT".equals(p)) {
/* 115 */       updateContent();
/* 116 */     } else if ("TEXT".equals(p)) {
/* 117 */       this.notificationBar.label.setText(((NotificationPane)getSkinnable()).getText());
/* 118 */     } else if ("GRAPHIC".equals(p)) {
/* 119 */       this.notificationBar.label.setGraphic(((NotificationPane)getSkinnable()).getGraphic());
/* 120 */     } else if ("SHOWING".equals(p)) {
/* 121 */       if (((NotificationPane)getSkinnable()).isShowing()) {
/* 122 */         this.notificationBar.doShow();
/*     */       } else {
/* 124 */         this.notificationBar.doHide();
/*     */       } 
/* 126 */     } else if ("SHOW_FROM_TOP".equals(p)) {
/* 127 */       if (((NotificationPane)getSkinnable()).isShowing()) {
/* 128 */         ((NotificationPane)getSkinnable()).requestLayout();
/*     */       }
/* 130 */     } else if ("CLOSE_BUTTON_VISIBLE".equals(p)) {
/* 131 */       this.notificationBar.updatePane();
/* 132 */     } else if ("HEIGHT".equals(p)) {
/*     */       
/* 134 */       if (((NotificationPane)getSkinnable()).isShowing() && !((NotificationPane)getSkinnable()).isShowFromTop()) {
/* 135 */         this.notificationBar.requestLayout();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateContent() {
/* 141 */     if (this.content != null) {
/* 142 */       getChildren().remove(this.content);
/*     */     }
/*     */     
/* 145 */     this.content = ((NotificationPane)getSkinnable()).getContent();
/*     */     
/* 147 */     if (this.content == null) {
/* 148 */       getChildren().setAll((Object[])new Node[] { (Node)this.notificationBar });
/*     */     } else {
/* 150 */       getChildren().setAll((Object[])new Node[] { this.content, (Node)this.notificationBar });
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void layoutChildren(double x, double y, double w, double h) {
/* 155 */     double notificationBarHeight = this.notificationBar.prefHeight(w);
/*     */     
/* 157 */     this.notificationBar.resize(w, notificationBarHeight);
/*     */ 
/*     */     
/* 160 */     if (this.content != null) {
/* 161 */       this.content.resizeRelocate(x, y, w, h);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 166 */     this.clip.setX(x);
/* 167 */     this.clip.setY(y);
/* 168 */     this.clip.setWidth(w);
/* 169 */     this.clip.setHeight(h);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 174 */     return (this.content == null) ? 0.0D : this.content.minWidth(height);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 179 */     return (this.content == null) ? 0.0D : this.content.minHeight(width);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 184 */     return (this.content == null) ? 0.0D : this.content.prefWidth(height);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 189 */     return (this.content == null) ? 0.0D : this.content.prefHeight(width);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 194 */     return (this.content == null) ? 0.0D : this.content.maxWidth(height);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 199 */     return (this.content == null) ? 0.0D : this.content.maxHeight(width);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\NotificationPaneSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */