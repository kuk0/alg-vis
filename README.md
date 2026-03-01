![Gnarley trees](https://kubokovac.eu/img/gnarley.png)
----------------------------------

Gnarley trees is a project focused on visualization of various tree data structures. It contains dozens of data structures, from balanced trees and priority queues to union find and stringology.
See the [web page](https://kubokovac.eu/gnarley-trees/).

Building
--------

The project uses the [Gradle](https://gradle.org/) build system. Use the included wrapper — no local Gradle installation required.

```sh
# Compile and run all checks
./gradlew build

# Build a standalone JAR (output: build/libs/alg-vis.jar)
./gradlew jar

# Run the application directly
./gradlew run
```

Development
-----------
* in eclipse, go to preferences, java, code style, formatter, and use the algvis-format.xml
* in intellij idea, install the external eclipse formatter plugin and again, use the algvis-format
* To contribute translations: copy `src/Messages_en.properties` to `src/Messages_xx.properties` (replace `xx` with the language tag), translate the values, and open a pull request.