/*     */ package org.controlsfx.control.textfield;
/*     */ 
/*     */ import com.sun.javafx.event.EventHandlerManager;
/*     */ import impl.org.controlsfx.skin.AutoCompletePopup;
/*     */ import impl.org.controlsfx.skin.AutoCompletePopupSkin;
/*     */ import java.util.Collection;
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.IntegerProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.ObjectPropertyBase;
/*     */ import javafx.concurrent.Task;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventDispatchChain;
/*     */ import javafx.event.EventDispatcher;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.event.EventType;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.util.Callback;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AutoCompletionBinding<T>
/*     */   implements EventTarget
/*     */ {
/*     */   private final Node completionTarget;
/*     */   private final AutoCompletePopup<T> autoCompletionPopup;
/*  69 */   private final Object suggestionsTaskLock = new Object();
/*     */   
/*  71 */   private FetchSuggestionsTask suggestionsTask = null;
/*  72 */   private Callback<ISuggestionRequest, Collection<T>> suggestionProvider = null;
/*     */   private boolean ignoreInputChanges = false;
/*  74 */   private long delay = 250L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObjectProperty<EventHandler<AutoCompletionEvent<T>>> onAutoCompleted;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final EventHandlerManager eventHandlerManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHideOnEscape(boolean value) {
/* 125 */     this.autoCompletionPopup.setHideOnEscape(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setUserInput(String userText) {
/* 133 */     if (!isIgnoreInputChanges()) {
/* 134 */       onUserInputChanged(userText);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setDelay(long delay) {
/* 144 */     this.delay = delay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getCompletionTarget() {
/* 152 */     return this.completionTarget;
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
/*     */   public final void setVisibleRowCount(int value) {
/* 168 */     this.autoCompletionPopup.setVisibleRowCount(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getVisibleRowCount() {
/* 179 */     return this.autoCompletionPopup.getVisibleRowCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IntegerProperty visibleRowCountProperty() {
/* 190 */     return this.autoCompletionPopup.visibleRowCountProperty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPrefWidth(double value) {
/* 199 */     this.autoCompletionPopup.setPrefWidth(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getPrefWidth() {
/* 208 */     return this.autoCompletionPopup.getPrefWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final DoubleProperty prefWidthProperty() {
/* 216 */     return this.autoCompletionPopup.prefWidthProperty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMinWidth(double value) {
/* 225 */     this.autoCompletionPopup.setMinWidth(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getMinWidth() {
/* 234 */     return this.autoCompletionPopup.getMinWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final DoubleProperty minWidthProperty() {
/* 242 */     return this.autoCompletionPopup.minWidthProperty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMaxWidth(double value) {
/* 251 */     this.autoCompletionPopup.setMaxWidth(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getMaxWidth() {
/* 260 */     return this.autoCompletionPopup.getMaxWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final DoubleProperty maxWidthProperty() {
/* 268 */     return this.autoCompletionPopup.maxWidthProperty();
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
/*     */   protected void showPopup() {
/* 289 */     this.autoCompletionPopup.show(this.completionTarget);
/* 290 */     selectFirstSuggestion(this.autoCompletionPopup);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void hidePopup() {
/* 297 */     this.autoCompletionPopup.hide();
/*     */   }
/*     */   
/*     */   protected void fireAutoCompletion(T completion) {
/* 301 */     Event.fireEvent(this, new AutoCompletionEvent<>(completion));
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
/*     */   private void selectFirstSuggestion(AutoCompletePopup<?> autoCompletionPopup) {
/* 316 */     Skin<?> skin = autoCompletionPopup.getSkin();
/* 317 */     if (skin instanceof AutoCompletePopupSkin) {
/* 318 */       AutoCompletePopupSkin<?> au = (AutoCompletePopupSkin)skin;
/* 319 */       ListView<?> li = (ListView)au.getNode();
/* 320 */       if (li.getItems() != null && !li.getItems().isEmpty()) {
/* 321 */         li.getSelectionModel().select(0);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void onUserInputChanged(String userText) {
/* 331 */     synchronized (this.suggestionsTaskLock) {
/* 332 */       if (this.suggestionsTask != null && this.suggestionsTask.isRunning())
/*     */       {
/* 334 */         this.suggestionsTask.cancel();
/*     */       }
/*     */       
/* 337 */       this.suggestionsTask = new FetchSuggestionsTask(userText, this.delay);
/* 338 */       (new Thread((Runnable)this.suggestionsTask)).start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isIgnoreInputChanges() {
/* 347 */     return this.ignoreInputChanges;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setIgnoreInputChanges(boolean state) {
/* 357 */     this.ignoreInputChanges = state;
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
/*     */   private class FetchSuggestionsTask
/*     */     extends Task<Void>
/*     */     implements ISuggestionRequest
/*     */   {
/*     */     private final String userText;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final long delay;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FetchSuggestionsTask(String userText, long delay) {
/* 397 */       this.userText = userText;
/* 398 */       this.delay = delay;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Void call() throws Exception {
/* 403 */       Callback<AutoCompletionBinding.ISuggestionRequest, Collection<T>> provider = AutoCompletionBinding.this.suggestionProvider;
/* 404 */       if (provider != null) {
/* 405 */         long startTime = System.currentTimeMillis();
/* 406 */         long sleepTime = startTime + this.delay - System.currentTimeMillis();
/* 407 */         if (sleepTime > 0L && !isCancelled()) {
/* 408 */           Thread.sleep(sleepTime);
/*     */         }
/* 410 */         if (!isCancelled()) {
/* 411 */           Collection<T> fetchedSuggestions = (Collection<T>)provider.call(this);
/* 412 */           Platform.runLater(() -> {
/*     */                 if (fetchedSuggestions != null && !fetchedSuggestions.isEmpty()) {
/*     */                   AutoCompletionBinding.this.autoCompletionPopup.getSuggestions().setAll(fetchedSuggestions);
/*     */ 
/*     */                   
/*     */                   AutoCompletionBinding.this.showPopup();
/*     */                 } else {
/*     */                   AutoCompletionBinding.this.hidePopup();
/*     */                 } 
/*     */               });
/*     */         } 
/*     */       } else {
/* 424 */         AutoCompletionBinding.this.hidePopup();
/*     */       } 
/* 426 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getUserText() {
/* 431 */       return this.userText;
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
/*     */   public static class AutoCompletionEvent<TE>
/*     */     extends Event
/*     */   {
/* 455 */     public static final EventType<AutoCompletionEvent> AUTO_COMPLETED = new EventType("AUTO_COMPLETED");
/*     */ 
/*     */     
/*     */     private final TE completion;
/*     */ 
/*     */ 
/*     */     
/*     */     public AutoCompletionEvent(TE completion) {
/* 463 */       super(AUTO_COMPLETED);
/* 464 */       this.completion = completion;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TE getCompletion() {
/* 471 */       return this.completion;
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
/*     */   public final void setOnAutoCompleted(EventHandler<AutoCompletionEvent<T>> value) {
/* 483 */     onAutoCompletedProperty().set(value);
/*     */   }
/*     */   
/*     */   public final EventHandler<AutoCompletionEvent<T>> getOnAutoCompleted() {
/* 487 */     return (this.onAutoCompleted == null) ? null : (EventHandler<AutoCompletionEvent<T>>)this.onAutoCompleted.get();
/*     */   }
/*     */   
/*     */   public final ObjectProperty<EventHandler<AutoCompletionEvent<T>>> onAutoCompletedProperty() {
/* 491 */     if (this.onAutoCompleted == null) {
/* 492 */       this.onAutoCompleted = (ObjectProperty<EventHandler<AutoCompletionEvent<T>>>)new ObjectPropertyBase<EventHandler<AutoCompletionEvent<T>>>()
/*     */         {
/*     */           protected void invalidated() {
/* 495 */             AutoCompletionBinding.this.eventHandlerManager.setEventHandler(AutoCompletionBinding.AutoCompletionEvent.AUTO_COMPLETED, (EventHandler)
/*     */                 
/* 497 */                 get());
/*     */           }
/*     */ 
/*     */           
/*     */           public Object getBean() {
/* 502 */             return AutoCompletionBinding.this;
/*     */           }
/*     */ 
/*     */           
/*     */           public String getName() {
/* 507 */             return "onAutoCompleted";
/*     */           }
/*     */         };
/*     */     }
/* 511 */     return this.onAutoCompleted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AutoCompletionBinding(Node completionTarget, Callback<ISuggestionRequest, Collection<T>> suggestionProvider, StringConverter<T> converter) {
/* 521 */     this.eventHandlerManager = new EventHandlerManager(this); this.completionTarget = completionTarget;
/*     */     this.suggestionProvider = suggestionProvider;
/*     */     this.autoCompletionPopup = new AutoCompletePopup();
/*     */     this.autoCompletionPopup.setConverter(converter);
/*     */     this.autoCompletionPopup.setOnSuggestion(sce -> {
/*     */           try {
/*     */             setIgnoreInputChanges(true);
/*     */             completeUserInput((T)sce.getSuggestion());
/*     */             fireAutoCompletion((T)sce.getSuggestion());
/*     */             hidePopup();
/*     */           } finally {
/*     */             setIgnoreInputChanges(false);
/*     */           } 
/* 534 */         }); } public <E extends Event> void addEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) { this.eventHandlerManager.addEventHandler(eventType, eventHandler); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends Event> void removeEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
/* 549 */     this.eventHandlerManager.removeEventHandler(eventType, eventHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
/* 554 */     return tail.prepend((EventDispatcher)this.eventHandlerManager);
/*     */   }
/*     */   
/*     */   public abstract void dispose();
/*     */   
/*     */   protected abstract void completeUserInput(T paramT);
/*     */   
/*     */   public static interface ISuggestionRequest {
/*     */     boolean isCancelled();
/*     */     
/*     */     String getUserText();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\textfield\AutoCompletionBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */