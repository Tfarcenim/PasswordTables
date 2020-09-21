package tfar.passwordtables;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import tfar.passwordtables.designer.PasswordTableDesignerBlock;
import tfar.passwordtables.designer.PasswordTableDesignerBlockEntity;

@Mod.EventBusSubscriber
public class RegistryEvents {

	public static Block THREE;
	public static Block FIVE;
	public static Block SEVEN;
	public static Block NINE;

	public static Block DESIGNER;

	@SubscribeEvent
	public static void blocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.registerAll(
						THREE = registerTable(3),
						FIVE = registerTable(5),
						SEVEN = registerTable(7),
						NINE = registerTable(9)
		);
		event.getRegistry().register(DESIGNER = new PasswordTableDesignerBlock(Material.WOOD)
						.setCreativeTab(CreativeTabs.DECORATIONS)
						.setRegistryName("designer")
						.setTranslationKey(PasswordTables.MODID+".designer"));
		GameRegistry.registerTileEntity(PasswordTableDesignerBlockEntity.class,new ResourceLocation(PasswordTables.MODID,"designer"));
	}

	public static Block registerTable(int size) {
		return new PasswordTableBlock(Material.WOOD,size).setHardness(2).setCreativeTab(CreativeTabs.DECORATIONS)
						.setRegistryName(size + "x" + size).setTranslationKey(PasswordTables.MODID+"."+size + "x" + size);
	}

	@SubscribeEvent
	public static void items(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.registerAll(
						new ItemBlock(THREE).setRegistryName(THREE.getRegistryName()).setTranslationKey(THREE.getTranslationKey()),
						new ItemBlock(FIVE).setRegistryName(FIVE.getRegistryName()).setTranslationKey(FIVE.getTranslationKey()),
						new ItemBlock(SEVEN).setRegistryName(SEVEN.getRegistryName()).setTranslationKey(SEVEN.getTranslationKey()),
						new ItemBlock(NINE).setRegistryName(NINE.getRegistryName()).setTranslationKey(NINE.getTranslationKey()),
						new ItemBlock(DESIGNER).setRegistryName(DESIGNER.getRegistryName()).setTranslationKey(DESIGNER.getTranslationKey())
		);
	}

	@SubscribeEvent
	public static void models(ModelRegistryEvent event) {
		registerItemModel(THREE);
		registerItemModel(FIVE);
		registerItemModel(SEVEN);
		registerItemModel(NINE);
		registerItemModel(DESIGNER);
	}

	public static void registerItemModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),0,new ModelResourceLocation(Item.getItemFromBlock(block).getRegistryName(), "inventory"));
	}
}
