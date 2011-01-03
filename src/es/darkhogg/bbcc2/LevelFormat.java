package es.darkhogg.bbcc2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * This class provides a way to load <tt>Level</tt>s from buffers or files in different
 * ways, specified by its subclasses.
 * 
 * @author Daniel Escoz (Darkhogg)
 * @version 1.0
 */
public abstract class LevelFormat {
	
	/**
	 * Reads a level from the given buffer. The level is read relative to the
	 * buffer's current position, and that position can be modified after
	 * completion.
	 * 
	 * @param buffer A buffer which contains a level
	 * @return A Level object represented by the buffer's contents
	 */
	public abstract Level loadLevelFromBuffer ( ByteBuffer buffer );
	
	/**
	 * Writes a representation of this level into the given buffer and returns
	 * the number of bytes written. The level is written relative to the current
	 * buffer position, and that position can be modified after completion.
	 * 
	 * @param buffer The buffer in which the level will be saves
	 * @param level The level to write into the buffer
	 * @return The number of bytes actually written in the buffer
	 */
	public abstract int saveLevelToBuffer ( Level level, ByteBuffer buffer );
	
	/**
	 * Reads a level from the given file.
	 * <p>
	 * This implementation loads the entire file into a ByteBuffer and passes
	 * it to <tt>loadLevelFromBuffer</tt>, with its position set to 0.
	 * 
	 * @param file The file from which the Level will be read
	 * @return A Level object represented but the file passed
	 * @throws IOException If some I/O error occurs
	 */
	public Level loadLevelFromFile ( File file )
	throws IOException {
		FileChannel fc = null;
		try {
			fc = new FileInputStream( file ).getChannel();
			if ( fc.size() > Integer.MAX_VALUE ) {
				throw new IOException( "Cannot read files bigger than 4GiB" );
			}
			ByteBuffer buffer = ByteBuffer.allocate( (int)fc.size() );
			fc.read( buffer );
			buffer.position( 0 );
			return loadLevelFromBuffer( buffer );
		} finally {
			if ( fc != null ) {
				fc.close();
			}
		}
	}
	
	/**
	 * Saves a level to a file.
	 * <p>
	 * This implementation just calls the utility method
	 * <tt>saveLevelToFile(File,Level,int)</tt> with a size of 16KiB. It
	 * is recommended to override method and call that utility method with a
	 * buffer size that is enough but not too much for the level.
	 * @param level Level to save into <tt>file</tt>
	 * @param file The file used to save the given level
	 * 
	 * @return the number of bytes written into the file
	 * @throws IOException If some I/O error occurs
	 */
	public int saveLevelToFile ( Level level, File file )
	throws IOException {
		return saveLevelToFile( level, file, 16*1024 );
	}
	
	/**
	 * Saves a level to a file using <tt>saveLevelToBuffer</tt> with a newly
	 * created buffer of <tt>size</tt> bytes. The size of the buffer is just
	 * a <i>limit</i>, if the actual number of bytes written to the buffer is
	 * less than the size of the buffer, only the written bytes will be saved
	 * into the file.
	 * <p>
	 * This method is present to simplify implementation of
	 * <tt>saveLevelToFile(File,Level</tt> giving a default, working
	 * implementation, while giving implementors the ability to specify a
	 * buffer size that satisfies their needs.
	 * @param level The level is going to be saved
	 * @param file The file the level is going to be saved to
	 * @param size The size of the intermediate buffer
	 * 
	 * @return the actual number of bytes written into the file
	 */
	protected final int saveLevelToFile ( Level level, File file, int size )
	throws IOException {
		FileChannel fc = null;
		try {
			fc = new FileOutputStream( file ).getChannel();
			ByteBuffer buffer = ByteBuffer.allocate( size );
			int num = saveLevelToBuffer( level, buffer );
			buffer.position( 0 );
			
			byte[] bytes = new byte[ num ];
			buffer.get( bytes, 0, num );
			fc.write( ByteBuffer.wrap( bytes ) );
			
			return num;
		} finally {
			if ( fc != null ) {
				fc.close();
			}
		}
	}


}
