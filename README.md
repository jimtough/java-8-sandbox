# java-8-sandbox
Playing around with various Java 8 features to help prepare for the OCPJP 8 certification exam.

I am reading the following book to prepare:

 
**Oracle Certified Professional Java SE 8 Programmer Exam 1Z0-809**
 
**A Comprehensive OCPJP 8 Certification Guide**
 
**ISBN: 978-1-4842-1835-8**
 
[www.apress.com](http://www.apress.com)


Much of my code is organized by book chapter, as reflected by the Java package structure.

I already have the OCPJP 7 certification, so I will be taking the Java 8 upgrade test. 
My code will be focused on features that are either new to Java 8, or existing features
that I need to brush up on before I take the exam.

Most of my code examples are written as JUnit test classes. I prefer to do it this way
rather than coding a bunch of classes with a main() method as the book does it.
I also use the JUnit assert family of methods to prove to myself that the API calls and
built-in classes behave as I expect. The JUnit test classes are under 'src/test/java',
which is standard for a Maven-based project. 
