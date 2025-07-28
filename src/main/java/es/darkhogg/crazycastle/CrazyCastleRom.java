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

import es.darkhogg.gameboy.Sprite;
import es.darkhogg.gameboy.SpriteInfo;
import es.darkhogg.gameboy.Tile;
import es.darkhogg.util.BufferUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * TODO Document this class
 *
 * @author Daniel Escoz (Darkhogg)
 * @version 1.0
 */
public final class CrazyCastleRom {

	/** Sprite index of the main character */
	private static final int SPRITE_BUGS_INDEX = 0;

	/** Sprite indices of the items */
	private static final int[] SPRITE_ITEMS_INDICES = new int[]{
		48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
	};

	/** Sprite indices of the enemies */
	private static final int[] SPRITE_ENEMIES_INDICES = new int[]{
		68, 68, 116, 116, 164, 168, 174,
	};

	/** Tileset indices of the level tilesets */
	private static final int[] TILESET_LEVELS_INDICES = new int[]{
		0, 1, 2, 3, 4, 5, 6,
	};

	/** Tileset indices of the enemy tilesets */
	private static final int[] TILESET_ENEMIES_INDICES = new int[]{
		3, 4, 5, 6,
	};

	/** Tileset index of the items/bugs tileset */
	private static final int TILESET_MAIN_INDEX = 0;

	/** ROM edition */
	private final CrazyCastleEdition edition;

	/** ROM contents */
	private final ByteBuffer contents;

	/** Map of combo types and collision */
	private final ComboInfoMap comboInfo;

	/** A list of all the levels read from the ROM */
	private final List<Level> levels;

	/** A list of all seven theme tilesets from the ROM */
	private final List<List<Tile>> themeTilesets;

	/** Tileset that contains all items and some other things */
	private final List<Tile> itemTileset;

	/** A list of all enemy tilesets */
	private final List<List<Tile>> enemyTilesets;

	/** A list of all seven theme spritesets from the ROM */
	private final List<List<Sprite>> themeSpritesets;

	/** Spriteset that contains all items */
	private final List<Sprite> itemSpriteset;

	/** List of the enemy spritesets */
	private final List<List<Sprite>> enemySpritesets;

	/** Sprite representing bugs bunny */
	private final Sprite bugsSprite;

	/**
	 * Creates a new BugsRom with the ROM contents given and parses it to get an
	 * internal abstract representation of it.
	 *
	 * @param edition The ROM edition to use
	 * @param contents The full ROM file
	 */
	private CrazyCastleRom(CrazyCastleEdition edition, ByteBuffer contents) {
		this.edition = edition;
		this.contents = contents;
		contents.order(ByteOrder.LITTLE_ENDIAN);

		contents.position(edition.getComboTypesAddress());
		comboInfo = ComboInfoMap.createFromBuffer(contents);

		// Get the passwords
		String[] passwords = new String[ 29 ];
		contents.position(edition.getPasswordsAddress());
		for (int i = 0; i < 28; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < 4; j++) {
				char chr = (char)( 'A' + (contents.get() & 0xFF) );
				sb.append(chr);
			}
			passwords[i] = sb.toString();
		}

		// Get the enemy groups
		final var groups = new EnemyGroup[ 29 ];
		contents.position(edition.getEnemyGroupsAddress());
		for (int i = 0; i < 29; i++) {
			groups[i] = EnemyGroup.valueOf(contents.get());
		}

		// Get the level addresses
		final var levelAddrInfos = new LevelAddressInfo[29];
		contents.position(edition.getLevelPointersAddress());
		for (int i = 0; i < 29; i++) {
			final var bank = contents.get();
			final var index = contents.get();

			final var bankAddr = bank * 0x4000;
			final var ptrAddr = bankAddr + (index * 2);
			final var location = contents.getShort(ptrAddr);

			levelAddrInfos[i] = new LevelAddressInfo(bank, index, location);
		}

