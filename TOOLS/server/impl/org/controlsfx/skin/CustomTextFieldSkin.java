/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
/*     */ import com.sun.javafx.scene.control.skin.TextFieldSkin;
/*     */ import com.sun.javafx.scene.text.HitInfo;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.css.PseudoClass;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.TextField;
/*     */ import javafx.scene.layout.StackPane;
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
/*     */ public abstract class CustomTextFieldSkin
/*     */   extends TextFieldSkin
/*     */ {
/*  42 */   private static final PseudoClass HAS_NO_SIDE_NODE = PseudoClass.getPseudoClass("no-side-nodes");
/*  43 */   private static final PseudoClass HAS_LEFT_NODE = PseudoClass.getPseudoClass("left-node-visible");
/*  44 */   private static final PseudoClass HAS_RIGHT_NODE = PseudoClass.getPseudoClass("right-node-visible");
/*     */   
/*     */   private Node left;
/*     */   
/*     */   private StackPane leftPane;
/*     */   private Node right;
/*     */   private StackPane rightPane;
/*     */   private final TextField control;
/*     */   
/*     */   public CustomTextFieldSkin(TextField control) {
/*  54 */     super(control, new TextFieldBehavior(control));
/*     */     
/*  56 */     this.control = control;
/*  57 */     updateChildren();
/*     */     
/*  59 */     registerChangeListener((ObservableValue)leftProperty(), "LEFT_NODE");
/*  60 */     registerChangeListener((ObservableValue)rightProperty(), "RIGHT_NODE");
/*  61 */     registerChangeListener((ObservableValue)control.focusedProperty(), "FOCUSED");
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract ObjectProperty<Node> leftProperty();
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/*  68 */     super.handleControlPropertyChanged(p);
/*     */     
/*  70 */     if (p == "LEFT_NODE" || p == "RIGHT_NODE")
/*  71 */       updateChildren(); 
/*     */   }
/*     */   public abstract ObjectProperty<Node> rightProperty();
/*     */   
/*     */   private void updateChildren() {
/*  76 */     Node newLeft = (Node)leftProperty().get();
/*  77 */     if (newLeft != null) {
/*  78 */       getChildren().remove(this.leftPane);
/*  79 */       this.leftPane = new StackPane(new Node[] { newLeft });
/*  80 */       this.leftPane.setAlignment(Pos.CENTER_LEFT);
/*  81 */       this.leftPane.getStyleClass().add("left-pane");
/*  82 */       getChildren().add(this.leftPane);
/*  83 */       this.left = newLeft;
/*     */     } 
/*     */     
/*  86 */     Node newRight = (Node)rightProperty().get();
/*  87 */     if (newRight != null) {
/*  88 */       getChildren().remove(this.rightPane);
/*  89 */       this.rightPane = new StackPane(new Node[] { newRight });
/*  90 */       this.rightPane.setAlignment(Pos.CENTER_RIGHT);
/*  91 */       this.rightPane.getStyleClass().add("right-pane");
/*  92 */       getChildren().add(this.rightPane);
/*  93 */       this.right = newRight;
/*     */     } 
/*     */     
/*  96 */     this.control.pseudoClassStateChanged(HAS_LEFT_NODE, (this.left != null));
/*  97 */     this.control.pseudoClassStateChanged(HAS_RIGHT_NODE, (this.right != null));
/*  98 */     this.control.pseudoClassStateChanged(HAS_NO_SIDE_NODE, (this.left == null && this.right == null));
/*     */   }
/*     */   
/*     */   protected void layoutChildren(double x, double y, double w, double h) {
/* 102 */     double fullHeight = h + snappedTopInset() + snappedBottomInset();
/*     */     
/* 104 */     double leftWidth = (this.leftPane == null) ? 0.0D : snapSize(this.leftPane.prefWidth(fullHeight));
/* 105 */     double rightWidth = (this.rightPane == null) ? 0.0D : snapSize(this.rightPane.prefWidth(fullHeight));
/*     */     
/* 107 */     double textFieldStartX = snapPosition(x) + snapSize(leftWidth);
/* 108 */     double textFieldWidth = w - snapSize(leftWidth) - snapSize(rightWidth);
/*     */     
/* 110 */     super.layoutChildren(textFieldStartX, 0.0D, textFieldWidth, fullHeight);
/*     */     
/* 112 */     if (this.leftPane != null) {
/* 113 */       double leftStartX = 0.0D;
/* 114 */       this.leftPane.resizeRelocate(0.0D, 0.0D, leftWidth, fullHeight);
/*     */     } 
/*     */     
/* 117 */     if (this.rightPane != null) {
/* 118 */       double rightStartX = (this.rightPane == null) ? 0.0D : (w - rightWidth + snappedLeftInset());
/* 119 */       this.rightPane.resizeRelocate(rightStartX, 0.0D, rightWidth, fullHeight);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HitInfo getIndex(double x, double y) {
/* 130 */     double leftWidth = (this.leftPane == null) ? 0.0D : snapSize(this.leftPane.prefWidth(((TextField)getSkinnable()).getHeight()));
/* 131 */     return super.getIndex(x - leftWidth, y);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefWidth(double h, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 136 */     double pw = super.computePrefWidth(h, topInset, rightInset, bottomInset, leftInset);
/* 137 */     double leftWidth = (this.leftPane == null) ? 0.0D : snapSize(this.leftPane.prefWidth(h));
/* 138 */     double rightWidth = (this.rightPane == null) ? 0.0D : snapSize(this.rightPane.prefWidth(h));
/*     */     
/* 140 */     return pw + leftWidth + rightWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computePrefHeight(double w, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 145 */     double ph = super.computePrefHeight(w, topInset, rightInset, bottomInset, leftInset);
/* 146 */     double leftHeight = (this.leftPane == null) ? 0.0D : snapSize(this.leftPane.prefHeight(-1.0D));
/* 147 */     double rightHeight = (this.rightPane == null) ? 0.0D : snapSize(this.rightPane.prefHeight(-1.0D));
/*     */     
/* 149 */     return Math.max(ph, Math.max(leftHeight, rightHeight));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\CustomTextFieldSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */