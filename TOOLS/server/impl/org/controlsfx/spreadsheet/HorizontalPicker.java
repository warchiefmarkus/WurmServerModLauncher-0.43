/*     */ package impl.org.controlsfx.spreadsheet;
/*     */ 
/*     */ import com.sun.javafx.scene.control.skin.TableColumnHeader;
/*     */ import java.util.Collection;
/*     */ import java.util.Stack;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.Region;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import org.controlsfx.control.spreadsheet.Picker;
/*     */ import org.controlsfx.control.spreadsheet.SpreadsheetView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HorizontalPicker
/*     */   extends StackPane
/*     */ {
/*     */   private static final String PICKER_INDEX = "PickerIndex";
/*     */   private final HorizontalHeader horizontalHeader;
/*     */   private final SpreadsheetView spv;
/*     */   private final Stack<Label> pickerPile;
/*     */   private final Stack<Label> pickerUsed;
/*  59 */   private final InnerHorizontalPicker innerPicker = new InnerHorizontalPicker();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventHandler<MouseEvent> pickerMouseEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InvalidationListener layoutListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void layoutChildren() {
/*  84 */     this.innerPicker.relocate(this.horizontalHeader.getRootHeader().getLayoutX(), snappedTopInset());
/*     */     
/*  86 */     for (Label label : this.pickerUsed) {
/*  87 */       label.setVisible((label.getLayoutX() + this.innerPicker.getLayoutX() + label.getWidth() > this.horizontalHeader.gridViewSkin.fixedColumnWidth));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateScrollX() {
/*  95 */     requestLayout();
/*     */   }
/*     */   
/*     */   private Label getPicker(Picker picker) {
/*     */     Label pickerLabel;
/* 100 */     if (this.pickerPile.isEmpty()) {
/* 101 */       pickerLabel = new Label();
/* 102 */       pickerLabel.getStyleClass().addListener(this.layoutListener);
/* 103 */       pickerLabel.setOnMouseClicked(this.pickerMouseEvent);
/*     */     } else {
/* 105 */       pickerLabel = this.pickerPile.pop();
/*     */     } 
/* 107 */     this.pickerUsed.push(pickerLabel);
/* 108 */     pickerLabel.getStyleClass().setAll((Collection)picker.getStyleClass());
/* 109 */     pickerLabel.getProperties().put("PickerIndex", picker);
/* 110 */     return pickerLabel;
/*     */   }
/*     */   public HorizontalPicker(HorizontalHeader horizontalHeader, SpreadsheetView spv) {
/* 113 */     this.pickerMouseEvent = (mouseEvent -> {
/*     */         Label picker = (Label)mouseEvent.getSource();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         ((Picker)picker.getProperties().get("PickerIndex")).onClick();
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     this.layoutListener = (arg0 -> this.innerPicker.requestLayout());
/*     */     this.horizontalHeader = horizontalHeader;
/*     */     this.spv = spv;
/*     */     this.pickerPile = new Stack<>();
/*     */     this.pickerUsed = new Stack<>();
/*     */     Rectangle clip = new Rectangle();
/*     */     clip.setSmooth(true);
/*     */     clip.setHeight(16.0D);
/*     */     clip.widthProperty().bind((ObservableValue)horizontalHeader.widthProperty());
/*     */     setClip((Node)clip);
/*     */     getChildren().add(this.innerPicker);
/*     */     horizontalHeader.getRootHeader().getColumnHeaders().addListener(this.layoutListener);
/*     */     spv.getColumnPickers().addListener(this.layoutListener);
/*     */   }
/*     */   
/*     */   private class InnerHorizontalPicker extends Region {
/*     */     private InnerHorizontalPicker() {}
/*     */     
/*     */     protected void layoutChildren() {
/*     */       HorizontalPicker.this.pickerPile.addAll(HorizontalPicker.this.pickerUsed.subList(0, HorizontalPicker.this.pickerUsed.size()));
/*     */       for (Label label : HorizontalPicker.this.pickerUsed) {
/*     */         label.layoutXProperty().unbind();
/*     */         label.setVisible(true);
/*     */       } 
/*     */       HorizontalPicker.this.pickerUsed.clear();
/*     */       getChildren().clear();
/*     */       int index = 0;
/*     */       for (TableColumnHeader column : HorizontalPicker.this.horizontalHeader.getRootHeader().getColumnHeaders()) {
/*     */         int modelColumn = HorizontalPicker.this.spv.getModelColumn(index);
/*     */         if (HorizontalPicker.this.spv.getColumnPickers().containsKey(Integer.valueOf(modelColumn))) {
/*     */           Label label = HorizontalPicker.this.getPicker((Picker)HorizontalPicker.this.spv.getColumnPickers().get(Integer.valueOf(modelColumn)));
/*     */           label.resize(column.getWidth(), 16.0D);
/*     */           label.layoutXProperty().bind((ObservableValue)column.layoutXProperty());
/*     */           getChildren().add(0, label);
/*     */         } 
/*     */         index++;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\spreadsheet\HorizontalPicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */