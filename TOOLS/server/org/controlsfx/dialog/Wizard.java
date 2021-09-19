/*     */ package org.controlsfx.dialog;
/*     */ 
/*     */ import impl.org.controlsfx.ImplUtils;
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Stack;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.property.SimpleStringProperty;
/*     */ import javafx.beans.property.StringProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.ObservableMap;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.geometry.Rectangle2D;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.ButtonBar;
/*     */ import javafx.scene.control.ButtonType;
/*     */ import javafx.scene.control.Dialog;
/*     */ import javafx.scene.layout.Pane;
/*     */ import javafx.stage.Screen;
/*     */ import javafx.stage.Window;
/*     */ import org.controlsfx.tools.ValueExtractor;
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
/*     */ public class Wizard
/*     */ {
/*     */   private Dialog<ButtonType> dialog;
/* 134 */   private final ObservableMap<String, Object> settings = FXCollections.observableHashMap();
/*     */   
/* 136 */   private final Stack<WizardPane> pageHistory = new Stack<>();
/*     */   
/* 138 */   private Optional<WizardPane> currentPage = Optional.empty();
/*     */   
/* 140 */   private final BooleanProperty invalidProperty = (BooleanProperty)new SimpleBooleanProperty(false);
/*     */ 
/*     */   
/* 143 */   private final BooleanProperty readSettingsProperty = (BooleanProperty)new SimpleBooleanProperty(true);
/*     */   
/* 145 */   private final ButtonType BUTTON_PREVIOUS = new ButtonType(Localization.localize(Localization.asKey("wizard.previous.button")), ButtonBar.ButtonData.BACK_PREVIOUS); private final EventHandler<ActionEvent> BUTTON_PREVIOUS_ACTION_HANDLER; private final ButtonType BUTTON_NEXT; private final EventHandler<ActionEvent> BUTTON_NEXT_ACTION_HANDLER; private final StringProperty titleProperty; private ObjectProperty<Flow> flow;
/* 146 */   public Wizard(Object owner, String title) { this.BUTTON_PREVIOUS_ACTION_HANDLER = (actionEvent -> {
/*     */         actionEvent.consume();
/*     */         
/*     */         this.currentPage = Optional.ofNullable(this.pageHistory.isEmpty() ? null : this.pageHistory.pop());
/*     */         updatePage(this.dialog, false);
/*     */       });
/* 152 */     this.BUTTON_NEXT = new ButtonType(Localization.localize(Localization.asKey("wizard.next.button")), ButtonBar.ButtonData.NEXT_FORWARD);
/* 153 */     this.BUTTON_NEXT_ACTION_HANDLER = (actionEvent -> {
/*     */         actionEvent.consume();
/*     */         
/*     */         this.currentPage.ifPresent(());
/*     */         this.currentPage = getFlow().advance(this.currentPage.orElse(null));
/*     */         updatePage(this.dialog, true);
/*     */       });
/* 160 */     this.titleProperty = (StringProperty)new SimpleStringProperty();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     this.flow = (ObjectProperty<Flow>)new SimpleObjectProperty<Flow>(new LinearFlow(new WizardPane[0])) {
/*     */         protected void invalidated() {
/* 292 */           Wizard.this.updatePage(Wizard.this.dialog, false);
/*     */         }
/*     */         
/*     */         public void set(Wizard.Flow flow) {
/* 296 */           super.set(flow);
/* 297 */           Wizard.this.pageHistory.clear();
/* 298 */           if (flow != null) {
/* 299 */             Wizard.this.currentPage = flow.advance(Wizard.this.currentPage.orElse(null));
/* 300 */             Wizard.this.updatePage(Wizard.this.dialog, true);
/*     */           } 
/*     */         }
/*     */       }; this.invalidProperty.addListener((o, ov, nv) -> validateActionState()); this.dialog = new Dialog(); this.dialog.titleProperty().bind((ObservableValue)this.titleProperty); setTitle(title); Window window = null; if (owner instanceof Window) { window = (Window)owner; }
/*     */     else if (owner instanceof Node)
/*     */     { window = ((Node)owner).getScene().getWindow(); }
/* 306 */      this.dialog.initOwner(window); } public Wizard() { this(null); } public Wizard(Object owner) { this(owner, ""); } public final ObjectProperty<Flow> flowProperty() { return this.flow; }
/*     */   public final Optional<ButtonType> showAndWait() { return this.dialog.showAndWait(); }
/*     */   public final ObjectProperty<ButtonType> resultProperty() { return this.dialog.resultProperty(); }
/*     */   public final ObservableMap<String, Object> getSettings() { return this.settings; }
/*     */   public final StringProperty titleProperty() { return this.titleProperty; }
/*     */   public final String getTitle() { return (String)this.titleProperty.get(); } public final void setTitle(String title) {
/*     */     this.titleProperty.set(title);
/*     */   } public final Flow getFlow() {
/* 314 */     return (Flow)this.flow.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setFlow(Flow flow) {
/* 321 */     this.flow.set(flow);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 326 */   private static final Object USER_DATA_KEY = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObservableMap<Object, Object> properties;
/*     */ 
/*     */ 
/*     */   
/*     */   private int settingCounter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ObservableMap<Object, Object> getProperties() {
/* 341 */     if (this.properties == null) {
/* 342 */       this.properties = FXCollections.observableMap(new HashMap<>());
/*     */     }
/* 344 */     return this.properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasProperties() {
/* 352 */     return (this.properties != null && !this.properties.isEmpty());
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
/*     */   public void setUserData(Object value) {
/* 367 */     getProperties().put(USER_DATA_KEY, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getUserData() {
/* 378 */     return getProperties().get(USER_DATA_KEY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setInvalid(boolean invalid) {
/* 388 */     this.invalidProperty.set(invalid);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isInvalid() {
/* 398 */     return this.invalidProperty.get();
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
/*     */   public final BooleanProperty invalidProperty() {
/* 416 */     return this.invalidProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setReadSettings(boolean readSettings) {
/* 426 */     this.readSettingsProperty.set(readSettings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isReadSettings() {
/* 436 */     return this.readSettingsProperty.get();
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
/*     */   public final BooleanProperty readSettingsProperty() {
/* 448 */     return this.readSettingsProperty;
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
/*     */   private void updatePage(Dialog<ButtonType> dialog, boolean advancing) {
/* 460 */     Flow flow = getFlow();
/* 461 */     if (flow == null) {
/*     */       return;
/*     */     }
/*     */     
/* 465 */     Optional<WizardPane> prevPage = Optional.ofNullable(this.pageHistory.isEmpty() ? null : this.pageHistory.peek());
/* 466 */     prevPage.ifPresent(page -> {
/*     */           if (advancing && isReadSettings()) {
/*     */             readSettings(page);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           page.onExitingPage(this);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 480 */     this.currentPage.ifPresent(currentPage -> {
/*     */           ObservableList<ButtonType> observableList = currentPage.getButtonTypes();
/*     */           
/*     */           if (!observableList.contains(this.BUTTON_PREVIOUS)) {
/*     */             observableList.add(this.BUTTON_PREVIOUS);
/*     */             
/*     */             Button button = (Button)currentPage.lookupButton(this.BUTTON_PREVIOUS);
/*     */             
/*     */             button.addEventFilter(ActionEvent.ACTION, this.BUTTON_PREVIOUS_ACTION_HANDLER);
/*     */           } 
/*     */           
/*     */           if (!observableList.contains(this.BUTTON_NEXT)) {
/*     */             observableList.add(this.BUTTON_NEXT);
/*     */             
/*     */             Button button = (Button)currentPage.lookupButton(this.BUTTON_NEXT);
/*     */             
/*     */             button.addEventFilter(ActionEvent.ACTION, this.BUTTON_NEXT_ACTION_HANDLER);
/*     */           } 
/*     */           
/*     */           if (!observableList.contains(ButtonType.FINISH)) {
/*     */             observableList.add(ButtonType.FINISH);
/*     */           }
/*     */           
/*     */           if (!observableList.contains(ButtonType.CANCEL)) {
/*     */             observableList.add(ButtonType.CANCEL);
/*     */           }
/*     */           
/*     */           currentPage.onEnteringPage(this);
/*     */           
/*     */           if (currentPage.getParent() != null && currentPage.getParent() instanceof Pane) {
/*     */             Pane parentOfCurrentPage = (Pane)currentPage.getParent();
/*     */             
/*     */             parentOfCurrentPage.getChildren().remove(currentPage);
/*     */           } 
/*     */           double previousX = dialog.getX();
/*     */           double previousY = dialog.getY();
/*     */           double previousWidth = dialog.getWidth();
/*     */           double previousHeight = dialog.getHeight();
/*     */           dialog.setDialogPane(currentPage);
/*     */           Window wizard = currentPage.getScene().getWindow();
/*     */           wizard.sizeToScene();
/*     */           if (!Double.isNaN(previousX) && !Double.isNaN(previousY)) {
/*     */             double newWidth = dialog.getWidth();
/*     */             double newHeight = dialog.getHeight();
/*     */             int newX = (int)(previousX + previousWidth / 2.0D - newWidth / 2.0D);
/*     */             int newY = (int)(previousY + previousHeight / 2.0D - newHeight / 2.0D);
/*     */             ObservableList<Screen> screens = Screen.getScreensForRectangle(previousX, previousY, 1.0D, 1.0D);
/*     */             Screen screen = screens.isEmpty() ? Screen.getPrimary() : (Screen)screens.get(0);
/*     */             Rectangle2D scrBounds = screen.getBounds();
/*     */             int minX = (int)Math.round(scrBounds.getMinX());
/*     */             int maxX = (int)Math.round(scrBounds.getMaxX());
/*     */             int minY = (int)Math.round(scrBounds.getMinY());
/*     */             int maxY = (int)Math.round(scrBounds.getMaxY());
/*     */             if (newX + newWidth > maxX) {
/*     */               newX = maxX - (int)Math.round(newWidth);
/*     */             }
/*     */             if (newY + newHeight > maxY) {
/*     */               newY = maxY - (int)Math.round(newHeight);
/*     */             }
/*     */             if (newX < minX) {
/*     */               newX = minX;
/*     */             }
/*     */             if (newY < minY) {
/*     */               newY = minY;
/*     */             }
/*     */             dialog.setX(newX);
/*     */             dialog.setY(newY);
/*     */           } 
/*     */         });
/* 549 */     validateActionState();
/*     */   }
/*     */   
/*     */   private void validateActionState() {
/* 553 */     ObservableList observableList = this.dialog.getDialogPane().getButtonTypes();
/*     */     
/* 555 */     if (getFlow().canAdvance(this.currentPage.orElse(null))) {
/* 556 */       observableList.remove(ButtonType.FINISH);
/*     */     } else {
/* 558 */       observableList.remove(this.BUTTON_NEXT);
/*     */     } 
/*     */     
/* 561 */     validateButton(this.BUTTON_PREVIOUS, () -> this.pageHistory.isEmpty());
/* 562 */     validateButton(this.BUTTON_NEXT, () -> this.invalidProperty.get());
/* 563 */     validateButton(ButtonType.FINISH, () -> this.invalidProperty.get());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateButton(ButtonType buttonType, BooleanSupplier condition) {
/* 569 */     Button btn = (Button)this.dialog.getDialogPane().lookupButton(buttonType);
/* 570 */     if (btn != null) {
/* 571 */       Node focusOwner = (btn.getScene() != null) ? btn.getScene().getFocusOwner() : null;
/* 572 */       btn.setDisable(condition.getAsBoolean());
/* 573 */       if (focusOwner != null) {
/* 574 */         focusOwner.requestFocus();
/*     */       }
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
/*     */   private void readSettings(WizardPane page) {
/* 587 */     this.settingCounter = 0;
/* 588 */     checkNode(page.getContent());
/*     */   }
/*     */   
/*     */   private boolean checkNode(Node n) {
/* 592 */     boolean success = readSetting(n);
/*     */     
/* 594 */     if (success)
/*     */     {
/* 596 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 603 */     List<Node> children = ImplUtils.getChildren(n, true);
/*     */ 
/*     */ 
/*     */     
/* 607 */     boolean childSuccess = false;
/* 608 */     for (Node child : children) {
/* 609 */       childSuccess |= checkNode(child);
/*     */     }
/* 611 */     return childSuccess;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean readSetting(Node n) {
/* 616 */     if (n == null) {
/* 617 */       return false;
/*     */     }
/*     */     
/* 620 */     Object setting = ValueExtractor.getValue(n);
/*     */     
/* 622 */     if (setting != null) {
/*     */ 
/*     */       
/* 625 */       String settingName = n.getId();
/*     */ 
/*     */       
/* 628 */       if (settingName == null || settingName.isEmpty()) {
/* 629 */         settingName = "page_.setting_" + this.settingCounter;
/*     */       }
/*     */       
/* 632 */       getSettings().put(settingName, setting);
/*     */       
/* 634 */       this.settingCounter++;
/*     */     } 
/*     */     
/* 637 */     return (setting != null);
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
/*     */   public static interface Flow
/*     */   {
/*     */     Optional<WizardPane> advance(WizardPane param1WizardPane);
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
/*     */     boolean canAdvance(WizardPane param1WizardPane);
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
/*     */   public static class LinearFlow
/*     */     implements Flow
/*     */   {
/*     */     private final List<WizardPane> pages;
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
/*     */     public LinearFlow(Collection<WizardPane> pages) {
/* 696 */       this.pages = new ArrayList<>(pages);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LinearFlow(WizardPane... pages) {
/* 704 */       this(Arrays.asList(pages));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<WizardPane> advance(WizardPane currentPage) {
/* 709 */       int pageIndex = this.pages.indexOf(currentPage);
/* 710 */       return Optional.ofNullable(this.pages.get(++pageIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canAdvance(WizardPane currentPage) {
/* 715 */       int pageIndex = this.pages.indexOf(currentPage);
/* 716 */       return (this.pages.size() - 1 > pageIndex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Dialog<ButtonType> getDialog() {
/* 734 */     return this.dialog;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\dialog\Wizard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */