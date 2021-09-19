/*    */ package org.controlsfx.property.editor;
/*    */ 
/*    */ import javafx.beans.property.ObjectProperty;
/*    */ import javafx.beans.property.SimpleObjectProperty;
/*    */ import javafx.beans.property.StringProperty;
/*    */ import javafx.beans.value.ObservableValue;
/*    */ import javafx.scene.Cursor;
/*    */ import javafx.scene.Node;
/*    */ import javafx.scene.image.Image;
/*    */ import javafx.scene.image.ImageView;
/*    */ import javafx.scene.input.MouseButton;
/*    */ import javafx.scene.input.MouseEvent;
/*    */ import javafx.scene.layout.HBox;
/*    */ import javafx.scene.layout.Priority;
/*    */ import javafx.scene.layout.StackPane;
/*    */ import org.controlsfx.control.textfield.CustomTextField;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractObjectField<T>
/*    */   extends HBox
/*    */ {
/* 46 */   private static final Image image = new Image(AbstractObjectField.class.getResource("/org/controlsfx/control/open-editor.png").toExternalForm());
/*    */   
/* 48 */   private final CustomTextField textField = new CustomTextField();
/*    */   
/* 50 */   private ObjectProperty<T> objectProperty = (ObjectProperty<T>)new SimpleObjectProperty();
/*    */   
/*    */   public AbstractObjectField() {
/* 53 */     super(1.0D);
/* 54 */     this.textField.setEditable(false);
/* 55 */     this.textField.setFocusTraversable(false);
/*    */     
/* 57 */     StackPane button = new StackPane(new Node[] { (Node)new ImageView(image) });
/* 58 */     button.setCursor(Cursor.DEFAULT);
/*    */     
/* 60 */     button.setOnMouseReleased(e -> {
/*    */           if (MouseButton.PRIMARY == e.getButton()) {
/*    */             T result = edit((T)this.objectProperty.get());
/*    */             
/*    */             if (result != null) {
/*    */               this.objectProperty.set(result);
/*    */             }
/*    */           } 
/*    */         });
/* 69 */     this.textField.setRight((Node)button);
/* 70 */     getChildren().add(this.textField);
/* 71 */     HBox.setHgrow((Node)this.textField, Priority.ALWAYS);
/*    */     
/* 73 */     this.objectProperty.addListener((o, oldValue, newValue) -> textProperty().set(objectToString((T)newValue)));
/*    */   }
/*    */   
/*    */   protected StringProperty textProperty() {
/* 77 */     return this.textField.textProperty();
/*    */   }
/*    */   
/*    */   public ObjectProperty<T> getObjectProperty() {
/* 81 */     return this.objectProperty;
/*    */   }
/*    */   
/*    */   protected String objectToString(T object) {
/* 85 */     return (object == null) ? "" : object.toString();
/*    */   }
/*    */   
/*    */   protected abstract Class<T> getType();
/*    */   
/*    */   protected abstract T edit(T paramT);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\property\editor\AbstractObjectField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */