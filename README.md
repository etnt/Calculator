# Android Calculator App
> Written in Kotlin using the Jetpack Compose

As picked up from the great YT tutorial: [How to Build a Calculator with Jetpack Compose](https://youtu.be/-aTcFJWxEQA?si=lAG7yqoqULSga9c5)

I followed the tutorial which created the basic layout of the calculator;
then I replaced the logic with my own that I created in collaboration
with the Github Co-Pilot (a fantastic addition to the Software writing toolbox!). 

Basically what I did:

* proper handling of the arithmetic expressions honoring operator precedence
* added a Shift mechanism to provide extended functionality (e.g square-root)

A Problem that remain is that the layout breaks down when the screen is rotated.
I have tried introducing a `ViewModel` in the `add-viewmodel` branch but get
the same break down of the layout. I need to read up on how to handle this.

All in all, a fun hack. The Android Studio has an impressive functionality
although I sometimes used VSCode just to have access to the Github Co-Pilot;
I would not have been able to get this far in this short amount of time
without it.

## Screen shots

<img src="screenshot1.png" alt="Screen1" width="200"/>
<img src="screenshot2.png" alt="Screen2" width="200"/>
<img src="screenshot3.png" alt="Screen3" width="300"/>

