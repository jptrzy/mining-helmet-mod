package net.jptrzy.mining.helmet.block;

import net.jptrzy.mining.helmet.Main;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class AbstractHiddenOreBlock extends Block {


    public enum State implements StringIdentifiable {
        UNHIDDEN,
        HIDDEN,
        HIDDEN_ANIMATED;

        @Override
        public String asString() {
            return this.toString().toLowerCase();
        }
    }

    public static final EnumProperty<State> STATE = EnumProperty.of("state", State.class);

    public AbstractHiddenOreBlock(Settings settings) {
        super(settings.ticksRandomly());
        setDefaultState(getStateManager().getDefaultState().with(STATE, State.HIDDEN_ANIMATED));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        State _state = state.get(AbstractHiddenOreBlock.STATE);
        if (pos.isWithinDistance(MinecraftClient.getInstance().player.getPos(), 16)) {
            if (_state.equals(State.HIDDEN_ANIMATED)) {
                world.setBlockState(pos, state.with(STATE, State.HIDDEN));
            }
        } else if (_state.equals(State.HIDDEN)) {
            world.setBlockState(pos, state.with(STATE, State.HIDDEN_ANIMATED));
        }
    }
//
//    @Override
//    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
//        super.randomTick(state, world, pos, random);
////        Main.LOGGER.warn("TEST");
////        world.getBlockTickScheduler().sche();
////        world.createAndScheduleBlockTick();
//////        this.sche
////        Main.LOGGER.warn("RANDOM");
//    }
//
//
//
//    @Override
//    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
//        super.scheduledTick(state, world, pos, random);
//
//        if (state.get(AbstractHiddenOreBlock.STATE).equals(State.HIDDEN_ANIMATED)) {
//            world.setBlockState(pos, state.with(STATE, State.HIDDEN));
//            Main.LOGGER.warn("STOP ANIMATION");
//        }
//    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        world.setBlockState(pos, state.with(STATE, State.HIDDEN));
        return ActionResult.SUCCESS;
    }

//    public void tryAnimate(BlockState state, World world, BlockPos pos){
//        if(state.get(AbstractHiddenOreBlock.STATE).equals(State.HIDDEN)){
//            Main.LOGGER.warn("ANIMATE");
//            world.setBlockState(pos, state.with(STATE, State.HIDDEN_ANIMATED));
//            world.createAndScheduleBlockTick(pos, state.getBlock(), 8);
//        }
//    }
}
