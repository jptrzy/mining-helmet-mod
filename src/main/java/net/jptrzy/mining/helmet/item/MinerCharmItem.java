package net.jptrzy.mining.helmet.item;

import net.jptrzy.mining.helmet.Main;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MinerCharmItem extends Item {
    public MinerCharmItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(!Main.TRINKETS_LOADED){
            tooltip.add(Text.translatable("tooltip." + Main.MOD_ID + ".miner_charm.trinket_not_loaded"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
