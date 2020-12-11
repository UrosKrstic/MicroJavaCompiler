#!/bin/bash

dir=src/rs/ac/bg/etf/pp1/ast/

for file in `ls $dir` 
do
    sed -i "s/package src\.rs\.ac\.bg\.etf\.pp1\.ast/package rs\.ac\.bg\.etf\.pp1\.ast/" $dir$file
done