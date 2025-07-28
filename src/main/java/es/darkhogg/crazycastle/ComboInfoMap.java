/**
 * This file is part of Hazelnut.
 *
 * Hazelnut is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hazelnut is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hazelnut.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.darkhogg.crazycastle;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ComboInfoMap {
    private final List<ComboCollision> colls;
    private final List<ComboType> types;

    private ComboInfoMap(List<ComboCollision> colls, List<ComboType> types) {
        if (colls.size() != 256) {
            throw new IllegalAccessError("collisions list must be exactly 256 entries long (was " + colls.size() + ")");
        }
        if (types.size() != 256) {
            throw new IllegalAccessError("types list must be exactly 256 entries long (was " + types.size() + ")");
        }

        this.colls = new ArrayList<>(colls);
        this.types = new ArrayList<>(types);
    }

    public static ComboInfoMap createFromBuffer(ByteBuffer buffer) {
        final var colls = new ComboCollision[256];
        final var types = new ComboType[256];

        for (var i = 0; i < 256; i++) {
            final var bt = buffer.get();

            colls[i] = (bt & 0x80) != 0 ? ComboCollision.SOLID : ComboCollision.NONSOLID;
            types[i] = ComboType.valueOf((byte)(bt & 0x7F));
        }

        return new ComboInfoMap(Arrays.asList(colls), Arrays.asList(types));
    }

    public ComboCollision getComboColl(byte combo) {
        return colls.get((int)combo & 0xFF);
    }

    public ComboType getComboType(byte combo) {
        return types.get((int)combo & 0xFF);
    }
}
