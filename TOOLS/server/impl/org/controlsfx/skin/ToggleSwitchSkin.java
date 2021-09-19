/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.css.converters.SizeConverter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javafx.animation.TranslateTransition;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.css.CssMetaData;
/*     */ import javafx.css.StyleConverter;
/*     */ import javafx.css.Styleable;
/*     */ import javafx.css.StyleableDoubleProperty;
/*     */ import javafx.css.StyleableProperty;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.util.Duration;
/*     */ import org.controlsfx.control.ToggleSwitch;
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
/*     */ public class ToggleSwitchSkin
/*     */   extends SkinBase<ToggleSwitch>
/*     */ {
/*     */   private final StackPane thumb;
/*     */   private final StackPane thumbArea;
/*     */   private final Label label;
/*     */   private final StackPane labelContainer;
/*     */   private final TranslateTransition transition;
/*     */   private DoubleProperty thumbMoveAnimationTime;
/*     */   
/*     */   public ToggleSwitchSkin(ToggleSwitch control) {
/*  66 */     super((Control)control);
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
/* 123 */     this.thumbMoveAnimationTime = null; this.thumb = new StackPane(); this.thumbArea = new StackPane(); this.label = new Label(); this.labelContainer = new StackPane(); this.transition = new TranslateTransition(Duration.millis(getThumbMoveAnimationTime()), (Node)this.thumb); this.label.textProperty().bind((ObservableValue)control.textProperty()); getChildren().addAll((Object[])new Node[] { (Node)this.labelContainer, (Node)this.thumbArea, (Node)this.thumb }); this.labelContainer.getChildren().addAll((Object[])new Node[] { (Node)this.label }); StackPane.setAlignment((Node)this.label, Pos.CENTER_LEFT); this.thumb.getStyleClass().setAll((Object[])new String[] { "thumb" }); this.thumbArea.getStyleClass().setAll((Object[])new String[] { "thumb-area" }); this.thumbArea.setOnMouseReleased(event -> mousePressedOnToggleSwitch(control)); this.thumb.setOnMouseReleased(event -> mousePressedOnToggleSwitch(control)); control.selectedProperty().addListener((observable, oldValue, newValue) -> { if (newValue.booleanValue() != oldValue.booleanValue())
/*     */             selectedStateChanged();  });
/*     */   } private void selectedStateChanged() { if (this.transition != null)
/* 126 */       this.transition.stop();  double thumbAreaWidth = snapSize(this.thumbArea.prefWidth(-1.0D)); double thumbWidth = snapSize(this.thumb.prefWidth(-1.0D)); double distance = thumbAreaWidth - thumbWidth; if (!((ToggleSwitch)getSkinnable()).isSelected()) { this.thumb.setLayoutX(this.thumbArea.getLayoutX()); this.transition.setFromX(distance); this.transition.setToX(0.0D); } else { this.thumb.setTranslateX(this.thumbArea.getLayoutX()); this.transition.setFromX(0.0D); this.transition.setToX(distance); }  this.transition.setCycleCount(1); this.transition.play(); } private DoubleProperty thumbMoveAnimationTimeProperty() { if (this.thumbMoveAnimationTime == null) {
/* 127 */       this.thumbMoveAnimationTime = (DoubleProperty)new StyleableDoubleProperty(200.0D)
/*     */         {
/*     */           public Object getBean()
/*     */           {
/* 131 */             return ToggleSwitchSkin.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public String getName() {
/* 136 */             return "thumbMoveAnimationTime";
/*     */           }
/*     */ 
/*     */           
/*     */           public CssMetaData<ToggleSwitch, Number> getCssMetaData() {
/* 141 */             return ToggleSwitchSkin.THUMB_MOVE_ANIMATION_TIME;
/*     */           }
/*     */         };
/*     */     }
/* 145 */     return this.thumbMoveAnimationTime; }
/*     */    private void mousePressedOnToggleSwitch(ToggleSwitch toggleSwitch) {
/*     */     toggleSwitch.setSelected(!toggleSwitch.isSelected());
/*     */   } private double getThumbMoveAnimationTime() {
/* 149 */     return (this.thumbMoveAnimationTime == null) ? 200.0D : this.thumbMoveAnimationTime.get();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
/* 154 */     ToggleSwitch toggleSwitch = (ToggleSwitch)getSkinnable();
/* 155 */     double thumbWidth = snapSize(this.thumb.prefWidth(-1.0D));
/* 156 */     double thumbHeight = snapSize(this.thumb.prefHeight(-1.0D));
/* 157 */     this.thumb.resize(thumbWidth, thumbHeight);
/*     */     
/* 159 */     if (this.transition != null) {
/* 160 */       this.transition.stop();
/*     */     }
/* 162 */     this.thumb.setTranslateX(0.0D);
/*     */     
/* 164 */     double thumbAreaY = snapPosition(contentY);
/* 165 */     double thumbAreaWidth = snapSize(this.thumbArea.prefWidth(-1.0D));
/* 166 */     double thumbAreaHeight = snapSize(this.thumbArea.prefHeight(-1.0D));
/*     */     
/* 168 */     this.thumbArea.resize(thumbAreaWidth, thumbAreaHeight);
/* 169 */     this.thumbArea.setLayoutX(contentWidth - thumbAreaWidth);
/* 170 */     this.thumbArea.setLayoutY(thumbAreaY);
/*     */     
/* 172 */     this.labelContainer.resize(contentWidth - thumbAreaWidth, thumbAreaHeight);
/* 173 */     this.labelContainer.setLayoutY(thumbAreaY);
/*     */     
/* 175 */     if (!toggleSwitch.isSelected()) {
/* 176 */       this.thumb.setLayoutX(this.thumbArea.getLayoutX());
/*     */     } else {
/* 178 */       this.thumb.setLayoutX(this.thumbArea.getLayoutX() + thumbAreaWidth - thumbWidth);
/* 179 */     }  this.thumb.setLayoutY(thumbAreaY + (thumbAreaHeight - thumbHeight) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 184 */     return leftInset + this.label.prefWidth(-1.0D) + this.thumbArea.prefWidth(-1.0D) + rightInset;
/*     */   }
/*     */   
/*     */   protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 188 */     return topInset + Math.max(this.thumb.prefHeight(-1.0D), this.label.prefHeight(-1.0D)) + bottomInset;
/*     */   }
/*     */   
/*     */   protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 192 */     return leftInset + this.label.prefWidth(-1.0D) + 20.0D + this.thumbArea.prefWidth(-1.0D) + rightInset;
/*     */   }
/*     */   
/*     */   protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 196 */     return topInset + Math.max(this.thumb.prefHeight(-1.0D), this.label.prefHeight(-1.0D)) + bottomInset;
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 201 */     return ((ToggleSwitch)getSkinnable()).prefWidth(height);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 206 */     return ((ToggleSwitch)getSkinnable()).prefHeight(width);
/*     */   }
/*     */   
/* 209 */   private static final CssMetaData<ToggleSwitch, Number> THUMB_MOVE_ANIMATION_TIME = new CssMetaData<ToggleSwitch, Number>("-thumb-move-animation-time", 
/*     */       
/* 211 */       SizeConverter.getInstance(), Integer.valueOf(200))
/*     */     {
/*     */       public boolean isSettable(ToggleSwitch toggleSwitch)
/*     */       {
/* 215 */         ToggleSwitchSkin skin = (ToggleSwitchSkin)toggleSwitch.getSkin();
/* 216 */         return (skin.thumbMoveAnimationTime == null || 
/* 217 */           !skin.thumbMoveAnimationTime.isBound());
/*     */       }
/*     */ 
/*     */       
/*     */       public StyleableProperty<Number> getStyleableProperty(ToggleSwitch toggleSwitch) {
/* 222 */         ToggleSwitchSkin skin = (ToggleSwitchSkin)toggleSwitch.getSkin();
/* 223 */         return (StyleableProperty<Number>)skin.thumbMoveAnimationTimeProperty();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
/*     */   
/*     */   static {
/* 231 */     List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(SkinBase.getClassCssMetaData());
/* 232 */     styleables.add(THUMB_MOVE_ANIMATION_TIME);
/* 233 */     STYLEABLES = Collections.unmodifiableList(styleables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
/* 241 */     return STYLEABLES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
/* 249 */     return getClassCssMetaData();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\ToggleSwitchSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */