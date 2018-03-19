import java.util.ArrayList;

public class CreationDate{
	public String date;
	public int counter;
	ArrayList<Integer> usersSameDate =new ArrayList<Integer>();
	ArrayList<Node> usersSameDateCount =new ArrayList<Node>();
	public CreationDate(String date){
		this.date=date;
		this.counter=1;
			}
	public boolean hasUser(int user_id){
		if(usersSameDate.contains(user_id))return true;
		else return false;
	}
	public void addUser(int userId){
		if(usersSameDate.indexOf(userId)<0){
		Integer temp=new Integer(userId);
		usersSameDate.add(temp);
		}
	}
	public void addNode(int newUser,int counter){
		Node newNode=new Node(newUser,counter); //Create new Node object
		usersSameDateCount.add(newNode);
	}
//Checking number of comments
	public int checkNumOfComments(int value){
		for(Node node:usersSameDateCount){
			if(node.counter>value)return node.userId;
		}
		return -1;
	}

//Getters
	public int getSizeOfNodeList(){
		return usersSameDateCount.size();
	}
	public int getGroupSize(){
		return usersSameDate.size();
	}
	public int getIndex(int index){
		return usersSameDate.get(index);
		
	}
	public int getNodeListSize(){
		return usersSameDateCount.size();
	}
}