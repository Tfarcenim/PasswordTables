package tfar.passwordtables;

import net.minecraft.inventory.InventoryCrafting;

public class PasswordInventoryCrafting extends InventoryCrafting {

	protected final PasswordTableMenu menu;

	public PasswordInventoryCrafting(PasswordTableMenu eventHandlerIn, int width, int height) {
		super(eventHandlerIn, width, height);
		this.menu = eventHandlerIn;
	}

	public String getPassword() {
		return menu.getPassword();
	}
}
