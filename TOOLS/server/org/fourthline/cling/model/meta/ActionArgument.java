/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.Validatable;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.types.Datatype;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActionArgument<S extends Service>
/*     */   implements Validatable
/*     */ {
/*     */   private final String name;
/*     */   private final String[] aliases;
/*     */   private final String relatedStateVariableName;
/*     */   private final Direction direction;
/*     */   private final boolean returnValue;
/*     */   private Action<S> action;
/*  37 */   private static final Logger log = Logger.getLogger(ActionArgument.class.getName());
/*     */   
/*     */   public enum Direction {
/*  40 */     IN, OUT;
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
/*     */   public ActionArgument(String name, String relatedStateVariableName, Direction direction) {
/*  53 */     this(name, new String[0], relatedStateVariableName, direction, false);
/*     */   }
/*     */   
/*     */   public ActionArgument(String name, String[] aliases, String relatedStateVariableName, Direction direction) {
/*  57 */     this(name, aliases, relatedStateVariableName, direction, false);
/*     */   }
/*     */   
/*     */   public ActionArgument(String name, String relatedStateVariableName, Direction direction, boolean returnValue) {
/*  61 */     this(name, new String[0], relatedStateVariableName, direction, returnValue);
/*     */   }
/*     */   
/*     */   public ActionArgument(String name, String[] aliases, String relatedStateVariableName, Direction direction, boolean returnValue) {
/*  65 */     this.name = name;
/*  66 */     this.aliases = aliases;
/*  67 */     this.relatedStateVariableName = relatedStateVariableName;
/*  68 */     this.direction = direction;
/*  69 */     this.returnValue = returnValue;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  73 */     return this.name;
/*     */   }
/*     */   
/*     */   public String[] getAliases() {
/*  77 */     return this.aliases;
/*     */   }
/*     */   
/*     */   public boolean isNameOrAlias(String name) {
/*  81 */     if (getName().equalsIgnoreCase(name)) return true; 
/*  82 */     for (String alias : this.aliases) {
/*  83 */       if (alias.equalsIgnoreCase(name)) return true; 
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */   
/*     */   public String getRelatedStateVariableName() {
/*  89 */     return this.relatedStateVariableName;
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/*  93 */     return this.direction;
/*     */   }
/*     */   
/*     */   public boolean isReturnValue() {
/*  97 */     return this.returnValue;
/*     */   }
/*     */   
/*     */   public Action<S> getAction() {
/* 101 */     return this.action;
/*     */   }
/*     */   
/*     */   void setAction(Action<S> action) {
/* 105 */     if (this.action != null)
/* 106 */       throw new IllegalStateException("Final value has been set already, model is immutable"); 
/* 107 */     this.action = action;
/*     */   }
/*     */   
/*     */   public Datatype getDatatype() {
/* 111 */     return getAction().getService().getDatatype(this);
/*     */   }
/*     */   
/*     */   public List<ValidationError> validate() {
/* 115 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/* 117 */     if (getName() == null || getName().length() == 0) {
/* 118 */       errors.add(new ValidationError(
/* 119 */             getClass(), "name", "Argument without name of: " + 
/*     */             
/* 121 */             getAction()));
/*     */     }
/* 123 */     else if (!ModelUtil.isValidUDAName(getName())) {
/* 124 */       log.warning("UPnP specification violation of: " + getAction().getService().getDevice());
/* 125 */       log.warning("Invalid argument name: " + this);
/* 126 */     } else if (getName().length() > 32) {
/* 127 */       log.warning("UPnP specification violation of: " + getAction().getService().getDevice());
/* 128 */       log.warning("Argument name should be less than 32 characters: " + this);
/*     */     } 
/*     */     
/* 131 */     if (getDirection() == null) {
/* 132 */       errors.add(new ValidationError(
/* 133 */             getClass(), "direction", "Argument '" + 
/*     */             
/* 135 */             getName() + "' requires a direction, either IN or OUT"));
/*     */     }
/*     */ 
/*     */     
/* 139 */     if (isReturnValue() && getDirection() != Direction.OUT) {
/* 140 */       errors.add(new ValidationError(
/* 141 */             getClass(), "direction", "Return value argument '" + 
/*     */             
/* 143 */             getName() + "' must be direction OUT"));
/*     */     }
/*     */ 
/*     */     
/* 147 */     return errors;
/*     */   }
/*     */   
/*     */   public ActionArgument<S> deepCopy() {
/* 151 */     return new ActionArgument(
/* 152 */         getName(), 
/* 153 */         getAliases(), 
/* 154 */         getRelatedStateVariableName(), 
/* 155 */         getDirection(), 
/* 156 */         isReturnValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 162 */     return "(" + getClass().getSimpleName() + ", " + getDirection() + ") " + getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\ActionArgument.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */