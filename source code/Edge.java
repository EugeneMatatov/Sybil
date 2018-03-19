//Checking the weights between users
public class Edge {
public int friendId;
public int weight;

public Edge(int id,int weight){
	this.friendId=id;;
	this.weight=weight;
	}

//Getters and Setters
public int getFriendId(){
	return friendId;
}
public void setWeight(int value){
	weight+=value;
}
public int getWeight(){
	return weight;
}

}
