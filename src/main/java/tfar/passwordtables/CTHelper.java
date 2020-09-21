package tfar.passwordtables;

import com.mojang.realmsclient.util.Pair;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.Sys;
import tfar.passwordtables.designer.PasswordTableDesignerBlockEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CTHelper {
	public static NonNullList<Ingredient> createIngredientList(IIngredient[][] ingredients) {
		int height = ingredients.length;
		int width = 0;
		for(IIngredient[] ingredientLine : ingredients) {
			width = Math.max(width, ingredientLine.length);
		}

		NonNullList<Ingredient> ingredientList = NonNullList.withSize(width * height, Ingredient.EMPTY);
		for(int row = 0; row < ingredients.length; row++) {
			for(int column = 0; column < ingredients[row].length; column++) {
				if(ingredients[row][column] != null)
					ingredientList.set(row * width + column, CraftTweakerMC.getIngredient(ingredients[row][column]));
			}
		}
		return ingredientList;
	}

	public static NonNullList<Ingredient> createIngredientList(IIngredient[] ingredients) {
		NonNullList<Ingredient> ingredientList = NonNullList.withSize(ingredients.length, Ingredient.EMPTY);
		for(int index = 0; index < ingredients.length; index++) {
			IIngredient ingredient = ingredients[index];
			if(ingredient != null)
				ingredientList.set(index, CraftTweakerMC.getIngredient(ingredients[index]));
		}
		return ingredientList;
	}

	private static final String NEW_LINE = System.lineSeparator();
	private static final String TAB = "\t";


	public static String toCTScript(@Nonnull final PasswordTableDesignerBlockEntity passwordTableDesignerBlockEntity,boolean shaped, String password) {
		ItemStackHandler handler = passwordTableDesignerBlockEntity.handler;
		final int outputSlot = 0;
		final ItemStack outputStack = handler.getStackInSlot(outputSlot);
		if (outputStack.isEmpty() || password.isEmpty())
			return null;

		password = "\"" + password + "\"";

		final StringBuilder scriptBuilder = new StringBuilder();

		scriptBuilder.append(CTMethods.PREFIX);

		scriptBuilder.append(".");

		if (shaped) {
			scriptBuilder.append("addShaped(");

			Pair<Integer,Integer> topLeft = getTopLeft(handler);

			Pair<Integer,Integer> bottomRight = getBottomRight(handler);

			StringJoiner joiner1 = new StringJoiner(",");

			for (int j = topLeft.first();j < bottomRight.first() + 1; j++) {

				StringJoiner joiner = new StringJoiner(",");
				for (int i1 = topLeft.second(); i1 < bottomRight.second() + 1; i1++) {
					ItemStack stackInSlot = handler.getStackInSlot(9 * j + i1 + 1);
					IItemStack iItemStack = CraftTweakerMC.getIItemStack(stackInSlot);
					String toString = iItemStack == null ? "null" : iItemStack.toString();
					joiner.add(toString);
				}
				String line = joiner.toString();
				line = "["+line+"]";
				joiner1.add(line);
			}

			String ingredients = "[" + joiner1.toString()+"]";

			StringJoiner joiner2 = new StringJoiner(",");

			joiner2.add(ingredients);
			joiner2.add(CraftTweakerMC.getIItemStack(outputStack).toString());
			joiner2.add(password);

			scriptBuilder.append(joiner2.toString());

			scriptBuilder.append(");");


		} else {
			scriptBuilder.append("addShapeless(");

			String ingredients = IntStream.range(1, handler.getSlots()).mapToObj(handler::getStackInSlot).filter(stack -> !stack.isEmpty())
							.map(CraftTweakerMC::getIItemStack).map(IItemStack::toString).collect(Collectors.joining(","));
			ingredients = "[" + ingredients + "]";

			StringJoiner joiner2 = new StringJoiner(",");

			joiner2.add(ingredients);
			joiner2.add(CraftTweakerMC.getIItemStack(outputStack).toString());
			joiner2.add(password);

			scriptBuilder.append(joiner2.toString());

			scriptBuilder.append(");");

		}

		scriptBuilder.append(NEW_LINE).append(NEW_LINE);
		return scriptBuilder.toString();
	}

	public static Pair<Integer,Integer> getTopLeft(ItemStackHandler handler) {
		return Pair.of(findTop(handler),findLeft(handler));
	}

	public static int findTop(ItemStackHandler handler) {
		for (int i = 0; i < 9;i++) {
			for (int j = 0; j < 9; j++) {
				ItemStack stack = handler.getStackInSlot((9 * i + j + 1));
				if (!stack.isEmpty()) {
					return i;
				}
			}
		}
		return  -1;
	}

	public static int findLeft(ItemStackHandler handler) {
		for (int i = 0; i < 9;i++) {
			for (int j = 0; j < 9; j++) {
				ItemStack stack = handler.getStackInSlot((9 * j + i + 1));
				if (!stack.isEmpty()) {
					return i;
				}
			}
		}
		return  -1;
	}

	public static Pair<Integer,Integer> getBottomRight(ItemStackHandler handler) {
		return Pair.of(findBottom(handler),findRight(handler));
	}

	public static int findBottom(ItemStackHandler handler) {
		for (int i = 8; i >= 0;i--) {
			for (int j = 0; j < 9; j++) {
				ItemStack stack = handler.getStackInSlot((9 * i + j + 1));
				if (!stack.isEmpty()) {
					return i;
				}
			}
		}
		return  -1;
	}

	public static int findRight(ItemStackHandler handler) {
		for (int i = 8; i >= 0;i--) {
			for (int j = 0; j < 9; j++) {
				ItemStack stack = handler.getStackInSlot((9 * j + i + 1));
				if (!stack.isEmpty()) {
					return i;
				}
			}
		}
		return  -1;
	}

}
