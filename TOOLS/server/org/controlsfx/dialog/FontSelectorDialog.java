/*     */ package org.controlsfx.dialog;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.binding.DoubleBinding;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.collections.transformation.FilteredList;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ButtonType;
/*     */ import javafx.scene.control.Dialog;
/*     */ import javafx.scene.control.DialogPane;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.ListCell;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.MultipleSelectionModel;
/*     */ import javafx.scene.layout.ColumnConstraints;
/*     */ import javafx.scene.layout.GridPane;
/*     */ import javafx.scene.layout.Priority;
/*     */ import javafx.scene.layout.RowConstraints;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import javafx.scene.text.Font;
/*     */ import javafx.scene.text.FontPosture;
/*     */ import javafx.scene.text.FontWeight;
/*     */ import javafx.scene.text.Text;
/*     */ import javafx.util.Callback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FontSelectorDialog
/*     */   extends Dialog<Font>
/*     */ {
/*     */   private FontPanel fontPanel;
/*     */   
/*     */   public FontSelectorDialog(Font defaultFont) {
/*  69 */     this.fontPanel = new FontPanel();
/*  70 */     this.fontPanel.setFont(defaultFont);
/*     */     
/*  72 */     setResultConverter(dialogButton -> (dialogButton == ButtonType.OK) ? this.fontPanel.getFont() : null);
/*     */     
/*  74 */     DialogPane dialogPane = getDialogPane();
/*     */     
/*  76 */     setTitle(Localization.localize(Localization.asKey("font.dlg.title")));
/*  77 */     dialogPane.setHeaderText(Localization.localize(Localization.asKey("font.dlg.header")));
/*  78 */     dialogPane.getStyleClass().add("font-selector-dialog");
/*  79 */     dialogPane.getStylesheets().add(FontSelectorDialog.class.getResource("dialogs.css").toExternalForm());
/*  80 */     dialogPane.getButtonTypes().addAll((Object[])new ButtonType[] { ButtonType.OK, ButtonType.CANCEL });
/*  81 */     dialogPane.setContent((Node)this.fontPanel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FontStyle
/*     */     implements Comparable<FontStyle>
/*     */   {
/*     */     private FontPosture posture;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private FontWeight weight;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FontStyle(FontWeight weight, FontPosture posture) {
/* 103 */       this.posture = (posture == null) ? FontPosture.REGULAR : posture;
/* 104 */       this.weight = weight;
/*     */     }
/*     */     
/*     */     public FontStyle() {
/* 108 */       this(null, null);
/*     */     }
/*     */     
/*     */     public FontStyle(String styles) {
/* 112 */       this();
/* 113 */       String[] fontStyles = ((styles == null) ? "" : styles.trim().toUpperCase()).split(" ");
/* 114 */       for (String style : fontStyles) {
/* 115 */         FontWeight w = FontWeight.findByName(style);
/* 116 */         if (w != null) {
/* 117 */           this.weight = w;
/*     */         } else {
/* 119 */           FontPosture p = FontPosture.findByName(style);
/* 120 */           if (p != null) this.posture = p; 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public FontStyle(Font font) {
/* 126 */       this(font.getStyle());
/*     */     }
/*     */     
/*     */     public FontPosture getPosture() {
/* 130 */       return this.posture;
/*     */     }
/*     */     
/*     */     public FontWeight getWeight() {
/* 134 */       return this.weight;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 139 */       int prime = 31;
/* 140 */       int result = 1;
/* 141 */       result = 31 * result + ((this.posture == null) ? 0 : this.posture.hashCode());
/* 142 */       result = 31 * result + ((this.weight == null) ? 0 : this.weight.hashCode());
/* 143 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(Object that) {
/* 147 */       if (this == that)
/* 148 */         return true; 
/* 149 */       if (that == null)
/* 150 */         return false; 
/* 151 */       if (getClass() != that.getClass())
/* 152 */         return false; 
/* 153 */       FontStyle other = (FontStyle)that;
/* 154 */       if (this.posture != other.posture)
/* 155 */         return false; 
/* 156 */       if (this.weight != other.weight)
/* 157 */         return false; 
/* 158 */       return true;
/*     */     }
/*     */     
/*     */     private static String makePretty(Object o) {
/* 162 */       String s = (o == null) ? "" : o.toString();
/* 163 */       if (!s.isEmpty()) {
/* 164 */         s = s.replace("_", " ");
/* 165 */         s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
/*     */       } 
/* 167 */       return s;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 171 */       return String.format("%s %s", new Object[] { makePretty(this.weight), makePretty(this.posture) }).trim();
/*     */     }
/*     */     
/*     */     private <T extends Enum<T>> int compareEnums(T e1, T e2) {
/* 175 */       if (e1 == e2) return 0; 
/* 176 */       if (e1 == null) return -1; 
/* 177 */       if (e2 == null) return 1; 
/* 178 */       return e1.compareTo(e2);
/*     */     }
/*     */     
/*     */     public int compareTo(FontStyle fs) {
/* 182 */       int result = compareEnums(this.weight, fs.weight);
/* 183 */       return (result != 0) ? result : compareEnums(this.posture, fs.posture);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FontPanel
/*     */     extends GridPane
/*     */   {
/*     */     private static final double HGAP = 10.0D;
/*     */     private static final double VGAP = 5.0D;
/*     */     
/* 193 */     private static final Predicate<Object> MATCH_ALL = new Predicate() {
/*     */         public boolean test(Object t) {
/* 195 */           return true;
/*     */         }
/*     */       };
/*     */     
/* 199 */     private static final Double[] fontSizes = new Double[] { Double.valueOf(8.0D), Double.valueOf(9.0D), Double.valueOf(11.0D), Double.valueOf(12.0D), Double.valueOf(14.0D), Double.valueOf(16.0D), Double.valueOf(18.0D), Double.valueOf(20.0D), Double.valueOf(22.0D), Double.valueOf(24.0D), Double.valueOf(26.0D), Double.valueOf(28.0D), Double.valueOf(36.0D), Double.valueOf(48.0D), Double.valueOf(72.0D) };
/*     */     
/*     */     private static List<FontSelectorDialog.FontStyle> getFontStyles(String fontFamily) {
/* 202 */       Set<FontSelectorDialog.FontStyle> set = new HashSet<>();
/* 203 */       for (String f : Font.getFontNames(fontFamily)) {
/* 204 */         set.add(new FontSelectorDialog.FontStyle(f.replace(fontFamily, "")));
/*     */       }
/*     */       
/* 207 */       List<FontSelectorDialog.FontStyle> result = new ArrayList<>(set);
/* 208 */       Collections.sort(result);
/* 209 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 214 */     private final FilteredList<String> filteredFontList = new FilteredList(FXCollections.observableArrayList(Font.getFamilies()), MATCH_ALL);
/* 215 */     private final FilteredList<FontSelectorDialog.FontStyle> filteredStyleList = new FilteredList(FXCollections.observableArrayList(), MATCH_ALL);
/* 216 */     private final FilteredList<Double> filteredSizeList = new FilteredList(FXCollections.observableArrayList((Object[])fontSizes), MATCH_ALL);
/*     */     
/* 218 */     private final ListView<String> fontListView = new ListView((ObservableList)this.filteredFontList);
/* 219 */     private final ListView<FontSelectorDialog.FontStyle> styleListView = new ListView((ObservableList)this.filteredStyleList);
/* 220 */     private final ListView<Double> sizeListView = new ListView((ObservableList)this.filteredSizeList);
/* 221 */     private final Text sample = new Text(Localization.localize(Localization.asKey("font.dlg.sample.text")));
/*     */     
/*     */     public FontPanel() {
/* 224 */       setHgap(10.0D);
/* 225 */       setVgap(5.0D);
/* 226 */       setPrefSize(500.0D, 300.0D);
/* 227 */       setMinSize(500.0D, 300.0D);
/*     */       
/* 229 */       ColumnConstraints c0 = new ColumnConstraints();
/* 230 */       c0.setPercentWidth(60.0D);
/* 231 */       ColumnConstraints c1 = new ColumnConstraints();
/* 232 */       c1.setPercentWidth(25.0D);
/* 233 */       ColumnConstraints c2 = new ColumnConstraints();
/* 234 */       c2.setPercentWidth(15.0D);
/* 235 */       getColumnConstraints().addAll((Object[])new ColumnConstraints[] { c0, c1, c2 });
/*     */       
/* 237 */       RowConstraints r0 = new RowConstraints();
/* 238 */       r0.setVgrow(Priority.NEVER);
/* 239 */       RowConstraints r1 = new RowConstraints();
/* 240 */       r1.setVgrow(Priority.NEVER);
/* 241 */       RowConstraints r2 = new RowConstraints();
/* 242 */       r2.setFillHeight(true);
/* 243 */       r2.setVgrow(Priority.NEVER);
/* 244 */       RowConstraints r3 = new RowConstraints();
/* 245 */       r3.setPrefHeight(250.0D);
/* 246 */       r3.setVgrow(Priority.NEVER);
/* 247 */       getRowConstraints().addAll((Object[])new RowConstraints[] { r0, r1, r2, r3 });
/*     */ 
/*     */       
/* 250 */       add((Node)new Label(Localization.localize(Localization.asKey("font.dlg.font.label"))), 0, 0);
/*     */ 
/*     */       
/* 253 */       add((Node)this.fontListView, 0, 1);
/* 254 */       this.fontListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
/*     */             public ListCell<String> call(ListView<String> listview) {
/* 256 */               return new ListCell<String>() {
/*     */                   protected void updateItem(String family, boolean empty) {
/* 258 */                     super.updateItem(family, empty);
/*     */                     
/* 260 */                     if (!empty) {
/* 261 */                       setFont(Font.font(family));
/* 262 */                       setText(family);
/*     */                     } else {
/* 264 */                       setText(null);
/*     */                     } 
/*     */                   }
/*     */                 };
/*     */             }
/*     */           });
/*     */ 
/*     */       
/* 272 */       ChangeListener<Object> sampleRefreshListener = new ChangeListener<Object>() {
/*     */           public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
/* 274 */             FontSelectorDialog.FontPanel.this.refreshSample();
/*     */           }
/*     */         };
/*     */       
/* 278 */       ((MultipleSelectionModel)this.fontListView.selectionModelProperty().get()).selectedItemProperty().addListener(new ChangeListener<String>()
/*     */           {
/*     */             public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
/* 281 */               String fontFamily = (String)FontSelectorDialog.FontPanel.this.listSelection((ListView)FontSelectorDialog.FontPanel.this.fontListView);
/* 282 */               FontSelectorDialog.FontPanel.this.styleListView.setItems(FXCollections.observableArrayList(FontSelectorDialog.FontPanel.getFontStyles(fontFamily)));
/* 283 */               FontSelectorDialog.FontPanel.this.refreshSample();
/*     */             }
/*     */           });
/* 286 */       add((Node)new Label(Localization.localize(Localization.asKey("font.dlg.style.label"))), 1, 0);
/*     */ 
/*     */       
/* 289 */       add((Node)this.styleListView, 1, 1);
/* 290 */       ((MultipleSelectionModel)this.styleListView.selectionModelProperty().get()).selectedItemProperty().addListener(sampleRefreshListener);
/*     */       
/* 292 */       add((Node)new Label(Localization.localize(Localization.asKey("font.dlg.size.label"))), 2, 0);
/*     */ 
/*     */       
/* 295 */       add((Node)this.sizeListView, 2, 1);
/* 296 */       ((MultipleSelectionModel)this.sizeListView.selectionModelProperty().get()).selectedItemProperty().addListener(sampleRefreshListener);
/*     */       
/* 298 */       double height = 45.0D;
/* 299 */       DoubleBinding sampleWidth = new DoubleBinding()
/*     */         {
/*     */ 
/*     */           
/*     */           protected double computeValue()
/*     */           {
/* 305 */             return FontSelectorDialog.FontPanel.this.fontListView.getWidth() + FontSelectorDialog.FontPanel.this.styleListView.getWidth() + FontSelectorDialog.FontPanel.this.sizeListView.getWidth() + 30.0D;
/*     */           }
/*     */         };
/* 308 */       StackPane sampleStack = new StackPane(new Node[] { (Node)this.sample });
/* 309 */       sampleStack.setAlignment(Pos.CENTER_LEFT);
/* 310 */       sampleStack.setMinHeight(45.0D);
/* 311 */       sampleStack.setPrefHeight(45.0D);
/* 312 */       sampleStack.setMaxHeight(45.0D);
/* 313 */       sampleStack.prefWidthProperty().bind((ObservableValue)sampleWidth);
/* 314 */       Rectangle clip = new Rectangle(0.0D, 45.0D);
/* 315 */       clip.widthProperty().bind((ObservableValue)sampleWidth);
/* 316 */       sampleStack.setClip((Node)clip);
/* 317 */       add((Node)sampleStack, 0, 3, 1, 3);
/*     */     }
/*     */     
/*     */     public void setFont(Font font) {
/* 321 */       Font _font = (font == null) ? Font.getDefault() : font;
/* 322 */       if (_font != null) {
/* 323 */         selectInList(this.fontListView, _font.getFamily());
/* 324 */         selectInList(this.styleListView, new FontSelectorDialog.FontStyle(_font));
/* 325 */         selectInList(this.sizeListView, Double.valueOf(_font.getSize()));
/*     */       } 
/*     */     }
/*     */     
/*     */     public Font getFont() {
/*     */       try {
/* 331 */         FontSelectorDialog.FontStyle style = listSelection(this.styleListView);
/* 332 */         if (style == null) {
/* 333 */           return Font.font(
/* 334 */               listSelection(this.fontListView), ((Double)
/* 335 */               listSelection(this.sizeListView)).doubleValue());
/*     */         }
/*     */         
/* 338 */         return Font.font(
/* 339 */             listSelection(this.fontListView), style
/* 340 */             .getWeight(), style
/* 341 */             .getPosture(), ((Double)
/* 342 */             listSelection(this.sizeListView)).doubleValue());
/*     */       
/*     */       }
/* 345 */       catch (Throwable ex) {
/* 346 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void refreshSample() {
/* 351 */       this.sample.setFont(getFont());
/*     */     }
/*     */     
/*     */     private <T> void selectInList(final ListView<T> listView, final T selection) {
/* 355 */       Platform.runLater(new Runnable() {
/*     */             public void run() {
/* 357 */               listView.scrollTo(selection);
/* 358 */               listView.getSelectionModel().select(selection);
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     private <T> T listSelection(ListView<T> listView) {
/* 364 */       return (T)((MultipleSelectionModel)listView.selectionModelProperty().get()).getSelectedItem();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\dialog\FontSelectorDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */