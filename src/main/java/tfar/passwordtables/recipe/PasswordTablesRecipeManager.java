package tfar.passwordtables.recipe;

import tfar.passwordtables.PasswordInventoryCrafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PasswordTablesRecipeManager {

	private static final List<PasswordProtectedRecipe> recipes = new ArrayList<>();


	public static void addRecipe(PasswordProtectedRecipe recipe) {
		recipes.add(recipe);
	}

	public static Optional<PasswordProtectedRecipe> findRecipe(PasswordInventoryCrafting inv) {
		return recipes.stream().filter(passwordProtectedRecipe -> passwordProtectedRecipe.matches(inv)).findFirst();
	}
}
