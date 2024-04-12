/*      Remote player waypoints for Xaero's Map
        Copyright (C) 2024  Leander Knüttel

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

package tbc.remote_player_waypoints_for_xaero.connections;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import tbc.remote_player_waypoints_for_xaero.ModConfig;
import tbc.remote_player_waypoints_for_xaero.PlayerPosition;
import tbc.remote_player_waypoints_for_xaero.RemotePlayerWaypointsForXaero;
import tbc.remote_player_waypoints_for_xaero.UpdateTask;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

public abstract class MapConnection {
    public URL queryURL;
    public final MinecraftClient mc;

    public MapConnection(ModConfig.ServerEntry serverEntry, UpdateTask updateTask) {
        this.mc = MinecraftClient.getInstance();
    }

    @NotNull
    protected String getBaseURL(ModConfig.ServerEntry serverEntry, boolean useHttps) {
        var baseURL = serverEntry.link.toLowerCase(Locale.ROOT);
        if (!baseURL.startsWith(useHttps ? "https://" : "http://")){
            baseURL = (useHttps ? "https://" : "http://") + baseURL;
        }

        int i = baseURL.indexOf("?");
        if (i != -1){
            baseURL = baseURL.substring(0, i - 1);
        }

        i = baseURL.indexOf("#");
        if (i != -1){
            baseURL = baseURL.substring(0, i - 1);
        }

        if (baseURL.endsWith("index.html")){
            baseURL = baseURL.substring(0, baseURL.length() - 10);
        }

        if (baseURL.endsWith("/")){
            baseURL = baseURL.substring(0, baseURL.length() - 1);
        }
        return baseURL;
    }

    public abstract PlayerPosition[] getPlayerPositions(ModConfig config) throws IOException;

    public PlayerPosition[] HandlePlayerPositions(PlayerPosition[] playerPositions, ModConfig config){
        String clientName = mc.player.getName().getLiteralString();
        String currentDimension = "";
        for (var p : playerPositions){
            if (Objects.equals(p.player, clientName)) {
                currentDimension = p.world;
            }
        }

        for (int i = 0; i < playerPositions.length; i++) {
            UpdateAfkInfo(playerPositions[i], config);

            if (!config.general.debugMode && (!Objects.equals(playerPositions[i].world, currentDimension) || Objects.equals(playerPositions[i].player, clientName))) {
                playerPositions[i] = null;
            }
        }

        return playerPositions;
    }

    public void UpdateAfkInfo(PlayerPosition playerPosition, ModConfig config){
        if (RemotePlayerWaypointsForXaero.lastPlayerDataDic.containsKey(playerPosition.player)) {
            if (RemotePlayerWaypointsForXaero.lastPlayerDataDic.get(playerPosition.player).CompareCords(playerPosition)) {
                if (RemotePlayerWaypointsForXaero.AfkTimeDic.containsKey(playerPosition.player)) {
                    RemotePlayerWaypointsForXaero.AfkTimeDic.put(playerPosition.player, RemotePlayerWaypointsForXaero.AfkTimeDic.get(playerPosition.player) + (config.general.updateDelay / 1000));
                } else {
                    RemotePlayerWaypointsForXaero.AfkTimeDic.put(playerPosition.player, (config.general.updateDelay / 1000));
                }
                if (config.general.debugMode) {
                    mc.inGameHud.getChatHud().addMessage(Text.literal(playerPosition.player + "  afk_time: " + RemotePlayerWaypointsForXaero.AfkTimeDic.get(playerPosition.player)));
                }
                if (RemotePlayerWaypointsForXaero.AfkTimeDic.get(playerPosition.player) >= config.general.timeUntilAfk) {
                    RemotePlayerWaypointsForXaero.AfkDic.put(playerPosition.player, true);
                }
            } else {
                RemotePlayerWaypointsForXaero.AfkTimeDic.put(playerPosition.player, 0);
                RemotePlayerWaypointsForXaero.AfkDic.put(playerPosition.player, false);
            }
        }
        RemotePlayerWaypointsForXaero.lastPlayerDataDic.put(playerPosition.player, playerPosition);
    }
}
