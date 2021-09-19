package com.wurmonline.server.loot;

import com.wurmonline.server.creatures.Creature;
import java.util.Optional;

public interface LootItemFunc {
  Optional<LootItem> item(Creature paramCreature1, Creature paramCreature2, LootPool paramLootPool);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\LootItemFunc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */