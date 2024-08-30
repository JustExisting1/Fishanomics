package com.justexisting1.fishanomics.item;

import com.justexisting1.fishanomics.entity.FishanomicFishingBobberEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FishanomicFishingRodItem extends FishingRodItem {
    private final Tier tier;
    private final int lureBonus;
    private List<ResourceKey<Enchantment>> supportedEnchants = List.of(Enchantments.FORTUNE, Enchantments.UNBREAKING, Enchantments.LURE, Enchantments.LUCK_OF_THE_SEA, Enchantments.MENDING);

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return supportedEnchants.contains(enchantment.getKey());
//        return stack.is(Items.ENCHANTED_BOOK) || enchantment.value().isSupportedItem(stack);

    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        return supportedEnchants.contains(enchantment.getKey());

//        Optional<HolderSet<Item>> primaryItems = enchantment.value().definition().primaryItems();
//        return this.supportsEnchantment(stack, enchantment) && (primaryItems.isEmpty() || stack.is(primaryItems.get())) || enchantment.equals(Enchantments.FORTUNE);
    }

    public FishanomicFishingRodItem(Tier tier, Properties properties) {
        super(properties);
        this.tier = tier;
        switch (tier){
            case Tiers.WOOD:
                this.lureBonus = 20;
                break;
            case Tiers.STONE:
                this.lureBonus = 40;
                break;
            case Tiers.IRON:
                this.lureBonus = 60;
                break;
            case Tiers.DIAMOND:
                this.lureBonus = 80;
                break;
            case Tiers.NETHERITE:
                this.lureBonus = 90;
                break;
            case Tiers.GOLD:
                this.lureBonus = 120;
                break;
            default:
                this.lureBonus = 0;
                break;
        }
    }

    //posibly some variables saying which loot table to look at eg,
    //private int fishingLevel = 2 -> Points to iron fishing rod loot table. This will be read in the loot table through loot params in retrieve
    //make a getter for sure, its private

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        //Must return result of interactionResultHolder, Fail, pass, consume, success
        ItemStack heldStack = player.getItemInHand(hand);

//        return new InteractionResultHolder<>(InteractionResult.FAIL, heldStack);    //Figure out why New is used here??? Only place, seems related to attacking

        if (player.fishing != null) {
            if (!level.isClientSide) {
                int i = player.fishing.retrieve(heldStack);

                ItemStack original = heldStack.copy();
                heldStack.hurtAndBreak(i, player, LivingEntity.getSlotForHand(hand));
                if (heldStack.isEmpty()) {
                    EventHooks.onPlayerDestroyItem(player, original, hand);
                }
            }

            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.FISHING_BOBBER_RETRIEVE,
                    SoundSource.NEUTRAL,
                    1.0F,
                    0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.FISHING_BOBBER_THROW,
                    SoundSource.NEUTRAL,
                    0.5F,
                    0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            if (level instanceof ServerLevel serverlevel) {
                int lureSpeed = (int) (EnchantmentHelper.getFishingTimeReduction(serverlevel, heldStack, player) * 20.0F);
                lureSpeed += lureBonus;
                int luck = EnchantmentHelper.getFishingLuckBonus(serverlevel, heldStack, player);
                level.addFreshEntity(new FishanomicFishingBobberEntity(player, level, luck, lureSpeed, heldStack)); //This casts the bobber -> recieves the reward
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }

        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide());

    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
    }
}
