# Alpha-Architecture
A generic agent architecture for cooperative multi-agent games

We propose a generic architecture that tackles several of the issues present in cooperative negotiation games, allowing for the creation of agents capable of negotiation, trust reasoning and opponent modeling in a simple way. In order to be used in a variety of games and environments this architecture is required to be as generic as possible, making no assumptions about negotiation protocol, negotiation strategy, player goals, and search strategy. As such the proposed architecture, which we dubbed the **_Alpha architecture_**, is modular and based on the architecture of the Israeli Diplomat and DipBlue.

Like the Israeli Diplomat, it is based on the structure of a wartime nation, with four different independent modules:

- **The President:** in charge of coordinating other modules and taking the final decisions.
- **The Strategy Office:** in charge of suggesting good strategies to the President.
- **The Foreign Office:** in charge of negotiation.
- **The Intelligence Office:** in charge of predicting what opponents are likely to do.

This structure allows the architecture to have a clean separation between the different independent modules for the different subjects of negotiation, opponent modeling, the strategic and tactical evaluation of the game and the high level agent personality and overall strategy. As a result the user can easily and effortlessly swap out the modules at will and decide what capabilities the agent should have, allowing for great freedom in designing agents. By creating a relatively small quantity of these modules and combining them in different ways the developer can create a large variety of agents with different behaviors and capabilities in a short time.

## The President

The President (PR) acts as the central module for the agent, holding its personality characteristics, coordinating the other modules, defining the overall high level strategy of the agent through the definition of its goals, and being in charge of selecting and executing the moves for the player. 

In order to do this the PR keeps a knowledge base of everything it needs to know about the environment and its opponents. This knowledge base is then also used and modified by the remaining three modules, allowing the PR to make the best decisions possible with up-to-date useful information regarding the environment. The information contained in the knowledge base is the following:

-The current state of the game.
- The moves played during the course of the game by each player.
- The current player goals for the game and their importance.
- The lists of deals confirmed, completed and proposed by itself and other players over the course of the game. 
- The player's current disposition towards other players, such as who are its allies and enemies.
- The opponent models for each other player.
- The general trustworthiness levels of each player.
- The trustworthiness levels of each deal.

Additionally, the PR also has a set of personality traits that can be defined depending on the game being played. These govern the general strategy of the PR, such as how aggressive it is, how trusting of other players it is or how prone to taking risks it is. Finally, the PR also keeps lists of moves and deals suggested by the other modules, which the PR can then choose to execute depending of several factors.

When defining the PR module the developer must define what constitutes a deal, a move and a goal because these are game-specific concepts that are difficult to generalize. Different games such as Diplomacy have very different possibilities for what moves and deals a player can make compared to a game like Werewolves of Miller's Hollow. These definitions specify the game-specific elements that are stored in the knowledge base and used by all modules when making calculations.

One important role of the President is deciding what overall goals the agent is striving for and what relative importance to attribute to each goal. This information is then used to inform the behavior of other modules, such as what moves are suggested by the Strategy Office or what deals are accepted by the Foreign Office. This allows the PR to dictate the overall strategy it wants to follow to its subordinate modules, allowing them to focus on the individual details of what actions are more likely to result in attaining these goals.

Different PR modules allow the developer to customize the agent's general strategy and personality allowing for different player archetypes.

## The Strategy Office

The Strategy Office (SO) has the purpose of analyzing the strategical situation of the game and suggesting good moves to the PR. It contains most of the game-specific heuristics, evaluating the utility of possible moves and deals, and as such is highly dependent and adapted to the specific game being played. In addition to that it also defines the search strategy used to explore the possibility space for different moves.

This module is generally separated into two parts: the search strategy used when looking for moves to analyze and the evaluation method used for calculating the utility value of moves and deals. While the search strategy used can generally be applied to different environments relatively easily, the tactical evaluation is, in general, entirely dependent on the game being played, as it relies on specific knowledge about the game's rules in order to calculate what constitute good moves. 

In order to find the best moves, the SO has access to the PR's knowledge base, being able to make use of the information contained there to evaluate the utility of different moves and deals. When the PR requests move suggestions and the SO finds a good move it suggests it back to the PR who stores it for consideration on its internal list.

Swapping the Strategic Office allows a developer the choice among different search strategies and heuristics for the game, which can have a major impact on a player's effectiveness.

## The Foreign Office

The Foreign Office (FO) has the purpose of managing any interaction with other players and  negotiating deals and coalitions in a way that best allows the PR to execute the moves it is considering in order to fulfill its goals. When the PR requests the FO to negotiate with other players it sends it a list of what moves it is considering for which the FO should attempt to find supporting deals. Using this information as well as any other information available in the PR's knowledge base that the FO finds useful, this module then autonomously communicates with other players and decides what deals to propose, reject and accept. When a deal is proposed, confirmed or completed the FO informs the PR so that it can add these deals to the appropriate lists in the Knowledge Base.

The Negotiation Strategy used is defined in this module and determines what deals are proposed and accepted and what concessions the agent is willing to make. This module also defines the negotiation protocol used by the agent when communicating with other players. The decision of what protocol to use is often dependent on the game being played or even the specific development framework on top of which the agent is being implemented.

Swapping the FO allows a developer to customize the negotiation capabilities of the agent, allowing the use of different negotiation and concession strategies, or even removing the FO altogether for an agent with no negotiation capabilities.

## The Intelligence Office

The Intelligence Office (IO) is in charge of calculating the trust values and opponent models for the different players in the game. 

This module is divided into two parts: the opponent modeling function and the trust reasoning function. The opponent modeling function is what, using the information in the PR's knowledge base, outputs the predicted goals and their relative importance for each opponent, to be updated in the PR's knowledge base. How this is done is often specific to each game since the goals themselves as well as the actions and deals being analyzed are also specific to each game. The trust reasoning function is similar, outputting the trustworthiness values both for each opponent as well as for each individual deal, depending on how likely they are to be kept.

Different IOs can allow the developer to customize the opponent modeling and trust reasoning strategies, or lack thereof, of an agent. This module is especially useful in conjunction with the FO, since negotiations are the most likely to benefit from a good opponent model and accurate trust reasoning.
