package info.mudbourn.mmsanimation.mixin.neasprint;

import info.mudbourn.mmsanimation.client.NeaSprintGuard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Closes the sprint guard immediately after EMF Compat: NEA's {@code applyAnimations}
 * RETURN handler runs.
 *
 * <p>Priority 1100 sits above the addon's default 1000, so this RETURN callback is
 * applied after — and therefore runs after — the addon's handler, restoring the real
 * {@code isSprinting} result the instant the bracket closes. See {@link NeaSprintEnterMixin}
 * for the full rationale and why the guard is bracketed here rather than patching the
 * addon's merged method.
 */
@Mixin(targets = "dev.tr7zw.notenoughanimations.logic.AnimationProvider", remap = false, priority = 1100)
public class NeaSprintExitMixin {

    @Inject(method = "applyAnimations", at = @At("RETURN"), require = 0)
    private void mms$closeSprintGuard(CallbackInfo ci) {
        NeaSprintGuard.exit();
    }
}
