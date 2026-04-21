# CSC120-FinalProject

## Deliverables:
 - Your final codebase
 - Your revised annotated architecture diagram
 - Design justification (including a brief discussion of at least one alternative you considered)
 - A map of your game's layout (if applicable)
 - `cheatsheet.md`
 - Completed `rubric.md`
  
## Additional Reflection Questions
 - What was your **overall approach** to tackling this project?
I went modular. I first built the rooms, movement, and picking up items. Once I knew the player wouldn't get stuck in a wall, I layered on the "Man Tax" logic. This kept the project manageable and made sure the core gameplay was solid before I got into the other mechanics.

 - What **new thing(s)** did you learn / figure out in completing this project?
I finally "clicked" with Polymorphism. Being able to throw different types of customers into one ArrayList and just call calculateBill() without the code panicking is a game-changer. I also got much faster at using HashMaps for room navigation.

 - Is there anything that you wish you had **implemented differently**?
I’d rewrite the command parser. Right now, it’s a big switch statement that’s a bit clunky. If I did it again, I’d use a dedicated "Command" class to make adding new actions like "talk" or "inspect" way easier to organize.

 - If you had **unlimited time**, what additional features would you implement?
I’d turn it into a full sim—adding a complex menu (Oat milk! Hojicha! Thai Milk Tea!), a timer to stress the player out, and maybe keep increasing the price when the man asks for extra toppings.

 - What was the most helpful **piece of feedback** you received while working on your project? Who gave it to you?
I asked my friend to test out the game after and her feedback helped me identify the things that wouldn't be clear to a player.

 - If you could go back in time and give your past self some **advice** about this project, what hints would you give?
Don't underestimate the "human factor." Players type weird things. I spent way too much time fixing crashes because someone typed "take the matcha" instead of just "take matcha."

 - _If you worked with a team:_ please comment on how your **team dynamics** influenced your experience working on this project.
I worked alone. It meant more work, but it also meant I have a 100% handle on how every single class interacts.