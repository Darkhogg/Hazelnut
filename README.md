Hazelnut
========

Hazelnut is a level editor for the Bugs Bunny Crazy Castle 2 Gameboy ROM.


Running
-------

Hazelnut is programmed in Java, so all you need to execute it is an
installed Java runtime Environment, which you probably already have.

The `log` directory will be filled with logs from the program. Feel free
to remove them from time to time if they waste too much space.

The `Hazelnut.properties` file contains some configuration options and
other things the program needs to remember.

To run the program, double click `hazelnut.jar`. It should launch it
directly. If that doesn't work, use the following command:

    java -jar hazelnut.jar

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
