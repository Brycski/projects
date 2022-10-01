#Gitlet

**Overview**

Recreation of "git", the file-tracking software that Github operates through. This project was done to understand Git
(and general persistence in programs) on a deeper level by building all of its basic features.

**Features**

- java gitlet.Main init = Creates a new repository in project file directory.
- java gitlet.Main add [file name] = Stages a file in project directory for addition.
- java gitlet.Main commit [message] = Creates a commit of tracked files (staged for addition/deletion).
- java gitlet.Main rm [file name] = Un-stages file in project directory or stages it for removal.
- java gitlet.Main log = Displays a log of all commits on a branch and information.
- java gitlet.Main global-log = Displays a log of all commits and information.
- java gitlet.Main find [commit message] = Prints commit id's that match given commit message.
- java gitlet.Main status = Prints branches, staged files, and removed files.
- java gitlet.Main checkout
    - -- [file name] = Overwrites given file with the version of it from most recent commit.
    - [commit id] -- [file name] = Overwrites given file with the version of it from given commit.
    - [branch name] = Overwrites all current files with the version of them from given branch.
- java gitlet.Main branch [branch name] = Creates a new branch.
- java gitlet.Main rm-branch [branch name] = Removes given branch.
- java gitlet.Main reset [commit id] = Modifies current files to reflect those from given commit.

**Running the Project**

Project currently only planned to through IntelliJ IDEA. Compile project with javac gitlet/*.java and then type any
command. **Must install the following project libraries: xchart-3.8.1.jar, ucb.jar, junit-4.13.2.jar, jh61b-junit.jar,
hamcrest-core-1.3.jar, and algs4.jar.**

**Credit**

Credit to CS61BL and Josh Hug for GitletTests.java and method instructions.





