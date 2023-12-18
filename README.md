# ArcelorMittal : CoilVision 

![Arcelor_Mittal](CoilVision_Arcelor_Mittal/Screenshot_CoilVision.png)

## **1. About this project**

Everything is explained in our different files within the *Reports* folder. However everything is written in French. To simplify and summarize our project, feel free to read this README!

Here are the details of each file: 

  - **'Specifications'** is simply the **introduction to the subject**. It explains where such a project comes from and what it will be used for
  - **'DeisgnFile'** allows external people to **understand the application** that has been developed, its components and all the relationships
  - **'UserGuide'** is simply a **notice** that allows the user to **take over and use the application**.

This project was done in a school context with three other classmates.

## **2. The context**

ArcelorMittal is a **leader among the world's steel groups**, more particularly in production, notably by being the world's leading steel producer in 2018 with nearly *100 million tons* produced. In order to **optimize production** and **improve product quality**, ArcelorMittal proposes the FIRST project in partnership with IMT Mines Alès.

Summarizing the production steps, the process ends with a rolling step during which the input metal blocks are transformed into metal coils after being heated, resized in width and thickness, then cooled to obtain the quality and mechanical properties and finally wound to obtain the desired structure.

<div id="process" align = "center">
  <img src="https://user-images.githubusercontent.com/105392989/176406142-c3d0fce8-721c-4c57-b55e-0988d548e073.png" width="900">
</div>

During this process, the metal may **degrade and show imperfections** on the surface. Once the imperfections appear, it is not possible to **use and sell** the material, so it is discarded or considered defective.   
In order to limit these degradations, it is **necessary to lubricate correctly and at the right time** the system in order to avoid damaging the metal plates to ensure quality production and material, and thus improve yield.

The **CoilVision project** was created to meet this main need.   
The objective is to **collect a set of data** *(every 200ms)* from sensors and **store them in a database** in order to **calculate** with the help of mathematical models proposed by ArcelorMittal, output values corresponding to **different magnitudes** essential to the optimization of production **including the coefficient of friction** to adjust the lubrication.   
To make the data understandable and usable for the users *(technicians, administrator)*, it is also necessary to **display the evolution in real time** 
of the output variables, in particular the coefficient of friction, and **store them once again in a database**.
