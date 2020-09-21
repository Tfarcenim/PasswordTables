package tfar.passwordtables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfar.passwordtables.recipe.PasswordProtectedRecipe;
import tfar.passwordtables.recipe.PasswordTablesRecipeManager;
import tfar.passwordtables.recipe.Recipe;

public class PasswordTableMenu extends Container {

	/**
	 * The crafting matrix inventory (3x3).
	 */
	public PasswordInventoryCrafting craftMatrix;
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	private final World world;
	public final int size;

	private String password = "";
	/**
	 * Position of the workbench
	 */
	private final BlockPos pos;
	private final EntityPlayer player;

	public PasswordTableMenu(InventoryPlayer playerInventory, World worldIn, BlockPos posIn,int size) {
		this.world = worldIn;
		this.pos = posIn;
		this.player = playerInventory.player;
		craftMatrix = new PasswordInventoryCrafting(this, size, size);
		this.size = size;

		int craftingSlotX = 70 + size * 18;
		int craftingslotY = 8 + size * 9 + (size > 8 ? 18 : 0);

		this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, craftingSlotX, craftingslotY));

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * size, 30 + j * 18, 17 + i * 18));
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
			this.addSlotToContainer(new Slot(playerInventory, l, playerX  + l * 18, playerY + 58));
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		this.refresh(this.world, this.player, this.craftMatrix, this.craftResult);
	}

	protected void refresh(World world, EntityPlayer player, PasswordInventoryCrafting crafting, InventoryCraftResult craftResult) {
		if (!world.isRemote) {
			EntityPlayerMP entityplayermp = (EntityPlayerMP)player;
			ItemStack output = PasswordTablesRecipeManager.findRecipe(crafting).map(Recipe::getOutput).orElse(ItemStack.EMPTY);
			craftResult.setInventorySlotContents(0, output);
			entityplayermp.connection.sendPacket(new SPacketSetSlot(this.windowId, 0, output));
		}
	}

	/**
	 * Called when the container is closed.
	 */
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		if (!this.world.isRemote) {
			this.clearContainer(playerIn, this.world, this.craftMatrix);
		}
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	public boolean canInteractWith(EntityPlayer playerIn) {
		if (!(this.world.getBlockState(this.pos).getBlock() instanceof PasswordTableBlock)) {
			return false;
		} else {
			return playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	//crafting slot, followed by input slot, followed by player slot
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		int endInput = size * size;

		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0) {
				itemstack1.getItem().onCreated(itemstack1, this.world, playerIn);

				if (!this.mergeItemStack(itemstack1, endInput + 1, endInput + 37, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37) {
				if (!this.mergeItemStack(itemstack1, 28 + endInput, 37 + endInput, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 37 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 1 + endInput, 28 + endInput, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 1 + endInput, 37 + endInput, false)) {
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
