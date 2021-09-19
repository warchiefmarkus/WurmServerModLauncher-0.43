/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import java.util.Collections;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.ToggleButton;
/*     */ import javafx.scene.control.ToggleGroup;
/*     */ import javafx.scene.layout.HBox;
/*     */ import org.controlsfx.control.SegmentedButton;
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
/*     */ public class SegmentedButtonSkin
/*     */   extends BehaviorSkinBase<SegmentedButton, BehaviorBase<SegmentedButton>>
/*     */ {
/*     */   private static final String ONLY_BUTTON = "only-button";
/*     */   private static final String LEFT_PILL = "left-pill";
/*     */   private static final String CENTER_PILL = "center-pill";
/*     */   private static final String RIGHT_PILL = "right-pill";
/*     */   private final HBox container;
/*     */   
/*     */   public SegmentedButtonSkin(SegmentedButton control) {
/*  54 */     super((Control)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*     */     
/*  56 */     this.container = new HBox();
/*     */     
/*  58 */     getChildren().add(this.container);
/*     */     
/*  60 */     updateButtons();
/*  61 */     getButtons().addListener(new InvalidationListener() {
/*     */           public void invalidated(Observable observable) {
/*  63 */             SegmentedButtonSkin.this.updateButtons();
/*     */           }
/*     */         });
/*     */     
/*  67 */     control.toggleGroupProperty().addListener((observable, oldValue, newValue) -> getButtons().forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObservableList<ToggleButton> getButtons() {
/*  75 */     return ((SegmentedButton)getSkinnable()).getButtons();
/*     */   }
/*     */   
/*     */   private void updateButtons() {
/*  79 */     ObservableList<ToggleButton> buttons = getButtons();
/*  80 */     ToggleGroup group = ((SegmentedButton)getSkinnable()).getToggleGroup();
/*     */     
/*  82 */     this.container.getChildren().clear();
/*     */     
/*  84 */     for (int i = 0; i < getButtons().size(); i++) {
/*  85 */       ToggleButton t = (ToggleButton)buttons.get(i);
/*     */       
/*  87 */       if (group != null) {
/*  88 */         t.setToggleGroup(group);
/*     */       }
/*     */       
/*  91 */       t.getStyleClass().removeAll((Object[])new String[] { "only-button", "left-pill", "center-pill", "right-pill" });
/*  92 */       this.container.getChildren().add(t);
/*     */       
/*  94 */       if (i == buttons.size() - 1) {
/*  95 */         if (i == 0) {
/*  96 */           t.getStyleClass().add("only-button");
/*     */         } else {
/*  98 */           t.getStyleClass().add("right-pill");
/*     */         } 
/* 100 */       } else if (i == 0) {
/* 101 */         t.getStyleClass().add("left-pill");
/*     */       } else {
/* 103 */         t.getStyleClass().add("center-pill");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 110 */     return ((SegmentedButton)getSkinnable()).prefWidth(height);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 115 */     return ((SegmentedButton)getSkinnable()).prefHeight(width);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\SegmentedButtonSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */