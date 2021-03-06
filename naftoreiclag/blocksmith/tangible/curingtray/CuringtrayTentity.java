package naftoreiclag.blocksmith.tangible.curingtray;

import naftoreiclag.blocksmith.ModBlocksmith;
import naftoreiclag.blocksmith.tangible.misc.ItemMisc;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class CuringtrayTentity extends TileEntity implements IInventory
{
	// The nbt keys
	public static final String STORAGE_KEY = "Storage";
	public static final String SLOT_KEY = "Slot";
	
	private boolean canCook = false;
	public boolean canSeeSun = true;
	protected int cookProgress = 0; // out of 216000
	protected int waterLevel = 0; // out of 360
	protected int catalyst = 0;
	
	// My storage
	private ItemStack[] storage = new ItemStack[CuringtrayContainer.size];

	// What? I don't even
	@Override
	public void openChest() {}
	@Override
	public void closeChest() {}

	/** MACHINERY **/
	
	@Override
	public void updateEntity()
	{
		if(worldObj.getTotalWorldTime() % 80L == 0L)
        {
			canSeeSun = worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord);
        }
		
		if(canCook && canSeeSun)
		{
			cookProgress += 93 + catalyst;
			
			if(cookProgress >= 1000000)
			{
				storage[0].stackSize --;
				
				if(storage[0].stackSize <= 0)
				{
					storage[0] = null;
				}
				
				if(storage[9] == null)
				{
					storage[9] = new ItemStack(ModBlocksmith.item_misc, 1, ItemMisc.CURED_LEATHER);
				}
				else
				{
					storage[9].stackSize ++;
				}
				
				updateCanCook();
				
				cookProgress = cookProgress - 1000000;
			}
		}
	}
	
	public void stopCooking() { canCook = false; cookProgress = 0; updateMetadata();}
	public void startCooking() { canCook = true; }
	
	public void updateCanCook()
	{
		if(storage[0] != null)
		{
			if(storage[0].itemID == Item.leather.itemID)
			{
				startCooking();
			}
			else
			{
				stopCooking();
			}
		}
		else
		{
			stopCooking();
		}
	}
	
	public void updateCatalyst()
	{
		catalyst = 0;
		for(int index = 1; index < 9; index ++)
		{
			if(storage[index] != null)
			{
				catalyst += itemIdToCatalystAmount(storage[index].itemID, storage[index].getItemDamage());
			}
		}
	}
	
	public static int itemIdToCatalystAmount(int id, int metadata)
	{
		return
			id == Block.cobblestone.blockID ? 1 :
			id == Block.stone.blockID ? 3 :
			id == Block.gravel.blockID ? 9 :
			id == Block.cobblestoneMossy.blockID ? 22 :
			id == Block.grass.blockID ? 30 :
			id == Block.stoneBrick.blockID && metadata == 1 ? 40 :
			id == Block.sponge.blockID ? 92 :
			0;
	}
	
	private void updateMetadata()
	{
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, canCook ? 1 : 0, 2);
	}
	
	/** READIN AND WRITIN **/
	
	// Called when loading the tile entity
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		// Normal tile entity-ish stuff
		super.readFromNBT(tagCompound);
	
		// Storage //
		
		// Get the contents of the storage data
		NBTTagList tagList = tagCompound.getTagList(STORAGE_KEY);
		
		// Loop through all tags (items) present
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			// Get that particular item
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			
			// Get the slot in its data
			byte slot = tag.getByte(SLOT_KEY);
			
			// If it is valid
			if (slot >= 0 && slot < storage.length)
			{
				// Get it via [nbt -> itemstack] util
				storage[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}
	
	// Called when saving the tile entity
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		// Normal tile entity-ish stuff
		super.writeToNBT(tagCompound);
	
		// Storage
		NBTTagList tagList = new NBTTagList();
		
		// Loop through all the ItemStacks
		for(int i = 0; i < storage.length; i++)
		{
			// Get its pointer
			ItemStack itemStack = storage[i];
			
			// If it is valid
			if(itemStack != null)
			{
				// Make a new tag to write to
				NBTTagCompound tagItem = new NBTTagCompound();
				
				// Write its slot
				tagItem.setByte(SLOT_KEY, (byte) i);
	
				// Set it via [itemstack -> nbt] util
				itemStack.writeToNBT(tagItem);
				
				// Append it to the list
				tagList.appendTag(tagItem);
			}
		}
		
		// Finally, set the new tag we made into the storage key
		tagCompound.setTag(STORAGE_KEY, tagList);
	}

	/** UNCHANGING VALUES **/
	
	// The internal name of this inventory; must be same as the string used when registering
	@Override
	public String getInvName() { return ModBlocksmith.modid + ".curingtrayTileEntity"; }
	
	// Get how many slots are in this inventory
	@Override
	public int getSizeInventory() { return storage.length; }
	
	// How big each item stack can get. Always return 64. Kinda redundant, eh?
	@Override
	public int getInventoryStackLimit() { return 64; }

	/** INVENTORY MANIPULATION **/
	
	// Get the ItemStack from the given slot
	@Override
	public ItemStack getStackInSlot(int slot) { return storage[slot]; }
	
	// Set an ItemStack to the given slot
	@Override
	public void setInventorySlotContents(int slot, ItemStack newItemStack)
	{
		if(storage[slot] == null)
		{
			if(newItemStack == null)
			{
				return;
			}
		}
		else if(storage[slot].equals(newItemStack))
		{
			return;
		}
		
		// lol
		storage[slot] = newItemStack;
		
		// If something is awry
		if(newItemStack != null && newItemStack.stackSize > getInventoryStackLimit())
		{
			// Fix it
			newItemStack.stackSize = getInventoryStackLimit();
		}
		
		// Update the catalyst
		updateCatalyst();
		
		updateCanCook();
		
		
	}
	
	// Decrease the size of a stack
	@Override
	public ItemStack decrStackSize(int slot, int amountToRemove)
	{
		// lol
		ItemStack itemStack = getStackInSlot(slot);
		
		// If it is not air
		if(itemStack != null)
		{
			// If we will end up with a stack with slot zero
			if(itemStack.stackSize <= amountToRemove)
			{
				// Then set it to air instead
				setInventorySlotContents(slot, null);
			}
			
			// Otherwise, there will be some crumbs left
			else
			{
				// So split the stack
				itemStack = itemStack.splitStack(amountToRemove);
				
				// Air check
				if(itemStack.stackSize == 0)
				{
					// Set it to air
					setInventorySlotContents(slot, null);
				}
			}
		}
		return itemStack;
	}
	
	// Dunno what this does
	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		// Get the current one
		ItemStack itemStack = getStackInSlot(slot);
		
		// Clear out the old one
		setInventorySlotContents(slot, null);
		
		// Return it
		return itemStack;
	}

	/** MISC **/
	
	// Can the player right-click it and get a result
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		// Return
		return
		
		// the result of this boolean logic:
		worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this // If the tile entity is really at the position it thinks its in
		&& player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64; // If the player is within reaching distance
	}

	// Is it localized?
	@Override
	public boolean isInvNameLocalized() { return false; }

	// Do something here for buildcraft and hoppers
	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
