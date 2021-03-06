package naftoreiclag.blocksmith.tangible.grate;

import naftoreiclag.blocksmith.lib.model.ModelGrate;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class GrateTentityRenderer extends TileEntitySpecialRenderer
{
	private ModelGrate model = new ModelGrate();
	
	private void renderTileEntityGrateAt(GrateTentity tileEntity, double x, double y, double z, float tick)
	{
		model.render(tileEntity, x, y, z, tileEntity.getBlockMetadata(), tileEntity.doorRot);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
	{
		renderTileEntityGrateAt((GrateTentity) tileEntity, x, y, z, tick);
	}
}
