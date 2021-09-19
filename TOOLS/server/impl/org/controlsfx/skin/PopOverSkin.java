/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.binding.Bindings;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.value.ObservableBooleanValue;
/*     */ import javafx.beans.value.ObservableNumberValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.geometry.Point2D;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Group;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ContentDisplay;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.Skinnable;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.BorderPane;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.scene.paint.Color;
/*     */ import javafx.scene.paint.Paint;
/*     */ import javafx.scene.shape.Circle;
/*     */ import javafx.scene.shape.HLineTo;
/*     */ import javafx.scene.shape.Line;
/*     */ import javafx.scene.shape.LineTo;
/*     */ import javafx.scene.shape.MoveTo;
/*     */ import javafx.scene.shape.Path;
/*     */ import javafx.scene.shape.PathElement;
/*     */ import javafx.scene.shape.QuadCurveTo;
/*     */ import javafx.scene.shape.VLineTo;
/*     */ import javafx.stage.Window;
/*     */ import org.controlsfx.control.PopOver;
/*     */ 
/*     */ public class PopOverSkin
/*     */   implements Skin<PopOver>
/*     */ {
/*     */   private static final String DETACHED_STYLE_CLASS = "detached";
/*     */   private double xOffset;
/*     */   private double yOffset;
/*     */   private boolean tornOff;
/*     */   private Label title;
/*     */   private Label closeIcon;
/*     */   private Path path;
/*     */   private Path clip;
/*     */   private BorderPane content;
/*     */   private StackPane titlePane;
/*     */   private StackPane stackPane;
/*     */   private Point2D dragStartLocation;
/*     */   private PopOver popOver;
/*     */   private MoveTo moveTo;
/*     */   private QuadCurveTo topCurveTo;
/*     */   private QuadCurveTo rightCurveTo;
/*     */   private QuadCurveTo bottomCurveTo;
/*     */   private QuadCurveTo leftCurveTo;
/*     */   private HLineTo lineBTop;
/*     */   private HLineTo lineETop;
/*     */   private HLineTo lineHTop;
/*     */   private HLineTo lineKTop;
/*     */   private LineTo lineCTop;
/*     */   private LineTo lineDTop;
/*     */   private LineTo lineFTop;
/*     */   private LineTo lineGTop;
/*     */   private LineTo lineITop;
/*     */   private LineTo lineJTop;
/*     */   private VLineTo lineBRight;
/*     */   private VLineTo lineERight;
/*     */   private VLineTo lineHRight;
/*     */   private VLineTo lineKRight;
/*     */   private LineTo lineCRight;
/*     */   private LineTo lineDRight;
/*     */   private LineTo lineFRight;
/*     */   private LineTo lineGRight;
/*     */   private LineTo lineIRight;
/*     */   private LineTo lineJRight;
/*     */   private HLineTo lineBBottom;
/*     */   private HLineTo lineEBottom;
/*     */   private HLineTo lineHBottom;
/*     */   private HLineTo lineKBottom;
/*     */   private LineTo lineCBottom;
/*     */   private LineTo lineDBottom;
/*     */   private LineTo lineFBottom;
/*     */   private LineTo lineGBottom;
/*     */   private LineTo lineIBottom;
/*     */   private LineTo lineJBottom;
/*     */   private VLineTo lineBLeft;
/*     */   private VLineTo lineELeft;
/*     */   private VLineTo lineHLeft;
/*     */   private VLineTo lineKLeft;
/*     */   private LineTo lineCLeft;
/*     */   private LineTo lineDLeft;
/*     */   private LineTo lineFLeft;
/*     */   private LineTo lineGLeft;
/*     */   private LineTo lineILeft;
/*     */   private LineTo lineJLeft;
/*     */   
/*     */   public PopOverSkin(PopOver popOver) {
/* 102 */     this.popOver = popOver;
/*     */     
/* 104 */     this.stackPane = popOver.getRoot();
/* 105 */     this.stackPane.setPickOnBounds(false);
/*     */     
/* 107 */     Bindings.bindContent((List)this.stackPane.getStyleClass(), popOver.getStyleClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     this.stackPane.minWidthProperty().bind(
/* 114 */         (ObservableValue)Bindings.add((ObservableNumberValue)Bindings.multiply(2, (ObservableNumberValue)popOver.arrowSizeProperty()), 
/* 115 */           (ObservableNumberValue)Bindings.add(
/* 116 */             (ObservableNumberValue)Bindings.multiply(2, (ObservableNumberValue)popOver
/* 117 */               .cornerRadiusProperty()), 
/* 118 */             (ObservableNumberValue)Bindings.multiply(2, (ObservableNumberValue)popOver
/* 119 */               .arrowIndentProperty()))));
/*     */     
/* 121 */     this.stackPane.minHeightProperty().bind((ObservableValue)this.stackPane.minWidthProperty());
/*     */     
/* 123 */     this.title = new Label();
/* 124 */     this.title.textProperty().bind((ObservableValue)popOver.titleProperty());
/* 125 */     this.title.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
/* 126 */     this.title.setAlignment(Pos.CENTER);
/* 127 */     this.title.getStyleClass().add("text");
/*     */     
/* 129 */     this.closeIcon = new Label();
/* 130 */     this.closeIcon.setGraphic(createCloseIcon());
/* 131 */     this.closeIcon.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
/* 132 */     this.closeIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
/* 133 */     this.closeIcon.visibleProperty().bind((ObservableValue)popOver
/* 134 */         .closeButtonEnabledProperty().and((ObservableBooleanValue)popOver
/* 135 */           .detachedProperty().or((ObservableBooleanValue)popOver.headerAlwaysVisibleProperty())));
/* 136 */     this.closeIcon.getStyleClass().add("icon");
/* 137 */     this.closeIcon.setAlignment(Pos.CENTER_LEFT);
/* 138 */     this.closeIcon.getGraphic().setOnMouseClicked(evt -> popOver.hide());
/*     */     
/* 140 */     this.titlePane = new StackPane();
/* 141 */     this.titlePane.getChildren().add(this.title);
/* 142 */     this.titlePane.getChildren().add(this.closeIcon);
/* 143 */     this.titlePane.getStyleClass().add("title");
/*     */     
/* 145 */     this.content = new BorderPane();
/* 146 */     this.content.setCenter(popOver.getContentNode());
/* 147 */     this.content.getStyleClass().add("content");
/*     */     
/* 149 */     if (popOver.isDetached() || popOver.isHeaderAlwaysVisible()) {
/* 150 */       this.content.setTop((Node)this.titlePane);
/*     */     }
/*     */     
/* 153 */     if (popOver.isDetached()) {
/* 154 */       popOver.getStyleClass().add("detached");
/* 155 */       this.content.getStyleClass().add("detached");
/*     */     } 
/*     */     
/* 158 */     popOver.headerAlwaysVisibleProperty().addListener((o, oV, isVisible) -> {
/*     */           if (isVisible.booleanValue()) {
/*     */             this.content.setTop((Node)this.titlePane);
/*     */           } else if (!popOver.isDetached()) {
/*     */             this.content.setTop(null);
/*     */           } 
/*     */         });
/*     */     
/* 166 */     InvalidationListener updatePathListener = observable -> updatePath();
/* 167 */     getPopupWindow().xProperty().addListener(updatePathListener);
/* 168 */     getPopupWindow().yProperty().addListener(updatePathListener);
/* 169 */     popOver.arrowLocationProperty().addListener(updatePathListener);
/* 170 */     popOver.contentNodeProperty().addListener((value, oldContent, newContent) -> this.content.setCenter(newContent));
/*     */ 
/*     */     
/* 173 */     popOver.detachedProperty()
/* 174 */       .addListener((value, oldDetached, newDetached) -> {
/*     */           if (newDetached.booleanValue()) {
/*     */             popOver.getStyleClass().add("detached");
/*     */             this.content.getStyleClass().add("detached");
/*     */             this.content.setTop((Node)this.titlePane);
/*     */             switch (getSkinnable().getArrowLocation()) {
/*     */               case LEFT_TOP:
/*     */               case LEFT_CENTER:
/*     */               case LEFT_BOTTOM:
/*     */                 popOver.setAnchorX(popOver.getAnchorX() + popOver.getArrowSize());
/*     */                 break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               case TOP_LEFT:
/*     */               case TOP_CENTER:
/*     */               case TOP_RIGHT:
/*     */                 popOver.setAnchorY(popOver.getAnchorY() + popOver.getArrowSize());
/*     */                 break;
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           } else {
/*     */             popOver.getStyleClass().remove("detached");
/*     */             this.content.getStyleClass().remove("detached");
/*     */             if (!popOver.isHeaderAlwaysVisible()) {
/*     */               this.content.setTop(null);
/*     */             }
/*     */           } 
/*     */           popOver.sizeToScene();
/*     */           updatePath();
/*     */         });
/* 211 */     this.path = new Path();
/* 212 */     this.path.getStyleClass().add("border");
/* 213 */     this.path.setManaged(false);
/*     */     
/* 215 */     this.clip = new Path();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     this.clip.setFill((Paint)Color.YELLOW);
/*     */     
/* 223 */     createPathElements();
/* 224 */     updatePath();
/*     */     
/* 226 */     EventHandler<MouseEvent> mousePressedHandler = evt -> {
/*     */         if (popOver.isDetachable() || popOver.isDetached()) {
/*     */           this.tornOff = false;
/*     */           
/*     */           this.xOffset = evt.getScreenX();
/*     */           
/*     */           this.yOffset = evt.getScreenY();
/*     */           
/*     */           this.dragStartLocation = new Point2D(this.xOffset, this.yOffset);
/*     */         } 
/*     */       };
/* 237 */     EventHandler<MouseEvent> mouseReleasedHandler = evt -> {
/*     */         if (this.tornOff && !getSkinnable().isDetached()) {
/*     */           this.tornOff = false;
/*     */           
/*     */           getSkinnable().detach();
/*     */         } 
/*     */       };
/* 244 */     EventHandler<MouseEvent> mouseDragHandler = evt -> {
/*     */         if (popOver.isDetachable() || popOver.isDetached()) {
/*     */           double deltaX = evt.getScreenX() - this.xOffset;
/*     */           
/*     */           double deltaY = evt.getScreenY() - this.yOffset;
/*     */           
/*     */           Window window = getSkinnable().getScene().getWindow();
/*     */           
/*     */           window.setX(window.getX() + deltaX);
/*     */           
/*     */           window.setY(window.getY() + deltaY);
/*     */           
/*     */           this.xOffset = evt.getScreenX();
/*     */           this.yOffset = evt.getScreenY();
/*     */           if (this.dragStartLocation.distance(this.xOffset, this.yOffset) > 20.0D) {
/*     */             this.tornOff = true;
/*     */             updatePath();
/*     */           } else if (this.tornOff) {
/*     */             this.tornOff = false;
/*     */             updatePath();
/*     */           } 
/*     */         } 
/*     */       };
/* 267 */     this.stackPane.setOnMousePressed(mousePressedHandler);
/* 268 */     this.stackPane.setOnMouseDragged(mouseDragHandler);
/* 269 */     this.stackPane.setOnMouseReleased(mouseReleasedHandler);
/*     */     
/* 271 */     this.stackPane.getChildren().add(this.path);
/* 272 */     this.stackPane.getChildren().add(this.content);
/*     */     
/* 274 */     this.content.setClip((Node)this.clip);
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getNode() {
/* 279 */     return (Node)this.stackPane;
/*     */   }
/*     */ 
/*     */   
/*     */   public PopOver getSkinnable() {
/* 284 */     return this.popOver;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {}
/*     */ 
/*     */   
/*     */   private Node createCloseIcon() {
/* 292 */     Group group = new Group();
/* 293 */     group.getStyleClass().add("graphics");
/*     */     
/* 295 */     Circle circle = new Circle();
/* 296 */     circle.getStyleClass().add("circle");
/* 297 */     circle.setRadius(6.0D);
/* 298 */     circle.setCenterX(6.0D);
/* 299 */     circle.setCenterY(6.0D);
/* 300 */     group.getChildren().add(circle);
/*     */     
/* 302 */     Line line1 = new Line();
/* 303 */     line1.getStyleClass().add("line");
/* 304 */     line1.setStartX(4.0D);
/* 305 */     line1.setStartY(4.0D);
/* 306 */     line1.setEndX(8.0D);
/* 307 */     line1.setEndY(8.0D);
/* 308 */     group.getChildren().add(line1);
/*     */     
/* 310 */     Line line2 = new Line();
/* 311 */     line2.getStyleClass().add("line");
/* 312 */     line2.setStartX(8.0D);
/* 313 */     line2.setStartY(4.0D);
/* 314 */     line2.setEndX(4.0D);
/* 315 */     line2.setEndY(8.0D);
/* 316 */     group.getChildren().add(line2);
/*     */     
/* 318 */     return (Node)group;
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
/*     */   private void createPathElements() {
/* 341 */     SimpleDoubleProperty simpleDoubleProperty1 = new SimpleDoubleProperty();
/* 342 */     SimpleDoubleProperty simpleDoubleProperty2 = new SimpleDoubleProperty();
/*     */     
/* 344 */     SimpleDoubleProperty simpleDoubleProperty3 = new SimpleDoubleProperty();
/* 345 */     SimpleDoubleProperty simpleDoubleProperty4 = new SimpleDoubleProperty();
/*     */     
/* 347 */     SimpleDoubleProperty simpleDoubleProperty5 = new SimpleDoubleProperty();
/* 348 */     SimpleDoubleProperty simpleDoubleProperty6 = new SimpleDoubleProperty();
/*     */     
/* 350 */     SimpleDoubleProperty simpleDoubleProperty7 = new SimpleDoubleProperty();
/* 351 */     SimpleDoubleProperty simpleDoubleProperty8 = new SimpleDoubleProperty();
/*     */     
/* 353 */     SimpleDoubleProperty simpleDoubleProperty9 = new SimpleDoubleProperty();
/* 354 */     SimpleDoubleProperty simpleDoubleProperty10 = new SimpleDoubleProperty();
/*     */     
/* 356 */     DoubleProperty cornerProperty = getSkinnable().cornerRadiusProperty();
/*     */     
/* 358 */     DoubleProperty arrowSizeProperty = getSkinnable().arrowSizeProperty();
/*     */     
/* 360 */     DoubleProperty arrowIndentProperty = getSkinnable().arrowIndentProperty();
/*     */     
/* 362 */     simpleDoubleProperty1.bind((ObservableValue)Bindings.divide((ObservableNumberValue)this.stackPane.heightProperty(), 2));
/* 363 */     simpleDoubleProperty2.bind((ObservableValue)Bindings.divide((ObservableNumberValue)this.stackPane.widthProperty(), 2));
/*     */     
/* 365 */     simpleDoubleProperty4.bind((ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty3, (ObservableNumberValue)
/* 366 */           getSkinnable().cornerRadiusProperty()));
/*     */     
/* 368 */     simpleDoubleProperty6.bind((ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty5, (ObservableNumberValue)
/* 369 */           getSkinnable().cornerRadiusProperty()));
/*     */     
/* 371 */     simpleDoubleProperty7.bind((ObservableValue)this.stackPane.widthProperty());
/* 372 */     simpleDoubleProperty8.bind((ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty7, (ObservableNumberValue)
/* 373 */           getSkinnable().cornerRadiusProperty()));
/*     */     
/* 375 */     simpleDoubleProperty9.bind((ObservableValue)this.stackPane.heightProperty());
/* 376 */     simpleDoubleProperty10.bind((ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty9, (ObservableNumberValue)
/* 377 */           getSkinnable().cornerRadiusProperty()));
/*     */ 
/*     */     
/* 380 */     this.moveTo = new MoveTo();
/* 381 */     this.moveTo.xProperty().bind((ObservableValue)simpleDoubleProperty4);
/* 382 */     this.moveTo.yProperty().bind((ObservableValue)simpleDoubleProperty5);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 387 */     this.lineBTop = new HLineTo();
/* 388 */     this.lineBTop.xProperty().bind(
/* 389 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty4, (ObservableNumberValue)arrowIndentProperty));
/*     */     
/* 391 */     this.lineCTop = new LineTo();
/* 392 */     this.lineCTop.xProperty().bind(
/* 393 */         (ObservableValue)Bindings.add((ObservableNumberValue)this.lineBTop.xProperty(), (ObservableNumberValue)arrowSizeProperty));
/* 394 */     this.lineCTop.yProperty().bind(
/* 395 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty5, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 397 */     this.lineDTop = new LineTo();
/* 398 */     this.lineDTop.xProperty().bind(
/* 399 */         (ObservableValue)Bindings.add((ObservableNumberValue)this.lineCTop.xProperty(), (ObservableNumberValue)arrowSizeProperty));
/* 400 */     this.lineDTop.yProperty().bind((ObservableValue)simpleDoubleProperty5);
/*     */     
/* 402 */     this.lineETop = new HLineTo();
/* 403 */     this.lineETop.xProperty().bind(
/* 404 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty2, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 406 */     this.lineFTop = new LineTo();
/* 407 */     this.lineFTop.xProperty().bind((ObservableValue)simpleDoubleProperty2);
/* 408 */     this.lineFTop.yProperty().bind(
/* 409 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty5, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 411 */     this.lineGTop = new LineTo();
/* 412 */     this.lineGTop.xProperty().bind(
/* 413 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty2, (ObservableNumberValue)arrowSizeProperty));
/* 414 */     this.lineGTop.yProperty().bind((ObservableValue)simpleDoubleProperty5);
/*     */     
/* 416 */     this.lineHTop = new HLineTo();
/* 417 */     this.lineHTop.xProperty().bind(
/* 418 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty8, (ObservableNumberValue)arrowIndentProperty), 
/*     */           
/* 420 */           (ObservableNumberValue)Bindings.multiply((ObservableNumberValue)arrowSizeProperty, 2)));
/*     */     
/* 422 */     this.lineITop = new LineTo();
/* 423 */     this.lineITop.xProperty().bind(
/* 424 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty8, (ObservableNumberValue)arrowIndentProperty), (ObservableNumberValue)arrowSizeProperty));
/*     */ 
/*     */     
/* 427 */     this.lineITop.yProperty().bind(
/* 428 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty5, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 430 */     this.lineJTop = new LineTo();
/* 431 */     this.lineJTop.xProperty().bind(
/* 432 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty8, (ObservableNumberValue)arrowIndentProperty));
/*     */     
/* 434 */     this.lineJTop.yProperty().bind((ObservableValue)simpleDoubleProperty5);
/*     */     
/* 436 */     this.lineKTop = new HLineTo();
/* 437 */     this.lineKTop.xProperty().bind((ObservableValue)simpleDoubleProperty8);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 442 */     this.rightCurveTo = new QuadCurveTo();
/* 443 */     this.rightCurveTo.xProperty().bind((ObservableValue)simpleDoubleProperty7);
/* 444 */     this.rightCurveTo.yProperty().bind(
/* 445 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty5, (ObservableNumberValue)cornerProperty));
/* 446 */     this.rightCurveTo.controlXProperty().bind((ObservableValue)simpleDoubleProperty7);
/* 447 */     this.rightCurveTo.controlYProperty().bind((ObservableValue)simpleDoubleProperty5);
/*     */     
/* 449 */     this.lineBRight = new VLineTo();
/* 450 */     this.lineBRight.yProperty().bind(
/* 451 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty6, (ObservableNumberValue)arrowIndentProperty));
/*     */     
/* 453 */     this.lineCRight = new LineTo();
/* 454 */     this.lineCRight.xProperty().bind(
/* 455 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty7, (ObservableNumberValue)arrowSizeProperty));
/* 456 */     this.lineCRight.yProperty().bind(
/* 457 */         (ObservableValue)Bindings.add((ObservableNumberValue)this.lineBRight.yProperty(), (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 459 */     this.lineDRight = new LineTo();
/* 460 */     this.lineDRight.xProperty().bind((ObservableValue)simpleDoubleProperty7);
/* 461 */     this.lineDRight.yProperty().bind(
/* 462 */         (ObservableValue)Bindings.add((ObservableNumberValue)this.lineCRight.yProperty(), (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 464 */     this.lineERight = new VLineTo();
/* 465 */     this.lineERight.yProperty().bind(
/* 466 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty1, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 468 */     this.lineFRight = new LineTo();
/* 469 */     this.lineFRight.xProperty().bind(
/* 470 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty7, (ObservableNumberValue)arrowSizeProperty));
/* 471 */     this.lineFRight.yProperty().bind((ObservableValue)simpleDoubleProperty1);
/*     */     
/* 473 */     this.lineGRight = new LineTo();
/* 474 */     this.lineGRight.xProperty().bind((ObservableValue)simpleDoubleProperty7);
/* 475 */     this.lineGRight.yProperty().bind(
/* 476 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty1, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 478 */     this.lineHRight = new VLineTo();
/* 479 */     this.lineHRight.yProperty().bind(
/* 480 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty10, (ObservableNumberValue)arrowIndentProperty), 
/*     */           
/* 482 */           (ObservableNumberValue)Bindings.multiply((ObservableNumberValue)arrowSizeProperty, 2)));
/*     */     
/* 484 */     this.lineIRight = new LineTo();
/* 485 */     this.lineIRight.xProperty().bind(
/* 486 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty7, (ObservableNumberValue)arrowSizeProperty));
/* 487 */     this.lineIRight.yProperty().bind(
/* 488 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty10, (ObservableNumberValue)arrowIndentProperty), (ObservableNumberValue)arrowSizeProperty));
/*     */ 
/*     */ 
/*     */     
/* 492 */     this.lineJRight = new LineTo();
/* 493 */     this.lineJRight.xProperty().bind((ObservableValue)simpleDoubleProperty7);
/* 494 */     this.lineJRight.yProperty().bind(
/* 495 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty10, (ObservableNumberValue)arrowIndentProperty));
/*     */ 
/*     */     
/* 498 */     this.lineKRight = new VLineTo();
/* 499 */     this.lineKRight.yProperty().bind((ObservableValue)simpleDoubleProperty10);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 505 */     this.bottomCurveTo = new QuadCurveTo();
/* 506 */     this.bottomCurveTo.xProperty().bind((ObservableValue)simpleDoubleProperty8);
/* 507 */     this.bottomCurveTo.yProperty().bind((ObservableValue)simpleDoubleProperty9);
/* 508 */     this.bottomCurveTo.controlXProperty().bind((ObservableValue)simpleDoubleProperty7);
/* 509 */     this.bottomCurveTo.controlYProperty().bind((ObservableValue)simpleDoubleProperty9);
/*     */     
/* 511 */     this.lineBBottom = new HLineTo();
/* 512 */     this.lineBBottom.xProperty().bind(
/* 513 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty8, (ObservableNumberValue)arrowIndentProperty));
/*     */ 
/*     */     
/* 516 */     this.lineCBottom = new LineTo();
/* 517 */     this.lineCBottom.xProperty().bind(
/* 518 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)this.lineBBottom.xProperty(), (ObservableNumberValue)arrowSizeProperty));
/* 519 */     this.lineCBottom.yProperty().bind(
/* 520 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty9, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 522 */     this.lineDBottom = new LineTo();
/* 523 */     this.lineDBottom.xProperty().bind(
/* 524 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)this.lineCBottom.xProperty(), (ObservableNumberValue)arrowSizeProperty));
/* 525 */     this.lineDBottom.yProperty().bind((ObservableValue)simpleDoubleProperty9);
/*     */     
/* 527 */     this.lineEBottom = new HLineTo();
/* 528 */     this.lineEBottom.xProperty().bind(
/* 529 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty2, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 531 */     this.lineFBottom = new LineTo();
/* 532 */     this.lineFBottom.xProperty().bind((ObservableValue)simpleDoubleProperty2);
/* 533 */     this.lineFBottom.yProperty().bind(
/* 534 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty9, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 536 */     this.lineGBottom = new LineTo();
/* 537 */     this.lineGBottom.xProperty().bind(
/* 538 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty2, (ObservableNumberValue)arrowSizeProperty));
/* 539 */     this.lineGBottom.yProperty().bind((ObservableValue)simpleDoubleProperty9);
/*     */     
/* 541 */     this.lineHBottom = new HLineTo();
/* 542 */     this.lineHBottom.xProperty().bind(
/* 543 */         (ObservableValue)Bindings.add((ObservableNumberValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty4, (ObservableNumberValue)arrowIndentProperty), 
/* 544 */           (ObservableNumberValue)Bindings.multiply((ObservableNumberValue)arrowSizeProperty, 2)));
/*     */ 
/*     */     
/* 547 */     this.lineIBottom = new LineTo();
/* 548 */     this.lineIBottom.xProperty().bind(
/* 549 */         (ObservableValue)Bindings.add((ObservableNumberValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty4, (ObservableNumberValue)arrowIndentProperty), (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 551 */     this.lineIBottom.yProperty().bind(
/* 552 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty9, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 554 */     this.lineJBottom = new LineTo();
/* 555 */     this.lineJBottom.xProperty().bind(
/* 556 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty4, (ObservableNumberValue)arrowIndentProperty));
/* 557 */     this.lineJBottom.yProperty().bind((ObservableValue)simpleDoubleProperty9);
/*     */     
/* 559 */     this.lineKBottom = new HLineTo();
/* 560 */     this.lineKBottom.xProperty().bind((ObservableValue)simpleDoubleProperty4);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 565 */     this.leftCurveTo = new QuadCurveTo();
/* 566 */     this.leftCurveTo.xProperty().bind((ObservableValue)simpleDoubleProperty3);
/* 567 */     this.leftCurveTo.yProperty().bind(
/* 568 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty9, (ObservableNumberValue)cornerProperty));
/* 569 */     this.leftCurveTo.controlXProperty().bind((ObservableValue)simpleDoubleProperty3);
/* 570 */     this.leftCurveTo.controlYProperty().bind((ObservableValue)simpleDoubleProperty9);
/*     */     
/* 572 */     this.lineBLeft = new VLineTo();
/* 573 */     this.lineBLeft.yProperty().bind(
/* 574 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty10, (ObservableNumberValue)arrowIndentProperty));
/*     */ 
/*     */     
/* 577 */     this.lineCLeft = new LineTo();
/* 578 */     this.lineCLeft.xProperty().bind(
/* 579 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty3, (ObservableNumberValue)arrowSizeProperty));
/* 580 */     this.lineCLeft.yProperty().bind(
/* 581 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)this.lineBLeft.yProperty(), (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 583 */     this.lineDLeft = new LineTo();
/* 584 */     this.lineDLeft.xProperty().bind((ObservableValue)simpleDoubleProperty3);
/* 585 */     this.lineDLeft.yProperty().bind(
/* 586 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)this.lineCLeft.yProperty(), (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 588 */     this.lineELeft = new VLineTo();
/* 589 */     this.lineELeft.yProperty().bind(
/* 590 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty1, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 592 */     this.lineFLeft = new LineTo();
/* 593 */     this.lineFLeft.xProperty().bind(
/* 594 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty3, (ObservableNumberValue)arrowSizeProperty));
/* 595 */     this.lineFLeft.yProperty().bind((ObservableValue)simpleDoubleProperty1);
/*     */     
/* 597 */     this.lineGLeft = new LineTo();
/* 598 */     this.lineGLeft.xProperty().bind((ObservableValue)simpleDoubleProperty3);
/* 599 */     this.lineGLeft.yProperty().bind(
/* 600 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty1, (ObservableNumberValue)arrowSizeProperty));
/*     */     
/* 602 */     this.lineHLeft = new VLineTo();
/* 603 */     this.lineHLeft.yProperty().bind(
/* 604 */         (ObservableValue)Bindings.add((ObservableNumberValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty6, (ObservableNumberValue)arrowIndentProperty), 
/* 605 */           (ObservableNumberValue)Bindings.multiply((ObservableNumberValue)arrowSizeProperty, 2)));
/*     */ 
/*     */     
/* 608 */     this.lineILeft = new LineTo();
/* 609 */     this.lineILeft.xProperty().bind(
/* 610 */         (ObservableValue)Bindings.subtract((ObservableNumberValue)simpleDoubleProperty3, (ObservableNumberValue)arrowSizeProperty));
/* 611 */     this.lineILeft.yProperty().bind(
/* 612 */         (ObservableValue)Bindings.add((ObservableNumberValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty6, (ObservableNumberValue)arrowIndentProperty), (ObservableNumberValue)arrowSizeProperty));
/*     */ 
/*     */     
/* 615 */     this.lineJLeft = new LineTo();
/* 616 */     this.lineJLeft.xProperty().bind((ObservableValue)simpleDoubleProperty3);
/* 617 */     this.lineJLeft.yProperty().bind(
/* 618 */         (ObservableValue)Bindings.add((ObservableNumberValue)simpleDoubleProperty6, (ObservableNumberValue)arrowIndentProperty));
/*     */     
/* 620 */     this.lineKLeft = new VLineTo();
/* 621 */     this.lineKLeft.yProperty().bind((ObservableValue)simpleDoubleProperty6);
/*     */     
/* 623 */     this.topCurveTo = new QuadCurveTo();
/* 624 */     this.topCurveTo.xProperty().bind((ObservableValue)simpleDoubleProperty4);
/* 625 */     this.topCurveTo.yProperty().bind((ObservableValue)simpleDoubleProperty5);
/* 626 */     this.topCurveTo.controlXProperty().bind((ObservableValue)simpleDoubleProperty3);
/* 627 */     this.topCurveTo.controlYProperty().bind((ObservableValue)simpleDoubleProperty5);
/*     */   }
/*     */   
/*     */   private Window getPopupWindow() {
/* 631 */     return getSkinnable().getScene().getWindow();
/*     */   }
/*     */   
/*     */   private boolean showArrow(PopOver.ArrowLocation location) {
/* 635 */     PopOver.ArrowLocation arrowLocation = getSkinnable().getArrowLocation();
/* 636 */     return (location.equals(arrowLocation) && !getSkinnable().isDetached() && !this.tornOff);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updatePath() {
/* 641 */     List<PathElement> elements = new ArrayList<>();
/* 642 */     elements.add(this.moveTo);
/*     */     
/* 644 */     if (showArrow(PopOver.ArrowLocation.TOP_LEFT)) {
/* 645 */       elements.add(this.lineBTop);
/* 646 */       elements.add(this.lineCTop);
/* 647 */       elements.add(this.lineDTop);
/*     */     } 
/* 649 */     if (showArrow(PopOver.ArrowLocation.TOP_CENTER)) {
/* 650 */       elements.add(this.lineETop);
/* 651 */       elements.add(this.lineFTop);
/* 652 */       elements.add(this.lineGTop);
/*     */     } 
/* 654 */     if (showArrow(PopOver.ArrowLocation.TOP_RIGHT)) {
/* 655 */       elements.add(this.lineHTop);
/* 656 */       elements.add(this.lineITop);
/* 657 */       elements.add(this.lineJTop);
/*     */     } 
/* 659 */     elements.add(this.lineKTop);
/* 660 */     elements.add(this.rightCurveTo);
/*     */     
/* 662 */     if (showArrow(PopOver.ArrowLocation.RIGHT_TOP)) {
/* 663 */       elements.add(this.lineBRight);
/* 664 */       elements.add(this.lineCRight);
/* 665 */       elements.add(this.lineDRight);
/*     */     } 
/* 667 */     if (showArrow(PopOver.ArrowLocation.RIGHT_CENTER)) {
/* 668 */       elements.add(this.lineERight);
/* 669 */       elements.add(this.lineFRight);
/* 670 */       elements.add(this.lineGRight);
/*     */     } 
/* 672 */     if (showArrow(PopOver.ArrowLocation.RIGHT_BOTTOM)) {
/* 673 */       elements.add(this.lineHRight);
/* 674 */       elements.add(this.lineIRight);
/* 675 */       elements.add(this.lineJRight);
/*     */     } 
/* 677 */     elements.add(this.lineKRight);
/* 678 */     elements.add(this.bottomCurveTo);
/*     */     
/* 680 */     if (showArrow(PopOver.ArrowLocation.BOTTOM_RIGHT)) {
/* 681 */       elements.add(this.lineBBottom);
/* 682 */       elements.add(this.lineCBottom);
/* 683 */       elements.add(this.lineDBottom);
/*     */     } 
/* 685 */     if (showArrow(PopOver.ArrowLocation.BOTTOM_CENTER)) {
/* 686 */       elements.add(this.lineEBottom);
/* 687 */       elements.add(this.lineFBottom);
/* 688 */       elements.add(this.lineGBottom);
/*     */     } 
/* 690 */     if (showArrow(PopOver.ArrowLocation.BOTTOM_LEFT)) {
/* 691 */       elements.add(this.lineHBottom);
/* 692 */       elements.add(this.lineIBottom);
/* 693 */       elements.add(this.lineJBottom);
/*     */     } 
/* 695 */     elements.add(this.lineKBottom);
/* 696 */     elements.add(this.leftCurveTo);
/*     */     
/* 698 */     if (showArrow(PopOver.ArrowLocation.LEFT_BOTTOM)) {
/* 699 */       elements.add(this.lineBLeft);
/* 700 */       elements.add(this.lineCLeft);
/* 701 */       elements.add(this.lineDLeft);
/*     */     } 
/* 703 */     if (showArrow(PopOver.ArrowLocation.LEFT_CENTER)) {
/* 704 */       elements.add(this.lineELeft);
/* 705 */       elements.add(this.lineFLeft);
/* 706 */       elements.add(this.lineGLeft);
/*     */     } 
/* 708 */     if (showArrow(PopOver.ArrowLocation.LEFT_TOP)) {
/* 709 */       elements.add(this.lineHLeft);
/* 710 */       elements.add(this.lineILeft);
/* 711 */       elements.add(this.lineJLeft);
/*     */     } 
/* 713 */     elements.add(this.lineKLeft);
/* 714 */     elements.add(this.topCurveTo);
/*     */     
/* 716 */     this.path.getElements().setAll(elements);
/* 717 */     this.clip.getElements().setAll(elements);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\PopOverSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */