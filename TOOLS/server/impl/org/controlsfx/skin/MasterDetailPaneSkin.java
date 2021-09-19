/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import javafx.animation.Animation;
/*     */ import javafx.animation.KeyFrame;
/*     */ import javafx.animation.KeyValue;
/*     */ import javafx.animation.Timeline;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.beans.value.WritableValue;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.geometry.Side;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.control.SplitPane;
/*     */ import javafx.scene.layout.Region;
/*     */ import javafx.util.Duration;
/*     */ import org.controlsfx.control.MasterDetailPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MasterDetailPaneSkin
/*     */   extends SkinBase<MasterDetailPane>
/*     */ {
/*     */   private boolean changing = false;
/*     */   private SplitPane splitPane;
/*  58 */   private final Timeline timeline = new Timeline();
/*  59 */   private BooleanProperty showDetailForTimeline = (BooleanProperty)new SimpleBooleanProperty();
/*     */   
/*     */   public MasterDetailPaneSkin(MasterDetailPane pane) {
/*  62 */     super((Control)pane);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     this.listenersDivider = new InvalidationListener()
/*     */       {
/*     */         public void invalidated(Observable arg0) {
/* 337 */           MasterDetailPaneSkin.this.changing = true;
/* 338 */           MasterDetailPaneSkin.this.splitPane.setDividerPosition(0, ((MasterDetailPane)MasterDetailPaneSkin.this.getSkinnable()).getDividerPosition());
/* 339 */           MasterDetailPaneSkin.this.changing = false;
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 451 */     this.updateDividerPositionListener = new ChangeListener<Number>()
/*     */       {
/*     */         public void changed(ObservableValue<? extends Number> ov, Number t, Number t1)
/*     */         {
/* 455 */           if (!MasterDetailPaneSkin.this.changing)
/* 456 */             ((MasterDetailPane)MasterDetailPaneSkin.this.getSkinnable()).setDividerPosition(t1.doubleValue()); 
/*     */         }
/*     */       };
/*     */     this.splitPane = new SplitPane();
/*     */     this.splitPane.setDividerPosition(0, pane.getDividerPosition());
/*     */     this.splitPane.getDividers().addListener(change -> {
/*     */           while (change.next()) {
/*     */             if (change.wasAdded()) {
/*     */               ((SplitPane.Divider)change.getAddedSubList().get(0)).positionProperty().addListener(this.updateDividerPositionListener);
/*     */               continue;
/*     */             } 
/*     */             if (change.wasRemoved())
/*     */               ((SplitPane.Divider)change.getRemoved().get(0)).positionProperty().removeListener(this.updateDividerPositionListener); 
/*     */           } 
/*     */         });
/*     */     SplitPane.setResizableWithParent(((MasterDetailPane)getSkinnable()).getDetailNode(), Boolean.valueOf(false));
/*     */     switch (((MasterDetailPane)getSkinnable()).getDetailSide()) {
/*     */       case HORIZONTAL:
/*     */       case VERTICAL:
/*     */         this.splitPane.setOrientation(Orientation.VERTICAL);
/*     */         break;
/*     */       case null:
/*     */       case null:
/*     */         this.splitPane.setOrientation(Orientation.HORIZONTAL);
/*     */         break;
/*     */     } 
/*     */     ((MasterDetailPane)getSkinnable()).masterNodeProperty().addListener((observable, oldNode, newNode) -> {
/*     */           if (oldNode != null)
/*     */             this.splitPane.getItems().remove(oldNode); 
/*     */           if (newNode != null) {
/*     */             updateMinAndMaxSizes();
/*     */             int masterIndex = 0;
/*     */             switch (this.splitPane.getOrientation()) {
/*     */               case HORIZONTAL:
/*     */                 switch (((MasterDetailPane)getSkinnable()).getDetailSide()) {
/*     */                   case null:
/*     */                     masterIndex = 1;
/*     */                     break;
/*     */                   case null:
/*     */                     masterIndex = 0;
/*     */                     break;
/*     */                 } 
/*     */                 throw new IllegalArgumentException("illegal details position " + ((MasterDetailPane)getSkinnable()).getDetailSide() + " for orientation " + this.splitPane.getOrientation());
/*     */               case VERTICAL:
/*     */                 switch (((MasterDetailPane)getSkinnable()).getDetailSide()) {
/*     */                   case VERTICAL:
/*     */                     masterIndex = 1;
/*     */                     break;
/*     */                   case HORIZONTAL:
/*     */                     masterIndex = 0;
/*     */                     break;
/*     */                 } 
/*     */                 throw new IllegalArgumentException("illegal details position " + ((MasterDetailPane)getSkinnable()).getDetailSide() + " for orientation " + this.splitPane.getOrientation());
/*     */             } 
/*     */             ObservableList<Node> observableList = this.splitPane.getItems();
/*     */             if (observableList.isEmpty()) {
/*     */               observableList.add(newNode);
/*     */             } else {
/*     */               observableList.add(masterIndex, newNode);
/*     */             } 
/*     */           } 
/*     */         });
/*     */     ((MasterDetailPane)getSkinnable()).detailNodeProperty().addListener((observable, oldNode, newNode) -> {
/*     */           if (oldNode != null)
/*     */             this.splitPane.getItems().remove(oldNode); 
/*     */           if (newNode != null && ((MasterDetailPane)getSkinnable()).isShowDetailNode()) {
/*     */             this.splitPane.setDividerPositions(new double[] { ((MasterDetailPane)getSkinnable()).getDividerPosition() });
/*     */             updateMinAndMaxSizes();
/*     */             SplitPane.setResizableWithParent(newNode, Boolean.valueOf(false));
/*     */             int detailsIndex = 0;
/*     */             switch (this.splitPane.getOrientation()) {
/*     */               case HORIZONTAL:
/*     */                 switch (((MasterDetailPane)getSkinnable()).getDetailSide()) {
/*     */                   case null:
/*     */                     detailsIndex = 0;
/*     */                     break;
/*     */                   case null:
/*     */                     detailsIndex = 1;
/*     */                     break;
/*     */                 } 
/*     */                 throw new IllegalArgumentException("illegal details position " + ((MasterDetailPane)getSkinnable()).getDetailSide() + " for orientation " + this.splitPane.getOrientation());
/*     */               case VERTICAL:
/*     */                 switch (((MasterDetailPane)getSkinnable()).getDetailSide()) {
/*     */                   case VERTICAL:
/*     */                     detailsIndex = 0;
/*     */                     break;
/*     */                   case HORIZONTAL:
/*     */                     detailsIndex = 1;
/*     */                     break;
/*     */                 } 
/*     */                 throw new IllegalArgumentException("illegal details position " + ((MasterDetailPane)getSkinnable()).getDetailSide() + " for orientation " + this.splitPane.getOrientation());
/*     */             } 
/*     */             ObservableList<Node> observableList = this.splitPane.getItems();
/*     */             if (observableList.isEmpty()) {
/*     */               observableList.add(newNode);
/*     */             } else {
/*     */               observableList.add(detailsIndex, newNode);
/*     */             } 
/*     */           } 
/*     */         });
/*     */     ((MasterDetailPane)getSkinnable()).showDetailNodeProperty().addListener((observable, oldShow, newShow) -> {
/*     */           if (((MasterDetailPane)getSkinnable()).isAnimated() && this.timeline.getStatus() == Animation.Status.RUNNING) {
/*     */             this.timeline.jumpTo("endAnimation");
/*     */             this.timeline.getOnFinished().handle(null);
/*     */           } 
/*     */           if (newShow.booleanValue()) {
/*     */             open();
/*     */           } else {
/*     */             close();
/*     */           } 
/*     */         });
/*     */     ((MasterDetailPane)getSkinnable()).detailSideProperty().addListener((observable, oldPos, newPos) -> {
/*     */           Node detailNode = ((MasterDetailPane)getSkinnable()).getDetailNode();
/*     */           Node masterNode = ((MasterDetailPane)getSkinnable()).getMasterNode();
/*     */           boolean showDetailNode = (((MasterDetailPane)getSkinnable()).isShowDetailNode() && detailNode != null);
/*     */           if (showDetailNode)
/*     */             this.splitPane.getItems().clear(); 
/*     */           switch (newPos) {
/*     */             case HORIZONTAL:
/*     */             case VERTICAL:
/*     */               this.splitPane.setOrientation(Orientation.VERTICAL);
/*     */               break;
/*     */             case null:
/*     */             case null:
/*     */               this.splitPane.setOrientation(Orientation.HORIZONTAL);
/*     */               break;
/*     */           } 
/*     */           switch (newPos) {
/*     */             case VERTICAL:
/*     */             case null:
/*     */               if (showDetailNode) {
/*     */                 this.splitPane.getItems().add(detailNode);
/*     */                 this.splitPane.getItems().add(masterNode);
/*     */               } 
/*     */               switch (oldPos) {
/*     */                 case HORIZONTAL:
/*     */                 case null:
/*     */                   ((MasterDetailPane)getSkinnable()).setDividerPosition(1.0D - ((MasterDetailPane)getSkinnable()).getDividerPosition());
/*     */                   break;
/*     */               } 
/*     */               break;
/*     */             case HORIZONTAL:
/*     */             case null:
/*     */               if (showDetailNode) {
/*     */                 this.splitPane.getItems().add(masterNode);
/*     */                 this.splitPane.getItems().add(detailNode);
/*     */               } 
/*     */               switch (oldPos) {
/*     */                 case VERTICAL:
/*     */                 case null:
/*     */                   ((MasterDetailPane)getSkinnable()).setDividerPosition(1.0D - ((MasterDetailPane)getSkinnable()).getDividerPosition());
/*     */                   break;
/*     */               } 
/*     */               break;
/*     */           } 
/*     */           if (showDetailNode)
/*     */             this.splitPane.setDividerPositions(new double[] { ((MasterDetailPane)getSkinnable()).getDividerPosition() }); 
/*     */         });
/*     */     updateMinAndMaxSizes();
/*     */     getChildren().add(this.splitPane);
/*     */     this.splitPane.getItems().add(((MasterDetailPane)getSkinnable()).getMasterNode());
/*     */     if (((MasterDetailPane)getSkinnable()).isShowDetailNode()) {
/*     */       switch (((MasterDetailPane)getSkinnable()).getDetailSide()) {
/*     */         case VERTICAL:
/*     */         case null:
/*     */           this.splitPane.getItems().add(0, ((MasterDetailPane)getSkinnable()).getDetailNode());
/*     */           break;
/*     */         case HORIZONTAL:
/*     */         case null:
/*     */           this.splitPane.getItems().add(((MasterDetailPane)getSkinnable()).getDetailNode());
/*     */           break;
/*     */       } 
/*     */       bindDividerPosition();
/*     */     } 
/*     */     this.timeline.setOnFinished(evt -> {
/*     */           if (!this.showDetailForTimeline.get()) {
/*     */             unbindDividerPosition();
/*     */             this.splitPane.getItems().remove(((MasterDetailPane)getSkinnable()).getDetailNode());
/*     */             ((MasterDetailPane)getSkinnable()).getDetailNode().setOpacity(1.0D);
/*     */           } 
/*     */           this.changing = false;
/*     */         });
/*     */   }
/*     */   
/*     */   private InvalidationListener listenersDivider;
/*     */   private ChangeListener<Number> updateDividerPositionListener;
/*     */   
/*     */   private void bindDividerPosition() {
/*     */     ((MasterDetailPane)getSkinnable()).dividerPositionProperty().addListener(this.listenersDivider);
/*     */   }
/*     */   
/*     */   private void unbindDividerPosition() {
/*     */     ((MasterDetailPane)getSkinnable()).dividerPositionProperty().removeListener(this.listenersDivider);
/*     */   }
/*     */   
/*     */   private void updateMinAndMaxSizes() {
/*     */     if (((MasterDetailPane)getSkinnable()).getMasterNode() instanceof Region) {
/*     */       ((Region)((MasterDetailPane)getSkinnable()).getMasterNode()).setMinSize(0.0D, 0.0D);
/*     */       ((Region)((MasterDetailPane)getSkinnable()).getMasterNode()).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
/*     */     } 
/*     */     if (((MasterDetailPane)getSkinnable()).getDetailNode() instanceof Region) {
/*     */       ((Region)((MasterDetailPane)getSkinnable()).getDetailNode()).setMinSize(0.0D, 0.0D);
/*     */       ((Region)((MasterDetailPane)getSkinnable()).getDetailNode()).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void open() {
/*     */     this.changing = true;
/*     */     Node node = ((MasterDetailPane)getSkinnable()).getDetailNode();
/*     */     if (node == null)
/*     */       return; 
/*     */     switch (((MasterDetailPane)getSkinnable()).getDetailSide()) {
/*     */       case VERTICAL:
/*     */       case null:
/*     */         this.splitPane.getItems().add(0, node);
/*     */         this.splitPane.setDividerPositions(new double[] { 0.0D });
/*     */         break;
/*     */       case HORIZONTAL:
/*     */       case null:
/*     */         this.splitPane.getItems().add(node);
/*     */         this.splitPane.setDividerPositions(new double[] { 1.0D });
/*     */         break;
/*     */     } 
/*     */     updateMinAndMaxSizes();
/*     */     maybeAnimatePositionChange(((MasterDetailPane)getSkinnable()).getDividerPosition(), true);
/*     */   }
/*     */   
/*     */   private void close() {
/*     */     this.changing = true;
/*     */     if (!this.splitPane.getDividers().isEmpty()) {
/*     */       double targetLocation = 0.0D;
/*     */       switch (((MasterDetailPane)getSkinnable()).getDetailSide()) {
/*     */         case HORIZONTAL:
/*     */         case null:
/*     */           targetLocation = 1.0D;
/*     */           break;
/*     */       } 
/*     */       maybeAnimatePositionChange(targetLocation, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void maybeAnimatePositionChange(double position, boolean showDetail) {
/*     */     Node detailNode = ((MasterDetailPane)getSkinnable()).getDetailNode();
/*     */     if (detailNode == null)
/*     */       return; 
/*     */     this.showDetailForTimeline.set(showDetail);
/*     */     SplitPane.Divider divider = (SplitPane.Divider)this.splitPane.getDividers().get(0);
/*     */     if (this.showDetailForTimeline.get()) {
/*     */       unbindDividerPosition();
/*     */       bindDividerPosition();
/*     */     } 
/*     */     if (((MasterDetailPane)getSkinnable()).isAnimated() && detailNode != null) {
/*     */       KeyValue positionKeyValue = new KeyValue((WritableValue)divider.positionProperty(), Double.valueOf(position));
/*     */       KeyValue opacityKeyValue = new KeyValue((WritableValue)detailNode.opacityProperty(), Integer.valueOf(this.showDetailForTimeline.get() ? 1 : 0));
/*     */       KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.1D), "endAnimation", new KeyValue[] { positionKeyValue, opacityKeyValue });
/*     */       this.timeline.getKeyFrames().clear();
/*     */       this.timeline.getKeyFrames().add(keyFrame);
/*     */       this.timeline.playFromStart();
/*     */     } else {
/*     */       detailNode.setOpacity(1.0D);
/*     */       divider.setPosition(position);
/*     */       if (!this.showDetailForTimeline.get()) {
/*     */         unbindDividerPosition();
/*     */         this.splitPane.getItems().remove(((MasterDetailPane)getSkinnable()).getDetailNode());
/*     */       } 
/*     */       this.changing = false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\MasterDetailPaneSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */