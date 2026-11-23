package net.augustus.cosmetics.molang;



import net.augustus.cosmetics.molang.ast.MolangVariable;
public class MolangQueryRegistrar {

   public static void registerVars(MolangParser molangParser) {
      MolangVariable molangVariable = new MolangVariable("query.anim_time", 0.0);
      molangParser.register(molangVariable);
      MolangVariable molangVariable2 = new MolangVariable("query.actor_count", 0.0);
      molangParser.register(molangVariable2);
      MolangVariable molangVariable3 = new MolangVariable("query.health", 0.0);
      molangParser.register(molangVariable3);
      MolangVariable molangVariable4 = new MolangVariable("query.max_health", 0.0);
      molangParser.register(molangVariable4);
      MolangVariable molangVariable5 = new MolangVariable("query.distance_from_camera", 0.0);
      molangParser.register(molangVariable5);
      MolangVariable molangVariable6 = new MolangVariable("query.yaw_speed", 0.0);
      molangParser.register(molangVariable6);
      MolangVariable molangVariable7 = new MolangVariable("query.is_in_water_or_rain", 0.0);
      molangParser.register(molangVariable7);
      MolangVariable molangVariable8 = new MolangVariable("query.is_in_water", 0.0);
      molangParser.register(molangVariable8);
      MolangVariable molangVariable9 = new MolangVariable("query.is_on_ground", 0.0);
      molangParser.register(molangVariable9);
      MolangVariable molangVariable10 = new MolangVariable("query.time_of_day", 0.0);
      molangParser.register(molangVariable10);
      MolangVariable molangVariable11 = new MolangVariable("query.is_on_fire", 0.0);
      molangParser.register(molangVariable11);
      MolangVariable molangVariable12 = new MolangVariable("query.ground_speed", 0.0);
      molangParser.register(molangVariable12);
   }




}
