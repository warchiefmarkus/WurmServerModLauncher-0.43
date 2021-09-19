/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Behaviours
/*     */ {
/*  31 */   private static final Logger logger = Logger.getLogger(Behaviours.class.getName());
/*     */   
/*  33 */   private static Behaviours instance = null;
/*  34 */   private static Map<Short, Behaviour> behaviours = new HashMap<>();
/*     */ 
/*     */   
/*     */   static {
/*  38 */     new Behaviour();
/*  39 */     new ItemBehaviour();
/*  40 */     new CreatureBehaviour();
/*  41 */     new TileBehaviour();
/*  42 */     new TileTreeBehaviour();
/*  43 */     new BodyPartBehaviour();
/*  44 */     new TileGrassBehaviour();
/*  45 */     new TileRockBehaviour();
/*  46 */     new ExamineBehaviour();
/*  47 */     new TileDirtBehaviour();
/*  48 */     new TileFieldBehaviour();
/*  49 */     new VegetableBehaviour();
/*  50 */     new FireBehaviour();
/*  51 */     new WallBehaviour();
/*  52 */     new WritBehaviour();
/*  53 */     new ItemPileBehaviour();
/*  54 */     new FenceBehaviour();
/*  55 */     new UnfinishedItemBehaviour();
/*  56 */     new VillageDeedBehaviour();
/*  57 */     new VillageTokenBehaviour();
/*  58 */     new ToyBehaviour();
/*  59 */     new WoundBehaviour();
/*  60 */     new CorpseBehaviour();
/*  61 */     new TraderBookBehaviour();
/*  62 */     new CornucopiaBehaviour();
/*  63 */     new PracticeDollBehaviour();
/*  64 */     new TileBorderBehaviour();
/*  65 */     new DomainItemBehaviour();
/*  66 */     new HugeAltarBehaviour();
/*  67 */     new ArtifactBehaviour();
/*  68 */     new PlanetBehaviour();
/*  69 */     new HugeLogBehaviour();
/*  70 */     new CaveWallBehaviour();
/*  71 */     new CaveTileBehaviour();
/*  72 */     new WarmachineBehaviour();
/*  73 */     new VehicleBehaviour();
/*  74 */     new SkillBehaviour();
/*  75 */     new MissionBehaviour();
/*  76 */     new PapyrusBehaviour();
/*  77 */     new StructureBehaviour();
/*  78 */     new FloorBehaviour();
/*  79 */     new ShardBehaviour();
/*  80 */     new FlowerpotBehaviour();
/*  81 */     new GravestoneBehaviour();
/*  82 */     new InventoryBehaviour();
/*  83 */     new TicketBehaviour();
/*  84 */     new BridgePartBehaviour();
/*  85 */     new OwnershipPaperBehaviour();
/*  86 */     new MenuRequestBehaviour();
/*  87 */     new TileCornerBehaviour();
/*  88 */     new PlanterBehaviour();
/*  89 */     new MarkerBehaviour();
/*  90 */     new AlmanacBehaviour();
/*  91 */     new TrellisBehaviour();
/*  92 */     new WagonerContractBehaviour();
/*  93 */     new BridgeCornerBehaviour();
/*  94 */     new WagonerContainerBehaviour();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Behaviours getInstance() {
/* 103 */     if (instance == null)
/* 104 */       instance = new Behaviours(); 
/* 105 */     return instance;
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
/*     */   void addBehaviour(Behaviour aBehaviour) {
/* 117 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 119 */       logger.finer("Adding Behaviour: " + aBehaviour + ", Class: " + aBehaviour.getClass());
/*     */     }
/* 121 */     behaviours.put(Short.valueOf(aBehaviour.getType()), aBehaviour);
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
/*     */   public Behaviour getBehaviour(short type) throws NoSuchBehaviourException {
/* 143 */     Behaviour toReturn = behaviours.get(Short.valueOf(type));
/* 144 */     if (toReturn == null)
/* 145 */       throw new NoSuchBehaviourException("No Behaviour with type " + type); 
/* 146 */     return toReturn;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Behaviours.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */