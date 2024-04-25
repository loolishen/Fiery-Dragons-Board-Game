package game;

import edu.monash.fit2099.engine.actions.*;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.positions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing the Player.
 * Created by: Adrian Kristanto
 * Modified by: Maliha Tariq
 */
public class Player extends Actor {
    private int hitPoints; // Updated to include hit points (HP)
    private int maxHitPoints; // To store maximum hit points
    private int stamina; // Added stamina attribute
    private int maxStamina; // To store maximum stamina
    private int staminaRecoveryRate; // Stamina recovery rate in percentage
    private int turnsSinceFocus; // To track turns since using "Focus" skill
    private Map<String, Integer> weaponSkills = new HashMap<>(); // To track weapon skills
    private boolean hasBroadsword = false; // Indicates whether the player has the Broadsword
    private int broadswordSkillTurns = 0; // Number of turns the Broadsword skill is active
    private boolean hasPickedUpBroadsword = false; // Indicates whether the player has picked up the Broadsword
    private boolean isBroadswordSkillActive = false; // Indicates whether the Broadsword skill is active
    private boolean isInVoid = false;
    private boolean hasOldKey = false; // Indicates whether the player has the Old Key
    private int gateX; // X-coordinate of the gate's location
    private int gateY; // Y-coordinate of the gate's location
    private GameMap destinationMap; // Define destinationMap
    private int destinationX; // Define destinationX
    private int destinationY; // Define destinationY
    private Map<Class<? extends UsableItem>, UsableItem> itemStrategies = new HashMap<>();




    /**
     * Constructor.
     *
     * @param name        Name to call the player in the UI
     * @param displayChar Character to represent the player in the UI
     * @param hitPoints   Player's starting number of hitpoints
     * @param maxHitPoints Player's maximum hitpoints
     * @param stamina     Player's starting stamina
     * @param maxStamina  Player's maximum stamina
     * @param staminaRecoveryRate Player's stamina recovery rate (percentage)
     * @param gateX       X-coordinate of the gate's location
     * @param gateY       Y-coordinate of the gate's location
     */
    public Player(String name, char displayChar, int hitPoints, int maxHitPoints,
                  int stamina, int maxStamina, int staminaRecoveryRate, int gateX, int gateY) {
        super(name, displayChar, hitPoints);
        this.hitPoints = hitPoints;
        this.maxHitPoints = maxHitPoints;
        this.stamina = stamina;
        this.maxStamina = maxStamina;
        this.staminaRecoveryRate = staminaRecoveryRate;
        this.turnsSinceFocus = 0;
        this.weaponSkills.put("Broadsword", 0); // Initialize Broadsword skill level to 0
        this.weaponSkills.put("BroadswordHitRate", 80); // Initialize Broadsword hit rate to 80%
        this.gateX = gateX;
        this.gateY = gateY;
        itemStrategies.put(HealingItem.class, new HealingItem("Healing Vial", 'a'));
        itemStrategies.put(RefreshingItem.class, new RefreshingItem("Refreshing Flask", 'u'));

    }

    /**
     * Checks if the player is currently in a void space.
     *
     * @return {@code true} if the player is in a void space, {@code false} otherwise.
     */
    public boolean isInVoid() {
        return isInVoid;
    }

    /**
     * Attempts to unlock the gate at the specified location using the Old Key if the player has it.
     *
     * @param location The location of the gate to unlock.
     * @param map      The game map containing the gate.
     * @param display  The display used to show messages and interact with the player.
     */
    public void unlockGate(Location location, GameMap map, Display display) {
        if (location.x() == gateX && location.y() == gateY && hasOldKey) {
            Gate gate = (Gate) location.getGround();
            gate.unlock(); // Unlock the gate
            display.println("The gate is unlocked.");

            // Create a TravelAction to move the player to the destination map and location
            TravelAction travelAction = new TravelAction(destinationMap, destinationX, destinationY);

            // Execute the TravelAction
            travelAction.execute(this, map);
        }
    }


