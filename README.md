whichsource
===========

Tool to find the source given the stacktrace.

## Problem  
Many a times finding the exact source match for the given exception becomes a tough proposition. With multiple versions, we often run into scenarios where the stacktrace line number says something and the retrieved source shows something else. WhchSource is an attempt to solve this problem. We can run it in hosted or stand-alone mode where jars with source or pom files can be uploaded.

## Solution  
Actively index the source in a AST aware way, given a stack trace use the caller and callee information to zero down on the right version and serve it balzingly fast. We use in memory representations heavily and any persistence store hit is seen as an anomaly rather than the norm. The solution exposes rest service for easier integration.

## Limitations  
We are focusing only on Java code for now.

## Developer
Saurabh Rawat  -  https://github.com/eklavya

