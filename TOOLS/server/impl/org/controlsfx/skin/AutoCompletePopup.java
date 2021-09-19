/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.event.EventHandlerManager;
/*     */ import javafx.beans.property.IntegerProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.ObjectPropertyBase;
/*     */ import javafx.beans.property.SimpleIntegerProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventDispatchChain;
/*     */ import javafx.event.EventDispatcher;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventType;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.PopupControl;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.stage.Window;
/*     */ import javafx.util.StringConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoCompletePopup<T>
/*     */   extends PopupControl
/*     */ {
/*     */   private static final int TITLE_HEIGHT = 28;
/*  60 */   private final ObservableList<T> suggestions = FXCollections.observableArrayList();
/*     */ 
/*     */ 
/*     */   
/*     */   private StringConverter<T> converter;
/*     */ 
/*     */   
/*  67 */   private IntegerProperty visibleRowCount = (IntegerProperty)new SimpleIntegerProperty(this, "visibleRowCount", 10);
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandlerManager eventHandlerManager;
/*     */ 
/*     */   
/*     */   private ObjectProperty<EventHandler<SuggestionEvent<T>>> onSuggestion;
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_STYLE_CLASS = "auto-complete-popup";
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SuggestionEvent<TE>
/*     */     extends Event
/*     */   {
/*  84 */     public static final EventType<SuggestionEvent> SUGGESTION = new EventType("SUGGESTION");
/*     */     
/*     */     private final TE suggestion;
/*     */     
/*     */     public SuggestionEvent(TE suggestion) {
/*  89 */       super(SUGGESTION);
/*  90 */       this.suggestion = suggestion;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TE getSuggestion() {
/*  98 */       return this.suggestion;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableList<T> getSuggestions() {
/* 133 */     return this.suggestions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void show(Node node) {
/* 142 */     if (node.getScene() == null || node.getScene().getWindow() == null) {
/* 143 */       throw new IllegalStateException("Can not show popup. The node must be attached to a scene/window.");
/*     */     }
/* 145 */     if (isShowing()) {
/*     */       return;
/*     */     }
/*     */     
/* 149 */     Window parent = node.getScene().getWindow();
/* 150 */     show(parent, parent
/*     */         
/* 152 */         .getX() + node.localToScene(0.0D, 0.0D).getX() + node
/* 153 */         .getScene().getX(), parent
/* 154 */         .getY() + node.localToScene(0.0D, 0.0D).getY() + node
/* 155 */         .getScene().getY() + 28.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConverter(StringConverter<T> converter) {
/* 163 */     this.converter = converter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringConverter<T> getConverter() {
/* 170 */     return this.converter;
/*     */   }
/*     */   
/*     */   public final void setVisibleRowCount(int value) {
/* 174 */     this.visibleRowCount.set(value);
/*     */   }
/*     */   
/*     */   public final int getVisibleRowCount() {
/* 178 */     return this.visibleRowCount.get();
/*     */   }
/*     */   
/*     */   public final IntegerProperty visibleRowCountProperty() {
/* 182 */     return this.visibleRowCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutoCompletePopup() {
/* 192 */     this.eventHandlerManager = new EventHandlerManager(this);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     this.onSuggestion = (ObjectProperty<EventHandler<SuggestionEvent<T>>>)new ObjectPropertyBase<EventHandler<SuggestionEvent<T>>>()
/*     */       {
/*     */         protected void invalidated() {
/* 200 */           AutoCompletePopup.this.eventHandlerManager.setEventHandler(AutoCompletePopup.SuggestionEvent.SUGGESTION, (EventHandler)get());
/*     */         }
/*     */ 
/*     */         
/*     */         public Object getBean() {
/* 205 */           return AutoCompletePopup.this;
/*     */         }
/*     */         
/*     */         public String getName()
/*     */         {
/* 210 */           return "onSuggestion"; }
/*     */       };
/*     */     setAutoFix(true);
/*     */     setAutoHide(true);
/*     */     setHideOnEscape(true);
/*     */     getStyleClass().add("auto-complete-popup"); } public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
/* 216 */     return super.buildEventDispatchChain(tail).append((EventDispatcher)this.eventHandlerManager);
/*     */   }
/*     */   public final ObjectProperty<EventHandler<SuggestionEvent<T>>> onSuggestionProperty() {
/*     */     return this.onSuggestion;
/*     */   }
/*     */   public final void setOnSuggestion(EventHandler<SuggestionEvent<T>> value) {
/*     */     onSuggestionProperty().set(value);
/*     */   }
/*     */   
/*     */   public final EventHandler<SuggestionEvent<T>> getOnSuggestion() {
/*     */     return (EventHandler<SuggestionEvent<T>>)onSuggestionProperty().get();
/*     */   }
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/* 230 */     return new AutoCompletePopupSkin<>(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\AutoCompletePopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */