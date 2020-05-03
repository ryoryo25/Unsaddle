package ryoryo.unsaddle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Unsaddle.MOD_ID, name = Unsaddle.MOD_NAME, version = Unsaddle.MOD_VERSION, dependencies = Unsaddle.MOD_DEPENDENCIES, acceptedMinecraftVersions = Unsaddle.MOD_ACCEPTED_MC_VERSIONS)
public class Unsaddle {
	public static final String MOD_ID = "unsaddle";
	public static final String MOD_NAME = "Unsaddle";

	public static final String MOD_VERSION_MAJOR = "GRADLE.VERSION_MAJOR";
	public static final String MOD_VERSION_MINOR = "GRADLE.VERSION_MINOR";
	public static final String MOD_VERSION_PATCH = "GRADLE.VERSION_PATCH";
	public static final String MOD_VERSION = MOD_VERSION_MAJOR + "." + MOD_VERSION_MINOR + "." + MOD_VERSION_PATCH;

	public static final String MOD_DEPENDENCIES = "required-after:forge@[14.23.5.2768,);";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12.2]";

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new Unsaddle());
	}

	@SubscribeEvent
	public void removePigSaddle(PlayerInteractEvent.EntityInteract event) {
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		ItemStack held = event.getItemStack();
		World world = event.getWorld();

		if(player != null && target != null) {
			if(target instanceof EntityPig && player.isSneaking()) {
				EntityPig pig = (EntityPig) target;
				if(pig.getSaddled()) {
					// to interrupt vanilla's function
					if(held.isItemEqual(new ItemStack(Items.SADDLE)))
						event.setCanceled(true);

					pig.world.playSound(player, pig.posX, pig.posY, pig.posZ, SoundEvents.ENTITY_PIG_SADDLE, SoundCategory.NEUTRAL, 0.5F, 1.0F);
					if(!world.isRemote) {
						pig.dropItem(Items.SADDLE, 1);
						pig.setSaddled(false);
					}

					event.setResult(Result.ALLOW);
				}
			}
		}
	}
}