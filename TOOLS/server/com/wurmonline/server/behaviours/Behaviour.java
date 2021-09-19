/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.bodys.Wound;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.structures.BridgePart;
/*     */ import com.wurmonline.server.structures.Fence;
/*     */ import com.wurmonline.server.structures.Floor;
/*     */ import com.wurmonline.server.structures.Wall;
/*     */ import com.wurmonline.shared.constants.SoundNames;
/*     */ import edu.umd.cs.findbugs.annotations.Nullable;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Behaviour
/*     */   implements SoundNames
/*     */ {
/*     */   private static List<ActionEntry> emptyActionList;
/*  42 */   private short behaviourType = 0;
/*     */   
/*  44 */   public static final int[] emptyIntArr = new int[0];
/*     */ 
/*     */   
/*     */   public Behaviour() {
/*  48 */     Behaviours.getInstance().addBehaviour(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Behaviour(short type) {
/*  53 */     this.behaviourType = type;
/*  54 */     Behaviours.getInstance().addBehaviour(this);
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
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, long target) {
/*  67 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  68 */     return toReturn;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item object, int tilex, int tiley, boolean onSurface, int tile) {
/*  85 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  86 */     toReturn.addAll(Actions.getDefaultTileActions());
/*     */     
/*  88 */     return toReturn;
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
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, int tilex, int tiley, boolean onSurface, int tile) {
/* 104 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 105 */     toReturn.addAll(Actions.getDefaultTileActions());
/* 106 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final List<ActionEntry> getEmptyActionList() {
/* 116 */     return emptyActionList;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item object, int tilex, int tiley, boolean onSurface, int tile, int dir) {
/* 133 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 134 */     toReturn.addAll(Actions.getDefaultTileActions());
/*     */     
/* 136 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, int tilex, int tiley, boolean onSurface, int tile, int dir) {
/* 143 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 144 */     toReturn.addAll(Actions.getDefaultTileActions());
/* 145 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item object, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, boolean border, int heightOffset) {
/* 153 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 155 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, int tilex, int tiley, boolean onSurface, Tiles.TileBorderDirection dir, boolean border, int heightOffset) {
/* 162 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 164 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item object, int tilex, int tiley, boolean onSurface, boolean corner, int tile, int heightOffset) {
/* 172 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 174 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, int tilex, int tiley, boolean onSurface, boolean corner, int tile, int heightOffset) {
/* 181 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 183 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, @Nonnull Item source, int tilex, int tiley, boolean onSurface, boolean corner, int tile, int heightOffset, short action, float counter) {
/* 190 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, int tilex, int tiley, boolean onSurface, boolean corner, int tile, int heightOffset, short action, float counter) {
/* 196 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item subject, @Nonnull Skill skill) {
/* 203 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 205 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Skill skill) {
/* 211 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 213 */     return toReturn;
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
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item target) {
/* 226 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 228 */     return toReturn;
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
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item subject, @Nonnull Item target) {
/* 244 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 246 */     return toReturn;
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
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Wound target) {
/* 259 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 261 */     return toReturn;
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
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item subject, @Nonnull Wound target) {
/* 276 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 278 */     return toReturn;
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
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Creature target) {
/* 291 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 293 */     return toReturn;
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
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item subject, @Nonnull Creature target) {
/* 309 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 311 */     return toReturn;
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
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item subject, @Nonnull Wall target) {
/* 326 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 328 */     return toReturn;
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
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Wall target) {
/* 341 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 343 */     return toReturn;
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
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item subject, @Nonnull Fence target) {
/* 358 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 360 */     return toReturn;
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
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Fence target) {
/* 373 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 375 */     return toReturn;
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
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, @Nonnull Item object, int planetId) {
/* 389 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 391 */     return toReturn;
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
/*     */   @Nonnull
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, int planetId) {
/* 404 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/* 406 */     return toReturn;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, int tilex, int tiley, boolean onSurface, int tile, short num, float counter) {
/* 425 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short num, float counter) {
/* 448 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, int tilex, int tiley, boolean onSurface, int tile, int dir, short num, float counter) {
/* 467 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, int dir, short num, float counter) {
/* 490 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, int planetId, short num, float counter) {
/* 513 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item source, int planetId, short num, float counter) {
/* 531 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item source, @Nonnull Item target, short num, float counter) {
/* 549 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Wound target, short num, float counter) {
/* 566 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item source, @Nonnull Wound target, short num, float counter) {
/* 584 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item target, short num, float counter) {
/* 601 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item source, @Nonnull Creature target, short num, float counter) {
/* 619 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Creature target, short num, float counter) {
/* 636 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final short getType() {
/* 646 */     return this.behaviourType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item source, @Nonnull Wall target, short num, float counter) {
/* 663 */     return true;
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
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Wall target, short num, float counter) {
/* 678 */     return true;
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
/*     */ 
/*     */   
/*     */   static void addEmotes(List<ActionEntry> list) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item source, boolean onSurface, @Nonnull Fence target, short num, float counter) {
/* 706 */     return true;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, boolean onSurface, @Nonnull Fence target, short num, float counter) {
/* 722 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, @Nonnull Item source, @Nonnull Skill skill, short action, float counter) {
/* 728 */     return action(act, performer, skill, action, counter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, @Nonnull Skill skill, short action, float counter) {
/* 734 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature performer, boolean onSurface, @Nonnull Floor floor) {
/* 742 */     return null;
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
/*     */   
/*     */   @Nullable
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature creature, @Nonnull Item item, boolean onSurface, Floor floor) {
/* 757 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item source, boolean onSurface, Floor target, int encodedTile, short num, float counter) {
/* 775 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, boolean onSurface, @Nonnull Floor floor, int encodedTile, short action, float counter) {
/* 792 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action aAct, @Nonnull Creature aPerformer, @Nonnull Item aSource, int aTilex, int aTiley, boolean onSurface, int aHeightOffset, Tiles.TileBorderDirection aDir, long borderId, short aAction, float aCounter) {
/* 815 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action aAct, @Nonnull Creature aPerformer, int aTilex, int aTiley, boolean onSurface, Tiles.TileBorderDirection aDir, long borderId, short aAction, float aCounter) {
/* 835 */     return true;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action action, @Nonnull Creature performer, @Nonnull Item[] targets, short num, float counter) {
/* 851 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 862 */     int prime = 31;
/* 863 */     return 31 * getType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 874 */     if (this == obj)
/*     */     {
/* 876 */       return true;
/*     */     }
/* 878 */     if (obj == null)
/*     */     {
/* 880 */       return false;
/*     */     }
/* 882 */     if (!(obj instanceof Behaviour))
/*     */     {
/* 884 */       return false;
/*     */     }
/* 886 */     Behaviour other = (Behaviour)obj;
/* 887 */     if (getType() != other.getType())
/*     */     {
/* 889 */       return false;
/*     */     }
/* 891 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String toString() {
/* 903 */     return "Behaviour [behaviourType=" + getType() + "]";
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
/*     */   @Nullable
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature aPerformer, boolean aOnSurface, @Nonnull BridgePart aBridgePart) {
/* 917 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<ActionEntry> getBehavioursFor(@Nonnull Creature aPerformer, @Nonnull Item item, boolean aOnSurface, @Nonnull BridgePart aBridgePart) {
/* 925 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, boolean onSurface, @Nonnull BridgePart aBridgePart, int encodedTile, short action, float counter) {
/* 932 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(@Nonnull Action act, @Nonnull Creature performer, @Nonnull Item item, boolean onSurface, @Nonnull BridgePart aBridgePart, int encodedTile, short action, float counter) {
/* 939 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Behaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */