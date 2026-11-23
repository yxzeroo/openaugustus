package net.augustus.quests;

import net.augustus.utils.UnaryFunction;
import java.util.HashMap;

public class QuestObjectiveFactory {
   private static final HashMap<Integer, UnaryFunction> OBJECTIVE_CREATORS = new HashMap<>();

   static {
      OBJECTIVE_CREATORS.put(0, obj -> new AttackPlayersQuestObjective((int)obj));
      OBJECTIVE_CREATORS.put(1, obj2 -> new JumpQuestObjective((int)obj2));
      OBJECTIVE_CREATORS.put(2, obj3 -> new LeftClickQuestObjective((int)obj3));
      OBJECTIVE_CREATORS.put(3, obj4 -> new MoveBlocksQuestObjective((int)obj4));
      OBJECTIVE_CREATORS.put(4, obj5 -> new PlaceBlocksQuestObjective((int)obj5));
      OBJECTIVE_CREATORS.put(5, obj6 -> new RightClickQuestObjective((int)obj6));
      OBJECTIVE_CREATORS.put(6, obj7 -> new SneakBlocksQuestObjective((int)obj7));
   }

   public static QuestObjective createObjective(int n, int n2) {
      UnaryFunction unaryFunction = (UnaryFunction)OBJECTIVE_CREATORS.get(n);
      Integer num = n2;
      return (QuestObjective)unaryFunction.apply(num);
   }
}