    /**
     * Performs the player's turn, including various actions and interactions.
     *
     * @param actions    The list of available actions for the player.
     * @param lastAction The last action performed, if any.
     * @param map        The game map representing the player's current location.
     * @param display    The display used to show messages and interact with the player.
     * @return The action selected by the player for the current turn.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        // Handle multi-turn Actions
        if (lastAction.getNextAction() != null)
            return lastAction.getNextAction();

        // Recover stamina
        recoverStamina();

        // Display hit points and stamina
        displayPlayerStats(display);

        // Handle "Focus" skill
        handleFocusSkill();

        Location playerLocation = map.locationOf(this);

        // Check if the player is on a void ground
        if (playerLocation.getGround().getDisplayChar() == '+') {
            isInVoid = true;
            display.println(FancyMessage.YOU_DIED);
            display.println("Game Over");
            System.exit(0);
        }

        // Check if the player is in front of a gate
        if (playerLocation.getGround() instanceof Gate) {
            Gate gate = (Gate) playerLocation.getGround();
            if (gate.isLocked()) {
                // The gate is locked, check if the player has the Old Key
                if (hasOldKey) {
                    // Offer the option to unlock the gate
                    display.println("a: Unlock the gate with the Old Key");
                    char choice = display.readChar();
                    switch (choice) {
                        case 'a':
                            unlockGate(playerLocation, map, display);
                            break;
                    }
                } else {
                    display.println("The gate is locked, and you don't have the Old Key.");
                }
            }
        }

        // Check if the player is on a SkillGround (with displayChar '1')
        int skillGroundX = 27;
        int skillGroundY = 6;

        if (map.locationOf(this).x() == skillGroundX && map.locationOf(this).y() == skillGroundY) {
            if (!hasPickedUpBroadsword) {
                display.println("a: The Abstracted One (150/150) picks up the Broadsword");
                char choice = display.readChar();
                switch (choice) {
                    case 'a':
                        pickUpBroadsword(map, display);
                        break; // Don't forget to break after performing the action
                }
            } else {
                display.println("a: The Abstracted One (150/150) activates the skill of Broadsword");
                display.println("b: The Abstracted One (150/150) drops the skill of Broadsword");
                char choice = display.readChar();
                switch (choice) {
                    case 'a':
                        activateBroadswordFocus();
                        display.println("The Abstracted One (150/150) takes a deep breath and focuses all their might");
                        break;
                    case 'b':
                        dropBroadswordSkill();
                        break;
                }
            }
        }

        // Return the console menu
        Menu menu = new Menu(actions);
        int currentX = map.locationOf(this).x();
        int currentY = map.locationOf(this).y();

        Action action = menu.showMenu(this, display);

        // Check if the Broadsword was picked up and the player moved
        if (hasPickedUpBroadsword && (currentX != map.locationOf(this).x() || currentY != map.locationOf(this).y())) {
            // Change the ground type at the previous location
            map.at(currentX, currentY).setGround(new Floor());
        }

        return action;
    }


    /**
     * This method to recover stamina each turn based on the recovery rate.
     */
    private void recoverStamina() {
        int staminaRecoveryAmount = (int) ((maxStamina * staminaRecoveryRate) / 100.0);
        if (stamina + staminaRecoveryAmount <= maxStamina) {
            stamina += staminaRecoveryAmount;
        } else {
            stamina = maxStamina;
        }
    }

    /**
     * Retrieves the current hit points (HP) of the player.
     *
     * @return The current hit points of the player.
     */
    public int getHitPoints() {
        return hitPoints;
    }


    /**
     * Method to display the player's hit points and stamina.
     *
     * @param display The game display
     */
    private void displayPlayerStats(Display display) {
        display.println("Hit Points: " + hitPoints + "/" + maxHitPoints);
        display.println("Stamina: " + stamina + "/" + maxStamina);
    }

    /**
     * Handles the "Focus" skill for the Broadsword. This method decreases the remaining duration of the "Focus" skill,
     * and if the skill duration has expired, it resets the damage multiplier and hit rate for the Broadsword.
     */
    private void handleFocusSkill() {
        if (weaponSkills.containsKey("Broadsword")) {
            if (turnsSinceFocus > 0) {
                // Decrease the turns remaining for "Focus" skill
                turnsSinceFocus--;
                if (turnsSinceFocus == 0) {
                    // "Focus" skill duration has expired, reset damage multiplier and hit rate
                    resetBroadswordSkills();
                }
            }
        }
    }


