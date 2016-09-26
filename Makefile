compile:
	javac -d bin -cp src src/voxspell/**/*.java && cp src/voxspell/gui/*.fxml bin/voxspell/gui

run: package
	java -jar dist/Voxspell.jar

package: compile
	jar cfe dist/Voxspell.jar voxspell.Voxspell -C bin .
