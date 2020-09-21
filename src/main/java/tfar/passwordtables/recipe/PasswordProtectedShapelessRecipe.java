package tfar.passwordtables.recipe;

import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import tfar.passwordtables.PasswordInventoryCrafting;

import javax.annotation.Nonnull;
import java.util.List;

public class PasswordProtectedShapelessRecipe implements PasswordProtectedRecipe {

	protected String password;
	@Nonnull
	protected ItemStack output;
	protected List<Ingredient> input;
	protected ResourceLocation group;
	protected boolean isSimple = true;

	public PasswordProtectedShapelessRecipe(ResourceLocation group, List<Ingredient> input, @Nonnull ItemStack result,String password) {
		this.group = group;
		output = result.copy();
		this.input = input;
		for (Ingredient i : input)
			this.isSimple &= i.isSimple();
		this.password = password;
	}


	/**
	 * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
	 * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
	 */
	@Nonnull
	public ItemStack getIcon() {
		return output;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Nonnull
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		return output.copy();
	}

	@Nonnull
	public List<Ingredient> getIngredients() {
		return this.input;
	}

	/**
	 * Recipes with equal group are combined into one button in the recipe book
	 */
	@Nonnull
	public String getGroup() {
		return this.group == null ? "" : this.group.toString();
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 */
	public boolean canFit(int width, int height) {
		return width * height >= this.input.size();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public List<Ingredient> getInputs() {
		return input;
	}

	@Override
	@Nonnull
	public ItemStack getOutput() {
		return output.copy();
	}


	@Override
	public boolean matches(PasswordInventoryCrafting inv) {

		if (!inv.getPassword().equals(password)) {
			return false;
		}

		int ingredientCount = 0;
		RecipeHelper recipeItemHelper = new RecipeHelper();
		List<ItemStack> items = Lists.newArrayList();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				++ingredientCount;
				if (this.isSimple)
					recipeItemHelper.accountStack(itemstack, 1);
				else
					items.add(itemstack);
			}
		}

		if (ingredientCount != this.input.size())
			return false;

		if (this.isSimple)
			return recipeItemHelper.canCraft(this, null);

		return RecipeMatcher.findMatches(items, this.input) != null;
	}
}
