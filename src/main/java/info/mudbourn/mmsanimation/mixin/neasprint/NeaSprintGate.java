package info.mudbourn.mmsanimation.mixin.neasprint;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

import java.util.List;
import java.util.Set;

/**
 * Gates the sprint fix on both mods it sits between: Not Enough Animations owns the
 * {@code applyAnimations} that the enter/exit mixins bracket, and EMF Compat: NEA owns
 * the RETURN handler that bracket neutralises the sprint read for. With the addon
 * absent there is no handler to bracket and the guard would never matter; with NEA
 * absent there is no class to mix into. The {@code isSprinting} suppressor rides the
 * same gate so no global injector is added when the feature is inactive.
 */
public class NeaSprintGate implements IMixinConfigPlugin {

    private final boolean present = FabricLoader.getInstance().isModLoaded("notenoughanimations")
            && FabricLoader.getInstance().isModLoaded("emf_compat_not_enough_animations");

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) { return present; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return List.of(); }
    @Override public void preApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass, String mixinClassName, org.spongepowered.asm.mixin.extensibility.IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass, String mixinClassName, org.spongepowered.asm.mixin.extensibility.IMixinInfo mixinInfo) {}
}
