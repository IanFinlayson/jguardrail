jwarn
-----

This project aims to provide more warnings for potentially incorrect code than
the javac compiler does, even with warnings enabled.  The potential errors that
jwarn looks for were chosen based on years of seeing the mistakes that beginning
computer science students make when using Java.

The ones currently caught by jwarn are:
1. Dividing two integer constants that do not divide evenly, such as having
   (1/2) or (9/5) as part of an arithmetic instruction.
2. Using == to compare a String variable with a literal string value.
3. Missing break statements in a switch case.
4. Creating a method with the same name as the class -- this comes from
   accidentally putting a return type in what is meant to be a constructor.
5. Shadowing an instance variable by making a local with the same name.
6. Setting a variable to itself.  This is usually done when trying to
   set an instance variable to a constructor parameter with the same name.

We plan to make jwarn also catch the following:
7. Not initializing instance variables in their declaration nor in a
   constructor.

None of these mistakes are caught by the javac compiler.  If we tell javac to
give us more warnings by passing "-Xlint:all", then it does give a warning for
number 3, missing a break in a switch case.  Ironically that is the only of
these that I think can have a legitimate use in beginner code (such as having
a case for 'a' fall into a case for 'A' to ignore case in a menu option).



