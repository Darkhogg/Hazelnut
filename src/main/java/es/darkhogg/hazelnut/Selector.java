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
package es.darkhogg.hazelnut;

import java.awt.Image;

public interface Selector {
	public static final class SelectionChangedEvent {
		public final SelectionType type;
		public final Object object;
		SelectionChangedEvent(SelectionType type, Object object) {
			this.type = type;
			this.object = object;
		}
	}

	public static interface SelectionChangedListener {
		public void onSelectionChanged(SelectionChangedEvent evt);
	}

	public SelectionType getSelectedType();
	public Object getSelectedObject();
	public Image getSelectionImage();

	public void addSelectionChangedListener(SelectionChangedListener listener);
	public void removeSelectionChangedListener(SelectionChangedListener listener);
}
