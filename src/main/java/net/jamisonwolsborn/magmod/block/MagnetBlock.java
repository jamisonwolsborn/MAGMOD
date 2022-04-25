package net.jamisonwolsborn.magmod.block;

import net.jamisonwolsborn.magmod.MagMod;
import net.jamisonwolsborn.magmod.entity.MagnetBlockEntity;
import net.jamisonwolsborn.magmod.util.*;
import net.jamisonwolsborn.magmod.util.Runnable;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import static net.jamisonwolsborn.magmod.util.MagnetBlockPlaced.orientBlock;
import static net.jamisonwolsborn.magmod.util.MagneticField.getMagField;

public class MagnetBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.FACING;


    protected MagnetBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction) state.get(FACING)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static Direction getDirection(BlockState bs) {
        Direction dir = bs.get(FACING);
        return dir;
    }

    public static Direction.Axis getAxis(BlockState bs) {
        Direction.Axis axis = getDirection(bs).getAxis();
        return axis;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        // orientBlock(world, pos, state);

        UpdateBlockPosRunnable R1 = new UpdateBlockPosRunnable("Thread-1", world, pos);
        R1.start();
        /*Vec3d mag_field = R1.mag_field;
        MinecraftClient mc = MinecraftClient.getInstance();
        Text message = Text.of(String.valueOf(mag_field) + " : End Func");
        mc.inGameHud.addChatMessage(MessageType.SYSTEM, message, mc.player.getUuid());*/
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        this.spawnBreakParticles(world, player, pos, state);
        if (state.isIn(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinBrain.onGuardedBlockInteracted(player, false);
        }

        UpdateBlockBreakRunnable R2 = new UpdateBlockBreakRunnable("Thread-2", world, pos);
        R2.start();

        world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MagnetBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, MagMod.MAGNET_BLOCK_ENTITY, (world1, pos, state1, be) -> MagnetBlockEntity.tick(world1, pos, state1, be));

    }
}
