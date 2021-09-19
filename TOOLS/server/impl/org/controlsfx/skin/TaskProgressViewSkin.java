/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import java.util.List;
/*     */ import javafx.beans.binding.Bindings;
/*     */ import javafx.beans.value.ObservableBooleanValue;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.concurrent.Task;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.ContentDisplay;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.ListCell;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.ProgressBar;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.control.Tooltip;
/*     */ import javafx.scene.layout.BorderPane;
/*     */ import javafx.scene.layout.VBox;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.control.TaskProgressView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaskProgressViewSkin<T extends Task<?>>
/*     */   extends SkinBase<TaskProgressView<T>>
/*     */ {
/*     */   public TaskProgressViewSkin(TaskProgressView<T> monitor) {
/*  52 */     super((Control)monitor);
/*     */     
/*  54 */     BorderPane borderPane = new BorderPane();
/*  55 */     borderPane.getStyleClass().add("box");
/*     */ 
/*     */     
/*  58 */     ListView<T> listView = new ListView();
/*  59 */     listView.setPrefSize(500.0D, 400.0D);
/*  60 */     listView.setPlaceholder((Node)new Label("No tasks running"));
/*  61 */     listView.setCellFactory(param -> new TaskCell());
/*  62 */     listView.setFocusTraversable(false);
/*     */     
/*  64 */     Bindings.bindContent((List)listView.getItems(), monitor.getTasks());
/*  65 */     borderPane.setCenter((Node)listView);
/*     */     
/*  67 */     getChildren().add(listView);
/*     */   }
/*     */   
/*     */   class TaskCell
/*     */     extends ListCell<T> {
/*     */     private ProgressBar progressBar;
/*     */     private Label titleText;
/*     */     private Label messageText;
/*     */     private Button cancelButton;
/*     */     private T task;
/*     */     private BorderPane borderPane;
/*     */     
/*     */     public TaskCell() {
/*  80 */       this.titleText = new Label();
/*  81 */       this.titleText.getStyleClass().add("task-title");
/*     */       
/*  83 */       this.messageText = new Label();
/*  84 */       this.messageText.getStyleClass().add("task-message");
/*     */       
/*  86 */       this.progressBar = new ProgressBar();
/*  87 */       this.progressBar.setMaxWidth(Double.MAX_VALUE);
/*  88 */       this.progressBar.setMaxHeight(8.0D);
/*  89 */       this.progressBar.getStyleClass().add("task-progress-bar");
/*     */       
/*  91 */       this.cancelButton = new Button("Cancel");
/*  92 */       this.cancelButton.getStyleClass().add("task-cancel-button");
/*  93 */       this.cancelButton.setTooltip(new Tooltip("Cancel Task"));
/*  94 */       this.cancelButton.setOnAction(evt -> {
/*     */             if (this.task != null) {
/*     */               this.task.cancel();
/*     */             }
/*     */           });
/*     */       
/* 100 */       VBox vbox = new VBox();
/* 101 */       vbox.setSpacing(4.0D);
/* 102 */       vbox.getChildren().add(this.titleText);
/* 103 */       vbox.getChildren().add(this.progressBar);
/* 104 */       vbox.getChildren().add(this.messageText);
/*     */       
/* 106 */       BorderPane.setAlignment((Node)this.cancelButton, Pos.CENTER);
/* 107 */       BorderPane.setMargin((Node)this.cancelButton, new Insets(0.0D, 0.0D, 0.0D, 4.0D));
/*     */       
/* 109 */       this.borderPane = new BorderPane();
/* 110 */       this.borderPane.setCenter((Node)vbox);
/* 111 */       this.borderPane.setRight((Node)this.cancelButton);
/* 112 */       setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateIndex(int index) {
/* 117 */       super.updateIndex(index);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       if (index == -1) {
/* 124 */         setGraphic(null);
/* 125 */         getStyleClass().setAll((Object[])new String[] { "task-list-cell-empty" });
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void updateItem(T task, boolean empty) {
/* 131 */       super.updateItem(task, empty);
/*     */       
/* 133 */       this.task = task;
/*     */       
/* 135 */       if (empty || task == null) {
/* 136 */         getStyleClass().setAll((Object[])new String[] { "task-list-cell-empty" });
/* 137 */         setGraphic(null);
/* 138 */       } else if (task != null) {
/* 139 */         getStyleClass().setAll((Object[])new String[] { "task-list-cell" });
/* 140 */         this.progressBar.progressProperty().bind((ObservableValue)task.progressProperty());
/* 141 */         this.titleText.textProperty().bind((ObservableValue)task.titleProperty());
/* 142 */         this.messageText.textProperty().bind((ObservableValue)task.messageProperty());
/* 143 */         this.cancelButton.disableProperty().bind(
/* 144 */             (ObservableValue)Bindings.not((ObservableBooleanValue)task.runningProperty()));
/*     */         
/* 146 */         Callback<T, Node> factory = ((TaskProgressView)TaskProgressViewSkin.this.getSkinnable()).getGraphicFactory();
/* 147 */         if (factory != null) {
/* 148 */           Node graphic = (Node)factory.call(task);
/* 149 */           if (graphic != null) {
/* 150 */             BorderPane.setAlignment(graphic, Pos.CENTER);
/* 151 */             BorderPane.setMargin(graphic, new Insets(0.0D, 4.0D, 0.0D, 0.0D));
/* 152 */             this.borderPane.setLeft(graphic);
/*     */           
/*     */           }
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 160 */           this.borderPane.setLeft(null);
/*     */         } 
/*     */         
/* 163 */         setGraphic((Node)this.borderPane);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\TaskProgressViewSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */