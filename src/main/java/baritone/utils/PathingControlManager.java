package baritone.utils;

import baritone.api.event.events.TickEvent;
import baritone.api.event.listener.AbstractGameEventListener;
import baritone.Baritone;
import baritone.api.pathing.calc.IPathingControlManager;
import baritone.api.pathing.goals.Goal;
import baritone.api.process.IBaritoneProcess;
import baritone.api.process.PathingCommand;
import baritone.api.process.PathingCommandType;
import baritone.api.utils.BetterBlockPos;
import baritone.behavior.PathingBehavior;
import baritone.pathing.path.PathExecutor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PathingControlManager implements IPathingControlManager {
   private final Baritone baritone;
   private final HashSet processes;
   private final List active;
   private IBaritoneProcess inControlLastTick;
   private IBaritoneProcess inControlThisTick;
   private PathingCommand command;

   @Override
   public void registerProcess(IBaritoneProcess baritoneProcess) {
      baritoneProcess.onLostControl();
      this.processes.add(baritoneProcess);
   }

   @Override
   public Optional mostRecentInControl() {
      return Optional.ofNullable(this.inControlThisTick);
   }

   public void cancelEverything() {
      this.inControlLastTick = null;
      this.inControlThisTick = null;
      this.command = null;
      this.active.clear();

      for (IBaritoneProcess baritoneProcess : (Iterable<IBaritoneProcess>)(this.processes)) {
         baritoneProcess.onLostControl();
         if (baritoneProcess.isActive() && !baritoneProcess.isTemporary()) {
            throw new IllegalStateException(baritoneProcess.displayName());
         }
      }
   }

   public static void invokePostTick(PathingControlManager pathingControlManager) {
      pathingControlManager.postTick();
   }

   public PathingControlManager(Baritone baritone2) {
      this.baritone = baritone2;
      this.processes = new HashSet();
      this.active = new ArrayList();
      baritone2.getGameEventHandler().registerEventListener(new PathingControlManager.DefaultAbstractGameEventListener(this));
   }

   public boolean forceRevalidate(Goal goal) {
      PathExecutor current2 = this.baritone.getPathingBehavior2().getCurrent2();
      if (current2 != null) {
         return goal.isInGoal(current2.getPath().getDest()) ? false : !goal.equals(current2.getPath().getGoal());
      } else {
         return false;
      }
   }

   public PathingCommand executeProcesses() {
      for (IBaritoneProcess baritoneProcess : (Iterable<IBaritoneProcess>)(this.processes)) {
         if (baritoneProcess.isActive()) {
            if (!this.active.contains(baritoneProcess)) {
               this.active.add(0, baritoneProcess);
            }
         } else {
            this.active.remove(baritoneProcess);
         }
      }

      this.active.sort(Comparator.comparingDouble(IBaritoneProcess::priority).reversed());
      Iterator it = this.active.iterator();

      while (it.hasNext()) {
         IBaritoneProcess baritoneProcess2 = (IBaritoneProcess)it.next();
         PathingCommand pathingCommand = baritoneProcess2.onTick(Objects.equals(baritoneProcess2, this.inControlLastTick) && this.baritone.getPathingBehavior2().isCalcFailedLastTick(), this.baritone.getPathingBehavior2().isSafeToCancel());
         if (pathingCommand == null) {
            if (baritoneProcess2.isActive()) {
               throw new IllegalStateException(baritoneProcess2.displayName() + " actively returned null PathingCommand");
            }
         } else if (pathingCommand.commandType != PathingCommandType.DEFER) {
            this.inControlThisTick = baritoneProcess2;
            if (!baritoneProcess2.isTemporary()) {
               it.forEachRemaining(obj -> ((IBaritoneProcess)obj).onLostControl());
            }

            return pathingCommand;
         }
      }

      return null;
   }

   public boolean revalidateGoal(Goal goal) {
      PathExecutor current2 = this.baritone.getPathingBehavior2().getCurrent2();
      if (current2 != null) {
         Goal goal2 = current2.getPath().getGoal();
         BetterBlockPos dest = current2.getPath().getDest();
         if (goal2.isInGoal(dest) && !goal.isInGoal(dest)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public Optional mostRecentCommand() {
      return Optional.ofNullable(this.command);
   }

   private void postTick() {
      if (this.command != null) {
         PathingBehavior pathingBehavior2 = this.baritone.getPathingBehavior2();
         switch (PathingControlManager.PathingCommandTypeSwitchMap.ORDINAL_MAP[this.command.commandType.ordinal()]) {
            case 4:
               if (this.command.goal == null || this.forceRevalidate(this.command.goal) || this.revalidateGoal(this.command.goal)) {
                  pathingBehavior2.softCancelIfSafe();
               }

               pathingBehavior2.secretInternalSetGoalAndPath(this.command);
               break;
            case 5:
               if ((Boolean)Baritone.settings().cancelOnGoalInvalidation.value && (this.command.goal == null || this.revalidateGoal(this.command.goal))) {
                  pathingBehavior2.softCancelIfSafe();
               }

               pathingBehavior2.secretInternalSetGoalAndPath(this.command);
         }
      }
   }

   public void preTick() {
      this.inControlLastTick = this.inControlThisTick;
      this.inControlThisTick = null;
      PathingBehavior pathingBehavior2 = this.baritone.getPathingBehavior2();
      this.command = this.executeProcesses();
      if (this.command == null) {
         pathingBehavior2.cancelSegmentIfSafe();
         pathingBehavior2.setGoal((Goal)null);
      } else {
         if (!Objects.equals(this.inControlThisTick, this.inControlLastTick) && this.command.commandType != PathingCommandType.REQUEST_PAUSE && this.inControlLastTick != null && !this.inControlLastTick.isTemporary()) {
            pathingBehavior2.cancelSegmentIfSafe();
         }

         switch (PathingControlManager.PathingCommandTypeSwitchMap.ORDINAL_MAP[this.command.commandType.ordinal()]) {
            case 1:
               pathingBehavior2.secretInternalSetGoalAndPath(this.command);
            case 2:
               pathingBehavior2.requestPause();
               break;
            case 3:
               pathingBehavior2.setGoal(this.command.goal);
               pathingBehavior2.cancelSegmentIfSafe();
               break;
            case 4:
            case 5:
               if (!pathingBehavior2.isPathing() && !pathingBehavior2.getInProgress().isPresent()) {
                  pathingBehavior2.secretInternalSetGoalAndPath(this.command);
               }
               break;
            case 6:
               if (this.command.goal != null) {
                  pathingBehavior2.secretInternalSetGoalAndPath(this.command);
               }
               break;
            default:
               throw new IllegalStateException();
         }
      }
   }


   public static class DefaultAbstractGameEventListener implements AbstractGameEventListener {
      public final PathingControlManager pathingControlManager;

      @Override
      public void onTick(TickEvent tickEvent) {
         if (tickEvent.getType() == TickEvent.Type.IN) {
            PathingControlManager.invokePostTick(this.pathingControlManager);
         }
      }

      public DefaultAbstractGameEventListener(PathingControlManager pathingControlManager2) {
         this.pathingControlManager = pathingControlManager2;
      }
   }

   public static class PathingCommandTypeSwitchMap {
      public static final int[] ORDINAL_MAP = new int[PathingCommandType.values().length];

      static {
         try {
            ORDINAL_MAP[PathingCommandType.SET_GOAL_AND_PAUSE.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[PathingCommandType.REQUEST_PAUSE.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[PathingCommandType.CANCEL_AND_SET_GOAL.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[PathingCommandType.FORCE_REVALIDATE_GOAL_AND_PATH.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[PathingCommandType.REVALIDATE_GOAL_AND_PATH.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORDINAL_MAP[PathingCommandType.SET_GOAL_AND_PATH.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }
      }
   }
}
