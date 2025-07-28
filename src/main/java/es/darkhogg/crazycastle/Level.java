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

public final class Level {

	private final RomLevel romLevel;
	private EnemyGroup enemyGroup;
	private String password;

	public Level(RomLevel romLevel, EnemyGroup enemyGroup, String password) {
		if ( romLevel == null ) {
			throw new NullPointerException();
		}

		this.romLevel = romLevel;
		setEnemyGroup(enemyGroup);
		setPassword(password);
	}

	public Level(Level level) {
		romLevel = new RomLevel( level.romLevel );
		enemyGroup = level.enemyGroup;
		password = level.password;
	}

	public RomLevel getRomLevel() {
		return romLevel;
	}

	public EnemyGroup getEnemyGroup() {
		return enemyGroup;
	}

	public void setEnemyGroup(EnemyGroup enemyGroup) {
		if ( enemyGroup == null ) {
			throw new NullPointerException();
		}
		this.enemyGroup = enemyGroup;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordNotNull() {
		return password == null ? "" : password;
	}

	public void setPassword(String password) {
		if ( password != null ) {
			if ( !password.matches("^[A-Za-z]{4}$") ) {
				throw new IllegalArgumentException();
			}

			this.password = password.toUpperCase();
		} else {
			this.password = null;
		}
	}

}
