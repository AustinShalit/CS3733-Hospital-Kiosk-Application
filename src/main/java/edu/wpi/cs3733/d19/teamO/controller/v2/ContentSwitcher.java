package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.util.function.BiConsumer;

/**
 * Consumes new content to switch to.
 */
interface ContentSwitcher extends BiConsumer<Class, String> {
}
