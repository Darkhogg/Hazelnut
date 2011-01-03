package es.darkhogg.bbcc2;

public final class Level {
	
	private final RomLevel romLevel;
	private EnemyGroup enemyGroup;
	private String password;
	
	public Level ( RomLevel romLevel, EnemyGroup enemyGroup, String password ) {
		if ( romLevel == null ) {
			throw new NullPointerException();
		}
		
		this.romLevel = romLevel;
		setEnemyGroup( enemyGroup );
		setPassword( password );
	}
	
	public Level ( Level level ) {
		romLevel = new RomLevel( level.romLevel );
		enemyGroup = level.enemyGroup;
		password = level.password;
	}

	public RomLevel getRomLevel () {
		return romLevel;
	}
	
	public EnemyGroup getEnemyGroup () {
		return enemyGroup;
	}
	
	public void setEnemyGroup ( EnemyGroup enemyGroup ) {
		if ( enemyGroup == null ) {
			throw new NullPointerException();
		}
		this.enemyGroup = enemyGroup;
	}
	
	public String getPassword () {
		return password;
	}
	
	public String getPasswordNotNull () {
		return (password==null) ? "NULL" : password;
	}
	
	public void setPassword ( String password) {
		if ( password != null ) {
			if ( !password.matches( "^[A-Za-z]{4}$" ) ) {
				throw new IllegalArgumentException();
			}
			
			this.password = password.toUpperCase();
		} else {
			this.password = null;
		}
	}
	
}
