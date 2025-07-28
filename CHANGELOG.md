# Changelog

## [1.1.0] - UNRELEASED
  - Editor changes:
    - Updated the Look&Feel to FlatLaf
    - Centered the level within the level editor window
    - Removed the tabbed pane on the side panel
    - Made the selected combo/entity display more information
    - Combo selector includes grid now
    - Added option to show solid combos
    - Added a lot more help icons to missing combo types
    - Added settings panel
      - With an option to ask before closing
      - With an option to create backups of saved files
      - With all of the used palettes, and ability to be changed
    - Removed the level area restrictions and the fields showing the area
    - Add new buttons in properties panel to move the level sideways
    - Removed the "Apply" and "Reset" buttons on the properties panel
      - Modifications will now be applied immediately
      - Size modifications as well as level movement via the new arrow buttons is non-destructive
    - Add warning system with corresponging button and option
      - Show warningss on misplaced door items
  - ROM changes:
    - Combo types and collision are now loaded from the ROM
    - Level positions are now loaded from the ROM
    - Tileset locations are now loaded from the ROM
    - Sprite layouts are now loaded from the ROM
    - ROM saving now (safely) modifies the level layout to allow for bigger levels
      - Data after levels in the level banks is moved to the end of the bank to make space
      - Level positions are picked in order so that levels fit in the ROM
      - Level data can sometimes overlap if deemed safe to save space
  - General changes
    - The editor will store a backup of the original file when saving to prevent data loss (if enabled)
    - The editor will now warn when loading an unsupported ROM
    - Normalized everything to `Hazelnut`
    - A lot of internal small changes to make everything a bit less clunky
    - Probably some small extra stuff I forgot...

## [1.0.11] - 2025-07-01
  - Fixed level themes' names not being updated

## [1.0.10] - 2025-06-29
  - Updated enemy groups' and level themes' names
  - Removed "Help" menu completely

## [1.0.9] - 2024-08-28
  - Added a build system
  - Fixed a bug caused by a missing icon
  - Removed the "Check for updates" menu option

## [1.0.8] - 2011-08-03
  - Who knows, I'm writing this 13 years in the future because it was missing from the changelog...

## [1.0.7] - 2010-12-30
  - Refactored almost everything and changed to fix a spelling mistake...
    Yes, it is Hazelnutt, with a single Z, I don't know what I was thinking
  - Included a 'License.txt' containing the GPL license and updated all files from the 'es.darkhogg.hazelnutt' package
    to contain a disclaimer.
  - (1.0.7_1) Changed Enemy Group and Theme names.
  - (1.0.7_2) As soon as a ROM is loaded, Level 1 is selected.
  - TO DO: Add an option to switch between two different mouse button schemes

## [1.0.6] - 2010-12-25
  - Merry Christmas!!
  - If you press 'PageUp' or 'PageDown', the editor will load the next or previous level, respectively.
  - Added a 'Recent files' menu option which remembers the last 8 successfully opened ROMs.
  - Fixed the 'Load Rom' action not asking for saving before loading a new file.

## [1.0.5] - 2010-12-24
  - Added keyboard shortcuts to almost everything on the menus
  - Added a 'Readme.txt' file both in the ZIP and inside the JAR
  - Made the 'About' dialog, now renamed to 'Readme', show the 'Readme.txt' file in a text area.

## [1.0.4] - 2010-12-23
  - Added a self-update feature in the 'Help' > 'Check Updates' menu item.

## [1.0.3] - 2010-12-22
  - Added the ability to import and export levels from/to files.
  - Fixed a bug where enemies $05 and $06 in the entity selector were swapped

## [1.0.2] - 2010-12-20
  - Fixed a bug where enemies $02 and $03 in the entity selector were swapped
  - Fixed a bug where deleted entities weren't actually removed from the ROM

## [1.0.1] - 2010-12-17
  - Added logic to 'Reload Level' and 'Save Level' on the 'File' menu
  - Added 'Save As...', which brings a dialog that lets you select a new file.
    Subsequent 'Save' actions will write to the last selected file in the 'Save As' dialog.
    If the saving fails for any reason, the file is not considered selected
  - Added 'Clear Level', which lets you delete every entity on the level and set all combos to value $03 at once.
  - Added information about the selected file and level and its modified state in the window title.
  - 'Save', 'Reload Level' and 'Save Level' are now disabled when pressing them would have no real effects
  - The editor now recalls the last opened directory
  - Added es.darkhogg.util package with Version in it, and refactored IntVector to fit in that package.

## [1.0.0] - 2010-12-16
  - First version!!
