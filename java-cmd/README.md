# Java CMD

Java 11 has a lot of new features, a cool feature included in [JEP 330](https://openjdk.java.net/jeps/330) is related to the enhancement of the java launcher to run a program supplied as a single file of Java source code.
Another interesting change is the support to execute Unix shebang based files.

Create executable scripts in Python is very simple:
```python
#!/usr/bin/env python

print("Hello World!")
```

```shell script
python Hello.py
```
But in java, for a very long time, the way of doing such a thing (like Python does) has a compilation step in the process.
```shell script
javac Hello.java
java Hello
```

Now with Java 11 with JEP 330 the execution was little simplified.  

```java
public class Hello {
    public static void main(String[] args) {
	System.out.println("Bye bye javac Hello.java!");
    }
}
```

```shell script
java Hello.java
```

Another cool feature is the support of shebang to invoke the Java launcher using source-file mode.

_main.sh_
```shell script
#!/e/opt/jdks/adopt-openjdk-11/bin/java --source 11
public class Main {
    public static void main(String[] args) {
    	System.out.println("Hello single-file program!");
    }
}
```

In other words, now our java program can be executed as a shell script. 

```shell script
./main.sh
```
