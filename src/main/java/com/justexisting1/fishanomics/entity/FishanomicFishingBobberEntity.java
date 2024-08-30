package com.justexisting1.fishanomics.entity;

import com.justexisting1.fishanomics.init.FishanomicLootTables;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.ItemAbilities;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class FishanomicFishingBobberEntity extends FishingHook {
    private int luck;
    private ItemStack fishingRod;

    public FishanomicFishingBobberEntity(EntityType<? extends FishanomicFishingBobberEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FishanomicFishingBobberEntity(Player player, Level level, int luck, int lureSpeed, @Nonnull ItemStack rod) {
        super(player, level, luck, lureSpeed);
        this.luck = luck;
        player.fishing = this;
        this.fishingRod = rod;
    }

    @Override
    public int retrieve(@Nonnull ItemStack stack) {
        Player player = this.getPlayerOwner();
        Level level = this.level();
        if (!this.level().isClientSide && player != null && !this.shouldStopFishing(player)) {
            int i = 0;
            net.neoforged.neoforge.event.entity.player.ItemFishedEvent event = null;
            if (this.getHookedIn() != null) {
                this.pullEntity(this.getHookedIn());
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) player, stack, this, Collections.emptyList());
                this.level().broadcastEntityEvent(this, (byte) 31);
                i = this.getHookedIn() instanceof ItemEntity ? 3 : 5;
            } else if (this.nibble > 0 && level instanceof ServerLevel serverLevel) {

                LootParams lootparams = new LootParams.Builder((ServerLevel) this.level())
                        .withParameter(LootContextParams.ORIGIN, this.position())
                        .withParameter(LootContextParams.TOOL, stack)
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .withParameter(LootContextParams.ATTACKING_ENTITY, this.getOwner())
                        .withLuck((float) this.luck + player.getLuck()) //Player luck assumed, try before, in the case of machines
                        .create(LootContextParamSets.FISHING);
                //Vanilla
//                LootTable loottable = this.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);
                //List<ItemStack> list = loottable.getRandomItems(lootparams);

                //Fishanomics
                List<ItemStack> list = getLoot(lootparams, serverLevel); //set in custom catching fish


                event = new net.neoforged.neoforge.event.entity.player.ItemFishedEvent(list, this.onGround() ? 2 : 1, this);
                net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    this.discard();
                    return event.getRodDamage();
                }
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) player, stack, this, list);

                for (ItemStack itemstack : list) {
                    ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemstack);
                    double d0 = player.getX() - this.getX();
                    double d1 = player.getY() - this.getY();
                    double d2 = player.getZ() - this.getZ();
                    double d3 = 0.1;
                    itementity.setDeltaMovement(d0 * 0.1, d1 * 0.1 + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08, d2 * 0.1);
                    this.level().addFreshEntity(itementity);
                    player.level()
                            .addFreshEntity(new ExperienceOrb(player.level(), player.getX(), player.getY() + 0.5, player.getZ() + 0.5, this.random.nextInt(6) + 1));
                    if (itemstack.is(ItemTags.FISHES)) {
                        player.awardStat(Stats.FISH_CAUGHT, 1);
                    }
                }

                i = 1;
            }

            if (this.onGround()) {
                i = 2;
            }

            this.discard();
            if (event != null) return event.getRodDamage();
            return i;
        } else {
            return 0;
        }
    }

    @Override
    protected boolean shouldStopFishing(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        boolean isMainHandRod = mainHand.canPerformAction(ItemAbilities.FISHING_ROD_CAST);
        boolean isOffHandRod = offHand.canPerformAction(ItemAbilities.FISHING_ROD_CAST);
        if (!player.isRemoved() && player.isAlive() && (isMainHandRod || isOffHandRod) && !(this.distanceToSqr(player) > 1024.0D)) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    private List<ItemStack> getLoot(LootParams lootParams, ServerLevel serverLevel){
        ResourceKey<LootTable> lootTableLocation;
        //I will need to register some loot tables
        lootTableLocation = FishanomicLootTables.WOODEN_ROD;
        LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableLocation);
        return lootTable.getRandomItems(lootParams);
    }


    @Override
    protected void catchingFish(BlockPos pos) {
        ServerLevel serverlevel = (ServerLevel)this.level();
        int i = 1;
        BlockPos blockpos = pos.above();
        if (this.random.nextFloat() < 0.25F && this.level().isRainingAt(blockpos)) {
            i++;
        }

        if (this.random.nextFloat() < 0.5F && !this.level().canSeeSky(blockpos)) {
            i--;
        }

        if (this.nibble > 0) {
            this.nibble--;
            if (this.nibble <= 0) {
                this.timeUntilLured = 0;
                this.timeUntilHooked = 0;
                this.getEntityData().set(DATA_BITING, false);
            }
        } else if (this.timeUntilHooked > 0) {
            this.timeUntilHooked -= i;
            if (this.timeUntilHooked > 0) {
                this.fishAngle = this.fishAngle + (float)this.random.triangle(0.0, 9.188);
                float f = this.fishAngle * (float) (Math.PI / 180.0);
                float f1 = Mth.sin(f);
                float f2 = Mth.cos(f);
                double d0 = this.getX() + (double)(f1 * (float)this.timeUntilHooked * 0.1F);
                double d1 = (double)((float)Mth.floor(this.getY()) + 1.0F);
                double d2 = this.getZ() + (double)(f2 * (float)this.timeUntilHooked * 0.1F);
                BlockState blockstate = serverlevel.getBlockState(BlockPos.containing(d0, d1 - 1.0, d2));
                if (blockstate.is(Blocks.WATER)) {
                    if (this.random.nextFloat() < 0.15F) {
                        serverlevel.sendParticles(ParticleTypes.BUBBLE, d0, d1 - 0.1F, d2, 1, (double)f1, 0.1, (double)f2, 0.0);
                    }

                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    serverlevel.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, (double)f4, 0.01, (double)(-f3), 1.0);
                    serverlevel.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, (double)(-f4), 0.01, (double)f3, 1.0);
                }
            } else {
                this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                double d3 = this.getY() + 0.5;
                serverlevel.sendParticles(
                        ParticleTypes.BUBBLE,
                        this.getX(),
                        d3,
                        this.getZ(),
                        (int)(1.0F + this.getBbWidth() * 20.0F),
                        (double)this.getBbWidth(),
                        0.0,
                        (double)this.getBbWidth(),
                        0.2F
                );
                serverlevel.sendParticles(
                        ParticleTypes.FISHING,
                        this.getX(),
                        d3,
                        this.getZ(),
                        (int)(1.0F + this.getBbWidth() * 20.0F),
                        (double)this.getBbWidth(),
                        0.0,
                        (double)this.getBbWidth(),
                        0.2F
                );
                this.nibble = Mth.nextInt(this.random, 20, 40);
                this.getEntityData().set(DATA_BITING, true);
            }
        } else if (this.timeUntilLured > 0) {
            this.timeUntilLured -= i;
            float f5 = 0.15F;
            if (this.timeUntilLured < 20) {
                f5 += (float)(20 - this.timeUntilLured) * 0.05F;
            } else if (this.timeUntilLured < 40) {
                f5 += (float)(40 - this.timeUntilLured) * 0.02F;
            } else if (this.timeUntilLured < 60) {
                f5 += (float)(60 - this.timeUntilLured) * 0.01F;
            }

            if (this.random.nextFloat() < f5) {
                float f6 = Mth.nextFloat(this.random, 0.0F, 360.0F) * (float) (Math.PI / 180.0);
                float f7 = Mth.nextFloat(this.random, 25.0F, 60.0F);
                double d4 = this.getX() + (double)(Mth.sin(f6) * f7) * 0.1;
                double d5 = (double)((float)Mth.floor(this.getY()) + 1.0F);
                double d6 = this.getZ() + (double)(Mth.cos(f6) * f7) * 0.1;
                BlockState blockstate1 = serverlevel.getBlockState(BlockPos.containing(d4, d5 - 1.0, d6));
                if (blockstate1.is(Blocks.WATER)) {
                    serverlevel.sendParticles(ParticleTypes.SPLASH, d4, d5, d6, 2 + this.random.nextInt(2), 0.1F, 0.0, 0.1F, 0.0);
                }
            }

            if (this.timeUntilLured <= 0) {
                this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
                this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
            }
        } else {
            this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
            this.timeUntilLured = this.timeUntilLured - this.lureSpeed;
        }
    }

}
