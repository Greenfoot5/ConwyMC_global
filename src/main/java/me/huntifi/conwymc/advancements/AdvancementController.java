package me.huntifi.conwymc.advancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.huntifi.conwymc.ConwyMC;
import org.bukkit.Material;

public class AdvancementController {

    public static AdvancementTab advancementTab;
    private RootAdvancement root;
    public static UltimateAdvancementAPI api;

    public AdvancementController() {
        api = UltimateAdvancementAPI.getInstance(ConwyMC.plugin);

        advancementTab = api.createAdvancementTab("conwymc");

        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.WARPED_BUTTON, "V4 Tester", AdvancementFrameType.TASK, true, true, 0, 0, "Join ConwyMC");

        root = new RootAdvancement(advancementTab, "root", rootDisplay, "textures/block/stone.png");

        advancementTab.registerAdvancements(root);
    }
}
