/*    */ package impl.org.controlsfx.skin;
/*    */ 
/*    */ import javafx.beans.value.ObservableValue;
/*    */ import javafx.geometry.Pos;
/*    */ import javafx.scene.Node;
/*    */ import javafx.scene.control.Control;
/*    */ import javafx.scene.control.Label;
/*    */ import javafx.scene.control.SkinBase;
/*    */ import javafx.scene.layout.HBox;
/*    */ import javafx.scene.layout.StackPane;
/*    */ import javafx.scene.layout.VBox;
/*    */ import org.controlsfx.control.MaskerPane;
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
/*    */ public class MaskerPaneSkin
/*    */   extends SkinBase<MaskerPane>
/*    */ {
/*    */   public MaskerPaneSkin(MaskerPane maskerPane) {
/* 40 */     super((Control)maskerPane);
/* 41 */     getChildren().add(createMasker(maskerPane));
/*    */   }
/*    */   
/*    */   private StackPane createMasker(MaskerPane maskerPane) {
/* 45 */     VBox vBox = new VBox();
/* 46 */     vBox.setAlignment(Pos.CENTER);
/* 47 */     vBox.setSpacing(10.0D);
/* 48 */     vBox.getStyleClass().add("masker-center");
/*    */     
/* 50 */     vBox.getChildren().add(createLabel());
/* 51 */     vBox.getChildren().add(createProgressIndicator());
/*    */     
/* 53 */     HBox hBox = new HBox();
/* 54 */     hBox.setAlignment(Pos.CENTER);
/* 55 */     hBox.getChildren().addAll((Object[])new Node[] { (Node)vBox });
/*    */     
/* 57 */     StackPane glass = new StackPane();
/* 58 */     glass.setAlignment(Pos.CENTER);
/* 59 */     glass.getStyleClass().add("masker-glass");
/* 60 */     glass.getChildren().add(hBox);
/*    */     
/* 62 */     return glass;
/*    */   }
/*    */   
/*    */   private Label createLabel() {
/* 66 */     Label text = new Label();
/* 67 */     text.textProperty().bind((ObservableValue)((MaskerPane)getSkinnable()).textProperty());
/* 68 */     text.getStyleClass().add("masker-text");
/* 69 */     return text;
/*    */   }
/*    */   
/*    */   private Label createProgressIndicator() {
/* 73 */     Label graphic = new Label();
/* 74 */     graphic.setGraphic(((MaskerPane)getSkinnable()).getProgressNode());
/* 75 */     graphic.visibleProperty().bind((ObservableValue)((MaskerPane)getSkinnable()).progressVisibleProperty());
/* 76 */     graphic.getStyleClass().add("masker-graphic");
/* 77 */     return graphic;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\MaskerPaneSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */