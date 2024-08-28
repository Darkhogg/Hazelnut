Hazelnut
========

Hazelnut is a level editor for the Bugs Bunny Crazy Castle 2 Gameboy ROM.


Running
-------

Hazelnutt is programmed in Java, so all you need to execute it is an
installed Java runtime Environment, which you probably already have.

Make sure you extract the contents of the ZIP to some location, as the
`lib` folder must be located within the same directory as the
`Hazelnutt.jar` file. The first time you run the program, it will
create a `log` directory and a `Hazelnutt.properties` file.

The `log` directory will be filled with logs from the program. Feel free
to remove them from time to time if they waste too much space.

The `Hazelnutt.properties` file contains some configuration options and
other things the program needs to remember.

To run the program, double click `Hazelnutt.jar`. It should launch it
directly. If that doesn't work, use the following command:

    java -jar Hazelnutt.jar

On Windows, you can use `javaw` instead of `java` to hide the dummy console
window.  If you are on Windows, you will probably need to add the directory
where `java.exe` and `javaw.exe` are to the `PATH` to use those commands.


Known Issues
------------

   + When a level is resized, `$00` is added on new positions instead of `$03`


Credits
-------

- Programmed by Daniel Escoz <https://danielescoz.dev>
- Silk Icon Set by Mark James <https://github.com/legacy-icons/famfamfam-silk> (Original link, broken: <http://www.famfamfam.com/lab/icons/silk>)
- Log4J by The Apache Software Foundation <http://logging.apache.org/log4j/1.2>
- Apache Commons by The Apache Software Foundation <http://commons.apache.org>
