package tfar.passwordtables.designer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfar.passwordtables.PasswordTables;

import javax.annotation.Nullable;

public class PasswordTableDesignerBlock extends Block {
	public PasswordTableDesignerBlock(Material materialIn) {
		super(materialIn);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
		player.openGui(PasswordTables.instance, 18, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new PasswordTableDesignerBlockEntity();
	}
}
