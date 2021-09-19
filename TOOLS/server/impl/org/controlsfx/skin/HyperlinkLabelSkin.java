/*     */ package impl.org.controlsfx.skin;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Hyperlink;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.text.Text;
/*     */ import javafx.scene.text.TextFlow;
/*     */ import org.controlsfx.control.HyperlinkLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HyperlinkLabelSkin
/*     */   extends BehaviorSkinBase<HyperlinkLabel, BehaviorBase<HyperlinkLabel>>
/*     */ {
/*     */   private static final String HYPERLINK_START = "[";
/*     */   private static final String HYPERLINK_END = "]";
/*     */   private final TextFlow textFlow;
/*     */   
/*  69 */   private final EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
/*     */       public void handle(ActionEvent event) {
/*  71 */         EventHandler<ActionEvent> onActionHandler = ((HyperlinkLabel)HyperlinkLabelSkin.this.getSkinnable()).getOnAction();
/*  72 */         if (onActionHandler != null) {
/*  73 */           onActionHandler.handle((Event)event);
/*     */         }
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HyperlinkLabelSkin(HyperlinkLabel control) {
/*  87 */     super((Control)control, new BehaviorBase((Control)control, Collections.emptyList()));
/*     */     
/*  89 */     this.textFlow = new TextFlow();
/*  90 */     getChildren().add(this.textFlow);
/*  91 */     updateText();
/*     */     
/*  93 */     registerChangeListener((ObservableValue)control.textProperty(), "TEXT");
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
/*     */   protected void handleControlPropertyChanged(String p) {
/* 105 */     super.handleControlPropertyChanged(p);
/*     */     
/* 107 */     if (p == "TEXT") {
/* 108 */       updateText();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateText() {
/* 115 */     String text = ((HyperlinkLabel)getSkinnable()).getText();
/*     */     
/* 117 */     if (text == null || text.isEmpty()) {
/* 118 */       this.textFlow.getChildren().clear();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 123 */     List<Node> nodes = new ArrayList<>();
/*     */     
/* 125 */     int start = 0;
/* 126 */     int textLength = text.length();
/* 127 */     while (start != -1 && start < textLength) {
/* 128 */       int startPos = text.indexOf("[", start);
/* 129 */       int endPos = text.indexOf("]", startPos);
/*     */ 
/*     */       
/* 132 */       if ((startPos == -1 || endPos == -1) && 
/* 133 */         textLength > start) {
/*     */         
/* 135 */         Label label1 = new Label(text.substring(start));
/* 136 */         nodes.add(label1);
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 142 */       Text label = new Text(text.substring(start, startPos));
/* 143 */       nodes.add(label);
/*     */ 
/*     */       
/* 146 */       Hyperlink hyperlink = new Hyperlink(text.substring(startPos + 1, endPos));
/* 147 */       hyperlink.setPadding(new Insets(0.0D, 0.0D, 0.0D, 0.0D));
/* 148 */       hyperlink.setOnAction(this.eventHandler);
/* 149 */       nodes.add(hyperlink);
/*     */       
/* 151 */       start = endPos + 1;
/*     */     } 
/*     */     
/* 154 */     this.textFlow.getChildren().setAll(nodes);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\skin\HyperlinkLabelSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */