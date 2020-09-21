package tfar.passwordtables;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tfar.passwordtables.recipe.PasswordProtectedShapedCraftingRecipe;
import tfar.passwordtables.recipe.PasswordProtectedShapelessRecipe;
import tfar.passwordtables.recipe.PasswordTablesRecipeManager;

import java.util.List;

@ZenRegister
@ZenClass(CTMethods.PREFIX)
public class CTMethods {

	public static final String PREFIX = "mods.passwordtables.PasswordTables";

	static  int id = 0;
	@ZenMethod
	public static void addShaped(IIngredient[][] iIngredients, IItemStack output, String password) {
		int height = iIngredients.length;
		int width = 0;
		for(IIngredient[] ingredientLine : iIngredients) {
			width = Math.max(width, ingredientLine.length);
		}

		List<Ingredient> ingredientList = CTHelper.createIngredientList(iIngredients);
		id++;
		PasswordTablesRecipeManager.addRecipe(new PasswordProtectedShapedCraftingRecipe(new ResourceLocation(PasswordTables.MODID,String.valueOf(id)), ingredientList,width,height, CraftTweakerMC.getItemStack(output),password));
	}

	@ZenMethod
	public static void addShapeless(IIngredient[] iIngredients, IItemStack output, String password) {
		List<Ingredient> ingredientList = CTHelper.createIngredientList(iIngredients);
		id++;
		PasswordTablesRecipeManager.addRecipe(new PasswordProtectedShapelessRecipe(new ResourceLocation(PasswordTables.MODID,String.valueOf(id)), ingredientList, CraftTweakerMC.getItemStack(output),password));
	}
}
