# folders and packages
src_folder=src/
output_folder=bin/
src_package=rs/ac/bg/etf/pp1/
util_package=$(src_package)util/
test_package=test/
spec_folder=spec/
lib_folder=lib/

# build
output_dir_build = -d $(output_folder)
classpath_build = -cp ".:$(output_folder):$(lib_folder)cup_v10k.jar:$(lib_folder)log4j-1.2.17.jar"
mf = program.mj
mj_source_code_file = $(mf)
src_list_build = $(wildcard $(src_folder)$(src_package)*.java)
util_src_list_build = $(wildcard $(src_folder)$(util_package)*.java)

# test global
test_arg = org.junit.runner.JUnitCore

# test lexer
lex_test_package = $(test_package)lex/
src_list_test = $(wildcard $(src_folder)$(lex_test_package)*.java)
output_dir_test_lex = -d $(output_folder)
classpath_test_lex = -cp ".:$(output_folder):$(lib_folder)cup_v10k.jar:$(lib_folder)junit-4.7.jar"

do:
	echo H$(mf)i

test_lex: $(output_folder)$(lex_test_package)LexerTest.class
	java $(classpath_test_lex) $(test_arg) test.lex.LexerTest

$(output_folder)$(lex_test_package)LexerTest.class: $(src_list_test)
	javac $(output_dir_test_lex) $(classpath_test_lex) $(src_list_test)

run:
	java $(classpath_build) rs.ac.bg.etf.pp1.MJCompiler $(output_folder)$(mj_source_code_file)

build:
	javac $(output_dir_build) $(classpath_build) $(src_list_build) $(util_src_list_build)

gen_lex:
	java -jar $(lib_folder)JFlex.jar -d $(src_folder)$(src_package) $(src_folder)$(spec_folder)mjlexer.flex

cleanb:
	rm $(output_folder)$(src_package)*.class

cleanlex:
	rm $(src_folder)$(src_package)Yylex.java 