/*
 * Copyright 2015 Paul Gestwicki
 *
 * This file is part of The Social Startup Game
 *
 * The Social Startup Game is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Social Startup Game is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with The Social Startup Game.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.bsu.cybersec.core.ui;

import java.util.List;

public final class ListStringifier {
    public String stringify(List<String> list) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0, limit = list.size(); i < limit; i++) {
            final String s = list.get(i);
            builder.append(s);
            if (i < limit - 1) {
                builder.append(", ");
            }
            if (i == limit - 2) {
                builder.append("and ");
            }
        }
        return builder.toString();
    }
}
