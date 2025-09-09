/*
 *    This file is part of the Map Link mod
 *    licensed under the GNU GPL v3 License.
 *    (some parts of this file are originally from the Distant Horizons mod by James Seibel)
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

package de.the_build_craft.maplink.common;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

/**
 * has constants for MC versions
 *
 * @author James Seibel
 * @author Leander Knüttel
 * @version 19.06.2025
 */
public class VersionConstants
{
	public static final VersionConstants INSTANCE = new VersionConstants();

	private VersionConstants()
	{
	}

	public String getMinecraftVersion()
	{
		#if MC_VER < MC_1_19_2
		return Minecraft.getInstance().getGame().getVersion().getId();
		#elif MC_VER < MC_1_21_6
		return SharedConstants.getCurrentVersion().getId();
		#else
		return SharedConstants.getCurrentVersion().id();
		#endif
	}
}