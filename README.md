Pain-Painter
============
HISTORY:
A small pixel physics project I made back in 2014. The code is far from as clean and documented as I would have done 
it today. There are also plenty of extra features I wanted to implement such as rotating objects, infinite canvas, 
fixing a one pixel offset bug etc. However, I realised the game bottlenecked on the Java Swing graphics library which 
forced the rendering to the CPU. This doesn't scale well. I attempted an Javafx implementation, but that didn't work either.
Since then I ventured on to learn about GPU programming but ended up working on other 3D projects.

Regardless of its current state, I find this a fun simple showcase java project, that I hope show some competence in the area.

BUILD:
The project was coded in intellij with Java 8 originally.


TO RUN IT:
There is a runnable jar file under out/artifacts/Pain_Painter_jar/Pain Painter.jar. It can be downloaded and run separately.

PS: There is no guarantee that later java versions will work. Java 11 should work.



TO PLAY:
A character will fall down in the upper left corner. He can get controlled by keys (A,W,S).

On the menu bar there are a couple of buttons that I encourage you to play around with. They should be relatively intuitive. 
What you can do is paint with a black line, erase pixels and add new images through the Add Image button. 
Images can be locked in place by pressing "stick". As you'll realise, things bounces around
