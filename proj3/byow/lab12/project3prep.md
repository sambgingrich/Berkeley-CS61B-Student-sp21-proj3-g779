# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: I didn't finish this section, I wasn't sure how to start. When Neil started by just drawing columns 
it seemed too simple, I felt like there has to be some really clever algorithm to make it all line up. 
I think for the project, I'll definitely use Neil's strategy of braking things down into smaller problems. 

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: The rooms and halways in project 3 are like the hexagons in this project. We'll need to make the 
rooms and hallways line up in such a way that they are connected, which is analogous to tessellating 
the hexagons in this lab. 

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: In lab, we built the hexagons first, then tessellated them. To start building worlds for the project,
we should probably try to build rooms and hallways first and make sure they have the right amount of 
openings/connections so that they can be aligned to make the whole world. 

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: The main distinction is that a hallway is just one row/column of floor tiles, whereas a room could have
multiple dimensions of floor tiles. Also, a hallway should have at least two openings, whereas a room should have
at least 1
