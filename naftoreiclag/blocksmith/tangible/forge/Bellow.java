package naftoreiclag.blocksmith.tangible.forge;

import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import naftoreiclag.blocksmith.ModBlocksmith;
import naftoreiclag.blocksmith.vector.Smaterial;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Bellow extends Block
{
	
	public Bellow(int id, Material material)
	{
		super(id, material);
		setStepSound(soundMetalFootstep);
		setCreativeTab(ModBlocksmith.creativetab_smithing);
		setBlockBounds(0.125f, 0.0f, 0.125f, 0.875f, 0.5f, 0.875f);
	}
	
	/**
	 * Called when the block is placed. world, x, y, z, side, hitX, hitY, hitZ, metadata
	 * Returns metadata
	 */
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hx, float hy, float hz, int metadata)
	{
		switch(side)
		{
			case 2: return 2;
			case 3: return 0;
			case 4: return 3;
			case 5: return 1;
			default: return 0;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	// Don't render any of the sides
	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
	   return false;
	}
	
	// This is not a solid cube
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	// Tile entity
	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}
	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new BellowTentity();
	}
}
