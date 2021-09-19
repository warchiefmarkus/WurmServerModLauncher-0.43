/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import javafx.beans.binding.Bindings;
/*     */ import javafx.beans.value.ObservableNumberValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.Skinnable;
/*     */ import javafx.scene.control.cell.TextFieldListCell;
/*     */ import javafx.scene.input.KeyCode;
/*     */ import javafx.scene.input.KeyEvent;
/*     */ import javafx.scene.input.MouseButton;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import org.controlsfx.control.textfield.AutoCompletionBinding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoCompletePopupSkin<T>
/*     */   implements Skin<AutoCompletePopup<T>>
/*     */ {
/*     */   private final AutoCompletePopup<T> control;
/*     */   private final ListView<T> suggestionList;
/*  43 */   final int LIST_CELL_HEIGHT = 24;
/*     */   
/*     */   public AutoCompletePopupSkin(AutoCompletePopup<T> control) {
/*  46 */     this.control = control;
/*  47 */     this.suggestionList = new ListView(control.getSuggestions());
/*     */     
/*  49 */     this.suggestionList.getStyleClass().add("auto-complete-popup");
/*     */     
/*  51 */     this.suggestionList.getStylesheets().add(AutoCompletionBinding.class
/*  52 */         .getResource("autocompletion.css").toExternalForm());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     this.suggestionList.prefHeightProperty().bind(
/*  60 */         (ObservableValue)Bindings.min((ObservableNumberValue)control.visibleRowCountProperty(), (ObservableNumberValue)Bindings.size(this.suggestionList.getItems()))
/*  61 */         .multiply(24).add(18));
/*  62 */     this.suggestionList.setCellFactory(TextFieldListCell.forListView(control.getConverter()));
/*     */ 
/*     */     
/*  65 */     this.suggestionList.prefWidthProperty().bind((ObservableValue)control.prefWidthProperty());
/*  66 */     this.suggestionList.maxWidthProperty().bind((ObservableValue)control.maxWidthProperty());
/*  67 */     this.suggestionList.minWidthProperty().bind((ObservableValue)control.minWidthProperty());
/*  68 */     registerEventListener();
/*     */   }
/*     */   
/*     */   private void registerEventListener() {
/*  72 */     this.suggestionList.setOnMouseClicked(me -> {
/*     */           if (me.getButton() == MouseButton.PRIMARY) {
/*     */             onSuggestionChoosen((T)this.suggestionList.getSelectionModel().getSelectedItem());
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  79 */     this.suggestionList.setOnKeyPressed(ke -> {
/*     */           switch (ke.getCode()) {
/*     */             case TAB:
/*     */             case ENTER:
/*     */               onSuggestionChoosen((T)this.suggestionList.getSelectionModel().getSelectedItem());
/*     */               break;
/*     */             case ESCAPE:
/*     */               if (this.control.isHideOnEscape()) {
/*     */                 this.control.hide();
/*     */               }
/*     */               break;
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void onSuggestionChoosen(T suggestion) {
/*  97 */     if (suggestion != null) {
/*  98 */       Event.fireEvent((EventTarget)this.control, new AutoCompletePopup.SuggestionEvent<>(suggestion));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode() {
/* 105 */     return (Node)this.suggestionList;
/*     */   }
/*     */ 
/*     */   
/*     */   public AutoCompletePopup<T> getSkinnable() {
/* 110 */     return this.control;
/*     */   }
/*     */   
/*     */   public void dispose() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\AutoCompletePopupSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */