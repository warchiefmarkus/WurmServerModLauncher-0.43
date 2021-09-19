/*     */ package impl.org.controlsfx.autocompletion;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.TextField;
/*     */ import javafx.util.Callback;
/*     */ import javafx.util.StringConverter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoCompletionTextFieldBinding<T>
/*     */   extends AutoCompletionBinding<T>
/*     */ {
/*     */   private StringConverter<T> converter;
/*     */   private final ChangeListener<String> textChangeListener;
/*     */   private final ChangeListener<Boolean> focusChangedListener;
/*     */   
/*     */   private static <T> StringConverter<T> defaultStringConverter() {
/*  53 */     return new StringConverter<T>() {
/*     */         public String toString(T t) {
/*  55 */           return (t == null) ? null : t.toString();
/*     */         }
/*     */         
/*     */         public T fromString(String string) {
/*  59 */           return (T)string;
/*     */         }
/*     */       };
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
/*     */   public AutoCompletionTextFieldBinding(TextField textField, Callback<AutoCompletionBinding.ISuggestionRequest, Collection<T>> suggestionProvider) {
/*  92 */     this(textField, suggestionProvider, 
/*  93 */         defaultStringConverter());
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
/*     */   public AutoCompletionTextFieldBinding(TextField textField, Callback<AutoCompletionBinding.ISuggestionRequest, Collection<T>> suggestionProvider, StringConverter<T> converter) {
/* 107 */     super((Node)textField, suggestionProvider, converter);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     this.textChangeListener = new ChangeListener<String>() {
/*     */         public void changed(ObservableValue<? extends String> obs, String oldText, String newText) {
/* 149 */           if (AutoCompletionTextFieldBinding.this.getCompletionTarget().isFocused()) {
/* 150 */             AutoCompletionTextFieldBinding.this.setUserInput(newText);
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 155 */     this.focusChangedListener = new ChangeListener<Boolean>() {
/*     */         public void changed(ObservableValue<? extends Boolean> obs, Boolean oldFocused, Boolean newFocused) {
/* 157 */           if (!newFocused.booleanValue())
/* 158 */             AutoCompletionTextFieldBinding.this.hidePopup(); 
/*     */         }
/*     */       };
/*     */     this.converter = converter;
/*     */     getCompletionTarget().textProperty().addListener(this.textChangeListener);
/*     */     getCompletionTarget().focusedProperty().addListener(this.focusChangedListener);
/*     */   }
/*     */   
/*     */   public TextField getCompletionTarget() {
/*     */     return (TextField)super.getCompletionTarget();
/*     */   }
/*     */   
/*     */   public void dispose() {
/*     */     getCompletionTarget().textProperty().removeListener(this.textChangeListener);
/*     */     getCompletionTarget().focusedProperty().removeListener(this.focusChangedListener);
/*     */   }
/*     */   
/*     */   protected void completeUserInput(T completion) {
/*     */     String newText = this.converter.toString(completion);
/*     */     getCompletionTarget().setText(newText);
/*     */     getCompletionTarget().positionCaret(newText.length());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\autocompletion\AutoCompletionTextFieldBinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */