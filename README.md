Software engineering: Architecture and design

In Sprint 3, each team reviewed and evaluated each member's submission of Sprint 2, made decision on
which parts from which submission was be taken as the basis for Sprint 3, worked out a “combined”
solution direction, and implemented a fully working solution of the Fiery Dragon’s game (without any
extensions). In Sprint 4, extensions to the Fiery Dragon’s game are now to be implemented.
The extensions are divided into two categories: (i) required and (ii) self-defined. In the required category,
the teaching team has defined two extensions that, depending on the number of students in the team,
need to be added to your Sprint 3 design and executable. In the self-defined category, guidelines on
what kind of extensions can be implemented, but it is up to each team how to interpret the guidelines,
identify and implement their own extensions. The two categories of extensions are defined below.
Required Extensions

● Introduce one of two additional, distinct Dragon cards (or “chit” cards) - animal/visualisation of your
choice - were the action to be taken when one of these two cards is flipped over is as follows:
○ New Dragon Card 1: the token of this player needs to go backwards on the board until it finds
a free cave (that is, a cave without a token - this can be any cave and does not need to be
the cave this token started from) and position itself in this cave. The player does not lose
their turn and hence can continue with flipping the next Dragon card. If the player’s token is
in a cave, the token does not move and the player’s turn continues.
○ New Dragon Card 2: the token of this player swaps position with the token that is closest to
its position (can be either backwards or forwards on the Volcano), but the player loses their
turn (hence it will be the next player’s turn). A token that is in a cave cannot be swapped with
(even if it is the closest token - consider such token as having infinite distance). A token that
is close to its cave after having almost gone around the Volcano may have to go around the
Volcano again if it is swapped “past” its cave.
If, for visualisation purposes, you prefer to have an even number of Dragon cards, please
feel free to add two of the new Dragon card you have implemented.
● Loading and saving the game from/to an external (configuration) file in a suitable text (not binary)
format. The specific format is up to you to define (we will kick-start discussions around file formats
on the forum; we strongly recommend not to use an XML format), but must allow the following:        
○ The format must allow for the entire state of a game to be stored and loaded:
■ all Volcano cards (including the animals on their tiles and their sequence),
Note: for Sprint 4, we require Volcano cards to be part of the design/implementation.

■ the sequence of Volcano cards that form the circular Volcano,
■ the location of the caves and the animal of each of the caves,
■ the location of each player’s token (there may be less than 4 tokens if less than 4
player play the game),
■ the location of each of the Dragon (“chit”) cards
■ the order of the players as well as which player’s turn it is.
○ The format you are choosing must support Volcano cards with a different number than 3
animal tiles. The format must also allow inserting an indentation for a Cave at a different
location than the middle of the 3 Tiles. However, there can only ever be 1 Cave per Volcano
card.
○ The format must support a different number than 8 Volcano cards.
○ You can assume that there are still 4 players and hence 4 caves, but the 4 caves may not be
as evenly distributed across the board as in the base game (e.g., you could have 4 Volcano
cards, one of the other, each with a cave attached to them).
○ The format you choose must support any extensions you implement in this Sprint.
Saving a game, quitting the game, restarting the game, and loading the previously saved game
should result in the same game situation as if the game was never quit in the first place.
If loading of a game is implemented correctly, you can use this functionality to create different
board configurations and load them at the start (instead of creating a new board configuration
programmatically).
Hint: we recommend to delegate the responsibility of saving to and loading from a file to the
relevant classes. For example, a Volcano Card should know how to load itself from a file and also
save itself to a file.
Self-Defined Extensions
We encourage teams to show some creativity and come up with their own extensions. One of these
extensions must explicitly address a Human Value (as discussed in the Week 10 workshop) - up to you
to choose which Human Value you want to include, how to interpret it in the context of the Fiery Dragon
game, and what specific functionality to add (it cannot be functionality that already exists). Any other
self-defined extensions must, in some specific form, alter the game play.
Examples include (but these are only examples to stimulate creativity) - you may not implement any of
these examples:
● adding a “memory score” that reflects how well a player has memorised the Dragon cards and
how often they have flipped the wrong Dragon card if they should have known a ‘better’ card to
be flipped over. This addresses the Achievement Human Value.
● Shuffling of the Dragon cards instead of making a turn. You may want to restrict this to once per
game per player.
● If a player’s token were to move to a Tile with another token, instead of being blocked, the other
token is sent back to its “home” cave and the player’s token moves to the now freed Tile.
● Two players play against each other with 2 tokens each. A player can choose which of their two
tokens to move depending on which Dragon card they have flipped over.
We hope that you will have many great ideas on how to extend the game.

