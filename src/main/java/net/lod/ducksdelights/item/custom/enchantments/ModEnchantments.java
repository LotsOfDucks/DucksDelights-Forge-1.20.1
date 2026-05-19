package net.lod.ducksdelights.item.custom.enchantments;

import net.lod.ducksdelights.DucksDelights;
import net.lod.ducksdelights.item.custom.CleaverItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    static EnchantmentCategory CLEAVER = EnchantmentCategory.create("cleaver", item -> item instanceof CleaverItem);

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, DucksDelights.MOD_ID);

    public static final RegistryObject<Enchantment> BONE_COLLECTION = ENCHANTMENTS.register("waste_not_want_not", () ->
            new BoneCollectionEnchantment(Enchantment.Rarity.UNCOMMON, CLEAVER, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
