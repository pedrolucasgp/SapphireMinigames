
package me.sapphire.spleeg.utils;


public class MapCommand extends CommandManager {

	public MapCommand() {
		super("map");
		register(new MapCopyCommand());
		register(new MapPasteCommand());
		register(new MapPos1Command());
		register(new MapPos2Command());
		register(new MapLoadCommand());
		register(new MapSaveCommand());
		register(new MapHelpCommand());
	}

}
