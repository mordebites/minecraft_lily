package mc.mod.prove.reasoning;

import mc.mod.prove.MainRegistry;
import mc.mod.prove.entity.BlockEvent;
import mc.mod.prove.entity.ai.enumerations.EntityDistance;
import mc.mod.prove.gui.sounds.SoundHandler;
import mc.mod.prove.match.MatchHandler;
import mc.mod.prove.match.PlayerDefeatHandler;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SightHandler {
	private EntityLivingBase entity;
	private EntityPlayer player;
	private boolean alreadySeen = false;
	
	//per settare la barra Lily's sight
	private int sightValue = 0;
	//TODO sistema costanti
	private int cont = 0;
	//distanza al quarto di secondo precedente
	private int prevDistance;
	
	public SightHandler(EntityLivingBase entity, EntityPlayer player){
		this.entity = entity;
		this.player = player;
		prevDistance = (int) entity.getPositionVector().distanceTo(player.getPositionVector());
	}
	
	public void setAlreadySeen(boolean playerAlreadySeen) {
		this.alreadySeen = playerAlreadySeen;
	}
	//stabilisce se il giocatore  e'  visibile e a quale distanza
	public EntityDistance checkPlayerInSight(Entity player, int distanceThreshold){
		EntityDistance playerInSight = EntityDistance.None;
		
		//controlla se Lily puo' vedere il giocatore verificando che non ci siano blocchi interposti tra i due
		if(entity.canEntityBeSeen(player)) {
			
			//controlla che il giocatore non sia invisibile a causa dell'oscurita'
			if(player.getBrightness(0) > 0.2 || 
				(player.getBrightness(0) > 0.1 && (player.motionX != 0 || player.motionY != 0 || player.motionZ != 0))) {
				
				if(entity.getPositionVector().distanceTo(player.getPositionVector()) > distanceThreshold) {
					playerInSight = EntityDistance.Far;
				} else {
					playerInSight = EntityDistance.Close;
				}	
			}
		}
		
		//per aggiornare ogni quarto di secondo la barra Lily's Sight
		if(cont % 2 == 0) {
			//TODO costante
			if(playerInSight != EntityDistance.None && sightValue <= 10) {
				int currentDistance = (int) entity.getPositionVector().distanceTo(player.getPositionVector());
				if(prevDistance >= currentDistance) {
					//TODO costanti
					if((currentDistance <= 3) 
						|| (currentDistance > 3 && currentDistance <= 7 && cont % 6 == 0)
						|| (currentDistance > 7 && cont % 10 == 0)) {
						sightValue++;
					}
				}
			} else if (playerInSight == EntityDistance.None && sightValue > 0) {
				sightValue--;
			}
			
			MainRegistry.match.setSightValue(sightValue);
			prevDistance = (int) entity.getPositionVector().distanceTo(player.getPositionVector());
			
			if(cont % 20 == 0) {
				System.out.println("Lily's position: " + entity.getPosition().toString());
			}
		}
		
		
		if(playerInSight != EntityDistance.None && !alreadySeen && 
				MainRegistry.match.getWinner() == MatchHandler.WINNER_NOBODY &&
				MainRegistry.match.isMatchStarted()) {
			this.handlePlayerInSightSound();
		}
		
		//resetta il valore da assegnare alla barra quando ricomincia il round
		if (MainRegistry.match.getMinutesTime() == MatchHandler.MAX_ROUND_TIME) {
			sightValue = 0;
		}
		
		cont++;
		
		if(sightValue == 10) {
			PlayerDefeatHandler.onPlayerDefeat();
		}
		
		return playerInSight;
	}
	
	//controlla se Lily puo'  vedere l'ultima luce che si  e'  accesa
	public EntityDistance checkLight(BlockEvent lastLight, int distanceThreshold){
		EntityDistance light = EntityDistance.None;
		
		//se  e'  stata accesa qualche luce
		if(lastLight != null && lastLight.getTimer() > 0) {
			int lightX = lastLight.getPos().getX();
			int lightZ = lastLight.getPos().getZ();
			
			boolean lightSeen = false;
			EnumFacing facing = entity.getHorizontalFacing();
			
			//controlla se l'NPC sta guardando nella stessa direzione della luce
			if ((facing == EnumFacing.WEST && (int) entity.posX >= lightX)
				|| (facing == EnumFacing.EAST && (int) entity.posX <= lightX)
				|| (facing == EnumFacing.NORTH && (int) entity.posZ >= lightZ )
				|| (facing == EnumFacing.SOUTH && (int) entity.posZ <= lightZ)) {
				
				if(entity.worldObj.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), new Vec3d(lightX, 4, lightZ), false, true, false) == null) {
					lightSeen = true;
				} else {
					switch(facing) {
						case EAST : {
							lightSeen = handleEast(lightX, lightZ);
							break;
						}
						case WEST : {
							lightSeen = handleWest(lightX, lightZ);
							break;
						}
						case SOUTH : {
							lightSeen = handleSouth(lightX, lightZ);
							break;
						}
						case NORTH : {
							lightSeen = handleNorth(lightX, lightZ);
							break;
						}
						default: {
							System.err.println("Lily is facing an invalid direction!");
						}
					}
				}
			}
			
						
			if (lightSeen) {
				System.out.println("Lily saw a light!");
				lastLight.setPerceived(true);
				if(entity.getPositionVector().distanceTo(new Vec3d(lastLight.getPos().getX(), lastLight.getPos().getY(), lastLight.getPos().getZ())) > distanceThreshold) {
					light = EntityDistance.Far;
				} else {
					light = EntityDistance.Close;
				}
			} 	
		}
		return light;
	}
	
	private boolean handleNorth(int lightX, int lightZ) {
		boolean lightSeen = false;
		
		//se la luce � a sinistra dell'npc
		if(lightX < (int) entity.posX) {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		//se la luce � a destra dell'npc
		} else if (lightX > (int) entity.posX) {
			lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		//se la luce � di fronte all'npc
		} else {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		}
		return lightSeen;
	}
	
	private boolean handleSouth(int lightX, int lightZ) {
		boolean lightSeen = false;
		
		//se la luce � a sinistra dell'npc
		if(lightX > (int) entity.posX) {
			lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		//se la luce � a destra dell'npc
		} else if (lightX < (int) entity.posX) {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		//se la luce � di fronte all'npc
		} else {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", true);
				}
			}
		}
		return lightSeen;
	}

	private boolean handleWest(int lightX, int lightZ) {
		boolean lightSeen = false;
		
		//se la luce � a sinistra dell'npc
		if(lightZ > (int) entity.posZ) {
			lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
				}
			}
		//se la luce � a destra dell'npc
		} else if (lightZ < (int) entity.posZ) {
			lightSeen = this.cross(lightZ+1, lightZ-4, lightX, "WE", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
				}
			}
		//se la luce � di fronte all'npc
		} else {
			lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		}
		return lightSeen;
	}

	private boolean handleEast(int lightX, int lightZ){
		boolean lightSeen = false;
		
		//se la luce � a sinistra dell'npc
		if(lightZ < (int) entity.posZ) {
			lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
				}
			}
		//se la luce � a destra dell'npc
		} else if (lightZ > (int) entity.posZ) {
			lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightX+1, lightX+4, lightZ, "NS", true);
				}
			}
		//se la luce � di fronte all'npc
		} else {
			lightSeen = this.cross(lightX-1, lightX-4, lightZ, "NS", false);
			if(!lightSeen) {
				lightSeen = this.cross(lightZ-1, lightZ-4, lightX, "WE", false);
				if(!lightSeen) {
					lightSeen = this.cross(lightZ+1, lightZ+4, lightX, "WE", true);
				}
			}
		}
		return lightSeen;
	}
	
	private boolean cross(int start, int end, int otherCoord, String axis, boolean increment) {
		boolean visible = false;
		int i = start;
		BlockPos pos = null;
		
		if(axis.compareTo("NS") == 0) {
			pos = new BlockPos(i, 4, otherCoord);
		} else {
			pos = new BlockPos(otherCoord, 4, i);
		}
		
		IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
		
		while (!visible && !(blockState.getBlock() instanceof BlockStone) && i >= (end)){
			Vec3d blockToCheck = null;
			if(axis.compareTo("NS") == 0) {
				blockToCheck = new Vec3d(i, 4, otherCoord);
			} else {
				blockToCheck = new Vec3d(otherCoord, 4, i);
			}
			if(entity.worldObj.rayTraceBlocks(new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), blockToCheck, false, true, false) == null) {
				visible = true;
			}
			
			i = increment? i+1 : i-1;
			if(axis.compareTo("NS") == 0) {
				pos = new BlockPos(i, 4, otherCoord);
			} else {
				pos = new BlockPos(otherCoord, 4, i);
			}
			blockState = Minecraft.getMinecraft().theWorld.getBlockState(pos);
		}
		return visible;
	}
	
	private void handlePlayerInSightSound() {
		player.worldObj.playSound(player, player.posX, player.posY,
				player.posZ, SoundHandler.lily_alert,	SoundCategory.AMBIENT, 2.0F, 1.0F);
	}
}
