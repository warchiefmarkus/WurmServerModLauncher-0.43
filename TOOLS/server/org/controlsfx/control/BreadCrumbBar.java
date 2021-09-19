/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import com.sun.javafx.event.EventHandlerManager;
/*     */ import impl.org.controlsfx.skin.BreadCrumbBarSkin;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.ObjectPropertyBase;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventDispatchChain;
/*     */ import javafx.event.EventDispatcher;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventType;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.TreeItem;
/*     */ import javafx.util.Callback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BreadCrumbBar<T>
/*     */   extends ControlsFXControl
/*     */ {
/*  60 */   private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BreadCrumbActionEvent<TE>
/*     */     extends Event
/*     */   {
/*  75 */     public static final EventType<BreadCrumbActionEvent> CRUMB_ACTION = new EventType("CRUMB_ACTION");
/*     */ 
/*     */     
/*     */     private final TreeItem<TE> selectedCrumb;
/*     */ 
/*     */ 
/*     */     
/*     */     public BreadCrumbActionEvent(TreeItem<TE> selectedCrumb) {
/*  83 */       super(CRUMB_ACTION);
/*  84 */       this.selectedCrumb = selectedCrumb;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TreeItem<TE> getSelectedCrumb() {
/*  91 */       return this.selectedCrumb;
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
/*     */   public static <T> TreeItem<T> buildTreeModel(T... crumbs) {
/* 103 */     TreeItem<T> subRoot = null;
/* 104 */     for (T crumb : crumbs) {
/* 105 */       TreeItem<T> currentNode = new TreeItem(crumb);
/* 106 */       if (subRoot == null) {
/* 107 */         subRoot = currentNode;
/*     */       } else {
/* 109 */         subRoot.getChildren().add(currentNode);
/* 110 */         subRoot = currentNode;
/*     */       } 
/*     */     } 
/* 113 */     return subRoot;
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
/* 131 */   private final Callback<TreeItem<T>, Button> defaultCrumbNodeFactory = new Callback<TreeItem<T>, Button>()
/*     */     {
/*     */       public Button call(TreeItem<T> crumb) {
/* 134 */         return (Button)new BreadCrumbBarSkin.BreadCrumbButton((crumb.getValue() != null) ? crumb.getValue().toString() : "");
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private final ObjectProperty<TreeItem<T>> selectedCrumb;
/*     */   
/*     */   private final BooleanProperty autoNavigation;
/*     */   
/*     */   private final ObjectProperty<Callback<TreeItem<T>, Button>> crumbFactory;
/*     */   
/*     */   private ObjectProperty<EventHandler<BreadCrumbActionEvent<T>>> onCrumbAction;
/*     */   
/*     */   private static final String DEFAULT_STYLE_CLASS = "bread-crumb-bar";
/*     */ 
/*     */   
/*     */   public BreadCrumbBar() {
/* 151 */     this((TreeItem<T>)null);
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
/*     */   public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
/* 174 */     return tail.prepend((EventDispatcher)this.eventHandlerManager);
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
/*     */   
/*     */   public final ObjectProperty<TreeItem<T>> selectedCrumbProperty() {
/* 198 */     return this.selectedCrumb;
/*     */   }
/* 200 */   public BreadCrumbBar(TreeItem<T> selectedCrumb) { this.selectedCrumb = (ObjectProperty<TreeItem<T>>)new SimpleObjectProperty(this, "selectedCrumb");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     this.autoNavigation = (BooleanProperty)new SimpleBooleanProperty(this, "autoNavigationEnabled", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     this.crumbFactory = (ObjectProperty<Callback<TreeItem<T>, Button>>)new SimpleObjectProperty(this, "crumbFactory");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     this.onCrumbAction = (ObjectProperty<EventHandler<BreadCrumbActionEvent<T>>>)new ObjectPropertyBase<EventHandler<BreadCrumbActionEvent<T>>>()
/*     */       {
/*     */         protected void invalidated() {
/* 311 */           BreadCrumbBar.this.eventHandlerManager.setEventHandler(BreadCrumbBar.BreadCrumbActionEvent.CRUMB_ACTION, (EventHandler)get());
/*     */         }
/*     */ 
/*     */         
/*     */         public Object getBean() {
/* 316 */           return BreadCrumbBar.this;
/*     */         }
/*     */         
/*     */         public String getName()
/*     */         {
/* 321 */           return "onCrumbAction";
/*     */         }
/*     */       };
/*     */     getStyleClass().add("bread-crumb-bar");
/*     */     setSelectedCrumb(selectedCrumb);
/*     */     setCrumbFactory(this.defaultCrumbNodeFactory); }
/*     */ 
/*     */   
/*     */   public final TreeItem<T> getSelectedCrumb() {
/*     */     return (TreeItem<T>)this.selectedCrumb.get();
/*     */   }
/*     */   public final void setSelectedCrumb(TreeItem<T> selectedCrumb) {
/*     */     this.selectedCrumb.set(selectedCrumb);
/*     */   }
/*     */   
/* 336 */   protected Skin<?> createDefaultSkin() { return (Skin<?>)new BreadCrumbBarSkin(this); }
/*     */   public final BooleanProperty autoNavigationEnabledProperty() { return this.autoNavigation; }
/*     */   public final boolean isAutoNavigationEnabled() { return this.autoNavigation.get(); }
/*     */   public final void setAutoNavigationEnabled(boolean enabled) { this.autoNavigation.set(enabled); }
/*     */   public final ObjectProperty<Callback<TreeItem<T>, Button>> crumbFactoryProperty() { return this.crumbFactory; }
/* 341 */   public final void setCrumbFactory(Callback<TreeItem<T>, Button> value) { if (value == null) value = this.defaultCrumbNodeFactory;  crumbFactoryProperty().set(value); } public final Callback<TreeItem<T>, Button> getCrumbFactory() { return (Callback<TreeItem<T>, Button>)this.crumbFactory.get(); } public String getUserAgentStylesheet() { return getUserAgentStylesheet(BreadCrumbBar.class, "breadcrumbbar.css"); }
/*     */ 
/*     */   
/*     */   public final ObjectProperty<EventHandler<BreadCrumbActionEvent<T>>> onCrumbActionProperty() {
/*     */     return this.onCrumbAction;
/*     */   }
/*     */   
/*     */   public final void setOnCrumbAction(EventHandler<BreadCrumbActionEvent<T>> value) {
/*     */     onCrumbActionProperty().set(value);
/*     */   }
/*     */   
/*     */   public final EventHandler<BreadCrumbActionEvent<T>> getOnCrumbAction() {
/*     */     return (EventHandler<BreadCrumbActionEvent<T>>)onCrumbActionProperty().get();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\BreadCrumbBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */