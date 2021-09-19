package com.wurmonline.server;

import com.wurmonline.server.creatures.NoSuchCreatureException;

public interface MovementListener {
  void creatureMoved(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2) throws NoSuchCreatureException, NoSuchPlayerException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\MovementListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */