compile:
	javac -d bin -cp src src/voxspell/**/*.java && cp src/voxspell/gui/*.fxml bin/voxspell/gui

run:
	java -cp bin voxspell.Voxspell
