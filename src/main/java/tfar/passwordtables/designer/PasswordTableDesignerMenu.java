package tfar.passwordtables.designer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import tfar.passwordtables.recipe.PasswordTablesRecipeManager;
import tfar.passwordtables.recipe.Recipe;

public class PasswordTableDesignerMenu extends Container {

	public final World world;
	public final BlockPos pos;
	/**
	 * The crafting matrix inventory (3x3).
	 */
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	public final int size;

	private String password = "";

	public final EntityPlayer player;

	private final ItemStackHandler handler;

	public PasswordTableDesignerMenu(InventoryPlayer playerInventory, World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
		size = 9;
		this.player = playerInventory.player;

		PasswordTableDesignerBlockEntity passwordTableDesignerBlockEntity = (PasswordTableDesignerBlockEntity)world.getTileEntity(pos);

		handler = passwordTableDesignerBlockEntity.handler;

		int craftingSlotX = 70 + size * 18;
		int craftingslotY = 8 + size * 9 + (size > 8 ? 18 : 0);

		this.addSlotToContainer(new SlotItemHandler(handler, 0, craftingSlotX, craftingslotY));

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				this.addSlotToContainer(new SlotItemHandler(handler, j + i * size+1, 30 + j * 18, 17 + i * 18));
			}
		}

		int playerX = -19 + 9 * Math.min(size,7);

		int playerY = 30 + 18 * size;

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, playerX + i1 * 18, playerY + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(playerInventory, l, playerX + l * 18, playerY + 58));
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory inventoryIn) {
	}

	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 0) {

				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 37 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	public void setPassword(String password) {
		this.password = password;
		this.onCraftMatrixChanged(null);
	}

	/**
	 * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
	 * is null for the initial slot that was double-clicked.
	 */
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
	}

	public String getPassword() {
		return password;
	}
}
