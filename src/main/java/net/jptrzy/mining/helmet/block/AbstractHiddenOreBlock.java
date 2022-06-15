package net.jptrzy.mining.helmet.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class AbstractHiddenOreBlock extends Block {

    public static final BooleanProperty HIDDEN = BooleanProperty.of("hidden");

    public AbstractHiddenOreBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(HIDDEN, true));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
//        if(!pos.isWithinDistance(MinecraftClient.getInstance().player.getPos(), 16)){
//            ParticleUtil.spawnParticle(world, pos, ParticleTypes.SCRAPE, UniformIntProvider.create(3, 5));
//            // WAX_OFF
//        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HIDDEN);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        world.setBlockState(pos, state.with(HIDDEN, true));
        return ActionResult.SUCCESS;
    }
}
