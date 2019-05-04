package xyz.sk7z.fastuse;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class FastUseUtils {
    public static boolean isPlaceFoodSeed(Block clickedBlock, ItemStack item) {
        return clickedBlock.getType() == Material.FARMLAND && CraftItemStack.asNMSCopy(item).getItem() instanceof ItemSeedFood;
    }

    public static boolean isCanRightClockBlock(Block block) {
        net.minecraft.server.v1_13_R2.Block nmsBlock = ((CraftBlock) block).getNMS().getBlock();
        return
                //GUI開く系
                nmsBlock instanceof BlockChest ||
                        nmsBlock instanceof BlockEnderChest ||
                        nmsBlock instanceof BlockHopper ||
                        nmsBlock instanceof BlockShulkerBox ||
                        nmsBlock instanceof BlockFurnace ||
                        nmsBlock instanceof BlockWorkbench ||
                        nmsBlock instanceof BlockAnvil ||
                        nmsBlock instanceof BlockDispenser ||
                        nmsBlock instanceof BlockEnchantmentTable ||
                        //レッドストーン関係
                        nmsBlock instanceof BlockRepeater ||
                        nmsBlock instanceof BlockRedstoneComparator ||
                        nmsBlock instanceof BlockDaylightDetector ||
                        nmsBlock instanceof BlockButtonAbstract ||
                        nmsBlock instanceof BlockLever ||
                        //何かを開く京
                        nmsBlock instanceof BlockFenceGate ||
                        nmsBlock instanceof BlockDoor ||
                        nmsBlock instanceof BlockTrapdoor ||
                        //その他
                        nmsBlock instanceof BlockBed ||
                        //nmsBlock instanceof BlockJukeBox || NG
                        nmsBlock instanceof BlockNote;


    }
}
