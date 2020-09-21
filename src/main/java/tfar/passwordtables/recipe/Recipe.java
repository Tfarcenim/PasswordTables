package tfar.passwordtables.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import tfar.passwordtables.PasswordInventoryCrafting;

import java.util.List;

public interface Recipe {

	List<Ingredient> getInputs();

	ItemStack getOutput();

	boolean matches(PasswordInventoryCrafting inv);

}
