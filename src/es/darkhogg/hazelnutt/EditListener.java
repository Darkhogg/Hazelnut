/**
 * This file is part of Hazelnutt.
 * 
 * Hazelnutt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Hazelnutt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Hazelnutt.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.darkhogg.hazelnutt;

public interface EditListener {

	public abstract void leftPressed ( int x, int y );
	public abstract void centerPressed ( int x, int y );
	public abstract void rightPressed ( int x, int y );
	
	public abstract void leftDragged ( int x, int y );
	public abstract void centerDragged ( int x, int y );
	public abstract void rightDragged ( int x, int y );
	
}
