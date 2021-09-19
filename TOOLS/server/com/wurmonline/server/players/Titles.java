/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Titles
/*     */   implements MiscConstants
/*     */ {
/*     */   public static final double SKILL_REQ_LEGENDARY = 99.99999615D;
/*     */   
/*     */   public enum TitleType
/*     */   {
/*  37 */     NORMAL(0),
/*  38 */     MINOR(1),
/*  39 */     MASTER(2),
/*  40 */     LEGENDARY(3),
/*  41 */     EPIC(4);
/*     */     
/*     */     public final int id;
/*     */ 
/*     */     
/*     */     TitleType(int _id) {
/*  47 */       this.id = _id;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Title
/*     */   {
/*  53 */     Miner_Master(1, "Mastermine", 1008, (String)Titles.TitleType.MASTER),
/*  54 */     Miner(2, "Miner", 1008, (String)Titles.TitleType.NORMAL),
/*  55 */     Woodcutter(3, "Lumberjack", 1007, (String)Titles.TitleType.NORMAL),
/*  56 */     Woodcutter_Master(4, "Deforester", 1007, (String)Titles.TitleType.MASTER),
/*  57 */     Herbalist(5, "Herbalist", 10072, (String)Titles.TitleType.NORMAL),
/*  58 */     Herbalist_Master(6, "Loremaster", 10072, (String)Titles.TitleType.MASTER),
/*  59 */     Alchemist(7, "Apothecarist", 10042, (String)Titles.TitleType.NORMAL),
/*  60 */     Alchemist_Master(8, "Rainbow Maker", 10042, (String)Titles.TitleType.MASTER),
/*  61 */     Mason(9, "Mason", 1013, (String)Titles.TitleType.NORMAL),
/*  62 */     Mason_Master(10, "Master Mason", 1013, (String)Titles.TitleType.MASTER),
/*  63 */     Potterer(11, "Potter", 1011, (String)Titles.TitleType.NORMAL),
/*  64 */     Potterer_Master(12, "Master Potter", "Terracotta Terror", 1011, (String)Titles.TitleType.MASTER),
/*  65 */     Channeler(13, "Channeler", 10067, (String)Titles.TitleType.NORMAL),
/*  66 */     Channeler_Master(14, "Casting Specialist", 10067, (String)Titles.TitleType.MASTER),
/*  67 */     Cleric(15, "Cleric", Titles.TitleType.NORMAL),
/*  68 */     Cleric_Minor(16, "Priest", "Priestess", (String)Titles.TitleType.MINOR),
/*  69 */     Cleric_Master(17, "High Priest", "High Priestess", (String)Titles.TitleType.MASTER),
/*  70 */     Warrior(18, "Warrior", "Warmaid", (String)Titles.TitleType.NORMAL),
/*  71 */     Warrior_Minor(19, "Battlemaster", "Battlemaiden", (String)Titles.TitleType.MINOR),
/*  72 */     Warrior_Master(20, "Warlord", "Valkyrie", (String)Titles.TitleType.MASTER),
/*  73 */     DragonSlayer(21, "Dragonslayer"),
/*  74 */     ArtifactFinder(22, "Explorer"),
/*  75 */     Carpenter(23, "Carpenter", 1005, (String)Titles.TitleType.NORMAL),
/*  76 */     Carpenter_Master(24, "Master Carpenter", 1005, (String)Titles.TitleType.MASTER),
/*  77 */     Smithing_Black(25, "Blacksmith", 10015, (String)Titles.TitleType.NORMAL),
/*  78 */     Smithing_Black_Master(26, "Master Blacksmith", 10015, (String)Titles.TitleType.MASTER),
/*  79 */     Smithing_GoldSmith(27, "Goldsmith", 10043, (String)Titles.TitleType.NORMAL),
/*  80 */     Smithing_GoldSmith_Master(28, "Midas Touch", 10043, (String)Titles.TitleType.MASTER),
/*  81 */     Smithing_Lock(29, "Locksmith", 10034, (String)Titles.TitleType.NORMAL),
/*  82 */     Smithing_Lock_Master(30, "Master Locksmith", 10034, (String)Titles.TitleType.MASTER),
/*  83 */     Smithing_Armour(31, "Armoursmith", 1017, (String)Titles.TitleType.NORMAL),
/*  84 */     Smithing_Armour_Master(32, "Master Armourer", 1017, (String)Titles.TitleType.MASTER),
/*  85 */     Smithing_Weapons(33, "Weaponsmith", 1016, (String)Titles.TitleType.NORMAL),
/*  86 */     Smithing_Weapons_Master(34, "Master Weaponsmith", 1016, (String)Titles.TitleType.MASTER),
/*  87 */     Carpenter_Fine(35, "Fine Carpenter", 10044, (String)Titles.TitleType.NORMAL),
/*  88 */     Carpenter_Fine_Master(36, "Mighty Fine Carpenter", 10044, (String)Titles.TitleType.MASTER),
/*  89 */     Paving(37, "Roadbuilder", 10031, (String)Titles.TitleType.NORMAL),
/*  90 */     Paving_Master(38, "Road Warrior", 10031, (String)Titles.TitleType.MASTER),
/*  91 */     Prospecting(39, "Prospector", 10032, (String)Titles.TitleType.NORMAL),
/*  92 */     Prospecting_Master(40, "Goldsniffer", 10032, (String)Titles.TitleType.MASTER),
/*  93 */     Exorcism(41, "Exorcist", 10068, (String)Titles.TitleType.NORMAL),
/*  94 */     Exorcism_Master(42, "Terrorwringer", 10068, (String)Titles.TitleType.MASTER),
/*  95 */     Farmer(43, "Farmer", 10049, (String)Titles.TitleType.NORMAL),
/*  96 */     Farmer_Master(44, "Master Farmer", 10049, (String)Titles.TitleType.MASTER),
/*  97 */     Thief(45, "Pilferer", 1028, (String)Titles.TitleType.NORMAL),
/*  98 */     Thief_Minor(46, "Thief", 1028, (String)Titles.TitleType.MINOR),
/*  99 */     Thief_Master(47, "Master Thief", 1028, (String)Titles.TitleType.MASTER),
/* 100 */     Butcherer(48, "Butcher", 10059, (String)Titles.TitleType.NORMAL),
/* 101 */     Butcherer_Master(49, "Master Butcher", 10059, (String)Titles.TitleType.MASTER),
/* 102 */     Toymaker_Master(50, "Master Toymaker", 10051, (String)Titles.TitleType.MASTER),
/* 103 */     Toymaker(51, "Toymaker", 10051, (String)Titles.TitleType.NORMAL),
/* 104 */     Miller(52, "Miller", 10040, (String)Titles.TitleType.NORMAL),
/* 105 */     Miller_Master(53, "Master Miller", 10040, (String)Titles.TitleType.MASTER),
/* 106 */     Digging(54, "Digger", 1009, (String)Titles.TitleType.NORMAL),
/* 107 */     Digging_Master(55, "Land Shaper", 1009, (String)Titles.TitleType.MASTER),
/* 108 */     GiantSlayer(56, "Giantslayer"),
/* 109 */     TrollSlayer(57, "Trollslayer"),
/* 110 */     Cook(58, "Cook", 1018, (String)Titles.TitleType.NORMAL),
/* 111 */     Cook_Minor(59, "Chef", 1018, (String)Titles.TitleType.MINOR),
/* 112 */     Cook_Master(60, "Master Chef", 1018, (String)Titles.TitleType.MASTER),
/* 113 */     Mason_Minor(61, "High Mason", 1013, (String)Titles.TitleType.MINOR),
/* 114 */     Tailor(62, "Tailor", 10016, (String)Titles.TitleType.NORMAL),
/* 115 */     Tailor_Master(63, "Seamster", "Seamstress", 10016, (String)Titles.TitleType.MASTER),
/* 116 */     AnimalTrainer(64, "Animal Trainer", 10078, (String)Titles.TitleType.NORMAL),
/* 117 */     AnimalTrainer_Master(65, "Beastmaster", 10078, (String)Titles.TitleType.MASTER),
/* 118 */     Archer(66, "Archer", 1030, (String)Titles.TitleType.NORMAL),
/* 119 */     Archer_Minor(67, "Hawk Eye", 1030, (String)Titles.TitleType.MINOR),
/* 120 */     Archer_Master(68, "Eagle Eye", 1030, (String)Titles.TitleType.MASTER),
/* 121 */     Fletcher(69, "Fletcher", 1032, (String)Titles.TitleType.NORMAL),
/* 122 */     Fletcher_Master(70, "Master Fletcher", 1032, (String)Titles.TitleType.MASTER),
/* 123 */     Bowmaker(71, "Bowmaker", 1031, (String)Titles.TitleType.NORMAL),
/* 124 */     Bowmaker_Master(72, "Master Bowmaker", 1031, (String)Titles.TitleType.MASTER),
/* 125 */     Smith(73, "Smith", 1015, (String)Titles.TitleType.NORMAL),
/* 126 */     Smith_Minor(74, "Tinker", 1015, (String)Titles.TitleType.MINOR),
/* 127 */     Smith_Master(75, "Master Smith", 1015, (String)Titles.TitleType.MASTER),
/* 128 */     Repairer(76, "Repairman", "Repairwoman", 10035, (String)Titles.TitleType.NORMAL),
/* 129 */     Repairer_Minor(77, "Materia Tamer", 10035, (String)Titles.TitleType.MINOR),
/* 130 */     Repairer_Master(78, "Master Fixer", 10035, (String)Titles.TitleType.MASTER),
/* 131 */     Tracker(79, "Tracker", 10018, (String)Titles.TitleType.NORMAL),
/* 132 */     Tracker_Minor(80, "Pathfinder", 10018, (String)Titles.TitleType.MINOR),
/* 133 */     Tracker_Master(81, "Master Pathfinder", 10018, (String)Titles.TitleType.MASTER),
/* 134 */     Ageless(82, "Ageless"),
/* 135 */     Fisher(83, "Fisherman", "Fisherwoman", 10033, (String)Titles.TitleType.NORMAL),
/* 136 */     Fisher_Minor(84, "Angler", 10033, (String)Titles.TitleType.MINOR),
/* 137 */     Fisher_Master(85, "Kingfisher", 10033, (String)Titles.TitleType.MASTER),
/* 138 */     Climber(86, "Mountaineer", 10073, (String)Titles.TitleType.NORMAL),
/* 139 */     Climber_Master(87, "Cliffhanger", 10073, (String)Titles.TitleType.MASTER),
/* 140 */     Firemaker(88, "Pyrotechnic", 1010, (String)Titles.TitleType.NORMAL),
/* 141 */     Firemaker_Master(89, "Pyromaniac", 1010, (String)Titles.TitleType.MASTER),
/* 142 */     Misc(90, "Handyman", "Handywoman", 1020, (String)Titles.TitleType.NORMAL),
/* 143 */     Misc_Master(91, "Fixer", 1020, (String)Titles.TitleType.MASTER),
/* 144 */     Nature(92, "Ecologist", 1019, (String)Titles.TitleType.NORMAL),
/* 145 */     Nature_Master(93, "Druidkin", 1019, (String)Titles.TitleType.MASTER),
/* 146 */     KeeperTruth(94, "Keeper of Truth"),
/* 147 */     Praying(95, "Reverent", 10066, (String)Titles.TitleType.NORMAL),
/* 148 */     Praying_Master(96, "Ascetic", 10066, (String)Titles.TitleType.MASTER),
/* 149 */     Leather(97, "Tanner", 10017, (String)Titles.TitleType.NORMAL),
/* 150 */     Leather_Master(98, "Master Tanner", 10017, (String)Titles.TitleType.MASTER),
/* 151 */     Drunkard(99, "Drunkard", Titles.TitleType.MINOR),
/* 152 */     Alcoholic(100, "Alcoholic", Titles.TitleType.MINOR),
/* 153 */     Firemaker_Minor(101, "Arsonist", 1010, (String)Titles.TitleType.MINOR),
/* 154 */     Woodcutter_Minor(102, "Timberman", "Timberwoman", 1007, (String)Titles.TitleType.MINOR),
/* 155 */     Leather_Minor(103, "High Tanner", 10017, (String)Titles.TitleType.MINOR),
/* 156 */     Smithing_Black_Minor(104, "Renowned Blacksmith", 10015, (String)Titles.TitleType.MINOR),
/* 157 */     Digging_Minor(105, "Excavator", 1009, (String)Titles.TitleType.MINOR),
/* 158 */     Miner_Minor(106, "Prime Minester", 1008, (String)Titles.TitleType.MINOR),
/* 159 */     Ropemaking(107, "Ropemaker", 1014, (String)Titles.TitleType.NORMAL),
/* 160 */     Ropemaking_Minor(108, "Renowned Ropemaker", 1014, (String)Titles.TitleType.MINOR),
/* 161 */     Ropemaking_Master(109, "Hangman", "Hangwoman", 1014, (String)Titles.TitleType.MASTER),
/* 162 */     Coaler(110, "Coaler", 10036, (String)Titles.TitleType.NORMAL),
/* 163 */     Coaler_Master(111, "Master Coaler", 10036, (String)Titles.TitleType.MASTER),
/* 164 */     Carpenter_Fine_Minor(112, "Renowned Fine Carpenter", 10044, (String)Titles.TitleType.MINOR),
/* 165 */     Carpenter_Minor(113, "Renowned Carpenter", 1005, (String)Titles.TitleType.MINOR),
/* 166 */     Keeper_Faith(114, "Keeper of Faith"),
/* 167 */     Destroyer_Faith(115, "Destroyer of Faith"),
/* 168 */     Smithing_ShieldSmith(116, "Shieldsmith", 10014, (String)Titles.TitleType.NORMAL),
/* 169 */     Smithing_ShieldSmith_Master(117, "Master Shieldsmith", 10014, (String)Titles.TitleType.MASTER),
/* 170 */     One(118, "The Unforgiven King"),
/* 171 */     HFC(119, "Caterer", 10038, (String)Titles.TitleType.NORMAL),
/* 172 */     HFC_Master(120, "Saucier", 10038, (String)Titles.TitleType.MASTER),
/* 173 */     Smithing_GoldSmith_Minor(121, "Renowned Goldsmith", 10043, (String)Titles.TitleType.MINOR),
/* 174 */     Firstaid(122, "Quacksalver", 10056, (String)Titles.TitleType.NORMAL),
/* 175 */     Firstaid_Minor(123, "Medic", 10056, (String)Titles.TitleType.MINOR),
/* 176 */     Firstaid_Master(124, "Paramedic", 10056, (String)Titles.TitleType.MASTER),
/* 177 */     Yoyo(125, "Trickster", 10050, (String)Titles.TitleType.NORMAL),
/* 178 */     Yoyo_Minor(126, "Freestyler", 10050, (String)Titles.TitleType.MINOR),
/* 179 */     Yoyo_Master(127, "Wild One", 10050, (String)Titles.TitleType.MASTER),
/* 180 */     Stonecut(128, "Sculptor", 10074, (String)Titles.TitleType.NORMAL),
/* 181 */     Stonecut_Minor(129, "Artist", 10074, (String)Titles.TitleType.MINOR),
/* 182 */     Stonecut_Master(130, "Michelangelo", 10074, (String)Titles.TitleType.MASTER),
/* 183 */     UniqueSlayer(131, "Fearless"),
/* 184 */     Smithing_ShieldSmith_Minor(132, "Able Shieldsmith", 10014, (String)Titles.TitleType.MINOR),
/* 185 */     Farmer_Minor(133, "Crofter", 10049, (String)Titles.TitleType.MINOR),
/* 186 */     Platesmith(134, "Platesmith", 10013, (String)Titles.TitleType.NORMAL),
/* 187 */     Platesmith_Minor(135, "Renowned Platesmith", 10013, (String)Titles.TitleType.MINOR),
/* 188 */     Platesmith_Master(136, "Master Platesmith", 10013, (String)Titles.TitleType.MASTER),
/* 189 */     Chainsmith(137, "Chainsmith", 10012, (String)Titles.TitleType.NORMAL),
/* 190 */     Chainsmith_Minor(138, "Renowned Chainsmith", 10012, (String)Titles.TitleType.MINOR),
/* 191 */     Chainsmith_Master(139, "Master Chainsmith", 10012, (String)Titles.TitleType.MASTER),
/* 192 */     Kingdomtitle(140, "Kingdom Title", -1, (String)Titles.TitleType.MASTER),
/* 193 */     Smithing_Weapons_minor(141, "Arms Master", 1016, (String)Titles.TitleType.MINOR),
/* 194 */     Renowned_Bard(142, "Renowned Bard"),
/* 195 */     Kingslayer(143, "Kingslayer"),
/* 196 */     Prospecting_Minor(144, "Geologist", 10032, (String)Titles.TitleType.MINOR),
/* 197 */     Forester(145, "Forester", 10048, (String)Titles.TitleType.NORMAL),
/* 198 */     Forester_Master(146, "Arbophiliac", 10048, (String)Titles.TitleType.MASTER),
/* 199 */     Forester_Minor(147, "Arboriculturist", 10048, (String)Titles.TitleType.MINOR),
/* 200 */     Shipbuilder(148, "Harbormaster", 10082, (String)Titles.TitleType.NORMAL),
/* 201 */     Shipbuilder_Master(149, "Master Shipwright", 10082, (String)Titles.TitleType.MASTER),
/* 202 */     Shipbuilder_Minor(150, "Shipwright", 10082, (String)Titles.TitleType.MINOR),
/* 203 */     Bowyer_Minor(151, "Able Bowmaker", 1031, (String)Titles.TitleType.MINOR),
/* 204 */     Fletcher_Minor(152, "Arrowmaker", 1032, (String)Titles.TitleType.MINOR),
/* 205 */     Knifer(153, "Stabber", 10007, (String)Titles.TitleType.NORMAL),
/* 206 */     Misc_Minor(154, "Tim the Toolman", "Tim the Toolwoman", 1020, (String)Titles.TitleType.MINOR),
/* 207 */     Exor_Minor(155, "Zealot", 10068, (String)Titles.TitleType.MINOR),
/* 208 */     Tailoring(156, "Textiler", 1012, (String)Titles.TitleType.NORMAL),
/* 209 */     Tailoring_Minor(157, "Needlefriend", 1012, (String)Titles.TitleType.MINOR),
/* 210 */     Tailoring_Master(158, "Velcro Fly", 1012, (String)Titles.TitleType.MASTER),
/* 211 */     Swords(159, "Swordsman", "Swordswoman", 1000, (String)Titles.TitleType.NORMAL),
/* 212 */     Swords_Minor(160, "Duelist", 1000, (String)Titles.TitleType.MINOR),
/* 213 */     Swords_Master(161, "Swordsmaster", "Swordsmistress", 1000, (String)Titles.TitleType.MASTER),
/* 214 */     Axes(162, "Axeman", "Axewoman", 1003, (String)Titles.TitleType.NORMAL),
/* 215 */     Axes_Minor(163, "Berserker", 1003, (String)Titles.TitleType.MINOR),
/* 216 */     Axes_Master(164, "Fleshrender", 1003, (String)Titles.TitleType.MASTER),
/* 217 */     Mauls(165, "Mauler", 1004, (String)Titles.TitleType.NORMAL),
/* 218 */     Mauls_Minor(166, "Bonker", 1004, (String)Titles.TitleType.MINOR),
/* 219 */     Mauls_Master(167, "Crusher", 1004, (String)Titles.TitleType.MASTER),
/* 220 */     Soldier(168, "Soldier", 1023, (String)Titles.TitleType.NORMAL),
/* 221 */     Soldier_Minor(169, "Mercenary", 1023, (String)Titles.TitleType.MINOR),
/* 222 */     Soldier_Master(170, "Knight", "Amazon", 1023, (String)Titles.TitleType.MASTER),
/* 223 */     Shields(171, "Shieldsman", "Shieldwoman", 1002, (String)Titles.TitleType.NORMAL),
/* 224 */     Shields_Minor(172, "Defender", "Defendress", 1002, (String)Titles.TitleType.MINOR),
/* 225 */     Shields_Master(173, "Protector", 1002, (String)Titles.TitleType.MASTER),
/* 226 */     Warmachines(174, "Destroyer", 1029, (String)Titles.TitleType.NORMAL),
/* 227 */     Warmachines_Minor(175, "Demolisher", 1029, (String)Titles.TitleType.MINOR),
/* 228 */     Warmachines_Master(176, "Siege Master", "Siege Mistress", 1029, (String)Titles.TitleType.MASTER),
/* 229 */     Botanist_Minor(177, "Botanist", 10072, (String)Titles.TitleType.MINOR),
/* 230 */     Nature_Minor(178, "Treehugger", 1019, (String)Titles.TitleType.MINOR),
/* 231 */     Pottery_Minor(179, "Moulder", 1011, (String)Titles.TitleType.MINOR),
/* 232 */     Cooking_Minor(180, "Iron Chef", 10038, (String)Titles.TitleType.MINOR),
/* 233 */     Channeling_Minor(181, "Conduit", 10067, (String)Titles.TitleType.MINOR),
/* 234 */     Faith(182, "Padre", "Norn", (String)Titles.TitleType.NORMAL),
/* 235 */     Faith_Minor(183, "Devout", "Mystic", (String)Titles.TitleType.MINOR),
/* 236 */     Faith_Master(184, "Enlightened", Titles.TitleType.MASTER),
/* 237 */     Taming_Minor(185, "Beastspeaker", 10078, (String)Titles.TitleType.MINOR),
/* 238 */     Body(186, "Athlete", 1, (String)Titles.TitleType.NORMAL),
/* 239 */     Butcher_Minor(187, "Der Metzgermeister", "Skinner", 10059, (String)Titles.TitleType.MINOR),
/* 240 */     Beverages(188, "Bartender", 10083, (String)Titles.TitleType.NORMAL),
/* 241 */     Beverages_Minor(189, "Party Animal", 10083, (String)Titles.TitleType.MINOR),
/* 242 */     Beverages_Master(190, "Sommelier", 10083, (String)Titles.TitleType.MASTER),
/* 243 */     PA(191, "Community Assistant", Titles.TitleType.MASTER),
/* 244 */     Milking(192, "Milkman", "Milkmaid", 10060, (String)Titles.TitleType.NORMAL),
/* 245 */     Meditation(193, "Guru", 10086, (String)Titles.TitleType.NORMAL),
/* 246 */     Meditation_Minor(194, "Swami", 10086, (String)Titles.TitleType.MINOR),
/* 247 */     Meditation_Master(195, "Rama", 10086, (String)Titles.TitleType.MASTER),
/* 248 */     Alchemist_Minor(196, "Transmutator", 10042, (String)Titles.TitleType.MINOR),
/* 249 */     Tailor_Cloth_Minor(197, "Dreamweaver", 10016, (String)Titles.TitleType.MINOR),
/* 250 */     AnimalHusbandry(198, "Drover", 10085, (String)Titles.TitleType.NORMAL),
/* 251 */     AnimalHusbandry_Minor(199, "Granger", 10085, (String)Titles.TitleType.MINOR),
/* 252 */     AnimalHusbandry_Master(200, "Rancher", 10085, (String)Titles.TitleType.MASTER),
/* 253 */     Puppeteering(201, "Puppeteer", 10087, (String)Titles.TitleType.NORMAL),
/* 254 */     Puppeteering_Minor(202, "Entertainer", 10087, (String)Titles.TitleType.MINOR),
/* 255 */     Puppeteering_Master(203, "Puppetmaster", 10087, (String)Titles.TitleType.MASTER),
/* 256 */     Saint(204, "Saint"),
/* 257 */     ChampSlayer(205, "Slayer of Champions"),
/* 258 */     Baker(206, "Boulanger", 10039, (String)Titles.TitleType.NORMAL),
/* 259 */     Baker_Minor(207, "Bake Sale", 10039, (String)Titles.TitleType.MINOR),
/* 260 */     Baker_Master(208, "Confissier", 10039, (String)Titles.TitleType.MASTER),
/* 261 */     First_Digger(209, "First digger of Wurm", Titles.TitleType.MASTER),
/* 262 */     Gardener(210, "Greenthumbs", 10045, (String)Titles.TitleType.NORMAL),
/* 263 */     Gardener_Minor(211, "Gardener", 10045, (String)Titles.TitleType.MINOR),
/* 264 */     Gardener_Master(212, "Jiko Enkai Zenji", 10045, (String)Titles.TitleType.MASTER),
/* 265 */     Lockpicking(213, "Lock Breaker", 10076, (String)Titles.TitleType.NORMAL),
/* 266 */     Lockpicking_Minor(214, "Safe Cracker", 10076, (String)Titles.TitleType.MINOR),
/* 267 */     Lockpicking_Master(215, "Vault Shadow", 10076, (String)Titles.TitleType.MASTER),
/* 268 */     Godsent(216, "Godsent"),
/* 269 */     Educated(217, "Educated"),
/* 270 */     Clairvoyant(218, "Clairvoyant"),
/* 271 */     Smithing_Armor_minor(219, "Renowned Armourer", 1017, (String)Titles.TitleType.MINOR),
/* 272 */     Champ_Previous(220, "Champion Emeritus"),
/* 273 */     Catapult(221, "Rock Hurler", 10077, (String)Titles.TitleType.NORMAL),
/* 274 */     Catapult_Minor(222, "Wall Breaker", 10077, (String)Titles.TitleType.MINOR),
/* 275 */     Catapult_Master(223, "Eradicator", 10077, (String)Titles.TitleType.MASTER),
/* 276 */     Altar_Destroyer(224, "Dawn of Glory", Titles.TitleType.MASTER),
/* 277 */     Paving_Minor(225, "Road Engineer", 10031, (String)Titles.TitleType.MINOR),
/* 278 */     Soul(226, "Soulman", 106, (String)Titles.TitleType.NORMAL),
/* 279 */     Coaler_Minor(227, "Renowned Coaler", 10036, (String)Titles.TitleType.MINOR),
/* 280 */     Body_Minor(228, "Body Builder", 1, (String)Titles.TitleType.MINOR),
/* 281 */     Body_Major(229, "Olympian", 1, (String)Titles.TitleType.MASTER),
/* 282 */     Secrets_Master(230, "Master of Secrets", "Mistress of secrets"),
/* 283 */     Hota_One(231, "Participant of the Hunt"),
/* 284 */     Hota_Two(232, "Bloodhound of the Hunt"),
/* 285 */     Hota_Three(233, "Leader of the Hunt"),
/* 286 */     Hota_Four(234, "Master of the Hunt"),
/* 287 */     Hota_Five(235, "King of the Hunt"),
/* 288 */     CM(236, "Chat Moderator", Titles.TitleType.MASTER),
/* 289 */     Carving_Knife_Minor(237, "Carver", 10007, (String)Titles.TitleType.MINOR),
/* 290 */     Carving_Knife_Master(238, "Whittler", 10007, (String)Titles.TitleType.MASTER),
/* 291 */     Climbing_Minor(239, "Sherpa", 10073, (String)Titles.TitleType.MINOR),
/* 292 */     Knives(240, "Knifer", 1001, (String)Titles.TitleType.NORMAL),
/* 293 */     Knives_Minor(241, "Piercer", 1001, (String)Titles.TitleType.MINOR),
/* 294 */     Knives_Master(242, "Knife Specialist", 1001, (String)Titles.TitleType.MASTER),
/* 295 */     Metallurgy(243, "Smelter", 10041, (String)Titles.TitleType.NORMAL),
/* 296 */     Metallurgy_Minor(244, "Metallurgist", 10041, (String)Titles.TitleType.MINOR),
/* 297 */     Metallurgy_Master(245, "Master Metallurgist", 10041, (String)Titles.TitleType.MASTER),
/* 298 */     Toymaking_Minor(246, "Gepetto", 10051, (String)Titles.TitleType.MINOR),
/* 299 */     Locksmithing_Minor(247, "Safe Smith", 10034, (String)Titles.TitleType.MINOR),
/* 300 */     First_Crafter(248, "First Crafter of Wurm", Titles.TitleType.MASTER),
/* 301 */     Taunter_Master(249, "Loathsome", 10057, (String)Titles.TitleType.MASTER),
/* 302 */     Taunter(250, "Annoying", 10057, (String)Titles.TitleType.NORMAL),
/* 303 */     Taunter_Minor(251, "Repulsive", 10057, (String)Titles.TitleType.MINOR),
/* 304 */     Winner(252, "Winner", Titles.TitleType.MASTER),
/* 305 */     UnholyAlly(253, "Unholy Ally", Titles.TitleType.MASTER),
/*     */     
/* 307 */     SoldierLomaner(254, "Soldier of Lomaner"),
/* 308 */     RiderLomaner(255, "Rider of Lomaner"),
/* 309 */     ChieftainLomaner(256, "Chieftain of Lomaner"),
/* 310 */     AmbassadorLomaner(257, "Ambassador of Lomaner"),
/* 311 */     BaronLomaner(258, "Baron of Lomaner", "Baroness of Lomaner"),
/* 312 */     JarlLomaner(259, "Jarl of Lomaner", "Countess of Lomaner"),
/* 313 */     DukeLomaner(260, "Duke of Lomaner", "Duchess of Lomaner"),
/* 314 */     ProvostLomaner(261, "Provost of Lomaner"),
/* 315 */     MarquisLomaner(262, "Marquis of Lomaner", "Marchioness of Lomaner"),
/* 316 */     GrandDukeLomaner(263, "Grand Duke of Lomaner", "Grand Duchess of Lomaner"),
/* 317 */     ViceRoyLomaner(264, "ViceRoy of Lomaner"),
/* 318 */     PrinceLomaner(265, "Prince of Lomaner", "Princess of Lomaner"),
/*     */     
/* 320 */     Puppeteering_Legend(266, "First Jester of Wurm", 10087, (String)Titles.TitleType.LEGENDARY),
/* 321 */     Digger_Legend(267, "Earthwurm", 1009, (String)Titles.TitleType.LEGENDARY),
/* 322 */     Tracker_Legend(268, "Bloodhound of Wurm", 10018, (String)Titles.TitleType.LEGENDARY),
/* 323 */     Yoyo_Legend(269, "Legendary Woodspinner", 10050, (String)Titles.TitleType.LEGENDARY),
/* 324 */     Farmer_Legend(270, "Pumpkin King", 10049, (String)Titles.TitleType.LEGENDARY),
/* 325 */     Mining_Legend(271, "The World is Mine", 1008, (String)Titles.TitleType.LEGENDARY),
/* 326 */     Fishing_Legend(272, "Hooked on Fishing", 10033, (String)Titles.TitleType.LEGENDARY),
/* 327 */     Hotfood_Legend(273, "Legendary Chef", 10038, (String)Titles.TitleType.LEGENDARY),
/* 328 */     Fighting_Legend(274, "Swift Death", 1023, (String)Titles.TitleType.LEGENDARY),
/*     */     
/* 330 */     Foraging(275, "Forager", 10071, (String)Titles.TitleType.NORMAL),
/* 331 */     Foraging_Minor(276, "Plant Gatherer", 10071, (String)Titles.TitleType.MINOR),
/* 332 */     Foraging_Master(277, "Master of Plants", 10071, (String)Titles.TitleType.MASTER),
/*     */     
/* 334 */     Mind(278, "Thinker", 2, (String)Titles.TitleType.NORMAL),
/* 335 */     Mind_Minor(279, "Scholar", 2, (String)Titles.TitleType.MINOR),
/* 336 */     Mind_Master(280, "Philosopher", 2, (String)Titles.TitleType.MASTER),
/* 337 */     Soul_Normal(281, "Spiritualist", 3, (String)Titles.TitleType.NORMAL),
/* 338 */     Soul_Minor(282, "Visionary", 3, (String)Titles.TitleType.MINOR),
/* 339 */     Soul_Master(283, "Maverick", 3, (String)Titles.TitleType.MASTER),
/* 340 */     Alchemy_Normal(284, "Alchemist", 1021, (String)Titles.TitleType.NORMAL),
/* 341 */     Alchemy_Minor(285, "Chemist", 1021, (String)Titles.TitleType.MINOR),
/* 342 */     Alchemy_Master(286, "Shaman", 1021, (String)Titles.TitleType.MASTER),
/* 343 */     Clubs_Normal(287, "Clubber", 1025, (String)Titles.TitleType.NORMAL),
/* 344 */     Clubs_Minor(288, "Thumper", 1025, (String)Titles.TitleType.MINOR),
/* 345 */     Clubs_Master(289, "Smasher", 1025, (String)Titles.TitleType.MASTER),
/* 346 */     Dairy_Normal(290, "Cheesemaker", 10037, (String)Titles.TitleType.NORMAL),
/* 347 */     Dairy_Minor(291, "Artisan Cheesemaker", 10037, (String)Titles.TitleType.MINOR),
/* 348 */     Dairy_Master(292, "Master Cheesemaker", 10037, (String)Titles.TitleType.MASTER),
/* 349 */     Hammers_Normal(293, "Hammerer", 1027, (String)Titles.TitleType.NORMAL),
/* 350 */     Hammers_Minor(294, "Assaulter", 1027, (String)Titles.TitleType.MINOR),
/* 351 */     Hammers_Master(295, "Thor", 1027, (String)Titles.TitleType.MASTER),
/* 352 */     Healing_Normal(296, "Healer", 1024, (String)Titles.TitleType.NORMAL),
/* 353 */     Healing_Minor(297, "Doctor", 1024, (String)Titles.TitleType.MINOR),
/* 354 */     Healing_Master(298, "Surgeon", 1024, (String)Titles.TitleType.MASTER),
/* 355 */     Milling_Minor(299, "Grain Expert", 10040, (String)Titles.TitleType.MINOR),
/* 356 */     Milking_Minor(300, "Milking Machine", 10060, (String)Titles.TitleType.MINOR),
/* 357 */     Milking_Master(301, "Cow Whisperer", 10060, (String)Titles.TitleType.MASTER),
/* 358 */     Papyrus_Normal(302, "Papermaker", 10091, (String)Titles.TitleType.NORMAL),
/* 359 */     Papyrus_Minor(303, "Pulp-beater", 10091, (String)Titles.TitleType.MINOR),
/* 360 */     Papyrus_Master(304, "Book Maker", 10091, (String)Titles.TitleType.MASTER),
/* 361 */     Polearms_Normal(305, "Lancer", 1033, (String)Titles.TitleType.NORMAL),
/* 362 */     Polearms_Minor(306, "Phalanx", 1033, (String)Titles.TitleType.MINOR),
/* 363 */     Polearms_Master(307, "Impaler", 1033, (String)Titles.TitleType.MASTER),
/* 364 */     Religion_Normal(308, "Spiritual", 1026, (String)Titles.TitleType.NORMAL),
/* 365 */     Religion_Minor(309, "Theological", 1026, (String)Titles.TitleType.MINOR),
/* 366 */     Religion_Master(310, "Zealous", 1026, (String)Titles.TitleType.MASTER),
/* 367 */     Archaeology_Normal(311, "Investigator", 10069, (String)Titles.TitleType.NORMAL),
/* 368 */     Archaeology_Minor(312, "Archaeologist", 10069, (String)Titles.TitleType.MINOR),
/* 369 */     Archaeology_Master(313, "Curator", 10069, (String)Titles.TitleType.MASTER),
/* 370 */     Prayer_Minor(314, "Pious", 10066, (String)Titles.TitleType.MINOR),
/* 371 */     Preaching_Normal(315, "Preacher", 10065, (String)Titles.TitleType.NORMAL),
/* 372 */     Preaching_Minor(316, "Orator", 10065, (String)Titles.TitleType.MINOR),
/* 373 */     Preaching_Master(317, "Soothsayer", 10065, (String)Titles.TitleType.MASTER),
/* 374 */     Thatching_Normal(318, "Thatcher", 10092, (String)Titles.TitleType.NORMAL),
/* 375 */     Thatching_Minor(319, "Roofer", 10092, (String)Titles.TitleType.MINOR),
/* 376 */     Thatching_Master(320, "Master Thatcher", 10092, (String)Titles.TitleType.MASTER),
/* 377 */     Stealing_Normal(321, "Pickpocket", 10075, (String)Titles.TitleType.NORMAL),
/* 378 */     Stealing_Minor(322, "Burglar", 10075, (String)Titles.TitleType.MINOR),
/* 379 */     Stealing_Master(323, "Robber", 10075, (String)Titles.TitleType.MASTER),
/* 380 */     Traps_Normal(324, "It's a Trap!", 10084, (String)Titles.TitleType.NORMAL),
/* 381 */     Traps_Minor(325, "Trapper", 10084, (String)Titles.TitleType.MINOR),
/* 382 */     Traps_Master(326, "Snare Artist", 10084, (String)Titles.TitleType.MASTER),
/* 383 */     Toys_Normal(327, "Performer", 1022, (String)Titles.TitleType.NORMAL),
/* 384 */     Toys_Minor(328, "Manipulator", 1022, (String)Titles.TitleType.MINOR),
/* 385 */     Toys_Master(329, "Thespian", 1022, (String)Titles.TitleType.MASTER),
/*     */     
/* 387 */     AnimalHusbandry_Legend(330, "Zoologist", 10085, (String)Titles.TitleType.LEGENDARY),
/* 388 */     Faith_Legend(331, "Illuminated", 2147483645, (String)Titles.TitleType.LEGENDARY),
/* 389 */     Paving_Legend(332, "World Connector", 10031, (String)Titles.TitleType.LEGENDARY),
/* 390 */     Stealing_Legend(333, "Criminal Mastermind", 10075, (String)Titles.TitleType.LEGENDARY),
/* 391 */     Woodcutting_Legend(334, "Wurmian Termite", 1007, (String)Titles.TitleType.LEGENDARY),
/* 392 */     Milking_Legend(335, "Udder Madness", 10060, (String)Titles.TitleType.LEGENDARY),
/*     */     
/* 394 */     Puppeteering_Epic(336, "Epic Puppeteer", 10087, (String)Titles.TitleType.EPIC),
/* 395 */     Digger_Epic(337, "Epic Digger", 1009, (String)Titles.TitleType.EPIC),
/* 396 */     Tracker_Epic(338, "Epic Tracker", 10018, (String)Titles.TitleType.EPIC),
/* 397 */     Yoyo_Epic(339, "Epic Woodspinner", 10050, (String)Titles.TitleType.EPIC),
/* 398 */     Farmer_Epic(340, "Epic Farmer", 10049, (String)Titles.TitleType.EPIC),
/* 399 */     Mining_Epic(341, "Epic Miner", 1008, (String)Titles.TitleType.EPIC),
/* 400 */     Fishing_Epic(342, "Epic Fisherman", "Epic Fisherwoman", 10033, (String)Titles.TitleType.EPIC),
/* 401 */     Hotfood_Epic(343, "Epic Chef", 10038, (String)Titles.TitleType.EPIC),
/* 402 */     Fighting_Epic(344, "Epic Fighter", 1023, (String)Titles.TitleType.EPIC),
/* 403 */     AnimalHusbandry_Epic(345, "Epic Animal Tamer", 10085, (String)Titles.TitleType.EPIC),
/* 404 */     Paving_Epic(346, "Epic Paver", 10031, (String)Titles.TitleType.EPIC),
/* 405 */     Stealing_Epic(347, "Epic Thief", 10075, (String)Titles.TitleType.EPIC),
/* 406 */     Woodcutting_Epic(348, "Epic Lumberjack", 1007, (String)Titles.TitleType.EPIC),
/* 407 */     Milking_Epic(349, "Epic Milkman", "Epic Milkmaid", 10060, (String)Titles.TitleType.EPIC),
/*     */     
/* 409 */     Carpenter_Legend(350, "Legendary Architect", 1005, (String)Titles.TitleType.LEGENDARY),
/* 410 */     Carpenter_Epic(351, "Epic Architect", 1005, (String)Titles.TitleType.EPIC),
/*     */     
/* 412 */     Saw_Normal(352, "I'm Sawry", 10008, (String)Titles.TitleType.NORMAL),
/* 413 */     Saw_Minor(353, "Chainsaw", 10008, (String)Titles.TitleType.MINOR),
/* 414 */     Saw_Master(354, "Jigsaw", 10008, (String)Titles.TitleType.MASTER),
/* 415 */     Saw_Legendary(355, "Tom Sawyer", 10008, (String)Titles.TitleType.LEGENDARY),
/* 416 */     Saw_Epic(356, "Epic Sawyer", 10008, (String)Titles.TitleType.EPIC),
/*     */     
/* 418 */     Master_of_Challenge(357, "Master of the Challenge", "Mistress of the Challenge"),
/* 419 */     Lord_of_Isles(358, "Lord of the Isles", "Lady of the Isles"),
/* 420 */     Blood_Ravager(359, "Blood Ravager"),
/* 421 */     Selfie(360, "Selfie"),
/* 422 */     Pit_Slayer(361, "Pit Slayer"),
/* 423 */     Guardian_of_the_Hunt(362, "Guardian of the Hunt"),
/* 424 */     Shadow_Assassin(363, "Shadow Assassin"),
/* 425 */     Future_Warden(364, "Future Warden"),
/* 426 */     Emperor_Emeritus(365, "Emperor Emeritus"),
/* 427 */     Pack_of_the_Hunt(366, "Pack of the Hunt"),
/*     */     
/* 429 */     Leather_Legend(367, "Master of the Hide", "Mistress of the Hide", 10017, (String)Titles.TitleType.LEGENDARY),
/* 430 */     Leather_Epic(368, "Epic Tanner", 10017, (String)Titles.TitleType.EPIC),
/* 431 */     Knigt(369, "Cavalier"),
/* 432 */     FallenKnight(370, "Fallen Cavalier"),
/*     */     
/* 434 */     Blacksmith_Legend(371, "Anvil of Tears", 10015, (String)Titles.TitleType.LEGENDARY),
/* 435 */     Blacksmith_Epic(372, "Epic Blacksmith", 10015, (String)Titles.TitleType.EPIC),
/* 436 */     Rifter(373, "Rifter"),
/* 437 */     RiftDefender(374, "Rift Defender"),
/*     */     
/* 439 */     Fletcher_Legend(375, "Quivering with Excitement", 1032, (String)Titles.TitleType.LEGENDARY),
/* 440 */     Fletcher_Epic(376, "Epic Fletcher", 1032, (String)Titles.TitleType.EPIC),
/*     */     
/* 442 */     Chainsmith_Legend(377, "Chains of Time", 10012, (String)Titles.TitleType.LEGENDARY),
/* 443 */     Chainsmith_Epic(378, "Epic Chainsmith", 10012, (String)Titles.TitleType.EPIC),
/* 444 */     Platesmith_Epic(379, "Imperial Dragonsmith", 10013, (String)Titles.TitleType.LEGENDARY),
/* 445 */     Bowyry_Legend(380, "Bow Down", 1031, (String)Titles.TitleType.LEGENDARY),
/*     */     
/* 447 */     Smithing_ShieldSmith_Legend(381, "Forge of the Shieldwall", 10014, (String)Titles.TitleType.LEGENDARY),
/* 448 */     Smithing_ShieldSmith_Epic(382, "Epic Shieldsmith", 10014, (String)Titles.TitleType.EPIC),
/*     */     
/* 450 */     Beverages_Legend(383, "Cocktail Shaker", 10083, (String)Titles.TitleType.LEGENDARY),
/* 451 */     Beverages_Epic(384, "Cocktail Shaker", 10083, (String)Titles.TitleType.EPIC),
/*     */     
/* 453 */     Restoration_Normal(385, "Restorer", 10095, (String)Titles.TitleType.NORMAL),
/* 454 */     Restoration_Minor(386, "Fragmented", 10095, (String)Titles.TitleType.MINOR),
/* 455 */     Restoration_Master(387, "Conservator", 10095, (String)Titles.TitleType.MASTER),
/*     */ 
/*     */     
/* 458 */     Masonry_Legend(388, "Another Brick In The Wall", 1013, (String)Titles.TitleType.LEGENDARY),
/* 459 */     Masonry_Epic(389, "Another Brick In The Wall", 1013, (String)Titles.TitleType.EPIC),
/*     */ 
/*     */     
/* 462 */     Prospecting_Legend(390, "I am so Vein", 10032, (String)Titles.TitleType.LEGENDARY),
/*     */     
/* 464 */     Aggressivefighting_Normal(391, "Feisty", 10053, (String)Titles.TitleType.NORMAL),
/* 465 */     Aggressivefighting_Minor(392, "Belligerent", 10053, (String)Titles.TitleType.MINOR),
/* 466 */     Aggressivefighting_Master(393, "Barbarian", 10053, (String)Titles.TitleType.MASTER),
/* 467 */     BodyControl_Normal(394, "Controlled", 104, (String)Titles.TitleType.NORMAL),
/* 468 */     BodyControl_Minor(395, "Disciplined", 104, (String)Titles.TitleType.MINOR),
/* 469 */     BodyControl_Master(396, "Acrobat", 104, (String)Titles.TitleType.MASTER),
/* 470 */     Bodystamina_Normal(397, "Enduring", 103, (String)Titles.TitleType.NORMAL),
/* 471 */     Bodystamina_Minor(398, "Perseverent", 103, (String)Titles.TitleType.MINOR),
/* 472 */     Bodystamina_Master(399, "Indefatigable", 103, (String)Titles.TitleType.MASTER),
/* 473 */     Bodystrength_Normal(400, "Robust", 102, (String)Titles.TitleType.NORMAL),
/* 474 */     Bodystrength_Minor(401, "Strongman", 102, (String)Titles.TitleType.MINOR),
/* 475 */     Bodystrength_Master(402, "Atlas", 102, (String)Titles.TitleType.MASTER),
/* 476 */     Butcheringknife_Normal(403, "Dissector", 10029, (String)Titles.TitleType.NORMAL),
/* 477 */     Butcheringknife_Minor(404, "Skinner", 10029, (String)Titles.TitleType.MINOR),
/* 478 */     Butcheringknife_Master(405, "Cleaver", 10029, (String)Titles.TitleType.MASTER),
/* 479 */     Defensivefighting_Normal(406, "Defensive", 10054, (String)Titles.TitleType.NORMAL),
/* 480 */     Defensivefighting_Minor(407, "Steadfast", 10054, (String)Titles.TitleType.MINOR),
/* 481 */     Defensivefighting_Master(408, "Guardian", 10054, (String)Titles.TitleType.MASTER),
/* 482 */     Halberd_Normal(409, "Pikeman", 10089, (String)Titles.TitleType.NORMAL),
/* 483 */     Halberd_Minor(410, "Halberdier", 10089, (String)Titles.TitleType.MINOR),
/* 484 */     Halberd_Master(411, "Man At Arms", "Woman At Arms", 10089, (String)Titles.TitleType.MASTER),
/* 485 */     Hammer_Normal(412, "Tapper", 10026, (String)Titles.TitleType.NORMAL),
/* 486 */     Hammer_Minor(413, "Knocker", 10026, (String)Titles.TitleType.MINOR),
/* 487 */     Hammer_Master(414, "Pounder", 10026, (String)Titles.TitleType.MASTER),
/* 488 */     Hatchet_Normal(415, "Woodsman", 10003, (String)Titles.TitleType.NORMAL),
/* 489 */     Hatchet_Minor(416, "Hewer", 10003, (String)Titles.TitleType.MINOR),
/* 490 */     Hatchet_Master(417, "Tomahawk", 10003, (String)Titles.TitleType.MASTER),
/* 491 */     Hugeaxe_Normal(418, "Huge Axeman", "Huge Axewoman", 10025, (String)Titles.TitleType.NORMAL),
/* 492 */     Hugeaxe_Minor(419, "Headsman", 10025, (String)Titles.TitleType.MINOR),
/* 493 */     Hugeaxe_Master(420, "Axecutioner", 10025, (String)Titles.TitleType.MASTER),
/* 494 */     HugeClub_Normal(421, "Troll", 10064, (String)Titles.TitleType.NORMAL),
/* 495 */     HugeClub_Minor(422, "Forest Giant", 10064, (String)Titles.TitleType.MINOR),
/* 496 */     HugeClub_Master(423, "Kyklops", 10064, (String)Titles.TitleType.MASTER),
/* 497 */     Largeaxe_Normal(424, "Large Axeman", "Large Axewoman", 10024, (String)Titles.TitleType.NORMAL),
/* 498 */     Largeaxe_Minor(425, "Battleaxe", 10024, (String)Titles.TitleType.MINOR),
/* 499 */     Largeaxe_Master(426, "Viking", 10024, (String)Titles.TitleType.MASTER),
/* 500 */     LargeMaul_Normal(427, "Large Mauler", 10061, (String)Titles.TitleType.NORMAL),
/* 501 */     LargeMaul_Minor(428, "Siegebreaker", 10061, (String)Titles.TitleType.MINOR),
/* 502 */     LargeMaul_Master(429, "Hand of Magranon", 10061, (String)Titles.TitleType.MASTER),
/* 503 */     Largemetalshield_Normal(430, "Sturdy Heavy Defender", 10023, (String)Titles.TitleType.NORMAL),
/* 504 */     Largemetalshield_Minor(431, "Sturdy Heavy Bastion", 10023, (String)Titles.TitleType.MINOR),
/* 505 */     Largemetalshield_Master(432, "Sturdy Heavy Bulwark", 10023, (String)Titles.TitleType.MASTER),
/* 506 */     Largewoodenshield_Normal(433, "Nimble Heavy Defender", 10021, (String)Titles.TitleType.NORMAL),
/* 507 */     Largewoodenshield_Minor(434, "Nimble Heavy Bastion", 10021, (String)Titles.TitleType.MINOR),
/* 508 */     Largewoodenshield_Master(435, "Nimble Heavy Bulwark", 10021, (String)Titles.TitleType.MASTER),
/* 509 */     Longbow_Normal(436, "Longbowman", 10081, (String)Titles.TitleType.NORMAL),
/* 510 */     Longbow_Minor(437, "Marksman", 10081, (String)Titles.TitleType.MINOR),
/* 511 */     Longbow_Master(438, "Robin Hood", 10081, (String)Titles.TitleType.MASTER),
/* 512 */     LongSpear_Normal(439, "Spearman", 10088, (String)Titles.TitleType.NORMAL),
/* 513 */     LongSpear_Minor(440, "Hussar", 10088, (String)Titles.TitleType.MINOR),
/* 514 */     LongSpear_Master(441, "Spartan", 10088, (String)Titles.TitleType.MASTER),
/* 515 */     Longsword_Normal(442, "Long Swordsman", 10005, (String)Titles.TitleType.NORMAL),
/* 516 */     Longsword_Minor(443, "Fighter", 10005, (String)Titles.TitleType.MINOR),
/* 517 */     Longsword_Master(444, "Myrmidon", 10005, (String)Titles.TitleType.MASTER),
/* 518 */     Mediumbow_Normal(445, "Bowman", 10080, (String)Titles.TitleType.NORMAL),
/* 519 */     Mediumbow_Minor(446, "Skirmisher", 10080, (String)Titles.TitleType.MINOR),
/* 520 */     Mediumbow_Master(447, "Ranger", 10080, (String)Titles.TitleType.MASTER),
/* 521 */     Mediummaul_Normal(448, "Medium Mauler", 10062, (String)Titles.TitleType.NORMAL),
/* 522 */     Mediummaul_Minor(449, "Whacker", 10062, (String)Titles.TitleType.MINOR),
/* 523 */     Mediummaul_Master(450, "Cracker", 10062, (String)Titles.TitleType.MASTER),
/* 524 */     Mediummetalshield_Normal(451, "Sturdy Defender", 10006, (String)Titles.TitleType.NORMAL),
/* 525 */     Mediummetalshield_Minor(452, "Sturdy Bastion", 10006, (String)Titles.TitleType.MINOR),
/* 526 */     Mediummetalshield_Master(453, "Sturdy Bulwark", 10006, (String)Titles.TitleType.MASTER),
/* 527 */     Mediumwoodenshield_Normal(454, "Nimble Defender", 10020, (String)Titles.TitleType.NORMAL),
/* 528 */     Mediumwoodenshield_Minor(455, "Nimble Bastion", 10020, (String)Titles.TitleType.MINOR),
/* 529 */     Mediumwoodenshield_Master(456, "Nimble Bulwark", 10020, (String)Titles.TitleType.MASTER),
/* 530 */     MindLogic_Normal(457, "Logician", 100, (String)Titles.TitleType.NORMAL),
/* 531 */     MindLogic_Minor(458, "Genius", 100, (String)Titles.TitleType.MINOR),
/* 532 */     MindLogic_Master(459, "Smarty Pants", 100, (String)Titles.TitleType.MASTER),
/* 533 */     MindSpeed_Normal(460, "Quick", 101, (String)Titles.TitleType.NORMAL),
/* 534 */     MindSpeed_Minor(461, "Witty", 101, (String)Titles.TitleType.MINOR),
/* 535 */     MindSpeed_Master(462, "Prodigy", 101, (String)Titles.TitleType.MASTER),
/* 536 */     Normalfighting_Normal(463, "Infantry", 10055, (String)Titles.TitleType.NORMAL),
/* 537 */     Normalfighting_Minor(464, "Sergeant", 10055, (String)Titles.TitleType.MINOR),
/* 538 */     Normalfighting_Master(465, "Captain", 10055, (String)Titles.TitleType.MASTER),
/* 539 */     Pickaxe_Normal(466, "Rockbreaker", 10009, (String)Titles.TitleType.NORMAL),
/* 540 */     Pickaxe_Minor(467, "Tunneller", 10009, (String)Titles.TitleType.MINOR),
/* 541 */     Pickaxe_Master(468, "Vein Destroyer", 10009, (String)Titles.TitleType.MASTER),
/* 542 */     Rake_Normal(469, "Cultivator", 10004, (String)Titles.TitleType.NORMAL),
/* 543 */     Rake_Minor(470, "Furrower", 10004, (String)Titles.TitleType.MINOR),
/* 544 */     Rake_Master(471, "Harrower", 10004, (String)Titles.TitleType.MASTER),
/* 545 */     Scythe_Normal(472, "Harvester", 10047, (String)Titles.TitleType.NORMAL),
/* 546 */     Scythe_Minor(473, "Reaper", 10047, (String)Titles.TitleType.MINOR),
/* 547 */     Scythe_Master(474, "Hand of Libila", 10047, (String)Titles.TitleType.MASTER),
/* 548 */     Shieldbashing_Normal(475, "Basher", 10058, (String)Titles.TitleType.NORMAL),
/* 549 */     Shieldbashing_Minor(476, "Stunner", 10058, (String)Titles.TitleType.MINOR),
/* 550 */     Shieldbashing_Master(477, "Juggernaut", 10058, (String)Titles.TitleType.MASTER),
/* 551 */     Shortbow_Normal(478, "Shortbowman", 10079, (String)Titles.TitleType.NORMAL),
/* 552 */     Shortbow_Minor(479, "Bowhunter", 10079, (String)Titles.TitleType.MINOR),
/* 553 */     Shortbow_Master(480, "Point Blank", 10079, (String)Titles.TitleType.MASTER),
/* 554 */     Shortsword_Normal(481, "Short Swordsman", "Short Swordswoman", 10027, (String)Titles.TitleType.NORMAL),
/* 555 */     Shortsword_Minor(482, "Gladiator", 10027, (String)Titles.TitleType.MINOR),
/* 556 */     Shortsword_Master(483, "Assassin", 10027, (String)Titles.TitleType.MASTER),
/* 557 */     Shovel_Normal(484, "Shoveler", 10002, (String)Titles.TitleType.NORMAL),
/* 558 */     Shovel_Minor(485, "Packer", 10002, (String)Titles.TitleType.MINOR),
/* 559 */     Shovel_Master(486, "Undertaker", 10002, (String)Titles.TitleType.MASTER),
/* 560 */     Sickle_Normal(487, "Pruner", 10046, (String)Titles.TitleType.NORMAL),
/* 561 */     Sickle_Minor(488, "Gatherer", 10046, (String)Titles.TitleType.MINOR),
/* 562 */     Sickle_Master(489, "Fully Sickle", 10046, (String)Titles.TitleType.MASTER),
/* 563 */     SmallAxe_Normal(490, "Small Axeman", "Small Axewoman", 10001, (String)Titles.TitleType.NORMAL),
/* 564 */     SmallAxe_Minor(491, "Hacker", 10001, (String)Titles.TitleType.MINOR),
/* 565 */     SmallAxe_Master(492, "Ripper", 10001, (String)Titles.TitleType.MASTER),
/* 566 */     Smallmaul_Normal(493, "Small Mauler", 10063, (String)Titles.TitleType.NORMAL),
/* 567 */     Smallmaul_Minor(494, "Banger", 10063, (String)Titles.TitleType.MINOR),
/* 568 */     Smallmaul_Master(495, "Masher", 10063, (String)Titles.TitleType.MASTER),
/* 569 */     Smallmetalshield_Normal(496, "Sturdy Light Defender", 10022, (String)Titles.TitleType.NORMAL),
/* 570 */     Smallmetalshield_Minor(497, "Sturdy Light Bastion", 10022, (String)Titles.TitleType.MINOR),
/* 571 */     Smallmetalshield_Master(498, "Sturdy Light Bulwark", 10022, (String)Titles.TitleType.MASTER),
/* 572 */     Smallwoodenshield_Normal(499, "Nimble Light Defender", 10019, (String)Titles.TitleType.NORMAL),
/* 573 */     Smallwoodenshield_Minor(500, "Nimble Light Bastion", 10019, (String)Titles.TitleType.MINOR),
/* 574 */     Smallwoodenshield_Master(501, "Nimble Light Bulwark", 10019, (String)Titles.TitleType.MASTER),
/* 575 */     Staff_Normal(502, "Disciple", 10090, (String)Titles.TitleType.NORMAL),
/* 576 */     Staff_Minor(503, "Monk", 10090, (String)Titles.TitleType.MINOR),
/* 577 */     Staff_Master(504, "Sensei", 10090, (String)Titles.TitleType.MASTER),
/* 578 */     StoneChisel_Normal(505, "Brickmaker", 10030, (String)Titles.TitleType.NORMAL),
/* 579 */     StoneChisel_Minor(506, "Chipper", 10030, (String)Titles.TitleType.MINOR),
/* 580 */     StoneChisel_Master(507, "One More Brick", 10030, (String)Titles.TitleType.MASTER),
/* 581 */     Twohandedsword_Normal(508, "Twohanded Swordsman", 10028, (String)Titles.TitleType.NORMAL),
/* 582 */     Twohandedsword_Minor(509, "Templar", 10028, (String)Titles.TitleType.MINOR),
/* 583 */     Twohandedsword_Master(510, "Paladin", 10028, (String)Titles.TitleType.MASTER),
/* 584 */     Warhammer_Normal(511, "Sledgehammer", 10070, (String)Titles.TitleType.NORMAL),
/* 585 */     Warhammer_Minor(512, "Dwarf", 10070, (String)Titles.TitleType.MINOR),
/* 586 */     Warhammer_Master(513, "Stag King", 10070, (String)Titles.TitleType.MASTER),
/* 587 */     Weaponlessfighting_Normal(514, "Lightweight", 10052, (String)Titles.TitleType.NORMAL),
/* 588 */     Weaponlessfighting_Minor(515, "Middleweight", 10052, (String)Titles.TitleType.MINOR),
/* 589 */     Weaponlessfighting_Master(516, "Heavyweight", 10052, (String)Titles.TitleType.MASTER),
/*     */     
/* 591 */     Shovel_Legendary(517, "Ace Of Spades", 10002, (String)Titles.TitleType.LEGENDARY),
/* 592 */     Shipbuilding_Legendary(518, "Ancient Mariner", 10082, (String)Titles.TitleType.LEGENDARY),
/*     */     
/* 594 */     Journal_T0(519, "Apprentice"),
/* 595 */     Journal_T1(520, "Learned"),
/* 596 */     Journal_T2(521, "Experienced"),
/* 597 */     Journal_T3(522, "Skilled"),
/* 598 */     Journal_T4(523, "Accomplished"),
/* 599 */     Journal_T5(524, "Proficient"),
/* 600 */     Journal_T6(525, "Talented"),
/*     */ 
/*     */     
/* 603 */     Restoration_Legendary(526, "Ancient Fraggle", 10095, (String)Titles.TitleType.LEGENDARY),
/*     */ 
/*     */     
/* 606 */     Forester_Legendary(527, "Lorax", 10048, (String)Titles.TitleType.LEGENDARY),
/*     */     
/* 608 */     Journal_T7(528, "Professional"),
/* 609 */     Journal_T8(529, "Expert"),
/* 610 */     Journal_T9(530, "Master"),
/* 611 */     Journal_P1(531, "Blessed"),
/* 612 */     Journal_P2(532, "Angelic"),
/* 613 */     Journal_P3(533, "Divine"),
/*     */     
/* 615 */     Pickaxe_Legendary(534, "Mountain Eater", 10009, (String)Titles.TitleType.LEGENDARY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final int id;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String femaleName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int skillId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final Titles.TitleType type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Title(int _id, String _name, String _femaleName, int _skillId, Titles.TitleType _type) {
/*     */       this.name = _name;
/*     */       this.femaleName = _femaleName;
/*     */       this.id = _id;
/*     */       this.skillId = _skillId;
/*     */       this.type = _type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getTitleId() {
/*     */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/*     */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getFemaleName() {
/*     */       return this.femaleName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 698 */     private static final Title[] titleArray = values();
/*     */     
/*     */     public String getName(boolean getMaleName) {
/*     */       if (!getMaleName)
/*     */         return this.femaleName; 
/*     */       return this.name;
/*     */     }
/*     */     
/*     */     public int getSkillId() {
/*     */       return this.skillId;
/*     */     }
/*     */     
/*     */     public static Title getTitle(String titleName, boolean isMaleName) {
/* 711 */       for (int x = 0; x < titleArray.length; x++) {
/*     */         
/* 713 */         if (titleArray[x].getName(isMaleName).equalsIgnoreCase(titleName))
/* 714 */           return titleArray[x]; 
/*     */       } 
/* 716 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static Title getTitle(int titleAsInt) {
/* 722 */       for (int i = 0; i < titleArray.length; i++) {
/*     */         
/* 724 */         if (titleAsInt == titleArray[i].getTitleId()) {
/* 725 */           return titleArray[i];
/*     */         }
/*     */       } 
/* 728 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static Title getTitle(int skillId, Titles.TitleType titleType) {
/* 734 */       for (int i = 0; i < titleArray.length; i++) {
/*     */         
/* 736 */         if (skillId == titleArray[i].getSkillId() && titleType == titleArray[i].getTitleType()) {
/* 737 */           return titleArray[i];
/*     */         }
/*     */       } 
/* 740 */       return null;
/*     */     }
/*     */     
/*     */     public Titles.TitleType getTitleType() {
/*     */       return this.type;
/*     */     }
/*     */     
/*     */     public boolean isRoyalTitle() {
/*     */       return (this.id == Kingdomtitle.id);
/*     */     }
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Titles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */