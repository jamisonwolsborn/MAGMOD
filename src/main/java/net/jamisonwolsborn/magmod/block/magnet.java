package net.jamisonwolsborn.magmod.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class magnet extends FacingBlock {

    public magnet(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.SOUTH)));
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case NORTH:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case SOUTH:
                return VoxelShapes.cuboid(0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f);
            case EAST:
                return VoxelShapes.cuboid(1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case WEST:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case UP:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case DOWN:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            default:
                return VoxelShapes.fullCube();
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(Properties.FACING, ctx.getPlayerFacing().getOpposite());
    }
}
