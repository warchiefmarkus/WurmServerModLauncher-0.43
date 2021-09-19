/*     */ package com.wurmonline.shared.constants;
/*     */ 
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
/*     */ public enum StructureStateEnum
/*     */ {
/*  18 */   UNINITIALIZED((byte)0),
/*  19 */   INITIALIZED((byte)1),
/*  20 */   STATE_2_NEEDED((byte)2),
/*  21 */   STATE_3_NEEDED((byte)3),
/*  22 */   STATE_4_NEEDED((byte)4),
/*  23 */   STATE_5_NEEDED((byte)5),
/*  24 */   STATE_6_NEEDED((byte)6),
/*  25 */   STATE_7_NEEDED((byte)7),
/*  26 */   STATE_8_NEEDED((byte)8),
/*  27 */   STATE_9_NEEDED((byte)9),
/*  28 */   STATE_10_NEEDED((byte)10),
/*  29 */   STATE_11_NEEDED((byte)11),
/*  30 */   STATE_12_NEEDED((byte)12),
/*  31 */   STATE_13_NEEDED((byte)13),
/*  32 */   STATE_14_NEEDED((byte)14),
/*  33 */   STATE_15_NEEDED((byte)15),
/*  34 */   STATE_16_NEEDED((byte)16),
/*  35 */   STATE_17_NEEDED((byte)17),
/*  36 */   STATE_18_NEEDED((byte)18),
/*  37 */   STATE_19_NEEDED((byte)19),
/*  38 */   STATE_20_NEEDED((byte)20),
/*  39 */   STATE_21_NEEDED((byte)21),
/*  40 */   STATE_22_NEEDED((byte)22),
/*  41 */   STATE_23_NEEDED((byte)23),
/*  42 */   STATE_24_NEEDED((byte)24),
/*  43 */   STATE_25_NEEDED((byte)25),
/*  44 */   STATE_26_NEEDED((byte)26),
/*  45 */   STATE_27_NEEDED((byte)27),
/*  46 */   STATE_28_NEEDED((byte)28),
/*  47 */   STATE_29_NEEDED((byte)29),
/*  48 */   STATE_30_NEEDED((byte)30),
/*  49 */   STATE_31_NEEDED((byte)31),
/*  50 */   STATE_32_NEEDED((byte)32),
/*  51 */   STATE_33_NEEDED((byte)33),
/*  52 */   STATE_34_NEEDED((byte)34),
/*  53 */   STATE_35_NEEDED((byte)35),
/*  54 */   STATE_36_NEEDED((byte)36),
/*  55 */   STATE_37_NEEDED((byte)37),
/*  56 */   STATE_38_NEEDED((byte)38),
/*  57 */   STATE_39_NEEDED((byte)39),
/*  58 */   STATE_40_NEEDED((byte)40),
/*  59 */   STATE_41_NEEDED((byte)41),
/*  60 */   STATE_42_NEEDED((byte)42),
/*  61 */   STATE_43_NEEDED((byte)43),
/*  62 */   STATE_44_NEEDED((byte)44),
/*  63 */   STATE_45_NEEDED((byte)45),
/*  64 */   STATE_46_NEEDED((byte)46),
/*  65 */   STATE_47_NEEDED((byte)47),
/*  66 */   STATE_48_NEEDED((byte)48),
/*  67 */   STATE_49_NEEDED((byte)49),
/*  68 */   STATE_50_NEEDED((byte)50),
/*  69 */   STATE_51_NEEDED((byte)51),
/*  70 */   STATE_52_NEEDED((byte)52),
/*  71 */   STATE_53_NEEDED((byte)53),
/*  72 */   STATE_54_NEEDED((byte)54),
/*  73 */   STATE_55_NEEDED((byte)55),
/*  74 */   STATE_56_NEEDED((byte)56),
/*  75 */   STATE_57_NEEDED((byte)57),
/*  76 */   STATE_58_NEEDED((byte)58),
/*  77 */   STATE_59_NEEDED((byte)59),
/*  78 */   STATE_60_NEEDED((byte)60),
/*  79 */   STATE_61_NEEDED((byte)61),
/*  80 */   STATE_62_NEEDED((byte)62),
/*  81 */   STATE_63_NEEDED((byte)63),
/*  82 */   STATE_64_NEEDED((byte)64),
/*  83 */   STATE_65_NEEDED((byte)65),
/*  84 */   STATE_66_NEEDED((byte)66),
/*  85 */   STATE_67_NEEDED((byte)67),
/*  86 */   STATE_68_NEEDED((byte)68),
/*  87 */   STATE_69_NEEDED((byte)69),
/*  88 */   STATE_70_NEEDED((byte)70),
/*  89 */   STATE_71_NEEDED((byte)71),
/*  90 */   STATE_72_NEEDED((byte)72),
/*  91 */   STATE_73_NEEDED((byte)73),
/*  92 */   STATE_74_NEEDED((byte)74),
/*  93 */   STATE_75_NEEDED((byte)75),
/*  94 */   STATE_76_NEEDED((byte)76),
/*  95 */   STATE_77_NEEDED((byte)77),
/*  96 */   STATE_78_NEEDED((byte)78),
/*  97 */   STATE_79_NEEDED((byte)79),
/*  98 */   STATE_80_NEEDED((byte)80),
/*  99 */   STATE_81_NEEDED((byte)81),
/* 100 */   STATE_82_NEEDED((byte)82),
/* 101 */   STATE_83_NEEDED((byte)83),
/* 102 */   STATE_84_NEEDED((byte)84),
/* 103 */   STATE_85_NEEDED((byte)85),
/* 104 */   STATE_86_NEEDED((byte)86),
/* 105 */   STATE_87_NEEDED((byte)87),
/* 106 */   STATE_88_NEEDED((byte)88),
/* 107 */   STATE_89_NEEDED((byte)89),
/* 108 */   STATE_90_NEEDED((byte)90),
/* 109 */   STATE_91_NEEDED((byte)91),
/* 110 */   STATE_92_NEEDED((byte)92),
/* 111 */   STATE_93_NEEDED((byte)93),
/* 112 */   STATE_94_NEEDED((byte)94),
/* 113 */   STATE_95_NEEDED((byte)95),
/* 114 */   STATE_96_NEEDED((byte)96),
/* 115 */   STATE_97_NEEDED((byte)97),
/* 116 */   STATE_98_NEEDED((byte)98),
/* 117 */   STATE_99_NEEDED((byte)99),
/* 118 */   STATE_100_NEEDED((byte)100),
/* 119 */   STATE_101_NEEDED((byte)101),
/* 120 */   STATE_102_NEEDED((byte)102),
/* 121 */   STATE_103_NEEDED((byte)103),
/* 122 */   STATE_104_NEEDED((byte)104),
/* 123 */   STATE_105_NEEDED((byte)105),
/* 124 */   STATE_106_NEEDED((byte)106),
/* 125 */   STATE_107_NEEDED((byte)107),
/* 126 */   STATE_108_NEEDED((byte)108),
/* 127 */   STATE_109_NEEDED((byte)109),
/* 128 */   STATE_110_NEEDED((byte)110),
/* 129 */   STATE_111_NEEDED((byte)111),
/* 130 */   STATE_112_NEEDED((byte)112),
/* 131 */   STATE_113_NEEDED((byte)113),
/* 132 */   STATE_114_NEEDED((byte)114),
/* 133 */   STATE_115_NEEDED((byte)115),
/* 134 */   STATE_116_NEEDED((byte)116),
/* 135 */   STATE_117_NEEDED((byte)117),
/* 136 */   STATE_118_NEEDED((byte)118),
/* 137 */   STATE_119_NEEDED((byte)119),
/* 138 */   STATE_120_NEEDED((byte)120),
/* 139 */   STATE_121_NEEDED((byte)121),
/* 140 */   STATE_122_NEEDED((byte)122),
/* 141 */   STATE_123_NEEDED((byte)123),
/* 142 */   STATE_124_NEEDED((byte)124),
/* 143 */   STATE_125_NEEDED((byte)125),
/* 144 */   STATE_126_NEEDED((byte)126),
/* 145 */   FINISHED(127),
/* 146 */   WALL_PLAN((byte)0);
/*     */   public final byte state;
/*     */   private static final Logger logger;
/*     */   
/*     */   StructureStateEnum(byte value) {
/* 151 */     this.state = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public static StructureStateEnum getStateByValue(byte value) {
/* 156 */     if (value >= 0 && value < (values()).length) {
/* 157 */       return values()[value];
/*     */     }
/* 159 */     logger.warning("Value not a valid state: " + value + " RETURNING PLAN(VAL=0)!");
/*     */     
/* 161 */     return WALL_PLAN;
/*     */   }
/*     */   static {
/* 164 */     logger = Logger.getLogger(StructureStateEnum.class.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\StructureStateEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */