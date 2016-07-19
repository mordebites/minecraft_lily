package mc.mod.prove.gui.sounds;

import mc.mod.prove.gui.MasterInterfacer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SoundHandler {
	public static SoundEvent lily_alert;

	/**
	 * Register the {@link SoundEvent}s.
	 */
	public static void init() {
		lily_alert = registerSound("lily_alert");
	}

	/**
	 * Register a {@link SoundEvent}.
	 *
	 * @param soundName
	 *            The SoundEvent's name without the testmod3 prefix
	 * @return The SoundEvent
	 */
	private static SoundEvent registerSound(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(MasterInterfacer.MODID, soundName);
		return GameRegistry.register(new SoundEvent(soundID)
				.setRegistryName(soundID));
	}
}