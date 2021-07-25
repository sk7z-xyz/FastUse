package xyz.sk7z.fastuse;

import net.minecraft.world.item.ItemLingeringPotion;
import net.minecraft.world.item.ItemPotion;
import net.minecraft.world.item.ItemSplashPotion;
import net.minecraft.world.level.block.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import xyz.sk7z.fastuse.player_options.PlayerFoodOptions;

public class FastUseUtils {
    public static FastUse plugin;

    public static boolean isPlaceFoodSeed(ItemStack item, Block clickedBlock) {
        if (item == null || clickedBlock == null) {
            return false;
        }
        if (item.getType() == Material.SWEET_BERRIES) {
            switch (clickedBlock.getType()) {
                case GRASS_BLOCK:
                case DIRT:
                case PODZOL:
                    return true;
            }
        } else if (clickedBlock.getType() == Material.FARMLAND) {
            switch (item.getType()) {
                case POTATO:
                case CARROT:
                    return true;
            }
        }
        return false;
    }

    public static boolean isFood(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsItemStack.getItem().isFood();
    }

    public static boolean isFoodSoup(ItemStack item) {
        switch (item.getType()) {
            case MUSHROOM_STEW:
            case BEETROOT_SOUP:
            case RABBIT_STEW:
            case SUSPICIOUS_STEW:
                return true;
        }
        return false;
    }

    public static boolean isDrink(ItemStack item) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);

        if (nmsItemStack.getItem() instanceof ItemPotion) {
            //スプラッシュポーションと残留ポーションはfalse
            return !(nmsItemStack.getItem() instanceof ItemLingeringPotion) && !(nmsItemStack.getItem() instanceof ItemSplashPotion);
        } else {
            return false;
        }
    }

    public static ItemStack getUsedFoodItemFromPlayer(Player player) {
        ItemStack itemStack;
        itemStack = player.getInventory().getItemInMainHand();
        if (!FastUseUtils.isFood(itemStack) && !FastUseUtils.isDrink(itemStack)) {
            itemStack = player.getInventory().getItemInOffHand();
            if (!FastUseUtils.isFood(itemStack) && !FastUseUtils.isDrink(itemStack)) {
                itemStack = new ItemStack(Material.AIR);
            }
        }
        return itemStack;
    }

    public static boolean isPlayerhungry(Player player) {
        return player.getFoodLevel() < 20;
    }

    //満腹でも食べられるアイテムか 英語わかんねー
    public static boolean isSatietyFood(ItemStack item) {

        switch (item.getType()) {
            case GOLDEN_APPLE:
            case ENCHANTED_GOLDEN_APPLE:
            case CHORUS_FRUIT:
                return true;
        }
        return false;
    }

    public static boolean canPlayerEatTime(Player player) {

        PlayerFoodOptions playerFoodOptions = plugin.getPlayerValues(player).getPlayerFoodOptions();
        //食べ始めてから30秒立ってたら拒否
        if (playerFoodOptions.getElapsedTimeMillis() >= 30 * 1000) {
            playerFoodOptions.setEndTime();
            return false;
        }
        return playerFoodOptions.getElapsedTimeMillis() >= 1.6 * 1000;
    }

    public static boolean isCanRightClockBlock(Block block) {
        net.minecraft.world.level.block.Block nmsBlock = ((CraftBlock) block).getNMS().getBlock();
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
                        //何かを開く系
                        nmsBlock instanceof BlockFenceGate ||
                        nmsBlock instanceof BlockDoor ||
                        nmsBlock instanceof BlockTrapdoor ||
                        //その他
                        nmsBlock instanceof BlockBed ||
                        nmsBlock instanceof BlockNote;


    }

    public static boolean isChairBlock(Block block) {
        net.minecraft.world.level.block.Block nmsBlock = ((CraftBlock) block).getNMS().getBlock();
        if (nmsBlock instanceof BlockStairs) {
            String dataStr = "[half=bottom]";
            BlockData data = Bukkit.createBlockData(block.getType(), dataStr);
            return block.getBlockData().matches(data);
        }
        return false;

    }

    public static MainHand getUseHand(Player player, ItemStack it){
        ItemStack itemStack;
        itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.equals(it)) {
            return MainHand.RIGHT;
        }else {
            return MainHand.LEFT;
        }
    }
    public static boolean isSameItem(ItemStack itemStack_1,ItemStack itemStack_2){
        if(itemStack_1 == null || itemStack_2 == null){
            return false;
        }
        return  itemStack_1.getType() == itemStack_2.getType();

    }
}
