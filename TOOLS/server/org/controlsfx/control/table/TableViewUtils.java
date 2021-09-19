/*     */ package org.controlsfx.control.table;
/*     */ 
/*     */ import com.sun.javafx.scene.control.skin.TableHeaderRow;
/*     */ import com.sun.javafx.scene.control.skin.TableViewSkin;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.function.Consumer;
/*     */ import javafx.beans.InvalidationListener;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.ContextMenu;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.TableView;
/*     */ import javafx.scene.control.TreeTableView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TableViewUtils
/*     */ {
/*     */   public static void modifyTableMenu(TableView<?> tableView, Consumer<ContextMenu> consumer) {
/*  61 */     modifyTableMenu((Control)tableView, consumer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void modifyTableMenu(TreeTableView<?> treeTableView, Consumer<ContextMenu> consumer) {
/*  71 */     modifyTableMenu((Control)treeTableView, consumer);
/*     */   }
/*     */   
/*     */   private static void modifyTableMenu(final Control control, final Consumer<ContextMenu> consumer) {
/*  75 */     if (control.getScene() == null) {
/*  76 */       control.sceneProperty().addListener(new InvalidationListener() {
/*     */             public void invalidated(Observable o) {
/*  78 */               control.sceneProperty().removeListener(this);
/*  79 */               TableViewUtils.modifyTableMenu(control, consumer);
/*     */             }
/*     */           });
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  86 */     Skin<?> skin = control.getSkin();
/*  87 */     if (skin == null) {
/*  88 */       control.skinProperty().addListener(new InvalidationListener() {
/*     */             public void invalidated(Observable o) {
/*  90 */               control.skinProperty().removeListener(this);
/*  91 */               TableViewUtils.modifyTableMenu(control, consumer);
/*     */             }
/*     */           });
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  98 */     doModify(skin, consumer);
/*     */   }
/*     */   
/*     */   private static void doModify(Skin<?> skin, Consumer<ContextMenu> consumer) {
/* 102 */     if (!(skin instanceof com.sun.javafx.scene.control.skin.TableViewSkinBase))
/*     */       return; 
/* 104 */     TableViewSkin<?> tableSkin = (TableViewSkin)skin;
/* 105 */     TableHeaderRow headerRow = getHeaderRow(tableSkin);
/* 106 */     if (headerRow == null)
/*     */       return; 
/* 108 */     ContextMenu contextMenu = getContextMenu(headerRow);
/* 109 */     consumer.accept(contextMenu);
/*     */   }
/*     */   
/*     */   private static TableHeaderRow getHeaderRow(TableViewSkin<?> tableSkin) {
/* 113 */     ObservableList<Node> children = tableSkin.getChildren();
/* 114 */     for (int i = 0, max = children.size(); i < max; i++) {
/* 115 */       Node child = (Node)children.get(i);
/* 116 */       if (child instanceof TableHeaderRow) return (TableHeaderRow)child; 
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   private static ContextMenu getContextMenu(TableHeaderRow headerRow) {
/*     */     try {
/* 123 */       Field privateContextMenuField = TableHeaderRow.class.getDeclaredField("columnPopupMenu");
/* 124 */       privateContextMenuField.setAccessible(true);
/* 125 */       ContextMenu contextMenu = (ContextMenu)privateContextMenuField.get(headerRow);
/* 126 */       return contextMenu;
/* 127 */     } catch (IllegalArgumentException ex) {
/* 128 */       ex.printStackTrace();
/* 129 */     } catch (IllegalAccessException ex) {
/* 130 */       ex.printStackTrace();
/* 131 */     } catch (NoSuchFieldException ex) {
/* 132 */       ex.printStackTrace();
/* 133 */     } catch (SecurityException ex) {
/* 134 */       ex.printStackTrace();
/*     */     } 
/* 136 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\table\TableViewUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */