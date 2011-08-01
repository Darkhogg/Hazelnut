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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import es.darkhogg.util.Version;

public class CheckAndDownloadUpdatesWorker extends SwingWorker<List<File>,String> {

	private CheckUpdatesDialog dialog;
	
	public CheckAndDownloadUpdatesWorker ( CheckUpdatesDialog dialog ) {
		this.dialog = dialog;
	}

	@Override
	protected List<File> doInBackground () /*throws Exception*/ {
		Logger logger = Hazelnutt.getLogger();
		Version currentVersion = Hazelnutt.getVersion();
		List<String> urls = new ArrayList<String>();
		List<String> vers = new ArrayList<String>();
		
		// Step 1: Retrieve the version from the server
		if ( !isCancelled() ) {
			BufferedReader input = null;
			try {
				setProgress( 0 );
				publish( "Checking for updates..." );
				logger.info( "Checking for updates..." );
				
				URL versionUrl = new URL( "http://darkhogg.es/hazelnutt/update-info.txt" );
				
				URLConnection conn = versionUrl.openConnection();
				input = new BufferedReader(
					new InputStreamReader( conn.getInputStream() ) );
	
				while ( input.ready() ) {
					String line = input.readLine().trim();
					String[] pieces = line.split( "\\s+" );
					
					if ( pieces.length == 2 ) {
						vers.add( pieces[0] );
						urls.add( pieces[1] );
					}
				}
			} catch ( IOException e ) {
				logger.error( "An error has ocurred while checking last version: " + e );
				setProgress( 100 );
				publish( "Connection Error" );
				e.printStackTrace();
			} finally {
				if ( input != null ) {
					try {
						input.close();
					} catch ( IOException e ) {
						e.printStackTrace();
					}
				}
			}
		}
		
		// Step 2: Compare the two versions
		boolean updatesAvailable = false;
		List<URL> dlurls = new ArrayList<URL>();
		
		try {
			for ( int i = 0; i < urls.size(); i++ ) {
				Version ver = Version.valueOf( vers.get( i ) );
				if ( ver.compareTo( currentVersion ) > 0 ) {
					updatesAvailable = true;
					dlurls.add( new URL( urls.get( i ) ) );
				}
			}
		} catch ( MalformedURLException e ) {
			logger.error( "An error has ocurred while checking for updates" );
			setProgress( 100 );
			publish( "Unexpected Error" );
			e.printStackTrace();
			return null;
		}
		
		if ( !isCancelled() && updatesAvailable ) {
			String msg = "Downloading Hazelnutt Updates";
			setProgress( 0 );
			publish( msg );
			
			// Step 3: Download the new version
			FileOutputStream fout = null;
			InputStream uin = null;
			try {
				logger.info( msg );
				
				List<File> ret = new ArrayList<File>();
				
				for ( URL downloadUrl : dlurls ) {
					
					File dlFile = File.createTempFile( "Hn-update-", ".zip" );
					URLConnection conn = downloadUrl.openConnection();
					uin = conn.getInputStream();
					fout = new FileOutputStream( dlFile );
					
					int fSize = conn.getContentLength();
					int read = 0;
					
					byte[] bytes = new byte[ 64*1024 ];
					while ( read < fSize && !isCancelled() ) {
						int num = uin.read( bytes );
						fout.write( bytes, 0, num );
						read += num;
						
						setProgress( (int)( 100*read/fSize ) );
						publish( msg );
					}
					
					if ( !isCancelled() ) {
						setProgress( 100 );
						publish( "Download Successful!" );
						logger.info( "Successfully Downloaded at " + dlFile );
						
						SwingUtilities.invokeLater( new Runnable(){
							@Override public void run () {
								dialog.applyButton.setEnabled( true );
							}
						});
						
						ret.add( dlFile );
					}
				}
				
				return ret;
			} catch ( IOException e ) {
				logger.error( "An error has ocurred while downloading the last version: " + e );
				setProgress( 100 );
				publish( "Download Error!" );
				e.printStackTrace();
			} finally {
				try {
					if ( fout != null ) {
						fout.close();
					} 
					if ( uin != null ) {
						uin.close();
					} 
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}

		if ( isCancelled() ) {
			setProgress( 100 );
			publish( "Update Cancelled" );
			logger.info( "Update Cancelled" );
			return null;
		}
		
		// No updates available
		if ( !updatesAvailable ) {
			setProgress( 100 );
			publish( "No Updates Available" );
			logger.info( "No Updates Available" );
			return null;
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

}
