compile:
	javac -d bin -cp src src/voxspell/**/*.java && cp src/voxspell/gui/*.fxml bin/voxspell/gui

run: package
	java -jar dist/Voxspell.jar

package: compile
	jar cfe dist/Voxspell.jar voxspell.Voxspell -C bin .

test:
	javac -d functiontest/bin -cp functiontest/src:src functiontest/src/test/Test.java
	java -cp functiontest/bin:bin test.Test

clean:
	rm -f saves/*

publish:
	COPYFILE_DISABLE=1 tar -cf release/Voxspell.tar resources/ README.txt -C dist Voxspell.jar
