package info.mudbourn.mmsanimation.client;

import info.mudbourn.mmsanimation.client.lean.LeanTuning;
import net.fabricmc.api.ClientModInitializer;

public class MmsAnimationClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Unconditional: the lean mixin stands itself down when CPA is present,
        // but the config file should still be written so the knobs are visible.
        LeanTuning.load();
        // Held-pose arm mode. Loaded unconditionally so the file exists even when
        // the mixins are gated off, and so /mmspose can report the current mode.
        PoseTuning.load();
        PoseCommand.register();
    }
}
