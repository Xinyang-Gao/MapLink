/*
 *    This file is part of the Remote player waypoints for Xaero's Map mod
 *    licensed under the GNU GPL v3 License.
 *    (some parts of this file are originally from "RemotePlayers" by TheMrEngMan)
 *
 *    Copyright (C) 2024 - 2025  Leander Knüttel and contributors
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.the_build_craft.remote_player_waypoints_for_xaero.mixins.neoforge.mods.xaerominimap;

import de.the_build_craft.remote_player_waypoints_for_xaero.common.CommonModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
#if MC_VER < MC_1_21_5
import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
#endif
import xaero.hud.minimap.waypoint.render.WaypointMapRenderer;


/**
 * @author TheMrEngMan
 * @author Leander Knüttel
 * @version 21.04.2025
 */

@Pseudo
#if MC_VER < MC_1_21_5
@Mixin(WaypointsGuiRenderer.class)
#else
@Mixin(WaypointMapRenderer.class)
#endif
public class WaypointsGuiRendererMixin {

    @Inject(method = "getOrder", at = @At("RETURN"), cancellable = true, remap = false)
    private void injected(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(CommonModConfig.Instance.getWaypointLayerOrder());
    }
}
