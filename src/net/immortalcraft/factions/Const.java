package net.immortalcraft.factions;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.massivecraft.mcore.util.MUtil;

public class Const
{
	// Collections & Aspects
        public static final String BASENAME = "factions";
        public static final String BASENAME_ = BASENAME+"_";
	
	// Aspect Ids
	public static final String COLLECTION_BOARD = BASENAME_+"board";
        public static final String COLLECTION_FACTION = BASENAME_+"faction";
        public static final String COLLECTION_UPLAYER = BASENAME_+"uplayer";
        public static final String COLLECTION_MPLAYER = BASENAME_+"mplayer";
        public static final String COLLECTION_UCONF = BASENAME_+"uconf";
        public static final String COLLECTION_MCONF = BASENAME_+"mconf";
  
        public static final String ASPECT = BASENAME;
	
	// ASCII Map
	public static final int MAP_HEIGHT = 8;
	public static final int MAP_WIDTH = 39;
	public static final char[] MAP_KEY_CHARS = "\\/#?$%=&^ABCDEFGHJKLMNOPQRSTUVWXYZ1234567890abcdeghjmnopqrsuvwxyz".toCharArray();
	
	// Enumerations
	
	public static final Set<Material> MATERIALS_EDIT_ON_INTERACT = MUtil.set(
		Material.DIODE_BLOCK_OFF,
		Material.DIODE_BLOCK_ON,
		Material.NOTE_BLOCK,
		Material.CAULDRON,
		Material.SOIL
	);
	
	public static final Set<Material> MATERIALS_EDIT_TOOLS = MUtil.set(
		Material.FIREBALL,
		Material.FLINT_AND_STEEL,
		Material.BUCKET,
		Material.WATER_BUCKET,
		Material.LAVA_BUCKET
	);
	
	public static final Set<Material> MATERIALS_DOOR = MUtil.set(
		Material.WOODEN_DOOR,
		Material.TRAP_DOOR,
		Material.FENCE_GATE
	);
	
	public static final Set<Material> MATERIALS_CONTAINER = MUtil.set(
		Material.DISPENSER,
		Material.CHEST,
		Material.FURNACE,
		Material.BURNING_FURNACE,
		Material.JUKEBOX,
		Material.BREWING_STAND,
		Material.ENCHANTMENT_TABLE,
		Material.ANVIL,
		Material.BEACON,
		Material.TRAPPED_CHEST,
		Material.HOPPER,
		Material.DROPPER
	);
	
	public static final Set<EntityType> ENTITY_TYPES_MONSTERS = MUtil.set(
		EntityType.BLAZE,
		EntityType.CAVE_SPIDER,
		EntityType.CREEPER,
		EntityType.ENDERMAN,
		EntityType.ENDER_DRAGON,
		EntityType.GHAST,
		EntityType.GIANT,
		EntityType.MAGMA_CUBE,
		EntityType.PIG_ZOMBIE,
		EntityType.SILVERFISH,
		EntityType.SKELETON,
		EntityType.SLIME,
		EntityType.SPIDER,
		EntityType.WITCH,
		EntityType.WITHER,
                EntityType.ZOMBIE
	);
}
