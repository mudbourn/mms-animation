package info.mudbourn.mmsanimation.client;

/**
 * A thread-confined flag that is raised only for the instant EMF Compat: NEA's
 * {@code applyAnimations} RETURN handler runs, and read by the {@code isSprinting}
 * suppressor so that one handler — and nothing else in the game — sees the player
 * as not sprinting.
 *
 * <p>This replaces the older approach of {@code @Redirect}-ing the {@code isSprinting}
 * call <em>inside</em> the compat addon's merged handler method. That method is an
 * {@code @Inject} callback another mod merges into NEA's {@code AnimationProvider};
 * a second mod's injectors cannot reliably resolve it, so the redirect silently
 * failed to apply (see the neasprint mixins). Here we never name the addon's method:
 * we bracket its RETURN callback on NEA's own {@code applyAnimations} and neutralise
 * the sprint read through vanilla {@code Entity#isSprinting} for the bracket's width.
 *
 * <p>Render-thread confined in practice, but a {@link ThreadLocal} keeps the flag
 * from leaking to the server thread's own {@code isSprinting} calls.
 */
public final class NeaSprintGuard {

    private static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private NeaSprintGuard() {}

    /** Raise the flag for this thread as the addon's RETURN handler is about to run. */
    public static void enter() {
        ACTIVE.set(Boolean.TRUE);
    }

    /** Lower the flag once the handler has run. */
    public static void exit() {
        ACTIVE.set(Boolean.FALSE);
    }

    /** Whether a sprint read should be answered as {@code false} right now. */
    public static boolean active() {
        return ACTIVE.get();
    }
}
