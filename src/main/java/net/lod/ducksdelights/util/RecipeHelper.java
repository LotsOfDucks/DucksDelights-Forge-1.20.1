package net.lod.ducksdelights.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class RecipeHelper {
    @SuppressWarnings("removal")
    public static EntityType<?> getEntityType(JsonObject json) {
        String entityName = GsonHelper.getAsString(json, "entity");
        ResourceLocation entityKey = new ResourceLocation(entityName);
        if (!ForgeRegistries.ENTITY_TYPES.containsKey(entityKey)) {
            throw new JsonSyntaxException("Unknown item '" + entityName + "'");
        } else {
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityKey);
            return Objects.requireNonNull(entityType);
        }
    }
}
