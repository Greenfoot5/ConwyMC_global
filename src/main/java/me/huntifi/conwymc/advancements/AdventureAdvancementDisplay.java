package me.huntifi.conwymc.advancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.VanillaVisibility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class AdventureAdvancementDisplay extends AdvancementDisplay implements VanillaVisibility {

    public AdventureAdvancementDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y,
                                       @NotNull Component title, @NotNull Component description) {
        super(icon, LegacyComponentSerializer.legacySection().serialize(title), frame, showToast, announceChat, x, y,
                LegacyComponentSerializer.legacySection().serialize(description));
    }

    public AdventureAdvancementDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y,
                                       @NotNull String title, @NotNull Component description) {
        super(icon, mmConvert(title), frame, showToast, announceChat, x, y,
                LegacyComponentSerializer.legacySection().serialize(description));
    }

    public AdventureAdvancementDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y,
                                       @NotNull Component title, @NotNull String description) {
        super(icon, LegacyComponentSerializer.legacySection().serialize(title), frame, showToast, announceChat, x, y,
                mmConvert(description));
    }

    public AdventureAdvancementDisplay(@NotNull Material icon, @NotNull AdvancementFrameType frame, boolean showToast, boolean announceChat, float x, float y,
                                       @NotNull String title, @NotNull String description) {
        super(icon, mmConvert(title), frame, showToast, announceChat, x, y,
                mmConvert(description));
    }

    private static String mmConvert(String input) {
        Component convert = MiniMessage.miniMessage().deserialize(input);
        return LegacyComponentSerializer.legacySection().serialize(convert);
    }
}
