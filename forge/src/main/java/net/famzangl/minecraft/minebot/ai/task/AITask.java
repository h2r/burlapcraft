package net.famzangl.minecraft.minebot.ai.task;

import com.kcaluru.burlapbot.helpers.BurlapAIHelper;


public abstract class AITask
{
  public abstract boolean isFinished(BurlapAIHelper paramAIHelper);

  public abstract void runTick(BurlapAIHelper paramAIHelper);

  public int getGameTickTimeout()
  {
    return 100;
  }
}