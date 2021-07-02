# AIChessProject.java

#### The Chess AI agent is designed to check mate the opposition at every move possible, this was made possible by providing a heuristic evaluation function that can give penalties and bonuses to each piece to maximize the gain. Java Swing was used in my implementation which helps to create graphics in a java pane. ####

#### The following concepts were developed to make this project: Move-generation of chess and its validity, Board evaluation, Minimax algorithm, and Alpha-Beta pruning for faster response and calculation. ####

#### Minimax is a search tree algorithm which can choose the next best move. In this algorithm, the recursive tree of all possible moves is explored to a given depth, and the position is evaluated at the ending “leaves” of the tree. The effectiveness of the minimax algorithm is heavily based on the search depth, this program perform very well at 4 or 5 depth, as it is increased complexity and difficulty of AI increases. ####

Sample run with depth = 4 where white player is user, and black player is Chess AI agent: 

<img src="https://user-images.githubusercontent.com/54603828/124211605-65683600-dabb-11eb-8954-36ca3be5b1bf.jpg" width="450" height="350">
