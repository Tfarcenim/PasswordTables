package tfar.passwordtables;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PasswordTableBlock extends Block {

	public final int size;

	public PasswordTableBlock(Material materialIn,int size) {
		super(materialIn);
		this.size = size;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
		player.openGui(PasswordTables.instance, size, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
