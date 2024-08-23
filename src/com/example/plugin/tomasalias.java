package com.example.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class tomasalias extends JavaPlugin implements Listener {

    private final Random random = new Random();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getLogger().info("Reforge plugin enabled");
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();
        Player player = event.getPlayer();

        if (isArmorPiece(itemStack.getType())) {
        	Block blockUnder = item.getLocation().getBlock().getRelative(0, -1, 0);
        	if (blockUnder.getType() == Material.SMITHING_TABLE) {
        		new BukkitRunnable() {
        			@Override
        			public void run() {
        				Block blockUnder = item.getLocation().getBlock().getRelative(0, -1, 0);
        				if (blockUnder.getType() == Material.SMITHING_TABLE) {
        					getLogger().info("Item is on a smithing table");
        					if (player.getLevel() >= 2) {
        						player.setLevel(player.getLevel() - 2);
        						applyReforge(item);
        					} else {
        						player.sendMessage(ChatColor.RED + "You need at least 2 XP levels to reforge this item.");
        					}
        					this.cancel();
        				} else {
        					this.cancel();
        				}
        			}
        		}.runTaskTimer(this, 0L, 20L);
        	}
        }
    }

    private boolean isArmorPiece(Material material) {
        switch (material) {
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
            case NETHERITE_HELMET:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_LEGGINGS:
            case NETHERITE_BOOTS:
            case TURTLE_HELMET:
            case GOLDEN_HELMET:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_LEGGINGS:
            case GOLDEN_BOOTS:
                return true;
            default:
                return false;
        }
    }

    private void applyReforge(Item item) {
        ItemStack original = item.getItemStack();
        Material material = original.getType();

        // Check if the item is not null
        if (item != null) {
            getLogger().info("Item is not null");
            item.remove();
            item.getWorld().playSound(item.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1.25f);
            getLogger().info("Item removed");

            ItemStack reforgedItem = getReforgedItem(material);
            item.getWorld().dropItem(item.getLocation(), reforgedItem);
            getLogger().info("Reforged item dropped");
        }
    }

    private ItemStack getReforgedItem(Material material) {
        int chance = random.nextInt(100);
        String reforge;
        if (chance < 30) {
            reforge = "Negative";
        } else if (chance < 65) {
            reforge = "Neutral";
        } else {
            reforge = "Positive";
        }

        switch (material) {
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
            case NETHERITE_HELMET:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_LEGGINGS:
            case NETHERITE_BOOTS:
                return getIronDiamondNetheriteReforgedItem(material, reforge);
            case TURTLE_HELMET:
                return getTurtleReforgedItem();
            case GOLDEN_HELMET:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_LEGGINGS:
            case GOLDEN_BOOTS:
                return getGoldenReforgedItem(material, reforge);
            default:
                return null;
        }
    }

    private ItemStack getIronDiamondNetheriteReforgedItem(Material material, String reforge) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        // Add armor attribute
        switch (material) {
            case IRON_HELMET:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 2, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case IRON_CHESTPLATE:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 6, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case IRON_LEGGINGS:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 5, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case IRON_BOOTS:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 2, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case DIAMOND_HELMET:
            case NETHERITE_HELMET:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 3, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case DIAMOND_CHESTPLATE:
            case NETHERITE_CHESTPLATE:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 8, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case DIAMOND_LEGGINGS:
            case NETHERITE_LEGGINGS:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 6, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case DIAMOND_BOOTS:
            case NETHERITE_BOOTS:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 3, AttributeModifier.Operation.ADD_NUMBER);
                break;
		default:
			break;
        }

        List<String> lore = new ArrayList<>();

        switch (reforge) {
            case "Positive":
                int positiveChance = random.nextInt(4);
                switch (positiveChance) {
                    case 0:
                        addAttribute(meta, Attribute.GENERIC_MAX_HEALTH, 5, AttributeModifier.Operation.ADD_NUMBER);
                        lore.add(ChatColor.GOLD + "Legendary");
                        break;
                    case 1:
                        addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, 0.1, AttributeModifier.Operation.ADD_NUMBER);
                        lore.add(ChatColor.GOLD + "Berserk");
                        break;
                    case 2:
                        addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, 0.05, AttributeModifier.Operation.ADD_NUMBER);
                        lore.add(ChatColor.BLUE + "Aggressive");
                        break;
                    case 3:
                        addAttribute(meta, Attribute.GENERIC_MAX_HEALTH, 2, AttributeModifier.Operation.ADD_NUMBER);
                        lore.add(ChatColor.BLUE + "Rare");
                        break;
                }
                break;
            case "Neutral":
                int neutralChance = random.nextInt(2);
                switch (neutralChance) {
                    case 0:
                        lore.add(ChatColor.WHITE + "Common");
                        break;
                    case 1:
                        addAttribute(meta, Attribute.GENERIC_MAX_HEALTH, 1, AttributeModifier.Operation.ADD_NUMBER);
                        lore.add(ChatColor.GREEN + "Uncommon");
                        break;
                }
                break;
            case "Negative":
                int negativeChance = random.nextInt(4);
                switch (negativeChance) {
                    case 0:
                        addAttribute(meta, Attribute.GENERIC_MAX_HEALTH, -4, AttributeModifier.Operation.ADD_NUMBER);
                        lore.add(ChatColor.DARK_RED + "Broken");
                        break;
                    case 1:
                        addAttribute(meta, Attribute.GENERIC_MAX_HEALTH, -2, AttributeModifier.Operation.ADD_NUMBER);
                        lore.add(ChatColor.RED + "Crappy");
                        break;
                    case 2:
                        addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, -0.05, AttributeModifier.Operation.ADD_NUMBER);
                        lore.add(ChatColor.RED + "Weak");
                        break;
                    case 3:
                        addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, -0.1, AttributeModifier.Operation.ADD_NUMBER);
                        lore.add(ChatColor.DARK_RED + "Restarted");
                        break;
                }
                break;
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getTurtleReforgedItem() {
        ItemStack item = new ItemStack(Material.TURTLE_HELMET);
        ItemMeta meta = item.getItemMeta();

        addAttribute(meta, Attribute.GENERIC_ARMOR, 2, AttributeModifier.Operation.ADD_NUMBER);
        addAttribute(meta, Attribute.GENERIC_MAX_HEALTH, 10, AttributeModifier.Operation.ADD_NUMBER);
        addAttribute(meta, Attribute.GENERIC_KNOCKBACK_RESISTANCE, 1, AttributeModifier.Operation.ADD_NUMBER);
        addAttribute(meta, Attribute.GENERIC_MOVEMENT_SPEED, -0.5, AttributeModifier.Operation.ADD_NUMBER);

        meta.setLore(Collections.singletonList(ChatColor.BLUE + "Tortoise"));
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getGoldenReforgedItem(Material material, String reforge) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        // Add armor attribute
        switch (material) {
            case GOLDEN_HELMET:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 2, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case GOLDEN_CHESTPLATE:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 5, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case GOLDEN_LEGGINGS:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 3, AttributeModifier.Operation.ADD_NUMBER);
                break;
            case GOLDEN_BOOTS:
                addAttribute(meta, Attribute.GENERIC_ARMOR, 1, AttributeModifier.Operation.ADD_NUMBER);
                break;
		default:
			break;
        }

        List<String> lore = new ArrayList<>();

        switch (reforge) {
            case "Positive":
                addAttribute(meta, Attribute.GENERIC_MAX_HEALTH, 6, AttributeModifier.Operation.ADD_NUMBER);
                addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, 0.125, AttributeModifier.Operation.ADD_NUMBER);
                addAttribute(meta, Attribute.GENERIC_LUCK, 1, AttributeModifier.Operation.ADD_NUMBER);
                lore.add(ChatColor.GOLD + "Kingly");
                break;
            case "Neutral":
                addAttribute(meta, Attribute.GENERIC_MAX_HEALTH, 2, AttributeModifier.Operation.ADD_NUMBER);
                addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, 0.03, AttributeModifier.Operation.ADD_NUMBER);
                addAttribute(meta, Attribute.GENERIC_LUCK, 0.03, AttributeModifier.Operation.ADD_NUMBER);
                lore.add(ChatColor.WHITE + "Common");
                break;
            case "Negative":
                addAttribute(meta, Attribute.GENERIC_MAX_HEALTH, -4, AttributeModifier.Operation.ADD_NUMBER);
                addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, -0.06, AttributeModifier.Operation.ADD_NUMBER);
                addAttribute(meta, Attribute.GENERIC_LUCK, -0.06, AttributeModifier.Operation.ADD_NUMBER);
                lore.add(ChatColor.GREEN + "Knightly");
                break;
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private void addAttribute(ItemMeta meta, Attribute attribute, double amount, AttributeModifier.Operation operation) {
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), attribute.name(), amount, operation);
        meta.addAttributeModifier(attribute, modifier);
    }
}
