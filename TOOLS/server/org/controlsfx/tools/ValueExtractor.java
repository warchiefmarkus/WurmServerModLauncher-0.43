/*     */ package org.controlsfx.tools;
/*     */ 
/*     */ import java.time.LocalDate;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.CheckBox;
/*     */ import javafx.scene.control.ChoiceBox;
/*     */ import javafx.scene.control.ColorPicker;
/*     */ import javafx.scene.control.ComboBox;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.DatePicker;
/*     */ import javafx.scene.control.ListView;
/*     */ import javafx.scene.control.MultipleSelectionModel;
/*     */ import javafx.scene.control.RadioButton;
/*     */ import javafx.scene.control.SelectionMode;
/*     */ import javafx.scene.control.Slider;
/*     */ import javafx.scene.control.TableView;
/*     */ import javafx.scene.control.TextInputControl;
/*     */ import javafx.scene.control.TreeTableView;
/*     */ import javafx.scene.control.TreeView;
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
/*     */ 
/*     */ 
/*     */ public class ValueExtractor
/*     */ {
/*     */   private static class ObservableValueExtractor
/*     */   {
/*     */     public final Predicate<Control> applicability;
/*     */     public final Callback<Control, ObservableValue<?>> extraction;
/*     */     
/*     */     public ObservableValueExtractor(Predicate<Control> applicability, Callback<Control, ObservableValue<?>> extraction) {
/*  62 */       this.applicability = Objects.<Predicate<Control>>requireNonNull(applicability);
/*  63 */       this.extraction = Objects.<Callback<Control, ObservableValue<?>>>requireNonNull(extraction);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  68 */   private static List<ObservableValueExtractor> extractors = (List<ObservableValueExtractor>)FXCollections.observableArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addObservableValueExtractor(Predicate<Control> test, Callback<Control, ObservableValue<?>> extract) {
/*  76 */     extractors.add(new ObservableValueExtractor(test, extract));
/*     */   }
/*     */   
/*     */   static {
/*  80 */     addObservableValueExtractor(c -> c instanceof TextInputControl, c -> ((TextInputControl)c).textProperty());
/*  81 */     addObservableValueExtractor(c -> c instanceof ComboBox, c -> ((ComboBox)c).valueProperty());
/*  82 */     addObservableValueExtractor(c -> c instanceof ChoiceBox, c -> ((ChoiceBox)c).valueProperty());
/*  83 */     addObservableValueExtractor(c -> c instanceof CheckBox, c -> ((CheckBox)c).selectedProperty());
/*  84 */     addObservableValueExtractor(c -> c instanceof Slider, c -> ((Slider)c).valueProperty());
/*  85 */     addObservableValueExtractor(c -> c instanceof ColorPicker, c -> ((ColorPicker)c).valueProperty());
/*  86 */     addObservableValueExtractor(c -> c instanceof DatePicker, c -> ((DatePicker)c).valueProperty());
/*     */     
/*  88 */     addObservableValueExtractor(c -> c instanceof ListView, c -> ((ListView)c).itemsProperty());
/*  89 */     addObservableValueExtractor(c -> c instanceof TableView, c -> ((TableView)c).itemsProperty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Optional<Callback<Control, ObservableValue<?>>> getObservableValueExtractor(Control c) {
/*  98 */     for (ObservableValueExtractor e : extractors) {
/*  99 */       if (e.applicability.test(c)) return Optional.of(e.extraction); 
/*     */     } 
/* 101 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class NodeValueExtractor
/*     */   {
/*     */     public final Predicate<Node> applicability;
/*     */     public final Callback<Node, Object> extraction;
/*     */     
/*     */     public NodeValueExtractor(Predicate<Node> applicability, Callback<Node, Object> extraction) {
/* 111 */       this.applicability = Objects.<Predicate<Node>>requireNonNull(applicability);
/* 112 */       this.extraction = Objects.<Callback<Node, Object>>requireNonNull(extraction);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 118 */   private static final List<NodeValueExtractor> valueExtractors = (List<NodeValueExtractor>)FXCollections.observableArrayList();
/*     */   
/*     */   static {
/* 121 */     addValueExtractor(n -> n instanceof CheckBox, cb -> Boolean.valueOf(((CheckBox)cb).isSelected()));
/* 122 */     addValueExtractor(n -> n instanceof ChoiceBox, cb -> ((ChoiceBox)cb).getValue());
/* 123 */     addValueExtractor(n -> n instanceof ComboBox, cb -> ((ComboBox)cb).getValue());
/* 124 */     addValueExtractor(n -> n instanceof DatePicker, dp -> (LocalDate)((DatePicker)dp).getValue());
/* 125 */     addValueExtractor(n -> n instanceof RadioButton, rb -> Boolean.valueOf(((RadioButton)rb).isSelected()));
/* 126 */     addValueExtractor(n -> n instanceof Slider, sl -> Double.valueOf(((Slider)sl).getValue()));
/* 127 */     addValueExtractor(n -> n instanceof TextInputControl, ta -> ((TextInputControl)ta).getText());
/*     */     
/* 129 */     addValueExtractor(n -> n instanceof ListView, lv -> {
/*     */           MultipleSelectionModel<?> sm = ((ListView)lv).getSelectionModel();
/*     */           return (sm.getSelectionMode() == SelectionMode.MULTIPLE) ? sm.getSelectedItems() : sm.getSelectedItem();
/*     */         });
/* 133 */     addValueExtractor(n -> n instanceof TreeView, tv -> {
/*     */           MultipleSelectionModel<?> sm = ((TreeView)tv).getSelectionModel();
/*     */           return (sm.getSelectionMode() == SelectionMode.MULTIPLE) ? sm.getSelectedItems() : sm.getSelectedItem();
/*     */         });
/* 137 */     addValueExtractor(n -> n instanceof TableView, tv -> {
/*     */           TableView.TableViewSelectionModel tableViewSelectionModel = ((TableView)tv).getSelectionModel();
/*     */           return (tableViewSelectionModel.getSelectionMode() == SelectionMode.MULTIPLE) ? tableViewSelectionModel.getSelectedItems() : tableViewSelectionModel.getSelectedItem();
/*     */         });
/* 141 */     addValueExtractor(n -> n instanceof TreeTableView, tv -> {
/*     */           TreeTableView.TreeTableViewSelectionModel treeTableViewSelectionModel = ((TreeTableView)tv).getSelectionModel();
/*     */           return (treeTableViewSelectionModel.getSelectionMode() == SelectionMode.MULTIPLE) ? treeTableViewSelectionModel.getSelectedItems() : treeTableViewSelectionModel.getSelectedItem();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addValueExtractor(Predicate<Node> test, Callback<Node, Object> extractor) {
/* 152 */     valueExtractors.add(new NodeValueExtractor(test, extractor));
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
/*     */   public static Object getValue(Node n) {
/* 165 */     for (NodeValueExtractor nve : valueExtractors) {
/* 166 */       if (nve.applicability.test(n)) return nve.extraction.call(n); 
/*     */     } 
/* 168 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\tools\ValueExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */