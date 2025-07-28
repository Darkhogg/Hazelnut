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

import es.darkhogg.crazycastle.CrazyCastleRom;
import es.darkhogg.gameboy.Palette;
import es.darkhogg.gameboy.SpriteInfo;
import es.darkhogg.gameboy.Tile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;



class HexNumberFormatter extends DefaultFormatter {
    @Override
    public Integer stringToValue(String string) throws ParseException {
        final var intValue = Integer.parseInt(string, 16);
        return Integer.valueOf(intValue);
    }
    @Override
    public String valueToString(Object value) throws ParseException {
        final var intValue = (Integer)value;
        return Integer.toHexString(intValue).toUpperCase();
    }
}

class TilesetDisplay extends JComponent {
    private List<Tile> tileset;

    public List<Tile> getTileset() {
        return tileset;
    }

    public void setTileset(List<Tile> tileset) {
        this.tileset = tileset;

        final var size = new Dimension(16 * 8, ((tileset.size() + 15) / 16) * 8);
        setMinimumSize(size);
        setPreferredSize(size);

        repaint();
    }

    @Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        gr.clearRect(0, 0, getWidth(), getHeight());

		if (tileset == null) {
			return;
		}

        for (var i = 0; i < tileset.size(); i++) {
            final var x = (i % 16) * 8;
            final var y = (i / 16) * 8;

            gr.drawImage(tileset.get(i).asImage(TilesetSpriteViewerDialog.PALETTE), x, y, null);
        }
    }
}

public class TilesetSpriteViewerDialog extends JDialog {
    static Palette PALETTE = new Palette(null, Color.DARK_GRAY, Color.GRAY, Color.WHITE);

	private final CrazyCastleRom rom;

	private final JSpinner spTilesetAddr;
	private final JSpinner spTilesetLength;
	private final TilesetDisplay dispTileset;

	private final JSpinner spSpriteAddr;
	private final JSpinner spSpriteLength;
	private final JPanel panelSprites;
	private final JCheckBox checkIndirect;

    public TilesetSpriteViewerDialog(CrazyCastleRom rom) {
        this.rom = rom;

		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent ev) {
				try {
                    saveState();
                } finally {
                    dispose();
                }
			}
		});

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(SettingsDialog.class.getResource("/es/darkhogg/hazelnut/icon_tssprview.png")));
		setTitle("Hazelnut - Tileset & Sprite Viewer");

        setPreferredSize(new Dimension(500, 400));
        setMaximumSize(new Dimension(500, 400));

        spTilesetAddr = new JSpinner();
        spTilesetAddr.addChangeListener(ev -> updateDisplays());
        final var editTilesetAddr = (JSpinner.DefaultEditor)spTilesetAddr.getEditor();
        editTilesetAddr.getTextField().setFormatterFactory(new DefaultFormatterFactory(){
            @Override
            public AbstractFormatter getDefaultFormatter() {
                return new HexNumberFormatter();
            }
        });

        spTilesetLength = new JSpinner();
        spTilesetLength.addChangeListener(ev -> updateDisplays());

        spSpriteAddr = new JSpinner();
        spSpriteAddr.addChangeListener(ev -> updateDisplays());
        final var editSpriteAddr = (JSpinner.DefaultEditor)spSpriteAddr.getEditor();
        editSpriteAddr.getTextField().setFormatterFactory(new DefaultFormatterFactory(){
            @Override
            public AbstractFormatter getDefaultFormatter() {
                return new HexNumberFormatter();
            }
        });

        spSpriteLength = new JSpinner();
        spSpriteLength.addChangeListener(ev -> updateDisplays());

        dispTileset = new TilesetDisplay();

        panelSprites = new JPanel();
        panelSprites.setLayout(new FlowLayout());

        checkIndirect = new JCheckBox("Indirect");
        checkIndirect.addChangeListener(ev -> updateDisplays());

		final var contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder( 4, 4, 4, 4 ));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        setContentPane(contentPane);

        final var tilesetNorthBox = Box.createHorizontalBox();
        tilesetNorthBox.add(spTilesetAddr);
        tilesetNorthBox.add(Box.createRigidArea(new Dimension(4, 4)));
        tilesetNorthBox.add(spTilesetLength);

        final var tilesetPane = new JPanel();
		tilesetPane.setBorder(new TitledBorder(null, "Tileset", TitledBorder.LEADING, TitledBorder.TOP));
		tilesetPane.setLayout(new BorderLayout(4, 4));
        tilesetPane.add(tilesetNorthBox, BorderLayout.NORTH);
        tilesetPane.add(dispTileset, BorderLayout.CENTER);
        add(tilesetPane);

        final var spriteNorthVerBox = Box.createVerticalBox();
        final var spriteNorthHorBox = Box.createHorizontalBox();
        spriteNorthHorBox.add(spSpriteAddr);
        spriteNorthHorBox.add(Box.createRigidArea(new Dimension(4, 4)));
        spriteNorthHorBox.add(spSpriteLength);

        spriteNorthVerBox.add(spriteNorthHorBox);
        spriteNorthVerBox.add(checkIndirect);

        final var spritePane = new JPanel();
		spritePane.setBorder(new TitledBorder(null, "Sprites", TitledBorder.LEADING, TitledBorder.TOP));
		spritePane.setLayout(new BorderLayout(4, 4));
        spritePane.add(spriteNorthVerBox, BorderLayout.NORTH);
        spritePane.add(panelSprites, BorderLayout.CENTER);
        add(spritePane);

        loadState();

        pack();
    }

    private void loadState() {
        final var config = Hazelnut.getConfiguration();
        spTilesetAddr.setValue(config.getInt("Hazelnut.tsv.tilesetAddr", 0xC00E));
        spTilesetLength.setValue(config.getInt("Hazelnut.tsv.tilesetLength", 128));
        spSpriteAddr.setValue(config.getInt("Hazelnut.tsv.spriteAddr", 0x33C3));
        spSpriteLength.setValue(config.getInt("Hazelnut.tsv.spriteLength", 8));
        checkIndirect.setSelected(config.getBoolean("Hazelnut.tsv.spriteIndirect", false));
    }

    private void saveState() {
        final var config = Hazelnut.getConfiguration();
        config.setProperty("Hazelnut.tsv.tilesetAddr", spTilesetAddr.getValue());
        config.setProperty("Hazelnut.tsv.tilesetLength", spTilesetLength.getValue());
        config.setProperty("Hazelnut.tsv.spriteAddr", spSpriteAddr.getValue());
        config.setProperty("Hazelnut.tsv.spriteLength", spSpriteLength.getValue());
        config.setProperty("Hazelnut.tsv.spriteIndirect", checkIndirect.isSelected());
    }

    private void updateDisplays() {
        final var log = Hazelnut.getLogger();

        try {
            log.trace(
                "Update TSV display: "
                + Integer.toHexString((Integer)spTilesetAddr.getValue())
                + " +" + spTilesetLength.getValue() + " | "
                + Integer.toHexString((Integer)spSpriteAddr.getValue())
                + " +" + spSpriteLength.getValue()
                + (checkIndirect.isSelected() ? " INDIR" : " DIR")
            );
            final var tileset = updateTileset();
            updateSprites(tileset);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            validate();
        }
    }

    private List<Tile> updateTileset() {
        final var buffer = rom.getContents();

        final var tilesetAddr = ((Integer)spTilesetAddr.getValue()).intValue();
        final var tilesetSize = ((Integer)spTilesetLength.getValue()).intValue();

        final var tileset = new ArrayList<Tile>();
        buffer.position(tilesetAddr);
        for (var i = 0; i < tilesetSize; i++) {
            final var tile = Tile.fromBuffer(buffer);
            tileset.add(tile);
        }

        dispTileset.setTileset(tileset);

        return tileset;
    }

    private void updateSprites(List<Tile> tileset) {
        final var log = Hazelnut.getLogger();
        final var buffer = rom.getContents();

        final var spriteAddr = ((Integer)spSpriteAddr.getValue()).intValue();
        final var spriteLength = ((Integer)spSpriteLength.getValue()).intValue();
        final var indirect = checkIndirect.isSelected();

        final var spriteInfos = new ArrayList<SpriteInfo>(spriteLength);

        panelSprites.removeAll();
        buffer.position(spriteAddr);
        if (indirect) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            for (var i = 0; i < spriteLength; i++) {
                final var addr = buffer.getShort();
                final var pos = buffer.position();

                buffer.position(addr);
                spriteInfos.add(SpriteInfo.fromSpriteBuffer(buffer));

                buffer.position(pos);
            }
        } else {
            for (var i = 0; i < spriteLength; i++) {
                spriteInfos.add(SpriteInfo.fromSpriteBuffer(buffer));
            }
        }

        for (var i = 0; i < spriteInfos.size(); i++) {
            final var spriteInfo = spriteInfos.get(i);

            if (spriteInfo == null || spriteInfo.getWidth() <= 0 || spriteInfo.getHeight() <= 0) {
                log.warn("sprite at " + i + " was invalid (" + spriteInfo + ")");
                break;
            }

            final var sprite = spriteInfo.generateSpriteFromTileSet(tileset);
            final var label = new JLabel(new ImageIcon(sprite.asImage(PALETTE)));
            label.setToolTipText(i + " ($" + Integer.toHexString(i) + ")");
            panelSprites.add(label);
        }
    }
}
