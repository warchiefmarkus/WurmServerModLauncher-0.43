/*     */ package com.wurmonline.server.gui.propertysheet;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.gui.PlayerData;
/*     */ import java.util.HashSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.layout.Priority;
/*     */ import javafx.scene.layout.VBox;
/*     */ import org.controlsfx.control.PropertySheet;
/*     */ import org.controlsfx.property.editor.PropertyEditor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerPropertySheet
/*     */   extends VBox
/*     */   implements MiscConstants
/*     */ {
/*  50 */   private static final Logger logger = Logger.getLogger(PlayerPropertySheet.class.getName());
/*     */   private PlayerData current;
/*     */   private final ObservableList<PropertySheet.Item> list;
/*     */   
/*     */   private enum PropertyType
/*     */   {
/*  56 */     NAME, POSX, POSY, POWER, CURRENTSERVER, UNDEAD;
/*     */   }
/*     */   
/*  59 */   private Set<PropertyType> changedProperties = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerPropertySheet(PlayerData entry) {
/*  65 */     this.current = entry;
/*  66 */     this.list = FXCollections.observableArrayList();
/*  67 */     this.list.add(new CustomPropertyItem(PropertyType.NAME, "Name", "Player Name", "Name", true, entry
/*  68 */           .getName()));
/*  69 */     this.list.add(new CustomPropertyItem(PropertyType.POSX, "Position X", "Position in X", "The X position of the player", true, 
/*  70 */           Float.valueOf(entry.getPosx())));
/*  71 */     this.list.add(new CustomPropertyItem(PropertyType.POSY, "Position Y", "Position in Y", "The Y position of the player", true, 
/*  72 */           Float.valueOf(entry.getPosy())));
/*  73 */     this.list.add(new CustomPropertyItem(PropertyType.POWER, "Power", "Player Game Management Power", "Power from 0 to 5. 2 is Game Manager, 4 is Head GM and 5 Implementor", true, 
/*  74 */           Integer.valueOf(entry.getPower())));
/*  75 */     this.list.add(new CustomPropertyItem(PropertyType.CURRENTSERVER, "Current server", "Server id of the player", "The id of the server that the player is on", true, 
/*  76 */           Integer.valueOf(entry.getServer())));
/*  77 */     this.list.add(new CustomPropertyItem(PropertyType.UNDEAD, "Undead", "Whether the player is undead", "Lets the player play as undead", true, 
/*  78 */           Boolean.valueOf(entry.isUndead())));
/*  79 */     PropertySheet propertySheet = new PropertySheet(this.list);
/*  80 */     VBox.setVgrow((Node)propertySheet, Priority.ALWAYS);
/*  81 */     getChildren().add(propertySheet);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerData getCurrentData() {
/*  86 */     return this.current;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class CustomPropertyItem
/*     */     implements PropertySheet.Item
/*     */   {
/*     */     private PlayerPropertySheet.PropertyType type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String category;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String description;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean editable = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Object value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CustomPropertyItem(PlayerPropertySheet.PropertyType aType, String aCategory, String aName, String aDescription, boolean aEditable, Object aValue) {
/* 133 */       this.type = aType;
/* 134 */       this.category = aCategory;
/* 135 */       this.name = aName;
/* 136 */       this.description = aDescription;
/* 137 */       this.editable = aEditable;
/* 138 */       this.value = aValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public PlayerPropertySheet.PropertyType getPropertyType() {
/* 143 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> getType() {
/* 149 */       return this.value.getClass();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getCategory() {
/* 155 */       return this.category;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 161 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 167 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
/* 179 */       return super.getPropertyEditorClass();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEditable() {
/* 185 */       return this.editable;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getValue() {
/* 191 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(Object aValue) {
/* 197 */       if (!this.value.equals(aValue))
/*     */       {
/* 199 */         PlayerPropertySheet.this.changedProperties.add(this.type);
/*     */       }
/* 201 */       this.value = aValue;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Optional<ObservableValue<? extends Object>> getObservableValue() {
/* 207 */       return (Optional)Optional.of(new SimpleObjectProperty(this.value));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final String save() {
/* 213 */     String toReturn = "";
/* 214 */     boolean saveAtAll = false;
/* 215 */     for (CustomPropertyItem item : (CustomPropertyItem[])this.list.toArray((Object[])new CustomPropertyItem[this.list.size()])) {
/*     */       
/* 217 */       if (this.changedProperties.contains(item.getPropertyType())) {
/*     */         
/* 219 */         saveAtAll = true;
/*     */         
/*     */         try {
/* 222 */           switch (item.getPropertyType()) {
/*     */             
/*     */             case NAME:
/* 225 */               this.current.setName(item.getValue().toString());
/*     */               break;
/*     */             case POSX:
/* 228 */               this.current.setPosx(((Float)item.getValue()).floatValue());
/*     */               break;
/*     */             case POSY:
/* 231 */               this.current.setPosy(((Float)item.getValue()).floatValue());
/*     */               break;
/*     */             case POWER:
/* 234 */               this.current.setPower(((Integer)item.getValue()).intValue());
/*     */               break;
/*     */             case CURRENTSERVER:
/* 237 */               this.current.setServer(((Integer)item.getValue()).intValue());
/*     */               break;
/*     */             
/*     */             case UNDEAD:
/* 241 */               if (!this.current.isUndead()) {
/*     */                 
/* 243 */                 this.current.setUndeadType((byte)(1 + Server.rand.nextInt(3)));
/*     */                 break;
/*     */               } 
/* 246 */               this.current.setUndeadType((byte)0);
/*     */               break;
/*     */           } 
/*     */ 
/*     */         
/* 251 */         } catch (Exception ex) {
/*     */           
/* 253 */           saveAtAll = false;
/* 254 */           toReturn = toReturn + "Invalid value " + item.getCategory() + ": " + item.getValue() + ". ";
/* 255 */           logger.log(Level.INFO, "Error " + ex.getMessage(), ex);
/*     */         } 
/*     */       } 
/*     */     } 
/* 259 */     if (toReturn.length() == 0)
/*     */     {
/* 261 */       if (saveAtAll) {
/*     */         
/*     */         try {
/*     */           
/* 265 */           this.current.save();
/* 266 */           toReturn = "ok";
/*     */         }
/* 268 */         catch (Exception ex) {
/*     */           
/* 270 */           toReturn = ex.getMessage();
/*     */         } 
/*     */       }
/*     */     }
/* 274 */     return toReturn;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\propertysheet\PlayerPropertySheet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */