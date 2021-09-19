/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.binding.Bindings;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.SelectionMode;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.input.MouseButton;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.ColumnConstraints;
/*     */ import javafx.scene.layout.GridPane;
/*     */ import javafx.scene.layout.HBox;
/*     */ import javafx.scene.layout.Priority;
/*     */ import javafx.scene.layout.RowConstraints;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.scene.layout.VBox;
/*     */ import org.controlsfx.control.ListSelectionView;
/*     */ import org.controlsfx.glyphfont.FontAwesome;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListSelectionViewSkin<T>
/*     */   extends SkinBase<ListSelectionView<T>>
/*     */ {
/*     */   private GridPane gridPane;
/*     */   private final HBox horizontalButtonBox;
/*     */   private final VBox verticalButtonBox;
/*     */   private Button moveToTarget;
/*     */   private Button moveToTargetAll;
/*     */   private Button moveToSourceAll;
/*     */   private Button moveToSource;
/*     */   private ListView<T> sourceListView;
/*     */   private ListView<T> targetListView;
/*     */   
/*     */   public ListSelectionViewSkin(ListSelectionView<T> view) {
/*  68 */     super((Control)view);
/*     */     
/*  70 */     this.sourceListView = Objects.<ListView<T>>requireNonNull(createSourceListView(), "source list view can not be null");
/*     */     
/*  72 */     this.sourceListView.setId("source-list-view");
/*  73 */     this.sourceListView.setItems(view.getSourceItems());
/*     */     
/*  75 */     this.targetListView = Objects.<ListView<T>>requireNonNull(createTargetListView(), "target list view can not be null");
/*     */     
/*  77 */     this.targetListView.setId("target-list-view");
/*  78 */     this.targetListView.setItems(view.getTargetItems());
/*     */     
/*  80 */     this.sourceListView.cellFactoryProperty().bind((ObservableValue)view.cellFactoryProperty());
/*  81 */     this.targetListView.cellFactoryProperty().bind((ObservableValue)view.cellFactoryProperty());
/*     */     
/*  83 */     this.gridPane = createGridPane();
/*  84 */     this.horizontalButtonBox = createHorizontalButtonBox();
/*  85 */     this.verticalButtonBox = createVerticalButtonBox();
/*     */     
/*  87 */     getChildren().add(this.gridPane);
/*     */     
/*  89 */     InvalidationListener updateListener = o -> updateView();
/*     */     
/*  91 */     view.sourceHeaderProperty().addListener(updateListener);
/*  92 */     view.sourceFooterProperty().addListener(updateListener);
/*  93 */     view.targetHeaderProperty().addListener(updateListener);
/*  94 */     view.targetFooterProperty().addListener(updateListener);
/*     */     
/*  96 */     updateView();
/*     */     
/*  98 */     getSourceListView().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
/*     */           if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
/*     */             moveToTarget();
/*     */           }
/*     */         });
/*     */     
/* 104 */     getTargetListView().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
/*     */           if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
/*     */             moveToSource();
/*     */           }
/*     */         });
/*     */     
/* 110 */     view.orientationProperty().addListener(observable -> updateView());
/*     */   }
/*     */   
/*     */   private GridPane createGridPane() {
/* 114 */     GridPane gridPane = new GridPane();
/* 115 */     gridPane.getStyleClass().add("grid-pane");
/* 116 */     return gridPane;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setHorizontalViewConstraints() {
/* 121 */     this.gridPane.getColumnConstraints().clear();
/* 122 */     this.gridPane.getRowConstraints().clear();
/*     */     
/* 124 */     ColumnConstraints col1 = new ColumnConstraints();
/*     */     
/* 126 */     col1.setFillWidth(true);
/* 127 */     col1.setHgrow(Priority.ALWAYS);
/* 128 */     col1.setMaxWidth(Double.MAX_VALUE);
/* 129 */     col1.setPrefWidth(200.0D);
/*     */     
/* 131 */     ColumnConstraints col2 = new ColumnConstraints();
/* 132 */     col2.setFillWidth(true);
/* 133 */     col2.setHgrow(Priority.NEVER);
/*     */     
/* 135 */     ColumnConstraints col3 = new ColumnConstraints();
/* 136 */     col3.setFillWidth(true);
/* 137 */     col3.setHgrow(Priority.ALWAYS);
/* 138 */     col3.setMaxWidth(Double.MAX_VALUE);
/* 139 */     col3.setPrefWidth(200.0D);
/*     */     
/* 141 */     this.gridPane.getColumnConstraints().addAll((Object[])new ColumnConstraints[] { col1, col2, col3 });
/*     */     
/* 143 */     RowConstraints row1 = new RowConstraints();
/* 144 */     row1.setFillHeight(true);
/* 145 */     row1.setVgrow(Priority.NEVER);
/*     */     
/* 147 */     RowConstraints row2 = new RowConstraints();
/* 148 */     row2.setMaxHeight(Double.MAX_VALUE);
/* 149 */     row2.setPrefHeight(200.0D);
/* 150 */     row2.setVgrow(Priority.ALWAYS);
/*     */     
/* 152 */     RowConstraints row3 = new RowConstraints();
/* 153 */     row3.setFillHeight(true);
/* 154 */     row3.setVgrow(Priority.NEVER);
/*     */     
/* 156 */     this.gridPane.getRowConstraints().addAll((Object[])new RowConstraints[] { row1, row2, row3 });
/*     */   }
/*     */ 
/*     */   
/*     */   private void setVerticalViewConstraints() {
/* 161 */     this.gridPane.getColumnConstraints().clear();
/* 162 */     this.gridPane.getRowConstraints().clear();
/*     */     
/* 164 */     ColumnConstraints col1 = new ColumnConstraints();
/*     */     
/* 166 */     col1.setFillWidth(true);
/* 167 */     col1.setHgrow(Priority.ALWAYS);
/* 168 */     col1.setMaxWidth(Double.MAX_VALUE);
/* 169 */     col1.setPrefWidth(200.0D);
/*     */     
/* 171 */     this.gridPane.getColumnConstraints().addAll((Object[])new ColumnConstraints[] { col1 });
/*     */     
/* 173 */     RowConstraints row1 = new RowConstraints();
/* 174 */     row1.setFillHeight(true);
/* 175 */     row1.setVgrow(Priority.NEVER);
/*     */     
/* 177 */     RowConstraints row2 = new RowConstraints();
/* 178 */     row2.setMaxHeight(Double.MAX_VALUE);
/* 179 */     row2.setPrefHeight(200.0D);
/* 180 */     row2.setVgrow(Priority.ALWAYS);
/*     */     
/* 182 */     RowConstraints row3 = new RowConstraints();
/* 183 */     row3.setFillHeight(true);
/* 184 */     row3.setVgrow(Priority.NEVER);
/*     */     
/* 186 */     RowConstraints row4 = new RowConstraints();
/* 187 */     row4.setFillHeight(true);
/* 188 */     row4.setVgrow(Priority.NEVER);
/*     */     
/* 190 */     RowConstraints row5 = new RowConstraints();
/* 191 */     row5.setFillHeight(true);
/* 192 */     row5.setVgrow(Priority.NEVER);
/*     */     
/* 194 */     RowConstraints row6 = new RowConstraints();
/* 195 */     row6.setMaxHeight(Double.MAX_VALUE);
/* 196 */     row6.setPrefHeight(200.0D);
/* 197 */     row6.setVgrow(Priority.ALWAYS);
/*     */     
/* 199 */     RowConstraints row7 = new RowConstraints();
/* 200 */     row7.setFillHeight(true);
/* 201 */     row7.setVgrow(Priority.NEVER);
/*     */ 
/*     */     
/* 204 */     this.gridPane.getRowConstraints().addAll((Object[])new RowConstraints[] { row1, row2, row3, row4, row5, row6, row7 });
/*     */   }
/*     */ 
/*     */   
/*     */   private VBox createVerticalButtonBox() {
/* 209 */     VBox box = new VBox(5.0D);
/* 210 */     box.setFillWidth(true);
/*     */     
/* 212 */     FontAwesome fontAwesome = new FontAwesome();
/* 213 */     this
/* 214 */       .moveToTarget = new Button("", (Node)fontAwesome.create((Enum)FontAwesome.Glyph.ANGLE_RIGHT));
/* 215 */     this
/* 216 */       .moveToTargetAll = new Button("", (Node)fontAwesome.create((Enum)FontAwesome.Glyph.ANGLE_DOUBLE_RIGHT));
/*     */     
/* 218 */     this
/* 219 */       .moveToSource = new Button("", (Node)fontAwesome.create((Enum)FontAwesome.Glyph.ANGLE_LEFT));
/* 220 */     this
/* 221 */       .moveToSourceAll = new Button("", (Node)fontAwesome.create((Enum)FontAwesome.Glyph.ANGLE_DOUBLE_LEFT));
/*     */     
/* 223 */     updateButtons();
/* 224 */     box.getChildren().addAll((Object[])new Node[] { (Node)this.moveToTarget, (Node)this.moveToTargetAll, (Node)this.moveToSource, (Node)this.moveToSourceAll });
/* 225 */     return box;
/*     */   }
/*     */ 
/*     */   
/*     */   private HBox createHorizontalButtonBox() {
/* 230 */     HBox box = new HBox(5.0D);
/* 231 */     box.setFillHeight(true);
/*     */     
/* 233 */     FontAwesome fontAwesome = new FontAwesome();
/* 234 */     this
/* 235 */       .moveToTarget = new Button("", (Node)fontAwesome.create((Enum)FontAwesome.Glyph.ANGLE_DOWN));
/* 236 */     this
/* 237 */       .moveToTargetAll = new Button("", (Node)fontAwesome.create((Enum)FontAwesome.Glyph.ANGLE_DOUBLE_DOWN));
/*     */     
/* 239 */     this
/* 240 */       .moveToSource = new Button("", (Node)fontAwesome.create((Enum)FontAwesome.Glyph.ANGLE_UP));
/* 241 */     this
/* 242 */       .moveToSourceAll = new Button("", (Node)fontAwesome.create((Enum)FontAwesome.Glyph.ANGLE_DOUBLE_UP));
/*     */     
/* 244 */     updateButtons();
/* 245 */     box.getChildren().addAll((Object[])new Node[] { (Node)this.moveToTarget, (Node)this.moveToTargetAll, (Node)this.moveToSource, (Node)this.moveToSourceAll });
/* 246 */     return box;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateButtons() {
/* 251 */     this.moveToTarget.getStyleClass().add("move-to-target-button");
/* 252 */     this.moveToTargetAll.getStyleClass().add("move-to-target-all-button");
/* 253 */     this.moveToSource.getStyleClass().add("move-to-source-button");
/* 254 */     this.moveToSourceAll.getStyleClass().add("move-to-source-all-button");
/*     */     
/* 256 */     this.moveToTarget.setMaxWidth(Double.MAX_VALUE);
/* 257 */     this.moveToTargetAll.setMaxWidth(Double.MAX_VALUE);
/* 258 */     this.moveToSource.setMaxWidth(Double.MAX_VALUE);
/* 259 */     this.moveToSourceAll.setMaxWidth(Double.MAX_VALUE);
/*     */     
/* 261 */     getSourceListView().itemsProperty().addListener(it -> bindMoveAllButtonsToDataModel());
/*     */ 
/*     */     
/* 264 */     getTargetListView().itemsProperty().addListener(it -> bindMoveAllButtonsToDataModel());
/*     */ 
/*     */     
/* 267 */     getSourceListView().selectionModelProperty().addListener(it -> bindMoveButtonsToSelectionModel());
/*     */ 
/*     */     
/* 270 */     getTargetListView().selectionModelProperty().addListener(it -> bindMoveButtonsToSelectionModel());
/*     */ 
/*     */     
/* 273 */     bindMoveButtonsToSelectionModel();
/* 274 */     bindMoveAllButtonsToDataModel();
/*     */     
/* 276 */     this.moveToTarget.setOnAction(evt -> moveToTarget());
/* 277 */     this.moveToTargetAll.setOnAction(evt -> moveToTargetAll());
/* 278 */     this.moveToSource.setOnAction(evt -> moveToSource());
/* 279 */     this.moveToSourceAll.setOnAction(evt -> moveToSourceAll());
/*     */   }
/*     */   
/*     */   private void bindMoveAllButtonsToDataModel() {
/* 283 */     this.moveToTargetAll.disableProperty().bind(
/* 284 */         (ObservableValue)Bindings.isEmpty(getSourceListView().getItems()));
/*     */     
/* 286 */     this.moveToSourceAll.disableProperty().bind(
/* 287 */         (ObservableValue)Bindings.isEmpty(getTargetListView().getItems()));
/*     */   }
/*     */   
/*     */   private void bindMoveButtonsToSelectionModel() {
/* 291 */     this.moveToTarget.disableProperty().bind(
/* 292 */         (ObservableValue)Bindings.isEmpty(getSourceListView().getSelectionModel()
/* 293 */           .getSelectedItems()));
/*     */     
/* 295 */     this.moveToSource.disableProperty().bind(
/* 296 */         (ObservableValue)Bindings.isEmpty(getTargetListView().getSelectionModel()
/* 297 */           .getSelectedItems()));
/*     */   }
/*     */   
/*     */   private void updateView() {
/* 301 */     this.gridPane.getChildren().clear();
/*     */     
/* 303 */     Node sourceHeader = ((ListSelectionView)getSkinnable()).getSourceHeader();
/* 304 */     Node targetHeader = ((ListSelectionView)getSkinnable()).getTargetHeader();
/* 305 */     Node sourceFooter = ((ListSelectionView)getSkinnable()).getSourceFooter();
/* 306 */     Node targetFooter = ((ListSelectionView)getSkinnable()).getTargetFooter();
/*     */     
/* 308 */     ListView<T> sourceList = getSourceListView();
/* 309 */     ListView<T> targetList = getTargetListView();
/*     */     
/* 311 */     StackPane stackPane = new StackPane();
/* 312 */     stackPane.setAlignment(Pos.CENTER);
/*     */     
/* 314 */     Orientation orientation = ((ListSelectionView)getSkinnable()).getOrientation();
/*     */     
/* 316 */     if (orientation == Orientation.HORIZONTAL) {
/* 317 */       setHorizontalViewConstraints();
/*     */       
/* 319 */       if (sourceHeader != null) {
/* 320 */         this.gridPane.add(sourceHeader, 0, 0);
/*     */       }
/*     */       
/* 323 */       if (targetHeader != null) {
/* 324 */         this.gridPane.add(targetHeader, 2, 0);
/*     */       }
/*     */       
/* 327 */       if (sourceList != null) {
/* 328 */         this.gridPane.add((Node)sourceList, 0, 1);
/*     */       }
/*     */       
/* 331 */       if (targetList != null) {
/* 332 */         this.gridPane.add((Node)targetList, 2, 1);
/*     */       }
/*     */       
/* 335 */       if (sourceFooter != null) {
/* 336 */         this.gridPane.add(sourceFooter, 0, 2);
/*     */       }
/*     */       
/* 339 */       if (targetFooter != null) {
/* 340 */         this.gridPane.add(targetFooter, 2, 2);
/*     */       }
/*     */       
/* 343 */       stackPane.getChildren().add(this.verticalButtonBox);
/* 344 */       this.gridPane.add((Node)stackPane, 1, 1);
/*     */     } else {
/* 346 */       setVerticalViewConstraints();
/*     */       
/* 348 */       if (sourceHeader != null) {
/* 349 */         this.gridPane.add(sourceHeader, 0, 0);
/*     */       }
/*     */       
/* 352 */       if (targetHeader != null) {
/* 353 */         this.gridPane.add(targetHeader, 0, 4);
/*     */       }
/*     */       
/* 356 */       if (sourceList != null) {
/* 357 */         this.gridPane.add((Node)sourceList, 0, 1);
/*     */       }
/*     */       
/* 360 */       if (targetList != null) {
/* 361 */         this.gridPane.add((Node)targetList, 0, 5);
/*     */       }
/*     */       
/* 364 */       if (sourceFooter != null) {
/* 365 */         this.gridPane.add(sourceFooter, 0, 2);
/*     */       }
/*     */       
/* 368 */       if (targetFooter != null) {
/* 369 */         this.gridPane.add(targetFooter, 0, 6);
/*     */       }
/*     */       
/* 372 */       stackPane.getChildren().add(this.horizontalButtonBox);
/* 373 */       this.gridPane.add((Node)stackPane, 0, 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveToTarget() {
/* 378 */     move(getSourceListView(), getTargetListView());
/* 379 */     getSourceListView().getSelectionModel().clearSelection();
/*     */   }
/*     */   
/*     */   private void moveToTargetAll() {
/* 383 */     move(getSourceListView(), getTargetListView(), new ArrayList<>((Collection<? extends T>)
/* 384 */           getSourceListView().getItems()));
/* 385 */     getSourceListView().getSelectionModel().clearSelection();
/*     */   }
/*     */   
/*     */   private void moveToSource() {
/* 389 */     move(getTargetListView(), getSourceListView());
/* 390 */     getTargetListView().getSelectionModel().clearSelection();
/*     */   }
/*     */   
/*     */   private void moveToSourceAll() {
/* 394 */     move(getTargetListView(), getSourceListView(), new ArrayList<>((Collection<? extends T>)
/* 395 */           getTargetListView().getItems()));
/* 396 */     getTargetListView().getSelectionModel().clearSelection();
/*     */   }
/*     */ 
/*     */   
/*     */   private void move(ListView<T> viewA, ListView<T> viewB) {
/* 401 */     List<T> selectedItems = new ArrayList<>((Collection<? extends T>)viewA.getSelectionModel().getSelectedItems());
/* 402 */     move(viewA, viewB, selectedItems);
/*     */   }
/*     */   
/*     */   private void move(ListView<T> viewA, ListView<T> viewB, List<T> items) {
/* 406 */     viewA.getItems().removeAll(items);
/* 407 */     viewB.getItems().addAll(items);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ListView<T> getSourceListView() {
/* 416 */     return this.sourceListView;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ListView<T> getTargetListView() {
/* 425 */     return this.targetListView;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListView<T> createSourceListView() {
/* 436 */     return createListView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListView<T> createTargetListView() {
/* 447 */     return createListView();
/*     */   }
/*     */   
/*     */   private ListView<T> createListView() {
/* 451 */     ListView<T> view = new ListView();
/* 452 */     view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
/* 453 */     return view;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\ListSelectionViewSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */