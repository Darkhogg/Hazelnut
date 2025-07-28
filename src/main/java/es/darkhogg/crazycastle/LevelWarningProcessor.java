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

import es.darkhogg.util.IntVector;

import java.util.ArrayList;
import java.util.List;

public class LevelWarningProcessor {
  public static final LevelWarningProcessor INSTANCE = new LevelWarningProcessor();

  private LevelWarningProcessor() {}

  public static enum LevelWarningType {
    DOOR_ITEM_WITHOUT_DOOR;
  }

  public static record LevelWarning (LevelWarningType type, IntVector position, Object argument) {}

  public List<LevelWarning> getWarnings(Level level, ComboInfoMap comboInfo) {
    final var warnings = new ArrayList<LevelWarning>();

    // door items warnings
    for (final var di : level.getRomLevel().getDoorItems()) {
      final var cv = level.getRomLevel().getGrid().get(di.getX(), di.getY() + 2);
      final var ct = comboInfo.getComboType(cv);
      if (ct != ComboType.DOOR) {
        warnings.add(new LevelWarning(LevelWarningType.DOOR_ITEM_WITHOUT_DOOR, di.getPosition(), null));
      }
    }

    return warnings;
  }

  public String getWarningString(LevelWarning warning) {
    switch(warning.type()) {
      case DOOR_ITEM_WITHOUT_DOOR:
        return "Door Item needs a DOOR combo two positions below";
      default:
        return null;
    }
  }
}
