/*      */ package com.wurmonline.server.items;
/*      */ 
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.combat.ArmourTemplate;
/*      */ import com.wurmonline.server.combat.CombatEngine;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoArmourException;
/*      */ import com.wurmonline.server.skills.NoSuchSkillException;
/*      */ import com.wurmonline.server.skills.Skill;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
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
/*      */ public final class Puppet
/*      */   implements MiscConstants, TimeConstants
/*      */ {
/*   42 */   private static final Logger logger = Logger.getLogger(Puppet.class.getName());
/*   43 */   private static final Map<Long, Long> puppetmasters = new HashMap<>();
/*   44 */   private static final Random random = new Random();
/*   45 */   private static final String[] FLSeventyPlus1 = new String[] { "@It was better before.", "Not for me. I feel alone.", "So am I. Yet I feel their suspicion and awe.", "You are always so kind and forgiving." };
/*      */ 
/*      */   
/*   48 */   private static final String[] LFSeventyPlus1 = new String[] { "", "This is better.", "I am friends with everyone now.", "Oh, I don't mind. Let them be suspicious.", "How nicely said of you. " };
/*      */ 
/*      */   
/*   51 */   private static final String[] FLSeventyPlus2 = new String[] { "@Come on!", "You said you wanted to. Now I am disappointed.", "Why not?", "It won't.", "I just know.", "Don't be afraid." };
/*      */ 
/*      */   
/*   54 */   private static final String[] LFSeventyPlus2 = new String[] { "", "I don't want to!", "Yes but now I don't want to.", "What if something happens?", "How do YOU know?", "Please! You make me sad! Stop torturing me!", "All that is left is fear." };
/*      */ 
/*      */ 
/*      */   
/*   58 */   private static final String[] VLSeventyPlus1 = new String[] { "@Have you done it yet?", "Coward.", "I will say what I want.", "Stop that!", "Well do as you please then. You are ridiculous.", "Do as you please." };
/*      */ 
/*      */   
/*   61 */   private static final String[] LVSeventyPlus1 = new String[] { "", "No. I don't dare to.", "You shouldn't call me that! It's not fair!", "I will say what I want.", "Stop that!", "I'm not! Stop this!" };
/*      */ 
/*      */   
/*   64 */   private static final String[] VLSeventyPlus2 = new String[] { "@We have decided that you have to walk that path again.", "You are the youngest.", "It is a reason. It is our reason.", "Frankly I do not care. Maybe if things were different.", "No YOU are! Egotistical little furball! You don't seem to care about anything! " };
/*      */ 
/*      */ 
/*      */   
/*   68 */   private static final String[] LVSeventyPlus2 = new String[] { "", "Why me? Why is it always me?", "Is that a good reason?", "You have no idea how this hurts.", "You are heartless!" };
/*      */ 
/*      */ 
/*      */   
/*   72 */   private static final String[] MLSeventyPlus1 = new String[] { "@I wonder what would help.", "All the time.", "You could do it for me.", "Do not fear. I will be there." };
/*      */   
/*   74 */   private static final String[] LMSeventyPlus1 = new String[] { "", "Does it hurt a lot?", "I could try I guess.", "It scares me.", "I have nobody else now." };
/*      */   
/*   76 */   private static final String[] MLSeventyPlus2 = new String[] { "@I don't see why you shouldn't!", "Of course it is!", "Because I am older?", "You always say that. Stop once and for all.", "If all goes well nobody will ever make fun of you again." };
/*      */ 
/*      */   
/*   79 */   private static final String[] LMSeventyPlus2 = new String[] { "", "It's not your decision!", "How can you say that?", "That's not fair!", "You're not the one who has to do this." };
/*      */ 
/*      */ 
/*      */   
/*   83 */   private static final String[] VFSeventyPlus1 = new String[] { "@She actually enters now!", "I start to feel weird.", "Oh my god. Oh dear.", "Magranon! Don't enter!", "Of course. Of course." };
/*      */ 
/*      */   
/*   86 */   private static final String[] VFSeventyPlus2 = new String[] { "", "Yes, I can't believe it. How... fascinating.", "Yes.. it is. I didn't expect to feel this way.", "NO! It can not be!", "He must. Don't you see?" };
/*      */ 
/*      */   
/*   89 */   private static final String[] FVSeventyPlus1 = new String[] { "", "A lot is lost in the pain. But euforia. Visions of mother.", "I wonder if we would survive. Do you?", "We could just touch it!" };
/*      */ 
/*      */   
/*   92 */   private static final String[] FVSeventyPlus2 = new String[] { "@How much do you remember?", "What happens if we try again?", "I would not take that chance.", "Do not speak of it any more please. And not in front of the others." };
/*      */ 
/*      */ 
/*      */   
/*   96 */   private static final String[] VMSeventyPlus1 = new String[] { "@Magranon! What did you see? Where is she?", "Gone?", "Oh love. Oh Magranon! Come here!", "We could not know. We could not... Oh Magranon.", "Magranon! You agreed to the plan! You accepted!" };
/*      */ 
/*      */   
/*   99 */   private static final String[] MVSeventyPlus1 = new String[] { "", "There was a change.. but she is dead.. gone..", "Yes.. her body is... shattered.", "My sister! My poor poor sister! What have we done?", "No.. I did not do this! You did! You both!", "No! No! This must be undone!" };
/*      */ 
/*      */ 
/*      */   
/*  103 */   private static final String[] VMSeventyPlus2 = new String[] { "", "That is weird. I feel only empty.", "Your rage is feeding rage. You should lie down with me.", "Who knows what would happen? What bad could come of it?", "Do not be afraid. Could you be harmed?" };
/*      */ 
/*      */   
/*  106 */   private static final String[] MVSeventyPlus2 = new String[] { "@I feel pain - all sorts of pain.", "I burn. I am a forest fire.", "I do not think that it will help.", "All sorts. I am not in control. Neither are you.", "How dare you patronize me over things that you know nothing about?" };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  111 */   private static final String[] FMSeventyPlus1 = new String[] { "@So how do you feel about this?", "She will be allright.", "You know I can not make such a promise.", "Yes. I know." };
/*      */   
/*  113 */   private static final String[] MFSeventyPlus1 = new String[] { "", "Not good honestly. I do not think that it is a good idea.", "Do you promise?", "If anything happens to her I do not know what I will do." };
/*      */ 
/*      */ 
/*      */   
/*  117 */   private static final String[] FMSeventyPlus2 = new String[] { "@I visited the source recently. It was gone.", "Oh, you know what happened to it?", "How?", "What kind of construction?", "Did they say where? What kind?", "What about the prisoners?" };
/*      */ 
/*      */   
/*  120 */   private static final String[] MFSeventyPlus2 = new String[] { "", "Yes.. I guess you have the right to know about it.", "She moved it. I think to her domains.", "I think humans helped her. Some prisoners spoke about a construction.", "They spoke of a basin.", "That was all I could make them say. They did not know where. Only that it was below ground.", "What do YOU think? That I left them alive?" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  126 */   private static final String[] AFSeventyPlus1 = new String[] { "@What did you do?", "What happened?", "How?", "Are you asking me?", "You became too powerful." };
/*      */   
/*  128 */   private static final String[] FASeventyPlus1 = new String[] { "", "She entered.", "She died.", "I do not know. What happened to us?", "I guess I am." };
/*      */   
/*  130 */   private static final String[] AFSeventyPlus2 = new String[] { "@I am scared now. Something is wrong.", "Are you afraid?", "He is broken.", "I wish we could help.", "That is not the right way." };
/*      */ 
/*      */   
/*  133 */   private static final String[] FASeventyPlus2 = new String[] { "", "Yes, it disappeared.", "Not me. No I am not. I am strong - I have to be.", "He is.", "This should all be forgotten." };
/*      */ 
/*      */ 
/*      */   
/*  137 */   private static final String[] AVSeventyPlus1 = new String[] { "@Should you not have known better?", "It was pretty obvious I think.", "So you do not know everything after all?", "I fear that one day you will have to pay the price." };
/*      */ 
/*      */   
/*  140 */   private static final String[] VASeventyPlus1 = new String[] { "", "There was no way of knowing!", "Thinking clear is not always easy.", "Not yet. Not yet.", "I will look to the stars. Maybe you are right." };
/*      */ 
/*      */   
/*  143 */   private static final String[] AVSeventyPlus2 = new String[] { "", "Nobody said it was.", "We understand.", "No.. What do you mean?", "Did you?", "Do not start any rumours now." };
/*      */ 
/*      */   
/*  146 */   private static final String[] VASeventyPlus2 = new String[] { "@It was not me! Not me!", "She did it to herself I say!", "I never meant to.. You know.", "Push her or anything.", "No.. I mean.. no.", "You are right. I should watch my tongue." };
/*      */ 
/*      */ 
/*      */   
/*  150 */   private static final String[] ALSeventyPlus1 = new String[] { "@Oh, to find you here. After all this time!", "I almost did not recognize you.", "You look... horrible... larvae.", "I will not. Disgusting!", "I will. I can not stand this any longer." };
/*      */ 
/*      */   
/*  153 */   private static final String[] LASeventyPlus1 = new String[] { "", "Help... Help.", "Neither... I.", "Not.. eat.. me...", "Leave.. leave..", "Mok... Kill it!" };
/*      */   
/*  155 */   private static final String[] ALSeventyPlus2 = new String[] { "", "No! What has happened to you?", "Tricked? I heard of greed.", "I do not believe you.", "For not believing? That is.. a joke?", "No. Not now." };
/*      */ 
/*      */   
/*  158 */   private static final String[] LASeventyPlus2 = new String[] { "@So. Will you retrieve my eye?", "Something horrible. I was tricked.", "That is a lie!", "Then I will slay you.", "Do I look like I make jokes now?", "Goodbye then." };
/*      */ 
/*      */ 
/*      */   
/*  162 */   private static final String[] AMSeventyPlus1 = new String[] { "@What did you do?", "Are you sure? You could have stopped it!", "It is your fault as much as anybody elses.", "Oh, stand up! Do not dare to blame someone else.", "Yes. It is a blessing." };
/*      */ 
/*      */   
/*  165 */   private static final String[] MASeventyPlus1 = new String[] { "", "I did nothing. It was them!", "Quiet! Quiet! Do not torment me!", "Stop it already! I was also tricked.", "I am glad mother did not have to see this." };
/*      */ 
/*      */   
/*  168 */   private static final String[] AMSeventyPlus2 = new String[] { "@We blame you now. And for the recent fires.", "Do not count on us.", "That will be hard. Not many of us like your ways.", "You should reconsider.", "Delusions." };
/*      */ 
/*      */   
/*  171 */   private static final String[] MASeventyPlus2 = new String[] { "", "You will stay with me will you not?", "I will find new followers then.", "I will find a way.", "The winds whisper of new times. Something is coming." };
/*      */ 
/*      */ 
/*      */   
/*  175 */   private static final String[] VFSeventyMinus1 = new String[] { "", "Who? Libila?", "How?", "A wolf? She would never kill anything.", "Well. There is little we can do unless we find her." };
/*      */ 
/*      */   
/*  178 */   private static final String[] FVSeventyMinus1 = new String[] { "@There is a rumour among the wolves that she is alive.", "Yes. She has changed.", "They say that she ordered one of them dead.", "Yes. One would think so but I believe them.", "Obviously she does not want to be found." };
/*      */ 
/*      */ 
/*      */   
/*  182 */   private static final String[] VFSeventyMinus2 = new String[] { "@These.. newcomers.. their presence is related to the second visit.", "More leaked when she moved it.", "Please do. I will as well, and I will talk to Magranon.", "So will I.", "Nothing. She was moved far away." };
/*      */ 
/*      */   
/*  185 */   private static final String[] FVSeventyMinus2 = new String[] { "", "Yes. Something opened up and they entered our forest.", "Oh. I will see if I can find a way to stop it.", "I will welcome them for now.", "Have you heard anything about her whereabouts?" };
/*      */ 
/*      */ 
/*      */   
/*  189 */   private static final String[] MLSeventyMinus1 = new String[] { "@Speak, wretch! What is your name and what do you do here?", "Hahaha! I heard she is nothing more than some kind of larvae!", "How?", "Is that so? What more?", "An altar? An altar.." };
/*      */ 
/*      */ 
/*      */   
/*  193 */   private static final String[] LMSeventyMinus1 = new String[] { "", "I am Baghmot. I speak on behalf of Her. I am to tell you that you will be undone!", "That may be. But she has significant strength already, and is gaining more quickly.", "Please ... the newcomers. Their faith feeds her.", "Aiii! She ordered them to build an altar!" };
/*      */ 
/*      */ 
/*      */   
/*  197 */   private static final String[] MLSeventyMinus2 = new String[] { "@Do you like my fire?", "That would be the point.", "I don't mind.", "We will see, won't we? Now where were we..", "Yes, what about it?", "Alive? How do you mean alive?", "I must see this for myself.", "I am sure I won't." };
/*      */ 
/*      */   
/*  200 */   private static final String[] LMSeventyMinus2 = new String[] { "", "It hurts! Aiii!", "You are cruel. More cruel than her!", "Will I even survive this?", "You asked about my skin.", "It is something in her lands that changes it.. the lands seem alive with something.", "Like.. snakes. Something in the ground is moving slowly.", "I like it." };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  205 */   private static final String[] FMSeventyMinus1 = new String[] { "", "Me and Vynora is going to welcome them while we figure out a way.", "Build an altar?", "I gathered some of it that was dropped as she left. Maybe it will protect our altar.", "Vynora learned their language. She can make inscriptions.", "Please reconsider. We need to unite.", "So be it then." };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  210 */   private static final String[] MFSeventyMinus1 = new String[] { "@She is building an altar. The newcomers give her strength.", "We should do the same then.", "Yes. I tried to approach hers. It is full with her tainted source. It hurts to approach.", "Yes.. Maybe it will. We can try.", "I have as well, and I have my own plans. I will write my own inscription.", "I have had time." };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  215 */   private static final String[] FMSeventyMinus2 = new String[] { "@Spirits come to me daily now.", "Yes. What can I say?", "Why? He will not love you anyways.", "I will not cast him out. I fear that it would ruin him.", "On another note - what about those lava fiends?", "They disturb the balance.", "I sense aggression.", "Do I sense a threat?", "So have I. So have I." };
/*      */ 
/*      */ 
/*      */   
/*  219 */   private static final String[] MFSeventyMinus2 = new String[] { "", "I have noticed such a change. Also my brother no longer speaks with me. He loves you instead.", "It hurts. You could refuse him.", "You do not know that.", "I made them from spirits. What about them?", "They do not! There are worse matters that YOU are the cause of!", "I sense ignorance and insinuation. I will react negatively.", "I have found other powers. You have no idea." };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  225 */   private static final String[] VMSeventyMinus1 = new String[] { "@The dragons told me that she moved the source.", "You should have told me yourself.", "I also have the right to know.", "Yes I did - no thanks to you.", "Things surely have changed, haven't they?" };
/*      */ 
/*      */   
/*  228 */   private static final String[] MVSeventyMinus1 = new String[] { "", "Yes I am aware that she did. They should not have told you. It is treason.", "Why?", "No you do not. In any case you found out.", "That is fine with me.", "Nothing ever stays the same, does it?" };
/*      */ 
/*      */ 
/*      */   
/*  232 */   private static final String[] VMSeventyMinus2 = new String[] { "", "Yes what about it? I heard Fo saved it.", "Oh, how?", "Abundant? That sounds bad.", "Ouch.", "The source is powerful. If those humans come in contact with it they will be affected as well.", "I am aware. They are probably doomed, then." };
/*      */ 
/*      */ 
/*      */   
/*  236 */   private static final String[] MVSeventyMinus2 = new String[] { "@That wretched wolf I told you about.", "He claimed that her lands are alive somehow.", "I had to go see for myself. Seems they are abundant with corrupt source.", "Yes I am sure it is. It is also spreading.", "What would you think can come from it?", "She has started to attract them.", "Let's hope not." };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  242 */   private static final String[] FLSeventyMinus1 = new String[] { "@What have you done?", "I saved your friend.", "Well, whatever. He is my friend now.", "That is not my intention.", "Poor thing.", "We meant no harm.", "Can I do anything for you?" };
/*      */ 
/*      */   
/*  245 */   private static final String[] LFSeventyMinus1 = new String[] { "", "How did you find me?", "Haha. Friend you say?", "Of course. You manage to fool everyone.", "So you say. I know better.", "I will have no mercy with you, you know that.", "You are only regretful. You game went awry and now you are scared. Cowards.", "Guess what? LEAVE ME ALONE! ALONE!" };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  250 */   private static final String[] FLSeventyMinus2 = new String[] { "@Did we actually meet afterwards?", "Did you really say those things?", "I love you, don't I?", "I don't understand why they didn't stop me.", "Am I still?", "And mine, of course." };
/*      */ 
/*      */   
/*  253 */   private static final String[] LFSeventyMinus2 = new String[] { "", "Who knows?", "Most probably.", "So they say.", "They probably couldn't. You were too powerful.", "I wouldn't think so. Depends on my followers.", "Yes. Yours as well." };
/*      */ 
/*      */ 
/*      */   
/*  257 */   private static final String[] VLSeventyMinus1 = new String[] { "@You are sleeping now aren't you?", "So I can reach you in your dreams only.", "Will you not end this then?", "We apologize.", "I can foresee the destruction you will cause.", "What can we offer?" };
/*      */ 
/*      */   
/*  260 */   private static final String[] LVSeventyMinus1 = new String[] { "", "Yes I am.", "Yes. I will hide.", "Give me one good reason.", "So I heard. You will still suffer.", "So be it. I will let death and desolation reign.", "Nothing. We will slowly dismember you." };
/*      */ 
/*      */   
/*  263 */   private static final String[] VLSeventyMinus2 = new String[] { "@I am back again.", "Can you lock me out do you think?", "Fascinating. Anyways, we have decided to offer you all the newcomers.", "Yes we would.", "If you say so. Anything else?", "We will be prepared." };
/*      */ 
/*      */ 
/*      */   
/*  267 */   private static final String[] LVSeventyMinus2 = new String[] { "", "You are not welcome. This is the last time.", "Yes. You are here only because I allow you to.", "That is not enough. Really sacrifice them?", "That is truly ruthless.", "No. This was the last time.", "I don't think so." };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  272 */   private static final String[] item1 = new String[] { "@Hello.", "Pleased to meet you.", "How are we today?", "Oh, nice.", "Yes yes YES!", "Hahaha!", "Take that!", "Bang!", "Tjoff!" };
/*      */ 
/*      */   
/*  275 */   private static final String[] item2 = new String[] { "@Hi.", "Pleased to meet you too.", "Fine thanks?", "You were one ugly doll.", "Silence, please!", "Why do you say that?", "Ouch!", "Buhuhu!", "Now you made me sad!" };
/*      */ 
/*      */   
/*  278 */   private static final String[] item3 = new String[] { "@Woof!", "Bongo bongo bey.", "Lamisika!", "Hoodeladi hoppsan sa.", "You don't fool me, biscuit eater!", "Your ugly twin was by yesterday.", "He said he looked better than you.", "Don't hit me!", "Please calm down.", "I never said that!" };
/*      */ 
/*      */ 
/*      */   
/*  282 */   private static final String[] item4 = new String[] { "@How are you doing, friend?", "Nice to see you here.", "I really like the weather today. Do you think it will rain?", "How have you been lately?", "Glad to hear that.", "I am fine thank you. Very fine indeed." };
/*      */ 
/*      */   
/*  285 */   private static final String[] item5 = new String[] { "@Aren't we the madhatter today?", "No I mean you!", "Help, guards!", "Guards!", "I am sure they will catch the perpetrator eventually.", "Who said that?", "No that's YOU!" };
/*      */ 
/*      */   
/*  288 */   private static final String[] item6 = new String[] { "@Where are we?", "What are we doing here?", "You can't be serious.", "Who are you talking to?", "Please calm down!", "I am not upset but I can see that you are.", "That is your problem." };
/*      */ 
/*      */   
/*  291 */   private static final String[] item7 = new String[] { "@Hello, dear friend.", "Yes it was too long.", "No I haven't.", "I have no idea what you are talking about.", "Are you accusing me of something?", "I would like to see you do that.", "Hahaha" };
/*      */ 
/*      */   
/*  294 */   private static final String[] item8 = new String[] { "@What is this place?", "Why do you look so strange?", "Are you Fo?", "Of course I see that.", "Are you afraid of the spirits?", "What's all this talk about mycelium anyways?", "I am not afraid at all." };
/*      */ 
/*      */   
/*  297 */   private static final String[] item9 = new String[] { "@Hello, you look like Libila today!", "No it was a joke.", "Actually I am really happy to see you.", "Where are you going? To the altar?", "They say the altar can be destroyed once the deities gain enough followers." };
/*      */ 
/*      */   
/*  300 */   private static final String[] item10 = new String[] { "@Bla hello bla.", "I actually like Magranon.", "Magranon is the best.", "Magranon can kill ANYTHING.", "Magranon blows fire from his ass.", "I didn't mean to say that last thing. Now I blush.", "Who are you to question me anyways?" };
/*      */ 
/*      */ 
/*      */   
/*  304 */   private static final String[] item11 = new String[] { "@Hey, you!", "Are you Vynora?", "They say you know eeeverything.", "So it's true?", "Where's my hat at?", "Is the world flat?", "Can I travel to Seris?", "Where's my wombat cheesecake anyways?" };
/*      */ 
/*      */   
/*  307 */   private static final String[] item12 = new String[] { "@Who was that?", "Oh it was you!", "No", "That is none of your business.", "Oh, please. Please please please.", "I am always here.", "I have never seen you before.", "That's all for me folks!" };
/*      */ 
/*      */   
/*  310 */   private static final String[] item13 = new String[] { "@Oh hi", "No I don't.", "Who are you anyways?", "That's me in a nutshell.", "Hey wonderboy, don't you grow tired of those remarks?", "I took old Sadking on a ride yesterday.", "He buckled so much and I fell off.", "I don't feel any differenk.", "Thank you, thank you very much." };
/*      */ 
/*      */ 
/*      */   
/*  314 */   private static final String[] item14 = new String[] { "@Hello again.", "Haven't we met?", "Take that, infidel!", "Who said that?", "Now, where did that come from? Did anyone see that one coming?", "I am so sad today.", "Well, at least the weather is good.", "Allright, catch you later folks!" };
/*      */ 
/*      */ 
/*      */   
/*  318 */   private static final String[] item15 = new String[] { "@Good day to you.", "Well, how nice.", "Ho ho ho.", "Is that my dandelion over there?", "Oh, jolly.", "Jeeves! Jeeves!", "Please pour my cup of tea, dear.", "Most sorry if I upset anyone. Now take care, folks!" };
/*      */ 
/*      */   
/*  321 */   private static final String[] item16 = new String[] { "@Wild thing! You make everything swing!", "Yeah!", "To the bottom, girls!", "Yumba, yumba - toot!", "That's right! Work it harder, dangerous one!", "You should meet my friend Spoon.", "That's all folks!" };
/*      */ 
/*      */ 
/*      */   
/*  325 */   private static final String[] FFbasic1 = new String[] { "@Fo.", "Yes.", "How am I today?", "Fine thank you.", "Am I sure?", "Yes I am.", "What am I thinking about?", "A fresh wind blowing in the trees and the grass I suppose. Keeping children and mothers safe from danger.", "Those are nice thoughts.", "Yes, I think so too." };
/*      */ 
/*      */ 
/*      */   
/*  329 */   private static final String[] MMbasic1 = new String[] { "@Oh, hello good old friend!", "Hello to me too!", "Mighty fine day, isn't it?", "Oh yes most assuredly.", "How are business?", "Thriving, thriving.", "How are my followers doing?", "Good I hope. I try to keep them safe.", "And how are my enemies doing?", "They should fear me and my people!", "And who are your people?", "The Mol Rehans are my people!" };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  334 */   private static final String[] LLbasic1 = new String[] { "@I am Libila, slayer of good!", "I am the bottom and the end.", "I am the stone you crash upon and split open.", "No enemy survives me.", "Nothing endures me.", "I turn all to black and blood.", "In all my work you will see malevolence.", "I promise power and wealth to those who trust me.", "Would you trust me?" };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  339 */   private static final String[] VVbasic1 = new String[] { "@Vynora is my name.", "There is nothing I do not know!", "Except the secrets of the other gods, of course.", "They will not be able to hide them forever.", "Why do I feel that there is something going on?", "Me and my people will search the truth!" };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  344 */   private static final String[] FMbasic1 = new String[] { "@Hello Magranon.", "Are you strong today?", "The lava creatures that you like so much scare the other animals.", "To scare animals?", "Protection against what?", "Oh, of course.", "We can agree on that." };
/*      */ 
/*      */   
/*  347 */   private static final String[] MFbasic1 = new String[] { "", "Fo.", "Always strong. Always on the hunt!", "Hah! That's why I created them!", "No. For protection.", "Against Libila!", "She must be stopped." };
/*      */ 
/*      */   
/*  350 */   private static final String[] FMbasic2 = new String[] { "", "Go ahead.", "Are you? Oh love is all you need, really.", "You should be gentle and benevolent. Do not rush things.", "In this case it may be a poor idea.", "Surely, you must be kidding? I invented love!" };
/*      */ 
/*      */ 
/*      */   
/*  354 */   private static final String[] MFbasic2 = new String[] { "@I need your advice, Fo!", "I am in love with Vynora!", "So they say. How should I approach her?", "But I am prone to rushing things! I can't wait!", "What do you know? My love is larger than any you have ever known!" };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  359 */   private static final String[] FLbasic1 = new String[] { "@Vile creature, begone!", "Do not come near me!", "Nooo! Aaah!", "Aaah!", "My creatures will stop you.", "You have no idea.", "No, they have strong hearts and souls!" };
/*      */ 
/*      */   
/*  362 */   private static final String[] LFbasic1 = new String[] { "", "I will have you first.", "Hahaha!", "Hahaha!", "Hahaha!", "Now you are joking. They do not stand a chance.", "Oh, but I think I do. They are weak and die easily.", "That does not matter. My creatures have no heart or soul and cannot die!" };
/*      */ 
/*      */ 
/*      */   
/*  366 */   private static final String[] FLbasic2 = new String[] { "@Libila! You will stay put! Bow before my might!", "I am the light! You will suffer in my presence!", "Change your ways, Libila.", "Do not hate. Hate will consume you.", "Then you will perish." };
/*      */ 
/*      */   
/*  369 */   private static final String[] LFbasic2 = new String[] { "", "Yield! Yield to me!", "I am darkness! You will go blind and weak while with me.", "I will never! I hate you!", "Little do you know. I have mastered hate.", "I laugh at you. Hahaha!" };
/*      */ 
/*      */ 
/*      */   
/*  373 */   private static final String[] FVbasic1 = new String[] { "@Vynora, my friend.", "What have you learnt today?", "That is good.", "What would that be?", "This world is full of mysteries. I did not create them all.", "So you say. Good luck then!" };
/*      */ 
/*      */   
/*  376 */   private static final String[] VFbasic1 = new String[] { "", "Dear Fo.", "I know that the sun shines always beyond the clouds.", "You hide something from me, do you?", "I do not know. But my priests speak about it.", "Nevertheless, I will find them out." };
/*      */ 
/*      */   
/*  379 */   private static final String[] FVbasic2 = new String[] { "", "I have met her.", "She was very forthcoming, if I may say.", "I will not elaborate. What do you insinuate?", "Don't get into deep water now!" };
/*      */ 
/*      */   
/*  382 */   private static final String[] VFbasic2 = new String[] { "@Have you met the Lady of the lake yet?", "Did she please you?", "Ooh, now what does that mean?", "Oh, nothing. Nothing at all!", "Oh I know who was in deep water! Haha!" };
/*      */ 
/*      */ 
/*      */   
/*  386 */   private static final String[] MVbasic1 = new String[] { "", "Creatures?", "Yes. I made them.", "They are animated fire spirits.", "You should.", "Not from what I hear. Her creations are made some other way.", "You should. Then tell me." };
/*      */ 
/*      */   
/*  389 */   private static final String[] VMbasic1 = new String[] { "@Magranon! What are these creatures I hear about?", "Made from lava?", "How do you create life?", "I may have to try that some day.", "Is that what Libila does as well?", "I must figure that out as well then." };
/*      */ 
/*      */   
/*  392 */   private static final String[] MVbasic2 = new String[] { "@Have you figured out how Libila creates her Zombies yet?", "I wonder if what she uses can be used some other way?", "That would be fun, haha!", "Armies of cottages to crush her zombies!", "Now you're talking!", "It sure would." };
/*      */ 
/*      */   
/*  395 */   private static final String[] VMbasic2 = new String[] { "", "Not yet.", "Maybe. Maybe we could animate tables!", "Yes, and cottages.", "I could animate the sea and drown her lands!", "Well, it would suit me, wouldn't it?", "One day maybe. One day." };
/*      */ 
/*      */ 
/*      */   
/*  399 */   private static final String[] MLbasic1 = new String[] { "@Hello Libila. What are you doing here?", "Hah! I am the only hunter here!", "Now, try not to scare the children.", "Why do you hate us so much?", "Please tell me!", "Would that make you want to kill us?", "Not that much." };
/*      */ 
/*      */   
/*  402 */   private static final String[] LMbasic1 = new String[] { "", "I am here to hunt you down!", "Are you sure about that?", "Scaring is not my aim.", "Surely you must know?", "Maybe you laughed at me?", "Have you never wanted to hurt someone who laughs at you?", "You know nothing about me." };
/*      */ 
/*      */ 
/*      */   
/*  406 */   private static final String[] MLbasic2 = new String[] { "@Take this!", "And this!", "Here! That hurt, didn't it?", "Haha! Smack!", "Eat that!", "I win!" };
/*      */   
/*  408 */   private static final String[] LMbasic2 = new String[] { "", "Ouch!", "Eek!", "Ow!", "Yes, please stop!", "Buhuhuu.", "Noo, noo! Have mercy!", "Yes Magranon! You win! You win!" };
/*      */ 
/*      */ 
/*      */   
/*  412 */   private static final String[] VLbasic1 = new String[] { "@Tell me your secrets!", "Then I will hurt you. Here!", "Now will you tell me?", "Aiee!", "Maybe this time.", "Aooo! Aiie!", "Noo please stop!", ".. please.." };
/*      */ 
/*      */   
/*  415 */   private static final String[] LVbasic1 = new String[] { "", "Is this a joke? I will not!", "Ouch!", "No. But I will poke you in the eye!", "Now, will you leave me alone?", "Wait. I will hit you again!", "And again!", "... until you are dead!", "Haha! Hahaha!" };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  420 */   private static final String[] VLbasic2 = new String[] { "", "Yes. Here we are. This time I will have you!", "Friends? What trap is this?", "Oh.. interesting. But it smells funny?", "That would explain it. I am sure it is delicious.", "Argh! What is this? My skin goes pale! WHAT IS HAPPENING?", "You tricked me!", "I will return." };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  425 */   private static final String[] LVbasic2 = new String[] { "@Aaand here we are again.", "No. Let's be friends.", "It is not a trap. Here, have some apple juice.", "Do not think about that. I used apples from the ground.", "Yes it still tastes wonderful - I tried it myself.", "Oh, look. You die. How sad.", "Yes, didn't I? Don't I always?", "I think not." };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getConversationLength(boolean puppetOneStarted, Action act, Item puppetOne, Item puppetTwo, Creature performerOne, Creature performerTwo, int counter) {
/*  440 */     random.setSeed(performerOne.getWurmId());
/*  441 */     int performerTypeOne = random.nextInt(24);
/*  442 */     random.setSeed(performerTwo.getWurmId());
/*  443 */     int performerTypeTwo = random.nextInt(24);
/*  444 */     int deityOne = getDeityFor(puppetOne);
/*  445 */     int deityTwo = getDeityFor(puppetTwo);
/*      */     
/*  447 */     float courierOne = puppetOne.getSpellCourierBonus();
/*  448 */     float courierTwo = puppetTwo.getSpellCourierBonus();
/*  449 */     if (deityOne == 0)
/*  450 */       courierOne = 0.0F; 
/*  451 */     if (deityTwo == 0)
/*  452 */       courierTwo = 0.0F; 
/*  453 */     return (getConversationArrayFor(act.hashCode(), puppetOne, puppetTwo, deityOne, performerTypeOne, courierOne, deityTwo, performerTypeTwo, courierTwo)).length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean sendConversationString(Action act, Item puppetOne, Item puppetTwo, Creature performerOne, Creature performerTwo, int counter) {
/*  460 */     String toReturn = "";
/*  461 */     random.setSeed(performerOne.getWurmId());
/*  462 */     int performerTypeOne = random.nextInt(24);
/*  463 */     random.setSeed(performerTwo.getWurmId());
/*  464 */     int performerTypeTwo = random.nextInt(24);
/*  465 */     int deityOne = getDeityFor(puppetOne);
/*  466 */     int deityTwo = getDeityFor(puppetTwo);
/*      */     
/*  468 */     float courierOne = puppetOne.getSpellCourierBonus();
/*  469 */     float courierTwo = puppetTwo.getSpellCourierBonus();
/*  470 */     if (deityOne == 0)
/*  471 */       courierOne = 0.0F; 
/*  472 */     if (deityTwo == 0) {
/*  473 */       courierTwo = 0.0F;
/*      */     }
/*  475 */     String[] toUse = emptyStringArray;
/*  476 */     Skill puppeteering = null;
/*      */     
/*      */     try {
/*  479 */       puppeteering = performerOne.getSkills().getSkill(10087);
/*      */     }
/*  481 */     catch (NoSuchSkillException nss) {
/*      */       
/*  483 */       puppeteering = performerOne.getSkills().learn(10087, 1.0F);
/*      */     } 
/*  485 */     toUse = getConversationArrayFor(act.hashCode(), puppetOne, puppetTwo, deityOne, performerTypeOne, courierOne, deityTwo, performerTypeTwo, courierTwo);
/*      */     
/*  487 */     if (!((puppeteering.skillCheck((counter * (3.0F - 0.2F * puppetOne.getRarity() - 0.2F * puppetTwo.getRarity()) + (courierOne + courierTwo) / 4.0F), puppetOne, 0.0D, false, 5.0F) > 0.0D) ? 1 : 0)) {
/*      */ 
/*      */ 
/*      */       
/*  491 */       sendFailString(performerOne, puppetOne, courierOne, deityOne);
/*  492 */       if (counter < toUse.length) {
/*  493 */         return false;
/*      */       
/*      */       }
/*      */     }
/*  497 */     else if (counter < toUse.length) {
/*      */ 
/*      */ 
/*      */       
/*  501 */       if (counter == 0 && toUse[counter].equals("")) {
/*  502 */         return false;
/*      */       }
/*      */       
/*  505 */       if (counter == 0 && toUse[counter].startsWith("@")) {
/*  506 */         act.setFailSecond(1.0F);
/*      */       }
/*      */ 
/*      */       
/*  510 */       StringBuilder s = new StringBuilder();
/*  511 */       s.append(performerOne.getNamePossessive());
/*  512 */       s.append(" ");
/*  513 */       s.append(puppetOne.getName());
/*  514 */       s.append(": '");
/*  515 */       toUse[counter] = toUse[counter].replace("@", "");
/*  516 */       if (Server.rand.nextInt(100) == 0) {
/*      */         
/*  518 */         toUse[counter] = toUse[counter].replace("WHAT IS HAPPENING", "WHAT HAPPEN");
/*  519 */         toUse[counter] = toUse[counter].replace("How sad.", "We get signal.");
/*      */       } 
/*  521 */       s.append(toUse[counter]);
/*  522 */       s.append("'");
/*  523 */       toReturn = s.toString();
/*  524 */       performerOne.getCommunicator()
/*  525 */         .sendNormalServerMessage("Your " + puppetOne.getName() + ": '" + toUse[counter] + "'");
/*  526 */       Server.getInstance().broadCastAction(toReturn, performerOne, 5);
/*  527 */       return false;
/*      */     } 
/*  529 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final void sendFailString(Creature performer, Item puppet, float enchantLevel, int puppetDeity) {
/*  535 */     int failType = Server.rand.nextInt(12);
/*  536 */     String toSendPerformer = "You fail to find words. How embarrassing!";
/*  537 */     String toSendBroadCast = performer.getName() + " is silent and blushes.";
/*  538 */     boolean red = false;
/*  539 */     switch (failType) {
/*      */       
/*      */       case 0:
/*  542 */         toSendPerformer = "You forget what it was you were supposed to say!";
/*  543 */         toSendBroadCast = performer.getName() + " seems to look for words.";
/*  544 */         if (puppet.isPuppet() && Server.rand.nextInt(100) < enchantLevel) {
/*      */           
/*  546 */           red = true;
/*  547 */           int x = Server.rand.nextInt(10);
/*  548 */           if (x == 0) {
/*      */             
/*  550 */             toSendPerformer = "The " + puppet.getName() + " glares at you!";
/*      */             
/*  552 */             toSendBroadCast = performer.getName() + " looks shocked as " + performer.getHisHerItsString() + " " + puppet.getName() + " glares at " + performer.getHisHerItsString() + "!"; break;
/*      */           } 
/*  554 */           if (x == 1) {
/*      */             
/*  556 */             toSendPerformer = "Your " + puppet.getName() + " snores from boredom!";
/*      */             
/*  558 */             toSendBroadCast = performer.getName() + " looks shocked as " + performer.getHisHerItsString() + " " + puppet.getName() + " snores from boredom!"; break;
/*      */           } 
/*  560 */           if (x == 2) {
/*      */             
/*  562 */             toSendPerformer = "Your " + puppet.getName() + " burps loudly!";
/*      */             
/*  564 */             toSendBroadCast = performer.getName() + " looks at " + performer.getHisHerItsString() + " " + puppet.getName() + " with disbelief as it burps loudly!"; break;
/*      */           } 
/*  566 */           if (x == 3) {
/*      */             
/*  568 */             toSendPerformer = "The " + puppet.getName() + " gives you a menacing look. Watch out!";
/*      */             
/*  570 */             toSendBroadCast = performer.getNamePossessive() + " " + puppet.getName() + " gives " + performer.getHimHerItString() + " a dark, menacing look!"; break;
/*      */           } 
/*  572 */           if (x == 3) {
/*      */             
/*  574 */             toSendPerformer = "The " + puppet.getName() + " points its finger and laughs at you!";
/*      */             
/*  576 */             toSendBroadCast = performer.getNamePossessive() + " " + puppet.getName() + " points its finger and laughs at " + performer.getHimHerItString() + "!"; break;
/*      */           } 
/*  578 */           if (x == 4) {
/*      */             
/*  580 */             toSendPerformer = "The " + puppet.getName() + " suddenly bonks you on the head!";
/*      */             
/*  582 */             toSendBroadCast = performer.getNamePossessive() + " " + puppet.getName() + " bonks " + performer.getHimHerItString() + " in the head!"; break;
/*      */           } 
/*  584 */           if (x == 5) {
/*      */             
/*  586 */             toSendPerformer = "The " + puppet.getName() + " makes a farting sound!";
/*  587 */             toSendBroadCast = performer.getNamePossessive() + " farts loudly. Or was it the " + puppet.getName() + "?";
/*      */             break;
/*      */           } 
/*  590 */           if (puppetDeity == 4) {
/*      */             
/*  592 */             toSendPerformer = "The " + puppet.getName() + " bites you!";
/*      */             
/*  594 */             toSendBroadCast = performer.getName() + " is suddenly assaulted by " + performer.getHisHerItsString() + " " + puppet.getName() + "!";
/*  595 */             float armourMod = 1.0F;
/*      */             
/*      */             try {
/*  598 */               byte pos = performer.getBody().getRandomWoundPos();
/*      */ 
/*      */               
/*      */               try {
/*  602 */                 byte bodyPosition = ArmourTemplate.getArmourPosition(pos);
/*  603 */                 Item armour = performer.getArmour(bodyPosition);
/*  604 */                 armourMod = ArmourTemplate.calculateDR(armour, (byte)3);
/*      */                 
/*  606 */                 armour.setDamage(armour.getDamage() + 
/*  607 */                     Math.max(0.05F, Math.min(1.0F, 7000.0F * 
/*  608 */                         ArmourTemplate.getArmourDamageModFor(armour, (byte)3) / 1200000.0F * armour
/*  609 */                         .getDamageModifier())));
/*      */               }
/*  611 */               catch (NoArmourException noArmourException) {
/*      */ 
/*      */               
/*      */               }
/*  615 */               catch (NoSpaceException nsp) {
/*      */                 
/*  617 */                 logger.log(Level.WARNING, performer.getName() + " no armour space on loc " + pos);
/*      */               } 
/*  619 */               float poisdam = puppet.getSpellVenomBonus();
/*  620 */               CombatEngine.addWound(null, performer, (byte)3, pos, (5000 + Server.rand.nextInt(10000)), armourMod, "bites", null, 20.0F, Server.rand
/*  621 */                   .nextInt((int)poisdam), false, false, false, false);
/*      */             
/*      */             }
/*  624 */             catch (Exception ex) {
/*      */               
/*  626 */               logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */             } 
/*      */             break;
/*      */           } 
/*  630 */           red = false;
/*      */         } 
/*      */         break;
/*      */       case 1:
/*  634 */         toSendPerformer = "You cough!";
/*  635 */         toSendBroadCast = performer.getName() + " coughs and excuses " + performer.getHimHerItString() + "self.";
/*      */         break;
/*      */       case 2:
/*  638 */         toSendPerformer = "You find yourself mumbling and nobody hears what you say!";
/*      */         
/*  640 */         toSendBroadCast = "You can't hear what " + performer.getName() + " says because " + performer.getHeSheItString() + " mumbles.";
/*      */         break;
/*      */       case 3:
/*  643 */         toSendPerformer = "You suddenly think about something totally different than puppeteering.";
/*  644 */         toSendBroadCast = performer.getName() + " seems far away in " + performer.getHisHerItsString() + " own thoughts.";
/*      */         break;
/*      */       
/*      */       case 4:
/*  648 */         toSendPerformer = "Your mind goes totally blank.";
/*  649 */         toSendBroadCast = "The eyes of " + performer.getName() + " suddenly lose focus.";
/*      */         break;
/*      */       case 5:
/*  652 */         toSendPerformer = "You can't help yourself! You scream loudly!";
/*  653 */         toSendBroadCast = performer.getName() + " emits a high-pitched shriek! Was that part of the show?";
/*      */         break;
/*      */       case 6:
/*  656 */         toSendPerformer = "You drop something and pick it up without thinking, missing a line.";
/*  657 */         toSendBroadCast = performer.getName() + " drops something and picks it up.";
/*      */         break;
/*      */       case 7:
/*  660 */         toSendPerformer = "You wonder if a large audience is better than a small one.";
/*  661 */         toSendBroadCast = performer.getName() + " looks at you hesitantly.";
/*      */         break;
/*      */       case 8:
/*  664 */         toSendPerformer = "You think, 'Is this really a good show?'";
/*  665 */         toSendBroadCast = performer.getName() + " silently moves " + performer.getHisHerItsString() + " mouth like a fish.";
/*      */         break;
/*      */       
/*      */       case 9:
/*  669 */         toSendPerformer = "You overplay and say the line really quickly. Did anyone understand that?";
/*      */         
/*  671 */         toSendBroadCast = "You don't understand what " + performer.getName() + " say since " + performer.getHeSheItString() + " speaks too fast.";
/*      */         break;
/*      */       case 10:
/*  674 */         toSendPerformer = "You just said something totally different than you were supposed to.";
/*  675 */         toSendBroadCast = performer.getName() + " seems to just be shilly-shallying now.";
/*      */         break;
/*      */       default:
/*  678 */         toSendPerformer = "You fail to find words. How embarrassing!";
/*  679 */         toSendBroadCast = performer.getName() + " is silent and blushes.";
/*      */         break;
/*      */     } 
/*  682 */     if (red) {
/*  683 */       performer.getCommunicator().sendAlertServerMessage(toSendPerformer);
/*      */     } else {
/*  685 */       performer.getCommunicator().sendNormalServerMessage(toSendPerformer);
/*  686 */     }  Server.getInstance().broadCastAction(toSendBroadCast, performer, 5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String[] getConversationArrayFor(int actSeed, Item puppetOne, Item puppetTwo, int deityOne, int performerTypeOne, float courierOne, int deityTwo, int performerTypeTwo, float courierTwo) {
/*  694 */     if (puppetOne.isPuppet() && puppetTwo.isPuppet() && courierOne > 70.0F && courierTwo > 70.0F) {
/*  695 */       return getSeventyPlusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     }
/*      */     
/*  698 */     if (puppetOne.isPuppet() && puppetTwo.isPuppet() && courierOne > 0.0F && courierTwo > 0.0F) {
/*  699 */       return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     }
/*      */     
/*  702 */     if (!puppetOne.isPuppet() && puppetTwo.isPuppet() && courierTwo > 70.0F) {
/*  703 */       return getSeventyPlusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     }
/*      */     
/*  706 */     if (!puppetOne.isPuppet() && puppetTwo.isPuppet()) {
/*  707 */       return getBasicItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     }
/*      */     
/*  710 */     if (puppetOne.isPuppet() && !puppetTwo.isPuppet() && courierOne > 70.0F) {
/*  711 */       return getSeventyPlusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     }
/*      */     
/*  714 */     if (puppetOne.isPuppet() && !puppetTwo.isPuppet() && courierOne > 0.0F) {
/*  715 */       return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     }
/*      */     
/*  718 */     if (puppetOne.isPuppet() && puppetTwo.isPuppet() && courierOne == 0.0F) {
/*  719 */       return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     }
/*      */     
/*  722 */     if (puppetOne.isPuppet() && !puppetTwo.isPuppet() && courierOne == 0.0F) {
/*  723 */       return getRandomDeityItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     }
/*  725 */     return emptyStringArray;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String[] getSeventyPlusArray(int actSeed, int deityOne, int performerTypeOne, int deityTwo, int performerTypeTwo) {
/*  731 */     random.setSeed(actSeed);
/*  732 */     int x = random.nextInt(4);
/*  733 */     switch (performerTypeOne) {
/*      */ 
/*      */       
/*      */       case 1:
/*  737 */         if (deityOne == 1 && (performerTypeTwo == 19 || x == 0)) {
/*  738 */           return FLSeventyPlus1;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  839 */         return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 2: if (deityOne == 1 && (performerTypeTwo == 20 || x == 0)) return FLSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 3: if (deityOne == 1 && (performerTypeTwo == 7 || x == 0)) return FVSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 4: if (deityOne == 1 && (performerTypeTwo == 8 || x == 0)) return FVSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 5: if (deityOne == 1 && (performerTypeTwo == 13 || x == 0)) return FMSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 6: if (deityOne == 1 && (performerTypeTwo == 14 || x == 0)) return FMSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 7: if (deityOne == 3 && (performerTypeTwo == 3 || x == 0)) return VFSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 8: if (deityOne == 3 && (performerTypeTwo == 4 || x == 0)) return VFSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 9: if (deityOne == 3 && (performerTypeTwo == 21 || x == 0)) return VLSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 10: if (deityOne == 3 && (performerTypeTwo == 22 || x == 0)) return VLSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 11: if (deityOne == 3 && (performerTypeTwo == 17 || x == 0)) return VMSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 12: if (deityOne == 3 && (performerTypeTwo == 18 || x == 0)) return VMSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 13: if (deityOne == 2 && (performerTypeTwo == 5 || x == 0)) return MFSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 14: if (deityOne == 2 && (performerTypeTwo == 6 || x == 0)) return MFSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 15: if (deityOne == 2 && (performerTypeTwo == 23 || x == 0)) return MLSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 16: if (deityOne == 2 && (performerTypeTwo == 24 || x == 0)) return MLSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 17: if (deityOne == 2 && (performerTypeTwo == 11 || x == 0)) return MVSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 18: if (deityOne == 2 && (performerTypeTwo == 12 || x == 0)) return MVSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 19: if (deityOne == 4 && (performerTypeTwo == 1 || x == 0)) return LFSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 20: if (deityOne == 4 && (performerTypeTwo == 2 || x == 0)) return LFSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 21: if (deityOne == 4 && (performerTypeTwo == 9 || x == 0)) return LVSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 22: if (deityOne == 4 && (performerTypeTwo == 10 || x == 0)) return LVSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 23: if (deityOne == 4 && (performerTypeTwo == 15 || x == 0)) return LMSeventyPlus1;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 24: if (deityOne == 4 && (performerTypeTwo == 16 || x == 0)) return LMSeventyPlus2;  return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     } 
/*      */     return getSeventyMinusArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String[] getSeventyMinusArray(int actSeed, int deityOne, int performerTypeOne, int deityTwo, int performerTypeTwo) {
/*  847 */     if (Server.rand.nextInt(2) == 1)
/*  848 */     { switch (performerTypeOne)
/*      */       
/*      */       { 
/*      */         case 1:
/*  852 */           if (deityOne == 1) {
/*  853 */             return FLSeventyMinus1;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  954 */           return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 2: if (deityOne == 1) return FLSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 3: if (deityOne == 1) return FVSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 4: if (deityOne == 1) return FVSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 5: if (deityOne == 1) return FMSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 6: if (deityOne == 1) return FMSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 7: if (deityOne == 3) return VFSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 8: if (deityOne == 3) return VFSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 9: if (deityOne == 3) return VLSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 10: if (deityOne == 3) return VLSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 11: if (deityOne == 3) return VMSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 12: if (deityOne == 3) return VMSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 13: if (deityOne == 2) return MFSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 14: if (deityOne == 2) return MFSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 15: if (deityOne == 2) return MLSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 16: if (deityOne == 2) return MLSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 17: if (deityOne == 2) return MVSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 18: if (deityOne == 2) return MVSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 19: if (deityOne == 4) return LFSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 20: if (deityOne == 4) return LFSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 21: if (deityOne == 4) return LVSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 22: if (deityOne == 4) return LVSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 23: if (deityOne == 4) return LMSeventyMinus1;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 24: if (deityOne == 4) return LMSeventyMinus2;  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo); }  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo); }  return getBasicDeityArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String[] getSeventyPlusItemArray(int actSeed, int deityOne, int performerTypeOne, int deityTwo, int performerTypeTwo) {
/*  960 */     switch (performerTypeOne) {
/*      */ 
/*      */       
/*      */       case 1:
/*  964 */         if (deityOne == 0 && performerTypeTwo == 3) {
/*  965 */           return AFSeventyPlus1;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1036 */         return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 2: if (deityOne == 0 && performerTypeTwo == 4) return AFSeventyPlus2;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 3: if (deityOne == 1 && performerTypeTwo == 1) return FASeventyPlus1;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 4: if (deityOne == 1 && performerTypeTwo == 2) return FASeventyPlus2;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 5: if (deityOne == 0 && performerTypeTwo == 77) return AVSeventyPlus1;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 6: if (deityOne == 0 && performerTypeTwo == 8) return AVSeventyPlus2;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 7: if (deityOne == 3 && performerTypeTwo == 5) return VASeventyPlus1;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 8: if (deityOne == 3 && performerTypeTwo == 6) return VASeventyPlus2;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 9: if (deityOne == 0 && performerTypeTwo == 11) return AMSeventyPlus1;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 10: if (deityOne == 0 && performerTypeTwo == 12) return AMSeventyPlus2;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 11: if (deityOne == 2 && performerTypeTwo == 9) return MASeventyPlus1;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 12: if (deityOne == 2 && performerTypeTwo == 10) return MASeventyPlus2;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 13: if (deityOne == 0 && performerTypeTwo == 15) return ALSeventyPlus1;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 14: if (deityOne == 0 && performerTypeTwo == 16) return ALSeventyPlus2;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 15: if (deityOne == 4 && performerTypeTwo == 13) return LASeventyPlus1;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);case 16: if (deityOne == 4 && performerTypeTwo == 14) return LASeventyPlus2;  return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */     } 
/*      */     return getSeventyMinusItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */   }
/*      */   
/*      */   private static final String[] getSeventyMinusItemArray(int actSeed, int deityOne, int performerTypeOne, int deityTwo, int performerTypeTwo) {
/* 1042 */     return getBasicItemArray(actSeed, deityOne, performerTypeOne, deityTwo, performerTypeTwo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String[] getBasicDeityArray(int actSeed, int deityOne, int performerTypeOne, int deityTwo, int performerTypeTwo) {
/* 1048 */     random.setSeed(actSeed);
/* 1049 */     int x = random.nextInt(2);
/* 1050 */     switch (deityOne) {
/*      */ 
/*      */       
/*      */       case 1:
/* 1054 */         if (deityTwo == 1)
/* 1055 */           return FFbasic1; 
/* 1056 */         if (deityTwo == 4) {
/*      */           
/* 1058 */           if (x == 0)
/* 1059 */             return FLbasic1; 
/* 1060 */           return FLbasic2;
/*      */         } 
/* 1062 */         if (deityTwo == 3) {
/*      */           
/* 1064 */           if (x == 0)
/* 1065 */             return FVbasic1; 
/* 1066 */           return FVbasic2;
/*      */         } 
/* 1068 */         if (deityTwo == 2) {
/*      */           
/* 1070 */           if (x == 0)
/* 1071 */             return FMbasic1; 
/* 1072 */           return FMbasic2;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1144 */         return FFbasic1;case 3: if (deityTwo == 3) return VVbasic1;  if (deityTwo == 1) { if (x == 0) return VFbasic1;  return VFbasic2; }  if (deityTwo == 4) { if (x == 0) return VLbasic1;  return VLbasic2; }  if (deityTwo == 2) { if (x == 0) return VMbasic1;  return VMbasic2; }  return FFbasic1;case 2: if (deityTwo == 2) return MMbasic1;  if (deityTwo == 1) { if (x == 0) return MFbasic1;  return MFbasic2; }  if (deityTwo == 4) { if (x == 0) return MLbasic1;  return MLbasic2; }  if (deityTwo == 3) { if (x == 0) return MVbasic1;  return MVbasic2; }  return FFbasic1;case 4: if (deityTwo == 4) return LLbasic1;  if (deityTwo == 1) { if (x == 0) return LFbasic1;  return LFbasic2; }  if (deityTwo == 3) { if (x == 0) return LVbasic1;  return LVbasic2; }  if (deityTwo == 2) { if (x == 0) return LMbasic1;  return LMbasic2; }  return FFbasic1;
/*      */     } 
/*      */     return FFbasic1;
/*      */   }
/*      */   
/*      */   private static final String[] getRandomDeityItemArray(int actSeed, int deityOne, int performerTypeOne, int deityTwo, int performerTypeTwo) {
/* 1150 */     random.setSeed(actSeed);
/* 1151 */     int x = random.nextInt(7);
/* 1152 */     switch (deityOne) {
/*      */       
/*      */       case 1:
/* 1155 */         if (x == 0)
/* 1156 */           return FFbasic1; 
/* 1157 */         if (x == 1)
/* 1158 */           return FLbasic1; 
/* 1159 */         if (x == 2)
/* 1160 */           return FLbasic2; 
/* 1161 */         if (x == 3)
/* 1162 */           return FVbasic1; 
/* 1163 */         if (x == 4)
/* 1164 */           return FVbasic2; 
/* 1165 */         if (x == 5)
/* 1166 */           return FMbasic1; 
/* 1167 */         if (x == 6) {
/* 1168 */           return FMbasic2;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1222 */         return FFbasic1;case 3: if (x == 0) return VVbasic1;  if (x == 1) return VFbasic1;  if (x == 2) return VFbasic2;  if (x == 3) return VLbasic1;  if (x == 4) return VLbasic2;  if (x == 5) return VMbasic1;  if (x == 6) return VMbasic2;  return FFbasic1;case 2: if (x == 0) return MMbasic1;  if (x == 1) return MFbasic1;  if (x == 2) return MFbasic2;  if (x == 3) return MLbasic1;  if (x == 4) return MLbasic2;  if (x == 5) return MVbasic1;  if (x == 6) return MVbasic2;  return FFbasic1;case 4: if (x == 0) return LLbasic1;  if (x == 1) return LFbasic1;  if (x == 2) return LFbasic2;  if (x == 3) return LMbasic1;  if (x == 4) return LMbasic2;  if (x == 5) return LVbasic1;  if (x == 6) return LVbasic2;  return FFbasic1;
/*      */     } 
/*      */     return FFbasic1;
/*      */   }
/*      */   
/*      */   private static final String[] getBasicItemArray(int actSeed, int deityOne, int performerTypeOne, int deityTwo, int performerTypeTwo) {
/* 1228 */     random.setSeed(actSeed);
/* 1229 */     int x = random.nextInt(100);
/* 1230 */     if (x < 7)
/* 1231 */       return item1; 
/* 1232 */     if (x < 14)
/* 1233 */       return item2; 
/* 1234 */     if (x < 21)
/* 1235 */       return item3; 
/* 1236 */     if (x < 28)
/* 1237 */       return item4; 
/* 1238 */     if (x < 35)
/* 1239 */       return item5; 
/* 1240 */     if (x < 42)
/* 1241 */       return item6; 
/* 1242 */     if (x < 49)
/* 1243 */       return item7; 
/* 1244 */     if (x < 54)
/* 1245 */       return item8; 
/* 1246 */     if (x < 60)
/* 1247 */       return item9; 
/* 1248 */     if (x < 66)
/* 1249 */       return item10; 
/* 1250 */     if (x < 72)
/* 1251 */       return item11; 
/* 1252 */     if (x < 78)
/* 1253 */       return item12; 
/* 1254 */     if (x < 84)
/* 1255 */       return item13; 
/* 1256 */     if (x < 90)
/* 1257 */       return item14; 
/* 1258 */     if (x < 95)
/* 1259 */       return item15; 
/* 1260 */     if (x < 100)
/* 1261 */       return item16; 
/* 1262 */     return item16;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getDeityFor(Item item) {
/* 1267 */     switch (item.getTemplateId()) {
/*      */       
/*      */       case 640:
/* 1270 */         return 1;
/*      */       case 641:
/* 1272 */         return 2;
/*      */       case 642:
/* 1274 */         return 3;
/*      */       case 643:
/* 1276 */         return 4;
/*      */     } 
/* 1278 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final boolean mayPuppetMaster(Creature performer) {
/* 1284 */     Long last = puppetmasters.get(Long.valueOf(performer.getWurmId()));
/* 1285 */     if (last == null || System.currentTimeMillis() - last.longValue() > 900000L)
/* 1286 */       return true; 
/* 1287 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void startPuppeteering(Creature performer) {
/* 1292 */     puppetmasters.put(Long.valueOf(performer.getWurmId()), Long.valueOf(System.currentTimeMillis()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final long getLastPuppeteered(long performerId) {
/* 1297 */     Long last = puppetmasters.get(Long.valueOf(performerId));
/* 1298 */     if (last == null)
/* 1299 */       return 0L; 
/* 1300 */     if (System.currentTimeMillis() - last.longValue() > 900000L)
/* 1301 */       return 0L; 
/* 1302 */     return last.longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void addPuppetTime(long performerId, long lastPuppeteered) {
/* 1307 */     puppetmasters.put(Long.valueOf(performerId), Long.valueOf(lastPuppeteered));
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\Puppet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */