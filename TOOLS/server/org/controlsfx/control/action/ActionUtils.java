/*     */ package org.controlsfx.control.action;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.binding.ObjectBinding;
/*     */ import javafx.beans.binding.StringBinding;
/*     */ import javafx.beans.binding.When;
/*     */ import javafx.beans.property.Property;
/*     */ import javafx.beans.value.ObservableBooleanValue;
/*     */ import javafx.beans.value.ObservableStringValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.MapChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.css.Styleable;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.ButtonBar;
/*     */ import javafx.scene.control.ButtonBase;
/*     */ import javafx.scene.control.CheckBox;
/*     */ import javafx.scene.control.CheckMenuItem;
/*     */ import javafx.scene.control.ContextMenu;
/*     */ import javafx.scene.control.Hyperlink;
/*     */ import javafx.scene.control.Menu;
/*     */ import javafx.scene.control.MenuBar;
/*     */ import javafx.scene.control.MenuButton;
/*     */ import javafx.scene.control.MenuItem;
/*     */ import javafx.scene.control.RadioButton;
/*     */ import javafx.scene.control.RadioMenuItem;
/*     */ import javafx.scene.control.Separator;
/*     */ import javafx.scene.control.SeparatorMenuItem;
/*     */ import javafx.scene.control.ToggleButton;
/*     */ import javafx.scene.control.ToolBar;
/*     */ import javafx.scene.control.Tooltip;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.layout.HBox;
/*     */ import javafx.scene.layout.Pane;
/*     */ import javafx.scene.layout.Priority;
/*     */ import javafx.scene.layout.VBox;
/*     */ import org.controlsfx.control.SegmentedButton;
/*     */ import org.controlsfx.tools.Duplicatable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActionUtils
/*     */ {
/*     */   public enum ActionTextBehavior
/*     */   {
/*  94 */     SHOW,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     HIDE;
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
/*     */   public static Button createButton(Action action, ActionTextBehavior textBehavior) {
/* 113 */     return configure(new Button(), action, textBehavior);
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
/*     */   public static Button createButton(Action action) {
/* 125 */     return configure(new Button(), action, ActionTextBehavior.SHOW);
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
/*     */   public static ButtonBase configureButton(Action action, ButtonBase button) {
/* 138 */     return configure(button, action, ActionTextBehavior.SHOW);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unconfigureButton(ButtonBase button) {
/* 149 */     unconfigure(button);
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
/*     */   public static MenuButton createMenuButton(Action action, ActionTextBehavior textBehavior) {
/* 162 */     return configure(new MenuButton(), action, textBehavior);
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
/*     */   public static MenuButton createMenuButton(Action action) {
/* 174 */     return configure(new MenuButton(), action, ActionTextBehavior.SHOW);
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
/*     */   public static Hyperlink createHyperlink(Action action) {
/* 186 */     return configure(new Hyperlink(), action, ActionTextBehavior.SHOW);
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
/*     */   public static ToggleButton createToggleButton(Action action, ActionTextBehavior textBehavior) {
/* 199 */     return configure(new ToggleButton(), action, textBehavior);
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
/*     */   public static ToggleButton createToggleButton(Action action) {
/* 211 */     return createToggleButton(action, ActionTextBehavior.SHOW);
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
/*     */   public static SegmentedButton createSegmentedButton(ActionTextBehavior textBehavior, Collection<? extends Action> actions) {
/* 223 */     ObservableList<ToggleButton> buttons = FXCollections.observableArrayList();
/* 224 */     for (Action a : actions) {
/* 225 */       buttons.add(createToggleButton(a, textBehavior));
/*     */     }
/* 227 */     return new SegmentedButton(buttons);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SegmentedButton createSegmentedButton(Collection<? extends Action> actions) {
/* 238 */     return createSegmentedButton(ActionTextBehavior.SHOW, actions);
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
/*     */   public static SegmentedButton createSegmentedButton(ActionTextBehavior textBehavior, Action... actions) {
/* 250 */     return createSegmentedButton(textBehavior, Arrays.asList(actions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SegmentedButton createSegmentedButton(Action... actions) {
/* 261 */     return createSegmentedButton(ActionTextBehavior.SHOW, Arrays.asList(actions));
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
/*     */   public static CheckBox createCheckBox(Action action) {
/* 275 */     return configure(new CheckBox(), action, ActionTextBehavior.SHOW);
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
/*     */   public static RadioButton createRadioButton(Action action) {
/* 287 */     return configure(new RadioButton(), action, ActionTextBehavior.SHOW);
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
/*     */   public static MenuItem createMenuItem(Action action) {
/* 301 */     MenuItem menuItem = action.getClass().isAnnotationPresent((Class)ActionCheck.class) ? (MenuItem)new CheckMenuItem() : new MenuItem();
/*     */     
/* 303 */     return configure(menuItem, action);
/*     */   }
/*     */   
/*     */   public static MenuItem configureMenuItem(Action action, MenuItem menuItem) {
/* 307 */     return configure(menuItem, action);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unconfigureMenuItem(MenuItem menuItem) {
/* 318 */     unconfigure(menuItem);
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
/*     */   public static Menu createMenu(Action action) {
/* 330 */     return configure(new Menu(), action);
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
/*     */   public static CheckMenuItem createCheckMenuItem(Action action) {
/* 342 */     return configure(new CheckMenuItem(), action);
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
/*     */   public static RadioMenuItem createRadioMenuItem(Action action) {
/* 354 */     return configure(new RadioMenuItem((String)action.textProperty().get()), action);
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
/* 371 */   public static Action ACTION_SEPARATOR = new Action(null, null) {
/*     */       public String toString() {
/* 373 */         return "Separator";
/*     */       }
/*     */     };
/*     */   
/* 377 */   public static Action ACTION_SPAN = new Action(null, null) {
/*     */       public String toString() {
/* 379 */         return "Span";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ToolBar createToolBar(Collection<? extends Action> actions, ActionTextBehavior textBehavior) {
/* 397 */     return updateToolBar(new ToolBar(), actions, textBehavior);
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
/*     */   public static ToolBar updateToolBar(ToolBar toolbar, Collection<? extends Action> actions, ActionTextBehavior textBehavior) {
/* 413 */     toolbar.getItems().clear();
/* 414 */     for (Action action : actions) {
/* 415 */       Button button; if (action instanceof ActionGroup) {
/* 416 */         MenuButton menu = createMenuButton(action, textBehavior);
/* 417 */         menu.setFocusTraversable(false);
/* 418 */         menu.getItems().addAll(toMenuItems((Collection<? extends Action>)((ActionGroup)action).getActions()));
/* 419 */         toolbar.getItems().add(menu); continue;
/* 420 */       }  if (action == ACTION_SEPARATOR) {
/* 421 */         toolbar.getItems().add(new Separator()); continue;
/* 422 */       }  if (action == ACTION_SPAN) {
/* 423 */         Pane span = new Pane();
/* 424 */         HBox.setHgrow((Node)span, Priority.ALWAYS);
/* 425 */         VBox.setVgrow((Node)span, Priority.ALWAYS);
/* 426 */         toolbar.getItems().add(span); continue;
/* 427 */       }  if (action == null) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 432 */       if (action.getClass().getAnnotation(ActionCheck.class) != null) {
/* 433 */         ToggleButton toggleButton = createToggleButton(action, textBehavior);
/*     */       } else {
/* 435 */         button = createButton(action, textBehavior);
/*     */       } 
/* 437 */       button.setFocusTraversable(false);
/* 438 */       toolbar.getItems().add(button);
/*     */     } 
/*     */ 
/*     */     
/* 442 */     return toolbar;
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
/*     */   public static MenuBar createMenuBar(Collection<? extends Action> actions) {
/* 456 */     return updateMenuBar(new MenuBar(), actions);
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
/*     */   public static MenuBar updateMenuBar(MenuBar menuBar, Collection<? extends Action> actions) {
/* 471 */     menuBar.getMenus().clear();
/* 472 */     for (Action action : actions) {
/*     */       
/* 474 */       if (action == ACTION_SEPARATOR || action == ACTION_SPAN)
/*     */         continue; 
/* 476 */       Menu menu = createMenu(action);
/*     */       
/* 478 */       if (action instanceof ActionGroup) {
/* 479 */         menu.getItems().addAll(toMenuItems((Collection<? extends Action>)((ActionGroup)action).getActions()));
/* 480 */       } else if (action == null) {
/*     */       
/*     */       } 
/*     */       
/* 484 */       menuBar.getMenus().add(menu);
/*     */     } 
/*     */     
/* 487 */     return menuBar;
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
/*     */   public static ButtonBar createButtonBar(Collection<? extends Action> actions) {
/* 501 */     return updateButtonBar(new ButtonBar(), actions);
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
/*     */   public static ButtonBar updateButtonBar(ButtonBar buttonBar, Collection<? extends Action> actions) {
/* 516 */     buttonBar.getButtons().clear();
/* 517 */     for (Action action : actions) {
/* 518 */       if (action instanceof ActionGroup)
/*     */         continue; 
/* 520 */       if (action == ACTION_SPAN || action == ACTION_SEPARATOR || action == null) {
/*     */         continue;
/*     */       }
/* 523 */       buttonBar.getButtons().add(createButton(action, ActionTextBehavior.SHOW));
/*     */     } 
/*     */ 
/*     */     
/* 527 */     return buttonBar;
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
/*     */   public static ContextMenu createContextMenu(Collection<? extends Action> actions) {
/* 541 */     return updateContextMenu(new ContextMenu(), actions);
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
/*     */   public static ContextMenu updateContextMenu(ContextMenu menu, Collection<? extends Action> actions) {
/* 556 */     menu.getItems().clear();
/* 557 */     menu.getItems().addAll(toMenuItems(actions));
/* 558 */     return menu;
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
/*     */   private static Collection<MenuItem> toMenuItems(Collection<? extends Action> actions) {
/* 570 */     Collection<MenuItem> items = new ArrayList<>();
/*     */     
/* 572 */     for (Action action : actions) {
/*     */       
/* 574 */       if (action instanceof ActionGroup) {
/*     */         
/* 576 */         Menu menu = createMenu(action);
/* 577 */         menu.getItems().addAll(toMenuItems((Collection<? extends Action>)((ActionGroup)action).getActions()));
/* 578 */         items.add(menu); continue;
/*     */       } 
/* 580 */       if (action == ACTION_SEPARATOR) {
/*     */         
/* 582 */         items.add(new SeparatorMenuItem()); continue;
/*     */       } 
/* 584 */       if (action == null || action == ACTION_SPAN) {
/*     */         continue;
/*     */       }
/*     */       
/* 588 */       items.add(createMenuItem(action));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 594 */     return items;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Node copyNode(Node node) {
/* 599 */     if (node instanceof ImageView)
/* 600 */       return (Node)new ImageView(((ImageView)node).getImage()); 
/* 601 */     if (node instanceof Duplicatable) {
/* 602 */       return (Node)((Duplicatable)node).duplicate();
/*     */     }
/* 604 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void bindStyle(final Styleable styleable, Action action) {
/* 611 */     styleable.getStyleClass().addAll((Collection)action.getStyleClass());
/* 612 */     action.getStyleClass().addListener(new ListChangeListener<String>()
/*     */         {
/*     */           public void onChanged(ListChangeListener.Change<? extends String> c) {
/* 615 */             while (c.next()) {
/* 616 */               if (c.wasRemoved()) {
/* 617 */                 styleable.getStyleClass().removeAll(c.getRemoved());
/*     */               }
/* 619 */               if (c.wasAdded()) {
/* 620 */                 styleable.getStyleClass().addAll(c.getAddedSubList());
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static <T extends ButtonBase> T configure(T btn, final Action action, ActionTextBehavior textBehavior) {
/* 628 */     if (action == null) {
/* 629 */       throw new NullPointerException("Action can not be null");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 634 */     bindStyle((Styleable)btn, action);
/*     */ 
/*     */     
/* 637 */     if (textBehavior == ActionTextBehavior.SHOW) {
/* 638 */       btn.textProperty().bind((ObservableValue)action.textProperty());
/*     */     }
/* 640 */     btn.disableProperty().bind((ObservableValue)action.disabledProperty());
/*     */ 
/*     */     
/* 643 */     btn.graphicProperty().bind((ObservableValue)new ObjectBinding<Node>()
/*     */         {
/*     */           protected Node computeValue()
/*     */           {
/* 647 */             return ActionUtils.copyNode((Node)action.graphicProperty().get());
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeListener(InvalidationListener listener) {
/* 652 */             super.removeListener(listener);
/* 653 */             unbind(new Observable[] { (Observable)this.val$action.graphicProperty() });
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 660 */     btn.getProperties().putAll((Map)action.getProperties());
/* 661 */     action.getProperties().addListener(new ButtonPropertiesMapChangeListener<>((ButtonBase)btn, action));
/*     */ 
/*     */ 
/*     */     
/* 665 */     btn.tooltipProperty().bind((ObservableValue)new ObjectBinding<Tooltip>()
/*     */         {
/*     */           private Tooltip tooltip;
/*     */ 
/*     */           
/*     */           private StringBinding textBinding;
/*     */ 
/*     */ 
/*     */           
/*     */           protected Tooltip computeValue() {
/* 675 */             String longText = this.textBinding.get();
/* 676 */             return (longText == null || this.textBinding.get().isEmpty()) ? null : this.tooltip;
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeListener(InvalidationListener listener) {
/* 681 */             super.removeListener(listener);
/* 682 */             unbind(new Observable[] { (Observable)this.val$action.longTextProperty() });
/* 683 */             this.tooltip.textProperty().unbind();
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 691 */     if (btn instanceof ToggleButton) {
/* 692 */       ((ToggleButton)btn).selectedProperty().bindBidirectional((Property)action.selectedProperty());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 697 */     btn.setOnAction(action);
/*     */     
/* 699 */     return btn;
/*     */   }
/*     */   
/*     */   private static void unconfigure(ButtonBase btn) {
/* 703 */     if (btn == null || !(btn.getOnAction() instanceof Action)) {
/*     */       return;
/*     */     }
/*     */     
/* 707 */     Action action = (Action)btn.getOnAction();
/*     */     
/* 709 */     btn.styleProperty().unbind();
/* 710 */     btn.textProperty().unbind();
/* 711 */     btn.disableProperty().unbind();
/* 712 */     btn.graphicProperty().unbind();
/*     */     
/* 714 */     action.getProperties().removeListener(new ButtonPropertiesMapChangeListener<>(btn, action));
/*     */     
/* 716 */     btn.tooltipProperty().unbind();
/*     */     
/* 718 */     if (btn instanceof ToggleButton) {
/* 719 */       ((ToggleButton)btn).selectedProperty().unbindBidirectional((Property)action.selectedProperty());
/*     */     }
/*     */     
/* 722 */     btn.setOnAction(null);
/*     */   }
/*     */   
/*     */   private static <T extends MenuItem> T configure(T menuItem, final Action action) {
/* 726 */     if (action == null) {
/* 727 */       throw new NullPointerException("Action can not be null");
/*     */     }
/*     */ 
/*     */     
/* 731 */     bindStyle((Styleable)menuItem, action);
/*     */     
/* 733 */     menuItem.textProperty().bind((ObservableValue)action.textProperty());
/* 734 */     menuItem.disableProperty().bind((ObservableValue)action.disabledProperty());
/* 735 */     menuItem.acceleratorProperty().bind((ObservableValue)action.acceleratorProperty());
/*     */     
/* 737 */     menuItem.graphicProperty().bind((ObservableValue)new ObjectBinding<Node>()
/*     */         {
/*     */           protected Node computeValue()
/*     */           {
/* 741 */             return ActionUtils.copyNode((Node)action.graphicProperty().get());
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeListener(InvalidationListener listener) {
/* 746 */             super.removeListener(listener);
/* 747 */             unbind(new Observable[] { (Observable)this.val$action.graphicProperty() });
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 754 */     menuItem.getProperties().putAll((Map)action.getProperties());
/* 755 */     action.getProperties().addListener(new MenuItemPropertiesMapChangeListener<>((MenuItem)menuItem, action));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 760 */     if (menuItem instanceof RadioMenuItem) {
/* 761 */       ((RadioMenuItem)menuItem).selectedProperty().bindBidirectional((Property)action.selectedProperty());
/* 762 */     } else if (menuItem instanceof CheckMenuItem) {
/* 763 */       ((CheckMenuItem)menuItem).selectedProperty().bindBidirectional((Property)action.selectedProperty());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 768 */     menuItem.setOnAction(action);
/*     */     
/* 770 */     return menuItem;
/*     */   }
/*     */   
/*     */   private static void unconfigure(MenuItem menuItem) {
/* 774 */     if (menuItem == null || !(menuItem.getOnAction() instanceof Action)) {
/*     */       return;
/*     */     }
/*     */     
/* 778 */     Action action = (Action)menuItem.getOnAction();
/*     */     
/* 780 */     menuItem.styleProperty().unbind();
/* 781 */     menuItem.textProperty().unbind();
/* 782 */     menuItem.disableProperty().unbind();
/* 783 */     menuItem.acceleratorProperty().unbind();
/* 784 */     menuItem.graphicProperty().unbind();
/*     */     
/* 786 */     action.getProperties().removeListener(new MenuItemPropertiesMapChangeListener<>(menuItem, action));
/*     */     
/* 788 */     if (menuItem instanceof RadioMenuItem) {
/* 789 */       ((RadioMenuItem)menuItem).selectedProperty().unbindBidirectional((Property)action.selectedProperty());
/* 790 */     } else if (menuItem instanceof CheckMenuItem) {
/* 791 */       ((CheckMenuItem)menuItem).selectedProperty().unbindBidirectional((Property)action.selectedProperty());
/*     */     } 
/*     */     
/* 794 */     menuItem.setOnAction(null);
/*     */   }
/*     */   
/*     */   private static class ButtonPropertiesMapChangeListener<T extends ButtonBase>
/*     */     implements MapChangeListener<Object, Object> {
/*     */     private final WeakReference<T> btnWeakReference;
/*     */     private final Action action;
/*     */     
/*     */     private ButtonPropertiesMapChangeListener(T btn, Action action) {
/* 803 */       this.btnWeakReference = new WeakReference<>(btn);
/* 804 */       this.action = action;
/*     */     }
/*     */     
/*     */     public void onChanged(MapChangeListener.Change<?, ?> change) {
/* 808 */       ButtonBase buttonBase = (ButtonBase)this.btnWeakReference.get();
/* 809 */       if (buttonBase == null) {
/* 810 */         this.action.getProperties().removeListener(this);
/*     */       } else {
/* 812 */         buttonBase.getProperties().clear();
/* 813 */         buttonBase.getProperties().putAll((Map)this.action.getProperties());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object otherObject) {
/* 819 */       if (this == otherObject) {
/* 820 */         return true;
/*     */       }
/* 822 */       if (otherObject == null || getClass() != otherObject.getClass()) {
/* 823 */         return false;
/*     */       }
/*     */       
/* 826 */       ButtonPropertiesMapChangeListener<?> otherListener = (ButtonPropertiesMapChangeListener)otherObject;
/*     */       
/* 828 */       ButtonBase buttonBase1 = (ButtonBase)this.btnWeakReference.get();
/* 829 */       ButtonBase otherBtn = (ButtonBase)otherListener.btnWeakReference.get();
/* 830 */       if ((buttonBase1 != null) ? !buttonBase1.equals(otherBtn) : (otherBtn != null)) {
/* 831 */         return false;
/*     */       }
/* 833 */       return this.action.equals(otherListener.action);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 838 */       ButtonBase buttonBase = (ButtonBase)this.btnWeakReference.get();
/* 839 */       int result = (buttonBase != null) ? buttonBase.hashCode() : 0;
/* 840 */       result = 31 * result + this.action.hashCode();
/* 841 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MenuItemPropertiesMapChangeListener<T extends MenuItem>
/*     */     implements MapChangeListener<Object, Object> {
/*     */     private final WeakReference<T> menuItemWeakReference;
/*     */     private final Action action;
/*     */     
/*     */     private MenuItemPropertiesMapChangeListener(T menuItem, Action action) {
/* 851 */       this.menuItemWeakReference = new WeakReference<>(menuItem);
/* 852 */       this.action = action;
/*     */     }
/*     */     
/*     */     public void onChanged(MapChangeListener.Change<?, ?> change) {
/* 856 */       MenuItem menuItem = (MenuItem)this.menuItemWeakReference.get();
/* 857 */       if (menuItem == null) {
/* 858 */         this.action.getProperties().removeListener(this);
/*     */       } else {
/* 860 */         menuItem.getProperties().clear();
/* 861 */         menuItem.getProperties().putAll((Map)this.action.getProperties());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object otherObject) {
/* 867 */       if (this == otherObject) {
/* 868 */         return true;
/*     */       }
/* 870 */       if (otherObject == null || getClass() != otherObject.getClass()) {
/* 871 */         return false;
/*     */       }
/*     */       
/* 874 */       MenuItemPropertiesMapChangeListener<?> otherListener = (MenuItemPropertiesMapChangeListener)otherObject;
/*     */       
/* 876 */       MenuItem menuItem1 = (MenuItem)this.menuItemWeakReference.get();
/* 877 */       MenuItem otherMenuItem = (MenuItem)otherListener.menuItemWeakReference.get();
/* 878 */       return (menuItem1 != null) ? menuItem1.equals(otherMenuItem) : ((otherMenuItem == null && this.action.equals(otherListener.action)));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 884 */       MenuItem menuItem = (MenuItem)this.menuItemWeakReference.get();
/* 885 */       int result = (menuItem != null) ? menuItem.hashCode() : 0;
/* 886 */       result = 31 * result + this.action.hashCode();
/* 887 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\action\ActionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */