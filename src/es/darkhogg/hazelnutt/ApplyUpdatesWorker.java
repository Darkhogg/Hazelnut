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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

public final class ApplyUpdatesWorker extends SwingWorker<Void,String> {

	private CheckUpdatesDialog dialog;
	private List<File> files;
	
	public ApplyUpdatesWorker ( CheckUpdatesDialog dialog, List<File> files ) {
		this.dialog = dialog;
		this.files = files;
	}
	

	@Override
	protected Void doInBackground () throws Exception {
		Logger logger = Hazelnutt.getLogger();
		
		for ( File file : files ) {
			ZipFile zipFile = new ZipFile( file );
			Enumeration<? extends ZipEntry> en = zipFile.entries();
			
			File bakDir = new File( "bak" );
			if ( !bakDir.exists() ) {
				bakDir.mkdir();
			}
	
			InputStream fis = null;
			OutputStream fos = null;
			
			setProgress( 0 );
			int n = 0;
			while ( !isCancelled() && en.hasMoreElements() ) {
				try {
					ZipEntry ze = en.nextElement();
					File oldFile = new File( ze.getName() );
					File newFile = new File( bakDir.getPath(), ze.getName() );
					
					if ( ze.isDirectory() ) {
						if ( !newFile.exists() ) {
							newFile.mkdir();
						}
					} else {
				
						// Copy old file to 'bak' directory
						if ( oldFile.exists() ) {
							logger.info( "Copying '" + oldFile + "' to '" + newFile + "'" );
							publish( "Backing up '" + oldFile + "'" );
							newFile.createNewFile();
							
							fis = new FileInputStream( oldFile );
							fos = new FileOutputStream( newFile );
		
							{
								byte[] buf = new byte[ 8*1024 ];
								int i = 0;
								while ( !isCancelled() && (i = fis.read( buf )) != -1 ) {
									fos.write( buf, 0, i );
								}
							}
						
							fis.close();
							fos.close();
						}
						
						// Copy new file
						logger.info( "Extracting '" + ze.getName() + "' to '" + oldFile + "'" );
						publish( "Extracting '" + ze.getName() + "'" );
						fis = zipFile.getInputStream( ze );
						fos = new FileOutputStream( oldFile );
						{
							byte[] buf = new byte[ 8*1024 ];
							int i = 0;
							while ( !isCancelled() && (i = fis.read( buf )) != -1 ) {
								fos.write( buf, 0, i );
							}
						}
						
						setProgress( 100*n/zipFile.size() );
						n++;
					}
					
					publish( "Finished!" );
				} catch ( IOException e ) {
					setProgress( 100 );
					publish( "Error extracting files" );
					logger.error( "Error while extracting files" );
					e.printStackTrace();
					cancel( false );
					return null;
				} finally {
					if ( fis != null ) { fis.close(); }
					if ( fos != null ) { fos.close(); }
				}
			}
		}
		
		return null;
	}
	
	@Override
	protected void process ( List<String> chunks ) {
		String info = chunks.get( chunks.size()-1 );

		dialog.statusBar.setIndeterminate( getProgress() < 0 );
		dialog.statusBar.setValue( Math.min( getProgress(), 100 ) );
		dialog.statusLabel.setText( info );
	}
	
	@Override
	protected void done () {
		if ( isCancelled() ) {
			
		} else {
			int res = JOptionPane.showConfirmDialog( dialog,
				"The update has been applied successfully, but it won't be " +
					"functional until the application is restarted\nDo you want to " +
					"restart now?",
				"Apply Update", JOptionPane.YES_NO_OPTION
			);
			
			if ( res == JOptionPane.YES_OPTION ) {
				dialog.dispose();
				dialog.frame.checkRomModified();
				Hazelnutt.restart( 1000 );
			}
		}
		
		dialog.dispose();
	}

}