Number of Extensions to implement
● Teams of 4 students must implement the two required extensions, plus two self-defined
extensions of their choice, one of which must address a Human Value.
● Teams of 3 students must implement the two required extensions, plus one self-defined extension
that explicitly addresses a Human Value.
● Teams of 2 students must implement one of the two required extensions, plus one self-defined
extension of their choice that explicitly addresses a Human Value.
Further details of each of these tasks is given below.
Sprint 4 Deliverables (20% of final unit mark)
All tasks are mandatory.
1. Object-Oriented Design
● Revised design: an updated class diagram that covers all the extensions you have implemented,
including class names, attributes, methods, relationship between classes, and cardinalities. If in the
process of working on the extensions you fixed any defects that were in Sprint 3, please update the
class diagram as well. You must highlight what has changed (e.g. using colour or
annotations, before vs after) compared to your Sprint 3 class diagram.
Note: it is perfectly fine to create a class diagram by hand (on paper) and include a scanned
version thereof in your submission. You do not need to create the class diagram using an
electronic tool if you prefer to draw it on paper.
Note: although we do not require the submission of any sequence diagrams (or similar), it is
strongly recommended that you reflect on interaction patterns in your design before jumping into
any coding. If you start with the latter, it may actually take you longer to get the code working.
2. Tech-based Software Prototype
● Fully functional/playable implementation of the Fiery Dragon's game according to the basic
requirement as well as all extensions required for your team.
● Create an executable of the Fiery Dragon’s game together with clear instructions on how to build
and run it on the target platform (as per previous sprints).
● If we cannot run the executable, the tech-based final prototype will be assessed as a fail (below
‘poor’).
3. Reflection on Sprint 3 Design
● A reflection on how easy/difficult/hard it was to incorporate the extensions you have implemented
to the design and implementation from Sprint 3.
● Please discuss the level of difficulty for each of the incorporated extensions separately and briefly
outline what aspects of the Sprint 3 design/implementation (for example, distribution of system
intelligence; presence of code smells; use of appropriate Design Patterns) made a particular
extension easy/difficult to address. If you were to go back to Sprint 3 and could start all over again,
what would you change so that the extension under consideration would have been easier to add?
Note: this is one of the key learning aspects of Sprint 4 - reflecting on the constraints introduced by
your Sprint 3 design/implementation and what you would do differently in the future if you were in a
similar situation.

4. Video Demonstrations
● Prepare and submit a video that demonstrates your game implementation (as per the basic
requirements as well as your extensions) of maximum 6 minutes for teams of three (e.g. roughly
2 minutes per team member) or maximum 8 minutes for teams of four. Teams of two students
should aim for approx. 4 - 5 minutes.
● You are required to submit the demonstration video through Moodle as per the submission
instructions given towards the end of this document.
● Focus on the functionality you have added in this sprint. Give a brief summary what the
extensions are that you have made and demonstrate how they work.
● The demonstration of the loading/saving functionality (where applicable) must include the
contents of a file that was created as the outcome of saving a game’s state.
● You must demonstrate in the video the technology stack (operating system, IDE etc.) you used
for development and testing of your implementation.
● You must demonstrate in the video that your implementation compiles, runs, and correctly follows
the rules of the Fiery Dragon’s game.
● When recording the demonstration, each team member will need to contribute/participate during
the demonstration. Each team member will also need to verbally explain at least one key part of
the implementation and have their computer camera enabled while doing so.
General Guidance
● Your implementation will still need to fully meet the basic requirements, where all standard
rules of the Fiery Dragon’s game have been implemented, unless one of your extensions
explicitly breaks one of the basic game rules.
● To boost your chances of getting the best mark you can, ensure that frequent and multiple Git
commits are made throughout the Sprint by all team members, and that all submission
instructions are followed. All artefacts that are part of your Sprint 4 submission (unless otherwise
stated) must be available in the repository.
● In this assessment, you can use generative artificial intelligence (AI) in order to consider design
alternatives/options and explore their advantages and disadvantages. You can also use
generative AI to create art work if you wish to do so. You may even use Generative AI to
generate ideas for extensions. Your final design decisions should be your own (and justified if it
matches AI recommendation) along with your rationale description.
● Any use of generative AI must be appropriately acknowledged (see Learn HQ).
● The remainder of the description is the same as with previous sprints, except see new rubric
below.
The precise submission instructions are given towards the end of this document - please carefully
read them to ensure your submission is in line with expectations.
