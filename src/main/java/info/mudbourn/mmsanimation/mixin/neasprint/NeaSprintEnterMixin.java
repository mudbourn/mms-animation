package info.mudbourn.mmsanimation.mixin.neasprint;

import info.mudbourn.mmsanimation.client.NeaSprintGuard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Opens the sprint guard immediately before EMF Compat: NEA's {@code applyAnimations}
 * RETURN handler runs.
 *
 * <h2>Ordering</h2>
 *
 * <p>The compat addon injects its pose-arbitration handler at the {@code RETURN} of
 * NEA's {@code applyAnimations} at default priority (1000). Callbacks at one point run
 * in application order, which is ascending mixin priority, so this mixin's lower
 * priority (900) makes {@link #mms$openSprintGuard} the first RETURN callback to run —
 * before the addon's. {@link NeaSprintExitMixin} sits above the addon (1100) and closes
 * the guard after it. Between the two, {@code Entity#isSprinting} reads {@code false}
 * for the addon's blanket sprint rule only, so a whitelisted held pose survives a sprint
 * with a blend instead of being cut to the run cycle in one frame.
 *
 * <h2>Why not target the addon's method directly</h2>
 *
 * <p>The sprint read lives in a method the addon merges into {@code AnimationProvider}
 * via {@code @Inject}; a second mod cannot reliably resolve another mod's merged
 * injector handler, which is why the previous single-redirect approach failed to apply.
 * Bracketing NEA's own {@code applyAnimations} depends on nothing but NEA and vanilla.
 *
 * <p>{@code remap = false}: {@code applyAnimations} is a mod method, matched by its own
 * name. The handler captures no target arguments, so no Minecraft types appear in this
 * unremapped mixin's signature. {@code require = 0}: with the addon absent the RETURN
 * handler it brackets is not present either, and the guard would simply never matter;
 * the config plugin already gates this on the addon being loaded.
 */
@Mixin(targets = "dev.tr7zw.notenoughanimations.logic.AnimationProvider", remap = false, priority = 900)
public class NeaSprintEnterMixin {

    @Inject(method = "applyAnimations", at = @At("RETURN"), require = 0)
    private void mms$openSprintGuard(CallbackInfo ci) {
        NeaSprintGuard.enter();
    }
}
