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

public class Grate extends Block
{
	private Icon blockIcon2;
	private Icon blockIcon3;
	
	public Grate(int id, Material material)
	{
		super(id, material);
		setStepSound(soundMetalFootstep);
		setCreativeTab(ModBlocksmith.creativetab_smithing);
	}
	
	/**
	 * Called when the block is placed. world, x, y, z, side, hitX, hitY, hitZ, metadata
	 * Returns metadata
	 */
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hx, float hy, float hz, int metadata)
	{
		//ModBlocksmith.logSide("Block placed on side: " + side);
		
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
	
	// Block bounds
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		//ModBlocksmith.logSide("Setting block bounds");
		
		int m = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		
		switch(m)
		{
			// North
			case 0:
			{
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
				return;
			}
			
			// West
			case 1:
			{
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
				return;
			}
			
			// South
			case 2:
			{
				this.setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
				return;
			}
			
			// East
			case 3:
			{
				this.setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				return;
			}
			
			// Open North
			case 4:
			{
				this.setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				return;
			}
			
			// Open West
			case 5:
			{
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
				return;
			}
			
			// Open South
			case 6:
			{
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
				return;
			}
			
			// Open East
			case 7:
			{
				this.setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
				return;
			}
			
			// Err
			default: return;
		}
	}
	
	// These allow for the bounding boxes to be block-specific; they re-call the setBlockBoundsBasedOnState method whenever a collision is probable
	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}
	
	// Open / close
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
	{
		
		int state = world.getBlockMetadata(x, y, z);
		int newState;
		
		newState = (state + 4) % 8;
		
		world.setBlockMetadataWithNotify(x, y, z, newState, 1 + 2);
		
		return true;
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
		return new GrateTentity();
	}
}
