/*     */ package org.controlsfx.control.textfield;
/*     */ 
/*     */ import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
/*     */ import impl.org.controlsfx.autocompletion.SuggestionProvider;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import javafx.animation.FadeTransition;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.Cursor;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.PasswordField;
/*     */ import javafx.scene.control.TextField;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.Region;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.util.Callback;
/*     */ import javafx.util.Duration;
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
/*     */ public class TextFields
/*     */ {
/*  60 */   private static final Duration FADE_DURATION = Duration.millis(350.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TextField createClearableTextField() {
/*  77 */     CustomTextField inputField = new CustomTextField();
/*  78 */     setupClearButtonField(inputField, inputField.rightProperty());
/*  79 */     return inputField;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PasswordField createClearablePasswordField() {
/*  87 */     CustomPasswordField inputField = new CustomPasswordField();
/*  88 */     setupClearButtonField((TextField)inputField, inputField.rightProperty());
/*  89 */     return inputField;
/*     */   }
/*     */   
/*     */   private static void setupClearButtonField(final TextField inputField, ObjectProperty<Node> rightProperty) {
/*  93 */     inputField.getStyleClass().add("clearable-field");
/*     */     
/*  95 */     Region clearButton = new Region();
/*  96 */     clearButton.getStyleClass().addAll((Object[])new String[] { "graphic" });
/*  97 */     StackPane clearButtonPane = new StackPane(new Node[] { (Node)clearButton });
/*  98 */     clearButtonPane.getStyleClass().addAll((Object[])new String[] { "clear-button" });
/*  99 */     clearButtonPane.setOpacity(0.0D);
/* 100 */     clearButtonPane.setCursor(Cursor.DEFAULT);
/* 101 */     clearButtonPane.setOnMouseReleased(e -> inputField.clear());
/* 102 */     clearButtonPane.managedProperty().bind((ObservableValue)inputField.editableProperty());
/* 103 */     clearButtonPane.visibleProperty().bind((ObservableValue)inputField.editableProperty());
/*     */     
/* 105 */     rightProperty.set(clearButtonPane);
/*     */     
/* 107 */     final FadeTransition fader = new FadeTransition(FADE_DURATION, (Node)clearButtonPane);
/* 108 */     fader.setCycleCount(1);
/*     */     
/* 110 */     inputField.textProperty().addListener(new InvalidationListener() {
/*     */           public void invalidated(Observable arg0) {
/* 112 */             String text = inputField.getText();
/* 113 */             boolean isTextEmpty = (text == null || text.isEmpty());
/* 114 */             boolean isButtonVisible = (fader.getNode().getOpacity() > 0.0D);
/*     */             
/* 116 */             if (isTextEmpty && isButtonVisible) {
/* 117 */               setButtonVisible(false);
/* 118 */             } else if (!isTextEmpty && !isButtonVisible) {
/* 119 */               setButtonVisible(true);
/*     */             } 
/*     */           }
/*     */           
/*     */           private void setButtonVisible(boolean visible) {
/* 124 */             fader.setFromValue(visible ? 0.0D : 1.0D);
/* 125 */             fader.setToValue(visible ? 1.0D : 0.0D);
/* 126 */             fader.play();
/*     */           }
/*     */         });
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
/*     */   public static <T> AutoCompletionBinding<T> bindAutoCompletion(TextField textField, Callback<AutoCompletionBinding.ISuggestionRequest, Collection<T>> suggestionProvider, StringConverter<T> converter) {
/* 151 */     return (AutoCompletionBinding<T>)new AutoCompletionTextFieldBinding(textField, suggestionProvider, converter);
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
/*     */   public static <T> AutoCompletionBinding<T> bindAutoCompletion(TextField textField, Callback<AutoCompletionBinding.ISuggestionRequest, Collection<T>> suggestionProvider) {
/* 168 */     return (AutoCompletionBinding<T>)new AutoCompletionTextFieldBinding(textField, suggestionProvider);
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
/*     */   public static <T> AutoCompletionBinding<T> bindAutoCompletion(TextField textField, T... possibleSuggestions) {
/* 181 */     return bindAutoCompletion(textField, Arrays.asList(possibleSuggestions));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> AutoCompletionBinding<T> bindAutoCompletion(TextField textField, Collection<T> possibleSuggestions) {
/* 186 */     return (AutoCompletionBinding<T>)new AutoCompletionTextFieldBinding(textField, 
/* 187 */         (Callback)SuggestionProvider.create(possibleSuggestions));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\textfield\TextFields.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */