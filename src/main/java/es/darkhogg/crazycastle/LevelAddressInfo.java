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

/**
 * A class representing the information about a level address, as a bank and index pair, optionally containing the
 * location information within the bank itself.
 */
public class LevelAddressInfo {
    private final int bank;
    private final int index;
    private final int location;

    public LevelAddressInfo(int bank, int index, int location) {
        this.bank = bank;
        this.index = index;
        this.location = location;
    }

    public LevelAddressInfo(int bank, int index) {
        this(bank, index, -1);
    }

    public int getBank() {
        return bank;
    }

    public int getIndex() {
        return index;
    }

    public int getLocation() {
        return location;
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder("LevelAddressInfo(bank=");
        sb.append(bank);
        sb.append(", index=");
        sb.append(index);
        if (location >= 0) {
            sb.append(", location=0x");
            sb.append(Integer.toHexString(location));
        }
        sb.append(")");
        return sb.toString();
    }
}