		// Get the levels
		final var levels = new Level[ 29 ];
		for (int i = 0; i < 29; i++) {
			final var info = levelAddrInfos[i];
			final var bankAddr = info.getBank() * 0x4000;

			final var levelAddr = bankAddr - 0x4000 + info.getLocation();

			contents.position(levelAddr);
			final var rl = RomLevelFormat.getInstance().loadRomLevelFromBuffer(contents);
      rl.prune(comboInfo);

			levels[i] = new Level( rl, groups[i], passwords[i] );
		}

		// Set the levels, at last
		this.levels = Arrays.asList(levels);

		// Get the main theme tilesets
		final var tsLvlPtrAddr = edition.getLevelTilesetPointersAddress();
		final var tsLvlPtrOffset = (tsLvlPtrAddr & 0xC000) - 0x4000;
		@SuppressWarnings( "unchecked" ) List<List<Tile>> themeTilesets =
			Arrays.asList((List<Tile>[])new List[ 7 ]);
		for (int i = 0; i < 7; i++) {
			contents.position(tsLvlPtrOffset + BufferUtils.getShortPointer(contents, tsLvlPtrAddr, TILESET_LEVELS_INDICES[i]));
			List<Tile> tiles = Arrays.asList(new Tile[ 128 ]);
			for (int j = 0; j < 128; j++) {
				tiles.set(j, Tile.fromBuffer(contents));
			}
			themeTilesets.set(i, tiles);
		}
		this.themeTilesets = themeTilesets;

		// load the combo sprite info
		contents.position(edition.getComboSpritesAddress());
		final var info = new SpriteInfo[256];
		for (var i = 0; i < info.length; i++) {
			info[i] = SpriteInfo.fromComboBuffer(contents);
		}

		// Create the combosets (a.k.a. themeSpritesets)
		@SuppressWarnings( "unchecked" ) List<List<Sprite>> themeSpritesets =
			Arrays.asList((List<Sprite>[])new List[ 7 ]);
		for (int i = 0; i < 7; i++) {
			List<Sprite> sprites = Arrays.asList(new Sprite[ 256 ]);

			for (int j = 0; j < sprites.size(); j++) {
				List<Tile> tiles = themeTilesets.get(i);
				Sprite spr = info[j].generateSpriteFromTileSet(tiles);
				sprites.set(j, spr);
			}

			themeSpritesets.set(i, sprites);
		}

		this.themeSpritesets = themeSpritesets;

		// Create the item tileset
		final var tsSprPtrAddr = edition.getSpriteTilesetPointersAddress();
		final var tsSprPtrOffset = (tsSprPtrAddr & 0xC000) - 0x4000;

		contents.position(tsSprPtrOffset + BufferUtils.getShortPointer(contents, tsSprPtrAddr, TILESET_MAIN_INDEX));
		final var itemTileset = Arrays.asList(new Tile[ 128 ]);
		for (var j = 0; j < itemTileset.size(); j++) {
			itemTileset.set(j, Tile.fromBuffer(contents));
		}
		this.itemTileset = itemTileset;

		// Read the items
		final var itemInfo = new SpriteInfo[ItemType.values().length];
		for (var i = 0; i < itemInfo.length; i++) {
			contents.position(BufferUtils.getShortPointer(contents, edition.getSpritePointersAddress(), SPRITE_ITEMS_INDICES[i]));
			itemInfo[i] = SpriteInfo.fromSpriteBuffer(contents);
		}

		// Create the item spriteset
		itemSpriteset = Arrays.asList(new Sprite[ itemInfo.length ]);
		for (int j = 0; j < itemInfo.length; j++) {
			Sprite spr = itemInfo[j].generateSpriteFromTileSet(itemTileset);
			itemSpriteset.set(j, spr);
		}

		// Create the bugs bunny sprite
		contents.position(BufferUtils.getShortPointer(contents, edition.getSpritePointersAddress(), SPRITE_BUGS_INDEX));
		final var bugsSpriteInfo = SpriteInfo.fromSpriteBuffer(contents);
		bugsSprite = bugsSpriteInfo.generateSpriteFromTileSet(itemTileset);

