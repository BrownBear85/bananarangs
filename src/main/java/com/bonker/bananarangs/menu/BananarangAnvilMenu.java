package com.bonker.bananarangs.menu;

import com.bonker.bananarangs.common.BRSounds;
import com.bonker.bananarangs.common.block.BRBlocks;
import com.bonker.bananarangs.common.item.BRItems;
import com.bonker.bananarangs.common.item.BananarangItem;
import com.bonker.bananarangs.common.item.UpgradeItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BananarangAnvilMenu extends AbstractContainerMenu {

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int ANVIL_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int ANVIL_INVENTORY_SLOT_COUNT = 4;
    private static final List<Integer> UPGRADE_SLOT_INDEXES = List.of(ANVIL_INVENTORY_FIRST_SLOT_INDEX, ANVIL_INVENTORY_FIRST_SLOT_INDEX + 1);
    private static final int BANANARANG_SLOT_INDEX = ANVIL_INVENTORY_FIRST_SLOT_INDEX + 2;
    private static final int ITEM_SLOT_INDEX = ANVIL_INVENTORY_FIRST_SLOT_INDEX + 3;

    private final ContainerLevelAccess access;
    private final Container container = new SimpleContainer(ANVIL_INVENTORY_SLOT_COUNT + VANILLA_SLOT_COUNT);
    private boolean changedNBT = false;

    public static BananarangAnvilMenu create(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        return new BananarangAnvilMenu(containerId, inventory, ContainerLevelAccess.NULL /*ContainerLevelAccess.create(inventory.player.level, buf.readBlockPos())*/);
    }

    public BananarangAnvilMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(BRMenus.BANANARANG_ANVIL.get(), containerId);
        this.access = access;
        createInventorySlots(inventory);
        createUpgradeSlot(0, 33, 25);
        createUpgradeSlot(1, 127, 25);
        createBananarangSlot(80, 29);
        createItemSlot(33, 52);
    }

    @Override
    public void removed(Player pPlayer) {
        clearSlots();
        super.removed(pPlayer);
        this.access.execute((level, pos) -> this.clearContainer(pPlayer, container));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, ANVIL_INVENTORY_FIRST_SLOT_INDEX, ANVIL_INVENTORY_FIRST_SLOT_INDEX
                    + ANVIL_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < ANVIL_INVENTORY_FIRST_SLOT_INDEX + ANVIL_INVENTORY_SLOT_COUNT) {
            // This is a custom slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }



    @Override
    public boolean stillValid(Player pPlayer) {
        return this.access.evaluate((level, pos) ->
                level.getBlockState(pos).is(BRBlocks.BANANARANG_ANVIL.get()) &&
                pPlayer.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D, true);
    }

    private void createBananarangSlot(int x, int y) {
        this.addSlot(new Slot(container, BananarangAnvilMenu.BANANARANG_SLOT_INDEX, x, y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(BRItems.BANANARANG.get());
            }
            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                onSetBananarang(stack);
            }
        });
    }

    private void createUpgradeSlot(int slot, int x, int y) {
        this.addSlot(new Slot(container, UPGRADE_SLOT_INDEXES.get(slot), x, y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return hasBananarang() && stack.getItem() instanceof UpgradeItem && canPlaceUpgrade(slot, stack);
            }
            @Override
            public boolean mayPickup(Player player) {
                return getItemUpgradeSlot() != slot || container.getItem(ITEM_SLOT_INDEX).isEmpty();
            }
            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                onSetUpgrade(slot, stack);
            }
            @Override
            public boolean isActive() {
                return super.isActive() && hasBananarang();
            }
        });
    }

    private void createItemSlot(int x, int y) {
        this.addSlot(new Slot(container, ITEM_SLOT_INDEX, x, y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                if (!hasBananarang()) return false;
                int itemUpgradeSlot = getItemUpgradeSlot();
                if (itemUpgradeSlot == -1) return false;
                if (container.getItem(UPGRADE_SLOT_INDEXES.get(itemUpgradeSlot)).getItem() instanceof UpgradeItem.AttachItemUpgrade attachItemUpgrade) {
                    return attachItemUpgrade.isValidItem(stack);
                }
                return true;
            }
            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                onSetItem(stack);
            }
            @Override
            public boolean isActive() {
                int slot = getItemUpgradeSlot();
                if (slot == 0) x = 33;
                if (slot == 1) x = 127;
                return super.isActive() && slot != -1;
            }
        });
    }

    boolean hasBananarang() {
        return container.getItem(BANANARANG_SLOT_INDEX).is(BRItems.BANANARANG.get());
    }

    int getItemUpgradeSlot() {
        if (container.getItem(UPGRADE_SLOT_INDEXES.get(0)).getItem() instanceof UpgradeItem.AttachItemUpgrade) return 0;
        if (container.getItem(UPGRADE_SLOT_INDEXES.get(1)).getItem() instanceof UpgradeItem.AttachItemUpgrade) return 1;
        return -1;
    }

    private boolean canPlaceUpgrade(int index, ItemStack upgrade) {
        ItemStack otherUpgrade = container.getItem(UPGRADE_SLOT_INDEXES.get(index == 0 ? 1 : 0));
        if (otherUpgrade.isEmpty() || !(otherUpgrade.getItem() instanceof UpgradeItem)) return true;
        return ((UpgradeItem) otherUpgrade.getItem()).isCompatible(((UpgradeItem) upgrade.getItem()).getUpgrade());
    }

    private void onSetBananarang(ItemStack stack) {
        if (stack.isEmpty()) {
            clearSlots();
            if (changedNBT) playSound(BRSounds.BANANARANG_SLOT_TAKE.get(), 1.1F);
            changedNBT = false;
        } else {
            UpgradeItem slot0 = UpgradeItem.UPGRADE_MAP.get(BananarangItem.getUpgradeInSlot(stack, 0));
            UpgradeItem slot1 = UpgradeItem.UPGRADE_MAP.get(BananarangItem.getUpgradeInSlot(stack, 1));
            if (slot0 != null) container.setItem(UPGRADE_SLOT_INDEXES.get(0), new ItemStack(slot0));
            if (slot1 != null) container.setItem(UPGRADE_SLOT_INDEXES.get(1), new ItemStack(slot1));
            container.setItem(ITEM_SLOT_INDEX, BananarangItem.getAttachedItem(stack));
            playSound(BRSounds.BANANARANG_SLOT_PLACE.get(), 0.7F);
        }
    }

    private void clearSlots() {
        container.setItem(UPGRADE_SLOT_INDEXES.get(0), ItemStack.EMPTY);
        container.setItem(UPGRADE_SLOT_INDEXES.get(1), ItemStack.EMPTY);
        container.setItem(ITEM_SLOT_INDEX, ItemStack.EMPTY);
    }

    private void onSetUpgrade(int slot, ItemStack stack) {
        BananarangItem.setUpgradeInSlot(container.getItem(BANANARANG_SLOT_INDEX), slot, stack.isEmpty() ? "" : ((UpgradeItem) stack.getItem()).getUpgrade());
        playSound(BRSounds.UPGRADE_SLOT_INTERACT.get(), stack.isEmpty() ? 1.0F : 0.8F);
        changedNBT = true;
        System.out.println("changedNBT");
    }

    private void onSetItem(ItemStack stack) {
        BananarangItem.setAttachedItem(container.getItem(BANANARANG_SLOT_INDEX), stack);
        playSound(BRSounds.ITEM_SLOT_INTERACT.get(), stack.isEmpty() ? 1.2F : 0.8F);
        changedNBT = true;
        System.out.println("changedNBT");
    }

    private void createInventorySlots(Inventory inventory) {
        for (int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    private void playSound(SoundEvent sound, float pitch) {
        access.execute((level, pos) -> {
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, pitch);
        });
    }
}
