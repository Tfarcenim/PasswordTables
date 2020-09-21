package tfar.passwordtables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import tfar.passwordtables.designer.PasswordTableDesignerMenu;
import tfar.passwordtables.designer.PasswordTableDesignerScreen;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
		/**
		 * Returns a Server side Container to be displayed to the user.
		 *
		 * @param ID     The Gui ID Number
		 * @param player The player viewing the Gui
		 * @param world  The current world
		 * @param x      X Position
		 * @param y      Y Position
		 * @param z      Z Position
		 * @return A GuiScreen/Container to be displayed to the user, null if none.
		 */
		@Nullable
		@Override
		public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
			return ID == 18 ? createMenuD(ID, player, world, x, y, z) : createMenu(ID, player, world, x, y, z);
		}

		public PasswordTableMenu createMenu(int ID, EntityPlayer player, World world, int x, int y, int z) {
			return new PasswordTableMenu(player.inventory, world, new BlockPos(x, y, z), ID);
		}
	public PasswordTableDesignerMenu createMenuD(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new PasswordTableDesignerMenu(player.inventory, world, new BlockPos(x, y, z));
	}

		/**
		 * Returns a Container to be displayed to the user. On the client side, this
		 * needs to return a instance of GuiScreen On the server side, this needs to
		 * return a instance of Container
		 *
		 * @param ID     The Gui ID Number
		 * @param player The player viewing the Gui
		 * @param world  The current world
		 * @param x      X Position
		 * @param y      Y Position
		 * @param z      Z Position
		 * @return A GuiScreen/Container to be displayed to the user, null if none.
		 */
		@Nullable
		@Override
		public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
			return ID == 18 ?  new PasswordTableDesignerScreen(createMenuD(ID, player, world, x, y, z))  :new PasswordTableScreen(createMenu(ID, player, world, x, y, z));
		}
}
