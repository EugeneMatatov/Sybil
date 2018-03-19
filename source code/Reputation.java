import java.util.ArrayList;

public class Reputation{
	public int reputation;
	ArrayList<Integer> usersSameRep = new ArrayList<Integer>(); //ArrayList of reputation
	
	public Reputation(int reputation){
		this.reputation=reputation;
	}
	
	public boolean hasUser(int user_id){
		if(usersSameRep.contains(user_id))return true;
		else return false;
	}
	
	public void addUser(int userId){
		if(usersSameRep.indexOf(userId)<0){
		Integer temp=new Integer(userId);
		usersSameRep.add(temp);
		}
	}
	
	//Getters (get Group size and get Index)
	
	public int getGroupSize(){
		return usersSameRep.size();
	}
	
	public int getIndex(int index){
		return usersSameRep.get(index);	
	}
}