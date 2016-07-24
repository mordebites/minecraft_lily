package mc.mod.prove.entity.ai.basic;

import mc.mod.prove.entity.EntityLilyMob;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class EntityAISuspect extends EntityAIBase {
	private EntityCreature entity;
	private EntityPlayer player;
	private double speed;
	private double newPosX;
	private double newPosY;
	private double newPosZ;
	private BlockPos lastPosition = null;
	private int timer = 30;

	
	public EntityAISuspect(EntityCreature entity, EntityPlayer player, double speed) {
		this.entity = entity;
		this.player = player;
		this.speed = speed;
	}

	public void setPlayerLastPosition(BlockPos lastPosition){
		this.lastPosition = lastPosition;
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public void startExecuting(){
		if(lastPosition == null) {
			System.err.println("Last plate not set!");
		} else {
			//position of the last activated plate
			Vec3d lastPosVec = new Vec3d(lastPosition.getX(), lastPosition.getY(), lastPosition.getZ());
			//distanza tra l'npc e il blocco attivato
			int distance = (int) entity.getPositionVector().distanceTo(lastPosVec);
			
			Vec3d vec3 = null;
			do {
				vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(entity, distance > 0 ? distance : 2, 0, lastPosVec);
			} while (vec3 == null);
			
			newPosX = vec3.xCoord;
			newPosY = vec3.yCoord;
			newPosZ = vec3.zCoord;
			
			this.entity.getNavigator().tryMoveToXYZ(newPosX, newPosY, newPosZ, speed);
		}
	}
	
	@Override
	public boolean continueExecuting() {
		pathHelper();
		return !entity.getNavigator().noPath();
	}
	
	private void pathHelper() {
		System.out.println("PathHelper!");
		if(entity.motionX != 0 && entity.motionZ != 0){
			BlockPos nextPos = new BlockPos(entity.posX + Math.signum(entity.motionX), 4, entity.posZ + (int)Math.signum(entity.motionZ));
			IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(nextPos);
			if(blockState.getBlock() instanceof BlockPressurePlate){
				System.out.println("Found Plate!");
				((EntityLilyMob) entity).jump();
				System.out.println("Jumped!");
			}
		}
	}
}