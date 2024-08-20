package com.justexisting1.fishanomics.item;

import com.justexisting1.fishanomics.entity.FishanomicFishingBobberEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class FishanomicFishingRodItem extends FishingRodItem {
    private final Tier tier;

    public FishanomicFishingRodItem(Tier tier, Properties properties) {
        super(properties);
        this.tier = tier;
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
                    net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player, original, hand);
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
