package xyz.sk7z.fastuse;

import net.minecraft.server.v1_13_R2.BlockChest;
import net.minecraft.server.v1_13_R2.BlockDispenser;
import net.minecraft.server.v1_13_R2.BlockDoor;
import net.minecraft.server.v1_13_R2.ItemSeedFood;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class FastUseUtils {
    public static boolean isPlaceFoodSeed(Block clickedBlock, ItemStack item) {
        return clickedBlock.getType() == Material.FARMLAND && CraftItemStack.asNMSCopy(item).getItem() instanceof ItemSeedFood;
    }

    public static boolean isOpenableBlock(Block block) {
        net.minecraft.server.v1_13_R2.Block nmsBlock = ((CraftBlock) block).getNMS().getBlock();
        return nmsBlock instanceof BlockDoor || nmsBlock instanceof BlockChest || nmsBlock instanceof BlockDispenser;
    }
}
