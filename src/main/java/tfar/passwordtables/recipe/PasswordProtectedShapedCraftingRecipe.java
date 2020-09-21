package tfar.passwordtables.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import tfar.passwordtables.PasswordInventoryCrafting;

import javax.annotation.Nonnull;
import java.util.List;

public class PasswordProtectedShapedCraftingRecipe implements PasswordProtectedRecipe {

	protected String password;
	@Nonnull
	protected ItemStack output;
	protected List<Ingredient> input;
	protected int width;
	protected int height;
	protected boolean mirrored = true;
	protected ResourceLocation group;

	public PasswordProtectedShapedCraftingRecipe(ResourceLocation group, List<Ingredient> inputs, int width, int height, @Nonnull ItemStack result, String password) {
		this.group = group;
		this.input = inputs;
		output = result;
		this.password = password;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Nonnull
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		return output.copy();
	}

	/**
	 * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
	 * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
	 */
	@Nonnull
	public ItemStack getRecipeOutput() {
		return output;
	}

	/**
	 * Based on {@link net.minecraft.item.crafting.ShapedRecipes#checkMatch(InventoryCrafting, int, int, boolean)}
	 */
	protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
		for (int x = 0; x < inv.getWidth(); x++) {
			for (int y = 0; y < inv.getHeight(); y++) {
				int subX = x - startX;
				int subY = y - startY;
				Ingredient target = Ingredient.EMPTY;

				if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
					if (mirror) {
						target = input.get(width - subX - 1 + subY * width);
					} else {
						target = input.get(subX + subY * width);
					}
				}

				if (!target.apply(inv.getStackInRowAndColumn(x, y))) {
					return false;
				}
			}
		}

		return true;
	}

	public PasswordProtectedShapedCraftingRecipe setMirrored(boolean mirror) {
		mirrored = mirror;
		return this;
	}

	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
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
		return width >= this.width && height >= this.height;
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

		for (int x = 0; x <= inv.getWidth() - width; x++) {
			for (int y = 0; y <= inv.getHeight() - height; ++y) {
				if (checkMatch(inv, x, y, false)) {
					return true;
				}

				if (mirrored && checkMatch(inv, x, y, true)) {
					return true;
				}
			}
		}
		return false;
	}
}