		// Create the enemy tilesets
		@SuppressWarnings( "unchecked" )
		List<List<Tile>> enemyTilesets = Arrays.asList((List<Tile>[])new List[ 4 ]);
		for (int i = 0; i < 4; i++) {
			contents.position(tsSprPtrOffset + BufferUtils.getShortPointer(contents, tsSprPtrAddr, TILESET_ENEMIES_INDICES[i]));
			List<Tile> tiles = Arrays.asList(new Tile[ 128 ]);
			for (int j = 0; j < 128; j++) {
				tiles.set(j, Tile.fromBuffer(contents));
			}
			enemyTilesets.set(i, tiles);
		}
		this.enemyTilesets = enemyTilesets;

		// Load the enemy info

		final var enemyInfo = new SpriteInfo[EnemyType.values().length];
		for (var i = 0; i < enemyInfo.length; i++) {
			contents.position(BufferUtils.getShortPointer(contents, edition.getSpritePointersAddress(), SPRITE_ENEMIES_INDICES[i]));
			enemyInfo[i] = SpriteInfo.fromSpriteBuffer(contents);
		}

		// Create the enemy spritesets
		@SuppressWarnings( "unchecked" ) List<List<Sprite>> enemySpritesets =
			Arrays.asList((List<Sprite>[])new List[ 4 ]);
		for (int i = 0; i < 4; i++) {
			List<Sprite> sprites = Arrays.asList(new Sprite[ enemyInfo.length ]);

			for (int j = 0; j < sprites.size(); j++) {
				List<Tile> tiles = enemyTilesets.get(i);
				Sprite spr = enemyInfo[j].generateSpriteFromTileSet(tiles, 128);
				sprites.set(j, spr);
			}

			enemySpritesets.set(i, sprites);
		}
		this.enemySpritesets = enemySpritesets;
	}

	/**
	 * TODO Document
	 */
	private void expandBanks() {
		for (var b = 0; b < edition.getLevelBanksLength(); b++) {
			final int bankAddr = edition.getLevelBanksAddress(b);
			final int ptrAddr = bankAddr + edition.getLevelBanksReservedIndexStart(b) * 2;
			final int size = edition.getLevelBanksReservedSize(b);
			final int indices = edition.getLevelBanksReservedIndexCount(b);

			contents.order(ByteOrder.LITTLE_ENDIAN);
			final var shStart = contents.getShort(ptrAddr);

			final var start = bankAddr + shStart - 0x4000;
			final var newStart = bankAddr + 0x4000 - size;

			final var shNewStart = (short)(0x4000 + newStart - bankAddr);

			if (newStart != start) {
				final var bytes = new byte[size];
				contents.get(start, bytes);
				contents.put(newStart, bytes);

        final var extras = edition.getLevelBanksExtraAddresses(b);
        final var addresses = new int[indices + extras.length];
        for (var i = 0; i < addresses.length; i++) {
          addresses[i] = i < indices ? ptrAddr + (i * 2) : extras[i - indices];
        }

				final var diff = shNewStart - shStart;
				for (final var modAddr : addresses) {
					final var oldValue = contents.getShort(modAddr);
					final var newValue = oldValue + diff;

					contents.putShort(modAddr, (short)newValue);
				}
			}
		}
	}

	/**
	 * TODO Document
	 */
	public LevelAddressInfo[] calcLevelPositions() {
		contents.order(ByteOrder.LITTLE_ENDIAN);
		final var bankEndPos = new int[edition.getLevelBanksLength()];
		for (var b = 0; b < bankEndPos.length; b++) {
			bankEndPos[b] = contents.getShort(
				edition.getLevelBanksAddress(b) + (edition.getLevelBanksReservedIndexStart(b) * 2)
			) - 0x4000;
		}

		final var infos = new LevelAddressInfo[29];
		final var lvlSizes = new int[29];
		final var lvlOvlaps = new int[29];

		final var bankIdx = new int[3];
		final var bankUsed = new int[3];
		final var bankLastLevel = new Level[3];

		/* find bank+index for levels */
		for (var i = 0; i < levels.size(); i++) {
			final var level = levels.get(i);

			final var levelSize = RomLevelFormat.getInstance().calcLevelSize(level);
			lvlSizes[i] = levelSize;

			for (var b = 0; b < 3; b++) {
				final var overlap = bankLastLevel[b] == null ? 0
					: RomLevelFormat.getInstance().getOverlapWith(bankLastLevel[b], level, comboInfo);

				final var startPos = Math.max(
					edition.getLevelBanksReservedIndexStart(b) + edition.getLevelBanksReservedIndexCount(b),
					bankIdx[b] + 1
				) * 2;
				final var remSize = bankEndPos[b] - startPos - bankUsed[b] + overlap;

				if (levelSize - overlap <= remSize) {
					infos[i] = new LevelAddressInfo(edition.getLevelBanksBase() + b, bankIdx[b]);
					lvlOvlaps[i] = overlap;

					bankIdx[b] += 1;
					if (bankIdx[b] == edition.getLevelBanksReservedIndexStart(b)) {
						bankIdx[b] += edition.getLevelBanksReservedIndexCount(b);
					}
					bankUsed[b] += levelSize - overlap;
					bankLastLevel[b] = level;

					break;
				}
			}
		}

		/* find actual locations for levels */
		final var bankPos = new int[3];
		for (var b = 0; b < bankPos.length; b++) {
			bankPos[b] = 0x4000
				+ Math.max(
					edition.getLevelBanksReservedIndexStart(b) + edition.getLevelBanksReservedIndexCount(b),
					bankIdx[b] + 1
				) * 2;
		}
		for (var i = 0; i < levels.size(); i++) {
			final var info = infos[i];

			final var levelSize = lvlSizes[i];
			final var overlap = lvlOvlaps[i];

			final var b = info.getBank() - edition.getLevelBanksBase();
			final var location = bankPos[b] - overlap;

			infos[i] = new LevelAddressInfo(info.getBank(), info.getIndex(), location);
			bankPos[b] += levelSize - overlap;
		}

		return infos;
	}

	/**
	 * Saves the whole ROM into a given file. This method writes into
	 * the internal contents buffer to update the ROM contents and then copies
	 * the buffer into the specified file.
	 *
	 * @param romFile The file in which the ROM is going to be saved
	 * @throws IOException If some I/O error occurs
	 */
	public void saveToFile(File romFile, SaveOptions options)
	throws IOException {
		// Expand level banks if necessary
		if (options.expandBanks) {
			expandBanks();
		}

		LevelAddressInfo[] levelAddrInfos;
		if (options.reorderLevels) {
			// Find a location for all levels
			levelAddrInfos = calcLevelPositions();

		} else {
			// Find all level information from its stored position
			levelAddrInfos =  new LevelAddressInfo[29];
			contents.order(ByteOrder.LITTLE_ENDIAN);
			contents.position(edition.getLevelPointersAddress());
			for (int i = 0; i < 29; i++) {
				final var bank = contents.get();
				final var index = contents.get();

				final var bankAddr = bank * 0x4000;
				final var ptrAddr = bankAddr + (index * 2);

				final var location = contents.getShort(ptrAddr);

				levelAddrInfos[i] = new LevelAddressInfo(bank, index, location);
			}
		}

		// Ensure levels are stored in ROM order so overlaps don't destroy data
		final var sortedLevels = new Integer[29];
		for (int i = 0; i < sortedLevels.length; i++) {
			sortedLevels[i] = i;
		}
		Arrays.sort(sortedLevels, (a, b) -> {
			final var infoA = levelAddrInfos[a];
			final var infoB = levelAddrInfos[b];

			final int cmp0 = infoA.getBank() - infoB.getBank();
			if (cmp0 != 0) {
				return cmp0;
			}

			final int cmp1 = infoA.getIndex() - infoB.getIndex();
			if (cmp1 != 0) {
				return cmp1;
			}

			return 0;
		});

		for ( final Integer lvlIdxObj : sortedLevels ) {
			final var lvlIdx = lvlIdxObj.intValue();
			final var level = levels.get(lvlIdx);

			final var info = levelAddrInfos[ lvlIdx ];

			final var bankAddr = info.getBank() * 0x4000;
			final var levelAddr = bankAddr + info.getLocation() - 0x4000;

			// Save level location
			contents.put(edition.getLevelPointersAddress() + lvlIdx * 2, (byte)info.getBank());
			contents.put(edition.getLevelPointersAddress() + lvlIdx * 2 + 1, (byte)info.getIndex());
			contents.putShort(bankAddr + info.getIndex() * 2, (short)info.getLocation());

			// Save password
			if ( level.getPassword() != null ) {
				contents.position(edition.getPasswordsAddress() + lvlIdx * 4);
				for (int j = 0; j < 4; j++) {
					contents.put((byte)(level.getPassword().charAt(j) - 'A' ));
				}
			}

			// Save enemy group
			contents.put(edition.getEnemyGroupsAddress() + lvlIdx, level.getEnemyGroup().getValue());

			// Save level data
			contents.position(levelAddr);
			RomLevelFormat.getInstance().saveRomLevelToBuffer(
				level.getRomLevel(), contents, lvlIdx == 28);

		}

		// Write it all to a file
		contents.position(0);

		try (
			final var fos = new FileOutputStream( romFile );
			final var fc = fos.getChannel();
		) {
			fc.write(contents);
		}
	}

	/**
	 * Creates a BugsRom object from the contents of a file.
	 * <p>
	 * No checks are performed on the file to ensure it is the actual Bugs Bunny
	 * Crazy Castle 2 ROM, but it will throw an IOException if something goes
	 * wrong while reading it.
	 *
	 * @param romFile File to load
	 * @return A BugsRom object representing the contents of the file.
	 * @throws IOException if something goes wrong while reading the file
	 */
	public static CrazyCastleRom loadFromFile(File romFile)
	throws IOException {
		return loadFromFile(romFile, null);
	}

	public static CrazyCastleRom loadFromFile(File romFile, CrazyCastleEdition edition)
	throws IOException {
		final var contents = ByteBuffer.allocate(128 * 1024);

		// Read the contents of the file
		try (
			final var fis = new FileInputStream( romFile );
			final var fc = fis.getChannel();
		) {
			fc.read(contents);
		}

		// Get the ROM title to locate the edition if necessary
		if (edition == null) {
			final var titleBytes = new byte[16];
			contents.get(0x134, titleBytes);

			final var rawTitle = new String(titleBytes, StandardCharsets.US_ASCII);
			final var zeroIdx = rawTitle.indexOf('\0');
			final var title = zeroIdx >= 0 ? rawTitle.substring(0, zeroIdx) : rawTitle;

			edition = CrazyCastleEdition.forTitle(title);
			if (edition == null) {
				return null;
			}
		}

		// Return a new object
		return new CrazyCastleRom( edition, contents );
	}

	/**
	 * Returns the combo info map loaded from this ROM
	 *
	 * @return The combo info map for this ROM
	 */
	public ComboInfoMap getComboInfoMap() {
		return comboInfo;
	}

	/**
	 * Returns the <i>num</i>-th level for this ROM. The level returned is the
	 * representation of the (<i>num+1</i>)-th level in the game, as levels are
	 * 0-indexed here but not in the game.
	 *
	 * @param num The level number
	 * @return The <i>num</i>-th level of this ROM
	 */
	public Level getLevel(int num) {
		if ( num < 0 || num > 28 ) {
			throw new IllegalArgumentException();
		}

		return levels.get(num);
	}

	/**
	 * Sets the <i>num</i>-th level of this ROM to a new level. The actual
	 * level this method modified is the (<i>num+1</i>)-th level in the game,
	 * as levels are 0-indexed here but not in the game.
	 * <p>
	 * This method performs no checks in the <i>level</i> argument other than
	 * <tt>null</tt>-check.
	 *
	 * @param num The level number
	 * @param level The new level
	 * @return The old level in that position
	 * @throws IllegalArgumentException If <i>num</i> is not in the range 0-28
	 * @throws NullPointerException If <i>level</i> is <tt>null</t>
	 */
	public Level setLevel(int num, Level level) {
		if ( num < 0 || num > 28 ) {
			throw new IllegalArgumentException();
		}

		if ( level == null ) {
			throw new NullPointerException();
		}

		return levels.set(num, level);
	}

	/**
	 * Returns the tileset used for the <i>num</i>-th theme of this ROM. The
	 * returned list is unmodifiable. The <i>num</i> argument is compatible
	 * with {@link Theme#getValue()}.
	 *
	 * @param num Theme number which tileset is to be retrieved
	 * @return The <i>num</i>-th tileset of the ROM
	 */
	public List<Tile> getThemeTileSet(int num) {
		if ( num < 0 || num > 6 ) {
			throw new IllegalArgumentException();
		}

		return Collections.unmodifiableList(themeTilesets.get(num));
	}

	/**
	 * Changes the <i>num</i>-th theme tileset to the one given and returns the
	 * old value as an unmodifiable list. The tileset passed will be
	 * <i>copied</i>, so no reference to it is kept by this object.
	 * <p>
	 * Note that because this method needs to check if some element of the
	 * given tileset is <tt>null</tt> and to copy the given tileset, it
	 * performs in linear time. Because the size of a tileset is always 128,
	 * it could be considered constant time, but it is still not exactly fast.
	 * <p>
	 * <b>Note: This method is currently <i>not implemented</i>. It does,
	 * however, return its correct value and throw the correct exceptions, but
	 * it will not modify the tileset in any way.
	 *
	 * @param num Theme number which tileset is to be modified
	 * @param tileset The new tileset
	 * @return The old tileset
	 */
	public List<Tile> setThemeTileSet(int num, List<Tile> tileset) {
		if ( num < 0 || num > 6 || tileset.size() != 128 ) {
			throw new IllegalArgumentException();
		}

		List<Tile> oldTileset = themeTilesets.get(num);

		Tile[] tiles = new Tile[ 128 ];
		{
			int i = 0;
			for (Iterator<Tile> it = tileset.iterator(); it.hasNext(); i++) {
				Tile tile = it.next();
				if (tile == null) {
					throw new NullPointerException();
				}
				tiles[i] = tile;
			}
		}

		// TODO uncomment the next line to "implement" this method
		// themeTilesets.set( num, Arrays.asList( tiles ) );

		return Collections.unmodifiableList(oldTileset);
	}

	/**
	 * Returns the comboset used for the <i>num</i>-th theme of this ROM. The
	 * returned list is unmodifiable. The <i>num</i> argument is
	 * compatible with {@link Theme#getValue()}.
	 *
	 * @param num Theme number which comboset is to be retrieved
	 * @return The specified comboset
	 */
	public List<Sprite> getThemeSpriteSet(int num) {
		if ( num < 0 || num >= 7 ) {
			throw new IllegalArgumentException();
		}

		return Collections.unmodifiableList(themeSpritesets.get(num));
	}

	/**
	 * Returns a list with the item tileset
	 *
	 * @return The item tileset
	 */
	public List<Tile> getItemTileSet() {
		return itemTileset;
	}

	/**
	 * Returns a list with all the item sprites
	 *
	 * @return The item spriteset
	 */
	public List<Sprite> getItemSpriteSet() {
		return itemSpriteset;
	}

	public List<Tile> getEnemyTileSet(int num) {
		if ( num < 0 || num >= 4 ) {
			throw new IllegalArgumentException();
		}
		return enemyTilesets.get(num);
	}

	public List<Sprite> getEnemySpriteSet(int num) {
		if ( num < 0 || num >= 4 ) {
			throw new IllegalArgumentException();
		}
		return enemySpritesets.get(num);
	}

	/**
	 * Returns a single sprite that represents bugs bunny in the game.
	 *
	 * @return A sprite of bugs bunny
	 */
	public Sprite getBugsSprite() {
		return bugsSprite;
	}

	public ByteBuffer getContents() {
		return contents;
	}

	public CrazyCastleEdition getEdition() {
		return edition;
	}
}
