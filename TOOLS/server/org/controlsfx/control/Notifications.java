/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.NotificationBar;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javafx.animation.KeyFrame;
/*     */ import javafx.animation.KeyValue;
/*     */ import javafx.animation.ParallelTransition;
/*     */ import javafx.animation.Timeline;
/*     */ import javafx.animation.Transition;
/*     */ import javafx.beans.value.WritableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.geometry.Rectangle2D;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.Scene;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.stage.Popup;
/*     */ import javafx.stage.PopupWindow;
/*     */ import javafx.stage.Screen;
/*     */ import javafx.stage.Window;
/*     */ import javafx.util.Duration;
/*     */ import org.controlsfx.control.action.Action;
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
/*     */ public class Notifications
/*     */ {
/*     */   private static final String STYLE_CLASS_DARK = "dark";
/*     */   private String title;
/*     */   private String text;
/*     */   private Node graphic;
/* 103 */   private ObservableList<Action> actions = FXCollections.observableArrayList();
/* 104 */   private Pos position = Pos.BOTTOM_RIGHT;
/* 105 */   private Duration hideAfterDuration = Duration.seconds(5.0D);
/*     */   private boolean hideCloseButton;
/*     */   private EventHandler<ActionEvent> onAction;
/*     */   private Window owner;
/* 109 */   private Screen screen = Screen.getPrimary();
/*     */   
/* 111 */   private List<String> styleClass = new ArrayList<>();
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
/*     */   public static Notifications create() {
/* 131 */     return new Notifications();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications text(String text) {
/* 138 */     this.text = text;
/* 139 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications title(String title) {
/* 146 */     this.title = title;
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications graphic(Node graphic) {
/* 154 */     this.graphic = graphic;
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications position(Pos position) {
/* 163 */     this.position = position;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications owner(Object owner) {
/* 174 */     if (owner instanceof Screen) {
/* 175 */       this.screen = (Screen)owner;
/*     */     } else {
/* 177 */       this.owner = Utils.getWindow(owner);
/*     */     } 
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications hideAfter(Duration duration) {
/* 187 */     this.hideAfterDuration = duration;
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications onAction(EventHandler<ActionEvent> onAction) {
/* 197 */     this.onAction = onAction;
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications darkStyle() {
/* 207 */     this.styleClass.add("dark");
/* 208 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications hideCloseButton() {
/* 216 */     this.hideCloseButton = true;
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Notifications action(Action... actions) {
/* 224 */     this
/* 225 */       .actions = (actions == null) ? FXCollections.observableArrayList() : FXCollections.observableArrayList((Object[])actions);
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void showWarning() {
/* 234 */     graphic((Node)new ImageView(Notifications.class.getResource("/org/controlsfx/dialog/dialog-warning.png").toExternalForm()));
/* 235 */     show();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void showInformation() {
/* 243 */     graphic((Node)new ImageView(Notifications.class.getResource("/org/controlsfx/dialog/dialog-information.png").toExternalForm()));
/* 244 */     show();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void showError() {
/* 252 */     graphic((Node)new ImageView(Notifications.class.getResource("/org/controlsfx/dialog/dialog-error.png").toExternalForm()));
/* 253 */     show();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void showConfirm() {
/* 261 */     graphic((Node)new ImageView(Notifications.class.getResource("/org/controlsfx/dialog/dialog-confirm.png").toExternalForm()));
/* 262 */     show();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void show() {
/* 269 */     NotificationPopupHandler.getInstance().show(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NotificationPopupHandler
/*     */   {
/* 279 */     private static final NotificationPopupHandler INSTANCE = new NotificationPopupHandler();
/*     */     
/*     */     private double startX;
/*     */     private double startY;
/*     */     private double screenWidth;
/*     */     private double screenHeight;
/*     */     
/*     */     static final NotificationPopupHandler getInstance() {
/* 287 */       return INSTANCE;
/*     */     }
/*     */     
/* 290 */     private final Map<Pos, List<Popup>> popupsMap = new HashMap<>();
/* 291 */     private final double padding = 15.0D;
/*     */ 
/*     */     
/* 294 */     private ParallelTransition parallelTransition = new ParallelTransition();
/*     */     
/*     */     private boolean isShowing = false;
/*     */     
/*     */     public void show(Notifications notification) {
/*     */       Window window;
/* 300 */       if (notification.owner == null) {
/*     */ 
/*     */ 
/*     */         
/* 304 */         Rectangle2D screenBounds = notification.screen.getVisualBounds();
/* 305 */         this.startX = screenBounds.getMinX();
/* 306 */         this.startY = screenBounds.getMinY();
/* 307 */         this.screenWidth = screenBounds.getWidth();
/* 308 */         this.screenHeight = screenBounds.getHeight();
/*     */         
/* 310 */         window = Utils.getWindow(null);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 316 */         this.startX = notification.owner.getX();
/* 317 */         this.startY = notification.owner.getY();
/* 318 */         this.screenWidth = notification.owner.getWidth();
/* 319 */         this.screenHeight = notification.owner.getHeight();
/* 320 */         window = notification.owner;
/*     */       } 
/* 322 */       show(window, notification);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void show(Window owner, final Notifications notification) {
/* 329 */       Window ownerWindow = owner;
/* 330 */       while (ownerWindow instanceof PopupWindow) {
/* 331 */         ownerWindow = ((PopupWindow)ownerWindow).getOwnerWindow();
/*     */       }
/*     */       
/* 334 */       Scene ownerScene = ownerWindow.getScene();
/* 335 */       if (ownerScene != null) {
/* 336 */         String stylesheetUrl = Notifications.class.getResource("notificationpopup.css").toExternalForm();
/* 337 */         if (!ownerScene.getStylesheets().contains(stylesheetUrl))
/*     */         {
/*     */           
/* 340 */           ownerScene.getStylesheets().add(0, stylesheetUrl);
/*     */         }
/*     */       } 
/*     */       
/* 344 */       final Popup popup = new Popup();
/* 345 */       popup.setAutoFix(false);
/*     */       
/* 347 */       final Pos p = notification.position;
/*     */       
/* 349 */       NotificationBar notificationBar = new NotificationBar() {
/*     */           public String getTitle() {
/* 351 */             return notification.title;
/*     */           }
/*     */           
/*     */           public String getText() {
/* 355 */             return notification.text;
/*     */           }
/*     */           
/*     */           public Node getGraphic() {
/* 359 */             return notification.graphic;
/*     */           }
/*     */           
/*     */           public ObservableList<Action> getActions() {
/* 363 */             return notification.actions;
/*     */           }
/*     */           
/*     */           public boolean isShowing() {
/* 367 */             return Notifications.NotificationPopupHandler.this.isShowing;
/*     */           }
/*     */           
/*     */           protected double computeMinWidth(double height) {
/* 371 */             String text = getText();
/* 372 */             Node graphic = getGraphic();
/* 373 */             if ((text == null || text.isEmpty()) && graphic != null) {
/* 374 */               return graphic.minWidth(height);
/*     */             }
/* 376 */             return 400.0D;
/*     */           }
/*     */           
/*     */           protected double computeMinHeight(double width) {
/* 380 */             String text = getText();
/* 381 */             Node graphic = getGraphic();
/* 382 */             if ((text == null || text.isEmpty()) && graphic != null) {
/* 383 */               return graphic.minHeight(width);
/*     */             }
/* 385 */             return 100.0D;
/*     */           }
/*     */           
/*     */           public boolean isShowFromTop() {
/* 389 */             return Notifications.NotificationPopupHandler.this.isShowFromTop(notification.position);
/*     */           }
/*     */           
/*     */           public void hide() {
/* 393 */             Notifications.NotificationPopupHandler.this.isShowing = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 400 */             Notifications.NotificationPopupHandler.this.createHideTimeline(popup, this, p, Duration.ZERO).play();
/*     */           }
/*     */           
/*     */           public boolean isCloseButtonVisible() {
/* 404 */             return !notification.hideCloseButton;
/*     */           }
/*     */           
/*     */           public double getContainerHeight() {
/* 408 */             return Notifications.NotificationPopupHandler.this.startY + Notifications.NotificationPopupHandler.this.screenHeight;
/*     */           }
/*     */ 
/*     */           
/*     */           public void relocateInParent(double x, double y) {
/* 413 */             switch (p) {
/*     */               case BOTTOM_LEFT:
/*     */               case BOTTOM_CENTER:
/*     */               case BOTTOM_RIGHT:
/* 417 */                 popup.setAnchorY(y - 15.0D);
/*     */                 break;
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */         };
/* 426 */       notificationBar.getStyleClass().addAll(notification.styleClass);
/*     */       
/* 428 */       notificationBar.setOnMouseClicked(e -> {
/*     */             if (notification.onAction != null) {
/*     */               ActionEvent actionEvent = new ActionEvent(notificationBar, (EventTarget)notificationBar);
/*     */               
/*     */               notification.onAction.handle((Event)actionEvent);
/*     */               
/*     */               createHideTimeline(popup, notificationBar, p, Duration.ZERO).play();
/*     */             } 
/*     */           });
/*     */       
/* 438 */       popup.getContent().add(notificationBar);
/* 439 */       popup.show(owner, 0.0D, 0.0D);
/*     */ 
/*     */       
/* 442 */       double anchorX = 0.0D, anchorY = 0.0D;
/* 443 */       double barWidth = notificationBar.getWidth();
/* 444 */       double barHeight = notificationBar.getHeight();
/*     */ 
/*     */       
/* 447 */       switch (p) {
/*     */         case BOTTOM_LEFT:
/*     */         case TOP_LEFT:
/*     */         case CENTER_LEFT:
/* 451 */           anchorX = 15.0D + this.startX;
/*     */           break;
/*     */         
/*     */         case BOTTOM_CENTER:
/*     */         case TOP_CENTER:
/*     */         case CENTER:
/* 457 */           anchorX = this.startX + this.screenWidth / 2.0D - barWidth / 2.0D - 7.5D;
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         default:
/* 464 */           anchorX = this.startX + this.screenWidth - barWidth - 15.0D;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 469 */       switch (p) {
/*     */         case TOP_LEFT:
/*     */         case TOP_CENTER:
/*     */         case TOP_RIGHT:
/* 473 */           anchorY = 15.0D + this.startY;
/*     */           break;
/*     */         
/*     */         case CENTER_LEFT:
/*     */         case CENTER:
/*     */         case CENTER_RIGHT:
/* 479 */           anchorY = this.startY + this.screenHeight / 2.0D - barHeight / 2.0D - 7.5D;
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         default:
/* 486 */           anchorY = this.startY + this.screenHeight - barHeight - 15.0D;
/*     */           break;
/*     */       } 
/*     */       
/* 490 */       popup.setAnchorX(anchorX);
/* 491 */       popup.setAnchorY(anchorY);
/*     */       
/* 493 */       this.isShowing = true;
/* 494 */       notificationBar.doShow();
/*     */       
/* 496 */       addPopupToMap(p, popup);
/*     */ 
/*     */       
/* 499 */       Timeline timeline = createHideTimeline(popup, notificationBar, p, notification.hideAfterDuration);
/* 500 */       timeline.play();
/*     */     }
/*     */     
/*     */     private void hide(Popup popup, Pos p) {
/* 504 */       popup.hide();
/* 505 */       removePopupFromMap(p, popup);
/*     */     }
/*     */     
/*     */     private Timeline createHideTimeline(final Popup popup, NotificationBar bar, final Pos p, Duration startDelay) {
/* 509 */       KeyValue fadeOutBegin = new KeyValue((WritableValue)bar.opacityProperty(), Double.valueOf(1.0D));
/* 510 */       KeyValue fadeOutEnd = new KeyValue((WritableValue)bar.opacityProperty(), Double.valueOf(0.0D));
/*     */       
/* 512 */       KeyFrame kfBegin = new KeyFrame(Duration.ZERO, new KeyValue[] { fadeOutBegin });
/* 513 */       KeyFrame kfEnd = new KeyFrame(Duration.millis(500.0D), new KeyValue[] { fadeOutEnd });
/*     */       
/* 515 */       Timeline timeline = new Timeline(new KeyFrame[] { kfBegin, kfEnd });
/* 516 */       timeline.setDelay(startDelay);
/* 517 */       timeline.setOnFinished(new EventHandler<ActionEvent>()
/*     */           {
/*     */             public void handle(ActionEvent e) {
/* 520 */               Notifications.NotificationPopupHandler.this.hide(popup, p);
/*     */             }
/*     */           });
/*     */       
/* 524 */       return timeline;
/*     */     }
/*     */     
/*     */     private void addPopupToMap(Pos p, Popup popup) {
/*     */       List<Popup> popups;
/* 529 */       if (!this.popupsMap.containsKey(p)) {
/* 530 */         popups = new LinkedList<>();
/* 531 */         this.popupsMap.put(p, popups);
/*     */       } else {
/* 533 */         popups = this.popupsMap.get(p);
/*     */       } 
/*     */       
/* 536 */       doAnimation(p, popup);
/*     */ 
/*     */ 
/*     */       
/* 540 */       popups.add(popup);
/*     */     }
/*     */     
/*     */     private void removePopupFromMap(Pos p, Popup popup) {
/* 544 */       if (this.popupsMap.containsKey(p)) {
/* 545 */         List<Popup> popups = this.popupsMap.get(p);
/* 546 */         popups.remove(popup);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void doAnimation(Pos p, Popup changedPopup) {
/* 551 */       List<Popup> popups = this.popupsMap.get(p);
/* 552 */       if (popups == null) {
/*     */         return;
/*     */       }
/*     */       
/* 556 */       double newPopupHeight = ((Node)changedPopup.getContent().get(0)).getBoundsInParent().getHeight();
/*     */       
/* 558 */       this.parallelTransition.stop();
/* 559 */       this.parallelTransition.getChildren().clear();
/*     */       
/* 561 */       boolean isShowFromTop = isShowFromTop(p);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 566 */       double sum = 0.0D;
/* 567 */       double[] targetAnchors = new double[popups.size()]; int i;
/* 568 */       for (i = popups.size() - 1; i >= 0; i--) {
/* 569 */         Popup _popup = popups.get(i);
/*     */         
/* 571 */         double popupHeight = ((Node)_popup.getContent().get(0)).getBoundsInParent().getHeight();
/*     */         
/* 573 */         if (isShowFromTop) {
/* 574 */           if (i == popups.size() - 1) {
/* 575 */             sum = this.startY + newPopupHeight + 15.0D;
/*     */           } else {
/* 577 */             sum += popupHeight;
/*     */           } 
/* 579 */           targetAnchors[i] = sum;
/*     */         } else {
/* 581 */           if (i == popups.size() - 1) {
/* 582 */             sum = changedPopup.getAnchorY() - popupHeight;
/*     */           } else {
/* 584 */             sum -= popupHeight;
/*     */           } 
/*     */           
/* 587 */           targetAnchors[i] = sum;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 593 */       for (i = popups.size() - 1; i >= 0; i--) {
/* 594 */         Popup _popup = popups.get(i);
/* 595 */         double anchorYTarget = targetAnchors[i];
/* 596 */         if (anchorYTarget < 0.0D) {
/* 597 */           _popup.hide();
/*     */         }
/* 599 */         double oldAnchorY = _popup.getAnchorY();
/* 600 */         double distance = anchorYTarget - oldAnchorY;
/*     */         
/* 602 */         Transition t = new CustomTransition(_popup, oldAnchorY, distance);
/* 603 */         t.setCycleCount(1);
/* 604 */         this.parallelTransition.getChildren().add(t);
/*     */       } 
/* 606 */       this.parallelTransition.play();
/*     */     }
/*     */     
/*     */     private boolean isShowFromTop(Pos p) {
/* 610 */       switch (p) {
/*     */         case TOP_LEFT:
/*     */         case TOP_CENTER:
/*     */         case TOP_RIGHT:
/* 614 */           return true;
/*     */       } 
/* 616 */       return false;
/*     */     }
/*     */     
/*     */     class CustomTransition
/*     */       extends Transition
/*     */     {
/*     */       private WeakReference<Popup> popupWeakReference;
/*     */       private double oldAnchorY;
/*     */       private double distance;
/*     */       
/*     */       CustomTransition(Popup popup, double oldAnchorY, double distance) {
/* 627 */         this.popupWeakReference = new WeakReference<>(popup);
/* 628 */         this.oldAnchorY = oldAnchorY;
/* 629 */         this.distance = distance;
/* 630 */         setCycleDuration(Duration.millis(350.0D));
/*     */       }
/*     */ 
/*     */       
/*     */       protected void interpolate(double frac) {
/* 635 */         Popup popup = this.popupWeakReference.get();
/* 636 */         if (popup != null) {
/* 637 */           double newAnchorY = this.oldAnchorY + this.distance * frac;
/* 638 */           popup.setAnchorY(newAnchorY);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\Notifications.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */