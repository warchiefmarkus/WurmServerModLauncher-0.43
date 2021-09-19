/*      */ package com.wurmonline.server.creatures;
/*      */ 
/*      */ import com.wurmonline.server.Features;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.combat.ArmourTemplate;
/*      */ import com.wurmonline.server.creatures.ai.CreatureAI;
/*      */ import com.wurmonline.server.creatures.ai.scripts.FishAI;
/*      */ import com.wurmonline.server.skills.Skills;
/*      */ import com.wurmonline.server.skills.SkillsFactory;
/*      */ import com.wurmonline.shared.constants.CreatureTypes;
/*      */ import com.wurmonline.shared.constants.ItemMaterials;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.io.IOException;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class CreatureTemplateCreator
/*      */   implements CreatureTypes, CreatureTemplateIds, SoundNames, ItemMaterials
/*      */ {
/*   54 */   private static final Logger logger = Logger.getLogger(CreatureTemplateCreator.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getDragonLoot(int type) {
/*   73 */     switch (type) {
/*      */       
/*      */       case 89:
/*   76 */         return 979;
/*      */       case 91:
/*   78 */         return 980;
/*      */       case 16:
/*   80 */         return 977;
/*      */       case 90:
/*   82 */         return 986;
/*      */       case 92:
/*   84 */         return 975;
/*      */       case 18:
/*   86 */         return 976;
/*      */       case 104:
/*   88 */         return 978;
/*      */       case 17:
/*   90 */         return 973;
/*      */       case 103:
/*   92 */         return 974;
/*      */       case 19:
/*   94 */         return 975;
/*      */     } 
/*      */     
/*   97 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getRandomDrakeId() {
/*  106 */     int rng = Server.rand.nextInt(5);
/*  107 */     switch (rng) {
/*      */       
/*      */       case 0:
/*  110 */         return 103;
/*      */       case 1:
/*  112 */         return 17;
/*      */       case 2:
/*  114 */         return 104;
/*      */       case 3:
/*  116 */         return 19;
/*      */       case 4:
/*  118 */         return 18;
/*      */     } 
/*  120 */     return 104;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getRandomDragonOrDrakeId() {
/*  130 */     int rng = Server.rand.nextInt(10);
/*  131 */     switch (rng) {
/*      */       
/*      */       case 0:
/*  134 */         return 91;
/*      */       case 1:
/*  136 */         return 104;
/*      */       case 2:
/*  138 */         return 16;
/*      */       case 3:
/*  140 */         return 103;
/*      */       case 4:
/*  142 */         return 89;
/*      */       case 5:
/*  144 */         return 18;
/*      */       case 6:
/*  146 */         return 90;
/*      */       case 7:
/*  148 */         return 17;
/*      */       case 8:
/*  150 */         return 92;
/*      */       case 9:
/*  152 */         return 19;
/*      */     } 
/*  154 */     return 103;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void createCreatureTemplates() {
/*  160 */     logger.info("Starting to create Creature Templates");
/*  161 */     long start = System.currentTimeMillis();
/*      */     
/*  163 */     createCreatureTemplate(1, "Human", "Humans", "Another explorer.");
/*  164 */     createCreatureTemplate(9, "Salesman", "Salesman", "An envoy from the king, buying and selling items.");
/*  165 */     createCreatureTemplate(3, "Brown cow", "Brown cows", "A brown docile cow.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  172 */     createCreatureTemplate(7, "guardTough", "guardToughs", "This warrior would pose problems for any intruder.");
/*      */     
/*  174 */     createCreatureTemplate(8, "guardBrutal", "guardBrutals", "Not many people would like to cross this warrior.");
/*      */     
/*  176 */     createCreatureTemplate(10, "Black wolf", "Black wolves", "This dark shadow of the forests glares hungrily at you.");
/*  177 */     createCreatureTemplate(11, "Troll", "Trolls", "A dark green stinking troll. Always hungry. Always deadly.");
/*  178 */     createCreatureTemplate(12, "Brown bear", "Brown bears", "The brown bear has a distinctive hump on the shoulders, and long deadly claws on the front paws.");
/*      */     
/*  180 */     createCreatureTemplate(42, "Black bear", "Black bears", "The black bear looks pretty kind, but has strong, highly curved claws ready to render you to pieces.");
/*      */     
/*  182 */     createCreatureTemplate(13, "Large rat", "Large rats", "This is an unnaturally large version of a standard black rat.");
/*  183 */     createCreatureTemplate(43, "Cave bug", "Cave bugs", "Some kind of unnaturally large and deformed insect lunges at you from the dark. It has a grey carapace, with small patches of lichen growing here and there.");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  188 */     createCreatureTemplate(14, "Mountain lion", "Mountain lions", "Looking like a huge cat, it is tawny-coloured, with a small head and small, rounded, black-tipped ears.");
/*      */     
/*  190 */     createCreatureTemplate(15, "Wild cat", "Wild cats", "A small wild cat, fierce and aggressive.");
/*  191 */     createCreatureTemplate(2, "Joe the Stupe", "Joe the Stupes", "A hollow-eyed person is standing here, potentially dangerous but stupid as ever.");
/*      */     
/*  193 */     createCreatureTemplate(16, "Red dragon", "Red dragons", "The menacing huge dragon, with scales in every possible red color.");
/*      */     
/*  195 */     createCreatureTemplate(17, "Green dragon hatchling", "Green dragon hatchlings", "The green dragon hatchling is not as large as a full-grown dragon and unable to fly.");
/*      */     
/*  197 */     createCreatureTemplate(18, "Black dragon hatchling", "Black dragon hatchlings", "The black dragon hatchling is not as large as a full-grown dragon and unable to fly.");
/*      */     
/*  199 */     createCreatureTemplate(19, "White dragon hatchling", "White dragon hatchlings", "The white dragon hatchling is not as large as a full-grown dragon and unable to fly.");
/*      */     
/*  201 */     createCreatureTemplate(20, "Forest giant", "Forest giants", "With an almost sad look upon its face, this filthy giant might be mistaken for a harmless huge baby.");
/*      */     
/*  203 */     createCreatureTemplate(21, "Unicorn", "Unicorns", "A bright white unicorn with a slender twisted horn.");
/*  204 */     createCreatureTemplate(118, "Unicorn Foal", "Unicorn Foals", "A small bright white unicorn foal with a budding horn.");
/*  205 */     createCreatureTemplate(22, "Kyklops", "Kyklops", "This large drooling one-eyed giant is obviously too stupid to feel any mercy.");
/*      */     
/*  207 */     createCreatureTemplate(23, "Goblin", "Goblins", "This small, dirty creature looks at you greedily, and would go into a frenzy if you show pain.");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  212 */     createCreatureTemplate(25, "Huge spider", "Huge spiders", "Monstrously huge and fast, these spiders love to be played with.");
/*  213 */     createCreatureTemplate(56, "Lava spider", "Lava spiders", "Lava spiders usually lurk in their lava pools, catching curious prey.");
/*      */     
/*  215 */     createCreatureTemplate(26, "Goblin leader", "Goblin leaders", "Always on the brink of cackling wildly, this creature is possibly insane.");
/*      */     
/*  217 */     createCreatureTemplate(27, "Troll king", "Troll kings", "This troll has a scary clever look in his eyes. He surely knows what he is doing.");
/*      */     
/*  219 */     createCreatureTemplate(28, "Spirit guard", "Spirit guards", "This fierce spirit vaguely resembles a human warrior, and for some reason guards here.");
/*      */     
/*  221 */     createCreatureTemplate(29, "Spirit sentry", "Spirit sentries", "This spirit vaguely resembles a human being, and for some reason guards here.");
/*      */     
/*  223 */     createCreatureTemplate(30, "Spirit avenger", "Spirit avengers", "This restless spirit vaguely resembles a human being, that for some reason has chosen to guard this place.");
/*      */     
/*  225 */     createCreatureTemplate(31, "Spirit brute", "Spirit brutes", "This fierce spirit seems restless and upset but for some reason has chosen to guard this place.");
/*      */     
/*  227 */     createCreatureTemplate(32, "Spirit templar", "Spirit templars", "The spirit of a proud knight has decided to protect this place.");
/*      */     
/*  229 */     createCreatureTemplate(33, "Spirit shadow", "Spirit shadows", "A dark humanoid shadow looms about, its intentions unclear.");
/*      */     
/*  231 */     createCreatureTemplate(34, "Jenn-Kellon tower guard", "Jenn-Kellon tower guards", "This person seems to be able to put up some resistance. These guards will help defend you if you say help.");
/*      */     
/*  233 */     createCreatureTemplate(35, "Horde of the Summoned tower guard", "Horde of the Summoned tower guards", "This person seems to be able to put up some resistance. These guards will help defend you if you say help.");
/*      */     
/*  235 */     createCreatureTemplate(36, "Mol-Rehan tower guard", "Mol-Rehan tower guards", "This person seems to be able to put up some resistance. These guards will help defend you if you say help.");
/*      */     
/*  237 */     createCreatureTemplate(67, "Isles tower guard", "Isles tower guards", "This person seems to be able to put up some resistance. These guards will help defend you if you say help.");
/*      */     
/*  239 */     createCreatureTemplate(41, "Bartender", "Bartenders", "A fat and jolly bartender, eager to help people settling in.");
/*  240 */     createCreatureTemplate(46, "Santa Claus", "Santa Clauses", "Santa Claus is standing here, with a jolly face behind his huge white beard.");
/*      */     
/*  242 */     createCreatureTemplate(47, "Evil Santa", "Evil Santas", "Some sort of Santa Claus is standing here, with a fat belly, yellow eyes, and a bad breath.");
/*      */ 
/*      */     
/*  245 */     createCreatureTemplate(37, "Wild boar", "Wild boars", "A large and strong boar is grunting here.");
/*  246 */     createCreatureTemplate(39, "Mountain gorilla", "Mountain gorillas", "This normally calm mountain gorilla may suddenly become a very fierce and dangerous foe if annoyed.");
/*      */     
/*  248 */     createCreatureTemplate(38, "Anaconda", "Anacondas", "An over 3 meters long muscle, this grey-green snake is formidable.");
/*  249 */     createCreatureTemplate(40, "Rabid hyena", "Rabid hyenas", "Normally this doglike creature would act very cowardly, but some sickness seems to have driven it mad and overly aggressive.");
/*      */ 
/*      */     
/*  252 */     createCreatureTemplate(44, "Pig", "Pigs", "A pig is here, wallowing in the mud.");
/*      */     
/*  254 */     createCreatureTemplate(45, "Hen", "Hens", "A fine hen proudly prods around here.");
/*  255 */     createCreatureTemplate(52, "Rooster", "Roosters", "A proud rooster struts around here.");
/*  256 */     createCreatureTemplate(48, "Chicken", "Chickens", "A cute chicken struts around here.");
/*  257 */     createCreatureTemplate(51, "Dog", "Dogs", "Occasionally this dog will bark and scratch itself behind the ears.");
/*  258 */     createCreatureTemplate(50, "Calf", "Calves", "This calf looks happy and free.");
/*      */     
/*  260 */     createCreatureTemplate(49, "Bull", "Bulls", "This bull looks pretty menacing.");
/*  261 */     createCreatureTemplate(82, "Bison", "Bison", "The bison are impressive creatures when moving in hordes.");
/*  262 */     createCreatureTemplate(64, "Horse", "Horses", "Horses like this one have many uses.");
/*  263 */     createCreatureTemplate(65, "Foal", "Foals", "A foal skips around here merrily.");
/*  264 */     createCreatureTemplate(53, "Easter bunny", "Easter bunnies", "Wow, the mystical easter bunny skips around here joyfully!");
/*  265 */     createCreatureTemplate(54, "Deer", "Deer", "A fallow deer is here, watching for enemies.");
/*  266 */     createCreatureTemplate(55, "Pheasant", "Pheasants", "The pheasant slowly paces here, vigilant as always.");
/*  267 */     createCreatureTemplate(57, "Lava fiend", "Lava fiends", "These lava creatures enter the surface through lava pools, probably in order to hunt. Or burn.");
/*      */     
/*  269 */     createCreatureTemplate(58, "Crocodile", "Crocodiles", "This meat-eating reptile swims very well but may also perform quick rushes on land in order to catch you.");
/*      */     
/*  271 */     createCreatureTemplate(59, "Scorpion", "Scorpions", "The monstruously large type of scorpion found in woods and caves here is fairly aggressive.");
/*      */     
/*  273 */     createCreatureTemplate(60, "Tormentor", "Tormentors", "A particularly grim person stands here, trying to sort things out.");
/*      */     
/*  275 */     createCreatureTemplate(61, "Guide", "Guides", "A rather stressed out person is here giving instructions on how to survive to everyone who just arrived.");
/*      */     
/*  277 */     createCreatureTemplate(62, "Lady of the lake", "Ladies of the lake", "The hazy shape of a female spirit lingers below the waves.");
/*  278 */     createCreatureTemplate(63, "Cobra King", "Cobra Kings", "A huge menacing king cobra is guarding here, head swaying back and forth.");
/*      */     
/*  280 */     createCreatureTemplate(66, "Child", "Children", "A small child is here, exploring the world.");
/*  281 */     createCreatureTemplate(68, "Avenger of the Light", "Avengers of the Light", "Some kind of giant lumbers here, hunting humans.");
/*  282 */     createCreatureTemplate(69, "Zombie", "Zombies", "A very bleak humanlike creature stands here, looking abscent-minded.");
/*      */     
/*  284 */     createCreatureTemplate(70, "Sea Serpent", "Sea Serpents", "Sea Serpents are said to sleep in the dark caves of the abyss for years, then head to the surface to hunt once they get hungry.");
/*      */     
/*  286 */     createCreatureTemplate(71, "Huge shark", "Huge sharks", "These huge sharks were apparently not just a rumour. How horrendous!");
/*      */     
/*  288 */     createCreatureTemplate(72, "Sol Demon", "Sol Demons", "This demon has been released from Sol.");
/*  289 */     createCreatureTemplate(73, "Deathcrawler minion", "Deathcrawler minions", "The Deathcrawler minions usually spawn in large numbers. They have deadly poisonous bites.");
/*      */     
/*  291 */     createCreatureTemplate(74, "Spawn of Uttacha", "Spawns of Uttacha", "Uttacha is a vengeful demigod who lives in the depths of an ocean on Valrei. These huge larvae are hungry and confused abominations here.");
/*      */     
/*  293 */     createCreatureTemplate(75, "Son of Nogump", "Sons of Nogump", "Nogump the dirty has given birth to this foul two-headed giant wielding a huge twohanded sword.");
/*      */     
/*  295 */     createCreatureTemplate(76, "Drakespirit", "Drakespirits", "Drakespirits are usually found in their gardens on Valrei. They are hungry and aggressive.");
/*      */     
/*  297 */     createCreatureTemplate(77, "Eaglespirit", "Eaglespirits", "The Eaglespirits live on a glacier on Valrei. They will attack if hungry or threatened.");
/*      */     
/*  299 */     createCreatureTemplate(78, "Epiphany of Vynora", "Epiphanies of Vynora", "This female creature is almost see-through, and you wonder if she is made of water or thoughts alone.");
/*      */     
/*  301 */     createCreatureTemplate(79, "Juggernaut of Magranon", "Juggernauts of Magranon", "A ferocious beast indeed, the juggernaut can crush mountains with its horned forehead.");
/*      */     
/*  303 */     createCreatureTemplate(80, "Manifestation of Fo", "Manifestations of Fo", "Something seems to have gone wrong as Fo tried to create his manifestation. The thorns are not loving at all and it seems very aggressive.");
/*      */     
/*  305 */     createCreatureTemplate(81, "Incarnation of Libila", "Incarnations of Libila", "This terrifying female apparition has something disturbing over it. As if it's just one facet of Libila.");
/*      */     
/*  307 */     createCreatureTemplate(83, "Hell Horse", "Hell Horses", "This fiery creature is rumoured to be the mounts of the demons of Sol.");
/*      */     
/*  309 */     createCreatureTemplate(117, "Hell Foal", "Hell Foals", "This fiery creature is rumoured to grow up to be a mount of the demons of Sol.");
/*      */     
/*  311 */     createCreatureTemplate(84, "Hell Hound", "Hell Hounds", "The hell hound is said to be spies and assassins for the demons of Sol.");
/*      */     
/*  313 */     createCreatureTemplate(85, "Hell Scorpious", "Hell Scorpii", "The pets of the demons of Sol are very playful.");
/*      */     
/*  315 */     createCreatureTemplate(86, "Worg", "Worgs", "This wolf-like creature is unnaturally big and clumsy. The Worg seems finicky and nervous, which makes it unpredictable and dangerous to deal with.");
/*      */     
/*  317 */     createCreatureTemplate(87, "Skeleton", "Skeletons", "This abomination has been animated by powerful magic.");
/*      */     
/*  319 */     createCreatureTemplate(88, "Wraith", "Wraiths", "The wraith is born of darkness and shuns the daylight.");
/*      */ 
/*      */     
/*  322 */     createCreatureTemplate(93, "Seal", "Seals", "These creatures love to bathe in the sun and go for a swim hunting fish.");
/*      */ 
/*      */     
/*  325 */     createCreatureTemplate(94, "Tortoise", "Tortoises", "The tortoise is pretty harmless but can pinch you quite bad with its bite.");
/*      */ 
/*      */     
/*  328 */     createCreatureTemplate(95, "Crab", "Crabs", "Crabs are known to hide well and walk sideways.");
/*      */ 
/*      */     
/*  331 */     createCreatureTemplate(96, "Sheep", "Sheep", "A mythical beast of legends, it stares back at you with blood filled eyes and froth around the mouth.");
/*      */     
/*  333 */     createCreatureTemplate(97, "Blue whale", "Blue whales", "These gigantic creatures travel huge distances looking for food, while singing their mysterious songs.");
/*      */     
/*  335 */     createCreatureTemplate(98, "Seal cub", "Seal cubs", "A young seal, waiting to be fed luscious fish.");
/*      */     
/*  337 */     createCreatureTemplate(99, "Dolphin", "Dolphins", "A playful dolphin. They have been known to defend sailors in distress from their natural enemy, the shark.");
/*      */     
/*  339 */     createCreatureTemplate(100, "Octopus", "Octopi", "Larger specimen have been known to pull whole ships down into the abyss. Luckily this one is small.");
/*      */     
/*  341 */     createCreatureTemplate(101, "Lamb", "Lambs", "A small cuddly ball of fluff.");
/*      */     
/*  343 */     createCreatureTemplate(102, "Ram", "Rams", "A mythical beast of legends, it stares back at you with blood filled eyes and froth around the mouth.");
/*      */ 
/*      */     
/*  346 */     createCreatureTemplate(89, "Black dragon", "Black dragons", "The menacing huge dragon, with scales as dark as the night.");
/*      */     
/*  348 */     createCreatureTemplate(91, "Blue dragon", "Blue dragons", "The menacing huge dragon, with dark blue scales.");
/*      */     
/*  350 */     createCreatureTemplate(90, "Green dragon", "Green dragons", "The menacing huge dragon, with emerald green scales.");
/*      */     
/*  352 */     createCreatureTemplate(92, "White dragon", "White dragons", "The menacing huge dragon, with snow white scales.");
/*      */     
/*  354 */     createCreatureTemplate(104, "Blue dragon hatchling", "Blue dragon hatchlings", "The blue dragon hatchling is not as large as a full-grown dragon and unable to fly.");
/*      */     
/*  356 */     createCreatureTemplate(103, "Red dragon hatchling", "Red dragon hatchlings", "The red dragon hatchling is not as large as a full-grown dragon and unable to fly.");
/*      */     
/*  358 */     createCreatureTemplate(105, "Fog Spider", "Fog Spiders", "Usually only encountered under foggy conditions, this creature is often considered an Omen.");
/*      */     
/*  360 */     createCreatureTemplate(106, "Rift Beast", "Rift Beasts", "These vile creatures emerge from the rift in great numbers.");
/*      */     
/*  362 */     createCreatureTemplate(107, "Rift Jackal", "Rift Jackals", "The Jackals accompany the Beasts as they spew out of the rift.");
/*      */     
/*  364 */     createCreatureTemplate(108, "Rift Ogre", "Rift Ogres", "The Rift Ogres seem to bully Beasts and Jackals into following orders.");
/*      */     
/*  366 */     createCreatureTemplate(109, "Rift Warmaster", "Rift Warmasters", "These plan and lead attacks from the rift.");
/*      */     
/*  368 */     createCreatureTemplate(111, "Rift Ogre Mage", "Rift Ogre Magi", "Ogre Mages have mysterious powers.");
/*      */     
/*  370 */     createCreatureTemplate(110, "Rift Caster", "Rift Casters", "Proficient spell casters, but they seem to avoid direct contact.");
/*      */     
/*  372 */     createCreatureTemplate(112, "Rift Summoner", "Rift Summoners", "Summoners seem to be able to call for aid from the Rift.");
/*      */     
/*  374 */     createCreatureTemplate(113, "NPC Human", "NPC Humans", "A relatively normal person stands here waiting for something to happen.");
/*      */     
/*  376 */     if (Features.Feature.WAGONER.isEnabled()) {
/*      */       
/*  378 */       createCreatureTemplate(114, "NPC Wagoner", "NPC Wagoners", "A relatively normal person stands here waiting to help transport bulk goods.");
/*      */       
/*  380 */       createCreatureTemplate(115, "Wagon Creature", "Wagon Creatures", "The wagon creature is only used for hauling a wagoner's wagon.");
/*      */     } 
/*      */     
/*  383 */     if (Servers.localServer.testServer) {
/*  384 */       createCreatureTemplate(116, "Weapon Test Dummy", "Weapon Test Dummies", "An immortal that shouts out any damage that it receives, then immediately heals.");
/*      */     }
/*  386 */     createCreatureTemplate(119, "Fish", "fishs", "a fish of some type or other.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  394 */     long end = System.currentTimeMillis();
/*  395 */     logger.info("Creating Creature Templates took " + (end - start) + " ms");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCreatureTemplate(int id, String name, String plural, String longDesc) {
/*  407 */     Skills skills = SkillsFactory.createSkills(name);
/*      */ 
/*      */     
/*      */     try {
/*  411 */       skills.learnTemp(102, 20.0F);
/*  412 */       skills.learnTemp(104, 20.0F);
/*  413 */       skills.learnTemp(103, 20.0F);
/*      */       
/*  415 */       skills.learnTemp(100, 20.0F);
/*  416 */       skills.learnTemp(101, 20.0F);
/*  417 */       skills.learnTemp(105, 20.0F);
/*  418 */       skills.learnTemp(106, 20.0F);
/*      */       
/*  420 */       if (id == 1)
/*      */       {
/*  422 */         createHumanTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  424 */       else if (id == 66)
/*      */       {
/*  426 */         createKidTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  428 */       else if (id == 3)
/*      */       {
/*  430 */         createBrownCowTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  432 */       else if (id == 50)
/*      */       {
/*  434 */         createCalfTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  436 */       else if (id == 49)
/*      */       {
/*  438 */         createBullTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  440 */       else if (id == 82)
/*      */       {
/*  442 */         createBisonTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  444 */       else if (id == 64)
/*      */       {
/*  446 */         createHorseTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  448 */       else if (id == 65)
/*      */       {
/*  450 */         createFoalTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  452 */       else if (id == 54)
/*      */       {
/*  454 */         createDeerTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  456 */       else if (id == 52)
/*      */       {
/*  458 */         createRoosterTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  460 */       else if (id == 55)
/*      */       {
/*  462 */         createPheasantTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  464 */       else if (id == 45)
/*      */       {
/*  466 */         createHenTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  468 */       else if (id == 48)
/*      */       {
/*  470 */         createChickenTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  472 */       else if (id == 9)
/*      */       {
/*  474 */         createSalesmanTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  476 */       else if (id == 41)
/*      */       {
/*  478 */         createBartenderTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  480 */       else if (id == 46)
/*      */       {
/*  482 */         createSantaClausTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  484 */       else if (id == 47)
/*      */       {
/*  486 */         createEvilSantaTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  488 */       else if (id == 61)
/*      */       {
/*  490 */         createGuideTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  492 */       else if (id == 60)
/*      */       {
/*  494 */         createGuideHotsTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  496 */       else if (id == 4)
/*      */       {
/*  498 */         createGuardLenientTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  500 */       else if (id == 5)
/*      */       {
/*  502 */         createGuardDecentTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  504 */       else if (id == 6)
/*      */       {
/*  506 */         createGuardAbleTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  508 */       else if (id == 7)
/*      */       {
/*  510 */         createGuardToughTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  512 */       else if (id == 8)
/*      */       {
/*  514 */         createGuardBrutalTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  516 */       else if (id == 32)
/*      */       {
/*  518 */         createGuardSpiritGoodDangerousTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  520 */       else if (id == 33)
/*      */       {
/*  522 */         createGuardSpiritEvilDangerousTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  524 */       else if (id == 30 || id == 31)
/*      */       {
/*  526 */         createGuardSpiritAbleTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  528 */       else if (id == 29)
/*      */       {
/*  530 */         createGuardSpiritEvilLenientTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  532 */       else if (id == 28)
/*      */       {
/*  534 */         createGuardSpiritGoodLenientTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  536 */       else if (id == 10)
/*      */       {
/*  538 */         createBlackWolfTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  540 */       else if (id == 51)
/*      */       {
/*  542 */         createDogTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  544 */       else if (id == 58)
/*      */       {
/*  546 */         createCrocodileTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  548 */       else if (id == 53)
/*      */       {
/*  550 */         createEasterBunnyTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  552 */       else if (id == 12)
/*      */       {
/*  554 */         createBearBrownTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  556 */       else if (id == 42)
/*      */       {
/*  558 */         createBearBlackTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  560 */       else if (id == 21)
/*      */       {
/*  562 */         createUnicornTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  564 */       else if (id == 118)
/*      */       {
/*  566 */         createUnicornFoalTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  568 */       else if (id == 59)
/*      */       {
/*  570 */         createScorpionTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  572 */       else if (id == 23)
/*      */       {
/*  574 */         createGoblinTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  576 */       else if (id == 26)
/*      */       {
/*  578 */         createGoblinLeaderTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  580 */       else if (id == 11)
/*      */       {
/*  582 */         createTrollTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  584 */       else if (id == 27)
/*      */       {
/*  586 */         createTrollKingTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  588 */       else if (id == 22)
/*      */       {
/*  590 */         createCyclopsTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  592 */       else if (id == 20)
/*      */       {
/*  594 */         createForestGiantTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  596 */       else if (id == 18)
/*      */       {
/*  598 */         createDrakeBlackTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  600 */       else if (id == 17)
/*      */       {
/*  602 */         createDrakeGreenTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  604 */       else if (id == 19)
/*      */       {
/*  606 */         createDrakeWhiteTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  608 */       else if (id == 103)
/*      */       {
/*  610 */         createDrakeRedTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  612 */       else if (id == 104)
/*      */       {
/*  614 */         createDrakeBlueTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  616 */       else if (id == 16)
/*      */       {
/*  618 */         createDragonRedTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  620 */       else if (id == 89)
/*      */       {
/*  622 */         createDragonBlackTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  624 */       else if (id == 91)
/*      */       {
/*  626 */         createDragonBlueTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  628 */       else if (id == 90)
/*      */       {
/*  630 */         createDragonGreenTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  632 */       else if (id == 92)
/*      */       {
/*  634 */         createDragonWhiteTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  636 */       else if (id == 13)
/*      */       {
/*  638 */         createRatLargeTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  640 */       else if (id == 14)
/*      */       {
/*  642 */         createLionMountainTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  644 */       else if (id == 43)
/*      */       {
/*  646 */         createCaveBugTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  648 */       else if (id == 15)
/*      */       {
/*  650 */         createCatWildTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  652 */       else if (id == 2)
/*      */       {
/*  654 */         createDummyDollTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  656 */       else if (id == 34 || id == 35 || id == 36 || id == 67)
/*      */       {
/*      */         
/*  659 */         createGuardKingdomTowerTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  661 */       else if (id == 39)
/*      */       {
/*  663 */         createGorillaMagranonTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  665 */       else if (id == 37)
/*      */       {
/*  667 */         createBoarFoTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  669 */       else if (id == 68)
/*      */       {
/*  671 */         createAvengerOfLightTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  673 */       else if (id == 70)
/*      */       {
/*  675 */         createSeaSerpentTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  677 */       else if (id == 71)
/*      */       {
/*  679 */         createSharkHugeTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  681 */       else if (id == 40)
/*      */       {
/*  683 */         createHyenaLabilaTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  685 */       else if (id == 25)
/*      */       {
/*  687 */         createSpiderTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  689 */       else if (id == 56)
/*      */       {
/*  691 */         createLavaSpiderTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  693 */       else if (id == 57)
/*      */       {
/*  695 */         createLavaCreatureTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  697 */       else if (id == 44)
/*      */       {
/*  699 */         createPigTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  701 */       else if (id == 38)
/*      */       {
/*  703 */         createAnadondaTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  705 */       else if (id == 63)
/*      */       {
/*  707 */         createKingCobraTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  709 */       else if (id == 62)
/*      */       {
/*  711 */         createLadyLakeTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  713 */       else if (id == 69)
/*      */       {
/*  715 */         createZombieTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  717 */       else if (id == 72)
/*      */       {
/*  719 */         createDemonSolTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  721 */       else if (id == 73)
/*      */       {
/*  723 */         createDeathCrawlerMinionTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  725 */       else if (id == 74)
/*      */       {
/*  727 */         createSpawnUttachaTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  729 */       else if (id == 75)
/*      */       {
/*  731 */         createSonOfNogumpTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  733 */       else if (id == 76)
/*      */       {
/*  735 */         createDrakeSpiritTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  737 */       else if (id == 77)
/*      */       {
/*  739 */         createEagleSpiritTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  741 */       else if (id == 78)
/*      */       {
/*  743 */         createEpiphanyVynoraTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  745 */       else if (id == 79)
/*      */       {
/*  747 */         createMagranonJuggernautTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  749 */       else if (id == 80)
/*      */       {
/*  751 */         createManifestationFoTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  753 */       else if (id == 81)
/*      */       {
/*  755 */         createIncarnationLibilaTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  757 */       else if (id == 83)
/*      */       {
/*  759 */         createHellHorseTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  761 */       else if (id == 117)
/*      */       {
/*  763 */         createHellFoalTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  765 */       else if (id == 84)
/*      */       {
/*  767 */         createHellHoundTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  769 */       else if (id == 85)
/*      */       {
/*  771 */         createHellScorpionTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  773 */       else if (id == 86)
/*      */       {
/*  775 */         createWorgTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  777 */       else if (id == 87)
/*      */       {
/*  779 */         createSkeletonTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  781 */       else if (id == 88)
/*      */       {
/*  783 */         createWraithTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  785 */       else if (id == 93)
/*      */       {
/*  787 */         createSealTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  789 */       else if (id == 94)
/*      */       {
/*  791 */         createTortoiseTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  793 */       else if (id == 95)
/*      */       {
/*  795 */         createCrabTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  797 */       else if (id == 101)
/*      */       {
/*  799 */         createLambTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  801 */       else if (id == 96)
/*      */       {
/*  803 */         createSheepTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  805 */       else if (id == 102)
/*      */       {
/*  807 */         createRamTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  809 */       else if (id == 97)
/*      */       {
/*  811 */         createBlueWhaleTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  813 */       else if (id == 98)
/*      */       {
/*  815 */         createSealCubTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  817 */       else if (id == 99)
/*      */       {
/*  819 */         createDolphinTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  821 */       else if (id == 100)
/*      */       {
/*  823 */         createOctopusTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  825 */       else if (id == 105)
/*      */       {
/*  827 */         createFogSpiderTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  829 */       else if (id == 106)
/*      */       {
/*  831 */         createRiftTemplateOne(id, name, plural, longDesc, skills);
/*      */       }
/*  833 */       else if (id == 107)
/*      */       {
/*  835 */         createRiftTemplateTwo(id, name, plural, longDesc, skills);
/*      */       }
/*  837 */       else if (id == 108)
/*      */       {
/*  839 */         createRiftTemplateThree(id, name, plural, longDesc, skills);
/*      */       }
/*  841 */       else if (id == 109)
/*      */       {
/*  843 */         createRiftTemplateFour(id, name, plural, longDesc, skills);
/*      */       }
/*  845 */       else if (id == 110)
/*      */       {
/*  847 */         createRiftCasterTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  849 */       else if (id == 111)
/*      */       {
/*  851 */         createRiftOgreMageTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  853 */       else if (id == 112)
/*      */       {
/*  855 */         createRiftSummonerTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  857 */       else if (id == 113)
/*      */       {
/*  859 */         createNpcHumanTemplate(id, name, plural, longDesc, skills);
/*      */       }
/*  861 */       else if (id == 119)
/*      */       {
/*  863 */         createFishTemplate(id, name, plural, longDesc, skills);
/*      */ 
/*      */       
/*      */       }
/*  867 */       else if (logger.isLoggable(Level.FINE))
/*      */       {
/*  869 */         logger.fine("Using standard creature skills and characteristics for template id: " + id);
/*      */       }
/*      */     
/*      */     }
/*  873 */     catch (Exception ex) {
/*      */       
/*  875 */       ex.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createHellScorpionTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/*  889 */     skills.learnTemp(102, 30.0F);
/*  890 */     skills.learnTemp(104, 15.0F);
/*  891 */     skills.learnTemp(103, 40.0F);
/*  892 */     skills.learnTemp(100, 2.0F);
/*  893 */     skills.learnTemp(101, 5.0F);
/*  894 */     skills.learnTemp(105, 70.0F);
/*  895 */     skills.learnTemp(106, 4.0F);
/*  896 */     skills.learnTemp(10052, 44.0F);
/*  897 */     int[] types = { 7, 6, 13, 16, 29, 32, 34, 39, 46, 55, 60, 61 };
/*      */ 
/*      */     
/*  900 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.multiped.scorpion.hell", types, (byte)8, skills, (short)5, (byte)0, (short)130, (short)30, (short)20, "sound.death.insect", "sound.death.insect", "sound.combat.hit.insect", "sound.combat.hit.insect", 0.3F, 8.0F, 14.0F, 13.0F, 0.0F, 0.0F, 0.75F, 1700, new int[] { 439 }, 7, 64, (byte)82);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  905 */     temp.setHandDamString("claw");
/*  906 */     temp.setBreathDamString("sting");
/*  907 */     temp.setAlignment(-40.0F);
/*  908 */     temp.setMaxAge(100);
/*  909 */     temp.setBaseCombatRating(18.0F);
/*  910 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/*  911 */     temp.combatDamageType = 2;
/*  912 */     temp.setMaxGroupAttackSize(5);
/*  913 */     temp.setColorBlue(255);
/*  914 */     temp.setColorGreen(255);
/*  915 */     temp.setColorRed(255);
/*  916 */     temp.setOnFire(false);
/*  917 */     temp.setGlowing(false);
/*  918 */     temp.setMaxPercentOfCreatures(0.02F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createHellHoundTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/*  932 */     skills.learnTemp(102, 20.0F);
/*  933 */     skills.learnTemp(104, 45.0F);
/*  934 */     skills.learnTemp(103, 25.0F);
/*      */     
/*  936 */     skills.learnTemp(100, 10.0F);
/*  937 */     skills.learnTemp(101, 15.0F);
/*  938 */     skills.learnTemp(105, 60.0F);
/*  939 */     skills.learnTemp(106, 12.0F);
/*  940 */     skills.learnTemp(10052, 40.0F);
/*  941 */     int[] types = { 7, 6, 13, 3, 29, 36, 39, 32, 46, 55, 60, 61 };
/*      */ 
/*      */     
/*  944 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.dog.hell", types, (byte)3, skills, (short)7, (byte)0, (short)40, (short)20, (short)100, "sound.death.dog", "sound.death.dog", "sound.combat.hit.dog", "sound.combat.hit.dog", 0.6F, 10.0F, 0.0F, 12.0F, 0.0F, 0.0F, 1.2F, 300, new int[] { 204 }, 10, 94, (byte)74);
/*      */ 
/*      */ 
/*      */     
/*  948 */     temp.setHandDamString("claw");
/*  949 */     temp.setAlignment(-60.0F);
/*  950 */     temp.setMaxAge(35);
/*  951 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER);
/*  952 */     temp.setBaseCombatRating(14.0F);
/*  953 */     temp.combatDamageType = 1;
/*  954 */     temp.setMaxGroupAttackSize(4);
/*  955 */     temp.setColorBlue(255);
/*  956 */     temp.setColorGreen(255);
/*  957 */     temp.setColorRed(255);
/*  958 */     temp.setOnFire(false);
/*  959 */     temp.setGlowing(true);
/*  960 */     temp.setMaxPercentOfCreatures(0.03F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createWorgTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/*  973 */     skills.learnTemp(102, 25.0F);
/*  974 */     skills.learnTemp(104, 25.0F);
/*  975 */     skills.learnTemp(103, 35.0F);
/*      */     
/*  977 */     skills.learnTemp(100, 10.0F);
/*  978 */     skills.learnTemp(101, 15.0F);
/*  979 */     skills.learnTemp(105, 20.0F);
/*  980 */     skills.learnTemp(106, 12.0F);
/*  981 */     skills.learnTemp(10052, 50.0F);
/*  982 */     int[] types = { 7, 6, 13, 3, 29, 36, 39 };
/*      */ 
/*      */     
/*  985 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.wolf.worg", types, (byte)3, skills, (short)7, (byte)0, (short)40, (short)20, (short)100, "sound.death.dog", "sound.death.dog", "sound.combat.hit.dog", "sound.combat.hit.dog", 0.2F, 10.0F, 0.0F, 12.0F, 0.0F, 0.0F, 1.2F, 300, new int[0], 10, 94, (byte)87);
/*      */ 
/*      */ 
/*      */     
/*  989 */     temp.setHandDamString("claw");
/*  990 */     temp.setAlignment(-60.0F);
/*  991 */     temp.setMaxAge(4);
/*  992 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/*  993 */     temp.setBaseCombatRating(14.0F);
/*  994 */     temp.combatDamageType = 2;
/*  995 */     temp.setMaxGroupAttackSize(4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createHellHorseTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1008 */     skills.learnTemp(102, 35.0F);
/* 1009 */     skills.learnTemp(104, 20.0F);
/* 1010 */     skills.learnTemp(103, 40.0F);
/*      */     
/* 1012 */     skills.learnTemp(100, 7.0F);
/* 1013 */     skills.learnTemp(101, 7.0F);
/* 1014 */     skills.learnTemp(105, 72.0F);
/* 1015 */     skills.learnTemp(106, 5.0F);
/* 1016 */     skills.learnTemp(10052, 38.0F);
/* 1017 */     int[] types = { 7, 41, 3, 14, 9, 27, 32, 6, 39, 55 };
/*      */ 
/*      */     
/* 1020 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.horse.hell", types, (byte)1, skills, (short)3, (byte)0, (short)180, (short)50, (short)250, "sound.death.horse", "sound.death.horse", "sound.combat.hit.horse", "sound.combat.hit.horse", 1.0F, 5.0F, 7.0F, 10.0F, 0.0F, 0.0F, 1.8F, 100, new int[] { 307, 306, 140, 71, 309, 308, 308 }, 5, 0, (byte)79);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1027 */     temp.setMaxAge(200);
/* 1028 */     temp.setBaseCombatRating(9.0F);
/* 1029 */     temp.combatDamageType = 4;
/* 1030 */     temp.setCombatMoves(new int[] { 10 });
/*      */     
/* 1032 */     temp.setAlignment(-40.0F);
/* 1033 */     temp.setHandDamString("kick");
/* 1034 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 1035 */     temp.setMaxGroupAttackSize(4);
/* 1036 */     temp.setColorBlue(255);
/* 1037 */     temp.setColorGreen(255);
/* 1038 */     temp.setColorRed(255);
/* 1039 */     temp.setOnFire(false);
/* 1040 */     temp.setGlowing(true);
/* 1041 */     temp.setMaxPercentOfCreatures(0.03F);
/* 1042 */     temp.setChildTemplateId(117);
/* 1043 */     temp.isHorse = true;
/*      */     
/* 1045 */     temp.setColourNames(new String[] { "ash", "cinder", "envious", "shadow", "pestilential", "nightshade", "incandescent", "molten" });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createHellFoalTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1058 */     skills.learnTemp(102, 15.0F);
/* 1059 */     skills.learnTemp(104, 10.0F);
/* 1060 */     skills.learnTemp(103, 15.0F);
/*      */     
/* 1062 */     skills.learnTemp(100, 3.0F);
/* 1063 */     skills.learnTemp(101, 3.0F);
/* 1064 */     skills.learnTemp(105, 35.0F);
/* 1065 */     skills.learnTemp(106, 2.0F);
/* 1066 */     skills.learnTemp(10052, 15.0F);
/* 1067 */     int[] types = { 7, 3, 14, 9, 27, 32, 6, 39, 55, 63 };
/*      */ 
/*      */     
/* 1070 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.horse.hell.foal", types, (byte)1, skills, (short)3, (byte)0, (short)100, (short)50, (short)75, "sound.death.horse", "sound.death.horse", "sound.combat.hit.horse", "sound.combat.hit.horse", 1.0F, 3.0F, 4.0F, 4.0F, 0.0F, 0.0F, 1.2F, 100, new int[] { 307, 306, 140, 71, 309, 308, 308 }, 5, 0, (byte)79);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1076 */     temp.setMaxAge(100);
/* 1077 */     temp.setBaseCombatRating(5.0F);
/* 1078 */     temp.combatDamageType = 4;
/* 1079 */     temp.setAlignment(-40.0F);
/* 1080 */     temp.setHandDamString("kick");
/* 1081 */     temp.setAdultFemaleTemplateId(83);
/* 1082 */     temp.setAdultMaleTemplateId(83);
/* 1083 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 1084 */     temp.setMaxGroupAttackSize(2);
/* 1085 */     temp.setColorBlue(255);
/* 1086 */     temp.setColorGreen(255);
/* 1087 */     temp.setColorRed(255);
/* 1088 */     temp.setOnFire(false);
/* 1089 */     temp.setGlowing(true);
/* 1090 */     temp.setMaxPercentOfCreatures(0.015F);
/* 1091 */     temp.isHorse = true;
/* 1092 */     temp.setCorpseName("hellhorse.foal.");
/*      */     
/* 1094 */     temp.setColourNames(new String[] { "ash", "cinder", "envious", "shadow", "pestilential", "nightshade", "incandescent", "molten" });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createIncarnationLibilaTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1107 */     skills.learnTemp(102, 80.0F);
/* 1108 */     skills.learnTemp(104, 45.0F);
/* 1109 */     skills.learnTemp(103, 70.0F);
/*      */     
/* 1111 */     skills.learnTemp(100, 44.0F);
/* 1112 */     skills.learnTemp(101, 30.0F);
/* 1113 */     skills.learnTemp(105, 40.0F);
/* 1114 */     skills.learnTemp(106, 49.0F);
/* 1115 */     skills.learnTemp(10052, 75.0F);
/* 1116 */     int[] types = { 8, 13, 16, 27, 36, 12, 62, 24, 25, 40, 45, 47 };
/*      */ 
/*      */     
/* 1119 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.giant.incarnation", types, (byte)0, skills, (short)20, (byte)1, (short)570, (short)100, (short)60, "sound.death.libila.incarnation", "sound.death.libila.incarnation", "sound.combat.hit.libila.incarnation", "sound.combat.hit.libila.incarnation", 0.03F, 7.0F, 30.0F, 30.0F, 40.0F, 0.0F, 1.5F, 10, new int[] { 683, 683, 308, 308 }, 10, 5, (byte)87);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1125 */     temp.setAlignment(-100.0F);
/* 1126 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 1127 */     temp.setBaseCombatRating(86.0F);
/* 1128 */     temp.setMaxGroupAttackSize(30);
/* 1129 */     temp.combatDamageType = 1;
/* 1130 */     temp.setCombatMoves(new int[] { 7, 2, 1 });
/*      */     
/* 1132 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createManifestationFoTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1145 */     skills.learnTemp(102, 70.0F);
/* 1146 */     skills.learnTemp(104, 35.0F);
/* 1147 */     skills.learnTemp(103, 70.0F);
/*      */     
/* 1149 */     skills.learnTemp(100, 24.0F);
/* 1150 */     skills.learnTemp(101, 30.0F);
/* 1151 */     skills.learnTemp(105, 50.0F);
/* 1152 */     skills.learnTemp(106, 59.0F);
/* 1153 */     skills.learnTemp(10052, 75.0F);
/* 1154 */     int[] types = { 8, 13, 16, 27, 36, 12, 24, 40, 45, 47, 62 };
/*      */ 
/*      */     
/* 1157 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.giant.manifestation", types, (byte)0, skills, (short)20, (byte)0, (short)570, (short)100, (short)60, "sound.death.fo.manifestation", "sound.death.fo.manifestation", "sound.combat.hit.fo.manifestation", "sound.combat.hit.fo.manifestation", 0.03F, 10.0F, 30.0F, 30.0F, 40.0F, 0.0F, 1.5F, 10, new int[] { 683, 683, 308, 308 }, 10, 5, (byte)87);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1163 */     temp.setAlignment(100.0F);
/* 1164 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 1165 */     temp.setBaseCombatRating(86.0F);
/* 1166 */     temp.setMaxGroupAttackSize(30);
/* 1167 */     temp.combatDamageType = 0;
/* 1168 */     temp.setCombatMoves(new int[] { 8, 1 });
/*      */     
/* 1170 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createMagranonJuggernautTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1183 */     skills.learnTemp(102, 90.0F);
/* 1184 */     skills.learnTemp(104, 35.0F);
/* 1185 */     skills.learnTemp(103, 90.0F);
/*      */     
/* 1187 */     skills.learnTemp(100, 14.0F);
/* 1188 */     skills.learnTemp(101, 10.0F);
/* 1189 */     skills.learnTemp(105, 20.0F);
/* 1190 */     skills.learnTemp(106, 19.0F);
/* 1191 */     skills.learnTemp(10052, 75.0F);
/* 1192 */     int[] types = { 8, 13, 16, 27, 36, 12, 24, 40, 45, 47, 62 };
/*      */ 
/*      */     
/* 1195 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.giant.juggernaut", types, (byte)0, skills, (short)20, (byte)0, (short)570, (short)100, (short)60, "sound.death.magranon.juggernaut", "sound.death.magranon.juggernaut", "sound.combat.hit.magranon.juggernaut", "sound.combat.hit.magranon.juggernaut", 0.03F, 10.0F, 30.0F, 30.0F, 40.0F, 0.0F, 1.5F, 10, new int[] { 683, 683, 308, 308 }, 10, 5, (byte)87);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1201 */     temp.setAlignment(100.0F);
/* 1202 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 1203 */     temp.setBaseCombatRating(96.0F);
/* 1204 */     temp.setMaxGroupAttackSize(30);
/* 1205 */     temp.combatDamageType = 0;
/* 1206 */     temp.setCombatMoves(new int[] { 8, 5, 1 });
/*      */     
/* 1208 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createEpiphanyVynoraTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1221 */     skills.learnTemp(102, 50.0F);
/* 1222 */     skills.learnTemp(104, 65.0F);
/* 1223 */     skills.learnTemp(103, 80.0F);
/*      */     
/* 1225 */     skills.learnTemp(100, 24.0F);
/* 1226 */     skills.learnTemp(101, 30.0F);
/* 1227 */     skills.learnTemp(105, 80.0F);
/* 1228 */     skills.learnTemp(106, 39.0F);
/* 1229 */     skills.learnTemp(10052, 75.0F);
/* 1230 */     int[] types = { 8, 24, 13, 16, 27, 62, 36, 12, 40, 45, 47 };
/*      */ 
/*      */     
/* 1233 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.giant.epiphany", types, (byte)0, skills, (short)20, (byte)0, (short)570, (short)100, (short)60, "sound.death.vynora.epiphany", "sound.death.vynora.epiphany", "sound.combat.hit.vynora.epiphany", "sound.combat.hit.vynora.epiphany", 0.03F, 10.0F, 24.0F, 26.0F, 0.0F, 0.0F, 1.5F, 10, new int[] { 683, 683, 308, 308 }, 10, 5, (byte)87);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1239 */     temp.setAlignment(100.0F);
/* 1240 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 1241 */     temp.setBaseCombatRating(96.0F);
/* 1242 */     temp.setMaxGroupAttackSize(30);
/* 1243 */     temp.combatDamageType = 2;
/* 1244 */     temp.setCombatMoves(new int[] { 7, 5 });
/*      */     
/* 1246 */     temp.hasHands = true;
/* 1247 */     temp.setPaintMode(1);
/* 1248 */     temp.setGlowing(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createEagleSpiritTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1261 */     skills.learnTemp(102, 20.0F);
/* 1262 */     skills.learnTemp(104, 35.0F);
/* 1263 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 1265 */     skills.learnTemp(100, 24.0F);
/* 1266 */     skills.learnTemp(101, 20.0F);
/* 1267 */     skills.learnTemp(105, 50.0F);
/* 1268 */     skills.learnTemp(106, 29.0F);
/* 1269 */     skills.learnTemp(10052, 45.0F);
/* 1270 */     int[] types = { 41, 8, 13, 16, 29, 12, 24, 40, 22, 46, 50 };
/*      */ 
/*      */ 
/*      */     
/* 1274 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.eagle.spirit", types, (byte)6, skills, (short)30, (byte)1, (short)150, (short)90, (short)320, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.1F, 7.0F, 10.0F, 13.0F, 17.0F, 0.0F, 1.9F, 500, new int[] { 683, 303, 308, 308, 310 }, 20, 49, (byte)77);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1279 */     temp.setHeadbuttDamString("tailwhip");
/* 1280 */     temp.setKickDamString("wingbuff");
/* 1281 */     temp.setAlignment(0.0F);
/* 1282 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 1283 */     temp.setEggLayer(true);
/* 1284 */     temp.setEggTemplateId(77);
/* 1285 */     temp.setBaseCombatRating(15.0F);
/* 1286 */     temp.setMaxGroupAttackSize(6);
/* 1287 */     temp.combatDamageType = 1;
/* 1288 */     temp.setCombatMoves(new int[] { 1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDrakeSpiritTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1302 */     skills.learnTemp(102, 20.0F);
/* 1303 */     skills.learnTemp(104, 65.0F);
/* 1304 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 1306 */     skills.learnTemp(100, 34.0F);
/* 1307 */     skills.learnTemp(101, 40.0F);
/* 1308 */     skills.learnTemp(105, 80.0F);
/* 1309 */     skills.learnTemp(106, 39.0F);
/* 1310 */     skills.learnTemp(10052, 75.0F);
/* 1311 */     int[] types = { 41, 8, 13, 16, 29, 12, 40, 22, 24, 46, 50 };
/*      */ 
/*      */ 
/*      */     
/* 1315 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.drake.spirit", types, (byte)6, skills, (short)30, (byte)1, (short)150, (short)90, (short)320, "sound.death.drakespirit", "sound.death.drakespirit", "sound.combat.hit.drakespirit", "sound.combat.hit.drakespirit", 0.1F, 7.0F, 10.0F, 13.0F, 17.0F, 0.0F, 1.9F, 500, new int[] { 683, 303, 308, 308, 310 }, 20, 49, (byte)77);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1321 */     temp.setHeadbuttDamString("tailwhip");
/* 1322 */     temp.setKickDamString("wingbuff");
/* 1323 */     temp.setAlignment(0.0F);
/* 1324 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON);
/* 1325 */     temp.setEggLayer(true);
/* 1326 */     temp.setEggTemplateId(76);
/* 1327 */     temp.setBaseCombatRating(27.0F);
/* 1328 */     temp.setMaxGroupAttackSize(10);
/* 1329 */     temp.combatDamageType = 2;
/* 1330 */     temp.setHandDamString("claw");
/* 1331 */     temp.setKickDamString("claw");
/* 1332 */     temp.setCombatMoves(new int[] { 1, 7 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSonOfNogumpTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1346 */     skills.learnTemp(102, 80.0F);
/* 1347 */     skills.learnTemp(104, 35.0F);
/* 1348 */     skills.learnTemp(103, 80.0F);
/*      */     
/* 1350 */     skills.learnTemp(100, 7.0F);
/* 1351 */     skills.learnTemp(101, 17.0F);
/* 1352 */     skills.learnTemp(105, 30.0F);
/* 1353 */     skills.learnTemp(106, 29.0F);
/* 1354 */     skills.learnTemp(10052, 80.0F);
/* 1355 */     skills.learnTemp(10053, 80.0F);
/* 1356 */     skills.learnTemp(10054, 50.0F);
/* 1357 */     skills.learnTemp(10055, 70.0F);
/*      */     
/* 1359 */     skills.learnTemp(1000, 90.0F);
/* 1360 */     skills.learnTemp(10028, 90.0F);
/*      */     
/* 1362 */     int[] types = { 30, 8, 13, 16, 27, 24, 40, 45, 46, 50 };
/*      */ 
/*      */     
/* 1365 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.nogump.son", types, (byte)4, skills, (short)20, (byte)0, (short)570, (short)200, (short)80, "sound.death.nogump.son", "sound.death.nogump.son", "sound.combat.hit.nogump.son", "sound.combat.hit.nogump.son", 0.3F, 26.0F, 30.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1500, new int[] { 683 }, 20, 49, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1370 */     temp.setAlignment(10.0F);
/* 1371 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 1372 */     temp.keepSex = true;
/* 1373 */     temp.setBaseCombatRating(6.0F);
/* 1374 */     temp.setMaxGroupAttackSize(10);
/* 1375 */     temp.setCombatMoves(new int[] { 8, 1 });
/*      */     
/* 1377 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSpawnUttachaTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1390 */     skills.learnTemp(102, 15.0F);
/* 1391 */     skills.learnTemp(104, 15.0F);
/* 1392 */     skills.learnTemp(103, 50.0F);
/* 1393 */     skills.learnTemp(100, 3.0F);
/* 1394 */     skills.learnTemp(101, 10.0F);
/* 1395 */     skills.learnTemp(105, 4.0F);
/* 1396 */     skills.learnTemp(106, 2.0F);
/* 1397 */     skills.learnTemp(10052, 30.0F);
/* 1398 */     int[] types = { 7, 13, 16, 29, 39, 24, 40, 46, 50 };
/*      */ 
/*      */     
/* 1401 */     int biteDamage = 15;
/* 1402 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.spawn.uttacha", types, (byte)9, skills, (short)5, (byte)1, (short)250, (short)100, (short)150, "sound.death.uttacha.spawn", "sound.death.uttacha.spawn", "sound.combat.hit.deathcrawler", "sound.combat.hit.deathcrawler", 0.7F, 7.0F, 0.0F, 15.0F, 0.0F, 0.0F, 0.5F, 1500, new int[] { 153, 683 }, 10, 34, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1407 */     temp.setAlignment(-10.0F);
/* 1408 */     temp.setMaxAge(200);
/* 1409 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER);
/* 1410 */     temp.setBaseCombatRating(12.0F);
/* 1411 */     temp.combatDamageType = 10;
/* 1412 */     temp.setMaxGroupAttackSize(8);
/* 1413 */     temp.setHandDamString("bite");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDeathCrawlerMinionTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1426 */     skills.learnTemp(102, 15.0F);
/* 1427 */     skills.learnTemp(104, 25.0F);
/* 1428 */     skills.learnTemp(103, 20.0F);
/* 1429 */     skills.learnTemp(100, 3.0F);
/* 1430 */     skills.learnTemp(101, 10.0F);
/* 1431 */     skills.learnTemp(105, 30.0F);
/* 1432 */     skills.learnTemp(106, 5.0F);
/* 1433 */     skills.learnTemp(10052, 40.0F);
/* 1434 */     int[] types = { 7, 13, 16, 29, 39, 24, 40, 46, 50 };
/*      */ 
/*      */     
/* 1437 */     int biteDamage = 15;
/* 1438 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.deathcrawler", types, (byte)8, skills, (short)5, (byte)0, (short)250, (short)575, (short)198, "sound.death.deathcrawler", "sound.death.deathcrawler", "sound.combat.hit.deathcrawler", "sound.combat.hit.deathcrawler", 0.3F, 8.0F, 0.0F, 15.0F, 0.0F, 0.0F, 1.1F, 1500, new int[] { 683, 310 }, 10, 34, (byte)87);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1444 */     temp.setBoundsValues(-1.065511F, -2.90318F, 1.065511F, 3.029689F);
/*      */     
/* 1446 */     temp.setAlignment(-10.0F);
/* 1447 */     temp.setMaxAge(200);
/* 1448 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 1449 */     temp.setBaseCombatRating(14.0F);
/* 1450 */     temp.combatDamageType = 5;
/* 1451 */     temp.setMaxGroupAttackSize(10);
/* 1452 */     temp.setHandDamString("claw");
/* 1453 */     temp.setUsesNewAttacks(true);
/* 1454 */     temp.addPrimaryAttack(new AttackAction("strike", AttackIdentifier.STRIKE, new AttackValues(8.0F, 0.02F, 3.0F, 2, 1, (byte)1, false, 3, 1.0F)));
/*      */ 
/*      */     
/* 1457 */     temp.addPrimaryAttack(new AttackAction("poison strike", AttackIdentifier.STRIKE, new AttackValues(8.0F, 0.02F, 8.0F, 2, 1, (byte)5, false, 3, 1.0F)));
/*      */ 
/*      */     
/* 1460 */     temp.addSecondaryAttack(new AttackAction("bite", AttackIdentifier.BITE, new AttackValues(15.0F, 0.08F, 4.0F, 3, 1, (byte)3, false, 4, 1.1F)));
/*      */     
/* 1462 */     temp.addSecondaryAttack(new AttackAction("claw", AttackIdentifier.CLAW, new AttackValues(10.0F, 0.04F, 4.0F, 3, 1, (byte)0, false, 5, 1.2F)));
/*      */ 
/*      */     
/* 1465 */     temp.setCombatMoves(new int[] { 1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDemonSolTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1479 */     skills.learnTemp(102, 35.0F);
/* 1480 */     skills.learnTemp(104, 35.0F);
/* 1481 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 1483 */     skills.learnTemp(100, 6.0F);
/* 1484 */     skills.learnTemp(101, 30.0F);
/* 1485 */     skills.learnTemp(105, 50.0F);
/* 1486 */     skills.learnTemp(106, 20.0F);
/* 1487 */     skills.learnTemp(10052, 70.0F);
/* 1488 */     int[] types = { 8, 13, 16, 29, 30, 36, 39, 24, 40, 45, 46, 50, 55 };
/*      */ 
/*      */ 
/*      */     
/* 1492 */     int biteDamage = 20;
/* 1493 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.demon.sol", types, (byte)0, skills, (short)7, (byte)0, (short)150, (short)100, (short)150, "sound.death.demon", "sound.death.demon", "sound.combat.hit.demon", "sound.combat.hit.demon", 0.5F, 6.0F, 10.0F, 20.0F, 6.0F, 12.0F, 1.5F, 1550, new int[] { 204, 636, 683 }, 30, 49, (byte)87);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1498 */     temp.setHandDamString("burn");
/* 1499 */     temp.setAlignment(-90.0F);
/* 1500 */     temp.setMaxAge(200);
/* 1501 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 1502 */     temp.setBaseCombatRating(14.0F);
/* 1503 */     temp.combatDamageType = 4;
/* 1504 */     temp.setOnFire(true);
/* 1505 */     temp.setFireRadius((byte)5);
/* 1506 */     temp.setMaxGroupAttackSize(6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createZombieTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1519 */     skills.learnTemp(102, 30.0F);
/* 1520 */     skills.learnTemp(104, 20.0F);
/* 1521 */     skills.learnTemp(103, 40.0F);
/* 1522 */     skills.learnTemp(100, 20.0F);
/* 1523 */     skills.learnTemp(101, 20.0F);
/* 1524 */     skills.learnTemp(105, 20.0F);
/* 1525 */     skills.learnTemp(106, 20.0F);
/* 1526 */     skills.learnTemp(1007, 1.0F);
/* 1527 */     skills.learnTemp(1009, 1.0F);
/* 1528 */     skills.learnTemp(1008, 1.0F);
/* 1529 */     skills.learnTemp(1019, 1.0F);
/* 1530 */     skills.learnTemp(10049, 1.0F);
/* 1531 */     skills.learnTemp(1011, 1.0F);
/* 1532 */     skills.learnTemp(10033, 1.0F);
/* 1533 */     skills.learnTemp(10031, 1.0F);
/* 1534 */     skills.learnTemp(10052, 70.0F);
/*      */     
/* 1536 */     int[] types = { 13, 17, 7, 25, 29 };
/*      */     
/* 1538 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.player", types, (byte)0, skills, (short)80, (byte)0, (short)180, (short)20, (short)35, "sound.combat.death.zombie", "sound.combat.death.zombie", "sound.combat.hit.zombie", "sound.combat.hit.zombie", 1.0F, 5.0F, 3.0F, 7.0F, 0.0F, 0.0F, 0.6F, 0, new int[0], 25, 100, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1543 */     temp.setBaseCombatRating(8.0F);
/* 1544 */     temp.hasHands = true;
/* 1545 */     temp.setAlignment(-20.0F);
/* 1546 */     temp.setMaxAge(2);
/* 1547 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER);
/* 1548 */     temp.combatDamageType = 0;
/* 1549 */     temp.setPaintMode(1);
/* 1550 */     temp.setMaxGroupAttackSize(3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSkeletonTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1563 */     skills.learnTemp(102, 10.0F);
/* 1564 */     skills.learnTemp(104, 20.0F);
/* 1565 */     skills.learnTemp(103, 20.0F);
/* 1566 */     skills.learnTemp(100, 5.0F);
/* 1567 */     skills.learnTemp(101, 10.0F);
/* 1568 */     skills.learnTemp(105, 10.0F);
/* 1569 */     skills.learnTemp(106, 10.0F);
/* 1570 */     skills.learnTemp(10052, 70.0F);
/*      */     
/* 1572 */     int[] types = { 13, 7, 24 };
/*      */     
/* 1574 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.skeleton", types, (byte)0, skills, (short)80, (byte)0, (short)180, (short)20, (short)35, "sound.combat.death.skeleton", "sound.combat.death.skeleton", "sound.combat.hit.skeleton", "sound.combat.hit.skeleton", 1.0F, 3.0F, 3.0F, 7.0F, 0.0F, 0.0F, 0.6F, 0, new int[0], 25, 100, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1579 */     temp.setBaseCombatRating(8.0F);
/* 1580 */     temp.hasHands = true;
/* 1581 */     temp.setAlignment(-20.0F);
/* 1582 */     temp.setMaxAge(2);
/* 1583 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 1584 */     temp.combatDamageType = 0;
/* 1585 */     temp.physicalResistance = 0.2F;
/* 1586 */     temp.acidVulnerability = 3.0F;
/* 1587 */     temp.fireVulnerability = 2.0F;
/* 1588 */     temp.setMaxGroupAttackSize(3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createLadyLakeTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1601 */     skills.learnTemp(102, 15.0F);
/* 1602 */     skills.learnTemp(104, 15.0F);
/* 1603 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 1605 */     skills.learnTemp(100, 10.0F);
/* 1606 */     skills.learnTemp(101, 10.0F);
/* 1607 */     skills.learnTemp(105, 90.0F);
/* 1608 */     skills.learnTemp(106, 99.0F);
/* 1609 */     skills.learnTemp(10052, 40.0F);
/* 1610 */     int[] types = { 0, 17, 18, 40, 20, 37 };
/*      */     
/* 1612 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.ladylake", types, (byte)0, skills, (short)5, (byte)1, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 0.001F, 10.0F, 20.0F, 0.0F, 0.0F, 0.0F, 0.8F, 0, new int[] { 308, 308 }, 3, 1, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1617 */     temp.setKeepSex(true);
/* 1618 */     temp.setBaseCombatRating(99.0F);
/* 1619 */     temp.setMaxGroupAttackSize(10);
/* 1620 */     temp.setCombatDamageType((byte)1);
/* 1621 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createKingCobraTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1634 */     skills.learnTemp(102, 50.0F);
/* 1635 */     skills.learnTemp(104, 45.0F);
/* 1636 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 1638 */     skills.learnTemp(100, 42.0F);
/* 1639 */     skills.learnTemp(101, 42.0F);
/* 1640 */     skills.learnTemp(105, 99.0F);
/* 1641 */     skills.learnTemp(106, 3.0F);
/* 1642 */     skills.learnTemp(10052, 95.0F);
/* 1643 */     int[] types = { 0, 3, 40, 20 };
/*      */     
/* 1645 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.snake.kingcobra", types, (byte)9, skills, (short)5, (byte)0, (short)20, (short)20, (short)450, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 0.001F, 10.0F, 0.0F, 20.0F, 0.0F, 30.0F, 0.8F, 0, new int[] { 303, 310 }, 1, 1, (byte)86);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1650 */     temp.setHandDamString("bite");
/* 1651 */     temp.setBreathDamString("squeeze");
/* 1652 */     temp.setMaxAge(1000000);
/* 1653 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER);
/* 1654 */     temp.setBaseCombatRating(87.0F);
/* 1655 */     temp.setMaxGroupAttackSize(10);
/* 1656 */     temp.combatDamageType = 0;
/* 1657 */     temp.setAlignment(-100.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createAnadondaTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1670 */     skills.learnTemp(102, 30.0F);
/* 1671 */     skills.learnTemp(104, 25.0F);
/* 1672 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 1674 */     skills.learnTemp(100, 2.0F);
/* 1675 */     skills.learnTemp(101, 4.0F);
/* 1676 */     skills.learnTemp(105, 30.0F);
/* 1677 */     skills.learnTemp(106, 3.0F);
/* 1678 */     skills.learnTemp(10052, 65.0F);
/* 1679 */     int[] types = { 7, 3, 29, 12, 6, 39, 24, 60, 61 };
/*      */ 
/*      */     
/* 1682 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.snake.anaconda", types, (byte)9, skills, (short)10, (byte)0, (short)20, (short)20, (short)350, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 0.3F, 0.0F, 0.0F, 6.0F, 0.0F, 10.0F, 0.8F, 50, new int[] { 303, 310 }, 10, 24, (byte)86);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1687 */     temp.setBreathDamString("squeeze");
/* 1688 */     temp.setMaxAge(100);
/* 1689 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER);
/* 1690 */     temp.setBaseCombatRating(13.0F);
/* 1691 */     temp.setMaxGroupAttackSize(4);
/* 1692 */     temp.combatDamageType = 0;
/* 1693 */     temp.setMaxPercentOfCreatures(0.02F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createPigTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1706 */     skills.learnTemp(102, 10.0F);
/* 1707 */     skills.learnTemp(104, 15.0F);
/* 1708 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 1710 */     skills.learnTemp(100, 2.0F);
/* 1711 */     skills.learnTemp(101, 4.0F);
/* 1712 */     skills.learnTemp(105, 10.0F);
/* 1713 */     skills.learnTemp(106, 3.0F);
/* 1714 */     skills.learnTemp(10052, 5.0F);
/* 1715 */     int[] types = { 7, 3, 43, 27, 14, 32 };
/*      */     
/* 1717 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.pig", types, (byte)3, skills, (short)10, (byte)0, (short)50, (short)50, (short)150, "sound.death.pig", "sound.death.pig", "sound.combat.hit.pig", "sound.combat.hit.pig", 1.0F, 1.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.8F, 20, new int[] { 303, 140, 310, 308, 308 }, 10, 54, (byte)84);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1722 */     temp.setHandDamString("kick");
/* 1723 */     temp.setMaxAge(100);
/* 1724 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 1725 */     temp.setBaseCombatRating(2.0F);
/* 1726 */     temp.setMaxGroupAttackSize(3);
/* 1727 */     temp.combatDamageType = 0;
/* 1728 */     temp.setMaxPercentOfCreatures(0.03F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createLavaCreatureTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1741 */     skills.learnTemp(102, 25.0F);
/* 1742 */     skills.learnTemp(104, 35.0F);
/* 1743 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 1745 */     skills.learnTemp(100, 12.0F);
/* 1746 */     skills.learnTemp(101, 14.0F);
/* 1747 */     skills.learnTemp(105, 90.0F);
/* 1748 */     skills.learnTemp(106, 5.0F);
/* 1749 */     skills.learnTemp(10052, 50.0F);
/* 1750 */     int[] types = { 7, 6, 13, 16, 40, 29, 30, 34, 32, 36, 39, 45, 55, 61 };
/*      */ 
/*      */ 
/*      */     
/* 1754 */     int biteDamage = 10;
/*      */ 
/*      */     
/* 1757 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.lavacreature", types, (byte)0, skills, (short)5, (byte)0, (short)150, (short)100, (short)150, "sound.death.lizard", "sound.death.lizard", "sound.combat.hit.lizard", "sound.combat.hit.lizard", 0.5F, 6.0F, 10.0F, 10.0F, 0.0F, 0.0F, 1.0F, 1500, new int[] { 204, 446, 636 }, 10, 34, (byte)87);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1762 */     temp.setHandDamString("burn");
/* 1763 */     temp.setAlignment(-20.0F);
/* 1764 */     temp.setMaxAge(200);
/* 1765 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 1766 */     temp.setBaseCombatRating(16.0F);
/* 1767 */     temp.combatDamageType = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1775 */     temp.setOnFire(false);
/*      */     
/* 1777 */     temp.setMaxGroupAttackSize(6);
/* 1778 */     temp.setGlowing(false);
/* 1779 */     temp.setSubterranean(true);
/* 1780 */     temp.setMaxPercentOfCreatures(0.03F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createLavaSpiderTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1793 */     skills.learnTemp(102, 20.0F);
/* 1794 */     skills.learnTemp(104, 45.0F);
/* 1795 */     skills.learnTemp(103, 35.0F);
/*      */     
/* 1797 */     skills.learnTemp(100, 8.0F);
/* 1798 */     skills.learnTemp(101, 10.0F);
/* 1799 */     skills.learnTemp(105, 40.0F);
/* 1800 */     skills.learnTemp(106, 2.0F);
/* 1801 */     skills.learnTemp(10052, 70.0F);
/* 1802 */     int[] types = { 7, 6, 13, 3, 29, 39, 55, 60, 61 };
/*      */ 
/*      */     
/* 1805 */     int biteDamage = 7;
/* 1806 */     if (Servers.localServer.PVPSERVER)
/* 1807 */       biteDamage = 10; 
/* 1808 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.multiped.spider.lava", types, (byte)8, skills, (short)5, (byte)0, (short)150, (short)100, (short)150, "sound.death.spider", "sound.death.spider", "sound.combat.hit.spider", "sound.combat.hit.spider", 0.6F, 6.0F, 0.0F, biteDamage, 0.0F, 0.0F, 0.9F, 500, new int[] { 204, 636 }, 10, 54, (byte)82);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1813 */     temp.setHandDamString("claw");
/* 1814 */     temp.setAlignment(-50.0F);
/* 1815 */     temp.setMaxAge(200);
/* 1816 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 1817 */     temp.setBaseCombatRating(13.0F);
/* 1818 */     temp.combatDamageType = 4;
/* 1819 */     temp.setOnFire(true);
/* 1820 */     temp.setFireRadius((byte)40);
/* 1821 */     temp.setMaxGroupAttackSize(6);
/* 1822 */     temp.setGlowing(true);
/* 1823 */     temp.setDenName("spider lair");
/* 1824 */     temp.setDenMaterial((byte)15);
/* 1825 */     temp.setSubterranean(true);
/* 1826 */     temp.setMaxPercentOfCreatures(0.05F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createFogSpiderTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1839 */     skills.learnTemp(102, 25.0F);
/* 1840 */     skills.learnTemp(104, 45.0F);
/* 1841 */     skills.learnTemp(103, 35.0F);
/*      */     
/* 1843 */     skills.learnTemp(100, 8.0F);
/* 1844 */     skills.learnTemp(101, 12.0F);
/* 1845 */     skills.learnTemp(105, 40.0F);
/* 1846 */     skills.learnTemp(106, 2.0F);
/* 1847 */     skills.learnTemp(10052, 70.0F);
/* 1848 */     int[] types = { 7, 6, 13, 3, 29, 39 };
/*      */     
/* 1850 */     int biteDamage = 11;
/* 1851 */     if (Servers.localServer.PVPSERVER)
/* 1852 */       biteDamage = 14; 
/* 1853 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.multiped.spider.fog", types, (byte)8, skills, (short)5, (byte)0, (short)150, (short)100, (short)150, "sound.death.spider", "sound.death.spider", "sound.combat.hit.spider", "sound.combat.hit.spider", 0.2F, 3.0F, 0.0F, biteDamage, 0.0F, 0.0F, 0.8F, 500, new int[0], 15, 74, (byte)82);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1858 */     temp.setHandDamString("claw");
/* 1859 */     temp.setAlignment(0.0F);
/* 1860 */     temp.setMaxAge(30);
/* 1861 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 1862 */     temp.setBaseCombatRating(15.0F);
/* 1863 */     temp.combatDamageType = 5;
/* 1864 */     temp.setMaxGroupAttackSize(6);
/* 1865 */     temp.setMaxPercentOfCreatures(0.01F);
/* 1866 */     temp.setCombatMoves(new int[] { 11 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSpiderTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1879 */     skills.learnTemp(102, 20.0F);
/* 1880 */     skills.learnTemp(104, 45.0F);
/* 1881 */     skills.learnTemp(103, 35.0F);
/*      */     
/* 1883 */     skills.learnTemp(100, 8.0F);
/* 1884 */     skills.learnTemp(101, 10.0F);
/* 1885 */     skills.learnTemp(105, 40.0F);
/* 1886 */     skills.learnTemp(106, 2.0F);
/* 1887 */     skills.learnTemp(10052, 40.0F);
/* 1888 */     int[] types = { 7, 6, 13, 3, 29, 39, 60, 61 };
/*      */ 
/*      */     
/* 1891 */     int biteDamage = 6;
/* 1892 */     if (Servers.localServer.PVPSERVER)
/* 1893 */       biteDamage = 10; 
/* 1894 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.multiped.spider.huge", types, (byte)8, skills, (short)5, (byte)0, (short)150, (short)100, (short)150, "sound.death.spider", "sound.death.spider", "sound.combat.hit.spider", "sound.combat.hit.spider", 0.7F, 0.0F, 0.0F, biteDamage, 0.0F, 0.0F, 1.2F, 500, new int[] { 636, 308, 308, 308, 308 }, 10, 74, (byte)82);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1900 */     temp.setHandDamString("claw");
/* 1901 */     temp.setAlignment(-50.0F);
/* 1902 */     temp.setMaxAge(100);
/* 1903 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 1904 */     temp.setBaseCombatRating(10.0F);
/* 1905 */     temp.combatDamageType = 2;
/* 1906 */     temp.setMaxGroupAttackSize(6);
/* 1907 */     temp.setDenName("spider lair");
/* 1908 */     temp.setDenMaterial((byte)15);
/* 1909 */     temp.setSubterranean(true);
/* 1910 */     temp.setMaxPercentOfCreatures(0.08F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createHyenaLabilaTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1923 */     skills.learnTemp(102, 20.0F);
/* 1924 */     skills.learnTemp(104, 45.0F);
/* 1925 */     skills.learnTemp(103, 35.0F);
/*      */     
/* 1927 */     skills.learnTemp(100, 8.0F);
/* 1928 */     skills.learnTemp(101, 10.0F);
/* 1929 */     skills.learnTemp(105, 40.0F);
/* 1930 */     skills.learnTemp(106, 2.0F);
/* 1931 */     skills.learnTemp(10052, 40.0F);
/* 1932 */     int[] types = { 7, 41, 25, 13, 3, 29, 36, 39 };
/*      */ 
/*      */     
/* 1935 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.hyena.rabid", types, (byte)3, skills, (short)10, (byte)0, (short)40, (short)20, (short)100, "sound.death.dog", "sound.death.dog", "sound.combat.hit.dog", "sound.combat.hit.dog", 0.6F, 10.0F, 0.0F, 12.0F, 0.0F, 0.0F, 1.2F, 300, new int[0], 10, 94, (byte)87);
/*      */ 
/*      */ 
/*      */     
/* 1939 */     temp.setHandDamString("claw");
/* 1940 */     temp.setAlignment(-50.0F);
/* 1941 */     temp.setMaxAge(5);
/* 1942 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 1943 */     temp.setBaseCombatRating(14.0F);
/* 1944 */     temp.combatDamageType = 1;
/* 1945 */     temp.setMaxGroupAttackSize(8);
/* 1946 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSharkHugeTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1959 */     skills.learnTemp(102, 20.0F);
/* 1960 */     skills.learnTemp(104, 45.0F);
/* 1961 */     skills.learnTemp(103, 50.0F);
/*      */     
/* 1963 */     skills.learnTemp(100, 14.0F);
/* 1964 */     skills.learnTemp(101, 10.0F);
/* 1965 */     skills.learnTemp(105, 40.0F);
/* 1966 */     skills.learnTemp(106, 5.0F);
/* 1967 */     skills.learnTemp(10052, 85.0F);
/* 1968 */     skills.learnTemp(1023, 80.0F);
/* 1969 */     int[] types = { 8, 38, 6, 13, 16, 29, 44, 40, 48, 37, 61 };
/*      */ 
/*      */ 
/*      */     
/* 1973 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.fish.shark.huge", types, (byte)9, skills, (short)3, (byte)0, (short)100, (short)1000, (short)100, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 0.2F, 0.0F, 0.0F, 16.0F, 10.0F, 0.0F, 1.0F, 100, new int[] { 308, 308, 310 }, 40, 59, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1978 */     temp.setAlignment(-20.0F);
/* 1979 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 1980 */     temp.setBaseCombatRating(12.0F);
/* 1981 */     temp.combatDamageType = 3;
/* 1982 */     temp.hasHands = false;
/* 1983 */     temp.setMaxAge(40);
/* 1984 */     temp.offZ = -1.4F;
/* 1985 */     temp.setBonusCombatRating(12.0F);
/* 1986 */     temp.setMaxGroupAttackSize(8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createBlueWhaleTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 1999 */     skills.learnTemp(102, 50.0F);
/* 2000 */     skills.learnTemp(104, 20.0F);
/* 2001 */     skills.learnTemp(103, 50.0F);
/*      */     
/* 2003 */     skills.learnTemp(100, 14.0F);
/* 2004 */     skills.learnTemp(101, 10.0F);
/* 2005 */     skills.learnTemp(105, 40.0F);
/* 2006 */     skills.learnTemp(106, 5.0F);
/* 2007 */     skills.learnTemp(10052, 85.0F);
/* 2008 */     int[] types = { 8, 38, 13, 3, 29, 44, 48, 37, 60, 61 };
/*      */ 
/*      */     
/* 2011 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.fish.blue.whale", types, (byte)9, skills, (short)3, (byte)0, (short)200, (short)1000, (short)300, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 0.3F, 0.0F, 0.0F, 25.0F, 15.0F, 0.0F, 1.0F, 100, new int[] { 308, 308 }, 40, 59, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2016 */     temp.setAlignment(-20.0F);
/* 2017 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 2018 */     temp.setBaseCombatRating(18.0F);
/* 2019 */     temp.combatDamageType = 3;
/* 2020 */     temp.hasHands = false;
/* 2021 */     temp.setMaxAge(80);
/* 2022 */     temp.offZ = -1.4F;
/* 2023 */     temp.setBonusCombatRating(12.0F);
/* 2024 */     temp.setMaxPopulationOfCreatures(3);
/* 2025 */     temp.setMaxGroupAttackSize(20);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDolphinTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2038 */     skills.learnTemp(102, 25.0F);
/* 2039 */     skills.learnTemp(104, 65.0F);
/* 2040 */     skills.learnTemp(103, 50.0F);
/*      */     
/* 2042 */     skills.learnTemp(100, 14.0F);
/* 2043 */     skills.learnTemp(101, 10.0F);
/* 2044 */     skills.learnTemp(105, 40.0F);
/* 2045 */     skills.learnTemp(106, 5.0F);
/* 2046 */     skills.learnTemp(10052, 60.0F);
/* 2047 */     int[] types = { 8, 38, 13, 16, 29, 44, 48, 37, 60, 61 };
/*      */ 
/*      */     
/* 2050 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.fish.dolphin", types, (byte)9, skills, (short)3, (byte)0, (short)80, (short)250, (short)50, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 0.7F, 0.0F, 0.0F, 16.0F, 10.0F, 0.0F, 1.0F, 100, new int[] { 308, 308 }, 40, 59, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2055 */     temp.setAlignment(-20.0F);
/* 2056 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 2057 */     temp.setBaseCombatRating(9.0F);
/* 2058 */     temp.combatDamageType = 3;
/* 2059 */     temp.hasHands = false;
/* 2060 */     temp.setMaxAge(80);
/* 2061 */     temp.offZ = -1.4F;
/* 2062 */     temp.setBonusCombatRating(7.0F);
/* 2063 */     temp.setMaxPercentOfCreatures(0.01F);
/* 2064 */     temp.setMaxPopulationOfCreatures(150);
/* 2065 */     temp.setMaxGroupAttackSize(4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createOctopusTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2078 */     skills.learnTemp(102, 35.0F);
/* 2079 */     skills.learnTemp(104, 45.0F);
/* 2080 */     skills.learnTemp(103, 40.0F);
/*      */     
/* 2082 */     skills.learnTemp(100, 14.0F);
/* 2083 */     skills.learnTemp(101, 10.0F);
/* 2084 */     skills.learnTemp(105, 40.0F);
/* 2085 */     skills.learnTemp(106, 5.0F);
/* 2086 */     skills.learnTemp(10052, 60.0F);
/* 2087 */     int[] types = { 8, 38, 13, 16, 29, 44, 48, 37, 60, 61 };
/*      */ 
/*      */     
/* 2090 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.fish.octopus", types, (byte)9, skills, (short)3, (byte)0, (short)100, (short)100, (short)100, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 1.0F, 0.0F, 0.0F, 14.0F, 12.0F, 0.0F, 1.0F, 100, new int[] { 308, 308, 752 }, 40, 59, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2095 */     temp.setAlignment(-20.0F);
/* 2096 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 2097 */     temp.setBaseCombatRating(9.0F);
/* 2098 */     temp.combatDamageType = 3;
/* 2099 */     temp.hasHands = false;
/* 2100 */     temp.setMaxAge(80);
/* 2101 */     temp.offZ = -1.4F;
/* 2102 */     temp.setBonusCombatRating(7.0F);
/* 2103 */     temp.setMaxPercentOfCreatures(0.01F);
/* 2104 */     temp.setMaxPopulationOfCreatures(150);
/* 2105 */     temp.setMaxGroupAttackSize(3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSealTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2118 */     skills.learnTemp(102, 20.0F);
/* 2119 */     skills.learnTemp(104, 45.0F);
/* 2120 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 2122 */     skills.learnTemp(100, 6.0F);
/* 2123 */     skills.learnTemp(101, 8.0F);
/* 2124 */     skills.learnTemp(105, 10.0F);
/* 2125 */     skills.learnTemp(106, 15.0F);
/* 2126 */     skills.learnTemp(10052, 25.0F);
/* 2127 */     int[] types = { 8, 12, 35, 3, 29, 44, 48, 51, 32, 60, 61 };
/*      */ 
/*      */ 
/*      */     
/* 2131 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.fish.seal", types, (byte)9, skills, (short)3, (byte)0, (short)100, (short)100, (short)100, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 0.7F, 0.0F, 0.0F, 8.0F, 5.0F, 0.0F, 0.5F, 100, new int[] { 140, 140, 71, 71, 310 }, 40, 59, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2138 */     temp.setAlignment(-20.0F);
/* 2139 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 2140 */     temp.setBaseCombatRating(7.0F);
/* 2141 */     temp.setChildTemplateId(98);
/* 2142 */     temp.combatDamageType = 3;
/* 2143 */     temp.hasHands = false;
/* 2144 */     temp.setMaxAge(40);
/* 2145 */     temp.offZ = -1.4F;
/* 2146 */     temp.setMaxPercentOfCreatures(0.02F);
/* 2147 */     temp.setMaxGroupAttackSize(4);
/*      */     
/* 2149 */     temp.setUsesNewAttacks(true);
/* 2150 */     temp.setBoundsValues(-0.975F, -0.9F, 0.975F, 0.9F);
/*      */     
/* 2152 */     temp.addPrimaryAttack(new AttackAction("bite", AttackIdentifier.BITE, new AttackValues(8.0F, 0.04F, 5.0F, 2, 1, (byte)3, false, 3, 1.0F)));
/*      */     
/* 2154 */     temp.addPrimaryAttack(new AttackAction("strike", AttackIdentifier.STRIKE, new AttackValues(5.0F, 0.04F, 6.0F, 2, 1, (byte)1, false, 2, 1.0F)));
/*      */ 
/*      */     
/* 2157 */     temp.addSecondaryAttack(new AttackAction("bite", AttackIdentifier.BITE, new AttackValues(7.0F, 0.1F, 7.0F, 2, 1, (byte)3, false, 4, 1.1F)));
/*      */     
/* 2159 */     temp.addSecondaryAttack(new AttackAction("headbutt", AttackIdentifier.HEADBUTT, new AttackValues(7.0F, 0.1F, 7.0F, 2, 1, (byte)0, false, 4, 1.1F)));
/*      */ 
/*      */     
/* 2162 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 2163 */       temp.setVision((short)4);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSealCubTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2176 */     skills.learnTemp(102, 10.0F);
/* 2177 */     skills.learnTemp(104, 15.0F);
/* 2178 */     skills.learnTemp(103, 15.0F);
/*      */     
/* 2180 */     skills.learnTemp(100, 6.0F);
/* 2181 */     skills.learnTemp(101, 8.0F);
/* 2182 */     skills.learnTemp(105, 10.0F);
/* 2183 */     skills.learnTemp(106, 15.0F);
/* 2184 */     skills.learnTemp(10052, 15.0F);
/* 2185 */     int[] types = { 8, 12, 35, 3, 29, 44, 48, 51, 32, 63 };
/*      */ 
/*      */     
/* 2188 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.fish.seal.cub", types, (byte)9, skills, (short)3, (byte)0, (short)80, (short)90, (short)70, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 0.7F, 0.0F, 0.0F, 8.0F, 5.0F, 0.0F, 0.5F, 100, new int[] { 140, 140, 71, 71, 310 }, 40, 59, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2196 */     temp.setAlignment(-20.0F);
/* 2197 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 2198 */     temp.setBaseCombatRating(1.0F);
/* 2199 */     temp.setAdultFemaleTemplateId(93);
/* 2200 */     temp.setAdultMaleTemplateId(93);
/* 2201 */     temp.combatDamageType = 3;
/* 2202 */     temp.hasHands = false;
/* 2203 */     temp.setMaxAge(20);
/* 2204 */     temp.setMaxGroupAttackSize(2);
/* 2205 */     temp.offZ = -1.4F;
/*      */     
/* 2207 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 2208 */       temp.setVision((short)3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSeaSerpentTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2221 */     skills.learnTemp(102, 40.0F);
/* 2222 */     skills.learnTemp(104, 55.0F);
/* 2223 */     skills.learnTemp(103, 60.0F);
/*      */     
/* 2225 */     skills.learnTemp(100, 24.0F);
/* 2226 */     skills.learnTemp(101, 10.0F);
/* 2227 */     skills.learnTemp(105, 80.0F);
/* 2228 */     skills.learnTemp(106, 5.0F);
/* 2229 */     skills.learnTemp(10052, 85.0F);
/*      */     
/* 2231 */     skills.learnTemp(1023, 80.0F);
/* 2232 */     int[] types = { 8, 38, 6, 13, 16, 29, 44, 40, 37, 61 };
/*      */ 
/*      */     
/* 2235 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.snake.serpent.sea", types, (byte)9, skills, (short)20, (byte)0, (short)100, (short)1000, (short)100, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 0.05F, 0.0F, 0.0F, 56.0F, 30.0F, 0.0F, 2.0F, 50, new int[] { 308, 308, 310 }, 40, 59, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2240 */     temp.setAlignment(-20.0F);
/* 2241 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 2242 */     temp.setBaseCombatRating(76.0F);
/* 2243 */     temp.setMaxGroupAttackSize(25);
/* 2244 */     temp.combatDamageType = 0;
/* 2245 */     temp.hasHands = false;
/* 2246 */     temp.setMaxAge(400);
/* 2247 */     temp.setSizeModX(200);
/* 2248 */     temp.setSizeModY(200);
/* 2249 */     temp.setSizeModZ(200);
/* 2250 */     temp.setMaxPopulationOfCreatures(4);
/* 2251 */     temp.offZ = -5.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createAvengerOfLightTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2264 */     skills.learnTemp(102, 40.0F);
/* 2265 */     skills.learnTemp(104, 55.0F);
/* 2266 */     skills.learnTemp(103, 60.0F);
/*      */     
/* 2268 */     skills.learnTemp(100, 24.0F);
/* 2269 */     skills.learnTemp(101, 30.0F);
/* 2270 */     skills.learnTemp(105, 80.0F);
/* 2271 */     skills.learnTemp(106, 39.0F);
/* 2272 */     skills.learnTemp(10052, 75.0F);
/* 2273 */     int[] types = { 8, 13, 16, 27, 36, 12, 24, 45, 50, 62, 65 };
/*      */ 
/*      */     
/* 2276 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.avenger.light", types, (byte)0, skills, (short)20, (byte)0, (short)370, (short)100, (short)60, "sound.death.giant", "sound.death.giant", "sound.combat.hit.giant", "sound.combat.hit.giant", 0.1F, 10.0F, 24.0F, 26.0F, 0.0F, 0.0F, 1.8F, 100, new int[] { 308, 308, 310 }, 40, 20, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2281 */     temp.setAlignment(-20.0F);
/* 2282 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 2283 */     temp.setBaseCombatRating(76.0F);
/* 2284 */     temp.setMaxGroupAttackSize(25);
/* 2285 */     temp.combatDamageType = 0;
/* 2286 */     temp.setCombatMoves(new int[] { 1, 7 });
/*      */     
/* 2288 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createBoarFoTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2301 */     skills.learnTemp(102, 30.0F);
/* 2302 */     skills.learnTemp(104, 35.0F);
/* 2303 */     skills.learnTemp(103, 40.0F);
/*      */     
/* 2305 */     skills.learnTemp(100, 2.0F);
/* 2306 */     skills.learnTemp(101, 8.0F);
/* 2307 */     skills.learnTemp(105, 34.0F);
/* 2308 */     skills.learnTemp(106, 3.0F);
/* 2309 */     skills.learnTemp(10052, 40.0F);
/* 2310 */     int[] types = { 7, 41, 24, 13, 3, 27, 36, 39 };
/*      */ 
/*      */     
/* 2313 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.boar.wild", types, (byte)3, skills, (short)10, (byte)0, (short)50, (short)50, (short)150, "sound.death.pig", "sound.death.pig", "sound.combat.hit.pig", "sound.combat.hit.pig", 0.6F, 6.0F, 0.0F, 7.0F, 10.0F, 0.0F, 1.2F, 300, new int[] { 92, 140, 303 }, 10, 94, (byte)84);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2318 */     temp.setHandDamString("kick");
/* 2319 */     temp.setAlignment(10.0F);
/* 2320 */     temp.setMaxAge(5);
/* 2321 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 2322 */     temp.setBaseCombatRating(14.0F);
/* 2323 */     temp.combatDamageType = 0;
/* 2324 */     temp.setMaxGroupAttackSize(4);
/* 2325 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGorillaMagranonTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2338 */     skills.learnTemp(102, 40.0F);
/* 2339 */     skills.learnTemp(104, 25.0F);
/* 2340 */     skills.learnTemp(103, 40.0F);
/*      */     
/* 2342 */     skills.learnTemp(100, 8.0F);
/* 2343 */     skills.learnTemp(101, 10.0F);
/* 2344 */     skills.learnTemp(105, 30.0F);
/* 2345 */     skills.learnTemp(106, 7.0F);
/* 2346 */     skills.learnTemp(10052, 40.0F);
/* 2347 */     int[] types = { 7, 24, 13, 3, 30, 27, 36, 39, 45 };
/*      */ 
/*      */     
/* 2350 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.gorilla.mountain", types, (byte)0, skills, (short)10, (byte)0, (short)210, (short)50, (short)50, "sound.death.gorilla", "sound.death.gorilla", "sound.combat.hit.gorilla", "sound.combat.hit.gorilla", 0.6F, 6.0F, 0.0F, 10.0F, 0.0F, 0.0F, 1.2F, 300, new int[] { 303, 308, 308 }, 10, 94, (byte)78);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2355 */     temp.setHandDamString("claw");
/* 2356 */     temp.setAlignment(10.0F);
/* 2357 */     temp.setMaxAge(10);
/* 2358 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 2359 */     temp.setBaseCombatRating(14.0F);
/* 2360 */     temp.combatDamageType = 0;
/* 2361 */     temp.setMaxGroupAttackSize(6);
/* 2362 */     temp.hasHands = true;
/* 2363 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardKingdomTowerTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2376 */     skills.learnTemp(102, 20.0F);
/* 2377 */     skills.learnTemp(104, 17.0F);
/* 2378 */     skills.learnTemp(103, 21.0F);
/*      */     
/* 2380 */     skills.learnTemp(100, 15.0F);
/* 2381 */     skills.learnTemp(101, 15.0F);
/* 2382 */     skills.learnTemp(105, 15.0F);
/* 2383 */     skills.learnTemp(106, 17.0F);
/*      */     
/* 2385 */     if (id == 34) {
/* 2386 */       skills.learnTemp(10005, 45.0F);
/* 2387 */     } else if (id == 35) {
/* 2388 */       skills.learnTemp(10024, 45.0F);
/*      */     
/*      */     }
/* 2391 */     else if (id == 36) {
/* 2392 */       skills.learnTemp(10061, 45.0F);
/* 2393 */     } else if (id == 67) {
/* 2394 */       skills.learnTemp(10028, 45.0F);
/*      */     } 
/*      */     
/* 2397 */     skills.learnTemp(10020, 45.0F);
/*      */ 
/*      */     
/* 2400 */     skills.learnTemp(10052, 45.0F);
/* 2401 */     String modelname = "model.creature.humanoid.human.guard.tower";
/* 2402 */     int[] types = { 21, 12, 13, 17, 45 };
/*      */     
/* 2404 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.guard.tower", types, (byte)0, skills, (short)10, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 0.3F, 
/*      */ 
/*      */         
/* 2407 */         Servers.localServer.isChallengeServer() ? 6.0F : 4.0F, 7.0F, 0.0F, 0.0F, 0.0F, 1.0F, 200, new int[0], 30, 80, (byte)80);
/*      */     
/* 2409 */     int cr = Servers.localServer.isChallengeServer() ? 12 : 6;
/* 2410 */     temp.setBaseCombatRating(cr);
/* 2411 */     temp.setMaxAge(20);
/* 2412 */     temp.combatDamageType = 1;
/* 2413 */     temp.setMaxGroupAttackSize(6);
/* 2414 */     temp.setNoSkillgain(true);
/* 2415 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDummyDollTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2428 */     skills.learnTemp(102, 20.0F);
/* 2429 */     skills.learnTemp(104, 20.0F);
/* 2430 */     skills.learnTemp(103, 60.0F);
/* 2431 */     skills.learnTemp(100, 20.0F);
/* 2432 */     skills.learnTemp(101, 20.0F);
/* 2433 */     skills.learnTemp(105, 20.0F);
/* 2434 */     skills.learnTemp(106, 20.0F);
/* 2435 */     skills.learnTemp(10052, 10.0F);
/* 2436 */     int[] types = { 12, 13, 17 };
/*      */     
/* 2438 */     CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.player", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 0.2F, 2.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0, new int[0], 5, 0, (byte)87);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCatWildTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2454 */     skills.learnTemp(102, 8.0F);
/* 2455 */     skills.learnTemp(104, 13.0F);
/* 2456 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 2458 */     skills.learnTemp(100, 6.0F);
/* 2459 */     skills.learnTemp(101, 8.0F);
/* 2460 */     skills.learnTemp(105, 20.0F);
/* 2461 */     skills.learnTemp(106, 1.0F);
/* 2462 */     skills.learnTemp(10052, 3.0F);
/* 2463 */     int[] types = { 7, 3, 6, 32, 29, 60, 61 };
/*      */     
/* 2465 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.cat.wild", types, (byte)3, skills, (short)3, (byte)0, (short)20, (short)10, (short)300, "sound.death.cat", "sound.death.cat", "sound.combat.hit.cat", "sound.combat.hit.cat", 1.0F, 1.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.7F, 500, new int[] { 313 }, 5, 10, (byte)75);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2470 */     temp.setHandDamString("claw");
/* 2471 */     temp.setKickDamString("claw");
/* 2472 */     temp.setMaxAge(40);
/* 2473 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 2474 */     temp.setBaseCombatRating(2.0F);
/* 2475 */     temp.combatDamageType = 1;
/* 2476 */     temp.setMaxGroupAttackSize(2);
/* 2477 */     temp.setDenName("wildcat hideout");
/* 2478 */     temp.setDenMaterial((byte)15);
/* 2479 */     temp.setMaxPercentOfCreatures(0.02F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCaveBugTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2492 */     skills.learnTemp(102, 10.0F);
/* 2493 */     skills.learnTemp(104, 13.0F);
/* 2494 */     skills.learnTemp(103, 5.0F);
/*      */     
/* 2496 */     skills.learnTemp(100, 7.0F);
/* 2497 */     skills.learnTemp(101, 8.0F);
/* 2498 */     skills.learnTemp(105, 40.0F);
/* 2499 */     skills.learnTemp(106, 4.0F);
/* 2500 */     skills.learnTemp(10052, 16.0F);
/* 2501 */     int[] types = { 7, 3, 6, 13, 27, 32, 34, 60, 61 };
/*      */ 
/*      */     
/* 2504 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.insect.cavebug", types, (byte)3, skills, (short)5, (byte)0, (short)30, (short)30, (short)30, "sound.death.insect", "sound.death.insect", "sound.combat.hit.insect", "sound.combat.hit.insect", 0.9F, 3.0F, 0.0F, 6.0F, 0.0F, 0.0F, 0.6F, 50, new int[] { 439, 439, 439, 439, 439, 439, 439, 439, 439 }, 10, 30, (byte)82);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2534 */     temp.setHandDamString("claw");
/* 2535 */     temp.setKickDamString("claw");
/* 2536 */     temp.setMaxAge(100);
/* 2537 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 2538 */     temp.setBaseCombatRating(5.0F);
/* 2539 */     temp.combatDamageType = 1;
/* 2540 */     temp.setMaxGroupAttackSize(2);
/* 2541 */     temp.setDenName("cave bug mound");
/* 2542 */     temp.setDenMaterial((byte)15);
/* 2543 */     temp.setSubterranean(true);
/* 2544 */     temp.setMaxPercentOfCreatures(0.03F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createTortoiseTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2557 */     skills.learnTemp(102, 22.0F);
/* 2558 */     skills.learnTemp(104, 6.0F);
/* 2559 */     skills.learnTemp(103, 5.0F);
/*      */     
/* 2561 */     skills.learnTemp(100, 14.0F);
/* 2562 */     skills.learnTemp(101, 6.0F);
/* 2563 */     skills.learnTemp(105, 30.0F);
/* 2564 */     skills.learnTemp(106, 24.0F);
/* 2565 */     skills.learnTemp(10052, 36.0F);
/* 2566 */     int[] types = { 7, 3, 6, 35, 44, 28, 32, 51, 48, 12, 60, 61 };
/*      */ 
/*      */     
/* 2569 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.tortoise", types, (byte)3, skills, (short)5, (byte)0, (short)30, (short)30, (short)30, "sound.death.insect", "sound.death.insect", "sound.combat.hit.insect", "sound.combat.hit.insect", 0.3F, 0.0F, 0.0F, 12.0F, 0.0F, 0.0F, 0.3F, 50, new int[] { 898, 308, 308, 92 }, 10, 10, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2575 */     temp.setMaxAge(100);
/* 2576 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 2577 */     temp.setBaseCombatRating(12.0F);
/* 2578 */     temp.setMaxGroupAttackSize(3);
/* 2579 */     temp.setMaxPercentOfCreatures(0.02F);
/*      */     
/* 2581 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 2582 */       temp.setVision((short)6);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCrabTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2595 */     skills.learnTemp(102, 12.0F);
/* 2596 */     skills.learnTemp(104, 16.0F);
/* 2597 */     skills.learnTemp(103, 5.0F);
/*      */     
/* 2599 */     skills.learnTemp(100, 4.0F);
/* 2600 */     skills.learnTemp(101, 6.0F);
/* 2601 */     skills.learnTemp(105, 20.0F);
/* 2602 */     skills.learnTemp(106, 4.0F);
/* 2603 */     skills.learnTemp(10052, 16.0F);
/* 2604 */     int[] types = { 7, 3, 6, 13, 27, 32, 51, 48, 12, 60, 61 };
/*      */ 
/*      */     
/* 2607 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.crab", types, (byte)3, skills, (short)5, (byte)0, (short)30, (short)30, (short)30, "sound.death.insect", "sound.death.insect", "sound.combat.hit.insect", "sound.combat.hit.insect", 0.7F, 5.0F, 0.0F, 6.0F, 0.0F, 0.0F, 0.8F, 50, new int[] { 900, 900, 308, 308, 900 }, 10, 30, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2614 */     temp.setHandDamString("claw");
/* 2615 */     temp.setKickDamString("claw");
/* 2616 */     temp.setMaxAge(100);
/* 2617 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 2618 */     temp.setBaseCombatRating(5.0F);
/* 2619 */     temp.combatDamageType = 1;
/* 2620 */     temp.setMaxGroupAttackSize(3);
/* 2621 */     temp.setMaxPercentOfCreatures(0.03F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createLionMountainTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2634 */     skills.learnTemp(102, 15.0F);
/* 2635 */     skills.learnTemp(104, 3.0F);
/* 2636 */     skills.learnTemp(103, 15.0F);
/*      */     
/* 2638 */     skills.learnTemp(100, 7.0F);
/* 2639 */     skills.learnTemp(101, 8.0F);
/* 2640 */     skills.learnTemp(105, 25.0F);
/* 2641 */     skills.learnTemp(106, 4.0F);
/* 2642 */     skills.learnTemp(10052, 6.0F);
/* 2643 */     int[] types = { 7, 3, 6, 13, 30, 32, 29, 60, 61 };
/*      */ 
/*      */     
/* 2646 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.lion.mountain", types, (byte)3, skills, (short)5, (byte)0, (short)60, (short)30, (short)90, "sound.death.lion", "sound.death.lion", "sound.combat.hit.lion", "sound.combat.hit.lion", 0.95F, 3.0F, 0.0F, 5.0F, 0.0F, 0.0F, 1.0F, 1200, new int[] { 92, 305, 313, 308, 308 }, 10, 40, (byte)75);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2651 */     temp.setHandDamString("claw");
/* 2652 */     temp.setKickDamString("claw");
/* 2653 */     temp.setMaxAge(100);
/* 2654 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 2655 */     temp.setBaseCombatRating(3.0F);
/* 2656 */     temp.combatDamageType = 1;
/* 2657 */     temp.setMaxGroupAttackSize(2);
/* 2658 */     temp.setDenName("mountain lion hideout");
/* 2659 */     temp.setDenMaterial((byte)15);
/* 2660 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRatLargeTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2673 */     skills.learnTemp(102, 5.0F);
/* 2674 */     skills.learnTemp(104, 10.0F);
/* 2675 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 2677 */     skills.learnTemp(100, 6.0F);
/* 2678 */     skills.learnTemp(101, 8.0F);
/* 2679 */     skills.learnTemp(105, 15.0F);
/* 2680 */     skills.learnTemp(106, 1.0F);
/* 2681 */     skills.learnTemp(10052, 2.0F);
/* 2682 */     int[] types = { 7, 3, 6, 12, 13, 27, 32, 60, 61 };
/*      */ 
/*      */     
/* 2685 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.rat.large", types, (byte)3, skills, (short)3, (byte)0, (short)20, (short)10, (short)50, "sound.death.rat", "sound.death.rat", "sound.combat.hit.rat", "sound.combat.hit.rat", 1.0F, 1.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.7F, 400, new int[] { 313, 310, 308, 308 }, 10, 40, (byte)78);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2690 */     temp.setHandDamString("claw");
/* 2691 */     temp.setMaxAge(30);
/* 2692 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 2693 */     temp.setBaseCombatRating(2.0F);
/* 2694 */     temp.combatDamageType = 2;
/* 2695 */     temp.setMaxGroupAttackSize(2);
/* 2696 */     temp.setDenName("garbage pile");
/* 2697 */     temp.setSubterranean(true);
/* 2698 */     temp.setMaxPercentOfCreatures(0.03F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDragonRedTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2711 */     skills.learnTemp(102, 80.0F);
/* 2712 */     skills.learnTemp(104, 90.0F);
/* 2713 */     skills.learnTemp(103, 99.0F);
/*      */     
/* 2715 */     skills.learnTemp(100, 60.0F);
/* 2716 */     skills.learnTemp(101, 57.0F);
/* 2717 */     skills.learnTemp(105, 60.0F);
/* 2718 */     skills.learnTemp(106, 30.0F);
/* 2719 */     skills.learnTemp(10052, 95.0F);
/* 2720 */     skills.learnTemp(1023, 80.0F);
/* 2721 */     int[] types = { 20, 19, 41, 7, 6, 13, 16, 32, 29, 12, 40 };
/*      */ 
/*      */     
/* 2724 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.dragon.red", types, (byte)6, skills, (short)20, (byte)0, (short)280, (short)210, (short)666, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.017F, 35.0F, 38.0F, 53.0F, 67.0F, 0.0F, 1.6F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2753 */     temp.setHeadbuttDamString("tailwhip");
/* 2754 */     temp.setKickDamString("wingbuff");
/* 2755 */     temp.setAlignment(-90.0F);
/* 2756 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON);
/* 2757 */     temp.setEggLayer(true);
/* 2758 */     temp.setEggTemplateId(16);
/* 2759 */     temp.setBaseCombatRating(100.0F);
/* 2760 */     temp.combatDamageType = 2;
/* 2761 */     temp.setCombatMoves(new int[] { 1, 2, 3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDragonBlueTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2775 */     skills.learnTemp(102, 80.0F);
/* 2776 */     skills.learnTemp(104, 90.0F);
/* 2777 */     skills.learnTemp(103, 95.0F);
/*      */     
/* 2779 */     skills.learnTemp(100, 56.0F);
/* 2780 */     skills.learnTemp(101, 57.0F);
/* 2781 */     skills.learnTemp(105, 60.0F);
/* 2782 */     skills.learnTemp(106, 30.0F);
/* 2783 */     skills.learnTemp(10052, 90.0F);
/* 2784 */     skills.learnTemp(1023, 80.0F);
/* 2785 */     int[] types = { 20, 19, 41, 7, 6, 13, 16, 32, 29, 12, 40 };
/*      */ 
/*      */     
/* 2788 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.dragon.blue", types, (byte)6, skills, (short)20, (byte)0, (short)280, (short)210, (short)666, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.1F, 35.0F, 38.0F, 50.0F, 63.0F, 0.0F, 1.6F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2817 */     temp.setHeadbuttDamString("tailwhip");
/* 2818 */     temp.setKickDamString("wingbuff");
/* 2819 */     temp.setAlignment(-90.0F);
/* 2820 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON);
/* 2821 */     temp.setEggLayer(true);
/* 2822 */     temp.setEggTemplateId(91);
/* 2823 */     temp.setBaseCombatRating(100.0F);
/* 2824 */     temp.combatDamageType = 2;
/* 2825 */     temp.setCombatMoves(new int[] { 1, 2, 3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDragonGreenTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2839 */     skills.learnTemp(102, 84.0F);
/* 2840 */     skills.learnTemp(104, 90.0F);
/* 2841 */     skills.learnTemp(103, 90.0F);
/*      */     
/* 2843 */     skills.learnTemp(100, 56.0F);
/* 2844 */     skills.learnTemp(101, 57.0F);
/* 2845 */     skills.learnTemp(105, 60.0F);
/* 2846 */     skills.learnTemp(106, 30.0F);
/* 2847 */     skills.learnTemp(10052, 90.0F);
/* 2848 */     skills.learnTemp(1023, 80.0F);
/* 2849 */     int[] types = { 20, 19, 41, 7, 6, 13, 16, 32, 29, 12, 40 };
/*      */ 
/*      */     
/* 2852 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.dragon.green", types, (byte)6, skills, (short)20, (byte)0, (short)280, (short)210, (short)666, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.1F, 35.0F, 38.0F, 50.0F, 58.0F, 0.0F, 1.6F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2881 */     temp.setHeadbuttDamString("tailwhip");
/* 2882 */     temp.setKickDamString("wingbuff");
/* 2883 */     temp.setAlignment(-90.0F);
/* 2884 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON);
/* 2885 */     temp.setEggLayer(true);
/* 2886 */     temp.setEggTemplateId(90);
/* 2887 */     temp.setBaseCombatRating(100.0F);
/* 2888 */     temp.combatDamageType = 2;
/* 2889 */     temp.setCombatMoves(new int[] { 1, 2, 3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDragonBlackTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2903 */     skills.learnTemp(102, 80.0F);
/* 2904 */     skills.learnTemp(104, 90.0F);
/* 2905 */     skills.learnTemp(103, 90.0F);
/*      */     
/* 2907 */     skills.learnTemp(100, 56.0F);
/* 2908 */     skills.learnTemp(101, 57.0F);
/* 2909 */     skills.learnTemp(105, 70.0F);
/* 2910 */     skills.learnTemp(106, 30.0F);
/* 2911 */     skills.learnTemp(10052, 90.0F);
/* 2912 */     skills.learnTemp(1023, 80.0F);
/* 2913 */     int[] types = { 20, 19, 41, 7, 6, 13, 16, 32, 29, 12, 40 };
/*      */ 
/*      */     
/* 2916 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.dragon.black", types, (byte)6, skills, (short)20, (byte)0, (short)280, (short)210, (short)666, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.2F, 35.0F, 38.0F, 58.0F, 62.0F, 0.0F, 1.6F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2945 */     temp.setHeadbuttDamString("tailwhip");
/* 2946 */     temp.setKickDamString("wingbuff");
/* 2947 */     temp.setAlignment(-90.0F);
/* 2948 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON);
/* 2949 */     temp.setEggLayer(true);
/* 2950 */     temp.setEggTemplateId(89);
/* 2951 */     temp.setBaseCombatRating(100.0F);
/* 2952 */     temp.combatDamageType = 2;
/* 2953 */     temp.setCombatMoves(new int[] { 1, 2, 3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDragonWhiteTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 2967 */     skills.learnTemp(102, 80.0F);
/* 2968 */     skills.learnTemp(104, 90.0F);
/* 2969 */     skills.learnTemp(103, 85.0F);
/*      */     
/* 2971 */     skills.learnTemp(100, 56.0F);
/* 2972 */     skills.learnTemp(101, 57.0F);
/* 2973 */     skills.learnTemp(105, 70.0F);
/* 2974 */     skills.learnTemp(106, 30.0F);
/* 2975 */     skills.learnTemp(10052, 90.0F);
/* 2976 */     skills.learnTemp(1023, 80.0F);
/* 2977 */     int[] types = { 20, 19, 41, 7, 6, 13, 16, 32, 29, 12, 40 };
/*      */ 
/*      */     
/* 2980 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.dragon.white", types, (byte)6, skills, (short)20, (byte)0, (short)280, (short)210, (short)666, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.1F, 35.0F, 38.0F, 55.0F, 60.0F, 0.0F, 1.6F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3009 */     temp.setHeadbuttDamString("tailwhip");
/* 3010 */     temp.setKickDamString("wingbuff");
/* 3011 */     temp.setAlignment(-90.0F);
/* 3012 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON);
/* 3013 */     temp.setEggLayer(true);
/* 3014 */     temp.setEggTemplateId(92);
/* 3015 */     temp.setBaseCombatRating(100.0F);
/* 3016 */     temp.combatDamageType = 2;
/* 3017 */     temp.setCombatMoves(new int[] { 1, 2, 3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDrakeRedTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3031 */     skills.learnTemp(102, 50.0F);
/* 3032 */     skills.learnTemp(104, 65.0F);
/* 3033 */     skills.learnTemp(103, 70.0F);
/*      */     
/* 3035 */     skills.learnTemp(100, 27.0F);
/* 3036 */     skills.learnTemp(101, 40.0F);
/* 3037 */     skills.learnTemp(105, 40.0F);
/* 3038 */     skills.learnTemp(106, 29.0F);
/* 3039 */     skills.learnTemp(10052, 80.0F);
/* 3040 */     skills.learnTemp(1023, 80.0F);
/* 3041 */     int[] types = { 20, 19, 41, 7, 12, 6, 13, 16, 32, 29, 40 };
/*      */ 
/*      */     
/* 3044 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.drake.red", types, (byte)6, skills, (short)20, (byte)1, (short)170, (short)100, (short)450, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.15F, 20.0F, 26.0F, 52.0F, 54.0F, 0.0F, 1.8F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3073 */     temp.setHeadbuttDamString("tailwhip");
/* 3074 */     temp.setKickDamString("wingbuff");
/* 3075 */     temp.setAlignment(-80.0F);
/* 3076 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON);
/* 3077 */     temp.setEggLayer(true);
/* 3078 */     temp.setEggTemplateId(103);
/* 3079 */     temp.setBaseCombatRating(95.0F);
/* 3080 */     temp.combatDamageType = 2;
/* 3081 */     temp.setCombatMoves(new int[] { 1, 2, 3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDrakeBlueTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3095 */     skills.learnTemp(102, 55.0F);
/* 3096 */     skills.learnTemp(104, 75.0F);
/* 3097 */     skills.learnTemp(103, 65.0F);
/*      */     
/* 3099 */     skills.learnTemp(100, 27.0F);
/* 3100 */     skills.learnTemp(101, 34.0F);
/* 3101 */     skills.learnTemp(105, 60.0F);
/* 3102 */     skills.learnTemp(106, 29.0F);
/* 3103 */     skills.learnTemp(10052, 80.0F);
/* 3104 */     skills.learnTemp(1023, 80.0F);
/* 3105 */     int[] types = { 20, 19, 41, 7, 12, 6, 13, 16, 32, 29, 40 };
/*      */ 
/*      */     
/* 3108 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.drake.blue", types, (byte)6, skills, (short)20, (byte)1, (short)170, (short)100, (short)450, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.15F, 20.0F, 26.0F, 50.0F, 58.0F, 0.0F, 1.8F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3137 */     temp.setHeadbuttDamString("tailwhip");
/* 3138 */     temp.setKickDamString("wingbuff");
/* 3139 */     temp.setAlignment(-80.0F);
/* 3140 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON);
/* 3141 */     temp.setEggLayer(true);
/* 3142 */     temp.setEggTemplateId(104);
/* 3143 */     temp.setBaseCombatRating(95.0F);
/* 3144 */     temp.combatDamageType = 2;
/* 3145 */     temp.setCombatMoves(new int[] { 1, 2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDrakeWhiteTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3159 */     skills.learnTemp(102, 40.0F);
/* 3160 */     skills.learnTemp(104, 55.0F);
/* 3161 */     skills.learnTemp(103, 60.0F);
/*      */     
/* 3163 */     skills.learnTemp(100, 24.0F);
/* 3164 */     skills.learnTemp(101, 30.0F);
/* 3165 */     skills.learnTemp(105, 35.0F);
/* 3166 */     skills.learnTemp(106, 39.0F);
/* 3167 */     skills.learnTemp(10052, 75.0F);
/* 3168 */     skills.learnTemp(1023, 80.0F);
/* 3169 */     int[] types = { 20, 19, 41, 7, 6, 13, 16, 32, 29, 12, 40 };
/*      */ 
/*      */     
/* 3172 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.drake.white", types, (byte)6, skills, (short)30, (byte)1, (short)150, (short)90, (short)420, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.2F, 21.0F, 24.0F, 53.0F, 57.0F, 45.0F, 1.9F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3201 */     temp.setHeadbuttDamString("tailwhip");
/* 3202 */     temp.setKickDamString("wingbuff");
/* 3203 */     temp.setBreathDamString("burn");
/* 3204 */     temp.setAlignment(-60.0F);
/* 3205 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON);
/* 3206 */     temp.setEggLayer(true);
/* 3207 */     temp.setEggTemplateId(19);
/* 3208 */     temp.setBaseCombatRating(95.0F);
/* 3209 */     temp.combatDamageType = 2;
/* 3210 */     temp.setCombatMoves(new int[] { 1, 2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDrakeGreenTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3224 */     skills.learnTemp(102, 60.0F);
/* 3225 */     skills.learnTemp(104, 65.0F);
/* 3226 */     skills.learnTemp(103, 80.0F);
/*      */     
/* 3228 */     skills.learnTemp(100, 17.0F);
/* 3229 */     skills.learnTemp(101, 27.0F);
/* 3230 */     skills.learnTemp(105, 50.0F);
/* 3231 */     skills.learnTemp(106, 24.0F);
/* 3232 */     skills.learnTemp(10052, 80.0F);
/* 3233 */     skills.learnTemp(1023, 80.0F);
/* 3234 */     int[] types = { 20, 19, 41, 7, 6, 13, 16, 32, 29, 12, 40 };
/*      */ 
/*      */     
/* 3237 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.drake.green", types, (byte)6, skills, (short)20, (byte)0, (short)180, (short)110, (short)480, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.15F, 20.0F, 24.0F, 55.0F, 56.0F, 0.0F, 1.6F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3266 */     temp.setHeadbuttDamString("tailwhip");
/* 3267 */     temp.setKickDamString("wingbuff");
/* 3268 */     temp.setAlignment(-60.0F);
/* 3269 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON);
/* 3270 */     temp.setEggLayer(true);
/* 3271 */     temp.setEggTemplateId(17);
/* 3272 */     temp.setBaseCombatRating(96.0F);
/* 3273 */     temp.combatDamageType = 2;
/* 3274 */     temp.setCombatMoves(new int[] { 1, 2, 5 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDrakeBlackTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3288 */     skills.learnTemp(102, 50.0F);
/* 3289 */     skills.learnTemp(104, 75.0F);
/* 3290 */     skills.learnTemp(103, 70.0F);
/*      */     
/* 3292 */     skills.learnTemp(100, 27.0F);
/* 3293 */     skills.learnTemp(101, 37.0F);
/* 3294 */     skills.learnTemp(105, 55.0F);
/* 3295 */     skills.learnTemp(106, 29.0F);
/* 3296 */     skills.learnTemp(10052, 85.0F);
/* 3297 */     skills.learnTemp(1023, 80.0F);
/* 3298 */     int[] types = { 20, 19, 41, 7, 12, 6, 13, 16, 32, 29, 40 };
/*      */ 
/*      */     
/* 3301 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.drake.black", types, (byte)6, skills, (short)20, (byte)1, (short)170, (short)100, (short)450, "sound.death.dragon", "sound.death.dragon", "sound.combat.hit.dragon", "sound.combat.hit.dragon", 0.12F, 20.0F, 26.0F, 56.0F, 58.0F, 0.0F, 1.8F, 500, new int[] { 867, 868, 303, 308, 308, 310 }, 40, 99, (byte)76);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3330 */     temp.setHeadbuttDamString("tailwhip");
/* 3331 */     temp.setKickDamString("wingbuff");
/* 3332 */     temp.setAlignment(-70.0F);
/* 3333 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON);
/* 3334 */     temp.setEggLayer(true);
/* 3335 */     temp.setEggTemplateId(18);
/* 3336 */     temp.setBaseCombatRating(98.0F);
/* 3337 */     temp.combatDamageType = 2;
/* 3338 */     temp.setCombatMoves(new int[] { 1, 2, 6 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createForestGiantTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3352 */     skills.learnTemp(102, 40.0F);
/* 3353 */     skills.learnTemp(104, 25.0F);
/* 3354 */     skills.learnTemp(103, 60.0F);
/*      */     
/* 3356 */     skills.learnTemp(100, 5.0F);
/* 3357 */     skills.learnTemp(101, 5.0F);
/* 3358 */     skills.learnTemp(105, 60.0F);
/* 3359 */     skills.learnTemp(106, 19.0F);
/* 3360 */     skills.learnTemp(10052, 65.0F);
/* 3361 */     skills.learnTemp(1023, 80.0F);
/* 3362 */     skills.learnTemp(10064, 60.0F);
/* 3363 */     int[] types = { 20, 7, 6, 13, 16, 27, 32, 40, 45 };
/*      */ 
/*      */     
/* 3366 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.giant.forest", types, (byte)0, skills, (short)20, (byte)0, (short)370, (short)100, (short)60, "sound.death.giant", "sound.death.giant", "sound.combat.hit.giant", "sound.combat.hit.giant", 0.02F, 10.0F, 24.0F, 26.0F, 0.0F, 0.0F, 1.5F, 1800, new int[] { 308, 308, 310, 868, 867 }, 40, 99, (byte)87);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3372 */     temp.setAlignment(-20.0F);
/* 3373 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 3374 */     temp.setBaseCombatRating(76.0F);
/* 3375 */     temp.combatDamageType = 0;
/* 3376 */     temp.setCombatMoves(new int[] { 1, 5, 6 });
/*      */     
/* 3378 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCyclopsTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3391 */     skills.learnTemp(102, 80.0F);
/* 3392 */     skills.learnTemp(104, 35.0F);
/* 3393 */     skills.learnTemp(103, 80.0F);
/*      */     
/* 3395 */     skills.learnTemp(100, 7.0F);
/* 3396 */     skills.learnTemp(101, 7.0F);
/* 3397 */     skills.learnTemp(105, 55.0F);
/* 3398 */     skills.learnTemp(106, 29.0F);
/* 3399 */     skills.learnTemp(10052, 80.0F);
/* 3400 */     skills.learnTemp(10064, 90.0F);
/* 3401 */     int[] types = { 30, 20, 7, 6, 13, 16, 27, 32, 40, 45 };
/*      */ 
/*      */     
/* 3404 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.kyklops", types, (byte)5, skills, (short)20, (byte)0, (short)570, (short)200, (short)80, "sound.death.giant", "sound.death.giant", "sound.combat.hit.giant", "sound.combat.hit.giant", 0.015F, 26.0F, 30.0F, 0.0F, 0.0F, 0.0F, 1.8F, 1800, new int[] { 308, 310, 868, 867 }, 40, 99, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3410 */     temp.setAlignment(-10.0F);
/* 3411 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 3412 */     temp.keepSex = true;
/* 3413 */     temp.setBaseCombatRating(86.0F);
/* 3414 */     temp.combatDamageType = 0;
/* 3415 */     temp.setCombatMoves(new int[] { 4, 1, 6 });
/*      */     
/* 3417 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createTrollKingTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3430 */     skills.learnTemp(102, 70.0F);
/* 3431 */     skills.learnTemp(104, 45.0F);
/* 3432 */     skills.learnTemp(103, 70.0F);
/* 3433 */     skills.learnTemp(100, 15.0F);
/* 3434 */     skills.learnTemp(101, 20.0F);
/* 3435 */     skills.learnTemp(105, 45.0F);
/* 3436 */     skills.learnTemp(106, 29.0F);
/* 3437 */     skills.learnTemp(1023, 80.0F);
/* 3438 */     skills.learnTemp(10052, 80.0F);
/* 3439 */     skills.learnTemp(10064, 90.0F);
/* 3440 */     int[] types = { 20, 7, 6, 13, 16, 18, 29, 30, 32, 40, 45 };
/*      */ 
/*      */     
/* 3443 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.troll.king", types, (byte)0, skills, (short)20, (byte)0, (short)270, (short)60, (short)60, "sound.death.troll", "sound.death.troll", "sound.combat.hit.troll", "sound.combat.hit.troll", 0.02F, 20.0F, 0.0F, 26.0F, 0.0F, 0.0F, 1.7F, 200, new int[] { 303, 310, 868, 867 }, 40, 99, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3449 */     temp.setHandDamString("claw");
/* 3450 */     temp.setAlignment(-60.0F);
/* 3451 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_RING);
/* 3452 */     temp.keepSex = true;
/* 3453 */     temp.setBaseCombatRating(86.0F);
/* 3454 */     temp.combatDamageType = 2;
/* 3455 */     temp.setCombatMoves(new int[] { 4, 1 });
/*      */     
/* 3457 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createTrollTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3470 */     skills.learnTemp(102, 40.0F);
/* 3471 */     skills.learnTemp(104, 25.0F);
/* 3472 */     skills.learnTemp(103, 40.0F);
/*      */     
/* 3474 */     skills.learnTemp(100, 8.0F);
/* 3475 */     skills.learnTemp(101, 10.0F);
/* 3476 */     skills.learnTemp(105, 39.0F);
/* 3477 */     skills.learnTemp(106, 7.0F);
/* 3478 */     skills.learnTemp(10052, 40.0F);
/* 3479 */     skills.learnTemp(10064, 70.0F);
/* 3480 */     int[] types = { 7, 6, 40, 13, 16, 18, 29, 30, 32, 36, 39, 45, 60, 61 };
/*      */ 
/*      */ 
/*      */     
/* 3484 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.troll.standard", types, (byte)0, skills, (short)5, (byte)0, (short)230, (short)50, (short)50, "sound.death.troll", "sound.death.troll", "sound.combat.hit.troll", "sound.combat.hit.troll", 0.4F, 8.0F, 4.0F, 12.0F, 0.0F, 0.0F, 1.2F, 1700, new int[] { 303, 310 }, 10, 94, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3490 */     temp.setHandDamString("claw");
/*      */     
/* 3492 */     temp.setLeaderTemplateId(27);
/* 3493 */     temp.setAlignment(-50.0F);
/* 3494 */     temp.setMaxAge(300);
/* 3495 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 3496 */     temp.setBaseCombatRating(12.0F);
/* 3497 */     temp.setBonusCombatRating(5.0F);
/* 3498 */     temp.combatDamageType = 2;
/* 3499 */     temp.setMaxGroupAttackSize(8);
/* 3500 */     temp.setDenName("troll mound");
/* 3501 */     temp.setDenMaterial((byte)15);
/* 3502 */     temp.setSubterranean(true);
/* 3503 */     temp.hasHands = true;
/* 3504 */     temp.setMaxPercentOfCreatures(0.06F);
/*      */     
/* 3506 */     temp.setUsesNewAttacks(true);
/* 3507 */     temp.setBoundsValues(-0.5F, -0.5F, 0.5F, 0.5F);
/*      */     
/* 3509 */     temp.addPrimaryAttack(new AttackAction("maul", AttackIdentifier.MAUL, new AttackValues(7.0F, 0.04F, 6.0F, 3, 2, (byte)0, true, 3, 1.4F)));
/*      */     
/* 3511 */     temp.addPrimaryAttack(new AttackAction("strike", AttackIdentifier.STRIKE, new AttackValues(7.0F, 0.04F, 4.0F, 3, 1, (byte)0, false, 3, 1.4F)));
/*      */ 
/*      */     
/* 3514 */     temp.addSecondaryAttack(new AttackAction("kick", AttackIdentifier.KICK, new AttackValues(4.0F, 0.04F, 5.0F, 3, 1, (byte)0, false, 3, 2.1F)));
/*      */     
/* 3516 */     temp.addSecondaryAttack(new AttackAction("bite", AttackIdentifier.BITE, new AttackValues(10.0F, 0.08F, 7.0F, 3, 1, (byte)3, false, 3, 2.0F)));
/*      */     
/* 3518 */     temp.addSecondaryAttack(new AttackAction("kick", AttackIdentifier.CLAW, new AttackValues(5.0F, 0.1F, 7.0F, 3, 1, (byte)1, false, 3, 1.8F)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGoblinLeaderTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3532 */     skills.learnTemp(102, 30.0F);
/* 3533 */     skills.learnTemp(104, 25.0F);
/* 3534 */     skills.learnTemp(103, 50.0F);
/*      */     
/* 3536 */     skills.learnTemp(100, 19.0F);
/* 3537 */     skills.learnTemp(101, 25.0F);
/* 3538 */     skills.learnTemp(105, 60.0F);
/* 3539 */     skills.learnTemp(106, 19.0F);
/* 3540 */     skills.learnTemp(10052, 60.0F);
/* 3541 */     skills.learnTemp(10027, 90.0F);
/* 3542 */     skills.learnTemp(10006, 90.0F);
/* 3543 */     skills.learnTemp(1023, 80.0F);
/* 3544 */     int[] types = { 20, 7, 6, 13, 16, 29, 30, 32, 40, 45 };
/*      */ 
/*      */     
/* 3547 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.goblin.leader", types, (byte)0, skills, (short)20, (byte)0, (short)150, (short)30, (short)20, "sound.death.goblin", "sound.death.goblin", "sound.combat.hit.goblin", "sound.combat.hit.goblin", 0.14F, 18.0F, 25.0F, 0.0F, 0.0F, 0.0F, 1.5F, 1200, new int[] { 303, 868, 867 }, 40, 99, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3553 */     temp.setHandDamString("claw");
/* 3554 */     temp.setAlignment(-50.0F);
/* 3555 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 3556 */     temp.keepSex = true;
/* 3557 */     temp.setBaseCombatRating(68.0F);
/* 3558 */     temp.combatDamageType = 2;
/* 3559 */     temp.setCombatMoves(new int[] { 4 });
/*      */     
/* 3561 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGoblinTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3574 */     skills.learnTemp(102, 20.0F);
/* 3575 */     skills.learnTemp(104, 15.0F);
/* 3576 */     skills.learnTemp(103, 20.0F);
/*      */     
/* 3578 */     skills.learnTemp(100, 12.0F);
/* 3579 */     skills.learnTemp(101, 15.0F);
/* 3580 */     skills.learnTemp(105, 26.0F);
/* 3581 */     skills.learnTemp(106, 7.0F);
/* 3582 */     skills.learnTemp(10052, 14.0F);
/* 3583 */     int[] types = { 7, 6, 13, 16, 29, 30, 32, 34, 45, 60, 61 };
/*      */ 
/*      */     
/* 3586 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.goblin.standard", types, (byte)0, skills, (short)5, (byte)0, (short)130, (short)30, (short)20, "sound.death.goblin", "sound.death.goblin", "sound.combat.hit.goblin", "sound.combat.hit.goblin", 0.7F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.7F, 1500, new int[] { 1250 }, 10, 94, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3591 */     temp.setHandDamString("claw");
/* 3592 */     temp.setLeaderTemplateId(26);
/* 3593 */     temp.setAlignment(-40.0F);
/* 3594 */     temp.setMaxAge(100);
/* 3595 */     temp.setBaseCombatRating(6.0F);
/* 3596 */     temp.combatDamageType = 2;
/* 3597 */     temp.setMaxGroupAttackSize(2);
/* 3598 */     temp.setDenName("goblin hut");
/* 3599 */     temp.hasHands = true;
/* 3600 */     temp.setMaxPercentOfCreatures(0.06F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createScorpionTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3613 */     skills.learnTemp(102, 20.0F);
/* 3614 */     skills.learnTemp(104, 15.0F);
/* 3615 */     skills.learnTemp(103, 20.0F);
/*      */     
/* 3617 */     skills.learnTemp(100, 2.0F);
/* 3618 */     skills.learnTemp(101, 25.0F);
/* 3619 */     skills.learnTemp(105, 40.0F);
/* 3620 */     skills.learnTemp(106, 2.0F);
/* 3621 */     skills.learnTemp(10052, 24.0F);
/* 3622 */     int[] types = { 7, 41, 6, 13, 16, 29, 32, 34, 39, 60, 61 };
/*      */ 
/*      */     
/* 3625 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.multiped.scorpion", types, (byte)8, skills, (short)5, (byte)0, (short)130, (short)30, (short)20, "sound.death.insect", "sound.death.insect", "sound.combat.hit.insect", "sound.combat.hit.insect", 0.4F, 6.0F, 10.0F, 13.0F, 0.0F, 0.0F, 0.75F, 1700, new int[] { 92, 439 }, 7, 64, (byte)82);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3631 */     temp.setHandDamString("claw");
/* 3632 */     temp.setBreathDamString("sting");
/* 3633 */     temp.setAlignment(-40.0F);
/* 3634 */     temp.setMaxAge(100);
/* 3635 */     temp.setBaseCombatRating(8.0F);
/* 3636 */     temp.setBonusCombatRating(8.0F);
/* 3637 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 3638 */     temp.combatDamageType = 2;
/* 3639 */     temp.setMaxGroupAttackSize(6);
/* 3640 */     temp.setDenName("scorpion stone");
/* 3641 */     temp.setDenMaterial((byte)15);
/* 3642 */     temp.setMaxPercentOfCreatures(0.05F);
/* 3643 */     temp.setNoServerSounds(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createUnicornTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3656 */     skills.learnTemp(102, 30.0F);
/* 3657 */     skills.learnTemp(104, 30.0F);
/* 3658 */     skills.learnTemp(103, 20.0F);
/*      */     
/* 3660 */     skills.learnTemp(100, 14.0F);
/* 3661 */     skills.learnTemp(101, 14.0F);
/* 3662 */     skills.learnTemp(105, 60.0F);
/* 3663 */     skills.learnTemp(106, 14.0F);
/* 3664 */     skills.learnTemp(10052, 35.0F);
/* 3665 */     int[] types = { 7, 41, 3, 32, 28, 9, 39, 35 };
/*      */ 
/*      */     
/* 3668 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.unicorn", types, (byte)1, skills, (short)5, (byte)0, (short)180, (short)50, (short)250, "sound.death.horse", "sound.death.horse", "sound.combat.hit.horse", "sound.combat.hit.horse", 0.7F, 6.0F, 10.0F, 8.0F, 0.0F, 0.0F, 1.6F, 1500, new int[] { 92, 311, 71 }, 10, 60, (byte)79);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3674 */     temp.setAlignment(100.0F);
/* 3675 */     temp.setHandDamString("kick");
/* 3676 */     temp.setMaxAge(400);
/* 3677 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 3678 */     temp.setBaseCombatRating(11.0F);
/* 3679 */     temp.combatDamageType = 0;
/* 3680 */     temp.setMaxGroupAttackSize(4);
/* 3681 */     temp.setDenName("unicorn rustle");
/* 3682 */     temp.setMaxPercentOfCreatures(0.02F);
/* 3683 */     temp.setChildTemplateId(118);
/*      */     
/* 3685 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 3686 */       temp.setVision((short)6);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createUnicornFoalTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3699 */     skills.learnTemp(102, 15.0F);
/* 3700 */     skills.learnTemp(104, 15.0F);
/* 3701 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 3703 */     skills.learnTemp(100, 6.0F);
/* 3704 */     skills.learnTemp(101, 5.0F);
/* 3705 */     skills.learnTemp(105, 30.0F);
/* 3706 */     skills.learnTemp(106, 10.0F);
/* 3707 */     skills.learnTemp(10052, 15.0F);
/* 3708 */     int[] types = { 7, 3, 9, 28, 32, 63, 35, 39 };
/*      */     
/* 3710 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.unicorn.foal", types, (byte)1, skills, (short)3, (byte)0, (short)100, (short)50, (short)75, "sound.death.horse", "sound.death.horse", "sound.combat.hit.horse", "sound.combat.hit.horse", 0.9F, 2.0F, 4.0F, 3.0F, 0.0F, 0.0F, 1.2F, 900, new int[] { 311, 71, 309 }, 5, 20, (byte)79);
/*      */ 
/*      */ 
/*      */     
/* 3714 */     temp.setAlignment(100.0F);
/* 3715 */     temp.setHandDamString("kick");
/* 3716 */     temp.setMaxAge(100);
/* 3717 */     temp.setAdultFemaleTemplateId(21);
/* 3718 */     temp.setAdultMaleTemplateId(21);
/* 3719 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 3720 */     temp.setBaseCombatRating(5.0F);
/* 3721 */     temp.setMaxGroupAttackSize(2);
/* 3722 */     temp.combatDamageType = 0;
/* 3723 */     temp.setCorpseName("unicorn.foal.");
/*      */     
/* 3725 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 3726 */       temp.setVision((short)3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createBearBlackTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3739 */     skills.learnTemp(102, 26.0F);
/* 3740 */     skills.learnTemp(104, 26.0F);
/* 3741 */     skills.learnTemp(103, 26.0F);
/*      */     
/* 3743 */     skills.learnTemp(100, 4.0F);
/* 3744 */     skills.learnTemp(101, 4.0F);
/* 3745 */     skills.learnTemp(105, 30.0F);
/* 3746 */     skills.learnTemp(106, 4.0F);
/* 3747 */     skills.learnTemp(10052, 30.0F);
/* 3748 */     int[] types = { 7, 3, 6, 13, 29, 32, 39, 60, 61 };
/*      */ 
/*      */     
/* 3751 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.bear.black", types, (byte)2, skills, (short)5, (byte)0, (short)160, (short)50, (short)50, "sound.death.bear", "sound.death.bear", "sound.combat.hit.bear", "sound.combat.hit.bear", 0.8F, 4.0F, 0.0F, 11.0F, 0.0F, 0.0F, 1.0F, 1500, new int[] { 303, 302 }, 10, 80, (byte)72);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3757 */     temp.setHandDamString("maul");
/* 3758 */     temp.setMaxAge(200);
/* 3759 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 3760 */     temp.setBaseCombatRating(9.0F);
/* 3761 */     temp.combatDamageType = 0;
/* 3762 */     temp.setMaxGroupAttackSize(6);
/* 3763 */     temp.setDenName("bear cave");
/* 3764 */     temp.setDenMaterial((byte)15);
/* 3765 */     temp.setMaxPercentOfCreatures(0.05F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createBearBrownTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3778 */     skills.learnTemp(102, 30.0F);
/* 3779 */     skills.learnTemp(104, 30.0F);
/* 3780 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 3782 */     skills.learnTemp(100, 4.0F);
/* 3783 */     skills.learnTemp(101, 4.0F);
/* 3784 */     skills.learnTemp(105, 30.0F);
/* 3785 */     skills.learnTemp(106, 4.0F);
/* 3786 */     skills.learnTemp(10052, 40.0F);
/* 3787 */     int[] types = { 8, 41, 3, 6, 12, 13, 32, 29, 39, 60, 61 };
/*      */ 
/*      */     
/* 3790 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.bear.brown", types, (byte)2, skills, (short)5, (byte)0, (short)230, (short)50, (short)50, "sound.death.bear", "sound.death.bear", "sound.combat.hit.bear", "sound.combat.hit.bear", 0.75F, 7.0F, 0.0F, 10.0F, 0.0F, 0.0F, 1.2F, 1500, new int[] { 92, 303, 302 }, 10, 70, (byte)72);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3797 */     temp.setBoundsValues(-0.5F, -1.0F, 0.5F, 1.42F);
/*      */     
/* 3799 */     temp.setHandDamString("maul");
/* 3800 */     temp.setMaxAge(200);
/* 3801 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 3802 */     temp.setBaseCombatRating(9.0F);
/* 3803 */     temp.combatDamageType = 0;
/* 3804 */     temp.setMaxGroupAttackSize(4);
/* 3805 */     temp.setDenName("bear cave");
/* 3806 */     temp.setDenMaterial((byte)15);
/* 3807 */     temp.setMaxPercentOfCreatures(0.04F);
/* 3808 */     temp.setUsesNewAttacks(true);
/* 3809 */     temp.addPrimaryAttack(new AttackAction("maul", AttackIdentifier.STRIKE, new AttackValues(7.0F, 0.01F, 6.0F, 3, 1, (byte)0, false, 2, 1.0F)));
/*      */     
/* 3811 */     temp.addPrimaryAttack(new AttackAction("gnaw", AttackIdentifier.BITE, new AttackValues(5.0F, 0.02F, 8.0F, 3, 1, (byte)3, false, 4, 1.1F)));
/*      */ 
/*      */     
/* 3814 */     temp.addSecondaryAttack(new AttackAction("bite", AttackIdentifier.BITE, new AttackValues(10.0F, 0.05F, 6.0F, 2, 1, (byte)3, false, 3, 1.1F)));
/*      */     
/* 3816 */     temp.addSecondaryAttack(new AttackAction("scratch", AttackIdentifier.STRIKE, new AttackValues(7.0F, 0.05F, 6.0F, 2, 1, (byte)1, false, 8, 1.0F)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createEasterBunnyTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3830 */     skills.learnTemp(102, 5.0F);
/* 3831 */     skills.learnTemp(104, 25.0F);
/* 3832 */     skills.learnTemp(103, 70.0F);
/*      */     
/* 3834 */     skills.learnTemp(100, 3.0F);
/* 3835 */     skills.learnTemp(101, 6.0F);
/* 3836 */     skills.learnTemp(105, 20.0F);
/* 3837 */     skills.learnTemp(106, 10.0F);
/* 3838 */     skills.learnTemp(10052, 40.0F);
/* 3839 */     int[] types = { 8, 3, 4, 30, 35 };
/*      */     
/* 3841 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.easterbunny", types, (byte)3, skills, (short)5, (byte)0, (short)20, (short)30, (short)50, "sound.death.wolf", "sound.death.wolf", "sound.combat.hit.wolf", "sound.combat.hit.wolf", 0.85F, 11.0F, 0.0F, 9.0F, 0.0F, 0.0F, 2.0F, 1900, new int[] { 92, 305, 466 }, 0, 0, (byte)78);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3847 */     temp.setHandDamString("claw");
/* 3848 */     temp.setKickDamString("claw");
/* 3849 */     temp.setMaxAge(3);
/* 3850 */     temp.setEggLayer(true);
/* 3851 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 3852 */     temp.setBaseCombatRating(50.0F);
/* 3853 */     temp.combatDamageType = 2;
/* 3854 */     temp.setMaxGroupAttackSize(15);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCrocodileTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3867 */     skills.learnTemp(102, 35.0F);
/* 3868 */     skills.learnTemp(104, 35.0F);
/* 3869 */     skills.learnTemp(103, 20.0F);
/*      */     
/* 3871 */     skills.learnTemp(100, 6.0F);
/* 3872 */     skills.learnTemp(101, 12.0F);
/* 3873 */     skills.learnTemp(105, 65.0F);
/* 3874 */     skills.learnTemp(106, 1.0F);
/* 3875 */     skills.learnTemp(10052, 50.0F);
/* 3876 */     int[] types = { 6, 41, 7, 3, 13, 32, 29, 12, 39, 60, 61 };
/*      */ 
/*      */     
/* 3879 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.crocodile", types, (byte)3, skills, (short)5, (byte)0, (short)80, (short)30, (short)120, "sound.death.croc", "sound.death.croc", "sound.combat.hit.croc", "sound.combat.hit.croc", 0.35F, 6.0F, 0.0F, 10.0F, 0.0F, 0.0F, 1.2F, 400, new int[] { 92, 305, 71, 310 }, 6, 40, (byte)78);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3885 */     temp.setHandDamString("claw");
/* 3886 */     temp.setKickDamString("claw");
/* 3887 */     temp.setMaxAge(200);
/* 3888 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 3889 */     temp.setBaseCombatRating(9.0F);
/* 3890 */     temp.setBonusCombatRating(7.0F);
/* 3891 */     temp.combatDamageType = 2;
/* 3892 */     temp.setMaxGroupAttackSize(4);
/* 3893 */     temp.setDenName("crocodile lair");
/* 3894 */     temp.setDenMaterial((byte)15);
/* 3895 */     temp.setMaxPercentOfCreatures(0.04F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDogTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3908 */     skills.learnTemp(102, 15.0F);
/* 3909 */     skills.learnTemp(104, 25.0F);
/* 3910 */     skills.learnTemp(103, 20.0F);
/*      */     
/* 3912 */     skills.learnTemp(100, 6.0F);
/* 3913 */     skills.learnTemp(101, 7.0F);
/* 3914 */     skills.learnTemp(105, 15.0F);
/* 3915 */     skills.learnTemp(106, 1.0F);
/* 3916 */     skills.learnTemp(10052, 7.0F);
/* 3917 */     int[] types = { 8, 3, 13, 43, 32, 27, 12 };
/*      */ 
/*      */     
/* 3920 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.dog", types, (byte)3, skills, (short)5, (byte)0, (short)80, (short)30, (short)120, "sound.death.dog", "sound.death.dog", "sound.combat.hit.dog", "sound.combat.hit.dog", 0.95F, 2.0F, 0.0F, 3.0F, 0.0F, 0.0F, 1.2F, 100, new int[] { 92, 140, 305, 313 }, 20, 10, (byte)74);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3927 */     temp.setHandDamString("claw");
/* 3928 */     temp.setKickDamString("claw");
/* 3929 */     temp.setMaxAge(70);
/* 3930 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 3931 */     temp.setBaseCombatRating(3.0F);
/* 3932 */     temp.combatDamageType = 2;
/* 3933 */     temp.setMaxGroupAttackSize(2);
/* 3934 */     temp.setDenMaterial((byte)15);
/* 3935 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createBlackWolfTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3948 */     skills.learnTemp(102, 20.0F);
/* 3949 */     skills.learnTemp(104, 25.0F);
/* 3950 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 3952 */     skills.learnTemp(100, 6.0F);
/* 3953 */     skills.learnTemp(101, 7.0F);
/* 3954 */     skills.learnTemp(105, 20.0F);
/* 3955 */     skills.learnTemp(106, 1.0F);
/* 3956 */     skills.learnTemp(10052, 10.0F);
/* 3957 */     int[] types = { 8, 3, 6, 13, 32, 29, 60, 61 };
/*      */ 
/*      */     
/* 3960 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.wolf.black", types, (byte)3, skills, (short)5, (byte)0, (short)80, (short)30, (short)150, "sound.death.wolf", "sound.death.wolf", "sound.combat.hit.wolf", "sound.combat.hit.wolf", 0.85F, 3.0F, 0.0F, 5.0F, 0.0F, 0.0F, 1.2F, 1500, new int[] { 140, 92, 305, 302 }, 20, 60, (byte)74);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3966 */     temp.setHandDamString("claw");
/* 3967 */     temp.setKickDamString("claw");
/* 3968 */     temp.setMaxAge(70);
/* 3969 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 3970 */     temp.setBaseCombatRating(6.0F);
/* 3971 */     temp.combatDamageType = 2;
/* 3972 */     temp.setMaxGroupAttackSize(3);
/* 3973 */     temp.setDenName("wolf den");
/* 3974 */     temp.setDenMaterial((byte)15);
/* 3975 */     temp.setMaxPercentOfCreatures(0.08F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardSpiritGoodLenientTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 3988 */     skills.learnTemp(102, 30.0F);
/* 3989 */     skills.learnTemp(104, 30.0F);
/* 3990 */     skills.learnTemp(103, 30.0F);
/* 3991 */     skills.learnTemp(100, 15.0F);
/* 3992 */     skills.learnTemp(101, 15.0F);
/* 3993 */     skills.learnTemp(105, 15.0F);
/* 3994 */     skills.learnTemp(106, 17.0F);
/* 3995 */     skills.learnTemp(10052, 30.0F);
/* 3996 */     int[] types = { 22, 23, 12, 13 };
/*      */     
/* 3998 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.spirit.guard", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.spirit.male", "sound.death.spirit.female", "sound.combat.hit.spirit.male", "sound.combat.hit.spirit.female", 0.4F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F, 1.5F, 100, new int[0], 100, 100, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4004 */     temp.setAlignment(40.0F);
/* 4005 */     temp.setBaseCombatRating(6.0F);
/* 4006 */     temp.combatDamageType = 2;
/* 4007 */     temp.setMaxGroupAttackSize(4);
/* 4008 */     temp.setNoSkillgain(true);
/* 4009 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardSpiritEvilLenientTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4022 */     skills.learnTemp(102, 30.0F);
/* 4023 */     skills.learnTemp(104, 30.0F);
/* 4024 */     skills.learnTemp(103, 30.0F);
/* 4025 */     skills.learnTemp(100, 15.0F);
/* 4026 */     skills.learnTemp(101, 15.0F);
/* 4027 */     skills.learnTemp(105, 15.0F);
/* 4028 */     skills.learnTemp(106, 17.0F);
/* 4029 */     skills.learnTemp(10052, 30.0F);
/* 4030 */     int[] types = { 22, 23, 12, 13 };
/*      */     
/* 4032 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.spirit.sentry", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.spirit.male", "sound.death.spirit.female", "sound.combat.hit.spirit.male", "sound.combat.hit.spirit.female", 0.4F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F, 1.5F, 100, new int[0], 100, 100, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4038 */     temp.setAlignment(-40.0F);
/* 4039 */     temp.setBaseCombatRating(6.0F);
/* 4040 */     temp.combatDamageType = 2;
/* 4041 */     temp.setMaxGroupAttackSize(4);
/* 4042 */     temp.setNoSkillgain(true);
/* 4043 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createWraithTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4056 */     skills.learnTemp(102, 20.0F);
/* 4057 */     skills.learnTemp(104, 40.0F);
/* 4058 */     skills.learnTemp(103, 23.0F);
/*      */     
/* 4060 */     skills.learnTemp(100, 18.0F);
/* 4061 */     skills.learnTemp(101, 21.0F);
/* 4062 */     skills.learnTemp(105, 19.0F);
/* 4063 */     skills.learnTemp(106, 17.0F);
/*      */     
/* 4065 */     skills.learnTemp(10052, 50.0F);
/* 4066 */     int[] types = { 22, 13, 24 };
/*      */     
/* 4068 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.spirit.wraith", types, (byte)0, skills, (short)25, (byte)0, (short)180, (short)20, (short)35, "sound.death.spirit.male", "sound.death.spirit.female", "sound.combat.hit.spirit.male", "sound.combat.hit.spirit.female", 0.3F, 4.0F, 0.0F, 5.0F, 0.0F, 0.0F, 1.5F, 100, new int[0], 100, 100, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4075 */     temp.setAlignment(-80.0F);
/* 4076 */     temp.setBaseCombatRating(24.0F);
/* 4077 */     temp.combatDamageType = 8;
/* 4078 */     temp.setMaxGroupAttackSize(4);
/* 4079 */     temp.hasHands = true;
/* 4080 */     temp.physicalResistance = 0.3F;
/* 4081 */     temp.fireVulnerability = 2.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardSpiritAbleTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4095 */     skills.learnTemp(102, 30.0F);
/* 4096 */     skills.learnTemp(104, 30.0F);
/* 4097 */     skills.learnTemp(103, 33.0F);
/*      */     
/* 4099 */     skills.learnTemp(100, 18.0F);
/* 4100 */     skills.learnTemp(101, 21.0F);
/* 4101 */     skills.learnTemp(105, 19.0F);
/* 4102 */     skills.learnTemp(106, 17.0F);
/*      */     
/* 4104 */     skills.learnTemp(10052, 50.0F);
/* 4105 */     int[] types = { 22, 23, 12, 13 };
/*      */     
/* 4107 */     String model = "model.creature.humanoid.human.spirit.avenger";
/* 4108 */     if (id == 31)
/* 4109 */       model = "model.creature.humanoid.human.spirit.brute"; 
/* 4110 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, model, types, (byte)0, skills, (short)25, (byte)0, (short)180, (short)20, (short)35, "sound.death.spirit.male", "sound.death.spirit.female", "sound.combat.hit.spirit.male", "sound.combat.hit.spirit.female", 0.3F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F, 1.5F, 100, new int[0], 100, 100, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4115 */     if (id == 31) {
/* 4116 */       temp.setAlignment(-50.0F);
/*      */     } else {
/* 4118 */       temp.setAlignment(50.0F);
/* 4119 */     }  temp.setBaseCombatRating(8.0F);
/* 4120 */     temp.combatDamageType = 1;
/* 4121 */     temp.setMaxGroupAttackSize(4);
/* 4122 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardSpiritEvilDangerousTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4135 */     skills.learnTemp(102, 30.0F);
/* 4136 */     skills.learnTemp(104, 30.0F);
/* 4137 */     skills.learnTemp(103, 35.0F);
/* 4138 */     skills.learnTemp(100, 17.0F);
/* 4139 */     skills.learnTemp(101, 27.0F);
/* 4140 */     skills.learnTemp(105, 24.0F);
/* 4141 */     skills.learnTemp(106, 24.0F);
/* 4142 */     skills.learnTemp(10052, 80.0F);
/* 4143 */     int[] types = { 22, 23, 12, 13 };
/*      */     
/* 4145 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.spirit.shadow", types, (byte)0, skills, (short)45, (byte)0, (short)180, (short)20, (short)35, "sound.death.spirit.male", "sound.death.spirit.female", "sound.combat.hit.spirit.male", "sound.combat.hit.spirit.female", 0.3F, 5.0F, 7.0F, 5.0F, 0.0F, 0.0F, 1.5F, 100, new int[0], 100, 100, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4150 */     temp.setHandDamString("claw");
/* 4151 */     temp.setKickDamString("claw");
/* 4152 */     temp.setAlignment(-70.0F);
/* 4153 */     temp.setBaseCombatRating(Servers.localServer.isChallengeOrEpicServer() ? 25.0F : 20.0F);
/* 4154 */     temp.combatDamageType = 1;
/* 4155 */     temp.setMaxGroupAttackSize(Servers.localServer.isChallengeOrEpicServer() ? 4 : 6);
/* 4156 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardSpiritGoodDangerousTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4169 */     skills.learnTemp(102, 30.0F);
/* 4170 */     skills.learnTemp(104, 30.0F);
/* 4171 */     skills.learnTemp(103, 35.0F);
/*      */     
/* 4173 */     skills.learnTemp(100, 17.0F);
/* 4174 */     skills.learnTemp(101, 27.0F);
/* 4175 */     skills.learnTemp(105, 24.0F);
/* 4176 */     skills.learnTemp(106, 24.0F);
/* 4177 */     skills.learnTemp(10052, 80.0F);
/* 4178 */     int[] types = { 22, 23, 12, 13 };
/*      */     
/* 4180 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.spirit.templar", types, (byte)0, skills, (short)45, (byte)0, (short)180, (short)20, (short)35, "sound.death.spirit.male", "sound.death.spirit.female", "sound.combat.hit.spirit.male", "sound.combat.hit.spirit.female", 0.3F, 5.0F, 7.0F, 5.0F, 0.0F, 0.0F, 1.5F, 100, new int[0], 100, 100, (byte)2);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4185 */     temp.setAlignment(70.0F);
/* 4186 */     temp.setBaseCombatRating(Servers.localServer.isChallengeOrEpicServer() ? 25.0F : 20.0F);
/* 4187 */     temp.combatDamageType = 1;
/* 4188 */     temp.setMaxGroupAttackSize(Servers.localServer.isChallengeOrEpicServer() ? 4 : 6);
/* 4189 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardBrutalTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4202 */     skills.learnTemp(102, 30.0F);
/* 4203 */     skills.learnTemp(104, 30.0F);
/* 4204 */     skills.learnTemp(103, 36.0F);
/*      */     
/* 4206 */     skills.learnTemp(100, 30.0F);
/* 4207 */     skills.learnTemp(101, 30.0F);
/* 4208 */     skills.learnTemp(105, 30.0F);
/* 4209 */     skills.learnTemp(106, 30.0F);
/* 4210 */     skills.learnTemp(10005, 90.0F);
/* 4211 */     skills.learnTemp(10028, 80.0F);
/* 4212 */     skills.learnTemp(10025, 80.0F);
/* 4213 */     skills.learnTemp(10001, 80.0F);
/* 4214 */     skills.learnTemp(10024, 80.0F);
/* 4215 */     skills.learnTemp(10023, 80.0F);
/* 4216 */     skills.learnTemp(10021, 80.0F);
/* 4217 */     skills.learnTemp(10020, 80.0F);
/*      */     
/* 4219 */     skills.learnTemp(10006, 90.0F);
/* 4220 */     skills.learnTemp(10052, 90.0F);
/* 4221 */     int[] types = { 12, 13, 17, 24, 45, 40, 21, 53, 7 };
/*      */ 
/*      */     
/* 4224 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.guard.tower", types, (byte)0, skills, (short)10, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 0.2F, 5.0F, 7.0F, 0.0F, 0.0F, 0.0F, 1.0F, 100, new int[0], 100, 100, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4229 */     temp.setBaseCombatRating(23.0F);
/* 4230 */     temp.setMaxGroupAttackSize(6);
/* 4231 */     temp.setNoSkillgain(true);
/* 4232 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardToughTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4245 */     skills.learnTemp(102, 24.0F);
/* 4246 */     skills.learnTemp(104, 22.0F);
/* 4247 */     skills.learnTemp(103, 31.0F);
/*      */     
/* 4249 */     skills.learnTemp(100, 18.0F);
/* 4250 */     skills.learnTemp(101, 21.0F);
/* 4251 */     skills.learnTemp(105, 19.0F);
/* 4252 */     skills.learnTemp(106, 17.0F);
/* 4253 */     skills.learnTemp(10005, 90.0F);
/* 4254 */     skills.learnTemp(10028, 60.0F);
/* 4255 */     skills.learnTemp(10025, 60.0F);
/* 4256 */     skills.learnTemp(10001, 60.0F);
/* 4257 */     skills.learnTemp(10024, 60.0F);
/* 4258 */     skills.learnTemp(10023, 60.0F);
/* 4259 */     skills.learnTemp(10021, 60.0F);
/* 4260 */     skills.learnTemp(10020, 60.0F);
/*      */     
/* 4262 */     skills.learnTemp(10006, 60.0F);
/* 4263 */     skills.learnTemp(10052, 90.0F);
/* 4264 */     int[] types = { 12, 13, 17, 24, 45, 40, 53, 7 };
/*      */ 
/*      */     
/* 4267 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.guard.tower", types, (byte)0, skills, (short)10, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 0.3F, 5.0F, 7.0F, 0.0F, 0.0F, 0.0F, 1.0F, 100, new int[0], 70, 100, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4272 */     temp.setBaseCombatRating(20.0F);
/* 4273 */     temp.setMaxGroupAttackSize(4);
/* 4274 */     temp.setNoSkillgain(true);
/* 4275 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardAbleTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4288 */     skills.learnTemp(102, 22.0F);
/* 4289 */     skills.learnTemp(104, 20.0F);
/* 4290 */     skills.learnTemp(103, 26.0F);
/*      */     
/* 4292 */     skills.learnTemp(100, 18.0F);
/* 4293 */     skills.learnTemp(101, 18.0F);
/* 4294 */     skills.learnTemp(105, 19.0F);
/* 4295 */     skills.learnTemp(106, 17.0F);
/* 4296 */     skills.learnTemp(10005, 70.0F);
/* 4297 */     skills.learnTemp(10028, 60.0F);
/* 4298 */     skills.learnTemp(10025, 60.0F);
/* 4299 */     skills.learnTemp(10001, 60.0F);
/* 4300 */     skills.learnTemp(10024, 60.0F);
/* 4301 */     skills.learnTemp(10023, 60.0F);
/* 4302 */     skills.learnTemp(10021, 60.0F);
/* 4303 */     skills.learnTemp(10020, 60.0F);
/*      */     
/* 4305 */     skills.learnTemp(10006, 60.0F);
/* 4306 */     skills.learnTemp(10052, 75.0F);
/* 4307 */     int[] types = { 11, 12, 13, 17, 45, 40, 53, 7 };
/*      */ 
/*      */     
/* 4310 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.guard.tower", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 0.4F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F, 1.0F, 100, new int[0], 60, 100, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4315 */     temp.setBaseCombatRating(99.0F);
/* 4316 */     temp.setMaxGroupAttackSize(4);
/* 4317 */     temp.setNoSkillgain(true);
/* 4318 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardDecentTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4331 */     skills.learnTemp(102, 20.0F);
/* 4332 */     skills.learnTemp(104, 17.0F);
/* 4333 */     skills.learnTemp(103, 21.0F);
/*      */     
/* 4335 */     skills.learnTemp(100, 15.0F);
/* 4336 */     skills.learnTemp(101, 15.0F);
/* 4337 */     skills.learnTemp(105, 15.0F);
/* 4338 */     skills.learnTemp(106, 17.0F);
/* 4339 */     skills.learnTemp(10005, 45.0F);
/* 4340 */     skills.learnTemp(10028, 45.0F);
/* 4341 */     skills.learnTemp(10025, 45.0F);
/* 4342 */     skills.learnTemp(10001, 45.0F);
/* 4343 */     skills.learnTemp(10024, 45.0F);
/* 4344 */     skills.learnTemp(10023, 45.0F);
/* 4345 */     skills.learnTemp(10021, 45.0F);
/* 4346 */     skills.learnTemp(10020, 45.0F);
/*      */     
/* 4348 */     skills.learnTemp(10006, 45.0F);
/* 4349 */     skills.learnTemp(10052, 45.0F);
/* 4350 */     int[] types = { 11, 12, 13, 17, 45, 40, 53, 7 };
/*      */ 
/*      */     
/* 4353 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.guardDecent", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 0.5F, 3.0F, 4.0F, 0.0F, 0.0F, 0.0F, 1.0F, 100, new int[0], 50, 100, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4358 */     temp.setBaseCombatRating(99.0F);
/* 4359 */     temp.setMaxGroupAttackSize(4);
/* 4360 */     temp.setNoSkillgain(true);
/* 4361 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuardLenientTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4374 */     skills.learnTemp(102, 17.0F);
/* 4375 */     skills.learnTemp(104, 17.0F);
/* 4376 */     skills.learnTemp(103, 18.0F);
/*      */     
/* 4378 */     skills.learnTemp(100, 15.0F);
/* 4379 */     skills.learnTemp(101, 15.0F);
/* 4380 */     skills.learnTemp(105, 15.0F);
/* 4381 */     skills.learnTemp(106, 15.0F);
/* 4382 */     skills.learnTemp(10005, 40.0F);
/*      */     
/* 4384 */     skills.learnTemp(10028, 40.0F);
/* 4385 */     skills.learnTemp(10025, 40.0F);
/* 4386 */     skills.learnTemp(10001, 40.0F);
/* 4387 */     skills.learnTemp(10024, 40.0F);
/* 4388 */     skills.learnTemp(10023, 40.0F);
/* 4389 */     skills.learnTemp(10021, 40.0F);
/* 4390 */     skills.learnTemp(10020, 40.0F);
/*      */     
/* 4392 */     skills.learnTemp(10006, 40.0F);
/* 4393 */     skills.learnTemp(10052, 40.0F);
/* 4394 */     int[] types = { 11, 12, 13, 17, 45, 40, 53, 7 };
/*      */ 
/*      */     
/* 4397 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.guardLenient", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 0.6F, 4.0F, 5.0F, 0.0F, 0.0F, 0.0F, 1.0F, 100, new int[0], 40, 100, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4402 */     temp.setMaxGroupAttackSize(4);
/* 4403 */     temp.setBaseCombatRating(99.0F);
/* 4404 */     temp.setNoSkillgain(true);
/* 4405 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuideHotsTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4418 */     skills.learnTemp(102, 15.0F);
/* 4419 */     skills.learnTemp(104, 15.0F);
/* 4420 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 4422 */     skills.learnTemp(100, 10.0F);
/* 4423 */     skills.learnTemp(101, 10.0F);
/* 4424 */     skills.learnTemp(105, 99.0F);
/* 4425 */     skills.learnTemp(106, 24.0F);
/* 4426 */     skills.learnTemp(10052, 40.0F);
/* 4427 */     int[] types = { 0, 4, 17 };
/*      */     
/* 4429 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.guide", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.8F, 0, new int[0], 3, 0, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4434 */     temp.keepSex = true;
/* 4435 */     temp.setBaseCombatRating(99.0F);
/* 4436 */     temp.setMaxGroupAttackSize(4);
/* 4437 */     temp.combatDamageType = 1;
/* 4438 */     temp.setTutorial(true);
/* 4439 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createGuideTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4452 */     skills.learnTemp(102, 15.0F);
/* 4453 */     skills.learnTemp(104, 15.0F);
/* 4454 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 4456 */     skills.learnTemp(100, 10.0F);
/* 4457 */     skills.learnTemp(101, 10.0F);
/* 4458 */     skills.learnTemp(105, 99.0F);
/* 4459 */     skills.learnTemp(106, 24.0F);
/* 4460 */     skills.learnTemp(10052, 40.0F);
/* 4461 */     int[] types = { 0, 4, 17 };
/*      */     
/* 4463 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.guide", types, (byte)0, skills, (short)5, (byte)1, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.8F, 0, new int[0], 3, 0, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4468 */     temp.keepSex = true;
/* 4469 */     temp.setBaseCombatRating(99.0F);
/* 4470 */     temp.setMaxGroupAttackSize(4);
/* 4471 */     temp.combatDamageType = 1;
/* 4472 */     temp.setTutorial(true);
/* 4473 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createEvilSantaTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4486 */     skills.learnTemp(102, 15.0F);
/* 4487 */     skills.learnTemp(104, 15.0F);
/* 4488 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 4490 */     skills.learnTemp(100, 10.0F);
/* 4491 */     skills.learnTemp(101, 10.0F);
/* 4492 */     skills.learnTemp(105, 99.0F);
/* 4493 */     skills.learnTemp(106, 24.0F);
/* 4494 */     skills.learnTemp(10052, 40.0F);
/* 4495 */     int[] types = { 0, 4, 17 };
/*      */     
/* 4497 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.evilsanta", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.8F, 0, new int[0], 3, 0, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4502 */     temp.keepSex = true;
/* 4503 */     temp.setBaseCombatRating(99.0F);
/* 4504 */     temp.combatDamageType = 1;
/* 4505 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSantaClausTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4518 */     skills.learnTemp(102, 15.0F);
/* 4519 */     skills.learnTemp(104, 15.0F);
/* 4520 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 4522 */     skills.learnTemp(100, 10.0F);
/* 4523 */     skills.learnTemp(101, 10.0F);
/* 4524 */     skills.learnTemp(105, 99.0F);
/* 4525 */     skills.learnTemp(106, 24.0F);
/* 4526 */     skills.learnTemp(10052, 40.0F);
/* 4527 */     int[] types = { 0, 4, 17 };
/*      */     
/* 4529 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.santa", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.8F, 0, new int[] { 310 }, 3, 0, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4534 */     temp.keepSex = true;
/* 4535 */     temp.setBaseCombatRating(99.0F);
/* 4536 */     temp.combatDamageType = 1;
/* 4537 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createBartenderTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4550 */     skills.learnTemp(102, 15.0F);
/* 4551 */     skills.learnTemp(104, 15.0F);
/* 4552 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 4554 */     skills.learnTemp(100, 10.0F);
/* 4555 */     skills.learnTemp(101, 10.0F);
/* 4556 */     skills.learnTemp(105, 99.0F);
/* 4557 */     skills.learnTemp(106, 14.0F);
/* 4558 */     skills.learnTemp(10052, 40.0F);
/* 4559 */     int[] types = { 0, 4, 17, 26 };
/*      */     
/* 4561 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.bartender", types, (byte)0, skills, (short)2, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.8F, 0, new int[0], 3, 0, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4566 */     temp.setBaseCombatRating(79.0F);
/* 4567 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSalesmanTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4581 */     skills.learnTemp(102, 15.0F);
/* 4582 */     skills.learnTemp(104, 15.0F);
/* 4583 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 4585 */     skills.learnTemp(100, 30.0F);
/* 4586 */     skills.learnTemp(101, 30.0F);
/* 4587 */     skills.learnTemp(105, 99.0F);
/* 4588 */     skills.learnTemp(106, 4.0F);
/* 4589 */     skills.learnTemp(10052, 40.0F);
/* 4590 */     int[] types = { 0, 1, 4, 5, 12, 17 };
/*      */     
/* 4592 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.salesman", types, (byte)0, skills, (short)2, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.8F, 0, new int[0], 3, 0, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4597 */     temp.setBaseCombatRating(70.0F);
/* 4598 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createChickenTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4611 */     skills.learnTemp(102, 1.0F);
/* 4612 */     skills.learnTemp(104, 5.0F);
/* 4613 */     skills.learnTemp(103, 1.0F);
/* 4614 */     skills.learnTemp(100, 5.0F);
/* 4615 */     skills.learnTemp(101, 4.0F);
/* 4616 */     skills.learnTemp(105, 5.0F);
/* 4617 */     skills.learnTemp(106, 1.0F);
/* 4618 */     skills.learnTemp(10052, 1.0F);
/* 4619 */     int[] types = { 7, 3, 14, 43, 28, 32, 63 };
/*      */     
/* 4621 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.chicken", types, (byte)7, skills, (short)3, (byte)0, (short)10, (short)5, (short)10, "sound.death.hen", "sound.death.hen", "sound.combat.hit.hen", "sound.combat.hit.hen", 1.0F, 0.5F, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F, 100, new int[0], 1, 0, (byte)77);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4626 */     temp.setHandDamString("claw");
/* 4627 */     temp.setKickDamString("claw");
/*      */     
/* 4629 */     temp.setMaxAge(20);
/* 4630 */     temp.setAdultFemaleTemplateId(45);
/* 4631 */     temp.setAdultMaleTemplateId(52);
/* 4632 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_NONE);
/* 4633 */     temp.setBaseCombatRating(1.0F);
/* 4634 */     temp.combatDamageType = 2;
/*      */     
/* 4636 */     temp.setColourNames(new String[] { "white", "brown", "black" });
/*      */     
/* 4638 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 4639 */       temp.setVision((short)3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createHenTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4652 */     skills.learnTemp(102, 3.0F);
/* 4653 */     skills.learnTemp(104, 15.0F);
/* 4654 */     skills.learnTemp(103, 1.0F);
/* 4655 */     skills.learnTemp(100, 5.0F);
/* 4656 */     skills.learnTemp(101, 4.0F);
/* 4657 */     skills.learnTemp(105, 5.0F);
/* 4658 */     skills.learnTemp(106, 1.0F);
/* 4659 */     skills.learnTemp(10052, 5.0F);
/* 4660 */     int[] types = { 7, 3, 14, 43, 28, 32, 35, 49, 64 };
/*      */ 
/*      */     
/* 4663 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.hen", types, (byte)7, skills, (short)3, (byte)1, (short)30, (short)14, (short)50, "sound.death.hen", "sound.death.hen", "sound.combat.hit.hen", "sound.combat.hit.hen", 1.0F, 0.5F, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F, 100, new int[] { 140, 1352, 1352, 1352 }, 1, 0, (byte)77);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4669 */     temp.setHandDamString("claw");
/* 4670 */     temp.setKickDamString("claw");
/*      */     
/* 4672 */     temp.setEggLayer(true);
/* 4673 */     temp.setEggTemplateId(48);
/* 4674 */     temp.setMaxAge(20);
/* 4675 */     temp.keepSex = true;
/* 4676 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_NONE);
/* 4677 */     temp.setBaseCombatRating(1.0F);
/* 4678 */     temp.combatDamageType = 2;
/* 4679 */     temp.setMaxPercentOfCreatures(0.02F);
/*      */     
/* 4681 */     temp.setColourNames(new String[] { "white", "brown", "black" });
/*      */     
/* 4683 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 4684 */       temp.setVision((short)3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createPheasantTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4697 */     skills.learnTemp(102, 5.0F);
/* 4698 */     skills.learnTemp(104, 25.0F);
/* 4699 */     skills.learnTemp(103, 5.0F);
/* 4700 */     skills.learnTemp(100, 5.0F);
/* 4701 */     skills.learnTemp(101, 4.0F);
/* 4702 */     skills.learnTemp(105, 10.0F);
/* 4703 */     skills.learnTemp(106, 1.0F);
/* 4704 */     skills.learnTemp(10052, 7.0F);
/* 4705 */     int[] types = { 7, 3, 28, 32, 35, 49, 60, 61 };
/*      */ 
/*      */     
/* 4708 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.pheasant", types, (byte)7, skills, (short)3, (byte)0, (short)30, (short)14, (short)50, "sound.death.pheasant", "sound.death.pheasant", "sound.combat.hit.pheasant", "sound.combat.hit.pheasant", 1.0F, 1.0F, 0.0F, 1.5F, 0.0F, 0.0F, 1.0F, 100, new int[] { 140, 1352, 1352, 1352, 1352, 1352 }, 1, 0, (byte)78);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4714 */     temp.setHandDamString("claw");
/* 4715 */     temp.setKickDamString("claw");
/* 4716 */     temp.setMaxAge(100);
/* 4717 */     temp.setBaseCombatRating(1.0F);
/* 4718 */     temp.combatDamageType = 2;
/* 4719 */     temp.setMaxGroupAttackSize(2);
/* 4720 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */     
/* 4722 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 4723 */       temp.setVision((short)4);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRoosterTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4736 */     skills.learnTemp(102, 5.0F);
/* 4737 */     skills.learnTemp(104, 25.0F);
/* 4738 */     skills.learnTemp(103, 5.0F);
/* 4739 */     skills.learnTemp(100, 5.0F);
/* 4740 */     skills.learnTemp(101, 4.0F);
/* 4741 */     skills.learnTemp(105, 10.0F);
/* 4742 */     skills.learnTemp(106, 1.0F);
/* 4743 */     skills.learnTemp(10052, 10.0F);
/* 4744 */     int[] types = { 7, 3, 14, 43, 28, 32, 64 };
/*      */     
/* 4746 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.rooster", types, (byte)7, skills, (short)3, (byte)0, (short)30, (short)14, (short)50, "sound.death.hen", "sound.death.hen", "sound.combat.hit.hen", "sound.combat.hit.hen", 1.0F, 0.5F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 100, new int[] { 140, 1352, 1352, 1352 }, 1, 0, (byte)77);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4752 */     temp.setHandDamString("claw");
/* 4753 */     temp.setKickDamString("claw");
/* 4754 */     temp.setMaxAge(30);
/* 4755 */     temp.keepSex = true;
/* 4756 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_NONE);
/* 4757 */     temp.setBaseCombatRating(1.0F);
/* 4758 */     temp.setMaxGroupAttackSize(2);
/* 4759 */     temp.combatDamageType = 2;
/* 4760 */     temp.setMaxPercentOfCreatures(0.002F);
/* 4761 */     temp.setColourNames(new String[] { "brown", "white", "black" });
/*      */     
/* 4763 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 4764 */       temp.setVision((short)4);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createDeerTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4777 */     skills.learnTemp(102, 15.0F);
/* 4778 */     skills.learnTemp(104, 33.0F);
/* 4779 */     skills.learnTemp(103, 20.0F);
/*      */     
/* 4781 */     skills.learnTemp(100, 5.0F);
/* 4782 */     skills.learnTemp(101, 4.0F);
/* 4783 */     skills.learnTemp(105, 10.0F);
/* 4784 */     skills.learnTemp(106, 8.0F);
/* 4785 */     skills.learnTemp(10052, 10.0F);
/* 4786 */     int[] types = { 7, 3, 9, 28, 32, 49, 35, 60, 61 };
/*      */ 
/*      */     
/* 4789 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.deer", types, (byte)1, skills, (short)10, (byte)0, (short)70, (short)50, (short)50, "sound.death.deer", "sound.death.deer", "sound.combat.hit.deer", "sound.combat.hit.deer", 1.0F, 1.0F, 1.0F, 0.5F, 1.0F, 0.0F, 1.5F, 30, new int[] { 307, 306, 71, 140, 309, 308, 308, 310 }, 5, 10, (byte)78);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4819 */     temp.setHandDamString("kick");
/* 4820 */     temp.setMaxAge(100);
/* 4821 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_NONE);
/* 4822 */     temp.setBaseCombatRating(2.0F);
/* 4823 */     temp.setMaxGroupAttackSize(3);
/* 4824 */     temp.combatDamageType = 0;
/* 4825 */     temp.setMaxPercentOfCreatures(0.005F);
/*      */     
/* 4827 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 4828 */       temp.setVision((short)9);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createFoalTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4841 */     skills.learnTemp(102, 10.0F);
/* 4842 */     skills.learnTemp(104, 10.0F);
/* 4843 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 4845 */     skills.learnTemp(100, 3.0F);
/* 4846 */     skills.learnTemp(101, 2.0F);
/* 4847 */     skills.learnTemp(105, 5.0F);
/* 4848 */     skills.learnTemp(106, 1.0F);
/* 4849 */     skills.learnTemp(10052, 5.0F);
/* 4850 */     int[] types = { 7, 12, 3, 43, 14, 9, 28, 32, 63 };
/*      */ 
/*      */     
/* 4853 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.foal", types, (byte)1, skills, (short)3, (byte)0, (short)100, (short)50, (short)50, "sound.death.horse", "sound.death.horse", "sound.combat.hit.horse", "sound.combat.hit.horse", 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 100, new int[] { 307, 140, 306, 71, 309, 308, 308 }, 5, 0, (byte)79);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4859 */     temp.setHandDamString("kick");
/* 4860 */     temp.setMaxAge(100);
/* 4861 */     temp.setAdultFemaleTemplateId(64);
/* 4862 */     temp.setAdultMaleTemplateId(64);
/* 4863 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_NONE);
/* 4864 */     temp.setBaseCombatRating(3.0F);
/* 4865 */     temp.setMaxGroupAttackSize(2);
/* 4866 */     temp.combatDamageType = 0;
/* 4867 */     temp.isHorse = true;
/*      */ 
/*      */     
/* 4870 */     temp.setColourNames(new String[] { "grey", "brown", "gold", "black", "white", "piebald pinto", "blood bay", "ebony black", "skewbald pinto", "gold buckskin", "black silver", "appaloosa", "chestnut" });
/*      */ 
/*      */     
/* 4873 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 4874 */       temp.setVision((short)3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createHorseTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4887 */     skills.learnTemp(102, 25.0F);
/* 4888 */     skills.learnTemp(104, 20.0F);
/* 4889 */     skills.learnTemp(103, 40.0F);
/*      */     
/* 4891 */     skills.learnTemp(100, 7.0F);
/* 4892 */     skills.learnTemp(101, 7.0F);
/* 4893 */     skills.learnTemp(105, 22.0F);
/* 4894 */     skills.learnTemp(106, 5.0F);
/* 4895 */     skills.learnTemp(10052, 28.0F);
/* 4896 */     int[] types = { 7, 12, 41, 43, 3, 14, 9, 28, 32 };
/*      */ 
/*      */     
/* 4899 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.horse", types, (byte)1, skills, (short)3, (byte)0, (short)180, (short)50, (short)250, "sound.death.horse", "sound.death.horse", "sound.combat.hit.horse", "sound.combat.hit.horse", 1.0F, 1.0F, 2.5F, 1.5F, 2.0F, 0.0F, 1.5F, 100, new int[] { 307, 306, 140, 71, 309, 308, 308 }, 5, 0, (byte)79);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4905 */     temp.setMaxAge(200);
/* 4906 */     temp.setChildTemplateId(65);
/* 4907 */     temp.setBaseCombatRating(6.0F);
/* 4908 */     temp.combatDamageType = 0;
/* 4909 */     temp.setAlignment(100.0F);
/* 4910 */     temp.setMaxGroupAttackSize(3);
/* 4911 */     temp.setHandDamString("kick");
/* 4912 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 4913 */     temp.isHorse = true;
/* 4914 */     temp.setMaxPercentOfCreatures(0.1F);
/*      */ 
/*      */     
/* 4917 */     temp.setColourNames(new String[] { "grey", "brown", "gold", "black", "white", "piebald pinto", "blood bay", "ebony black", "skewbald pinto", "gold buckskin", "black silver", "appaloosa", "chestnut" });
/*      */ 
/*      */     
/* 4920 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 4921 */       temp.setVision((short)4);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createBisonTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 4934 */     skills.learnTemp(102, 25.0F);
/* 4935 */     skills.learnTemp(104, 23.0F);
/* 4936 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 4938 */     skills.learnTemp(100, 5.0F);
/* 4939 */     skills.learnTemp(101, 4.0F);
/* 4940 */     skills.learnTemp(105, 10.0F);
/* 4941 */     skills.learnTemp(106, 3.0F);
/* 4942 */     skills.learnTemp(10052, 15.0F);
/*      */ 
/*      */     
/* 4945 */     int[] types = { 7, 3, 43, 14, 9, 28, 32, 49, 35, 15 };
/*      */ 
/*      */     
/* 4948 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.bison", types, (byte)1, skills, (short)10, (byte)0, (short)180, (short)50, (short)250, "sound.death.bison", "sound.death.bison", "sound.combat.hit.bison", "sound.combat.hit.bison", 0.3F, 5.0F, 5.0F, 10.0F, 4.0F, 0.0F, 0.8F, 30, new int[] { 307, 306, 140, 71, 309, 308, 308, 304, 304 }, 5, 10, (byte)73);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4978 */     temp.setHandDamString("kick");
/* 4979 */     temp.setMaxAge(50);
/* 4980 */     temp.keepSex = false;
/* 4981 */     temp.setBaseCombatRating(4.0F);
/* 4982 */     temp.combatDamageType = 0;
/* 4983 */     temp.setMaxGroupAttackSize(3);
/* 4984 */     temp.setBonusCombatRating(14.0F);
/* 4985 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */     
/* 4987 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 4988 */       temp.setVision((short)7);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createBullTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5001 */     skills.learnTemp(102, 25.0F);
/* 5002 */     skills.learnTemp(104, 23.0F);
/* 5003 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 5005 */     skills.learnTemp(100, 5.0F);
/* 5006 */     skills.learnTemp(101, 4.0F);
/* 5007 */     skills.learnTemp(105, 10.0F);
/* 5008 */     skills.learnTemp(106, 3.0F);
/* 5009 */     skills.learnTemp(10052, 15.0F);
/* 5010 */     int[] types = { 7, 41, 3, 43, 14, 9, 28, 32 };
/*      */ 
/*      */     
/* 5013 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.bull", types, (byte)1, skills, (short)3, (byte)0, (short)180, (short)50, (short)250, "sound.death.cow.brown", "sound.death.cow.brown", "sound.combat.hit.cow.brown", "sound.combat.hit.cow.brown", 1.0F, 2.0F, 2.0F, 3.0F, 4.0F, 0.0F, 0.5F, 100, new int[] { 307, 306, 140, 71, 309, 308, 308, 312, 312 }, 5, 10, (byte)73);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5021 */     temp.setHandDamString("kick");
/* 5022 */     temp.setMaxAge(50);
/* 5023 */     temp.keepSex = true;
/* 5024 */     temp.setBaseCombatRating(4.0F);
/* 5025 */     temp.combatDamageType = 0;
/* 5026 */     temp.setMaxGroupAttackSize(3);
/* 5027 */     temp.setMateTemplateId(3);
/* 5028 */     temp.setMaxPercentOfCreatures(0.02F);
/*      */     
/* 5030 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 5031 */       temp.setVision((short)5);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCalfTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5044 */     skills.learnTemp(102, 10.0F);
/* 5045 */     skills.learnTemp(104, 10.0F);
/* 5046 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 5048 */     skills.learnTemp(100, 3.0F);
/* 5049 */     skills.learnTemp(101, 2.0F);
/* 5050 */     skills.learnTemp(105, 5.0F);
/* 5051 */     skills.learnTemp(106, 1.0F);
/* 5052 */     skills.learnTemp(10052, 5.0F);
/* 5053 */     int[] types = { 7, 3, 14, 43, 9, 28, 32, 49, 35, 63 };
/*      */ 
/*      */     
/* 5056 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.calf", types, (byte)1, skills, (short)6, (byte)0, (short)100, (short)50, (short)100, "sound.death.cow.brown", "sound.death.cow.brown", "sound.combat.hit.cow.brown", "sound.combat.hit.cow.brown", 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 100, new int[] { 307, 140, 306, 71, 309, 308, 308 }, 5, 0, (byte)73);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5062 */     temp.setHandDamString("kick");
/* 5063 */     temp.setMaxAge(100);
/* 5064 */     temp.setAdultFemaleTemplateId(3);
/* 5065 */     temp.setAdultMaleTemplateId(49);
/* 5066 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_NONE);
/* 5067 */     temp.setBaseCombatRating(3.0F);
/* 5068 */     temp.setMaxGroupAttackSize(2);
/* 5069 */     temp.setCombatDamageType((byte)0);
/*      */     
/* 5071 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 5072 */       temp.setVision((short)3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createBrownCowTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5085 */     skills.learnTemp(102, 20.0F);
/* 5086 */     skills.learnTemp(104, 20.0F);
/* 5087 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 5089 */     skills.learnTemp(100, 5.0F);
/* 5090 */     skills.learnTemp(101, 4.0F);
/* 5091 */     skills.learnTemp(105, 10.0F);
/* 5092 */     skills.learnTemp(106, 1.0F);
/* 5093 */     skills.learnTemp(10052, 8.0F);
/* 5094 */     int[] types = { 7, 41, 3, 43, 14, 15, 9, 28, 32, 49, 35 };
/*      */ 
/*      */     
/* 5097 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.cow", types, (byte)1, skills, (short)3, (byte)1, (short)180, (short)50, (short)250, "sound.death.cow.brown", "sound.death.cow.brown", "sound.combat.hit.cow.brown", "sound.combat.hit.cow.brown", 1.0F, 1.0F, 1.0F, 0.0F, 2.0F, 0.0F, 0.5F, 100, new int[] { 307, 306, 140, 71, 309, 308, 308 }, 5, 0, (byte)73);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5103 */     temp.keepSex = true;
/* 5104 */     temp.setMaxAge(100);
/* 5105 */     temp.setBaseCombatRating(1.0F);
/* 5106 */     temp.setChildTemplateId(50);
/* 5107 */     temp.setMateTemplateId(49);
/* 5108 */     temp.setMaxGroupAttackSize(2);
/* 5109 */     temp.combatDamageType = 0;
/* 5110 */     temp.setMaxPercentOfCreatures(0.02F);
/*      */     
/* 5112 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 5113 */       temp.setVision((short)4);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createLambTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5125 */     skills.learnTemp(102, 7.0F);
/* 5126 */     skills.learnTemp(104, 7.0F);
/* 5127 */     skills.learnTemp(103, 7.0F);
/*      */     
/* 5129 */     skills.learnTemp(100, 3.0F);
/* 5130 */     skills.learnTemp(101, 2.0F);
/* 5131 */     skills.learnTemp(105, 5.0F);
/* 5132 */     skills.learnTemp(106, 1.0F);
/* 5133 */     skills.learnTemp(10052, 5.0F);
/* 5134 */     int[] types = { 7, 3, 43, 14, 54, 9, 28, 32, 49, 35, 63 };
/*      */ 
/*      */     
/* 5137 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.lamb", types, (byte)1, skills, (short)3, (byte)0, (short)50, (short)30, (short)30, "sound.death.deer", "sound.death.deer", "sound.combat.hit.deer", "sound.combat.hit.deer", 1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.5F, 100, new int[] { 140, 309, 308, 308 }, 5, 0, (byte)83);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5143 */     temp.setMaxAge(100);
/* 5144 */     temp.setAdultFemaleTemplateId(96);
/* 5145 */     temp.setAdultMaleTemplateId(102);
/* 5146 */     temp.combatDamageType = 0;
/* 5147 */     temp.setBaseCombatRating(1.0F);
/* 5148 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_NONE);
/*      */     
/* 5150 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 5151 */       temp.setVision((short)3);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createSheepTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5164 */     skills.learnTemp(102, 17.0F);
/* 5165 */     skills.learnTemp(104, 17.0F);
/* 5166 */     skills.learnTemp(103, 25.0F);
/*      */     
/* 5168 */     skills.learnTemp(100, 5.0F);
/* 5169 */     skills.learnTemp(101, 4.0F);
/* 5170 */     skills.learnTemp(105, 10.0F);
/* 5171 */     skills.learnTemp(106, 1.0F);
/* 5172 */     skills.learnTemp(10052, 8.0F);
/* 5173 */     int[] types = { 7, 3, 43, 14, 54, 15, 9, 28, 32, 49, 35, 52 };
/*      */ 
/*      */     
/* 5176 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.sheep", types, (byte)1, skills, (short)3, (byte)1, (short)50, (short)50, (short)30, "sound.death.deer", "sound.death.deer", "sound.combat.hit.deer", "sound.combat.hit.deer", 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.5F, 100, new int[] { 140, 309, 308, 308 }, 5, 0, (byte)83);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5182 */     temp.setMaxAge(100);
/* 5183 */     temp.keepSex = true;
/* 5184 */     temp.setChildTemplateId(101);
/* 5185 */     temp.setMateTemplateId(102);
/* 5186 */     temp.setBaseCombatRating(1.0F);
/* 5187 */     temp.setMaxGroupAttackSize(2);
/* 5188 */     temp.combatDamageType = 0;
/* 5189 */     temp.setMaxPercentOfCreatures(0.03F);
/*      */     
/* 5191 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 5192 */       temp.setVision((short)5);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRamTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5205 */     skills.learnTemp(102, 23.0F);
/* 5206 */     skills.learnTemp(104, 18.0F);
/* 5207 */     skills.learnTemp(103, 35.0F);
/*      */     
/* 5209 */     skills.learnTemp(100, 6.0F);
/* 5210 */     skills.learnTemp(101, 6.0F);
/* 5211 */     skills.learnTemp(105, 20.0F);
/* 5212 */     skills.learnTemp(106, 3.0F);
/* 5213 */     skills.learnTemp(10052, 22.0F);
/* 5214 */     int[] types = { 7, 3, 43, 14, 54, 9, 28, 32, 49, 35, 52 };
/*      */ 
/*      */     
/* 5217 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.sheep", types, (byte)1, skills, (short)3, (byte)0, (short)50, (short)50, (short)30, "sound.death.deer", "sound.death.deer", "sound.combat.hit.deer", "sound.combat.hit.deer", 1.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.5F, 100, new int[] { 140, 309, 308, 308, 304, 304 }, 5, 7, (byte)83);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5223 */     temp.setHandDamString("headbutt");
/* 5224 */     temp.setMaxAge(100);
/* 5225 */     temp.setChildTemplateId(101);
/* 5226 */     temp.setMateTemplateId(96);
/* 5227 */     temp.keepSex = true;
/* 5228 */     temp.setBaseCombatRating(5.0F);
/* 5229 */     temp.setMaxGroupAttackSize(3);
/* 5230 */     temp.combatDamageType = 0;
/* 5231 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 5232 */     temp.setMaxPercentOfCreatures(0.05F);
/*      */     
/* 5234 */     if (Features.Feature.CREATURE_MOVEMENT_CHANGES.isEnabled()) {
/* 5235 */       temp.setVision((short)5);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createKidTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5248 */     skills.learnTemp(102, 10.0F);
/* 5249 */     skills.learnTemp(104, 10.0F);
/* 5250 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 5252 */     skills.learnTemp(100, 3.0F);
/* 5253 */     skills.learnTemp(101, 2.0F);
/* 5254 */     skills.learnTemp(105, 5.0F);
/* 5255 */     skills.learnTemp(106, 1.0F);
/* 5256 */     skills.learnTemp(10052, 5.0F);
/* 5257 */     int[] types = { 7, 14, 27, 32, 45 };
/*      */     
/* 5259 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.child", types, (byte)0, skills, (short)5, (byte)0, (short)100, (short)30, (short)20, "sound.death.male.child", "sound.death.female.child", "sound.combat.hit.male.child", "sound.combat.hit.female.child", 1.0F, 2.0F, 0.0F, 3.0F, 2.0F, 0.0F, 1.0F, 100, new int[0], 25, 10, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5264 */     temp.setMaxAge(10);
/* 5265 */     temp.setAdultFemaleTemplateId(1);
/* 5266 */     temp.setAdultMaleTemplateId(1);
/* 5267 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_NONE);
/* 5268 */     temp.setBaseCombatRating(3.0F);
/* 5269 */     temp.combatDamageType = 0;
/* 5270 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createHumanTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5283 */     float num = Servers.localServer.getSkillbasicval();
/* 5284 */     skills.learn(1, num);
/* 5285 */     skills.learn(3, num);
/* 5286 */     skills.learn(2, num);
/*      */     
/* 5288 */     skills.learn(102, num);
/* 5289 */     skills.learn(104, Servers.localServer.getSkillbcval());
/* 5290 */     skills.learn(103, num);
/* 5291 */     skills.learn(100, Servers.localServer.getSkillmindval());
/* 5292 */     skills.learn(101, num);
/* 5293 */     skills.learn(105, num);
/* 5294 */     skills.learn(106, num);
/*      */     
/* 5296 */     skills.learn(1023, Servers.localServer.getSkillfightval());
/*      */     
/* 5298 */     int[] types = { 1, 12, 13, 17, 45, 7 };
/*      */     
/* 5300 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.player", types, (byte)0, skills, (short)80, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.5F, 0, new int[0], 25, 100, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5305 */     temp.setBaseCombatRating(4.0F);
/* 5306 */     temp.setChildTemplateId(66);
/* 5307 */     temp.setMaxGroupAttackSize(7);
/* 5308 */     temp.setAdultFemaleTemplateId(1);
/* 5309 */     temp.setAdultMaleTemplateId(1);
/* 5310 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRiftTemplateOne(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5323 */     skills.learnTemp(102, 20.0F);
/* 5324 */     skills.learnTemp(104, 35.0F);
/* 5325 */     skills.learnTemp(103, 25.0F);
/*      */     
/* 5327 */     skills.learnTemp(100, 8.0F);
/* 5328 */     skills.learnTemp(101, 10.0F);
/* 5329 */     skills.learnTemp(105, 30.0F);
/* 5330 */     skills.learnTemp(106, 2.0F);
/* 5331 */     skills.learnTemp(10052, 40.0F);
/* 5332 */     int[] types = { 7, 13, 3, 29, 6, 12 };
/*      */     
/* 5334 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.quadraped.beast.rift", types, (byte)3, skills, (short)8, (byte)0, (short)40, (short)20, (short)100, "sound.death.dog", "sound.death.dog", "sound.combat.hit.dog", "sound.combat.hit.dog", 0.3F, 12.0F, 0.0F, 14.0F, 15.0F, 0.0F, 1.6F, 300, new int[] { 636 }, 10, 90, (byte)74);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5339 */     temp.setHandDamString("claw");
/* 5340 */     temp.setAlignment(-50.0F);
/* 5341 */     temp.setMaxAge(50);
/* 5342 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
/* 5343 */     temp.setBaseCombatRating(14.0F);
/* 5344 */     temp.combatDamageType = 1;
/* 5345 */     temp.setMaxGroupAttackSize(8);
/* 5346 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRiftTemplateTwo(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5359 */     skills.learnTemp(102, 25.0F);
/* 5360 */     skills.learnTemp(104, 25.0F);
/* 5361 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 5363 */     skills.learnTemp(100, 12.0F);
/* 5364 */     skills.learnTemp(101, 14.0F);
/* 5365 */     skills.learnTemp(105, 30.0F);
/* 5366 */     skills.learnTemp(106, 12.0F);
/* 5367 */     skills.learnTemp(10052, 40.0F);
/* 5368 */     int[] types = { 7, 6, 13, 16, 40, 29, 30, 34, 39, 45, 55, 18, 12 };
/*      */ 
/*      */ 
/*      */     
/* 5372 */     int biteDamage = 10;
/* 5373 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.jackal.rift", types, (byte)0, skills, (short)8, (byte)0, (short)150, (short)100, (short)150, "sound.death.troll", "sound.death.troll", "sound.combat.hit.troll", "sound.combat.hit.troll", 0.2F, 15.0F, 6.0F, 10.0F, 0.0F, 0.0F, 1.4F, 700, new int[] { 636 }, 10, 80, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5378 */     temp.setHandDamString("burn");
/* 5379 */     temp.setAlignment(-20.0F);
/* 5380 */     temp.setMaxAge(50);
/* 5381 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_CHAIN);
/* 5382 */     temp.setBaseCombatRating(19.0F);
/* 5383 */     temp.combatDamageType = 2;
/* 5384 */     temp.setMaxGroupAttackSize(6);
/* 5385 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRiftTemplateThree(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5398 */     skills.learnTemp(102, 25.0F);
/* 5399 */     skills.learnTemp(104, 25.0F);
/* 5400 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 5402 */     skills.learnTemp(100, 12.0F);
/* 5403 */     skills.learnTemp(101, 14.0F);
/* 5404 */     skills.learnTemp(105, 30.0F);
/* 5405 */     skills.learnTemp(106, 12.0F);
/* 5406 */     skills.learnTemp(10052, 40.0F);
/* 5407 */     int[] types = { 7, 6, 13, 16, 40, 29, 30, 34, 39, 45, 55, 18, 12 };
/*      */ 
/*      */ 
/*      */     
/* 5411 */     int biteDamage = 10;
/* 5412 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.ogre.rift", types, (byte)0, skills, (short)8, (byte)0, (short)450, (short)100, (short)150, "sound.death.troll", "sound.death.troll", "sound.combat.hit.troll", "sound.combat.hit.troll", 0.1F, 20.0F, 10.0F, 10.0F, 0.0F, 0.0F, 1.4F, 700, new int[] { 636 }, 10, 84, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5417 */     temp.setHandDamString("burn");
/* 5418 */     temp.setAlignment(-20.0F);
/* 5419 */     temp.setMaxAge(50);
/* 5420 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 5421 */     temp.setBaseCombatRating(19.0F);
/* 5422 */     temp.combatDamageType = 0;
/* 5423 */     temp.setMaxGroupAttackSize(8);
/* 5424 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRiftTemplateFour(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5437 */     skills.learnTemp(102, 25.0F);
/* 5438 */     skills.learnTemp(104, 25.0F);
/* 5439 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 5441 */     skills.learnTemp(100, 12.0F);
/* 5442 */     skills.learnTemp(101, 14.0F);
/* 5443 */     skills.learnTemp(105, 30.0F);
/* 5444 */     skills.learnTemp(106, 12.0F);
/* 5445 */     skills.learnTemp(10052, 40.0F);
/* 5446 */     int[] types = { 7, 6, 13, 16, 40, 29, 30, 34, 39, 45, 55, 18, 12 };
/*      */ 
/*      */ 
/*      */     
/* 5450 */     int biteDamage = 40;
/* 5451 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.warmaster.rift", types, (byte)0, skills, (short)8, (byte)0, (short)450, (short)100, (short)150, "sound.death.troll", "sound.death.troll", "sound.combat.hit.troll", "sound.combat.hit.troll", 0.03F, 32.0F, 20.0F, 40.0F, 0.0F, 0.0F, 1.6F, 700, new int[] { 636 }, 20, 94, (byte)74);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5456 */     temp.setHandDamString("burn");
/* 5457 */     temp.setAlignment(-20.0F);
/* 5458 */     temp.setMaxAge(50);
/* 5459 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_PLATE);
/* 5460 */     temp.setBaseCombatRating(19.0F);
/* 5461 */     temp.combatDamageType = 1;
/* 5462 */     temp.setMaxGroupAttackSize(20);
/* 5463 */     temp.setMaxPercentOfCreatures(0.01F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRiftCasterTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5475 */     skills.learnTemp(102, 20.0F);
/* 5476 */     skills.learnTemp(104, 20.0F);
/* 5477 */     skills.learnTemp(103, 20.0F);
/*      */     
/* 5479 */     skills.learnTemp(100, 32.0F);
/* 5480 */     skills.learnTemp(101, 34.0F);
/* 5481 */     skills.learnTemp(105, 20.0F);
/* 5482 */     skills.learnTemp(106, 15.0F);
/* 5483 */     skills.learnTemp(10052, 40.0F);
/* 5484 */     skills.learnTemp(10067, 50.0F);
/* 5485 */     int[] types = { 7, 6, 13, 16, 40, 29, 30, 34, 39, 45, 55, 18, 12 };
/*      */     
/* 5487 */     int biteDamage = 10;
/* 5488 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.jackal.rift.caster", types, (byte)0, skills, (short)8, (byte)0, (short)150, (short)100, (short)150, "sound.death.troll", "sound.death.troll", "sound.combat.hit.troll", "sound.combat.hit.troll", 0.2F, 15.0F, 6.0F, 10.0F, 0.0F, 0.0F, 1.4F, 700, new int[] { 636 }, 10, 90, (byte)74);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5493 */     temp.setHandDamString("burn");
/* 5494 */     temp.setAlignment(-20.0F);
/* 5495 */     temp.setMaxAge(50);
/* 5496 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 5497 */     temp.setBaseCombatRating(14.0F);
/* 5498 */     temp.combatDamageType = 4;
/* 5499 */     temp.setMaxGroupAttackSize(6);
/* 5500 */     temp.setMaxPercentOfCreatures(0.001F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRiftOgreMageTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5512 */     skills.learnTemp(102, 20.0F);
/* 5513 */     skills.learnTemp(104, 20.0F);
/* 5514 */     skills.learnTemp(103, 20.0F);
/*      */     
/* 5516 */     skills.learnTemp(100, 42.0F);
/* 5517 */     skills.learnTemp(101, 44.0F);
/* 5518 */     skills.learnTemp(105, 30.0F);
/* 5519 */     skills.learnTemp(106, 15.0F);
/* 5520 */     skills.learnTemp(10052, 50.0F);
/* 5521 */     skills.learnTemp(10067, 50.0F);
/* 5522 */     int[] types = { 7, 6, 13, 16, 40, 29, 30, 34, 39, 45, 55, 18, 12 };
/*      */     
/* 5524 */     int biteDamage = 10;
/* 5525 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.ogre.rift.mage", types, (byte)0, skills, (short)8, (byte)0, (short)350, (short)100, (short)150, "sound.death.troll", "sound.death.troll", "sound.combat.hit.troll", "sound.combat.hit.troll", 0.1F, 20.0F, 10.0F, 10.0F, 0.0F, 0.0F, 1.4F, 700, new int[] { 636 }, 10, 90, (byte)81);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5530 */     temp.setHandDamString("burn");
/* 5531 */     temp.setAlignment(-50.0F);
/* 5532 */     temp.setMaxAge(50);
/* 5533 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON);
/* 5534 */     temp.setBaseCombatRating(19.0F);
/* 5535 */     temp.combatDamageType = 9;
/* 5536 */     temp.setMaxGroupAttackSize(8);
/* 5537 */     temp.setMaxPercentOfCreatures(0.001F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createRiftSummonerTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5549 */     skills.learnTemp(102, 40.0F);
/* 5550 */     skills.learnTemp(104, 25.0F);
/* 5551 */     skills.learnTemp(103, 30.0F);
/*      */     
/* 5553 */     skills.learnTemp(100, 18.0F);
/* 5554 */     skills.learnTemp(101, 14.0F);
/* 5555 */     skills.learnTemp(105, 40.0F);
/* 5556 */     skills.learnTemp(106, 15.0F);
/* 5557 */     skills.learnTemp(10052, 45.0F);
/* 5558 */     skills.learnTemp(10067, 60.0F);
/* 5559 */     int[] types = { 7, 6, 13, 16, 40, 29, 30, 34, 39, 45, 55, 18, 12 };
/*      */     
/* 5561 */     int biteDamage = 10;
/* 5562 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.jackal.rift.summoner", types, (byte)0, skills, (short)8, (byte)0, (short)150, (short)100, (short)150, "sound.death.troll", "sound.death.troll", "sound.combat.hit.troll", "sound.combat.hit.troll", 0.2F, 15.0F, 6.0F, 10.0F, 0.0F, 0.0F, 1.4F, 700, new int[] { 636 }, 10, 70, (byte)74);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5567 */     temp.setHandDamString("claw");
/* 5568 */     temp.setAlignment(-30.0F);
/* 5569 */     temp.setMaxAge(50);
/* 5570 */     temp.setArmourType(ArmourTemplate.ARMOUR_TYPE_STUDDED);
/* 5571 */     temp.setBaseCombatRating(12.0F);
/* 5572 */     temp.combatDamageType = 1;
/* 5573 */     temp.setMaxGroupAttackSize(6);
/* 5574 */     temp.setMaxPercentOfCreatures(0.001F);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void createNpcHumanTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5579 */     skills.learnTemp(102, 15.0F);
/* 5580 */     skills.learnTemp(104, 15.0F);
/* 5581 */     skills.learnTemp(103, 10.0F);
/*      */     
/* 5583 */     skills.learnTemp(100, 10.0F);
/* 5584 */     skills.learnTemp(101, 10.0F);
/* 5585 */     skills.learnTemp(105, 99.0F);
/* 5586 */     skills.learnTemp(106, 24.0F);
/* 5587 */     skills.learnTemp(10052, 40.0F);
/* 5588 */     int[] types = { 0, 4, 17 };
/*      */     
/* 5590 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.humanoid.human.player", types, (byte)0, skills, (short)5, (byte)0, (short)180, (short)20, (short)35, "sound.death.male", "sound.death.female", "sound.combat.hit.male", "sound.combat.hit.female", 1.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.8F, 0, new int[0], 3, 0, (byte)80);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5595 */     temp.setBaseCombatRating(99.0F);
/* 5596 */     temp.setMaxGroupAttackSize(4);
/* 5597 */     temp.combatDamageType = 1;
/* 5598 */     temp.hasHands = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createFishTemplate(int id, String name, String plural, String longDesc, Skills skills) throws IOException {
/* 5614 */     int[] types = { 4, 38, 37 };
/*      */     
/* 5616 */     CreatureTemplate temp = CreatureTemplateFactory.getInstance().createCreatureTemplate(id, name, plural, longDesc, "model.creature.fish", types, (byte)9, skills, (short)3, (byte)0, (short)10, (short)10, (short)100, "sound.death.snake", "sound.death.snake", "sound.combat.hit.snake", "sound.combat.hit.snake", 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 100, new int[0], 40, 59, (byte)85);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5621 */     temp.offZ = -1.4F;
/* 5622 */     temp.setCreatureAI((CreatureAI)new FishAI());
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CreatureTemplateCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */