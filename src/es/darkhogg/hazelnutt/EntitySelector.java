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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import es.darkhogg.bbcc2.EnemyType;
import es.darkhogg.bbcc2.ItemType;

@SuppressWarnings( "serial" )
public class EntitySelector extends JPanel {

	private SelectorPanel selParent;
	
	private JToggleButton btnSpawn;
	private JToggleButton btnEnemy1;
	private JToggleButton btnEnemy3;
	private JToggleButton btnEnemy2;
	private JToggleButton btnEnemy4;
	private JToggleButton btnEnemy6;
	private JToggleButton btnEnemy5;
	private JToggleButton btnBoss;
	private JToggleButton btnItemBomb;
	private JToggleButton btnItemKey;
	private JToggleButton btnItemPick;
	private JToggleButton btnItemHammer;
	private JToggleButton btnItemShield;
	private JToggleButton btnItemPotion;
	private JToggleButton btnItemLife;
	private JToggleButton btnItemClock;
	private JToggleButton btnItemBolt;
	private JToggleButton btnItemWeight;
	private JToggleButton btnItemBow;
	private JToggleButton btnItemChest;
	private JToggleButton btnDItemBow;
	private JToggleButton btnDItemBomb;
	private JToggleButton btnDItemKey;
	private JToggleButton btnDItemPick;
	private JToggleButton btnDItemHammer;
	private JToggleButton btnDItemShield;
	private JToggleButton btnDItemPotion;
	private JToggleButton btnDItemLife;
	private JToggleButton btnDItemClock;
	private JToggleButton btnDItemBolt;

	private List<Image> itemSet;
	private List<Image> doorItemSet;
	private List<Image> enemySet;
	private Image spawnImage;
	
	private final Collection<JToggleButton> buttons;
	private final Map<JToggleButton,SelectionType> buttonTypes;
	private final Map<JToggleButton,Object> buttonValues;
	private JPanel panel_3;
	
