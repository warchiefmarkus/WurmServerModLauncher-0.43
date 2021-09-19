/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import impl.org.controlsfx.behavior.RatingBehavior;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.geometry.Point2D;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.HBox;
/*     */ import javafx.scene.layout.Pane;
/*     */ import javafx.scene.layout.Region;
/*     */ import javafx.scene.layout.VBox;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import org.controlsfx.control.Rating;
/*     */ import org.controlsfx.tools.Utils;
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
/*     */ public class RatingSkin
/*     */   extends BehaviorSkinBase<Rating, RatingBehavior>
/*     */ {
/*     */   private static final String STRONG = "strong";
/*     */   private boolean updateOnHover;
/*     */   private boolean partialRating;
/*     */   private Pane backgroundContainer;
/*     */   private Pane foregroundContainer;
/*  76 */   private double rating = -1.0D;
/*     */   
/*     */   private Rectangle forgroundClipRect;
/*     */   
/*  80 */   private final EventHandler<MouseEvent> mouseMoveHandler = new EventHandler<MouseEvent>()
/*     */     {
/*     */ 
/*     */       
/*     */       public void handle(MouseEvent event)
/*     */       {
/*  86 */         if (RatingSkin.this.updateOnHover) {
/*  87 */           RatingSkin.this.updateRatingFromMouseEvent(event);
/*     */         }
/*     */       }
/*     */     };
/*     */   
/*  92 */   private final EventHandler<MouseEvent> mouseClickHandler = new EventHandler<MouseEvent>()
/*     */     {
/*     */ 
/*     */       
/*     */       public void handle(MouseEvent event)
/*     */       {
/*  98 */         if (!RatingSkin.this.updateOnHover) {
/*  99 */           RatingSkin.this.updateRatingFromMouseEvent(event);
/*     */         }
/*     */       }
/*     */     };
/*     */   
/*     */   private void updateRatingFromMouseEvent(MouseEvent event) {
/* 105 */     Rating control = (Rating)getSkinnable();
/* 106 */     if (!control.ratingProperty().isBound()) {
/* 107 */       Point2D mouseLocation = new Point2D(event.getSceneX(), event.getSceneY());
/* 108 */       control.setRating(calculateRating(mouseLocation));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RatingSkin(Rating control) {
/* 119 */     super((Control)control, (BehaviorBase)new RatingBehavior(control));
/*     */     
/* 121 */     this.updateOnHover = control.isUpdateOnHover();
/* 122 */     this.partialRating = control.isPartialRating();
/*     */ 
/*     */     
/* 125 */     recreateButtons();
/* 126 */     updateRating();
/*     */ 
/*     */     
/* 129 */     registerChangeListener((ObservableValue)control.ratingProperty(), "RATING");
/* 130 */     registerChangeListener((ObservableValue)control.maxProperty(), "MAX");
/* 131 */     registerChangeListener((ObservableValue)control.orientationProperty(), "ORIENTATION");
/* 132 */     registerChangeListener((ObservableValue)control.updateOnHoverProperty(), "UPDATE_ON_HOVER");
/* 133 */     registerChangeListener((ObservableValue)control.partialRatingProperty(), "PARTIAL_RATING");
/*     */     
/* 135 */     registerChangeListener((ObservableValue)control.boundsInLocalProperty(), "BOUNDS");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleControlPropertyChanged(String p) {
/* 147 */     super.handleControlPropertyChanged(p);
/*     */     
/* 149 */     if (p == "RATING") {
/* 150 */       updateRating();
/* 151 */     } else if (p == "MAX") {
/* 152 */       recreateButtons();
/* 153 */     } else if (p == "ORIENTATION") {
/* 154 */       recreateButtons();
/* 155 */     } else if (p == "PARTIAL_RATING") {
/* 156 */       this.partialRating = ((Rating)getSkinnable()).isPartialRating();
/* 157 */       recreateButtons();
/* 158 */     } else if (p == "UPDATE_ON_HOVER") {
/* 159 */       this.updateOnHover = ((Rating)getSkinnable()).isUpdateOnHover();
/* 160 */       recreateButtons();
/* 161 */     } else if (p == "BOUNDS" && 
/* 162 */       this.partialRating) {
/* 163 */       updateClip();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void recreateButtons() {
/* 169 */     this.backgroundContainer = null;
/* 170 */     this.foregroundContainer = null;
/*     */     
/* 172 */     this.backgroundContainer = isVertical() ? (Pane)new VBox() : (Pane)new HBox();
/* 173 */     this.backgroundContainer.getStyleClass().add("container");
/* 174 */     getChildren().setAll((Object[])new Node[] { (Node)this.backgroundContainer });
/*     */     
/* 176 */     if (this.updateOnHover || this.partialRating) {
/* 177 */       this.foregroundContainer = isVertical() ? (Pane)new VBox() : (Pane)new HBox();
/* 178 */       this.foregroundContainer.getStyleClass().add("container");
/* 179 */       this.foregroundContainer.setMouseTransparent(true);
/* 180 */       getChildren().add(this.foregroundContainer);
/*     */       
/* 182 */       this.forgroundClipRect = new Rectangle();
/* 183 */       this.foregroundContainer.setClip((Node)this.forgroundClipRect);
/*     */     } 
/*     */ 
/*     */     
/* 187 */     for (int index = 0; index <= ((Rating)getSkinnable()).getMax(); index++) {
/* 188 */       Node backgroundNode = createButton();
/*     */       
/* 190 */       if (index > 0) {
/* 191 */         if (isVertical()) {
/* 192 */           this.backgroundContainer.getChildren().add(0, backgroundNode);
/*     */         } else {
/* 194 */           this.backgroundContainer.getChildren().add(backgroundNode);
/*     */         } 
/*     */         
/* 197 */         if (this.partialRating) {
/* 198 */           Node foregroundNode = createButton();
/* 199 */           foregroundNode.getStyleClass().add("strong");
/* 200 */           foregroundNode.setMouseTransparent(true);
/*     */           
/* 202 */           if (isVertical()) {
/* 203 */             this.foregroundContainer.getChildren().add(0, foregroundNode);
/*     */           } else {
/* 205 */             this.foregroundContainer.getChildren().add(foregroundNode);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     updateRating();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double calculateRating(Point2D sceneLocation) {
/* 218 */     Point2D b = this.backgroundContainer.sceneToLocal(sceneLocation);
/*     */     
/* 220 */     double x = b.getX();
/* 221 */     double y = b.getY();
/*     */     
/* 223 */     Rating control = (Rating)getSkinnable();
/*     */     
/* 225 */     int max = control.getMax();
/* 226 */     double w = control.getWidth() - snappedLeftInset() + snappedRightInset();
/* 227 */     double h = control.getHeight() - snappedTopInset() + snappedBottomInset();
/*     */     
/* 229 */     double newRating = -1.0D;
/*     */     
/* 231 */     if (isVertical()) {
/* 232 */       newRating = (h - y) / h * max;
/*     */     } else {
/* 234 */       newRating = x / w * max;
/*     */     } 
/*     */     
/* 237 */     if (!this.partialRating) {
/* 238 */       newRating = Utils.clamp(1.0D, Math.ceil(newRating), control.getMax());
/*     */     }
/*     */     
/* 241 */     return newRating;
/*     */   }
/*     */   
/*     */   private void updateClip() {
/* 245 */     Rating control = (Rating)getSkinnable();
/* 246 */     double h = control.getHeight() - snappedTopInset() + snappedBottomInset();
/* 247 */     double w = control.getWidth() - snappedLeftInset() + snappedRightInset();
/*     */     
/* 249 */     if (isVertical()) {
/* 250 */       double y = h * this.rating / control.getMax();
/* 251 */       this.forgroundClipRect.relocate(0.0D, h - y);
/* 252 */       this.forgroundClipRect.setWidth(control.getWidth());
/* 253 */       this.forgroundClipRect.setHeight(y);
/*     */     } else {
/* 255 */       double x = w * this.rating / control.getMax();
/* 256 */       this.forgroundClipRect.setWidth(x);
/* 257 */       this.forgroundClipRect.setHeight(control.getHeight());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Node createButton() {
/* 269 */     Region btn = new Region();
/* 270 */     btn.getStyleClass().add("button");
/*     */     
/* 272 */     btn.setOnMouseMoved(this.mouseMoveHandler);
/* 273 */     btn.setOnMouseClicked(this.mouseClickHandler);
/* 274 */     return (Node)btn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateRating() {
/* 283 */     double newRating = ((Rating)getSkinnable()).getRating();
/*     */     
/* 285 */     if (newRating == this.rating)
/*     */       return; 
/* 287 */     this.rating = Utils.clamp(0.0D, newRating, ((Rating)getSkinnable()).getMax());
/*     */     
/* 289 */     if (this.partialRating) {
/* 290 */       updateClip();
/*     */     } else {
/* 292 */       updateButtonStyles();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateButtonStyles() {
/* 297 */     int max = ((Rating)getSkinnable()).getMax();
/*     */ 
/*     */ 
/*     */     
/* 301 */     List<Node> buttons = new ArrayList<>((Collection<? extends Node>)this.backgroundContainer.getChildren());
/* 302 */     if (isVertical()) {
/* 303 */       Collections.reverse(buttons);
/*     */     }
/*     */     
/* 306 */     for (int i = 0; i < max; i++) {
/* 307 */       Node button = buttons.get(i);
/*     */       
/* 309 */       ObservableList<String> observableList = button.getStyleClass();
/* 310 */       boolean containsStrong = observableList.contains("strong");
/*     */       
/* 312 */       if (i < this.rating) {
/* 313 */         if (!containsStrong) {
/* 314 */           observableList.add("strong");
/*     */         }
/* 316 */       } else if (containsStrong) {
/* 317 */         observableList.remove("strong");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isVertical() {
/* 323 */     return (((Rating)getSkinnable()).getOrientation() == Orientation.VERTICAL);
/*     */   }
/*     */   
/*     */   protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
/* 327 */     return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\RatingSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */