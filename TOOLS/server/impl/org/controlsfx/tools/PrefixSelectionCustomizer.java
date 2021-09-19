/*     */ package impl.org.controlsfx.tools;
/*     */ 
/*     */ import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BiFunction;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventHandler;
/*     */ import javafx.scene.control.ChoiceBox;
/*     */ import javafx.scene.control.ComboBox;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.input.KeyCode;
/*     */ import javafx.scene.input.KeyEvent;
/*     */ import org.controlsfx.control.PrefixSelectionComboBox;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrefixSelectionCustomizer
/*     */ {
/*     */   public static final int DEFAULT_TYPING_DELAY = 500;
/*     */   private static final String SELECTION_PREFIX_STRING = "selectionPrefixString";
/* 109 */   private static final Object SELECTION_PREFIX_TASK = "selectionPrefixTask";
/*     */   static {
/* 111 */     DEFAULT_LOOKUP_COMBOBOX = ((comboBox, selection) -> 
/*     */       
/* 113 */       (comboBox == null || selection == null || selection.isEmpty()) ? Optional.empty() : comboBox.getItems().stream().filter(Objects::nonNull).filter(()).findFirst());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     DEFAULT_LOOKUP_CHOICEBOX = ((choiceBox, selection) -> 
/*     */       
/* 131 */       (choiceBox == null || selection == null || selection.isEmpty()) ? Optional.empty() : choiceBox.getItems().stream().filter(Objects::nonNull).filter(()).findFirst());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final BiFunction<ComboBox, String, Optional> DEFAULT_LOOKUP_COMBOBOX;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final BiFunction<ChoiceBox, String, Optional> DEFAULT_LOOKUP_CHOICEBOX;
/*     */ 
/*     */ 
/*     */   
/* 147 */   private static EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
/* 148 */       private ScheduledExecutorService executorService = null;
/*     */       
/*     */       private PrefixSelectionComboBox prefixSelectionComboBox;
/*     */       private int typingDelay;
/*     */       private Object result;
/*     */       
/*     */       public void handle(KeyEvent event) {
/* 155 */         keyPressed(event);
/*     */       }
/*     */       
/*     */       private <T> void keyPressed(KeyEvent event) {
/* 159 */         KeyCode code = event.getCode();
/* 160 */         if (code.isLetterKey() || code.isDigitKey() || code == KeyCode.SPACE || code == KeyCode.BACK_SPACE) {
/* 161 */           if (event.getSource() instanceof PrefixSelectionComboBox && 
/* 162 */             code == KeyCode.BACK_SPACE && !((PrefixSelectionComboBox)event.getSource()).isBackSpaceAllowed()) {
/*     */             return;
/*     */           }
/*     */           
/* 166 */           String letter = code.impl_getChar();
/* 167 */           if (event.getSource() instanceof ComboBox) {
/* 168 */             ComboBox<T> comboBox = (ComboBox<T>)event.getSource();
/* 169 */             T item = getEntryWithKey(letter, (Control)comboBox);
/* 170 */             if (item != null) {
/* 171 */               comboBox.setValue(item);
/*     */               
/* 173 */               ComboBoxListViewSkin<?> skin = (ComboBoxListViewSkin)comboBox.getSkin();
/* 174 */               ((ListView)skin.getPopupContent()).scrollTo(item);
/*     */             } 
/* 176 */           } else if (event.getSource() instanceof ChoiceBox) {
/* 177 */             ChoiceBox<T> choiceBox = (ChoiceBox<T>)event.getSource();
/* 178 */             T item = getEntryWithKey(letter, (Control)choiceBox);
/* 179 */             if (item != null) {
/* 180 */               choiceBox.setValue(item);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/*     */       private <T> T getEntryWithKey(String letter, Control control) {
/* 187 */         this.result = null;
/* 188 */         this.typingDelay = 500;
/* 189 */         this.prefixSelectionComboBox = (control instanceof PrefixSelectionComboBox) ? (PrefixSelectionComboBox)control : null;
/*     */         
/* 191 */         String selectionPrefixString = processInput((String)control.getProperties().get("selectionPrefixString"), letter);
/* 192 */         control.getProperties().put("selectionPrefixString", selectionPrefixString);
/*     */         
/* 194 */         if (this.prefixSelectionComboBox != null) {
/* 195 */           this.typingDelay = this.prefixSelectionComboBox.getTypingDelay();
/* 196 */           BiFunction<ComboBox, String, Optional> lookup = this.prefixSelectionComboBox.getLookup();
/* 197 */           if (lookup != null) {
/* 198 */             ((Optional)lookup.apply(this.prefixSelectionComboBox, selectionPrefixString)).ifPresent(t -> this.result = t);
/*     */           }
/* 200 */         } else if (control instanceof ComboBox) {
/* 201 */           ((Optional)PrefixSelectionCustomizer.DEFAULT_LOOKUP_COMBOBOX.apply((ComboBox)control, selectionPrefixString)).ifPresent(t -> this.result = t);
/* 202 */         } else if (control instanceof ChoiceBox) {
/* 203 */           ((Optional)PrefixSelectionCustomizer.DEFAULT_LOOKUP_CHOICEBOX.apply((ChoiceBox)control, selectionPrefixString)).ifPresent(t -> this.result = t);
/*     */         } 
/*     */         
/* 206 */         ScheduledFuture<?> task = (ScheduledFuture)control.getProperties().get(PrefixSelectionCustomizer.SELECTION_PREFIX_TASK);
/* 207 */         if (task != null) {
/* 208 */           task.cancel(false);
/*     */         }
/* 210 */         task = getExecutorService().schedule(() -> control.getProperties().put("selectionPrefixString", ""), this.typingDelay, TimeUnit.MILLISECONDS);
/*     */         
/* 212 */         control.getProperties().put(PrefixSelectionCustomizer.SELECTION_PREFIX_TASK, task);
/*     */         
/* 214 */         return (T)this.result;
/*     */       }
/*     */       
/*     */       private ScheduledExecutorService getExecutorService() {
/* 218 */         if (this.executorService == null) {
/* 219 */           this.executorService = Executors.newScheduledThreadPool(1, runnabble -> {
/*     */                 Thread result = new Thread(runnabble);
/*     */                 
/*     */                 result.setDaemon(true);
/*     */                 return result;
/*     */               });
/*     */         }
/* 226 */         return this.executorService;
/*     */       }
/*     */       
/*     */       private String processInput(String initialText, String letter) {
/* 230 */         if (initialText == null) {
/* 231 */           initialText = "";
/*     */         }
/*     */         
/* 234 */         StringBuilder sb = new StringBuilder();
/* 235 */         for (char c : initialText.concat(letter).toCharArray()) {
/* 236 */           if (c == '\b') {
/* 237 */             if (sb.length() > 0) {
/* 238 */               sb.delete(0, sb.length());
/*     */               break;
/*     */             } 
/*     */           } else {
/* 242 */             sb.append(c);
/*     */           } 
/*     */         } 
/* 245 */         return sb.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void customize(ComboBox<?> comboBox) {
/* 262 */     if (!comboBox.isEditable()) {
/* 263 */       comboBox.addEventHandler(KeyEvent.KEY_PRESSED, handler);
/*     */     }
/* 265 */     comboBox.editableProperty().addListener((o, oV, nV) -> {
/*     */           if (!nV.booleanValue()) {
/*     */             comboBox.addEventHandler(KeyEvent.KEY_PRESSED, handler);
/*     */           } else {
/*     */             comboBox.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
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
/*     */   public static void customize(ChoiceBox<?> choiceBox) {
/* 284 */     choiceBox.addEventHandler(KeyEvent.KEY_PRESSED, handler);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\PrefixSelectionCustomizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */