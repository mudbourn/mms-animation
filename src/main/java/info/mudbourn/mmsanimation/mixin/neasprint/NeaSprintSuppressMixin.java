package info.mudbourn.mmsanimation.mixin.neasprint;

import info.mudbourn.mmsanimation.client.NeaSprintGuard;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Answers {@code isSprinting} as {@code false} for the width of the sprint guard, and
 * is otherwise inert.
 *
 * <p>The guard is raised by {@link NeaSprintEnterMixin} and lowered by
 * {@link NeaSprintExitMixin} around exactly one callback — EMF Compat: NEA's
 * {@code applyAnimations} RETURN handler — so every other {@code isSprinting} read in
 * the game, on this entity or any other, returns the real value. When the guard is
 * down this injector falls straight through without touching the return value.
 *
 * <p>Injected on {@code Entity} because that is where {@code isSprinting} is declared;
 * the client-only mixin config keeps it off the server. The guard being thread-local
 * means server-thread reads are never affected even while a client animation is mid
 * bracket.
 */
@Mixin(Entity.class)
public abstract class NeaSprintSuppressMixin {

    @Inject(method = "isSprinting", at = @At("HEAD"), cancellable = true)
    private void mms$ignoreSprintWhileGuarded(CallbackInfoReturnable<Boolean> cir) {
        if (NeaSprintGuard.active()) {
            cir.setReturnValue(false);
        }
    }
}