    /**
     * Attempts to pick up the Broadsword item from the specified location on the game map.
     *
     * @param map     The GameMap where the Broadsword is located.
     * @param display The Display object for printing messages.
     */
    public void pickUpBroadsword(GameMap map, Display display) {
        if (map.locationOf(this).x() == 27 && map.locationOf(this).y() == 6 && !hasPickedUpBroadsword) {
            hasPickedUpBroadsword = true;
            hasBroadsword = true; // Player now has the Broadsword
            display.println("The Abstracted One picks up the Broadsword.");
        }
    }

    /**
     * Increases the damage multiplier for the Broadsword skill.
     *
     * @param damageMultiplierIncrease The amount by which to increase the damage multiplier.
     * It should be a positive decimal value (e.g., 0.10 for a 10% increase).
     * @throws IllegalArgumentException If damageMultiplierIncrease is not a positive value.
     */
    private void increaseBroadswordDamageMultiplier(double damageMultiplierIncrease) {
        if (damageMultiplierIncrease <= 0) {
            throw new IllegalArgumentException("Damage multiplier increase must be a positive value.");
        }

        int currentDamageMultiplier = weaponSkills.get("Broadsword");
        weaponSkills.put("Broadsword", (int) (currentDamageMultiplier + damageMultiplierIncrease));
    }


    /**
     * Activates the "Focus" skill for the Broadsword if certain conditions are met.
     * The Broadsword skill increases damage and hit rate for a limited duration.
     * To activate the skill, the player must have the Broadsword, the skill must not already be active,
     * and the player's stamina must be sufficient.
     * If the conditions are met, the skill is activated for 5 turns, increasing damage and hit rate,
     * and deducting stamina.
     */
    public void activateBroadswordFocus() {
        if (hasBroadsword && broadswordSkillTurns == 0 && stamina >= maxStamina * 0.2) {
            // Activate the skill
            broadswordSkillTurns = 5; // Set the skill duration to 5 turns
            // Apply damage multiplier increase and set hit rate to 90%
            increaseBroadswordDamageMultiplier(0.10);
            weaponSkills.put("BroadswordHitRate", 90);
            // Deduct stamina
            stamina -= maxStamina * 0.2;
        }
    }

    /**
     * Drops the skill of the Broadsword if it is currently active.
     * This method resets the duration of the Broadsword skill and removes its associated damage multiplier and hit rate.
     */
    public void dropBroadswordSkill() {
        if (hasBroadsword && broadswordSkillTurns > 0) {
            broadswordSkillTurns = 0; // Reset the skill duration
            // Reset damage multiplier and hit rate
            resetBroadswordSkills();
        }
    }



    /**
     * Resets the damage multiplier and hit rate for the Broadsword skill.
     * This method removes the entries related to Broadsword skills from the player's weapon skills.
     */
    private void resetBroadswordSkills() {
        // Remove the damage multiplier and hit rate entries
        weaponSkills.remove("Broadsword");
        weaponSkills.remove("BroadswordHitRate");
    }

    /**
     * Reduces the player's hit points by a specified amount and handles the player's death if hit points reach zero.
     *
     * @param points  The amount of damage to deduct from the player's hit points.
     * @param display The display object for printing messages.
     */
    public void hurt(int points, Display display) {
        super.hurt(points);
        if (points > 0 && isInVoid) {
            display.println(FancyMessage.YOU_DIED);
            display.println("Game Over");
            System.exit(0);
        }
    }
    public void increaseStamina(double amount) {
        stamina += amount;
        if (stamina > maxStamina) {
            stamina = maxStamina;
        }
    }

    public int getMaxStamina() {
        return maxStamina;
    }
    public void useItemOnGround(Item item) {
        UsableItem usableItem = itemStrategies.get(item.getClass());
        if (usableItem != null) {
            usableItem.use(this);
        }
    }
}