	public EntitySelector () {
		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				actionSelect( (JToggleButton) ev.getSource() );
			}
		};
		
		setLayout(new BorderLayout(0, 0));
		
		panel_3 = new JPanel();
		add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.PAGE_AXIS));
		
		JPanel panel = new JPanel();
		panel_3.add(panel);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Spawn & Enemies", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 70, 213)));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnSpawn = new JToggleButton("");
		btnSpawn.addActionListener(al);
		panel.add(btnSpawn);
		
		btnEnemy1 = new JToggleButton("");
		btnEnemy1.addActionListener(al);
		panel.add(btnEnemy1);
		
		btnEnemy2 = new JToggleButton("");
		btnEnemy2.addActionListener(al);
		panel.add(btnEnemy2);
		
		btnEnemy3 = new JToggleButton("");
		btnEnemy3.addActionListener(al);
		panel.add(btnEnemy3);

		btnEnemy4 = new JToggleButton("");
		btnEnemy4.addActionListener(al);
		panel.add(btnEnemy4);

		btnEnemy5 = new JToggleButton("");
		btnEnemy5.addActionListener(al);
		panel.add(btnEnemy5);

		btnEnemy6 = new JToggleButton("");
		btnEnemy6.addActionListener(al);
		panel.add(btnEnemy6);

		btnBoss = new JToggleButton("");
		btnBoss.addActionListener(al);
		panel.add(btnBoss);

		JPanel panel_1 = new JPanel();
		panel_3.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Items", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 70, 213)));

		btnItemBow = new JToggleButton("");
		btnItemBow.addActionListener(al);
		panel_1.add(btnItemBow);

		btnItemBomb = new JToggleButton("");
		btnItemBomb.addActionListener(al);
		panel_1.add(btnItemBomb);

		btnItemKey = new JToggleButton("");
		btnItemKey.addActionListener(al);
		panel_1.add(btnItemKey);

		btnItemPick = new JToggleButton("");
		btnItemPick.addActionListener(al);
		panel_1.add(btnItemPick);

		btnItemHammer = new JToggleButton("");
		btnItemHammer.addActionListener(al);
		panel_1.add(btnItemHammer);

		btnItemShield = new JToggleButton("");
		btnItemShield.addActionListener(al);
		panel_1.add(btnItemShield);

		btnItemPotion = new JToggleButton("");
		btnItemPotion.addActionListener(al);
		panel_1.add(btnItemPotion);

		btnItemLife = new JToggleButton("");
		btnItemLife.addActionListener(al);
		panel_1.add(btnItemLife);

		btnItemClock = new JToggleButton("");
		btnItemClock.addActionListener(al);
		panel_1.add(btnItemClock);

		btnItemBolt = new JToggleButton("");
		btnItemBolt.addActionListener(al);
		panel_1.add(btnItemBolt);

		btnItemWeight = new JToggleButton("");
		btnItemWeight.addActionListener(al);
		panel_1.add(btnItemWeight);

		btnItemChest = new JToggleButton("");
		btnItemChest.addActionListener(al);
		panel_1.add(btnItemChest);

		JPanel panel_2 = new JPanel();
		panel_3.add(panel_2);
		panel_2.setBorder(new TitledBorder(null, "Door Items", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		btnDItemBow = new JToggleButton("");
		btnDItemBow.addActionListener(al);
		panel_2.add(btnDItemBow);

		btnDItemBomb = new JToggleButton("");
		btnDItemBomb.addActionListener(al);
		panel_2.add(btnDItemBomb);

		btnDItemKey = new JToggleButton("");
		btnDItemKey.addActionListener(al);
		panel_2.add(btnDItemKey);

		btnDItemPick = new JToggleButton("");
		btnDItemPick.addActionListener(al);
		panel_2.add(btnDItemPick);

		btnDItemHammer = new JToggleButton("");
		btnDItemHammer.addActionListener(al);
		panel_2.add(btnDItemHammer);

		btnDItemShield = new JToggleButton("");
		btnDItemShield.addActionListener(al);
		panel_2.add(btnDItemShield);

		btnDItemPotion = new JToggleButton("");
		btnDItemPotion.addActionListener(al);
		panel_2.add(btnDItemPotion);

		btnDItemLife = new JToggleButton("");
		btnDItemLife.addActionListener(al);
		panel_2.add(btnDItemLife);

		btnDItemClock = new JToggleButton("");
		btnDItemClock.addActionListener(al);
		panel_2.add(btnDItemClock);

		btnDItemBolt = new JToggleButton("");
		btnDItemBolt.addActionListener(al);

		panel_2.add(btnDItemBolt);

		setEnabled( false );
		
		buttons = Collections.unmodifiableCollection( Arrays.asList( new JToggleButton[]{
			btnSpawn, btnEnemy1, btnEnemy2, btnEnemy3, btnEnemy4, btnEnemy6,
			btnEnemy5, btnBoss, btnItemBow, btnItemBomb, btnItemKey, btnItemPick, btnItemHammer,
			btnItemShield, btnItemPotion, btnItemLife, btnItemClock, btnItemBolt,
			btnItemWeight, btnItemBow, btnItemChest, btnDItemBow, btnDItemBomb,
			btnDItemKey, btnDItemPick, btnDItemHammer, btnDItemShield,
			btnDItemPotion, btnDItemLife, btnDItemClock, btnDItemBolt
		}));

		buttonTypes = Collections.unmodifiableMap( new HashMap<JToggleButton,SelectionType>(){{
			put( btnSpawn, SelectionType.SPAWN );
			put( btnEnemy1, SelectionType.ENEMY );
			put( btnEnemy3, SelectionType.ENEMY );
			put( btnEnemy2, SelectionType.ENEMY );
			put( btnEnemy4, SelectionType.ENEMY );
			put( btnEnemy6, SelectionType.ENEMY );
			put( btnEnemy5, SelectionType.ENEMY );
			put( btnBoss, SelectionType.ENEMY );

			put( btnItemBomb, SelectionType.ITEM );
			put( btnItemKey, SelectionType.ITEM );
			put( btnItemPick, SelectionType.ITEM );
			put( btnItemHammer, SelectionType.ITEM );
			put( btnItemShield, SelectionType.ITEM );
			put( btnItemPotion, SelectionType.ITEM );
			put( btnItemLife, SelectionType.ITEM );
			put( btnItemClock, SelectionType.ITEM );
			put( btnItemBolt, SelectionType.ITEM );
			put( btnItemWeight, SelectionType.ITEM );
			put( btnItemBow, SelectionType.ITEM );
			put( btnItemChest, SelectionType.ITEM );

			put( btnDItemBow, SelectionType.DOOR_ITEM );
			put( btnDItemBomb, SelectionType.DOOR_ITEM );
			put( btnDItemKey, SelectionType.DOOR_ITEM );
			put( btnDItemPick, SelectionType.DOOR_ITEM );
			put( btnDItemHammer, SelectionType.DOOR_ITEM );
			put( btnDItemShield, SelectionType.DOOR_ITEM );
			put( btnDItemPotion, SelectionType.DOOR_ITEM );
			put( btnDItemLife, SelectionType.DOOR_ITEM );
			put( btnDItemClock, SelectionType.DOOR_ITEM );
			put( btnDItemBolt, SelectionType.DOOR_ITEM );
		}});

		buttonValues = Collections.unmodifiableMap( new HashMap<JToggleButton,Object>(){{
			put( btnSpawn, null );
			put( btnEnemy1, EnemyType.FOGHORN );
			put( btnEnemy2, EnemyType.FOGHORN_DARK );
			put( btnEnemy3, EnemyType.BEAKY );
			put( btnEnemy4, EnemyType.BEAKY_DARK );
			put( btnEnemy5, EnemyType.DAFFY );
			put( btnEnemy6, EnemyType.FLAME );
			put( btnBoss, EnemyType.HAZEL );

			put( btnItemBow, ItemType.BOW );
			put( btnItemBomb, ItemType.BOMB );
			put( btnItemKey, ItemType.KEY );
			put( btnItemPick, ItemType.PICKAXE );
			put( btnItemHammer, ItemType.HAMMER );
			put( btnItemShield, ItemType.SHIELD );
			put( btnItemPotion, ItemType.POTION );
			put( btnItemLife, ItemType.LIFE );
			put( btnItemClock, ItemType.CLOCK );
			put( btnItemBolt, ItemType.BOLT );
			put( btnItemWeight, ItemType.WEIGHT );
			put( btnItemChest, ItemType.CHEST );

			put( btnDItemBow, ItemType.BOW );
			put( btnDItemBomb, ItemType.BOMB );
			put( btnDItemKey, ItemType.KEY );
			put( btnDItemPick, ItemType.PICKAXE );
			put( btnDItemHammer, ItemType.HAMMER );
			put( btnDItemShield, ItemType.SHIELD );
			put( btnDItemPotion, ItemType.POTION );
			put( btnDItemLife, ItemType.LIFE );
			put( btnDItemClock, ItemType.CLOCK );
			put( btnDItemBolt, ItemType.BOLT );
		}});
	}

	public EntitySelector ( SelectorPanel selParent ) {
		this();
		this.selParent = selParent;
	}

	public void setEnabled ( boolean enabled ) {
		super.setEnabled( enabled );

		btnSpawn.setEnabled( enabled );
		btnEnemy1.setEnabled( enabled );
		btnEnemy2.setEnabled( enabled );
		btnEnemy3.setEnabled( enabled );
		btnEnemy4.setEnabled( enabled );
		btnEnemy5.setEnabled( enabled );
		btnEnemy6.setEnabled( enabled );
		btnBoss.setEnabled( enabled );

		btnItemBow.setEnabled( enabled );
		btnItemBomb.setEnabled( enabled );
		btnItemKey.setEnabled( enabled );
		btnItemPick.setEnabled( enabled );
		btnItemHammer.setEnabled( enabled );
		btnItemShield.setEnabled( enabled );
		btnItemPotion.setEnabled( enabled );
		btnItemLife.setEnabled( enabled );
		btnItemClock.setEnabled( enabled );
		btnItemBolt.setEnabled( enabled );
		btnItemWeight.setEnabled( enabled );
		btnItemChest.setEnabled( enabled );

		btnDItemBow.setEnabled( enabled );
		btnDItemBomb.setEnabled( enabled );
		btnDItemKey.setEnabled( enabled );
		btnDItemPick.setEnabled( enabled );
		btnDItemHammer.setEnabled( enabled );
		btnDItemShield.setEnabled( enabled );
		btnDItemPotion.setEnabled( enabled );
		btnDItemLife.setEnabled( enabled );
		btnDItemClock.setEnabled( enabled );
		btnDItemBolt.setEnabled( enabled );

		updateDisplay();
	}

	protected void updateDisplay () {
		if ( isEnabled() ) {
			if ( spawnImage != null ) {
				btnSpawn.setIcon( new ImageIcon( spawnImage ) );
			}
			if ( enemySet != null ) {
				btnEnemy1.setIcon( new ImageIcon( enemySet.get( 1 ) ) );
				btnEnemy2.setIcon( new ImageIcon( enemySet.get( 2 ) ) );
				btnEnemy3.setIcon( new ImageIcon( enemySet.get( 3 ) ) );
				btnEnemy4.setIcon( new ImageIcon( enemySet.get( 4 ) ) );
				btnEnemy5.setIcon( new ImageIcon( enemySet.get( 5 ) ) );
				btnEnemy6.setIcon( new ImageIcon( enemySet.get( 6 ) ) );
				btnBoss.setIcon( new ImageIcon( enemySet.get( 7 ) ) );
			}
			if ( itemSet != null ) {
				btnItemBow.setIcon( new ImageIcon( itemSet.get( 1 ) ) );
				btnItemClock.setIcon( new ImageIcon( itemSet.get( 2 ) ) );
				btnItemBomb.setIcon( new ImageIcon( itemSet.get( 3 ) ) );
				btnItemPick.setIcon( new ImageIcon( itemSet.get( 4 ) ) );
				btnItemPotion.setIcon( new ImageIcon( itemSet.get( 5 ) ) );
				btnItemLife.setIcon( new ImageIcon( itemSet.get( 6 ) ) );
				btnItemHammer.setIcon( new ImageIcon( itemSet.get( 7 ) ) );
				btnItemChest.setIcon( new ImageIcon( itemSet.get( 8 ) ) );
				btnItemShield.setIcon( new ImageIcon( itemSet.get( 9 ) ) );
				btnItemBolt.setIcon( new ImageIcon( itemSet.get( 10 ) ) );
				btnItemKey.setIcon( new ImageIcon( itemSet.get( 11 ) ) );
				btnItemWeight.setIcon( new ImageIcon( itemSet.get( 12 ) ) );
			}
			if ( doorItemSet != null ) {
				btnDItemBow.setIcon( new ImageIcon( doorItemSet.get( 1 ) ) );
				btnDItemClock.setIcon( new ImageIcon( doorItemSet.get( 2 ) ) );
				btnDItemBomb.setIcon( new ImageIcon( doorItemSet.get( 3 ) ) );
				btnDItemPick.setIcon( new ImageIcon( doorItemSet.get( 4 ) ) );
				btnDItemPotion.setIcon( new ImageIcon( doorItemSet.get( 5 ) ) );
				btnDItemLife.setIcon( new ImageIcon( doorItemSet.get( 6 ) ) );
				btnDItemHammer.setIcon( new ImageIcon( doorItemSet.get( 7 ) ) );
				btnDItemShield.setIcon( new ImageIcon( doorItemSet.get( 9 ) ) );
				btnDItemBolt.setIcon( new ImageIcon( doorItemSet.get( 10 ) ) );
				btnDItemKey.setIcon( new ImageIcon( doorItemSet.get( 11 ) ) );
			}
		} else {
			ImageIcon qMark = new ImageIcon(EntitySelector.class.getResource("/es/darkhogg/hazelnutt/qmark_small.png"));

			btnSpawn.setIcon(qMark);
			btnEnemy1.setIcon(qMark);
			btnEnemy2.setIcon(qMark);
			btnEnemy3.setIcon(qMark);
			btnEnemy4.setIcon(qMark);
			btnEnemy5.setIcon(qMark);
			btnEnemy6.setIcon(qMark);
			btnBoss.setIcon(qMark);

			btnItemBow.setIcon(qMark);
			btnItemBomb.setIcon(qMark);
			btnItemKey.setIcon(qMark);
			btnItemPick.setIcon(qMark);
			btnItemHammer.setIcon(qMark);
			btnItemShield.setIcon(qMark);
			btnItemPotion.setIcon(qMark);
			btnItemLife.setIcon(qMark);
			btnItemClock.setIcon(qMark);
			btnItemBolt.setIcon(qMark);
			btnItemWeight.setIcon(qMark);
			btnItemChest.setIcon(qMark);

			btnDItemBow.setIcon(qMark);
			btnDItemBomb.setIcon(qMark);
			btnDItemKey.setIcon(qMark);
			btnDItemPick.setIcon(qMark);
			btnDItemHammer.setIcon(qMark);
			btnDItemShield.setIcon(qMark);
			btnDItemPotion.setIcon(qMark);
			btnDItemLife.setIcon(qMark);
			btnDItemClock.setIcon(qMark);
			btnDItemBolt.setIcon(qMark);
		}
	}

	public void setItemSet ( List<Image> itemSet ) {
		this.itemSet = itemSet;
	}

	public void setDoorItemSet ( List<Image> doorItemSet ) {
		this.doorItemSet = doorItemSet;
	}

	public void setEnemySet ( List<Image> enemySet ) {
		this.enemySet = enemySet;
	}

	public void setSpawnImage ( Image spawnImage ) {
		this.spawnImage = spawnImage;
	}
	
	protected void actionSelect ( JToggleButton but ) {
		for ( JToggleButton button : buttons ) {
			if ( but != button ) {
				button.setSelected( false );
			}
		}
		
		if ( but != null ) {
			select( buttonTypes.get( but ), buttonValues.get( but ), but );
		}
	}

	private void select ( SelectionType selType, Object value, Image img ) {
		selParent.setSelectionImage( img );
		selParent.setSelectionObject( value );
		selParent.setSelectionType( selType );
	}
	
	private void select ( SelectionType selType, Object value, JToggleButton but ) {
		select( selType, value, ((ImageIcon)but.getIcon()).getImage() );
	}
}
