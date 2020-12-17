# folders and packages
src_folder=src/
output_folder=bin/
src_package=rs/ac/bg/etf/pp1/
util_package=$(src_package)util/
ast_package=$(src_package)ast/
test_package=test/
spec_folder=spec/
lib_folder=lib/

ast_class_path = src.rs.ac.bg.etf.pp1.ast

# build
output_dir_build = -d $(output_folder)
classpath_build = -cp ".:$(output_folder):$(lib_folder)cup_v10k.jar:$(lib_folder)log4j-1.2.17.jar:$(lib_folder)JFlex.jar:$(lib_folder)symboltable-1-1.jar"
mf = program.mj
mj_source_code_file = $(mf)
src_list_build = $(wildcard $(src_folder)$(src_package)*.java)
util_src_list_build = $(wildcard $(src_folder)$(util_package)*.java)
ast_src_list_build = $(wildcard $(src_folder)$(ast_package)*.java)

# test global
test_arg = org.junit.runner.JUnitCore

# test lexer
lex_test_package = $(test_package)lex/
src_list_test = $(wildcard $(src_folder)$(lex_test_package)*.java)
output_dir_test_lex = -d $(output_folder)
classpath_test_lex = -cp ".:$(output_folder):$(lib_folder)cup_v10k.jar:$(lib_folder)junit-4.7.jar"

all: clean_pars gen_parser build

test_lex: $(output_folder)$(lex_test_package)LexerTest.class
	java $(classpath_test_lex) $(test_arg) test.lex.LexerTest

$(output_folder)$(lex_test_package)LexerTest.class: $(src_list_test)
	javac $(output_dir_test_lex) $(classpath_test_lex) $(src_list_test)

run:
	java $(classpath_build) rs.ac.bg.etf.pp1.Compiler $(output_folder)$(mj_source_code_file)

build:
	javac $(output_dir_build) $(classpath_build) $(src_list_build) $(util_src_list_build) $(ast_src_list_build)

gen_lex:
	java -jar $(lib_folder)JFlex.jar -d $(src_folder)$(src_package) $(src_folder)$(spec_folder)mjlexer.flex

gen_parser:
	java -jar $(lib_folder)cup_v10k.jar -destdir $(src_folder)$(src_package)  -ast $(ast_class_path) -parser MJParser -buildtree $(src_folder)$(spec_folder)mjparser.cup
	./repackage.sh

cleanb:
	rm $(output_folder)$(src_package)*.class

clean_lex:
	rm $(src_folder)$(src_package)Yylex.java

clean_pars:
	rm $(src_folder)$(ast_package)*.java

clean_logs:
	rm logs/*.log

# ant build commands:
antdel:
	ant delete

antcl:
	ant clean_build

antbuild:
	ant compile

antrun:
	ant run -Darg0=$(mj_source_code_file)
