/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.binding.Bindings;
/*     */ import javafx.beans.binding.BooleanBinding;
/*     */ import javafx.beans.value.ObservableNumberValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.ProgressBar;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.layout.GridPane;
/*     */ import javafx.scene.layout.HBox;
/*     */ import javafx.scene.layout.Priority;
/*     */ import org.controlsfx.control.StatusBar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StatusBarSkin
/*     */   extends SkinBase<StatusBar>
/*     */ {
/*     */   private HBox leftBox;
/*     */   private HBox rightBox;
/*     */   private Label label;
/*     */   private ProgressBar progressBar;
/*     */   
/*     */   public StatusBarSkin(StatusBar statusBar) {
/*  49 */     super((Control)statusBar);
/*  50 */     BooleanBinding notZeroProgressProperty = Bindings.notEqual(0, (ObservableNumberValue)statusBar.progressProperty());
/*     */     
/*  52 */     GridPane gridPane = new GridPane();
/*     */     
/*  54 */     this.leftBox = new HBox();
/*  55 */     this.leftBox.getStyleClass().add("left-items");
/*     */     
/*  57 */     this.rightBox = new HBox();
/*  58 */     this.rightBox.getStyleClass().add("right-items");
/*     */     
/*  60 */     this.progressBar = new ProgressBar();
/*  61 */     this.progressBar.progressProperty().bind((ObservableValue)statusBar.progressProperty());
/*  62 */     this.progressBar.visibleProperty().bind((ObservableValue)notZeroProgressProperty);
/*  63 */     this.progressBar.managedProperty().bind((ObservableValue)notZeroProgressProperty);
/*     */     
/*  65 */     this.label = new Label();
/*  66 */     this.label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
/*  67 */     this.label.textProperty().bind((ObservableValue)statusBar.textProperty());
/*  68 */     this.label.graphicProperty().bind((ObservableValue)statusBar.graphicProperty());
/*  69 */     this.label.styleProperty().bind((ObservableValue)((StatusBar)getSkinnable()).styleProperty());
/*  70 */     this.label.getStyleClass().add("status-label");
/*     */     
/*  72 */     this.leftBox.getChildren().setAll((Collection)((StatusBar)getSkinnable()).getLeftItems());
/*  73 */     this.rightBox.getChildren().setAll((Collection)((StatusBar)getSkinnable()).getRightItems());
/*     */     
/*  75 */     statusBar.getLeftItems().addListener(evt -> this.leftBox.getChildren().setAll((Collection)((StatusBar)getSkinnable()).getLeftItems()));
/*     */ 
/*     */ 
/*     */     
/*  79 */     statusBar.getRightItems().addListener(evt -> this.rightBox.getChildren().setAll((Collection)((StatusBar)getSkinnable()).getRightItems()));
/*     */ 
/*     */ 
/*     */     
/*  83 */     GridPane.setFillHeight((Node)this.leftBox, Boolean.valueOf(true));
/*  84 */     GridPane.setFillHeight((Node)this.rightBox, Boolean.valueOf(true));
/*  85 */     GridPane.setFillHeight((Node)this.label, Boolean.valueOf(true));
/*  86 */     GridPane.setFillHeight((Node)this.progressBar, Boolean.valueOf(true));
/*     */     
/*  88 */     GridPane.setVgrow((Node)this.leftBox, Priority.ALWAYS);
/*  89 */     GridPane.setVgrow((Node)this.rightBox, Priority.ALWAYS);
/*  90 */     GridPane.setVgrow((Node)this.label, Priority.ALWAYS);
/*  91 */     GridPane.setVgrow((Node)this.progressBar, Priority.ALWAYS);
/*     */     
/*  93 */     GridPane.setHgrow((Node)this.label, Priority.ALWAYS);
/*     */     
/*  95 */     gridPane.add((Node)this.leftBox, 0, 0);
/*  96 */     gridPane.add((Node)this.label, 1, 0);
/*  97 */     gridPane.add((Node)this.progressBar, 2, 0);
/*  98 */     gridPane.add((Node)this.rightBox, 3, 0);
/*     */     
/* 100 */     getChildren().add(gridPane);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\StatusBarSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */