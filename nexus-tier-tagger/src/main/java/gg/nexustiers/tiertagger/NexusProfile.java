package gg.nexustiers.tiertagger;

import java.util.List;

public final class NexusProfile {
    public String ign;
    public String bestTier;
    public List<TierResult> tiers;

    public String tierFor(String selectedKit) {
        if ("overall".equalsIgnoreCase(selectedKit)) {
            return usable(bestTier) ? bestTier : null;
        }

        if (tiers != null) {
            for (TierResult result : tiers) {
                if (result != null
                        && result.kit != null
                        && result.kit.equalsIgnoreCase(selectedKit)
                        && usable(result.tier)) {
                    return result.tier;
                }
            }
        }
        return null;
    }

    private static boolean usable(String tier) {
        return tier != null && !tier.isBlank() && !"n/a".equalsIgnoreCase(tier);
    }

    public static final class TierResult {
        public String kit;
        public String tier;
    }
}
