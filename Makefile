run: package
	java -jar dist/Voxspell.jar

compile:
	javac -d bin -cp src src/voxspell/**/*.java
	cp src/voxspell/gui/*.fxml bin/voxspell/gui/
	cp -r src/voxspell/properties bin/voxspell/
	cp -r src/voxspell/gui/styles bin/voxspell/gui/

package: compile
	jar cfe dist/Voxspell.jar voxspell.Voxspell -C bin .

test:
	javac -d functiontest/bin -cp functiontest/src:src functiontest/src/test/Test.java
	java -cp functiontest/bin:bin test.Test

clean:
	rm -f saves/*
	rm -r bin/*

publish:
	COPYFILE_DISABLE=1 tar --exclude=".DS_Store" -cf release/Voxspell-${version}.tar khir664_Voxspell_Manual.pdf khir664_Report.pdf resources/ src/ README.txt -C dist Voxspell.jar
