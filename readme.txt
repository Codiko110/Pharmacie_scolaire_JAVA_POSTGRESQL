# Compiler
javac -cp "lib/postgresql-42.7.7.jar" -d out $(find src -name "*.java")

# Lancer
java -cp "lib/postgresql-42.7.7.jar:out" main.App