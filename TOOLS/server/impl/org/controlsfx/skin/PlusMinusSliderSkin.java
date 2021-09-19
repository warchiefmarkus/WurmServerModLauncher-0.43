/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import javafx.animation.AnimationTimer;
/*     */ import javafx.animation.KeyFrame;
/*     */ import javafx.animation.KeyValue;
/*     */ import javafx.animation.Timeline;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.beans.value.WritableValue;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.control.Slider;
/*     */ import javafx.scene.input.KeyEvent;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.layout.BorderPane;
/*     */ import javafx.scene.layout.Region;
/*     */ import javafx.util.Duration;
/*     */ import org.controlsfx.control.PlusMinusSlider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlusMinusSliderSkin
/*     */   extends SkinBase<PlusMinusSlider>
/*     */ {
/*     */   private SliderReader reader;
/*     */   private Slider slider;
/*     */   private Region plusRegion;
/*     */   private Region minusRegion;
/*     */   private BorderPane borderPane;
/*     */   
/*     */   public PlusMinusSliderSkin(PlusMinusSlider adjuster) {
/*  63 */     super((Control)adjuster);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     adjuster.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>()
/*     */         {
/*     */           public void handle(KeyEvent event) {
/*  74 */             event.consume();
/*     */           }
/*     */         });
/*     */     
/*  78 */     this.slider = new Slider(-1.0D, 1.0D, 0.0D);
/*     */     
/*  80 */     this.slider.valueProperty().addListener(new ChangeListener<Number>()
/*     */         {
/*     */           public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
/*     */           {
/*  84 */             ((PlusMinusSlider)PlusMinusSliderSkin.this.getSkinnable()).getProperties().put("plusminusslidervalue", 
/*  85 */                 Double.valueOf(newValue.doubleValue()));
/*     */           }
/*     */         });
/*     */     
/*  89 */     this.slider.orientationProperty().bind((ObservableValue)adjuster.orientationProperty());
/*     */     
/*  91 */     this.slider.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
/*     */         {
/*     */           public void handle(MouseEvent evt)
/*     */           {
/*  95 */             PlusMinusSliderSkin.this.reader = new PlusMinusSliderSkin.SliderReader();
/*  96 */             PlusMinusSliderSkin.this.reader.start();
/*     */           }
/*     */         });
/*     */     
/* 100 */     this.slider.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
/*     */         {
/*     */           public void handle(MouseEvent evt)
/*     */           {
/* 104 */             if (PlusMinusSliderSkin.this.reader != null) {
/* 105 */               PlusMinusSliderSkin.this.reader.stop();
/*     */             }
/*     */             
/* 108 */             KeyValue keyValue = new KeyValue((WritableValue)PlusMinusSliderSkin.this.slider.valueProperty(), Integer.valueOf(0));
/* 109 */             KeyFrame keyFrame = new KeyFrame(Duration.millis(100.0D), new KeyValue[] { keyValue });
/* 110 */             Timeline timeline = new Timeline(new KeyFrame[] { keyFrame });
/* 111 */             timeline.play();
/*     */           }
/*     */         });
/*     */     
/* 115 */     this.plusRegion = new Region();
/* 116 */     this.plusRegion.getStyleClass().add("adjust-plus");
/*     */     
/* 118 */     this.minusRegion = new Region();
/* 119 */     this.minusRegion.getStyleClass().add("adjust-minus");
/*     */     
/* 121 */     this.borderPane = new BorderPane();
/*     */     
/* 123 */     updateLayout(adjuster.getOrientation());
/*     */     
/* 125 */     getChildren().add(this.borderPane);
/*     */     
/* 127 */     adjuster.orientationProperty().addListener((observable, oldValue, newValue) -> updateLayout(newValue));
/*     */   }
/*     */   
/*     */   private void updateLayout(Orientation orientation) {
/* 131 */     this.borderPane.getChildren().clear();
/*     */     
/* 133 */     switch (orientation) {
/*     */       case HORIZONTAL:
/* 135 */         this.borderPane.setLeft((Node)this.minusRegion);
/* 136 */         this.borderPane.setCenter((Node)this.slider);
/* 137 */         this.borderPane.setRight((Node)this.plusRegion);
/*     */         break;
/*     */       case VERTICAL:
/* 140 */         this.borderPane.setTop((Node)this.plusRegion);
/* 141 */         this.borderPane.setCenter((Node)this.slider);
/* 142 */         this.borderPane.setBottom((Node)this.minusRegion);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   class SliderReader extends AnimationTimer {
/* 148 */     private long lastTime = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */     
/*     */     public void handle(long now) {
/* 153 */       if (now - this.lastTime > 10000000L) {
/* 154 */         this.lastTime = now;
/* 155 */         PlusMinusSliderSkin.this.slider.fireEvent((Event)new PlusMinusSlider.PlusMinusEvent(PlusMinusSliderSkin.this.slider, (EventTarget)PlusMinusSliderSkin.this.slider, PlusMinusSlider.PlusMinusEvent.VALUE_CHANGED, PlusMinusSliderSkin.this
/* 156 */               .slider.getValue()));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\PlusMinusSliderSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */