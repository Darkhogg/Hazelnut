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

import java.util.Arrays;

public enum CrazyCastleEdition {
  BBCC2(
    "Bugs Bunny Crazy Castle 2",
    "BUGSCRAZYCASTLE2",
    0x8000,
    0xC000,
    0x2FA2,
    0x1CB9,
    0x42A3,
    0x2BF0,
    0x27D1,
    0x288A,
    5,
    new int[]{0x14000, 0x18000, 0x1c000},
    new int[]{0x800, 0x800, 0xc00},
    new int[]{10, 9, 10},
    new int[]{1, 1, 8},
    new int[][]{{}, {}, {}}
  ),

  // HUGO(
  //   "Hugo (E)",
  //   "HUGO",
  //   0x8000,
  //   0xC000,
  //   0x28C4,
  //   0x1437,
  //   0x3BBC,
  //   0x2495,
  //   0x1FEE,
  //   0x20A7,
  //   5,
  //   new int[]{0x14000, 0x18000, 0x1c000},
  //   new int[]{0x1400, 0x800, 0xc00},
  //   new int[]{10, 9, 10},
  //   new int[]{1, 1, 8},
  //   new int[][]{{0x2591}, {}, {}}
  // ),

  // MME("Mickey Mouse (E)", "MICKEY MOUSE"),
  // MM2J("Mickey Mouse II (J)", "MICKEY MOUSE 2"),
  ;

  private final String name;
  private final String title;

  private final int levelTilesetPointersAddress;
  private final int spriteTilesetPointersAddress;

  private final int spritePointersAddress;

  private final int comboSpritesAddress;
  private final int comboTypesAddress;

  private final int passwordsAddress;
  private final int enemyGroupsAddress;
  private final int levelPointersAddress;

  private final int levelBanksBase;
  private final int[] levelBanksAddress;
  private final int[] levelBanksReservedSize;
  private final int[] levelBanksReservedIndexStart;
  private final int[] levelBanksReservedIndexCount;
  private final int[][] levelBanksExtraAddresses;

  CrazyCastleEdition(
    String title,
    String romTitle,

    int levelTilesetPointersAddress,
    int spriteTilesetPointersAddress,

    int spritePointersAddress,

    int comboSpritesAddress,
    int comboTypesAddress,

    int passwordsAddress,
    int enemyGroupsAddress,
    int levelPointersAddress,

    int levelBanksBase,
    int[] levelBanksAddress,
    int[] levelBanksReservedSize,
    int[] levelBanksReservedIndexStart,
    int[] levelBanksReservedIndexCount,
    int[][] levelBanksExtraAddresses
  ) {
    this.name = title;
    this.title = romTitle;

    this.levelTilesetPointersAddress = levelTilesetPointersAddress;
    this.spriteTilesetPointersAddress = spriteTilesetPointersAddress;

    this.spritePointersAddress = spritePointersAddress;

    this.comboSpritesAddress = comboSpritesAddress;
    this.comboTypesAddress = comboTypesAddress;

    this.passwordsAddress = passwordsAddress;
    this.enemyGroupsAddress = enemyGroupsAddress;
    this.levelPointersAddress = levelPointersAddress;

    this.levelBanksBase = levelBanksBase;
    this.levelBanksAddress = Arrays.copyOf(levelBanksAddress, levelBanksAddress.length);
    this.levelBanksReservedSize = Arrays.copyOf(levelBanksReservedSize, levelBanksReservedSize.length);
    this.levelBanksReservedIndexStart = Arrays.copyOf(levelBanksReservedIndexStart, levelBanksReservedIndexStart.length);
    this.levelBanksReservedIndexCount = Arrays.copyOf(levelBanksReservedIndexCount, levelBanksReservedIndexCount.length);

    this.levelBanksExtraAddresses = new int[levelBanksExtraAddresses.length][];
    for (var i = 0; i < levelBanksExtraAddresses.length; i++) {
      this.levelBanksExtraAddresses[i] = Arrays.copyOf(levelBanksExtraAddresses[i], levelBanksExtraAddresses[i].length);
    }

    if (
      levelBanksAddress.length != levelBanksReservedSize.length
      || levelBanksAddress.length != levelBanksReservedIndexStart.length
      || levelBanksAddress.length != levelBanksReservedIndexCount.length
      || levelBanksAddress.length != levelBanksExtraAddresses.length
    ) {
      throw new IllegalArgumentException(
        "all array arguments must have the same length (" + levelBanksAddress.length + ", "
        + levelBanksReservedSize.length + ", " + levelBanksReservedIndexStart.length + ", "
        + levelBanksReservedIndexCount.length + ", " + levelBanksExtraAddresses.length + ")"
      );
    }
  }

  public String getName() {
    return name;
  }

  public String getTitle() {
    return title;
  }

  public int getLevelTilesetPointersAddress() {
    return levelTilesetPointersAddress;
  }

  public int getSpriteTilesetPointersAddress() {
    return spriteTilesetPointersAddress;
  }

  public int getSpritePointersAddress() {
    return spritePointersAddress;
  }

  public int getComboSpritesAddress() {
    return comboSpritesAddress;
  }

  public int getComboTypesAddress() {
    return comboTypesAddress;
  }

  public int getPasswordsAddress() {
    return passwordsAddress;
  }

  public int getEnemyGroupsAddress() {
    return enemyGroupsAddress;
  }

  public int getLevelPointersAddress() {
    return levelPointersAddress;
  }

  public static CrazyCastleEdition forTitle(String title) {
    for (var edition : values()) {
      if (title.equals(edition.title)) {
        return edition;
      }
    }

    return null;
  }

  public int getLevelBanksBase() {
    return levelBanksBase;
  }

  public int getLevelBanksLength() {
    return levelBanksAddress.length;
  }

  public int[] getLevelBanksAddress() {
    return Arrays.copyOf(levelBanksAddress, levelBanksAddress.length);
  }

  public int getLevelBanksAddress(int index) {
    return levelBanksAddress[index];
  }

  public int[] getLevelBanksReservedSize() {
    return Arrays.copyOf(levelBanksReservedSize, levelBanksReservedSize.length);
  }

  public int getLevelBanksReservedSize(int index) {
    return levelBanksReservedSize[index];
  }

  public int[] getLevelBanksReservedIndexStart() {
    return Arrays.copyOf(levelBanksReservedIndexStart, levelBanksReservedIndexStart.length);
  }

  public int getLevelBanksReservedIndexStart(int index) {
    return levelBanksReservedIndexStart[index];
  }

  public int[] getLevelBanksReservedIndexCount() {
    return Arrays.copyOf(levelBanksReservedIndexCount, levelBanksReservedIndexCount.length);
  }

  public int getLevelBanksReservedIndexCount(int index) {
    return levelBanksReservedIndexCount[index];
  }

  public int[][] getLevelBanksExtraAddresses() {
    final var ret = new int[levelBanksExtraAddresses.length][];
    for (var i = 0; i < ret.length; i++) {
      ret[i] = Arrays.copyOf(levelBanksExtraAddresses[i], levelBanksExtraAddresses[i].length);
    }
    return ret;
  }

  public int[] getLevelBanksExtraAddresses(int index) {
    return Arrays.copyOf(levelBanksExtraAddresses[index], levelBanksExtraAddresses[index].length);
  }
}
